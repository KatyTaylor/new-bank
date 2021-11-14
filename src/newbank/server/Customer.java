package newbank.server;

import java.util.ArrayList;

public class Customer {
	
	private Registration registration; //MK 14/11/21

	private ArrayList<Account> accounts;



	public Customer() {
		accounts = new ArrayList<>();
	}
	
	public String accountsToString() {
		String s = "";
		for(Account a : accounts) {
			s += a.toString();
		}
		return s;
	}

	public void addAccount(Account account) {
		accounts.add(account);		
	}


	//Add by MK 14/11/21
	public Registration getRegistration() {
		return registration;
	}

	public void setRegistration(Registration registration) {
		this.registration = registration;
	}
	
	
	
}
