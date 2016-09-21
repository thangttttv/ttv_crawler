package com.vtc.crawler;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.util.html.parser.XPathReader;
import hdc.util.text.StringUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.xpath.XPathConstants;
import org.apache.http.HttpResponse;
import org.w3c.dom.NodeList;

import com.az24.crawler.model.Feed;
import com.az24.crawler.model.KQMB;
import com.az24.crawler.model.KQMN;
import com.az24.dao.W10HDAO;
import com.az24.test.HttpClientUtil;
public class CopyOfCrawlerDailyXoSoKQVS extends CrawlerDailyXoSo{
	
	public void crawlerXSMN() throws Exception {
		String url ="http://ketquaveso.com/ttkq/mien-nam.html?t=1400492037710";
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		System.out.println(html);
		String xpath__weather_td= "//UL[@class='list-col']/LI";
		NodeList linkNodes = (NodeList) reader.read(xpath__weather_td, XPathConstants.NODESET);
		NodeList linkNodeProvinces = (NodeList) reader.read("//UL[@class='gr-yellow clearfix']/LI", XPathConstants.NODESET);
		int i = 2;
		List<String> arrG8 = new ArrayList<String>();
		List<String> arrG7 = new ArrayList<String>();
		List<String> arrG6 = new ArrayList<String>();
		List<String> arrG5 = new ArrayList<String>();
		List<String> arrG4 = new ArrayList<String>();
		List<String> arrG3 = new ArrayList<String>();
		List<String> arrG2 = new ArrayList<String>();
		List<String> arrG1 = new ArrayList<String>();
		List<String> arrG0 = new ArrayList<String>();
		List<String> arrTinh = new ArrayList<String>();
		ArrayList<KQMN> listKQ = new ArrayList<KQMN>();
		
		// get Ngay Tuong Thuat
		String xpath__date= "HTML/BODY[1]/UL[1]/LI[1]/A[2]";
		String str_date_tt =  (String) reader.read(xpath__date, XPathConstants.STRING);
		str_date_tt = str_date_tt.replace("Ngày", "").trim();
		System.out.println(str_date_tt);
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date date_tt = formatter.parse(str_date_tt+" 23:59:00");
		
		java.util.Date currentDate = Calendar.getInstance().getTime();
		System.out.println("currentDate:"+currentDate);
	
		// Neu chua toi thoi gian tuong thuat
		if(currentDate.after(date_tt)){ 
			formatter = new SimpleDateFormat("dd/MM/yyyy");
			String ngay_quay_vn = formatter.format(currentDate);
			
			ArrayList<Integer> provinceIds = getProvinceOpenByRegio(3);
			for (Integer pid : provinceIds) {
				KQMN kqmn = initKQMienNamTheoTinh(pid);
				kqmn.province_id = pid;
				kqmn.ngay_quay = ngay_quay_vn;
				kqmn.province_name = getProvinceNameByMap(pid+"");
				listKQ.add(kqmn);
			}
			createFileJSONMN(listKQ,ngay_quay_vn);
			createFileTT_HTML_MN(listKQ);
			Thread.sleep(1000);
			return; 
		}
		
		
		if(linkNodeProvinces!=null)
		{
			while(i<=linkNodeProvinces.getLength())
			{
				String xpath_province = "//UL[@class='gr-yellow clearfix']/LI["+i+"]/SPAN/text()";
				String province_code =  (String) reader.read(xpath_province, XPathConstants.STRING);
				String code = province_code.replace("Mã:", "").trim();
				System.out.println("MT:"+province_code.replace("Mã:", "").trim());
				arrTinh.add(code);
				i++;
			}
		}
		
		W10HDAO w10hdao = new W10HDAO();
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		String ngay_quay = formatter.format(date_tt);
		System.out.println("ngay_quay:"+ngay_quay);
		
		boolean kq_check = true;
		for (String code : arrTinh) {
			int province_id = w10hdao.getProvinceIdByCode(code);
			int kq = w10hdao.checkKqMN(ngay_quay,province_id);
			kq_check = (kq==2?true:false)&kq_check;
		}
		
		// Neu da tuong thuat xong
		if(kq_check)return;
		
		i = 0;
		if(linkNodes!=null)
		{
			while(i<=linkNodes.getLength())
			{
				String xpath_province = "//UL[@class='list-col']/LI["+i+"]/LABEL/text()";
				String province =  (String) reader.read(xpath_province, XPathConstants.STRING);
				
				String xpath_rs = "//UL[@class='list-col']/LI["+i+"]/DIV[1]/SPAN";
				NodeList linkNodes2 = (NodeList) reader.read(xpath_rs, XPathConstants.NODESET);
				
				int j = 1;
				System.out.println(province+":");
				while(j<=linkNodes2.getLength())
				{
					String rs =  (String) reader.read(xpath_rs+"["+j+"]/STRONG/text()", XPathConstants.STRING);
					System.out.println(rs);
					switch (i){
						case 1:
							arrG8.add(rs); 	
							break;
						case 2:
							arrG7.add(rs); 	
							break;
						case 3:
							arrG6.add(rs); 	
							break;
						case 4:
							arrG5.add(rs); 	
							break;
						case 5:
							arrG4.add(rs); 	
							break;
						case 6:
							arrG3.add(rs); 	
							break;
						case 7:
							arrG2.add(rs); 	
							break;
						case 8:
							arrG1.add(rs); 	
							break;
						case 9:
							arrG0.add(rs); 	
							break;
					}
					j++;
				}
				i++;
			}
		}
		
		i = 0;int sotinh = arrTinh.size();
		
		for (String provinceCode : arrTinh) {
				KQMN kqmn = new KQMN();
				int province_id = w10hdao.getProvinceIdByCode(provinceCode);
				int kq = w10hdao.checkKqMN(ngay_quay, province_id);
				kqmn = getKQMienNamTheoTinh(provinceCode,i,sotinh,arrG0,arrG1,arrG2,arrG3,arrG4,arrG5,arrG6,arrG7,arrG8);
				kqmn.ngay_quay = ngay_quay;
				
				formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String ngay_quay_1 = formatter.format(currentDate);
				kqmn.create_date = ngay_quay_1;
				kqmn.modify_date = ngay_quay_1;
				kqmn.create_user ="crawler";
				kqmn.modify_user ="crawler";
				kqmn.province_id = province_id;
				kqmn.province_name = getProvinceNameByMap(province_id+"");
				
				listKQ.add(kqmn);
				
				if(kq==0)
					w10hdao.saveKqMienNam(kqmn);
				else
					if(kq==1)
					w10hdao.updateKqMienNam(kqmn);
				i++;
		}
		formatter = new SimpleDateFormat("dd/MM/yyyy");
		String ngay_quay_vn = formatter.format(date_tt);
		createFileJSONMN(listKQ,ngay_quay_vn);
		createFileTT_HTML_MN(listKQ);
		
		i = 0;
		String titleFeedKQ = "";
		while(i<listKQ.size()){
			if(!StringUtil.isEmpty(listKQ.get(i).giai_dacbiet)){
				titleFeedKQ+=listKQ.get(i).province_name.toUpperCase()+" Giai DB: "+listKQ.get(i).giai_dacbiet+" ";
			}
			i++;
		}
		
		if(!StringUtil.isEmpty(titleFeedKQ)){
			com.az24.crawler.model.Feed feed = new Feed();
		    feed.title = "KQXS Miền Nam ngày "+ngay_quay+": "+titleFeedKQ;
			feed.image = "http://10h.vn/themes/images/xo-so-10h-icon-NOTE.png";
			feed.url ="http://10h.vn/ket-qua-xo-so-mien-nam.html";
			feed.type =3; //"TK_12BOSORAIT_MB";
			feed.isMobile = 0;
			feed.create_user ="thongke";
			w10hdao.deleteFeedByType(3);
			w10hdao.saveFeed(feed);
			
			feed.image = "http://10h.vn/themes/images/xo-so-10h-icon-NOTE.png";
			feed.url ="http://m.10h.vn/ket-qua-xo-so-mien-nam.html";
			feed.isMobile = 1;
			w10hdao.saveFeed(feed);
			
			ArrayList<Feed> feeds = new ArrayList<Feed>();
			feeds = w10hdao.getNewFeed(0);
			createFileFeed(feeds);
			
			feeds = new ArrayList<Feed>();
			feeds = w10hdao.getNewFeed(1);
			createFileFeedMobile(feeds);
		}
		
	}
	
