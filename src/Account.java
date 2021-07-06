import Database.DatabaseManager;

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

    public void getAccountBalance() {

    }

    public void commitTransfer(String fromAccount, String toAccount, BigDecimal transferAmount){
        databaseManager.commitTransfer(fromAccount, toAccount, transferAmount);
    }

    public void commitDeposit(String accountNumber, BigDecimal depositAmount){
        databaseManager.commitDeposit(accountNumber, depositAmount);
    }

    public void commitWithdrawal(String accountNumber, BigDecimal withdrawAmount){
        databaseManager.commitWithdrawal(accountNumber, withdrawAmount);
    }

    public void printAllBalances(String custSSN) {
        databaseManager.getAllAccountBalances(custSSN);
    }

    private int randomAccountNumGenerator() {
        int rangeMin = 100000000;
        int rangeMax = 999999999;
        int randomAccountNum = (int)Math.floor(Math.random()*(rangeMax-rangeMin+1)+rangeMin);

        return randomAccountNum;
    }

}
