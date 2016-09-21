package com.ttv.football;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class CountryUnit {
	
	public static HashMap<String,String> countryMap;
	
	public static void getCountry()
	{
		try {
			FileInputStream propsFile = new java.io.FileInputStream("./conf/country.xls");
			
			Workbook w =Workbook.getWorkbook(propsFile);
			Sheet sheet = w.getSheet(0);
			Cell cellVN = null;	
			Cell cellEng = null;	
			countryMap = new HashMap<String,String>();
			for (int i = 0; i < sheet.getRows(); i++) {
				cellVN = sheet.getCell(0, i);
				cellEng = sheet.getCell(1, i);	
				System.out.println(cellVN.getContents());
				countryMap.put(cellVN.getContents().trim().toLowerCase(),cellEng.getContents().trim());				
			}
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		} catch (BiffException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
	}
	
	public static void getCountryTxt()
	{

		BufferedReader br = null;
		countryMap = new HashMap<String,String>();
		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader("./conf/country.txt"));

			while ((sCurrentLine = br.readLine()) != null) {
				String[] arrCountry = sCurrentLine.split("_");
				System.out.println(arrCountry[0]);
				countryMap.put(arrCountry[0].trim().toLowerCase(),arrCountry[1].trim());		
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
	}
	
	public static void main(String[] args) {
		CountryUnit.getCountryTxt();
	}
	
	
}
