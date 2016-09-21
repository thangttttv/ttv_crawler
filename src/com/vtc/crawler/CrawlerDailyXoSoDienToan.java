package com.vtc.crawler;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.util.html.parser.XPathReader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathConstants;

import org.apache.http.HttpResponse;
import org.w3c.dom.NodeList;

import com.az24.crawler.model.KQDienToan123;
import com.az24.crawler.model.KQDienToan6x36;
import com.az24.crawler.model.KQThanTai;
import com.az24.dao.W10HDAO;
import com.az24.test.HttpClientUtil;

public class CrawlerDailyXoSoDienToan extends CrawlerDailyXoSo {
	public void crawler6X36() throws Exception {
		String url ="http://ketqua.net/xo-so-dien-toan-6x36.php";
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		String xpath__ngay_quay= "//div[@id='ketqua']/div/table/thead/tr/th/h2/text()";
		String ngay_quay = (String) reader.read(xpath__ngay_quay, XPathConstants.STRING);
		System.out.println(ngay_quay);
		Pattern  r1 = Pattern.compile(".* (\\d{2}/\\d{2}/\\d{4})");
	    Matcher  m1 = r1.matcher(ngay_quay);
	    if(m1.find())
	      {
	    	 ngay_quay = m1.group(1);
	    	 
	    	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
	    	SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
	    	Date date = formatter.parse(ngay_quay+" 23:59:59");
	    	ngay_quay = formatter2.format(date);
	    		
	    	 System.out.println("Found value: "+ngay_quay);
	    	 NodeList nodeResult = (NodeList) reader.read("//div[@id='ketqua']/div/table/tbody/tr[1]/td", XPathConstants.NODESET);
	    	 int i = 1;
	    	 KQDienToan6x36 kqDienToan6x36 = new KQDienToan6x36();
	    	 kqDienToan6x36.ngay_quay = ngay_quay;
	    	 formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	    	 
	    	 java.util.Date currentDate = Calendar.getInstance().getTime();
				String ngay_quay_1 = formatter.format(currentDate);
				kqDienToan6x36.create_date = ngay_quay_1;
				kqDienToan6x36.modify_date = ngay_quay_1;
				kqDienToan6x36.create_user ="crawler";
				kqDienToan6x36.modify_user ="crawler";
				
	    	 W10HDAO w10hdao = new W10HDAO();
	    	 if(nodeResult!=null)
	    		{
	    			while(i<=nodeResult.getLength())
	    			{
	    				String kq = (String) reader.read("//div[@id='ketqua']/div/table/tbody/tr/td["+i+"]/text()", XPathConstants.STRING);
	    				System.out.println("Found value: "+kq);
	    				if(i==1)kqDienToan6x36.ketqua_1 = kq;
	    				if(i==2)kqDienToan6x36.ketqua_2 = kq;
	    				if(i==3)kqDienToan6x36.ketqua_3 = kq;
	    				if(i==4)kqDienToan6x36.ketqua_4 = kq;
	    				if(i==5)kqDienToan6x36.ketqua_5= kq;
	    				if(i==6)kqDienToan6x36.ketqua_6 = kq;
	    				i++;
	    			}
	    			
	    			if(w10hdao.checkKqDT6x36(ngay_quay)==0)
	    			{
	    				w10hdao.saveKqDienToan6x36(kqDienToan6x36);
	    			}else{
	    				w10hdao.updateKqDienToan6x36(kqDienToan6x36,ngay_quay);
	    			}
	    			
	    		}
	    	
	    }
		
	}
	
	
	public void crawler123() throws Exception {
		String url ="http://ketqua.net/xo-so-dien-toan-123.php";
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		String xpath__ngay_quay= "//div[@id='ketqua']/div/table/thead/tr/th/h2/text()";
		String ngay_quay = (String) reader.read(xpath__ngay_quay, XPathConstants.STRING);
		System.out.println(ngay_quay);
		Pattern  r1 = Pattern.compile(".* (\\d{2}/\\d{2}/\\d{4})");
	    Matcher  m1 = r1.matcher(ngay_quay);
	    if(m1.find())
	      {
	    	 ngay_quay = m1.group(1);
	    	 SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		     SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
		     Date date = formatter.parse(ngay_quay+" 23:59:59");
		     ngay_quay = formatter2.format(date);
		    	 
	    	 System.out.println("Found value: "+ngay_quay);
	    	 NodeList nodeResult = (NodeList) reader.read("//div[@id='ketqua']/div/table/tbody/tr[1]/td", XPathConstants.NODESET);
	    	 int i = 1;
	    	 KQDienToan123 kqDienToan123 = new KQDienToan123();
	    	 kqDienToan123.ngay_quay = ngay_quay;
	    	 W10HDAO w10hdao = new W10HDAO();
	    	 formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	    	 java.util.Date currentDate = Calendar.getInstance().getTime();
				String ngay_quay_1 = formatter.format(currentDate);
				kqDienToan123.create_date = ngay_quay_1;
				kqDienToan123.modify_date = ngay_quay_1;
				kqDienToan123.create_user ="crawler";
				kqDienToan123.modify_user ="crawler";
			
	    	 
	    	 if(nodeResult!=null)
	    		{
	    			while(i<=nodeResult.getLength())
	    			{
	    				String kq = (String) reader.read("//div[@id='ketqua']/div/table/tbody/tr/td["+i+"]/text()", XPathConstants.STRING);
	    				System.out.println("Found value: "+kq);
	    				if(i==1)kqDienToan123.ketqua_1 = kq;
	    				if(i==2)kqDienToan123.ketqua_2 = kq;
	    				if(i==3)kqDienToan123.ketqua_3 = kq;
	    				
	    				i++;
	    			}
	    			
	    			if(w10hdao.checkKqDT123(ngay_quay)==0)
	    			{
	    				w10hdao.saveKqDienToan123(kqDienToan123);
	    			}else{
	    				w10hdao.updateKqDienToan123(kqDienToan123,ngay_quay);
	    			}
	    			
	    		}
	    	
	    }
		
	}
	
