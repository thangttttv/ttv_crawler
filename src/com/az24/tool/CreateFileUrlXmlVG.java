package com.az24.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class CreateFileUrlXmlVG {
	public static void main(String[] args) {
		try {
			java.io.File file = new File("src/com/az24/crawler/config/urlsVatGia.xml");
			FileWriter outputStream = new FileWriter(file);
			URL url = CreateFileUrlXmlVG.class.getResource("batdongsan.xls");
			//String realPathFile = url.getFile().replaceAll("%20", " ");			
			FileInputStream propsFile = new java.io.FileInputStream("D:/Document/Document_2012/DM_RaoVat/vatgia_az_2012.xls");
			
			Workbook w =Workbook.getWorkbook(propsFile);
			Sheet sheet = w.getSheet(0);
			Cell cell = null;
			StringBuffer stringb = new StringBuffer("<?xml version=\"1.0\" ?>\n  <info>\n    <urls>");
			String az_cate = "";	
			String url1="";
			String url2="";
			String regex="";int page=0;
			for (int i = 1; i < sheet.getRows(); i++) {
				try{
				 page = Integer.parseInt(sheet.getCell(2, i).getContents());
				}catch (Exception e) {
					page=0;
				}
				int j=1;
				while(j<=page)
				{
					az_cate = sheet.getCell(0, i).getContents();			
					url1 = sheet.getCell(3, i).getContents();
					url2 = sheet.getCell(4, i).getContents();
					regex = sheet.getCell(5, i).getContents();					
									
					stringb.append("\n      <url");stringb.append(" catid=\""+az_cate+"\"");
				
					stringb.append(" regex=\""+regex+"\""+">\n");
					
					cell = sheet.getCell(1, i);	
					stringb.append("            "+url1.trim()+j+url2.trim());
					
					stringb.append("\n      </url>");
					
					j++;
				}
			}
			stringb.append("\n    </urls>\n  ");
			stringb.append("\n    <website>\n          <baseUrl></baseUrl>  \n        <rewriterUrl></rewriterUrl>        \n </website>");
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
