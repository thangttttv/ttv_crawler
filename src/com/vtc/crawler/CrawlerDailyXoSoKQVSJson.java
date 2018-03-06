package com.vtc.crawler;

import hdc.crawler.fetcher.HttpClientImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.http.HttpResponse;

import com.az24.crawler.model.KQMB;
import com.az24.crawler.model.KQMN;
import com.az24.dao.W10HDAO;
import com.az24.test.HttpClientUtil;

public class CrawlerDailyXoSoKQVSJson extends CrawlerDailyXoSo {
	
	public void crawlerXSMB() throws Exception {
		String url ="http://xoso.me/ttkq/json_kqmb.html?t=1487063592806";
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		String jsonTxt = HttpClientUtil.getResponseBody(res);
		System.out.println(jsonTxt);
		
		if(jsonTxt==null){
			url ="http://ketquaveso.com/ttkq/json_kqmb.html?t=1487063592806";
			client = new HttpClientImpl();
			res = client.fetch(url);
			jsonTxt = HttpClientUtil.getResponseBody(res);
		}
		
		JSONObject json = (JSONObject) JSONSerializer.toJSON(jsonTxt);
		W10HDAO w10hdao = new W10HDAO();
		
		System.out.println("KQ: " + json.getJSONObject("lotData"));
		System.out.println("KQ: " + json.getJSONObject("lotData").getJSONArray("1").get(0));
		System.out.println("KQ: " + json.getJSONObject("lotData").getJSONArray("2").get(0));
		System.out.println("KQ: " + json.getJSONObject("lotData").getJSONArray("2").get(1));
		
		long resultDate = json.getLong("resultDate");
		Date date_result = new Date(resultDate);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		String ngay_quay = formatter.format(date_result);
		System.out.println("ngay_quay:"+ngay_quay);
		int kq = w10hdao.checkKqMB(ngay_quay);
		
		KQMB kqmb = new KQMB();
		kqmb.province_id = 1;
		kqmb.giai_nhat = (String) json.getJSONObject("lotData").getJSONArray("1").get(0);
		
		kqmb.giai_nhi_1 = (String) json.getJSONObject("lotData").getJSONArray("2").get(0);
		kqmb.giai_nhi_2 = (String) json.getJSONObject("lotData").getJSONArray("2").get(1);
		
		kqmb.giai_ba_1 = (String) json.getJSONObject("lotData").getJSONArray("3").get(0);
		kqmb.giai_ba_2 = (String) json.getJSONObject("lotData").getJSONArray("3").get(1);
		kqmb.giai_ba_3 = (String) json.getJSONObject("lotData").getJSONArray("3").get(2);
		kqmb.giai_ba_4 = (String) json.getJSONObject("lotData").getJSONArray("3").get(3);
		kqmb.giai_ba_5 = (String) json.getJSONObject("lotData").getJSONArray("3").get(4);
		kqmb.giai_ba_6 = (String) json.getJSONObject("lotData").getJSONArray("3").get(5);
		
		kqmb.giai_tu_1 = (String) json.getJSONObject("lotData").getJSONArray("4").get(0);
		kqmb.giai_tu_2 = (String) json.getJSONObject("lotData").getJSONArray("4").get(1);
		kqmb.giai_tu_3 = (String) json.getJSONObject("lotData").getJSONArray("4").get(2);
		kqmb.giai_tu_4 = (String) json.getJSONObject("lotData").getJSONArray("4").get(3);
		
		kqmb.giai_nam_1 = (String) json.getJSONObject("lotData").getJSONArray("5").get(0);
		kqmb.giai_nam_2 = (String) json.getJSONObject("lotData").getJSONArray("5").get(1);
		kqmb.giai_nam_3 = (String) json.getJSONObject("lotData").getJSONArray("5").get(2);
		kqmb.giai_nam_4 = (String) json.getJSONObject("lotData").getJSONArray("5").get(3);
		kqmb.giai_nam_5 = (String) json.getJSONObject("lotData").getJSONArray("5").get(4);
		kqmb.giai_nam_6 = (String) json.getJSONObject("lotData").getJSONArray("5").get(5);
		
		kqmb.giai_sau_1 = (String) json.getJSONObject("lotData").getJSONArray("6").get(0);
		kqmb.giai_sau_2 = (String) json.getJSONObject("lotData").getJSONArray("6").get(1);
		kqmb.giai_sau_3 = (String) json.getJSONObject("lotData").getJSONArray("6").get(2);
		
		kqmb.giai_bay_1 = (String) json.getJSONObject("lotData").getJSONArray("7").get(0);
		kqmb.giai_bay_2 = (String) json.getJSONObject("lotData").getJSONArray("7").get(1);
		kqmb.giai_bay_3 = (String) json.getJSONObject("lotData").getJSONArray("7").get(2);
		kqmb.giai_bay_4 = (String) json.getJSONObject("lotData").getJSONArray("7").get(3);
		
		kqmb.giai_dacbiet = (String) json.getJSONObject("lotData").getJSONArray("DB").get(0);
		
		
		kqmb.ngay_quay = ngay_quay;
		
		formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String ngay_quay_1 = formatter.format(date_result);
		kqmb.create_date = ngay_quay_1;
		kqmb.modify_date = ngay_quay_1;
		kqmb.create_user ="crawler";
		kqmb.modify_user ="crawler";
		
		formatter = new SimpleDateFormat("dd/MM/yyyy");
		String ngay_quay_vn = formatter.format(date_result);
		createFileJSONMB(kqmb,ngay_quay_vn);
		createFileTT_HTML_MB(kqmb);
		
		if(kq==0)
			w10hdao.saveKqMienBac(kqmb);
		else
			w10hdao.update(kqmb,kqmb.ngay_quay);
		
		/*if(!StringUtil.isEmpty(kqmb.giai_dacbiet)){
			String notify = "KQXS miền Bắc ngày "+ngay_quay+" giải đặc biệt: "+kqmb.giai_dacbiet;
			if(w10hdao.checkNotify(1,ngay_quay)==0)
			w10hdao.saveNotify(1,ngay_quay,notify, 1);
			
		}*/
	}
	
	
	
