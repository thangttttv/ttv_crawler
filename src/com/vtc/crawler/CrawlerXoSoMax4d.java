package com.vtc.crawler;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.util.html.parser.XPathReader;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.xpath.XPathConstants;

import org.apache.http.HttpResponse;
import org.w3c.dom.NodeList;

import com.az24.dao.VietlotoDAO;
import com.az24.test.HttpClientUtil;

public class CrawlerXoSoMax4d extends CrawlerDailyXoSo {
	
	public void crawlerXoSoMeGa() throws Exception {
		String url ="http://xoso.me/kqxs-max4d-ket-qua-xo-so-dien-toan-tu-chon-so-max-4d-vietlott-ngay-hom-nay/1.html";
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		String html = HttpClientUtil.getResponseBody(res);
		
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		
		
		//CrawlerUtil.analysis(reader.getDocument(), "");
		
		String xpath__result= "//DIV[@class='box']";
		NodeList nodeResult = (NodeList) reader.read(xpath__result, XPathConstants.NODESET);
		
		int i = nodeResult.getLength();
		VietlotoDAO vietlotoDAO = new VietlotoDAO();
		
		while(i>=1){
			xpath__result= "//DIV[@class='box']["+i+"]/H2[@class='title-bor clearfix']";
			String ngay_quay = (String) reader.read(xpath__result, XPathConstants.STRING);
			
			ngay_quay = ngay_quay.trim();
			System.out.println(i+"-"+ngay_quay.trim());
			
			if(ngay_quay!=null&&!("".compareTo(ngay_quay)==0)){
				xpath__result= "//DIV[@class='box']["+i+"]/DIV/DIV/TABLE/TBODY/TR[2]/TD[1]";
				String giai_nhat = (String) reader.read(xpath__result, XPathConstants.STRING);
				System.out.println(i+"-"+giai_nhat.trim());
				
				xpath__result= "//DIV[@class='box']["+i+"]/DIV/DIV/TABLE/TBODY/TR[2]/TD[2]";
				String nhat = (String) reader.read(xpath__result, XPathConstants.STRING);
				System.out.println(i+"-"+nhat.trim());
				
				xpath__result= "//DIV[@class='box']["+i+"]/DIV/DIV/TABLE/TBODY/TR[2]/TD[3]";
				String nhat_soluong = (String) reader.read(xpath__result, XPathConstants.STRING);
				System.out.println(i+"-"+nhat_soluong.trim());
				
				xpath__result= "//DIV[@class='box']["+i+"]/DIV/DIV/TABLE/TBODY/TR[3]/TD[1]";
				String giai_nhi_1 = (String) reader.read(xpath__result, XPathConstants.STRING);
				System.out.println(i+"-"+giai_nhi_1.trim());
				
				xpath__result= "//DIV[@class='box']["+i+"]/DIV/DIV/TABLE/TBODY/TR[3]/TD[2]";
				String nhi_1 = (String) reader.read(xpath__result, XPathConstants.STRING);
				System.out.println(i+"-"+nhi_1.trim());
				
				xpath__result= "//DIV[@class='box']["+i+"]/DIV/DIV/TABLE/TBODY/TR[3]/TD[3]";
				String nhi_2 = (String) reader.read(xpath__result, XPathConstants.STRING);
				System.out.println(i+"-"+nhi_2.trim());
				
				xpath__result= "//DIV[@class='box']["+i+"]/DIV/DIV/TABLE/TBODY/TR[3]/TD[4]";
				String nhi_soluong = (String) reader.read(xpath__result, XPathConstants.STRING);
				System.out.println(i+"-"+nhi_soluong.trim());
				
				xpath__result= "//DIV[@class='box']["+i+"]/DIV/DIV/TABLE/TBODY/TR[4]/TD[1]";
				String giai_ba = (String) reader.read(xpath__result, XPathConstants.STRING);
				System.out.println(i+"-"+giai_ba.trim());
				
				xpath__result= "//DIV[@class='box']["+i+"]/DIV/DIV/TABLE/TBODY/TR[4]/TD[2]";
				String ba_1 = (String) reader.read(xpath__result, XPathConstants.STRING);
				System.out.println(i+"-"+ba_1.trim());
				
				xpath__result= "//DIV[@class='box']["+i+"]/DIV/DIV/TABLE/TBODY/TR[4]/TD[3]";
				String ba_2 = (String) reader.read(xpath__result, XPathConstants.STRING);
				System.out.println(i+"-"+ba_2.trim());
				
				xpath__result= "//DIV[@class='box']["+i+"]/DIV/DIV/TABLE/TBODY/TR[4]/TD[4]";
				String ba_3 = (String) reader.read(xpath__result, XPathConstants.STRING);
				System.out.println(i+"-"+ba_3.trim());
				
				xpath__result= "//DIV[@class='box']["+i+"]/DIV/DIV/TABLE/TBODY/TR[4]/TD[5]";
				String ba_soluong = (String) reader.read(xpath__result, XPathConstants.STRING);
				System.out.println(i+"-"+ba_soluong.trim());
				
				
				xpath__result= "//DIV[@class='box']["+i+"]/DIV/DIV/TABLE/TBODY/TR[5]/TD[1]";
				String giai_kk = (String) reader.read(xpath__result, XPathConstants.STRING);
				System.out.println(i+"-"+giai_kk.trim());
				
				xpath__result= "//DIV[@class='box']["+i+"]/DIV/DIV/TABLE/TBODY/TR[5]/TD[2]";
				String kk_1 = (String) reader.read(xpath__result, XPathConstants.STRING);
				System.out.println(i+"-"+kk_1.trim());
				
				xpath__result= "//DIV[@class='box']["+i+"]/DIV/DIV/TABLE/TBODY/TR[5]/TD[3]";
				String kk_1_soluong = (String) reader.read(xpath__result, XPathConstants.STRING);
				System.out.println(i+"-"+kk_1_soluong.trim());
				
				xpath__result= "//DIV[@class='box']["+i+"]/DIV/DIV/TABLE/TBODY/TR[6]/TD[2]";
				String kk_2 = (String) reader.read(xpath__result, XPathConstants.STRING);
				System.out.println(i+"-"+kk_2.trim());
				
				xpath__result= "//DIV[@class='box']["+i+"]/DIV/DIV/TABLE/TBODY/TR[6]/TD[3]";
				String kk_2_soluong = (String) reader.read(xpath__result, XPathConstants.STRING);
				System.out.println(i+"-"+kk_2_soluong.trim());
				
				String s_ngay_quay[] = ngay_quay.split(" ");
				ngay_quay = s_ngay_quay[s_ngay_quay.length-1];
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
				Date date = sdf.parse(ngay_quay);
				sdf = new SimpleDateFormat("yyyy-M-dd");
				ngay_quay = sdf.format(date);
				
				int id = vietlotoDAO.checkXoSoMax4d(ngay_quay);
				if(id==0)
				vietlotoDAO.saveMax4d(ngay_quay, nhat.trim(), nhi_1.trim(),
						nhi_2.trim(),ba_1.trim(),ba_2.trim()
						,ba_3.trim(),kk_1.trim(),kk_2.trim(),Integer.parseInt(nhat_soluong),Integer.parseInt(nhi_soluong),Integer.parseInt(ba_soluong)
						,Integer.parseInt(kk_1_soluong),Integer.parseInt(kk_2_soluong),15000000,6500000,3000000,1000000,100000);
				else
				{
					vietlotoDAO.updateMax4d(id, ngay_quay, nhat.trim(), nhi_1.trim(), nhi_2.trim(), ba_1.trim(),
							ba_2.trim(), ba_3.trim(), kk_1.trim(), kk_2.trim(),Integer.parseInt(nhat_soluong),Integer.parseInt(nhi_soluong),Integer.parseInt(ba_soluong)
							,Integer.parseInt(kk_1_soluong),Integer.parseInt(kk_2_soluong)
							,15000000,6500000,3000000,1000000,100000);
				}
			}
			
			
			i--;
		}
		
		
	}
	