	public void crawlerThanTai() throws Exception {
		String url ="http://ketqua.net/xo-so-than-tai.php";
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		String xpath__ngay_quay= "//div[@id='ketqua']/div/table/thead/tr/th/h2/text()";
		String ngay_quay = (String) reader.read(xpath__ngay_quay, XPathConstants.STRING);
		System.out.println(ngay_quay);
		Pattern  r1 = Pattern.compile(".* (\\d{2}/\\d{2}/\\d{4})");
	    Matcher  m1 = r1.matcher(ngay_quay);
	    if(m1.find())
	      {
	    	 ngay_quay = m1.group(1);
	    	 SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		     SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
		     Date date = formatter.parse(ngay_quay+" 23:59:59");
		     ngay_quay = formatter2.format(date);
		     
	    	 System.out.println("Found value: "+ngay_quay);
	    	 NodeList nodeResult = (NodeList) reader.read("//div[@id='ketqua']/div/table/tbody/tr[1]/td", XPathConstants.NODESET);
	    	 int i = 1;
	    	 
	    	 KQThanTai kqThanTai = new KQThanTai();
	    	 kqThanTai.ngay_quay = ngay_quay;
	    	 W10HDAO w10hdao = new W10HDAO();
	    	 
	    	 java.util.Date currentDate = Calendar.getInstance().getTime();
	    	 formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				String ngay_quay_1 = formatter.format(currentDate);
				kqThanTai.create_date = ngay_quay_1;
				kqThanTai.modify_date = ngay_quay_1;
				kqThanTai.create_user ="crawler";
				kqThanTai.modify_user ="crawler";
	    	 
	    	 if(nodeResult!=null)
	    		{
	    			while(i<=nodeResult.getLength())
	    			{
	    				String kq = (String) reader.read("//div[@id='ketqua']/div/table/tbody/tr/td["+i+"]/text()", XPathConstants.STRING);
	    				System.out.println("Found value: "+kq);
	    				kqThanTai.ketqua = kq;
	    				i++;
	    			}
	    			
	    			if(w10hdao.checkKqThanTai(ngay_quay)==0)
	    			{
	    				w10hdao.saveKqDienToanThanTai(kqThanTai);
	    			}else{
	    				w10hdao.updateKqThanTai(kqThanTai,ngay_quay);
	    			}
	    		}
	    	
	    }
		
	}
	
