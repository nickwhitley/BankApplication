import Database.DatabaseManager;

public class Customer {
    private String firstName;
    private String lastName;
    private String ssn;
    private String onlineUsername;
    private String onlinePassword;

    DatabaseManager databaseManager = new DatabaseManager();

    public Customer(String firstName, String lastName, String ssn, String onlineUsername, String onlinePassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.ssn = ssn;
        this.onlineUsername = onlineUsername;
        this.onlinePassword = onlinePassword;
        databaseManager.createNewCustomer(firstName, lastName, ssn, onlineUsername, onlinePassword);
    }

}
