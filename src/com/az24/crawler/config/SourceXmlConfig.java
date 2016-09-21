package com.az24.crawler.config;

import hdc.crawler.AbstractCrawler.Url;

import java.io.IOException;
import java.util.ArrayList;

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

import com.az24.crawler.model.BeanConfig;

public class SourceXmlConfig extends AbstractXmlConfig{	
	public BeanConfig bean = null;
	
	public SourceXmlConfig(String filePath) {
		super(filePath);		
	}
	
	public void parseConfig() {
		urlConfigs = new ArrayList<Url>();
		
		DocumentBuilderFactory domFactory = DocumentBuilderFactory
				.newInstance();
		domFactory.setNamespaceAware(true);
		DocumentBuilder builder;
		
		try {
			builder = domFactory.newDocumentBuilder();
			Document doc = builder.parse(this.filePath);
			XPath xpath = XPathFactory.newInstance().newXPath();
			XPathExpression expr = xpath.compile("/info/urls/url");
			Object result = expr.evaluate(doc, XPathConstants.NODESET);

			// input url fetch
			NodeList nodes = (NodeList) result;
			Url url =null;
			for (int i = 0; i < nodes.getLength(); i++) {				
					url = new Url();
					url.url = nodes.item(i).getChildNodes().item(0).getTextContent().trim().replaceAll("amp", "&");
					url.id = nodes.item(i).getAttributes().getNamedItem("id").getTextContent();
					urlConfigs.add(url);
					//System.out.println(url.url);
					//System.out.println(url.id);
				
			}
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
		SourceXmlConfig beanXmlConfig = new SourceXmlConfig("src/com/az24/crawler/config/source_qa.xml");
		beanXmlConfig.parseConfig();
	}
}
