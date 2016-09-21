package com.az24.crawler.config;

import hdc.crawler.Node;
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

import com.az24.crawler.model.BeanConfig;
import com.az24.crawler.model.DatabaseConfig;
import com.az24.crawler.model.ImageConfig;
import com.az24.crawler.model.Property;

public class BeanXmlConfig extends AbstractXmlConfig{	
	public BeanConfig bean = null;
	public BeanXmlConfig(String filePath) {
		super(filePath);		
	}
	
	public void parseConfig() {	
		bean = new BeanConfig();
		DocumentBuilderFactory domFactory = DocumentBuilderFactory
				.newInstance();
		domFactory.setNamespaceAware(true);
		DocumentBuilder builder;
		try {
			builder = domFactory.newDocumentBuilder();
			Document doc = builder.parse(this.filePath);
			XPath xpath = XPathFactory.newInstance().newXPath();
			XPathExpression expr = xpath.compile("/bean-mapping/database/driver/text()");
			
			// database
			Object result = expr.evaluate(doc, XPathConstants.STRING);
			DatabaseConfig.driver = (String) result;
		
			expr = xpath.compile("/bean-mapping/database/url/text()");
			result = expr.evaluate(doc, XPathConstants.STRING);
			DatabaseConfig.url = (String) result;
			
			expr = xpath.compile("/bean-mapping/database/user/text()");
			result = expr.evaluate(doc, XPathConstants.STRING);
			DatabaseConfig.user = (String) result;
			
			expr = xpath.compile("/bean-mapping/database/password/text()");
			result = expr.evaluate(doc, XPathConstants.STRING);
			DatabaseConfig.password = (String) result;
			
			expr = xpath.compile("/bean-mapping/database/connects/text()");
			result = expr.evaluate(doc, XPathConstants.STRING);
			DatabaseConfig.connections = (String) result;
			
			xpath = XPathFactory.newInstance().newXPath();
			expr = xpath.compile("/bean-mapping/database-log/driver/text()");
			
			// database log
			result = expr.evaluate(doc, XPathConstants.STRING);
			DatabaseConfig.driver_log = (String) result;
		
			expr = xpath.compile("/bean-mapping/database-log/url/text()");
			result = expr.evaluate(doc, XPathConstants.STRING);
			DatabaseConfig.url_log = (String) result;
			
			expr = xpath.compile("/bean-mapping/database-log/user/text()");
			result = expr.evaluate(doc, XPathConstants.STRING);
			DatabaseConfig.user_log = (String) result;
			
			expr = xpath.compile("/bean-mapping/database-log/password/text()");
			result = expr.evaluate(doc, XPathConstants.STRING);
			DatabaseConfig.password_log = (String) result;
			
			expr = xpath.compile("/bean-mapping/database-log/table-entity/text()");
			result = expr.evaluate(doc, XPathConstants.STRING);
			DatabaseConfig.table_entity_log = (String) result;
			
			expr = xpath.compile("/bean-mapping/database-log/table-image/text()");
			result = expr.evaluate(doc, XPathConstants.STRING);
			DatabaseConfig.table_image_log = (String) result;
			
			
			expr = xpath.compile("/bean-mapping/database-log/connects/text()");
			result = expr.evaluate(doc, XPathConstants.STRING);
			DatabaseConfig.connections_log = (String) result;
				
			expr = xpath.compile("/bean-mapping/class/@table");
			result = expr.evaluate(doc, XPathConstants.STRING);
			bean.setTable((String)result);
			
			expr = xpath.compile("/bean-mapping/class/id/@column");
			result = expr.evaluate(doc, XPathConstants.STRING);
			bean.setId((String)result);
			//System.out.println(bean.getId());
			
			expr = xpath.compile("/bean-mapping/class/xpath/text()");
			result = expr.evaluate(doc, XPathConstants.STRING);
			bean.setXpath((String)result);
			
			// images
			expr = xpath.compile("/bean-mapping/class/image");
			result = expr.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;
			ImageConfig imageConfig = null;
			List<ImageConfig> images = new ArrayList<ImageConfig>();
			for (int i = 0; i < nodes.getLength(); i++) {
				imageConfig = new ImageConfig();
				imageConfig.column = nodes.item(i).getAttributes().getNamedItem("column").getTextContent();
				imageConfig.name = nodes.item(i).getAttributes().getNamedItem("name").getTextContent();
				imageConfig.node_type =nodes.item(i).getAttributes().getNamedItem("node_type").getTextContent();
				expr = xpath.compile("/bean-mapping/class/image["+(i+1)+"]/xpath/text()");
				imageConfig.xpath =(String) expr.evaluate(doc, XPathConstants.STRING);
				
				expr = xpath.compile("/bean-mapping/class/image["+(i+1)+"]/xpath-sub/text()");
				imageConfig.xpath_sub =(String) expr.evaluate(doc, XPathConstants.STRING);
				
				expr = xpath.compile("/bean-mapping/class/image["+(i+1)+"]/regex/text()");
				imageConfig.regex = (String)expr.evaluate(doc, XPathConstants.STRING);
				
			//	System.out.println(imageConfig.xpath);
				//System.out.println(imageConfig.regex);
				images.add(imageConfig);
			}
			bean.setImages(images);
			expr = xpath.compile("/bean-mapping/class/property");
			result = expr.evaluate(doc, XPathConstants.NODESET);
			nodes = (NodeList) result;
			List<Property> properties = new ArrayList<Property>();
			Property property = null;
			for (int i = 0; i < nodes.getLength(); i++) {
				property = new Property();
				property.setName(nodes.item(i).getAttributes().getNamedItem("column").getTextContent());
				property.setType(nodes.item(i).getAttributes().getNamedItem("type").getTextContent());
				property.setValue(nodes.item(i).getAttributes().getNamedItem("value").getTextContent());
				property.setNode_type(nodes.item(i).getAttributes().getNamedItem("node_type").getTextContent());				
				property.setFilter(nodes.item(i).getAttributes().getNamedItem("filter").getTextContent());
				
				if(nodes.item(i).getAttributes().getNamedItem("changelink")!=null)
					property.setChangeLink(nodes.item(i).getAttributes().getNamedItem("changelink").getTextContent());
								
				expr = xpath.compile("/bean-mapping/class/property["+(i+1)+"]/xpath/text()");
				result = expr.evaluate(doc, XPathConstants.STRING);
				//System.out.println(property.getName());
				//System.out.println(result);
				property.setXpath((String)result);
				
				expr = xpath.compile("/bean-mapping/class/property["+(i+1)+"]/nodedels/nodedel");
				NodeList nodedels  =(NodeList)expr.evaluate(doc, XPathConstants.NODESET);
				Node nodedel = null;
				
				List<Node> listNode  = new ArrayList<Node>();
				for (int k = 0; k < nodedels.getLength(); k++) {
					nodedel = new Node();					
					nodedel.name= nodedels.item(k).getAttributes().getNamedItem("name").getTextContent();
					nodedel.attribue= nodedels.item(k).getAttributes().getNamedItem("attribute").getTextContent();
					listNode.add(nodedel);
				}
				property.setNodedels(listNode);
				
				expr = xpath.compile("/bean-mapping/class/property["+(i+1)+"]/nodedelxpaths/nodedel");
				NodeList nodedelByXpaths  =(NodeList)expr.evaluate(doc, XPathConstants.NODESET);
				nodedel = null;
				
				List<Node> listNodeDel  = new ArrayList<Node>();
				for (int k = 0; k < nodedelByXpaths.getLength(); k++) {
					nodedel = new Node();					
					nodedel.xpath= nodedelByXpaths.item(k).getAttributes().getNamedItem("xpath").getTextContent();
					listNodeDel.add(nodedel);
				}
				property.setNodedelByXpaths(listNodeDel);
				
				
				expr = xpath.compile("/bean-mapping/class/property["+(i+1)+"]/xpath-sub/text()");
				result = expr.evaluate(doc, XPathConstants.STRING);
				if(!StringUtil.isEmpty((String)result))
				{
					property.setXpath_sub((String)result);
				}
				
				properties.add(property);
				
			}
			bean.setProperties(properties);
			
			expr = xpath.compile("/bean-mapping/class/one-to-many");
			result = expr.evaluate(doc, XPathConstants.NODESET);
			nodes = (NodeList) result;
			
			BeanConfig subBeanConfig = null;
			List<BeanConfig> beans = new ArrayList<BeanConfig>();
			int node_one_many = nodes.getLength();
			if(nodes!=null&&nodes.getLength()>0)
			{
				int j = 1;
				while(j<=node_one_many)
				{
					subBeanConfig = new BeanConfig();
					expr = xpath.compile("/bean-mapping/class/one-to-many["+j+"]//@table");
					result = expr.evaluate(doc, XPathConstants.STRING);
					subBeanConfig.setTable((String)result);
					
					expr = xpath.compile("/bean-mapping/class/one-to-many["+j+"]/id//@column");
					result = expr.evaluate(doc, XPathConstants.STRING);
					subBeanConfig.setId((String)result);
					
					expr = xpath.compile("/bean-mapping/class/one-to-many["+j+"]/xpath/text()");
					result = expr.evaluate(doc, XPathConstants.STRING);
					subBeanConfig.setXpath((String)result);
					
					expr = xpath.compile("/bean-mapping/class/one-to-many["+j+"]/xpath_sub_label/text()");
					result = expr.evaluate(doc, XPathConstants.STRING);
					subBeanConfig.setXpath_sub_label((String)result);
					
					expr = xpath.compile("/bean-mapping/class/one-to-many["+j+"]/xpath_sub_value/text()");
					result = expr.evaluate(doc, XPathConstants.STRING);
					subBeanConfig.setXpath_sub_value((String)result);
					
					expr = xpath.compile("/bean-mapping/class/one-to-many["+j+"]/xpath_sub/text()");
					result = expr.evaluate(doc, XPathConstants.STRING);
					subBeanConfig.setXpath_sub((String)result);
					
					
					expr = xpath.compile("/bean-mapping/class/one-to-many["+j+"]/pkey/@column");
					result = expr.evaluate(doc, XPathConstants.STRING);
					subBeanConfig.setPkey((String)result);
					
					expr = xpath.compile("/bean-mapping/class/one-to-many["+j+"]/property");
					result = expr.evaluate(doc, XPathConstants.NODESET);
					nodes = (NodeList) result;
					properties = new ArrayList<Property>();
					property = null;
					for (int i = 0; i < nodes.getLength(); i++) {
						property = new Property();
						property.setName(nodes.item(i).getAttributes().getNamedItem("column").getTextContent());
						property.setType(nodes.item(i).getAttributes().getNamedItem("type").getTextContent());
						property.setFilter(nodes.item(i).getAttributes().getNamedItem("filter").getTextContent());	
						property.setNode_type(nodes.item(i).getAttributes().getNamedItem("node_type").getTextContent());	
						if(nodes.item(i).getAttributes().getNamedItem("changelink")!=null)
							property.setChangeLink(nodes.item(i).getAttributes().getNamedItem("changelink").getTextContent());
						
						expr = xpath.compile("/bean-mapping/class/one-to-many["+j+"]/property["+(i+1)+"]/xpath/text()");
						result = expr.evaluate(doc, XPathConstants.STRING);
						property.setXpath((String)result);
						//System.out.println(property.getName());
						//System.out.println(result);
						properties.add(property);
						
					}
					subBeanConfig.setProperties(properties);
					
					beans.add(subBeanConfig);
					j++;
					
				}
				
			}
			
			bean.setBeans(beans);
			
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
		BeanXmlConfig beanXmlConfig = new BeanXmlConfig("src/com/az24/crawler/config/beanProductStore.xml");
		beanXmlConfig.parseConfig();
	}
}
