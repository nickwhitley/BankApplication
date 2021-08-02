package BankApplication;

import Database.DatabaseManager;

import java.math.BigDecimal;
import java.util.ArrayList;

public class UIManger {

    private final InputManager inputManager = new InputManager();
    private final DatabaseManager databaseManager = new DatabaseManager();
    Account account = new Account();
    Customer customer = new Customer();

    public void displaySplashScreen() {
        //maybe center text in screen.
        System.out.println("Welcome to the Better Than Donald's Bank or BTD Bank!\n" +
                "\tPress Enter to continue.");

        if (inputManager.enterKeyHit()) {
            clearScreen();
            displayInitialMenu();
        }

    }

    public void displayInitialMenu() {
        System.out.println("Select an option:\n" +
                "1. Create account.\n" +
                "2. Login to existing account.\n" +
                "3. Exit application.");

        int menuChoice = inputManager.menuInput();
        String goodbyeMessage = "Have a great day!";
        switch (menuChoice) {
            case 1:
                displayCreateOnlineAccountScreen();
                break;
            case 2:
                displayLoginScreen();
                break;
            case 3:
                System.out.println(goodbyeMessage);
                break;
            default:
                System.out.println("Incorrect input, try again.");
                displayInitialMenu();
                break;
        }
    }

    private void displayCreateOnlineAccountScreen() {
        //creates customer object and stores in database
        System.out.println("CREATE ACCOUNT\n" +
                "----------\n" +
                "Please enter the following information.\n" +
                "First name: ");
        String firstName = inputManager.getName();

        System.out.println("Last Name:");
        String lastName = inputManager.getName();

        System.out.println("Social Security Number:");
        String custSSN = inputManager.getCustSSN();
        //check if customer already exists in DB using their SSN
        if (databaseManager.isCustSSNInDatabase(custSSN)) {
            System.out.println("You already have an online account with us. Please login to continue.");
            displayInitialMenu();
        }

        System.out.println("Time to create your online username, usernames can be no longer than 20 characters and can" +
                "contain special characters.\n" +
                "Create your username: ");
        String username = inputManager.createCustUsername();

        System.out.println("Now create your password, this will be used to access your accounts. Passwords must be between 8-20 characters\n" +
                " and can only contain letters and numbers.\n" +
                "Create your password: ");
        String password = inputManager.createCustPassword();
        System.out.println("\n" +
                "- - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\n" +
                "");

        confirmCustomerInfo(firstName, lastName, custSSN, username, password);
    }

    private void confirmCustomerInfo(String firstName, String lastName, String custSSN, String username, String password) {
        System.out.println("Please confirm your information:\n" +
                "First Name: " + firstName + "\n" +
                "Last Name: " + lastName + "\n" +
                "SSN: " + custSSN + "\n" +
                "Username: " + username + "\n" +
                "Password: " + password + "\n");
        boolean infoConfirmed = false;

        do {
            System.out.println("Does this look correct?\n" +
                    "1. Yes, it's all correct.\n" +
                    "2. No, I need to change some info.\n" +
                    "Enter choice: ");
            int choice = inputManager.menuInput();
            switch (choice){
                case 1:
                    System.out.println("BankApplication.Account has been created successfully.");
                    infoConfirmed = true;
                    Customer customer = new Customer(firstName, lastName, custSSN, username, password);
                    displayCustomerAccountScreen(custSSN);
                    break;
                case 2:
                    System.out.println("Information not correct, going back to change.");
                    displayCreateOnlineAccountScreen();
                    break;
                default:
                    System.out.println("Incorrect input, please try again.");
                    break;
            }
        } while(!infoConfirmed);
    }

