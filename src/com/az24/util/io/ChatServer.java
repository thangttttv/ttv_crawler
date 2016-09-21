package com.az24.util.io;

import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.io.*;

import org.apache.log4j.Logger;

public class ChatServer implements Runnable
{  private ChatServerThread clients[] = new ChatServerThread[50];
   private ServerSocket server = null;
   private Thread       thread = null;
   private int clientCount = 0;
   public HashMap<Integer, ChatServerThread> mapClients = new HashMap<Integer, ChatServerThread>();
   private final static Logger logger = Logger.getLogger(ChatServer.class);

   public ChatServer(int port)
   {  try
      {  logger.info("Binding to port " + port + ", please wait  ...");
         server = new ServerSocket(port);  
         logger.info("Server started: " + server);
      }catch(IOException ioe){
    	  logger.error("Can not bind to port " + port + ": " + ioe.getMessage()); }
   }
   
   public void run()
   {  while (thread != null)
      {  try
         {  logger.info("Waiting for a client ..."); 
            addThread(server.accept()); }
         catch(IOException ioe)
         {  logger.error("Server accept error: " + ioe); stop(); }
      }
   }
   
   public void start()
   {  if (thread == null)
      {  thread = new Thread(this); 
         thread.start();
      }
   }
   
   public void stop()
   {  if (thread != null)
      {   
         thread = null;
      }
   }
   
   private int findClient(int ID)
   {  for (int i = 0; i < clientCount; i++)
         if (clients[i].getID() == ID)
            return i;
      return -1;
   }
   
 
   public synchronized void handleChatMessageText(int toUSERID, ChatMessage chatMessage)
   {  
	     ChatServerThread chatServerThread = null;
	     chatServerThread = mapClients.get(toUSERID);
	     SwapHubDAO swapHubDAO = new SwapHubDAO();
	     swapHubDAO.saveMessage(chatMessage);
	     if(chatServerThread != null)
	     {
			 //chatServerThread.sendInt(ChatConstant.M_TYPE_TEXT);
			 //chatServerThread.sendText(chatMessage.toJSonStringMessageText()); 
			 chatServerThread.putMessage(chatMessage);
			 logger.info(chatServerThread.getID() + "-->"+"["+ChatConstant.M_TYPE_TEXT+"]" +":"+ chatMessage.toJSonStringMessageText()+"");
	     }else{
	    	 swapHubDAO.saveMessageQueue(chatMessage);
	    	 logger.info(" Queue <--"+"["+ChatConstant.M_TYPE_TEXT+"]" +":"+ chatMessage.toJSonStringMessageText());
	     }
      
   }
   
   public synchronized void handleChatMessageOfferSwap(int toUSERID, ChatMessage chatMessage,int presence_action)
   {  
	     ChatServerThread chatServerThread = null;
		 chatServerThread = mapClients.get(toUSERID);
		 SwapHubDAO swapHubDAO = new SwapHubDAO();
	     swapHubDAO.saveMessage(chatMessage);
	     if(chatServerThread != null)
	     {
			 chatServerThread.sendInt(presence_action);
			 chatServerThread.sendText(chatMessage.toJSonStringMessageOfferSwap()); 
			 logger.info(chatServerThread.getID() + "-->"+"["+presence_action+"]" +":"+ chatMessage.toJSonStringMessageOfferSwap());
	     }else {
	    	// Log chat send queue
	    	  swapHubDAO.saveMessageQueue(chatMessage);
	    	  logger.info(" Queue <--"+"["+presence_action+"]" +":"+ chatMessage.toJSonStringMessageOfferSwap());
	     }
   }
   
   
   public synchronized void handleChatMessageOfferBuy(int toUSERID, ChatMessage chatMessage,int presence_action)
   {  
	     ChatServerThread chatServerThread = null;
		 chatServerThread = mapClients.get(toUSERID);
		 SwapHubDAO swapHubDAO = new SwapHubDAO();
	     swapHubDAO.saveMessage(chatMessage);
	     if(chatServerThread != null)
	     {
			 chatServerThread.sendInt(presence_action);
			 chatServerThread.sendText(chatMessage.toJSonStringMessageOfferBuy()); 
			 logger.info(chatServerThread.getID() + "-->"+"["+presence_action+"]" +":"+ chatMessage.toJSonStringMessageOfferBuy());
	     }else{
	    	 swapHubDAO.saveMessageQueue(chatMessage);
	    	 logger.info(" Queue <--"+"["+presence_action+"]" +":"+ chatMessage.toJSonStringMessageOfferBuy());
	     }
   }
   
