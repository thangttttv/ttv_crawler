package com.vtc.crawler;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.util.html.parser.XPathReader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathConstants;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.http.HttpResponse;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.az24.crawler.model.KQMB;
import com.az24.crawler.model.KQMN;
import com.az24.dao.W10HDAO;
import com.az24.test.HttpClientUtil;

public class CrawlerDailyXoSoTrucTiep extends CrawlerDailyXoSo {
	
	public void crawlerXSMN() throws Exception {
		String url ="http://xoso.tructiep.vn/website/truc-tiep-xo-so-mien-nam.aspx";
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		String xpath__weather_td= "//table[@class='tb_ttmn']/TBODY/tr";
		NodeList linkNodes = (NodeList) reader.read(xpath__weather_td, XPathConstants.NODESET);
		NodeList linkNodeProvinces = (NodeList) reader.read("//table[@class='tb_ttmn']/TBODY/tr[1]/td", XPathConstants.NODESET);
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
		
		if(linkNodeProvinces!=null)
		{
			while(i<=linkNodeProvinces.getLength())
			{
				String xpath_province = "//table[@class='tb_ttmn']/TBODY/tr[1]/td["+i+"]/div/text()";
				String province_code =  (String) reader.read(xpath_province, XPathConstants.STRING);
				String code = province_code.replace("Mã:", "").trim();
				System.out.println("MT:"+province_code.replace("Mã:", "").trim());
				arrTinh.add(code);
				i++;
			}
		}
		
		i = 2;
		if(linkNodes!=null)
		{
			while(i<=linkNodes.getLength())
			{
				
				String xpath_rs = "//table[@class='tb_ttmn']/TBODY/tr["+i+"]/td";
				NodeList linkNodesTD = (NodeList) reader.read(xpath_rs, XPathConstants.NODESET);
				
				int j = 1;
				
				while(j<=linkNodesTD.getLength())
				{
					Node rs2 =  (Node) reader.read(xpath_rs+"["+j+"]/div", XPathConstants.NODE);
					System.out.println(rs2.getTextContent().trim());
					String rs =rs2.getTextContent().trim();
					String arrKQ[] = rs.split(" ");
					switch (i){
						case 2:
							for (String kqItem : arrKQ) {
								System.out.println(kqItem);
								if("...".equalsIgnoreCase(kqItem))kqItem=" ";
								arrG8.add(kqItem); 
							}
							break;
						case 3:
							for (String kqItem : arrKQ) {
								System.out.println(kqItem);
								if("...".equalsIgnoreCase(kqItem))kqItem=" ";
								arrG7.add(kqItem); 
							}
							break;
						case 4:
							for (String kqItem : arrKQ) {
								System.out.println(kqItem);
								if("...".equalsIgnoreCase(kqItem))kqItem=" ";
								arrG6.add(kqItem); 
							}
							break;
						case 5:
							for (String kqItem : arrKQ) {
								System.out.println(kqItem);
								if("...".equalsIgnoreCase(kqItem))kqItem=" ";
								arrG5.add(kqItem); 
							}	
							break;
						case 6:
							for (String kqItem : arrKQ) {
								System.out.println(kqItem);
								if("...".equalsIgnoreCase(kqItem))kqItem=" ";
								arrG4.add(kqItem); 
							}
							break;
						case 7:
							for (String kqItem : arrKQ) {
								System.out.println(kqItem);
								if("...".equalsIgnoreCase(kqItem))kqItem=" ";
								arrG3.add(kqItem); 
							}
							break;
						case 8:
							for (String kqItem : arrKQ) {
								System.out.println(kqItem);
								if("...".equalsIgnoreCase(kqItem))kqItem=" ";
								arrG2.add(kqItem); 
							}
							break;
						case 9:
							for (String kqItem : arrKQ) {
								System.out.println(kqItem);
								if("...".equalsIgnoreCase(kqItem))kqItem=" ";
								arrG1.add(kqItem); 
							}
							break;
						case 10:
							for (String kqItem : arrKQ) {
								System.out.println(kqItem);
								if("...".equalsIgnoreCase(kqItem))kqItem=" ";
								arrG0.add(kqItem); 
							}	
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
				kqmn = getKQMienNamTheoTinh(provinceCode,i,sotinh,arrG0,arrG1,arrG2,arrG3,arrG4,arrG5,arrG6,arrG7,arrG8);
				i++;
		}
	}
	
	
	public void crawlerXSMT() throws Exception {
		String url ="http://xoso.tructiep.vn/website/truc-tiep-xo-so-mien-trung.aspx";
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		String xpath__weather_td= "//table[@class='tb_ttmn']/TBODY/tr";
		NodeList linkNodes = (NodeList) reader.read(xpath__weather_td, XPathConstants.NODESET);
		NodeList linkNodeProvinces = (NodeList) reader.read("//table[@class='tb_ttmn']/TBODY/tr[1]/td", XPathConstants.NODESET);
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
		
		if(linkNodeProvinces!=null)
		{
			while(i<=linkNodeProvinces.getLength())
			{
				String xpath_province = "//table[@class='tb_ttmn']/TBODY/tr[1]/td["+i+"]/div/text()";
				String province_code =  (String) reader.read(xpath_province, XPathConstants.STRING);
				String code = province_code.replace("Mã:", "").trim();
				System.out.println("MT:"+province_code.replace("Mã:", "").trim());
				arrTinh.add(code);
				i++;
			}
		}
		
		i = 2;
		if(linkNodes!=null)
		{
			while(i<=linkNodes.getLength())
			{
				
				String xpath_rs = "//table[@class='tb_ttmn']/TBODY/tr["+i+"]/td";
				NodeList linkNodesTD = (NodeList) reader.read(xpath_rs, XPathConstants.NODESET);
				
				int j = 1;
				
				while(j<=linkNodesTD.getLength())
				{
					Node rs2 =  (Node) reader.read(xpath_rs+"["+j+"]/div", XPathConstants.NODE);
					System.out.println(rs2.getTextContent().trim());
					String rs =rs2.getTextContent().trim();
					String arrKQ[] = rs.split(" ");
					switch (i){
						case 2:
							for (String kqItem : arrKQ) {
								System.out.println(kqItem);
								if("...".equalsIgnoreCase(kqItem))kqItem=" ";
								arrG8.add(kqItem); 
							}
							break;
						case 3:
							for (String kqItem : arrKQ) {
								System.out.println(kqItem);
								if("...".equalsIgnoreCase(kqItem))kqItem=" ";
								arrG7.add(kqItem); 
							}
							break;
						case 4:
							for (String kqItem : arrKQ) {
								System.out.println(kqItem);
								if("...".equalsIgnoreCase(kqItem))kqItem=" ";
								arrG6.add(kqItem); 
							}
							break;
						case 5:
							for (String kqItem : arrKQ) {
								System.out.println(kqItem);
								if("...".equalsIgnoreCase(kqItem))kqItem=" ";
								arrG5.add(kqItem); 
							}	
							break;
						case 6:
							for (String kqItem : arrKQ) {
								System.out.println(kqItem);
								if("...".equalsIgnoreCase(kqItem))kqItem=" ";
								arrG4.add(kqItem); 
							}
							break;
						case 7:
							for (String kqItem : arrKQ) {
								System.out.println(kqItem);
								if("...".equalsIgnoreCase(kqItem))kqItem=" ";
								arrG3.add(kqItem); 
							}
							break;
						case 8:
							for (String kqItem : arrKQ) {
								System.out.println(kqItem);
								if("...".equalsIgnoreCase(kqItem))kqItem=" ";
								arrG2.add(kqItem); 
							}
							break;
						case 9:
							for (String kqItem : arrKQ) {
								System.out.println(kqItem);
								if("...".equalsIgnoreCase(kqItem))kqItem=" ";
								arrG1.add(kqItem); 
							}
							break;
						case 10:
							for (String kqItem : arrKQ) {
								System.out.println(kqItem);
								if("...".equalsIgnoreCase(kqItem))kqItem=" ";
								arrG0.add(kqItem); 
							}	
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
				kqmn = getKQMienNamTheoTinh(provinceCode,i,sotinh,arrG0,arrG1,arrG2,arrG3,arrG4,arrG5,arrG6,arrG7,arrG8);
				i++;
		}
	}
	
	
	public void crawlerXSMB() throws Exception {
		
		String url ="http://xoso.tructiep.vn/website/truc-tiep-mien-bac.aspx";
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		String xpath__weather_td= "//div[@class='div_ttmb_left_body_item']/div";
		NodeList linkNodes = (NodeList) reader.read(xpath__weather_td, XPathConstants.NODESET);
		int i = 1;
		
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
				String xpath_titlegiai = "//div[@class='div_ttmb_left_body_title']/div["+i+"]/text()";
				String giai =  (String) reader.read(xpath_titlegiai, XPathConstants.STRING);
				
				String xpath_rs = "//div[@class='div_ttmb_left_body_item']/div["+i+"]/div";
				NodeList linkNodes2 = (NodeList) reader.read(xpath_rs, XPathConstants.NODESET);
				
				int j = 1;
				System.out.println(giai.trim()+":");
				while(j<=linkNodes2.getLength())
				{
					Node rsNode =  (Node) reader.read("//div[@class='div_ttmb_left_body_item']/div[1]/div[1]", XPathConstants.NODE);
					System.out.println(xpath_rs+"["+j+"]");
					System.out.println(rsNode.getTextContent().toString());
					String rs = rsNode.getTextContent().trim();
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
	}
	
	
	public void crawlerXSThanTai() throws Exception {
		
		String url ="http://xoso.tructiep.vn/xo-so-than-tai.html";
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		String xpath__ngay_quay= "//div[@id='ctl00_ContentMasterPageHomeId_KqTTControlId_UpdatePanel1']/div[1]/div[1]/div[1]/div[2]/b/text()";
		String xpath_kq= "//div[@id='ctl00_ContentMasterPageHomeId_KqTTControlId_UpdatePanel1']/div[1]/div[1]/div[2]/div[1]/text()";
		String ngay_quay = (String) reader.read(xpath__ngay_quay, XPathConstants.STRING);
		String ketqua = (String) reader.read(xpath_kq, XPathConstants.STRING);
		System.out.println(ngay_quay+":"+ketqua);
		
		
	}

	public  void getKQMBJSON() throws Exception {
		String jsonTxt = "";
		String key = getKey();
		String url ="http://xoso.tructiep.vn/lottery_ws/LotteryWCFService.svc/GetLotteryMsgLiveByGroup/1,1,21,1,4,"+key+",1";
		System.out.println(url);
		
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		jsonTxt = HttpClientUtil.getResponseBody(res);
		System.out.println(jsonTxt);
		JSONArray json = (JSONArray) JSONSerializer.toJSON(jsonTxt);
		System.out.println("KQ: " + json.getJSONObject(0).getJSONArray("LotPrizes"));
		JSONArray arrayKQ = json.getJSONObject(0).getJSONArray("LotPrizes");
		
		Pattern  r1 = Pattern.compile(".* (\\d{2}/\\d{2}/\\d{4})");
		System.out.println(json.getJSONObject(0).getString("CrDateTime"));
	    Matcher  m1 = r1.matcher(json.getJSONObject(0).getString("CrDateTime"));
	    String ngay_quay_vn = "";
	    if(m1.find()){
			   ngay_quay_vn = m1.group(1);
			   System.out.println(ngay_quay_vn);
		   }
	    
		int i = 0;
		KQMB kqmb = new KQMB();
		W10HDAO w10hdao = new W10HDAO();
		
		java.util.Date currentDate = Calendar.getInstance().getTime();
		
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
		Date date_tt = formatter1.parse(ngay_quay_vn);
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String ngay_quay = formatter.format(date_tt);
		System.out.println("ngay_quay:"+ngay_quay);
		
		//int province_id = 1;
		
		
		String arrSplit[] = null;
		
		while(i<arrayKQ.size()){
			JSONObject object = (JSONObject)arrayKQ.get(i);
			System.out.println("Prize: " + object.getString("Prize"));
			System.out.println("Range: " + object.getString("Range"));
			String range = object.getString("Range").trim();
			
			switch (i) {
				case 0:
					kqmb.giai_dacbiet = "...".equalsIgnoreCase(range)==true?" ":range;
					break;
				case 1:
					kqmb.giai_nhat = "...".equalsIgnoreCase(range)==true?" ":range;;
					break;
				case 2:
					arrSplit = range.split("-");
					kqmb.giai_nhi_1 = "...".equalsIgnoreCase(arrSplit[0].trim())==true?" ":arrSplit[0];
					kqmb.giai_nhi_2 = "...".equalsIgnoreCase(arrSplit[1].trim())==true?" ":arrSplit[1];
					break;
				case 3:
					arrSplit = range.split("-");
					kqmb.giai_ba_1 = "...".equalsIgnoreCase(arrSplit[0].trim())==true?" ":arrSplit[0];
					kqmb.giai_ba_2 = "...".equalsIgnoreCase(arrSplit[1].trim())==true?" ":arrSplit[1];
					kqmb.giai_ba_3 = "...".equalsIgnoreCase(arrSplit[2].trim())==true?" ":arrSplit[2];
					kqmb.giai_ba_4 = "...".equalsIgnoreCase(arrSplit[3].trim())==true?" ":arrSplit[3];
					kqmb.giai_ba_5 = "...".equalsIgnoreCase(arrSplit[4].trim())==true?" ":arrSplit[4];
					kqmb.giai_ba_6 = "...".equalsIgnoreCase(arrSplit[5].trim())==true?" ":arrSplit[5];
					break;
				case 4:
					arrSplit = range.split("-");
					kqmb.giai_tu_1 = "...".equalsIgnoreCase(arrSplit[0].trim())==true?" ":arrSplit[0];
					kqmb.giai_tu_2 = "...".equalsIgnoreCase(arrSplit[1].trim())==true?" ":arrSplit[1];
					kqmb.giai_tu_3 = "...".equalsIgnoreCase(arrSplit[2].trim())==true?" ":arrSplit[2];
					kqmb.giai_tu_4 = "...".equalsIgnoreCase(arrSplit[3].trim())==true?" ":arrSplit[3];
					break;
				case 5:
					arrSplit = range.split("-");
					kqmb.giai_nam_1 = "...".equalsIgnoreCase(arrSplit[0].trim())==true?" ":arrSplit[0];
					kqmb.giai_nam_2 = "...".equalsIgnoreCase(arrSplit[1].trim())==true?" ":arrSplit[1];
					kqmb.giai_nam_3 = "...".equalsIgnoreCase(arrSplit[2].trim())==true?" ":arrSplit[2];
					kqmb.giai_nam_4 = "...".equalsIgnoreCase(arrSplit[3].trim())==true?" ":arrSplit[3];
					kqmb.giai_nam_5 = "...".equalsIgnoreCase(arrSplit[4].trim())==true?" ":arrSplit[4];
					kqmb.giai_nam_6 = "...".equalsIgnoreCase(arrSplit[5].trim())==true?" ":arrSplit[5];
					break;
				case 6:
					arrSplit = range.split("-");
					kqmb.giai_sau_1 = "...".equalsIgnoreCase(arrSplit[0].trim())==true?" ":arrSplit[0];
					kqmb.giai_sau_2 = "...".equalsIgnoreCase(arrSplit[1].trim())==true?" ":arrSplit[1];
					kqmb.giai_sau_3 = "...".equalsIgnoreCase(arrSplit[2].trim())==true?" ":arrSplit[2];
					break;
				case 7:
					arrSplit = range.split("-");
					kqmb.giai_bay_1 = "...".equalsIgnoreCase(arrSplit[0].trim())==true?" ":arrSplit[0];
					kqmb.giai_bay_2 = "...".equalsIgnoreCase(arrSplit[1].trim())==true?" ":arrSplit[1];
					kqmb.giai_bay_3 = "...".equalsIgnoreCase(arrSplit[2].trim())==true?" ":arrSplit[2];
					kqmb.giai_bay_4 = "...".equalsIgnoreCase(arrSplit[3].trim())==true?" ":arrSplit[3];
					break;
				default:
					break;
			}
			i++;
		}
		
		int kq = w10hdao.checkKqMB(ngay_quay);
		kqmb.ngay_quay = ngay_quay;

		formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String ngay_tao = formatter.format(currentDate);

		kqmb.create_date = ngay_tao;
		kqmb.modify_date = ngay_tao;
		kqmb.create_user ="crawler";
		kqmb.modify_user ="crawler";
		
		createFileJSONMB(kqmb,ngay_quay_vn);
		createFileTT_HTML_MB(kqmb);
		
		if(kq==2) return;
		
		if(kq==0)
			w10hdao.saveKqMienBac(kqmb);
		else
			w10hdao.update(kqmb,kqmb.ngay_quay);
	}
	
	
	public  void getKQMNJSON() throws Exception {
		String jsonTxt = "";
		String key = getKey();
		String url ="http://xoso.tructiep.vn/lottery_ws/LotteryWCFService.svc/GetLotteryMsgLiveByGroup/2,1,21,1,4,"+key+",1";
		System.out.println(url);
		
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		jsonTxt = HttpClientUtil.getResponseBody(res);
		System.out.println(jsonTxt);
		JSONArray json = (JSONArray) JSONSerializer.toJSON(jsonTxt);
		int sotinh = 0;
		
		W10HDAO w10hdao = new W10HDAO();
		Date date_tt = null;
		
		
		ArrayList<KQMN> listKQ = new ArrayList<KQMN>();
		
		while(sotinh<json.size())
		{
			System.out.println("LotPrizes: " + json.getJSONObject(sotinh).getJSONArray("LotPrizes"));
			System.out.println("LotteryCode: " + json.getJSONObject(sotinh).getString("LotteryCode"));
			String LotteryId = json.getJSONObject(sotinh).getString("LotteryId");
			JSONArray arrayKQ = json.getJSONObject(sotinh).getJSONArray("LotPrizes");
			
			Pattern  r1 = Pattern.compile(".* (\\d{2}/\\d{2}/\\d{4})");
			System.out.println(json.getJSONObject(0).getString("CrDateTime"));
		    Matcher  m1 = r1.matcher(json.getJSONObject(0).getString("CrDateTime"));
		    String ngay_quay_vn = "";
		    if(m1.find()){
				   ngay_quay_vn = m1.group(1);
				   System.out.println(ngay_quay_vn);
			   }
		    
		    SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
			date_tt = formatter1.parse(ngay_quay_vn);
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String ngay_quay = formatter.format(date_tt);
			System.out.println("ngay_quay:"+ngay_quay);
			
			int province_id = 0;
			province_id = getProvinceIdByMap(LotteryId);
			/*int kq = w10hdao.checkKqMN(ngay_quay,province_id);
			if(kq==2){sotinh++;continue;};*/
			
			int i = 0;
			KQMN kqmb = new KQMN();
			String arrSplit[] = null;
			
			while(i<arrayKQ.size()){
				JSONObject object = (JSONObject)arrayKQ.get(i);
				System.out.println("Prize: " + object.getString("Prize"));
				System.out.println("Range: " + object.getString("Range"));
				String range = object.getString("Range").trim();
				
				switch (i) {
					case 0:
						kqmb.giai_tam = "...".equalsIgnoreCase(range)==true?" ":range;
						break;
					case 1:
						kqmb.giai_bay = "...".equalsIgnoreCase(range)==true?" ":range;;
						break;
					case 2:
						arrSplit = range.split("-");
						kqmb.giai_sau_1 = "...".equalsIgnoreCase(arrSplit[0].trim())==true?" ":arrSplit[0];
						kqmb.giai_sau_2 = "...".equalsIgnoreCase(arrSplit[1].trim())==true?" ":arrSplit[1];
						kqmb.giai_sau_3 = "...".equalsIgnoreCase(arrSplit[2].trim())==true?" ":arrSplit[2];
						break;
					case 3:
						arrSplit = range.split("-");
						kqmb.giai_nam = "...".equalsIgnoreCase(arrSplit[0].trim())==true?" ":arrSplit[0];
						break;
					case 4:
						arrSplit = range.split("-");
						kqmb.giai_tu_1 = "...".equalsIgnoreCase(arrSplit[0].trim())==true?" ":arrSplit[0];
						kqmb.giai_tu_2 = "...".equalsIgnoreCase(arrSplit[1].trim())==true?" ":arrSplit[1];
						kqmb.giai_tu_3 = "...".equalsIgnoreCase(arrSplit[2].trim())==true?" ":arrSplit[2];
						kqmb.giai_tu_4 = "...".equalsIgnoreCase(arrSplit[3].trim())==true?" ":arrSplit[3];
						kqmb.giai_tu_5 = "...".equalsIgnoreCase(arrSplit[4].trim())==true?" ":arrSplit[4];
						kqmb.giai_tu_6 = "...".equalsIgnoreCase(arrSplit[5].trim())==true?" ":arrSplit[5];
						kqmb.giai_tu_7 = "...".equalsIgnoreCase(arrSplit[6].trim())==true?" ":arrSplit[6];
						break;
					case 5:
						arrSplit = range.split("-");
						kqmb.giai_ba_1 = "...".equalsIgnoreCase(arrSplit[0].trim())==true?" ":arrSplit[0];
						kqmb.giai_ba_2 = "...".equalsIgnoreCase(arrSplit[1].trim())==true?" ":arrSplit[1];
						break;
					case 6:
						arrSplit = range.split("-");
						kqmb.giai_nhi = "...".equalsIgnoreCase(arrSplit[0].trim())==true?" ":arrSplit[0];
						break;
					case 7:
						arrSplit = range.split("-");
						kqmb.giai_nhat = "...".equalsIgnoreCase(arrSplit[0].trim())==true?" ":arrSplit[0];
						break;
					case 8:
						arrSplit = range.split("-");
						kqmb.giai_dacbiet = "...".equalsIgnoreCase(arrSplit[0].trim())==true?" ":arrSplit[0];
						break;
					default:
						break;
				}
				i++;
			}
			
			
			
			kqmb.province_id = province_id;
			kqmb.ngay_quay = ngay_quay;
			
			formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String ngay_tao = formatter.format(Calendar.getInstance().getTime());
			kqmb.create_date = ngay_tao;
			kqmb.modify_date = ngay_tao;
			kqmb.create_user ="crawler";
			kqmb.modify_user ="crawler";
			kqmb.province_name=getProvinceNameByMap(province_id+"");
			
			listKQ.add(kqmb);
			int kq = w10hdao.checkKqMN(ngay_quay,province_id);
			if(kq==2){sotinh++;continue;};
			
			if(kq==0)
				w10hdao.saveKqMienNam(kqmb);
			else
				if(kq==1)
				w10hdao.updateKqMienNam(kqmb);
			
			
			sotinh++;
			
		}
		
		Pattern  r1 = Pattern.compile(".* (\\d{2}/\\d{2}/\\d{4})");
		System.out.println(json.getJSONObject(0).getString("CrDateTime"));
	    Matcher  m1 = r1.matcher(json.getJSONObject(0).getString("CrDateTime"));
	    String ngay_quay_vn = "";
	    if(m1.find()){
			   ngay_quay_vn = m1.group(1);
			   System.out.println(ngay_quay_vn);
		   }
	    
		createFileJSONMN(listKQ,ngay_quay_vn);
		createFileTT_HTML_MN(listKQ);
	}
	
	
	public  void getKQMTJSON() throws Exception {
		String jsonTxt = "";
		String key = getKey();
		String url ="http://xoso.tructiep.vn/lottery_ws/LotteryWCFService.svc/GetLotteryMsgLiveByGroup/3,1,21,1,4,"+key+",1";
		System.out.println(url);
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		jsonTxt = HttpClientUtil.getResponseBody(res);
		System.out.println(jsonTxt);
		JSONArray json = (JSONArray) JSONSerializer.toJSON(jsonTxt);
		int sotinh = 0;
		W10HDAO w10hdao = new W10HDAO();
		java.util.Date currentDate = Calendar.getInstance().getTime();
		/*SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String ngay_quay = formatter.format(currentDate);
		System.out.println("ngay_quay:"+ngay_quay);*/
		ArrayList<KQMN> listKQ = new ArrayList<KQMN>();
		
		while(sotinh<json.size())
		{
			System.out.println("LotPrizes: " + json.getJSONObject(sotinh).getJSONArray("LotPrizes"));
			System.out.println("LotteryCode: " + json.getJSONObject(sotinh).getString("LotteryCode"));
			String LotteryId = json.getJSONObject(sotinh).getString("LotteryId");
			JSONArray arrayKQ = json.getJSONObject(sotinh).getJSONArray("LotPrizes");
			
			Pattern  r1 = Pattern.compile(".* (\\d{2}/\\d{2}/\\d{4})");
			System.out.println(json.getJSONObject(0).getString("CrDateTime"));
		    Matcher  m1 = r1.matcher(json.getJSONObject(0).getString("CrDateTime"));
		    String ngay_quay_vn = "";
		    if(m1.find()){
				   ngay_quay_vn = m1.group(1);
				   System.out.println(ngay_quay_vn);
			   }
		    
		    SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
			Date date_tt = formatter1.parse(ngay_quay_vn);
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String ngay_quay = formatter.format(date_tt);
			System.out.println("ngay_quay:"+ngay_quay);
			
			int province_id = 0;
			province_id = getProvinceIdByMap(LotteryId);
			
			
			int i = 0;
			KQMN kqmb = new KQMN();
			String arrSplit[] = null;
			
			while(i<arrayKQ.size()){
				JSONObject object = (JSONObject)arrayKQ.get(i);
				System.out.println("Prize: " + object.getString("Prize"));
				System.out.println("Range: " + object.getString("Range"));
				String range = object.getString("Range").trim();
				
				switch (i) {
					case 0:
						kqmb.giai_tam = "...".equalsIgnoreCase(range)==true?" ":range;
						break;
					case 1:
						kqmb.giai_bay = "...".equalsIgnoreCase(range)==true?" ":range;;
						break;
					case 2:
						arrSplit = range.split("-");
						kqmb.giai_sau_1 = "...".equalsIgnoreCase(arrSplit[0].trim())==true?" ":arrSplit[0];
						kqmb.giai_sau_2 = "...".equalsIgnoreCase(arrSplit[1].trim())==true?" ":arrSplit[1];
						kqmb.giai_sau_3 = "...".equalsIgnoreCase(arrSplit[2].trim())==true?" ":arrSplit[2];
						break;
					case 3:
						arrSplit = range.split("-");
						kqmb.giai_nam = "...".equalsIgnoreCase(arrSplit[0].trim())==true?" ":arrSplit[0];
						break;
					case 4:
						arrSplit = range.split("-");
						kqmb.giai_tu_1 = "...".equalsIgnoreCase(arrSplit[0].trim())==true?" ":arrSplit[0];
						kqmb.giai_tu_2 = "...".equalsIgnoreCase(arrSplit[1].trim())==true?" ":arrSplit[1];
						kqmb.giai_tu_3 = "...".equalsIgnoreCase(arrSplit[2].trim())==true?" ":arrSplit[2];
						kqmb.giai_tu_4 = "...".equalsIgnoreCase(arrSplit[3].trim())==true?" ":arrSplit[3];
						kqmb.giai_tu_5 = "...".equalsIgnoreCase(arrSplit[4].trim())==true?" ":arrSplit[4];
						kqmb.giai_tu_6 = "...".equalsIgnoreCase(arrSplit[5].trim())==true?" ":arrSplit[5];
						kqmb.giai_tu_7 = "...".equalsIgnoreCase(arrSplit[6].trim())==true?" ":arrSplit[6];
						break;
					case 5:
						arrSplit = range.split("-");
						kqmb.giai_ba_1 = "...".equalsIgnoreCase(arrSplit[0].trim())==true?" ":arrSplit[0];
						kqmb.giai_ba_2 = "...".equalsIgnoreCase(arrSplit[1].trim())==true?" ":arrSplit[1];
						break;
					case 6:
						arrSplit = range.split("-");
						kqmb.giai_nhi = "...".equalsIgnoreCase(arrSplit[0].trim())==true?" ":arrSplit[0];
						break;
					case 7:
						arrSplit = range.split("-");
						kqmb.giai_nhat = "...".equalsIgnoreCase(arrSplit[0].trim())==true?" ":arrSplit[0];
						break;
					case 8:
						arrSplit = range.split("-");
						kqmb.giai_dacbiet = "...".equalsIgnoreCase(arrSplit[0].trim())==true?" ":arrSplit[0];
						break;
					default:
						break;
				}
				
				
				i++;
			}
			
			
			kqmb.province_id = province_id;
			kqmb.ngay_quay = ngay_quay;
			formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String ngay_tao = formatter.format(currentDate);
			kqmb.create_date = ngay_tao;
			kqmb.modify_date = ngay_tao;
			kqmb.create_user ="crawler";
			kqmb.modify_user ="crawler";
			
			kqmb.province_name=getProvinceNameByMap(province_id+"");
			
			listKQ.add(kqmb);
			
			int kq = w10hdao.checkKqMT(ngay_quay,province_id);
			if(kq==2){sotinh++;continue;};
			
			if(kq==0)
				w10hdao.saveKqMienTrung(kqmb);
			else
				if(kq==1)
				w10hdao.updateKqMienTrung(kqmb);
			
			
			sotinh++;
		}
		
		Pattern  r1 = Pattern.compile(".* (\\d{2}/\\d{2}/\\d{4})");
		System.out.println(json.getJSONObject(0).getString("CrDateTime"));
	    Matcher  m1 = r1.matcher(json.getJSONObject(0).getString("CrDateTime"));
	    String ngay_quay_vn = "";
	    if(m1.find()){
			   ngay_quay_vn = m1.group(1);
			   System.out.println(ngay_quay_vn);
		   }
		
		createFileJSONMT(listKQ,ngay_quay_vn);
		createFileTT_HTML_MT(listKQ);
		
	}
	
	
	
	public String getKey() throws Exception {
		String url ="http://xoso.tructiep.vn/website/truc-tiep-xo-so-mien-trung.aspx";
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		String html = HttpClientUtil.getResponseBody(res);
		Pattern r1  = Pattern.compile("var appKey='(\\w+)'");
		Matcher m1  = r1.matcher(html);
		String key = "";
		if(m1.find())
	      {
			  System.out.println("ok");
			  System.out.println("Found value: "+m1.group(1));
			  key = m1.group(1);
	      }else{
	    	  System.out.println("not ok");
	      }
		return key;
	}
	
	/*public void createFileJSONMB(KQMB kqmb,String ngay_quay_vn ){
		File file = new File("d://kq_mb.txt");
		String kq_str="[{\"ngay_quay\":\""+ngay_quay_vn+"\",\"province_id\":\""+kqmb.province_id+"\",\"giai_dacbiet\":\""+kqmb.giai_dacbiet+"\",\"giai_nhat\":\""+kqmb.giai_nhat+"\",\"giai_nhi\":\""+kqmb.giai_nhi_1+"-"+kqmb.giai_nhi_2+"\",\"giai_ba\":\""+kqmb.giai_ba_1+"-"+kqmb.giai_ba_2+"-"+kqmb.giai_ba_3+"-"+kqmb.giai_ba_4+"-"+kqmb.giai_ba_5+"-"+kqmb.giai_ba_6+"\",\"giai_tu\":\""+kqmb.giai_tu_1+"-"+kqmb.giai_tu_2+"-"+kqmb.giai_tu_3+"-"+kqmb.giai_tu_4+"\",\"giai_nam\":\""+kqmb.giai_nam_1+"-"+kqmb.giai_nam_2+"-"+kqmb.giai_nam_3+"-"+kqmb.giai_nam_4+"-"+kqmb.giai_nam_5+"-"+kqmb.giai_ba_6+"\",\"giai_sau\":\""+kqmb.giai_sau_1+"-"+kqmb.giai_sau_2+"-"+kqmb.giai_sau_3+"\",\"giai_bay\":\""+kqmb.giai_bay_1+"-"+kqmb.giai_bay_2+"-"+kqmb.giai_bay_3+"-"+kqmb.giai_bay_4+"\"}]";
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(kq_str.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void createFileJSONMN(ArrayList<KQMN> kqmns,String ngay_quay_vn ){
		File file = new File("d://kq_mn.txt");
		String kq_str="[";
		if(kqmns.size()>0){
			int i = 0;
			while(i<kqmns.size()){
				KQMN kqmn = kqmns.get(i);
				String kq = "{\"province_id\":\""+kqmn.province_id+"\",\"ngay_quay\":\""+ngay_quay_vn+"\",\"giai_dacbiet\":\""+kqmn.giai_dacbiet+"\",\"giai_nhat\":\""+kqmn.giai_nhat+"\",\"giai_nhi\":\""+kqmn.giai_nhi+"\",\"giai_ba\":\""+kqmn.giai_ba_1+"-"+kqmn.giai_ba_2+"\",\"giai_tu\":\""+kqmn.giai_tu_1+"-"+kqmn.giai_tu_2+"-"+kqmn.giai_tu_3+"-"+kqmn.giai_tu_4+"-"+kqmn.giai_tu_5+"-"+kqmn.giai_tu_6+"-"+kqmn.giai_tu_7+"\",\"giai_nam\":\""+kqmn.giai_nam+"\",\"giai_sau\":\""+kqmn.giai_sau_1+"-"+kqmn.giai_sau_2+"-"+kqmn.giai_sau_3+"\",\"giai_bay\":\""+kqmn.giai_bay+"\",\"giai_tam\":\""+kqmn.giai_tam+"\"}";
				if(i<kqmns.size()-1)
					kq_str +=kq+",";
				else
					kq_str +=kq+"";
				i++;
			}
		}
		kq_str+="]";
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(kq_str.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void createFileJSONMT(ArrayList<KQMN> kqmns,String ngay_quay_vn ){
		File file = new File("d://kq_mt.txt");
		String kq_str="[";
		if(kqmns.size()>0){
			int i = 0;
			while(i<kqmns.size()){
				KQMN kqmn = kqmns.get(i);
				String kq = "{\"province_id\":\""+kqmn.province_id+"\",\"ngay_quay\":\""+ngay_quay_vn+"\",\"giai_dacbiet\":\""+kqmn.giai_dacbiet+"\",\"giai_nhat\":\""+kqmn.giai_nhat+"\",\"giai_nhi\":\""+kqmn.giai_nhi+"\",\"giai_ba\":\""+kqmn.giai_ba_1+"-"+kqmn.giai_ba_2+"\",\"giai_tu\":\""+kqmn.giai_tu_1+"-"+kqmn.giai_tu_2+"-"+kqmn.giai_tu_3+"-"+kqmn.giai_tu_4+"-"+kqmn.giai_tu_5+"-"+kqmn.giai_tu_6+"-"+kqmn.giai_tu_7+"\",\"giai_nam\":\""+kqmn.giai_nam+"\",\"giai_sau\":\""+kqmn.giai_sau_1+"-"+kqmn.giai_sau_2+"-"+kqmn.giai_sau_3+"\",\"giai_bay\":\""+kqmn.giai_bay+"\",\"giai_tam\":\""+kqmn.giai_tam+"\"}";
				if(i<kqmns.size()-1)
					kq_str +=kq+",";
				else
					kq_str +=kq+"";
				i++;
			}
		}
		kq_str+="]";
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(kq_str.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
	
	public static void main(String[] args) throws ParseException {
		CrawlerDailyXoSoTrucTiep.filePID = "./conf/pidTrucTiep.txt";
		if(CrawlerDailyXoSoTrucTiep.existPID()) return; else CrawlerDailyXoSoTrucTiep.createPID();
		
		try {
			CrawlerDailyXoSoTrucTiep xoSoKQVS = new CrawlerDailyXoSoTrucTiep();
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
				xoSoKQVS.crawlerXSThanTai();
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			while(currentDate.after(date1MN)&&date2MN.after(currentDate)||runNowMN==1){
				try {
					xoSoKQVS.getKQMNJSON();
					Thread.sleep(1000);
					calendar = Calendar.getInstance();
					currentDate = calendar.getTime();
					System.out.println("----------------Nghi 1000 S-------------------------");
				} catch (Exception e) {
					e.printStackTrace();
					try {
						Thread.sleep(10);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
				
			}
			
			while(currentDate.after(date1MT)&&date2MT.after(currentDate)||runNowMT==1){
				try {
					xoSoKQVS.getKQMTJSON();
					Thread.sleep(1000);
					calendar = Calendar.getInstance();
					currentDate = calendar.getTime();
					System.out.println("----------------Nghi 1000 S-------------------------");
				} catch (Exception e) {
					e.printStackTrace();
					try {
						Thread.sleep(10);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
				
			}
			
			while(currentDate.after(date1MB)&&date2MB.after(currentDate)||runNowMB==1){
				try {
					xoSoKQVS.getKQMBJSON();
					Thread.sleep(1000);
					calendar = Calendar.getInstance();
					currentDate = calendar.getTime();
					System.out.println("----------------Nghi 1000 S-------------------------");
				} catch (Exception e) {
					e.printStackTrace();
					try {
						Thread.sleep(10);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			CrawlerDailyXoSoTrucTiep.deletePID();
		}
		
		
	}
}