    private void displayCustomerAccountScreen(String custSSN){
        //custSSN is used to pull all customer info for this screen.
        System.out.println("Hello, " + databaseManager.getCustomerFullName(custSSN));
        System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\n");
        System.out.println("How can we help you today?\n" +
                "");
        //menu for customer options
        System.out.println("1. Open new banking account.\n" +
                "2. Check available balances.\n" +
                "3. Make a transfer.\n" +
                "4. Deposit funds.\n" +
                "5. Withdraw funds.\n" +
                "6. View/Change your information.\n" +
                "7. View transactions.\n" +
                "8. Close banking account.\n" +
                "9. Log out.\n" +
                "Enter choice: ");

        ///todo test if this works
        boolean correctChoice;
        do {
            int menuChoice = inputManager.menuInput();
            correctChoice = true;
            switch (menuChoice) {
                case 1:
                    openBankAccount(custSSN);
                    break;
                case 2:
                    account.printAllBalances(custSSN);
                    displayCustomerAccountScreen(custSSN);
                    break;
                case 3:
                    displayTransferMenu(custSSN);
                    break;
                case 4:
                    displayDepositMenu(custSSN);
                    break;
                case 5:
                    displayWithdrawMenu(custSSN);
                    break;
                case 6:
                    displayViewChangeInfoScreen(custSSN);
                    break;
                case 7:
                    displayTransactionLogScreen(custSSN);
                    break;
                case 8:
                    displayCloseAccountScreen(custSSN);
                    break;
                case 9:
                    System.out.println("Logging you out. See you again soon!");
                    clearScreen();
                    custSSN = null;
                    displayInitialMenu();
                    break;
                default:
                    correctChoice = false;
                    System.out.println("Incorrect option, please try again.");
            }
        } while (!correctChoice);
    }

    private void displayCloseAccountScreen(String custSSN) {

        clearScreen();
        customerAccountCheck(custSSN);

        System.out.println("Close BankApplication.Account:\n");
        System.out.println("Your open accounts:");
        //prints account balances
        account.printAllBalances(custSSN);
        System.out.println();

        //prints all accounts
        ArrayList<Integer> accountNumbers = databaseManager.getAllCustAccountNumber(custSSN);
        for (int i = 0; i < accountNumbers.size(); i++){
            System.out.println((i + 1) + ". " + accountNumbers.get(i));
        }

        System.out.println("Select one of your accounts to close or enter '0' to cancel");
        int menuChoice = inputManager.menuInput();
        if ((menuChoice < 0) || (menuChoice > accountNumbers.size())){
            System.out.println("Incorrect choice, please try again.");
            displayCloseAccountScreen(custSSN);
        }
        if (menuChoice == 0){
            System.out.println("Cancelling and going back to main menu...");
            displayCustomerAccountScreen(custSSN);
        }
        int accountSelected = accountNumbers.get(menuChoice-1);

        ///TODO could simplify code below
        //verifies account selected is correct account before committing
        boolean correctChoice = false;
        int menuChoiceForAccountSelected;
        do {
            System.out.println("You have selected:" + accountSelected);
            System.out.println("Please select an option below:\n" +
                    "1. This is correct, close this account.\n" +
                    "2. This is not correct, choose different account\n" +
                    "3. Cancel account closing.");
            menuChoiceForAccountSelected = inputManager.menuInput();
            if ((menuChoiceForAccountSelected >0) && (menuChoiceForAccountSelected <= 3)){
                correctChoice = true;
            } else{
                System.out.println("Incorrect choice, please try again.\n");
            }
        } while (!correctChoice);

        switch (menuChoiceForAccountSelected){
            case 1:
                ///todo account stills acts like it has a remaining amount
                BigDecimal compareNum = new BigDecimal("0.00");
                if (databaseManager.getAccountBalance(accountSelected).compareTo(compareNum) == 0){
                    //close account
                    account.closeAccount(accountSelected);
                    System.out.println("BankApplication.Account: " + accountSelected + " successfully closed.");
                    displayCustomerAccountScreen(custSSN);
                } else {displayCloseAccountRemainingBalanceScreen(accountSelected, custSSN);}
                break;
            case 2:
                displayCloseAccountScreen(custSSN);
                break;
            case 3:
                System.out.println("Cancelling and going back to main menu.");
                displayCustomerAccountScreen(custSSN);
                break;
        }


    }