	public void initFileTTMN() throws ParseException{
		ArrayList<KQMN> listKQ = new ArrayList<KQMN>();
		java.util.Date currentDate = Calendar.getInstance().getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String ngay_quay = formatter.format(currentDate);
		
		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Date date1MN = formatter.parse(ngay_quay+" 00:00:00");
		
		Date date2MN = formatter.parse(ngay_quay+" 16:00:00");
		
		// Neu chua toi thoi gian tuong thuat
		if(currentDate.after(date1MN)&&date2MN.after(currentDate)){
			formatter = new SimpleDateFormat("dd/MM/yyyy");
			String ngay_quay_vn = formatter.format(currentDate);
			
			ArrayList<Integer> provinceIds = getProvinceOpenByRegio(3);
			for (Integer pid : provinceIds) {
				KQMN kqmn = initKQMienNamTheoTinh(pid);
				kqmn.province_id = pid;
				kqmn.ngay_quay = ngay_quay_vn;
				kqmn.province_name = getProvinceNameByMap(pid+"");
				listKQ.add(kqmn);
			}
			createFileJSONMN(listKQ,ngay_quay_vn);
			createFileTT_HTML_MN(listKQ);
			
		}
	}
	
	
	public void initFileTTMT() throws ParseException{
		ArrayList<KQMN> listKQ = new ArrayList<KQMN>();
		java.util.Date currentDate = Calendar.getInstance().getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String ngay_quay = formatter.format(currentDate);
		
		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date1MN = formatter.parse(ngay_quay+" 00:00:00");
		Date date2MN = formatter.parse(ngay_quay+" 16:00:00");
		
		// Neu chua toi thoi gian tuong thuat
		if(currentDate.after(date1MN)&&date2MN.after(currentDate)){
			formatter = new SimpleDateFormat("dd/MM/yyyy");
			String ngay_quay_vn = formatter.format(currentDate);
			
			ArrayList<Integer> provinceIds = getProvinceOpenByRegio(2);
			for (Integer pid : provinceIds) {
				KQMN kqmn = initKQMienNamTheoTinh(pid);
				kqmn.province_id = pid;
				kqmn.ngay_quay = ngay_quay_vn;
				kqmn.province_name = getProvinceNameByMap(pid+"");
				listKQ.add(kqmn);
			}
			createFileJSONMT(listKQ,ngay_quay_vn);
			createFileTT_HTML_MT(listKQ);
		}
	}
	
