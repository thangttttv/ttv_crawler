package com.az24.util.io;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import org.apache.log4j.Logger;

public class ChatServerSendThread extends Thread {
	private ChatServer server = null;
	private Socket socket = null;
	private int ID = -1;
	private int userID  = 0;
	private ChatServerThread chatServerThread = null;
	private final static Logger logger = Logger.getLogger(ChatServerSendThread.class);
	
	public ChatServerSendThread(ChatServer _server,ChatServerThread _chatServerThread, Socket _socket,int _userID) {
		super();
		server = _server;
		socket = _socket;
		ID = socket.getPort();
		userID = _userID;
		chatServerThread  = _chatServerThread;
	}

	public int getID() {
		return ID;
	}

	public void run() {
		logger.info(ID + "--> running send log.");
		boolean running = true;
		SwapHubDAO swapHubDAO = new SwapHubDAO();
		while (running) {
			ArrayList<ChatMessage> messages = new ArrayList<ChatMessage>();
			messages = swapHubDAO.getListMessageQueue(userID);
			if(messages.size()==0) break;
			int i = 0;
			try {
				while (i < messages.size()) {
					processMessage(messages.get(i));
					i++;
				}
			} catch (IOException e) {
				logger.error(""+e.getStackTrace());
			}

		}
		
		logger.info(ID + "---> end log.");
		while (!socket.isClosed()) {
			ArrayList<ChatMessage> messages = chatServerThread.getListMessage();
			
			for (ChatMessage chatMessage : messages) {
				switch (chatMessage.getType()) {
				case 2:
					server.handleChatMessageTextFromBuffer(chatMessage);
					break;
				case 3:
					System.out.println(chatMessage.getFile());
					server.handleSendFileFormBuffer(chatMessage);
					break;	
				default:
					break;
				}
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private void processMessage(ChatMessage message) throws IOException{
		switch (message.getType()) {
			case ChatConstant.M_TYPE_TEXT:
				processSendMessageChatText(message,
						ChatConstant.M_TYPE_TEXT);
				break;
			case ChatConstant.M_TYPE_FILE_IMAGE:
				processSendMessageFile(message,
						ChatConstant.M_TYPE_FILE_IMAGE);
				break;
			case ChatConstant.M_TYPE_OFFER_BUY:
				processSendMessageChatBuy(message,
						ChatConstant.M_TYPE_OFFER_BUY);
				break;
			case ChatConstant.M_TYPE_OFFER_SWAP:
				processSendMessageChatSwap(message,
						ChatConstant.M_TYPE_OFFER_SWAP);
				break;
			case ChatConstant.M_TYPE_OFFER_BUY_AGREE:
				processSendMessageChatBuy(message,
						ChatConstant.M_TYPE_OFFER_BUY_AGREE);
				break;
			case ChatConstant.M_TYPE_OFFER_BUY_DENY:
				processSendMessageChatBuy(message,
						ChatConstant.M_TYPE_OFFER_BUY_DENY);
				break;
			case ChatConstant.M_TYPE_OFFER_BUY_CANCEL:
				processSendMessageChatBuy(message,
						ChatConstant.M_TYPE_OFFER_BUY_CANCEL);
				break;
			case ChatConstant.M_TYPE_OFFER_SWAP_AGREE:
				processSendMessageChatSwap(message,
						ChatConstant.M_TYPE_OFFER_SWAP_AGREE);
				break;
			case ChatConstant.M_TYPE_OFFER_SWAP_DENY:
				processSendMessageChatSwap(message,
						ChatConstant.M_TYPE_OFFER_SWAP_DENY);
				break;
			case ChatConstant.M_TYPE_OFFER_SWAP_CANCEL:
				processSendMessageChatSwap(message,
						ChatConstant.M_TYPE_OFFER_SWAP_CANCEL);
				break;
		}
	}

	private void processSendMessageChatText(ChatMessage chatMessage,
			int action_presence) throws IOException {
		server.handleChatMessageLogText(chatMessage.getTuID(), chatMessage, action_presence);
	}

	private void processSendMessageChatBuy(ChatMessage chatMessage,
			int action_presence) throws IOException {
		server.handleChatMessageLogBuy(chatMessage.getTuID(), chatMessage, action_presence);
	}

	private void processSendMessageChatSwap(ChatMessage chatMessage,
			int action_presence) throws IOException {
		server.handleChatMessageLogSwap(chatMessage.getTuID(), chatMessage, action_presence);
	}

	private void processSendMessageFile(ChatMessage chatMessage,int action_presence) throws IOException {
			server.handleSendFile(chatMessage.getTuID(), chatMessage);
		
	}

}
