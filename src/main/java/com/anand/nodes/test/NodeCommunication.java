package com.anand.nodes.test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * This class start the client program that connects to a server by using a socket 
 * and sends a greeting, and then waits for a response.
 * 
 * @author anand prakash
 *
 */
public class NodeCommunication {
	static String HOST = "127.0.0.1";
	static int PORT = 8080;

	public static void main(String[] args) {
		HOST = args[0];
		PORT = Integer.valueOf(args[1]);
		
		NodeCommunication nc = new NodeCommunication();
		System.out.println("Stat server!");

		try {
			nc.startServer();
			Thread.sleep(1000);
			
			System.out.println("\nStart clientApp..");
			nc.startClientApp(HOST, PORT);
			
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}

	}


	public void startServer() throws InterruptedException{
	      try
	      {
	    	 Thread t1 = new ServerApp(PORT);
	         t1.start();
	         
	      }catch(IOException e)
	      {
	         e.printStackTrace();
	      }
	}

	/**
	 * Start the client communication to the server over the socket and establish
	 * handshaking with the server with message "Hello from" client.Read server message.
	 *  
	 * @param serverName
	 * @param portConnect
	 */
    public void startClientApp(String serverName,int portConnect){
    	
    	try {
    		System.out.println("Connecting to " + serverName + " on port " + portConnect);
	        Socket client = new Socket(serverName, portConnect);
	        System.out.println("Client Just connected to " + client.getRemoteSocketAddress().toString().replaceAll("/","-"));
	         
	        OutputStream outToServer = client.getOutputStream();
	        DataOutputStream out = new DataOutputStream(outToServer);
	        out.writeUTF("Hello from " + client.getRemoteSocketAddress().toString().replaceAll("/","-"));
	         
	        InputStream inFromServer = client.getInputStream();
	        DataInputStream in = new DataInputStream(inFromServer);
	        System.out.println("\nServer says-> " + in.readUTF());
	        client.close();
	      }catch(IOException e)
	      {
	         e.printStackTrace();
	      }
   }


}
