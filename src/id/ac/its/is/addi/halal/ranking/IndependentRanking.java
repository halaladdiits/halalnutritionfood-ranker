package id.ac.its.is.addi.halal.ranking;

import id.ac.its.is.addi.halal.utils.Settings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class IndependentRanking {

    public IndependentRanking() {

    }

    public static void main(String[] args) {
        Double ranking = getIndependentRanking("FoodAdditive", 283);
        System.out.println(ranking);
    }

    public static Double getIndependentRanking(String typeEntityParams, int idParams) {
        try {
            // create our mysql database connection
            String myDriver = "org.gjt.mm.mysql.Driver";
            String myUrl = "jdbc:mysql://"+ Settings.DB_HOST+"/"+Settings.DB_NAME;
            Class.forName(myDriver);
            Connection conn = DriverManager.getConnection(myUrl, Settings.DB_USER, Settings.DB_PASSWORD);

            // our SQL SELECT query.
            // if you only need a few columns, specify them by name instead of using "*"
            if (typeEntityParams.equalsIgnoreCase("FoodProduct")) {
                String query = "SELECT * FROM in_foodproduct_ranking where foodproduct_id ="+idParams;

                // create the java statement
                Statement st = conn.createStatement();

                // execute the query, and get a java resultset
                ResultSet rs = st.executeQuery(query);

                // iterate through the java resultset
                Double ranking = 0.0;
                while (rs.next())
                {
                    ranking = rs.getDouble("ranking");
                    //String fName = rs.getString("fName");
                    /*String lastName = rs.getString("last_name");
                    Date dateCreated = rs.getDate("date_created");
                    boolean isAdmin = rs.getBoolean("is_admin");
                    int numPoints = rs.getInt("num_points");*/

                    // print the results
                    //System.out.format("%s\n", ranking);
                }
                st.close();
                return ranking;
            } else if (typeEntityParams.equalsIgnoreCase("FoodAdditive")) {
                String query = "SELECT count(ingredient_id) as ingredient_ranking FROM in_ingredient_ranking where ingredient_id = "+idParams;

                // create the java statement
                Statement st = conn.createStatement();

                // execute the query, and get a java resultset
                ResultSet rs = st.executeQuery(query);

                // iterate through the java resultset
                Double ranking = 0.0;
                while (rs.next())
                {
                    ranking = rs.getDouble("ingredient_ranking");
                    //ranking = Double.valueOf(iranking);
                    //System.out.println(ranking);
                    //String fName = rs.getString("fName");
                    /*String lastName = rs.getString("last_name");
                    Date dateCreated = rs.getDate("date_created");
                    boolean isAdmin = rs.getBoolean("is_admin");
                    int numPoints = rs.getInt("num_points");*/

                    // print the results
                    //System.out.format("%s\n", ranking);
                }
                st.close();
                return ranking;
            } else {
                return  0.0;
            }

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
            return 0.0;
        }


    }
}