	public void crawlerXSDienToan6x36TrucTiep() throws Exception {
		String url ="http://xoso.tructiep.vn/xo-so-dien-toan-6x36.html";
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		String html = HttpClientUtil.getResponseBody(res);

		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		String xpath__ngay_quay= "//div[@id='ctl00_XSDienToan_Kq6x36ControlId_UpdatePanel1']/div[1]/div[1]/div[1]/div[1]/h3/span/b/text()";
		String xpath_kq_1= "//div[@id='ctl00_XSDienToan_Kq6x36ControlId_UpdatePanel1']/div[1]/div[1]/div[2]/div[1]/text()";
		String xpath_kq_2= "//div[@id='ctl00_XSDienToan_Kq6x36ControlId_UpdatePanel1']/div[1]/div[1]/div[2]/div[2]/text()";
		String xpath_kq_3= "//div[@id='ctl00_XSDienToan_Kq6x36ControlId_UpdatePanel1']/div[1]/div[1]/div[2]/div[3]/text()";
		String xpath_kq_4= "//div[@id='ctl00_XSDienToan_Kq6x36ControlId_UpdatePanel1']/div[1]/div[1]/div[2]/div[4]/text()";
		String xpath_kq_5= "//div[@id='ctl00_XSDienToan_Kq6x36ControlId_UpdatePanel1']/div[1]/div[1]/div[2]/div[5]/text()";
		String xpath_kq_6= "//div[@id='ctl00_XSDienToan_Kq6x36ControlId_UpdatePanel1']/div[1]/div[1]/div[2]/div[6]/text()";
		String ngay_quay = (String) reader.read(xpath__ngay_quay, XPathConstants.STRING);
		
		String ketqua_1 = (String) reader.read(xpath_kq_1, XPathConstants.STRING);
		String ketqua_2 = (String) reader.read(xpath_kq_2, XPathConstants.STRING);
		String ketqua_3 = (String) reader.read(xpath_kq_3, XPathConstants.STRING);
		String ketqua_4 = (String) reader.read(xpath_kq_4, XPathConstants.STRING);
		String ketqua_5 = (String) reader.read(xpath_kq_5, XPathConstants.STRING);
		String ketqua_6 = (String) reader.read(xpath_kq_6, XPathConstants.STRING);
		
		System.out.println(ngay_quay+":"+ketqua_1);
		
		Pattern  r1 = Pattern.compile(".* (\\d{2}-\\d{2}-\\d{4})");
	    Matcher  m1 = r1.matcher(ngay_quay);
	    
	    if(m1.find())
	      {
	    	 ngay_quay = m1.group(1);
	    	 SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		     SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
		     Date date_open = formatter.parse(ngay_quay+" 23:59:59");
		     ngay_quay = formatter2.format(date_open);
		     
		     KQDienToan6x36 kqThanTai = new KQDienToan6x36();
	    	 kqThanTai.ngay_quay = ngay_quay;
	    	 W10HDAO w10hdao = new W10HDAO();
	    	 
	    	 java.util.Date currentDate = Calendar.getInstance().getTime();
	    	 formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				
	    	 String ngay_quay_1 = formatter.format(currentDate);
				
	    	 kqThanTai.create_date = ngay_quay_1;
			 kqThanTai.modify_date = ngay_quay_1;
			 kqThanTai.create_user ="crawler";
			 kqThanTai.modify_user ="crawler";
			 kqThanTai.ketqua_1 = ketqua_1;
			 kqThanTai.ketqua_2 = ketqua_2;
			 kqThanTai.ketqua_3 = ketqua_3;
			 kqThanTai.ketqua_4 = ketqua_4;
			 kqThanTai.ketqua_5 = ketqua_5;
			 kqThanTai.ketqua_6 = ketqua_6;
			 if(w10hdao.checkKqDT6x36(ngay_quay)==0)
    			{
    				w10hdao.saveKqDienToan6x36(kqThanTai);
    			}else{
    				w10hdao.updateKqDienToan6x36(kqThanTai,ngay_quay);
    			}
	      }
	}
	
	public void crawlerXSDienToan123TrucTiep() throws Exception {
		String url ="http://xoso.tructiep.vn/xo-so-dien-toan-123.html";
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		String html = HttpClientUtil.getResponseBody(res);

		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		String xpath__ngay_quay= "//div[@id='ctl00_XSDienToan_Kq123ControlId_UpdatePanel1']/div[1]/div[1]/div[1]/div[1]/h3[1]/span[1]/b[1]/text()";
		String xpath_kq_1= "//div[@id='ctl00_XSDienToan_Kq123ControlId_UpdatePanel1']/div[1]/div[1]/div[2]/div[1]/text()";
		String xpath_kq_2= "//div[@id='ctl00_XSDienToan_Kq123ControlId_UpdatePanel1']/div[1]/div[1]/div[2]/div[2]/text()";
		String xpath_kq_3= "//div[@id='ctl00_XSDienToan_Kq123ControlId_UpdatePanel1']/div[1]/div[1]/div[2]/div[3]/text()";
		String ngay_quay = (String) reader.read(xpath__ngay_quay, XPathConstants.STRING);
		
		String ketqua_1 = (String) reader.read(xpath_kq_1, XPathConstants.STRING);
		String ketqua_2 = (String) reader.read(xpath_kq_2, XPathConstants.STRING);
		String ketqua_3 = (String) reader.read(xpath_kq_3, XPathConstants.STRING);
		
		System.out.println(ngay_quay+":"+ketqua_1);
		
		Pattern  r1 = Pattern.compile(".* (\\d{2}-\\d{2}-\\d{4})");
	    Matcher  m1 = r1.matcher(ngay_quay);
	    
	    if(m1.find())
	      {
	    	 ngay_quay = m1.group(1);
	    	 SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		     SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
		     Date date_open = formatter.parse(ngay_quay+" 23:59:59");
		     ngay_quay = formatter2.format(date_open);
		     
		     KQDienToan123 kqThanTai = new KQDienToan123();
	    	 kqThanTai.ngay_quay = ngay_quay;
	    	 W10HDAO w10hdao = new W10HDAO();
	    	 
	    	 java.util.Date currentDate = Calendar.getInstance().getTime();
	    	 formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				
	    	 String ngay_quay_1 = formatter.format(currentDate);
				
	    	 kqThanTai.create_date = ngay_quay_1;
			 kqThanTai.modify_date = ngay_quay_1;
			 kqThanTai.create_user ="crawler";
			 kqThanTai.modify_user ="crawler";
			 kqThanTai.ketqua_1 = ketqua_1;
			 kqThanTai.ketqua_2 = ketqua_2;
			 kqThanTai.ketqua_3 = ketqua_3;
			 if(w10hdao.checkKqDT123(ngay_quay)==0)
    			{
    				w10hdao.saveKqDienToan123(kqThanTai);
    			}else{
    				w10hdao.updateKqDienToan123(kqThanTai,ngay_quay);
    			}
	      }
	}

