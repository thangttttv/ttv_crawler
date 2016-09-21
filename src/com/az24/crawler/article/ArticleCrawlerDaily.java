package com.az24.crawler.article;

import java.util.Calendar;

import com.az24.crawler.CrawlerFetcher;
import com.az24.crawler.CrawlerInjectUrl;
import com.az24.crawler.config.UrlInjectXmlConfig;

public class ArticleCrawlerDaily implements Runnable {

	private String beanconfig = "";
	private String jdbmconfig = "";
	private String urlconfig = "";
	private int step;
	public String date;
	
	public ArticleCrawlerDaily(String beanconfig, String jdbmconfig,
			String urlconfig, int step,String date) {
		this.beanconfig = beanconfig;
		this.jdbmconfig = jdbmconfig;
		this.urlconfig = urlconfig;
		this.step = step;
		this.date = date;

	}

	public void run() {

		switch (step) {
		case 1:
			CrawlerInjectUrl crawlerInjectUrl = new CrawlerInjectUrl(urlconfig,
					jdbmconfig, beanconfig, 1, 1);
			crawlerInjectUrl.collectionUrl(this.date);
			break;
		case 2:
			CrawlerFetcher crawlerFetcher = new CrawlerFetcher(jdbmconfig, 1, 1);
			crawlerFetcher.run();
			break;
		case 3:
			ArticleCrawlerExtracter crawlerExtracter = new ArticleCrawlerExtracter(
					beanconfig, jdbmconfig,urlconfig);
			crawlerExtracter.run();
			break;
		case 4:
			ArticleImporter productImporter = new ArticleImporter(beanconfig,
					jdbmconfig);
			productImporter.run();
			break;
		case 5:
			ArticleDownloadImage downloadImage = new ArticleDownloadImage(
					this.jdbmconfig, this.beanconfig, UrlInjectXmlConfig.baseUrl);
			downloadImage.run();
			break;

		default:
			crawlerInjectUrl = new CrawlerInjectUrl(urlconfig, jdbmconfig,
					beanconfig, 1, 1);
			crawlerInjectUrl.collectionUrl(this.date);
			try {
				java.lang.Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			crawlerFetcher = new CrawlerFetcher(jdbmconfig, 1, 1);
			crawlerFetcher.run();
			try {
				java.lang.Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			crawlerExtracter = new ArticleCrawlerExtracter(beanconfig,
					jdbmconfig,urlconfig);
			crawlerExtracter.run();
			try {
				java.lang.Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			productImporter = new ArticleImporter(beanconfig, jdbmconfig);
			productImporter.run();

			/*downloadImage = new ArticleDownloadImage(this.jdbmconfig,
					this.beanconfig, UrlInjectXmlConfig.baseUrl);
			downloadImage.run();
			
			Calendar calendar = Calendar.getInstance();			
			String folder = "", pre_folder = "/usr/src/java/tomcat7/webapps/images.az24.vn/baloteen/picture/article/";			
			int month = calendar.get(Calendar.MONTH)+1;
			String strmonth = month<10?"0"+month:month+"";
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			String strDay = day<10?"0"+day:day+"";
			folder = pre_folder + calendar.get(Calendar.YEAR) + "/"
					+ strmonth
					+ strDay ;
			try {
					Runtime.getRuntime().exec("chmod -R 777 " + folder);
				} catch (IOException e) {					
					e.printStackTrace();
				}*/
			
			break;
		}

	}


	public static void main(String[] args) {
		/*String beanconfig = "D:/categorys2/beanVietnamnetTH.xml";
		String urlconfig = "D:/categorys2/urlsVietnamnetTH.xml";
		String jdmconfig = "src/com/az24/crawler/config/jdbm.xml";*/
		
		String urlconfig = args[0];
		String jdmconfig = args[1];
		String beanconfig = args[2];
		String step = args[3];
		String startDates[] = args[4].split("/");
		String endDates[] = args[5].split("/");
		
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
		
		int intstep =Integer.parseInt(step);
		while(beginDate<=endDate)
		{
			Calendar calendarTmp = Calendar.getInstance();
			calendarTmp.setTimeInMillis(beginDate);	
			String day = calendarTmp.get(Calendar.DAY_OF_MONTH)<10?"0"+calendarTmp.get(Calendar.DAY_OF_MONTH):calendarTmp.get(Calendar.DAY_OF_MONTH)+"";
			String month = (calendarTmp.get(Calendar.MONTH)+1)<10?"0"+(calendarTmp.get(Calendar.MONTH)+1):(calendarTmp.get(Calendar.MONTH)+1)+"";
			String date = calendarTmp.get(Calendar.YEAR)+"/"+month+"/"+day;
			ArticleCrawlerDaily crawlerDaily = new ArticleCrawlerDaily(beanconfig,jdmconfig, urlconfig, intstep,date);			
			crawlerDaily.run();
			beginDate +=timeday; 
		}
	}
}
