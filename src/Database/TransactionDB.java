package Database;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;

public class TransactionDB {

    private static final String DB_NAME = "transactions.db";
    private static final String CONNECTION_STRING =
            "jdbc:sqlite:E:\\Documents\\Coding\\Projects\\Bank Application\\Databases\\" + DB_NAME;
    private static final String TABLE_TRANSACTIONS = "Transactions";
    private static final String COLUMN_ACCOUNT_NUMBER = "AccountNumber";
    private static final String COLUMN_TRANSACTION_DETAIL = "TransactionDetail";
    private static final String COLUMN_TRANSACTION_AMOUNT = "TransactionAmount";
    private static final String COLUMN_TO_ACCOUNT = "ToAccount";
    private static final String COLUMN_FROM_ACCOUNT = "FromAccount";
    private static final String COLUMN_TRANSACTION_DATE = "TransactionDate";

    private static Connection conn;
    private static Statement statement;

    /*
    Transaction details different types:
    withdrawal
    deposit
    internal transfer
    external transfer
    account closure
    initial deposit
     */

    //create connection to DB
    static {
        try{
            conn = DriverManager.getConnection(CONNECTION_STRING);
            statement = conn.createStatement();
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.out.println("Error connecting to transaction DB. Please check transactionDB class!");
        }
    }

    //creates db table if it doesn't already exist
    static {
        try {
            statement.execute("CREATE TABLE IF NOT EXISTS " + TABLE_TRANSACTIONS +
                    " (" + COLUMN_ACCOUNT_NUMBER + " INTEGER, " +
                    COLUMN_TRANSACTION_DETAIL + " TEXT, " +
                    COLUMN_TRANSACTION_DATE + " TEXT, " +
                    COLUMN_TRANSACTION_AMOUNT + " TEXT, " +
                    COLUMN_TO_ACCOUNT + " TEXT, " +
                    COLUMN_FROM_ACCOUNT + " TEXT ) ");
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.out.println("Error creating transactions DB table. Please see TransactionDB class.");
        }
    }

    public void createWithdrawalTransaction(int accountNumber, String transactionDetail, String transactionAmount, String fromAccount){

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String transactionDate = dateFormat.format(Date.valueOf(LocalDate.now()));

        try {
            statement.execute("INSERT INTO " + TABLE_TRANSACTIONS +
                    " (" + COLUMN_ACCOUNT_NUMBER + ", " +
                    COLUMN_TRANSACTION_DETAIL + ", " +
                    COLUMN_TRANSACTION_DATE + ", " +
                    COLUMN_TRANSACTION_AMOUNT + ", " +
                    COLUMN_TO_ACCOUNT + ", " +
                    COLUMN_FROM_ACCOUNT + ") " +
                    "VALUES('" +
                    accountNumber + "', '" +
                    transactionDetail + "', '" +
                    transactionDate + "', '" +
                    transactionAmount + "', '-', '" +
                    fromAccount + "')");
        } catch (SQLException exception) {
            System.out.println("Error when adding new transaction to transaction DB" +
                    ". Please see createTransaction() in TransactionDB class.");
            exception.printStackTrace();
        }
    }

    //used for deposit
    public void createDepositTransaction(int accountNumber, String transactionDetail, String transactionAmount,
                                  String toAccount){

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String transactionDate = dateFormat.format(Date.valueOf(LocalDate.now()));

        try {
            statement.execute("INSERT INTO " + TABLE_TRANSACTIONS +
                    " (" + COLUMN_ACCOUNT_NUMBER + ", " +
                    COLUMN_TRANSACTION_DETAIL + ", " +
                    COLUMN_TRANSACTION_DATE + ", " +
                    COLUMN_TRANSACTION_AMOUNT + ", " +
                    COLUMN_TO_ACCOUNT + ", " +
                    COLUMN_FROM_ACCOUNT + ") " +
                    "VALUES('" +
                    accountNumber + "', '" +
                    transactionDetail + "', '" +
                    transactionDate + "', '" +
                    transactionAmount + "', '" +
                    toAccount + "', '-')");
        } catch (SQLException exception) {
            System.out.println("Error when adding new transaction to transaction DB" +
                    ". Please see createTransaction() in TransactionDB class.");
            exception.printStackTrace();
        }
    }

    //used for transfers
    public void createTransferTransaction(int accountNumber, String transactionDetail, String transactionAmount,
                                  String toAccount, String fromAccount){

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String transactionDate = dateFormat.format(Date.valueOf(LocalDate.now()));

        try {
            statement.execute("INSERT INTO " + TABLE_TRANSACTIONS +
                    " (" + COLUMN_ACCOUNT_NUMBER + ", " +
                    COLUMN_TRANSACTION_DETAIL + ", " +
                    COLUMN_TRANSACTION_DATE + ", " +
                    COLUMN_TRANSACTION_AMOUNT + ", " +
                    COLUMN_TO_ACCOUNT + ", " +
                    COLUMN_FROM_ACCOUNT + ") " +
                    "VALUES('" +
                    accountNumber + "', '" +
                    transactionDetail + "', '" +
                    transactionDate + "', '" +
                    transactionAmount + "', '" +
                    toAccount + "', '" +
                    fromAccount + "')");
        } catch (SQLException exception) {
            System.out.println("Error when adding new transaction to transaction DB" +
                    ". Please see createTransaction() in TransactionDB class.");
            exception.printStackTrace();
        }
    }

    //print account transactions for one account
    public void printAccountTransactions(int accountNumber){
        try{
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM " + TABLE_TRANSACTIONS + " WHERE " +
                    COLUMN_ACCOUNT_NUMBER + " ='" + accountNumber + "';");
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
            System.out.println("Error when printing all account transactions.");
            exception.printStackTrace();
        }
    }

    //print account transactions for all accounts
    public void printCustomerTransactions(String custSSN){
        DatabaseManager databaseManager = new DatabaseManager();
        ArrayList<Integer> accountNumbers = databaseManager.getAllCustAccountNumber(custSSN);
        for (int i = 0; i<accountNumbers.size(); i++){
            printAccountTransactions(accountNumbers.get(i));
        }

    }

    //WARNING!!! Prints all transactions from every account
    public void printAllCustomersAllAccounts(){
        try{
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM " + TABLE_TRANSACTIONS);

            ResultSetMetaData resultData = result.getMetaData();
            int columnCount = resultData.getColumnCount();

            //iterate through columns and print each value
            System.out.println();
            for(int i = 1; i <=columnCount; i++){
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
            System.out.println("Error when printing all transaction databases contents");
            exception.printStackTrace();
        }
    }

    public void deleteTable() {
        try {
            statement.execute("DROP TABLE " + TABLE_TRANSACTIONS);
        } catch (SQLException exception) {
            System.out.println("error when deleting transaction DB table.");
            exception.printStackTrace();
        }
    }

}
