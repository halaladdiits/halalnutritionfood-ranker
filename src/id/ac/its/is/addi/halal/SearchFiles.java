package id.ac.its.is.addi.halal;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import com.google.gson.Gson;
import id.ac.its.is.addi.halal.model.Result;
import id.ac.its.is.addi.halal.ranking.DependentRanking;
import id.ac.its.is.addi.halal.ranking.FinalScore;
import id.ac.its.is.addi.halal.ranking.IndependentRanking;
import id.ac.its.is.addi.halal.utils.Settings;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

/** Simple command-line based search demo. */
public class SearchFiles {

    private SearchFiles() {}

    /** Simple command-line based search demo. */
    public static void main(String[] args) throws Exception {
        String usage =
                "Usage:\tjava org.apache.lucene.demo.SearchFiles [-index dir] [-field f] [-repeat n] [-queries file] [-query string] [-raw] [-paging hitsPerPage]\n\nSee http://lucene.apache.org/core/4_1_0/demo/ for details.";
        if (args.length > 0 && ("-h".equals(args[0]) || "-help".equals(args[0]))) {
            System.out.println(usage);
            System.exit(0);
        }

        String index = "index";
        String field = "contents";
        String queries = null;
        int repeat = 0;
        boolean raw = false;
        String queryString = null;
        int hitsPerPage = 10;

        for(int i = 0;i < args.length;i++) {
            if ("-index".equals(args[i])) {
                index = args[i+1];
                i++;
            } else if ("-field".equals(args[i])) {
                field = args[i+1];
                i++;
            } else if ("-queries".equals(args[i])) {
                queries = args[i+1];
                i++;
            } else if ("-query".equals(args[i])) {
                queryString = args[i+1];
                i++;
            } else if ("-repeat".equals(args[i])) {
                repeat = Integer.parseInt(args[i+1]);
                i++;
            } else if ("-raw".equals(args[i])) {
                raw = true;
            } else if ("-paging".equals(args[i])) {
                hitsPerPage = Integer.parseInt(args[i+1]);
                if (hitsPerPage <= 0) {
                    System.err.println("There must be at least 1 hit per page.");
                    System.exit(1);
                }
                i++;
            }
        }

        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));

        /*START MENGETAHUI TERMS PALING SERING MUNCUL*/
