import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ResultSetToHTMLFormatter {

    public static synchronized String getHTMLRows(ResultSet results) throws SQLException {

        StringBuffer htmlRows = new StringBuffer(); // htmlRows is  a buffer holding a giant string that contains the result table in html format
        ResultSetMetaData metaData = results.getMetaData(); // the metadata belonging to the inbound Resultset object
        int columnCount = metaData.getColumnCount();

        //set the table header row
        //add <tr> element for header row of the outbound table
        //htmlRows.append("<table>");
        htmlRows.append("<tr>");//start header row

        //loop through all the columns to print the column header data
        for (int i = 1; i <= columnCount ; i++) {
            htmlRows.append("<th>" + metaData.getColumnName(i) + "</th>");
        }
        //add the <tr> element to close the header row
        htmlRows.append("</tr>");//close header row

        //set the remainder of the table - row by row
        int cnt = 0; //counter for zebra striping effect
        //loop across all the rows in the resultset
        while (results.next()) {
            //use cnt for zebra stripping effect
            cnt++;
            htmlRows.append("<tr>");

            //for each column in the row add the <td> element
            for (int i = 1; i <=columnCount; i++) {
                htmlRows.append("<td>" + results.getObject(i) + "</td>");
            }//end for
            htmlRows.append("</tr>");
        }//end while
        //htmlRows.append("</tr>");//append final row ending element
        //htmlRows.append("</table>");
        return htmlRows.toString();// convert buffer into a string and return
    }//end getHTMLRows method
}//end ResultSetToHTMLFormatter class
