package com.vtc.crawler;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.util.html.parser.XPathReader;
import hdc.util.text.StringUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.az24.crawler.model.ATM;
import com.az24.crawler.model.City;
import com.az24.dao.ProvinceDAO;
import com.az24.dao.TienIchDAO;
import com.az24.test.HttpClientFactory;
import com.az24.test.HttpClientUtil;
import com.az24.util.io.FileUtil;

public class CrawlerDailyATM {
	
	TienIchDAO tienIchDAO = new TienIchDAO();	
	
	public void crawlerDongABank(int city,int district,int region,int ti_city,int ti_district) throws Exception {
		DefaultHttpClient client = HttpClientFactory.getInstance();
		client.getParams().setParameter("application/x-www-form-urlencoded",false);
		HttpPost post = new HttpPost("http://www.dongabank.com.vn/atm/list_atm");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("city", city+""));
		list.add(new BasicNameValuePair("district",district+""));
		list.add(new BasicNameValuePair("region",region+""));
		list.add(new BasicNameValuePair("submit","Danh+s%C3%A1ch+t%C3%ACm+ki%E1%BA%BFm"));
		post.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		post.addHeader("Accept-Encoding", "gzip, deflate");
		post.addHeader("Accept-Language", "en-us,en;q=0.5");
		post.addHeader("Connection", "keep-alive");
		post.addHeader("Cookie","__utma=100876180.298132746.1349977706.1349977706.1350009330.2; __utmz=100876180.1349977706.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); ci_session=a%3A4%3A%7Bs%3A10%3A%22session_id%22%3Bs%3A32%3A%22c0ac2bc6f66287434419ee44802cf862%22%3Bs%3A10%3A%22ip_address%22%3Bs%3A12%3A%22192.168.34.2%22%3Bs%3A10%3A%22user_agent%22%3Bs%3A50%3A%22Mozilla%2F5.0+%28Windows+NT+6.1%3B+rv%3A15.0%29+Gecko%2F201001%22%3Bs%3A13%3A%22last_activity%22%3Bs%3A10%3A%221350011165%22%3B%7D33c3615c3668a87d300150377a8df0ee; __utmb=100876180.14.10.1350009330; __utmc=100876180; menu_title_3=1");
		post.addHeader("Host", "www.dongabank.com.vn");
		post.addHeader("Referer", "http://www.dongabank.com.vn/atm/list_atm");
		post.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; rv:15.0) Gecko/20100101 Firefox/15.0.1");
		post.setEntity(new UrlEncodedFormEntity(list));
		HttpResponse res = client.execute(post);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		CrawlerUtil.analysis(reader.getDocument(), "");
		String xpath__weather_td = "//table[@class='apTable']/TBODY[1]/tr";
		NodeList linkNodes = (NodeList) reader.read(xpath__weather_td,XPathConstants.NODESET);
		int i = 2;		
		TienIchDAO tienIchDAO = new TienIchDAO();		
		if (linkNodes != null) {
			while (i <= linkNodes.getLength()) {
				ATM atm = new ATM();
				atm.bank = "Dong A Bank";
				atm.province_id = ti_city;
				atm.district_id = ti_district;
				String xpath_channel = xpath__weather_td + "[" + i + "]/td[1]";
				Node position = (Node) reader.read(xpath_channel,XPathConstants.NODE);
				System.out.println(i + "  " + position.getTextContent().trim());
				atm.position = StringUtil.trim(position.getTextContent());
				
				String xpath_address = xpath__weather_td + "[" + i + "]/td[2]";
				Node address = (Node) reader.read(xpath_address,XPathConstants.NODE);
				System.out.println(i + "  " + address.getTextContent().trim());
				atm.address = StringUtil.trim(address.getTextContent());
				
				String xpath_guitien = xpath__weather_td + "[" + i + "]/td[3]";
				Node guitien = (Node) reader.read(xpath_guitien,XPathConstants.NODE);
				System.out.println(i + "  " + guitien.getTextContent().trim());
				
				String check = StringUtil.trim(guitien.getTextContent());
				if(!StringUtil.isEmpty(check))atm.has_send_money = 1; else atm.has_send_money = 0;
				atm.create_date = new Date(Calendar.getInstance().getTimeInMillis());
				tienIchDAO.saveATM(atm);
				String xpath_giohoatdong = xpath__weather_td + "[" + i+ "]/td[4]";
				Node giohoatdong = (Node) reader.read(xpath_giohoatdong,XPathConstants.NODE);
				System.out.println(i + "  "	+ giohoatdong.getTextContent().trim());
				i++;
			}
		}
		FileUtil.writeToFile("d:/kplus.html", html, false);
	}

	public void crawlerVietCombank(int province_id ,int city) throws Exception {
		DefaultHttpClient client = HttpClientFactory.getInstance();
		client.getParams().setParameter("application/x-www-form-urlencoded",false);
		HttpPost post = new HttpPost("http://www.vietcombank.com.vn/atm/Default.aspx");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
		list.add(new BasicNameValuePair("__LASTFOCUS", ""));
		list.add(new BasicNameValuePair("__EVENTTARGET","ctl00$Content$CityList"));
		list.add(new BasicNameValuePair("__VIEWSTATE",
						"/wEPDwUJNTI5OTE5NTk3D2QWAmYPZBYCAgMPZBYEAgEPZBYCAgMPZBYCAgIPFgIeC18hSXRlbUNvdW50AggWEAIBD2QWAgIBDw8WBB4EVGV4dAULVHJhbmcgY2jhu6ceC05hdmlnYXRlVXJsBQJ+L2RkAgIPZBYCAgEPDxYEHwEFCUPDoSBuaMOibh8CBQt+L1BlcnNvbmFsL2RkAgMPZBYCAgEPDxYEHwEFDkRvYW5oIG5naGnhu4dwHwIFB34vQ29ycC9kZAIED2QWAgIBDw8WBB8BBRnEkOG7i25oIGNo4bq/IHTDoGkgY2jDrW5oHwIFBX4vRkkvZGQCBQ9kFgICAQ8PFgQfAQUYTmfDom4gaMOgbmcgxJFp4buHbiB04butHwIFC34vRUJhbmtpbmcvZGQCBg9kFgICAQ8PFgQfAQUOVHV54buDbiBk4bulbmcfAgUKfi9DYXJlZXJzL2RkAgcPZBYCAgEPDxYEHwEFD05ow6AgxJHhuqd1IHTGsB8CBQx+L0ludmVzdG9ycy9kZAIID2QWAgIBDw8WBB8BBQ5HaeG7m2kgdGhp4buHdR8CBQh+L0Fib3V0L2RkAg0PZBYCAgEPZBYCAgEPFgIfAAIFFgoCAQ9kFgICAQ8PFgQfAQUbxJBp4buBdSBraG/huqNuIHPhu60gZOG7pW5nHwIFGH4vVW5kZXJDb25zdHJ1Y3Rpb24uYXNweGRkAgIPZBYCAgEPDxYEHwEFC0LhuqNvIG3huq10HwIFGH4vVW5kZXJDb25zdHJ1Y3Rpb24uYXNweGRkAgMPZBYCAgEPDxYEHwEFCkxpw6puIGjhu4cfAgUmbWFpbHRvOndlYm1hc3RlclthdF12aWV0Y29tYmFuay5jb20udm5kZAIED2QWAgIBDw8WBB8BBRFTxqEgxJHhu5Mgd2Vic2l0ZR8CBQ5+L1NpdGVtYXAuYXNweGRkAgUPZBYCAgEPDxYEHwEFFFZpZXRjb21iYW5rIFdlYiBNYWlsHwIFIGh0dHBzOi8vbWFpbC52aWV0Y29tYmFuay5jb20udm4vZGQYAgUVY3RsMDAkQ29udGVudCRBVE1WaWV3DzwrAAoBCAIBZAUqY3RsMDAkSGVhZGVyJExhbmd1YWdlU3dpdGNoZXIkTGFuZ3VhZ2VWaWV3Dw9kAgJkpVQQdl5ccG96iMzwPm04o8nrlFw="));
		list.add(new BasicNameValuePair("ctl00$Content$CityList", city+""));

		post.setEntity(new UrlEncodedFormEntity(list));
		HttpResponse res = client.execute(post);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);

		//CrawlerUtil.analysis(reader.getDocument(), "");

		String xpath__weather_td = "//table[@id='ctl00_Content_ATMView']/TBODY[1]/tr";
		NodeList linkNodes = (NodeList) reader.read(xpath__weather_td,XPathConstants.NODESET);
		int i = 2;
		TienIchDAO tienIchDAO = new TienIchDAO();		
		if (linkNodes != null) {
			while (i <= linkNodes.getLength()) {
				
				String xpath_channel = xpath__weather_td + "[" + i + "]/td[1]";
				Node position = (Node) reader.read(xpath_channel,XPathConstants.NODE);
				System.out.println(i + "  " + position.getTextContent().trim());

				String xpath_address = xpath__weather_td + "[" + i + "]/td[3]";
				Node address = (Node) reader.read(xpath_address,XPathConstants.NODE);
				System.out.println(i + "  " + address.getTextContent().trim());

				String xpath_atmnumber = xpath__weather_td + "[" + i + "]/td[2]";
				Node atmnumber = (Node) reader.read(xpath_atmnumber,XPathConstants.NODE);
				System.out.println(i + "  " + atmnumber.getTextContent().trim());

				String xpath_giohoatdong = xpath__weather_td + "[" + i	+ "]/td[4]";
				Node giohoatdong = (Node) reader.read(xpath_giohoatdong,XPathConstants.NODE);
				System.out.println(i + "  "	+ giohoatdong.getTextContent().trim());
				
				ATM atm = new ATM();
				atm.bank = "VietcomBank";
				atm.province_id = province_id;
				atm.position = StringUtil.trim(position.getTextContent());
				atm.address = StringUtil.trim(address.getTextContent());
				atm.atm_number =Integer.parseInt(StringUtil.trim(atmnumber.getTextContent()));
				atm.create_date = new Date(Calendar.getInstance().getTimeInMillis());
				tienIchDAO.saveATM(atm);
				
				i++;
			}
		}

		FileUtil.writeToFile("d:/kplus.html", html, false);
	}

	public void crawlerTechComBank(int province_id,int district_id,String city,String district) {
		org.apache.http.impl.client.DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost("https://www.techcombank.com.vn/Desktop.aspx/Ket-qua-tim-kiem-ATM/Desktop.aspx?desktop=Ket-qua-tim-kiem-ATM&catName=&Occupation="+city+"&Department="+district+"&Contact=&SFirst=0");
		String html = "";
		try {
			HttpResponse response = client.execute(post);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				html += line;
			}
			XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
			CrawlerUtil.analysis(reader.getDocument(), "");
			String xpath__weather_td = "//TABLE[@id='vie__ctl7__ctl0_Container']/TBODY[1]/TR[2]/TD[@id='vie__ctl7__ctl0_Content']/TABLE[2]/TBODY[1]/TR[1]/TD[1]/TABLE[1]/TBODY[1]/TR";
			NodeList linkNodes = (NodeList) reader.read(xpath__weather_td,XPathConstants.NODESET);
			int i = 1;
			if (linkNodes != null) {
				while (i <= linkNodes.getLength()) {					
					String xpath_channel = xpath__weather_td + "[" + i+ "]/TD[2]";
					Node position = (Node) reader.read(xpath_channel,XPathConstants.NODE);
					if (position==null||StringUtil.isEmpty(position.getTextContent())) {
						i++;continue;
					}					
					System.out.println(i + "  "	+ position.getTextContent().trim());
					String xpath_address = xpath__weather_td + "[" + i	+ "]/TD[3]";
					Node address = (Node) reader.read(xpath_address,XPathConstants.NODE);
					System.out.println(i + "  "	+ address.getTextContent().trim());
					
					ATM atm = new ATM();
					atm.bank = "TechcomBank";
					atm.province_id = province_id;
					atm.district_id = district_id;
					atm.position = StringUtil.trim(position.getTextContent());
					atm.address = StringUtil.trim(address.getTextContent());
					atm.atm_number =1;
					atm.create_date = new Date(Calendar.getInstance().getTimeInMillis());
					tienIchDAO.saveATM(atm);
					i++;
				}
			}
			FileUtil.writeToFile("d:/kplus.html", html, false);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void crawlerVietinbank() throws Exception {
		String url = "http://www.vietinbank.vn/mangluoi/getATMs.action?fromLat=10.226360999999999&toLat=10.256361&fromLng=106.36126&toLng=106.39126&districtID=0&cityID=25";
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		String xpath__tag = "//TABLE[@class='table-data']/TBODY[1]/TR";
		NodeList linkNodes = (NodeList) reader.read(xpath__tag,
				XPathConstants.NODESET);
		int node_one_many = linkNodes.getLength();
		int i = 1;
		CrawlerUtil.analysis(reader.getDocument(), "");
		while (i <= node_one_many) {
			if (i % 2 == 0) {
				i++;
				continue;
			}
			String position = (String) reader.read(xpath__tag + "[" + i	+ "]/TD[2]/text()", XPathConstants.STRING);
			System.out.println("position = " + position);
			String address = (String) reader.read(xpath__tag + "[" + i+ "]/TD[2]/A[1]/text()", XPathConstants.STRING);
			System.out.println("address" + address);
			String number = (String) reader.read(xpath__tag + "[" + i+ "]/TD[3]/text()", XPathConstants.STRING);
			System.out.println("number = " + number);
			String tel = (String) reader.read(xpath__tag + "[" + i+ "]/TD[4]/text()", XPathConstants.STRING);
			System.out.println("tel = " + tel);
			ATM atm = new ATM();
			atm.bank = "Vietinbank";
			atm.province_id = 0;
			atm.position = StringUtil.trim(address);
			atm.address = StringUtil.trim(address);
			atm.atm_number =Integer.parseInt(number);
			atm.create_date = new Date(Calendar.getInstance().getTimeInMillis());
			tienIchDAO.saveATM(atm);
			i++;

		}
	}

	public void crawlerAnbinhBank(int province_id,int district_id,String a_province,String a_district) throws Exception {
		DefaultHttpClient client = HttpClientFactory.getInstance();
		client.getParams().setParameter("application/x-www-form-urlencoded",false);
		HttpPost post = new HttpPost("http://www.abbank.vn/vi/Mang-Luoi/ATMs.aspx");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
		list.add(new BasicNameValuePair("__EVENTTARGET", "ctl00$Cnt$Cb_Pr"));
		list.add(new BasicNameValuePair("__EVENTVALIDATION",
						"WfIc7Sw6qrFnEiRhyUbMPBWo9mSEkUU4cSYReUSWigFXa3M7L9dhzi0J/fV8lCk0KDcoM2jCN9vaQ7kgB+iLsoQIamLgZ0ll4EJ+esqoM5/hmDDULPFw5gs9f9TB+5oLNA7Rly4tSosf5pPSIEG8M699swLXY1aV8170rasiSW7bjqldqRk0D6l375QbHXD+IW3EmErNFcC3Iis2v1CDEVxzVZNTxxP5jmih5QZgCnJW78ajMP7NVREepsyoh2tEQJW2L+w7f1Vp0W7mucp2Ed452LOKBkOkRKnWbh3qPQM="));
		list.add(new BasicNameValuePair("__LASTFOCUS", ""));
		list.add(new BasicNameValuePair("__VIEWSTATE",
						"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"));
		list.add(new BasicNameValuePair("__VIEWSTATEENCRYPTED", ""));
		list.add(new BasicNameValuePair("ctl00$Cnt$Cb_Dis", a_district));
		list.add(new BasicNameValuePair("ctl00$Cnt$Cb_Pr", a_province));

		post.setEntity(new UrlEncodedFormEntity(list));
		HttpResponse res = client.execute(post);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		//CrawlerUtil.analysis(reader.getDocument(), "");
		String xpath__weather_td = "//table[@id='ctl00_Cnt_Gr_List']/TBODY[1]/tr";
		NodeList linkNodes = (NodeList) reader.read(xpath__weather_td,XPathConstants.NODESET);
		int i = 2;
		if (linkNodes != null) {
			while (i <= linkNodes.getLength()) {
				String xpath_channel = xpath__weather_td + "[" + i + "]/td[1]";
				Node position = (Node) reader.read(xpath_channel,XPathConstants.NODE);
				System.out.println(i + "  " + position.getTextContent().trim());

				String xpath_address = xpath__weather_td + "[" + i + "]/td[2]";
				Node address = (Node) reader.read(xpath_address,XPathConstants.NODE);
				//System.out.println(i + "  " + address.getTextContent().trim());

				String xpath_sl = xpath__weather_td + "[" + i + "]/td[3]";
				Node sl = (Node) reader.read(xpath_sl,XPathConstants.NODE);
				//System.out.println(i + "  " + sl.getTextContent().trim());
				
				ATM atm = new ATM();
				atm.bank = "AnBinhBank";
				atm.province_id = province_id;
				atm.district_id = district_id;
				atm.position = StringUtil.trim(position.getTextContent());
				if(address!=null)
				atm.address = StringUtil.trim(address.getTextContent());
				if(sl!=null)
				atm.atm_number =Integer.parseInt(StringUtil.trim(sl.getTextContent()));
				atm.create_date = new Date(Calendar.getInstance().getTimeInMillis());
				tienIchDAO.saveATM(atm);

				i++;
			}
		}

		FileUtil.writeToFile("d:/kplus.html", html, false);
	}

	public void crawlerOceanBank(int province_id,String region,String city_id) throws Exception {
		DefaultHttpClient client = HttpClientFactory.getInstance();
		client.getParams().setParameter("application/x-www-form-urlencoded",false);
		HttpPost post = new HttpPost("http://www.oceanbank.vn/Mang-Luoi/Diem-Dat-May-ATM/Index.html");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
		list.add(new BasicNameValuePair("__EVENTTARGET", ""));
		list.add(new BasicNameValuePair("__LASTFOCUS", ""));
		list.add(new BasicNameValuePair("__VIEWSTATE","/wEPDwUKMTEzNTg0NTkyNA8WBh4JUGFnZVRpdGxlBSJPY2VhbkJhbmsgLSDEkGnhu4NtIMSR4bq3dCBBVE0vUE9THgtEZXNjcmlwdGlvbgUXxJBp4buDbSDEkeG6t3QgIEFUTS9QT1MeCEtleXdvcmRzBQEsFgJmD2QWAgIDD2QWAgIBD2QWBgUKd3A3MTAzNDY1MQ9kFgJmD2QWBGYPFgIeB1Zpc2libGVnFgICAQ8WAh4LXyFJdGVtQ291bnQCBhYMZg9kFgJmDxUCKy9OZ2FuLUhhbmctQ2EtTmhhbi9UaGUtT2NlYW5CYW5rL0luZGV4Lmh0bWwPVGjhursgT2NlYW5CYW5rZAIBD2QWAmYPFQIrL1Rpbi1UdWMvS2h1eWVuLU1haS1TYW4tUGhhbS1Nb2kvSW5kZXguaHRtbCNLaHV54bq/biBt4bqhaSAtIFPhuqNuIHBo4bqpbSBt4bubaWQCAg9kFgJmDxUCIi9UaW4tVHVjL0Jhbi1UaW4tTm9pLUJvL0luZGV4Lmh0bWwfQuG6o24gdGluIG7hu5lpIGLhu5kgT2NlYW5UaW1lc2QCAw9kFgJmDxUCKy9UdXllbi1EdW5nL05vcC1Iby1Tby1UcnVjLVR1eWVuL0luZGV4Lmh0bWwdTuG7mXAgaOG7kyBzxqEgdHLhu7FjIHR1eeG6v25kAgQPZBYCZg8VAjYvVHV5ZW4tRHVuZy9UaG9uZy1UaW4tVmEtS2V0LVF1YS1UdXllbi1EdW5nL0luZGV4Lmh0bWwpVGjDtG5nIHRpbiB2w6Aga+G6v3QgcXXhuqMgdHV54buDbiBk4bulbmdkAgUPZBYCZg8VAhUvTWFuZy1MdW9pL0luZGV4Lmh0bWwOTeG6oW5nIGzGsOG7m2lkAgMPZBYCZg8WAh8EAgoWFGYPZBYCZg8VBAEwWmh0dHA6Ly93d3cub2NlYW5iYW5rLnZuL1Rpbi1UdWMvOTI1L09jZWFuQmFuay10by1jaHVjLURhaS1ob2ktZG9uZy1jby1kb25nLWJhdC10aHVvbmcuaHRtbEZPY2VhbkJhbmsgdOG7lSBjaOG7qWMgxJDhuqFpIGjhu5lpIMSR4buTbmcgY+G7lSDEkcO0bmcgYuG6pXQgdGjGsOG7nW5nEDA2LzEwLzIwMTIgMTE6MjFkAgEPZBYCZg8VBAExdWh0dHA6Ly93d3cub2NlYW5iYW5rLnZuL1Rpbi1UdWMvOTI0L0NodXllbi10aWVuLWxpZW4tbmdhbi1oYW5nLXRvaS1zby10aGUtcXVhLWtlbmgtZ2lhby1kaWNoLUVhc3ktTS1QbHVzLUJhbmtpbmcuaHRtbFxDaHV54buDbiB0aeG7gW4gbGnDqm4gbmfDom4gaMOgbmcgdOG7m2kgc+G7kSB0aOG6uyBxdWEga8OqbmggZ2lhbyBk4buLY2ggRWFzeSBNLVBsdXMgQmFua2luZxAwNS8xMC8yMDEyIDE1OjI2ZAICD2QWAmYPFQQBMk9odHRwOi8vd3d3Lm9jZWFuYmFuay52bi9UaW4tVHVjLzkyMi9UaGFuaC10b2FuLW9ubGluZS0tLU5oYW4tbmdheS1xdWEtdGFuZy5odG1sLFRoYW5oIHRvw6FuIG9ubGluZSAtIE5o4bqtbiBuZ2F5IHF1w6AgdOG6t25nEDAzLzEwLzIwMTIgMDg6MjdkAgMPZBYCZg8VBAEzbWh0dHA6Ly93d3cub2NlYW5iYW5rLnZuL1Rpbi1UdWMvOTIxL0NodW9uZy10cmluaC1raHV5ZW4tbWFpLeKAnENodXllbi10aWVuLXF1YS10aGUtLS12dWEtcmUtdnVhLW5oYW5o4oCdLmh0bWxXQ2jGsMahbmcgdHLDrG5oIGtodXnhur9uIG3huqFpIOKAnENodXnhu4NuIHRp4buBbiBxdWEgdGjhursgLSB24burYSBy4bq7IHbhu6thIG5oYW5o4oCdEDAxLzEwLzIwMTIgMTY6MjVkAgQPZBYCZg8VBAE0XWh0dHA6Ly93d3cub2NlYW5iYW5rLnZuL1Rpbi1UdWMvOTE4L0NodW9uZy10cmluaC1raHV5ZW4tbWFpLeKAnE5oYW4tZG9pLXN1LXNhbmctdHJvbmfigJ0uaHRtbEJDaMawxqFuZyB0csOsbmgga2h1eeG6v24gbeG6oWkg4oCcTmjDom4gxJHDtGkgc+G7sSBzYW5nIHRy4buNbmfigJ0QMjcvMDkvMjAxMiAwOTowMWQCBQ9kFgJmDxUEATVaaHR0cDovL3d3dy5vY2VhbmJhbmsudm4vVGluLVR1Yy85MTcvVGhvbmctYmFvLW1vaS1ob3AtRGFpLWhvaS1kb25nLUNvLWRvbmctYmF0LXRodW9uZy5odG1s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"));
		list.add(new BasicNameValuePair("ctl00$webPartManager$wp785291999$wp2121929887$DropDownListChiNhanh", region));
		list.add(new BasicNameValuePair("ctl00$webPartManager$wp785291999$wp2121929887$DropDownListCity", city_id));
		list.add(new BasicNameValuePair("ctl00$webPartManager$wp78...","Tìm kiếm"));
		post.setEntity(new UrlEncodedFormEntity(list));
		HttpResponse res = client.execute(post);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		CrawlerUtil.analysis(reader.getDocument(), "");
		String xpath__weather_td = "//div[@class='MangLuoi']/div[1]/div[1]/div[3]/table[@class='listDsAtm']/TBODY[1]/tr";
		NodeList linkNodes = (NodeList) reader.read(xpath__weather_td,XPathConstants.NODESET);
		int i = 2;
		if (linkNodes != null) {
			while (i <= linkNodes.getLength()) {
				String xpath_channel = xpath__weather_td + "[" + i + "]/td[2]";
				Node position = (Node) reader.read(xpath_channel,XPathConstants.NODE);
				if(position==null)continue;
				System.out.println(i + "  " + position.getTextContent().trim());
				String xpath_address = xpath__weather_td + "[" + i + "]/td[2]";
				Node address = (Node) reader.read(xpath_address,XPathConstants.NODE);
				System.out.println(i + "  " + address.getTextContent().trim());
				ATM atm = new ATM();
				atm.bank = "Oceanbank";
				atm.province_id = province_id;
				atm.district_id =0;
				atm.position = StringUtil.trim(position.getTextContent());
				atm.address = StringUtil.trim(address.getTextContent());
				atm.create_date = new Date(Calendar.getInstance().getTimeInMillis());
				tienIchDAO.saveATM(atm);
				i++;
			}
		}

		FileUtil.writeToFile("d:/kplus.html", html, false);
	}

	public void crawlerPhuongDongBank(int province_id,int district_id,String url) throws Exception {
		DefaultHttpClient client = HttpClientFactory.getInstance();
		client.getParams().setParameter("application/x-www-form-urlencoded",false);
		HttpGet get = new HttpGet("http://www.ocb.com.vn/"+url);
		HttpResponse res = client.execute(get);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		CrawlerUtil.analysis(reader.getDocument(), "");
		String xpath__weather_td = "HTML/BODY[1]/TABLE[1]/TBODY[1]/TR[7]/TD[1]/TABLE[1]/TBODY[1]/TR[1]/TD[3]/TABLE[1]/TBODY[1]/TR[2]/TD[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/TABLE[1]/TBODY[1]/TR[1]/TD[1]/TABLE[1]/TBODY[1]/TR";
		NodeList linkNodes = (NodeList) reader.read(xpath__weather_td,
				XPathConstants.NODESET);
		int i = 4;
		if (linkNodes != null) {
			while (i <= linkNodes.getLength()) {
				String xpath_channel = xpath__weather_td + "[" + i + "]/TD[2]";
				Node position = (Node) reader.read(xpath_channel,XPathConstants.NODE);
				System.out.println(i + "  " + position.getTextContent().trim());
				String xpath_number = xpath__weather_td + "[" + i + "]/TD[3]";
				Node number = (Node) reader.read(xpath_number,	XPathConstants.NODE);
				System.out.println(i + "  " + number.getTextContent().trim());
				String xpath_address = xpath__weather_td + "[" + i + "]/TD[4]";
				Node address = (Node) reader.read(xpath_address,XPathConstants.NODE);
				System.out.println(i + "  " + address.getTextContent().trim());
				String xpath_time = xpath__weather_td + "[" + i + "]/TD[5]";
				Node time = (Node) reader.read(xpath_time, XPathConstants.NODE);
				System.out.println(i + "  " + time.getTextContent().trim());
				
				ATM atm = new ATM();
				atm.bank = "Phuong Dong Bank";
				atm.province_id = province_id;
				atm.district_id = district_id;
				atm.position = StringUtil.trim(position.getTextContent());
				atm.address = StringUtil.trim(address.getTextContent());
				atm.atm_number = Integer.parseInt(StringUtil.trim(number.getTextContent()));
				atm.create_date = new Date(Calendar.getInstance().getTimeInMillis());
				tienIchDAO.saveATM(atm);
				
				i++;
			}
		}

		FileUtil.writeToFile("d:/kplus.html", html, false);
	}

	public void crawlerMBBank(int province_id,int district_id,String province_value) throws Exception {
		DefaultHttpClient client = HttpClientFactory.getInstance();
		client.getParams().setParameter("application/x-www-form-urlencoded",false);
		HttpGet get = new HttpGet("http://www.mbbank.com.vn/mangluoi/Lists/ATM/AllItems.aspx"+province_value);
		HttpResponse res = client.execute(get);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		CrawlerUtil.analysis(reader.getDocument(), "");
		String xpath__weather_td = "//DIV[@id='ctl00_m_g_cade8dae_f5dc_4417_b551_c04726effc7a']/TABLE[1]/TBODY[1]/TR";
		NodeList linkNodes = (NodeList) reader.read(xpath__weather_td,XPathConstants.NODESET);
		int i = 4;
		if (linkNodes != null) {
			while (i <= linkNodes.getLength()) {
				String xpath_channel = xpath__weather_td + "[" + i + "]/TD[1]";
				Node channel = (Node) reader.read(xpath_channel,XPathConstants.NODE);
				System.out.println(i + "  " + channel.getTextContent().trim());

				String xpath_number = xpath__weather_td + "[" + i + "]/TD[2]";
				Node number = (Node) reader.read(xpath_number,XPathConstants.NODE);
				System.out.println(i + "  " + number.getTextContent().trim());

				String xpath_address = xpath__weather_td + "[" + i + "]/TD[4]";
				Node address = (Node) reader.read(xpath_address,XPathConstants.NODE);
				System.out.println(i + "  " + address.getTextContent().trim());

				String xpath_position = xpath__weather_td + "[" + i + "]/TD[3]";
				Node position = (Node) reader.read(xpath_position, XPathConstants.NODE);
				System.out.println(i + "  " + position.getTextContent().trim());
				
				ATM atm = new ATM();
				atm.bank = "MBank";
				atm.province_id = province_id;
				atm.district_id = district_id;
				atm.position = StringUtil.trim(position.getTextContent());
				atm.address = StringUtil.trim(address.getTextContent());
				atm.atm_number = 1;
				atm.create_date = new Date(Calendar.getInstance().getTimeInMillis());
				tienIchDAO.saveATM(atm);
				
				i++;
			}
		}

		FileUtil.writeToFile("d:/kplus.html", html, false);
	}
	
	public void crawlerMariTimeBankHaNoi(int province_id) throws Exception {
		DefaultHttpClient client = HttpClientFactory.getInstance();
		client.getParams().setParameter("application/x-www-form-urlencoded",false);
		HttpGet get = new HttpGet("http://www.msb.com.vn/home/diadiematm-hanoi-map");
		HttpResponse res = client.execute(get);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		//CrawlerUtil.analysis(reader.getDocument(), "");
		String xpath__weather_td = "//table[@id='table1']/TBODY[1]/tr";
		NodeList linkNodes = (NodeList) reader.read(xpath__weather_td,XPathConstants.NODESET);
		int i = 2;
		ProvinceDAO provinceDAO = new ProvinceDAO();
		if (linkNodes != null) {
			City city = null;
			while (i <= linkNodes.getLength()) {				
				String xpath_channel_city = xpath__weather_td + "[" + i + "]/td[1]/@colspan";
				String district = (String) reader.read(xpath_channel_city,XPathConstants.STRING);
				if(!StringUtil.isEmpty(district)) {
					xpath_channel_city = xpath__weather_td + "[" + i + "]/td[1]";
					Node city_name_node = (Node) reader.read(xpath_channel_city,XPathConstants.NODE);					
					String city_name = StringUtil.trim(city_name_node.getTextContent());
					System.out.println("-----"+city_name);
					city_name=city_name.replaceFirst("KHU VỰC", "");
					city_name=city_name.replaceFirst("HUYỆN", "");
					city_name=city_name.replaceFirst("QUẬN", "");
					city=provinceDAO.getProvinceByName(city_name,142);
					i++;continue;
				}				
				String xpath_channel = xpath__weather_td + "[" + i + "]/td[1]";
				Node position = (Node) reader.read(xpath_channel,XPathConstants.NODE);
				if(position==null) break;
				System.out.println(i + "  " + StringUtil.trim(position.getTextContent()));
				
				ATM atm = new ATM();
				atm.bank = "MaritimeBank";
				atm.province_id = province_id;
				if(city!=null)
				atm.district_id = city.id;
				atm.position = StringUtil.trim(position.getTextContent());
				atm.address = StringUtil.trim(position.getTextContent());
				atm.atm_number = 1;
				atm.create_date = new Date(Calendar.getInstance().getTimeInMillis());
				tienIchDAO.saveATM(atm);

				i++;
			}
		}

		FileUtil.writeToFile("d:/kplus.html", html, false);
	}
	
	
	public void crawlerMariTimeBankHCM(int province_id) throws Exception {
		DefaultHttpClient client = HttpClientFactory.getInstance();
		client.getParams().setParameter("application/x-www-form-urlencoded",false);
		HttpGet get = new HttpGet("http://www.msb.com.vn/home/diadiematm-hcm-map");
		HttpResponse res = client.execute(get);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		//CrawlerUtil.analysis(reader.getDocument(), "");
		String xpath__weather_td = "//table[@id='table1']/TBODY[1]/tr";
		NodeList linkNodes = (NodeList) reader.read(xpath__weather_td,XPathConstants.NODESET);
		int i = 2;
		ProvinceDAO provinceDAO = new ProvinceDAO();
		if (linkNodes != null) {
			City city = null;
			while (i <= linkNodes.getLength()) {				
				String xpath_channel_city = xpath__weather_td + "[" + i + "]/td[1]/@colspan";
				String district = (String) reader.read(xpath_channel_city,XPathConstants.STRING);
				if(!StringUtil.isEmpty(district)) {
					xpath_channel_city = xpath__weather_td + "[" + i + "]/td[1]";
					Node city_name_node = (Node) reader.read(xpath_channel_city,XPathConstants.NODE);					
					String city_name = StringUtil.trim(city_name_node.getTextContent());
					System.out.println("-----"+city_name);
					city_name=city_name.replaceFirst("KHU VỰC", "");
					city_name=city_name.replaceFirst("HUYỆN", "");
					city_name=city_name.replaceFirst("QUẬN", "");
					city=provinceDAO.getProvinceByName(city_name,118);
					i++;continue;
				}				
				String xpath_channel = xpath__weather_td + "[" + i + "]/td[1]";
				Node position = (Node) reader.read(xpath_channel,XPathConstants.NODE);
				if(position==null) break;
				System.out.println(i + "  " + StringUtil.trim(position.getTextContent()));
				
				ATM atm = new ATM();
				atm.bank = "MaritimeBank";
				atm.province_id = province_id;
				if(city!=null)
				atm.district_id = city.id;
				atm.position = StringUtil.trim(position.getTextContent());
				atm.address = StringUtil.trim(position.getTextContent());
				atm.atm_number = 1;
				atm.create_date = new Date(Calendar.getInstance().getTimeInMillis());
				tienIchDAO.saveATM(atm);

				i++;
			}
		}

		FileUtil.writeToFile("d:/kplus.html", html, false);
	}
	
	
	public void crawlerMariTimeBankMienBac() throws Exception {
		DefaultHttpClient client = HttpClientFactory.getInstance();
		client.getParams().setParameter("application/x-www-form-urlencoded",false);
		HttpGet get = new HttpGet("http://www.msb.com.vn/home/b1-san-pham-dich-vu/a-khcn/c-san-pham-the/the-noi-111ia-1/diadiem/atm-mienbac#hanam");
		HttpResponse res = client.execute(get);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		CrawlerUtil.analysis(reader.getDocument(), "");
		String xpath__weather_td = "//table[@id='table1']/TBODY[1]/tr";
		NodeList linkNodes = (NodeList) reader.read(xpath__weather_td,XPathConstants.NODESET);
		int i = 2;
		ProvinceDAO provinceDAO = new ProvinceDAO();
		if (linkNodes != null) {
			City city = null;
			System.out.println("linkNodes.getLength()"+linkNodes.getLength());
			while (i <= 100) {				
				String xpath_channel_city = "//table[@id='table1']/tbody[1]/tr["+i+"]/td[1]/@colspan";
				String district = (String) reader.read(xpath_channel_city,XPathConstants.STRING);
				if(!StringUtil.isEmpty(district)) {
					xpath_channel_city = "//table[@id='table1']/tbody[1]/tr" + "[" + i + "]/td[1]";
					Node city_name_node = (Node) reader.read(xpath_channel_city,XPathConstants.NODE);					
					String city_name = StringUtil.trim(city_name_node.getTextContent());
					System.out.println("-----"+city_name);
					city_name=city_name.replaceFirst("KHU VỰC", "");
					city_name=city_name.replaceFirst("HUYỆN", "");
					city_name=city_name.replaceFirst("QUẬN", "");
					city=provinceDAO.getProvinceByName(StringUtil.trim(city_name.trim()));
					i++;continue;
				}				
				String xpath_channel = "//table[@id='table1']/tbody[1]/tr" + "[" + i + "]/td[1]";
				Node position = (Node) reader.read(xpath_channel,XPathConstants.NODE);
				if(position==null){i++; continue;}
				System.out.println(i + "  " + StringUtil.trim(position.getTextContent()));
				
				ATM atm = new ATM();
				atm.bank = "MaritimeBank";
				if(city!=null)
				atm.province_id = city.id;				
				atm.district_id = 0;
				atm.position = StringUtil.trim(position.getTextContent());
				atm.address = StringUtil.trim(position.getTextContent());
				atm.atm_number = 1;
				atm.create_date = new Date(Calendar.getInstance().getTimeInMillis());
				tienIchDAO.saveATM(atm);

				i++;
			}
		}

		FileUtil.writeToFile("d:/kplus.html", html, false);
	}
	
	
	public void crawlerMariTimeBankTrung() throws Exception {
		DefaultHttpClient client = HttpClientFactory.getInstance();
		client.getParams().setParameter("application/x-www-form-urlencoded",false);
		HttpGet get = new HttpGet("http://www.msb.com.vn/home/b1-san-pham-dich-vu/a-khcn/c-san-pham-the/the-noi-111ia-1/diadiem/atm-mientrung#quangbinh");
		HttpResponse res = client.execute(get);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		CrawlerUtil.analysis(reader.getDocument(), "");
		String xpath__weather_td = "//table[@id='table1']/TBODY[1]/tr";
		NodeList linkNodes = (NodeList) reader.read(xpath__weather_td,XPathConstants.NODESET);
		int i = 2;
		ProvinceDAO provinceDAO = new ProvinceDAO();
		if (linkNodes != null) {
			City city = null;
			System.out.println("linkNodes.getLength()"+linkNodes.getLength());
			while (i <= 100) {				
				String xpath_channel_city = "//table[@id='table1']/tbody[1]/tr["+i+"]/td[1]/@colspan";
				String district = (String) reader.read(xpath_channel_city,XPathConstants.STRING);
				if(!StringUtil.isEmpty(district)) {
					xpath_channel_city = "//table[@id='table1']/tbody[1]/tr" + "[" + i + "]/td[1]";
					Node city_name_node = (Node) reader.read(xpath_channel_city,XPathConstants.NODE);					
					String city_name = StringUtil.trim(city_name_node.getTextContent());
					System.out.println("-----"+city_name);
					city_name=city_name.replaceFirst("KHU VỰC", "");
					city_name=city_name.replaceFirst("HUYỆN", "");
					city_name=city_name.replaceFirst("QUẬN", "");
					city=provinceDAO.getProvinceByName(StringUtil.trim(city_name.trim()));
					i++;continue;
				}				
				String xpath_channel = "//table[@id='table1']/tbody[1]/tr" + "[" + i + "]/td[1]";
				Node position = (Node) reader.read(xpath_channel,XPathConstants.NODE);
				if(position==null){i++; continue;}
				System.out.println(i + "  " + StringUtil.trim(position.getTextContent()));
				
				ATM atm = new ATM();
				atm.bank = "MaritimeBank";
				if(city!=null)
				atm.province_id = city.id;				
				atm.district_id = 0;
				atm.position = StringUtil.trim(position.getTextContent());
				atm.address = StringUtil.trim(position.getTextContent());
				atm.atm_number = 1;
				atm.create_date = new Date(Calendar.getInstance().getTimeInMillis());
				tienIchDAO.saveATM(atm);

				i++;
			}
		}

		FileUtil.writeToFile("d:/kplus.html", html, false);
	}
	
	
	public void crawlerMariTimeBankNam() throws Exception {
		DefaultHttpClient client = HttpClientFactory.getInstance();
		client.getParams().setParameter("application/x-www-form-urlencoded",false);
		HttpGet get = new HttpGet("http://www.msb.com.vn/home/b1-san-pham-dich-vu/a-khcn/c-san-pham-the/the-noi-111ia-1/diadiem/atm-miennam#vungtau");
		HttpResponse res = client.execute(get);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		CrawlerUtil.analysis(reader.getDocument(), "");
		String xpath__weather_td = "//table[@id='table1']/TBODY[1]/tr";
		NodeList linkNodes = (NodeList) reader.read(xpath__weather_td,XPathConstants.NODESET);
		int i = 2;
		ProvinceDAO provinceDAO = new ProvinceDAO();
		if (linkNodes != null) {
			City city = null;
			System.out.println("linkNodes.getLength()"+linkNodes.getLength());
			while (i <= 100) {				
				String xpath_channel_city = "//table[@id='table1']/tbody[1]/tr["+i+"]/td[1]/@colspan";
				String district = (String) reader.read(xpath_channel_city,XPathConstants.STRING);
				if(!StringUtil.isEmpty(district)) {
					xpath_channel_city = "//table[@id='table1']/tbody[1]/tr" + "[" + i + "]/td[1]";
					Node city_name_node = (Node) reader.read(xpath_channel_city,XPathConstants.NODE);					
					String city_name = StringUtil.trim(city_name_node.getTextContent());
					System.out.println("-----"+city_name);
					city_name=city_name.replaceFirst("KHU VỰC", "");
					city_name=city_name.replaceFirst("HUYỆN", "");
					city_name=city_name.replaceFirst("QUẬN", "");
					city=provinceDAO.getProvinceByName(StringUtil.trim(city_name.trim()));
					i++;continue;
				}				
				String xpath_channel = "//table[@id='table1']/tbody[1]/tr" + "[" + i + "]/td[1]";
				Node position = (Node) reader.read(xpath_channel,XPathConstants.NODE);
				if(position==null){i++; continue;}
				System.out.println(i + "  " + StringUtil.trim(position.getTextContent()));
				
				ATM atm = new ATM();
				atm.bank = "MaritimeBank";
				if(city!=null)
				atm.province_id = city.id;				
				atm.district_id = 0;
				atm.position = StringUtil.trim(position.getTextContent());
				atm.address = StringUtil.trim(position.getTextContent());
				atm.atm_number = 1;
				atm.create_date = new Date(Calendar.getInstance().getTimeInMillis());
				tienIchDAO.saveATM(atm);

				i++;
			}
		}

		FileUtil.writeToFile("d:/kplus.html", html, false);
	}
	
	public void crawlerAgriBank(int province_id,int district_id,String province,String district) throws Exception {
		DefaultHttpClient client = HttpClientFactory.getInstance();
		client.getParams().setParameter("application/x-www-form-urlencoded",false);
		System.out.println("http://www.agribank.com.vn/tim-kiem/atm/1147/"+province+"/"+district+"/ket-qua.aspx");
		HttpGet get = new HttpGet("http://www.agribank.com.vn/tim-kiem/atm/1147/"+province+"/"+district+"/ket-qua.aspx");
		HttpResponse res = client.execute(get);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		//CrawlerUtil.analysis(reader.getDocument(), "");
		String xpath__weather_td = "//div[@id='ATM']/div";
		NodeList linkNodes = (NodeList) reader.read(xpath__weather_td,XPathConstants.NODESET);
		int i = 1;
		if (linkNodes != null) {
			while (i <= linkNodes.getLength()) {
				String xpath_channel_city = xpath__weather_td + "[" + i + "]/table[1]/TBODY[1]/tr[1]/th[1]";
				String city = (String) reader.read(xpath_channel_city,XPathConstants.STRING);
				if(!StringUtil.isEmpty(city)) {
					System.out.println(city);
					String xpath_node = xpath__weather_td + "[" + i + "]/table[1]/TBODY[1]/tr";
					NodeList nodeList = (NodeList) reader.read(xpath_node,XPathConstants.NODESET);
					int j = 2;
					while (j <= nodeList.getLength()) {
						String xpath_address = xpath_node + "[" + j + "]/td[1]";
						Node address = (Node) reader.read(xpath_address,XPathConstants.NODE);
						System.out.println(i + "  " + address.getTextContent().trim());
						String xpath_somay = xpath_node + "[" + j + "]/td[3]/text()";
						String somay = (String) reader.read(xpath_somay,XPathConstants.STRING);
						System.out.println(i + " sm= " + somay.trim());
						String xpath_time = xpath_node + "[" + j + "]/td[4]";
						Node time = (Node) reader.read(xpath_time,XPathConstants.NODE);
						System.out.println(i + "  " + time.getTextContent().trim());
						
						ATM atm = new ATM();
						atm.bank = "Agribank";
						atm.province_id = province_id;
						atm.district_id = district_id;
						atm.position = StringUtil.trim(address.getTextContent());
						atm.address = StringUtil.trim(address.getTextContent());
						atm.atm_number = Integer.parseInt(StringUtil.parseNumber(somay));
						atm.create_date = new Date(Calendar.getInstance().getTimeInMillis());
						tienIchDAO.saveATM(atm);
						
						j++;
					}
				}				
				i++;
			}
		}

		FileUtil.writeToFile("d:/kplus.html", html, false);
	}
	
	
	public void crawlerBIDV(int province_id,int district_id,String province,String district) throws Exception {
		DefaultHttpClient client = HttpClientFactory.getInstance();
		client.getParams().setParameter("application/x-www-form-urlencoded",true);
		HttpPost post = new HttpPost("http://bidv.com.vn/chinhanh/ATM.aspx");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("__ASYNCPOST", "true"));
		list.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
		list.add(new BasicNameValuePair("__EVENTTARGET", "plcRoot$Layout$zoneMenu$PagePlaceholder$PagePlaceholder$Layout$zoneContent$pageplaceholder$pageplaceholder$Layout$zoneContent$DSATM$ddlTinh"));
		list.add(new BasicNameValuePair("__LASTFOCUS", ""));
		list.add(new BasicNameValuePair("__SCROLLPOSITIONX","0"));
		list.add(new BasicNameValuePair("__SCROLLPOSITIONY", "0"));
		list.add(new BasicNameValuePair("__VIEWSTATE", "/wEPDwUKMjExOTgxNDYyMA9kFgICARBkZBYEAgMPZBYCZg9kFgJmD2QWCAIDD2QWAmYPZBYCZg9kFgICBQ8QZGQWAWZkAgUPZBYCZg9kFgYCAQ8WAh4EVGV4dAWMATxsaT48YSByZWw9InN1YiIgaHJlZj0ifi9kZWZhdWx0LmFzcHgiIHN0eWxlPSIiIGNsYXNzPSIiPjxzcGFuIGNsYXNzPSJpdGVtX3JpZ2h0Ij48c3BhbiBjbGFzcz0iaXRlbV9sZWZ0Ij5UcmFuZyBjaOG7pzwvc3Bhbj48L3NwYW4+PC9hPjwvbGk+ZAIDDxYCHgtfIUl0ZW1Db3VudAIGFgxmD2QWAmYPFQMDMzA4EH4vR2lvaXRoaWV1LmFzcHgOR2nhu5tpIHRoaeG7h3VkAgEPZBYCZg8VAwUyMzE4MhF+L05oYS1kYXUtdHUuYXNweA9OaMOgIMSR4bqndSB0xrBkAgIPZBYCZg8VAwMzMTcUfi9TYW5waGFtZGljaHZ1LmFzcHgaU+G6o24gcGjhuqltIC0gROG7i2NoIHbhu6VkAgMPZBYCZg8VAwMzMDkWfi9UaW4tdHVjLXN1LWtpZW4uYXNweBdUaW4gdOG7qWMgLSBT4buxIGtp4buHbmQCBA9kFgJmDxUDAzMxMA9+L2NoaW5oYW5oLmFzcHgOTeG6oW5nIGzGsOG7m2lkAgUPZBYCZg8VAwQxNjA2En4vTmdoZS1uZ2hpZXAuYXNweA5UdXnhu4NuIGThu6VuZ2QCBQ8WAh8ABYkOPGRpdiBzdHlsZT0iZGlzcGxheTogbm9uZTsiIGNsYXNzPSJ0YWJjb250ZW50IiBpZD0iMzA4Ij48YSBocmVmPSIvR2lvaXRoaWV1L0dpb2ktdGhpZXUtY2h1bmcuYXNweCI+R2nhu5tpIHRoaeG7h3UgY2h1bmc8L2E+PGEgaHJlZj0iL0dpb2l0aGlldS9MaWNoLXN1LXBoLS0yMjU7dC10cmllbi5hc3B4Ij5M4buLY2ggc+G7rSBwaCYjMjI1O3QgdHJp4buDbjwvYT48L2Rpdj48ZGl2IHN0eWxlPSJkaXNwbGF5OiBub25lOyIgY2xhc3M9InRhYmNvbnRlbnQiIGlkPSIyMzE4MiI+PGEgaHJlZj0ifi9OaGEtZGF1LXR1L1RvbmctcXVhbi12ZS1CSURWLmFzcHgiPlThu5VuZyBxdWFuIHbhu4EgQklEVjwvYT48YSBocmVmPSJ+L05oYS1kYXUtdHUvQmFvLWNhby10YWktY2hpbmguYXNweCI+QsOhbyBjw6FvIHTDoGkgY2jDrW5oPC9hPjxhIGhyZWY9In4vTmhhLWRhdS10dS9EaWV1LWxlLXZhLXF1YW4tdHJpLW5nYW4taGFuZy5hc3B4Ij7EkGnhu4F1IGzhu4cgdsOgIHF14bqjbiB0cuG7iyBuZ8OibiBow6BuZzwvYT48YSBocmVmPSJ+L05oYS1kYXUtdHUvVGhvbmctdGluLWRhbmgtY2hvLW5oYS1kYXUtdHUuYXNweCI+VGjDtG5nIHRpbiBkw6BuaCBjaG8gbmjDoCDEkeG6p3UgdMawPC9hPjxhIGhyZWY9In4vTmhhLWRhdS10dS9Ib2ktZGFwLWxpZW4taGUuYXNweCI+SOG7j2kgxJHDoXAmbGnDqm4gaOG7hzwvYT48L2Rpdj48ZGl2IHN0eWxlPSJkaXNwbGF5OiBub25lOyIgY2xhc3M9InRhYmNvbnRlbnQiIGlkPSIzMTciPjxhIGhyZWY9In4vU2FucGhhbWRpY2h2dS9raGFjaGhhbmdjYW5oYW4uYXNweCI+S2jDoWNoIGjDoG5nIGPDoSBuaMOibjwvYT48YSBocmVmPSJ+L1NhbnBoYW1kaWNodnUvS2hhY2hoYW5nZG9hbmhuZ2hpZXAuYXNweCI+S2jDoWNoIGjDoG5nIGRvYW5oIG5naGnhu4dwPC9hPjxhIGhyZWY9In4vU2FucGhhbWRpY2h2dS9EaW5oLWNoZS10YWktY2hpbmguYXNweCI+xJDhu4tuaCBjaOG6vyB0w6BpIGNow61uaDwvYT48L2Rpdj48ZGl2IHN0eWxlPSJkaXNwbGF5OiBub25lOyIgY2xhc3M9InRhYmNvbnRlbnQiIGlkPSIzMDkiPjxhIGhyZWY9In4vVGluLXR1Yy1zdS1raWVuL1Rpbi1CSURWLmFzcHgiPlRpbiBCSURWPC9hPjxhIGhyZWY9In4vVGluLXR1Yy1zdS1raWVuL1Rob25nLXRpbi1iYW8tY2hpLmFzcHgiPlRow7RuZyBjw6FvIGLDoW8gY2jDrTwvYT48YSBocmVmPSJ+L1Rpbi10dWMtc3Uta2llbi9UaG9uZy10aW4tdGFpLWNo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YW4taGFuZy1iYW4tbGUvQmlldS1waGkvUGhpLWRpY2gtdnUtbmdhbi1xdXkuYXNweChQaCYjMjM3OyBk4buLY2ggduG7pSBuZyYjMjI2O24gcXXhu7kgVk5EZAIFD2QWAmYPFQI7L05nYW4taGFuZy1iYW4tbGUvQmlldS1waGkvUGhpLWdpYW8tZGljaC10YWkta2hvYW4tVVNELmFzcHgyUGgmIzIzNzsgROG7i2NoIHbhu6UgdCYjMjI0O2kga2hv4bqjbiBuZ2/huqFpIHThu4dkAgYPZBYCZg8VAjMvTmdhbi1oYW5nLWJhbi1sZS9CaWV1LXBoaS9QaGktY2h1eWVuLXRpZW4tVVNELmFzcHglUGgmIzIzNzsgY2h1eeG7g24gdGnhu4FuIG5nb+G6oWkgdOG7h2QCBw9kFgJmDxUCMC9OZ2FuLWhhbmctYmFuLWxlL0JpZXUtcGhpL1BoaS1iYW8tbGFuaC1VU0QuYXNweCVQaCYjMjM3OyBi4bqjbyBsJiMyMjc7bmggbmdv4bqhaSB04buHZAIID2QWAmYPFQI4L05nYW4taGFuZy1iYW4tbGUvQmlldS1waGkvUGhpLWRpY2gtdnUtbmdhbi1xdXktVVNELmFzcHgxUGgmIzIzNzsgZOG7i2NoIHbhu6UgbmcmIzIyNjtuIHF14bu5IG5nb+G6oWkgdOG7h2QCCQ9kFgJmDxUCQS9OZ2FuLWhhbmctYmFuLWxlL0JpZXUtcGhpL0JpZXUtcGgtLTIzNzstRGljaC12dS1CSURWLU1vYmlsZS5hc3B4J0Jp4buDdSBwaCYjMjM3OyBE4buLY2ggduG7pSBCSURWIE1vYmlsZWQCCg9kFgJmDxUCUi9OZ2FuLWhhbmctYmFuLWxlL0JpZXUtcGhpL0JpZXUtcGgtLTIzNzstRGljaC12dS1CSURWLUJ1c2luZXNzLU9ubGluZS1jaG8tS2gtLmFzcHhYQmnhu4N1IHBoJiMyMzc7IEThu4tjaCB24bulIEJJRFYgQnVzaW5lc3MgT25saW5lIGNobyBLaCYjMjI1O2NoIGgmIzIyNDtuZyBEb2FuaCBuZ2hp4buHcGQCAQ8WAh8BAhIWJAIBD2QWBgIBDxYCHwAFFFVTRCAgICAgICAgICAgICAgICAgZAIDDxYCHwAFCTIwLjg0MCwwMGQCBQ8WAh8ABQkyMC44ODAsMDBkAgIPZBYGAgEPFgIfAAUWVVNEIGzhursgICAgICAgICAgICAgIGQCAw8WAh8ABQkyMC44MzAsMDBkAgUPFgIfAAUBLWQCAw9kFgYCAQ8WAh8ABRRFVVIgICAgICAgICAgICAgICAgIGQCAw8WAh8ABQkyNi42ODYsMDBkAgUPFgIfAAUJMjcuMDUxLDAwZAIED2QWBgIBDxYCHwAFFEdCUCAgICAgICAgICAgICAgICAgZAIDDxYCHwAFCTMzLjAxNywwMGQCBQ8WAh8ABQkzMy42MzgsMDBkAgUPZBYGAgEPFgIfAAUUSEtEICAgICAgICAgICAgICAgICBkAgMPFgIfAAUIMi42NDgsMDBkAgUPFgIfAAUIMi43MTUsMDBkAgYPZBYGAgEPFgIfAAUUQ0hGICAgICAgICAgICAgICAgICBkAgMPFgIfAAUJMjEuOTU2LDAwZAIFDxYCHwAFCTIyLjQzOCwwMGQCBw9kFgYCAQ8WAh8ABRRKUFkgICAgICAgICAgICAgICAgIGQCAw8WAh8ABQYyNjEsNjJkAgUPFgIfAAUGMjY3LDc3ZAIID2QWBgIBDxYCHwAFFEFVRCAgICAgICAgICAgICAgICAgZAIDDxYCHwAFCTIxLjAwMiwwMGQCBQ8WAh8ABQkyMS40NTMsMDBkAgkPZBYGAgEPFgIfAAUUQ0FEICAgICAgICAgICAgICAgICBkAgMPFgIfAAUJMjAuOTA0LDAwZAIFDxYCHwAFCTIxLjQxOSwwMGQCCg9kFgYCAQ8WAh8ABRRTR0QgICAgICAgICAgICAgICAgIGQCAw8WAh8ABQkxNi43OTgsMDBkAgUPFgIfAAUJMTcuMTkwLDAwZAILD2QWBgIBDxYCHwAFFFNFSyAgICAgICAgICAgICAgICAgZAIDDxYCHwAFAS1kAgUPFgIfAAUIMy4xNDAsMDBkAgwPZBYGAgEPFgIfAAUUTEFLICAgICAgICAgICAgICAgICBkAgMPFgIfAAUBLWQCBQ8WAh8ABQUwMiw4MGQCDQ9kFgYCAQ8WAh8ABRRES0sgICAgICAgICAgICAgICAgIGQCAw8WAh8ABQEtZAIFDxYCHwAFCDMuNjQ5LDAwZAIOD2QWBgIBDxYCHwAFFE5PSyAgICAgICAgICAgICAgICAgZAIDDxYCHwAFAS1kAgUPFgIfAAUIMy42ODMsMDBkAg8PZBYGAgEPFgIfAAUUQ05ZICAgICAgICAgICAgICAgICBkAgMPFgIfAAUBLWQCBQ8WAh8ABQgzLjM2NSwwMGQCEA9kFgYCAQ8WAh8ABRRUSEIgICAgICAgICAgICAgICAgIGQCAw8WAh8ABQEtZAIFDxYCHwAFBjcwNCwxMWQCEQ9kFgYCAQ8WAh8ABRVWTsSQICAgICAgICAgICAgICAgICBkAgMPFgIfAAUBLWQCBQ8WAh8ABQEtZAISD2QWBgIBDxYCHwAFFFJVQiAgICAgICAgICAgICAgICAgZAIDDxYCHwAFAS1kAgUPFgIfAAUGNzQ2LDAwZAICDxYCHwAFDFRQIEjDoCBO4buZaWQCAw8WAh8BAgIWBAIBD2QWAgIBDxYCHwAFA1VTRGQCAg9kFgICAQ8WAh8ABQRWTsSQZAIEDxYCHwECChYUZg9kFgQCAQ8WAh8ABQNLS0hkAgMPFgIfAQICFgQCAQ9kFgICAQ8WAh8ABQQwLDIlZAICD2QWAgIBDxYCHwAFAjIlZAIBD2QWBAIBDxYCHwAFCDEgdGjDoW5nZAIDDxYCHwECAhYEAgEPZBYCAgEPFgIfAAUCMiVkAgIPZBYCAgEPFgIfAAUCOSVkAgIPZBYEAgEPFgIfAAUIMiB0aMOhbmdkAgMPFgIfAQICFgQCAQ9kFgICAQ8WAh8ABQIyJWQCAg9kFgICAQ8WAh8ABQI5JWQCAw9kFgQCAQ8WAh8ABQgzIHRow6FuZ2QCAw8WAh8BAgIWBAIBD2QWAgIBDxYCHwAFAjIlZAICD2QWAgIBDxYCHwAFAjklZAIED2QWBAIBDxYCHwAFCDYgdGjDoW5nZAIDDxYCHwECAhYEAgEPZBYCAgEPFgIfAAUCMiVkAgIPZBYCAgEPFgIfAAUCOSVkAgUPZBYEAgEPFgIfAAUIOSB0aMOhbmdkAgMPFgIfAQICFgQCAQ9kFgICAQ8WAh8ABQIyJWQCAg9kFgICAQ8WAh8ABQI5JWQCBg9kFgQCAQ8WAh8ABQkxMiB0aMOhbmdkAgMPFgIfAQICFgQCAQ9kFgICAQ8WAh8ABQIyJWQCAg9kFgICAQ8WAh8ABQMxMCVkAgcPZBYEAgEPFgIfAAUJMTggdGjDoW5nZAIDDxYCHwECAhYEAgEPZBYCAgEPFgIfAAUCMiVkAgIPZBYCAgEPFgIfAAUDMTAlZAIID2QWBAIBDxYCHwAFCTI0IHRow6FuZ2QCAw8WAh8BAgIWBAIBD2QWAgIBDxYCHwAFAjIlZAICD2QWAgIBDxYCHwAFAzEwJWQCCQ9kFgQCAQ8WAh8ABQkzNiB0aMOhbmdkAgMPFgIfAQICFgQCAQ9kFgICAQ8WAh8ABQIyJWQCAg9kFgICAQ8WAh8ABQMxMCVkAgUPFgIfAAUKMTEvMDYvMjAxMmQCCQ9kFgJmD2QWAgIDDxYCHwAF9wU8ZGl2IGNsYXNzPSJtYXJxdWVlIiBpZD0ibXljcmF3bGVyMiI+PGEgaHJlZj0naHR0cDovL3d3dy5iaWMudm4vZnJvbnQtZW5kL2hvbWUuYXNwJyB0YXJnZXQ9J19ibGFuayc+PGltZyBzdHlsZT0iaGVpZ2h0OjQwcHg7IiBzcmM9Ii9BY2NvdW50aW5nL0dldEZpbGUyLmFzcHg/RmlsZV9JRD1FQUVVc01BMG95VndyaEMvZ0JvZUJiRmtoZVpXbXkrciIgIGJvcmRlcj0iMCIvPjwvYT48YSBocmVmPSdodHRwOi8vd3d3LnZhbGMuY29tLnZuL1ZpZXROYW0vSG9tZS8nIHRhcmdldD0nX2JsYW5rJz48aW1nIHN0eWxlPSJoZWlnaHQ6NDBweDsiIHNyYz0iL0FjY291bnRpbmcvR2V0RmlsZTIuYXNweD9GaWxlX0lEPUVBRVVzTUEwb3lWd3JoQy9nQm9lQmNTcHB5bTdWZFovIiAgYm9yZGVyPSIwIi8+PC9hPjwvZGl2PjxzY3JpcHQgdHlwZT0idGV4dC9qYXZhc2NyaXB0Ij4KbWFycXVlZUluaXQoewp1bmlxdWVpZDogJ215Y3Jhd2xlcjInLApzdHlsZTogewoncGFkZGluZyc6ICcycHgnLAond2lkdGgnOiAnNjAwcHgnLAonaGVpZ2h0JzogJzQwcHgnCn0sCmluYzogNSwgLy9zcGVlZCAtIHBpeGVsIGluY3JlbWVudCBmb3IgZWFjaCBpdGVyYXRpb24gb2YgdGhpcyBtYXJxdWVlJ3MgbW92ZW1lbnQKbW91c2U6ICdjdXJzb3IgZHJpdmVuJywgLy9tb3VzZW92ZXIgYmVoYXZpb3IgKCdwYXVzZScgJ2N1cnNvciBkcml2ZW4nIG9yIGZhbHNlKQptb3ZlYXRsZWFzdDogMiwKbmV1dHJhbDogMTUwLApzYXZlZGlyZWN0aW9uOiB0cnVlCn0pOwo8L3NjcmlwdD5kAgUPZBYCZg8PFgIfBmhkZBgDBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WAQU1cGxjUm9vdCRMYXlvdXQkem9uZVNlYXJjaCRjbXNzZWFyY2hib3gkYnRuSW1hZ2VCdXR0b24FFHZpZXdTdGF0ZSRncmlkU3RhdGVzD2dkBRRsb2dRdWVyeSRncmlkUXVlcmllcw9nZA=="));
		list.add(new BasicNameValuePair("lng", "vi-VN"));
		list.add(new BasicNameValuePair("manScript","plcRoot$Layout$zoneMenu$PagePlaceholder$PagePlaceholder$Layout$zoneContent$pageplaceholder$pageplaceholder$Layout$zoneContent$DSATM$UpdatePanel1|plcRoot$Layout$zoneMenu$PagePlaceholder$PagePlaceholder$Layout$zoneContent$pageplaceholder$pageplaceholder$Layout$zoneContent$DSATM$ddlTinh"));
		list.add(new BasicNameValuePair("plcRoot$Layout$zoneMenu$P...","0"));
		list.add(new BasicNameValuePair("plcRoot$Layout$zoneMenu$PagePlaceholder$PagePlaceholder$Layout$zoneContent$pageplaceholder$pageplaceholder$Layout$zoneContent$DSATM$ddlTinh", province));
		list.add(new BasicNameValuePair("plcRoot$Layout$zoneMenu$PagePlaceholder$PagePlaceholder$Layout$zoneContent$pageplaceholder$pageplaceholder$Layout$zoneContent$DSATM$ddlHuyen", province));
		list.add(new BasicNameValuePair("plcRoot$Layout$zoneMenu$P...", "0"));
		list.add(new BasicNameValuePair("plcRoot$Layout$zoneMenu$P...", "1"));
		list.add(new BasicNameValuePair("plcRoot$Layout$zoneSearch...", "Tìm kiếm"));
		
		post.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		post.addHeader("Accept-Encoding", "gzip, deflate");
		post.addHeader("Accept-Language", "en-us,en;q=0.5");
		post.addHeader("Cache-Control","no-cache, no-cache");
		post.addHeader("Connection","keep-alive");
		//post.addHeader("Content-Length","13799");
		post.addHeader("Content-Type","application/x-www-form-urlencoded; charset=utf-8");
		post.addHeader("Cookie","CMSPreferredCulture=vi-VN; VisitorStatus=2; ViewMode=0; CurrentVisitStatus=2; ASP.NET_SessionId=fsz5wmmmwjomzvat2hovs5r0");
		post.addHeader("Host","www.bidv.com.vn");
		post.addHeader("Referer","http://www.bidv.com.vn/chinhanh/ATM.aspx");
		post.addHeader("Pragma", "no-cache");		
		post.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; rv:15.0) Gecko/20100101 Firefox/15.0.1");
		post.addHeader("X-MicrosoftAjax","Delta=true");
		
		post.setEntity(new UrlEncodedFormEntity(list));
		HttpResponse res = client.execute(post);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);

		CrawlerUtil.analysis(reader.getDocument(), "");

		String xpath__weather_td = "//DIV[@id='page_1']/TABLE";
		NodeList linkNodes = (NodeList) reader.read(xpath__weather_td,XPathConstants.NODESET);
		
		int i = 1;
		if (linkNodes != null) {
			while (i <=linkNodes.getLength()) {
				String xpath_channel =xpath__weather_td+"["+i+"]/TBODY[1]/TR[1]/TD[1]";
				Node channel = (Node) reader.read(xpath_channel,XPathConstants.NODE);
				System.out.println(i + "  " + channel.getTextContent().trim());

				String xpath_address = xpath__weather_td+"["+i+"]/TBODY[1]/TR[1]/TD[2]";
				Node address = (Node) reader.read(xpath_address,XPathConstants.NODE);
				System.out.println(i + "  " + address.getTextContent().trim());

				String xpath_guitien = xpath__weather_td+"["+i+"]/TBODY[1]/TR[1]/TD[3]";
				Node guitien = (Node) reader.read(xpath_guitien,XPathConstants.NODE);
				System.out.println(i + "  " + guitien.getTextContent().trim());
				
				ATM atm = new ATM();
				atm.bank = "BIDV";
				atm.province_id = province_id;
				atm.district_id = district_id;
				atm.position = StringUtil.trim(address.getTextContent());
				atm.address = StringUtil.trim(address.getTextContent());
				atm.atm_number = 1;
				atm.create_date = new Date(Calendar.getInstance().getTimeInMillis());
				tienIchDAO.saveATM(atm);
				
				i++;
			}
		}

		FileUtil.writeToFile("d:/kplus.html", html, false);
	}
	
	
	public void crawlerProvinceDongABank() throws Exception {
		DefaultHttpClient client = HttpClientFactory.getInstance();
		client.getParams().setParameter("application/x-www-form-urlencoded",false);
		HttpPost post = new HttpPost("http://www.dongabank.com.vn/atm/list_atm");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("city", "36"));
		list.add(new BasicNameValuePair("district", "37"));
		list.add(new BasicNameValuePair("region", "9"));
		list.add(new BasicNameValuePair("submit","Danh+s%C3%A1ch+t%C3%ACm+ki%E1%BA%BFm"));
		post.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		post.addHeader("Accept-Encoding", "gzip, deflate");
		post.addHeader("Accept-Language", "en-us,en;q=0.5");
		post.addHeader("Connection", "keep-alive");
		post.addHeader("Cookie","__utma=100876180.298132746.1349977706.1349977706.1350009330.2; __utmz=100876180.1349977706.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); ci_session=a%3A4%3A%7Bs%3A10%3A%22session_id%22%3Bs%3A32%3A%22c0ac2bc6f66287434419ee44802cf862%22%3Bs%3A10%3A%22ip_address%22%3Bs%3A12%3A%22192.168.34.2%22%3Bs%3A10%3A%22user_agent%22%3Bs%3A50%3A%22Mozilla%2F5.0+%28Windows+NT+6.1%3B+rv%3A15.0%29+Gecko%2F201001%22%3Bs%3A13%3A%22last_activity%22%3Bs%3A10%3A%221350011165%22%3B%7D33c3615c3668a87d300150377a8df0ee; __utmb=100876180.14.10.1350009330; __utmc=100876180; menu_title_3=1");
		post.addHeader("Host", "www.dongabank.com.vn");
		post.addHeader("Referer", "http://www.dongabank.com.vn/atm/list_atm");
		post.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; rv:15.0) Gecko/20100101 Firefox/15.0.1");
		post.setEntity(new UrlEncodedFormEntity(list));
		HttpResponse res = client.execute(post);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		
		String xpath__select= "//select[@id='city']/option";
		String xpath__select_quan= "//select[@id='district']/option";
		NodeList linkNodes = (NodeList) reader.read(xpath__select,XPathConstants.NODESET);
		NodeList linkNodes_quan = (NodeList) reader.read(xpath__select_quan,XPathConstants.NODESET);
		int i = 1,j=1;
		ProvinceDAO provinceDAO = new ProvinceDAO();
		if (linkNodes != null) {
			while (i <= linkNodes.getLength()) {
					City city = new City();
					String xpath_node = xpath__select + "[" + i + "]/text()";
					String name = (String) reader.read(xpath_node,XPathConstants.STRING);
					System.out.println(name);
					String xpath_class = xpath__select + "[" + i + "]/@class";
					String pclass = (String) reader.read(xpath_class,XPathConstants.STRING);
					
					String xpath_value = xpath__select + "[" + i + "]/@value";
					String pvalue = (String) reader.read(xpath_value,XPathConstants.STRING);
					String ss = "sub_"+pvalue;
					
					city.name = StringUtil.trim(name);
					int region = 1;
					if("sub_9".equalsIgnoreCase(pclass)) region = 1;
					if("sub_16".equalsIgnoreCase(pclass)) region = 2;
					if("sub_10".equalsIgnoreCase(pclass)) region = 3;
					city.region=region;
					city.dongabank_id = Integer.parseInt(pvalue);
					city.create_date = new Date(Calendar.getInstance().getTimeInMillis());
					int city_id = provinceDAO.saveCity10h(city);
					System.out.println(pclass+pvalue);		
					// Get Quan
					j = 1;
					while (j <= linkNodes_quan.getLength()) {	
						String quan = (String) reader.read("//select[@id='district']/option["+j+"]/text()",XPathConstants.STRING);
						String class_sub = (String) reader.read("//select[@id='district']/option["+j+"]/@class",XPathConstants.STRING);
						String pvalue_quan = (String) reader.read("//select[@id='district']/option["+j+"]/@value",XPathConstants.STRING);
					
						if(ss.equalsIgnoreCase(class_sub))
						{
							System.out.println("------------>"+j+" TT "+quan.trim());
							city = new City();
							city.name =StringUtil.trim(quan);
							city.region=region;
							city.province_id = city_id;
							city.dongabank_id = Integer.parseInt(pvalue_quan);
							city.create_date = new Date(Calendar.getInstance().getTimeInMillis());
							provinceDAO.saveCity10h(city);
						}
						
						j++;
					}
				i++;
			}
		}

		FileUtil.writeToFile("d:/kplus.html", html, false);
	}
	
	
	public void crawlerProvinceVietCombank() throws Exception {
		DefaultHttpClient client = HttpClientFactory.getInstance();
		client.getParams().setParameter("application/x-www-form-urlencoded",false);
		HttpPost post = new HttpPost("http://www.vietcombank.com.vn/atm/Default.aspx");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
		list.add(new BasicNameValuePair("__LASTFOCUS", ""));
		list.add(new BasicNameValuePair("__EVENTTARGET","ctl00$Content$CityList"));
		list.add(new BasicNameValuePair("__VIEWSTATE","/wEPDwUJNTI5OTE5NTk3D2QWAmYPZBYCAgMPZBYEAgEPZBYCAgMPZBYCAgIPFgIeC18hSXRlbUNvdW50AggWEAIBD2QWAgIBDw8WBB4EVGV4dAULVHJhbmcgY2jhu6ceC05hdmlnYXRlVXJsBQJ+L2RkAgIPZBYCAgEPDxYEHwEFCUPDoSBuaMOibh8CBQt+L1BlcnNvbmFsL2RkAgMPZBYCAgEPDxYEHwEFDkRvYW5oIG5naGnhu4dwHwIFB34vQ29ycC9kZAIED2QWAgIBDw8WBB8BBRnEkOG7i25oIGNo4bq/IHTDoGkgY2jDrW5oHwIFBX4vRkkvZGQCBQ9kFgICAQ8PFgQfAQUYTmfDom4gaMOgbmcgxJFp4buHbiB04butHwIFC34vRUJhbmtpbmcvZGQCBg9kFgICAQ8PFgQfAQUOVHV54buDbiBk4bulbmcfAgUKfi9DYXJlZXJzL2RkAgcPZBYCAgEPDxYEHwEFD05ow6AgxJHhuqd1IHTGsB8CBQx+L0ludmVzdG9ycy9kZAIID2QWAgIBDw8WBB8BBQ5HaeG7m2kgdGhp4buHdR8CBQh+L0Fib3V0L2RkAg0PZBYCAgEPZBYCAgEPFgIfAAIFFgoCAQ9kFgICAQ8PFgQfAQUbxJBp4buBdSBraG/huqNuIHPhu60gZOG7pW5nHwIFGH4vVW5kZXJDb25zdHJ1Y3Rpb24uYXNweGRkAgIPZBYCAgEPDxYEHwEFC0LhuqNvIG3huq10HwIFGH4vVW5kZXJDb25zdHJ1Y3Rpb24uYXNweGRkAgMPZBYCAgEPDxYEHwEFCkxpw6puIGjhu4cfAgUmbWFpbHRvOndlYm1hc3RlclthdF12aWV0Y29tYmFuay5jb20udm5kZAIED2QWAgIBDw8WBB8BBRFTxqEgxJHhu5Mgd2Vic2l0ZR8CBQ5+L1NpdGVtYXAuYXNweGRkAgUPZBYCAgEPDxYEHwEFFFZpZXRjb21iYW5rIFdlYiBNYWlsHwIFIGh0dHBzOi8vbWFpbC52aWV0Y29tYmFuay5jb20udm4vZGQYAgUVY3RsMDAkQ29udGVudCRBVE1WaWV3DzwrAAoBCAIBZAUqY3RsMDAkSGVhZGVyJExhbmd1YWdlU3dpdGNoZXIkTGFuZ3VhZ2VWaWV3Dw9kAgJkpVQQdl5ccG96iMzwPm04o8nrlFw="));
		list.add(new BasicNameValuePair("ctl00$Content$CityList", "25"));
		post.setEntity(new UrlEncodedFormEntity(list));
		HttpResponse res = client.execute(post);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);

		String xpath__select = "//select[@id='ctl00_Content_CityList']/option";
		NodeList linkNodes = (NodeList) reader.read(xpath__select,XPathConstants.NODESET);
		int i = 2;
		ProvinceDAO provinceDAO = new ProvinceDAO();		
		if (linkNodes != null) {
			while (i <= linkNodes.getLength()) {
				City city = new City();
				String xpath_node = xpath__select + "[" + i + "]/text()";
				String name = (String) reader.read(xpath_node,XPathConstants.STRING);				
				String xpath_value = xpath__select + "[" + i + "]/@value";
				String value = (String) reader.read(xpath_value,XPathConstants.STRING);
				//System.out.println(value);
				city = provinceDAO.getProvinceByName(name);
				if(city!=null){
					System.out.println(i+"-"+value+"-"+name); 
					city.vietcombank_id = Integer.parseInt(value);
					//provinceDAO.updateCity10h(city);
				}
				else System.out.println(i+"-"+value+"-"+name);
				i++;
			}
		}

		FileUtil.writeToFile("d:/kplus.html", html, false);
	}
	
	public static void crawlerATMDongA() {
		CrawlerDailyATM crawlerDailyTivi = new CrawlerDailyATM();
		try {
			ProvinceDAO provinceDAO = new ProvinceDAO();
			List<City> provinces = provinceDAO.getProvinceByRegion(3);
			for (City city : provinces) {
				System.out.println(city.name);
				List<City> districts = provinceDAO.getDistrictByProvince(city.id);
				for (City city2 : districts) {
					System.out.println("--->"+city2.name);
					crawlerDailyTivi.crawlerDongABank(city.dongabank_id,city2.dongabank_id,10,city.id,city2.id);
				}				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public  void crawlerATMVietcomBank() {
		CrawlerDailyATM crawlerDailyTivi = new CrawlerDailyATM();
		try {
			ProvinceDAO provinceDAO = new ProvinceDAO();
			List<City> provinces = provinceDAO.getProvinceByVietcomBank();
			for (City city : provinces) {
					System.out.println(city.name);				
					crawlerDailyTivi.crawlerVietCombank(city.id,city.vietcombank_id);
								
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public  void crawlerATMTechcomBank() {
		CrawlerDailyATM crawlerDailyTivi = new CrawlerDailyATM();
		try {
			ProvinceDAO provinceDAO = new ProvinceDAO();
			List<City> provinces = provinceDAO.getProvinceByTechcomBank();
			for (City city : provinces) {
					List<City> districts = provinceDAO.getDistrictByTechcomBank(city.id);
					if(districts!=null&&districts.size()>0)
					{
						for (City city2 : districts) {
							System.out.println(city2.name);				
							crawlerDailyTivi.crawlerTechComBank(city.id,city2.id,city.techcombank_id,city2.techcombank_id);
						}
						
					}else{
							crawlerDailyTivi.crawlerTechComBank(city.id,0,city.techcombank_id,"");
					}
								
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public  void crawlerATMAnbinhBank() {
		CrawlerDailyATM crawlerDailyTivi = new CrawlerDailyATM();
		try {
			ProvinceDAO provinceDAO = new ProvinceDAO();
			List<City> provinces = provinceDAO.getProvinceByAnbinhBank();
			for (City city : provinces) {
					List<City> districts = provinceDAO.getDistrictByAnbinhBank(city.id);
					if(districts!=null&&districts.size()>0)
					{
						for (City city2 : districts) {
							System.out.println(city2.name);				
							crawlerDailyTivi.crawlerAnbinhBank(city.id,city2.id,city.anbinh_id+"",city2.anbinh_id+"");
						}
						
					}else{
						crawlerDailyTivi.crawlerAnbinhBank(city.id,0,city.anbinh_id+"","");
					}
								
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public  void crawlerATMAgriBank() {
		CrawlerDailyATM crawlerDailyTivi = new CrawlerDailyATM();
		try {
			ProvinceDAO provinceDAO = new ProvinceDAO();
			List<City> provinces = provinceDAO.getProvinceByAgriBank();
			for (City city : provinces) {
					List<City> districts = provinceDAO.getDistrictByAgriBank(city.id);
					if(districts!=null&&districts.size()>0)
					{
						for (City city2 : districts) {
							System.out.println(city2.name);				
							crawlerDailyTivi.crawlerAgriBank(city.id,city2.id,city.agribank_id+"",city2.agribank_id+"");
						}
					}else{
						crawlerDailyTivi.crawlerAnbinhBank(city.id,0,city.agribank_id+"","0");
					}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	public  void crawlerATMOceanBank() {
		CrawlerDailyATM crawlerDailyTivi = new CrawlerDailyATM();
		try {
			ProvinceDAO provinceDAO = new ProvinceDAO();
			List<City> provinces = provinceDAO.getProvinceByOceanBank();
			for (City city : provinces) {
					crawlerDailyTivi.crawlerOceanBank(city.id,city.region+"",city.oceanbank_id+"");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void crawlerProvinceTechComBank() {
		org.apache.http.impl.client.DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost("https://www.techcombank.com.vn/Desktop.aspx/Ket-qua-tim-kiem-ATM/Desktopaspx/Desktop.aspx?desktop=Ket-qua-tim-kiem-ATM&catName=Desktopaspx&Occupation=C5CC694F072149CB8157B7AFA11DBEF0&Department=&Contact=&SFirst=0");
		String html = "";
		try {
			HttpResponse response = client.execute(post);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				html += line;
			}
			XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
			CrawlerUtil.analysis(reader.getDocument(), "");
			String xpath__select_td = "//SELECT[@id='vie__ctl7__ctl0__ctl3_ListCongTy']/OPTION";
			String xpath__select_quan_td = "//SELECT[@id='vie__ctl7__ctl0__ctl3_ListPhongBan']/OPTION";
			NodeList linkNodes = (NodeList) reader.read(xpath__select_td,XPathConstants.NODESET);
			int i = 2;
			ProvinceDAO provinceDAO = new ProvinceDAO();
			if (linkNodes != null) {
				while (i <= linkNodes.getLength()) {	
					City city = new City();
					String xpath_province_name = xpath__select_td + "[" + i+ "]/text()";
					String province_name = (String) reader.read(xpath_province_name,XPathConstants.STRING);
					String xpath_province_value = xpath__select_td + "[" + i+ "]/@value";
					String province_value = (String) reader.read(xpath_province_value,XPathConstants.STRING);
					System.out.println(province_name+"---"+province_value);		
					
					city = provinceDAO.getProvinceByName(province_name);
					if(city!=null){
						city.techcombank_id = province_value;
						provinceDAO.updateCity10hTechcomBank(city);
					}
					
					int j = 2;
					HttpPost post2 = new HttpPost("https://www.techcombank.com.vn/Desktop.aspx/Ket-qua-tim-kiem-ATM/Desktopaspx/Desktop.aspx?desktop=Ket-qua-tim-kiem-ATM&catName=Desktopaspx&Occupation=C5CC694F072149CB8157B7AFA11DBEF0&Department=&Contact=&SFirst=0");
					HttpResponse response2 = client.execute(post2);
					rd = new BufferedReader(new InputStreamReader(response2.getEntity().getContent()));
					line = "";
					html = "";
					while ((line = rd.readLine()) != null) {
						html += line;
					}
					XPathReader reader_quan = CrawlerUtil.createXPathReaderByData(html);
					NodeList listQuan = (NodeList) reader_quan.read(xpath__select_quan_td,XPathConstants.NODESET);
					while(j<listQuan.getLength())
					{
						String xpath_quan_name = xpath__select_quan_td + "[" + j+ "]/text()";
						String quan_name = (String) reader_quan.read(xpath_quan_name,XPathConstants.STRING);
						String xpath_quan_value = xpath__select_quan_td + "[" + j+ "]/@value";
						String quan_value = (String) reader_quan.read(xpath_quan_value,XPathConstants.STRING);
						System.out.println("---"+quan_name+"---"+quan_value);	
						
						city = provinceDAO.getProvinceByName(quan_name.replaceFirst("Quận", "").trim());
						if(city!=null){
							city.techcombank_id = quan_value;
							provinceDAO.updateCity10hTechcomBank(city);
						}
						
						j++;
					}
					FileUtil.writeToFile("d:/tcb_"+province_name+".html", html, false);
					i++;
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	public void crawlerProvinceAnbinhBank() throws Exception {
		DefaultHttpClient client = HttpClientFactory.getInstance();
		client.getParams().setParameter("application/x-www-form-urlencoded",false);
		HttpPost post = new HttpPost("http://www.abbank.vn/vi/Mang-Luoi/ATMs.aspx");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
		list.add(new BasicNameValuePair("__EVENTTARGET", "ctl00$Cnt$Cb_Pr"));
		list.add(new BasicNameValuePair("__EVENTVALIDATION",
						"WfIc7Sw6qrFnEiRhyUbMPBWo9mSEkUU4cSYReUSWigFXa3M7L9dhzi0J/fV8lCk0KDcoM2jCN9vaQ7kgB+iLsoQIamLgZ0ll4EJ+esqoM5/hmDDULPFw5gs9f9TB+5oLNA7Rly4tSosf5pPSIEG8M699swLXY1aV8170rasiSW7bjqldqRk0D6l375QbHXD+IW3EmErNFcC3Iis2v1CDEVxzVZNTxxP5jmih5QZgCnJW78ajMP7NVREepsyoh2tEQJW2L+w7f1Vp0W7mucp2Ed452LOKBkOkRKnWbh3qPQM="));
		list.add(new BasicNameValuePair("__LASTFOCUS", ""));
		list.add(new BasicNameValuePair("__VIEWSTATE",
						"tWLhpUH5zN0URDbUpSdpw8ZWe+4AwSJU6JDgJKiBvlCTku3/DciX4jTrsTcuVRS6WV0fZUBENzvs0xX2tSJJftOvNTjxT/aaaQCk7rcNhhmCZdwUWnqpce/oD5s62jp2YgK0tMcpMjYPlm198sAB/sV3TAH8RLVz2OeH/VBWBdkwMvZZJbDYqy0oHrjv/qWBssluYU+EZZtoz1oWFvHohIx0ZXLXaTDJ/csaRwz+VzuRHgMq4i5SEcAAf/xIisf1jS+7jpy1viaHrIuHRClaFROWaUMspGm9UNxg54j4uhljmDxn3V4O5qsdqN4stISg2h8xuQyU59V0M/dFMqLtAgck321p+s8cJQ/tBZIFt/YadMmlOQ5OjZHC6cp1kCAcr994AYky7oGPX/Ub3JM8g24plN6ajX8svgrQ+ICfJr5tOgvgIyZH/+x3WfwUQNxPZ0O1ypaUaFAdmBNqmtIcbslCzOIW71cZ5YaYj8YyieTGGcxc5+rUbjslndcBgg+krjPIx6ddVHa7LLqNojeTmTV/l/T0OTUQijXrFHvi7A+dsb21MPnYI0Na9dYKZKkgBPoNd+wwLhhMPk8Pw66nlhFM3MgXzD7DcnV5j01th+Sj7LumlUnFZmxjApqAc3rjzClW3j76ZCij+7rV2MQZFELH0JVd6I7ed1dVBxeDWcYOwB7Y8jgq2/ytd/T5IzdnQ9I887sWRItO6uf2e1lU6nWIyZUUssW+z03Kf0ZX/vQUKpemz937iUdZKd5OoixAW+iuRgxL649E343AQWKfatSgb3TXufqXYfu9MnIHSGFSkQu1uz0DaxoaqCnPcbwIQvK52JMR5+t5SNxBLfls6Ut60pOYdUgiCb1GGE8jUtlQ6LgDGpnCDJWorv4gk00Hos7NEILsppJjEZnYwDVkWk/E6cVR04nqJ+XJSA6Fkvzo96MS4uWX+LBjWsZ6tofvCnW/ZtSf/7soNvXk5bp8Hxjz3jje8syJg0hyvXS7GP82Mur6nOqJGwnjfuaoYuMlMW9tFMCDBeu8CaAupDRK00jxnqnGu5EkIZ4knALhH7hqfx0Pf9ylI0UiTPsr44+nrtMHLVoONOSWLQgDVTc5KiLdgxiBN3b8Ty1qUYr1OAWeJ/HHhFx1TJRsnFwYVX4AmFaebwCwZSa5AYnKmOYNNnzhZcEII0nshsRls6kBptlphE5SuT5oCu/PfKRYTeMzmLl6mq6XXX0xUpAdDDnN5Yvp6KPzu/jYeelHotEJcw6dVOZjiDk5V5gAR5eh1g/1UAnMmjGHvcPTX/N24k/EF8XppXkWpAhsFGrdR6OMrOr2DvHhq8IrzHA8LJaaiyL1wF1NdBSf1FXyz3TlTo3yJLrwd9dZWbpILWyVsDrIPWNjekB0hAjnZUOl2BbpYa3aHNQYXnozqgZZQ65W+jUtGToYsmIHH9aWYCnh/f0MVwHs/HdaU5RWpDtbqO+GsYRtzUu1LnVphyZ0ujKR5hFVcxa1gbpGbvK4WrzgjIeqUYOjC63uSaSiZmpaWxSgWzjc721kS9q0xPLU5S5owx6AJ05sCK32EXVzccs/F0P7gC8T/IeXXL+/BhahIPRLZ8+fkHOlfxt21wiitLHPweScyYWtX93/lQBsneHbY+MApNv/kWpmMuWsnUaqFzLlzi87pmXkyi3SiKA5CcPemDY/VOdsUenwl0osRY73csNbKpP8zly8zIaaLPSnw9rdtNxtXo718Mm6wIwplVe1Of9NF7BVswTm8PtwNn8pOfCyfErdUZcgIp6CJ65bo4swOwtMgquLTXBAhGbWtG8WZaoM3HwJiNXdn1a2qDWIf04Vu6UcvQA+ExewjrKsq+Hs/jMmONPEakHZBFnxEQ2If5/3uabFxOivnoq0JhUhzVEvuf+ExB8WBPXDxp37uLHQQa0CFEObXhJn/VWIb+goE4UCrxF7HXP0LJTmKlbkjePRFaRemoZCUjJSc/9quKNXQSULfoC/gzPsugfgk0LNrUtI2lD28lfNQpan+RDBSmgDNHVfeXY2VvhdngEN6WcJZ/6XNkejrzPIvQ+0fbE8jyTcUQz7TTYVuopVH2P3OGk/rtZ0Hsaknx1RNSfeIbcWszUiSZflUatt18mfOXE9WSPW1X2wzFfkyP5giSPDZ70HmwhwHWYqOFG3xaKUdxVcpFjvlthniHqcjSZHQ01mHmHtoRngUBdVn6fmk5erDKrRCaqUbNjrV8LKcBgJOPojWNLuo5an4zTsqxHN9jCvfUtvdrvItp3MDIKMWO8ukHaPr1DYaNyZyaoxZuQVKA8ZZkGoM1tJGymhWB7wagrKSyQZ2AtbpWlpPQa/QGAdjPN/YlLvEQuMVfwqqVMkqGgeQ9QguxRKy1gwXRiKlnFKzDnwc9pAl+W/jL0EfG6Vv9RD21z9gK3AfFfcNIyFmVwXWyGDRqiwzMC+BNpD08bYTZLR+Pdtm/5lc2BQfu+2mvQnxjL0daycd16SQFU5BCl9cauywzacjKGY+8ITWzDjmmdwJ3V05Unjms+F+7OHvxy5by5dFZvfvgNu1fRsnKQ213H/LVyUxBLICUskl95bnfMNumi8yNWioHLRF4Xipfea5mBlg6jKwPmEaLTXtjmyPa5yHDN6VuK/74tfQiUzVd2HekdhSOSWZmTM+MHhfL3SI+6yW9KKMq13r45wKiQ1eKHMkRbNlRt52Hug2PTMzYqwdab+1k4DczY8cvMHhKzxIX0g/Q1MjB8zXparLEsosqtiOopgphqc9BM3g+Fx3HQjJ1q7CbDHML5eciC3RtXollhJSsSa+kUXIc9HtbjLmmWVPnzrTSccDUcQBiSaRVyLYyzCw81Rf+sEmH38sjq7oqDp0/EqMe/alDMfLqiMq0QxKCsfxnLaFrEe0MQKgw/4PG01InpEg84DrGbmyDc9lRgAjuWDvvq+3vDegPv0YgCV8eLtSeQ6gCJV0YfSd55MiZJQwmXcIKcrcYC24K85oZpw6t+2PPd1gc++I3Vol/oLlu/ur9CWyRJzLnD5yvkyrW0BfcNj3pfjmNt0nVhmZwtUO+uqdgzotyLRELfZ0/nx/IDtq/7ypomk2D80nrFh8L7AWPqI0jvaRtKyVcug2zWdNb6InEg2EpVvynFTBnetfsaaUg1K0NUd9al2hBXOB+Qddl8INz16yyYypdP7/+3QP2XvgwcJDfSxv2rbMCFF0jPSFXyHD38KzjO/pSiiWhwZGVPSKARQpHidx86vFTZDzrUmcZHFPZlPqw92J8Id/NbLn5JhnlperRQy4/mAdpu6Yx9hX5KIBOBOCGs9oX68pFcP4Pn/wHxRFyW44pz8+ei22SNQosqKaVy4O0p0wz3LnWzm2JR8YXSrtuIkXKYHrVryoz49J16fOgnq0PefMFfv9TYV9VDCNH3vbcb7TTLABHZX5ylbKm/9IKH5o6mzHuXfjY6ugoCRlRONeZ5hCtp8vzesOtuf1jpYk0mfRuFyrBhd6xQ3KEmsGszfJp+c84VYHAXlRjPeSoim4yoYS59BXLo7SBaNkO+7nnlTnyZhKNVErCNLm8fRamK0FVeWREzux9sPshnXQTvn0pT1g34ry8y5AGxJz5V/UEWLRz8PkcUjM2givrr51PHQYFhC1lPzmpNvmCBsgSkFUTRoOGnM1tHbvnNNk0JStOyjpPw/VUGEkjnx81E/7/HpLnZtJEVlrDtOld2AbdveHOhjc7fseV2xi0rJDd7ODrD4MbU3NovyaYYajqQXiu/4AKVHKO2CVRg/c0ZUgzkzjU3HALDXj4B7+iAmmc0fYKEfrooZb7mt9MTFKz4re/7XFCz29z39eVp9c7+jrT9nGDKXhns9eWmzwJ/Ykkz3FxCfQY7I3I2pJONa7nu33j5V28aQyK/kgShNUnebQET2JOnnh5c8yido1UJVGmh1K7GDNNdYB3lU3s4jriT2B3MeINF3YeRQV97N68580VIcU+KDlN8+P0bQ/y6LjkeGEeNzOAdudFIczlDhu/HKPI6s1G4UEOJl/1gdKhzv2V/0PR++TdxKsC5S74LnQmj1/qvebsLNz34GGcJ9gfGU1GYTG15hRrqG9oK9my6Zz7spiRlkup2TA3jXvxaUMLdcOoIjON32OS6eQJrY6nD1LTBCSkWpNkuO8y4G/OgVYL3hHW8ayDbGR31kxijeVaL5s2qsmcHCffgZ6Y2tYEjaHv3COL7oQvwvEVCfH6TTS9y9AKCRcpD9n6Sc4mqVk31E9Ih6SqHZAV8FY3Aiep+FkdCB0T4dT0B/CT5jDxrIxqbKITJzMXILjzb9aLDPiq8A/hVybzyUELmxirQ3jn/SmIbMyQpsO0e/FGL4Fyl+fYz99sZgQ3kc6/8d66F29PqseiuQslvysyuVgBP9QspABrSDgMAVN5UGDnGQ9np5k7xwkA5pQ8k9/N0P1pLShc7rBT/1qa/e/XtLCwM2foonniIq0hXTKrhWEZCssG62u0SnkOp1KuqMupuegg2aD3hAE4L2GbHuQMnhI3+rUG9WeYcu0D20ugK6gD9zn8mUqcdgykH+/UwMz4dkvRR69gvn5PJspm3XihI0x+jzhs6l/RjQiIioDPLErToPVqEJj08qJ2YnEaYcuP+Hoq/r5Bx0swd11POu0LrAiEmNR9Jhwpj+fIMj1dDPLeuyUZzdqvyIXadl6mqIx930LPmB4xPnbT5oTimK52jY2m4vAjI6WRw40H0ltJcs6vdfp+Uc49t/0qYjxKEHKy1tPzXsKkFL1wiSHY/EByS5zssCX5QUpVJaBf87ZMkgcuZQ4OnWYumWZQ/NvQnGPnUNz0ahda8M4PjQaxYW+IHcv3iHaN0Zqf2G/7BB3i2B6LHOPcGi59qFSidrL5X+aYccYXx30aa1KSVnXpHi+MgAqVlTHDOcgnFSvfoLq/pifIFEdQFFB0F9s0/P7E9DvecwL3T65bth0bm7UMzuX7+JesTGLa06NUCRQrQDVfWIy0Me8y0Pa7IXgneINuDDJ9tEN3Bn34KErDJEQo1zTLRsCQqScrr6ACfI+XsBKEwkEIo7yKVE+bKMnXUXvJu8SjzWdEnaOtJTytgflCIepDevWq3v92dZ7E5VpYtTAg8Giw3helwJeHEYh/JTnpJB+lpcLl7ElfF6gMkq/C6Rx1JdHD/neicHy9RIBH7dy/Hcohkg/+8zjvOkmXjBjVLZCVm7pfTufxkEcje28BQfH8doXxdAKiYv8B8oxVQT7PfKp9tLpCjhvkhVYYV5LVHmc+owz+ijuFMuJhEuSG7HosUuiD7uOf2xltHNT2AkO9YEZ4W2nDJ7D3mJ+DTapOzUX/6fwfd4bB3fDouTvSTfErkA6pNtjGU7hQMxE9cfu7YT3AN1cncEOcGC05Y0qTcMKg05UVIIsQopViZZ3G1UprMmbz6ej47cHKxyF6d+bZVt1jFJzLUzcYNOwU6iNIRmg0mPqd02YLJ2Y2f5cK8QbXkpwTP86AKb6z89hRsQUxSt3SMuJE6O3UPuZzfyBLJL5IBhARM92FHf9ThwjqUe+tSJ4a1u7Ege9reWZ3mjVK4gWm0SP6gi3GAhNnC/JFxRbfNau47qUpunTKhTbxdDHfI0/9rOFS1BPOPgqqNwAjHDQ0mdpiJZnaTnXPVQB5ncE3kUiSKlyIL+IR3kL/H4jMWCvU8fl+DBZpYngowTMntyNBBSWxOARWKFWxZPbZDuV29/EReYbGQK4nFckoIHRZGjtq1tLiot3pDV2iMul/PFeQM+3xed741jP16fnmipsZp+50jpi2ykITM+bF3EDwxGMts7+5Ly7Qxp7T8/rl3x6H9dP6TwG+uYcPrUApUhGh+Y3EHqxajHWKTwIcvNxjot7/VW83ssLyICNKG1MZJ099kky/y/ShAiYdIMoobvLI9clAIvKbL2l6zX2QP0iXeRibIPwaIEemFUU1SdQHkwvrsmQL22SEMqldp/Y6+zPytNJmW4bq4nsXhJFywlfR6VxIZOnhdTvxzBvAw4MMyG7rupFbCo2p4EQmQxENcatjIcNw5y02FrmE3PM2EPxlHMYkM7//rlANLgkUkuWAbFAuBHgxHphBi6N8nCYIPm/fnPrEziD1ithdHJ0K0LJCQHrkYWQmIFUT+bU77FCbTNFcMywbO59UiyuB7lXfooJJVBFH/YQT9koN7UV/RXVq3sKMaYXZFmPfLGCSurj6vOT/C5M9MP1DcNxN4+mKADVgFn9hlZkAaucbRSArX68ZA9T0n96Nj/N7t7UxZRRtwQmnXg4hheqkdDEAVoFm9FadHdiHjXcpumUhEMQZMnuv+toK21AYFLD/DOuR+b9wQADfR1oBGocCXvOVNCte72Aqq4J02p5q0kojGOuTPnG4Ysgj1WliS0RmTRgE85x5VzC5Y0NFV4thaSsasxSAY4vP3RmZUbs0fLansSTCBcAQ1uq4PM40Ip//FGwALpYxx4LNMFhVsg+gpQml/3B9AvpBXWPJUSFBpnjd2F5xBEjIBDS6xadpukk3e1V5ux/xPltpj79ssB15QKeJqiZlJ64XltNyCep5AtO+/MAqsbPgQlcWBdbFPp/Yr9WamznoSMefHaiaf4rG3OcuDz7M4F7E3UHeOx4AL83zYf7ZEhJPS87JdTCALFE1hyOsO/6jJhwzfliIdeDkLH0Kqf5Xf6yqOp0yCkDYNgM28kTV/l/fH45XAJCYjgM7yEcBoDCPwum3lpTcowr7lI+sSJ5V8J+72utu8aeFTLtLQekiqR43Vg+VyF8odhg6as7Z0ML/gSOlwTzSiB1S0zIg0BzwWLFsI6rVvHl7uyruEqvGQsqJF9KkXFYNftQ44DqVUs7iEDkUu98uO93wNbSCJtbWsIw7VTrUh8T/R5MCYfLeNP4TBZiagAOjWNfankJze9eE/ie1YyCDEBeJFC+3x9SYczl4RhSO1Fw5Ndues94qCNM7Vutiny9+A9s4luFqR7VR3vOGQ4Bg3zoPa0I/wbymlhRCXaWnWsv+fGrBMyIpsJeW3WIu56wHXz/HkO/uEOJI2ldIiU+lp+hH9SZOskxT2FNg6ZRrE7zigJwzNtQDe7IwmjsQznPQOudGAzPXGCjpjr4wQgbyZR7ivVHa4Jmd7TJeFhosKwW+KGSLZkQY8IM22sQhMlt43AtNwyBm3yy1CZIac4C3beGnqHC97cvr7I3bFi8e+NTV5yqDo6kl8WTM27RZG9rH+EX6kLJ5TZICx0e9qL5tkYR0RmAiqnuG0HGCL4ovHYoGWkWEgfspDnqgmPyKrVlwdZOhiZHYAkHyWSD1D44g26fipxyMiQum946X6PiIQbqo5mVBD2sHvvcFqOVz/tmrqX6KMfy3XwqhkNhIxeqAmc4SRHdVR7nfjs+8wx9YzgPk+aqtr5BJa8R79Yn5Url+xt7GTU4VzUgyMkovt8/iLwcVIQlcWRi+dMk/CUmLkd4hf3/a/c7pbAcxIZsEv5+fvPJP6fnv0dVYAN9x4t3NjNL+HqaRJyvNGOlMUnsihrOEKHbYlq0QNkFVvO1bnyExE45fe84KR1ataV6tTuaern09Wv+0z9/EzHDyzm/uBnCTR0Ld3dtqFVMOD1x3aQSOyWfPh1V2b2y8M2ktLdWF0RXEc4i3labYCP8QfMW9iiyWB39Dnr/r69OE0UGYuL7gQm0XZZ4v8R7NnYqeyaZuei9BoKk8WZmYeQJ5HVGY6RzAPiEwPR/WUo/rrYKlFBhLLAGA6S6zevRLQH2nASdjnQd6eMHDHuOe4rGBt54hwFApsBVGRrQ0zBX26A86BzoTZAqHl62vRTVH9rUfSkS6poli0Fuk8ILVLCyrCe8ZhFNcCUwa+x/2LqHyH5Ru9nkD2c8HLAxMHvB1olVrW43Hkxtbl1/C6ufN/JqfQMIMpT7psVM3i4UwIUUT1GeISqEzxYB6u57Kyb3QcZIPIctHzjmvyKlRISJic7CH+Tt4aPggT8hRtc2ZYJhd1ffR/G6PCnGiQ7YGEGqmjXhx4luEvVVwFVXdyWG6w+giykClsNBx/dWhIS5gJpTBBihdPqkc32cg68Vm8MW62AD4A119bqb9DepaCVX/9+viVtrKuR5M4ZCyzDdvAyqz9csjJp0U2MlkoBPwPII8UWxPcZLLUVvIUkKm3al+QjeOHyhkt+nsdLJiRNToKW+47ZzSz9GTvjwJtmtkre7K8YN7YVml/EnrY0o6NvmZAZy7IjQmz6DlH/rWWTfMYP7kPeJZzf2aQiwWUuHHDgSnj2eWOOQb0sGg2Uxvg/MnmpML7KYLNFFn1YTrE3YVtP8NmHZevR1yVfRZBj6VL7vnnkqSoGyrNwGRxi1aadd0iU0PCtvM4Vslp25lfg7zQJogrT34m6abb5xTWzY9WST5frw9hjBca/ie7cxlI1aPReoqifylgAV41YmYjINiorCCeacMASzQehABBxeZV1aJacRpj4RIt5HNniiSMcWe+ZwY2TZ2Iuop6ZQD/tMPvDeOJQ2J5TxkDW54WC7Dl0KWKNZjztgoVeievNE3F/cqilKSrajfhHLYspJ5bC4EvdzPdf2cO7OD2DFkC9rKLqfj/hla0JoYqwLu7XlpgtyV0zSQkOr2IYVZaY3a30UhrrAmfGds7qf2+gaDZPj4demJZtD+7QYleZzy+jovPlnVnWWvP4Az96DsNL/PbzfxGiuB2fi12rJq8oGDH7qo5jbyyTgVu+8pTdzH2q6c9+wximiRcLdMUNud/Xvxv2Lg=="));
		list.add(new BasicNameValuePair("__VIEWSTATEENCRYPTED", ""));
		list.add(new BasicNameValuePair("ctl00$Cnt$Cb_Dis", ""));
		list.add(new BasicNameValuePair("ctl00$Cnt$Cb_Pr", "5"));

		post.setEntity(new UrlEncodedFormEntity(list));
		HttpResponse res = client.execute(post);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		//CrawlerUtil.analysis(reader.getDocument(), "");
		String xpath__select_td = "//select[@id='ctl00_Cnt_Cb_Pr']/option";
		String xpath__select_quan_td = "//select[@id='ctl00_Cnt_Cb_Dis']/option";
		NodeList linkNodes = (NodeList) reader.read(xpath__select_td,XPathConstants.NODESET);
		int i = 2;
		ProvinceDAO provinceDAO = new ProvinceDAO();
		
		if (linkNodes != null) {
			while (i <= linkNodes.getLength()) {	
				City city = new City();
				String xpath_province_name = xpath__select_td + "[" + i+ "]/text()";
				String province_name = (String) reader.read(xpath_province_name,XPathConstants.STRING);
				String xpath_province_value = xpath__select_td + "[" + i+ "]/@value";
				String province_value = (String) reader.read(xpath_province_value,XPathConstants.STRING);
				System.out.println(i+"----"+province_name+"---"+province_value);		
				city = provinceDAO.getProvinceByName(province_name);
				if(city!=null){
					city.anbinh_id = Integer.parseInt(province_value);
					provinceDAO.updateCity10hAnbinhBank(city);
				}
				client = HttpClientFactory.getInstance();
				client.getParams().setParameter("application/x-www-form-urlencoded",false);
				post = new HttpPost("http://www.abbank.vn/vi/Mang-Luoi/ATMs.aspx");
				list = new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
				list.add(new BasicNameValuePair("__EVENTTARGET", "ctl00$Cnt$Cb_Pr"));
				list.add(new BasicNameValuePair("__EVENTVALIDATION","WfIc7Sw6qrFnEiRhyUbMPBWo9mSEkUU4cSYReUSWigFXa3M7L9dhzi0J/fV8lCk0KDcoM2jCN9vaQ7kgB+iLsoQIamLgZ0ll4EJ+esqoM5/hmDDULPFw5gs9f9TB+5oLNA7Rly4tSosf5pPSIEG8M699swLXY1aV8170rasiSW7bjqldqRk0D6l375QbHXD+IW3EmErNFcC3Iis2v1CDEVxzVZNTxxP5jmih5QZgCnJW78ajMP7NVREepsyoh2tEQJW2L+w7f1Vp0W7mucp2Ed452LOKBkOkRKnWbh3qPQM="));
				list.add(new BasicNameValuePair("__LASTFOCUS", ""));
				list.add(new BasicNameValuePair("__VIEWSTATE",						"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"));
				list.add(new BasicNameValuePair("__VIEWSTATEENCRYPTED", ""));
				list.add(new BasicNameValuePair("ctl00$Cnt$Cb_Dis", ""));
				list.add(new BasicNameValuePair("ctl00$Cnt$Cb_Pr", province_value));
				post.setEntity(new UrlEncodedFormEntity(list));
				res = client.execute(post);
				html = HttpClientUtil.getResponseBody(res);
				XPathReader reader_quan = CrawlerUtil.createXPathReaderByData(html);
				int j =2;
				NodeList listQuan = (NodeList) reader_quan.read(xpath__select_quan_td,XPathConstants.NODESET);
				while(j<listQuan.getLength())
				{
					String xpath_quan_name = xpath__select_quan_td + "[" + j+ "]/text()";
					String quan_name = (String) reader_quan.read(xpath_quan_name,XPathConstants.STRING);
					String xpath_quan_value = xpath__select_quan_td + "[" + j+ "]/@value";
					String quan_value = (String) reader_quan.read(xpath_quan_value,XPathConstants.STRING);
					System.out.println("---"+quan_name+"---"+quan_value);	

					//replaceFirst("Quận", "")
					city = provinceDAO.getProvinceByName(quan_name.trim());
					if(city!=null){
						city.anbinh_id = Integer.parseInt(quan_value);
						provinceDAO.updateCity10hAnbinhBank(city);
					}
					j++;
				}
				FileUtil.writeToFile("d:/tcb_"+province_name+".html", html, false);
				i++;
			}
		
		}

		FileUtil.writeToFile("d:/kplus.html", html, false);
	}
	
	
	public void crawlerProvinceOceanBank() throws Exception {
		DefaultHttpClient client = HttpClientFactory.getInstance();
		client.getParams().setParameter("application/x-www-form-urlencoded",false);
		HttpPost post = new HttpPost("http://www.oceanbank.vn/Mang-Luoi/Diem-Dat-May-ATM/Index.html");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
		list.add(new BasicNameValuePair("__EVENTTARGET", ""));
		list.add(new BasicNameValuePair("__LASTFOCUS", ""));
		list.add(new BasicNameValuePair("__VIEWSTATE","/wEPDwUKMTEzNTg0NTkyNA8WBh4JUGFnZVRpdGxlBSJPY2VhbkJhbmsgLSDEkGnhu4NtIMSR4bq3dCBBVE0vUE9THgtEZXNjcmlwdGlvbgUXxJBp4buDbSDEkeG6t3QgIEFUTS9QT1MeCEtleXdvcmRzBQEsFgJmD2QWAgIDD2QWAgIBD2QWBgUKd3A3MTAzNDY1MQ9kFgJmD2QWBGYPFgIeB1Zpc2libGVnFgICAQ8WAh4LXyFJdGVtQ291bnQCBhYMZg9kFgJmDxUCKy9OZ2FuLUhhbmctQ2EtTmhhbi9UaGUtT2NlYW5CYW5rL0luZGV4Lmh0bWwPVGjhursgT2NlYW5CYW5rZAIBD2QWAmYPFQIrL1Rpbi1UdWMvS2h1eWVuLU1haS1TYW4tUGhhbS1Nb2kvSW5kZXguaHRtbCNLaHV54bq/biBt4bqhaSAtIFPhuqNuIHBo4bqpbSBt4bubaWQCAg9kFgJmDxUCIi9UaW4tVHVjL0Jhbi1UaW4tTm9pLUJvL0luZGV4Lmh0bWwfQuG6o24gdGluIG7hu5lpIGLhu5kgT2NlYW5UaW1lc2QCAw9kFgJmDxUCKy9UdXllbi1EdW5nL05vcC1Iby1Tby1UcnVjLVR1eWVuL0luZGV4Lmh0bWwdTuG7mXAgaOG7kyBzxqEgdHLhu7FjIHR1eeG6v25kAgQPZBYCZg8VAjYvVHV5ZW4tRHVuZy9UaG9uZy1UaW4tVmEtS2V0LVF1YS1UdXllbi1EdW5nL0luZGV4Lmh0bWwpVGjDtG5nIHRpbiB2w6Aga+G6v3QgcXXhuqMgdHV54buDbiBk4bulbmdkAgUPZBYCZg8VAhUvTWFuZy1MdW9pL0luZGV4Lmh0bWwOTeG6oW5nIGzGsOG7m2lkAgMPZBYCZg8WAh8EAgoWFGYPZBYCZg8VBAEwWmh0dHA6Ly93d3cub2NlYW5iYW5rLnZuL1Rpbi1UdWMvOTI1L09jZWFuQmFuay10by1jaHVjLURhaS1ob2ktZG9uZy1jby1kb25nLWJhdC10aHVvbmcuaHRtbEZPY2VhbkJhbmsgdOG7lSBjaOG7qWMgxJDhuqFpIGjhu5lpIMSR4buTbmcgY+G7lSDEkcO0bmcgYuG6pXQgdGjGsOG7nW5nEDA2LzEwLzIwMTIgMTE6MjFkAgEPZBYCZg8VBAExdWh0dHA6Ly93d3cub2NlYW5iYW5rLnZuL1Rpbi1UdWMvOTI0L0NodXllbi10aWVuLWxpZW4tbmdhbi1oYW5nLXRvaS1zby10aGUtcXVhLWtlbmgtZ2lhby1kaWNoLUVhc3ktTS1QbHVzLUJhbmtpbmcuaHRtbFxDaHV54buDbiB0aeG7gW4gbGnDqm4gbmfDom4gaMOgbmcgdOG7m2kgc+G7kSB0aOG6uyBxdWEga8OqbmggZ2lhbyBk4buLY2ggRWFzeSBNLVBsdXMgQmFua2luZxAwNS8xMC8yMDEyIDE1OjI2ZAICD2QWAmYPFQQBMk9odHRwOi8vd3d3Lm9jZWFuYmFuay52bi9UaW4tVHVjLzkyMi9UaGFuaC10b2FuLW9ubGluZS0tLU5oYW4tbmdheS1xdWEtdGFuZy5odG1sLFRoYW5oIHRvw6FuIG9ubGluZSAtIE5o4bqtbiBuZ2F5IHF1w6AgdOG6t25nEDAzLzEwLzIwMTIgMDg6MjdkAgMPZBYCZg8VBAEzbWh0dHA6Ly93d3cub2NlYW5iYW5rLnZuL1Rpbi1UdWMvOTIxL0NodW9uZy10cmluaC1raHV5ZW4tbWFpLeKAnENodXllbi10aWVuLXF1YS10aGUtLS12dWEtcmUtdnVhLW5oYW5o4oCdLmh0bWxXQ2jGsMahbmcgdHLDrG5oIGtodXnhur9uIG3huqFpIOKAnENodXnhu4NuIHRp4buBbiBxdWEgdGjhursgLSB24burYSBy4bq7IHbhu6thIG5oYW5o4oCdEDAxLzEwLzIwMTIgMTY6MjVkAgQPZBYCZg8VBAE0XWh0dHA6Ly93d3cub2NlYW5iYW5rLnZuL1Rpbi1UdWMvOTE4L0NodW9uZy10cmluaC1raHV5ZW4tbWFpLeKAnE5oYW4tZG9pLXN1LXNhbmctdHJvbmfigJ0uaHRtbEJDaMawxqFuZyB0csOsbmgga2h1eeG6v24gbeG6oWkg4oCcTmjDom4gxJHDtGkgc+G7sSBzYW5nIHRy4buNbmfigJ0QMjcvMDkvMjAxMiAwOTowMWQCBQ9kFgJmDxUEATVaaHR0cDovL3d3dy5vY2VhbmJhbmsudm4vVGluLVR1Yy85MTcvVGhvbmctYmFvLW1vaS1ob3AtRGFpLWhvaS1kb25nLUNvLWRvbmctYmF0LXRodW9uZy5odG1s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"));
		list.add(new BasicNameValuePair("ctl00$TopSearch$inputSear...", ""));
		list.add(new BasicNameValuePair("ctl00$webPartManager$wp785291999$wp2121929887$DropDownListChiNhanh", "3"));
		list.add(new BasicNameValuePair("ctl00$webPartManager$wp78...", "1"));
		list.add(new BasicNameValuePair("ctl00$webPartManager$wp78...","Tìm kiếm"));
		list.add(new BasicNameValuePair("ctl00$webPartManager$wp83...", "1"));
		post.setEntity(new UrlEncodedFormEntity(list));
		HttpResponse res = client.execute(post);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		//CrawlerUtil.analysis(reader.getDocument(), "");
		String xpath__weather_td = "//select[@id='ctl00_webPartManager_wp785291999_wp2121929887_DropDownListCity']/option";
		NodeList linkNodes = (NodeList) reader.read(xpath__weather_td,XPathConstants.NODESET);
		int i = 1;
		ProvinceDAO provinceDAO = new ProvinceDAO();
		if (linkNodes != null) {
			while (i <= linkNodes.getLength()) {
				City city = new City();
				String xpath_province = xpath__weather_td + "[" + i + "]/text()";
				String province = (String) reader.read(xpath_province,XPathConstants.STRING);
				System.out.println(i + "  " + province.trim());
				String xpath_province_id = xpath__weather_td + "[" + i + "]/@value";
				String province_value = (String) reader.read(xpath_province_id,XPathConstants.STRING);
				System.out.println(i + "  " + province_value.trim());
				
				
				city = provinceDAO.getProvinceByName(province.trim());
				if(city!=null){
					city.oceanbank_id = Integer.parseInt(province_value);
					provinceDAO.updateCity10hOceanBank(city);
				}
				
				i++;
			}
		}

		FileUtil.writeToFile("d:/kplus.html", html, false);
	}

	
	public void crawlerProvinceMBBank() throws Exception {
		DefaultHttpClient client = HttpClientFactory.getInstance();
		client.getParams().setParameter("application/x-www-form-urlencoded",false);
		HttpGet get = new HttpGet("http://www.mbbank.com.vn/mangluoi/Lists/ATM/AllItems.aspx?FilterField1=TinhThanhPho&FilterValue1=B%C3%ACnh+%C4%90%E1%BB%8Bnh");
		HttpResponse res = client.execute(get);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		String xpath__weather_td = "//SELECT[@id='ctl00_m_g_dd2d90e7_843f_47d4_97ff_c3a6dd1c63b4_ctl01_DropDownList_Field']/OPTION";
		String xpath__district = "//SELECT[@id='ctl00_m_g_dd2d90e7_843f_47d4_97ff_c3a6dd1c63b4_ctl01_DropDownList_Field2']/OPTION";
		NodeList linkNodes = (NodeList) reader.read(xpath__weather_td,XPathConstants.NODESET);
		int i = 1,j=1;
		ProvinceDAO provinceDAO = new ProvinceDAO();
		if (linkNodes != null) {
			while (i <= linkNodes.getLength()) {
				City city = new City();
				String xpath_province = xpath__weather_td + "[" + i + "]/text()";
				String province = (String) reader.read(xpath_province,XPathConstants.STRING);
				
				String xpath_province_id = xpath__weather_td + "[" + i + "]/@value";
				String province_value = (String) reader.read(xpath_province_id,XPathConstants.STRING);
				
				city = provinceDAO.getProvinceByName(province.trim());
				if(city!=null){
					client = HttpClientFactory.getInstance();
					client.getParams().setParameter("application/x-www-form-urlencoded",false);
					get = new HttpGet("http://www.mbbank.com.vn/mangluoi/Lists/ATM/AllItems.aspx"+province_value);
					res = client.execute(get);
					html = HttpClientUtil.getResponseBody(res);
					XPathReader reader_2 = CrawlerUtil.createXPathReaderByData(html);
					NodeList nodeDistricts = (NodeList) reader.read(xpath__district,XPathConstants.NODESET);
					j=2;
					FileUtil.writeToFile("d:/kplus.html", html, false);
					System.out.println("SL--------"+nodeDistricts.getLength());
					while(j<30){
						String xpath_district = xpath__district + "[" + j + "]/text()";
						String district = (String) reader_2.read(xpath_district,XPathConstants.STRING);
						String xpath_district_value = xpath__district + "[" + j + "]/@value";
						String value = (String) reader_2.read(xpath_district_value,XPathConstants.STRING);
						//replaceFirst("Quận", "")
						City city2 = provinceDAO.getProvinceByName(district.trim());
					
						if(city2!=null){
							city2.mbbank_id = value;
							provinceDAO.updateCity10hMBBank(city2);
							//System.out.println(j + " --------- " + district.trim());
							//System.out.println(j + " ---------  " + value.trim());
						}else{
							System.out.println(j + " --------- " + district.trim());
							System.out.println(j + " ---------  " + value.trim());
						}
						j++;
					}
					city.mbbank_id = province_value;
					provinceDAO.updateCity10hMBBank(city);
				}else {
					System.out.println(i + "  " + province.trim());
					System.out.println(i + "  " + province_value.trim());
				}
				i++;
			}
		}

		
	}
	
	
	public  void crawlerATMMBBank() {	
		try {
			ProvinceDAO provinceDAO = new ProvinceDAO();
			List<City> provinces = provinceDAO.getProvinceByMBBank();
			for (City city : provinces) {
					List<City> districts = provinceDAO.getDistrictByMBBank(city.id);
					if(districts!=null&&districts.size()>0)
					{
						for (City city2 : districts) {
							System.out.println(city2.name);			
							crawlerMBBank(city.id,city2.id,city2.mbbank_id);
						}
					}else{
						crawlerMBBank(city.id,0,city.mbbank_id+"");
					}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void crawlerProvinceBIDV() throws Exception {
		DefaultHttpClient client = HttpClientFactory.getInstance();
		client.getParams().setParameter("application/x-www-form-urlencoded",true);
		HttpPost post = new HttpPost("http://bidv.com.vn/chinhanh/ATM.aspx");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("__ASYNCPOST", "true"));
		list.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
		list.add(new BasicNameValuePair("__EVENTTARGET", "plcRoot$Layout$zoneMenu$PagePlaceholder$PagePlaceholder$Layout$zoneContent$pageplaceholder$pageplaceholder$Layout$zoneContent$DSATM$ddlTinh"));
		list.add(new BasicNameValuePair("__LASTFOCUS", ""));
		list.add(new BasicNameValuePair("__SCROLLPOSITIONX","0"));
		list.add(new BasicNameValuePair("__SCROLLPOSITIONY", "0"));
		list.add(new BasicNameValuePair("__VIEWSTATE", "/wEPDwUKMjExOTgxNDYyMA9kFgICARBkZBYEAgMPZBYCZg9kFgJmD2QWCAIDD2QWAmYPZBYCZg9kFgICBQ8QZGQWAWZkAgUPZBYCZg9kFgYCAQ8WAh4EVGV4dAWMATxsaT48YSByZWw9InN1YiIgaHJlZj0ifi9kZWZhdWx0LmFzcHgiIHN0eWxlPSIiIGNsYXNzPSIiPjxzcGFuIGNsYXNzPSJpdGVtX3JpZ2h0Ij48c3BhbiBjbGFzcz0iaXRlbV9sZWZ0Ij5UcmFuZyBjaOG7pzwvc3Bhbj48L3NwYW4+PC9hPjwvbGk+ZAIDDxYCHgtfIUl0ZW1Db3VudAIGFgxmD2QWAmYPFQMDMzA4EH4vR2lvaXRoaWV1LmFzcHgOR2nhu5tpIHRoaeG7h3VkAgEPZBYCZg8VAwUyMzE4MhF+L05oYS1kYXUtdHUuYXNweA9OaMOgIMSR4bqndSB0xrBkAgIPZBYCZg8VAwMzMTcUfi9TYW5waGFtZGljaHZ1LmFzcHgaU+G6o24gcGjhuqltIC0gROG7i2NoIHbhu6VkAgMPZBYCZg8VAwMzMDkWfi9UaW4tdHVjLXN1LWtpZW4uYXNweBdUaW4gdOG7qWMgLSBT4buxIGtp4buHbmQCBA9kFgJmDxUDAzMxMA9+L2NoaW5oYW5oLmFzcHgOTeG6oW5nIGzGsOG7m2lkAgUPZBYCZg8VAwQxNjA2En4vTmdoZS1uZ2hpZXAuYXNweA5UdXnhu4NuIGThu6VuZ2QCBQ8WAh8ABYkOPGRpdiBzdHlsZT0iZGlzcGxheTogbm9uZTsiIGNsYXNzPSJ0YWJjb250ZW50IiBpZD0iMzA4Ij48YSBocmVmPSIvR2lvaXRoaWV1L0dpb2ktdGhpZXUtY2h1bmcuYXNweCI+R2nhu5tpIHRoaeG7h3UgY2h1bmc8L2E+PGEgaHJlZj0iL0dpb2l0aGlldS9MaWNoLXN1LXBoLS0yMjU7dC10cmllbi5hc3B4Ij5M4buLY2ggc+G7rSBwaCYjMjI1O3QgdHJp4buDbjwvYT48L2Rpdj48ZGl2IHN0eWxlPSJkaXNwbGF5OiBub25lOyIgY2xhc3M9InRhYmNvbnRlbnQiIGlkPSIyMzE4MiI+PGEgaHJlZj0ifi9OaGEtZGF1LXR1L1RvbmctcXVhbi12ZS1CSURWLmFzcHgiPlThu5VuZyBxdWFuIHbhu4EgQklEVjwvYT48YSBocmVmPSJ+L05oYS1kYXUtdHUvQmFvLWNhby10YWktY2hpbmguYXNweCI+QsOhbyBjw6FvIHTDoGkgY2jDrW5oPC9hPjxhIGhyZWY9In4vTmhhLWRhdS10dS9EaWV1LWxlLXZhLXF1YW4tdHJpLW5nYW4taGFuZy5hc3B4Ij7EkGnhu4F1IGzhu4cgdsOgIHF14bqjbiB0cuG7iyBuZ8OibiBow6BuZzwvYT48YSBocmVmPSJ+L05oYS1kYXUtdHUvVGhvbmctdGluLWRhbmgtY2hvLW5oYS1kYXUtdHUuYXNweCI+VGjDtG5nIHRpbiBkw6BuaCBjaG8gbmjDoCDEkeG6p3UgdMawPC9hPjxhIGhyZWY9In4vTmhhLWRhdS10dS9Ib2ktZGFwLWxpZW4taGUuYXNweCI+SOG7j2kgxJHDoXAmbGnDqm4gaOG7hzwvYT48L2Rpdj48ZGl2IHN0eWxlPSJkaXNwbGF5OiBub25lOyIgY2xhc3M9InRhYmNvbnRlbnQiIGlkPSIzMTciPjxhIGhyZWY9In4vU2FucGhhbWRpY2h2dS9raGFjaGhhbmdjYW5oYW4uYXNweCI+S2jDoWNoIGjDoG5nIGPDoSBuaMOibjwvYT48YSBocmVmPSJ+L1NhbnBoYW1kaWNodnUvS2hhY2hoYW5nZG9hbmhuZ2hpZXAuYXNweCI+S2jDoWNoIGjDoG5nIGRvYW5oIG5naGnhu4dwPC9hPjxhIGhyZWY9In4vU2FucGhhbWRpY2h2dS9EaW5oLWNoZS10YWktY2hpbmguYXNweCI+xJDhu4tuaCBjaOG6vyB0w6BpIGNow61uaDwvYT48L2Rpdj48ZGl2IHN0eWxlPSJkaXNwbGF5OiBub25lOyIgY2xhc3M9InRhYmNvbnRlbnQiIGlkPSIzMDkiPjxhIGhyZWY9In4vVGluLXR1Yy1zdS1raWVuL1Rpbi1CSURWLmFzcHgiPlRpbiBCSURWPC9hPjxhIGhyZWY9In4vVGluLXR1Yy1zdS1raWVuL1Rob25nLXRpbi1iYW8tY2hpLmFzcHgiPlRow7RuZyBjw6FvIGLDoW8gY2jDrTwvYT48YSBocmVmPSJ+L1Rpbi10dWMtc3Uta2llbi9UaG9uZy10aW4tdGFpLWNo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YW4taGFuZy1iYW4tbGUvQmlldS1waGkvUGhpLWRpY2gtdnUtbmdhbi1xdXkuYXNweChQaCYjMjM3OyBk4buLY2ggduG7pSBuZyYjMjI2O24gcXXhu7kgVk5EZAIFD2QWAmYPFQI7L05nYW4taGFuZy1iYW4tbGUvQmlldS1waGkvUGhpLWdpYW8tZGljaC10YWkta2hvYW4tVVNELmFzcHgyUGgmIzIzNzsgROG7i2NoIHbhu6UgdCYjMjI0O2kga2hv4bqjbiBuZ2/huqFpIHThu4dkAgYPZBYCZg8VAjMvTmdhbi1oYW5nLWJhbi1sZS9CaWV1LXBoaS9QaGktY2h1eWVuLXRpZW4tVVNELmFzcHglUGgmIzIzNzsgY2h1eeG7g24gdGnhu4FuIG5nb+G6oWkgdOG7h2QCBw9kFgJmDxUCMC9OZ2FuLWhhbmctYmFuLWxlL0JpZXUtcGhpL1BoaS1iYW8tbGFuaC1VU0QuYXNweCVQaCYjMjM3OyBi4bqjbyBsJiMyMjc7bmggbmdv4bqhaSB04buHZAIID2QWAmYPFQI4L05nYW4taGFuZy1iYW4tbGUvQmlldS1waGkvUGhpLWRpY2gtdnUtbmdhbi1xdXktVVNELmFzcHgxUGgmIzIzNzsgZOG7i2NoIHbhu6UgbmcmIzIyNjtuIHF14bu5IG5nb+G6oWkgdOG7h2QCCQ9kFgJmDxUCQS9OZ2FuLWhhbmctYmFuLWxlL0JpZXUtcGhpL0JpZXUtcGgtLTIzNzstRGljaC12dS1CSURWLU1vYmlsZS5hc3B4J0Jp4buDdSBwaCYjMjM3OyBE4buLY2ggduG7pSBCSURWIE1vYmlsZWQCCg9kFgJmDxUCUi9OZ2FuLWhhbmctYmFuLWxlL0JpZXUtcGhpL0JpZXUtcGgtLTIzNzstRGljaC12dS1CSURWLUJ1c2luZXNzLU9ubGluZS1jaG8tS2gtLmFzcHhYQmnhu4N1IHBoJiMyMzc7IEThu4tjaCB24bulIEJJRFYgQnVzaW5lc3MgT25saW5lIGNobyBLaCYjMjI1O2NoIGgmIzIyNDtuZyBEb2FuaCBuZ2hp4buHcGQCAQ8WAh8BAhIWJAIBD2QWBgIBDxYCHwAFFFVTRCAgICAgICAgICAgICAgICAgZAIDDxYCHwAFCTIwLjg0MCwwMGQCBQ8W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AAUCMiVkAgIPZBYCAgEPFgIfAAUDMTAlZAIID2QWBAIBDxYCHwAFCTI0IHRow6FuZ2QCAw8WAh8BAgIWBAIBD2QWAgIBDxYCHwAFAjIlZAICD2QWAgIBDxYCHwAFAzEwJWQCCQ9kFgQCAQ8WAh8ABQkzNiB0aMOhbmdkAgMPFgIfAQICFgQCAQ9kFgICAQ8WAh8ABQIyJWQCAg9kFgICAQ8WAh8ABQMxMCVkAgUPFgIfAAUKMTEvMDYvMjAxMmQCCQ9kFgJmD2QWAgIDDxYCHwAF9wU8ZGl2IGNsYXNzPSJtYXJxdWVlIiBpZD0ibXljcmF3bGVyMiI+PGEgaHJlZj0naHR0cDovL3d3dy5iaWMudm4vZnJvbnQtZW5kL2hvbWUuYXNwJyB0YXJnZXQ9J19ibGFuayc+PGltZyBzdHlsZT0iaGVpZ2h0OjQwcHg7IiBzcmM9Ii9BY2NvdW50aW5nL0dldEZpbGUyLmFzcHg/RmlsZV9JRD1FQUVVc01BMG95VndyaEMvZ0JvZUJiRmtoZVpXbXkrciIgIGJvcmRlcj0iMCIvPjwvYT48YSBocmVmPSdodHRwOi8vd3d3LnZhbGMuY29tLnZuL1ZpZXROYW0vSG9tZS8nIHRhcmdldD0nX2JsYW5rJz48aW1nIHN0eWxlPSJoZWlnaHQ6NDBweDsiIHNyYz0iL0FjY291bnRpbmcvR2V0RmlsZTIuYXNweD9GaWxlX0lEPUVBRVVzTUEwb3lWd3JoQy9nQm9lQmNTcHB5bTdWZFovIiAgYm9yZGVyPSIwIi8+PC9hPjwvZGl2PjxzY3JpcHQgdHlwZT0idGV4dC9qYXZhc2NyaXB0Ij4KbWFycXVlZUluaXQoewp1bmlxdWVpZDogJ215Y3Jhd2xlcjInLApzdHlsZTogewoncGFkZGluZyc6ICcycHgnLAond2lkdGgnOiAnNjAwcHgnLAonaGVpZ2h0JzogJzQwcHgnCn0sCmluYzogNSwgLy9zcGVlZCAtIHBpeGVsIGluY3JlbWVudCBmb3IgZWFjaCBpdGVyYXRpb24gb2YgdGhpcyBtYXJxdWVlJ3MgbW92ZW1lbnQKbW91c2U6ICdjdXJzb3IgZHJpdmVuJywgLy9tb3VzZW92ZXIgYmVoYXZpb3IgKCdwYXVzZScgJ2N1cnNvciBkcml2ZW4nIG9yIGZhbHNlKQptb3ZlYXRsZWFzdDogMiwKbmV1dHJhbDogMTUwLApzYXZlZGlyZWN0aW9uOiB0cnVlCn0pOwo8L3NjcmlwdD5kAgUPZBYCZg8PFgIfBmhkZBgDBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WAQU1cGxjUm9vdCRMYXlvdXQkem9uZVNlYXJjaCRjbXNzZWFyY2hib3gkYnRuSW1hZ2VCdXR0b24FFHZpZXdTdGF0ZSRncmlkU3RhdGVzD2dkBRRsb2dRdWVyeSRncmlkUXVlcmllcw9nZA=="));
		list.add(new BasicNameValuePair("lng", "vi-VN"));
		list.add(new BasicNameValuePair("manScript","plcRoot$Layout$zoneMenu$PagePlaceholder$PagePlaceholder$Layout$zoneContent$pageplaceholder$pageplaceholder$Layout$zoneContent$DSATM$UpdatePanel1|plcRoot$Layout$zoneMenu$PagePlaceholder$PagePlaceholder$Layout$zoneContent$pageplaceholder$pageplaceholder$Layout$zoneContent$DSATM$ddlTinh"));
		list.add(new BasicNameValuePair("plcRoot$Layout$zoneMenu$P...","0"));
		list.add(new BasicNameValuePair("plcRoot$Layout$zoneMenu$PagePlaceholder$PagePlaceholder$Layout$zoneContent$pageplaceholder$pageplaceholder$Layout$zoneContent$DSATM$ddlTinh", "436"));
		list.add(new BasicNameValuePair("plcRoot$Layout$zoneMenu$P...", "0"));
		list.add(new BasicNameValuePair("plcRoot$Layout$zoneMenu$P...", "1"));
		list.add(new BasicNameValuePair("plcRoot$Layout$zoneSearch...", "Tìm kiếm"));
		post.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		post.addHeader("Accept-Encoding", "gzip, deflate");
		post.addHeader("Accept-Language", "en-us,en;q=0.5");
		post.addHeader("Cache-Control","no-cache, no-cache");
		post.addHeader("Connection","keep-alive");
		//post.addHeader("Content-Length","13799");
		post.addHeader("Content-Type","application/x-www-form-urlencoded; charset=utf-8");
		post.addHeader("Cookie","CMSPreferredCulture=vi-VN; VisitorStatus=2; ViewMode=0; CurrentVisitStatus=2; ASP.NET_SessionId=fsz5wmmmwjomzvat2hovs5r0");
		post.addHeader("Host","www.bidv.com.vn");
		post.addHeader("Referer","http://www.bidv.com.vn/chinhanh/ATM.aspx");
		post.addHeader("Pragma", "no-cache");		
		post.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; rv:15.0) Gecko/20100101 Firefox/15.0.1");
		post.addHeader("X-MicrosoftAjax","Delta=true");		
		post.setEntity(new UrlEncodedFormEntity(list));
		HttpResponse res = client.execute(post);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		//CrawlerUtil.analysis(reader.getDocument(), "");
		String xpath_select = "//SELECT[@id='plcRoot_Layout_zoneMenu_PagePlaceholder_PagePlaceholder_Layout_zoneContent_pageplaceholder_pageplaceholder_Layout_zoneContent_DSATM_ddlTinh']/OPTION";
		String xpath_select_district = "//SELECT[@id='plcRoot_Layout_zoneMenu_PagePlaceholder_PagePlaceholder_Layout_zoneContent_pageplaceholder_pageplaceholder_Layout_zoneContent_DSATM_ddlHuyen']/OPTION";
		NodeList linkNodes = (NodeList) reader.read(xpath_select,XPathConstants.NODESET);
		int i = 2;
		ProvinceDAO provinceDAO = new ProvinceDAO();
		
		if (linkNodes != null) {
			while (i <=linkNodes.getLength()) {
				com.az24.crawler.model.City city = new com.az24.crawler.model.City();
				String xpath_name =xpath_select+"["+i+"]/text()";
				String name = (String) reader.read(xpath_name,XPathConstants.STRING);
			
				String xpath_value =xpath_select+"["+i+"]/@value";
				String value = (String) reader.read(xpath_value,XPathConstants.STRING);
				
				System.out.println(i +"  " + name.trim() + "  " + value.trim());
				city = provinceDAO.getProvinceByName(name.trim());
				//if("Hà Nội (Hà Tây)".equalsIgnoreCase(name)) city = provinceDAO.getProvinceByID(142);
				if(city!=null){
					city.bidvbank_id = Integer.parseInt(value);
					provinceDAO.updateCityBidvbank10h(city);
				}else{
					//System.out.println(i +"  " + name.trim() + "  " + value.trim());
				}
				
				
				client.getParams().setParameter("application/x-www-form-urlencoded",true);
				post = new HttpPost("http://bidv.com.vn/chinhanh/ATM.aspx");
				list = new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("__ASYNCPOST", "true"));
				list.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
				list.add(new BasicNameValuePair("__EVENTTARGET", "plcRoot$Layout$zoneMenu$PagePlaceholder$PagePlaceholder$Layout$zoneContent$pageplaceholder$pageplaceholder$Layout$zoneContent$DSATM$ddlTinh"));
				list.add(new BasicNameValuePair("__LASTFOCUS", ""));
				list.add(new BasicNameValuePair("__SCROLLPOSITIONX","0"));
				list.add(new BasicNameValuePair("__SCROLLPOSITIONY", "0"));
				list.add(new BasicNameValuePair("__VIEWSTATE", "/wEPDwUKMjExOTgxNDYyMA9kFgICARBkZBYEAgMPZBYCZg9kFgJmD2QWCAIDD2QWAmYPZBYCZg9kFgICBQ8QZGQWAWZkAgUPZBYCZg9kFgYCAQ8WAh4EVGV4dAWMATxsaT48YSByZWw9InN1YiIgaHJlZj0ifi9kZWZhdWx0LmFzcHgiIHN0eWxlPSIiIGNsYXNzPSIiPjxzcGFuIGNsYXNzPSJpdGVtX3JpZ2h0Ij48c3BhbiBjbGFzcz0iaXRlbV9sZWZ0Ij5UcmFuZyBjaOG7pzwvc3Bhbj48L3NwYW4+PC9hPjwvbGk+ZAIDDxYCHgtfIUl0ZW1Db3VudAIGFgxmD2QWAmYPFQMDMzA4EH4vR2lvaXRoaWV1LmFzcHgOR2nhu5tpIHRoaeG7h3VkAgEPZBYCZg8VAwUyMzE4MhF+L05oYS1kYXUtdHUuYXNweA9OaMOgIMSR4bqndSB0xrBkAgIPZBYCZg8VAwMzMTcUfi9TYW5waGFtZGljaHZ1LmFzcHgaU+G6o24gcGjhuqltIC0gROG7i2NoIHbhu6VkAgMPZBYCZg8VAwMzMDkWfi9UaW4tdHVjLXN1LWtpZW4uYXNweBdUaW4gdOG7qWMgLSBT4buxIGtp4buHbmQCBA9kFgJmDxUDAzMxMA9+L2NoaW5oYW5oLmFzcHgOTeG6oW5nIGzGsOG7m2lkAgUPZBYCZg8VAwQxNjA2En4vTmdoZS1uZ2hpZXAuYXNweA5UdXnhu4NuIGThu6VuZ2QCBQ8WAh8ABYkOPGRpdiBzdHlsZT0iZGlzcGxheTogbm9uZTsiIGNsYXNzPSJ0YWJjb250ZW50IiBpZD0iMzA4Ij48YSBocmVmPSIvR2lvaXRoaWV1L0dpb2ktdGhpZXUtY2h1bmcuYXNweCI+R2nhu5tpIHRoaeG7h3UgY2h1bmc8L2E+PGEgaHJlZj0iL0dpb2l0aGlldS9MaWNoLXN1LXBoLS0yMjU7dC10cmllbi5hc3B4Ij5M4buLY2ggc+G7rSBwaCYjMjI1O3QgdHJp4buDbjwvYT48L2Rpdj48ZGl2IHN0eWxlPSJkaXNwbGF5OiBub25lOyIgY2xhc3M9InRhYmNvbnRlbnQiIGlkPSIyMzE4MiI+PGEgaHJlZj0ifi9OaGEtZGF1LXR1L1RvbmctcXVhbi12ZS1CSURWLmFzcHgiPlThu5VuZyBxdWFuIHbhu4EgQklEVjwvYT48YSBocmVmPSJ+L05oYS1kYXUtdHUvQmFvLWNhby10YWktY2hpbmguYXNweCI+QsOhbyBjw6FvIHTDoGkgY2jDrW5oPC9hPjxhIGhyZWY9In4vTmhhLWRhdS10dS9EaWV1LWxlLXZhLXF1YW4tdHJpLW5nYW4taGFuZy5hc3B4Ij7EkGnhu4F1IGzhu4cgdsOgIHF14bqjbiB0cuG7iyBuZ8OibiBow6BuZzwvYT48YSBocmVmPSJ+L05oYS1kYXUtdHUvVGhvbmctdGluLWRhbmgtY2hvLW5oYS1kYXUtdHUuYXNweCI+VGjDtG5nIHRpbiBkw6BuaCBjaG8gbmjDoCDEkeG6p3UgdMawPC9hPjxhIGhyZWY9In4vTmhhLWRhdS10dS9Ib2ktZGFwLWxpZW4taGUuYXNweCI+SOG7j2kgxJHDoXAmbGnDqm4gaOG7hzwvYT48L2Rpdj48ZGl2IHN0eWxlPSJkaXNwbGF5OiBub25lOyIgY2xhc3M9InRhYmNvbnRlbnQiIGlkPSIzMTciPjxhIGhyZWY9In4vU2FucGhhbWRpY2h2dS9raGFjaGhhbmdjYW5oYW4uYXNweCI+S2jDoWNoIGjDoG5nIGPDoSBuaMOibjwvYT48YSBocmVmPSJ+L1NhbnBoYW1kaWNodnUvS2hhY2hoYW5nZG9hbmhuZ2hpZXAuYXNweCI+S2jDoWNoIGjDoG5nIGRvYW5oIG5naGnhu4dwPC9hPjxhIGhyZWY9In4vU2FucGhhbWRpY2h2dS9EaW5oLWNoZS10YWktY2hpbmguYXNweCI+xJDhu4tuaCBjaOG6vyB0w6BpIGNow61uaDwvYT48L2Rpdj48ZGl2IHN0eWxlPSJkaXNwbGF5OiBub25lOyIgY2xhc3M9InRhYmNvbnRlbnQiIGlkPSIzMDkiPjxhIGhyZWY9In4vVGluLXR1Yy1zdS1raWVuL1Rpbi1CSURWLmFzcHgiPlRpbiBCSURWPC9hPjxhIGhyZWY9In4vVGluLXR1Yy1zdS1raWVuL1Rob25nLXRpbi1iYW8tY2hpLmFzcHgiPlRow7RuZyBjw6FvIGLDoW8gY2jDrTwvYT48YSBocmVmPSJ+L1Rpbi10dWMtc3Uta2llbi9UaG9uZy10aW4tdGFpLWNo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YW4taGFuZy1iYW4tbGUvQmlldS1waGkvUGhpLWRpY2gtdnUtbmdhbi1xdXkuYXNweChQaCYjMjM3OyBk4buLY2ggduG7pSBuZyYjMjI2O24gcXXhu7kgVk5EZAIFD2QWAmYPFQI7L05nYW4taGFuZy1iYW4tbGUvQmlldS1waGkvUGhpLWdpYW8tZGljaC10YWkta2hvYW4tVVNELmFzcHgyUGgmIzIzNzsgROG7i2NoIHbhu6UgdCYjMjI0O2kga2hv4bqjbiBuZ2/huqFpIHThu4dkAgYPZBYCZg8VAjMvTmdhbi1oYW5nLWJhbi1sZS9CaWV1LXBoaS9QaGktY2h1eWVuLXRpZW4tVVNELmFzcHglUGgmIzIzNzsgY2h1eeG7g24gdGnhu4FuIG5nb+G6oWkgdOG7h2QCBw9kFgJmDxUCMC9OZ2FuLWhhbmctYmFuLWxlL0JpZXUtcGhpL1BoaS1iYW8tbGFuaC1VU0QuYXNweCVQaCYjMjM3OyBi4bqjbyBsJiMyMjc7bmggbmdv4bqhaSB04buHZAIID2QWAmYPFQI4L05nYW4taGFuZy1iYW4tbGUvQmlldS1waGkvUGhpLWRpY2gtdnUtbmdhbi1xdXktVVNELmFzcHgxUGgmIzIzNzsgZOG7i2NoIHbhu6UgbmcmIzIyNjtuIHF14bu5IG5nb+G6oWkgdOG7h2QCCQ9kFgJmDxUCQS9OZ2FuLWhhbmctYmFuLWxlL0JpZXUtcGhpL0JpZXUtcGgtLTIzNzstRGljaC12dS1CSURWLU1vYmlsZS5hc3B4J0Jp4buDdSBwaCYjMjM3OyBE4buLY2ggduG7pSBCSURWIE1vYmlsZWQCCg9kFgJmDxUCUi9OZ2FuLWhhbmctYmFuLWxlL0JpZXUtcGhpL0JpZXUtcGgtLTIzNzstRGljaC12dS1CSURWLUJ1c2luZXNzLU9ubGluZS1jaG8tS2gtLmFzcHhYQmnhu4N1IHBoJiMyMzc7IEThu4tjaCB24bulIEJJRFYgQnVzaW5lc3MgT25saW5lIGNobyBLaCYjMjI1O2NoIGgmIzIyNDtuZyBEb2FuaCBuZ2hp4buHcGQCAQ8WAh8BAhIWJAIBD2QWBgIBDxYCHwAFFFVTRCAgICAgICAgICAgICAgICAgZAIDDxYCHwAFCTIwLjg0MCwwMGQCBQ8W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AAUCMiVkAgIPZBYCAgEPFgIfAAUDMTAlZAIID2QWBAIBDxYCHwAFCTI0IHRow6FuZ2QCAw8WAh8BAgIWBAIBD2QWAgIBDxYCHwAFAjIlZAICD2QWAgIBDxYCHwAFAzEwJWQCCQ9kFgQCAQ8WAh8ABQkzNiB0aMOhbmdkAgMPFgIfAQICFgQCAQ9kFgICAQ8WAh8ABQIyJWQCAg9kFgICAQ8WAh8ABQMxMCVkAgUPFgIfAAUKMTEvMDYvMjAxMmQCCQ9kFgJmD2QWAgIDDxYCHwAF9wU8ZGl2IGNsYXNzPSJtYXJxdWVlIiBpZD0ibXljcmF3bGVyMiI+PGEgaHJlZj0naHR0cDovL3d3dy5iaWMudm4vZnJvbnQtZW5kL2hvbWUuYXNwJyB0YXJnZXQ9J19ibGFuayc+PGltZyBzdHlsZT0iaGVpZ2h0OjQwcHg7IiBzcmM9Ii9BY2NvdW50aW5nL0dldEZpbGUyLmFzcHg/RmlsZV9JRD1FQUVVc01BMG95VndyaEMvZ0JvZUJiRmtoZVpXbXkrciIgIGJvcmRlcj0iMCIvPjwvYT48YSBocmVmPSdodHRwOi8vd3d3LnZhbGMuY29tLnZuL1ZpZXROYW0vSG9tZS8nIHRhcmdldD0nX2JsYW5rJz48aW1nIHN0eWxlPSJoZWlnaHQ6NDBweDsiIHNyYz0iL0FjY291bnRpbmcvR2V0RmlsZTIuYXNweD9GaWxlX0lEPUVBRVVzTUEwb3lWd3JoQy9nQm9lQmNTcHB5bTdWZFovIiAgYm9yZGVyPSIwIi8+PC9hPjwvZGl2PjxzY3JpcHQgdHlwZT0idGV4dC9qYXZhc2NyaXB0Ij4KbWFycXVlZUluaXQoewp1bmlxdWVpZDogJ215Y3Jhd2xlcjInLApzdHlsZTogewoncGFkZGluZyc6ICcycHgnLAond2lkdGgnOiAnNjAwcHgnLAonaGVpZ2h0JzogJzQwcHgnCn0sCmluYzogNSwgLy9zcGVlZCAtIHBpeGVsIGluY3JlbWVudCBmb3IgZWFjaCBpdGVyYXRpb24gb2YgdGhpcyBtYXJxdWVlJ3MgbW92ZW1lbnQKbW91c2U6ICdjdXJzb3IgZHJpdmVuJywgLy9tb3VzZW92ZXIgYmVoYXZpb3IgKCdwYXVzZScgJ2N1cnNvciBkcml2ZW4nIG9yIGZhbHNlKQptb3ZlYXRsZWFzdDogMiwKbmV1dHJhbDogMTUwLApzYXZlZGlyZWN0aW9uOiB0cnVlCn0pOwo8L3NjcmlwdD5kAgUPZBYCZg8PFgIfBmhkZBgDBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WAQU1cGxjUm9vdCRMYXlvdXQkem9uZVNlYXJjaCRjbXNzZWFyY2hib3gkYnRuSW1hZ2VCdXR0b24FFHZpZXdTdGF0ZSRncmlkU3RhdGVzD2dkBRRsb2dRdWVyeSRncmlkUXVlcmllcw9nZA=="));
				list.add(new BasicNameValuePair("lng", "vi-VN"));
				list.add(new BasicNameValuePair("manScript","plcRoot$Layout$zoneMenu$PagePlaceholder$PagePlaceholder$Layout$zoneContent$pageplaceholder$pageplaceholder$Layout$zoneContent$DSATM$UpdatePanel1|plcRoot$Layout$zoneMenu$PagePlaceholder$PagePlaceholder$Layout$zoneContent$pageplaceholder$pageplaceholder$Layout$zoneContent$DSATM$ddlTinh"));
				list.add(new BasicNameValuePair("plcRoot$Layout$zoneMenu$P...","0"));
				list.add(new BasicNameValuePair("plcRoot$Layout$zoneMenu$PagePlaceholder$PagePlaceholder$Layout$zoneContent$pageplaceholder$pageplaceholder$Layout$zoneContent$DSATM$ddlTinh", value));
				list.add(new BasicNameValuePair("plcRoot$Layout$zoneMenu$P...", "0"));
				list.add(new BasicNameValuePair("plcRoot$Layout$zoneMenu$P...", "1"));
				list.add(new BasicNameValuePair("plcRoot$Layout$zoneSearch...", "Tìm kiếm"));
				post.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
				post.addHeader("Accept-Encoding", "gzip, deflate");
				post.addHeader("Accept-Language", "en-us,en;q=0.5");
				post.addHeader("Cache-Control","no-cache, no-cache");
				post.addHeader("Connection","keep-alive");
				//post.addHeader("Content-Length","13799");
				post.addHeader("Content-Type","application/x-www-form-urlencoded; charset=utf-8");
				post.addHeader("Cookie","CMSPreferredCulture=vi-VN; VisitorStatus=2; ViewMode=0; CurrentVisitStatus=2; ASP.NET_SessionId=fsz5wmmmwjomzvat2hovs5r0");
				post.addHeader("Host","www.bidv.com.vn");
				post.addHeader("Referer","http://www.bidv.com.vn/chinhanh/ATM.aspx");
				post.addHeader("Pragma", "no-cache");		
				post.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; rv:15.0) Gecko/20100101 Firefox/15.0.1");
				post.addHeader("X-MicrosoftAjax","Delta=true");		
				post.setEntity(new UrlEncodedFormEntity(list));
				res = client.execute(post);
				html = HttpClientUtil.getResponseBody(res);
				XPathReader reader_2 = CrawlerUtil.createXPathReaderByData(html);
				NodeList districtnodes = (NodeList) reader_2.read(xpath_select_district,XPathConstants.NODESET);
				int j = 2;
				while(j<=districtnodes.getLength())
				{
					String xpath_district_name =xpath_select_district+"["+j+"]/text()";
					String d_name = (String) reader_2.read(xpath_district_name,XPathConstants.STRING);
					String xpath_district_value =xpath_select_district+"["+j+"]/@value";
					String d_value = (String) reader_2.read(xpath_district_value,XPathConstants.STRING);
					//System.out.println("------------"+j +"  " + d_name.trim() + "  " + d_value.trim());
					
					String pattern = "\\.";
				    Pattern r = Pattern.compile(pattern);
				    Matcher m = r.matcher(d_name);
				    if(m.find())
				    	  d_name = m.replaceAll("");
				    
				      
				    String d_name_1 =d_name.toLowerCase();
					d_name_1 = d_name_1.toLowerCase().replaceFirst("quận", "");
					d_name_1 = d_name_1.replaceFirst("huyện", "");
					d_name_1 = d_name_1.replaceFirst("tp", "");
					d_name_1 = d_name_1.replaceFirst("tx", "");
					
					City quan = provinceDAO.getDistrictByName(d_name_1);
					if(quan!=null){
						System.out.println(j+"---"+d_name_1+"--"+d_value);
						quan.bidvbank_id = Integer.parseInt(d_value);
						provinceDAO.updateCityBidvbank10h(quan);
					}else
					{
						
						if(city!=null){
							City city_2 = new City();
							city_2.name =StringUtil.trim(d_name);
							city_2.region=city.region;
							city_2.province_id = city.id;
							city_2.bidvbank_id = Integer.parseInt(d_value);
							city_2.create_date = new Date(Calendar.getInstance().getTimeInMillis());
							provinceDAO.saveCityBidvBank10h(city_2);
						}
					}
					
					j++;
				}
				
				i++;
			}
		}

		FileUtil.writeToFile("d:/kplus.html", html, false);
	}
	
	
	public void crawlerProvincePhuongDongBank() throws Exception {
		DefaultHttpClient client = HttpClientFactory.getInstance();
		client.getParams().setParameter("application/x-www-form-urlencoded",false);
		HttpGet get = new HttpGet("http://www.ocb.com.vn/listatm-lag-1-did-2-id-45.html");
		HttpResponse res = client.execute(get);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		CrawlerUtil.analysis(reader.getDocument(), "");
		String xpath_select = "//SELECT[@name='d_c_id']/OPTION";
		String xpath_select_district = "//SELECT[@name='a_p_id']/OPTION";
		NodeList linkNodes = (NodeList) reader.read(xpath_select,XPathConstants.NODESET);
		int i = 2;
		ProvinceDAO provinceDAO = new ProvinceDAO();
		
		if (linkNodes != null) {
			while (i <=linkNodes.getLength()) {
				com.az24.crawler.model.City city = new com.az24.crawler.model.City();
				String xpath_name =xpath_select+"["+i+"]/text()";
				String name = (String) reader.read(xpath_name,XPathConstants.STRING);
				String xpath_value =xpath_select+"["+i+"]/@value";
				String value = (String) reader.read(xpath_value,XPathConstants.STRING);
				//System.out.println(i +"  " + name.trim() + "  " + value.trim());
				city = provinceDAO.getProvinceByName(name.trim());
				if("Tp.Hồ Chí Minh".equalsIgnoreCase(name)) city = provinceDAO.getProvinceByID(118);
				if("Bà Bịa Vũng Tàu".equalsIgnoreCase(name)) city = provinceDAO.getProvinceByID(47);
				if("Đak lak".equalsIgnoreCase(name)) city = provinceDAO.getProvinceByID(80);
				
				
				if(city!=null){
					city.phuongdongbank_id = value;
					provinceDAO.updateCityPhuongDongBank10h(city);
				}else{
					//System.out.println(i +"  " + name.trim() + "  " + value.trim());
				}
				
				
				client = HttpClientFactory.getInstance();
				client.getParams().setParameter("application/x-www-form-urlencoded",false);
				get = new HttpGet("http://www.ocb.com.vn/listatm-lag-1-did-2-id-45.html");
				res = client.execute(get);
				html = HttpClientUtil.getResponseBody(res);
				XPathReader reader_2 = CrawlerUtil.createXPathReaderByData(html);
				NodeList districtnodes = (NodeList) reader_2.read(xpath_select_district,XPathConstants.NODESET);
				int j = 2;
				while(j<=districtnodes.getLength())
				{
					String xpath_district_name =xpath_select_district+"["+j+"]/text()";
					String d_name = (String) reader_2.read(xpath_district_name,XPathConstants.STRING);
					String xpath_district_value =xpath_select_district+"["+j+"]/@value";
					String d_value = (String) reader_2.read(xpath_district_value,XPathConstants.STRING);
					//System.out.println("------------"+j +"  " + d_name.trim() + "  " + d_value.trim());
					
					String pattern = "\\.";
				    Pattern r = Pattern.compile(pattern);
				    Matcher m = r.matcher(d_name);
				    if(m.find())
				    	  d_name = m.replaceAll("");
				    
				      
				    String d_name_1 =d_name.toLowerCase();
					d_name_1 = d_name_1.toLowerCase().replaceFirst("quận", "");
					d_name_1 = d_name_1.replaceFirst("huyện", "");
					d_name_1 = d_name_1.replaceFirst("tp", "");
					d_name_1 = d_name_1.replaceFirst("tx", "");
					
					City quan = provinceDAO.getDistrictByName(d_name_1);
					if(quan!=null){
						//System.out.println(j+"---"+d_name_1+"--"+d_value);
						quan.phuongdongbank_id = d_value;
						provinceDAO.updateCityPhuongDongBank10h(quan);
					}else
					{
						
						if(city!=null){
							City city_2 = new City();
							city_2.name =StringUtil.trim(d_name);
							city_2.region=city.region;
							city_2.province_id = city.id;
							city_2.phuongdongbank_id =d_value;
							city_2.create_date = new Date(Calendar.getInstance().getTimeInMillis());
							provinceDAO.updateCityPhuongDongBank10h(city_2);
						}
					}
					
					j++;
				}
				
				i++;
			}
		}

		FileUtil.writeToFile("d:/kplus.html", html, false);
	}
	
	
	public void crawlerProvinceAgriBank() throws Exception {
		DefaultHttpClient client = HttpClientFactory.getInstance();
		client.getParams().setParameter("application/x-www-form-urlencoded",false);
		HttpGet get = new HttpGet("http://www.agribank.com.vn/tim-kiem/atm/1147/375/0/ket-qua.aspx");
		HttpResponse res = client.execute(get);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		//CrawlerUtil.analysis(reader.getDocument(), "");
		String xpath_province_path = "//select[@id='cphMain_Map1_ddlTinh']/option";
		String xpath_d_path = "//SELECT[1]/OPTION";
		NodeList linkNodes = (NodeList) reader.read(xpath_province_path,XPathConstants.NODESET);
		int i = 2;
		ProvinceDAO provinceDAO = new ProvinceDAO();
		if (linkNodes != null) {
			while (i <= linkNodes.getLength()) {
				City city = new City();
				String xpath_province_name = xpath_province_path + "[" + i + "]/text()";
				String name = (String) reader.read(xpath_province_name,XPathConstants.STRING);
				String xpath_province_value = xpath_province_path + "[" + i + "]/@value";
				String value = (String) reader.read(xpath_province_value,XPathConstants.STRING);
				
				city = provinceDAO.getProvinceByName(name);
				if("Hà Nội (Hà Tây)".equalsIgnoreCase(name)) city = provinceDAO.getProvinceByID(142);
				
				if(city!=null){
					city.agribank_id = Integer.parseInt(value);
					provinceDAO.updateCityAgribank10h(city);
				}else{
					System.out.println(name+"--"+value);
				}
				
				HttpPost post = new HttpPost("http://www.agribank.com.vn/Layout/Pages/ListHuyen.aspx");
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("hcIdLangHuyen", ""));
				list.add(new BasicNameValuePair("hcIdLangTinh", value));
				list.add(new BasicNameValuePair("lang","1"));				
				post.setEntity(new UrlEncodedFormEntity(list));
				res = client.execute(post);
				FileUtil.writeToFile("d:/kplus.html", html, false);
			    html = HttpClientUtil.getResponseBody(res);
				XPathReader reader_2 = CrawlerUtil.createXPathReaderByData(html);
				//CrawlerUtil.analysis(reader_2.getDocument(), "");
				NodeList dlinkNodes = (NodeList) reader_2.read(xpath_d_path,XPathConstants.NODESET);
				int j = 2;
				while(j<=dlinkNodes.getLength())
				{
					String xpath_district_name = xpath_d_path + "[" + j + "]/text()";
					String d_name = (String) reader_2.read(xpath_district_name,XPathConstants.STRING);
					String xpath_distric_value = xpath_d_path + "[" + j + "]/@value";
					String d_value = (String) reader_2.read(xpath_distric_value,XPathConstants.STRING);
					
					String pattern = "\\.";
				    Pattern r = Pattern.compile(pattern);
				    Matcher m = r.matcher(d_name);
				    if(m.find())
				    	  d_name = m.replaceAll("");
				    
				      
				    String d_name_1 =d_name.toLowerCase();
					d_name_1 = d_name_1.toLowerCase().replaceFirst("quận", "");
					d_name_1 = d_name_1.replaceFirst("huyện", "");
					d_name_1 = d_name_1.replaceFirst("tp", "");
					d_name_1 = d_name_1.replaceFirst("tx", "");
					
					City quan = provinceDAO.getProvinceByName(d_name_1);
					if(quan!=null){
						quan.agribank_id = Integer.parseInt(d_value);
						provinceDAO.updateCityAgribank10h(quan);
					}else
					{
						System.out.println(j+"---"+d_name_1+"--"+d_value);
						if(city!=null){
							City city_2 = new City();
							city_2.name =StringUtil.trim(d_name);
							city_2.region=city.region;
							city_2.province_id = city.id;
							city_2.agribank_id = Integer.parseInt(d_value);
							city_2.create_date = new Date(Calendar.getInstance().getTimeInMillis());
							//provinceDAO.saveCity10h(city_2);
						}
					}
					j++;
				}
				i++;
			}
		}

		
	}
	
	public  void crawlerATMBIDVBank() {
		CrawlerDailyATM crawlerDailyTivi = new CrawlerDailyATM();
		try {
			ProvinceDAO provinceDAO = new ProvinceDAO();
			List<City> provinces = provinceDAO.getProvinceByBIDVBank();
			for (City city : provinces) {
					List<City> districts = provinceDAO.getDistrictByBIDVBank(city.id);
					if(districts!=null&&districts.size()>0)
					{
						for (City city2 : districts) {
							System.out.println(city2.name);				
							crawlerDailyTivi.crawlerBIDV(city.id,city2.id,city.bidvbank_id+"",city2.bidvbank_id+"");
						}
					}else{
						crawlerDailyTivi.crawlerBIDV(city.id,0,city.bidvbank_id+"","0");
					}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public  void crawlerATMPhuongDongBank() {
		CrawlerDailyATM crawlerDailyTivi = new CrawlerDailyATM();
		try {
			ProvinceDAO provinceDAO = new ProvinceDAO();
			List<City> provinces = provinceDAO.getProvinceByPhuongDongBank();
			for (City city : provinces) {
					List<City> districts = provinceDAO.getDistrictByPhuongDongBank(city.id);
					if(districts!=null&&districts.size()>0)
					{
						for (City city2 : districts) {
							System.out.println(city2.name);				
							crawlerDailyTivi.crawlerPhuongDongBank(city.id,city2.id, city2.phuongdongbank_id);
						}
					}else{
						crawlerDailyTivi.crawlerPhuongDongBank(city.id,0, city.phuongdongbank_id);
					}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	public static void main(String[] args) {
		CrawlerDailyATM crawlerDaily = new CrawlerDailyATM();
		try {
			//crawlerDaily.crawlerProvinceVietCombank();
			//crawlerDaily.crawlerATMVietcomBank();
			//crawlerDaily.crawlerATMDongA();
			//crawlerDaily.crawlerATMAnbinhBank();
			crawlerDaily.crawlerATMPhuongDongBank();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
