package com.az24.crawler.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class KeywordFilterConfig {
	
	public static List<String> keywords;
	
	public static void getKeyword()
	{
		try {
		/*	URL url = KeywordFilterConfig.class.getResource("keywords.xls");
			String realPathFile = url.getFile().replaceAll("%20", " ");
			FileInputStream propsFile = new java.io.FileInputStream(realPathFile);*/
			FileInputStream propsFile = new java.io.FileInputStream("./conf/keywords.xls");
			
			Workbook w =Workbook.getWorkbook(propsFile);
			Sheet sheet = w.getSheet(0);
			Cell cell = null;			
			keywords = new ArrayList<String>();
			for (int i = 1; i < sheet.getRows(); i++) {
				cell = sheet.getCell(0, i);				
				keywords.add(cell.getContents());				
			}
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		} catch (BiffException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		KeywordFilterConfig.getKeyword();
	}
	
	
}
