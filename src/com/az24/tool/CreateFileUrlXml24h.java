package com.az24.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class CreateFileUrlXml24h {
	public static void main(String[] args) {
		try {
			java.io.File file = new File("src/com/az24/crawler/config/kenh14.xml");
			FileWriter outputStream = new FileWriter(file);
			URL url = CreateFileUrlXml24h.class.getResource("baloteen_map.xls");
			String realPathFile = url.getFile().replaceAll("%20", " ");			
			FileInputStream propsFile = new java.io.FileInputStream(realPathFile);
			
			Workbook w =Workbook.getWorkbook(propsFile);
			Sheet sheet = w.getSheet(0);
			Cell cell = null;
			String url1="http://vieclam.24h.com.vn/ban-hang-c";
			StringBuffer stringb = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n  <info>\n    <urls>");
			for (int i = 1; i < sheet.getRows(); i++) {
				for(int j=1;j<=5;j++)
				{
				cell = sheet.getCell(0, i);	
				
				stringb.append("\n      <url");stringb.append(" catid=\""+cell.getContents().trim()+"\"");
				cell = sheet.getCell(2, i);	
				stringb.append(" fetchNumber=\""+cell.getContents().trim()+"\"");
				cell = sheet.getCell(3, i);	
				stringb.append(" pagePara=\""+cell.getContents().trim()+"\">\n");
				
				cell = sheet.getCell(1, i);	
				stringb.append("            "+url1+cell.getContents().trim()+".html"+"?&p="+(j-1)*30);
				
				stringb.append("\n      </url>");
				//System.out.println(Integer.parseInt(cell.getContents()));
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
