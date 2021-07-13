import Database.DatabaseManager;

public class Customer {
    private String firstName;
    private String lastName;
    private String ssn;
    private String onlineUsername;
    private String onlinePassword;

    DatabaseManager databaseManager = new DatabaseManager();

    public Customer(){}

    public Customer(String firstName, String lastName, String ssn, String onlineUsername, String onlinePassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.ssn = ssn;
        this.onlineUsername = onlineUsername;
        this.onlinePassword = onlinePassword;
        databaseManager.createNewCustomer(firstName, lastName, ssn, onlineUsername, onlinePassword);
    }

    public String getFirstName(String custSSN) {
        return databaseManager.getCustomerFirstName(custSSN);
    }

    public void setFirstName(String custSSN, String newFirstName) {
        databaseManager.setCustomerFirstName(custSSN, newFirstName);
    }

    public String getLastName(String custSSN) {
        return databaseManager.getCustomerLastName(custSSN);
    }

    public void setLastName(String custSSN, String newLastName){
        databaseManager.setCustomerLastName(custSSN, newLastName);
    }

    public String getOnlineUsername(String custSSN){
        return databaseManager.getOnlineUsername(custSSN);
    }

    public void setOnlineUsername(String custSSN, String newUsername){
        databaseManager.setOnlineUsername(custSSN, newUsername);
    }

    public void setOnlinePassword(String custSSN, String newPassword){
        databaseManager.setOnlinePassword(custSSN, newPassword);
    }

    public void setAllInfo(String custSSN, String firstName, String lastName, String onlineUsername,
                           String onlinePassword){
        setFirstName(custSSN, firstName);
        setLastName(custSSN, lastName);
        setOnlineUsername(custSSN, onlineUsername);
        setOnlinePassword(custSSN, onlinePassword);

    }

}
