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
		bhagy.addAccount(new Account("Main", 1000.0));
		customers.put("Bhagy", bhagy);
		
		Customer christina = new Customer();
		christina.addAccount(new Account("Savings", 1500.0));
		customers.put("Christina", christina);
		
		Customer john = new Customer();
		john.addAccount(new Account("Checking", 250.0));
		customers.put("John", john);
	}
	
	public static NewBank getBank() {
		return bank;
	}
	
	public synchronized CustomerID checkLogInDetails(String userName, String password) {
		if(customers.containsKey(userName)) {
			return new CustomerID(userName);
		}
		return null;
	}

	// commands from the NewBank customer are processed in this method
	public synchronized String processRequest(CustomerID customer, String request) {
		if(customers.containsKey(customer.getKey())) {
			switch(request) {
			//case "SHOWMYACCOUNTS" : return showMyAccounts(customer);
				case "1" : return showMyAccounts(customer);
				case "2" : return addMainAccount(customer);
				case "3" : return addSavingsAccount(customer);
				case "4" : return addCheckingAccount(customer);
			default : return "FAIL";
			}
		}
		return "FAIL";
	}
	
	private String showMyAccounts(CustomerID customer) {
		return (customers.get(customer.getKey())).accountsToString();
	}

	private String addMainAccount(CustomerID customer) {
		Customer userName = customers.get(customer.getKey());
		Account main = new Account("Main", 0.00);
		userName.addAccount(main);
		return ("SUCCESS New account " + main.toString());
	}

	private String addSavingsAccount(CustomerID customer) {
		Customer userName = customers.get(customer.getKey());
		Account savings = new Account("Savings", 0.00);
		userName.addAccount(savings);
		return ("SUCCESS New account " + savings.toString());
	}

	private String addCheckingAccount(CustomerID customer) {
		Customer userName = customers.get(customer.getKey());
		Account checking = new Account("Checking", 0.00);
		userName.addAccount(checking);
		return ("SUCCESS New account " + checking.toString());
	}
}
