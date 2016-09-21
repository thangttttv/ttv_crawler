package io.syntx.socketClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient implements Runnable
{  private Socket socket              = null;
   private Thread thread              = null;
   private DataInputStream  console   = null;
   private DataOutputStream streamOut = null;
   private BufferedWriter writer = null;
   private ChatClientThread client    = null;

   public ChatClient(String serverName, int serverPort)
   {  System.out.println("Establishing connection. Please wait ...");
      try
      {  socket = new Socket(serverName, serverPort);
         System.out.println("Connected: " + socket);
         start();
      }
      catch(UnknownHostException uhe)
      {  System.out.println("Host unknown: " + uhe.getMessage()); }
      catch(IOException ioe)
      {  System.out.println("Unexpected exception: " + ioe.getMessage()); }
   }
   
   // nhap du lieu tu console day len server
   public void run()
   { 
	   while (thread != null)
      {  
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
            stop();
         }
      }
   }

   // view chat ra console
   public void handle(String msg)
   {  if (msg.equals(".bye"))
      {  System.out.println("Good bye. Press RETURN to exit ...");
         stop();
      }
      else
         System.out.println(msg);
   }
   
   public void readResponse() throws IOException{
       String userInput;
       BufferedReader stdIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

       System.out.print("RESPONSE FROM SERVER:");
       while ((userInput = stdIn.readLine()) != null) {
           System.out.println(userInput);
       }
   }
   
   public void start() throws IOException
   {  console   = new DataInputStream(System.in);
      streamOut = new DataOutputStream(socket.getOutputStream());
      writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
      if (thread == null)
      {  client = new ChatClientThread(this, socket);
         thread = new Thread(this);                   
         thread.start();
      }
   }
   public void stop()
   {  if (thread != null)
      {    
         thread = null;
      }
      try
      {  if (console   != null)  console.close();
         if (streamOut != null)  streamOut.close();
         if (socket    != null)  socket.close();
      }
      catch(IOException ioe)
      {  System.out.println("Error closing ..."); }
      client.close();  
   }
   
   public void askForTime() throws IOException{
   		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		writer.write("Gui THu xem sn");
		writer.newLine();
		writer.flush();
   }
   
   public static void main(String args[])
   {   
         	ChatClient  client = new ChatClient("127.0.0.1", 9991);
         	
   }
}