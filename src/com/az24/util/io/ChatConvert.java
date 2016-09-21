package com.az24.util.io;

import java.io.EOFException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ChatConvert {
	public static int byteArrayToInt(byte[] b) 
	{
	    return   b[3] & 0xFF |
	            (b[2] & 0xFF) << 8 |
	            (b[1] & 0xFF) << 16 |
	            (b[0] & 0xFF) << 24;
	}

	public static byte[] intToByteArray(int a)
	{
	    return new byte[] {
	        (byte) ((a >> 24) & 0xFF),
	        (byte) ((a >> 16) & 0xFF),   
	        (byte) ((a >> 8) & 0xFF),   
	        (byte) (a & 0xFF)
	    };
	}
	
	public static int iosByteToInt(byte[] b){
		
		int intAction = b[0] + (b[1] << 8) + (b[2] << 16) + (b[3] << 24);
		return intAction;
	}
	
	public static int byteArrayToLeInt(byte[] b) {
	    final ByteBuffer bb = ByteBuffer.wrap(b);
	    bb.order(ByteOrder.BIG_ENDIAN);
	    return bb.getInt();
	}

	public static byte[] leIntToByteArray(int i) {
	    final ByteBuffer bb = ByteBuffer.allocate(Integer.SIZE / Byte.SIZE);
	    bb.order(ByteOrder.BIG_ENDIAN);
	    bb.putInt(i);
	    return bb.array();
	}
	
	public static void main(String[] args) {
		byte[] bytes= ChatConvert.intToByteArray(4);
		System.out.println(ChatConvert.byteArrayToInt(bytes));
		System.out.println(ChatConvert.iosByteToInt(bytes));
		
		byte[] bytes2= ChatConvert.leIntToByteArray(4);
		System.out.println(ChatConvert.byteArrayToLeInt(bytes2));
	}
}
