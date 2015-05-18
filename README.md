

**Inter JVM/Processes communication between Servers**
-----------------------------------------------------

This application need to coordinate among the nodes and make sure that one and only one of them does a System.out.println("We are started!")
Different process of this application on different nodes/machines will check the message status from the memory mapped file(status.txt) and accordingly print the message to the console. 
It creates a Client and Server socket communication and once server starts,it creates a file and map entire file to memory and does memory-mapped IO .

**Files details:**
 1. NodeCommunication:  This class start the client program that connects to a   
     server by using a socket and sends a greeting, and then waits for a response.
 2. ServerApp: This class is an example of a server application that uses the 
     Socket class to listen for clients on a port number specified by a command-
    the status an accordingly write to the stream and print on console.

***To run this tool:***

The source code can be imported into your IDE or local folder using git clone command and build the maven module using Maven command as below

> mvn clean install

Sample command line argument to run the application is mentioned below.

> ./runApplication.sh

This script points to the executable jar (NodesCommunication-0.0.1-SNAPSHOT.jar) available inside the target folder , and it also has host and port where server connects to.