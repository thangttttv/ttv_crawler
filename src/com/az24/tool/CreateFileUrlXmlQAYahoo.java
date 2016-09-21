package com.az24.tool;

import hdc.util.text.StringUtil;

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

public class CreateFileUrlXmlQAYahoo {
	public static void main(String[] args) {
		try {
			java.io.File file = new File("src/com/az24/crawler/config/qayahoo.xml");
			FileWriter outputStream = new FileWriter(file);
			URL url = CreateFileUrlXmlVG.class.getResource("qayahoo.xls");
			String realPathFile = url.getFile().replaceAll("%20", " ");			
			FileInputStream propsFile = new java.io.FileInputStream(realPathFile);
			
			Workbook w =Workbook.getWorkbook(propsFile);
			Sheet sheet = w.getSheet(0);
			Cell cell = null;
			String url1="";
			String cat_id="";
			StringBuffer stringb = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n  <info>\n    <urls>");
			for (int i = 3; i < sheet.getRows(); i++) {
				cell = sheet.getCell(1, i);	cat_id = cell.getContents();
				cell = sheet.getCell(3, i);	url1 = cell.getContents();
				if(!StringUtil.isEmpty(url1))
				{				
					stringb.append("\n      <url");stringb.append(" catid=\""+cat_id.trim()+"\"");
					
					stringb.append(" fetchNumber=\"5\"");
						
					stringb.append(" pagePara=\"amplink=resolvedampcp=\">\n");
					
				
					stringb.append("            "+url1.replaceAll("&", "amp"));
					
					stringb.append("\n      </url>");
					
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
