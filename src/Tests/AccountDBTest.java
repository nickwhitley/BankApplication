package Tests;

import Database.AccountDB;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

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
}