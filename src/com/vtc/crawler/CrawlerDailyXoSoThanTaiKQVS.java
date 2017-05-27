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

import com.az24.crawler.model.KQMB;
import com.az24.crawler.model.KQMN;
import com.az24.dao.W10HDAO;
import com.az24.test.HttpClientUtil;


public class CrawlerDailyXoSoThanTaiKQVS extends CrawlerDailyXoSo{
	
	public void crawlerXSMN() throws Exception {
		String url ="http://ketquaveso.com/ttkq/mien-nam.html?t=1400492037710";
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		//System.out.println(html);
		
		NodeList linkNodeProvinces =  (NodeList) reader.read("//TABLE/TBODY/TR[1]/TH", XPathConstants.NODESET);
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
		java.util.Date currentDate = Calendar.getInstance().getTime();
		
		
		
		if(linkNodeProvinces!=null)
		{
			while(i<=linkNodeProvinces.getLength())
			{
				String xpath_province = "//TABLE[1]/TBODY/TR[1]/TH["+i+"]/SPAN/text()";
				String province_code =  (String) reader.read(xpath_province, XPathConstants.STRING);
				String code = province_code.replace("Mã:", "").trim();
				System.out.println("MT:"+province_code.replace("Mã:", "").trim());
				if(!StringUtil.isEmpty(code))
				arrTinh.add(code);
				i++;
			}
		}
		
		W10HDAO w10hdao = new W10HDAO();
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date_tt = Calendar.getInstance().getTime();
		String ngay_quay = formatter.format(date_tt);
		System.out.println("ngay_quay:"+ngay_quay);
		
		int r = 2;
		while(r<=10){
		i = 2;
		while(i<=linkNodeProvinces.getLength())
				{
					String xpath_rs = "//TABLE/TBODY/TR["+r+"]/TD["+i+"]/DIV";
					NodeList linkNodesSoGiai = (NodeList) reader.read(xpath_rs, XPathConstants.NODESET);
					int soGiai = 1;
					while(soGiai<=linkNodesSoGiai.getLength()){
						String xpath_kq = "//TABLE/TBODY/TR["+r+"]/TD["+i+"]/DIV["+soGiai+"]/STRONG[1]/text()";
						String kq =  (String) reader.read(xpath_kq, XPathConstants.STRING);
						System.out.println("content:"+kq);
						
						switch (r){
						case 2:
							arrG8.add(kq); 	
							break;
						case 3:
							arrG7.add(kq); 	
							break;
						case 4:
							arrG6.add(kq); 	
							break;
						case 5:
							arrG5.add(kq); 	
							break;
						case 6:
							arrG4.add(kq); 	
							break;
						case 7:
							arrG3.add(kq); 	
							break;
						case 8:
							arrG2.add(kq); 	
							break;
						case 9:
							arrG1.add(kq); 	
							break;
						case 10:
							arrG0.add(kq); 	
							break;
					}
						
						soGiai++;
					}
					i ++;
				}
				
				
				
				r++;
			}
		
	
		
		i = 0;int sotinh = arrTinh.size();
		
		for (String provinceCode : arrTinh) {
				KQMN kqmn = new KQMN();
				int province_id = w10hdao.getProvinceIdByCode(provinceCode);
				int kq = w10hdao.checkKqMN(ngay_quay, province_id);
				// VTT Giai (tinh tu 0) + sô lượng kq của giải (arrrG.lengt/so tỉnh) * (vttinh tính từ 0)
				kqmn = getKQMienNamTheoTinhLoai2("w",i,sotinh,arrG0,arrG1,arrG2,arrG3,arrG4,arrG5,arrG6,arrG7,arrG8);
			
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
				if(!StringUtil.isEmpty(kqmn.giai_dacbiet)){
					String notify = "KQXS Tỉnh " + kqmn.province_name +" ngày "+ngay_quay+" giải đặc biệt: "+kqmn.giai_dacbiet;
					if(w10hdao.checkNotify(kqmn.province_id,ngay_quay)==0)
					w10hdao.saveNotify(kqmn.province_id,ngay_quay,notify, 3);
					
				}
				i++;
		}
		formatter = new SimpleDateFormat("dd/MM/yyyy");
		String ngay_quay_vn = formatter.format(date_tt);
		createFileJSONMNThanTai(listKQ,ngay_quay_vn);
		createFileTT_HTML_MN_ThanTai(listKQ);
		// Xo So Plus
		
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
			createFileTT_HTML_MN_ThanTai(listKQ);
			
			
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
			createFileTT_HTML_MT_ThanTai(listKQ);
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
			createFileTT_HTML_MB_ThanTai(kqmb);
			//createFileTT_HTML_MB_XOSOPLus(kqmb);
		}
	}
	
	public void crawlerXSMT() throws Exception {
		String url ="http://ketquaveso.com/ttkq/mien-trung.html?t=1400492354836";
	
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		String html = HttpClientUtil.getResponseBody(res);
		//System.out.println(html);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		NodeList linkNodeProvinces =  (NodeList) reader.read("//TABLE/TBODY/TR[1]/TH", XPathConstants.NODESET);
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
		
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		
		java.util.Date currentDate = Calendar.getInstance().getTime();
		System.out.println("currentDate:"+currentDate);
		
		
		
		if(linkNodeProvinces!=null)
		{
			while(i<=linkNodeProvinces.getLength())
			{
				String xpath_province = "//TABLE[1]/TBODY/TR[1]/TH["+i+"]/SPAN/text()";
				String province_code =  (String) reader.read(xpath_province, XPathConstants.STRING);
				String code = province_code.replace("Mã:", "").trim();
				System.out.println("MT:"+province_code.replace("Mã:", "").trim());
				if(!StringUtil.isEmpty(code))
				arrTinh.add(code);
				i++;
			}
		}
		
		W10HDAO w10hdao = new W10HDAO();
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		String ngay_quay = formatter.format(currentDate);
		System.out.println("ngay_quay:"+ngay_quay);
		
		
		
		int r = 2;
		while(r<=10){
			i = 2;
			while(i<=linkNodeProvinces.getLength())
			{
				String xpath_rs = "//TABLE/TBODY/TR["+r+"]/TD["+i+"]/DIV";
				NodeList linkNodesSoGiai = (NodeList) reader.read(xpath_rs, XPathConstants.NODESET);
				int soGiai = 1;
				while(soGiai<=linkNodesSoGiai.getLength()){
					String xpath_kq = "//TABLE/TBODY/TR["+r+"]/TD["+i+"]/DIV["+soGiai+"]/STRONG[1]/text()";
					String kq =  (String) reader.read(xpath_kq, XPathConstants.STRING);
					System.out.println("content:"+kq);
					
					switch (r){
					case 2:
						arrG8.add(kq); 	
						break;
					case 3:
						arrG7.add(kq); 	
						break;
					case 4:
						arrG6.add(kq); 	
						break;
					case 5:
						arrG5.add(kq); 	
						break;
					case 6:
						arrG4.add(kq); 	
						break;
					case 7:
						arrG3.add(kq); 	
						break;
					case 8:
						arrG2.add(kq); 	
						break;
					case 9:
						arrG1.add(kq); 	
						break;
					case 10:
						arrG0.add(kq); 	
						break;
				}
					
					soGiai++;
				}
				i ++;
			}
			
			
			
			r++;
		}
		
		i = 0;int sotinh = arrTinh.size();
		
		for (String provinceCode : arrTinh) {
			KQMN kqmn = new KQMN();
			int province_id = w10hdao.getProvinceIdByCode(provinceCode);
			System.out.println(provinceCode+":"+province_id);
			int kq = w10hdao.checkKqMT(ngay_quay, province_id);
			// VTT Giai (tinh tu 0) + sô lượng kq của giải (arrrG.lengt/so tỉnh) * (vttinh tính từ 0)
			kqmn = getKQMienNamTheoTinhLoai2(provinceCode,i,sotinh,arrG0,arrG1,arrG2,arrG3,arrG4,arrG5,arrG6,arrG7,arrG8);
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
			
			if(!StringUtil.isEmpty(kqmn.giai_dacbiet)){
				String notify = "KQXS Tỉnh " + kqmn.province_name +" ngày "+ngay_quay+" giải đặc biệt: "+kqmn.giai_dacbiet;
				
				if(w10hdao.checkNotify(kqmn.province_id,ngay_quay)==0)
				w10hdao.saveNotify(kqmn.province_id,ngay_quay,notify, 2);
				
			}
			
			i++;
		}
		
		formatter = new SimpleDateFormat("dd/MM/yyyy");
		String ngay_quay_vn = formatter.format(currentDate);
		createFileJSONMTThanTai(listKQ,ngay_quay_vn);
		createFileTT_HTML_MT_ThanTai(listKQ);
		
	}
	
	
	public void crawlerXSMB() throws Exception {
		String url ="http://ketquaveso.com/ttkq/mien-bac.html?t=1400492354836";
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		String html = HttpClientUtil.getResponseBody(res);
		//System.out.println(html);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		String xpath__weather_td= "//TABLE[@class='kqmb']/TBODY/TR";
		NodeList linkNodes = (NodeList) reader.read(xpath__weather_td, XPathConstants.NODESET);
		int i = 1;
		
		// get Ngay Tuong Thuat
		/*String xpath__date= "//UL[1]/LI[2]/A";
		xpath__date = "//DIV[1]/DIV[1]/H2/A[2]/text()";
		String str_date_tt =  (String) reader.read(xpath__date, XPathConstants.STRING);
		str_date_tt = str_date_tt.replace("Ngày", "").trim();
		System.out.println(str_date_tt);*/
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		//Date date_tt = formatter.parse("20-01-2015"+" 23:59:00");
		
		java.util.Date currentDate = Calendar.getInstance().getTime();
		System.out.println("currentDate:"+currentDate);
		
		
		W10HDAO w10hdao = new W10HDAO();
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		String ngay_quay = formatter.format(currentDate);
		//System.out.println("ngay_quay:"+ngay_quay);
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
		i = 1;
		if(linkNodes!=null)
		{
			while(i<=linkNodes.getLength())
			{
				//String xpath_province = "//TABLE[@class='kqmb']/TBODY/TR["+i+"]/LABEL/text()";
				//String province =  (String) reader.read(xpath_province, XPathConstants.STRING);
				
				String xpath_rs = "//TABLE[@class='kqmb']/TBODY/TR["+i+"]/TD";
				NodeList linkNodesTD = (NodeList) reader.read(xpath_rs, XPathConstants.NODESET);
				
				int j = 2;
				if(i==5||i==8) j = 1;
				//System.out.println(province+":");
				while(j<=linkNodesTD.getLength())
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
							arrG3.add(rs);
							break;
						case 6:
							arrG4.add(rs);
							break;
						case 7:
							arrG5.add(rs);
							break;
						case 8:
							arrG5.add(rs);
							break;
						case 9:
							arrG6.add(rs);
							break;
						case 10:
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
		
		if(kq==0)
		w10hdao.saveKqMienBac(kqmb);
		else
		w10hdao.update(kqmb,kqmb.ngay_quay);
		
		formatter = new SimpleDateFormat("dd/MM/yyyy");
		String ngay_quay_vn = formatter.format(currentDate);
		createFileJSONMBThanTai(kqmb,ngay_quay_vn);
		createFileTT_HTML_MB_ThanTai(kqmb);
		
		
		
	}
	
	
	//http://xoso.wap.vn/content_resp.jsp?code=XSMT
	
	public static void main(String[] args) throws ParseException {
		CrawlerDailyXoSoThanTaiKQVS.filePID = "./conf/pidKetQuaVeSo.txt";
		if(CrawlerDailyXoSoKQVS.existPID()) return; else CrawlerDailyXoSoKQVS.createPID();
		
		try {
			CrawlerDailyXoSoThanTaiKQVS xoSoKQVS = new CrawlerDailyXoSoThanTaiKQVS();
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
			
			
			while(currentDate.after(date1MN)&&date2MN.after(currentDate)||runNowMN==1){
				
				try {
					xoSoKQVS.crawlerXSMN();
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			System.out.println("----------------Nghi 1000 S-------------------------");
			calendar = Calendar.getInstance();
			currentDate = calendar.getTime();
			}
			
			
			
			while(currentDate.after(date1MT)&&date2MT.after(currentDate)||runNowMT==1){
				try {
					xoSoKQVS.crawlerXSMT();
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			System.out.println("----------------Nghi 1000 S-------------------------");
			calendar = Calendar.getInstance();
			currentDate = calendar.getTime();
			}
			
			
			
			while(currentDate.after(date1MB)&&date2MB.after(currentDate)||runNowMB==1){
				try {
					xoSoKQVS.crawlerXSMB();
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			System.out.println("----------------Nghi 1000 S-------------------------");
			calendar = Calendar.getInstance();
			currentDate = calendar.getTime();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			CrawlerDailyXoSoKQVS.deletePID();
		}
		
		
	}
	
}
