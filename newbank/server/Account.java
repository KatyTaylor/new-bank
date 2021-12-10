package newbank.server;

import java.text.DecimalFormat;

/**
 *Represents a bank account owned by a Customer
 *a bank account has an account name and an opening balance
 */
public class Account {

	private String accountName;
	private double openingBalance;
	private double balance;
	//DecimalFormat moneyFormat = new DecimalFormat("'£'###,##0.00");

	public Account(String accountName, double openingBalance) {
		this.accountName = accountName;
		this.openingBalance = openingBalance;
		this.balance = openingBalance;
	}

	public String toString() {
		return (accountName + ": " + balance);
	}

	public String getAccountName() {
		return accountName;
	}
	public double getAccountBalance() {
		return balance;
	}

	public void deposit( double amount){
		this.balance = balance + amount;
	}
	public void withdraw (double amount) {

		this.balance = balance - amount;
	}
}
