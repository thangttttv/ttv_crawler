package com.az24.util.io;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;


public class TransferToServer  {
  ServerSocketChannel listener = null;
  protected void mySetup()
  {
    InetSocketAddress listenAddr =  new InetSocketAddress(9026);

    try {
      listener = ServerSocketChannel.open();
      ServerSocket ss = listener.socket();
      ss.setReuseAddress(true);
      ss.bind(listenAddr);
      System.out.println("Listening on port : "+ listenAddr.toString());
    } catch (IOException e) {
      System.out.println("Failed to bind, is port : "+ listenAddr.toString()
          + " already in use ? Error Msg : "+e.getMessage());
      e.printStackTrace();
    }

  }

  public static void main(String[] args)
  {
    TransferToServer dns = new TransferToServer();
    dns.mySetup();
    try {
		dns.readData();
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }

  private void readData() throws FileNotFoundException  {
	  ByteBuffer dst = ByteBuffer.allocate(8192);
	  FileOutputStream fileOut = new FileOutputStream("/home/crawler/wallpage222fdfd34.jpg");
	  ByteArrayOutputStream baos = new ByteArrayOutputStream();
	  try {
		  while(true) {
			  SocketChannel conn = listener.accept();
			  System.out.println("Accepted : "+conn);
			  conn.configureBlocking(true);
			  int nread = 0;
			  while (nread != -1)  {
				  try {
					  nread = conn.read(dst);
					  baos.write(dst.array(), 0, nread);
					  System.out.println("Read Leng : "+nread);
				  } catch (IOException e) {
					  e.printStackTrace();
					  nread = -1;
				  }
				  dst.rewind();
			  }
			  fileOut.write(baos.toByteArray());
			  fileOut.close();
			 
		  }
	  } catch (IOException e) {
		  e.printStackTrace();
	  }
  }
}
