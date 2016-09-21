package com.az24.tool;

import hdc.util.text.StringUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class CreateFileUrlXmlTinhHinh {
	public static void main(String[] args) {
		try {
			java.io.File file = new File("D:/categorys2/urlsGiadinhTH.xml");
			FileWriter outputStream = new FileWriter(file);
			FileInputStream propsFile = new java.io.FileInputStream("D:/categorys2/tinhinh_map0803.xls");
			
			Workbook w =Workbook.getWorkbook(propsFile);
			Sheet sheet = w.getSheet(0);
			String url_1="",url_2="";
			String url1="http://giadinh.net.vn";
			String link="",regex="",cat_id="",fetch="10";
			StringBuffer stringb = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n  <info>\n    <urls>");
			for (int i = 2; i < sheet.getRows(); i++) {
				cat_id = sheet.getCell(5, i).getContents();	
				regex = sheet.getCell(4, i).getContents();
				link = sheet.getCell(3, i).getContents();
				url_1 = sheet.getCell(6, i).getContents();
				url_2 = sheet.getCell(7, i).getContents();
				fetch= sheet.getCell(8, i).getContents();
				if(StringUtil.isEmpty(regex))
				{
					regex = link.replaceAll("http://giadinh.net.vn", "")+"\\d+/\\d+/.*/";
				}
				
				if(!StringUtil.isEmpty(link))
				{
				stringb.append("\n      <url");stringb.append(" catid=\""+cat_id.trim()+"\"");				
				stringb.append(" fetchNumber=\""+fetch+"\"");		
				stringb.append(" url_1=\""+url_1+"\"");
				stringb.append(" url_2=\""+url_2+"\"");
				stringb.append(" collection=\"8\" pagePara=\"\" regex=\""+regex+"\" >\n");
				stringb.append("            "+link);
				stringb.append("\n      </url>");
				}
			}
			stringb.append("\n    </urls>\n  ");
			stringb.append("\n    <website>\n          <baseUrl>"+url1+"</baseUrl>  \n        <rewriterUrl>"+url1+"</rewriterUrl>        \n </website>");
			stringb.append("\n    <regexs>\n           <regex>/c\\d+/\\d+/.*chn$</regex>      \n    	  </regexs>");
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
