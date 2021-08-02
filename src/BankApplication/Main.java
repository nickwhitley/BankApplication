package BankApplication;

import Database.AccountDB;
import Database.CustomerDB;
import Database.TransactionDB;

public class Main {

    public static void main(String[] args) {

        //Troubleshooting section Begin


        AccountDB accountDB = new AccountDB();
        accountDB.printAllAccountsAllData();
//        accountDB.deleteTable();

        CustomerDB customerDB = new CustomerDB();
//        customerDB.createCustomer("sample","sample","sample","sample","sample");
        customerDB.printAllCustomersAllData();
//        customerDB.deleteAllDataEntries();

        TransactionDB transactionDB = new TransactionDB();
        transactionDB.printAllCustomersAllAccounts();
//        transactionDB.deleteTable();




        //Troubleshooting section ends




        UIManger uiManger = new UIManger();
        uiManger.displaySplashScreen();
        uiManger.displayInitialMenu();
    }

}
