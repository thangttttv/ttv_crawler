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

public class CreateFileUrlXmlMucTim {
	public static void main(String[] args) {
		try {
			java.io.File file = new File("src/com/az24/crawler/config/muctim.xml");
			FileWriter outputStream = new FileWriter(file);
			URL url = CreateFileUrlXmlMucTim.class.getResource("baloteen_map2508.xls");
			String realPathFile = url.getFile().replaceAll("%20", " ");			
			FileInputStream propsFile = new java.io.FileInputStream(realPathFile);
			
			Workbook w =Workbook.getWorkbook(propsFile);
			Sheet sheet = w.getSheet(6);
			Cell cell = null;
			String url1="http://muctim.com.vn";
			StringBuffer stringb = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n  <info>\n    <urls>");
			for (int i = 2; i < 9; i++) {
				for(int j=1;j>0;j--)
				{
				cell = sheet.getCell(4, i);	
				
				stringb.append("\n      <url");stringb.append(" catid=\""+cell.getContents().trim()+"\"");
				
				stringb.append(" fetchNumber=\"1\"");
				
				stringb.append(" collection=\"4\" pagePara=\"\" regex=\".*\\d+/$\" >\n");
				
				cell = sheet.getCell(3, i);	
				System.out.println(cell.getContents());
				String url2 = cell.getContents();				 
				stringb.append("            "+url2);
				
				stringb.append("\n      </url>");
				
				}
			}
			stringb.append("\n    </urls>\n  ");
			stringb.append("\n    <website>\n          <baseUrl>"+url1+"</baseUrl>  \n        <rewriterUrl>"+url1+"</rewriterUrl>        \n </website>");
			stringb.append("\n    <regexs>\n           <regex>.*\\d+$/</regex>      \n    	  </regexs>");
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
