package tcpchat;

import java.net.ServerSocket; //accept
import java.net.Socket; //getOutputStream, getInputStream, close
import java.net.UnknownHostException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

//to run this in the command line, open 2 command line windows.  Change directory of the Java Project (TCPChat). 
//type "java -cp bin tcpchat.TCPChatServer" and in the other command window: "java -cp bin tcpchat.TCPChatClient".


class TCPChatServer {

	private static final int PORT = 3000;

	public static void main(String args[]) throws Exception {
		System.out.println("server running...");

		// create socket
		ServerSocket serverSocket = new ServerSocket(TCPChatServer.PORT);

		while (true) {
			Socket socket = serverSocket.accept();

			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
			
			String sentence = inFromClient.readLine();
			System.out.println("Received: " + sentence);

			String capitalizedSentence = sentence.toUpperCase();
			
			outToClient.writeBytes(capitalizedSentence);
			socket.close();
		}
	}
}

class TCPChatClient {
	private static final int PORT = 3000;

	public static void main(String args[]) {
		System.out.println("client running...");

		while (true) {
			// get user input from console
			BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

			try {
			// create socket
			Socket clientSocket = new Socket("localhost", PORT);
			// send data to server
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

			// receive data BACK from server
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			String sentence = inFromUser.readLine();

			// enable user to quit
			if (sentence.equals("quit")) {
				break;
			}

			outToServer.writeBytes(sentence + '\n');

			String modifiedSentence = inFromServer.readLine();

			System.out.println("FROM SERVER: " + modifiedSentence);

			// clientSocket.close();
			} catch (ConnectException e) {
				System.out.println("Please make sure the server is up and running before proceeding.");
				break;
			} catch (UnknownHostException e) {
				System.out.println("There was an unknown Host Exception.");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("There was an input/output exception.");
				e.printStackTrace();
			}
		}

	}
}