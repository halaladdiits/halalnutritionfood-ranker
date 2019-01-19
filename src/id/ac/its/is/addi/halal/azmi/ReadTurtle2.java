/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.ac.its.is.addi.halal.azmi;

import java.io.*;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;

/**
 * @author USER
 */
public class ReadTurtle2 {

    static final String inputFileName = "./ttl/41.ttl";

    public static void main(String[] args) throws FileNotFoundException {

        Model model = ModelFactory.createDefaultModel();

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
                + "SELECT ?s ?p ?o ?object WHERE { "
                + "?s ?p ?o"
                + "OPTIONAL {"
                + "?o rdfs:label ?object"
                + "}"
                + "}";

        //System.out.println(query);
        final QueryExecution exec = QueryExecutionFactory.create(query_id, model);
        final ResultSet rs = exec.execSelect();
        while (rs.hasNext()) {

            final QuerySolution qs = rs.next();

            String s = qs.get("s").toString();
            String sub[] = s.split("/");
            String subjek = sub[sub.length - 2] + " " + sub[sub.length - 1];

            String p = qs.get("p").toString();
            String pred[] = p.split("#");
            String predikat = pred[pred.length - 1];

            String o = qs.get("o").toString();
            String objek = o;

            if (o.contains("^^")) {
                String obj[] = o.split("\\^\\^");
                objek = obj[0];
                ;
            } else if (o.contains("/")) {
                String obj[] = o.split("/");
                objek = obj[obj.length - 1];
            } else {
                objek = o;
            }

            System.out.println(
                    ""
                            + "\nSubject : " + subjek
                            + "\nPredicate : " + predikat
                            + "\nObject : " + objek
                            + "\nObject : " + qs.get("object").toString()
            );
        }

        // write it to standard out
        //model.write(System.out, "TURTLE");
    }

}
