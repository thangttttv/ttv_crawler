package com.az24.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class HtmlParser {

  private DOMParser nonWellFormParser = new DOMParser();
  private DocumentBuilder wellFormParser ;
  
  public HtmlParser() {
    this.nonWellFormParser = new DOMParser();
    try {
      this.nonWellFormParser.setFeature("http://cyberneko.org/html/features/augmentations", true);
      this.nonWellFormParser.setProperty("http://cyberneko.org/html/properties/names/elems", "lower");
      
      //System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "org.apache.crimson.jaxp.DocumentBuilderFactoryImpl") ;
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      System.out.println(dbFactory);
      wellFormParser = dbFactory.newDocumentBuilder();
      wellFormParser.setEntityResolver(new EntityResolver() {
        public InputSource resolveEntity(String publicId, String systemId)
        throws SAXException, IOException {
          //System.out.println("Ignoring " + publicId + ", " + systemId);
          return new InputSource(new StringReader(""));
        }
      });
      
      wellFormParser.setErrorHandler(new ErrorHandler(){
        public void error(SAXParseException arg0) throws SAXException {
        }

        public void fatalError(SAXParseException arg0) throws SAXException {
        }

        public void warning(SAXParseException arg0) throws SAXException {
        }
      });
    } catch(Exception ex) {
      ex.printStackTrace() ;
    }
  }

  public Document parse(String data) throws Exception {
    try {
      return wellFormParser.parse(new InputSource(new StringReader(data)));
    } catch(Throwable ex) {
      nonWellFormParser.parse(new InputSource(new StringReader(data)));
      return nonWellFormParser.getDocument() ;
    }
  }

  public Document parse(byte[] data) throws Exception {
    try {
      ByteArrayInputStream bis = new ByteArrayInputStream(data) ; 
      return wellFormParser.parse(bis);
    } catch(Throwable ex) {
      ByteArrayInputStream bis = new ByteArrayInputStream(data) ;
      nonWellFormParser.parse(new InputSource(bis));
      return nonWellFormParser.getDocument() ;
    }
  }
  
  public Document parseNonWellForm(String html) throws Exception {
    return parseNonWellForm(new StringReader(html)) ;
  }
  
  public Document parseNonWellForm(Reader reader) throws Exception {
    nonWellFormParser.parse(new InputSource(reader));
    return nonWellFormParser.getDocument() ;
  }
  
  public Document parseNonWellForm(InputStream is) throws Exception {
    nonWellFormParser.parse(new InputSource(is));
    return nonWellFormParser.getDocument() ;
  }
  
  public Document parseWellForm(String data) throws Exception {
    return parseWellForm(new StringReader(data)) ;
  }
  
  public Document parseWellForm(Reader reader) throws Exception {
    Document doc = wellFormParser.parse(new InputSource(reader));
    return doc ;
  }

  public Document parseWellForm(InputStream is) throws Exception {
    Document doc = wellFormParser.parse(is);
    return doc ;
  }


  final public static void print(Node node, String indent) {
    String tagName = node.getLocalName() ;
    if(tagName != null) {
      System.out.println(indent + tagName + "[" + printAttributes(node) + "]");
    }
    Node child = node.getFirstChild();
    while (child != null) {
      print(child, indent + " ");
      child = child.getNextSibling();
    }
  }
  
  final public static String printAttributes(Node node) {
    StringBuilder b = new StringBuilder() ;
    NamedNodeMap map = node.getAttributes() ;
    for(int i = 0; i < map.getLength(); i++) {
      Node sel =  map.item(i) ;
      b.append(sel.getNodeName()).append("|").append(sel.getNodeValue()).append(";");
    }
    return b.toString() ;
  }
}