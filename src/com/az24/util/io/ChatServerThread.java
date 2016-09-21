package com.az24.util.io;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class ChatServerThread extends Thread {
	private ChatServer server = null;
	private Socket socket = null;
	private DataInputStream streamIn = null;
	private DataOutputStream streamOut = null;
	private UserChat USERCHAT = null;
	private int ID = -1;
	private final static Logger logger = Logger.getLogger(ChatServerThread.class);
	private ArrayList<ChatMessage> arrChatMessages = null;
	
	public ChatServerThread(ChatServer _server, Socket _socket) {
		super();
		server = _server;
		socket = _socket;
		ID = socket.getPort();
		arrChatMessages = new ArrayList<ChatMessage>();
	}
	

	public int getID() {
		return ID;
	}

	public UserChat getUser() {
		return USERCHAT;
	}
	
	public ArrayList<ChatMessage> getListMessage(){
		ArrayList<ChatMessage> arrayList = new ArrayList<ChatMessage>();
		for (ChatMessage chatMessage : arrChatMessages) {
			arrayList.add(chatMessage);
		}
		return arrayList;
	}
	
	public void putMessage(ChatMessage chatMessage){
		arrChatMessages.add(chatMessage);
	}
	
	public void removeMessage(ChatMessage chatMessage){
		arrChatMessages.remove(chatMessage);
	}

	public void run() {
		logger.info(ID + " running.");
		boolean running = true;

		while (running) {
			try {
				int m_type = receiveInt();
				logger.info(ID + " Type message:" + m_type);

				switch (m_type) {
				case ChatConstant.M_TYPE_LOGIN:// login
					processLogin();
					break;
				case ChatConstant.M_TYPE_TEXT:// send Message Text
					processSendMessageChat();
					break;
				case ChatConstant.M_TYPE_FILE_IMAGE:// send File Image
					processSendFileChat();
					break;
				case ChatConstant.M_TYPE_OFFER_BUY:// SEND OFFER BUY
					processSendOfferBuy(ChatConstant.M_TYPE_OFFER_BUY);
					break;
				case ChatConstant.M_TYPE_OFFER_SWAP:
					processSendOfferSwap(ChatConstant.M_TYPE_OFFER_SWAP);
					break;
				case ChatConstant.M_TYPE_OFFER_BUY_AGREE:
					processSendOfferBuy(ChatConstant.M_TYPE_OFFER_BUY_AGREE);
					break;
				case ChatConstant.M_TYPE_OFFER_BUY_DENY:
					processSendOfferBuy(ChatConstant.M_TYPE_OFFER_BUY_DENY);
					break;
				case ChatConstant.M_TYPE_OFFER_BUY_CANCEL:
					processSendOfferBuy(ChatConstant.M_TYPE_OFFER_BUY_CANCEL);
					break;
				case ChatConstant.M_TYPE_OFFER_SWAP_AGREE:
					processSendOfferSwap(ChatConstant.M_TYPE_OFFER_SWAP_AGREE);
					break;
				case ChatConstant.M_TYPE_OFFER_SWAP_DENY:
					processSendOfferSwap(ChatConstant.M_TYPE_OFFER_SWAP_DENY);
					break;
				case ChatConstant.M_TYPE_OFFER_SWAP_CANCEL:
					processSendOfferSwap(ChatConstant.M_TYPE_OFFER_SWAP_CANCEL);
					break;
				case ChatConstant.M_TYPE_PING:// send Presentation
					break;
				case ChatConstant.M_TYPE_LOGOUT:// logout
					processLogout();
					break;
				}
			} catch (IOException ioe) {
				logger.error(ID +" "+ ioe.getMessage());
				processLogout();
				running = false;
			} catch (Exception e) {
				logger.error(ID + e.getMessage());
				processLogout();
				running = false;
			}
		}
	}

	public boolean sendText(String msg) {
		boolean kq = false;
		try {
			streamOut.writeUTF(msg);
			streamOut.flush();
			kq = true;
		} catch (IOException ioe) {
			logger.error(ID + ioe.getMessage());
			server.remove(ID);
		}
		return kq;
	}

	public void sendInt(int action) {
		try {
			streamOut.writeInt(action);
			streamOut.flush();
		} catch (IOException ioe) {
			logger.error(ID + ioe.getMessage());
			server.remove(ID);
		}
	}

	public void sendLong(long size) {
		try {
			streamOut.writeLong(size);
			streamOut.flush();
		} catch (IOException ioe) {
			logger.error(ID +" "+ ioe.getMessage());
			server.remove(ID);
		}
	}

		public int receiveInt() throws IOException {
		int intAction = streamIn.readInt();
		return intAction;
	}

	// Receive Text
	public String receiveText() throws IOException {
		String message = streamIn.readUTF();
		return message;
	}

	// Function Send Data
	// Send File Short
	public void sendFileShort(File file) throws IOException {
		FileInputStream fileIn = new FileInputStream(file);

		byte[] buf = new byte[Short.MAX_VALUE];
		int bytesRead;
		while ((bytesRead = fileIn.read(buf)) != -1) {
			streamOut.writeShort(bytesRead);
			streamOut.write(buf, 0, bytesRead);
		}
		streamOut.writeShort(-1);
		fileIn.close();
	}

	// Receive File When File Send Short
	public void receiveFileShort(File file) throws IOException {
		FileOutputStream fileOut = new FileOutputStream(file);
		byte[] buf = new byte[Short.MAX_VALUE];
		int bytesSent;
		while ((bytesSent = streamIn.readShort()) != -1) {
			streamIn.readFully(buf, 0, bytesSent);
			fileOut.write(buf, 0, bytesSent);
		}
		fileOut.close();
	}

	// Test send File To CLient
	public void sendFile(File file) {
		try {
			FileInputStream fileIn = new FileInputStream(file);
			int bytes = 0;
			byte[] buffer = new byte[8192];
			int len;
			while ((len = fileIn.read(buffer)) > 0) {
				streamOut.write(buffer, 0, len);
				streamOut.flush();
				bytes += len;
			}
			fileIn.close();
			logger.info(ID + " Transfer completed, " + bytes + " bytes sent");
		} catch (IOException ioe) {
			logger.error(ID + ioe.getMessage());
			server.remove(ID);
		}
	}

	// Send File To Client From Array Buffer
	public void sendFileFromArrayBuffer(byte[] buf) {
		try {
			int totalRead = 0;
			int len = 8192;
			while (totalRead < buf.length) {
				len = (buf.length - totalRead) > len ? len : buf.length
						- totalRead;
				streamOut.write(buf, totalRead, len);
				streamOut.flush();
				totalRead += len;
			}
			logger.error(ID + "Tong dung luong gui"+totalRead);
		} catch (IOException ioe) {
			logger.error(ID + ioe.getMessage());
			server.remove(ID);
		}

	}

	// Receive From Client Save To File Template
	public void receiveFileToFileTemp() throws IOException {
		File temp = File.createTempFile(ID
				+ Calendar.getInstance().getTimeInMillis() + "", ".tmp");
		FileOutputStream out = new FileOutputStream(temp);
		byte[] buffer = new byte[8192];

		System.out.println(ID + " Start receive file");
		long size = streamIn.readLong();
		System.out.println(ID + " file size:" + size);

		int bytesRead = 0;
		while (size > 0
				&& (bytesRead = streamIn.read(buffer, 0,
						(int) Math.min(buffer.length, size))) != -1) {
			out.write(buffer, 0, bytesRead);
			size -= bytesRead;
			out.flush();
		}
		
		out.close();
	}

	// Receive File To Buffer
	public ByteArrayOutputStream receiveFileToBuffer(long size) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			logger.info(ID + " <-- Start receive file size " + size + " bytes sent");
			int bytesRead;

			byte[] buffer = new byte[socket.getReceiveBufferSize()];
			while (size > 0
					&& (bytesRead = streamIn.read(buffer, 0,
							(int) Math.min(buffer.length, size))) != -1) {
				baos.write(buffer, 0, bytesRead);
				size -= bytesRead;

				baos.flush();
			}

			logger.info(ID + " <-- End receive file");
		} catch (Exception e) {
			logger.error(ID + e.getMessage());
		}
		
		InputStream in = new ByteArrayInputStream(baos.toByteArray());
		try {
			BufferedImage bImageFromConvert = ImageIO.read(in);
			int type = bImageFromConvert.getType() > 0 ? bImageFromConvert.getType(): 5;
			int width = bImageFromConvert.getWidth();
			int height = bImageFromConvert.getHeight();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return baos;
	}

	// Receive File To Buffer When File Send Short
	public ByteArrayOutputStream receiveFileToBufferShort() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[8192];
		int bytesSent;
		while ((bytesSent = streamIn.readShort()) != -1) {
			streamIn.readFully(buf, 0, bytesSent);
			baos.write(buf, 0, bytesSent);
			baos.flush();
		}
		return baos;
	}

	// Process Functions
	private void processLogin() throws IOException {
		String userLogin;
		userLogin = this.receiveText();
		JSONObject user = (JSONObject) JSONSerializer.toJSON(userLogin);

		USERCHAT = new UserChat();
		USERCHAT.id = user.getInt("id");
		USERCHAT.name = user.getString("username");
		// Put Map Thread Chat Server
		server.mapClients.put(USERCHAT.id, this);
		
		ChatServerSendThread chatServerSendLogThread = new ChatServerSendThread(server,this, socket,USERCHAT.id) ;
        chatServerSendLogThread.start();

		logger.info(ID + " Login:" + USERCHAT.name +" "+ USERCHAT.id);

	}

	private void processSendMessageChat() throws IOException {
		String message = this.receiveText();
		JSONObject json = (JSONObject) JSONSerializer.toJSON(message);
		logger.info(ID +" <-- " + message);
		int fuID = json.getInt("fuID");
		int tuID = json.getInt("tuID");
		int pID = json.getInt("pID");
		String content = json.getString("content");
		ChatMessage chatMessage = new ChatMessage(fuID, tuID, pID, content, 2);
		logger.info(ID +" <-- " + chatMessage.toJSonStringMessageText());

		server.handleChatMessageText(tuID, chatMessage);

		

	}

	private void processSendOfferBuy(int action_presence) throws IOException {
		String message = this.receiveText();
		JSONObject json = (JSONObject) JSONSerializer.toJSON(message);
		int fuID = json.getInt("fuID");
		int tuID = json.getInt("tuID");
		int pID = json.getInt("pID");
		
		String content = json.getString("content");
		double price = json.getDouble("price");
		int quantity = json.getInt("quantity");
		int ship = json.getInt("ship");
		
		ChatMessage chatMessage = new ChatMessage(fuID, tuID, pID, content,action_presence,
				price, quantity, ship);
		logger.info(ID +" <-- " + chatMessage.toJSonStringMessageOfferBuy());
		server.handleChatMessageOfferBuy(tuID, chatMessage, action_presence);

		

	}

	private void processSendOfferSwap(int action_presence) throws IOException {
		String message = this.receiveText();
		JSONObject json = (JSONObject) JSONSerializer.toJSON(message);
		int fuID = json.getInt("fuID");
		int tuID = json.getInt("tuID");
		int pID = json.getInt("pID");
		String content = json.getString("content");
		String psID = json.getString("psID");
		double price = json.getDouble("price");
		int quantity = json.getInt("quantity");
		int ship = json.getInt("ship");
		ChatMessage chatMessage = new ChatMessage(fuID, tuID, pID, content, action_presence,
				price, quantity, ship, psID);
		logger.info(ID +" <-- " + chatMessage.toJSonStringMessageOfferSwap());
		server.handleChatMessageOfferSwap(tuID, chatMessage,action_presence);

		

	}

	private void processSendFileChat() throws IOException {
		String message = this.receiveText();
		JSONObject json = (JSONObject) JSONSerializer.toJSON(message);
		int fuID = json.getInt("fuID");
		int tuID = json.getInt("tuID");
		int pID = json.getInt("pID");
		long fileSize = json.getLong("size");
		ChatMessage chatMessage = new ChatMessage(fuID, tuID, pID, "", ChatConstant.M_TYPE_FILE_IMAGE,
				"", fileSize);
		logger.info(ID +" <-- " + chatMessage.toJSonStringMessageFileHeader());
		ByteArrayOutputStream baos = receiveFileToBuffer(fileSize);
		byte[] buf = baos.toByteArray();
		server.handleSendFile(tuID, buf,chatMessage);
		baos.reset();
		baos.close();
	}

	private void processLogout() {
		server.remove(ID);

	}

	public void open() throws IOException {
		streamIn = new DataInputStream(new BufferedInputStream(
				socket.getInputStream()));
		streamOut = new DataOutputStream(new BufferedOutputStream(
				socket.getOutputStream()));
	}

	public void close() throws IOException {
		if (socket != null)
			socket.close();
		if (streamIn != null)
			streamIn.close();
		if (streamOut != null)
			streamOut.close();
	}

}