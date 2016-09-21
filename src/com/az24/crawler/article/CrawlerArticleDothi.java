package com.az24.crawler.article;

import java.io.IOException;
import java.util.Calendar;

public class CrawlerArticleDothi {
	public static void main(String[] args) {
			try {
				Calendar calendarTmp = Calendar.getInstance();
				String day = calendarTmp.get(Calendar.DAY_OF_MONTH)<10?"0"+calendarTmp.get(Calendar.DAY_OF_MONTH):calendarTmp.get(Calendar.DAY_OF_MONTH)+"";
				String month = (calendarTmp.get(Calendar.MONTH)+1)<10?"0"+(calendarTmp.get(Calendar.MONTH)+1):(calendarTmp.get(Calendar.MONTH)+1)+"";
				String date = calendarTmp.get(Calendar.YEAR)+"-"+month+"-"+day+"%2023:59:59";
				ThreadCrawlerArticle threadCrawlerArticle = new ThreadCrawlerArticle(2,20);
				threadCrawlerArticle.addQuery("/data/crawler/conf/beanDothiTH.xml", "/data/crawler/conf/urlsDothiTH.xml", 1, 1,date,1,10,0,"toan-quoc");
				threadCrawlerArticle.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