    private void displayCloseAccountRemainingBalanceScreen(int accountSelected, String custSSN) {
        System.out.println("BankApplication.Account: " + accountSelected + " has a remaining balance of $" + account.getAccountBalance(accountSelected));

        boolean correctChoice = false;
        int menuChoiceForBalance;
        do {
            System.out.println("What would you like to do with the remaining balance? Select an option below:\n" +
                    "0. Cancel.\n" +
                    "1. Transfer to different account.\n" +
                    "2. Withdraw funds\n");
            menuChoiceForBalance = inputManager.menuInput();
            if ((menuChoiceForBalance >= 0) && (menuChoiceForBalance <= 2)){
                correctChoice = true;
            } else{
                System.out.println("Incorrect choice, please try again.\n");
            }
        } while (!correctChoice);

        switch (menuChoiceForBalance) {
            case 0:
                System.out.println("Cancelling account closure.");
                displayCustomerAccountScreen(custSSN);
                break;
            case 1:
                System.out.println("Transfer to a different account:");
                displayAccountClosureTransferMenu(accountSelected, custSSN);
                break;
            case 2:
                System.out.println("Withdraw the funds.");
                account.commitClosingWithdrawal(Integer.toString(accountSelected));
                break;
        }
    }

    private void displayAccountClosureTransferMenu(int accountBeingClosed, String custSSN) {
        ArrayList<Integer> accountNumbers = databaseManager.getAllCustAccountNumber(custSSN);
        accountNumbers.remove(accountNumbers.indexOf(accountBeingClosed));
        System.out.println("Select which account you would like the remaining funds to be transferred to:");
        System.out.println("0. Cancel and go back to main menu.");
        for (int i = 0; i < accountNumbers.size(); i++){
            System.out.println((i+1) + ". " + accountNumbers.get(i));
        }
        int menuChoice = inputManager.menuInput();
        boolean correctChoice = (menuChoice >= 0) && (menuChoice <= accountNumbers.size());
        if (!correctChoice){
            System.out.println("Incorrect choice, please try again.");
            displayAccountClosureTransferMenu(accountBeingClosed, custSSN);
        } else {
            if (menuChoice == 0){
                System.out.println("Going back to main menu.");
                displayCustomerAccountScreen(custSSN);
            }
            account.commitClosingTransfer(Integer.toString(accountBeingClosed),accountNumbers.get(menuChoice-1).toString());
        }

    }

    private void displayTransactionLogScreen(String custSSN){
        clearScreen();
        customerAccountCheck(custSSN);

        System.out.println("Transaction Log:");
        System.out.println("Please select an option below:\n" +
                "1. View transactions for specific account.\n" +
                "2. View all transactions.\n" +
                "3. Return to account menu.\n");

            int menuChoice = inputManager.menuInput();

            switch (menuChoice) {
                case 1:
                    displaySelectAccountForTransaction(custSSN);
                    break;
                case 2:
                    customer.printAllAccountTransactions(custSSN);
                    displayCustomerAccountScreen(custSSN);
                    break;
                case 3:
                    displayCustomerAccountScreen(custSSN);
                    break;
                default:
                    System.out.println("Incorrect choice, please try again: ");
                    displayTransactionLogScreen(custSSN);
                    break;
            }
    }

    private void displaySelectAccountForTransaction(String custSSN) {
        System.out.println("Please select one of your accounts or enter '0' to cancel:\n");
        ArrayList<Integer> accountNumbers = databaseManager.getAllCustAccountNumber(custSSN);
        for (int i = 0; i < accountNumbers.size(); i++){
            System.out.println((i + 1) + ". " + accountNumbers.get(i));
        }

        int menuChoice = inputManager.menuInput();
        boolean correctChoice = (menuChoice >= 0) && (menuChoice <= accountNumbers.size());
        if (!correctChoice){
            System.out.println("Incorrect choice, please try again.");
            displaySelectAccountForTransaction(custSSN);
        } else {
            if (menuChoice == 0){
                System.out.println("Going back to main menu.");
                displayCustomerAccountScreen(custSSN);
            }
            account.printAccountTransactions(accountNumbers.get(menuChoice -1));
        }

        displayCustomerAccountScreen(custSSN);
    }

