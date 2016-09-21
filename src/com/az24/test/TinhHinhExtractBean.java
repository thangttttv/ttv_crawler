package com.az24.test;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.crawler.fetcher.HttpClientUtil;
import hdc.util.html.parser.DomWriter;
import hdc.util.html.parser.XPathReader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathConstants;

import org.apache.http.HttpResponse;
import org.json.simple.JSONValue;
import org.w3c.dom.NodeList;

import com.az24.util.io.FileUtil;

public class TinhHinhExtractBean {

	public void extract(String url, String xpath_title, String xpath_tomtat,
			String xpath_tags, String xpath_tags_sub, String xpath_time,
			String xpath_content, String[] arrNodeDel, String token)
			throws Exception {
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		FileUtil fileUtil = new FileUtil();
		String html = HttpClientUtil.getResponseBody(res);
		//html=html.replace("class=\"row0 height=\"18\"", ""); 
		//html=html.replace("class=\"row1 height=\"18\"", "");
		//fileUtil.writeToFile("d:/html2.html",html, false);
		//String html2 = IOUtil.getFileContenntAsString(new File("d:/html2.html"));
		//html=HttpClientUtil.getHtml(url);
	
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		CrawlerUtil.analysis(reader.getDocument());
		
		
		String title = (String) reader.read(xpath_title, XPathConstants.STRING);
		System.out.println("Tieu De = " + title);
		

		
		String tomtat = (String) reader.read(xpath_tomtat,
				XPathConstants.STRING);
		System.out.println("Tom tat = " + tomtat);
		String time = (String) reader.read(xpath_time, XPathConstants.STRING);
		System.out.println("Time = " + time);

		NodeList nodes = (NodeList) reader.read(xpath_tags,
				XPathConstants.NODESET);
		if (nodes != null) {
			int node_one_many = nodes.getLength();
			int i = 1;

			while (i <= node_one_many) {
				String tag = (String) reader.read(xpath_tags + "[" + i + "]"
						+ xpath_tags_sub, XPathConstants.STRING);
				System.out.println(tag.trim());

				i++;
			}
		}

		/*nodes = (NodeList) reader.read("//a", XPathConstants.NODESET);
		if (nodes != null) {
			int node_one_many = nodes.getLength();
			int i = 0;

			while (i < node_one_many) {
				Node node = reader.getDocument().createTextNode(nodes.item(i).getTextContent());
				nodes.item( i).getParentNode().replaceChild(node, nodes.item(i));
				String tag = nodes.item(i).getTextContent();
				System.out.println("I=" + i + "-----" + tag.trim());

				i++;
			}
		}*/
		DomWriter writer = new DomWriter();
		//fileUtil.writeToFile("d:/log2.html",writer.toXMLString(reader.getDocument()), true);
		for (String xpath : arrNodeDel) {
			CrawlerUtil.removeNodeByXpath(reader, xpath);
		}

		
		NodeList contents = (NodeList) reader.read(xpath_content,
				XPathConstants.NODESET);
		System.out.println("Content = " + writer.toXMLString(contents.item(0)));

	}
	
	public void extractFormXml(String url, String xpath_title, String xpath_tomtat,
			String xpath_tags, String xpath_tags_sub, String xpath_time,
			String xpath_content, String[] arrNodeDel, String token)
			throws Exception {
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);
		 
	
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		CrawlerUtil.analysis(reader.getDocument(), token);
		String title = (String) reader.read(xpath_title, XPathConstants.STRING);
		System.out.println("Tieu De = " + title);
		String tomtat = (String) reader.read(xpath_tomtat,
				XPathConstants.STRING);
		
		NodeList nodes = (NodeList) reader.read("//result[@name='response']/doc",XPathConstants.NODESET);
		if (nodes != null) {
			int node_one_many = nodes.getLength();
			int i = 1;

			while (i <= node_one_many) {
				String avatar = (String) reader.read(xpath_tags + "[" + i + "]"
						+ "/str[@name='avatar']/text()", XPathConstants.STRING);
				System.out.println(avatar.trim());

				i++;
			}
		}

		
	}

	public void extractFromJSon(String url, String xpath_title,
			String xpath_tomtat, String xpath_tags, String xpath_tags_sub,
			String xpath_time, String xpath_content, String[] arrNodeDel,
			String token) throws Exception {
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);
		Pattern pattern = Pattern.compile("\\s{2}");
		Matcher m = pattern.matcher(html);
		html = m.replaceAll(" ");
		pattern = Pattern.compile("getSearchData.*\"docs\":");
		m = pattern.matcher(html);
		
		if (m.find()) {
			html = m.replaceAll("");
			pattern = Pattern.compile("},\\s+\"highlighting\":.*");
			m = pattern.matcher(html);
			html = m.replaceAll("");	
			Object obj = JSONValue.parse(html);
			org.json.simple.JSONArray array = (org.json.simple.JSONArray) obj;
	
			int i = 0;
			String url_bb = "", title = "", lead = "", image = "";
			while (i < array.size()) {
				org.json.simple.JSONObject obj2 = (org.json.simple.JSONObject) array
						.get(i);
				if(i==1)
				{
					url_bb = (String) obj2.get("url");
					System.out.println(url_bb);
					title = (String) obj2.get("title");
					System.out.println(title);
					lead = (String) obj2.get("lead");
					System.out.println(lead);
					image = (String) obj2.get("avatar");
					System.out.println(image);
				}
				i++;
			}

		}

	}

	public static void main(String[] args) {
		TinhHinhExtractBean classifieExtractBean = new TinhHinhExtractBean();
		String url = "http://happyring.vietnamobile.com.vn/media/lookupMediaList.action?index=0&d-16544-p=253&action=&feature=&pk=62";
		String xpath_title = "//h2[@class='post-title entry-title']/a[1]/text()";
		String xpath_tomtat = "html/body[1]/div[@id='EB-Wrapper']/center[1]/div[6]/div[@id='DETAIL_RIGHT']/div[@id='image_info']/div[1]/span[1]/@text";
		String xpath_content = "//div[@id='content']/div[@class='entry']";
		String xpath_time = "//div[@id='date']/text()";
		String xpath_tags = "//div[@id='content']";
		String xpath_tags_sub = "/text()";
		String[] arrXpathNodel = {"//div[@id='content']/div[@class='entry']/center[1]"
				,"//div[@id='content']/div[@class='entry']/table[@width='620']"
				,"//div[@id='content']/div[@class='entry']/div[@class='bvlq']"
				,"//div[@id='content']/div[@class='entry']/div[@class='bvnn']"
				,"//div[@id='content']/div[@class='entry']/div[@class='su-linkbox']"
				,"//div[@id='content']/div[@class='entry']/div[@class='tags']"
				};
		try {
			
			classifieExtractBean.extract(url, xpath_title,
					xpath_tomtat, xpath_tags, xpath_tags_sub, xpath_time,
					xpath_content, arrXpathNodel, "Hà Nội");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
