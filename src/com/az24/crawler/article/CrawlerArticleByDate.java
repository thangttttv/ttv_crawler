package com.az24.crawler.article;

import java.util.Calendar;

public class CrawlerArticleByDate {
	public static void main(String[] args) {
		try {				
				
			Calendar calendarTmp = Calendar.getInstance();						
			String day = calendarTmp.get(Calendar.DAY_OF_MONTH)<10?"0"+calendarTmp.get(Calendar.DAY_OF_MONTH):calendarTmp.get(Calendar.DAY_OF_MONTH)+"";
			String month = (calendarTmp.get(Calendar.MONTH)+1)<10?"0"+(calendarTmp.get(Calendar.MONTH)+1):(calendarTmp.get(Calendar.MONTH)+1)+"";
			String date =month+"/"+ day+"/"+calendarTmp.get(Calendar.YEAR);
			CrawlerArticleByPage crawlerArticleByPage = new CrawlerArticleByPage();
			crawlerArticleByPage.initialization(args[0],args[1], Integer.parseInt(args[4]), Integer.parseInt(args[5]),Integer.parseInt(args[6]),Integer.parseInt(args[7]),args[8],0,1,0);
			crawlerArticleByPage.collectionDataByDate(date);
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public static void main1(String[] args) {
		
		try {				
				String startDates[] = args[2].split("/");
				String endDates[] = args[3].split("/");
				/*String startDates[] ="01/00/2006".split("/");;
				String endDates[] = "01/01/2006".split("/");*/
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
					String date =month+"/"+ day+"/"+calendarTmp.get(Calendar.YEAR);
					CrawlerArticleByPage crawlerArticleByPage = new CrawlerArticleByPage();
					crawlerArticleByPage.initialization(args[0],args[1], Integer.parseInt(args[4]), Integer.parseInt(args[5]),Integer.parseInt(args[6]),Integer.parseInt(args[7]),args[8],0,1,0);
					crawlerArticleByPage.collectionDataByDate(date);
				
					beginDate +=timeday; 
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
