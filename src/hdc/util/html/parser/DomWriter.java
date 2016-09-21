package hdc.util.html.parser;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

public class DomWriter {
  private DOMImplementationLS domImpl ;
  public DomWriter() throws Exception {
    DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
    this.domImpl = (DOMImplementationLS)registry.getDOMImplementation("LS");
  }

  public void write(OutputStream os, Document doc) throws Exception {
    LSSerializer serializer = domImpl.createLSSerializer();
    LSOutput lso = domImpl.createLSOutput();
    lso.setByteStream(os);
    lso.setEncoding("UTF-8");
    serializer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE) ;
    //    serializer.getDomConfig().setParameter("xml-declaration", Boolean.FALSE) ;
    //    serializer.getDomConfig().setParameter("discard-default-content", Boolean.FALSE) ;
    //    DOMStringList list = serializer.getDomConfig().getParameterNames() ;
    //    for(int i = 0; i < list.getLength(); i++) {
    //    	System.out.println(list.item(i) + " : " + serializer.getDomConfig().getParameter(list.item(i)));
    //    }
    serializer.write(doc, lso);
  }

  public String toXMLString(Document doc) throws Exception {
    ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
    LSSerializer serializer = domImpl.createLSSerializer();
    LSOutput lso = domImpl.createLSOutput();
    lso.setByteStream(bos);
    lso.setEncoding("UTF-8");
    serializer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE) ;
    serializer.write(doc, lso);
    return new String(bos.toByteArray(), "UTF-8") ;
  }
  
  public String toXMLString(Node node)  {  
	  String str="";
    try {
    	  	ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
    	    LSSerializer serializer = domImpl.createLSSerializer();
    	    LSOutput lso = domImpl.createLSOutput();
    	    lso.setByteStream(bos);
    	    lso.setEncoding("UTF-8");
    	    serializer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE) ;
    	    serializer.write(node, lso);
    	    str = new String(bos.toByteArray(), "UTF-8") ;
	} catch (UnsupportedEncodingException e) {		
		e.printStackTrace();
		return "";
	}
	return str;
  }
}