package com.az24.util.io;

import java.io.IOException;

public class ChatConnection2 {

	public static void main(String[] args) {
		ChatConnection chatConnection =  ChatConnection.getInstance(
				"210.245.90.243", 25003, 10,"van");
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
