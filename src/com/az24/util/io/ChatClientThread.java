package com.az24.util.io;

import java.net.*;
import java.util.Calendar;
import java.io.*;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class ChatClientThread extends Thread {
	private Socket socket = null;
	private ChatClient client = null;
	private DataInputStream streamIn = null;
	private DataOutputStream streamOut = null;
	private UserChat USERCHAT = null;

	public ChatClientThread(ChatClient _client, Socket _socket) {
		client = _client;
		socket = _socket;
		open();
		start();
	}

	// nhan du lieu tu server
	public void open() {
		try {
			streamIn = new DataInputStream(socket.getInputStream());
			streamOut = new DataOutputStream(socket.getOutputStream());
		} catch (IOException ioe) {
			System.out.println("Error getting input stream: " + ioe);
			client.stop();
		}
	}

	public void close() {
		try {
			if (streamIn != null)
				streamIn.close();
			if (streamOut != null)
				streamOut.close();
		} catch (IOException ioe) {
			System.out.println("Error closing input stream: " + ioe);
		}
	}

	// Process Message Receive
	public void run() {
		boolean running = true;
		int intAction = 0;

		while (running) {
			try {
				intAction = receivePresentationAction();
				System.out.println("Receive Presentation Action: " + intAction);

				switch (intAction) {
				case 2:// receive Message Text chat
					processReceiveMessage();
					break;
				case 3:// receive File Image chat
					processReceiveFile();
					break;
				case 4:// receive Message Text chat Room
					break;
				case 5:// receive File Image chat Room
					break;
				case 100:// receive Presentation
					break;
				case 1000:// logout
					break;
				}
			} catch (IOException ioe) {
				System.out.println("ChatClientThread RUN IOException ERROR reading: " + ioe.getMessage());
				running = false;
				
			}catch (Exception e) {
				System.out.println("ChatClientThread RUN Exception ERROR reading: " + e.getMessage());
				
				running = false;
			}
		}
		
		
	}

	public void sendMessage(int toUserID, String message) throws IOException {
		sendPresentationAction(ChatConstant.M_TYPE_TEXT);
		ChatMessage chatMessage = new ChatMessage(USERCHAT.id, toUserID,
				message, USERCHAT.name, "", "", "", Calendar
						.getInstance().getTime().toString());
		streamOut.writeUTF(chatMessage.toJSonString());
		streamOut.flush();
	}

	public String receiveMessage() throws IOException {
		String message = streamIn.readUTF();
		return message;
	}

	public void sendPresentationAction(int action) throws IOException {
		streamOut.writeInt(action);
		streamOut.flush();
	}
	
	public void logout() throws IOException {
		streamOut.writeInt(ChatConstant.M_TYPE_LOGOUT);
		streamOut.flush();
	}

	public void sendFileSize(long size) throws IOException {
		streamOut.writeLong(size);
		streamOut.flush();
	}

	public void sendToUserID(int toUserID) throws IOException {
		streamOut.writeInt(toUserID);
		streamOut.flush();
	}

	// Receive Presentation Action
	public int receivePresentationAction() throws IOException {
		int intAction = streamIn.readInt();
		return intAction;
	}

	public void processReceiveMessage() throws IOException {
		String message = streamIn.readUTF();
		JSONObject json = (JSONObject) JSONSerializer.toJSON(message);
		System.out.println(json.getString("fuName") + ":"
				+ json.getString("content"));

	}

	public void processReceiveFile() throws IOException {
		receiveFile();
	}

	// Send File Short Test
	public void sendFileShort(File file) throws IOException {
		FileInputStream fileIn = new FileInputStream(
				"/home/crawler/wallpage.jpg");
		byte[] buf = new byte[8192];
		int bytesRead;
		while ((bytesRead = fileIn.read(buf)) != -1) {
			streamOut.writeShort(bytesRead);
			streamOut.write(buf, 0, bytesRead);
		}
		streamOut.writeShort(-1);
		fileIn.close();
	}

	// Send File Short Test
	public void receiveFileShort() throws IOException {
		FileOutputStream fileOut = new FileOutputStream(new File(
				"/home/crawler/client.jpg"));
		byte[] buf = new byte[8192];
		int bytesSent;
		while ((bytesSent = streamIn.readShort()) != -1) {
			streamIn.readFully(buf, 0, bytesSent);
			fileOut.write(buf, 0, bytesSent);
		}
		fileOut.close();
	}

	public void sendFile(int toUID) throws IOException {
		File file = new File("/home/crawler/wallpage.jpg");
		FileInputStream fileIn = new FileInputStream(file);
		int bytes = 0;
		byte[] buffer = new byte[8192];
		int len;

		sendPresentationAction(ChatConstant.M_TYPE_FILE_IMAGE);
		sendToUserID(toUID);
		sendFileSize(file.length());

		while ((len = fileIn.read(buffer)) > 0) {
			streamOut.write(buffer, 0, len);
			bytes += len;
		}
		System.out.println("Transfer completed, " + bytes + " bytes sent");
		fileIn.close();
	}

	public void receiveFile() throws IOException {

		FileOutputStream fileOut = new FileOutputStream(new File(
				"/home/crawler/client"
						+ Calendar.getInstance().getTimeInMillis() + ".jpg"));
		byte[] buf = new byte[8192];
		int bytesSent;
		while ((bytesSent = streamIn.read(buf)) != -1) {
			fileOut.write(buf, 0, bytesSent);
		}
		fileOut.close();
		System.out.println("receiveFile completed, " + bytesSent
				+ " bytes sent");
	}

	public void login(int userID, String username) throws IOException {
		USERCHAT = new UserChat();
		USERCHAT.id = userID;
		USERCHAT.name = username;

		String strLogin = "{\"user\":{\"id\":\"" + userID
				+ "\",\"username\":\"" + username + "\"}}";
		 sendPresentationAction(ChatConstant.M_TYPE_LOGIN);
		streamOut.writeUTF(strLogin);
		streamOut.flush();
	}

}