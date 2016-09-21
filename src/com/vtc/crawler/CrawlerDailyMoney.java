package com.vtc.crawler;

import java.util.Calendar;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.util.html.parser.XPathReader;
import hdc.util.text.StringUtil;

import javax.xml.xpath.XPathConstants;
import org.apache.http.HttpResponse;
import org.w3c.dom.NodeList;

import com.az24.crawler.model.Money;
import com.az24.dao.TienIchDAO;
import com.az24.test.HttpClientUtil;

public class CrawlerDailyMoney {
	
	public void collectionLink() throws Exception {
		String url ="http://hn.24h.com.vn/ttcb/tygia/tygia.php";
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		String xpath__tag= "//table[@class='tb-giaVang']/TBODY[1]/tr";
		NodeList linkNodes = (NodeList) reader.read(xpath__tag, XPathConstants.NODESET);
		int node_one_many = linkNodes.getLength();
		int i=3;
		
		TienIchDAO tienIchDAO = new TienIchDAO();
		
		String xpath__date_1= "//table[@class='tb-giaVang']/TBODY[1]/tr[1]/td[2]/span[1]/text()";
		String date_1 =  (String) reader.read(xpath__date_1, XPathConstants.STRING);
		date_1= StringUtil.parseDate(date_1.trim());
		Calendar calendar = Calendar.getInstance();
		
		calendar.set(Integer.parseInt(date_1.split("/")[2]), Integer.parseInt(date_1.split("/")[1]), Integer.parseInt(date_1.split("/")[0]));
		java.sql.Date odate_1 = new  java.sql.Date(calendar.getTimeInMillis());
		
		String xpath__date_2= "//table[@class='tb-giaVang']/TBODY[1]/tr[1]/td[3]/span[1]/text()";
		String date_2 =  (String) reader.read(xpath__date_2, XPathConstants.STRING);
		date_2= StringUtil.parseDate(date_2.trim());
		calendar = Calendar.getInstance();
		
		calendar.set(Integer.parseInt(date_2.split("/")[2]), Integer.parseInt(date_2.split("/")[1]), Integer.parseInt(date_2.split("/")[0]));
		java.sql.Date odate_2 = new  java.sql.Date(calendar.getTimeInMillis());
		
		
		
		while (i <= node_one_many) {
			Money money = new Money();
			String unit = (String) reader.read(xpath__tag+"["+ i + "]/td[1]/span[1]/text()", XPathConstants.STRING);
			String buy =  (String) reader.read(xpath__tag+"["+ i + "]/td[2]/span[1]/text()", XPathConstants.STRING);
			String tranfer = (String) reader.read(xpath__tag+"["+ i + "]/td[3]/span[1]/text()", XPathConstants.STRING);
			String sale = (String) reader.read(xpath__tag+"["+ i + "]/td[4]/span[1]/text()", XPathConstants.STRING);
			money.unit = unit;
			try{
			money.buy = Float.valueOf(buy.replaceAll(",", ""));
			}catch (Exception e) {				
			}
			try{
			money.sale = Float.valueOf(sale.replaceAll(",", ""));
			
			}catch (Exception e) {				
			}
			try{
			money.tranfer = Float.valueOf(tranfer.replaceAll(",", ""));
			}catch (Exception e) {				
			}
			money.create_date = odate_1;
			tienIchDAO.saveMoney(money);
			
			String buy_1 = (String) reader.read(xpath__tag+"["+ i + "]/td[5]/text()", XPathConstants.STRING);
			String tranfer_1 =  (String) reader.read(xpath__tag+"["+ i + "]/td[6]/text()", XPathConstants.STRING);
			String sale_1 = (String) reader.read(xpath__tag+"["+ i + "]/td[7]/text()", XPathConstants.STRING);
			i++;			
			money = new Money();
			money.unit = unit;
			try{
			money.buy = Float.valueOf(buy_1.replaceAll(",", ""));
			
			}catch (Exception e) {				
			}
			try{
			money.sale = Float.valueOf(sale_1.replaceAll(",", ""));
			System.out.println("money.sale="+money.sale);
			}catch (Exception e) {				
			}
			try{
			money.tranfer = Float.valueOf(tranfer_1.replaceAll(",", ""));
			}catch (Exception e) {				
			}
			money.create_date = odate_2;
			tienIchDAO.saveMoney(money);
			
			System.out.println(i);
			System.out.println(unit+" "+buy+" "+tranfer+" "+sale);
			System.out.println(buy_1+" "+tranfer_1+" "+sale_1);
		}
	}
	
	public static void main(String[] args) {
		CrawlerDailyMoney crawlerDailyMoney = new CrawlerDailyMoney();
		try {
			crawlerDailyMoney.collectionLink();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
