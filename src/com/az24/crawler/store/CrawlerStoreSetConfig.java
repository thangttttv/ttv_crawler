package com.az24.crawler.store;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.util.html.parser.XPathReader;
import hdc.util.text.StringUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathConstants;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.http.HttpResponse;

import com.az24.dao.StoreConfigDAO;
import com.az24.dao.StoreDAO;
import com.az24.test.HttpClientUtil;

public class CrawlerStoreSetConfig {
	public List<Store> getStoreFromExcel(String urlExcelMap)
			throws BiffException, IOException {
		//URL url = CrawlerStore.class.getResource(urlExcelMap);
		// String realPathFile = url.getFile().replaceAll("%20", " ");
		FileInputStream propsFile = new java.io.FileInputStream(urlExcelMap);
		Workbook w = Workbook.getWorkbook(propsFile);
		Sheet sheet = w.getSheet(1);
		Cell cell = null;
		List<Store> lisStore = new ArrayList<Store>();
		Store store = null;
		// Vat_Gia_Cat| Az24_Cat
		int type_menu = 1;
		for (int i = 1; i < sheet.getRows(); i++) {
			try {
				store = new Store();
				cell = sheet.getCell(1, i);
				String name = cell.getContents();
				store.name = name;
				
				cell = sheet.getCell(4, i);
				String alias = cell.getContents();
				store.store_alias = alias;
				
				
				cell = sheet.getCell(3, i);
				System.out.println(cell.getContents());
				type_menu = Integer.parseInt(cell.getContents());
				store.type_menu = type_menu;

				/*cell = sheet.getCell(4, i);
				String store_alias = cell.getContents();
				store.store_alias = store_alias;*/

				cell = sheet.getCell(5, i);
				String areas = cell.getContents();
				store.area = areas;

				cell = sheet.getCell(6, i);
				String email = cell.getContents();
				store.email = email;

				cell = sheet.getCell(7, i);
				String mobile = cell.getContents();
				store.mobile = mobile;

				cell = sheet.getCell(8, i);
				String address = cell.getContents();
				store.address = address;

				/*cell = sheet.getCell(9, i);
				String city = cell.getContents();
				store.province_code = city;*/
				System.out.println(store.store_alias);
				lisStore.add(store);
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
		return lisStore;
	}
	
	public  void downloadBanner(String store_alias) throws Exception {
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch("http://www.vatgia.com/"+store_alias.trim());
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		int them_ids[]  = {8,10,11,12,13,14,15,16,18,25,26,27,31,32,33,34,35,36,37,38,39};
		String pattern_src = "'src','(.*)','quality'";
	    String pattern_width = "'width','(.*)','height'";
	    String pattern_height = "'height','(.*)','title'";
	    
		String flash = (String) reader.read("//div[@class='header_banner']/div[1]/script[1]/text()", XPathConstants.STRING);
		if(StringUtil.isEmpty(flash))
		{
			flash = (String) reader.read("//DIV[@id='header']/TABLE[1]/TBODY[1]/TR[1]/TD[1]/DIV[1]/SCRIPT[2]/text()", XPathConstants.STRING);
			pattern_src = "'src','(.*)','quality'";
		    pattern_width = "'width','(.*)','height'";
		    pattern_height = "'height','(.*)','title'";
		}
		
		System.out.println("Store_alias------->"+store_alias);	
		System.out.println("Flash------->"+flash);	
		if(flash!=null)
		{
		    String src = "";
		    String width = "";
		    String height = "";
		      // Create a Pattern object
		      Pattern r = Pattern.compile(pattern_src);
		      // Now create matcher object.
		      Matcher m = r.matcher(flash);
		      if (m.find( )) {
		         System.out.println("Found value: " + m.group(0) );
		         System.out.println("Found value: " + m.group(1) );		
		         src = m.group(1);
		      } else {
		         System.out.println("NO MATCH");
		      }
		      
		      // Create a Pattern object
		      r = Pattern.compile(pattern_width);
		      // Now create matcher object.
		      m = r.matcher(flash);
		      if (m.find( )) {
		         System.out.println("Found value: " + m.group(0) );
		         System.out.println("Found value: " + m.group(1) );		
		         width = m.group(1);
		      } else {
		         System.out.println("NO MATCH");
		      }
		      
		      // Create a Pattern object
		      r = Pattern.compile(pattern_height);
		      // Now create matcher object.
		      m = r.matcher(flash);
		      if (m.find( )) {
		         System.out.println("Found value: " + m.group(0) );
		         System.out.println("Found value: " + m.group(1) );		
		         height = m.group(1);
		      } else {
		         System.out.println("NO MATCH");
		      }
		  
		  
		  
		   String fileName ="";
		    if(StringUtil.isEmpty(src))
		    {
		    	flash = (String) reader.read("//div[@class='header_banner']/div[1]/img[1]/@src", XPathConstants.STRING);
		    	src = flash;
		    	if(src.split("/").length<3) 
				   {
				     System.out.println("src----------->"+src);
				     return;
				 }
		    	fileName = flash.split("/")[2];
		    	
		    }else
		    {
		    	if(src.split("/").length<3) 
				   {
				     System.out.println("src----------->"+src);
				     return;
				 }		    	 
		    	 r = Pattern.compile(".swf");
		         m = r.matcher(src);
			     if (m.find( )) {
			    	fileName = src.split("/")[2];
			      }else
			      {
			    	fileName = src.split("/")[2]+ ".swf";
			    	src = src+".swf";
			      }
				
		    }
		    System.out.println("src----------->"+src);
		    if(StringUtil.isEmpty(src)) return;
		    
			StoreDAO storeDAO = new StoreDAO();
			CrawlerStore crawlerStore = new CrawlerStore();
			StoreConfigDAO storeConfigDAO = new StoreConfigDAO();	
			
		
			Calendar calendar =  Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH);
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			String strMonth = month<9?"0"+month:month+"";
			String strDay = day<9?"0"+day:day+"";
			String pre_folder = "/usr/src/java/tomcat7/webapps/images.az24.vn/store_upload/logo/";
			pre_folder = "d:/logo/";
			String disFolder = "/"+store_alias.trim()+"/"+year+"/"+strMonth+strDay+"/";
			File file = new File(pre_folder+disFolder);
			if(!file.exists())file.mkdirs();
			crawlerStore.downloadImage("http://www.vatgia.com"+src,pre_folder+disFolder+fileName);
			
			int stt = (int)Math.round(21*Math.random());
			if(stt>20)stt=20;
			int them_id = them_ids[stt];
			
			System.out.println(store_alias);
			System.out.println(them_id);
			if(StringUtil.isEmpty(width))width="1000";
			if(StringUtil.isEmpty(height))height="250";
			storeConfigDAO.updateStore(storeDAO.checkStore(store_alias.trim()), disFolder+fileName, Integer.parseInt(width),Integer.parseInt(height),them_id);
		}
	}

	
	public static void main(String[] args) throws Exception {
		CrawlerStoreSetConfig setConfig = new CrawlerStoreSetConfig();
		try {
			//List<Store> stores = setConfig.getStoreFromExcel(args[1]);
			List<Store> stores = setConfig.getStoreFromExcel("C:/Users/Tran The Thang/Desktop/500 GH/estores_9.xls");
			for (Store store : stores) {
				System.out.println(store.store_alias);
				setConfig.downloadBanner(store.store_alias);
				Thread.sleep(15);
			}
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
}
