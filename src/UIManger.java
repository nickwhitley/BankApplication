import Database.DatabaseManager;

import java.math.BigDecimal;
import java.util.Scanner;

public class UIManger {

    private InputManager inputManager = new InputManager();
    private DatabaseManager databaseManager = new DatabaseManager();
    private final String goodbyeMessage = "Have a great day fucker!";
    Scanner scanner = new Scanner(System.in);
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
            if (choice == 1) {
                System.out.println("Account has been created successfully.");
                infoConfirmed = true;
                Customer customer = new Customer(firstName, lastName, custSSN, username, password);
                displayCustomerAccountScreen(custSSN);
            } else if(choice == 2) {
                System.out.println("Okay.... maybe do it right this time.");
                displayCreateOnlineAccountScreen();
            } else {
                System.out.println("Idiot....");
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

        boolean correctChoice = false;
        do {
            int menuChoice = inputManager.menuInput();
            if(menuChoice > 0 && menuChoice < 10) {
                correctChoice = true;
            }
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
                    //handled in account class
                    break;
                case 8:
                    //handled in account class
                    break;
                case 9:
                    System.out.println("Logging you out. See you again soon!");
                    clearScreen();
                    custSSN = null;
                    displayInitialMenu();
                    break;
                default:
                    System.out.println("Maybe try again and this time don't fuck it up.");
            }
        } while (!correctChoice);
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
            if ((menuChoice == 1) || (menuChoice == 2) || (menuChoice == 3)) {
                correctChoice = true;
            }
            switch (menuChoice) {
                case 0:
                    System.out.println("Cancelling information change...");
                    displayCustomerAccountScreen(custSSN);
                    break;
                case 1:
                    ///TODO left off here
                    break;
                case 2:

                    break;
                case 3:

                    break;
                case 4:

                    break;
                case 5:

                    break;
                default:

                    break;
            }
        } while (!correctChoice);
    }

    private void displayWithdrawMenu(String custSSN) {
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
                    System.out.println("You fucking moron.");
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
                "1. Checking.\n" +
                "2. Savings");
        int accountChoice = inputManager.menuInput();
        boolean validChoice = false;
        if (accountChoice == 1 || accountChoice == 2){
            validChoice = true;
        }
        String accountType = "error";
        do {
            switch (accountChoice) {
                case 1:
                    accountType = "checking";
                    break;
                case 2:
                    accountType = "savings";
                    break;
                default:
                    System.out.println("Pick the right one dipshit.");
                    break;
            }
        } while(validChoice = false);
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

    public void displayOnlineAccountCreateScreen() {
        System.out.println("Please create your online account using a username and password.\n" +
                "What would you like your username to be, please note usernames can only contain up to 20 characters.\n" +
                "Enter your username below: ");

        String username = inputManager.createCustUsername();
        System.out.println("Your username is: " + username);
        System.out.println("\n" +
                "Now please enter your password, your password can only contain letters and numbers, no special characters.\n" +
                "Passwords must be at least 8 characters in length but can't be longer than 20.");
        String password = inputManager.createCustPassword();
        System.out.println("Your password is: " + password);


    }

    private static void clearScreen(){
        System.out.println("");
        System.out.println("=============================================================");
        System.out.println("");
        System.out.println("BBBBBBBBBBBBBBBBB    TTTTTTTTTTTTTTTTTTTTTTT DDDDDDDDDDDDD              BBBBBBBBBBBBBBBBB                                         kkkkkkkk");
        System.out.println("B::::::::::::::::B   T:::::::::::::::::::::T D::::::::::::DDD           B::::::::::::::::B                                        k::::::k");
        System.out.println("B::::::BBBBBB:::::B  T:::::::::::::::::::::T D:::::::::::::::DD         B::::::BBBBBB:::::B                                       k::::::k           ");
        System.out.println("BB:::::B     B:::::B T:::::TT:::::::TT:::::T DDD:::::DDDDD:::::D        BB:::::B     B:::::B                                      k::::::k           ");
        System.out.println("  B::::B     B:::::B TTTTTT  T:::::T  TTTTTT   D:::::D    D:::::D         B::::B     B:::::B   aaaaaaaaaaaaa   nnnn  nnnnnnnn      k:::::k    kkkkkkk");
        System.out.println("  B::::B     B:::::B         T:::::T           D:::::D     D:::::D        B::::B     B:::::B   a::::::::::::a  n:::nn::::::::nn    k:::::k   k:::::k ");
        System.out.println("  B::::BBBBBB:::::B          T:::::T           D:::::D     D:::::D        B::::BBBBBB:::::B    aaaaaaaaa:::::a n::::::::::::::nn   k:::::k  k:::::k  ");
        System.out.println("  B:::::::::::::BB           T:::::T           D:::::D     D:::::D        B:::::::::::::BB              a::::a nn:::::::::::::::n  k:::::k k:::::k   ");
        System.out.println("  B::::BBBBBB:::::B          T:::::T           D:::::D     D:::::D        B::::BBBBBB:::::B      aaaaaaa:::::a   n:::::nnnn:::::n  k::::::k:::::k    ");
        System.out.println("  B::::B     B:::::B         T:::::T           D:::::D     D:::::D        B::::B     B:::::B   aa::::::::::::a   n::::n    n::::n  k:::::::::::k     ");
        System.out.println("  B::::B     B:::::B         T:::::T           D:::::D     D:::::D        B::::B     B:::::B  a::::aaaa::::::a   n::::n    n::::n  k:::::::::::k     ");
        System.out.println("  B::::B     B:::::B         T:::::T           D:::::D    D:::::D         B::::B     B:::::B a::::a    a:::::a   n::::n    n::::n  k::::::k:::::k    ");
        System.out.println("BB:::::BBBBBB::::::B       TT:::::::TT       DDD:::::DDDDD:::::D        BB:::::BBBBBB::::::B a::::a    a:::::a   n::::n    n::::n k::::::k k:::::k   ");
        System.out.println("B:::::::::::::::::B        T:::::::::T       D:::::::::::::::DD         B:::::::::::::::::B  a:::::aaaa::::::a   n::::n    n::::n k::::::k  k:::::k  ");
        System.out.println("B::::::::::::::::B         T:::::::::T       D::::::::::::DDD           B::::::::::::::::B    a::::::::::aa:::a  n::::n    n::::n k::::::k   k:::::k ");
        System.out.println("BBBBBBBBBBBBBBBBB          TTTTTTTTTTT       DDDDDDDDDDDDD              BBBBBBBBBBBBBBBBB      aaaaaaaaaa  aaaa  nnnnnn    nnnnnn kkkkkkkk    kkkkkkk");
        System.out.println("Donald's bank sucks.");
        System.out.println("\n" +
                "");
    }



}