    private void displayViewChangeInfoScreen(String custSSN){
        System.out.println("View/Change Information:\n");
        //info to display first name, last name, username, and password(*******)
        String firstName = customer.getFirstName(custSSN);
        String lastName = customer.getLastName(custSSN);
        String hiddenPassword = "***********";
        String username = customer.getOnlineUsername(custSSN);

        System.out.println("1. First Name: " + firstName +
                "\n2. Last Name: " + lastName +
                "\n3. Username: " + username +
                "\n4. Password: " + hiddenPassword +
                "\n\n Please enter the corresponding number for which field you would like to change, press '5' to change all fields, or press '0' to go back without change\n" +
                "Enter your choice here:");
        boolean correctChoice = false;
        do {
            int menuChoice = inputManager.menuInput();
            if ((menuChoice > 0) && (menuChoice < 5)) {
                correctChoice = true;
            }
            switch (menuChoice) {
                case 0:
                    System.out.println("Cancelling information change...");
                    displayCustomerAccountScreen(custSSN);
                    break;
                case 1:
                    displayFirstNameChangeScreen(custSSN);
                    break;
                case 2:
                    displayLastNameChangeScreen(custSSN);
                    break;
                case 3:
                    displayUsernameChangeScreen(custSSN);
                    break;
                case 4:
                    displayPasswordChangeScreen(custSSN);
                    break;
                case 5:
                    displayAllInfoChangeScreen(custSSN);
                    break;
                default:
                    System.out.println("Incorrect choice, please try again: ");
                    break;
            }
        } while (!correctChoice);
    }

    private void displayFirstNameChangeScreen(String custSSN) {
        System.out.println("Change first name:\n");
        System.out.println("Current first name: " + customer.getFirstName(custSSN));
        System.out.println("Please enter your new first name: ");
        String newFirstName = inputManager.getName();
        System.out.println("\n" +
                "You are changing your first name from \"" + customer.getFirstName(custSSN) +
                "\" to \"" + newFirstName + "\". Is this correct? Select option below.");
        System.out.println("1. Yes this is correct, commit change.\n" +
                "2. No I need to change it.\n" +
                "3. Cancel name change and go back to menu.");
        boolean correctChoice = false;
        do {
            int menuChoice = inputManager.menuInput();
            if ((menuChoice > 0) && (menuChoice < 3)) {
                correctChoice = true;
            }
            switch (menuChoice) {
                case 1:
                    customer.setFirstName(custSSN, newFirstName);
                    System.out.println("First name changed, returning to change info screen.");
                    displayViewChangeInfoScreen(custSSN);
                    break;
                case 2:
                    displayFirstNameChangeScreen(custSSN);
                    break;
                case 3:
                    System.out.println("Cancelling....");
                    displayCustomerAccountScreen(custSSN);
                    break;
                default:
                    System.out.println("Incorrect choice, please try again: ");
                    break;
            }
        } while (!correctChoice);
    }

    private void displayLastNameChangeScreen(String custSSN) {
        clearScreen();
        System.out.println("Change last name:\n");
        System.out.println("Current last name: " + customer.getLastName(custSSN));
        System.out.println("Please enter your new Last name: ");
        String newLastName = inputManager.getName();
        System.out.println("\n" +
                "You are changing your last name from \"" + customer.getLastName(custSSN) +
                "\" to \"" + newLastName + "\". Is this correct? Select option below.");
        System.out.println("1. Yes this is correct, commit change.\n" +
                "2. No I need to change it.\n" +
                "3. Cancel name change and go back to menu.");
        boolean correctChoice = false;
        do {
            int menuChoice = inputManager.menuInput();
            if ((menuChoice > 0) && (menuChoice < 3)) {
                correctChoice = true;
            }
            switch (menuChoice) {
                case 1:
                    customer.setLastName(custSSN, newLastName);
                    System.out.println("Last name changed, returning to change info screen.");
                    displayViewChangeInfoScreen(custSSN);
                    break;
                case 2:
                    displayLastNameChangeScreen(custSSN);
                    break;
                case 3:
                    System.out.println("Cancelling....");
                    displayCustomerAccountScreen(custSSN);
                    break;
                default:
                    System.out.println("Incorrect choice, please try again: ");
                    break;
            }
        } while (!correctChoice);
    }

