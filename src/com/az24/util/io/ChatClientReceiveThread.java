package com.az24.util.io;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Calendar;

import javax.imageio.ImageIO;

import org.omg.CORBA.portable.InputStream;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class ChatClientReceiveThread extends Thread {
	private DataInputStream streamIn = null;
	private DataOutputStream streamOut = null;
	private UserChat USERCHAT = null;
	private int userID = 0;
	private String username = null;

	private Socket socket;
	private final String host;
	private final int port;
	private final int connectionRetryAfter = 10 * 1000;

	public ChatClientReceiveThread(String _serverName, int _serverPort,int _userID, String _username) {
		port = _serverPort;
		host = _serverName;
		userID = _userID;
		username = _username;
	}

	@Override
	public void run() {
		tryCreateSocket();
		listenToServer();
	}

	private void listenToServer() {
		int intAction = 0;
		//BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			try {
				intAction = receivePresentationAction();
				System.out.println("Receive Presentation Action: " + intAction);
				// Receive
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
				System.out.println("Cho nhan");
			} catch (IOException e) {
				// logging
				e.printStackTrace();
				System.out.println("ChatClientThread RUN IOException ERROR reading: "
								+ e.getMessage());
				closeSocket();
			} /*
			 * finally { closeSocket(); }
			 */
			// At this point, either an exception occured or the stream equals
			// null (which means it's closed?)
			tryCreateSocket();
		}
	}

	public void sendMessage(int toUserID, String message) throws IOException {
		sendPresentationAction(ChatConstant.M_TYPE_TEXT);
		ChatMessage chatMessage = new ChatMessage(USERCHAT.id, toUserID,
				message, USERCHAT.name, "", "", "", Calendar.getInstance()
						.getTime().toString());
		streamOut.writeUTF(chatMessage.toJSonString());
		streamOut.flush();
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
	
	public String receiveMessage() throws IOException {
		String message = streamIn.readUTF();
		return message;
	}
	
	// Receive Presentation Action
	public int receivePresentationAction() throws IOException {
		int intAction = streamIn.readInt();
		return intAction;
	}

	public void processReceiveMessage() throws IOException {
		String message = streamIn.readUTF();
		JSONObject json = (JSONObject) JSONSerializer.toJSON(message);
		System.out.println(json.getString("fuID") + ":"
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
		// Get File Header
		String message = streamIn.readUTF();
		JSONObject json = (JSONObject) JSONSerializer.toJSON(message);
		System.out.println(json.getString("fuID") + ":"
				+ json.getString("pID")+":"+json.getString("size"));
		
		FileOutputStream fileOut = new FileOutputStream(new File("/home/crawler/client"
						+ Calendar.getInstance().getTimeInMillis() + ".jpg"));
		byte[] buf = new byte[8192];
		int bytesSent;long file_size = Long.parseLong(json.getString("size"));
		while (file_size>0&&(bytesSent = streamIn.read(buf)) != -1) {
			fileOut.write(buf, 0, bytesSent);
			file_size -=bytesSent;
			
		}
		fileOut.close();
		System.out.println("receiveFile completed, " + file_size
				+ " bytes sent");
	}
	
	public ByteArrayOutputStream receiveFileToBuffer() throws IOException {
		String message = streamIn.readUTF();
		JSONObject json = (JSONObject) JSONSerializer.toJSON(message);
		System.out.println(json.getString("fuID") + ":"
				+ json.getString("pID")+":"+json.getString("size"));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		long file_size = Long.parseLong(json.getString("size"));
		try {
			int bytesRead;

			byte[] buffer = new byte[socket.getReceiveBufferSize()];
			while (file_size > 0
					&& (bytesRead = streamIn.read(buffer, 0,
							(int) Math.min(buffer.length, file_size))) != -1) {
				baos.write(buffer, 0, bytesRead);
				file_size -= bytesRead;
				System.out.println(file_size);
				baos.flush();
			}

			System.out.println( " <-- End receive file");
			ByteArrayInputStream in = new ByteArrayInputStream(baos.toByteArray());
			BufferedImage bImageFromConvert = ImageIO.read(in);

			ImageIO.write(bImageFromConvert, "jpg", new File("/home/crawler/client"
					+ Calendar.getInstance().getTimeInMillis() + ".jpg"));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return baos;
	}

	public void login(int userID, String username) throws IOException {
		USERCHAT = new UserChat();
		USERCHAT.id = userID;
		USERCHAT.name = username;

		String strLogin = "{\"id\":\"" + userID
				+ "\",\"username\":\"" + username + "\"}";
		sendPresentationAction(ChatConstant.M_TYPE_LOGIN);
		streamOut.writeUTF(strLogin);
		streamOut.flush();
	}

	private void tryCreateSocket() {
		try {
			if (socket == null || socket.isClosed()){
				System.out.println(Calendar.getInstance().getTime().toString()
						+ "-->co tao lai");
				socket = new Socket(host, port);
				
				
			streamIn = new DataInputStream(socket.getInputStream());
			streamOut = new DataOutputStream(socket.getOutputStream());
			login(userID, username);}
		} catch (IOException e) {
			// logging
			try {
				Thread.sleep(connectionRetryAfter);
			} catch (InterruptedException ex) {
				// logging
				Thread.currentThread().interrupt();
			}
			// retry
			tryCreateSocket();
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
	
	private void closeSocket() {
		System.out.println(Calendar.getInstance().getTime().toString()
				+ "-->closeSocket");
		close();
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				// logging
			}
		}
	}

	public static void main(String[] args) {
		int userID = Integer.parseInt(args[0]);
		String username = args[1];
		ChatClientReceiveThread chatClientReceiveThread = new ChatClientReceiveThread(
				"127.0.0.1", 25003, userID,username);
		chatClientReceiveThread.run();
	}

}
