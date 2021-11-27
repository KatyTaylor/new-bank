package newbank.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *NewBankClientHandler is a Thread which deals with requests from the client.
 * It initially asks for the userName and a password and deals with the login.
 * Then it waits for commands and passes them to a NewBank instance to respond
 * (KT)
 */
public class NewBankClientHandler extends Thread{

	private NewBank bank;
	private BufferedReader in;
	private PrintWriter out;

	public NewBankClientHandler(Socket s) throws IOException {
		bank = NewBank.getBank();
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		out = new PrintWriter(s.getOutputStream(), true);
	}

	//Case switch newsletter registration or existing account.
	public void run() {

		// keep getting requests from the client and processing them
		try {

			out.println("\nWelcome to New Bank.\n");

			while (true) {
				printMenu1();

				int selection = Integer.parseInt(in.readLine());
				switch (selection) {
					case 1:
						handleLogIn();
						break;
					case 2:
						handleRegister();
						break;
					case 3:
						handleShowBankInfo();
						break;
					case 4:
						handleRequestCallBack();
						break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
	}

	private void printMenu1(){
		out.println("\nWhat do you want to do?\n");
		out.println("1 \tLog in");
		out.println("2 \tRegister");
		out.println("3 \tShow bank information");
		out.println("4 \tRequest a call back");
	}

	private void handleLogIn() throws IOException{
		// ask for user name
		out.println("Enter Username");
		String userName = in.readLine();
		// ask for password
		out.println("Enter Password");
		String password = in.readLine();
		out.println("Checking Details...");
		// authenticate user and get customer ID token from bank for use in subsequent
		// requests
		CustomerID customer = bank.checkLogInDetails(userName, password);
		// if the user is authenticated then get requests from the user and process them
		if(customer != null) {
			printMenu2();

			while(true) {
				String request = in.readLine();
				System.out.println("Request from " + customer.getKey());
				String responce = bank.processRequest(customer, request);
				out.println(responce);
			}
		} else {
			out.println("Log In Failed");
		}
	}

	private void handleRegister() throws IOException{
		out.println("Please enter your Full Name including Title");
		String fullname = in.readLine();

		out.println("Please enter your email address. You will be enrolled to our newsletter & updates");
		String email = in.readLine();

		out.println("Please enter your subscription password. Please note this is NOT your BANKING PASSWORD");
		String pass = in.readLine();

		Registration registration = new Registration(fullname, email, pass);

		Customer newcustomer = new Customer();

		newcustomer.setRegistration(registration);

		bank.addCustomer(newcustomer);

		String username = fullname.split(" ")[0];

		out.println("\n--------------------------------------");
		out.println("Your UserName is: " + username);
		out.println("--------------------------------------");
		out.println("Your details have been saved and you should shortly receive an email to confirm. Thank you!");
		out.println("--------------------------------------\n");
	}

	private void handleShowBankInfo(){
		out.println("\nNew Bank contact information:");
		out.println("Address: 235 Financial Avenue, London NE5 8JQ");
		out.println("Phone: 12345 678901");
		out.println("Email: newbankinfo@example.com");
		out.println("--------------------------------------\n");

		out.println("Current interest rates:");
		out.println("Current: 0.4%");
		out.println("Savings: 6%");
		out.println("Checking: 1%");
		out.println("--------------------------------------\n");
	}

	private void handleRequestCallBack() throws IOException{
		out.println("\nPlease tell us the number you would like to be called on:");
		in.readLine();

		out.println("\n--------------------------------------");
		out.println("Thank you. A member of the team will be in touch within one hour.");
		out.println("--------------------------------------\n");
	}

	private void printMenu2(){
		out.println("Log In Successful. What do you want to do?");

		out.println("1. Check your accounts");
		out.println("2. Open new Main account");
		out.println("3. Open new Savings account");
		out.println("4. Open new Checking account");
	}

}
