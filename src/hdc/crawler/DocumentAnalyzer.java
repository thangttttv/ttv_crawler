package hdc.crawler;

import hdc.util.html.A;
import hdc.util.html.NodeDeleteVisitor;
import hdc.util.html.NodeNormalizeVisitor;
import hdc.util.html.SelectLinkVisitor;
import hdc.util.html.URLRewriteVisitor;
import hdc.util.html.parser.DomWriter;
import hdc.util.html.parser.HtmlParser;
import hdc.util.html.parser.XPathReader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ls.LSException;
import org.xml.sax.SAXParseException;

public interface DocumentAnalyzer {

	public List<A> analyze(String data) throws Exception;

	public List<A> analyze(String data, String url) throws Exception;

	public static class DefaultDocumentAnayzer implements DocumentAnalyzer {
		private String baseUrl;
		private String rewriteUrl;
		private HtmlParser parser;
		private DomWriter writer;
		private NodeDeleteVisitor deleteVisitor;
		private NodeNormalizeVisitor normalVisitor;

		public DefaultDocumentAnayzer(String rewriteUrl, String baseUrl)
				throws Exception {
			this.baseUrl = baseUrl;
			this.rewriteUrl = rewriteUrl;
			this.writer = new DomWriter();
			this.parser = new HtmlParser();

			this.deleteVisitor = new NodeDeleteVisitor() {
				protected boolean shouldDelete(Node node) {
					if (node.getNodeName().equalsIgnoreCase("meta"))
						return true;
					else if (node.getNodeName().equalsIgnoreCase("link"))
						return true;
					else if (node.getNodeName().equalsIgnoreCase("style"))
						return true;
					else if (node.getNodeName().equalsIgnoreCase("script"))
						return true;
					else if (node.getNodeName().equalsIgnoreCase("height"))
						return true;
					else if (node.getNodeName().equalsIgnoreCase("width"))
						return true;
					return false;
				}
			};

			this.normalVisitor = new NodeNormalizeVisitor() {
				protected void normalize(Node node) {
					if (node.hasAttributes()) {
						NamedNodeMap attributes = node.getAttributes();
						if (attributes.getNamedItem("class") != null)
							attributes.removeNamedItem("class");
						if (attributes.getNamedItem("id") != null)
							attributes.removeNamedItem("id");
						if (attributes.getNamedItem("style") != null)
							attributes.removeNamedItem("style");
					}
				}
			};
		}
		
		
		public List<A> analyze(String data) throws Exception {
			Document document = parser.parseNonWellForm(data);
			//      visitor.traverse(document) ;
			//      normalVisitor.traverse(document);
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			try {
				writer.write(output, document);
			} catch (LSException ex) {
				System.out.println(ex.getMessage());
				return null;
			}

			try {
				document = parser.parseWellForm(new ByteArrayInputStream(output
						.toByteArray()));
			} catch (SAXParseException ex) {
				if (ex.getMessage().indexOf("Premature end of file") != -1)
					return null;
				String tmp = new String(output.toByteArray(), "UTF-8");
				tmp = tmp.replaceAll("\\&lt;", "<");
				tmp = tmp.replaceAll("\\&gt;", ">");
				tmp = tmp.replaceAll("\"--\"", "");
				tmp = tmp.replaceAll("&", "amp");
				tmp = tmp.replaceAll("\"", ""); // Node Vi du:<td width="> La Loi
				Pattern comment = Pattern.compile("<!--.*?-->");
				Matcher mcomment = comment.matcher(tmp);
				while (mcomment.find())
					tmp = mcomment.replaceAll("");
				try {
					document = parser.parseWellForm(tmp);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (rewriteUrl != null) {
				URLRewriteVisitor urlVisitor = new URLRewriteVisitor(
						rewriteUrl, baseUrl);
				urlVisitor.traverse(document);
			}

			SelectLinkVisitor linkVisitor = new SelectLinkVisitor(baseUrl);
			linkVisitor.traverse(document);
			return linkVisitor.getLinks();
		}

		public List<A> analyze(String data, String url) throws Exception {
			data = data.replaceAll("\"", ""); // Node Vi du:<td width="> La Loi
			Document document = parser.parseNonWellForm(data);
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			try {
				
				writer.write(output, document);
			} catch (LSException ex) {
				System.out.println(ex.getMessage());
				return null;
			}

			try {
				document = parser.parseWellForm(new ByteArrayInputStream(output
						.toByteArray()));
			} catch (SAXParseException ex) {
				if (ex.getMessage().indexOf("Premature end of file") != -1)
					return null;
				String tmp = new String(output.toByteArray(), "UTF-8");
				tmp = tmp.replaceAll("\\&lt;", "<");
				tmp = tmp.replaceAll("\\&gt;", ">");
				tmp = tmp.replaceAll("\"--\"", "");
				tmp = tmp.replaceAll("&", "amp");
				tmp = tmp.replaceAll("&", "amp");
				tmp = tmp.replaceAll("\"", ""); // Node Vi du:<td width="> La Loi
				Pattern comment = Pattern.compile("<!--.*?-->");
				Matcher mcomment = comment.matcher(tmp);
				while (mcomment.find())
					tmp = mcomment.replaceAll("");
				try {
					document = parser.parseWellForm(tmp);
				} catch (Exception e) {
					XPathReader reader = CrawlerUtil
							.createXPathReaderByData(data);
					document = reader.getDocument();
					System.out.println("Error:SaxParse.");
				}
			}

			if (rewriteUrl != null) {
				URLRewriteVisitor urlVisitor = new URLRewriteVisitor(
						rewriteUrl, baseUrl);
				urlVisitor.traverse(document);
			}

			SelectLinkVisitor linkVisitor = new SelectLinkVisitor(baseUrl);
			linkVisitor.traverse(document);
			return linkVisitor.getLinks();
		}
	}
}