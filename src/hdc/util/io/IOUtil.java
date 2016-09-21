package hdc.util.io;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;

public class IOUtil {
  static public String getFileContenntAsString(File file, String encoding) throws Exception {
    FileInputStream is = new FileInputStream(file) ;
    return new String(getStreamContentAsBytes(is), encoding) ;
  }

  static public String getFileContenntAsString(File file) throws Exception {
    FileInputStream is = new FileInputStream(file) ;
    return new String(getStreamContentAsBytes(is)) ;
  }

  static public String getFileContenntAsString(String fileName, String encoding) throws Exception {
    FileInputStream is = new FileInputStream(fileName) ;
    return new String(getStreamContentAsBytes(is), encoding) ;
  }

  static public String getFileContenntAsString(String fileName) throws Exception {
    FileInputStream is = new FileInputStream(fileName) ;
    String data = new String(getStreamContentAsBytes(is)) ;
    is.close() ;
    return data ;
  }

  static public byte[] getFileContentAsBytes(String fileName) throws Exception {
    FileInputStream is = new FileInputStream(fileName) ;
    byte[] data = getStreamContentAsBytes(is) ;
    is.close() ;
    return data ;
  }

  static public String getStreamContentAsString(InputStream is, String encoding) throws Exception {
    return new String(getStreamContentAsBytes(is), encoding) ;
  }

  static public byte[] getStreamContentAsBytes(InputStream is) throws IOException {
    BufferedInputStream buffer = new BufferedInputStream(is);    
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    byte[] data  = new byte[4912];      
    int available = -1;
    while((available = buffer.read(data)) > -1){
      output.write(data, 0, available);
    }   
    return output.toByteArray();
  }
  
  static public byte[] getStreamContentAsBytes(InputStream is, int maxRead) throws IOException {
    BufferedInputStream buffer = new BufferedInputStream(is);    
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    byte[] data  = new byte[4912];      
    int available = -1, read = 0 ;
    while((available = buffer.read(data)) > -1 && read < maxRead){
      if(maxRead - read < available) available = maxRead - read ;
      output.write(data, 0, available);
      read += available ;
    }   
    return output.toByteArray();
  }

  static public String getResourceAsString(String resource, String encoding) throws Exception {
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    URL url = cl.getResource(resource);
    InputStream is = url.openStream();
    String data = getStreamContentAsString(is, encoding) ;
    is.close() ;
    return data ;
  }

  static public byte[] getResourceAsBytes(String resource) throws Exception {
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    URL url = cl.getResource(resource);
    InputStream is = url.openStream();
    byte[] data = getStreamContentAsBytes(is) ;
    is.close() ;
    return data ;
  }

  static public  byte[] serialize(Object obj) throws Exception {
    ByteArrayOutputStream bytes = new  ByteArrayOutputStream() ;
    ObjectOutputStream out = new ObjectOutputStream(bytes);
    out.writeObject(obj);
    out.close();
    byte[] ret = bytes.toByteArray() ;
    return ret ;
  }

  static public Object deserialize(byte[] bytes) throws Exception {
    if (bytes == null) return null ;
    ByteArrayInputStream is = new  ByteArrayInputStream(bytes) ;
    ObjectInputStream in = new ObjectInputStream(is);
    Object obj =  in.readObject() ;
    in.close();
    return obj ;
  }
  
  static public String findContainingJar(Class my_class) {
    ClassLoader loader = my_class.getClassLoader();
    String class_file = my_class.getName().replaceAll("\\.", "/") + ".class";
    try {
      for(Enumeration itr = loader.getResources(class_file);
          itr.hasMoreElements();) {
        URL url = (URL) itr.nextElement();
        if ("jar".equals(url.getProtocol())) {
          String toReturn = url.getPath();
          if (toReturn.startsWith("file:")) {
            toReturn = toReturn.substring("file:".length());
          }
          toReturn = URLDecoder.decode(toReturn, "UTF-8");
          return toReturn.replaceAll("!.*$", "");
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
   
    return null;
  }
}