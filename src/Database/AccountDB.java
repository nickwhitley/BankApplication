package Database;

import java.math.BigDecimal;
import java.sql.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class AccountDB {

    private static final String DB_NAME = "accounts.db";
    private static final String CONNECTION_STRING =
            "jdbc:sqlite:E:\\Documents\\Coding\\Projects\\Bank Application\\Databases\\" + DB_NAME;
    private static final String TABLE_ACCOUNTS = "Accounts";
    private static final String COLUMN_ACCOUNT_NUMBER = "AccountNumber";
    private static final String COLUMN_ACCOUNT_TYPE = "AccountType";
    private static final String COLUMN_ACCOUNT_BALANCE = "AccountBalance";
    private static final String COLUMN_CUST_FIRST_NAME = "CustomerFirstName";
    private static final String COLUMN_CUST_LAST_NAME = "CustomerLastName";
    private static final String COLUMN_CUST_SSN = "CustomerSSN";

    private static Connection conn;
    private static Statement statement;

    static int accountNumber;
    static String accountType;
    static String accountBalance;
    static String custFirstName;
    static String custLastName;
    static String custSSN;

    //create connection to DB
    static {
        try{
            conn = DriverManager.getConnection(CONNECTION_STRING);
            statement = conn.createStatement();
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.out.println("Error connecting to Accounts Database. Please check AccountDB class!");
        }
    }

    //creates database table if it doesn't already exist
    static {
        try {
            statement.execute("CREATE TABLE IF NOT EXISTS " + TABLE_ACCOUNTS +
                    " (" + COLUMN_ACCOUNT_NUMBER + " INTEGER, " +
                    COLUMN_ACCOUNT_TYPE + " TEXT, " +
                    COLUMN_ACCOUNT_BALANCE + " TEXT, " +
                    COLUMN_CUST_FIRST_NAME + " TEXT, " +
                    COLUMN_CUST_LAST_NAME + " TEXT, " +
                    COLUMN_CUST_SSN + " TEXT ) ");
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.out.println("Error creating account DB table. Please see createAccount() " +
                    "method in AcccountDB class.");
        }
    }

    public void createAccount(int accountNumber, String accountType, String accountBalance,
                                     String custFirstName, String custLastName, String custSSN) {

        try {
            statement.execute("INSERT INTO " + TABLE_ACCOUNTS +
                    " (" + COLUMN_ACCOUNT_NUMBER + ", " +
                    COLUMN_ACCOUNT_TYPE + ", " +
                    COLUMN_ACCOUNT_BALANCE + ", " +
                    COLUMN_CUST_FIRST_NAME + ", " +
                    COLUMN_CUST_LAST_NAME + ", " +
                    COLUMN_CUST_SSN + ") " +
                    "VALUES('" +
                    accountNumber + "', '" +
                    accountType + "', '" +
                    accountBalance + "', '" +
                    custFirstName + "', '" +
                    custLastName + "', '" +
                    custSSN + "')");
        } catch (SQLException exception) {
            System.out.println("Error when adding new account to account database" +
                    ". Please see createAccount() in AccountDb class.");
            exception.printStackTrace();
        }
    }

    public String getAccountBalance(String accountNumber) {
        try{
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(" SELECT " + COLUMN_ACCOUNT_BALANCE +
                    " FROM " + TABLE_ACCOUNTS + " WHERE " + COLUMN_ACCOUNT_NUMBER + " = '" + accountNumber + "';'");

            System.out.println();

            accountBalance = result.getString("AccountBalance");
        } catch (SQLException exception) {
            System.out.println("Error getting account balance. See getAccountBalance() in AccountDB class.");
            exception.printStackTrace();
        }

        return accountBalance;
    }

    public ArrayList<Integer> getAllCustAccountNumbers(String custSSN){
        ArrayList<Integer> accountNumbers = new ArrayList<Integer>();
        try{
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery("SELECT " + COLUMN_ACCOUNT_NUMBER + " FROM " + TABLE_ACCOUNTS +
                    " WHERE " + COLUMN_CUST_SSN + " = '" + custSSN + "';"   );
            ResultSetMetaData resultData = result.getMetaData();
            while (result.next()){
                accountNumbers.add(result.getInt(resultData.getColumnCount()));
            }
        } catch (SQLException e) {
            System.out.println("Error getting all account number for customer, see getAllCustAccountNumbers() in AccountDB");
            e.printStackTrace();
        }
        return accountNumbers;
    }

    public void printAllAccountBalances(String custSSN) {
        try{
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery("SELECT " + COLUMN_ACCOUNT_NUMBER + ", " + COLUMN_ACCOUNT_TYPE + ", " + COLUMN_ACCOUNT_BALANCE +" FROM " + TABLE_ACCOUNTS +
                    " WHERE " + COLUMN_CUST_SSN + " = '" + custSSN + "';");
            ResultSetMetaData resultData = result.getMetaData();
            int columnCount = resultData.getColumnCount();

            //iterate and print each line
            System.out.println();
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(resultData.getColumnLabel(i) + "      |      ");
            }
            System.out.println();
            System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
            while (result.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(" " + result.getString(i) + "       |       ");
                }
                System.out.println();
            }
            System.out.println();
        } catch (SQLException exception) {
            System.out.println("Error when printing all account balances.");
            exception.printStackTrace();
        }
    }

    public boolean checkIfCustOwnsAccountNumber(String custSSN, String accountNumber){
        try {
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery("SELECT " + COLUMN_ACCOUNT_NUMBER + " FROM " + TABLE_ACCOUNTS +
                    " WHERE " + COLUMN_CUST_SSN + " = '" + custSSN + "';");
            if (result.isClosed()) {
                System.out.println("No matching account number found.");
                return false;
            }
            do {
                if (result.getString("AccountNumber").matches(accountNumber)) {
                    return true;
                }
            } while (result.next());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public void printAllAccountsAllData() {
        try{
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM " + TABLE_ACCOUNTS);

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
            System.out.println("Error when printing all account databases contents");
            exception.printStackTrace();
        }
    }

    public void withdrawFunds(String accountNumber, BigDecimal amount){
        BigDecimal currentBalance = new BigDecimal(getAccountBalance(accountNumber).replaceAll("[^0-9.]", ""));
        BigDecimal newBalance = currentBalance.subtract(amount);
        NumberFormat USFormat = NumberFormat.getCurrencyInstance(Locale.US);
        setAccountBalance(accountNumber, USFormat.format(newBalance));
    }

    public void closeAccount(int accountNumber){
        try {
            statement.execute("DELETE FROM " + TABLE_ACCOUNTS + " WHERE " +
                    COLUMN_ACCOUNT_NUMBER + " = '" + accountNumber + "';");
            System.out.println("BankApplication.Account " + accountNumber + " has been closed.");
        } catch (SQLException exception) {
            System.out.println("Error when deleting account from table, see 'closeAccount()' in AccountDB");
            exception.printStackTrace();
        }
    }

    public void depositFunds(String accountNumber, BigDecimal amount){
        BigDecimal currentBalance = new BigDecimal(getAccountBalance(accountNumber).replaceAll("[^0-9.]", ""));
        BigDecimal newBalance = currentBalance.add(amount);
        NumberFormat USFormat = NumberFormat.getCurrencyInstance(Locale.US);
        setAccountBalance(accountNumber, USFormat.format(newBalance));
    }

    public boolean doesCustHaveOpenAccount(String custSSN){
        try{
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM " + TABLE_ACCOUNTS +
                    " WHERE " + COLUMN_CUST_SSN + " = '" + custSSN + "';");

            if (result.isClosed()){
                return false;
            }
            return true;
        } catch (SQLException exception){
            System.out.println("Error when checking if customer has open account, check doesCustHaveOpenAccount()" +
                    " in AccountDB.");
            exception.printStackTrace();
        }
        return true;
    }

    private void setAccountBalance(String accountNumber, String accountBalance){
        try{
            statement.execute(" UPDATE " + TABLE_ACCOUNTS +
                    " SET " + COLUMN_ACCOUNT_BALANCE + " = '" + accountBalance +
                    "' WHERE " + COLUMN_ACCOUNT_NUMBER + " = '" + accountNumber + "';");
        } catch (SQLException exception) {
            System.out.println("ERROR when setting account balance, see 'setAccountBalance()' in AccountDB");
            exception.printStackTrace();
        }
    }


    public void deleteAllDataEntries() {
        try{
            statement.execute("DELETE FROM " + TABLE_ACCOUNTS);
        } catch (SQLException exception) {
            System.out.println("Error deleting all data entries in AccountDB");
            exception.printStackTrace();
        }
    }

    public void deleteTable() {
        try {
            statement.execute("DROP TABLE " + TABLE_ACCOUNTS);
        } catch (SQLException exception) {
            System.out.println("error when deleting accounts DB table.");
            exception.printStackTrace();
        }
    }
    }

