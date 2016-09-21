package com.az24.tool;

import hdc.util.text.StringUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.az24.crawler.model.DatabaseConfig;
import com.az24.crawler.model.product.BaseDao;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class CreateFileUrlXmlVGProduct {
	public static void main(String[] args) {
		try {
			Connection conn = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection(
						"jdbc:mysql://210.211.97.11:3306/az24_store?characterEncoding=UTF-8",
						"product",
						"product!@#$%^");
		
				
			} catch (ClassNotFoundException e) {			
				e.printStackTrace();
			} catch (SQLException e) {		
				e.printStackTrace();
			}    
			
			BaseDao baseDao = new BaseDao();
			Map<Integer,Integer> cats = baseDao.getCats(conn);
			java.io.File file = new File("src/com/az24/crawler/config/vatgiaPro.xml");
			FileWriter outputStream = new FileWriter(file);
			URL url = CreateFileUrlXmlVGProduct.class.getResource("vatgia_nnban_cuoi.xls");
			String realPathFile = url.getFile().replaceAll("%20", " ");			
			FileInputStream propsFile = new java.io.FileInputStream(realPathFile);
			
			Workbook w =Workbook.getWorkbook(propsFile);
			Sheet sheet = w.getSheet(0);
			Cell cell = null;
			String urlVG="";int page=1, totalPage=1;
			StringBuffer stringb = new StringBuffer("<?xml version=\"1.0\" ?>\n  <info>\n    <urls>");
			
			for (int i = 1; i < sheet.getRows(); i++) {
				//get catId
				
				cell = sheet.getCell(2, i);	
				try{
				page = Integer.parseInt(cell.getContents().trim());
				}catch (Exception e) {
					page=1;
				}
				
				totalPage = page/50;
				totalPage +=page%50==0?0:1;
				
				int j =0;
				cell = sheet.getCell(1, i);
				urlVG = cell.getContents().trim();
				
				cell = sheet.getCell(2, i);
				int exten_id = 0;
				try{
					 System.out.println(cell.getContents().trim());
					 exten_id = Integer.parseInt(cell.getContents().trim());
				}catch (Exception e) {
					exten_id=0;
				}
				cell = sheet.getCell(0, i);	
				String strcat = cell.getContents().trim();
				if(StringUtil.isEmpty(strcat))continue;
				int cat_id = Integer.parseInt(strcat.trim());
				
				if(cats.containsKey(cat_id)) continue;
				
				System.out.println(urlVG);
				
				if(!StringUtil.isEmpty(urlVG))
				while(j<totalPage)
				{
					int fetchFrom  = j*50+1;
					String catVG ="0";
					try{
					 urlVG=urlVG.replaceAll("www.", "");
					 catVG = urlVG.substring("http://vatgia.com/".length(),urlVG.indexOf(","));
					}catch (Exception e) {
						 j++;
						continue;
					}
					stringb.append("\n      <url");stringb.append(" catid=\""+cat_id+"\"");					
					stringb.append(" fetchNumber=\"1\"");
					stringb.append(" fetchFrom=\""+fetchFrom+"\"");
					
					String regex = "http://www.vatgia.com/"+catVG+"/\\d+/.*.html$";
					int fetchTo  = (j+1)*50;
					fetchTo=fetchTo>page ? page:fetchTo;
					stringb.append(" fetchTo=\""+fetchTo+"\" ");
						
					stringb.append(" regex=\""+regex+"\" pagePara=\"\">\n");
					
					
					stringb.append("            "+urlVG);
					
					stringb.append("\n      </url>");
				    j++;
				   
				    
				}
				System.out.println("i="+i);
				
			}
			
			stringb.append("\n    </urls>\n  ");
			stringb.append("\n    <website>\n          <baseUrl>htttp://www.vatgia.com</baseUrl>  \n        <rewriterUrl>htttp://www.vatgia.com</rewriterUrl>        \n </website>");
			stringb.append("\n    <regexs>\n           <regex></regex>      \n    	  </regexs>");
			stringb.append("\n </info> ");
			outputStream.write(stringb.toString());
			outputStream.close();
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
