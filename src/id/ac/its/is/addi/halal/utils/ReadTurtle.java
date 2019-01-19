/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.ac.its.is.addi.halal.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import id.ac.its.is.addi.halal.model.TurtleModel;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import org.apache.jena.rdf.model.*;

/**
 * @author USER
 */
public class ReadTurtle {

    static final String inputFileName = "./Happy_Tos_Rasa_Jagung_Bakar.ttl";
    private String filePath;
    private List<TurtleModel> spo;

    public static void main(String[] args) throws IOException {



        /*Model model = ModelFactory.createDefaultModel();

        InputStream in = FileManager.get().open(inputFileName);
        if (in == null) {
            throw new IllegalArgumentException("File: " + inputFileName + " not found");
        }

        // read the RDF/XML file
        model.read(in, null, "TURTLE");
        //model.read(new FileInputStream("41.ttl"),null,"TTL");

        String query = ""
                + "prefix halalc: <http://halal.addi.is.its.ac.id/resources/certificates/> \n"
                + "prefix halals: <http://halal.addi.is.its.ac.id/resources/halalsources/> \n"
                + "prefix owl:   <http://www.w3.org/2002/07/owl#> \n"
                + "prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
                + "prefix xsd:   <http://www.w3.org/2001/XMLSchema#> \n"
                + "prefix halalf: <http://halal.addi.is.its.ac.id/resources/foodproducts/> \n"
                + "prefix halalv: <http://halal.addi.is.its.ac.id/halalv.ttl#> \n"
                + "prefix halali: <http://halal.addi.is.its.ac.id/resources/ingredients/> \n"
                + "prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> \n"
                + "prefix halalm: <http://halal.addi.is.its.ac.id/resources/manufactures/> \n"
                + "prefix foaf:  <http://xmlns.com/foaf/0.1/> \n\n"
                + "SELECT ?cings WHERE { "
                + "?s a halalv:FoodProduct;"
                + "halalv:containsIngredient ?cings"
                + " }";

        String query_id = ""
                + "prefix halalc: <http://halal.addi.is.its.ac.id/resources/certificates/> \n"
                + "prefix halals: <http://halal.addi.is.its.ac.id/resources/halalsources/> \n"
                + "prefix owl:   <http://www.w3.org/2002/07/owl#> \n"
                + "prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
                + "prefix xsd:   <http://www.w3.org/2001/XMLSchema#> \n"
                + "prefix halalf: <http://halal.addi.is.its.ac.id/resources/foodproducts/> \n"
                + "prefix halalv: <http://halal.addi.is.its.ac.id/halalv.ttl#> \n"
                + "prefix halali: <http://halal.addi.is.its.ac.id/resources/ingredients/> \n"
                + "prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> \n"
                + "prefix halalm: <http://halal.addi.is.its.ac.id/resources/manufactures/> \n"
                + "prefix foaf:  <http://xmlns.com/foaf/0.1/> \n\n"
                + "SELECT ?s ?p ?o  WHERE { "
                + "?s ?p ?o"
                + " }";

        //System.out.println(query);
        final QueryExecution exec = QueryExecutionFactory.create(query_id, model);
        final ResultSet rs = exec.execSelect();
        while (rs.hasNext()) {

            final QuerySolution qs = rs.next();

            String s = qs.get("s").toString();
            String sub[] = s.split("/");
            String subjek = sub[sub.length - 1];

            String p = qs.get("p").toString();
            String pred[] = p.split("#");
            String predikat = pred[pred.length - 1];

            String o = qs.get("o").toString();
            String objek = o;

            if (o.contains("^^")) {
                String obj[] = o.split("\\^\\^");
                objek = obj[0];;
            } else if (o.contains("/")) {
                String obj[] = o.split("/");
                //objek = obj[obj.length - 2] + " " + obj[obj.length - 1];
                objek = obj[obj.length - 1];
            } else {
                objek = o;
            }

            System.out.println(
                    ""
                            + "\nSubject : " + subjek
                            + "\nPredicate : " + predikat
                            + "\nObject : " + objek
            );
        }*/


        ReadTurtle readTurtle = new ReadTurtle();
        List<TurtleModel> turtle = readTurtle.readTurtle("C:\\Users\\ahm\\IdeaProjects\\Halal\\Happy_Tos_Rasa_Jagung_Bakar.ttl");
        for (int i = 0; i < turtle.size(); i++) {
            System.out.println("s : " +turtle.get(i).getSubject().replace("_", " ")+" \n\"p : \" "
                    +turtle.get(i).getPredicate().replace("_", " ")
                    +" \n\"o : \" "+turtle.get(i).getObject().replace("_", " "));
        }

        // write it to standard out
        //model.write(System.out, "TURTLE");
    }

