package newbank.server;

import java.util.ArrayList;
/**
 *Represents a bank's Customer
 * Accounts owned by the Customer are stored in an ArrayList "accounts"
 * already contains a method to add new account: addAccount() and
 * a method to print the accounts owned by the Customer: accountsToString()
 */
public class Customer {

	private ArrayList<Account> accounts;
	private String password;
	private Registration registration;

	public Customer() {
		accounts = new ArrayList<>();
	}

	public String accountsToString() {
		String s = "";
		for(Account a : accounts) {
			s += a.toString() + "\n"; //added new line for better visibility when printing multiple accounts
		}
		return s;
	}

	public void addAccount(Account account) {
		accounts.add(account);
	}

	public void setPassword(String password) {
		// don't allow password resets at the moment
		if(this.password != null){
			return;
		}

		this.password = password;
	}

	public Boolean passwordMatches(String password) {
		return this.password.equals(password);
	}

	public Registration getRegistration() {
		return registration;
	}

	public void setRegistration(Registration registration) {
		this.registration = registration;
	}
}
