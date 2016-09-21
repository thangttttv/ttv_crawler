package com.vtc.crawler;

import java.util.Calendar;
import java.sql.Date;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.util.html.parser.XPathReader;
import hdc.util.text.StringUtil;

import javax.xml.xpath.XPathConstants;

import org.apache.http.HttpResponse;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.az24.crawler.model.Taxi;
import com.az24.dao.TienIchDAO;
import com.az24.test.HttpClientUtil;

public class CrawlerDailyTaxi {
	
	public void crawlerTaxi() throws Exception {
		String url ="http://www.dulichvietnam.com.vn/danh-ba-cac-hang-taxi-tai-viet-nam.html";
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
		TienIchDAO tienIchDAO = new TienIchDAO();
		while (i <= node_one_many) {
			Taxi taxi = new Taxi();
			taxi.province_id = 0;
			String province = (String) reader.read(xpath__tag+"["+ i + "]/TD[1]/P[1]/text()", XPathConstants.STRING);
			System.out.println(i+" = "+ province);
			Node taxi_name = (Node) reader.read(xpath__tag+"["+ i + "]/TD[2]", XPathConstants.NODE);
			System.out.println(i+" = "+ taxi_name.getTextContent().trim());
			taxi.firm = StringUtil.trim(taxi_name.getTextContent()); 
			Node taxi_phone = (Node) reader.read(xpath__tag+"["+ i + "]/TD[3]", XPathConstants.NODE);
			System.out.println(i+" = "+ taxi_phone.getTextContent().trim());
			taxi.phone = StringUtil.trim(taxi_phone.getTextContent());
			taxi.create_date = new Date(Calendar.getInstance().getTimeInMillis());
			tienIchDAO.saveTaxi(taxi);
			i++;	
		}
		
		xpath__tag= "HTML/BODY[1]/DIV[3]/DIV[1]/DIV[1]/DIV[2]/TABLE[1]/TBODY[1]/TR";
		linkNodes = (NodeList) reader.read(xpath__tag, XPathConstants.NODESET);
		node_one_many = linkNodes.getLength();
		i=2;
		while (i <= node_one_many) {
			Taxi taxi = new Taxi();
			taxi.province_id = 0;
			String province = (String) reader.read(xpath__tag+"["+ i + "]/TD[1]/P[1]/text()", XPathConstants.STRING);
			System.out.println(i+" = "+ province);
			Node taxi_name = (Node) reader.read(xpath__tag+"["+ i + "]/TD[2]", XPathConstants.NODE);
			System.out.println(i+" = "+ taxi_name.getTextContent().trim());
			Node taxi_phone = (Node) reader.read(xpath__tag+"["+ i + "]/TD[3]", XPathConstants.NODE);
			System.out.println(i+" = "+ taxi_phone.getTextContent().trim());
			taxi.firm = StringUtil.trim(taxi_name.getTextContent()); 
			
			taxi.phone = StringUtil.trim(taxi_phone.getTextContent());
			taxi.create_date = new Date(Calendar.getInstance().getTimeInMillis());
			tienIchDAO.saveTaxi(taxi);
			
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
