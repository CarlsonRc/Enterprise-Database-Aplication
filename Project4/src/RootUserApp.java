// A simple servlet to process get requests.
// Main servlet in first-example web-app

//import javax.servlet.*;
//import javax.servlet.http.*;
import com.mysql.cj.jdbc.MysqlDataSource;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.sql.*;
import java.util.Objects;
import java.util.Properties;
import java.util.Vector;

public class RootUserApp extends HttpServlet {

    private static Connection key;
    // process "get" requests from clients
    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException  {

        //PrintWriter writer = response.getWriter();// get response writer
        String sqlStatement = request.getParameter("sqlStatement");// grab statement from html
        String message = "";

        try {
            connectToDataBase();

            String driver = "com.mysql.cj.jdbc.Driver";
            Class.forName(driver);
            Connection connection = key;
            String sql = sqlStatement;
            Statement statement = connection.prepareStatement(sql);// prepare statement
            String businessLogic = "update suppliers\n" +
                    "set status = status + 5\n" +
                    "where snum in (select snum\n" +
                    " from shipments\n" +
                    "where quantity >= 100)";

            if (statement.execute(sql)){
                try {
                    ResultSet resultSet = statement.executeQuery(sql);//Execute query
                    // create table to put in html
                    message = ResultSetToHTMLFormatter.getHTMLRows(resultSet);
                    //writer.println(message);// send string to html
                } catch (SQLException e) {
                    message = "<tr bgcolor=#ff0000><td><font color=#ffffff><b><Error executing the SQL statement:></b><br>" + e.getMessage() + "</tr></td></font>";
                    e.printStackTrace();
                }
            }
            else  {
                //business logic goes here
                int rows = statement.executeUpdate(businessLogic);//Execute Business Logic
                statement.close();
                message = "<tr bgcolor=#11b840><td><font color=#ffffff><b><Error executing the SQL statement:></b><br> Statement executed successfully! Rows updated: " + rows + "  <br><br> Business logic triggered! <br></tr></td></font>";
            }

        } catch (SQLException e) {
            message = "<tr bgcolor=#ff0000><td><font color=#ffffff><b><Error executing the SQL statement:></b><br>" + e.getMessage() + "</tr></td></font>";
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        HttpSession session = request.getSession();
        session.setAttribute("message", message);
        session.setAttribute("sqlStatement", sqlStatement);
        RequestDispatcher dispatcher =
                getServletContext().getRequestDispatcher("/rootHome.jsp");
        dispatcher.forward(request, response);


    } //end doPost() method

    public static void connectToDataBase() throws SQLException {

        Properties properties = new Properties();
        FileInputStream filein;
        MysqlDataSource dataSource;
        //read properties file
        try {
            filein = new FileInputStream("C:/Program Files/Apache Software Foundation/Tomcat 10.0/webapps/Project4/WEB-INF/lib/root.properties"); // location of the .properties file to access SQL
            properties.load(filein);
            dataSource = new MysqlDataSource();
            dataSource.setURL(properties.getProperty("MYSQL_DB_URL"));
            dataSource.setUser(properties.getProperty("MYSQL_DB_USERNAME"));
            dataSource.setPassword(properties.getProperty("MYSQL_DB_PASSWORD"));

            key = dataSource.getConnection();

        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
    }// end conncectToDataBase
} //end RootUserApp class
