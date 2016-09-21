package com.az24.util.io;

import java.net.*;
import java.io.*;

public class Network extends Thread {

	protected InputStream Incoming;
	protected OutputStream Outgoing;
	public Socket socket = null; // Client's Connection To The Server
	public ServerSocket port = null; // Server's Connection To The Client

	public Network() {
	}

	protected String ReadNet(Socket sock) throws IOException // Read from the
																// Network
	{
		Incoming = sock.getInputStream();
		String string = "";
		char a;
		while ((a = (char) Incoming.read()) != '\n')
			string = string + a + "";
		return string;
	}

	protected void WriteNet(Socket socket, String string) throws IOException // Send
																				// over
																				// the
																				// Network
	{
		Outgoing = socket.getOutputStream();
		if (string.charAt(string.length() - 1) != '\n')
			string = string + '\n';
		for (int a = 0; a < string.length(); a++)
			Outgoing.write(string.charAt(a));
	}

	public Socket Connect(String IPAddress, int Port, int Log,
			boolean isServer, int TimeOut) {
		int TimeOutTries = 0;
		if (isServer) // Establish a connection and wait for a Client to connect
		{
			try {
				port = new ServerSocket(Port, Log);
				System.out.println(InetAddress.getLocalHost()
						+ " is waiting for connections.");
				socket = port.accept();
				System.out.println(socket.getInetAddress() + " Connected.");
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		} else // Establish a connection to the Server
		{
			do {
				try {
					socket = new Socket(IPAddress, Port); // Will only try once,
															// need to figure
															// out how to get
															// this to try
															// multiple times
					System.out.println("The Client is connected to "
							+ IPAddress + ":" + Port);
				} catch (Exception e) {
				}
				if (!Status()) // Check If Connected. If Not Then Try To
								// Reconnect To The Server.
				{
					TimeOutTries++;
					System.out.println(TimeOutTries);
				} // Currently Not Working Right
			} while (!Status() && TimeOutTries < TimeOut);
			if (TimeOutTries == TimeOut)
				System.out.println("Failed to connect to " + IPAddress + ":"
						+ Port);
		}
		return socket;
	}

	public void Disconnect(boolean Server) throws IOException {
		Incoming.close();
		Outgoing.close();
		socket.close();
		if (Server)
			port.close();
		System.out.println("Closed the connection");
	}

	public boolean Status() // Return if Connection is Up or Down
	{
		boolean status = false;
		try {
			status = socket.isConnected();
		} catch (Exception e) {
			status = false;
		}
		return status;
	}
}