   public synchronized void handleSendFile(int toUSERID, byte[] buf,ChatMessage chatMessage)
   {  
			ChatServerThread chatServerThread = null;
			chatServerThread = mapClients.get(toUSERID);
			SimpleDateFormat formatter = new SimpleDateFormat("/yyyy/MMdd/");
			String date= formatter.format(Calendar.getInstance().getTime());
			SwapHubDAO swapHubDAO = new SwapHubDAO();
			
			String file_path = "/home/crawler/image"+date;
			chatMessage.file = file_path+toUSERID+"_"+Calendar.getInstance().getTimeInMillis()+".jpg";
			try {
				FileUtil.mkdirs(file_path);
				FileOutputStream fileOutputStream = new FileOutputStream(chatMessage.file);
				fileOutputStream.write(buf);
				fileOutputStream.flush();
				fileOutputStream.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(chatServerThread != null)
		     {
				swapHubDAO.saveMessage(chatMessage);
				//String msg = chatMessage.toJSonStringMessageFileHeader();
				//logger.info(chatServerThread.getID() + "-->"+"["+ChatConstant.M_TYPE_FILE_IMAGE+"]" +":"+ msg);
				//chatServerThread.sendInt(ChatConstant.M_TYPE_FILE_IMAGE);
				//chatServerThread.sendText(msg);
				//chatServerThread.sendFileFromArrayBuffer(buf);
				
				chatServerThread.putMessage(chatMessage);
				
		     }else{
		    	 swapHubDAO.saveMessageQueue(chatMessage);
		    	 logger.info(" Queue <--"+"["+ChatConstant.M_TYPE_FILE_IMAGE+"]" +":"+ chatMessage.toJSonStringMessageFileHeader());
		     }
			buf = null;
   }
   
   public synchronized void handleSendFile(int toUSERID,ChatMessage chatMessage)
   {  
			ChatServerThread chatServerThread = null;
			chatServerThread = mapClients.get(toUSERID);
			SwapHubDAO swapHubDAO = new SwapHubDAO();
			File file = new File(chatMessage.file);
			if(!file.exists())swapHubDAO.deleteMessageQueue(chatMessage.id);
			if(chatServerThread != null&&file.exists())
		     {
				chatServerThread.sendInt(ChatConstant.M_TYPE_FILE_IMAGE);
				chatMessage.file_size = file.length();
				String msg = chatMessage.toJSonStringMessageFileHeader();
				chatServerThread.sendText(msg);
				chatServerThread.sendFile(file);
				swapHubDAO.deleteMessageQueue(chatMessage.id);
				
				logger.info(chatServerThread.getID() + " Queue -->"+"["+ChatConstant.M_TYPE_FILE_IMAGE+"]" +":"+ msg);
		     }
			
   }
   
   public synchronized void handleChatMessageLogText(int toUSERID, ChatMessage chatMessage,int presence_action)
   {  
	     ChatServerThread chatServerThread = null;
	     chatServerThread = mapClients.get(toUSERID);
	     SwapHubDAO swapHubDAO = new SwapHubDAO();
	     
	     if(chatServerThread != null)
	     {
			 chatServerThread.sendInt(presence_action);
			 chatServerThread.sendText(chatMessage.toJSonStringMessageText());
			 swapHubDAO.deleteMessageQueue(chatMessage.id);
			 logger.info(chatServerThread.getID() + "Queue-->"+"["+presence_action+"]" +":"+ chatMessage.toJSonStringMessageText());
				 
	     }
      
   }
   
   public synchronized void handleChatMessageTextFromBuffer(ChatMessage chatMessage)
   {  
	     ChatServerThread chatServerThread = null;
	     chatServerThread = mapClients.get(chatMessage.getTuID());
	     if(chatServerThread != null)
	     {
			 chatServerThread.sendInt(chatMessage.getType());
			 chatServerThread.sendText(chatMessage.toJSonStringMessageText());
			 chatServerThread.removeMessage(chatMessage);
			 logger.info(chatServerThread.getID() + " Queue-->"+"["+chatMessage.getType()+"]" +":"+ chatMessage.toJSonStringMessageText());
				 
	     }
      
   }
   
   public synchronized void handleSendFileFormBuffer(ChatMessage chatMessage)
   {  
			ChatServerThread chatServerThread = null;
			chatServerThread = mapClients.get(chatMessage.getTuID());
			File file = new File(chatMessage.file);
			if(!file.exists())chatServerThread.removeMessage(chatMessage);
			
			if(chatServerThread != null&&file.exists())
		     {
				chatServerThread.sendInt(ChatConstant.M_TYPE_FILE_IMAGE);
				chatMessage.file_size = file.length();
				String msg = chatMessage.toJSonStringMessageFileHeader();
				chatServerThread.sendText(msg);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				chatServerThread.sendFile(file);
				chatServerThread.removeMessage(chatMessage);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				logger.info(chatServerThread.getID() + " Queue -->"+"["+ChatConstant.M_TYPE_FILE_IMAGE+"]" +":"+ msg);
		     }
			
   }
   
   public synchronized void handleChatMessageLogBuy(int toUSERID, ChatMessage chatMessage,int presence_action)
   {  
	     ChatServerThread chatServerThread = null;
	     chatServerThread = mapClients.get(toUSERID);
	     SwapHubDAO swapHubDAO = new SwapHubDAO();
	     
	     if(chatServerThread != null)
	     {
			 chatServerThread.sendInt(presence_action);
			 if(chatServerThread.sendText(chatMessage.toJSonStringMessageOfferBuy()))
				 swapHubDAO.deleteMessageQueue(chatMessage.id);
			 logger.info(chatServerThread.getID() + "Queue-->"+"["+presence_action+"]" +":"+ chatMessage.toJSonStringMessageOfferBuy());
				 
	     }
      
   }
   
	public synchronized void handleChatMessageLogSwap(int toUSERID,
			ChatMessage chatMessage, int presence_action) {
		ChatServerThread chatServerThread = null;
		chatServerThread = mapClients.get(toUSERID);
		SwapHubDAO swapHubDAO = new SwapHubDAO();

		if (chatServerThread != null) {
			chatServerThread.sendInt(presence_action);
			if (chatServerThread.sendText(chatMessage
					.toJSonStringMessageOfferSwap()))
				swapHubDAO.deleteMessageQueue(chatMessage.id);
			logger.info(chatServerThread.getID() + "Queue-->"+"["+presence_action+"]" +":"+ chatMessage
					.toJSonStringMessageOfferSwap());

		}

	}
   
	public synchronized void remove(int ID) {
		int pos = findClient(ID);
		if (pos >= 0) {
			ChatServerThread toTerminate = clients[pos];
			mapClients.remove(toTerminate.getUser().id);
			logger.info("Removing client thread " + ID + " at " + pos);
			if (pos < clientCount - 1)
				for (int i = pos + 1; i < clientCount; i++)
					clients[i - 1] = clients[i];
			clientCount--;
			
			try {
				toTerminate.close();
			} catch (IOException ioe) {
				logger.error("Error closing thread: " + ioe);
			}
		}
	}
   
	private void addThread(Socket socket) {
		if (clientCount < clients.length) {
			logger.info("Client accepted: " + socket);
			clients[clientCount] = new ChatServerThread(this, socket);
			try {
				clients[clientCount].open();
				clients[clientCount].start();
				clientCount++;

			} catch (IOException ioe) {
				logger.error("Error opening thread: " + ioe);
			}
		} else
			logger.error("Client refused: maximum " + clients.length
					+ " reached.");
	}
   
   public static void main(String args[]) { 
	   ChatServer  server = new ChatServer(25003);
	   server.start();
   }
}