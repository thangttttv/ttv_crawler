package com.az24.test;

import hdc.crawler.CrawlerUtil;
import hdc.util.html.parser.XPathNode;
import hdc.util.html.parser.XPathReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TreeNode {
	public static String getHTML(String urlToRead) {
		URL url; // The URL to read
		HttpURLConnection conn; // The actual connection to the web page
		BufferedReader rd; // Used to read results from the web page
		String line; // An individual line of the web page HTML
		String result = ""; // A long string containing all the HTML
		try {
			url = new URL(urlToRead);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "UTF-8"));
			while ((line = rd.readLine()) != null) {
				result += line;
			}
			rd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(result);
		return result;
	}
	
	  public static String xmlToString(Node node) {
	        try {
	            Source source = new DOMSource(node);
	            StringWriter stringWriter = new StringWriter();
	            Result result = new StreamResult(stringWriter);
	            TransformerFactory factory = TransformerFactory.newInstance();
	            Transformer transformer = factory.newTransformer();
	            transformer.transform(source, result);
	            return stringWriter.getBuffer().toString();
	        } catch (TransformerConfigurationException e) {
	            e.printStackTrace();
	        } catch (TransformerException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	  
	public static void main(String[] args) {
		DocumentBuilderFactory domFactory = DocumentBuilderFactory
				.newInstance();
		domFactory.setNamespaceAware(true);
		DocumentBuilder builder;
		try {
			/*String sourceLine;
			String content = "";
			URL address = new URL("http://www.vatgia.com/raovat/1936/3358397/order-hang-quang-chau-hot-nhat-thi-truong.html");

			// Open the address and create a BufferedReader with the source code.
			InputStreamReader pageInput = new InputStreamReader(address
					.openStream());
			BufferedReader source = new BufferedReader(pageInput);

			// Append each new HTML line into one string. Add a tab character.
			while ((sourceLine = source.readLine()) != null)
				content += sourceLine + "\t";
			
			InputStream inputStream = new StringBufferInputStream(content);
			builder = domFactory.newDocumentBuilder();*/
			
			XPathReader reader = CrawlerUtil
			.createXPathReaderByData(getHTML("http://www.vatgia.com/raovat/1936/3358397/order-hang-quang-chau-hot-nhat-thi-truong.html"));
			
			Document doc = reader.getDocument();
			XPath xpath = XPathFactory.newInstance().newXPath();
			XPathExpression expr = xpath
					.compile("//div");
			NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			int i=0;
			System.out.println(nodes.getLength());
			while(i<nodes.getLength())
			{
				///*System.out.println(nodes.item(i).getNodeValue()+"fdfd");
				if(nodes.item(i).getAttributes().getNamedItem("id")!=null)
				if(nodes.item(i).getAttributes().getNamedItem("id").getTextContent().equalsIgnoreCase("raovat_description"))
				{
					System.out.println("Thangtt");
					//System.out.println(xmlToString(nodes.item(i)));
					XPathNode xNode = new XPathNode(nodes.item(i));
					
					System.out.println(xNode.getXPath());
				}
				//System.out.println(nodes.item(i).getNodeValue());
				//System.out.println(i);
				i++;
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
