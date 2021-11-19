package newbank.server;
/**
 *CustomerID gets instantiated using the Customer's userName, as part of the login process.
 * contains a String "key" and a method to access it
 * (KT)
 */
public class CustomerID {
	private String key;

	public CustomerID(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}
}