	public void initFileTTMB() throws ParseException{
		java.util.Date currentDate = Calendar.getInstance().getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String ngay_quay = formatter.format(currentDate);
		
		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date1MN = formatter.parse(ngay_quay+" 00:00:00");
		Date date2MN = formatter.parse(ngay_quay+" 16:00:00");
		
		// Neu chua toi thoi gian tuong thuat
		if(currentDate.after(date1MN)&&date2MN.after(currentDate)){
			formatter = new SimpleDateFormat("dd/MM/yyyy");
			String ngay_quay_vn = formatter.format(currentDate);
			KQMB kqmb = initKQMienBac();
			kqmb.ngay_quay = ngay_quay_vn;
			createFileJSONMB(kqmb,ngay_quay_vn);
			createFileTT_HTML_MB(kqmb);
		}
	}
	
	public void crawlerXSMT() throws Exception {
		String url ="http://ketquaveso.com/ttkq/mien-trung.html?t=1400492354836";
	
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		String html = HttpClientUtil.getResponseBody(res);
		System.out.println(html);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		String xpath__weather_td= "//UL[@class='list-col']/LI";
		NodeList linkNodes = (NodeList) reader.read(xpath__weather_td, XPathConstants.NODESET);
		NodeList linkNodeProvinces = (NodeList) reader.read("//UL[@class='gr-yellow clearfix']/LI", XPathConstants.NODESET);
		int i = 2;
		List<String> arrG8 = new ArrayList<String>();
		List<String> arrG7 = new ArrayList<String>();
		List<String> arrG6 = new ArrayList<String>();
		List<String> arrG5 = new ArrayList<String>();
		List<String> arrG4 = new ArrayList<String>();
		List<String> arrG3 = new ArrayList<String>();
		List<String> arrG2 = new ArrayList<String>();
		List<String> arrG1 = new ArrayList<String>();
		List<String> arrG0 = new ArrayList<String>();
		List<String> arrTinh = new ArrayList<String>();
		ArrayList<KQMN> listKQ = new ArrayList<KQMN>();
		
		// get Ngay Tuong Thuat
		String xpath__date= "//UL[1]/LI[1]/A[2]";
		String str_date_tt =  (String) reader.read(xpath__date, XPathConstants.STRING);
		str_date_tt = str_date_tt.replace("Ngày", "").trim();
		System.out.println(str_date_tt);
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date date_tt = formatter.parse(str_date_tt+" 23:59:00");
		
		java.util.Date currentDate = Calendar.getInstance().getTime();
		System.out.println("currentDate:"+currentDate);
		
		// Neu chua toi thoi gian tuong thuat
		if(currentDate.after(date_tt)){ 
			formatter = new SimpleDateFormat("dd/MM/yyyy");
			String ngay_quay_vn = formatter.format(currentDate);
			ArrayList<Integer> provinceIds = getProvinceOpenByRegio(2);
			for (Integer pid : provinceIds) {
				KQMN kqmn = initKQMienNamTheoTinh(pid);
				kqmn.province_id = pid;
				kqmn.ngay_quay = ngay_quay_vn;
				kqmn.province_name = getProvinceNameByMap(pid+"");
				listKQ.add(kqmn);
			}
			
			createFileJSONMT(listKQ,ngay_quay_vn);
			createFileTT_HTML_MT(listKQ);
			Thread.sleep(1000);
			return; 
		}
		
		
		if(linkNodeProvinces!=null)
		{
			while(i<=linkNodeProvinces.getLength())
			{
				String xpath_province = "//UL[@class='gr-yellow clearfix']/LI["+i+"]/SPAN/text()";
				String province_code =  (String) reader.read(xpath_province, XPathConstants.STRING);
				String code = province_code.replace("Mã:", "").trim();
				System.out.println("MT:"+province_code.replace("Mã:", "").trim());
				arrTinh.add(code);
				i++;
			}
		}
		
		W10HDAO w10hdao = new W10HDAO();
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		String ngay_quay = formatter.format(date_tt);
		System.out.println("ngay_quay:"+ngay_quay);
		
		boolean kq_check = true;
		for (String code : arrTinh) {
			int province_id = w10hdao.getProvinceIdByCode(code);
			int kq = w10hdao.checkKqMT(ngay_quay,province_id);
			kq_check = (kq==2?true:false)&kq_check;
		}
		
		// Neu da tuong thuat xong
		//if(kq_check)return;
		
		
		i = 0;
		if(linkNodes!=null)
		{
			while(i<=linkNodes.getLength())
			{
				String xpath_province = "//UL[2]/LI["+i+"]/LABEL/text()";
				String province =  (String) reader.read(xpath_province, XPathConstants.STRING);
				
				String xpath_rs = "//UL[2]/LI["+i+"]/DIV[1]/SPAN";
				NodeList linkNodes2 = (NodeList) reader.read(xpath_rs, XPathConstants.NODESET);
				
				int j = 1;
				System.out.println(province+":");
				while(j<=linkNodes2.getLength())
				{
					String rs =  (String) reader.read(xpath_rs+"["+j+"]/STRONG/text()", XPathConstants.STRING);
					System.out.println(rs);
					switch (i){
						case 1:
							arrG8.add(rs); 	
							break;
						case 2:
							arrG7.add(rs); 	
							break;
						case 3:
							arrG6.add(rs); 	
							break;
						case 4:
							arrG5.add(rs); 	
							break;
						case 5:
							arrG4.add(rs); 	
							break;
						case 6:
							arrG3.add(rs); 	
							break;
						case 7:
							arrG2.add(rs); 	
							break;
						case 8:
							arrG1.add(rs); 	
							break;
						case 9:
							arrG0.add(rs); 	
							break;
					}
					j++;
				}
				i++;
			}
		}
		
		i = 0;int sotinh = arrTinh.size();
		
		for (String provinceCode : arrTinh) {
			KQMN kqmn = new KQMN();
			int province_id = w10hdao.getProvinceIdByCode(provinceCode);
			System.out.println(provinceCode+":"+province_id);
			int kq = w10hdao.checkKqMT(ngay_quay, province_id);
			kqmn = getKQMienNamTheoTinh(provinceCode,i,sotinh,arrG0,arrG1,arrG2,arrG3,arrG4,arrG5,arrG6,arrG7,arrG8);
			kqmn.ngay_quay = ngay_quay;
			
			formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String ngay_quay_1 = formatter.format(currentDate);
			kqmn.create_date = ngay_quay_1;
			kqmn.modify_date = ngay_quay_1;
			kqmn.create_user ="crawler";
			kqmn.modify_user ="crawler";
			kqmn.province_id = province_id;
			kqmn.province_name = getProvinceNameByMap(province_id+"");
			
			listKQ.add(kqmn);
			
			if(kq==0)
				w10hdao.saveKqMienTrung(kqmn);
			else
				w10hdao.updateKqMienTrung(kqmn);
			i++;
		}
		
		formatter = new SimpleDateFormat("dd/MM/yyyy");
		String ngay_quay_vn = formatter.format(date_tt);
		createFileJSONMT(listKQ,ngay_quay_vn);
		createFileTT_HTML_MT(listKQ);
		
		i = 0;
		String titleFeedKQ = "";
		while(i<listKQ.size()){
			if(!StringUtil.isEmpty(listKQ.get(i).giai_dacbiet)){
				titleFeedKQ+=listKQ.get(i).province_name.toUpperCase()+" Giai DB: "+listKQ.get(i).giai_dacbiet+" ";
			}
			i++;
		}
		
		if(!StringUtil.isEmpty(titleFeedKQ)){
			com.az24.crawler.model.Feed feed = new Feed();
		    feed.title = "KQXS Miền Trung ngày "+ngay_quay+": "+titleFeedKQ;
			feed.image = "http://10h.vn/themes/images/xo-so-10h-icon-NOTE.png";
			feed.url ="http://10h.vn/ket-qua-xo-so-mien-trung.html";
			feed.type =2; //"TK_12BOSORAIT_MB";
			feed.isMobile = 0;
			feed.create_user ="thongke";
			w10hdao.deleteFeedByType(2);
			w10hdao.saveFeed(feed);
			
			feed.image = "http://10h.vn/themes/images/xo-so-10h-icon-NOTE.png";
			feed.url ="http://m.10h.vn/ket-qua-xo-so-mien-trung.html";
			feed.isMobile = 1;
			
			ArrayList<Feed> feeds = new ArrayList<Feed>();
			feeds = w10hdao.getNewFeed(0);
			createFileFeed(feeds);
			
			feeds = new ArrayList<Feed>();
			feeds = w10hdao.getNewFeed(1);
			createFileFeedMobile(feeds);
		}
	}
	
