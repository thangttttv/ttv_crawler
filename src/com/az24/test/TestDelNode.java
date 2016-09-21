package com.az24.test;


import hdc.crawler.CrawlerUtil;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.util.html.parser.DomWriter;
import hdc.util.html.parser.XPathReader;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.http.HttpResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;



public class TestDelNode {
	  public static void main(String[] args) throws Exception {
		    Document doc = (Document) DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
		        new InputSource("d:/beanIone.xml"));

		    XPath xpath = XPathFactory.newInstance().newXPath();
		    Node nodes = ( Node) xpath.evaluate("//database-log", doc,
		        XPathConstants.NODE);

		   
		     
	        HttpClientImpl client = new HttpClientImpl();
			HttpResponse res = client.fetch("http://www.timviecnhanh.com/vieclam/congviec/2519168/nhan-vien-moi-gioi-dau-tu-vang.html");
			HttpClientUtil.printResponseHeaders(res);
			String html = HttpClientUtil.getResponseBody(res);

			XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
			//CrawlerUtil.analysis(reader.getDocument());
			
			DomWriter writer = new DomWriter();
			Node node_name = (Node) reader.read("//div[@id='contentrightinner']", XPathConstants.NODE);
			 

			
			XPathReader reader1 = CrawlerUtil.createXPathReaderByData(writer.toXMLString(node_name));
			CrawlerUtil.analysis(reader1.getDocument());
			
			Node node_name2 = (Node) reader.read("//div[@id='contentrightinner']/div[3]/div[2]/div[1]/div[1]/table[1]/TBODY[1]/tr[1]/td[2]", XPathConstants.NODE);
			
			TestDelNode.removeNodeByXpath(reader,"//div[@id='contentrightinner']/div[3]/div[2]/div[1]/div[1]/table[1]/TBODY[1]/tr[1]/td[2]");
			
			 Node parent1 = node_name2.getParentNode() ;
		    //  parent1.removeChild(node_name2) ;
		      
		      Node node_name3 = (Node) reader.read("//div[@id='contentrightinner']/div[7]", XPathConstants.NODE);
		      parent1 = node_name3.getParentNode() ;
		      parent1.removeChild(node_name3) ;
		      
		      node_name3 = (Node) reader.read("//div[@id='contentrightinner']/div[8]", XPathConstants.NODE);
		      parent1 = node_name3.getParentNode() ;
		      parent1.removeChild(node_name3) ;
		      
		      node_name3 = (Node) reader.read("//div[@id='contentrightinner']/div[9]", XPathConstants.NODE);
		      parent1 = node_name3.getParentNode() ;
		      parent1.removeChild(node_name3) ;
		      
		      System.out.println(writer.toXMLString(node_name));
		      
		    Transformer xformer = TransformerFactory.newInstance().newTransformer();
		    xformer.transform(new DOMSource(node_name), new StreamResult(new File("d:/data_new.html")));
		  }
	  
	  public static void removeNodeByXpath(XPathReader reader,String xpath)
	  {
			try {					
				    Node node = (Node) reader.read(xpath, XPathConstants.NODE);
					Node parent = node.getParentNode() ;
				    parent.removeChild(node) ;
			} catch (Exception e) {
				e.printStackTrace();
			}
		 
	
	  }
}