	public void crawlerXSMT() throws Exception {
		String url ="http://xoso.me/ttkq/json_kqmt.html?t=1487063592806";
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		String jsonTxt = HttpClientUtil.getResponseBody(res);
		System.out.println(jsonTxt);
		
		JSONArray json = (JSONArray) JSONSerializer.toJSON(jsonTxt);
		W10HDAO w10hdao = new W10HDAO();
		ArrayList<KQMN> listKQ = new ArrayList<KQMN>();
		
		System.out.println("So Luong Tinh:"+json.size());
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		
		int i = 0;
		Date date_result = null;
		while(i<json.size()){
			KQMN kqmn = new KQMN();
			JSONObject ketquaTinh =  (JSONObject) json.get(i);
			kqmn.giai_nhat = (String)ketquaTinh.getJSONObject("lotData").getJSONArray("1").get(0);
			
			kqmn.giai_nhi = (String)ketquaTinh.getJSONObject("lotData").getJSONArray("2").get(0);
			
			kqmn.giai_ba_1 = (String)ketquaTinh.getJSONObject("lotData").getJSONArray("3").get(0);
			kqmn.giai_ba_2 = (String)ketquaTinh.getJSONObject("lotData").getJSONArray("3").get(1);
			
			kqmn.giai_tu_1 = (String)ketquaTinh.getJSONObject("lotData").getJSONArray("4").get(0);
			kqmn.giai_tu_2 = (String)ketquaTinh.getJSONObject("lotData").getJSONArray("4").get(1);
			kqmn.giai_tu_3 = (String)ketquaTinh.getJSONObject("lotData").getJSONArray("4").get(2);
			kqmn.giai_tu_4 = (String)ketquaTinh.getJSONObject("lotData").getJSONArray("4").get(3);
			kqmn.giai_tu_5 = (String)ketquaTinh.getJSONObject("lotData").getJSONArray("4").get(4);
			kqmn.giai_tu_6 = (String)ketquaTinh.getJSONObject("lotData").getJSONArray("4").get(5);
			kqmn.giai_tu_7 = (String)ketquaTinh.getJSONObject("lotData").getJSONArray("4").get(6);
			
			kqmn.giai_nam = (String)ketquaTinh.getJSONObject("lotData").getJSONArray("5").get(0);
			
			kqmn.giai_sau_1 = (String)ketquaTinh.getJSONObject("lotData").getJSONArray("6").get(0);
			kqmn.giai_sau_2 = (String)ketquaTinh.getJSONObject("lotData").getJSONArray("6").get(1);
			kqmn.giai_sau_3 = (String)ketquaTinh.getJSONObject("lotData").getJSONArray("6").get(2);
			
			kqmn.giai_bay = (String)ketquaTinh.getJSONObject("lotData").getJSONArray("7").get(0);
			kqmn.giai_tam = (String)ketquaTinh.getJSONObject("lotData").getJSONArray("8").get(0);
			kqmn.giai_dacbiet = (String)ketquaTinh.getJSONObject("lotData").getJSONArray("DB").get(0);
			
			long resultDate = ketquaTinh.getLong("resultDate");
			String provinceCode =  ketquaTinh.getString("provinceCode");
			
			int province_id = w10hdao.getProvinceIdByCode(provinceCode);
			System.out.println(provinceCode+":"+province_id);
			
			date_result = new Date(resultDate);
			
			
			
			formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String ngay_quay_1 = formatter.format(date_result);
			kqmn.create_date = ngay_quay_1;
			kqmn.modify_date = ngay_quay_1;
			kqmn.create_user ="crawler";
			kqmn.modify_user ="crawler";
			kqmn.province_id = province_id;
			kqmn.province_name = getProvinceNameByMap(province_id+"");
			
			
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			String ngay_quay = formatter.format(date_result);
			System.out.println("ngay_quay:"+ngay_quay);
			kqmn.ngay_quay = ngay_quay;
			
			int kq = w10hdao.checkKqMT(ngay_quay, province_id);
			
			listKQ.add(kqmn);
			
			if(kq==0)
				w10hdao.saveKqMienTrung(kqmn);
			else
				w10hdao.updateKqMienTrung(kqmn);
			
		/*	if(!StringUtil.isEmpty(kqmn.giai_dacbiet)){
				String notify = "KQXS Tỉnh " + kqmn.province_name +" ngày "+ngay_quay+" giải đặc biệt: "+kqmn.giai_dacbiet;
				
				if(w10hdao.checkNotify(kqmn.province_id,ngay_quay)==0)
				w10hdao.saveNotify(kqmn.province_id,ngay_quay,notify, 2);
				
			}*/
			i++;
		}
		
		formatter = new SimpleDateFormat("dd/MM/yyyy");
		String ngay_quay_vn = formatter.format(date_result);
		createFileJSONMT(listKQ,ngay_quay_vn);
		createFileTT_HTML_MT(listKQ);
		
	}
	
