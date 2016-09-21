package com.vtc.crawler;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.util.html.parser.XPathReader;
import hdc.util.text.StringUtil;
import javax.xml.xpath.XPathConstants;
import org.apache.http.HttpResponse;
import org.w3c.dom.NodeList;
import com.az24.test.HttpClientUtil;

public class CrawlerDailyHospital {
	
	public void crawlerHospital() throws Exception {
		String url ="http://danhba.bacsi.com/category/trung-tam-y-te/";
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		//CrawlerUtil.analysis(reader.getDocument(), "");
		String xpath__tag= "//div[@class='bg_contentsmall']/div";
		NodeList linkNodes = (NodeList) reader.read(xpath__tag, XPathConstants.NODESET);
		int node_one_many = linkNodes.getLength();
		int i=2;
		while (i <= node_one_many) {
			String url_hospital = (String) reader.read(xpath__tag+"["+ i + "]/p/a[1]/@href", XPathConstants.STRING);
			System.out.println(i+" = "+ url_hospital);	
			if(StringUtil.isEmpty(url_hospital)) {i++;continue;}
			try{
				res = client.fetch(url_hospital);
				html = HttpClientUtil.getResponseBody(res);
				XPathReader reader2 = CrawlerUtil.createXPathReaderByData(html);
				String xpath__tag_name= "//div[@class='left_infodetail']/h1[1]/text()";
				String hospital_name = (String) reader2.read(xpath__tag_name, XPathConstants.STRING);
				System.out.println(i+" = "+ hospital_name);	
				String xpath__tag_address= "//div[@class='left_infodetail']/font[1]/text()";
				String hospital_address= (String) reader2.read(xpath__tag_address, XPathConstants.STRING);
				System.out.println(i+" = "+ hospital_address);
				String xpath__tag_tell= "//div[@class='left_infodetail']/font[2]/text()";
				String hospital_tell= (String) reader2.read(xpath__tag_tell, XPathConstants.STRING);
				System.out.println(i+" = "+ hospital_tell);
			}catch (Exception e) {
				e.printStackTrace();
			}
			i++;	
		}
		
		
	}
	
	public static void main(String[] args) {
		CrawlerDailyHospital crawlerDailyHospital = new CrawlerDailyHospital();
		try {
			crawlerDailyHospital.crawlerHospital();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
