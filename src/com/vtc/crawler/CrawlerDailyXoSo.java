package com.vtc.crawler;

import hdc.util.io.IOUtil;
import hdc.util.text.StringUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.az24.crawler.model.Feed;
import com.az24.crawler.model.KQMB;
import com.az24.crawler.model.KQMN;
import com.az24.db.pool.DBConfig;
import  com.az24.util.FileLog;

public class CrawlerDailyXoSo {
	protected KQMN getKQMienNamTheoTinh(String provinceCode,int vtTinh,int sotinh,List<String> arrG0,List<String> arrG1,List<String> arrG2,List<String> arrG3,List<String> arrG4,
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
	
	// VTT Giai (tinh tu 0) + sô lượng kq của giải (arrrG.lengt/so tỉnh) * (vttinh tính từ 0)
	protected KQMN getKQMienNamTheoTinhLoai2(String provinceCode,int vtTinh,int sotinh,List<String> arrG0,List<String> arrG1,List<String> arrG2,List<String> arrG3,List<String> arrG4,
			List<String> arrG5,List<String> arrG6,List<String> arrG7,List<String> arrG8)
	{
		KQMN kqmn = new KQMN();
		kqmn.province_id = 0;
		kqmn.giai_tam =String.valueOf(arrG8.get(0+(arrG8.size()/sotinh)*vtTinh));
		kqmn.giai_bay = String.valueOf(arrG7.get(0+(arrG7.size()/sotinh)*vtTinh));
		kqmn.giai_sau_1 =String.valueOf(arrG6.get(0+(arrG6.size()/sotinh)*vtTinh));
		kqmn.giai_sau_2 =String.valueOf(arrG6.get(1+(arrG6.size()/sotinh)*vtTinh));
		kqmn.giai_sau_3 =String.valueOf(arrG6.get(2+(arrG6.size()/sotinh)*vtTinh));
		kqmn.giai_nam =String.valueOf(arrG5.get(0+(arrG5.size()/sotinh)*vtTinh)); 
		
		kqmn.giai_tu_1 =String.valueOf(arrG4.get(0+(arrG4.size()/sotinh)*vtTinh));  
		kqmn.giai_tu_2 =String.valueOf(arrG4.get(1+(arrG4.size()/sotinh)*vtTinh));  
		kqmn.giai_tu_3 = String.valueOf(arrG4.get(2+(arrG4.size()/sotinh)*vtTinh));  
		kqmn.giai_tu_4 = String.valueOf(arrG4.get(3+(arrG4.size()/sotinh)*vtTinh));  
		kqmn.giai_tu_5 = String.valueOf(arrG4.get(4+(arrG4.size()/sotinh)*vtTinh));  
		kqmn.giai_tu_6 = String.valueOf(arrG4.get(5+(arrG4.size()/sotinh)*vtTinh));  
		kqmn.giai_tu_7 = String.valueOf(arrG4.get(6+(arrG4.size()/sotinh)*vtTinh));  
		
		kqmn.giai_ba_1 = String.valueOf(arrG3.get(0+(arrG3.size()/sotinh)*vtTinh));
		kqmn.giai_ba_2 = String.valueOf(arrG3.get(1+(arrG3.size()/sotinh)*vtTinh));
		
		kqmn.giai_nhi =String.valueOf(arrG2.get(0+(arrG2.size()/sotinh)*vtTinh));
		kqmn.giai_nhat =String.valueOf(arrG1.get(0+(arrG1.size()/sotinh)*vtTinh));  
		kqmn.giai_dacbiet = String.valueOf(arrG0.get(0+(arrG0.size()/sotinh)*vtTinh));   
		return kqmn;
	}
	
	protected KQMN initKQMienNamTheoTinh(int provinceId)
	{
		KQMN kqmn = new KQMN();
		kqmn.province_id = provinceId;
		kqmn.giai_tam = " ";
		kqmn.giai_bay = " ";
		kqmn.giai_sau_1 = " ";
		kqmn.giai_sau_2 = " ";
		kqmn.giai_sau_3 = " ";
		kqmn.giai_nam = " ";
		kqmn.giai_tu_1 = " ";
		kqmn.giai_tu_2 = " ";
		kqmn.giai_tu_3 = " ";
		kqmn.giai_tu_4 = " ";
		kqmn.giai_tu_5 = " ";
		kqmn.giai_tu_6 = " ";
		kqmn.giai_tu_7 = " ";
		kqmn.giai_ba_1 = " ";
		kqmn.giai_ba_2 = " ";
		kqmn.giai_nhi = " ";
		kqmn.giai_nhat = " ";
		kqmn.giai_dacbiet =" ";
		return kqmn;
	}
	
	
	protected String getKqGiai(int vitriTinh,int sttGiai,int soTinh,List<String> arrGiais){
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
	
	protected KQMB getKQMienBac(int vtTinh,int sotinh,List<String> arrG0,List<String> arrG1,List<String> arrG2,List<String> arrG3,List<String> arrG4,
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
	
	
	protected KQMB initKQMienBac()
	{
		KQMB kqmn = new KQMB();
		kqmn.province_id = 1;
		kqmn.giai_bay_1 = " ";
		kqmn.giai_bay_2 = " ";
		kqmn.giai_bay_3 = " ";
		kqmn.giai_bay_4 = " ";
		
		kqmn.giai_sau_1 = " ";
		kqmn.giai_sau_2 = " ";
		kqmn.giai_sau_3 = " ";
		
		kqmn.giai_nam_1 = " ";
		kqmn.giai_nam_2 = " ";
		kqmn.giai_nam_3 = " ";
		kqmn.giai_nam_4 = " ";
		kqmn.giai_nam_5 = " ";
		kqmn.giai_nam_6 = " ";
		
		kqmn.giai_tu_1 = " ";
		kqmn.giai_tu_2 = " ";
		kqmn.giai_tu_3 = " ";
		kqmn.giai_tu_4 = " ";
		
		kqmn.giai_ba_1 = " ";
		kqmn.giai_ba_2 = " ";
		kqmn.giai_ba_3 = " ";
		kqmn.giai_ba_4 = " ";
		kqmn.giai_ba_5 = " ";
		kqmn.giai_ba_6 = " ";
		
		kqmn.giai_nhi_1 = " ";
		kqmn.giai_nhi_2 = " ";
		
		kqmn.giai_nhat = " ";
		kqmn.giai_dacbiet = " ";
		return kqmn;
	}
	
	
	public void createFileJSONMB(KQMB kqmb,String ngay_quay_vn ){
		try {
			DBConfig.loadProperties();
			File file = new File(DBConfig.file_kqjson_mb);
			File file2 = new File(DBConfig.file_kqjson_mb_2);
			String kq_str="[{\"ngay_quay\":\""+ngay_quay_vn+"\",\"province_id\":\""+kqmb.province_id+"\",\"giai_dacbiet\":\""+kqmb.giai_dacbiet.trim()+"\",\"giai_nhat\":\""+kqmb.giai_nhat.trim()+"\",\"giai_nhi\":\""+kqmb.giai_nhi_1.trim()+"-"+kqmb.giai_nhi_2.trim()+"\",\"giai_ba\":\""+kqmb.giai_ba_1.trim()+"-"+kqmb.giai_ba_2.trim()+"-"+kqmb.giai_ba_3.trim()+"-"+kqmb.giai_ba_4.trim()+"-"+kqmb.giai_ba_5.trim()+"-"+kqmb.giai_ba_6.trim()+"\",\"giai_tu\":\""+kqmb.giai_tu_1.trim()+"-"+kqmb.giai_tu_2.trim()+"-"+kqmb.giai_tu_3.trim()+"-"+kqmb.giai_tu_4.trim()+"\",\"giai_nam\":\""+kqmb.giai_nam_1.trim()+"-"+kqmb.giai_nam_2.trim()+"-"+kqmb.giai_nam_3.trim()+"-"+kqmb.giai_nam_4.trim()+"-"+kqmb.giai_nam_5.trim()+"-"+kqmb.giai_nam_6.trim()+"\",\"giai_sau\":\""+kqmb.giai_sau_1.trim()+"-"+kqmb.giai_sau_2.trim()+"-"+kqmb.giai_sau_3.trim()+"\",\"giai_bay\":\""+kqmb.giai_bay_1.trim()+"-"+kqmb.giai_bay_2.trim()+"-"+kqmb.giai_bay_3.trim()+"-"+kqmb.giai_bay_4.trim()+"\"}]";
		
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(kq_str.getBytes());
			fileOutputStream.close();
			
			// comment voi 10h.vn
			FileOutputStream fileOutputStream2 = new FileOutputStream(file2);
			fileOutputStream2.write(kq_str.getBytes());
			fileOutputStream2.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void createFileJSONMBThanTai(KQMB kqmb,String ngay_quay_vn ){
		try {
			DBConfig.loadProperties();
			File file = new File(DBConfig.file_kqjson_mb);
			String kq_str="[{\"ngay_quay\":\""+ngay_quay_vn+"\",\"province_id\":\""+kqmb.province_id+"\",\"giai_dacbiet\":\""+kqmb.giai_dacbiet.trim()+"\",\"giai_nhat\":\""+kqmb.giai_nhat.trim()+"\",\"giai_nhi\":\""+kqmb.giai_nhi_1.trim()+"-"+kqmb.giai_nhi_2.trim()+"\",\"giai_ba\":\""+kqmb.giai_ba_1.trim()+"-"+kqmb.giai_ba_2.trim()+"-"+kqmb.giai_ba_3.trim()+"-"+kqmb.giai_ba_4.trim()+"-"+kqmb.giai_ba_5.trim()+"-"+kqmb.giai_ba_6.trim()+"\",\"giai_tu\":\""+kqmb.giai_tu_1.trim()+"-"+kqmb.giai_tu_2.trim()+"-"+kqmb.giai_tu_3.trim()+"-"+kqmb.giai_tu_4.trim()+"\",\"giai_nam\":\""+kqmb.giai_nam_1.trim()+"-"+kqmb.giai_nam_2.trim()+"-"+kqmb.giai_nam_3.trim()+"-"+kqmb.giai_nam_4.trim()+"-"+kqmb.giai_nam_5.trim()+"-"+kqmb.giai_nam_6.trim()+"\",\"giai_sau\":\""+kqmb.giai_sau_1.trim()+"-"+kqmb.giai_sau_2.trim()+"-"+kqmb.giai_sau_3.trim()+"\",\"giai_bay\":\""+kqmb.giai_bay_1.trim()+"-"+kqmb.giai_bay_2.trim()+"-"+kqmb.giai_bay_3.trim()+"-"+kqmb.giai_bay_4.trim()+"\"}]";
		
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(kq_str.getBytes());
			fileOutputStream.close();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void createFileFeed(ArrayList<Feed> feeds ){
		try {
			DBConfig.loadProperties();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String kq_str="<script type=\"text/javascript\" src=\"http://10h.vn/themes/js/jquery.cycle.all.js\"></script>";
		
		kq_str+="<script type=\"text/javascript\">";
		kq_str+="$(document).ready(function(){ ";
		kq_str+=" $('#slideshow').cycle({";
		kq_str+=" timeout: 3000, ";
		kq_str+=" cleartype: 1, ";
		kq_str+=" speed: 400,}); ";
		kq_str+=" $('#slideshow').cycle({ cleartype:  false }); ";
		kq_str+=" }); ";
		kq_str+="</script>\r\n";
		
		kq_str+="<div id=\"slideshow\" style=\"width: 500px;\">\r\n";
		if(feeds.size()>0){
			int i = 0;
			while(i<feeds.size()){
				Feed feed = feeds.get(i);
				kq_str+="<div class=\"feed\">\r\n";
				kq_str+="<a href='"+feed.url+"'>\r\n";
				kq_str+="<img src='"+feed.image+"' class='img_Note' alt='"+feed.title+"'>&nbsp;\r\n";
				kq_str+=feed.title;
				kq_str+="</a>\r\n";
				kq_str+="</div>\r\n";
				
				i++;
			}
		}
		kq_str+="</div>";
		try {
			Writer out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(DBConfig.file_feed), "UTF8"));
				out.write(kq_str, 0, kq_str.length());
				out.flush();
				out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void createFileFeedMobile(ArrayList<Feed> feeds ){
		try {
			DBConfig.loadProperties();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String kq_str="<script type=\"text/javascript\" src=\"http://10h.vn/themes/js/jquery.cycle.all.js\"></script>";
		
		kq_str+="<script type=\"text/javascript\">";
		kq_str+="$(document).ready(function(){ ";
		kq_str+=" $('#slideshow').cycle({";
		kq_str+=" timeout: 3000, ";
		kq_str+=" cleartype: 1, ";
		kq_str+=" speed: 400,}); ";
		kq_str+=" $('#slideshow').cycle({ cleartype:  false }); ";
		kq_str+=" }); ";
		kq_str+="</script>\r\n";
		
		kq_str+="<div id=\"slideshow\" style=\"width: 500px;\">\r\n";
		if(feeds.size()>0){
			int i = 0;
			while(i<feeds.size()){
				Feed feed = feeds.get(i);
				kq_str+="<div class=\"feed\">\r\n";
				kq_str+="<a href='"+feed.url+"'>\r\n";
				kq_str+="<img src='"+feed.image+"' class='img_Note' alt='"+feed.title+"'>&nbsp;\r\n";
				kq_str+=feed.title;
				kq_str+="</a>\r\n";
				kq_str+="</div>\r\n";
				
				i++;
			}
		}
		kq_str+="</div>";
		try {
			Writer out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(DBConfig.file_feed_mobile), "UTF8"));
				out.write(kq_str, 0, kq_str.length());
				out.flush();
				out.close();
				
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void createFileJSONMN(ArrayList<KQMN> kqmns,String ngay_quay_vn ){
		try {
			DBConfig.loadProperties();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		File file = new File(DBConfig.file_kqjson_mn);
		File file2 = new File(DBConfig.file_kqjson_mn_2);
		String kq_str="[";
		if(kqmns.size()>0){
			int i = 0;
			while(i<kqmns.size()){
				KQMN kqmn = kqmns.get(i);
				String kq = "{\"province_id\":\""+kqmn.province_id+"\",\"ngay_quay\":\""+ngay_quay_vn+"\",\"giai_dacbiet\":\""+kqmn.giai_dacbiet.trim()+"\",\"giai_nhat\":\""+kqmn.giai_nhat.trim()+"\",\"giai_nhi\":\""+kqmn.giai_nhi.trim()+"\",\"giai_ba\":\""+kqmn.giai_ba_1.trim()+"-"+kqmn.giai_ba_2.trim()+"\",\"giai_tu\":\""+kqmn.giai_tu_1.trim()+"-"+kqmn.giai_tu_2.trim()+"-"+kqmn.giai_tu_3.trim()+"-"+kqmn.giai_tu_4.trim()+"-"+kqmn.giai_tu_5.trim()+"-"+kqmn.giai_tu_6.trim()+"-"+kqmn.giai_tu_7.trim()+"\",\"giai_nam\":\""+kqmn.giai_nam.trim()+"\",\"giai_sau\":\""+kqmn.giai_sau_1.trim()+"-"+kqmn.giai_sau_2.trim()+"-"+kqmn.giai_sau_3.trim()+"\",\"giai_bay\":\""+kqmn.giai_bay.trim()+"\",\"giai_tam\":\""+kqmn.giai_tam.trim()+"\"}";
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
			fileOutputStream.close();
			// comment voi 10h.vn
			FileOutputStream fileOutputStream2 = new FileOutputStream(file2);
			fileOutputStream2.write(kq_str.getBytes());
			fileOutputStream2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void createFileJSONMNThanTai(ArrayList<KQMN> kqmns,String ngay_quay_vn ){
		try {
			DBConfig.loadProperties();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		File file = new File(DBConfig.file_kqjson_mn);
		String kq_str="[";
		if(kqmns.size()>0){
			int i = 0;
			while(i<kqmns.size()){
				KQMN kqmn = kqmns.get(i);
				String kq = "{\"province_id\":\""+kqmn.province_id+"\",\"ngay_quay\":\""+ngay_quay_vn+"\",\"giai_dacbiet\":\""+kqmn.giai_dacbiet.trim()+"\",\"giai_nhat\":\""+kqmn.giai_nhat.trim()+"\",\"giai_nhi\":\""+kqmn.giai_nhi.trim()+"\",\"giai_ba\":\""+kqmn.giai_ba_1.trim()+"-"+kqmn.giai_ba_2.trim()+"\",\"giai_tu\":\""+kqmn.giai_tu_1.trim()+"-"+kqmn.giai_tu_2.trim()+"-"+kqmn.giai_tu_3.trim()+"-"+kqmn.giai_tu_4.trim()+"-"+kqmn.giai_tu_5.trim()+"-"+kqmn.giai_tu_6.trim()+"-"+kqmn.giai_tu_7.trim()+"\",\"giai_nam\":\""+kqmn.giai_nam.trim()+"\",\"giai_sau\":\""+kqmn.giai_sau_1.trim()+"-"+kqmn.giai_sau_2.trim()+"-"+kqmn.giai_sau_3.trim()+"\",\"giai_bay\":\""+kqmn.giai_bay.trim()+"\",\"giai_tam\":\""+kqmn.giai_tam.trim()+"\"}";
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
			fileOutputStream.close();
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * @param kqmns
	 * @param ngay_quay_vn
	 */
	public void createFileJSONMT(ArrayList<KQMN> kqmns,String ngay_quay_vn ){
		try {
			DBConfig.loadProperties();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		File file = new File(DBConfig.file_kqjson_mt);
		File file2 = new File(DBConfig.file_kqjson_mt_2);
		String kq_str="[";
		if(kqmns.size()>0){
			int i = 0;
			while(i<kqmns.size()){
				KQMN kqmn = kqmns.get(i);
				String kq = "{\"province_id\":\""+kqmn.province_id+"\",\"ngay_quay\":\""+ngay_quay_vn+"\",\"giai_dacbiet\":\""+kqmn.giai_dacbiet.trim()+"\",\"giai_nhat\":\""+kqmn.giai_nhat.trim()+"\",\"giai_nhi\":\""+kqmn.giai_nhi.trim()+"\",\"giai_ba\":\""+kqmn.giai_ba_1.trim()+"-"+kqmn.giai_ba_2.trim()+"\",\"giai_tu\":\""+kqmn.giai_tu_1.trim()+"-"+kqmn.giai_tu_2.trim()+"-"+kqmn.giai_tu_3.trim()+"-"+kqmn.giai_tu_4.trim()+"-"+kqmn.giai_tu_5.trim()+"-"+kqmn.giai_tu_6.trim()+"-"+kqmn.giai_tu_7.trim()+"\",\"giai_nam\":\""+kqmn.giai_nam.trim()+"\",\"giai_sau\":\""+kqmn.giai_sau_1.trim()+"-"+kqmn.giai_sau_2.trim()+"-"+kqmn.giai_sau_3.trim()+"\",\"giai_bay\":\""+kqmn.giai_bay.trim()+"\",\"giai_tam\":\""+kqmn.giai_tam.trim()+"\"}";
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
			fileOutputStream.close();
			
			// comment voi 10h.vn
			FileOutputStream fileOutputStream2 = new FileOutputStream(file2);
			fileOutputStream2.write(kq_str.getBytes());
			fileOutputStream2.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void createFileJSONMTThanTai(ArrayList<KQMN> kqmns,String ngay_quay_vn ){
		try {
			DBConfig.loadProperties();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		File file = new File(DBConfig.file_kqjson_mt);
		
		String kq_str="[";
		if(kqmns.size()>0){
			int i = 0;
			while(i<kqmns.size()){
				KQMN kqmn = kqmns.get(i);
				String kq = "{\"province_id\":\""+kqmn.province_id+"\",\"ngay_quay\":\""+ngay_quay_vn+"\",\"giai_dacbiet\":\""+kqmn.giai_dacbiet.trim()+"\",\"giai_nhat\":\""+kqmn.giai_nhat.trim()+"\",\"giai_nhi\":\""+kqmn.giai_nhi.trim()+"\",\"giai_ba\":\""+kqmn.giai_ba_1.trim()+"-"+kqmn.giai_ba_2.trim()+"\",\"giai_tu\":\""+kqmn.giai_tu_1.trim()+"-"+kqmn.giai_tu_2.trim()+"-"+kqmn.giai_tu_3.trim()+"-"+kqmn.giai_tu_4.trim()+"-"+kqmn.giai_tu_5.trim()+"-"+kqmn.giai_tu_6.trim()+"-"+kqmn.giai_tu_7.trim()+"\",\"giai_nam\":\""+kqmn.giai_nam.trim()+"\",\"giai_sau\":\""+kqmn.giai_sau_1.trim()+"-"+kqmn.giai_sau_2.trim()+"-"+kqmn.giai_sau_3.trim()+"\",\"giai_bay\":\""+kqmn.giai_bay.trim()+"\",\"giai_tam\":\""+kqmn.giai_tam.trim()+"\"}";
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
			fileOutputStream.close();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Integer> getProvinceOpenByRegio(int region)
    {
		ArrayList<Integer> arrProvince = new ArrayList<Integer>();
        Calendar calendar = Calendar.getInstance();
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK);
        
        switch(day_of_week)   {
            case Calendar.SUNDAY:
                 if(region==2){
                	 arrProvince.add(28);
                	 arrProvince.add(29);
                 }else{
                     arrProvince.add(10);
                     arrProvince.add(15);
                     arrProvince.add(19);
                 }
                 break;
            
            case Calendar.MONDAY: // thu 2
                if(region==2){
                 arrProvince.add(31);
                 arrProvince.add(36);
                 }else{
                     arrProvince.add(14);
                     arrProvince.add(12);
                     arrProvince.add(8);
                 }
                 break;
            
            case Calendar.TUESDAY:// thu 3
                if(region==2){
                 arrProvince.add(25);
                 arrProvince.add(34);
                 }else{
                	 arrProvince.add(3);
                	 arrProvince.add(4);
                	 arrProvince.add(22);
                 }
                 break;
            case Calendar.WEDNESDAY: // thu 4
                if(region==2){
                 arrProvince.add(24);
                 arrProvince.add(28);
                 }else{
                	 arrProvince.add(9);
                	 arrProvince.add(11);
                	 arrProvince.add(17);
                 }
                 break;
            case Calendar.THURSDAY:   // thu 5
                if(region==2){
                	  arrProvince.add(23);
                	  arrProvince.add(32);
                	  arrProvince.add(35);
                 }else{
                	 arrProvince.add(2);
                	 arrProvince.add(7);
                	 arrProvince.add(18);
                      
                 }
                 break;
            case Calendar.FRIDAY:   // thu 6
                if(region==2){
                	arrProvince.add(27);
                	arrProvince.add(30);
                 }else{
                	 arrProvince.add(5);
                	 arrProvince.add(20);
                	 arrProvince.add(21);
                 }
                 break;
             case Calendar.SATURDAY: // thu 7
                if(region==2){
                	arrProvince.add(24);
                	arrProvince.add(26);
                	arrProvince.add(33);
                 }else{
                	 arrProvince.add(6);
                	 arrProvince.add(13);
                	 arrProvince.add(14);
                	 arrProvince.add(16);
                 }
                 break;
        }
        return  arrProvince;
    }
	
	public int getProvinceIdByMap(String provinceTTID){
		int provinceId = 0;
		
		Map<String, String> mapProvince = new HashMap<String, String>();
		mapProvince.put("0", "1"); // Mien Bac
		mapProvince.put("20", "2"); // An Giang
		mapProvince.put("17", "3"); // Bạc Liêu
		mapProvince.put("16", "4"); // Bến Tre
		mapProvince.put("24", "5"); // Bình Dương
		mapProvince.put("27", "6"); // Bình Phước
		mapProvince.put("22", "7"); // Bình Thuận
		mapProvince.put("15", "8"); // Cà Mau
		mapProvince.put("11", "9"); // Cần Thơ
		mapProvince.put("31", "10"); // Đà Lạt
		mapProvince.put("19", "11"); // Đồng Nai
		mapProvince.put("13", "12"); // Đồng Tháp
		mapProvince.put("28", "13"); // Hậu Giang
		mapProvince.put("14", "14"); // Hồ Chí Minh
		mapProvince.put("29", "15"); // Kiên Giang
		mapProvince.put("26", "16"); // Long An
		mapProvince.put("18", "17"); // Sóc Trăng
		mapProvince.put("21", "18"); // Tây Ninh
		mapProvince.put("30", "19"); // Tiền Giang
		mapProvince.put("25", "20"); // Trà Vinh
		mapProvince.put("23", "21"); // Vĩnh Long
		mapProvince.put("10", "22"); // Vũng Tàu
		mapProvince.put("38", "23"); // Bình Định
		mapProvince.put("37", "24"); // Đà Nẵng
		mapProvince.put("34", "25"); // Đắc Lắc
		mapProvince.put("44", "26"); // Đắc Nông
		mapProvince.put("42", "27"); // Gia Lai
		mapProvince.put("36", "28"); // Khánh Hòa
		mapProvince.put("45", "29"); // Kon Tum
		mapProvince.put("41", "30"); // Ninh Thuận
		mapProvince.put("33", "31"); // Phú Yên
		mapProvince.put("39", "32"); // Quảng Bình
		mapProvince.put("43", "33"); // Quảng Ngãi
		mapProvince.put("35", "34"); // Quảng Nam
		mapProvince.put("40", "35"); // Quảng Trị
		mapProvince.put("32", "36"); // Thừa Thiên Huế
		/*mapProvince.put("20", "37"); // Hà Nội
		mapProvince.put("20", "38"); // Quảng Ninh
		mapProvince.put("20", "39"); // Bắc Ninh
		mapProvince.put("20", "40"); // Hải Phòng
		mapProvince.put("20", "41"); // Nam Định
		mapProvince.put("20", "42"); // Thái Bình*/
		provinceId = Integer.parseInt(mapProvince.get(provinceTTID));
		return provinceId;
	}
	
	public String getProvinceNameByMap(String province_id){
		String provinceName = "";
		
		Map<String, String> mapProvince = new HashMap<String, String>();
		mapProvince.put("1", "Miền Bắc"); // Mien Bac
		mapProvince.put("2", "An Giang"); // An Giang
		mapProvince.put("3", "Bạc Liêu"); // Bạc Liêu
		mapProvince.put("4", "Bến Tre"); // Bến Tre
		mapProvince.put("5", "Bình Dương"); // Bình Dương
		mapProvince.put("6", "Bình Phước"); // Bình Phước
		mapProvince.put("7", "Bình Thuận"); // Bình Thuận
		mapProvince.put("8", "Cà Mau"); // Cà Mau
		mapProvince.put("9", "Cần Thơ"); // Cần Thơ
		mapProvince.put("10", "Đà Lạt"); // Đà Lạt
		mapProvince.put("11", "Đồng Nai"); // Đồng Nai
		mapProvince.put("12", "Đồng Tháp"); // Đồng Tháp
		mapProvince.put("13", "Hậu Giang"); // Hậu Giang
		mapProvince.put("14", "Hồ Chí Minh"); // Hồ Chí Minh
		mapProvince.put("15", "Kiên Giang"); // Kiên Giang
		mapProvince.put("16", "Long An"); // Long An
		mapProvince.put("17", "Sóc Trăng"); // Sóc Trăng
		mapProvince.put("18", "Tây Ninh"); // Tây Ninh
		mapProvince.put("19", "Tiền Giang"); // Tiền Giang
		mapProvince.put("20", "Trà Vinh"); // Trà Vinh
		mapProvince.put("21", "Vĩnh Long"); // Vĩnh Long
		mapProvince.put("22", "Vũng Tàu"); // Vũng Tàu
		mapProvince.put("23", "Bình Định"); // Bình Định
		mapProvince.put("24", "Đà Nẵng"); // Đà Nẵng
		mapProvince.put("25", "Đắc Lắc"); // Đắc Lắc
		mapProvince.put("26", "Đắc Nông"); // Đắc Nông
		mapProvince.put("27", "Gia Lai"); // Gia Lai
		mapProvince.put("28", "Khánh Hòa"); // Khánh Hòa
		mapProvince.put("29", "Kon Tum"); // Kon Tum
		mapProvince.put("30", "Ninh Thuận"); // Ninh Thuận
		mapProvince.put("31", "Phú Yên"); // Phú Yên
		mapProvince.put("32", "Quảng Bình"); // Quảng Bình
		mapProvince.put("33", "Quảng Ngãi"); // Quảng Ngãi
		mapProvince.put("34", "Quảng Nam"); // Quảng Nam
		mapProvince.put("35", "Quảng Trị"); // Quảng Trị
		mapProvince.put("36", "Thừa Thiên Huế"); // Thừa Thiên Huế
		/*mapProvince.put("20", "Hà Nội"); // Hà Nội
		mapProvince.put("20", "Quảng Ninh"); // Quảng Ninh
		mapProvince.put("20", "Bắc Ninh"); // Bắc Ninh
		mapProvince.put("20", "Hải Phòng"); // Hải Phòng
		mapProvince.put("20", "Nam Định"); // Nam Định
		mapProvince.put("20", "Thái Bình"); // Thái Bình*/
		provinceName = mapProvince.get(province_id);
		
		return provinceName;
	}
	
	public void createFileTT_HTML_MB(KQMB kqmb){
		try {
			String htmlMB = IOUtil.getFileContenntAsString("./conf/MienBac.html","UTF8");
			String imageloading ="<img style=\"width: 20px;margin-top: 10px;margin-bottom: 10px;\" src=\"http://10h.vn/themes/images/loading.gif\">";
			
			htmlMB=htmlMB.replaceAll("NGAY_QUAY",kqmb.ngay_quay);
			
			htmlMB=htmlMB.replaceAll("TTMB_GIAIDACBIET",StringUtil.isEmpty(kqmb.giai_dacbiet)?imageloading:kqmb.giai_dacbiet);
			htmlMB=htmlMB.replaceAll("TTMB_GIAINHAT",StringUtil.isEmpty(kqmb.giai_nhat)?imageloading:kqmb.giai_nhat);
			htmlMB=htmlMB.replaceAll("TTMB_GIAINHI_1",StringUtil.isEmpty(kqmb.giai_nhi_1)?imageloading:kqmb.giai_nhi_1);
			htmlMB=htmlMB.replaceAll("TTMB_GIAINHI_2",StringUtil.isEmpty(kqmb.giai_nhi_2)?imageloading:kqmb.giai_nhi_2);
			
			htmlMB=htmlMB.replaceAll("TTMB_GIAIBA_1",StringUtil.isEmpty(kqmb.giai_ba_1)?imageloading:kqmb.giai_ba_1);
			htmlMB=htmlMB.replaceAll("TTMB_GIAIBA_2",StringUtil.isEmpty(kqmb.giai_ba_2)?imageloading:kqmb.giai_ba_2);
			htmlMB=htmlMB.replaceAll("TTMB_GIAIBA_3",StringUtil.isEmpty(kqmb.giai_ba_3)?imageloading:kqmb.giai_ba_3);
			htmlMB=htmlMB.replaceAll("TTMB_GIAIBA_4",StringUtil.isEmpty(kqmb.giai_ba_4)?imageloading:kqmb.giai_ba_4);
			htmlMB=htmlMB.replaceAll("TTMB_GIAIBA_5",StringUtil.isEmpty(kqmb.giai_ba_5)?imageloading:kqmb.giai_ba_5);
			htmlMB=htmlMB.replaceAll("TTMB_GIAIBA_6",StringUtil.isEmpty(kqmb.giai_ba_6)?imageloading:kqmb.giai_ba_6);
			
			htmlMB=htmlMB.replaceAll("TTMB_GIAITU_1",StringUtil.isEmpty(kqmb.giai_tu_1)?imageloading:kqmb.giai_tu_1);
			htmlMB=htmlMB.replaceAll("TTMB_GIAITU_2",StringUtil.isEmpty(kqmb.giai_tu_2)?imageloading:kqmb.giai_tu_2);
			htmlMB=htmlMB.replaceAll("TTMB_GIAITU_3",StringUtil.isEmpty(kqmb.giai_tu_3)?imageloading:kqmb.giai_tu_3);
			htmlMB=htmlMB.replaceAll("TTMB_GIAITU_4",StringUtil.isEmpty(kqmb.giai_tu_4)?imageloading:kqmb.giai_tu_4);
			
			htmlMB=htmlMB.replaceAll("TTMB_GIAINAM_1",StringUtil.isEmpty(kqmb.giai_nam_1)?imageloading:kqmb.giai_nam_1);
			htmlMB=htmlMB.replaceAll("TTMB_GIAINAM_2",StringUtil.isEmpty(kqmb.giai_nam_2)?imageloading:kqmb.giai_nam_2);
			htmlMB=htmlMB.replaceAll("TTMB_GIAINAM_3",StringUtil.isEmpty(kqmb.giai_nam_3)?imageloading:kqmb.giai_nam_3);
			htmlMB=htmlMB.replaceAll("TTMB_GIAINAM_4",StringUtil.isEmpty(kqmb.giai_nam_4)?imageloading:kqmb.giai_nam_4);
			htmlMB=htmlMB.replaceAll("TTMB_GIAINAM_5",StringUtil.isEmpty(kqmb.giai_nam_5)?imageloading:kqmb.giai_nam_5);
			htmlMB=htmlMB.replaceAll("TTMB_GIAINAM_6",StringUtil.isEmpty(kqmb.giai_nam_6)?imageloading:kqmb.giai_nam_6);
			
			htmlMB=htmlMB.replaceAll("TTMB_GIAISAU_1",StringUtil.isEmpty(kqmb.giai_sau_1)?imageloading:kqmb.giai_sau_1);
			htmlMB=htmlMB.replaceAll("TTMB_GIAISAU_2",StringUtil.isEmpty(kqmb.giai_sau_2)?imageloading:kqmb.giai_sau_2);
			htmlMB=htmlMB.replaceAll("TTMB_GIAISAU_3",StringUtil.isEmpty(kqmb.giai_sau_3)?imageloading:kqmb.giai_sau_3);
			
			htmlMB=htmlMB.replaceAll("TTMB_GIAIBAY_1",StringUtil.isEmpty(kqmb.giai_bay_1)?imageloading:kqmb.giai_bay_1);
			htmlMB=htmlMB.replaceAll("TTMB_GIAIBAY_2",StringUtil.isEmpty(kqmb.giai_bay_2)?imageloading:kqmb.giai_bay_2);
			htmlMB=htmlMB.replaceAll("TTMB_GIAIBAY_3",StringUtil.isEmpty(kqmb.giai_bay_3)?imageloading:kqmb.giai_bay_3);
			htmlMB=htmlMB.replaceAll("TTMB_GIAIBAY_4",StringUtil.isEmpty(kqmb.giai_bay_4)?imageloading:kqmb.giai_bay_4);
			
			ArrayList<String> arrLoto = new ArrayList<String>();
			if(!StringUtil.isEmpty(kqmb.giai_nhat))arrLoto.add(kqmb.giai_nhat.substring(kqmb.giai_nhat.length()-2)); 
			if(!StringUtil.isEmpty(kqmb.giai_nhi_1))arrLoto.add(kqmb.giai_nhi_1.substring(kqmb.giai_nhi_1.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_nhi_2))arrLoto.add(kqmb.giai_nhi_2.substring(kqmb.giai_nhi_2.length()-2));
			
			if(!StringUtil.isEmpty(kqmb.giai_ba_1))arrLoto.add(kqmb.giai_ba_1.substring(kqmb.giai_ba_1.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_ba_2))arrLoto.add(kqmb.giai_ba_2.substring(kqmb.giai_ba_2.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_ba_3))arrLoto.add(kqmb.giai_ba_3.substring(kqmb.giai_ba_3.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_ba_4))arrLoto.add(kqmb.giai_ba_4.substring(kqmb.giai_ba_4.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_ba_5))arrLoto.add(kqmb.giai_ba_5.substring(kqmb.giai_ba_5.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_ba_6))arrLoto.add(kqmb.giai_ba_6.substring(kqmb.giai_ba_6.length()-2));
			
			if(!StringUtil.isEmpty(kqmb.giai_tu_1))arrLoto.add(kqmb.giai_tu_1.substring(kqmb.giai_tu_1.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_tu_2))arrLoto.add(kqmb.giai_tu_2.substring(kqmb.giai_tu_2.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_tu_3))arrLoto.add(kqmb.giai_tu_3.substring(kqmb.giai_tu_3.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_tu_4))arrLoto.add(kqmb.giai_tu_4.substring(kqmb.giai_tu_4.length()-2));
			
			if(!StringUtil.isEmpty(kqmb.giai_nam_1))arrLoto.add(kqmb.giai_nam_1.substring(kqmb.giai_nam_1.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_nam_2))arrLoto.add(kqmb.giai_nam_2.substring(kqmb.giai_nam_2.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_nam_3))arrLoto.add(kqmb.giai_nam_3.substring(kqmb.giai_nam_3.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_nam_4))arrLoto.add(kqmb.giai_nam_4.substring(kqmb.giai_nam_4.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_nam_5))arrLoto.add(kqmb.giai_nam_5.substring(kqmb.giai_nam_5.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_nam_6))arrLoto.add(kqmb.giai_nam_6.substring(kqmb.giai_nam_6.length()-2));
			
			if(!StringUtil.isEmpty(kqmb.giai_sau_1))arrLoto.add(kqmb.giai_sau_1.substring(kqmb.giai_sau_1.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_sau_2))arrLoto.add(kqmb.giai_sau_2.substring(kqmb.giai_sau_2.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_sau_3))arrLoto.add(kqmb.giai_sau_3.substring(kqmb.giai_sau_3.length()-2));
			
			if(!StringUtil.isEmpty(kqmb.giai_bay_1))arrLoto.add(kqmb.giai_bay_1.substring(kqmb.giai_bay_1.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_bay_2))arrLoto.add(kqmb.giai_bay_2.substring(kqmb.giai_bay_2.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_bay_3))arrLoto.add(kqmb.giai_bay_3.substring(kqmb.giai_bay_3.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_bay_4))arrLoto.add(kqmb.giai_bay_4.substring(kqmb.giai_bay_4.length()-2));
			
			if(!StringUtil.isEmpty(kqmb.giai_dacbiet))arrLoto.add(kqmb.giai_dacbiet.substring(kqmb.giai_dacbiet.length()-2)); 
			
			String dau_0 = getDauDuoi(0,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_0",dau_0);
			String dau_1 = getDauDuoi(1,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_1",dau_1);
			String dau_2 = getDauDuoi(2,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_2",dau_2);
			String dau_3 = getDauDuoi(3,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_3",dau_3);
			String dau_4 = getDauDuoi(4,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_4",dau_4);
			String dau_5 = getDauDuoi(5,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_5",dau_5);
			String dau_6 = getDauDuoi(6,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_6",dau_6);
			String dau_7 = getDauDuoi(7,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_7",dau_7);
			String dau_8 = getDauDuoi(8,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_8",dau_8);
			String dau_9 = getDauDuoi(9,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_9",dau_9);
			
			sortLoto(arrLoto);
			String strlotott ="";
			for (String loto : arrLoto) {
				strlotott += "<span class=\"boxLOTO\">"+loto+"</span>\r\n";
			}
			htmlMB=htmlMB.replaceAll("LOTO_TRUCTIEP",strlotott);
			
			DBConfig.loadProperties();
			Writer out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(DBConfig.file_kqhtml_mb), "UTF8"));
			out.write(htmlMB, 0, htmlMB.length());
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void createFileTT_HTML_MB_ThanTai(KQMB kqmb){
		try {
			String htmlMB = IOUtil.getFileContenntAsString("./conf/MienBac.html","UTF8");
			String imageloading ="<img style=\"width: 20px;margin-top: 10px;margin-bottom: 10px;\" src=\"http://xosothantai.vn/kkt_api/livexs/loading.gif\">";
			
			htmlMB=htmlMB.replaceAll("NGAY_QUAY",kqmb.ngay_quay);
			
			htmlMB=htmlMB.replaceAll("TTMB_GIAIDACBIET",StringUtil.isEmpty(kqmb.giai_dacbiet)?imageloading:kqmb.giai_dacbiet);
			htmlMB=htmlMB.replaceAll("TTMB_GIAINHAT",StringUtil.isEmpty(kqmb.giai_nhat)?imageloading:kqmb.giai_nhat);
			htmlMB=htmlMB.replaceAll("TTMB_GIAINHI_1",StringUtil.isEmpty(kqmb.giai_nhi_1)?imageloading:kqmb.giai_nhi_1);
			htmlMB=htmlMB.replaceAll("TTMB_GIAINHI_2",StringUtil.isEmpty(kqmb.giai_nhi_2)?imageloading:kqmb.giai_nhi_2);
			
			htmlMB=htmlMB.replaceAll("TTMB_GIAIBA_1",StringUtil.isEmpty(kqmb.giai_ba_1)?imageloading:kqmb.giai_ba_1);
			htmlMB=htmlMB.replaceAll("TTMB_GIAIBA_2",StringUtil.isEmpty(kqmb.giai_ba_2)?imageloading:kqmb.giai_ba_2);
			htmlMB=htmlMB.replaceAll("TTMB_GIAIBA_3",StringUtil.isEmpty(kqmb.giai_ba_3)?imageloading:kqmb.giai_ba_3);
			htmlMB=htmlMB.replaceAll("TTMB_GIAIBA_4",StringUtil.isEmpty(kqmb.giai_ba_4)?imageloading:kqmb.giai_ba_4);
			htmlMB=htmlMB.replaceAll("TTMB_GIAIBA_5",StringUtil.isEmpty(kqmb.giai_ba_5)?imageloading:kqmb.giai_ba_5);
			htmlMB=htmlMB.replaceAll("TTMB_GIAIBA_6",StringUtil.isEmpty(kqmb.giai_ba_6)?imageloading:kqmb.giai_ba_6);
			
			htmlMB=htmlMB.replaceAll("TTMB_GIAITU_1",StringUtil.isEmpty(kqmb.giai_tu_1)?imageloading:kqmb.giai_tu_1);
			htmlMB=htmlMB.replaceAll("TTMB_GIAITU_2",StringUtil.isEmpty(kqmb.giai_tu_2)?imageloading:kqmb.giai_tu_2);
			htmlMB=htmlMB.replaceAll("TTMB_GIAITU_3",StringUtil.isEmpty(kqmb.giai_tu_3)?imageloading:kqmb.giai_tu_3);
			htmlMB=htmlMB.replaceAll("TTMB_GIAITU_4",StringUtil.isEmpty(kqmb.giai_tu_4)?imageloading:kqmb.giai_tu_4);
			
			htmlMB=htmlMB.replaceAll("TTMB_GIAINAM_1",StringUtil.isEmpty(kqmb.giai_nam_1)?imageloading:kqmb.giai_nam_1);
			htmlMB=htmlMB.replaceAll("TTMB_GIAINAM_2",StringUtil.isEmpty(kqmb.giai_nam_2)?imageloading:kqmb.giai_nam_2);
			htmlMB=htmlMB.replaceAll("TTMB_GIAINAM_3",StringUtil.isEmpty(kqmb.giai_nam_3)?imageloading:kqmb.giai_nam_3);
			htmlMB=htmlMB.replaceAll("TTMB_GIAINAM_4",StringUtil.isEmpty(kqmb.giai_nam_4)?imageloading:kqmb.giai_nam_4);
			htmlMB=htmlMB.replaceAll("TTMB_GIAINAM_5",StringUtil.isEmpty(kqmb.giai_nam_5)?imageloading:kqmb.giai_nam_5);
			htmlMB=htmlMB.replaceAll("TTMB_GIAINAM_6",StringUtil.isEmpty(kqmb.giai_nam_6)?imageloading:kqmb.giai_nam_6);
			
			htmlMB=htmlMB.replaceAll("TTMB_GIAISAU_1",StringUtil.isEmpty(kqmb.giai_sau_1)?imageloading:kqmb.giai_sau_1);
			htmlMB=htmlMB.replaceAll("TTMB_GIAISAU_2",StringUtil.isEmpty(kqmb.giai_sau_2)?imageloading:kqmb.giai_sau_2);
			htmlMB=htmlMB.replaceAll("TTMB_GIAISAU_3",StringUtil.isEmpty(kqmb.giai_sau_3)?imageloading:kqmb.giai_sau_3);
			
			htmlMB=htmlMB.replaceAll("TTMB_GIAIBAY_1",StringUtil.isEmpty(kqmb.giai_bay_1)?imageloading:kqmb.giai_bay_1);
			htmlMB=htmlMB.replaceAll("TTMB_GIAIBAY_2",StringUtil.isEmpty(kqmb.giai_bay_2)?imageloading:kqmb.giai_bay_2);
			htmlMB=htmlMB.replaceAll("TTMB_GIAIBAY_3",StringUtil.isEmpty(kqmb.giai_bay_3)?imageloading:kqmb.giai_bay_3);
			htmlMB=htmlMB.replaceAll("TTMB_GIAIBAY_4",StringUtil.isEmpty(kqmb.giai_bay_4)?imageloading:kqmb.giai_bay_4);
			
			ArrayList<String> arrLoto = new ArrayList<String>();
			if(!StringUtil.isEmpty(kqmb.giai_nhat))arrLoto.add(kqmb.giai_nhat.substring(kqmb.giai_nhat.length()-2)); 
			if(!StringUtil.isEmpty(kqmb.giai_nhi_1))arrLoto.add(kqmb.giai_nhi_1.substring(kqmb.giai_nhi_1.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_nhi_2))arrLoto.add(kqmb.giai_nhi_2.substring(kqmb.giai_nhi_2.length()-2));
			
			if(!StringUtil.isEmpty(kqmb.giai_ba_1))arrLoto.add(kqmb.giai_ba_1.substring(kqmb.giai_ba_1.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_ba_2))arrLoto.add(kqmb.giai_ba_2.substring(kqmb.giai_ba_2.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_ba_3))arrLoto.add(kqmb.giai_ba_3.substring(kqmb.giai_ba_3.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_ba_4))arrLoto.add(kqmb.giai_ba_4.substring(kqmb.giai_ba_4.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_ba_5))arrLoto.add(kqmb.giai_ba_5.substring(kqmb.giai_ba_5.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_ba_6))arrLoto.add(kqmb.giai_ba_6.substring(kqmb.giai_ba_6.length()-2));
			
			if(!StringUtil.isEmpty(kqmb.giai_tu_1))arrLoto.add(kqmb.giai_tu_1.substring(kqmb.giai_tu_1.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_tu_2))arrLoto.add(kqmb.giai_tu_2.substring(kqmb.giai_tu_2.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_tu_3))arrLoto.add(kqmb.giai_tu_3.substring(kqmb.giai_tu_3.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_tu_4))arrLoto.add(kqmb.giai_tu_4.substring(kqmb.giai_tu_4.length()-2));
			
			if(!StringUtil.isEmpty(kqmb.giai_nam_1))arrLoto.add(kqmb.giai_nam_1.substring(kqmb.giai_nam_1.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_nam_2))arrLoto.add(kqmb.giai_nam_2.substring(kqmb.giai_nam_2.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_nam_3))arrLoto.add(kqmb.giai_nam_3.substring(kqmb.giai_nam_3.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_nam_4))arrLoto.add(kqmb.giai_nam_4.substring(kqmb.giai_nam_4.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_nam_5))arrLoto.add(kqmb.giai_nam_5.substring(kqmb.giai_nam_5.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_nam_6))arrLoto.add(kqmb.giai_nam_6.substring(kqmb.giai_nam_6.length()-2));
			
			if(!StringUtil.isEmpty(kqmb.giai_sau_1))arrLoto.add(kqmb.giai_sau_1.substring(kqmb.giai_sau_1.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_sau_2))arrLoto.add(kqmb.giai_sau_2.substring(kqmb.giai_sau_2.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_sau_3))arrLoto.add(kqmb.giai_sau_3.substring(kqmb.giai_sau_3.length()-2));
			
			if(!StringUtil.isEmpty(kqmb.giai_bay_1))arrLoto.add(kqmb.giai_bay_1.substring(kqmb.giai_bay_1.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_bay_2))arrLoto.add(kqmb.giai_bay_2.substring(kqmb.giai_bay_2.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_bay_3))arrLoto.add(kqmb.giai_bay_3.substring(kqmb.giai_bay_3.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_bay_4))arrLoto.add(kqmb.giai_bay_4.substring(kqmb.giai_bay_4.length()-2));
			
			if(!StringUtil.isEmpty(kqmb.giai_dacbiet))arrLoto.add(kqmb.giai_dacbiet.substring(kqmb.giai_dacbiet.length()-2)); 
			
			String dau_0 = getDauDuoi(0,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_0",dau_0);
			String dau_1 = getDauDuoi(1,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_1",dau_1);
			String dau_2 = getDauDuoi(2,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_2",dau_2);
			String dau_3 = getDauDuoi(3,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_3",dau_3);
			String dau_4 = getDauDuoi(4,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_4",dau_4);
			String dau_5 = getDauDuoi(5,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_5",dau_5);
			String dau_6 = getDauDuoi(6,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_6",dau_6);
			String dau_7 = getDauDuoi(7,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_7",dau_7);
			String dau_8 = getDauDuoi(8,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_8",dau_8);
			String dau_9 = getDauDuoi(9,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_9",dau_9);
			
			sortLoto(arrLoto);
			String strlotott ="";
			for (String loto : arrLoto) {
				strlotott += "<li class=\"in-block gr-gray\"><strong class=\"\">"+loto+"</strong></li>\r\n";
			}
			htmlMB=htmlMB.replaceAll("LOTO_TRUCTIEP",strlotott);
			
			DBConfig.loadProperties();
			Writer out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(DBConfig.file_kqhtml_mb), "UTF8"));
			out.write(htmlMB, 0, htmlMB.length());
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void createFileTT_HTML_MB_XOSOPLus(KQMB kqmb){
		try {
			String htmlMB = IOUtil.getFileContenntAsString("./conf/MienBacXoSoPlus.html","UTF8");
			String imageloading ="<img style=\"width: 20px;margin-top: 10px;margin-bottom: 10px;\" src=\"http://10h.vn/themes/images/loading.gif\">";
			
			htmlMB=htmlMB.replaceAll("NGAY_QUAY",kqmb.ngay_quay);
			
			htmlMB=htmlMB.replaceAll("TTMB_GIAIDACBIET",StringUtil.isEmpty(kqmb.giai_dacbiet)?imageloading:kqmb.giai_dacbiet);
			htmlMB=htmlMB.replaceAll("TTMB_GIAINHAT",StringUtil.isEmpty(kqmb.giai_nhat)?imageloading:kqmb.giai_nhat);
			htmlMB=htmlMB.replaceAll("TTMB_GIAINHI_1",StringUtil.isEmpty(kqmb.giai_nhi_1)?imageloading:kqmb.giai_nhi_1);
			htmlMB=htmlMB.replaceAll("TTMB_GIAINHI_2",StringUtil.isEmpty(kqmb.giai_nhi_2)?imageloading:kqmb.giai_nhi_2);
			
			htmlMB=htmlMB.replaceAll("TTMB_GIAIBA_1",StringUtil.isEmpty(kqmb.giai_ba_1)?imageloading:kqmb.giai_ba_1);
			htmlMB=htmlMB.replaceAll("TTMB_GIAIBA_2",StringUtil.isEmpty(kqmb.giai_ba_2)?imageloading:kqmb.giai_ba_2);
			htmlMB=htmlMB.replaceAll("TTMB_GIAIBA_3",StringUtil.isEmpty(kqmb.giai_ba_3)?imageloading:kqmb.giai_ba_3);
			htmlMB=htmlMB.replaceAll("TTMB_GIAIBA_4",StringUtil.isEmpty(kqmb.giai_ba_4)?imageloading:kqmb.giai_ba_4);
			htmlMB=htmlMB.replaceAll("TTMB_GIAIBA_5",StringUtil.isEmpty(kqmb.giai_ba_5)?imageloading:kqmb.giai_ba_5);
			htmlMB=htmlMB.replaceAll("TTMB_GIAIBA_6",StringUtil.isEmpty(kqmb.giai_ba_6)?imageloading:kqmb.giai_ba_6);
			
			htmlMB=htmlMB.replaceAll("TTMB_GIAITU_1",StringUtil.isEmpty(kqmb.giai_tu_1)?imageloading:kqmb.giai_tu_1);
			htmlMB=htmlMB.replaceAll("TTMB_GIAITU_2",StringUtil.isEmpty(kqmb.giai_tu_2)?imageloading:kqmb.giai_tu_2);
			htmlMB=htmlMB.replaceAll("TTMB_GIAITU_3",StringUtil.isEmpty(kqmb.giai_tu_3)?imageloading:kqmb.giai_tu_3);
			htmlMB=htmlMB.replaceAll("TTMB_GIAITU_4",StringUtil.isEmpty(kqmb.giai_tu_4)?imageloading:kqmb.giai_tu_4);
			
			htmlMB=htmlMB.replaceAll("TTMB_GIAINAM_1",StringUtil.isEmpty(kqmb.giai_nam_1)?imageloading:kqmb.giai_nam_1);
			htmlMB=htmlMB.replaceAll("TTMB_GIAINAM_2",StringUtil.isEmpty(kqmb.giai_nam_2)?imageloading:kqmb.giai_nam_2);
			htmlMB=htmlMB.replaceAll("TTMB_GIAINAM_3",StringUtil.isEmpty(kqmb.giai_nam_3)?imageloading:kqmb.giai_nam_3);
			htmlMB=htmlMB.replaceAll("TTMB_GIAINAM_4",StringUtil.isEmpty(kqmb.giai_nam_4)?imageloading:kqmb.giai_nam_4);
			htmlMB=htmlMB.replaceAll("TTMB_GIAINAM_5",StringUtil.isEmpty(kqmb.giai_nam_5)?imageloading:kqmb.giai_nam_5);
			htmlMB=htmlMB.replaceAll("TTMB_GIAINAM_6",StringUtil.isEmpty(kqmb.giai_nam_6)?imageloading:kqmb.giai_nam_6);
			
			htmlMB=htmlMB.replaceAll("TTMB_GIAISAU_1",StringUtil.isEmpty(kqmb.giai_sau_1)?imageloading:kqmb.giai_sau_1);
			htmlMB=htmlMB.replaceAll("TTMB_GIAISAU_2",StringUtil.isEmpty(kqmb.giai_sau_2)?imageloading:kqmb.giai_sau_2);
			htmlMB=htmlMB.replaceAll("TTMB_GIAISAU_3",StringUtil.isEmpty(kqmb.giai_sau_3)?imageloading:kqmb.giai_sau_3);
			
			htmlMB=htmlMB.replaceAll("TTMB_GIAIBAY_1",StringUtil.isEmpty(kqmb.giai_bay_1)?imageloading:kqmb.giai_bay_1);
			htmlMB=htmlMB.replaceAll("TTMB_GIAIBAY_2",StringUtil.isEmpty(kqmb.giai_bay_2)?imageloading:kqmb.giai_bay_2);
			htmlMB=htmlMB.replaceAll("TTMB_GIAIBAY_3",StringUtil.isEmpty(kqmb.giai_bay_3)?imageloading:kqmb.giai_bay_3);
			htmlMB=htmlMB.replaceAll("TTMB_GIAIBAY_4",StringUtil.isEmpty(kqmb.giai_bay_4)?imageloading:kqmb.giai_bay_4);
			
			ArrayList<String> arrLoto = new ArrayList<String>();
			if(!StringUtil.isEmpty(kqmb.giai_nhat))arrLoto.add(kqmb.giai_nhat.substring(kqmb.giai_nhat.length()-2)); 
			if(!StringUtil.isEmpty(kqmb.giai_nhi_1))arrLoto.add(kqmb.giai_nhi_1.substring(kqmb.giai_nhi_1.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_nhi_2))arrLoto.add(kqmb.giai_nhi_2.substring(kqmb.giai_nhi_2.length()-2));
			
			if(!StringUtil.isEmpty(kqmb.giai_ba_1))arrLoto.add(kqmb.giai_ba_1.substring(kqmb.giai_ba_1.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_ba_2))arrLoto.add(kqmb.giai_ba_2.substring(kqmb.giai_ba_2.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_ba_3))arrLoto.add(kqmb.giai_ba_3.substring(kqmb.giai_ba_3.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_ba_4))arrLoto.add(kqmb.giai_ba_4.substring(kqmb.giai_ba_4.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_ba_5))arrLoto.add(kqmb.giai_ba_5.substring(kqmb.giai_ba_5.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_ba_6))arrLoto.add(kqmb.giai_ba_6.substring(kqmb.giai_ba_6.length()-2));
			
			if(!StringUtil.isEmpty(kqmb.giai_tu_1))arrLoto.add(kqmb.giai_tu_1.substring(kqmb.giai_tu_1.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_tu_2))arrLoto.add(kqmb.giai_tu_2.substring(kqmb.giai_tu_2.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_tu_3))arrLoto.add(kqmb.giai_tu_3.substring(kqmb.giai_tu_3.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_tu_4))arrLoto.add(kqmb.giai_tu_4.substring(kqmb.giai_tu_4.length()-2));
			
			if(!StringUtil.isEmpty(kqmb.giai_nam_1))arrLoto.add(kqmb.giai_nam_1.substring(kqmb.giai_nam_1.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_nam_2))arrLoto.add(kqmb.giai_nam_2.substring(kqmb.giai_nam_2.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_nam_3))arrLoto.add(kqmb.giai_nam_3.substring(kqmb.giai_nam_3.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_nam_4))arrLoto.add(kqmb.giai_nam_4.substring(kqmb.giai_nam_4.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_nam_5))arrLoto.add(kqmb.giai_nam_5.substring(kqmb.giai_nam_5.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_nam_6))arrLoto.add(kqmb.giai_nam_6.substring(kqmb.giai_nam_6.length()-2));
			
			if(!StringUtil.isEmpty(kqmb.giai_sau_1))arrLoto.add(kqmb.giai_sau_1.substring(kqmb.giai_sau_1.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_sau_2))arrLoto.add(kqmb.giai_sau_2.substring(kqmb.giai_sau_2.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_sau_3))arrLoto.add(kqmb.giai_sau_3.substring(kqmb.giai_sau_3.length()-2));
			
			if(!StringUtil.isEmpty(kqmb.giai_bay_1))arrLoto.add(kqmb.giai_bay_1.substring(kqmb.giai_bay_1.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_bay_2))arrLoto.add(kqmb.giai_bay_2.substring(kqmb.giai_bay_2.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_bay_3))arrLoto.add(kqmb.giai_bay_3.substring(kqmb.giai_bay_3.length()-2));
			if(!StringUtil.isEmpty(kqmb.giai_bay_4))arrLoto.add(kqmb.giai_bay_4.substring(kqmb.giai_bay_4.length()-2));
			
			if(!StringUtil.isEmpty(kqmb.giai_dacbiet))arrLoto.add(kqmb.giai_dacbiet.substring(kqmb.giai_dacbiet.length()-2)); 
			
			String dau_0 = getDauDuoi(0,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_0",dau_0);
			String dau_1 = getDauDuoi(1,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_1",dau_1);
			String dau_2 = getDauDuoi(2,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_2",dau_2);
			String dau_3 = getDauDuoi(3,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_3",dau_3);
			String dau_4 = getDauDuoi(4,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_4",dau_4);
			String dau_5 = getDauDuoi(5,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_5",dau_5);
			String dau_6 = getDauDuoi(6,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_6",dau_6);
			String dau_7 = getDauDuoi(7,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_7",dau_7);
			String dau_8 = getDauDuoi(8,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_8",dau_8);
			String dau_9 = getDauDuoi(9,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_9",dau_9);
			
			sortLoto(arrLoto);
			String strlotott ="";
			for (String loto : arrLoto) {
				strlotott += "<span class=\"boxLOTO\">"+loto+"</span>\r\n";
			}
			htmlMB=htmlMB.replaceAll("LOTO_TRUCTIEP",strlotott);
			
			DBConfig.loadProperties();
			Writer out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(DBConfig.file_kqhtml_mb_xosoplus), "UTF8"));
			out.write(htmlMB, 0, htmlMB.length());
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public String getDauDuoi(int dau,ArrayList<String> arrayLoto){
		String dauduoi ="";
		int i = 0;
		
		while(i<arrayLoto.size()){
			if(Integer.parseInt(arrayLoto.get(i).substring(0,1))==dau){
				dauduoi +=" "+arrayLoto.get(i).substring(1,2);
			}
			i++;
		}
		
		return dauduoi;
	}
	
	public void createFileTT_HTML_MN(ArrayList<KQMN> arrKQMN){
		try {
			String htmlMB = IOUtil.getFileContenntAsString("./conf/MienNam3C.html","UTF8");
			if(arrKQMN.size()==4)
				htmlMB = IOUtil.getFileContenntAsString("./conf/MienNam4C.html","UTF8");
		
			KQMN kqmb1 = arrKQMN.get(0);
			htmlMB=htmlMB.replaceAll("NGAY_QUAY", kqmb1.ngay_quay);
			String imageloading ="<img style=\"width: 20px;margin-top: 10px;margin-bottom: 10px;\" src=\"http://10h.vn/themes/images/loading.gif\">";
			
			int i = 0;int j = 1;
			while(i<arrKQMN.size()){
				kqmb1= arrKQMN.get(i);
				
				htmlMB=htmlMB.replaceAll("PROVINCE_NAME"+j, kqmb1.province_name);
				
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TAM"+j, StringUtil.isEmpty(kqmb1.giai_tam)?imageloading:kqmb1.giai_tam);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_BAY"+j, StringUtil.isEmpty(kqmb1.giai_bay)?imageloading:kqmb1.giai_bay);
				
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_SAU"+j+"1",StringUtil.isEmpty(kqmb1.giai_sau_1)?imageloading:kqmb1.giai_sau_1);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_SAU"+j+"2",StringUtil.isEmpty(kqmb1.giai_sau_2)?imageloading:kqmb1.giai_sau_2);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_SAU"+j+"3",StringUtil.isEmpty(kqmb1.giai_sau_3)?imageloading:kqmb1.giai_sau_3);
				
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_NAM"+j,StringUtil.isEmpty(kqmb1.giai_nam)?imageloading:kqmb1.giai_nam);
				
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TU"+j+"1",StringUtil.isEmpty(kqmb1.giai_tu_1)?imageloading:kqmb1.giai_tu_1);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TU"+j+"2",StringUtil.isEmpty(kqmb1.giai_tu_2)?imageloading:kqmb1.giai_tu_2);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TU"+j+"3",StringUtil.isEmpty(kqmb1.giai_tu_3)?imageloading:kqmb1.giai_tu_3);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TU"+j+"4",StringUtil.isEmpty(kqmb1.giai_tu_4)?imageloading:kqmb1.giai_tu_4);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TU"+j+"5",StringUtil.isEmpty(kqmb1.giai_tu_5)?imageloading:kqmb1.giai_tu_5);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TU"+j+"6",StringUtil.isEmpty(kqmb1.giai_tu_6)?imageloading:kqmb1.giai_tu_6);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TU"+j+"7",StringUtil.isEmpty(kqmb1.giai_tu_7)?imageloading:kqmb1.giai_tu_7);
				
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_BA"+j+"1",StringUtil.isEmpty(kqmb1.giai_ba_1)?imageloading:kqmb1.giai_ba_1);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_BA"+j+"2",StringUtil.isEmpty(kqmb1.giai_ba_2)?imageloading: kqmb1.giai_ba_2);
				
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_NHI"+j,StringUtil.isEmpty(kqmb1.giai_nhi)?imageloading:kqmb1.giai_nhi);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_NHAT"+j,StringUtil.isEmpty(kqmb1.giai_nhat)?imageloading:kqmb1.giai_nhat);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_DACBIET"+j,StringUtil.isEmpty(kqmb1.giai_dacbiet)?imageloading:kqmb1.giai_dacbiet);
				
				ArrayList<String> arrLoto = new ArrayList<String>();
				
				if(!StringUtil.isEmpty(kqmb1.giai_tam))arrLoto.add(kqmb1.giai_tam.substring(kqmb1.giai_tam.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_bay))arrLoto.add(kqmb1.giai_bay.substring(kqmb1.giai_bay.length()-2));
				
				if(!StringUtil.isEmpty(kqmb1.giai_sau_1))arrLoto.add(kqmb1.giai_sau_1.substring(kqmb1.giai_sau_1.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_sau_2))arrLoto.add(kqmb1.giai_sau_2.substring(kqmb1.giai_sau_2.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_sau_3))arrLoto.add(kqmb1.giai_sau_3.substring(kqmb1.giai_sau_3.length()-2));
				
				if(!StringUtil.isEmpty(kqmb1.giai_nam))arrLoto.add(kqmb1.giai_nam.substring(kqmb1.giai_nam.length()-2));
				
				if(!StringUtil.isEmpty(kqmb1.giai_tu_1))arrLoto.add(kqmb1.giai_tu_1.substring(kqmb1.giai_tu_1.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_tu_2))arrLoto.add(kqmb1.giai_tu_2.substring(kqmb1.giai_tu_2.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_tu_3))arrLoto.add(kqmb1.giai_tu_3.substring(kqmb1.giai_tu_3.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_tu_4))arrLoto.add(kqmb1.giai_tu_4.substring(kqmb1.giai_tu_4.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_tu_5))arrLoto.add(kqmb1.giai_tu_5.substring(kqmb1.giai_tu_5.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_tu_6))arrLoto.add(kqmb1.giai_tu_6.substring(kqmb1.giai_tu_6.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_tu_7))arrLoto.add(kqmb1.giai_tu_7.substring(kqmb1.giai_tu_7.length()-2));
				
				if(!StringUtil.isEmpty(kqmb1.giai_ba_1))arrLoto.add(kqmb1.giai_ba_1.substring(kqmb1.giai_ba_1.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_ba_2))arrLoto.add(kqmb1.giai_ba_2.substring(kqmb1.giai_ba_2.length()-2));
				
				if(!StringUtil.isEmpty(kqmb1.giai_nhi))arrLoto.add(kqmb1.giai_nhi.substring(kqmb1.giai_nhi.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_nhat))arrLoto.add(kqmb1.giai_nhat.substring(kqmb1.giai_nhat.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_dacbiet))arrLoto.add(kqmb1.giai_dacbiet.substring(kqmb1.giai_dacbiet.length()-2));
				
				String dau_0 = getDauDuoi(0,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_0"+j,dau_0);
				String dau_1 = getDauDuoi(1,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_1"+j,dau_1);
				String dau_2 = getDauDuoi(2,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_2"+j,dau_2);
				String dau_3 = getDauDuoi(3,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_3"+j,dau_3);
				String dau_4 = getDauDuoi(4,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_4"+j,dau_4);
				String dau_5 = getDauDuoi(5,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_5"+j,dau_5);
				String dau_6 = getDauDuoi(6,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_6"+j,dau_6);
				String dau_7 = getDauDuoi(7,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_7"+j,dau_7);
				String dau_8 = getDauDuoi(8,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_8"+j,dau_8);
				String dau_9 = getDauDuoi(9,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_9"+j,dau_9);
				
				j++;
				i++;
			}
			DBConfig.loadProperties();
			Writer out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(DBConfig.file_kqhtml_mn), "UTF8"));
			out.write(htmlMB, 0, htmlMB.length());
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void createFileTT_HTML_MN_ThanTai(ArrayList<KQMN> arrKQMN){
		try {
			String htmlMB = IOUtil.getFileContenntAsString("./conf/MienNam3C.html","UTF8");
			if(arrKQMN.size()==4)
				htmlMB = IOUtil.getFileContenntAsString("./conf/MienNam4C.html","UTF8");
		
			KQMN kqmb1 = arrKQMN.get(0);
			SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd"); 
			Date date;String str_thu = "Thu Hai";
				try {
					date = dt.parse(kqmb1.ngay_quay);
					SimpleDateFormat dt1 = new SimpleDateFormat("u");
					int thu = Integer.parseInt(dt1.format(date));
					
				    if(thu==1)str_thu = "Thu Hai"; else 
				    	if(thu==2)str_thu = "Thu Ba"; else
				    		if(thu==3)str_thu = "Thu Tư"; else
				    			if(thu==4)str_thu = "Thu Năm"; else
				    				if(thu==5)str_thu = "Thu Sau"; else
				    					if(thu==6)str_thu = "Thu Bảy"; else
				    						str_thu = "Chủ nhật";
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			
			htmlMB=htmlMB.replaceAll("THU_QUAY", str_thu);	
			htmlMB=htmlMB.replaceAll("NGAY_QUAY", kqmb1.ngay_quay);
			String imageloading ="<img style=\"width: 20px;margin-top: 10px;margin-bottom: 10px;\" src=\"http://xosothantai.vn/kkt_api/livexs/loading.gif\">";
			int i = 0;int j = 1;
			while(i<arrKQMN.size()){
				kqmb1= arrKQMN.get(i);
				
				htmlMB=htmlMB.replaceAll("PROVINCE_NAME"+j, kqmb1.province_name);
				
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TAM"+j, StringUtil.isEmpty(kqmb1.giai_tam)?imageloading:kqmb1.giai_tam);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_BAY"+j, StringUtil.isEmpty(kqmb1.giai_bay)?imageloading:kqmb1.giai_bay);
				
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_SAU"+j+"1",StringUtil.isEmpty(kqmb1.giai_sau_1)?imageloading:kqmb1.giai_sau_1);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_SAU"+j+"2",StringUtil.isEmpty(kqmb1.giai_sau_2)?imageloading:kqmb1.giai_sau_2);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_SAU"+j+"3",StringUtil.isEmpty(kqmb1.giai_sau_3)?imageloading:kqmb1.giai_sau_3);
				
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_NAM"+j,StringUtil.isEmpty(kqmb1.giai_nam)?imageloading:kqmb1.giai_nam);
				
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TU"+j+"1",StringUtil.isEmpty(kqmb1.giai_tu_1)?imageloading:kqmb1.giai_tu_1);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TU"+j+"2",StringUtil.isEmpty(kqmb1.giai_tu_2)?imageloading:kqmb1.giai_tu_2);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TU"+j+"3",StringUtil.isEmpty(kqmb1.giai_tu_3)?imageloading:kqmb1.giai_tu_3);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TU"+j+"4",StringUtil.isEmpty(kqmb1.giai_tu_4)?imageloading:kqmb1.giai_tu_4);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TU"+j+"5",StringUtil.isEmpty(kqmb1.giai_tu_5)?imageloading:kqmb1.giai_tu_5);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TU"+j+"6",StringUtil.isEmpty(kqmb1.giai_tu_6)?imageloading:kqmb1.giai_tu_6);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TU"+j+"7",StringUtil.isEmpty(kqmb1.giai_tu_7)?imageloading:kqmb1.giai_tu_7);
				
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_BA"+j+"1",StringUtil.isEmpty(kqmb1.giai_ba_1)?imageloading:kqmb1.giai_ba_1);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_BA"+j+"2",StringUtil.isEmpty(kqmb1.giai_ba_2)?imageloading: kqmb1.giai_ba_2);
				
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_NHI"+j,StringUtil.isEmpty(kqmb1.giai_nhi)?imageloading:kqmb1.giai_nhi);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_NHAT"+j,StringUtil.isEmpty(kqmb1.giai_nhat)?imageloading:kqmb1.giai_nhat);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_DACBIET"+j,StringUtil.isEmpty(kqmb1.giai_dacbiet)?imageloading:kqmb1.giai_dacbiet);
				
				ArrayList<String> arrLoto = new ArrayList<String>();
				
				if(!StringUtil.isEmpty(kqmb1.giai_tam))arrLoto.add(kqmb1.giai_tam.substring(kqmb1.giai_tam.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_bay))arrLoto.add(kqmb1.giai_bay.substring(kqmb1.giai_bay.length()-2));
				
				if(!StringUtil.isEmpty(kqmb1.giai_sau_1))arrLoto.add(kqmb1.giai_sau_1.substring(kqmb1.giai_sau_1.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_sau_2))arrLoto.add(kqmb1.giai_sau_2.substring(kqmb1.giai_sau_2.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_sau_3))arrLoto.add(kqmb1.giai_sau_3.substring(kqmb1.giai_sau_3.length()-2));
				
				if(!StringUtil.isEmpty(kqmb1.giai_nam))arrLoto.add(kqmb1.giai_nam.substring(kqmb1.giai_nam.length()-2));
				
				if(!StringUtil.isEmpty(kqmb1.giai_tu_1))arrLoto.add(kqmb1.giai_tu_1.substring(kqmb1.giai_tu_1.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_tu_2))arrLoto.add(kqmb1.giai_tu_2.substring(kqmb1.giai_tu_2.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_tu_3))arrLoto.add(kqmb1.giai_tu_3.substring(kqmb1.giai_tu_3.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_tu_4))arrLoto.add(kqmb1.giai_tu_4.substring(kqmb1.giai_tu_4.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_tu_5))arrLoto.add(kqmb1.giai_tu_5.substring(kqmb1.giai_tu_5.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_tu_6))arrLoto.add(kqmb1.giai_tu_6.substring(kqmb1.giai_tu_6.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_tu_7))arrLoto.add(kqmb1.giai_tu_7.substring(kqmb1.giai_tu_7.length()-2));
				
				if(!StringUtil.isEmpty(kqmb1.giai_ba_1))arrLoto.add(kqmb1.giai_ba_1.substring(kqmb1.giai_ba_1.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_ba_2))arrLoto.add(kqmb1.giai_ba_2.substring(kqmb1.giai_ba_2.length()-2));
				
				if(!StringUtil.isEmpty(kqmb1.giai_nhi))arrLoto.add(kqmb1.giai_nhi.substring(kqmb1.giai_nhi.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_nhat))arrLoto.add(kqmb1.giai_nhat.substring(kqmb1.giai_nhat.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_dacbiet))arrLoto.add(kqmb1.giai_dacbiet.substring(kqmb1.giai_dacbiet.length()-2));
				
				String dau_0 = getDauDuoi(0,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_0"+j,dau_0);
				String dau_1 = getDauDuoi(1,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_1"+j,dau_1);
				String dau_2 = getDauDuoi(2,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_2"+j,dau_2);
				String dau_3 = getDauDuoi(3,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_3"+j,dau_3);
				String dau_4 = getDauDuoi(4,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_4"+j,dau_4);
				String dau_5 = getDauDuoi(5,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_5"+j,dau_5);
				String dau_6 = getDauDuoi(6,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_6"+j,dau_6);
				String dau_7 = getDauDuoi(7,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_7"+j,dau_7);
				String dau_8 = getDauDuoi(8,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_8"+j,dau_8);
				String dau_9 = getDauDuoi(9,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_9"+j,dau_9);
				
				j++;
				i++;
			}
			DBConfig.loadProperties();
			Writer out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(DBConfig.file_kqhtml_mn), "UTF8"));
			out.write(htmlMB, 0, htmlMB.length());
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void createFileTT_HTML_MN_XoSoPlus(ArrayList<KQMN> arrKQMN){
		try {
			String htmlMB = IOUtil.getFileContenntAsString("./conf/MienNam3CXoSoPlus.html","UTF8");
			if(arrKQMN.size()==4)
				htmlMB = IOUtil.getFileContenntAsString("./conf/MienNam4CXoSoPlus.html","UTF8");
		
			KQMN kqmb1 = arrKQMN.get(0);
			htmlMB=htmlMB.replaceAll("NGAY_QUAY", kqmb1.ngay_quay);
			String imageloading ="<img style=\"width: 20px;margin-top: 10px;margin-bottom: 10px;\" src=\"http://10h.vn/themes/images/loading.gif\">";
			
			int i = 0;int j = 1;
			while(i<arrKQMN.size()){
				kqmb1= arrKQMN.get(i);
				
				htmlMB=htmlMB.replaceAll("PROVINCE_NAME"+j, kqmb1.province_name);
				
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TAM"+j, StringUtil.isEmpty(kqmb1.giai_tam)?imageloading:kqmb1.giai_tam);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_BAY"+j, StringUtil.isEmpty(kqmb1.giai_bay)?imageloading:kqmb1.giai_bay);
				
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_SAU"+j+"1",StringUtil.isEmpty(kqmb1.giai_sau_1)?imageloading:kqmb1.giai_sau_1);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_SAU"+j+"2",StringUtil.isEmpty(kqmb1.giai_sau_2)?imageloading:kqmb1.giai_sau_2);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_SAU"+j+"3",StringUtil.isEmpty(kqmb1.giai_sau_3)?imageloading:kqmb1.giai_sau_3);
				
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_NAM"+j,StringUtil.isEmpty(kqmb1.giai_nam)?imageloading:kqmb1.giai_nam);
				
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TU"+j+"1",StringUtil.isEmpty(kqmb1.giai_tu_1)?imageloading:kqmb1.giai_tu_1);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TU"+j+"2",StringUtil.isEmpty(kqmb1.giai_tu_2)?imageloading:kqmb1.giai_tu_2);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TU"+j+"3",StringUtil.isEmpty(kqmb1.giai_tu_3)?imageloading:kqmb1.giai_tu_3);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TU"+j+"4",StringUtil.isEmpty(kqmb1.giai_tu_4)?imageloading:kqmb1.giai_tu_4);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TU"+j+"5",StringUtil.isEmpty(kqmb1.giai_tu_5)?imageloading:kqmb1.giai_tu_5);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TU"+j+"6",StringUtil.isEmpty(kqmb1.giai_tu_6)?imageloading:kqmb1.giai_tu_6);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TU"+j+"7",StringUtil.isEmpty(kqmb1.giai_tu_7)?imageloading:kqmb1.giai_tu_7);
				
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_BA"+j+"1",StringUtil.isEmpty(kqmb1.giai_ba_1)?imageloading:kqmb1.giai_ba_1);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_BA"+j+"2",StringUtil.isEmpty(kqmb1.giai_ba_2)?imageloading: kqmb1.giai_ba_2);
				
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_NHI"+j,StringUtil.isEmpty(kqmb1.giai_nhi)?imageloading:kqmb1.giai_nhi);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_NHAT"+j,StringUtil.isEmpty(kqmb1.giai_nhat)?imageloading:kqmb1.giai_nhat);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_DACBIET"+j,StringUtil.isEmpty(kqmb1.giai_dacbiet)?imageloading:kqmb1.giai_dacbiet);
				
				ArrayList<String> arrLoto = new ArrayList<String>();
				
				if(!StringUtil.isEmpty(kqmb1.giai_tam))arrLoto.add(kqmb1.giai_tam.substring(kqmb1.giai_tam.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_bay))arrLoto.add(kqmb1.giai_bay.substring(kqmb1.giai_bay.length()-2));
				
				if(!StringUtil.isEmpty(kqmb1.giai_sau_1))arrLoto.add(kqmb1.giai_sau_1.substring(kqmb1.giai_sau_1.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_sau_2))arrLoto.add(kqmb1.giai_sau_2.substring(kqmb1.giai_sau_2.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_sau_3))arrLoto.add(kqmb1.giai_sau_3.substring(kqmb1.giai_sau_3.length()-2));
				
				if(!StringUtil.isEmpty(kqmb1.giai_nam))arrLoto.add(kqmb1.giai_nam.substring(kqmb1.giai_nam.length()-2));
				
				if(!StringUtil.isEmpty(kqmb1.giai_tu_1))arrLoto.add(kqmb1.giai_tu_1.substring(kqmb1.giai_tu_1.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_tu_2))arrLoto.add(kqmb1.giai_tu_2.substring(kqmb1.giai_tu_2.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_tu_3))arrLoto.add(kqmb1.giai_tu_3.substring(kqmb1.giai_tu_3.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_tu_4))arrLoto.add(kqmb1.giai_tu_4.substring(kqmb1.giai_tu_4.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_tu_5))arrLoto.add(kqmb1.giai_tu_5.substring(kqmb1.giai_tu_5.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_tu_6))arrLoto.add(kqmb1.giai_tu_6.substring(kqmb1.giai_tu_6.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_tu_7))arrLoto.add(kqmb1.giai_tu_7.substring(kqmb1.giai_tu_7.length()-2));
				
				if(!StringUtil.isEmpty(kqmb1.giai_ba_1))arrLoto.add(kqmb1.giai_ba_1.substring(kqmb1.giai_ba_1.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_ba_2))arrLoto.add(kqmb1.giai_ba_2.substring(kqmb1.giai_ba_2.length()-2));
				
				if(!StringUtil.isEmpty(kqmb1.giai_nhi))arrLoto.add(kqmb1.giai_nhi.substring(kqmb1.giai_nhi.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_nhat))arrLoto.add(kqmb1.giai_nhat.substring(kqmb1.giai_nhat.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_dacbiet))arrLoto.add(kqmb1.giai_dacbiet.substring(kqmb1.giai_dacbiet.length()-2));
				
				String dau_0 = getDauDuoi(0,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_0"+j,dau_0);
				String dau_1 = getDauDuoi(1,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_1"+j,dau_1);
				String dau_2 = getDauDuoi(2,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_2"+j,dau_2);
				String dau_3 = getDauDuoi(3,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_3"+j,dau_3);
				String dau_4 = getDauDuoi(4,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_4"+j,dau_4);
				String dau_5 = getDauDuoi(5,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_5"+j,dau_5);
				String dau_6 = getDauDuoi(6,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_6"+j,dau_6);
				String dau_7 = getDauDuoi(7,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_7"+j,dau_7);
				String dau_8 = getDauDuoi(8,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_8"+j,dau_8);
				String dau_9 = getDauDuoi(9,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_9"+j,dau_9);
				
				j++;
				i++;
			}
			DBConfig.loadProperties();
			Writer out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(DBConfig.file_kqhtml_mn_xosoplus), "UTF8"));
			out.write(htmlMB, 0, htmlMB.length());
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void createFileTT_HTML_MT(ArrayList<KQMN> arrKQMN){
		try {
			String htmlMB = IOUtil.getFileContenntAsString("./conf/MienTrung2C.html","UTF8");
			if(arrKQMN.size()==3)
				htmlMB = IOUtil.getFileContenntAsString("./conf/MienTrung3C.html","UTF8");
		
			KQMN kqmb1 = arrKQMN.get(0);
			htmlMB=htmlMB.replaceAll("NGAY_QUAY", kqmb1.ngay_quay);
			String imageloading ="<img style=\"width: 20px;margin-top: 10px;margin-bottom: 10px;\" src=\"http://10h.vn/themes/images/loading.gif\">";
			
			int i = 0;int j = 1;
			while(i<arrKQMN.size()){
				kqmb1= arrKQMN.get(i);
				
				String regex = "PROVINCE_NAME"+j;
				htmlMB=htmlMB.replaceAll(regex, kqmb1.province_name);
				
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TAM"+j, StringUtil.isEmpty(kqmb1.giai_tam)?imageloading:kqmb1.giai_tam);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_BAY"+j, StringUtil.isEmpty(kqmb1.giai_bay)?imageloading:kqmb1.giai_bay);
				
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_SAU"+j+"1",StringUtil.isEmpty(kqmb1.giai_sau_1)?imageloading:kqmb1.giai_sau_1);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_SAU"+j+"2",StringUtil.isEmpty(kqmb1.giai_sau_2)?imageloading:kqmb1.giai_sau_2);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_SAU"+j+"3",StringUtil.isEmpty(kqmb1.giai_sau_3)?imageloading:kqmb1.giai_sau_3);
				
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_NAM"+j,StringUtil.isEmpty(kqmb1.giai_nam)?imageloading:kqmb1.giai_nam);
				
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TU"+j+"1",StringUtil.isEmpty(kqmb1.giai_tu_1)?imageloading:kqmb1.giai_tu_1);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TU"+j+"2",StringUtil.isEmpty(kqmb1.giai_tu_2)?imageloading:kqmb1.giai_tu_2);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TU"+j+"3",StringUtil.isEmpty(kqmb1.giai_tu_3)?imageloading:kqmb1.giai_tu_3);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TU"+j+"4",StringUtil.isEmpty(kqmb1.giai_tu_4)?imageloading:kqmb1.giai_tu_4);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TU"+j+"5",StringUtil.isEmpty(kqmb1.giai_tu_5)?imageloading:kqmb1.giai_tu_5);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TU"+j+"6",StringUtil.isEmpty(kqmb1.giai_tu_6)?imageloading:kqmb1.giai_tu_6);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TU"+j+"7",StringUtil.isEmpty(kqmb1.giai_tu_7)?imageloading:kqmb1.giai_tu_7);
				
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_BA"+j+"1",StringUtil.isEmpty(kqmb1.giai_ba_1)?imageloading:kqmb1.giai_ba_1);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_BA"+j+"2",StringUtil.isEmpty(kqmb1.giai_ba_2)?imageloading: kqmb1.giai_ba_2);
				
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_NHI"+j,StringUtil.isEmpty(kqmb1.giai_nhi)?imageloading:kqmb1.giai_nhi);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_NHAT"+j,StringUtil.isEmpty(kqmb1.giai_nhat)?imageloading:kqmb1.giai_nhat);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_DACBIET"+j,StringUtil.isEmpty(kqmb1.giai_dacbiet)?imageloading:kqmb1.giai_dacbiet);
				
				ArrayList<String> arrLoto = new ArrayList<String>();
				
				if(!StringUtil.isEmpty(kqmb1.giai_tam))arrLoto.add(kqmb1.giai_tam.substring(kqmb1.giai_tam.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_bay))arrLoto.add(kqmb1.giai_bay.substring(kqmb1.giai_bay.length()-2));
				
				if(!StringUtil.isEmpty(kqmb1.giai_sau_1))arrLoto.add(kqmb1.giai_sau_1.substring(kqmb1.giai_sau_1.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_sau_2))arrLoto.add(kqmb1.giai_sau_2.substring(kqmb1.giai_sau_2.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_sau_3))arrLoto.add(kqmb1.giai_sau_3.substring(kqmb1.giai_sau_3.length()-2));
				
				if(!StringUtil.isEmpty(kqmb1.giai_nam))arrLoto.add(kqmb1.giai_nam.substring(kqmb1.giai_nam.length()-2));
				
				if(!StringUtil.isEmpty(kqmb1.giai_tu_1))arrLoto.add(kqmb1.giai_tu_1.substring(kqmb1.giai_tu_1.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_tu_2))arrLoto.add(kqmb1.giai_tu_2.substring(kqmb1.giai_tu_2.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_tu_3))arrLoto.add(kqmb1.giai_tu_3.substring(kqmb1.giai_tu_3.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_tu_4))arrLoto.add(kqmb1.giai_tu_4.substring(kqmb1.giai_tu_4.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_tu_5))arrLoto.add(kqmb1.giai_tu_5.substring(kqmb1.giai_tu_5.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_tu_6))arrLoto.add(kqmb1.giai_tu_6.substring(kqmb1.giai_tu_6.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_tu_7))arrLoto.add(kqmb1.giai_tu_7.substring(kqmb1.giai_tu_7.length()-2));
				
				if(!StringUtil.isEmpty(kqmb1.giai_ba_1))arrLoto.add(kqmb1.giai_ba_1.substring(kqmb1.giai_ba_1.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_ba_2))arrLoto.add(kqmb1.giai_ba_2.substring(kqmb1.giai_ba_2.length()-2));
				
				if(!StringUtil.isEmpty(kqmb1.giai_nhi))arrLoto.add(kqmb1.giai_nhi.substring(kqmb1.giai_nhi.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_nhat))arrLoto.add(kqmb1.giai_nhat.substring(kqmb1.giai_nhat.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_dacbiet))arrLoto.add(kqmb1.giai_dacbiet.substring(kqmb1.giai_dacbiet.length()-2));
				
				String dau_0 = getDauDuoi(0,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_0"+j,dau_0);
				String dau_1 = getDauDuoi(1,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_1"+j,dau_1);
				String dau_2 = getDauDuoi(2,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_2"+j,dau_2);
				String dau_3 = getDauDuoi(3,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_3"+j,dau_3);
				String dau_4 = getDauDuoi(4,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_4"+j,dau_4);
				String dau_5 = getDauDuoi(5,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_5"+j,dau_5);
				String dau_6 = getDauDuoi(6,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_6"+j,dau_6);
				String dau_7 = getDauDuoi(7,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_7"+j,dau_7);
				String dau_8 = getDauDuoi(8,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_8"+j,dau_8);
				String dau_9 = getDauDuoi(9,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_9"+j,dau_9);
				
				
				j++;
				i++;
			}
			DBConfig.loadProperties();
			Writer out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(DBConfig.file_kqhtml_mt), "UTF8"));
			out.write(htmlMB, 0, htmlMB.length());
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void createFileTT_HTML_MT_ThanTai(ArrayList<KQMN> arrKQMN){
		try {
			String htmlMB = IOUtil.getFileContenntAsString("./conf/MienTrung2C.html","UTF8");
			if(arrKQMN.size()==3)
				htmlMB = IOUtil.getFileContenntAsString("./conf/MienTrung3C.html","UTF8");
		
			KQMN kqmb1 = arrKQMN.get(0);
			
			SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd"); 
			Date date;String str_thu = "Thu Hai";
				try {
					date = dt.parse(kqmb1.ngay_quay);
					SimpleDateFormat dt1 = new SimpleDateFormat("u");
					int thu = Integer.parseInt(dt1.format(date));
					
				    if(thu==1)str_thu = "Thu Hai"; else 
				    	if(thu==2)str_thu = "Thu Ba"; else
				    		if(thu==3)str_thu = "Thu Tư"; else
				    			if(thu==4)str_thu = "Thu Năm"; else
				    				if(thu==5)str_thu = "Thu Sau"; else
				    					if(thu==6)str_thu = "Thu Bảy"; else
				    						str_thu = "Chủ nhật";
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			
			htmlMB=htmlMB.replaceAll("THU_QUAY", str_thu);	
			
			htmlMB=htmlMB.replaceAll("NGAY_QUAY", kqmb1.ngay_quay);
			String imageloading ="<img style=\"width: 20px;margin-top: 10px;margin-bottom: 10px;\" src=\"http://xosothantai.vn/kkt_api/livexs/loading.gif\">";
			
			int i = 0;int j = 1;
			while(i<arrKQMN.size()){
				kqmb1= arrKQMN.get(i);
				
				String regex = "PROVINCE_NAME"+j;
				htmlMB=htmlMB.replaceAll(regex, kqmb1.province_name);
				
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TAM"+j, StringUtil.isEmpty(kqmb1.giai_tam)?imageloading:kqmb1.giai_tam);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_BAY"+j, StringUtil.isEmpty(kqmb1.giai_bay)?imageloading:kqmb1.giai_bay);
				
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_SAU"+j+"1",StringUtil.isEmpty(kqmb1.giai_sau_1)?imageloading:kqmb1.giai_sau_1);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_SAU"+j+"2",StringUtil.isEmpty(kqmb1.giai_sau_2)?imageloading:kqmb1.giai_sau_2);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_SAU"+j+"3",StringUtil.isEmpty(kqmb1.giai_sau_3)?imageloading:kqmb1.giai_sau_3);
				
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_NAM"+j,StringUtil.isEmpty(kqmb1.giai_nam)?imageloading:kqmb1.giai_nam);
				
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TU"+j+"1",StringUtil.isEmpty(kqmb1.giai_tu_1)?imageloading:kqmb1.giai_tu_1);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TU"+j+"2",StringUtil.isEmpty(kqmb1.giai_tu_2)?imageloading:kqmb1.giai_tu_2);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TU"+j+"3",StringUtil.isEmpty(kqmb1.giai_tu_3)?imageloading:kqmb1.giai_tu_3);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TU"+j+"4",StringUtil.isEmpty(kqmb1.giai_tu_4)?imageloading:kqmb1.giai_tu_4);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TU"+j+"5",StringUtil.isEmpty(kqmb1.giai_tu_5)?imageloading:kqmb1.giai_tu_5);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TU"+j+"6",StringUtil.isEmpty(kqmb1.giai_tu_6)?imageloading:kqmb1.giai_tu_6);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TU"+j+"7",StringUtil.isEmpty(kqmb1.giai_tu_7)?imageloading:kqmb1.giai_tu_7);
				
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_BA"+j+"1",StringUtil.isEmpty(kqmb1.giai_ba_1)?imageloading:kqmb1.giai_ba_1);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_BA"+j+"2",StringUtil.isEmpty(kqmb1.giai_ba_2)?imageloading: kqmb1.giai_ba_2);
				
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_NHI"+j,StringUtil.isEmpty(kqmb1.giai_nhi)?imageloading:kqmb1.giai_nhi);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_NHAT"+j,StringUtil.isEmpty(kqmb1.giai_nhat)?imageloading:kqmb1.giai_nhat);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_DACBIET"+j,StringUtil.isEmpty(kqmb1.giai_dacbiet)?imageloading:kqmb1.giai_dacbiet);
				
				ArrayList<String> arrLoto = new ArrayList<String>();
				
				if(!StringUtil.isEmpty(kqmb1.giai_tam))arrLoto.add(kqmb1.giai_tam.substring(kqmb1.giai_tam.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_bay))arrLoto.add(kqmb1.giai_bay.substring(kqmb1.giai_bay.length()-2));
				
				if(!StringUtil.isEmpty(kqmb1.giai_sau_1))arrLoto.add(kqmb1.giai_sau_1.substring(kqmb1.giai_sau_1.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_sau_2))arrLoto.add(kqmb1.giai_sau_2.substring(kqmb1.giai_sau_2.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_sau_3))arrLoto.add(kqmb1.giai_sau_3.substring(kqmb1.giai_sau_3.length()-2));
				
				if(!StringUtil.isEmpty(kqmb1.giai_nam))arrLoto.add(kqmb1.giai_nam.substring(kqmb1.giai_nam.length()-2));
				
				if(!StringUtil.isEmpty(kqmb1.giai_tu_1))arrLoto.add(kqmb1.giai_tu_1.substring(kqmb1.giai_tu_1.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_tu_2))arrLoto.add(kqmb1.giai_tu_2.substring(kqmb1.giai_tu_2.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_tu_3))arrLoto.add(kqmb1.giai_tu_3.substring(kqmb1.giai_tu_3.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_tu_4))arrLoto.add(kqmb1.giai_tu_4.substring(kqmb1.giai_tu_4.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_tu_5))arrLoto.add(kqmb1.giai_tu_5.substring(kqmb1.giai_tu_5.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_tu_6))arrLoto.add(kqmb1.giai_tu_6.substring(kqmb1.giai_tu_6.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_tu_7))arrLoto.add(kqmb1.giai_tu_7.substring(kqmb1.giai_tu_7.length()-2));
				
				if(!StringUtil.isEmpty(kqmb1.giai_ba_1))arrLoto.add(kqmb1.giai_ba_1.substring(kqmb1.giai_ba_1.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_ba_2))arrLoto.add(kqmb1.giai_ba_2.substring(kqmb1.giai_ba_2.length()-2));
				
				if(!StringUtil.isEmpty(kqmb1.giai_nhi))arrLoto.add(kqmb1.giai_nhi.substring(kqmb1.giai_nhi.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_nhat))arrLoto.add(kqmb1.giai_nhat.substring(kqmb1.giai_nhat.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_dacbiet))arrLoto.add(kqmb1.giai_dacbiet.substring(kqmb1.giai_dacbiet.length()-2));
				
				String dau_0 = getDauDuoi(0,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_0"+j,dau_0);
				String dau_1 = getDauDuoi(1,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_1"+j,dau_1);
				String dau_2 = getDauDuoi(2,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_2"+j,dau_2);
				String dau_3 = getDauDuoi(3,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_3"+j,dau_3);
				String dau_4 = getDauDuoi(4,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_4"+j,dau_4);
				String dau_5 = getDauDuoi(5,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_5"+j,dau_5);
				String dau_6 = getDauDuoi(6,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_6"+j,dau_6);
				String dau_7 = getDauDuoi(7,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_7"+j,dau_7);
				String dau_8 = getDauDuoi(8,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_8"+j,dau_8);
				String dau_9 = getDauDuoi(9,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_9"+j,dau_9);
				
				
				j++;
				i++;
			}
			DBConfig.loadProperties();
			Writer out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(DBConfig.file_kqhtml_mt), "UTF8"));
			out.write(htmlMB, 0, htmlMB.length());
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void createFileTT_HTML_MT_XoSoPlus(ArrayList<KQMN> arrKQMN){
		try {
			String htmlMB = IOUtil.getFileContenntAsString("./conf/MienTrung2CXoSoPlus.html","UTF8");
			if(arrKQMN.size()==3)
				htmlMB = IOUtil.getFileContenntAsString("./conf/MienTrung3CXoSoPlus.html","UTF8");
		
			KQMN kqmb1 = arrKQMN.get(0);
			htmlMB=htmlMB.replaceAll("NGAY_QUAY", kqmb1.ngay_quay);
			String imageloading ="<img style=\"width: 20px;margin-top: 10px;margin-bottom: 10px;\" src=\"http://10h.vn/themes/images/loading.gif\">";
			
			int i = 0;int j = 1;
			while(i<arrKQMN.size()){
				kqmb1= arrKQMN.get(i);
				
				String regex = "PROVINCE_NAME"+j;
				htmlMB=htmlMB.replaceAll(regex, kqmb1.province_name);
				
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TAM"+j, StringUtil.isEmpty(kqmb1.giai_tam)?imageloading:kqmb1.giai_tam);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_BAY"+j, StringUtil.isEmpty(kqmb1.giai_bay)?imageloading:kqmb1.giai_bay);
				
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_SAU"+j+"1",StringUtil.isEmpty(kqmb1.giai_sau_1)?imageloading:kqmb1.giai_sau_1);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_SAU"+j+"2",StringUtil.isEmpty(kqmb1.giai_sau_2)?imageloading:kqmb1.giai_sau_2);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_SAU"+j+"3",StringUtil.isEmpty(kqmb1.giai_sau_3)?imageloading:kqmb1.giai_sau_3);
				
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_NAM"+j,StringUtil.isEmpty(kqmb1.giai_nam)?imageloading:kqmb1.giai_nam);
				
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TU"+j+"1",StringUtil.isEmpty(kqmb1.giai_tu_1)?imageloading:kqmb1.giai_tu_1);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TU"+j+"2",StringUtil.isEmpty(kqmb1.giai_tu_2)?imageloading:kqmb1.giai_tu_2);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TU"+j+"3",StringUtil.isEmpty(kqmb1.giai_tu_3)?imageloading:kqmb1.giai_tu_3);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TU"+j+"4",StringUtil.isEmpty(kqmb1.giai_tu_4)?imageloading:kqmb1.giai_tu_4);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TU"+j+"5",StringUtil.isEmpty(kqmb1.giai_tu_5)?imageloading:kqmb1.giai_tu_5);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TU"+j+"6",StringUtil.isEmpty(kqmb1.giai_tu_6)?imageloading:kqmb1.giai_tu_6);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_TU"+j+"7",StringUtil.isEmpty(kqmb1.giai_tu_7)?imageloading:kqmb1.giai_tu_7);
				
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_BA"+j+"1",StringUtil.isEmpty(kqmb1.giai_ba_1)?imageloading:kqmb1.giai_ba_1);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_BA"+j+"2",StringUtil.isEmpty(kqmb1.giai_ba_2)?imageloading: kqmb1.giai_ba_2);
				
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_NHI"+j,StringUtil.isEmpty(kqmb1.giai_nhi)?imageloading:kqmb1.giai_nhi);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_NHAT"+j,StringUtil.isEmpty(kqmb1.giai_nhat)?imageloading:kqmb1.giai_nhat);
				htmlMB=htmlMB.replaceAll("KQMN_GIAI_DACBIET"+j,StringUtil.isEmpty(kqmb1.giai_dacbiet)?imageloading:kqmb1.giai_dacbiet);
				
				ArrayList<String> arrLoto = new ArrayList<String>();
				
				if(!StringUtil.isEmpty(kqmb1.giai_tam))arrLoto.add(kqmb1.giai_tam.substring(kqmb1.giai_tam.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_bay))arrLoto.add(kqmb1.giai_bay.substring(kqmb1.giai_bay.length()-2));
				
				if(!StringUtil.isEmpty(kqmb1.giai_sau_1))arrLoto.add(kqmb1.giai_sau_1.substring(kqmb1.giai_sau_1.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_sau_2))arrLoto.add(kqmb1.giai_sau_2.substring(kqmb1.giai_sau_2.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_sau_3))arrLoto.add(kqmb1.giai_sau_3.substring(kqmb1.giai_sau_3.length()-2));
				
				if(!StringUtil.isEmpty(kqmb1.giai_nam))arrLoto.add(kqmb1.giai_nam.substring(kqmb1.giai_nam.length()-2));
				
				if(!StringUtil.isEmpty(kqmb1.giai_tu_1))arrLoto.add(kqmb1.giai_tu_1.substring(kqmb1.giai_tu_1.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_tu_2))arrLoto.add(kqmb1.giai_tu_2.substring(kqmb1.giai_tu_2.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_tu_3))arrLoto.add(kqmb1.giai_tu_3.substring(kqmb1.giai_tu_3.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_tu_4))arrLoto.add(kqmb1.giai_tu_4.substring(kqmb1.giai_tu_4.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_tu_5))arrLoto.add(kqmb1.giai_tu_5.substring(kqmb1.giai_tu_5.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_tu_6))arrLoto.add(kqmb1.giai_tu_6.substring(kqmb1.giai_tu_6.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_tu_7))arrLoto.add(kqmb1.giai_tu_7.substring(kqmb1.giai_tu_7.length()-2));
				
				if(!StringUtil.isEmpty(kqmb1.giai_ba_1))arrLoto.add(kqmb1.giai_ba_1.substring(kqmb1.giai_ba_1.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_ba_2))arrLoto.add(kqmb1.giai_ba_2.substring(kqmb1.giai_ba_2.length()-2));
				
				if(!StringUtil.isEmpty(kqmb1.giai_nhi))arrLoto.add(kqmb1.giai_nhi.substring(kqmb1.giai_nhi.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_nhat))arrLoto.add(kqmb1.giai_nhat.substring(kqmb1.giai_nhat.length()-2));
				if(!StringUtil.isEmpty(kqmb1.giai_dacbiet))arrLoto.add(kqmb1.giai_dacbiet.substring(kqmb1.giai_dacbiet.length()-2));
				
				String dau_0 = getDauDuoi(0,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_0"+j,dau_0);
				String dau_1 = getDauDuoi(1,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_1"+j,dau_1);
				String dau_2 = getDauDuoi(2,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_2"+j,dau_2);
				String dau_3 = getDauDuoi(3,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_3"+j,dau_3);
				String dau_4 = getDauDuoi(4,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_4"+j,dau_4);
				String dau_5 = getDauDuoi(5,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_5"+j,dau_5);
				String dau_6 = getDauDuoi(6,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_6"+j,dau_6);
				String dau_7 = getDauDuoi(7,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_7"+j,dau_7);
				String dau_8 = getDauDuoi(8,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_8"+j,dau_8);
				String dau_9 = getDauDuoi(9,arrLoto);htmlMB=htmlMB.replaceAll("DUOI_9"+j,dau_9);
				
				
				j++;
				i++;
			}
			DBConfig.loadProperties();
			Writer out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(DBConfig.file_kqhtml_mt_xosoplus), "UTF8"));
			out.write(htmlMB, 0, htmlMB.length());
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> sortLoto(ArrayList<String> boso){
		    int i = 0;
		    for(i = 1;i<boso.size();i++)
		    {
		    	String tem = boso.get(i);
		    	for(int j=0;j<boso.size()-1;j++){
		    		if(Integer.parseInt(boso.get(j).trim())>Integer.parseInt(boso.get(j+1).trim())){
		    			tem = boso.get(j);
		    			boso.set(j, boso.get(j+1));
		    			boso.set(j+1,tem);
		    		}
		    	}
		    }
		    return boso;
	}
	
	public static String filePID;
	public static void createPID()
	{
		try {
		 com.az24.util.FileLog.createFileLog(filePID);
		 FileLog.writer(Calendar.getInstance().getTimeInMillis()+"");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Khoi Tao Log PID");
		}
	}
	
	public static void deletePID()
	{
		try {
		 FileLog.file = filePID;
		 FileLog.delete();
		} catch (Exception e) {
			System.out.println("Xoa Log PID");
		}
	}
	
	public static boolean existPID()
	{
		boolean exist = false;
		try {
			 FileLog.file = filePID;
			 String log = FileLog.read();
			 long id = Long.parseLong(log);
			 if(id>0) exist = true;
		} catch (Exception e) {
			System.out.println("Khoi Tao Log PID");
		}
		return exist;
	}
}
