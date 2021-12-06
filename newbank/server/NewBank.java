package newbank.server;

import java.util.HashMap;
/**
 *Represents the Bank
 *contains a HashMap "customers", where Customer's userName (a String) is a key which is mapped to a Customer object - a value in the HashMap
 *commands from NewBanks customer processed in processRequest()
 * also contains a validation method checkLogInDetails() that needs building on to validate passwords
 */
public class NewBank {

	private static final NewBank bank = new NewBank();
	private HashMap<String,Customer> customers;

	private NewBank() {
		customers = new HashMap<>();
		addTestData();
	}

	private void addTestData() {
		Customer bhagy = new Customer();
		bhagy.setPassword("secretB");
		bhagy.setMemorableWord("memorableB");
		bhagy.addAccount(new Account("Main", 1000.0));
		customers.put("Bhagy", bhagy);

		Customer christina = new Customer();
		christina.setPassword("secretC");
		christina.setMemorableWord("memorableC");
		christina.addAccount(new Account("Savings", 1500.0));
		customers.put("Christina", christina);

		Customer john = new Customer();
		john.setPassword("secretJ");
		john.setMemorableWord("memorableJ");
		john.addAccount(new Account("Checking", 250.0));
		customers.put("John", john);
	}

	public static NewBank getBank() {
		return bank;
	}

	public synchronized CustomerID checkLogInDetails(String userName, String password) {
		if(customers.containsKey(userName)) {
			if(customers.get(userName).passwordMatches(password)) {
				return new CustomerID(userName);
			}
		}
		return null;
	}

	public boolean isUsername(String userName) {
		if(customers.containsKey(userName)) {
			return true;
	}
	return false;
}

	public synchronized Customer getCustomer(String userName) {
		if(customers.containsKey(userName)) {
				return customers.get(userName);
			}
		return null;
	}

	public boolean isMemorableWord(String userName, String memorableWord){
		if(customers.containsKey(userName)) {
			if(customers.get(userName).memorableWordMatches(memorableWord)) {
				return true;
			}
		}
		return false;
	}

	// commands from the NewBank customer are processed in this method
	public synchronized String processRequest(CustomerID customer, String request) {
		if(customers.containsKey(customer.getKey())) {
			switch(request) {
			// if user has no accounts, show accounts is an invalid request
			//do not allow users to create accounts that they already have
				case "1" : if(!hasAccount(customer, "Main") && !hasAccount(customer, "Savings") && !hasAccount(customer, "Checking")){
								return "Invalid request. Please try again.";} 
						   else{
							   return showMyAccounts(customer);}

				case "2" : if(hasAccount(customer, "Main")){
								return "Invalid request. Please try again.";} 
						   else{
								return addAccount(customer, "Main");}

				case "3" : if(hasAccount(customer, "Savings")){
								return "Invalid request. Please try again.";} 
						   else{
							   return addAccount(customer, "Savings");}

				case "4" : if(hasAccount(customer, "Checking")){
								return "Invalid request. Please try again.";} 
				           else{
							   return addAccount(customer, "Checking");}
			default : return "FAIL";
			}
		}
		return "FAIL";
	}

	private String showMyAccounts(CustomerID customer) {
		return (customers.get(customer.getKey())).accountsToString();
	}

	public boolean hasAccount(CustomerID customer, String account) {
		if((customers.get(customer.getKey())).accountsToString().contains(account)){
			return true;
		}
		return false;
	}

	public void addCustomer(Customer customer, String password, String memorableWord) {
		customer.addAccount(new Account("Main", 0));
		customer.setPassword(password);
		customer.setMemorableWord(memorableWord);
		String username = customer.getRegistration().getFullname().split(" ")[0];
		customers.put(username, customer);
	}

	private String addAccount(CustomerID customer, String type) {
		Customer userName = customers.get(customer.getKey());
		Account account = new Account(type, 0.00);
		userName.addAccount(account);
		return ("SUCCESS New account " + account.toString());
	}
}
