package com.az24.util.io;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientReviceFile {
	public static void main(String[] args) throws IOException {
		Socket sock = new Socket("127.0.0.1", 13267);
		System.out.println("Connecting to : " + sock);

		DataInputStream in = new DataInputStream(sock.getInputStream());
		FileOutputStream out = new FileOutputStream("/home/crawler/wallpage222fdfd.jpg");

		int bytes = 0;
		byte[] buffer = new byte[8192];
		int len;

		long start = System.currentTimeMillis();

		while ((len = in.read(buffer)) > 0) {
		    out.write(buffer, 0, len);
		    bytes += len;
		}

		long end = System.currentTimeMillis();

		out.close();
		sock.close();

		double kbps = (bytes / 1000) / ((end - start) / 1000);
		System.out.println("Speed: " + kbps + " kbps");

	}
}
