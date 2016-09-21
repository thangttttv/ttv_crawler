package com.az24.crawler.config;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.az24.util.Logger;

public class JdbmXmlConfig {
	
	//public static String filePath="";
	public static String url="";
	public static String url_store="";
	public static String datum="";
	public static String datum_store="";
	public static String extract="";
	public static String extract_store="";
	public static String url_image="";
	public static String keyword_filter="";
	public static String file_log="";
	
	public static void parseConfig(String filePath) {
		DocumentBuilderFactory domFactory = DocumentBuilderFactory
				.newInstance();
		domFactory.setNamespaceAware(true);
		DocumentBuilder builder;
		com.az24.util.Logger logger = new Logger("com.az24.crawler.config.JdbmXmlConfig");
		try {
			//filePath= "src/com/az24/crawler/config/jdbm.xml";
			builder = domFactory.newDocumentBuilder();
			Document doc = builder.parse(filePath);
			XPath xpath = XPathFactory.newInstance().newXPath();
			XPathExpression expr = xpath.compile("/jdbm/url/text()");
			Object result = expr.evaluate(doc, XPathConstants.STRING);
			url = (String) result;
			
			logger.log("url="+url);
			
			expr = xpath.compile("/jdbm/url-store/text()");
			result = expr.evaluate(doc, XPathConstants.STRING);
			url_store = (String) result;
			
			logger.log("url_store="+url_store);
			
			expr = xpath.compile("/jdbm/datum/text()");
			result = expr.evaluate(doc, XPathConstants.STRING);
			datum = (String) result;
			logger.log("datum="+datum);
			
			expr = xpath.compile("/jdbm/datum-store/text()");
			result = expr.evaluate(doc, XPathConstants.STRING);
			datum_store = (String) result;
			logger.log("datum_store="+datum_store);
			
			expr = xpath.compile("/jdbm/extract/text()");
			result = expr.evaluate(doc, XPathConstants.STRING);
			extract = (String) result;
			logger.log("extract="+extract);
			
			expr = xpath.compile("/jdbm/extract-store/text()");
			result = expr.evaluate(doc, XPathConstants.STRING);
			extract_store = (String) result;
			logger.log("extract_store="+extract_store);
			
			expr = xpath.compile("/jdbm/url-image/text()");
			result = expr.evaluate(doc, XPathConstants.STRING);
			url_image = (String) result;
			logger.log("url_image="+url_image);
			
			
			
			expr = xpath.compile("/jdbm/url-image/text()");
			result = expr.evaluate(doc, XPathConstants.STRING);
			url_image = (String) result;
			logger.log("keyword_filter="+keyword_filter);
			
			expr = xpath.compile("/jdbm/file-log/text()");
			result = expr.evaluate(doc, XPathConstants.STRING);
			file_log = (String) result;
			logger.log("file_log="+file_log);
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		JdbmXmlConfig.parseConfig("src/com/az24/crawler/config/jdbm.xml");

	}
}
