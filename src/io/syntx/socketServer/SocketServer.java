package io.syntx.socketServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
* A simple socket server
* @author faheem
*
*/
public class SocketServer {
    
    private ServerSocket serverSocket;
    private int port;
    
    public SocketServer(int port) {
        this.port = port;
    }
    
    public void start() throws IOException {
        System.out.println("Starting the socket server at port:" + port);
        serverSocket = new ServerSocket(port);
        
        //Listen for clients. Block till one connects
        Socket client = null;
        
        while(true){
        	System.out.println("Waiting for clients...");
        	client = serverSocket.accept();
        	System.out.println("The following client has connected:"+client.getInetAddress().getCanonicalHostName());
        	//A client has connected to this server. Send welcome message
            Thread thread = new Thread(new SocketClientHandler(client));
          
            thread.start();
        }     
    }
    
    
    
    /**
    * Creates a SocketServer object and starts the server.
    *
    * @param args
    */
    public static void main(String[] args) {
        // Setting a default port number.
        int portNumber = 9991;
        
        try {
            // initializing the Socket Server
            SocketServer socketServer = new SocketServer(portNumber);
            socketServer.start();
            
            } catch (IOException e) {
            e.printStackTrace();
        }
    }
}