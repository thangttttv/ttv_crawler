package com.az24.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Php {
  private static String convertToHex(byte[] data) {
    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < data.length; i++) {
      int halfbyte = (data[i] >>> 4) & 0x0F;
      int two_halfs = 0;
      do {
        if ((0 <= halfbyte) && (halfbyte <= 9))
          buf.append((char) ('0' + halfbyte));
        else
          buf.append((char) ('a' + (halfbyte - 10)));
        halfbyte = data[i] & 0x0F;
      } while(two_halfs++ < 1);
    }
    return buf.toString();
  }

  public static String MD5(String text) 
  throws NoSuchAlgorithmException, UnsupportedEncodingException  {
    MessageDigest md;
    md = MessageDigest.getInstance("MD5");
    byte[] md5hash = new byte[32];
    md.update(text.getBytes("iso-8859-1"), 0, text.length());
    md5hash = md.digest();
    return convertToHex(md5hash);
  }

  public static String SHA(String text) 
  throws NoSuchAlgorithmException, UnsupportedEncodingException  {
    MessageDigest md;
    md = MessageDigest.getInstance("SHA");
    byte[] md5hash = new byte[32];
    md.update(text.getBytes("8859-1"), 0, text.length());
    md5hash = md.digest();
    return convertToHex(md5hash);
  }
  
  public static void main(String[] args) {
	  try {
		String txt = MD5Php.MD5("thangtt"); System.out.println(MD5Php.MD5("thangtt"));
		System.out.println(txt.equals("841bc3aebc18b175893b6a50a0df9484"));
		txt = MD5Php.MD5("thangtt!@#$%^"); System.out.println(MD5Php.MD5("thangtt!@#$%^"));
		System.out.println(txt.equals("857db7d1f34d7e5030fd2f48420d8001"));
		txt = MD5Php.MD5("!@#$%^"); System.out.println(MD5Php.MD5("!@#$%^"));
		System.out.println(txt.equals("c92b51b2f4d93d4e1081670bd9273402"));
		
	} catch (NoSuchAlgorithmException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
  
}