package com.az24.util.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Calendar;

public class Receiver implements Runnable {

	private Socket socket;
	private final String host;
	private final int port;
	private final int connectionRetryAfter = 10 * 1000;

	public Receiver(String host, int port) {
		this.host = host;
		this.port = port;
	}

	@Override
	public void run() {
		tryCreateSocket();
		listenToServer();
	}

	private void listenToServer() {
		String receivedLine;
		BufferedReader buf;
		while (true) {
			try {
				buf = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				while ((receivedLine = buf.readLine()) != null) {
					// do something with 'inputLine'
				}
			} catch (IOException e) {
				// logging
				e.printStackTrace();
				System.out.println("ChatClientThread RUN IOException ERROR reading: " + e.getMessage());
			} finally {
				closeSocket();
			}
			// At this point, either an exception occured or the stream equals
			// null (which means it's closed?)
			tryCreateSocket();
		}
	}

	private void tryCreateSocket() {
		try {
			System.out.println(Calendar.getInstance().getTime().toString()+"-->tryCreateSocket");
			socket = new Socket(host, port);
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

	private void closeSocket() {
		System.out.println(Calendar.getInstance().getTime().toString()+"-->closeSocket");
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				// logging
			}
		}
	}
	
	public static void main(String[] args) {
		Receiver receiver = new Receiver("127.0.0.1", 25003);
		receiver.run();
	}
}
