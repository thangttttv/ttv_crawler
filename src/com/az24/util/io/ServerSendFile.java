package com.az24.util.io;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.omg.CORBA.portable.OutputStream;

public class ServerSendFile {
	public static void main(String[] args) throws IOException {
		ServerSocket servsock = new ServerSocket(13267);
		while (true) {
		    System.out.println("Waiting...");

		    Socket sock = servsock.accept();
		    System.out.println("Accepted connection : " + sock);

		    File myFile = new File("/home/crawler/wallpage.jpg");
		    FileInputStream in = new FileInputStream(myFile);
		    DataOutputStream out = new DataOutputStream(sock.getOutputStream());

		    int bytes = 0;
		    byte[] buffer = new byte[8192];
		    int len;

		    while ((len = in.read(buffer)) > 0) {
		        out.write(buffer, 0, len);
		        bytes += len;
		    }

		    System.out.println("Transfer completed, " + bytes + " bytes sent");

		    out.flush();
		    sock.close();
		  //  in.close();
		   // servsock.close();
		}

	}
}
