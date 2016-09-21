package com.vtc.crawler;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.util.html.parser.XPathReader;
import hdc.util.text.StringUtil;

import javax.xml.xpath.XPathConstants;

import org.apache.http.HttpResponse;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.az24.test.HttpClientUtil;

public class CrawlerDailyHotel {
	
	public void crawlerHotel(String url,int page) throws Exception {
		int i=1,p=1;
		while(p<=page){
			//http://www.khachsan.dulichvietnam.com.vn/city/khachsan/51/0/10/0/0/khach-san-hue.html
			url = "http://www.khachsan.dulichvietnam.com.vn/city/khachsan/51/0/10/0/0/"+p+"/khach-san-hue.html";
			HttpClientImpl client = new HttpClientImpl();
			HttpResponse res = client.fetch(url);
			HttpClientUtil.printResponseHeaders(res);
			String html = HttpClientUtil.getResponseBody(res);
			XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
			//CrawlerUtil.analysis(reader.getDocument(), "");
			String xpath__tag= "//div[@id='back_content']/div[5]/div";
			NodeList linkNodes = (NodeList) reader.read(xpath__tag, XPathConstants.NODESET);
			int node_one_many = linkNodes.getLength();
			i=1;
			while (i <= node_one_many) {
				String hotel = (String) reader.read(xpath__tag+"["+ i + "]/div[2]/h1[1]/a[1]/text()", XPathConstants.STRING);
				System.out.println(i+" = "+ hotel);
				if(!StringUtil.isEmpty(hotel))
				{
					NodeList start = (NodeList) reader.read(xpath__tag+"["+ i + "]/div[2]/div[1]/img", XPathConstants.NODESET);
					System.out.println(i+" = "+ start.getLength());
					Node address = (Node) reader.read(xpath__tag+"["+ i + "]/div[2]/div[2]", XPathConstants.NODE);
					System.out.println(i+" = "+ address.getTextContent().trim());
					Node tell = (Node) reader.read(xpath__tag+"["+ i + "]/div[2]/div[3]", XPathConstants.NODE);
					System.out.println(i+" = "+ tell.getTextContent().trim());
					Node fax = (Node) reader.read(xpath__tag+"["+ i + "]/div[2]/div[4]", XPathConstants.NODE);
					System.out.println(i+" = "+ fax.getTextContent().trim());
					Node web = (Node) reader.read(xpath__tag+"["+ i + "]/div[2]/div[5]", XPathConstants.NODE);
					System.out.println(i+" = "+ web.getTextContent().trim());
				}
				/*Node taxi_name = (Node) reader.read(xpath__tag+"["+ i + "]/TD[2]", XPathConstants.NODE);
				System.out.println(i+" = "+ taxi_name.getTextContent().trim());
				Node taxi_phone = (Node) reader.read(xpath__tag+"["+ i + "]/TD[3]", XPathConstants.NODE);
				System.out.println(i+" = "+ taxi_phone.getTextContent().trim());*/
				i++;	
			}
			p++;
		}
		
	}
	
	public static void main(String[] args) {
		CrawlerDailyHotel crawlerDailyTivi = new CrawlerDailyHotel();
		try {
			String url ="http://www.khachsan.dulichvietnam.com.vn/city/khachsan/20/0/10/0/0/khach-san-ha-noi.html";
			crawlerDailyTivi.crawlerHotel(url,18);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