	public void crawlerXSMN() throws Exception {
		String url ="http://xoso.me/ttkq/json_kqmn.html?t=1487063592806";
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		String jsonTxt = HttpClientUtil.getResponseBody(res);
		System.out.println(jsonTxt);
		
		JSONArray json = (JSONArray) JSONSerializer.toJSON(jsonTxt);
		W10HDAO w10hdao = new W10HDAO();
		ArrayList<KQMN> listKQ = new ArrayList<KQMN>();
		
		System.out.println("So Luong Tinh:"+json.size());
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		
		int i = 0;
		Date date_result = null;
		while(i<json.size()){
			KQMN kqmn = new KQMN();
			JSONObject ketquaTinh =  (JSONObject) json.get(i);
			kqmn.giai_nhat = (String)ketquaTinh.getJSONObject("lotData").getJSONArray("1").get(0);
			
			kqmn.giai_nhi = (String)ketquaTinh.getJSONObject("lotData").getJSONArray("2").get(0);
			
			kqmn.giai_ba_1 = (String)ketquaTinh.getJSONObject("lotData").getJSONArray("3").get(0);
			kqmn.giai_ba_2 = (String)ketquaTinh.getJSONObject("lotData").getJSONArray("3").get(1);
			
			kqmn.giai_tu_1 = (String)ketquaTinh.getJSONObject("lotData").getJSONArray("4").get(0);
			kqmn.giai_tu_2 = (String)ketquaTinh.getJSONObject("lotData").getJSONArray("4").get(1);
			kqmn.giai_tu_3 = (String)ketquaTinh.getJSONObject("lotData").getJSONArray("4").get(2);
			kqmn.giai_tu_4 = (String)ketquaTinh.getJSONObject("lotData").getJSONArray("4").get(3);
			kqmn.giai_tu_5 = (String)ketquaTinh.getJSONObject("lotData").getJSONArray("4").get(4);
			kqmn.giai_tu_6 = (String)ketquaTinh.getJSONObject("lotData").getJSONArray("4").get(5);
			kqmn.giai_tu_7 = (String)ketquaTinh.getJSONObject("lotData").getJSONArray("4").get(6);
			
			kqmn.giai_nam = (String)ketquaTinh.getJSONObject("lotData").getJSONArray("5").get(0);
			
			kqmn.giai_sau_1 = (String)ketquaTinh.getJSONObject("lotData").getJSONArray("6").get(0);
			kqmn.giai_sau_2 = (String)ketquaTinh.getJSONObject("lotData").getJSONArray("6").get(1);
			kqmn.giai_sau_3 = (String)ketquaTinh.getJSONObject("lotData").getJSONArray("6").get(2);
			
			kqmn.giai_bay = (String)ketquaTinh.getJSONObject("lotData").getJSONArray("7").get(0);
			kqmn.giai_tam = (String)ketquaTinh.getJSONObject("lotData").getJSONArray("8").get(0);
			kqmn.giai_dacbiet = (String)ketquaTinh.getJSONObject("lotData").getJSONArray("DB").get(0);
			
			long resultDate = ketquaTinh.getLong("resultDate");
			String provinceCode =  ketquaTinh.getString("provinceCode");
			
			int province_id = w10hdao.getProvinceIdByCode(provinceCode);
			System.out.println(provinceCode+":"+province_id);
			
			date_result = new Date(resultDate);
			
			
			
			formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String ngay_quay_1 = formatter.format(date_result);
			kqmn.create_date = ngay_quay_1;
			kqmn.modify_date = ngay_quay_1;
			kqmn.create_user ="crawler";
			kqmn.modify_user ="crawler";
			kqmn.province_id = province_id;
			kqmn.province_name = getProvinceNameByMap(province_id+"");
			
			
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			String ngay_quay = formatter.format(date_result);
			System.out.println("ngay_quay:"+ngay_quay);
			kqmn.ngay_quay = ngay_quay;
			
			int kq = w10hdao.checkKqMN(ngay_quay, province_id);
			
			listKQ.add(kqmn);
			
			if(kq==0)
				w10hdao.saveKqMienNam(kqmn);
			else
				w10hdao.updateKqMienNam(kqmn);
			
			/*if(!StringUtil.isEmpty(kqmn.giai_dacbiet)){
				String notify = "KQXS Tỉnh " + kqmn.province_name +" ngày "+ngay_quay+" giải đặc biệt: "+kqmn.giai_dacbiet;
				
				if(w10hdao.checkNotify(kqmn.province_id,ngay_quay)==0)
				w10hdao.saveNotify(kqmn.province_id,ngay_quay,notify, 2);
				
			}*/
			i++;
		}
		
		formatter = new SimpleDateFormat("dd/MM/yyyy");
		String ngay_quay_vn = formatter.format(date_result);
		createFileJSONMN(listKQ,ngay_quay_vn);
		createFileTT_HTML_MN(listKQ);
		
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
	
	
	public static void main(String[] args) throws ParseException {
		CrawlerDailyXoSoKQVSJson.filePID = "./conf/pidKetQuaVeSo.txt";
		if(CrawlerDailyXoSoKQVSJson.existPID()) return; else CrawlerDailyXoSoKQVSJson.createPID();
		
		try {
			CrawlerDailyXoSoKQVSJson xoSoKQVS = new CrawlerDailyXoSoKQVSJson();
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