	public static void main(String[] args) {
		CrawlerDailyXoSoTrucTiep.filePID = "./conf/pidMax4D.txt";
		//if(CrawlerDailyXoSoMega.existPID()) return; else CrawlerDailyXoSoMega.createPID();
		CrawlerXoSoMax4d xoSoMega = new CrawlerXoSoMax4d();
		try {
			

			Calendar calendar = Calendar.getInstance();
			
			SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String ngay_quay = formatter1.format(calendar.getTime());
			
			Date date1MN = formatter2.parse(ngay_quay+" 18:00:00");
			Date date2MN = formatter2.parse(ngay_quay+" 18:50:00");
			
			int runNowMN = 1;
			if(args!=null&&args.length>0)
				runNowMN = Integer.parseInt(args[0]);
			
			Date currentDate = calendar.getTime();
			int day_of_week = calendar.get(Calendar.DAY_OF_WEEK);
			while(currentDate.after(date1MN)&&date2MN.after(currentDate)
					&&(day_of_week==Calendar.TUESDAY||day_of_week==Calendar.THURSDAY
					||day_of_week==Calendar.SATURDAY)||runNowMN==1){
				
				try {
					xoSoMega.crawlerXoSoMeGa();
					Thread.sleep(300000);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			System.out.println("----------------Nghi 2000 S-------------------------");
			calendar = Calendar.getInstance();
			currentDate = calendar.getTime();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			CrawlerDailyXoSoKQVS.deletePID();
		}
		
	}

}
