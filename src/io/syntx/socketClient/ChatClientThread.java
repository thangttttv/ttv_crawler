package io.syntx.socketClient;

import java.net.*;
import java.io.*;

public class ChatClientThread extends Thread
{  private Socket           socket   = null;
   private ChatClient       client   = null;
   private DataInputStream  streamIn = null;

   public ChatClientThread(ChatClient _client, Socket _socket)
   {  client   = _client;
      socket   = _socket;
      open();  
      start();
      System.out.println("Khoi Tao Client Thread ");
   }
   // nhan du lieu tu server
   public void open()
   {  try
      {  streamIn  = new DataInputStream(socket.getInputStream());
      }
      catch(IOException ioe)
      {  System.out.println("Error getting input stream: " + ioe);
         client.stop();
      }
   }
   public void close()
   {  try
      {  if (streamIn != null) streamIn.close();
      }
      catch(IOException ioe)
      {  System.out.println("Error closing input stream: " + ioe);
      }
   }
   // view ra man hinh
   public void run()
   {  while (true)
      {  
	  // System.out.println("Bat dau vui ra man hinh");
	   try
         {  
		   //client.readResponse() ;
		   String userInput;
	       BufferedReader stdIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

	       
	       while ((userInput = stdIn.readLine()) != null) {
	           System.out.println(userInput);
	       }
         }
         catch(IOException ioe)
         {  System.out.println("Listening error: " + ioe.getMessage());
            client.stop();
         }
      }
   }
}