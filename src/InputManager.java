import Database.DatabaseManager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.InputMismatchException;
import java.util.Scanner;

/*
Input Manager will be used for the following:
- Verify input is in correct format.
- Handle all inputs using the scanner object.
 */

public class InputManager {
    private final Scanner scanner = new Scanner(System.in);
    DatabaseManager databaseManager = new DatabaseManager();


    public boolean enterKeyHit() {
        String tempInput = scanner.nextLine();
        if (tempInput.isEmpty()) {
            return true;
        }
        System.out.println("Just press enter moron!");
        return false;
    }

    public int menuInput() {
        return scanner.nextInt();
    }

    public String getName() {

        String name;
        do {
            name = scanner.next();
            scanner.nextLine();
        } while (!validateName(name));

        return name;
    }

    public BigDecimal getDeposit() {
        BigDecimal deposit;
        try {
            do {
                deposit = scanner.nextBigDecimal().setScale(2, RoundingMode.UP);
            } while (!validateDeposit(deposit));

        } catch (InputMismatchException exception) {
            System.out.println("Deposit amount can only contain numbers and '.'. Please try again: ");
            scanner.nextLine();
            return getDeposit();
        }

        return deposit;
    }

    public BigDecimal getWithdrawal(String accountNumber){
        BigDecimal withdrawAmount;
        try{
            do {
                withdrawAmount = scanner.nextBigDecimal().setScale(2, RoundingMode.UP);
            } while (!validateWithdrawal(withdrawAmount, accountNumber));
        } catch (InputMismatchException exception){
            System.out.println("Withdrawal amount can only contain numbers and '.'. Please try again: ");
            scanner.nextLine();
            return getDeposit();
        }

        return withdrawAmount;
    }

    private boolean validateWithdrawal(BigDecimal withdrawAmount, String fromAccount) {
        BigDecimal maxAmount = new BigDecimal(databaseManager.getAccountBalanceForTransfer(fromAccount));
        BigDecimal minAmount = new BigDecimal(0);

        if (withdrawAmount.compareTo(maxAmount) == 1) {
            System.out.println("You don't have enough in your account to cover this transfer. \n" +
                    "Please enter a new amount under " + maxAmount + ":");
            return false;
        }
        if (withdrawAmount.compareTo(minAmount) == -1) {
            System.out.println("Hey guy, you have to transfer at least $0.01, don't be cheap. Please try again: ");
            return false;
        }
        return true;
    }

    public BigDecimal getTransferAmount(String fromAccount){
        BigDecimal transferAmount;
        try {
            do {
                transferAmount = scanner.nextBigDecimal().setScale(2, RoundingMode.UP);
            } while (!validateTransfer(transferAmount, fromAccount));

        } catch (InputMismatchException exception) {
            System.out.println("Transfer amount can only contain numbers and '.'. Please try again: ");
            scanner.nextLine();
            return getDeposit();
        }

        return transferAmount;
    }

    private boolean validateTransfer(BigDecimal transferAmount, String fromAccount){
//        BigDecimal maxAmount = new BigDecimal(10000000000L);
        BigDecimal maxAmount = new BigDecimal(databaseManager.getAccountBalanceForTransfer(fromAccount));
        BigDecimal minAmount = new BigDecimal("0.01");

        if (transferAmount.compareTo(maxAmount) == 1) {
            System.out.println("You don't have enough in your account to cover this transfer. \n" +
                    "Please enter a new amount under " + maxAmount + ":");
            return false;
        }
        if (transferAmount.compareTo(minAmount) == -1) {
            System.out.println("Hey guy, you have to transfer at least $0.01, don't be cheap. Please try again: ");
            return false;
        }

        return true;
    }

    public String getCustSSN() {
        String returnSSN;

        do {
            returnSSN = scanner.nextLine();
        } while (!validateCustSSN(returnSSN));


        return returnSSN;
    }

    private boolean validateCustSSN(String input) {
        if (!input.matches("[0-9]+")) {
            System.out.println("SSN can only contain 9 numbers. Please try again: ");
            return false;
        }
        if (input.length() == 9) {
            return true;
        } else {
            System.out.println("SSN can only contain 9 numbers. Please try again: ");
            return false;
        }
    }

    private boolean validateName(String stringToCheck) {
        if (!stringToCheck.matches("[a-zA-Z]+")) {
            System.out.println("Name can not contain an special characters. Please try again: ");
            return false;
        }
        return true;
    }

    private boolean validateDeposit(BigDecimal deposit) {
        BigDecimal maxDeposit = new BigDecimal(10000000000L);
        BigDecimal minDeposit = new BigDecimal(0);

        if (deposit.compareTo(maxDeposit) == 1) {
            System.out.println("whoa whoa, that's a shit ton of dough nigga, go spread that shit around!\n" +
                    "You can only deposit up to $10,000,000,000. Please try again rich boy or go to Donald's bank!");
            return false;
        }
        if (deposit.compareTo(minDeposit) == -1) {
            System.out.println("Hey guy, you have to deposit at least $0.01, don't be cheap. Please try again: ");
            return false;
        }

        return true;
    }