    private void displayUsernameChangeScreen(String custSSN) {
        clearScreen();
        System.out.println("Change username:\n");
        System.out.println("Current username: " + customer.getOnlineUsername(custSSN));
        System.out.println("Please enter your new username: ");
        String newUsername = inputManager.createCustUsername();
        System.out.println("\n" +
                "You are changing your username from \"" + customer.getOnlineUsername(custSSN) +
                "\" to \"" + newUsername + "\". Is this correct? Select option below.");
        System.out.println("1. Yes this is correct, commit change.\n" +
                "2. No I need to change it.\n" +
                "3. Cancel name change and go back to menu.");
        boolean correctChoice = false;
        do {
            int menuChoice = inputManager.menuInput();
            if ((menuChoice > 0) && (menuChoice < 3)) {
                correctChoice = true;
            }
            switch (menuChoice) {
                case 1:
                    customer.setOnlineUsername(custSSN, newUsername);
                    System.out.println("Username changed, returning to change info screen.");
                    displayViewChangeInfoScreen(custSSN);
                    break;
                case 2:
                    displayUsernameChangeScreen(custSSN);
                    break;
                case 3:
                    System.out.println("Cancelling....");
                    displayCustomerAccountScreen(custSSN);
                    break;
                default:
                    System.out.println("Incorrect choice, please try again: ");
                    break;
            }
        } while (!correctChoice);
    }

    private void displayPasswordChangeScreen(String custSSN) {
        clearScreen();
        System.out.println("Password Change: \n" +
                "");
        System.out.println("Please enter your current password: ");
        String currentPassword = inputManager.getPasswordForChange(custSSN);
        System.out.println("Current Password: " + currentPassword);
        System.out.println("Please enter your new password: ");
        String newPassword = inputManager.createCustPassword();
        customer.setOnlinePassword(custSSN, newPassword);
        System.out.println("Password changed successfully. Returning to main menu...");
        displayCustomerAccountScreen(custSSN);

    }

    private void displayAllInfoChangeScreen(String custSSN) {
        clearScreen();
        System.out.println("Changing all information:\n");
        System.out.println("Current first name: " + customer.getFirstName(custSSN) + "\n" +
                "Please enter new first name:");
        String newFirstName = inputManager.getName();
        System.out.println("- - - - - -");
        System.out.println("Current last name: " + customer.getLastName(custSSN) + "\n" +
                "Please enter new last name:");
        String newLastName = inputManager.getName();
        System.out.println("- - - - - -");
        System.out.println("Current username: " + customer.getOnlineUsername(custSSN) + "\n" +
                "Please enter new username:");
        String newUsername = inputManager.createCustUsername();
        System.out.println("- - - - - -");
        System.out.println("Please enter your current password: ");
        String currentPassword = inputManager.getPasswordForChange(custSSN);
        System.out.println("Current Password: " + currentPassword);
        System.out.println("Please enter your new password: ");
        String newPassword = inputManager.createCustPassword();
        System.out.println("- - - - - -");
        System.out.println("Please confirm information below: \n" +
                "New first name: " + newFirstName + "\n" +
                "New last name: " + newLastName + "\n" +
                "New username: " + newUsername + "\n" +
                "New password: " + newPassword + "\n");

        System.out.println("1. Yes this is correct, commit change.\n" +
                "2. No I need to change it.\n" +
                "3. Cancel name change and go back to menu.");
        boolean correctChoice = false;
        do {
            int menuChoice = inputManager.menuInput();
            if ((menuChoice > 0) && (menuChoice < 3)) {
                correctChoice = true;
            }
            switch (menuChoice) {
                case 1:
                    customer.setAllInfo(custSSN, newFirstName, newLastName, newUsername, newPassword);
                    System.out.println("Information change successful.");
                    displayViewChangeInfoScreen(custSSN);
                    break;
                case 2:
                    displayAllInfoChangeScreen(custSSN);
                    break;
                case 3:
                    System.out.println("Cancelling....");
                    displayCustomerAccountScreen(custSSN);
                    break;
                default:
                    System.out.println("Incorrect choice, please try again: ");
                    break;
            }
        } while (!correctChoice);


    }

