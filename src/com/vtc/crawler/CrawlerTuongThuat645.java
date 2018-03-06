package com.vtc.crawler;

import hdc.crawler.fetcher.HttpClientImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.http.HttpResponse;

import com.az24.crawler.model.Max4D;
import com.az24.crawler.model.Mega645;
import com.az24.dao.VietlotoDAO;
import com.az24.db.pool.DBConfig;
import com.az24.test.HttpClientUtil;
import com.az24.util.io.IOUtil;

public class CrawlerTuongThuat645 extends CrawlerDailyXoSo {
	
	//http://www.minhngoc.com.vn/xstt/vietlott/vietlott.php?visit=0&_=1481950349270
	
	public void crawlerXoSoMeGa() throws Exception {
		String url ="http://www.minhngoc.com.vn/xstt/vietlott/vietlott.php?visit=0&_=1481950349270";
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		String html = HttpClientUtil.getResponseBody(res);
		
	//	html = IOUtil.getFileContenntAsString("C:/Projects/f1.txt");
	
		String ketqua[] = html.split(";");
		System.out.println(ketqua[7]);
		
		Calendar calendar = Calendar.getInstance();
		int day_of_week = calendar.get(Calendar.DAY_OF_WEEK);
		
		if(day_of_week==Calendar.WEDNESDAY||day_of_week==Calendar.SUNDAY
						||day_of_week==Calendar.FRIDAY)
		{
			if("dt6x45=true".equalsIgnoreCase(ketqua[6])){
				xoSoMeGa(ketqua[7]);
			}
		}else {
			if("dtmax4d=true".equalsIgnoreCase(ketqua[7])){
				xoSoMax4D(ketqua[8]);
			}
		}
	}
	
	
	public void xoSoMeGa(String ketqua) throws Exception {
		Mega645  mega645 = new Mega645();
		VietlotoDAO vietlotoDAO = new VietlotoDAO();
		
		String kqmg = ketqua.replaceAll("kqxs.mega=", "");
		JSONObject json = (JSONObject) JSONSerializer.toJSON(kqmg);
		JSONObject g = json.getJSONObject("g");	
		mega645.bo_1 = g.getString("1");
		mega645.bo_2 = g.getString("2");
		mega645.bo_3 = g.getString("3");
		mega645.bo_4 = g.getString("4");
		mega645.bo_5 = g.getString("5");
		mega645.bo_6 = g.getString("6");
		
		System.out.println(mega645.bo_1);
		JSONObject gt = json.getJSONObject("gt");	
		String jacpot_gt = gt.getString("j");
		try{
		mega645.jackpot_gia_tri = Double.parseDouble(jacpot_gt.replaceAll(",", ""));
		}catch(Exception e){}
		mega645.nhat_gia_tri = 10000000;
		mega645.nhi_gia_tri = 300000;
		mega645.ba_gia_tri = 30000;
		
		
		JSONObject sl = json.getJSONObject("sl");	
		try{
			mega645.jackpot_so_giai = sl.getInt("j");
		}catch(Exception e){}
		
		try{
			mega645.nhat_so_giai = sl.getInt("1");
		}catch(Exception e){}
			
		try{
			mega645.nhi_so_giai = sl.getInt("2");
		}catch(Exception e){}
		
		try{
			mega645.ba_so_giai = sl.getInt("3");
		}catch(Exception e){}
			
			
		
		
		System.out.println(mega645.jackpot_so_giai);
		
		String jp_giai_tri = json.getString("jp");
		String ngay_quay = json.getString("ng");
		String ngay_quay_tiep = json.getString("ne");
		mega645.ky_ve = json.getString("kxs");
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
		Date date_ngay_quay = sdf.parse(ngay_quay);
		sdf = new SimpleDateFormat("yyyy-M-dd");
		mega645.ngay_mo = sdf.format(date_ngay_quay);
		
		
	 	/*int id = vietlotoDAO.checkXoSo645(mega645.ngay_mo);
	 	
	 	if(id==0){
	 		vietlotoDAO.saveXoSo645(mega645.ky_ve, mega645.ngay_mo, mega645.bo_1, mega645.bo_2, mega645.bo_3, mega645.bo_4
	 				, mega645.bo_5, mega645.bo_6, mega645.jackpot_so_giai, mega645.jackpot_gia_tri, mega645.nhat_so_giai,mega645.nhat_gia_tri, mega645.nhi_so_giai, mega645.nhi_gia_tri, mega645.ba_so_giai
	 				, mega645.ba_gia_tri);
	 	}else{
	 		vietlotoDAO.updateXoSo645(id,mega645.ky_ve, mega645.ngay_mo, mega645.bo_1, mega645.bo_2, mega645.bo_3, mega645.bo_4
	 				, mega645.bo_5, mega645.bo_6, mega645.jackpot_so_giai, mega645.jackpot_gia_tri, mega645.nhat_so_giai,mega645.nhat_gia_tri, mega645.nhi_so_giai, mega645.nhi_gia_tri, mega645.ba_so_giai
	 				, mega645.ba_gia_tri);
	 	}*/
	 	
	 	
		Calendar calendar = Calendar.getInstance();
		sdf = new SimpleDateFormat("yyyy-M-dd");
		String ngay_hien_tai = sdf.format(calendar.getTime());
		Date date_ngay_hien_tai = sdf.parse(ngay_hien_tai);
		
		/*if(date_ngay_hien_tai.equals(date_ngay_quay))
		{*/
			JSONObject formDetailsJson = new JSONObject();
			formDetailsJson.put("ky_ve", mega645.ky_ve);
			formDetailsJson.put("ngay_mo", mega645.ngay_mo);
			formDetailsJson.put("bo_1", mega645.bo_1);
			formDetailsJson.put("bo_2", mega645.bo_2);
			formDetailsJson.put("bo_3", mega645.bo_3);
			formDetailsJson.put("bo_4", mega645.bo_4);
			formDetailsJson.put("bo_5", mega645.bo_5);
			formDetailsJson.put("bo_6", mega645.bo_6);
			
			System.out.println(String.format(",.2f", mega645.jackpot_gia_tri));
			
			formDetailsJson.put("jackpot_so_giai", mega645.jackpot_so_giai);
			formDetailsJson.put("jackpot_gia_tri", jacpot_gt.replaceAll(",", ""));
			formDetailsJson.put("nhat_so_giai", mega645.nhat_so_giai);
			formDetailsJson.put("nhat_gia_tri", "10000000");
			formDetailsJson.put("nhi_so_giai", mega645.nhi_so_giai);
			formDetailsJson.put("nhi_gia_tri", mega645.nhi_gia_tri);
			formDetailsJson.put("ba_so_giai", mega645.ba_so_giai);
			formDetailsJson.put("ba_gia_tri", mega645.ba_gia_tri);
			
			sdf = new SimpleDateFormat("M/dd/yyyy");
			Date date_ngay_quay_tiep = sdf.parse(ngay_quay_tiep);
			sdf = new SimpleDateFormat("yyyy-M-dd");
		
			
			formDetailsJson.put("ngay_mo_tiep", sdf.format(date_ngay_quay_tiep));
			formDetailsJson.put("gio_quay", "18:00");
			formDetailsJson.put("jackpot_gia_tri_ki_tiep", jp_giai_tri.replaceAll(",", ""));
			System.out.println(formDetailsJson.toString());
			createFileJSONTT645(formDetailsJson.toString());
		/*}else{
			JSONObject formDetailsJson = new JSONObject();
			String ky_ve = mega645.ky_ve.replaceAll("#", "");
			int int_ky_ve =Integer.parseInt(ky_ve)+1;
			
			formDetailsJson.put("ky_ve", int_ky_ve);
			formDetailsJson.put("ngay_mo", ngay_hien_tai);
			formDetailsJson.put("bo_1", "");
			formDetailsJson.put("bo_2", "");
			formDetailsJson.put("bo_3", "");
			formDetailsJson.put("bo_4", "");
			formDetailsJson.put("bo_5", "");
			formDetailsJson.put("bo_6", "");
			
			
			formDetailsJson.put("jackpot_so_giai", "");
			formDetailsJson.put("jackpot_gia_tri", jp_giai_tri);
			formDetailsJson.put("nhat_so_giai", "");
			formDetailsJson.put("nhat_gia_tri", mega645.nhat_gia_tri+"");
			formDetailsJson.put("nhi_so_giai", "");
			formDetailsJson.put("nhi_gia_tri", mega645.nhi_gia_tri);
			formDetailsJson.put("ba_so_giai", "");
			formDetailsJson.put("ba_gia_tri", mega645.ba_gia_tri);
			
			Date currentDate = calendar.getTime();
			int day_of_week = calendar.get(Calendar.DAY_OF_WEEK);
			
			sdf = new SimpleDateFormat("M/dd/yyyy");
			Date date_ngay_quay_tiep = sdf.parse(ngay_quay_tiep);
			if(day_of_week==Calendar.WEDNESDAY){
				date_ngay_quay_tiep = new Date(currentDate.getTime()+(2*24*60*60*1000)+10000);
			}else if(day_of_week==Calendar.FRIDAY){
				date_ngay_quay_tiep = new Date(currentDate.getTime()+(2*24*60*60*1000)+10000);
			}else if(day_of_week==Calendar.SUNDAY){
				date_ngay_quay_tiep = new Date(currentDate.getTime()+(3*24*60*60*1000)+10000);
			}
			
			
			sdf = new SimpleDateFormat("yyyy-M-dd");
		
			
			formDetailsJson.put("ngay_mo_tiep", sdf.format(date_ngay_quay_tiep));
			formDetailsJson.put("gio_quay", "18:00");
			formDetailsJson.put("jackpot_gia_tri_ki_tiep", jp_giai_tri);
			System.out.println(formDetailsJson.toString());
			createFileJSONTT645(formDetailsJson.toString());
		}
		*/
		
	}
	
	
	public void xoSoMax4D(String ketqua) throws Exception {
		Map<String, Integer> mapKT = new HashMap<String, Integer>();
		mapKT.put("A", 1);
		mapKT.put("B", 2);
		mapKT.put("C", 3);
		mapKT.put("D", 4);
		mapKT.put("E", 5);
		mapKT.put("G", 6);
		
		Max4D  mega645 = new Max4D();
		
		System.out.println(ketqua);
		VietlotoDAO vietlotoDAO = new VietlotoDAO();
		
		String kqmg = ketqua.replaceAll("kqxs.max=", "");
		JSONObject json = (JSONObject) JSONSerializer.toJSON(kqmg);
		JSONObject g = json.getJSONObject("g");	
		JSONObject kt = json.getJSONObject("kt");
		
		
		System.out.println((mapKT.get(kt.getString("3"))));
		Pattern r1 = Pattern.compile("\\W");
		Matcher m1 = null;
		try{
			mega645.nhat =g.getString(mapKT.get(kt.getString("3")).intValue()+"");
			
		    m1 = r1.matcher(mega645.nhat);
		    mega645.nhat = m1.replaceAll(" ");
		    int i = mega645.nhat.length();
		    while(i<4){
		    	mega645.nhat +=" ";
		    	i++;
		    }
			}catch(Exception exception){
				
			}
		try{
			mega645.nhi_1 =g.getString(mapKT.get(kt.getString("1")).intValue()+"");
			
		    m1 = r1.matcher(mega645.nhi_1);
		    mega645.nhi_1 = m1.replaceAll(" ");
		    int i = mega645.nhi_1.length();
		    while(i<4){
		    	mega645.nhi_1 +=" ";
		    	i++;
		    }
		    
			}catch(Exception exception){
				
			}
		try{
			mega645.nhi_2 =g.getString(mapKT.get(kt.getString("2")).intValue()+"");
			
		    m1 = r1.matcher(mega645.nhi_2);
		    mega645.nhi_2 = m1.replaceAll(" ");
		    int i = mega645.nhi_2.length();
		    while(i<4){
		    	mega645.nhi_2 +=" ";
		    	i++;
		    }
		    
			}catch(Exception exception){
				
			}
		try{
			mega645.kk_1 = (mega645.nhat+"").substring(1); 
			
		    m1 = r1.matcher(mega645.kk_1);
		    mega645.kk_1 = m1.replaceAll(" ");
		    int i = mega645.kk_1.length();
		    while(i<3){
		    	mega645.kk_1 +=" ";
		    	i++;
		    }
		    
			}catch(Exception exception){
				
			}
		try{
			mega645.kk_2 = (mega645.nhat+"").substring(2); 
			
		    m1 = r1.matcher(mega645.kk_2);
		    mega645.kk_2 = m1.replaceAll(" ");
		    int i = mega645.kk_2.length();
		    while(i<2){
		    	mega645.kk_2 +=" ";
		    	i++;
		    }
		    
			}catch(Exception exception){
				
			}
		
		int i = 1;int j = 0;
		while(i<=6){
			try{
				if(kt.getString("3")!=null&&kt.getString("2")!=null&&kt.getString("1")!=null)
				{
					if(i!=mapKT.get(kt.getString("3")).intValue()
							&&i!=mapKT.get(kt.getString("2")).intValue()
							&&i!=mapKT.get(kt.getString("1")).intValue())
						{
							if(j==0){
							mega645.ba_1 =g.getString(i+"");
							m1 = r1.matcher(mega645.ba_1);
						    mega645.ba_1 = m1.replaceAll(" ");
						    int k = mega645.ba_1.length();
						    while(k<4){
						    	mega645.ba_1 +=" ";
						    	k++;
						    }
						    
							}
							if(j==1){
							mega645.ba_2 =g.getString(i+"");
							m1 = r1.matcher(mega645.ba_2);
						    mega645.ba_2 = m1.replaceAll(" ");
						    int k = mega645.ba_2.length();
						    while(k<4){
						    	mega645.ba_2 +=" ";
						    	k++;
						    }
						    
							}
							if(j==2){
								mega645.ba_3 =g.getString(i+"");
								
								m1 = r1.matcher(mega645.ba_3);
							    mega645.ba_3 = m1.replaceAll(" ");
							    int k = mega645.ba_3.length();
							    while(k<4){
							    	mega645.ba_3 +=" ";
							    	k++;
							    }
							    
							}
							
							j++;
						}
				}
			}catch(Exception e){
				
			}
			
			i++;
		}
		
		
		mega645.nhat_giai_tri = 15000000;
		mega645.nhi_giai_tri = 6500000;
		mega645.ba_giai_tri = 3000000;
		mega645.kk_1_giai_tri = 1000000;
		mega645.kk_2_giai_tri = 100000;
		
		
		JSONObject sl = json.getJSONObject("sl");
		try{
		mega645.nhat_so_giai = sl.getInt("1");
		}catch(Exception e){}
		try{
			mega645.nhi_so_giai = sl.getInt("2");
			}catch(Exception e){}
		
		try{
			mega645.ba_so_giai = sl.getInt("3");
			}catch(Exception e){}
		try{
			mega645.kk_1_so_giai = sl.getInt("4");
			}catch(Exception e){}
		try{
			mega645.kk_2_so_giai = sl.getInt("5");
			}catch(Exception e){}
		
		
		
		
		
		System.out.println(mega645.nhat_so_giai);
		
		
		//String ngay_quay = json.getString("ng");
		String ngay_quay_tiep = json.getString("ne");
		mega645.ky_ve = json.getString("kxs");
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
		Date date_ngay_quay = null;
		
		
		
		sdf = new SimpleDateFormat("M/dd/yyyy");
		Date date_ngay_quay_tiep = sdf.parse(ngay_quay_tiep);
		sdf = new SimpleDateFormat("yyyy-M-dd");
		
		Calendar calendar_next = Calendar.getInstance();
		calendar_next.setTime(date_ngay_quay_tiep);
		int day_of_week = calendar_next.get(Calendar.DAY_OF_WEEK);
		
		if(day_of_week==Calendar.SATURDAY){
			date_ngay_quay = new Date(calendar_next.getTimeInMillis()-(2*24*60*60*1000)+10000);
		}else if(day_of_week==Calendar.THURSDAY){
			date_ngay_quay = new Date(calendar_next.getTimeInMillis()-(2*24*60*60*1000)+10000);
		}else if(day_of_week==Calendar.TUESDAY){
			date_ngay_quay =new Date(calendar_next.getTimeInMillis()-(3*24*60*60*1000)+10000);
		}
		Calendar calendar_now = Calendar.getInstance();
	/*	
		if(date_ngay_quay_tiep.after(calendar_now.getTime())){
			sdf = new SimpleDateFormat("yyyy-M-dd");
			mega645.ngay_mo = sdf.format(date_ngay_quay);
		}else{*/
			sdf = new SimpleDateFormat("yyyy-M-dd");
			mega645.ngay_mo = sdf.format(calendar_now.getTime());
	//	}
		
		
		
		
	 	int id = vietlotoDAO.checkXoSoMax4d(mega645.ngay_mo);
	 	
	 	if(id==0){
	 		vietlotoDAO.saveMax4d(mega645.ngay_mo, mega645.nhat+"", mega645.nhi_1+"", mega645.nhi_2+"", mega645.ba_1+""
	 				, mega645.ba_2+"", mega645.ba_3+""
	 				, mega645.kk_1+"", mega645.kk_2+"",mega645.nhat_so_giai,mega645.nhi_so_giai
	 				,mega645.ba_so_giai,mega645.kk_1_so_giai,mega645.kk_2_so_giai,mega645.nhat_giai_tri
	 				,mega645.nhi_giai_tri,mega645.ba_giai_tri,mega645.kk_1_giai_tri,mega645.kk_2_giai_tri);
	 	}else{
	 		vietlotoDAO.updateMax4d(id,mega645.ngay_mo, mega645.nhat+"", mega645.nhi_1+"", mega645.nhi_2+"", mega645.ba_1+""
	 				, mega645.ba_2+"", mega645.ba_3+""
	 				, mega645.kk_1+"", mega645.kk_2+"",mega645.nhat_so_giai,mega645.nhi_so_giai
	 				,mega645.ba_so_giai,mega645.kk_1_so_giai,mega645.kk_2_so_giai,mega645.nhat_giai_tri
	 				,mega645.nhi_giai_tri,mega645.ba_giai_tri,mega645.kk_1_giai_tri,mega645.kk_2_giai_tri);
	 	}
	 	
	 	
		Calendar calendar = Calendar.getInstance();
		sdf = new SimpleDateFormat("yyyy-M-dd");
		String ngay_hien_tai = sdf.format(calendar.getTime());
		Date date_ngay_hien_tai = sdf.parse(ngay_hien_tai);
		
		/*if(date_ngay_hien_tai.equals(date_ngay_quay))
		{*/
			JSONObject formDetailsJson = new JSONObject();
			formDetailsJson.put("ky_ve", mega645.ky_ve);
			formDetailsJson.put("ngay_mo", mega645.ngay_mo);
			formDetailsJson.put("nhat", mega645.nhat);
			formDetailsJson.put("nhi_1", mega645.nhi_1);
			formDetailsJson.put("nhi_2", mega645.nhi_2);
			formDetailsJson.put("ba_1", mega645.ba_1);
			formDetailsJson.put("ba_2", mega645.ba_2);
			formDetailsJson.put("ba_3", mega645.ba_3);
			formDetailsJson.put("kk_1", mega645.kk_1);
			formDetailsJson.put("kk_2", mega645.kk_2);
			
			
			formDetailsJson.put("nhat_so_giai", mega645.nhat_so_giai);
			formDetailsJson.put("nhat_giai_tri", "15000000");
			formDetailsJson.put("nhi_so_giai", mega645.nhi_so_giai);
			formDetailsJson.put("nhi_giai_tri",mega645.nhi_giai_tri);
			
			formDetailsJson.put("ba_so_giai", mega645.ba_so_giai);
			formDetailsJson.put("ba_giai_tri", mega645.ba_giai_tri);
			formDetailsJson.put("kk_1_so_giai", mega645.kk_1_so_giai);
			formDetailsJson.put("kk_1_giai_tri", mega645.kk_1_giai_tri);
			formDetailsJson.put("kk_2_so_giai", mega645.kk_2_so_giai);
			formDetailsJson.put("kk_2_giai_tri", mega645.kk_2_giai_tri);
			
			
		
			
			formDetailsJson.put("ngay_mo_tiep", sdf.format(date_ngay_quay_tiep));
			formDetailsJson.put("gio_quay", "18:00");
			
			System.out.println(formDetailsJson.toString());
			createFileJSONTTMax4D(formDetailsJson.toString());
		
		
	}
	
	
	public void createFileJSONTT645(String  kqjon ){
		try {
			DBConfig.loadProperties();
			File file = new File(DBConfig.file_kqjson_645);
			
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(kqjon.getBytes());
			fileOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void createFileJSONTTMax4D(String  kqjon ){
		try {
			DBConfig.loadProperties();
			File file = new File(DBConfig.file_kqjson_max4d);
			
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(kqjon.getBytes());
			fileOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		CrawlerDailyXoSoTrucTiep.filePID = "./conf/pidMega.txt";
		//if(CrawlerDailyXoSoMega.existPID()) return; else CrawlerDailyXoSoMega.createPID();
		CrawlerTuongThuat645 xoSoMega = new CrawlerTuongThuat645();
		try {
			Calendar calendar = Calendar.getInstance();
			
			SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String ngay_quay = formatter1.format(calendar.getTime());
			
			Date date1MN = formatter2.parse(ngay_quay+" 18:00:00");
			Date date2MN = formatter2.parse(ngay_quay+" 18:50:00");
			
			/*int runNowMN = 0;
			if(args!=null&&args.length>0)
				runNowMN = Integer.parseInt(args[0]);*/
			
			Date currentDate = calendar.getTime();
			int day_of_week = calendar.get(Calendar.DAY_OF_WEEK);
			while(true){
				if(currentDate.after(date1MN)&&date2MN.after(currentDate)
						&&(day_of_week==Calendar.WEDNESDAY||day_of_week==Calendar.SUNDAY
						||day_of_week==Calendar.FRIDAY||day_of_week==Calendar.THURSDAY||day_of_week==Calendar.TUESDAY||day_of_week==Calendar.SATURDAY)){
					
					try {
						xoSoMega.crawlerXoSoMeGa();
						Thread.sleep(2000);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				System.out.println("----------------Nghi 2000 S-------------------------");
				calendar = Calendar.getInstance();
				currentDate = calendar.getTime();
				}else{
					try {
						xoSoMega.crawlerXoSoMeGa();
						Thread.sleep(5000*60);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			CrawlerDailyXoSoKQVS.deletePID();
		}
		
	}

}
