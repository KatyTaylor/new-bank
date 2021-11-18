package newbank.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NewBankClientHandler extends Thread {

	private NewBank bank;
	private BufferedReader in;
	private PrintWriter out;

	public NewBankClientHandler(Socket s) throws IOException {
		bank = NewBank.getBank();
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		out = new PrintWriter(s.getOutputStream(), true);
	}


	//MK Updated this section 14/11/21
	//Case switch newsletter registration or existing account.

	public void run() {

		// keep getting requests from the client and processing them
		try {

			while (true) {

				out.println(
						"Type the number 1 if you have bank with us or Type the number 2 if would you like to register and learn more about banking with us");
				int selection = Integer.parseInt(in.readLine());

				switch (selection) {
				case 1:

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
					if (customer != null) {
						out.println("Log In Successful. What do you want to do?");
						while (true) {
							String request = in.readLine();
							System.out.println("Request from " + customer.getKey());
							String responce = bank.processRequest(customer, request);
							out.println(responce);
						}
					} else {
						out.println("Log In Failed");
					}

					break;
				case 2: // ADDED BY MK 14/11/21
					out.println("Please enter your Full Name including Title");
					String fullname = in.readLine();

					out.println("Please enter your email address. You will be enrolled to our newsletter & updates");
					String email = in.readLine();

					out.println("Please enter your subsription password. Please note this is NOT your BANKING PASSWORD");
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

					break;

				}
				if (selection == 1) {
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

}