	public void crawlerXSThanTaiATrucTiep() throws Exception {
		
		String url ="http://xoso.tructiep.vn/xo-so-than-tai.html";
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		String html = HttpClientUtil.getResponseBody(res);

		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		String xpath__ngay_quay= "//div[@id='ctl00_XSThanTai_KqTTControlId_UpdatePanel1']/div[1]/div[1]/div[1]/div[1]/h3/span/b/text()";
		String xpath_kq= "//div[@id='ctl00_XSThanTai_KqTTControlId_UpdatePanel1']/div[1]/div[1]/div[2]/div[1]/text()";
		String ngay_quay = (String) reader.read(xpath__ngay_quay, XPathConstants.STRING);
		String ketqua = (String) reader.read(xpath_kq, XPathConstants.STRING);
		System.out.println(ngay_quay+":"+ketqua);
		
		Pattern  r1 = Pattern.compile(".* (\\d{2}-\\d{2}-\\d{4})");
	    Matcher  m1 = r1.matcher(ngay_quay);
	    
	    if(m1.find())
	      {
	    	 ngay_quay = m1.group(1);
	    	 SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		     SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
		     Date date_open = formatter.parse(ngay_quay+" 23:59:59");
		     ngay_quay = formatter2.format(date_open);
		     
		     KQThanTai kqThanTai = new KQThanTai();
	    	 kqThanTai.ngay_quay = ngay_quay;
	    	 W10HDAO w10hdao = new W10HDAO();
	    	 
	    	 java.util.Date currentDate = Calendar.getInstance().getTime();
	    	 formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				
	    	 String ngay_quay_1 = formatter.format(currentDate);
				
	    	 kqThanTai.create_date = ngay_quay_1;
			 kqThanTai.modify_date = ngay_quay_1;
			 kqThanTai.create_user ="crawler";
			 kqThanTai.modify_user ="crawler";
			 kqThanTai.ketqua = ketqua;
			 if(w10hdao.checkKqThanTai(ngay_quay)==0)
    			{
    				w10hdao.saveKqDienToanThanTai(kqThanTai);
    			}else{
    				w10hdao.updateKqThanTai(kqThanTai,ngay_quay);
    			}
	      }
	}

	public static void main(String args[]) throws ParseException{
		CrawlerDailyXoSoDienToan.filePID = "./conf/pidKetQuaDienToan.txt";
		if(CrawlerDailyXoSoDienToan.existPID()) return; else CrawlerDailyXoSoDienToan.createPID();
		
		try {
			CrawlerDailyXoSoDienToan dienToan = new CrawlerDailyXoSoDienToan();
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String ngay_quay = formatter1.format(calendar.getTime());
			
			Date date1MN = formatter2.parse(ngay_quay+" 16:10:00");
			Date date2MN = formatter2.parse(ngay_quay+" 19:00:00");
			Date currentDate = calendar.getTime();
			int runNow = 0;
			if(args!=null&&args.length>0)
				runNow = Integer.parseInt(args[0]);
			
			try {
				while(currentDate.after(date1MN)&&date2MN.after(currentDate)||runNow==1){
				dienToan.crawlerXSDienToan123TrucTiep();
				Thread.sleep(10000);
				dienToan.crawlerXSDienToan6x36TrucTiep();
				Thread.sleep(10000);
				dienToan.crawlerXSThanTaiATrucTiep();
				Thread.sleep(10000);
				calendar = Calendar.getInstance();
				currentDate = calendar.getTime();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			CrawlerDailyXoSoDienToan.deletePID();
		}
		
	}
}
