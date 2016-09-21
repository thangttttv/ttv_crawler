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

import com.az24.crawler.model.Bus;
import com.az24.dao.TienIchDAO;
import com.az24.test.HttpClientUtil;

public class CrawlerDailyBus {
	
	TienIchDAO tienIchDAO = new TienIchDAO();
	
	public void crawlerBusHaNoi() throws Exception {
		String url ="http://www.hanoibus.com.vn/Default.aspx?pageid=253";
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		CrawlerUtil.analysis(reader.getDocument(), "");
		String xpath__tag= "//TABLE[@id='table2']/TBODY[1]/TR";
		NodeList linkNodes = (NodeList) reader.read(xpath__tag, XPathConstants.NODESET);
		int node_one_many = linkNodes.getLength();
		int i=3;
		while (i <= node_one_many) {			
			String sohieu = (String) reader.read(xpath__tag+"["+ i + "]/TD[1]/P[1]", XPathConstants.STRING);
			System.out.println(i+" = "+ sohieu.trim());
			String ten_sh_tuyen = (String) reader.read(xpath__tag+"["+ i + "]/TD[1]/P[2]", XPathConstants.STRING);
			System.out.println(i+" = "+ ten_sh_tuyen.trim());
			String ten_tuyen = (String) reader.read(xpath__tag+"["+ i + "]/TD[2]/P[1]", XPathConstants.STRING);
			System.out.println(i+" = "+ ten_tuyen.trim());
			String tuyent_hdd = (String) reader.read(xpath__tag+"["+ i + "]/TD[2]/P[2]", XPathConstants.STRING);
			System.out.println(i+" = "+ tuyent_hdd.trim());
			String tuyent_ts = (String) reader.read(xpath__tag+"["+ i + "]/TD[2]/P[3]", XPathConstants.STRING);
			System.out.println(i+" = "+ tuyent_ts.trim());
			String tuyent_ld = (String) reader.read(xpath__tag+"["+ i + "]/TD[3]", XPathConstants.STRING);
			System.out.println(i+" = "+ tuyent_ld.trim());
			String tuyent_lv = (String) reader.read(xpath__tag+"["+ i + "]/TD[4]", XPathConstants.STRING);
			System.out.println(i+" = "+ tuyent_lv.trim());
			
			Bus bus = new Bus(); 
			bus.code= StringUtil.trim(sohieu);
			bus.name= StringUtil.trim(ten_tuyen);
			bus.uptime= StringUtil.trim(tuyent_hdd);
			bus.frequency= StringUtil.trim(tuyent_ts);
			bus.path_1= StringUtil.trim(tuyent_ld);
			bus.path_2= StringUtil.trim(tuyent_lv);
			bus.create_date = new Date(Calendar.getInstance().getTimeInMillis());
			tienIchDAO.saveBUS(bus);
			i++;	
		}
		
		
	}
	
	public void crawlerBusHCM() throws Exception {
		String url ="http://www.buyttphcm.com.vn/TTLT.aspx";
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		CrawlerUtil.analysis(reader.getDocument(), "");
		String xpath__tag= "//table[@id='ctl00_ContentPlaceHolder1_GridView1']/TBODY[1]/tr";
		NodeList linkNodes = (NodeList) reader.read(xpath__tag, XPathConstants.NODESET);
		int node_one_many = linkNodes.getLength();
		int i=2;
		while (i <= node_one_many) {
			Bus bus = new Bus(); 
			bus.uptime= StringUtil.trim(" ");
			bus.frequency= StringUtil.trim("");
			String sohieu = (String) reader.read(xpath__tag+"["+ i + "]/td[1]", XPathConstants.STRING);
			System.out.println(i+" = "+ sohieu.trim());
			String ten_sh_tuyen = (String) reader.read(xpath__tag+"["+ i + "]/td[2]", XPathConstants.STRING);
			System.out.println(i+" = "+ ten_sh_tuyen.trim());
			String link = (String) reader.read(xpath__tag+"["+ i + "]/td[2]/a[1]/@href", XPathConstants.STRING);
			System.out.println(i+" = "+ link.trim());
			String lv ="",ld="",other="";
			if(!StringUtil.isEmpty(link)){
				res = client.fetch("http://www.buyttphcm.com.vn/"+link.trim());
				String html_2 = HttpClientUtil.getResponseBody(res);
				XPathReader reader_2 = CrawlerUtil.createXPathReaderByData(html_2);
				String xpath_detail_ld = "//div[@id='ctl00_ContentPlaceHolder1_UpdatePanel2']/table[2]/TBODY[1]/tr[3]/td[1]";
				ld = (String) reader_2.read(xpath_detail_ld, XPathConstants.STRING);
				System.out.println(i+" = "+ ld.trim());
				String xpath_detail_lv = "//div[@id='ctl00_ContentPlaceHolder1_UpdatePanel2']/table[2]/TBODY[1]/tr[4]/td[1]";
				lv = (String) reader_2.read(xpath_detail_lv, XPathConstants.STRING);
				System.out.println(i+" = "+ lv.trim());
				
				String xpath_detail_other = "//div[@id='ctl00_ContentPlaceHolder1_UpdatePanel2']/table[2]/TBODY[1]/tr[5]/td[1]";
				other = (String) reader_2.read(xpath_detail_other, XPathConstants.STRING);
			//	System.out.println(i+" = "+ other.trim());
				
				String xpath_detail_other_li= "//div[@id='ctl00_ContentPlaceHolder1_UpdatePanel2']/table[2]/TBODY[1]/tr[5]/td[1]/ul[1]/li";
				NodeList linkNodes_li = (NodeList) reader_2.read(xpath_detail_other_li, XPathConstants.NODESET);
				int snut = linkNodes_li.getLength();
				int j = 1;
				while(j<=snut){
					String other_detail = (String) reader_2.read("//div[@id='ctl00_ContentPlaceHolder1_UpdatePanel2']/table[2]/TBODY[1]/tr[5]/td[1]/ul[1]/li["+j+"]/text()", XPathConstants.STRING);
					System.out.println(j +" detail li= "+ other_detail.trim());
					if(other_detail!=null&&other_detail.indexOf("Giãn cách")>-1) bus.frequency = StringUtil.trim(other_detail).replaceAll("Giãn cách:", "");
					if(other_detail!=null&&other_detail.indexOf("Thời gian hoạt động")>-1) bus.uptime = StringUtil.trim(other_detail).replaceAll("Thời gian hoạt động:", "");
					j++;
				}
				
			}
			
			bus.code= StringUtil.trim(sohieu);
			bus.name= StringUtil.trim(ten_sh_tuyen);
			
			bus.path_1= StringUtil.trim(ld).replaceAll("Lượt đi :", "");
			bus.path_2= StringUtil.trim(lv).replaceAll("Lượt về :", "");
			bus.other = StringUtil.trim(other);
			bus.create_date = new Date(Calendar.getInstance().getTimeInMillis());
			tienIchDAO.saveBUS(bus);
			i++;	
		}
		
		
	}
	
	public static void main(String[] args) {
		CrawlerDailyBus crawlerDailyTivi = new CrawlerDailyBus();
		try {
			crawlerDailyTivi.crawlerBusHCM();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
