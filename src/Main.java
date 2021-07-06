import Database.AccountDB;
import Database.CustomerDB;

public class Main {

    public static void main(String[] args) {
        /**
         * ui manager
         * account
         * customer
         * transactions
         *
         */

        //Troubleshooting section Begin


        AccountDB accountDB = new AccountDB();
        accountDB.printAllAccountsAllData();
//        accountDB.deleteTable();

        CustomerDB customerDB = new CustomerDB();
//        customerDB.createCustomer("sample","sample","sample","sample","sample");
        customerDB.printAllCustomersAllData();
//        customerDB.deleteAllDataEntries();




        //Troubleshooting section ends




        UIManger uiManger = new UIManger();
        uiManger.displayInitialMenu();
    }

}