	/*private KQMN getKQMienNamTheoTinh(String provinceCode,int vtTinh,int sotinh,List<String> arrG0,List<String> arrG1,List<String> arrG2,List<String> arrG3,List<String> arrG4,
			List<String> arrG5,List<String> arrG6,List<String> arrG7,List<String> arrG8)
	{
		KQMN kqmn = new KQMN();
		kqmn.province_id = 0;
		kqmn.giai_tam = getKqGiai(vtTinh,0,sotinh,arrG8);
		kqmn.giai_bay = getKqGiai(vtTinh,0,sotinh,arrG7);
		kqmn.giai_sau_1 = getKqGiai(vtTinh,0,sotinh,arrG6);
		kqmn.giai_sau_2 = getKqGiai(vtTinh,1,sotinh,arrG6);
		kqmn.giai_sau_3 = getKqGiai(vtTinh,2,sotinh,arrG6);
		kqmn.giai_nam = getKqGiai(vtTinh,0,sotinh,arrG5);
		kqmn.giai_tu_1 = getKqGiai(vtTinh,0,sotinh,arrG4);
		kqmn.giai_tu_2 = getKqGiai(vtTinh,1,sotinh,arrG4);
		kqmn.giai_tu_3 = getKqGiai(vtTinh,2,sotinh,arrG4);
		kqmn.giai_tu_4 = getKqGiai(vtTinh,3,sotinh,arrG4);
		kqmn.giai_tu_5 = getKqGiai(vtTinh,4,sotinh,arrG4);
		kqmn.giai_tu_6 = getKqGiai(vtTinh,5,sotinh,arrG4);
		kqmn.giai_tu_7 = getKqGiai(vtTinh,6,sotinh,arrG4);
		kqmn.giai_ba_1 = getKqGiai(vtTinh,0,sotinh,arrG3);
		kqmn.giai_ba_2 = getKqGiai(vtTinh,1,sotinh,arrG3);
		kqmn.giai_nhi = getKqGiai(vtTinh,0,sotinh,arrG2);
		kqmn.giai_nhat = getKqGiai(vtTinh,0,sotinh,arrG1);
		kqmn.giai_dacbiet = getKqGiai(vtTinh,0,sotinh,arrG0);
		return kqmn;
	}
	
	private String getKqGiai(int vitriTinh,int sttGiai,int soTinh,List<String> arrGiais){
		int i = 0, row=1;
		int sorow = arrGiais.size()/soTinh;
		List<String> arrKqGiaiTheoTinh = new ArrayList<String>();
		while(row<=sorow){
			i = vitriTinh+ (row-1)*soTinh;
			arrKqGiaiTheoTinh.add(arrGiais.get(i));
			row++;
		}
		String kq =  arrKqGiaiTheoTinh.get(sttGiai);
		return kq;
	}
	*/
	public void crawlerXSMB() throws Exception {
		String url ="http://ketquaveso.com/ttkq/mien-bac.html?t=1400492354836";
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		String html = HttpClientUtil.getResponseBody(res);
		System.out.println(html);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		String xpath__weather_td= "//UL[@class='list-kqmb']/LI";
		NodeList linkNodes = (NodeList) reader.read(xpath__weather_td, XPathConstants.NODESET);
		int i = 1;
		
		// get Ngay Tuong Thuat
		String xpath__date= "//UL[1]/LI[2]/A";
		xpath__date = "//DIV[1]/DIV[1]/H2/A[2]/text()";
		String str_date_tt =  (String) reader.read(xpath__date, XPathConstants.STRING);
		str_date_tt = str_date_tt.replace("Ngày", "").trim();
		System.out.println(str_date_tt);
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		Date date_tt = formatter.parse("20-01-2015"+" 23:59:00");
		
		java.util.Date currentDate = Calendar.getInstance().getTime();
		System.out.println("currentDate:"+currentDate);
		
		// Neu chua toi thoi gian tuong thuat
		if(currentDate.after(date_tt)){ 
			formatter = new SimpleDateFormat("dd/MM/yyyy");
			String ngay_quay_vn = formatter.format(currentDate);
			KQMB kqmb = initKQMienBac();
			kqmb.ngay_quay = ngay_quay_vn;
			createFileJSONMB(kqmb,ngay_quay_vn);
			createFileTT_HTML_MB(kqmb);
			Thread.sleep(1000);
			return; 
		}
		
		W10HDAO w10hdao = new W10HDAO();
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		String ngay_quay = formatter.format(date_tt);
		System.out.println("ngay_quay:"+ngay_quay);
		int kq = w10hdao.checkKqMB(ngay_quay);
		
		// Neu da tuong thuat xong
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		if(kq==2&&calendar.get(Calendar.MINUTE)>40)return;
		
		List<String> arrG7 = new ArrayList<String>();
		List<String> arrG6 = new ArrayList<String>();
		List<String> arrG5 = new ArrayList<String>();
		List<String> arrG4 = new ArrayList<String>();
		List<String> arrG3 = new ArrayList<String>();
		List<String> arrG2 = new ArrayList<String>();
		List<String> arrG1 = new ArrayList<String>();
		List<String> arrG0 = new ArrayList<String>();
		
		if(linkNodes!=null)
		{
			while(i<=linkNodes.getLength())
			{
				String xpath_province = "//UL[@class='list-kqmb']/LI["+i+"]/LABEL/text()";
				String province =  (String) reader.read(xpath_province, XPathConstants.STRING);
				
				String xpath_rs = "//UL[@class='list-kqmb']/LI["+i+"]/DIV[1]/SPAN";
				NodeList linkNodes2 = (NodeList) reader.read(xpath_rs, XPathConstants.NODESET);
				
				int j = 1;
				System.out.println(province+":");
				while(j<=linkNodes2.getLength())
				{
					String rs =  (String) reader.read(xpath_rs+"["+j+"]/STRONG/text()", XPathConstants.STRING);
					System.out.println(rs);
					switch (i){
						case 1:
							arrG0.add(rs);
							break;
						case 2:
							arrG1.add(rs);
							break;
						case 3:
							arrG2.add(rs);
							break;
						case 4:
							arrG3.add(rs);
							break;
						case 5:
							arrG4.add(rs);
							break;
						case 6:
							arrG5.add(rs);
							break;
						case 7:
							arrG6.add(rs);
							break;
						case 8:
							arrG7.add(rs);
							break;
					}
					j++;
				}
				i++;
			}
		}
		
		KQMB kqmb = getKQMienBac(0,1,arrG0,arrG1,arrG2,arrG3,arrG4,arrG5,arrG6,arrG7);
		kqmb.ngay_quay = ngay_quay;
		
		formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		ngay_quay = formatter.format(currentDate);
		kqmb.create_date = ngay_quay;
		kqmb.modify_date = ngay_quay;
		kqmb.create_user ="crawler";
		kqmb.modify_user ="crawler";
		
		formatter = new SimpleDateFormat("dd/MM/yyyy");
		String ngay_quay_vn = formatter.format(date_tt);
		createFileJSONMB(kqmb,ngay_quay_vn);
		createFileTT_HTML_MB(kqmb);
		
		if(kq==0)
			w10hdao.saveKqMienBac(kqmb);
		else
			w10hdao.update(kqmb,kqmb.ngay_quay);
		
		i = 0;
		String titleFeedKQ = "";
		
		if(!StringUtil.isEmpty(kqmb.giai_dacbiet)){
				titleFeedKQ+=" Giai DB: "+kqmb.giai_dacbiet+" ";
			}
		
		
		if(!StringUtil.isEmpty(titleFeedKQ)){
			Feed feed = new Feed();
		    feed.title = "KQXS Miền Bắc ngày "+ngay_quay+": "+titleFeedKQ;
			feed.image = "http://10h.vn/themes/images/xo-so-10h-icon-NOTE.png";
			feed.url ="http://10h.vn/ket-qua-xo-so-mien-bac.html";
			feed.type =1; //"TK_12BOSORAIT_MB";
			feed.create_user ="thongke";
			feed.isMobile = 0;
			w10hdao.deleteFeedByType(1);
			w10hdao.saveFeed(feed);
			
			feed.image = "http://10h.vn/themes/images/xo-so-10h-icon-NOTE.png";
			feed.url ="http://m.10h.vn/ket-qua-xo-so-mien-bac.html";
			feed.isMobile = 1;
			w10hdao.saveFeed(feed);
			
			ArrayList<Feed> feeds = new ArrayList<Feed>();
			feeds = w10hdao.getNewFeed(0);
			createFileFeed(feeds);
			
			feeds = new ArrayList<Feed>();
			feeds = w10hdao.getNewFeed(1);
			createFileFeedMobile(feeds);
		}
	}
	
