package com.az24.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import jxl.Cell;

public class CreateFileUrlXmlQAYeuLaptop {
	public static void main(String[] args) {
		try {
			java.io.File file = new File("src/com/az24/crawler/config/yeulaptop.xml");
			FileWriter outputStream = new FileWriter(file);
		
			String url1="http://www.yeulaptop.com/f97/i";
			StringBuffer stringb = new StringBuffer("<?xml version=\"1.0\" ?>\n  <info>\n    <urls>");
			for (int i = 1; i <= 136; i++) {
				
				stringb.append("\n      <url");stringb.append(" catid=\"246\"");
				
				stringb.append(" fetchNumber=\"1\"");
			
				stringb.append(" pagePara=\"\">\n");
				
			
				stringb.append("  "+url1+i+".html");
				
				stringb.append("\n      </url>");
				
			}
			stringb.append("\n    </urls>\n  ");
			stringb.append("\n    <website>\n          <baseUrl></baseUrl>  \n        <rewriterUrl></rewriterUrl>        \n </website>");
			stringb.append("\n    <regexs>\n           <regex></regex>      \n    	  </regexs>");
			stringb.append("\n </info> ");
			outputStream.write(stringb.toString());
			outputStream.close();
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		}  catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
