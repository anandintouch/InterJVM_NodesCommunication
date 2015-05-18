package com.anand.nodes.test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * This class is an example of a server application that uses the Socket class to listen for 
 * clients on a port number specified by a command-line argument.
 * Check server status.Read the memory mapped file and check the status an accordingly
 * write to the stream and print on console.
 * 
 * @author anand prakash
 *
 */
public class ServerApp extends Thread {
	 private ServerSocket serverSocket;
	   
	   public ServerApp(int port) throws IOException
	   {
	      serverSocket = new ServerSocket(port);
	      serverSocket.setSoTimeout(20000);
	   }

	   public void run()
	   {
	      while(true)
	      {
	         try
	         {
	            System.out.println("Waiting for client on port " +serverSocket.getLocalPort() + "...");
	            Socket server = serverSocket.accept();
	            System.out.println("Server Just connected to "
	                  + server.getLocalSocketAddress().toString().replaceAll("/","-"));
	            DataInputStream in = new DataInputStream(server.getInputStream());
	            System.out.println(in.readUTF()); // message from client app
	            DataOutputStream out = new DataOutputStream(server.getOutputStream());
	            checkServerMessage(out,server.getLocalSocketAddress());

	            //out.writeUTF("We are started!"+"\nGoodbye!");
	            
	            server.close();
	         }catch(SocketTimeoutException s)
	         {
	            System.out.println("Socket timed out!");
	            break;
	         }catch(IOException e)
	         {
	            e.printStackTrace();
	            break;
	         }
	      }
	   }
	   
	   /**
	    * Check server status.Read the memory mapped file and check the status and
	    * accordingly write to the stream and print on console.
	    * 
	    * @param out
	    * @param socketAddress
	    * @return
	    * @throws UnknownHostException
	    * @throws IOException
	    */
	   public boolean checkServerMessage(DataOutputStream out, SocketAddress socketAddress) throws UnknownHostException, IOException{
	   	   boolean status = false;
		   String fileData = null;
		   if(fileExist())
			   fileData = SharedMemoryFile.readMemoryMapFile(SharedMemoryFile.fileSize);
		   
		   if(fileData == null){
			   SharedMemoryFile.writeStatus("started");
			   SharedMemoryFile.mapFileToMemory();
			   out.writeUTF("We are started! at"+socketAddress.toString().replaceAll("/","-"));
			   System.out.println("====> We are started!, Node is"+socketAddress.toString().replaceAll("/","-")
					   +" <====");
			   status = true;
		   }else{
			   out.writeUTF("I'm not started,Master node is already started at"+socketAddress.toString().replaceAll("/","-"));
			   System.out.println("I'm not started,Master node is already started");
		   }
		
			return status;	
		}
	   
	   /**
	    * Checks if file exist or not.
	    * 
	    * @return file exist status
	    */
	   private boolean fileExist(){
		   File f = new File("status.txt");
		   
			  if(f.exists()){
				  System.out.println("File existed");
				  return true;
			  }else{
				  System.out.println("File not found!");
				  return false;
			  }
	   }
	   
	   public static void main(String [] args)
	   {
	      int port = Integer.parseInt(args[0]);
	      try
	      {
	         Thread t = new ServerApp(port);
	         t.start();
	      }catch(IOException e)
	      {
	         e.printStackTrace();
	      }
	   }
}
