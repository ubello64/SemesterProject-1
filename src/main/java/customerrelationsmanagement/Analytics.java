package customerrelationsmanagement;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.StandardEntityCollection;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Analytics extends JPanel {
    public static final String ASSETS_TITLE = "Assets";
    public static final String DAILY_NUMBER_OF_ORDERS_TITLE =
     "Daily Number of Orders";
    public static final String DAILY_REVENUE_TITLE = "Daily Revenue";
    private final Crud crud;
    private final File directory;
    JFreeChart topCustomersHistogram;
    JFreeChart topOrdersHistogram;
    JFreeChart assetTimeSeries;
    JFreeChart revenueTimeSeries;

    public Analytics(Crud crud) {
        this.crud = crud;
        directory = createDirs("analytics");
    }

    
    
    private File createDirs(String pathName) {
        File directory = new File(pathName);
        if (!directory.exists()) {
            if (directory.mkdir()) {
                System.out.println("Directory is created!");
            } else {
                System.out.println("Failed to create directory!");
            }
        }
        return directory;
    }

    public File save(String filename, JFreeChart chart) throws IOException {
        final File output = new File(filename);
        final ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
        try {
            ChartUtilities.saveChartAsPNG(output, chart, 560, 370, info);
        } catch (Exception e) {
            e.printStackTrace();
        }
      //  System.out.println("-- saved");
        return output;
    }
}

 /*   public static Date StringToDate(String s) {
        Date result = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            result = dateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String DateToString(Date d) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(d);
        return date;
    }

    public Object[][] dailyStats() throws SQLException {
        String query = "SELECT date_accepted, SUM(sales.product_quantity * \n" +
                "inventory.sale_price) as result, COUNT(sales.product_id) \n" +
                "FROM sales join inventory on sales.product_id = inventory.product_id \n" +
                "GROUP BY date_accepted ORDER BY date_accepted ASC";
        Object[] assetTotals = getAssetTotal("");
        ResultSet rs = crud.query(query);
        query = "Select SUM(sales.product_quantity * " +
                        "(sale_price - wholesale_cost)) " +
                        "        as assets from sales " +
                        "INNER JOIN inventory on sales.product_id = inventory.product_id " +
                        "GROUP BY date_accepted " +
                        "ORDER BY date_accepted ASC";
        ResultSet rs2 = crud.query(query);


        Object[][] returnArray = new Object[crud.rowCountResults(rs)][4];
        for (int i = 0; rs.next() && rs2.next(); i++) {
            returnArray[i][0] =rs.getTimestamp(1);
            returnArray[i][1] =rs.getBigDecimal(2);
            returnArray[i][2] =rs.getLong(3);
            returnArray[i][3] =rs2.getBigDecimal(1);

        }
        return returnArray;
    }

    *//**
     * TODO: David & Uriel
     *
     * @param onDate TODO: David & Uriel
     * @return TODO: David & Uriel
     * @throws SQLException if there is an issue with the sql command or connection.
     *//*
    public Object[] getAssetTotal(String onDate)
            throws SQLException {

        String query = (onDate == null) ?
                "SELECT SUM(quantity * (sale_price - wholesale_cost))" +
                        "as assets from inventory" :
                "Select SUM(sales.product_quantity * " +
                        "(sale_price - wholesale_cost)) " +
                        "        as assets from sales " +
                        "INNER JOIN inventory on sales.product_id = inventory.product_id " +
                        "GROUP BY date_accepted " +
                        "ORDER BY date_accepted ASC";

        ResultSet rs = crud.query(query);
        rs.next();
        return crud.getRecords(rs);
    } // End getAssetTotal

    *//**
     * return an array of the top products on a date
     * @param date the date to return top products for
     * @param limit number of top products to return
     *
     * @return array of top products
     *
     * @throws SQLException
     *  if there is an issue with the sql command or connection.
     *//*
    public Object[] mostOrderedProducts(String date, int limit)
            throws SQLException {

        String query =
                "SELECT product_id " +
                        "FROM sales WHERE date_accepted = '" + date +
                        "' GROUP BY product_id ORDER BY sum(product_quantity) desc limit " + limit;
        ResultSet rs = crud.query(query);
        rs.next();
        return crud.getRecords(rs);
    } // End mostOrderedProducts

    *//**
     * return an array of the top customers ordered by most spent
     * @param date the date to return top customers for
     * @param rowResultLimit number of top customers to return
     * @return array of top customers emails
     *
     * @throws SQLException
     *  if there is an issue with the sql command or connection.
     *//*
    public Object[] mostValuableCustomers(String date, int rowResultLimit) throws SQLException {
        String MVC =
                "SELECT cust_email " +
                "AS revenue FROM sales " +
                "INNER JOIN " +
                " inventory i ON sales.product_id = i.product_id " +
                "WHERE date_accepted = '" + date +
                "' GROUP BY cust_email ORDER BY SUM(sales.product_quantity * (sale_price - " +
                "wholesale_cost)) DESC LIMIT " + rowResultLimit;

        ResultSet rs = crud.query(MVC);
        rs.next();
        return crud.getRecords(rs);
    } // End mostValuableCustomers

    *//**
     * TODO: David & Uriel
     *
     * @param date
     *  that date you want to produce the analysis for
     * @param columnName
     *  the column name that identifies the data to analyze
     * @param limit
     *  where limit is the number of rows you want (i.e., top 5? 10? 1000?)
     * @param isDescending
     *  top- to bottom if true, bottom to top if false
     * @param orderArg
     *  if you want to order these results by a specific column,
     *  include it here (i.e., "quantity" -> higher quantities will
     *  be on the top of the results
     *
     * @return 2d array of all the results
     *
     * @throws SQLException
     *  if the query was an incorrect string, according to sql syntax
     *//*
    public Object[][] topNByDate(String date, String columnName,
                                 int limit, boolean isDescending,
                                 String orderArg) throws SQLException {

        return crud.getRecords(crud.query(
                "SELECT * FROM sales WHERE " + columnName + " = '" + date +
                        "' ORDER BY " + orderArg + (isDescending ? " DESC" : "ASC") +
                        " LIMIT " + limit));
    } // End topNByDate

    *//**
     * TODO: David & Uriel
     *
     * @param date TODO: David & Uriel
     * @param limit TODO: David & Uriel
     * @param isDescending TODO: David & Uriel
     * @param gui TODO: David & Uriel
     *
     * @return TODO: David & Uriel
     *
     * @throws SQLException
     *  if there is an issue with the sql command or connection.
     *//*
    public String topNByCustomer
    (String date, int limit, boolean isDescending, GUI gui)
            throws SQLException {

        crud.setWorkingTable("customers");
        crud.update(" DROP TABLE IF EXISTS top_customers");
        String newTableName = "Top_" + limit + "_Customers";

        String dateClause;
        if(date != null) {
            dateClause = "WHERE date_accepted = '" + date + "'";
        } else {
            dateClause = "";
        } // End if

        String sql =
                " CREATE TEMPORARY TABLE IF NOT EXISTS " + newTableName +
                        " AS (SELECT cust_email, SUM(sales.product_quantity * " +
                        "(sale_price - wholesale_cost)) AS revenue " +
                        " FROM sales " +

                        dateClause +

                        " INNER JOIN customers on sales.cust_email" +
                        " = customers.cust_email " +
                        " INNER JOIN inventory on sales.product_id = " +
                        "inventory.product_id " +
                        " GROUP BY cust_email " +
                        " ORDER BY revenue " + (isDescending ? " DESC" : "ASC") +
                        " LIMIT " + limit + ")";

        crud.update(sql);
        crud.temporaryTables.add(newTableName);
        gui.addTable(newTableName);
        return newTableName;
    } // End topNByCustomer

}*/
/*
   String MVC =
                "SELECT cust_email " +
                "AS revenue FROM sales " +
                "INNER JOIN " +
                " inventory i ON sales.product_id = i.product_id " +
                "WHERE date_accepted = '" + date +
                "' GROUP BY cust_email ORDER BY SUM(sales.product_quantity * (sale_price - " +
                "wholesale_cost)) DESC LIMIT " + rowResultLimit;
    String query =
            "SELECT product_id " +
                    "FROM sales WHERE date_accepted = '" + date +
                    "' GROUP BY product_id ORDER BY sum(product_quantity) desc limit " + limit;

* 
* 
* */


