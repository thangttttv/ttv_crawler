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

public class UrlInjectXmlConfig extends AbstractXmlConfig {

	public UrlInjectXmlConfig(String filePath) {
		super(filePath);
	}

	public void parseConfig() {
		urlConfigs = new ArrayList<Url>();
		regrexConfigs = new ArrayList<Object>();
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
			String fetch="1";int j=0;Url url =null;String fetchFrom="1",fetchTo="1",regex="";
			
			for (int i = 0; i < nodes.getLength(); i++) {				
					url = new Url();
					url.url = nodes.item(i).getChildNodes().item(0).getTextContent().trim().replaceAll("amp", "&");
					url.cat_id = Integer.parseInt(nodes.item(i).getAttributes().getNamedItem("catid").getTextContent());
					url.collection = Integer.parseInt(nodes.item(i).getAttributes().getNamedItem("collection")!=null?nodes.item(i).getAttributes().getNamedItem("collection").getTextContent():"1");
					fetch = nodes.item(i).getAttributes().getNamedItem("fetchNumber")!=null?nodes.item(i).getAttributes().getNamedItem("fetchNumber").getTextContent():"1";
					fetchFrom = nodes.item(i).getAttributes().getNamedItem("fetchFrom")!=null?nodes.item(i).getAttributes().getNamedItem("fetchFrom").getTextContent():"1";
					fetchTo = nodes.item(i).getAttributes().getNamedItem("fetchTo")!=null?nodes.item(i).getAttributes().getNamedItem("fetchTo").getTextContent():"1";
					regex = nodes.item(i).getAttributes().getNamedItem("regex")!=null?nodes.item(i).getAttributes().getNamedItem("regex").getTextContent():"1";
					fetch = fetch==null?"1":fetch;
					fetchFrom = fetchFrom==null?"1":fetchFrom;
					fetchTo = fetchTo==null?"1":fetchTo;
					
					url.fetchNumber = Integer.parseInt(fetch);
					url.fetchFrom = Integer.parseInt(fetchFrom);
					url.fetchTo = Integer.parseInt(fetchTo);
					url.pagePara = nodes.item(i).getAttributes().getNamedItem("pagePara")!=null?nodes.item(i).getAttributes().getNamedItem("pagePara").getTextContent():"";
					url.regex = regex;
					url.url_1 = nodes.item(i).getAttributes().getNamedItem("url_1")!=null?nodes.item(i).getAttributes().getNamedItem("url_1").getTextContent().replaceAll("&amp;", "&"):"";
					url.url_2 = nodes.item(i).getAttributes().getNamedItem("url_2")!=null?nodes.item(i).getAttributes().getNamedItem("url_2").getTextContent().replaceAll("&amp;", "&"):"";
					urlConfigs.add(url);
					/*if(url.fetchNumber>1)
					{
						j=1;
						while(j<=Integer.parseInt(fetch))
						{
							url = new Url();
							url.url = nodes.item(i).getChildNodes().item(0).getTextContent().trim().replaceAll("amp", "&")
							+nodes.item(i).getAttributes().getNamedItem("pagePara").getTextContent().replaceAll("amp", "&")+(j+1);
							url.cat_id = Integer.parseInt(nodes.item(i).getAttributes().getNamedItem("catid").getTextContent());
							url.collection = Integer.parseInt(nodes.item(i).getAttributes().getNamedItem("collection")!=null?nodes.item(i).getAttributes().getNamedItem("collection").getTextContent():"1");
							urlConfigs.add(url);
							j++;
							System.out.println(url.url);
						}
						
					}*/
					
					//System.out.println(nodes.item(i).getAttributes().getNamedItem("catid").getTextContent());
					//System.out.println(nodes.item(i).getChildNodes().item(0).getTextContent().trim());
				
			}
			
			// input url website			
			expr = xpath.compile("/info/website/baseUrl/text()");
			result = expr.evaluate(doc, XPathConstants.STRING);
			baseUrl = (String) result;
			System.out.println(result);
			expr = xpath.compile("/info/website/rewriterUrl/text()");
			result = expr.evaluate(doc, XPathConstants.STRING);
			rewriterUrl = (String) result;
			System.out.println(result);
			// input regex
			expr = xpath.compile("/info/regexs/regex/text()");
			result = expr.evaluate(doc, XPathConstants.NODESET);
			nodes = (NodeList) result;
			for (int i = 0; i < nodes.getLength(); i++) {
				if (nodes.item(i).getNodeValue() != null) {
					regrexConfigs.add(nodes.item(i).getNodeValue().trim());
					System.out.println(nodes.item(i).getNodeValue().trim());
				}
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
		AbstractXmlConfig xmlConfig = new UrlInjectXmlConfig(
				"src/com/az24/crawler/config/urlsQAYeuLaptop.xml");
		xmlConfig.parseConfig();

	}

}
