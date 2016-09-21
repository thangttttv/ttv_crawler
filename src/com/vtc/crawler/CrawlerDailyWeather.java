package com.vtc.crawler;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.util.html.parser.XPathReader;
import hdc.util.text.StringUtil;

import java.util.Calendar;

import javax.xml.xpath.XPathConstants;

import org.apache.http.HttpResponse;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.az24.crawler.model.Weather;
import com.az24.dao.TienIchDAO;
import com.az24.test.HttpClientUtil;

public class CrawlerDailyWeather {
	
	public void weatherNow() throws Exception {
		
		String url ="http://www.nchmf.gov.vn/web/vi-VN/81/Default.aspx";
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		String xpath__weather_td= "//TABLE[@id='_ctl1__ctl0__ctl0_dl_Bantindubao']/TBODY[1]/TR";
		NodeList linkNodes = (NodeList) reader.read(xpath__weather_td, XPathConstants.NODESET);
		int i = 1;
		String xpath_updatetime = "//TD[@class='SubTitleNews_Special']";
		Node updatetime =  (Node) reader.read(xpath_updatetime, XPathConstants.NODE);
		System.out.println(updatetime.getTextContent().trim());
		
		TienIchDAO tienIchDAO = new TienIchDAO();
		
		if(linkNodes!=null)
		{
			while(i<linkNodes.getLength())
			{
				Weather weather = new Weather();
				
				String xpath_province = "//TABLE[@id='_ctl1__ctl0__ctl0_dl_Bantindubao']/TBODY[1]/TR["+i+"]/TD[1]/TABLE[1]/TBODY[1]/TR[1]/TD[1]/text()";
				String province =  (String) reader.read(xpath_province, XPathConstants.STRING);
				System.out.println(province);
				weather.province_id = 0;
				
				String xpath_info = "//TABLE[@id='_ctl1__ctl0__ctl0_dl_Bantindubao']/TBODY[1]/TR["+i+"]/TD[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]";
				String info =  (String) reader.read(xpath_info, XPathConstants.STRING);
				System.out.println(StringUtil.trim(info));
				weather.info =StringUtil.trim(info).trim();
				
				String xpath_temperature = "//TABLE[@id='_ctl1__ctl0__ctl0_dl_Bantindubao']/TBODY[1]/TR["+i+"]/TD[1]/TABLE[1]/TBODY[1]/TR[1]/TD[3]";
				String temperature =  (String) reader.read(xpath_temperature, XPathConstants.STRING);
				System.out.println(StringUtil.parseNumber(temperature.trim()));
				weather.temperature_f = Float.valueOf(StringUtil.parseNumber(temperature));
				
				String xpath_humidity= "//TABLE[@id='_ctl1__ctl0__ctl0_dl_Bantindubao']/TBODY[1]/TR["+i+"]/TD[1]/TABLE[1]/TBODY[1]/TR[1]/TD[4]";
				String humidity =  (String) reader.read(xpath_humidity, XPathConstants.STRING);
				System.out.println(StringUtil.parseNumber(humidity.trim()));
				weather.humidity = Float.valueOf(StringUtil.parseNumber(humidity));
				
				String xpath_wind = "//TABLE[@id='_ctl1__ctl0__ctl0_dl_Bantindubao']/TBODY[1]/TR["+i+"]/TD[1]/TABLE[1]/TBODY[1]/TR[1]/TD[5]";
				String wind =  (String) reader.read(xpath_wind, XPathConstants.STRING);
				System.out.println(wind.trim());
				weather.wind = StringUtil.trim(wind).trim();
				
				weather.create_date = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
				tienIchDAO.saveWeather(weather);
				
				i++;
			}
		}
	}
	
	
	public void collectionLink(int province_id,String url) throws Exception {
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		//CrawlerUtil.analysis(reader.getDocument(), "31");
		Weather bean_weather_1 = new Weather();
		Weather bean_weather_2 = new Weather();
		Weather bean_weather_3 = new Weather();
		TienIchDAO tienIchDAO = new TienIchDAO();
		
		String xpath__date_1= "//TABLE[@id='_ctl1__ctl0__ctl0_dl_Thoitietthanhpho']/TBODY[1]/TR[1]/TD[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/text()";
		String date_1 =  (String) reader.read(xpath__date_1, XPathConstants.STRING);
		date_1= StringUtil.parseDate(date_1.trim());
		Calendar calendar = Calendar.getInstance();
		
		calendar.set(Integer.parseInt(date_1.split("/")[2]), Integer.parseInt(date_1.split("/")[1]), Integer.parseInt(date_1.split("/")[0]));
		java.sql.Date odate = new  java.sql.Date(calendar.getTimeInMillis());
		bean_weather_1.create_date = odate;
		
		String xpath__date_2= "//TABLE[@id='_ctl1__ctl0__ctl0_dl_Thoitietthanhpho']/TBODY[1]/TR[1]/TD[1]/TABLE[1]/TBODY[1]/TR[1]/TD[3]/text()";
		String date_2 =  (String) reader.read(xpath__date_2, XPathConstants.STRING);
		date_2= StringUtil.parseDate(date_2.trim());
		
		calendar.set(Integer.parseInt(date_2.split("/")[2]), Integer.parseInt(date_2.split("/")[1]), Integer.parseInt(date_2.split("/")[0]));
		odate = new  java.sql.Date(calendar.getTimeInMillis());
		bean_weather_2.create_date = odate;
		
		String xpath__date_3= "//TABLE[@id='_ctl1__ctl0__ctl0_dl_Thoitietthanhpho']/TBODY[1]/TR[1]/TD[1]/TABLE[1]/TBODY[1]/TR[1]/TD[4]/text()";
		String date_3 =  (String) reader.read(xpath__date_3, XPathConstants.STRING);
		date_3= StringUtil.parseDate(date_3.trim());
		
		calendar.set(Integer.parseInt(date_3.split("/")[2]), Integer.parseInt(date_3.split("/")[1]), Integer.parseInt(date_3.split("/")[0]));
		odate = new  java.sql.Date(calendar.getTimeInMillis());
		bean_weather_3.create_date = odate;
	
		
		
		String xpath__weather= "//TABLE[@id='_ctl1__ctl0__ctl0_dl_Thoitietthanhpho__ctl0_pnlText_24']/TBODY[1]/TR[1]/TD[1]/text()";
		String weather_1 =  (String) reader.read(xpath__weather, XPathConstants.STRING);
		System.out.println(weather_1.trim());
		bean_weather_1.info =StringUtil.trim(weather_1).trim();
		
		String xpath__weather_2= "//TABLE[@id='_ctl1__ctl0__ctl0_dl_Thoitietthanhpho__ctl0_pnlText_48']/TBODY[1]/TR[1]/TD[1]/text()";
		String weather_2 =  (String) reader.read(xpath__weather_2, XPathConstants.STRING);
		bean_weather_2.info =StringUtil.trim( weather_2).trim(); 
		
		String xpath__weather_3= "//TABLE[@id='_ctl1__ctl0__ctl0_dl_Thoitietthanhpho__ctl0_pnlText_72']/TBODY[1]/TR[1]/TD[1]/text()";
		String weather_3 =  (String) reader.read(xpath__weather_3, XPathConstants.STRING);
		System.out.println(weather_3.trim());
		bean_weather_3.info =StringUtil.trim(weather_3).trim();
		
		String xpath__weather_4= "//TABLE[@id='_ctl1__ctl0__ctl0_dl_Thoitietthanhpho']/TBODY[1]/TR[1]/TD[1]/TABLE[1]/TBODY[1]/TR[3]/TD[3]/STRONG[1]/text()";
		String weather_4 =  (String) reader.read(xpath__weather_4, XPathConstants.STRING);
		System.out.println(weather_4.trim());
		bean_weather_1.temperature_f = Float.valueOf(StringUtil.parseNumber(weather_4));
		
		String xpath__weather_5= "//TABLE[@id='_ctl1__ctl0__ctl0_dl_Thoitietthanhpho']/TBODY[1]/TR[1]/TD[1]/TABLE[1]/TBODY[1]/TR[3]/TD[4]/STRONG[1]/text()";
		String weather_5 =  (String) reader.read(xpath__weather_5, XPathConstants.STRING);
		System.out.println(weather_5.trim());
		bean_weather_2.temperature_f = Float.valueOf(StringUtil.parseNumber(weather_5));
		
		String xpath__weather_6= "//TABLE[@id='_ctl1__ctl0__ctl0_dl_Thoitietthanhpho']/TBODY[1]/TR[1]/TD[1]/TABLE[1]/TBODY[1]/TR[3]/TD[5]/STRONG[1]/text()";
		String weather_6 =  (String) reader.read(xpath__weather_6, XPathConstants.STRING);
		System.out.println(weather_6.trim());
		bean_weather_3.temperature_f = Float.valueOf(StringUtil.parseNumber(weather_6));
		
		String xpath__weather_7= "//TABLE[@id='_ctl1__ctl0__ctl0_dl_Thoitietthanhpho']/TBODY[1]/TR[1]/TD[1]/TABLE[1]/TBODY[1]/TR[4]/TD[2]/STRONG[1]/text()";
		String weather_7 =  (String) reader.read(xpath__weather_7, XPathConstants.STRING);
		System.out.println(weather_7.trim());
		bean_weather_1.temperature_t = Float.valueOf(StringUtil.parseNumber(weather_7));
		
		String xpath__weather_8= "//TABLE[@id='_ctl1__ctl0__ctl0_dl_Thoitietthanhpho']/TBODY[1]/TR[1]/TD[1]/TABLE[1]/TBODY[1]/TR[4]/TD[3]/STRONG[1]/text()";
		String weather_8 =  (String) reader.read(xpath__weather_8, XPathConstants.STRING);
		System.out.println(weather_8.trim());
		bean_weather_2.temperature_t = Float.valueOf(StringUtil.parseNumber(weather_8));
		
		String xpath__weather_9= "//TABLE[@id='_ctl1__ctl0__ctl0_dl_Thoitietthanhpho']/TBODY[1]/TR[1]/TD[1]/TABLE[1]/TBODY[1]/TR[4]/TD[4]/STRONG[1]/text()";
		String weather_9 =  (String) reader.read(xpath__weather_9, XPathConstants.STRING);
		System.out.println(weather_9.trim());
		bean_weather_3.temperature_t = Float.valueOf(StringUtil.parseNumber(weather_9));
		
		tienIchDAO.saveWeather(bean_weather_1);
		tienIchDAO.saveWeather(bean_weather_2);
		tienIchDAO.saveWeather(bean_weather_3);
	}
	
	public static void main(String[] args) {
		CrawlerDailyWeather crawlerDailyMoney = new CrawlerDailyWeather();
		try {
			String url ="http://www.nchmf.gov.vn/web/vi-VN/62/21/92/map/Default.aspx";
			crawlerDailyMoney.weatherNow();
			crawlerDailyMoney.collectionLink(1,url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
