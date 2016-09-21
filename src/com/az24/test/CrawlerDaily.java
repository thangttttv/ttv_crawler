package com.az24.test;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.crawler.fetcher.HttpClientUtil;
import hdc.util.html.parser.DomWriter;
import hdc.util.html.parser.XPathReader;

import javax.xml.xpath.XPathConstants;

import org.apache.http.HttpResponse;
import org.w3c.dom.NodeList;

import com.az24.crawler.fiter.ContentFilter;
import com.az24.util.io.FileUtil;

public class CrawlerDaily {

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
			fileUtil.writeToFile("C:/Projects/giay.html", result, false);
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
		CrawlerDaily crawler = new CrawlerDaily();
		crawler.getHTML("http://live.m.7m.cn/");
		crawler.getHTML("http://data2.7m.cn/matches_data/92/en/fixture.js");
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch("http://data2.7m.cn/matches_data/92/en/fixture.js");
		HttpClientUtil.printResponseHeaders(res);
		try {
			String html = HttpClientUtil.getResponseBody(res);
			System.out.println(html);
			 try {
				 URL url = new URL("http://data2.7m.cn/matches_data/92/en/fixture.js");
					InputStream is = url.openStream();
					OutputStream os = new FileOutputStream("C:/Projects/fixture.js");

					byte[] b = new byte[2048];
					int length;

					while ((length = is.read(b)) != -1) {
						os.write(b, 0, length);
					}

					is.close();
					os.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
				
			
		//	String url_team ="http://team.7m.cn/239/data/info_en.js";
			String url_team ="http://www.nowgoal.com/";
			 res = client.fetch(url_team);

			HttpClientUtil.printResponseHeaders(res);
			 html = HttpClientUtil.getResponseBody(res);
			 System.out.println(html);
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
