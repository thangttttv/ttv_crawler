package com.az24.crawler.article;

import java.io.IOException;
import java.util.Calendar;

public class CrawlerArticleVnexpress {
	public static void main(String[] args) {
			try {
				
				String startDates[] = args[4].split("/");
				String endDates[] = args[5].split("/");
				
				//String startDates[] ="01/00/2006".split("/");;
				//String endDates[] = "01/01/2006".split("/");
				
				
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.YEAR, Integer.parseInt(startDates[2]));
				calendar.set(Calendar.MONTH, Integer.parseInt(startDates[1]));
				calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(startDates[0]));		
				long beginDate= calendar.getTimeInMillis();
				Calendar calendarEnd = Calendar.getInstance();
				calendarEnd.set(Calendar.YEAR, Integer.parseInt(endDates[2]));
				calendarEnd.set(Calendar.MONTH, Integer.parseInt(endDates[1]));
				calendarEnd.set(Calendar.DAY_OF_MONTH, Integer.parseInt(endDates[0]));		
				long endDate= calendarEnd.getTimeInMillis();
				long timeday = 24*60*60*1000;				
				
				while(beginDate<=endDate)
				{		
					Calendar calendarTmp = Calendar.getInstance();
					calendarTmp.setTimeInMillis(beginDate);	
					String day = calendarTmp.get(Calendar.DAY_OF_MONTH)<10?"0"+calendarTmp.get(Calendar.DAY_OF_MONTH):calendarTmp.get(Calendar.DAY_OF_MONTH)+"";
					String month = (calendarTmp.get(Calendar.MONTH)+1)<10?"0"+(calendarTmp.get(Calendar.MONTH)+1):(calendarTmp.get(Calendar.MONTH)+1)+"";
					String date = calendarTmp.get(Calendar.YEAR)+"/"+month+"/"+day;
				
					ThreadCrawlerArticle threadCrawlerArticle = new ThreadCrawlerArticle(2,20);
					threadCrawlerArticle.addQuery("/data/crawler/conf/beanVnexpressTH.xml", "/data/crawler/conf/urlsVnexpressTH.xml", 1, 1,date,1,6,0,"Toan-Quoc");
					threadCrawlerArticle.close();
					beginDate +=timeday; 
				}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
