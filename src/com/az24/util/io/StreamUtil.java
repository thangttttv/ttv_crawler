package com.az24.util.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class StreamUtil {
  static public byte[] readBytes(DataInput in) throws IOException {
    int len = in.readInt() ;
    if(len == -1) return null ;
    byte[] buf = new byte[len] ;
    in.readFully(buf) ;
    return buf ;
  }
  
  static public void writeBytes(DataOutput out, byte[] buf) throws IOException {
    if(buf == null) {
      out.writeInt(-1) ;
    } else {
      out.writeInt(buf.length) ;
      out.write(buf) ;
    }
  }
  
  static public short[] readShorts(DataInput in) throws IOException {
    int len = in.readInt() ;
    if(len == -1) return null ;
    short[] array = new short[len] ;
    for(int i = 0; i < len; i++) array[i] = in.readShort() ;
    return array;
  }
  
  static public void writeShorts(DataOutput out, short[] array) throws IOException {
    if(array == null) {
      out.writeInt(-1) ;
    } else {
      out.writeInt(array.length) ;
      for(int i = 0; i < array.length; i++) out.writeShort(array[i]) ;
    }
  }
  
  static public int[] readInts(DataInput in) throws IOException {
    int len = in.readInt() ;
    if(len == -1) return null ;
    int[] array = new int[len] ;
    for(int i = 0; i < len; i++) array[i] = in.readInt() ;
    return array;
  }
  
  static public void writeInts(DataOutput out, int[] array) throws IOException {
    if(array == null) {
      out.writeInt(-1) ;
    } else {
      out.writeInt(array.length) ;
      for(int i = 0; i < array.length; i++) out.writeInt(array[i]) ;
    }
  }
}