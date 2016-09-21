package com.az24.util.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient implements Runnable {
	private Socket socket = null;
	private ChatClientThread chatClientThread = null;
	private static ChatClient chatClient = null;

	private ChatClient(String serverName, int serverPort,int userID, String username) {
		System.out.println("Establishing connection. Please wait ...");
		try {
			socket = new Socket(serverName, serverPort);
			System.out.println("Connected: " + socket);
			run();
			login(userID, username);
		} catch (UnknownHostException uhe) {
			System.out.println("Host unknown: " + uhe.getMessage());
		} catch (IOException ioe) {
			System.out.println("Unexpected exception: " + ioe.getMessage());
		}
	}

	public static ChatClient getInstance(String serverName, int serverPort,int userID, String username) {
		if (chatClient == null){
			chatClient = new ChatClient(serverName, serverPort, userID,  username);
			System.out.println("Khoi tao chat client");
		}else {
			System.out.println("Khong Khoi tao chat client");
		}
		return chatClient;
	}

	
	@Override
	public void run() {
		chatClientThread = new ChatClientThread(chatClient, socket);
	}
	


	public void stop() {
		try {
			if (socket != null)
				socket.close();
			
		} catch (IOException ioe) {
			System.out.println("Error closing ...");
		}
		
		if (chatClientThread != null)
			chatClientThread.close();
		
	}

	public void login(int userID, String username) {
		try {
			chatClientThread.login(userID, username);
		} catch (IOException ioe) {
			System.out.println("login error: " + ioe.getMessage());
			stop();
		}

	}

	public void logout() {
		try {
			chatClientThread.logout();
		} catch (IOException ioe) {
			System.out.println("logout error: " + ioe.getMessage());
			stop();
		}

	}

	public void sendImage(int toUID) throws IOException {
		chatClientThread.sendFile(toUID);
	}

	public void sendMessage(int toUserID, String message) throws IOException {
		chatClientThread.sendMessage(toUserID, message);
	}
	
	public static void main(String args[]) {

		int userID = Integer.parseInt(args[0]);
		String username = args[1];
		ChatClient chatClient = ChatClient.getInstance("127.0.0.1", 25003,userID, username);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		boolean run = true;
		while (run) {
			try {
				String s = br.readLine();
			String[] arrS = s.split(",");
				String action = arrS[0];
				// System.out.println(action);
				int toUID = Integer.parseInt(arrS[1]);
				if ("3".equalsIgnoreCase(action))
					chatClient.sendImage(toUID);
				if ("2".equalsIgnoreCase(action)) {
					String content = arrS[2];
					chatClient.sendMessage(toUID, content);
				}
				if ("1000".equalsIgnoreCase(action)) {
					chatClient.logout();
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Main error: " + e.getMessage());
				//run = false;
				chatClient.stop();
				chatClient=null;
			}catch (Exception e) {
				e.printStackTrace();
				System.out.println("Main error: " + e.getMessage());
				//run = false;
				chatClient.logout();
				chatClient.stop();
				chatClient=null;
			}
			try {
				System.out.println("ngu");
				Thread.sleep(222);
				
			} catch (InterruptedException ex) {
				// logging
				Thread.currentThread().interrupt();
			}
			ChatClient.getInstance("127.0.0.1", 25003,userID, username);
		}
		

	}

	
}