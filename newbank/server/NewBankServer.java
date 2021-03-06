package newbank.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * NewBankServer is a Thread which is started as part of the main() function
 * and runs on localhost port 14002
 * It starts up a new client handler thresd to receive incoming connections and process requests
 * (KT)
 */
public class NewBankServer extends Thread{

	private ServerSocket server;

	public NewBankServer(int port) throws IOException {
		server = new ServerSocket(port);
	}

	public void run() {
		// starts up a new client handler thread to receive incoming connections and process requests
		System.out.println("New Bank Server listening on " + server.getLocalPort());
		try {
			while(true) {
				Socket s = server.accept();
				NewBankClientHandler clientHandler = new NewBankClientHandler(s);
				clientHandler.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
	}

	public static void main(String[] args) throws IOException {
		// starts a new NewBankServer thread on a specified port number
		new NewBankServer(15002).start();
	}
}