    private void displayWithdrawMenu(String custSSN) {
        customerAccountCheck(custSSN);
        System.out.println("Withdraw:");
        account.printAllBalances(custSSN);
        System.out.println("Please enter the account number you would like to withdraw from:");
        String accountNumber = inputManager.getInternalTransferAccountNumber(custSSN);
        System.out.println("How much would you like to withdraw:");
        BigDecimal withdrawAmount = inputManager.getWithdrawal(accountNumber);

        System.out.println("Please confirm all information for the withdrawal is correct.\n" +
                "Withdraw from: " + accountNumber + "\n" +
                "Withdraw amount: " + withdrawAmount);
        System.out.println("\nPlease select an option below:\n" +
                "1. Information is correct, commit withdrawal.\n" +
                "2. Information is not correct, reenter information.\n" +
                "3. Cancel withdrawal and go back.");
        boolean correctChoice = false;
        do {
            int menuChoice = inputManager.menuInput();
            if ((menuChoice == 1) || (menuChoice == 2) || (menuChoice == 3)) {
                correctChoice = true;
            }
            switch (menuChoice) {
                case 1:
                    account.commitWithdrawal(accountNumber, withdrawAmount);
                    System.out.println("Withdrawal successful.");
                    displayCustomerAccountScreen(custSSN);
                    break;
                case 2:
                    System.out.println("Please reenter correct information.");
                    displayWithdrawMenu(custSSN);
                    break;
                case 3:
                    System.out.println("Canceled withdrawal. Returning to menu.");
                    displayCustomerAccountScreen(custSSN);
                    break;
                default:
                    System.out.println("Incorrect choice, please try again: ");
                    break;
            }
        } while (!correctChoice);
    }

    private void displayDepositMenu(String custSSN) {
        customerAccountCheck(custSSN);
        System.out.println("Deposit:");
        account.printAllBalances(custSSN);
        System.out.println("Please enter the account number you would like to deposit to:");
        String accountNumber = inputManager.getInternalTransferAccountNumber(custSSN);
        System.out.println("How much would you like to deposit:");
        BigDecimal depositAmount = inputManager.getDeposit();

        System.out.println("Please confirm all information for the deposit is correct.\n" +
                "Deposit into: " + accountNumber + "\n" +
                "Deposit amount: " + depositAmount);
        System.out.println("\nPlease select an option below:\n" +
                "1. Information is correct, commit deposit.\n" +
                "2. Information is not correct, reenter information.\n" +
                "3. Cancel deposit and go back.");
        boolean correctChoice = false;
        do {
            int menuChoice = inputManager.menuInput();
            if ((menuChoice == 1) || (menuChoice == 2) || (menuChoice == 3)) {
                correctChoice = true;
            }
            switch (menuChoice) {
                case 1:
                    account.commitDeposit(accountNumber, depositAmount);
                    System.out.println("Deposit successful.");
                    displayCustomerAccountScreen(custSSN);
                    break;
                case 2:
                    System.out.println("Please reenter correct information.");
                    displayDepositMenu(custSSN);
                    break;
                case 3:
                    System.out.println("Canceled deposit. Returning to menu.");
                    displayCustomerAccountScreen(custSSN);
                    break;
                default:
                    System.out.println("Incorrect choice, please try again: ");
                    break;
            }
        } while (!correctChoice);
    }

    private void displayTransferMenu(String custSSN) {
        customerAccountCheck(custSSN);
        System.out.println("Please choose one of the following:\n" +
                "1. Internal Transfer.\n" +
                "2. External Transfer. (Function not available at this time.)\n" +
                "3. Return to account menu.\n" +
                "Enter choice: ");
        boolean correctMenuChoice = false;
        do {
            int menuChoice = inputManager.menuInput();
            if ((menuChoice == 1) || (menuChoice == 3)){
                correctMenuChoice = true;
            }
            switch (menuChoice) {
                case 1:
                    displayInternalTransferMenu(custSSN);
                    break;
                case 2:
                    System.out.println("This function does not work at this time.");
                    break;
                case 3:
                    displayCustomerAccountScreen(custSSN);
                    break;
                default:
                    System.out.println("Wrong choice please try again.");
                    break;
            }
        } while (!correctMenuChoice);
    }

