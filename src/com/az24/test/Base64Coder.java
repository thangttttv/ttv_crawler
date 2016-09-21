// Copyright 2003-2010 Christian d'Heureuse, Inventec Informatik AG, Zurich, Switzerland
// www.source-code.biz, www.inventec.ch/chdh
//
// This module is multi-licensed and may be used under the terms
// of any of the following licenses:
//
//  EPL, Eclipse Public License, V1.0 or later, http://www.eclipse.org/legal
//  LGPL, GNU Lesser General Public License, V2.1 or later, http://www.gnu.org/licenses/lgpl.html
//  GPL, GNU General Public License, V2 or later, http://www.gnu.org/licenses/gpl.html
//  AL, Apache License, V2.0 or later, http://www.apache.org/licenses
//  BSD, BSD License, http://www.opensource.org/licenses/bsd-license.php
//  MIT, MIT License, http://www.opensource.org/licenses/MIT
//
// Please contact the author if you need another license.
// This module is provided "as is", without warranties of any kind.
package com.az24.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
* A Base64 encoder/decoder.
*
* <p>
* This class is used to encode and decode data in Base64 format as described in RFC 1521.
*
* <p>
* Project home page: <a href="http://www.source-code.biz/base64coder/java/">www.source-code.biz/base64coder/java</a><br>
* Author: Christian d'Heureuse, Inventec Informatik AG, Zurich, Switzerland<br>
* Multi-licensed: EPL / LGPL / GPL / AL / BSD / MIT.
*/
public class Base64Coder {

// The line separator string of the operating system.
private static final String systemLineSeparator = System.getProperty("line.separator");

// Mapping table from 6-bit nibbles to Base64 characters.
private static final char[] map1 = new char[64];
   static {
      int i=0;
      for (char c='A'; c<='Z'; c++) map1[i++] = c;
      for (char c='a'; c<='z'; c++) map1[i++] = c;
      for (char c='0'; c<='9'; c++) map1[i++] = c;
      map1[i++] = '+'; map1[i++] = '/'; }

// Mapping table from Base64 characters to 6-bit nibbles.
private static final byte[] map2 = new byte[128];
   static {
      for (int i=0; i<map2.length; i++) map2[i] = -1;
      for (int i=0; i<64; i++) map2[map1[i]] = (byte)i; }

/**
* Encodes a string into Base64 format.
* No blanks or line breaks are inserted.
* @param s  A String to be encoded.
* @return   A String containing the Base64 encoded data.
*/
public static String encodeString (String s) {
   return new String(encode(s.getBytes())); }

/**
* Encodes a byte array into Base 64 format and breaks the output into lines of 76 characters.
* This method is compatible with <code>sun.misc.BASE64Encoder.encodeBuffer(byte[])</code>.
* @param in  An array containing the data bytes to be encoded.
* @return    A String containing the Base64 encoded data, broken into lines.
*/
public static String encodeLines (byte[] in) {
   return encodeLines(in, 0, in.length, 76, systemLineSeparator); }

/**
* Encodes a byte array into Base 64 format and breaks the output into lines.
* @param in            An array containing the data bytes to be encoded.
* @param iOff          Offset of the first byte in <code>in</code> to be processed.
* @param iLen          Number of bytes to be processed in <code>in</code>, starting at <code>iOff</code>.
* @param lineLen       Line length for the output data. Should be a multiple of 4.
* @param lineSeparator The line separator to be used to separate the output lines.
* @return              A String containing the Base64 encoded data, broken into lines.
*/
public static String encodeLines (byte[] in, int iOff, int iLen, int lineLen, String lineSeparator) {
   int blockLen = (lineLen*3) / 4;
   if (blockLen <= 0) throw new IllegalArgumentException();
   int lines = (iLen+blockLen-1) / blockLen;
   int bufLen = ((iLen+2)/3)*4 + lines*lineSeparator.length();
   StringBuilder buf = new StringBuilder(bufLen);
   int ip = 0;
   while (ip < iLen) {
      int l = Math.min(iLen-ip, blockLen);
      buf.append (encode(in, iOff+ip, l));
      buf.append (lineSeparator);
      ip += l; }
   return buf.toString(); }

/**
* Encodes a byte array into Base64 format.
* No blanks or line breaks are inserted in the output.
* @param in  An array containing the data bytes to be encoded.
* @return    A character array containing the Base64 encoded data.
*/
public static char[] encode (byte[] in) {
   return encode(in, 0, in.length); }

/**
* Encodes a byte array into Base64 format.
* No blanks or line breaks are inserted in the output.
* @param in    An array containing the data bytes to be encoded.
* @param iLen  Number of bytes to process in <code>in</code>.
* @return      A character array containing the Base64 encoded data.
*/
public static char[] encode (byte[] in, int iLen) {
   return encode(in, 0, iLen); }

/**
* Encodes a byte array into Base64 format.
* No blanks or line breaks are inserted in the output.
* @param in    An array containing the data bytes to be encoded.
* @param iOff  Offset of the first byte in <code>in</code> to be processed.
* @param iLen  Number of bytes to process in <code>in</code>, starting at <code>iOff</code>.
* @return      A character array containing the Base64 encoded data.
*/
public static char[] encode (byte[] in, int iOff, int iLen) {
   int oDataLen = (iLen*4+2)/3;       // output length without padding
   int oLen = ((iLen+2)/3)*4;         // output length including padding
   char[] out = new char[oLen];
   int ip = iOff;
   int iEnd = iOff + iLen;
   int op = 0;
   while (ip < iEnd) {
      int i0 = in[ip++] & 0xff;
      int i1 = ip < iEnd ? in[ip++] & 0xff : 0;
      int i2 = ip < iEnd ? in[ip++] & 0xff : 0;
      int o0 = i0 >>> 2;
      int o1 = ((i0 &   3) << 4) | (i1 >>> 4);
      int o2 = ((i1 & 0xf) << 2) | (i2 >>> 6);
      int o3 = i2 & 0x3F;
      out[op++] = map1[o0];
      out[op++] = map1[o1];
      out[op] = op < oDataLen ? map1[o2] : '='; op++;
      out[op] = op < oDataLen ? map1[o3] : '='; op++; }
   return out; }

/**
* Decodes a string from Base64 format.
* No blanks or line breaks are allowed within the Base64 encoded input data.
* @param s  A Base64 String to be decoded.
* @return   A String containing the decoded data.
* @throws   IllegalArgumentException If the input is not valid Base64 encoded data.
*/
public static String decodeString (String s) {
   return new String(decode(s)); }

/**
* Decodes a byte array from Base64 format and ignores line separators, tabs and blanks.
* CR, LF, Tab and Space characters are ignored in the input data.
* This method is compatible with <code>sun.misc.BASE64Decoder.decodeBuffer(String)</code>.
* @param s  A Base64 String to be decoded.
* @return   An array containing the decoded data bytes.
* @throws   IllegalArgumentException If the input is not valid Base64 encoded data.
*/
public static byte[] decodeLines (String s) {
   char[] buf = new char[s.length()];
   int p = 0;
   for (int ip = 0; ip < s.length(); ip++) {
      char c = s.charAt(ip);
      if (c != ' ' && c != '\r' && c != '\n' && c != '\t')
         buf[p++] = c; }
   return decode(buf, 0, p); }

/**
* Decodes a byte array from Base64 format.
* No blanks or line breaks are allowed within the Base64 encoded input data.
* @param s  A Base64 String to be decoded.
* @return   An array containing the decoded data bytes.
* @throws   IllegalArgumentException If the input is not valid Base64 encoded data.
*/
public static byte[] decode (String s) {
   return decode(s.toCharArray()); }

/**
* Decodes a byte array from Base64 format.
* No blanks or line breaks are allowed within the Base64 encoded input data.
* @param in  A character array containing the Base64 encoded data.
* @return    An array containing the decoded data bytes.
* @throws    IllegalArgumentException If the input is not valid Base64 encoded data.
*/
public static byte[] decode (char[] in) {
   return decode(in, 0, in.length); }

/**
* Decodes a byte array from Base64 format.
* No blanks or line breaks are allowed within the Base64 encoded input data.
* @param in    A character array containing the Base64 encoded data.
* @param iOff  Offset of the first character in <code>in</code> to be processed.
* @param iLen  Number of characters to process in <code>in</code>, starting at <code>iOff</code>.
* @return      An array containing the decoded data bytes.
* @throws      IllegalArgumentException If the input is not valid Base64 encoded data.
*/
public static byte[] decode (char[] in, int iOff, int iLen) {
   if (iLen%4 != 0) throw new IllegalArgumentException ("Length of Base64 encoded input string is not a multiple of 4.");
   while (iLen > 0 && in[iOff+iLen-1] == '=') iLen--;
   int oLen = (iLen*3) / 4;
   byte[] out = new byte[oLen];
   int ip = iOff;
   int iEnd = iOff + iLen;
   int op = 0;
   while (ip < iEnd) {
      int i0 = in[ip++];
      int i1 = in[ip++];
      int i2 = ip < iEnd ? in[ip++] : 'A';
      int i3 = ip < iEnd ? in[ip++] : 'A';
      if (i0 > 127 || i1 > 127 || i2 > 127 || i3 > 127)
         throw new IllegalArgumentException ("Illegal character in Base64 encoded data.");
      int b0 = map2[i0];
      int b1 = map2[i1];
      int b2 = map2[i2];
      int b3 = map2[i3];
      if (b0 < 0 || b1 < 0 || b2 < 0 || b3 < 0)
         throw new IllegalArgumentException ("Illegal character in Base64 encoded data.");
      int o0 = ( b0       <<2) | (b1>>>4);
      int o1 = ((b1 & 0xf)<<4) | (b2>>>2);
      int o2 = ((b2 &   3)<<6) |  b3;
      out[op++] = (byte)o0;
      if (op<oLen) out[op++] = (byte)o1;
      if (op<oLen) out[op++] = (byte)o2; }
   return out; }

// Dummy constructor.
private Base64Coder() {}

	public static void main(String[] args) throws FileNotFoundException {
		String value= "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";
		value = "iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==";
		Base64Coder decoder = new Base64Coder();   
		byte[] imgBytes = decoder.decode(value);  
		
		try {
			FileOutputStream osf = new FileOutputStream(new File("d:/yourImage.png"));  
			osf.write(imgBytes);
			osf.flush();  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
	}

} // end class Base64Coder