package id.ac.its.is.addi.halal;

import id.ac.its.is.addi.halal.utils.Settings;

import java.sql.*;

public class Jdbc {
    public static void main(String[] args)
    {
        try
        {
            // create our mysql database connection
            String myDriver = "org.gjt.mm.mysql.Driver";
            String myUrl = "jdbc:mysql://"+ Settings.DB_HOST+"/"+Settings.DB_NAME;
            Class.forName(myDriver);
            Connection conn = DriverManager.getConnection(myUrl, Settings.DB_USER, Settings.DB_PASSWORD);

            // our SQL SELECT query.
            // if you only need a few columns, specify them by name instead of using "*"
            String query = "SELECT count(ingredient_id) as ingredient_ranking FROM in_ingredient_ranking where ingredient_id = 283";

            // create the java statement
            Statement st = conn.createStatement();

            // execute the query, and get a java resultset
            ResultSet rs = st.executeQuery(query);

            // iterate through the java resultset
            while (rs.next())
            {
                int id = rs.getInt("ingredient_ranking");
                //String fName = rs.getString("fName");
                /*String lastName = rs.getString("last_name");
                Date dateCreated = rs.getDate("date_created");
                boolean isAdmin = rs.getBoolean("is_admin");
                int numPoints = rs.getInt("num_points");*/

                // print the results
                System.out.format("%s\n", id);
            }
            st.close();
        }
        catch (Exception e)
        {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }


    public String getIndependentRanking(String typeEntityParams, int idParams) {
        try {
            // create our mysql database connection
            String myDriver = "org.gjt.mm.mysql.Driver";
            String myUrl = "jdbc:mysql://"+ Settings.DB_HOST+"/"+Settings.DB_NAME;
            Class.forName(myDriver);
            Connection conn = DriverManager.getConnection(myUrl, Settings.DB_USER, Settings.DB_PASSWORD);

            // our SQL SELECT query.
            // if you only need a few columns, specify them by name instead of using "*"
            String query = "SELECT count(ingredient_id) as ingredient_ranking FROM in_ingredient_ranking where ingredient_id = 283";

            // create the java statement
            Statement st = conn.createStatement();

            // execute the query, and get a java resultset
            ResultSet rs = st.executeQuery(query);

            // iterate through the java resultset
            while (rs.next())
            {
                int id = rs.getInt("ingredient_ranking");
                //String fName = rs.getString("fName");
                    /*String lastName = rs.getString("last_name");
                    Date dateCreated = rs.getDate("date_created");
                    boolean isAdmin = rs.getBoolean("is_admin");
                    int numPoints = rs.getInt("num_points");*/

                // print the results
                System.out.format("%s\n", id);
            }
            st.close();
            return (String) "a";
    } catch (Exception e) { 
        System.err.println("Got an exception! ");
        System.err.println(e.getMessage());
        return "b";
    }


    }
}