	/*private KQMB getKQMienBac(int vtTinh,int sotinh,List<String> arrG0,List<String> arrG1,List<String> arrG2,List<String> arrG3,List<String> arrG4,
			List<String> arrG5,List<String> arrG6,List<String> arrG7)
	{
		KQMB kqmn = new KQMB();
		kqmn.province_id = 1;
		kqmn.giai_bay_1 = getKqGiai(vtTinh,0,sotinh,arrG7);
		kqmn.giai_bay_2 = getKqGiai(vtTinh,1,sotinh,arrG7);
		kqmn.giai_bay_3 = getKqGiai(vtTinh,2,sotinh,arrG7);
		kqmn.giai_bay_4 = getKqGiai(vtTinh,3,sotinh,arrG7);
		
		kqmn.giai_sau_1 = getKqGiai(vtTinh,0,sotinh,arrG6);
		kqmn.giai_sau_2 = getKqGiai(vtTinh,1,sotinh,arrG6);
		kqmn.giai_sau_3 = getKqGiai(vtTinh,2,sotinh,arrG6);
		
		kqmn.giai_nam_1 = getKqGiai(vtTinh,0,sotinh,arrG5);
		kqmn.giai_nam_2 = getKqGiai(vtTinh,1,sotinh,arrG5);
		kqmn.giai_nam_3 = getKqGiai(vtTinh,2,sotinh,arrG5);
		kqmn.giai_nam_4 = getKqGiai(vtTinh,3,sotinh,arrG5);
		kqmn.giai_nam_5 = getKqGiai(vtTinh,4,sotinh,arrG5);
		kqmn.giai_nam_6 = getKqGiai(vtTinh,5,sotinh,arrG5);
		
		kqmn.giai_tu_1 = getKqGiai(vtTinh,0,sotinh,arrG4);
		kqmn.giai_tu_2 = getKqGiai(vtTinh,1,sotinh,arrG4);
		kqmn.giai_tu_3 = getKqGiai(vtTinh,2,sotinh,arrG4);
		kqmn.giai_tu_4 = getKqGiai(vtTinh,3,sotinh,arrG4);
		
		kqmn.giai_ba_1 = getKqGiai(vtTinh,0,sotinh,arrG3);
		kqmn.giai_ba_2 = getKqGiai(vtTinh,1,sotinh,arrG3);
		kqmn.giai_ba_3 = getKqGiai(vtTinh,2,sotinh,arrG3);
		kqmn.giai_ba_4 = getKqGiai(vtTinh,3,sotinh,arrG3);
		kqmn.giai_ba_5 = getKqGiai(vtTinh,4,sotinh,arrG3);
		kqmn.giai_ba_6 = getKqGiai(vtTinh,5,sotinh,arrG3);
		
		kqmn.giai_nhi_1 = getKqGiai(vtTinh,0,sotinh,arrG2);
		kqmn.giai_nhi_2 = getKqGiai(vtTinh,1,sotinh,arrG2);
		
		kqmn.giai_nhat = getKqGiai(vtTinh,0,sotinh,arrG1);
		kqmn.giai_dacbiet = getKqGiai(vtTinh,0,sotinh,arrG0);
		return kqmn;
	}
	*/
	
