package com.az24.util.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ChatClientT {
	public static void main(String args[]) {

		int userID = Integer.parseInt(args[0]);
		String username = args[1];
		ChatClient client = ChatClient.getInstance("127.0.0.1", 25003,userID, username);
		//client.login(userID, username);
		boolean run = true;
		while (run) {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			try {
				String s = br.readLine();
				String[] arrS = s.split(",");
				String action = arrS[0];
				// System.out.println(action);
				int toUID = Integer.parseInt(arrS[1]);
				if ("3".equalsIgnoreCase(action))
					client.sendImage(toUID);
				if ("2".equalsIgnoreCase(action)) {
					String content = arrS[2];
					client.sendMessage(toUID, content);
				}
				if ("1000".equalsIgnoreCase(action)) {
					client.logout();
					run = false;
				}
			} catch (IOException e) {
				e.printStackTrace();
				run = false;
				client.logout();
			}catch (Exception e) {
				e.printStackTrace();
				run = false;
				client.logout();
			}
		}
		

	}
}