    public void printTurtle() throws IOException{
        System.out.println(this.readTurtle("C:\\Users\\ahm\\IdeaProjects\\Halal\\Happy_Tos_Rasa_Jagung_Bakar.ttl"));

    }

    public List<TurtleModel> readTurtle(String path) throws IOException{
        this.spo = new ArrayList<>();
        Model model = ModelFactory.createDefaultModel();

        Path sfilePath = Paths.get(path);

        //InputStream in = FileManager.get().open(inputFileName);
        try(InputStream in = Files.newInputStream(sfilePath)) {
            if (in == null) {
                throw new IllegalArgumentException("File: " + inputFileName + " not found");
            }

            // read the RDF/XML file
            model.read(in, null, "TURTLE");
            //model.read(new FileInputStream("41.ttl"),null,"TTL");

            String query_id = ""
                    + "prefix halalc: <http://halal.addi.is.its.ac.id/resources/certificates/> \n"
                    + "prefix halals: <http://halal.addi.is.its.ac.id/resources/halalsources/> \n"
                    + "prefix owl:   <http://www.w3.org/2002/07/owl#> \n"
                    + "prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
                    + "prefix xsd:   <http://www.w3.org/2001/XMLSchema#> \n"
                    + "prefix halalf: <http://halal.addi.is.its.ac.id/resources/foodproducts/> \n"
                    + "prefix halalv: <http://halal.addi.is.its.ac.id/halalv.ttl#> \n"
                    + "prefix halali: <http://halal.addi.is.its.ac.id/resources/ingredients/> \n"
                    + "prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> \n"
                    + "prefix halalm: <http://halal.addi.is.its.ac.id/resources/manufactures/> \n"
                    + "prefix foaf:  <http://xmlns.com/foaf/0.1/> \n\n"
                    + "SELECT ?s ?p ?o  WHERE { "
                    + "?s ?p ?o"
                    + " }";

            //System.out.println(query);
            final QueryExecution exec = QueryExecutionFactory.create(query_id, model);
            final ResultSet rs = exec.execSelect();
            while (rs.hasNext()) {

                final QuerySolution qs = rs.next();

                String s = qs.get("s").toString();
                String sub[] = s.split("/");
                String subjek = sub[sub.length - 1];

                String p = qs.get("p").toString();
                String pred[] = p.split("#");
                String predikat = pred[pred.length - 1];

                String o = qs.get("o").toString();
                String objek = o;


                if (o.contains("^^")) {
                    String obj[] = o.split("\\^\\^");
                    objek = obj[0];;
                } else if (o.contains("/")) {
                    String obj[] = o.split("/");
                    //objek = obj[obj.length - 2] + " " + obj[obj.length - 1];
                    objek = obj[obj.length - 1];
                    objek = objek.replace("halalv.ttl#", "");
                }
                else {
                    objek = o;
                }

                /*System.out.println(
                        ""
                                + "\nSubject : " + subjek
                                + "\nPredicate : " + predikat
                                + "\nObject : " + objek
                );*/

                spo.add(new TurtleModel(subjek, predikat, objek));

            }
        }

        return spo;

    }

}
