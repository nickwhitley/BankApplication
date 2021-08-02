package Tests;

import Database.AccountDB;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

class AccountDBTest {

    @Test
    void withdrawFunds(){
        AccountDB accountDB = new AccountDB();
        BigDecimal amount = new BigDecimal(500);
        accountDB.withdrawFunds("735726792", amount);

    }

    @Test
    void depositFunds() {
        AccountDB accountDB = new AccountDB();
        BigDecimal amount = new BigDecimal(500);
        accountDB.depositFunds("735726792", amount);
    }

    @Test
    void getAllCustAccountNumbers() {
        AccountDB accountDB = new AccountDB();
        ArrayList<Integer> accountNumbers = accountDB.getAllCustAccountNumbers("222222222");
        for (Integer accountNumber : accountNumbers) {
            System.out.println(accountNumber);
        }
    }

    @Test
    void getAccountBalance() {
        AccountDB accountDB = new AccountDB();
        String accountBalance = accountDB.getAccountBalance("735726792");
        System.out.println(accountBalance);
    }
}