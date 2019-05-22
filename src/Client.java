/**
 *  A Java program for a Client-based connections 
 *  More individual, lowest-level
 */
import java.net.*;
import java.io.*; 

public class Client
{ 
	private Socket clientSocket;
	private PrintWriter out;
	private BufferedReader in;
	private String timeOut = null;
	
	public String Communicate(String ip, int port, String command) {
		clientSocket = new Socket();
		// makes single connection
		try {
			clientSocket.connect(new InetSocketAddress(ip, port), 1000);
			clientSocket.setSoTimeout(200);
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return "NoDev";
		}

		
		// this waits to see if the preset print comes in for error control
		String inputLine = attemptConnect();
		try {
			sendMessage(command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return timeOut ;
		}
		
		inputLine = attemptConnect();
		
		//closes connection to prevent hanging
		try {
			stopConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return timeOut;
		}
		
		return inputLine;
	} 

	private String sendMessage(String msg) throws IOException {
		out.println(msg);
		String resp = in.readLine();
		return resp;
	}

	private void stopConnection() throws IOException {
		in.close();
		out.close();
		clientSocket.close();
	} 
	
	private String attemptConnect() {
		String line = "";
		try {
			line = connSucc();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return timeOut;
		}
		return line;
	}
	
		// checks to see if its our meter
	private String connSucc() throws IOException {
		String line = "";
		while ((line = in.readLine()) != null) {
			break;
		}
		return line;
	}

} 