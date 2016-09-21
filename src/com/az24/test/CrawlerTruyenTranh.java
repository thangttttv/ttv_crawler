package com.az24.test;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.util.html.parser.DomWriter;
import hdc.util.html.parser.XPathReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.xpath.XPathConstants;

import org.apache.http.HttpResponse;
import org.w3c.dom.NodeList;

import com.az24.crawler.fiter.ContentFilter;
import com.az24.util.io.FileUtil;

public class CrawlerTruyenTranh {
	public String getHTML(String urlToRead) {
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
				result += line + "\n";
			}
			rd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		FileUtil fileUtil = new FileUtil();
		try {
			fileUtil.writeToFile("C://Projects/bongda.html", result, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(result);
		return result;
	}

	public void extractDailyMoney(String url, String xpathsubject, String xpathContent,
			String xpathImage, String xpathcity, String xpathcontact,
			String token) throws Exception {
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);
		html = getHTML(url);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		CrawlerUtil.analysis(reader.getDocument(), token);
		// Title
		String nodeList = (String) reader.read(xpathsubject,
				XPathConstants.STRING);

		System.out.println("Title=" + nodeList);

		// Image
		reader = CrawlerUtil.createXPathReaderByData(html);
		nodeList = (String) reader.read(xpathImage, XPathConstants.STRING);

		System.out.println("Image=" + nodeList);

		// xpathcity
		reader = CrawlerUtil.createXPathReaderByData(html);
		nodeList = (String) reader.read(xpathcity, XPathConstants.STRING);

		System.out.println("City=" + nodeList);

		// contact
		reader = CrawlerUtil.createXPathReaderByData(html);
		nodeList = (String) reader.read(xpathcontact, XPathConstants.STRING);

		System.out.println("Contact=" + nodeList);

		// Content
		reader = CrawlerUtil.createXPathReaderByData(html);
		NodeList nodeList1 = (NodeList) reader.read(xpathContent,
				XPathConstants.NODESET);
		DomWriter writer = new DomWriter();

		// deleteVisitor.traverse(nodeList1.item(0));
		System.out.println(ContentFilter.changeLink(writer
				.toXMLString(nodeList1.item(0))));

		System.out.println("Content" + writer.toXMLString(nodeList1.item(0)));
	}

	public static void main(String[] args) {
		CrawlerTruyenTranh crawler = new CrawlerTruyenTranh();
		System.out.println(crawler.getHTML("http://bongda.wap.vn/process-web-livescore.jsp"));
	}
}
