package BankApplication;

import Database.DatabaseManager;
import Database.TransactionDB;

import java.math.BigDecimal;
import java.util.Random;

public class Account {
    private int accountNumber;
    private String custFirstName;
    private String custLastName;
    private String accountType;
    private String accountBalance;
    private BigDecimal initialDeposit;
    private Random randomNumGen;
    static Customer customer;

    DatabaseManager databaseManager = new DatabaseManager();
    TransactionDB transactionDB = new TransactionDB();

    public Account(){

    }

    public Account(String custFirstName, String custLastName, String accountType, BigDecimal initialDeposit, String custSSN) {
        this.accountNumber = randomAccountNumGenerator();
        this.custFirstName = custFirstName;
        this.custLastName = custLastName;
        this.accountType = accountType;
        this.initialDeposit = initialDeposit;
        databaseManager.createNewAccount(this.accountNumber, this.accountType, this.initialDeposit, this.custFirstName,
                this.custLastName, custSSN);
    }

    public BigDecimal getAccountBalance(int accountNumber) {
        return databaseManager.getAccountBalance(accountNumber);
    }

    public void printAccountTransactions(int accountNumber){
        databaseManager.printAccountTransactions(accountNumber);
    }

    public void commitTransfer(String fromAccount, String toAccount, BigDecimal transferAmount){
        databaseManager.commitTransfer(fromAccount, toAccount, transferAmount);
        transactionDB.createTransferTransaction(Integer.parseInt(fromAccount), "Internal Transfer", transferAmount.toString(), toAccount, fromAccount);
    }

    public void commitClosingTransfer(String fromAccount, String toAccount){
        String accountBalance = getAccountBalance(Integer.parseInt(fromAccount)).toString();
        transactionDB.createTransferTransaction(Integer.parseInt(fromAccount), "BankApplication.Account Closure Transfer", accountBalance, toAccount, fromAccount);
        databaseManager.commitTransfer(fromAccount, toAccount, getAccountBalance(Integer.parseInt(fromAccount)));

    }

    public void commitClosingWithdrawal(String fromAccount){
        String accountBalance = getAccountBalance(Integer.parseInt(fromAccount)).toString();
        transactionDB.createWithdrawalTransaction(Integer.parseInt(fromAccount), "BankApplication.Account Closing Withdrawal", accountBalance, fromAccount);
        databaseManager.commitWithdrawal(fromAccount, getAccountBalance(Integer.parseInt(fromAccount)));
    }

    public void commitDeposit(String accountNumber, BigDecimal depositAmount){
        databaseManager.commitDeposit(accountNumber, depositAmount);
        transactionDB.createDepositTransaction(Integer.parseInt(accountNumber), "Deposit", depositAmount.toString(), accountNumber);
    }

    public void commitWithdrawal(String accountNumber, BigDecimal withdrawAmount){
        databaseManager.commitWithdrawal(accountNumber, withdrawAmount);
        transactionDB.createWithdrawalTransaction(Integer.parseInt(accountNumber), "Withdrawal", withdrawAmount.toString(), accountNumber);
    }

    public void printAllBalances(String custSSN) {
        databaseManager.getAllAccountBalances(custSSN);
    }

    public void closeAccount(int accountNumber){
        databaseManager.closeAccount(accountNumber);
    }

    public boolean doesCustHaveOpenAccount(String custSSN){
        return databaseManager.doesCustHaveOpenAccounts(custSSN);
    }

    private int randomAccountNumGenerator() {
        int rangeMin = 100000000;
        int rangeMax = 999999999;
        int randomAccountNum = (int)Math.floor(Math.random()*(rangeMax-rangeMin+1)+rangeMin);

        return randomAccountNum;
    }

}