/*
        Comparator<TermStats> comparator = new HighFreqTerms.DocFreqComparator();
        HighFreqTerms freqTerms = new HighFreqTerms();
        TermStats[] termsLabel = freqTerms.getHighFreqTerms(reader, 5, "label", comparator);
        TermStats[] termsManufacture = freqTerms.getHighFreqTerms(reader, 5, "manufacture", comparator);
        TermStats[] termsContainsIngredients = freqTerms.getHighFreqTerms(reader, 5, "containsIngredient", comparator);

        for(int i = 0; i < termsLabel.length; ++i) {
            System.out.printf(Locale.ROOT, "%s:%s \t totalTF = %,d \t docFreq = %,d \n", termsLabel[i].field, termsLabel[i].termtext.utf8ToString(), termsLabel[i].totalTermFreq, termsLabel[i].docFreq);
        }

        for(int i = 0; i < termsManufacture.length; ++i) {
            System.out.printf(Locale.ROOT, "%s:%s \t totalTF = %,d \t docFreq = %,d \n", termsManufacture[i].field,
                    termsManufacture[i].termtext.utf8ToString(),
                    termsManufacture[i].totalTermFreq,
                    termsManufacture[i].docFreq);
        }


        for(int i = 0; i < termsContainsIngredients.length; ++i) {
            System.out.printf(Locale.ROOT, "%s:%s \t totalTF = %,d \t docFreq = %,d \n", termsContainsIngredients[i].field,
                    termsContainsIngredients[i].termtext.utf8ToString(), termsContainsIngredients[i].totalTermFreq, termsContainsIngredients[i].docFreq);
        }
*/

        /*END MENGETAHUI TERMS PALING SERING MUNCUL*/

        IndexSearcher searcher = new IndexSearcher(reader);
        searcher.setSimilarity(new TFIEFSimilarity());
        Analyzer analyzer = new StandardAnalyzer();

        BufferedReader in = null;
        if (queries != null) {
            in = Files.newBufferedReader(Paths.get(queries), StandardCharsets.UTF_8);
        } else {
            in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        }

        MultiFieldQueryParser parser = new MultiFieldQueryParser(
                new String[] {Settings.FIELD1_LABEL, Settings.FIELD2_CONTAINSINGREDIENT},
                analyzer);

        //QueryParser parser = new QueryParser(field, analyzer);
        while (true) {
            if (queries == null && queryString == null) {                        // prompt the user
                System.out.println("Enter query: ");
            }

            String line = queryString != null ? queryString : in.readLine();

            if (line == null || line.length() == -1) {
                break;
            }

            line = line.trim();
            if (line.length() == 0) {
                break;
            }

            Query query = parser.parse(line);
            System.out.println("Searching for: " + query.toString(field));

            if (repeat > 0) {                           // repeat & time as benchmark
                Date start = new Date();
                for (int i = 0; i < repeat; i++) {
                    searcher.search(query, 100);
                }
                Date end = new Date();
                System.out.println("Time: "+(end.getTime()-start.getTime())+"ms");
            }

            doPagingSearch(in, searcher, query, hitsPerPage, raw, queries == null && queryString == null);

            if (queryString != null) {
                break;
            }
        }
        reader.close();
    }

    /**
     * This demonstrates a typical paging search scenario, where the search engine presents
     * pages of size n to the user. The user can then go to the next page if interested in
     * the next hits.
     *
     * When the query is executed for the first time, then only enough results are collected
     * to fill 5 result pages. If the user wants to page beyond this limit, then the query
     * is executed another time and all hits are collected.
     *
     */
    public static void doPagingSearch(BufferedReader in, IndexSearcher searcher, Query query,
                                      int hitsPerPage, boolean raw, boolean interactive) throws IOException {

        // Collect enough docs to show 5 pages
        TopDocs results = searcher.search(query, 5 * hitsPerPage);
        ScoreDoc[] hits = results.scoreDocs;

        int numTotalHits = Math.toIntExact(results.totalHits);
        System.out.println(numTotalHits + " total matching documents");

        int start = 0;
        int end = Math.min(numTotalHits, hitsPerPage);

        /*System.out.println("start = +"+start +" --------- end = "+end+"\nQuery = "+query.toString()+"\n=================================" +
                "\nQuery field label = "+query.toString("label")+"\nQuery field containsIngredient = "+query.toString("containsIngredient"));
*/


        String[] uniqueTerms = getUniqueTerms(query.toString());

        while (true) {
            if (end > hits.length) {
                System.out.println("Only results 1 - " + hits.length +" of " + numTotalHits + " total matching documents collected.");
                System.out.println("Collect more (y/n) ?");
                String line = in.readLine();
                if (line.length() == 0 || line.charAt(0) == 'n') {
                    break;
                }

                hits = searcher.search(query, numTotalHits).scoreDocs;
            }

            end = Math.min(hits.length, start + hitsPerPage);
            List<Result> resultList = new ArrayList<>();


            for (int i = start; i < end; i++) {
                if (raw) {                              // output raw format
                    System.out.println("doc="+hits[i].doc+" score="+hits[i].score);
                    continue;
                }

                Document doc = searcher.doc(hits[i].doc);
                //String judul[] = doc.getValues("foodproductId");
                List<IndexableField> fields = doc.getFields();

                DependentRanking dependentRanking = new DependentRanking();
                IndependentRanking independentRanking = new IndependentRanking();
                FinalScore finalScoreRanking = new FinalScore();

                Result result = new Result();
                result.setLabel(doc.get("subject"));


                String entityType = "";
                Integer entityIdforDb = 0;

                List<Map<String,String>> atributeList = new ArrayList<Map<String, String>>();
                for (int j = 0; j < fields.size(); j++) {

                    //tambah data atribute ke list
                    Map<String, String> atribute = new HashMap<String, String>();
                    if (!fields.get(j).name().equalsIgnoreCase(Settings.FIELD0_SUBJECT)) {
                        atribute.put(fields.get(j).name(), fields.get(j).stringValue());
                        atributeList.add(atribute);
                    }

                    //get id entity berdasarkan data pada database
                    if (fields.get(j).name().equalsIgnoreCase("foodproductId") || fields.get(j).name().equalsIgnoreCase("rank")) {
                        //System.out.println("hmmmmmmmm == "+fields.get(j).name());
                        entityIdforDb = Integer.parseInt(fields.get(j).stringValue());
                    }

                    if (fields.get(j).name().equalsIgnoreCase("type")) {
                        if (fields.get(j).stringValue().equalsIgnoreCase("FoodProduct")) {
                            entityType = "FoodProduct";
                        } else if (fields.get(j).stringValue().equalsIgnoreCase("FoodAdditive")) {
                            entityType = "FoodAdditive";
                        } else {
                            entityType = "Not Found";
                        }
                    }

                }

                Double queryScore = hits[i].score * dependentRanking.getSpreadValue(uniqueTerms, fields);
                System.out.println("queryscore = "+queryScore);
                System.out.println("entityType = "+entityType+ " ========= "+"id = "+entityIdforDb);
                Double staticScore = independentRanking.getIndependentRanking(entityType, entityIdforDb);
                System.out.println("staticScore = "+staticScore);
                Double finalScore = finalScoreRanking.getFinalScore(staticScore, queryScore);
                System.out.println("Final score = "+finalScore);
                result.setScore(finalScore);
                result.setAtributes(atributeList);
                resultList.add(result);

                resultList.sort(Comparator.comparingDouble(Result::getScore)
                        .reversed());

                /*for (int j = 0; j < judul.length; j++) {
                    System.out.print(judul[j]+" ==zz \n");
                }*/
                String path = doc.get("path");
                //String label = doc.get("label")+doc.get("subject")+doc.get("manufacture");
                //System.out.println("Ini label = "+label);
                if (path != null) {
                    System.out.println((i+1) + ". " + path +"  ----  doc="+hits[i].doc+" score="+hits[i].score+" ---- ");
                    //System.out.println(searcher.explain(query, hits[i].doc));
                    String title = doc.get("title");
                    if (title != null) {
                        System.out.println("   Title: " + doc.get("title"));
                    }
                } else {
                    System.out.println((i+1) + ". " + "No path for this document");
                }

            }

            Gson gson = new Gson();
            String json = gson.toJson(resultList);
            System.out.println(json);

            if (!interactive || end == 0) {
                break;
            }

            if (numTotalHits >= end) {
                boolean quit = false;
                while (true) {
                    System.out.print("Press ");
                    if (start - hitsPerPage >= 0) {
                        System.out.print("(p)revious page, ");
                    }
                    if (start + hitsPerPage < numTotalHits) {
                        System.out.print("(n)ext page, ");
                    }
                    System.out.println("(q)uit or enter number to jump to a page.");

                    String line = in.readLine();
                    if (line.length() == 0 || line.charAt(0)=='q') {
                        quit = true;
                        break;
                    }
                    if (line.charAt(0) == 'p') {
                        start = Math.max(0, start - hitsPerPage);
                        break;
                    } else if (line.charAt(0) == 'n') {
                        if (start + hitsPerPage < numTotalHits) {
                            start+=hitsPerPage;
                        }
                        break;
                    } else {
                        int page = Integer.parseInt(line);
                        if ((page - 1) * hitsPerPage < numTotalHits) {
                            start = (page - 1) * hitsPerPage;
                            break;
                        } else {
                            System.out.println("No such page");
                        }
                    }
                }
                if (quit) break;
                end = Math.min(numTotalHits, start + hitsPerPage);
            }
        }
    }

    private static String[] getUniqueTerms(String wordString) {
        String[] uniqueTerms = wordString.split("\\s+");
        for (int i = 0; i < uniqueTerms.length; i++) {
            // You may want to check for a non-word character before blindly
            // performing a replacement
            // It may also be necessary to adjust the character class
            uniqueTerms[i] = uniqueTerms[i].replaceAll("[^\\w]", "").replace("label", "").replace("containsIngredient","");
        }
        uniqueTerms = new HashSet<String>(Arrays.asList(uniqueTerms)).toArray(new String[0]);

        return uniqueTerms;
    }


}
