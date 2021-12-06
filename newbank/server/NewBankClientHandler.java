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
					case 5:
						handleResetPassword();
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
		out.println("5 \tReset Password");
	}

	private void handleLogIn() throws IOException{
		// ask for user name
		out.println("Enter Username");
		String userName = in.readLine();
		//check if username is valid
		while(!bank.isUsername(userName)){
			out.println("Not a valid username. Please type your username again.");
			userName = in.readLine();
		}
		int i=3;
		for(i=3;i>0;i--){
			// ask for password. The customer has 3 attempts
			out.println("Enter Password");
			String password = in.readLine();
			out.println("Checking Details...");
			// authenticate user and get customer ID token from bank for use in subsequent
			// requests
			CustomerID customer = bank.checkLogInDetails(userName, password);
			// if the user is authenticated then get requests from the user and process them

			if(customer != null) {
				out.println("Log In Successful");
				while(true) {

					printMenu2(customer);
					String request = in.readLine();
					System.out.println("Request from " + customer.getKey());
					String responce = bank.processRequest(customer, request);
					out.println(responce);

				}
			} else {
				int n =i-1;
				if(n>0){
					out.println("Log In Failed. You have " + n + " more attempts before your account is blocked.");
				}
				else{
					out.println("Log In Failed. Your account is now blocked.");
				}
			}
		}
	}

	private void handleRegister() throws IOException{
		out.println("Please enter your Name");
		String fullname = in.readLine();

		out.println("Please enter your email address. You will be enrolled to our newsletter & updates");
		String email = in.readLine();

		out.println("Please enter a password for your account.");
		String pass = in.readLine();

		out.println("In case you need to reset your password in the future, please answer the following question...");
		out.println("What was the name of your first pet?.");
		String memorableWord = in.readLine();

		Registration registration = new Registration(fullname, email, pass);

		Customer newcustomer = new Customer();

		newcustomer.setRegistration(registration);

		bank.addCustomer(newcustomer, pass, memorableWord);

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

	private void handleResetPassword() throws IOException{
		// ask for user name
		out.println("Enter username");
		String userName = in.readLine();
		while(!bank.isUsername(userName)){
			out.println("Not a valid username. Please type your username again.");
			userName = in.readLine();			
		}
		// ask for memorable word
		out.println("What was the name of your first pet?");
		String memorableWord = in.readLine();
		out.println("Checking Details...");
		// check memorable word against word stored
		if(bank.isMemorableWord(userName, memorableWord)){
			out.println("Memorable word correct. Please type out a new password.");
			String password = in.readLine();
			bank.getCustomer(userName).setPassword(password);
			out.println("Password reset.");			
		}
		// requests


	}

	private void printMenu2(CustomerID customer){
		out.println("\nWhat do you want to do?\n");

		if(bank.hasAccount(customer, "Main") || bank.hasAccount(customer, "Savings") || bank.hasAccount(customer, "Checking")){
		out.println("1. Check your accounts");
		if(!bank.hasAccount(customer, "Main")){
			out.println("2. Open new Main account");}
		if(!bank.hasAccount(customer, "Savings")){
			out.println("3. Open new Savings account");}
		if(!bank.hasAccount(customer, "Checking")){
			out.println("4. Open new Checking account");}
	}
	}
}
