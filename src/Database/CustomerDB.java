package Database;

import java.sql.*;

public class CustomerDB {

    private static final String DB_NAME = "customers.db";
    private static final String CONNECTION_STRING =
            "jdbc:sqlite:E:\\Documents\\Coding\\Projects\\Bank Application\\Databases\\" + DB_NAME;
    private static final String TABLE_CUSTOMERS = "Customers";
    private static final String COLUMN_CUST_FIRST_NAME = "CustomerFirstName";
    private static final String COLUMN_CUST_LAST_NAME = "CustomerLastName";
    private static final String COLUMN_CUST_SSN = "CustomerSSN";
    private static final String COLUMN_ONLINE_USERNAME = "OnlineUsername";
    private static final String COLUMN_ONLINE_PASSWORD = "OnlinePassword";

    private static Connection conn;
    private static Statement statement;

    static String customerFirstName;
    static String customerLastName;
    static String custSSN;
    static String onlineUsername;
    static String onlinePassword;

    //create connection to database
    static {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            statement = conn.createStatement();
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.out.println("Error connecting to Customer Database. Please check CustomerDB!");
        }
    }

    //create table if it doesn't already exist
    static {
        try{
            statement.execute("CREATE TABLE IF NOT EXISTS " + TABLE_CUSTOMERS +
                    " (" + COLUMN_CUST_FIRST_NAME + " TEXT, " +
                    COLUMN_CUST_LAST_NAME + " TEXT, " +
                    COLUMN_CUST_SSN + " TEXT, " +
                    COLUMN_ONLINE_USERNAME + " TEXT, " +
                    COLUMN_ONLINE_PASSWORD + " TEXT ) ");
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.out.println("Error creating customer Db table. Please see createCustomer() " +
                    "method in CustomerDB class.");
        }
    }

    public void createCustomer(String customerFirstName, String customerLastName, String custSSN, String onlineUsername,
                               String onlinePassword) {
        try{
            statement.execute("INSERT INTO " + TABLE_CUSTOMERS +
                    " (" + COLUMN_CUST_FIRST_NAME + ", " +
                    COLUMN_CUST_LAST_NAME + ", " +
                    COLUMN_CUST_SSN + ", " +
                    COLUMN_ONLINE_USERNAME + ", " +
                    COLUMN_ONLINE_PASSWORD + ") " +
                    "VALUES('" +
                    customerFirstName + "', '" +
                    customerLastName + "', '" +
                    custSSN + "', '" +
                    onlineUsername + "', '" +
                    onlinePassword + "')");
        } catch (SQLException exception) {
            System.out.println("Error when adding new customer data into customer database." +
                    "Please see createCustomer() in CustomerDB class.");
        }
    }

    public boolean isCustSSNInDatabase(String custSSN){
        try{
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery("SELECT " + COLUMN_CUST_SSN + " FROM " + TABLE_CUSTOMERS +
                    " WHERE " + COLUMN_CUST_SSN + " = '" + custSSN + "';");

            if (result.isClosed()) {
                return false;
            }
            String returnedCust = result.getString("CustomerSSN");
            if (returnedCust.isEmpty()) {
                return false;
            }

        } catch (SQLException exception) {
            System.out.println("Error checking if customer SSN exists in CustomerDB.");
            exception.printStackTrace();
        }

        return true;
    }

    public boolean isCustUsernameInDatabase(String onlineUsername){
        try{
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery("SELECT " + COLUMN_ONLINE_USERNAME + " FROM " + TABLE_CUSTOMERS +
                    " WHERE " + COLUMN_ONLINE_USERNAME + " = '" + onlineUsername + "';");

            if (result.isClosed()) {
                return false;
            }
            String returnedCust = result.getString("OnlineUsername");
            if (returnedCust.isEmpty()) {
                return false;
            }

        } catch (SQLException exception) {
            System.out.println("Error checking if customer username exists in CustomerDB.");
        }

        return true;
    }

    public String getCustomerFirstName(String custSSN){
        try{
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery("SELECT " + COLUMN_CUST_FIRST_NAME + " FROM " + TABLE_CUSTOMERS +
                    " WHERE " + COLUMN_CUST_SSN + " = '" + custSSN + "';");

            if (result.isClosed()) {
                System.out.println("Customer does not exist in Database.");
            }
            String returnedFirstName = result.getString("CustomerFirstName");
            return returnedFirstName;
        } catch (SQLException exception) {
            System.out.println("Error checking for customer in DB, check 'getCustomerFirstName()' in CustomerDB.");
            exception.printStackTrace();
        }
        System.out.println("Something went wrong in 'getCustomerFirstName()' in CustomerDB.");
        return null;
    }

    public String getCustomerLastName(String custSSN) {
        try{
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery("SELECT " + COLUMN_CUST_LAST_NAME + " FROM " + TABLE_CUSTOMERS +
                    " WHERE " + COLUMN_CUST_SSN + " = '" + custSSN + "';");

            if (result.isClosed()){
                System.out.println("Customer does not exist in Database.");
            }
            String returnedLastName = result.getString("CustomerLastName");
            return returnedLastName;
        } catch (SQLException exception) {
            System.out.println("Error checking for customer in DB, check 'getCustomerLastName()' in CustomerDB.");
            exception.printStackTrace();
        }
        System.out.println("Something went wrong in 'getCustomerLastName()' in CustomerDB.");
        return null;
    }

    public String getCustomerPassword(String onlineUsername) {
        try {
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery("SELECT " + COLUMN_ONLINE_PASSWORD + " FROM " + TABLE_CUSTOMERS +
                    " WHERE " + COLUMN_ONLINE_USERNAME + " = '" + onlineUsername + "';");
            if (result.isClosed()) {
                System.out.println("Username does not exist in database.");
            }
            String returnedPassword = result.getString("OnlinePassword");
            return returnedPassword;
        } catch (SQLException exception) {
            System.out.println("Error getting customer password, check getCustomerPassword() in CustomerDB.");
            exception.printStackTrace();
        }
        System.out.println("Something went wrong in getCustomerPassword() in CustomerDB.");
        return null;
    }

    public String getCustomerSSN(String onlineUsername) {
        try{
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery("SELECT " + COLUMN_CUST_SSN + " FROM " + TABLE_CUSTOMERS +
                    " WHERE " + COLUMN_ONLINE_USERNAME + " = '" + onlineUsername + "';");
            if (result.isClosed()){
                System.out.println("Username does not exist in database.");
            }
            String returnedSSN = result.getString("CustomerSSN");
            return returnedSSN;
        } catch (SQLException exception) {
            System.out.println("Error getting customerSSN, check getCustomerSSN() in CustomerDB.");
            exception.printStackTrace();
        }
        System.out.println("Something went wrong in getCustomerSSN() in customerDB.");
        return null;
    }

    public void printAllCustomersAllData() {
        try{
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM " + TABLE_CUSTOMERS);

            ResultSetMetaData resultData = result.getMetaData();
            int columnCount = resultData.getColumnCount();

            //iterate through columns and print each value
            System.out.println();
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(resultData.getColumnLabel(i) + " | ");
            }
            while(result.next()) {
                System.out.println();
                System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");

                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(" " + result.getString(i) + "     |   ");
                }
            }
            System.out.println();
        } catch (SQLException exception) {
            System.out.println("Error when printing all customers database contents");
            exception.printStackTrace();
        }
        System.out.println();
    }

    public void deleteAllDataEntries() {
        try{
            statement.execute("DELETE FROM " + TABLE_CUSTOMERS);
        } catch (SQLException exception) {
            System.out.println("Error deleting all data entries in CustomerDB");
            exception.printStackTrace();
        }
    }


}
