package com.az24.crawler.config;

import hdc.util.text.StringUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.az24.crawler.model.Thread;
import com.az24.util.Logger;

public class ThreadXmlConfig {
	
	public static List<Thread> threads;
	
	public static void parseConfig(String filePath) {
		DocumentBuilderFactory domFactory = DocumentBuilderFactory
				.newInstance();
		domFactory.setNamespaceAware(true);
		DocumentBuilder builder;
		com.az24.util.Logger logger = new Logger("com.az24.crawler.config.ThreadXmlConfig");
		threads = new ArrayList<Thread>();
		Thread thread = null;
		try {
			builder = domFactory.newDocumentBuilder();
			Document doc = builder.parse(filePath);
			XPath xpath = XPathFactory.newInstance().newXPath();
			XPathExpression expr = xpath.compile("/threads/thread");
			Object result = expr.evaluate(doc, XPathConstants.NODESET);

			// input url fetch
			NodeList nodes = (NodeList) result;
			String str="";
			for (int i = 1; i <=nodes.getLength(); i++) {	
					thread = new Thread();
					expr = xpath.compile("/threads/thread["+i+"]/time/text()");
					str =(String) expr.evaluate(doc, XPathConstants.STRING);
					thread.setTime(str);
					expr = xpath.compile("/threads/thread["+i+"]/bean_config/text()");
					str =(String) expr.evaluate(doc, XPathConstants.STRING);					
					thread.setBean_config(str);
					expr = xpath.compile("/threads/thread["+i+"]/jdbm_config/text()");
					str =(String) expr.evaluate(doc, XPathConstants.STRING);	
					thread.setJdbm_config(str);
					expr = xpath.compile("/threads/thread["+i+"]/url_config/text()");
					str =(String) expr.evaluate(doc, XPathConstants.STRING);
					thread.setUrl_config(str);
					expr = xpath.compile("/threads/thread["+i+"]/importer_config/text()");
					str =(String) expr.evaluate(doc, XPathConstants.STRING);
					try{
						thread.setImporter_config(Integer.parseInt(str));
					}catch (Exception e) {
						thread.setImporter_config(0);
					}
					expr = xpath.compile("/threads/thread["+i+"]/step/text()");
					str =(String) expr.evaluate(doc, XPathConstants.STRING);
					if(StringUtil.isEmpty(str))str="0";
					try{
						thread.setStep(Integer.parseInt(str));
					}catch (Exception e) {
						thread.setStep(0);
					}
					
					expr = xpath.compile("/threads/thread["+i+"]/stepTo/text()");
					str =(String) expr.evaluate(doc, XPathConstants.STRING);
					if(StringUtil.isEmpty(str))str="0";
					try{
						thread.setStepTo(Integer.parseInt(str));
					}catch (Exception e) {
						thread.setStep(0);
					}
					
					expr = xpath.compile("/threads/thread["+i+"]/fetch/text()");
					str =(String) expr.evaluate(doc, XPathConstants.STRING);
					if(StringUtil.isEmpty(str))str="1";
					try{
						thread.setFetch(Integer.parseInt(str));
					}catch (Exception e) {
						thread.setStep(0);
					}
					
					expr = xpath.compile("/threads/thread["+i+"]/is_slow/text()");
					str =(String) expr.evaluate(doc, XPathConstants.STRING);
					if(StringUtil.isEmpty(str))str="1";
					try{
						thread.setIs_slow(Integer.parseInt(str));
					}catch (Exception e) {
						thread.setStep(0);
					}
					
					logger.log(thread.getTime());
					logger.log(thread.getImporter_config()+"");
					threads.add(thread);				
			}
			
		} catch (ParserConfigurationException e) {			
			e.printStackTrace();
		} catch (SAXException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		} catch (XPathExpressionException e) {			
			e.printStackTrace();
		}

	}
	
	public static void main(String[] args) {
		ThreadXmlConfig.parseConfig("src/com/az24/crawler/config/threads.xml");
	}
	
}
