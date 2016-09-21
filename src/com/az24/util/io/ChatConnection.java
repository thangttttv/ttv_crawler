package com.az24.util.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Calendar;

public class ChatConnection implements Runnable {
	public static Socket socket;
	private static DataInputStream streamIn = null;
	private static DataOutputStream streamOut = null;
    private boolean tryToReconnect = true;
    private final Thread heartbeatThread;
    private long heartbeatDelayMillis = 5000;
    private static ChatConnection instance;
    private final Thread thread = new Thread(this);
    
    private ChatConnection(final String server, final int port,final int userID,final String username) {
        connect(server, port,userID,username);
        heartbeatThread = new Thread() {
            public void run() {
                while (tryToReconnect) {
                    //send a test signal
                    try {
                    	System.out.println(666);
                    	streamOut.writeInt(900);
                        sleep(heartbeatDelayMillis);
                    } catch (InterruptedException e) {
                        // You may or may not want to stop the thread here
                        // tryToReconnect = false;
                    } catch (IOException e) {
                        //logger.warn("Server is offline");
                    	System.out.println("heartbeatThread  IOException ERROR writeInt: "
								+ e.getMessage());
                        connect(server, port,userID,username);
                    }catch (Exception e) {
						// TODO: handle exception
                    	System.out.println("heartbeatThread  Exception ERROR writeInt: "
								+ e.getMessage());
                    	 connect(server, port,userID,username);
					}
                }
            };
        };
        heartbeatThread.start();
    }
    
    public static ChatConnection getInstance(String server, int port,int userID,String username) {
        if(instance == null) {
           instance = new ChatConnection(server, port,userID,username);
        }
        return instance;
     }
    
    private void connect(String server, int port,int userID,String username){
        try {
        	if (socket == null || socket.isClosed()){
            socket = new Socket(server, port);
            login(userID,username);
            System.out.println("Connection new");
        	}else System.out.println("Connectioned ");
        } catch (UnknownHostException e) {
           // logger.error(e, e);
        } catch (IOException e) {
           // logger.error(e, e);
        }
    }
    
    public void login(int userID, String username) throws IOException {
		
		String strLogin = "{\"user\":{\"id\":\"" + userID
				+ "\",\"username\":\"" + username + "\"}}";
		streamOut = new DataOutputStream(socket.getOutputStream());
		streamIn = new DataInputStream(socket.getInputStream());
		streamOut.writeInt(1);
		streamOut.flush();
		streamOut.writeUTF(strLogin);
		streamOut.flush();
		
	}
    
    public static Socket getSocket()
    {
    	return socket;
    }
    
    public static DataInputStream getInputStream()
    {
    	try {
    		if(socket != null && !socket.isClosed())
			streamIn = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			
		}
    	return streamIn;
    }
    
    public static DataOutputStream getOutputStream()
    {
    	try {
    		if(socket != null && !socket.isClosed())
			streamOut = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return streamOut;
    }

    public void shutdown() {
        tryToReconnect = false;
    }
    
    public void start() throws IOException {
        thread.start();
      }

	
    @Override
	public void run() {
		listenToServer();
	}

	private void listenToServer() {
		while (true) {
			try {
				String message = streamIn.readUTF();
				System.out.println("Tin Nhan:"+message);
				} catch (IOException e) {
				// logging
				System.out.println("listenToServer  IOException ERROR reading: "
								+ e.getMessage());
				try {
					socket=null;
		            Thread.sleep(1000);
		        } catch (InterruptedException e1) {
		             e.printStackTrace();
		        }
				
			} 
		}
	}
	
	public void sendMessage(int toUserID, String message) throws IOException {
		sendPresentationAction(ChatConstant.M_TYPE_TEXT);
		ChatMessage chatMessage = new ChatMessage(1, toUserID,
				message, "thangtt", "", "", "", Calendar
						.getInstance().getTime().toString());
		System.out.println(chatMessage.toJSonString());
		String s = chatMessage.toJSonString();
		streamOut.writeUTF(s);
		streamOut.flush();
		//streamOut.writeInt(900);
	}

	public String receiveMessage() throws IOException {
		String message = streamIn.readUTF();
		return message;
	}

	public void sendPresentationAction(int action) throws IOException {
		streamOut.writeInt(action);
		streamOut.flush();
	}
	
	
	public static void main(String[] args) {
		//int userID = Integer.parseInt(args[0]);
		//String username = args[1];
		ChatConnection chatConnection = new ChatConnection(
				"210.245.90.243", 25003, 1,"thangtt");
		try {
			chatConnection.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		boolean run = true;
		while (run) {
			try {
				String s = "2,2,xinchao";
				String[] arrS = s.split(",");
				String action = arrS[0];
				// System.out.println(action);
				int toUID = Integer.parseInt(arrS[1]);
					if ("2".equalsIgnoreCase(action)) {
					String content = arrS[2];
					chatConnection.sendMessage(toUID, content);
				}
				
			} catch (IOException e) {
				System.out.println("Main error: " + e.getMessage());
			
			}catch (Exception e) {
				System.out.println("Main error: " + e.getMessage());
				//run = false;
					}
			try {
				System.out.println("Tam Sleep");
				Thread.sleep(10000);
				
			} catch (InterruptedException ex) {
				// logging
				Thread.currentThread().interrupt();
			}
			
		}
	}

}
