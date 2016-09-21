package com.az24.crawler.config;

import hdc.crawler.AbstractCrawler.Url;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class AbstractXmlConfig {
	protected String filePath="";
	public static List<Url> urlConfigs = null;
	public static List<Object> regrexConfigs = null;
	public static String baseUrl ="";
	public static String rewriterUrl="";
	
	public AbstractXmlConfig(String filePath)
	{
		this.filePath = filePath;
	}
	
	public void parseConfig()
	{
		urlConfigs = new ArrayList<Url>();
	}	
	
	protected  void traverse(NodeList rootNode) {
		for (int index = 0; index < rootNode.getLength(); index++) {
			Node aNode = rootNode.item(index);
			if (aNode.getNodeType() == Node.ELEMENT_NODE) {
				NodeList childNodes = aNode.getChildNodes();
				if (childNodes.getLength() > 0) {
					System.out.println("Node Name-->" + aNode.getNodeName()
							+ " , Node Value-->" + aNode.getTextContent());
				}
				traverse(aNode.getChildNodes());
			}
		}
	}
	
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

		
}