    public String createCustUsername() {
        String username;
        do{
            username = scanner.next();
            scanner.nextLine();
        } while (!validateUsernameFormat(username));
//        String username = scanner.nextLine();
//        if (!validateUsernameFormat(username)) {
//            return createCustUsername();
//        }

        return username;
    }

    public String createCustPassword() {
        String password = scanner.nextLine();
        if (!validatePasswordFormat(password)) {
            return createCustPassword();
        }

        System.out.println("Please verify your password by entering it below: ");
        String passwordCheck = scanner.nextLine();

        if (!passwordCheck.equals(password)) {
            System.out.println("Passwords do not match. Please try again: ");
            return createCustPassword();
        }

        System.out.println("Passwords match.");
        return password;
    }

    public String getUsernameForLogin() {
        ///TODO Does not throw username is empty error due to using "username = scanner.next();"
        boolean passedThrough = false;
        String username;
        do {
            username = scanner.next();
            scanner.nextLine();
            if (username.equalsIgnoreCase("cancel")) {
                return username;
            }
            if (!validateUsernameFormat(username)) {
                return getUsernameForLogin();
            } else if (!doesUsernameExist(username)) {
                System.out.println("Username does not exist. Please try again or enter cancel to return to initial menu: ");
                return getUsernameForLogin();
            }
            passedThrough = true;
        }while (!passedThrough);
        return username;
    }

    public String getPasswordForLogin(String username){
        boolean passedThrough = false;
        String password;
        do {

            password = scanner.next();
            scanner.nextLine();
            if (!validatePasswordFormat(password)) {
                return getPasswordForLogin(username);
            }
            if (!isPasswordCorrect(password, username)) {
                System.out.println("Incorrect password, please try again.");
                return getPasswordForLogin(username);
            }
            passedThrough = true;
        } while (!passedThrough);
        return password;
    }

    public String getPasswordForChange(String custSSN){
        boolean passedThrough = false;
        String password;
        do {

            password = scanner.nextLine();
            if (!validatePasswordFormat(password)) {
                return getPasswordForChange(custSSN);
            }
            if (!isPasswordCorrect(password, databaseManager.getOnlineUsername(custSSN))) {
                System.out.println("Incorrect password, please try again.");
                return getPasswordForChange(custSSN);
            }
            passedThrough = true;
        } while (!passedThrough);
        return password;
    }

    public String getInternalTransferAccountNumber(String custSSN){

        String transferFromAccountNumber;

        boolean passedThrough = false;
        do {
            transferFromAccountNumber = scanner.next();
            scanner.nextLine();
            if (transferFromAccountNumber.length() != 9) {
                System.out.println("Invalid input, account number must only contain 9 digits. Please try again.");
                return getInternalTransferAccountNumber(custSSN);
            }
            if (!transferFromAccountNumber.matches("[0-9]+")) {
                System.out.println("Invalid input, account number must only contain 9 digits. Please try again.");
                return getInternalTransferAccountNumber(custSSN);
            }
            if (!databaseManager.checkIfCustOwnsAccountNumber(custSSN, transferFromAccountNumber)) {
                System.out.println("Incorrect account number, please enter one of your account numbers above.");
                return getInternalTransferAccountNumber(custSSN);
            }
            passedThrough = true;
        } while (!passedThrough);


        return transferFromAccountNumber;
    }

    private boolean isPasswordCorrect(String password, String usernameToCheck) {
        String returnedPassword = databaseManager.getCustomerPassword(usernameToCheck);
        if (!returnedPassword.equals(password)) {
            return false;
        } else if (returnedPassword.equals(password)){
            System.out.println("Password is correct.");
            return true;
        }
        System.out.println("Something went wrong in verifyPasswordIsCorrect() in InputManager.");
        return false;
    }

    public boolean doesUsernameExist(String usernameToCheck){
        return databaseManager.isCustUsernameInDatabase(usernameToCheck);
    }

    private boolean validateUsernameFormat(String usernameToCheck){
        if (usernameToCheck.length() > 20) {
            System.out.println("Username can be no loner than 20 characters, please try again: ");
            return false;
        } else if(usernameToCheck.isEmpty()) {
            System.out.println("Username can't be empty dipshit, please try again: ");
            return false;
        }
        return true;
    }

    private boolean validatePasswordFormat(String passwordToCheck) {
        if ((passwordToCheck.length() < 8) || (passwordToCheck.length() > 20)) {
            System.out.println("Password must be between 8 to 20 characters in length. Please try again: ");
            return false;
        } else if(!passwordToCheck.matches("[a-zA-Z0-9]+")) {
            System.out.println("password can not contain special characters, only letters and numbers. Please try again: ");
            return false;
        }
        return true;
    }
}
