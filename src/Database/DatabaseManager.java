package Database;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class DatabaseManager {

    //need account DB and customer DB
    //manage input formatting in this class
    AccountDB accountDB = new AccountDB();
    CustomerDB customerDB = new CustomerDB();
    TransactionDB transactionDB = new TransactionDB();

    public void createNewAccount(int accountNumber, String accountType, BigDecimal initialDeposit,
                                 String custFirstName, String custLastName, String custSSN) {
        accountDB.createAccount(accountNumber, accountType, convertDepositIntoString(initialDeposit),
                                custFirstName, custLastName, custSSN);
    }

    public void createNewCustomer(String custFirstName, String custLastName, String custSSN,
                                  String onlineUsername, String onlinePassword) {
        customerDB.createCustomer(custFirstName, custLastName, custSSN, onlineUsername, onlinePassword);
    }

    public boolean isCustSSNInDatabase(String custSSN){
        return customerDB.isCustSSNInDatabase(custSSN);
    }

    public boolean isCustUsernameInDatabase(String custUsername) {
        return customerDB.isCustUsernameInDatabase(custUsername);
    }

    private String convertDepositIntoString(BigDecimal transactionAmount){
        BigDecimal deposit = new BigDecimal(String.valueOf(transactionAmount));
        NumberFormat USFormat = NumberFormat.getCurrencyInstance(Locale.US);

        return USFormat.format(deposit);
    }

    public void commitTransfer(String fromAccount, String toAccount, BigDecimal transferAmount){

        accountDB.withdrawFunds(fromAccount, transferAmount);
        accountDB.depositFunds(toAccount, transferAmount);

    }

    public void commitDeposit(String toAccount, BigDecimal depositAmount){
        accountDB.depositFunds(toAccount, depositAmount);
    }

    public void commitWithdrawal(String fromAccount, BigDecimal withdrawAmount){
        accountDB.withdrawFunds(fromAccount, withdrawAmount);
    }

    public String getCustomerFullName(String custSSN){
        String firstName = customerDB.getCustomerFirstName(custSSN);
        String lastName = customerDB.getCustomerLastName(custSSN);
        String fullName = firstName + " " + lastName;
        return fullName;
    }

    public String getCustomerFirstName(String custSSN){
        return customerDB.getCustomerFirstName(custSSN);
    }

    public String getCustomerLastName(String custSSN){
        return customerDB.getCustomerLastName(custSSN);
    }

    public String getCustomerPassword(String username) {
        return customerDB.getCustomerPassword(username);
    }

    public String getCustomerSSN(String username) {
        return customerDB.getCustomerSSN(username);
    }

    public String getOnlineUsername(String custSSN){
        return customerDB.getOnlineUsername(custSSN);
    }

    public void printAllCustTransactions(String custSSN){
        transactionDB.printCustomerTransactions(custSSN);
    }

    public void printAccountTransactions(int accountNumber){
        transactionDB.printAccountTransactions(accountNumber);
    }

    public void setCustomerFirstName(String custSSN, String newFirstName){
        customerDB.setCustomerFirstName(custSSN, newFirstName);
    }

    public void setCustomerLastName(String custSSN, String newLastName){
        customerDB.setCustomerLastName(custSSN, newLastName);
    }

    public void setOnlineUsername(String custSSN, String newUsername){
        customerDB.setOnlineUsername(custSSN, newUsername);
    }

    public void setOnlinePassword(String custSSN, String newPassword){
        customerDB.setOnlinePassword(custSSN, newPassword);
    }

    public ArrayList<Integer> getAllCustAccountNumber(String custSSN){
        ArrayList<Integer> accountNumbers = accountDB.getAllCustAccountNumbers(custSSN);
        return accountNumbers;
    }

    public String getAccountBalanceForTransfer(String accountNumber){
        String accountBalanceUnformatted = accountDB.getAccountBalance(accountNumber);
        String accountBalanceFormatted = accountBalanceUnformatted.replaceAll("[^0-9.]", "");
        return accountBalanceFormatted;
    }

    public BigDecimal getAccountBalance(int accountNumber){
        //convert account num to String for DB search
        BigDecimal accountBalance = new BigDecimal(accountDB.getAccountBalance(Integer.toString(accountNumber)).replaceAll("[^0-9.]", ""));
        return accountBalance;
    }

    public void closeAccount(int accountNumber){
        accountDB.closeAccount(accountNumber);
    }

    public void getAllAccountBalances(String custSSN) {
        accountDB.printAllAccountBalances(custSSN);
    }

    public boolean checkIfCustOwnsAccountNumber(String custSSN, String accountNumber) {
        return accountDB.checkIfCustOwnsAccountNumber(custSSN, accountNumber);
    }

    public boolean doesCustHaveOpenAccounts(String custSSN) {
        return accountDB.doesCustHaveOpenAccount(custSSN);
    }
}
