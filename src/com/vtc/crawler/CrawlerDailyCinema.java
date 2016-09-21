package com.vtc.crawler;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.util.html.parser.XPathReader;

import javax.xml.xpath.XPathConstants;

import org.apache.http.HttpResponse;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.az24.test.HttpClientUtil;

public class CrawlerDailyCinema {
	
	public void crawlerTaxi() throws Exception {
		String url ="http://vietbao.vn/vn/lich-chieu-phim/tphcm/ngay-10-10-2012/";
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		CrawlerUtil.analysis(reader.getDocument(), "");
		
		String xpath__tag= "HTML/BODY[1]/DIV[3]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/TABLE[1]/TBODY[1]/TR";
		NodeList linkNodes = (NodeList) reader.read(xpath__tag, XPathConstants.NODESET);
		int node_one_many = linkNodes.getLength();
		int i=2;
		while (i <= node_one_many) {
			String province = (String) reader.read(xpath__tag+"["+ i + "]/TD[1]/P[1]/text()", XPathConstants.STRING);
			System.out.println(i+" = "+ province);
			Node taxi_name = (Node) reader.read(xpath__tag+"["+ i + "]/TD[2]", XPathConstants.NODE);
			System.out.println(i+" = "+ taxi_name.getTextContent().trim());
			Node taxi_phone = (Node) reader.read(xpath__tag+"["+ i + "]/TD[3]", XPathConstants.NODE);
			System.out.println(i+" = "+ taxi_phone.getTextContent().trim());
			i++;	
		}
		
		xpath__tag= "HTML/BODY[1]/DIV[3]/DIV[1]/DIV[1]/DIV[2]/TABLE[1]/TBODY[1]/TR";
		linkNodes = (NodeList) reader.read(xpath__tag, XPathConstants.NODESET);
		node_one_many = linkNodes.getLength();
		i=2;
		while (i <= node_one_many) {
			String province = (String) reader.read(xpath__tag+"["+ i + "]/TD[1]/P[1]/text()", XPathConstants.STRING);
			System.out.println(i+" = "+ province);
			Node taxi_name = (Node) reader.read(xpath__tag+"["+ i + "]/TD[2]", XPathConstants.NODE);
			System.out.println(i+" = "+ taxi_name.getTextContent().trim());
			Node taxi_phone = (Node) reader.read(xpath__tag+"["+ i + "]/TD[3]", XPathConstants.NODE);
			System.out.println(i+" = "+ taxi_phone.getTextContent().trim());
			i++;	
		}
	}
	
	public static void main(String[] args) {
		CrawlerDailyTaxi crawlerDailyTivi = new CrawlerDailyTaxi();
		try {
			crawlerDailyTivi.crawlerTaxi();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