	//http://xoso.wap.vn/content_resp.jsp?code=XSMT
	
	public static void main(String[] args) throws ParseException {
		CopyOfCrawlerDailyXoSoKQVS.filePID = "./conf/pidKetQuaVeSo.txt";
		if(CopyOfCrawlerDailyXoSoKQVS.existPID()) return; else CopyOfCrawlerDailyXoSoKQVS.createPID();
		
		try {
			CopyOfCrawlerDailyXoSoKQVS xoSoKQVS = new CopyOfCrawlerDailyXoSoKQVS();
			xoSoKQVS.initFileTTMB();
			xoSoKQVS.initFileTTMT();
			xoSoKQVS.initFileTTMN();
			
			Calendar calendar = Calendar.getInstance();
			
			SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String ngay_quay = formatter1.format(calendar.getTime());
			
			Date date1MN = formatter2.parse(ngay_quay+" 16:10:00");
			Date date2MN = formatter2.parse(ngay_quay+" 16:50:00");
			
			Date date1MT = formatter2.parse(ngay_quay+" 17:10:00");
			Date date2MT = formatter2.parse(ngay_quay+" 17:50:00");
			
			Date date1MB = formatter2.parse(ngay_quay+" 18:10:00");
			Date date2MB = formatter2.parse(ngay_quay+" 18:50:00");
			
			
			Date currentDate = calendar.getTime();
			
			int runNowMN = 0;
			if(args!=null&&args.length>0)
				runNowMN = Integer.parseInt(args[0]);
			
			int runNowMT = 0;
			if(args!=null&&args.length>0)
				runNowMT = Integer.parseInt(args[1]);
			
			int runNowMB = 0;
			if(args!=null&&args.length>0)
				runNowMB = Integer.parseInt(args[2]);
			
			try {
				while(currentDate.after(date1MN)&&date2MN.after(currentDate)||1==1){
				xoSoKQVS.crawlerXSMN();
				Thread.sleep(1000);
				System.out.println("----------------Nghi 1000 S-------------------------");
				calendar = Calendar.getInstance();
				currentDate = calendar.getTime();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				while(currentDate.after(date1MT)&&date2MT.after(currentDate)||runNowMT==1){
				xoSoKQVS.crawlerXSMT();
				Thread.sleep(1000);
				System.out.println("----------------Nghi 1000 S-------------------------");
				calendar = Calendar.getInstance();
				currentDate = calendar.getTime();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				while(currentDate.after(date1MB)&&date2MB.after(currentDate)||runNowMB==1){
				xoSoKQVS.crawlerXSMB();
				Thread.sleep(1000);
				System.out.println("----------------Nghi 1000 S-------------------------");
				calendar = Calendar.getInstance();
				currentDate = calendar.getTime();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			CopyOfCrawlerDailyXoSoKQVS.deletePID();
		}
		
		
	}
	
}
