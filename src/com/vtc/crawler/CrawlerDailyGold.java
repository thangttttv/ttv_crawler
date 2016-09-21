package com.vtc.crawler;

import java.sql.Date;
import java.util.Calendar;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.util.html.parser.XPathReader;
import hdc.util.text.StringUtil;

import javax.xml.xpath.XPathConstants;

import org.apache.http.HttpResponse;
import org.w3c.dom.NodeList;

import com.az24.crawler.model.Gold;
import com.az24.dao.TienIchDAO;
import com.az24.test.HttpClientUtil;

public class CrawlerDailyGold {
	
	public void collectionLink() throws Exception {
	
		String url ="http://hn.24h.com.vn/ttcb/giavang/giavang.php";
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		String xpath__tag= "//table[@class='tb-giaVang']/tbody[1]/tr";
		NodeList linkNodes = (NodeList) reader.read(xpath__tag, XPathConstants.NODESET);
		int node_one_many = linkNodes.getLength();
		int i=3;
		TienIchDAO tienIchDAO = new TienIchDAO();
		String create_date_today = (String) reader.read(xpath__tag+"[1]/td[2]/span[1]/text()", XPathConstants.STRING);
		create_date_today=StringUtil.parseDate(create_date_today);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Integer.parseInt(create_date_today.split("-")[2]), 
				Integer.parseInt(create_date_today.split("-")[1]),Integer.parseInt(create_date_today.split("-")[0]));
		Date today = new Date(calendar.getTimeInMillis());
		
		String create_date_yesterday = (String) reader.read(xpath__tag+"[1]/td[3]/span[1]/text()", XPathConstants.STRING);
		create_date_yesterday=StringUtil.parseDate(create_date_yesterday);
		
		calendar = Calendar.getInstance();
		calendar.set(Integer.parseInt(create_date_yesterday.split("-")[2]), 
				Integer.parseInt(create_date_yesterday.split("-")[1]),Integer.parseInt(create_date_yesterday.split("-")[0]));
		Date yesterday = new Date(calendar.getTimeInMillis());
		
		while (i <= node_one_many) {
			String province = (String) reader.read(xpath__tag+"["+ i + "]/td[@colspan='5']/text()", XPathConstants.STRING);
			if(!StringUtil.isEmpty(province)) {
				i++;
				continue;
			}

			String unit = (String) reader.read(xpath__tag+"["+ i + "]/td[1]/span[1]/text()", XPathConstants.STRING);
			String buy =  (String) reader.read(xpath__tag+"["+ i + "]/td[2]/span[1]/text()", XPathConstants.STRING);
			String sale = (String) reader.read(xpath__tag+"["+ i + "]/td[3]/span[1]/text()", XPathConstants.STRING);
			String buy_1 = (String) reader.read(xpath__tag+"["+ i + "]/td[4]/text()", XPathConstants.STRING);
			String sale_1 = (String) reader.read(xpath__tag+"["+ i + "]/td[5]/text()", XPathConstants.STRING);
			System.out.println(i+" = "+ unit+" "+buy+" "+sale+" "+buy_1+" "+sale_1);
			
			Gold gold = new Gold();
			gold.setBuy(buy);
			gold.setProvider("sjc");
			gold.setProvince_id(0);
			gold.setSale(sale);
			gold.setUnit(unit);
			gold.setCreate_date(today);
			
			if(!StringUtil.isEmpty(sale)||!StringUtil.isEmpty(buy))
			tienIchDAO.saveGold(gold);
			
			gold = new Gold();
			gold.setBuy(buy_1);
			gold.setProvider("sjc");
			gold.setProvince_id(0);
			gold.setSale(sale_1);
			gold.setUnit(unit);
			gold.setCreate_date(yesterday);
			
			if(!StringUtil.isEmpty(sale_1)||!StringUtil.isEmpty(buy_1))
			tienIchDAO.saveGold(gold);
			
			i++;	
			
		}
	}
	
	public static void main(String[] args) {
		CrawlerDailyGold crawlerDailyMoney = new CrawlerDailyGold();
		try {
			crawlerDailyMoney.collectionLink();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
