package com.anand.nodes.test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class SharedMemoryFile {
	static long fileSize = 0;

	/**
	 * Write buffer data to the file.
	 * 
	 * @param message
	 * @throws IOException
	 */
	public static void writeStatus(String message) throws IOException{
		//String message = "started";
		ByteBuffer buf =  ByteBuffer.wrap(message.getBytes());
		new File("status.txt").delete();  
		//Get a channel for writing a random access file in rw mode.
		FileChannel rwCh = (new RandomAccessFile("status.txt","rw")).getChannel();
		                        
		//Write buffer data to the file
		System.out.println("Bytes written = "  + rwCh.write(buf));
		                 
		//Close the channel so that it can no longer be used to access the file.
		rwCh.close();
		    
		buf = null;
		message = null;
	}
	
	/**
	 * Get a new FileChannel object for reading and writing the existing file.
	 * Map entire file to Memory and close the channel.
	 * 
	 * @return
	 */
	public static long mapFileToMemory(){
		
		try {
			
			FileChannel rwCh = new RandomAccessFile("status.txt","rw").getChannel();
			// Lock the file while processing
			FileLock rwFileLockCh = new RandomAccessFile("status.txt","rw").getChannel().tryLock();
			ByteBuffer mapFile = null;
			//long fileSize = 0;
			if(rwFileLockCh != null) {
			    //Map entire file to Memory and close the channel
			    fileSize = rwCh.size();
			    //System.out.println("file size is-"+fileSize);
			    mapFile = rwCh.map( FileChannel.MapMode.READ_WRITE,0, fileSize);
			    rwCh.close();
			    //Display contents of memory map
			    //showBufferData(mapFile,"mapFile");
			     
			    rwFileLockCh.release();
			   }
	
		  }catch(Exception e){
		      System.out.println(e);
		  }
		return fileSize;
		 
	}
	
	/**
	 * Read file data into the new  buffer, close the channel, and display the data.
	 * 
	 * @param fileSize
	 * @return
	 * @throws IOException
	 */
	public static String readMemoryMapFile(long fileSize) throws IOException{
		  StringBuilder sb = new StringBuilder();
		 // System.out.println("Read/display modified file");
	      //Get new channel for read only
	      FileChannel newInCh = new RandomAccessFile("status.txt","r").getChannel();
	                          
	      //Allocate (don't wrap) a new ByteBuffer
	      ByteBuffer newBuf = ByteBuffer.allocate((int)fileSize);

	     // System.out.println("Bytes read = " + newInCh.read(newBuf));
	      newInCh.close();

	      int pos = newBuf.position();
	      //Set position to zero
	      newBuf.position(0);

	      while(newBuf.hasRemaining()){
	        sb.append((char)newBuf.get());
	        
	      }
	      //Restore position and return
	      newBuf.position(pos);
		  return sb.toString();
	}
	
	/**
	 * Change/Modify the contents of the map and hence the file.
	 * 
	 * @return
	 */
	public static long modifyMapFileToMemory(){
		long fileSize = 0;
		try {
			FileChannel rwCh = new RandomAccessFile("status.txt","rw").getChannel();
			// Lock the file while processing
			FileLock rwFileLockCh = new RandomAccessFile("status.txt","rw").getChannel().tryLock();
			ByteBuffer mapFile = null;
			//long fileSize = 0;
			if(rwFileLockCh != null) {
			    fileSize = rwCh.size();
			    //System.out.println("file size is-"+fileSize);
			    mapFile = rwCh.map( FileChannel.MapMode.READ_WRITE,0, fileSize);
			    rwCh.close();
			    //Change contents of the map and hence the file.  Note that the channel is closed.
			    mapFile.position(5);
			    mapFile.put((byte)1);
			    mapFile.position(6);
			    mapFile.put((byte)2);
			     
			    rwFileLockCh.release();	      
			}

		  }catch(Exception e){
		      System.out.println(e);
		  }
		return fileSize;
	}
	
	/**
	 * Display contents of memory map.
	 * 
	 * @param buf
	 * @param name
	 */
	static void showBufferData( ByteBuffer buf, String name){
		StringBuilder sb = new StringBuilder();
		//Displays byte buffer contents
		//Save position
		int pos = buf.position();
		//Set position to zero
		buf.position(0);
		System.out.println("Data for " + name);
		
		while(buf.hasRemaining()){
			sb.append((char)buf.get());
		
		}
		//Display new contents of memory map
		System.out.println(sb.toString());
		//Restore position and return
		buf.position(pos);
	}
	 
	public static void main(String[] args) {

	}

}