    private void displayInternalTransferMenu(String custSSN){
        account.printAllBalances(custSSN);
        System.out.println("\nInternal Transfer: \n");
        System.out.println("Enter the account number of which account you would like to transfer from: ");
        String transferFromAccountNumber = inputManager.getInternalTransferAccountNumber(custSSN);
        System.out.println("Please enter which account you would like to transfer to: ");
        String transferToAccountNumber = inputManager.getInternalTransferAccountNumber(custSSN);
        System.out.println("- - - - - - - - - - - - - - \n" +
                "You are transferring from: " + transferFromAccountNumber + "\n" +
                "You are transferring to: " + transferToAccountNumber + "\n" +
                "How much would you like to transfer?");
        BigDecimal transferAmount = inputManager.getTransferAmount(transferFromAccountNumber);

        System.out.println("Please confirm all information for the transfer is correct.\n" +
                "Transfer from: " + transferFromAccountNumber + "\n" +
                "Transfer To: " + transferToAccountNumber + "\n" +
                "Transfer amount: " + transferAmount);
        System.out.println("\nPlease select an option below:\n" +
                "1. Information is correct, commit transfer.\n" +
                "2. Information is not correct, reenter information.\n" +
                "3. Cancel transfer and go back.");
        boolean correctChoice = false;
        do {
            int menuChoice = inputManager.menuInput();
            if ((menuChoice == 1) || (menuChoice == 2) || (menuChoice == 3)) {
                correctChoice = true;
            }
            switch (menuChoice) {
                case 1:
                    account.commitTransfer(transferFromAccountNumber, transferToAccountNumber, transferAmount);
                    System.out.println("Transfer Complete.");
                    displayCustomerAccountScreen(custSSN);
                    break;
                case 2:
                    displayInternalTransferMenu(custSSN);
                    break;
                case 3:
                    System.out.println("Canceled transfer. Returning to menu.");
                    displayCustomerAccountScreen(custSSN);
                    break;
                default:
                    System.out.println("Incorrect choice, please try again: ");
                    break;
            }
        }while (!correctChoice);

        ///TODO verify customer does not just have one account and if they do have an option to go to the create new account screen
    }

    private void openBankAccount(String custSSN){
        ///TODO add the ability to track how many accounts someone has and limit them.
        System.out.println("Open new bank account.\n" +
                "- - - - - - - - - - - - - -");
        System.out.println("Choose which type of account you would like to open.\n" +
                "0. Cancel account opening." +
                "1. Checking.\n" +
                "2. Savings");

        boolean validChoice = false;
        String accountType = "error";
        do {
            int accountChoice = inputManager.menuInput();
            validChoice = accountChoice == 1 || accountChoice == 2;

            switch (accountChoice) {
                case 0:
                    System.out.println("Cancelled account opening.");
                    displayCustomerAccountScreen(custSSN);
                    break;
                case 1:
                    accountType = "checking";
                    break;
                case 2:
                    accountType = "savings";
                    break;
                default:
                    System.out.println("Incorrect selection, please try again.");
                    break;
            }
        } while(validChoice == false);
        System.out.println("You have selected a " + accountType + ".");

        System.out.println("How much would you like to initially deposit?");
        System.out.println("Deposit: ");
        BigDecimal initialDeposit = inputManager.getDeposit();

        Account newAccount = new Account(databaseManager.getCustomerFirstName(custSSN),
                databaseManager.getCustomerLastName(custSSN), accountType, initialDeposit, custSSN);
        System.out.println(accountType + " opened. " + initialDeposit + " deposited.");
        displayCustomerAccountScreen(custSSN);
    }

    private void displayLoginScreen() {
        System.out.println("Please login below.\n" +
                "If you would like to go back please enter \"cancel\" \n" +
                "Username: ");

        String username = inputManager.getUsernameForLogin();
        if (username.equalsIgnoreCase("cancel")){
            displayInitialMenu();
        }

        System.out.println("Please enter your password: ");
        String password = inputManager.getPasswordForLogin(username);
        ///TODO add functionality to back out of password entry and go to initial screen.

        displayCustomerAccountScreen(databaseManager.getCustomerSSN(username));

    }

    private void clearScreen(){
        System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
    }

    private void customerAccountCheck(String custSSN){
        if (!account.doesCustHaveOpenAccount(custSSN)){
            System.out.println("You currently don't have any bank accounts open.");
            displayCustomerAccountScreen(custSSN);
        }
    }

}
