import com.mysql.cj.jdbc.MysqlDataSource;

import java.awt.BorderLayout;
import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

class MySQLTable
{

    private static Connection con = null;
    private static String URL = "jdbc:mysql://localhost:3306/project3?useTimezone=true&serverTimezone=UTC";
    private static String driver = "com.mysql.cj.jdbc.Driver";
    private static String user = "client";
    private static String pass = "client";

    /**
     * Main aplication entry point
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException
    {

        Properties properties = new Properties();
        FileInputStream filein;
        MysqlDataSource dataSource;

        // a MySQL statement
        Statement stmt;
        // a MySQL query
        String query;
        // the results from a MySQL query
        ResultSet rs;
        ResultSetMetaData metaData;
        int numRows;

        // 2 dimension array to hold table contents
        // it holds temp values for now
        Object rowData[][] = {{"Row1-Column1", "Row1-Column2", "Row1-Column3"}};
        // array to hold column names
        Object columnNames[] = {"Column One", "Column Two", "Column Three"};

        // create a table model and table based on it
        DefaultTableModel mTableModel = new DefaultTableModel(rowData, columnNames);
        JTable table = new JTable(mTableModel);

        // try and connect to the database
        try {
            filein = new FileInputStream("C:/Program Files/Apache Software Foundation/Tomcat 10.0/webapps/Project4/WEB-INF/lib/client.properties"); // location of the .properties file to access SQL
            properties.load(filein);
            dataSource = new MysqlDataSource();
            dataSource.setURL(properties.getProperty("MYSQL_DB_URL"));
            dataSource.setUser(properties.getProperty("MYSQL_DB_USERNAME"));
            dataSource.setPassword(properties.getProperty("MYSQL_DB_PASSWORD"));

            Class.forName(driver).newInstance();
            con = dataSource.getConnection();
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }

        // run the desired query
        query = "SELECT * from jobs";
        // make a statement with the server
        stmt = con.createStatement();
        // execute the query and return the result
        rs = stmt.executeQuery(query);

        // create the gui
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setSize(300, 150);
        frame.setVisible(true);

        // remove the temp row
        mTableModel.removeRow(0);

        // create a temporary object array to hold the result for each row
        Object[] rows;
        // for each row returned
        while (rs.next()) {
            // add the values to the temporary row
            rows = new Object[]{rs.getString(1), rs.getString(2), rs.getString(3)};
            // add the temp row to the table
            mTableModel.addRow(rows);


        }
    }
}