package newbank.server;

import java.text.DecimalFormat;

/**
 *Represents a bank account owned by a Customer
 *a bank account has an account name and an opening balance
 */
public class Account {

	private String accountName;
	private double openingBalance;
	//DecimalFormat moneyFormat = new DecimalFormat("'£'###,##0.00");

	public Account(String accountName, double openingBalance) {
		this.accountName = accountName;
		this.openingBalance = openingBalance;
	}

	public String toString() {
		return (accountName + ": " + openingBalance);
	}

}
