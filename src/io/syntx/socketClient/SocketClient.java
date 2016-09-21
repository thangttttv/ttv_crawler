package io.syntx.socketClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * A Simple Socket client that connects to our socket server
 *
 */
public class SocketClient {

    private String hostname;
    private int port;
    Socket socketClient;
    private DataInputStream  console   = null;

    public SocketClient(String hostname, int port){
        this.hostname = hostname;
        this.port = port;
        console   = new DataInputStream(System.in);
    }

    public void connect() throws UnknownHostException, IOException{
        System.out.println("Attempting to connect to "+hostname+":"+port);
        socketClient = new Socket(hostname,port);
        System.out.println("Connection Established");
    }

    public void readResponse() throws IOException{
        String userInput;
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));

        System.out.print("RESPONSE FROM SERVER:");
        while ((userInput = stdIn.readLine()) != null) {
            System.out.println(userInput);
        }
    }
    
    public void askForTime() throws IOException{
    	BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));
		writer.write("TIME?");
		writer.newLine();
		writer.flush();
    }
    
    public void chatFormConsole() throws IOException{
    	BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));
    	System.out.println("Dang cho gui 1");
    	while(true){
    		System.out.println("Dang cho gui bat dau vong");
    		  try
    	         {  
    			  BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    			    String s = br.readLine();
    	         	writer.write(s);
    	    		writer.newLine();
    	    		writer.flush();
    	         }
    	         catch(IOException ioe)
    	         {  System.out.println("Sending error: " + ioe.getMessage());
    	            
    	         }
    		System.out.println("Dang cho gui cuoi vong");
    	}
		
    }
    
    public void viewChat()
    {  while (true)
       {  try
          {  
    	   String userInput;
           BufferedReader stdIn = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));

           System.out.print("RESPONSE FROM SERVER:");
           while ((userInput = stdIn.readLine()) != null) {
               System.out.println(userInput);
           }
          }
          catch(IOException ioe)
          {  System.out.println("Listening error: " + ioe.getMessage());
            
          }
       }
    }

    public static void main(String arg[]){
        //Creating a SocketClient object
        SocketClient client = new SocketClient ("localhost",9991);
        try {
            //trying to establish connection to the server
            client.connect();
            //asking server for time
            client.chatFormConsole();
            //waiting to read response from server
           // client.viewChat();
            
        } catch (UnknownHostException e) {
            System.err.println("Host unknown. Cannot establish connection");
        } catch (IOException e) {
            System.err.println("Cannot establish connection. Server may not be up."+e.getMessage());
        }
    }
}