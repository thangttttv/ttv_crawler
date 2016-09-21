package hdc.crawler;

import hdc.crawler.fetcher.HttpClientImpl;
import hdc.crawler.fetcher.HttpClientUtil;
import hdc.util.html.NodeDeleteVisitor;
import hdc.util.html.NodeNormalizeVisitor;
import hdc.util.html.URLRewriteVisitor;
import hdc.util.html.parser.AnalysisDocument;
import hdc.util.html.parser.DomWriter;
import hdc.util.html.parser.HtmlParser;
import hdc.util.html.parser.XPathNode;
import hdc.util.html.parser.XPathReader;
import hdc.util.text.StringUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathConstants;

import org.apache.http.HttpResponse;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CrawlerUtil {
	private static final HtmlParser parser = new HtmlParser();

	final public static void analysis(Document doc) throws Exception {
		AnalysisDocument analysis = new AnalysisDocument(doc);
		List<XPathNode> list = analysis.getCandidateXPath();
		for (XPathNode xpath : list) {
			//System.out.println(xpath.getXPath());
			//      FileUtil.writeToFile("target/xpath.txt", (xpath.getXPath() + "\n").getBytes("UTF-8"), true) ;
			//System.out.println(xpath.getNormalizedText());
			//      FileUtil.writeToFile("target/xpath.txt", (xpath.getNormalizedText() + "\n").getBytes("UTF-8"), true) ;
		}
	}

	final public static void analysis(Document doc, String token)
			throws Exception {
		AnalysisDocument analysis = new AnalysisDocument(doc);
		List<XPathNode> list = analysis.getCandidateXPath();
		for (XPathNode xpath : list) {

			if (xpath.getXPath().indexOf(token) > -1
					|| xpath.getNormalizedText().indexOf(token) > -1) {
				System.out.println(xpath.getXPath());
				System.out.println(xpath.getNormalizedText());
			}
			if (xpath.getNode().getAttributes() != null
					&& xpath.getNode().getAttributes().getNamedItem("alt") != null) {
				if (xpath.getNode().getAttributes().getNamedItem("alt")
						.getTextContent().indexOf(token) > -1)
					System.out.println(xpath.getXPath());

			}
		}
	}

	final public static List<XPathNode> analysisToList(Document doc,
			List<String> tokens) throws Exception {
		AnalysisDocument analysis = new AnalysisDocument(doc);
		List<XPathNode> list = analysis.getCandidateXPath();
		List<XPathNode> listResult = new ArrayList<XPathNode>();
		for (XPathNode xpath : list) {
			for (String token : tokens) {
				if (token.equals(xpath.getNormalizedText())) {
					listResult.add(xpath);
					System.out.println(xpath.getXPath());
					System.out.println(xpath.getNormalizedText());
				}
			}
		}
		return listResult;
	}

	final public static void traverse(NodeList rootNode) {
		for (int index = 0; index < rootNode.getLength(); index++) {
			Node aNode = rootNode.item(index);
			if (aNode.getNodeType() == Node.ELEMENT_NODE) {
				NodeList childNodes = aNode.getChildNodes();
				if (childNodes.getLength() > 0) {
					System.out.println("Node Name-->" + aNode.getNodeName()
							+ " , Node Value-->"
							+ aNode.getTextContent().trim());
					if (aNode.hasAttributes()) {
						for (int j = 0; j < aNode.getAttributes().getLength(); j++) {
							String s = aNode.getAttributes().item(j).toString();
							if (s.indexOf("href=\"") != -1)
								s = s.substring("href=\"".length(),
										s.length() - 1);
							System.out.println(j + " : " + s);
						}
					}
				}
				traverse(aNode.getChildNodes());
			}
		}
	}

	final public static XPathReader createXPathReaderByData(String data,
			String siteUrl, String baseUrl) throws Exception {
		Document doc = null;
		try {
			
			Pattern comment = Pattern
			.compile("\\w'\\w");
			Matcher mcomment = comment.matcher(data);
			while (mcomment.find()) {
				data = mcomment.replaceAll("");
				
			}
			
			doc = parser.parseNonWellForm(data);

			DomWriter writer = new DomWriter();
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			writer.write(output, doc);
			doc = parser.parseWellForm(new ByteArrayInputStream(output
					.toByteArray()));
		} catch (Exception ex) {
			data = StringUtil.stripNonValidXMLCharacters(data);
			data = data.replaceAll("\\&lt;", "<");
			data = data.replaceAll("\\&gt;", ">");
			data = data.replaceAll("\"--\"", "");		

			//System.out.println(data);
			Pattern comment = Pattern.compile("<!--.*?-->");
			Matcher mcomment = comment.matcher(data);
			while (mcomment.find())
				data = mcomment.replaceAll("");

			comment = Pattern.compile("<!--.*-->");
			mcomment = comment.matcher(data);
			while (mcomment.find())
				data = mcomment.replaceAll("");

			data = data.replaceAll("--", "");
			data = data.replaceAll("&", "");
			data = data.replaceAll("#", "");
			data = data.replaceAll("$", "");
			data = data.replaceAll("''", "&#39;");
			
			data = data.replaceAll("''", "");
			data = data.replaceAll("=\\)\\)\\*", "");
			data = data.replaceAll("\"", "''"); // Node Vi du:<td width="> La Loi
			
			doc = parser.parseNonWellForm(data);
			DomWriter writer = new DomWriter();
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			writer.write(output, doc);
			doc = parser.parseWellForm(new ByteArrayInputStream(output
					.toByteArray()));
		}

		if (doc == null)
			return null;

		if (siteUrl != null && baseUrl != null) {
			URLRewriteVisitor urlVisitor = new URLRewriteVisitor(siteUrl,
					baseUrl);
			urlVisitor.traverse(doc);
		}

		XPathReader reader = new XPathReader(doc);
		return reader;

	}

	final public static XPathReader createXPathReaderByData(String data) {
		try {
			return createXPathReaderByData(data, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	final public static XPathReader createXPathReader(String url)
			throws Exception {
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		String data = HttpClientUtil.getResponseBody(res);
		return createXPathReaderByData(data);
	}

	final public static Node cleanUp(Node node) {
		NodeDeleteVisitor deleteVisitor = new NodeDeleteVisitor() {
			protected boolean shouldDelete(Node node) {
				if (node.getNodeName().equalsIgnoreCase("meta"))
					return true;
				else if (node.getNodeName().equalsIgnoreCase("link"))
					return true;
				else if (node.getNodeName().equalsIgnoreCase("style"))
					return true;
				else if (node.getNodeName().equalsIgnoreCase("script"))
					return true;
				return false;
			}
		};
		deleteVisitor.traverse(node);

		NodeNormalizeVisitor normalVisitor = new NodeNormalizeVisitor() {
			protected void normalize(Node node) {
				if (node.hasAttributes()) {
					NamedNodeMap attributes = node.getAttributes();
					if (attributes.getNamedItem("class") != null)
						attributes.removeNamedItem("class");
					if (attributes.getNamedItem("id") != null)
						attributes.removeNamedItem("id");
					if (attributes.getNamedItem("style") != null)
						attributes.removeNamedItem("style");
					if (attributes.getNamedItem("height") != null)
						attributes.removeNamedItem("height");
					if (attributes.getNamedItem("width") != null)
						attributes.removeNamedItem("width");
					if (attributes.getNamedItem("onclick") != null)
						attributes.removeNamedItem("onclick");
					if (attributes.getNamedItem("title") != null)
						attributes.removeNamedItem("title");
					if (attributes.getNamedItem("rel") != null)
						attributes.removeNamedItem("rel");
					if (attributes.getNamedItem("target") != null)
						attributes.removeNamedItem("target");
				}
			}
		};
		normalVisitor.traverse(node);
		return node;
	}

	final public static NodeList removeNodeByID(NodeList root, String[] ids) {
		for (int index = 0; index < root.getLength(); index++) {
			Node aNode = root.item(index);
			if (aNode.getNodeType() == Node.ELEMENT_NODE) {
				NodeList childNodes = aNode.getChildNodes();
				if (childNodes.getLength() > 0) {
					if (aNode.hasAttributes()) {
						NamedNodeMap attributes = aNode.getAttributes();
						for (int i = 0; i < ids.length; i++) {
							if (attributes.getNamedItem("id") != null) {
								if (attributes.getNamedItem("id")
										.getTextContent().equals(ids[i])) {
									aNode.getParentNode().removeChild(aNode);
									break;
								}
							}
						}
					}
				}
			}
			removeNodeByID(aNode.getChildNodes(), ids);
		}
		return root;
	}

	final public static NodeList removeNodeByAttribute(NodeList root,
			Map<String, String> keyValuePair) {
		for (int index = 0; index < root.getLength(); index++) {
			Node aNode = root.item(index);
			if (aNode.getNodeType() == Node.ELEMENT_NODE) {
				NodeList childNodes = aNode.getChildNodes();
				if (childNodes.getLength() > 0) {
					if (aNode.hasAttributes()) {
						for (int j = 0; j < aNode.getAttributes().getLength(); j++) {
							String attrName = aNode.getAttributes().item(j)
									.getNodeName();
							String attrValue = aNode.getAttributes().item(j)
									.getNodeValue();
							Iterator<Map.Entry<String, String>> iterator = keyValuePair
									.entrySet().iterator();
							while (iterator.hasNext()) {
								Map.Entry<String, String> entry = iterator
										.next();
								String name = entry.getKey();
								String value = entry.getValue();
								if (attrName.equals(value)
										&& attrValue.equals(name)) {
									aNode.getParentNode().removeChild(aNode);
									break;
								}
							}
						}
					}
				}
			}
			removeNodeByAttribute(aNode.getChildNodes(), keyValuePair);
		}
		return root;
	}

	final public static void removeNodesByXPath(Document doc, String[] xpaths)
			throws Exception {
		AnalysisDocument analysis = new AnalysisDocument(doc);
		List<XPathNode> list = analysis.getCandidateXPath();
		for (int i = 0; i < list.size(); i++) {
			XPathNode xNode = list.get(i);
			for (int j = 0; j < xpaths.length; j++) {
				if (xNode.getXPath().equals(xpaths[j])) {
					Node node = xNode.getNode();
					node.getParentNode().removeChild(node);
					break;
				}
			}
		}
	}
	


	final public static NodeList removeElements(NodeList root,
			String[] elementNames) {
		for (int index = 0; index < root.getLength(); index++) {
			Node aNode = root.item(index);
			if (aNode.getNodeType() == Node.ELEMENT_NODE) {
				NodeList childNodes = aNode.getChildNodes();
				if (childNodes.getLength() > 0) {
					for (String name : elementNames) {
						if (aNode.getNodeName().equalsIgnoreCase(name)) {
							aNode.getParentNode().removeChild(aNode);
							break;
						}
					}
				}
				removeElements(aNode.getChildNodes(), elementNames);
			}
		}
		return root;
	}
	
	  
	public static void removeNodeByXpath(XPathReader reader,String xpath)
	  {
			try {					
				    Node node = (Node) reader.read(xpath, XPathConstants.NODE);
				    if(node!=null)
				    {
					Node parent = node.getParentNode() ;
				    parent.removeChild(node) ;
				    }
			} catch (Exception e) {
				e.printStackTrace();
			}
		 
	
	  }
	


	public static void main(String[] args) {
		XPathReader reader;
		try {
			reader = CrawlerUtil
					.createXPathReader("http://bongda.wap.vn/ket-qua-vdqg-chi-le-1385.html");
			Document doc = reader.getDocument();
			CrawlerUtil.analysis(doc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
