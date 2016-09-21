package com.az24.crawler;

import hdc.crawler.AbstractCrawler.Datum;
import hdc.crawler.AbstractCrawler.Url;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.crawler.fetcher.HttpClientUtil;
import hdc.util.io.FileUtil;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;

import jdbm.PrimaryHashMap;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

import com.az24.crawler.config.JdbmXmlConfig;
import com.az24.util.FileLog;


public class CrawlerFetcher implements Runnable {
	protected RecordManager datumManager;
	protected PrimaryHashMap<String, Datum> datumPrimary;
	protected RecordManager urlManager;
	protected PrimaryHashMap<String, Url> urlPrimary;
	private String filejdbmconfig="";
	private  int fetch=1;
	
	
	public CrawlerFetcher(String filejdbmconfig,int fetch,int is_slow)
	{
		this.filejdbmconfig = filejdbmconfig;
		this.fetch = fetch;
		
	}
	
	public void run() {
		try {
		 
			JdbmXmlConfig.parseConfig(filejdbmconfig);
			FileUtil.deleteFile(JdbmXmlConfig.datum);
			HttpClientImpl client = new HttpClientImpl();
			HttpResponse res = null;
			datumManager = RecordManagerFactory.createRecordManager(JdbmXmlConfig.datum
					+ "/datum");
			datumPrimary = datumManager.hashMap("datum");
			urlManager = RecordManagerFactory.createRecordManager(JdbmXmlConfig.url
					+ "/url");
			urlPrimary = urlManager.hashMap("url");
			Iterator<Url> i = urlPrimary.values().iterator();
			int count = 0;
			Url urlFetch = null;
			Datum dataFetch = null;
			String html="";
			Pattern pattern = Pattern.compile("\\s");
			Matcher  matcher  =null;
			while (i.hasNext()) {
				urlFetch = i.next();
				matcher = pattern.matcher(urlFetch.url);
				if(matcher.find())
				urlFetch.url = matcher.replaceAll("%20");
				if(this.fetch==1)
				{
				  res = client.fetch(urlFetch.url);
				  html = HttpClientUtil.getResponseBody(res) ;
				}else
				{
				  html=HttpClientUtil.getHtml(urlFetch.url);
				}
				
				dataFetch = new Datum();
				dataFetch.id = urlFetch.id;
				dataFetch.url = urlFetch.url;
				dataFetch.data = html;
				dataFetch.processed = true;
				dataFetch.cat_id = urlFetch.cat_id;
				datumPrimary.put(urlFetch.id, dataFetch);
				if (count % 10 == 0)
					datumManager.commit();
				count++;
				System.out.println(new Date()+" Fetch Link Thu:"+count+" "+urlFetch.url); 

				Thread.sleep(1000);
			}
			
			 Calendar calendar = Calendar.getInstance();
			 String log=calendar.getTime().toString()+ " Tong Fetched:"+count;			
			 FileLog.createFileLog(JdbmXmlConfig.file_log+"_log_"+calendar.get(Calendar.DAY_OF_MONTH)+calendar.get(Calendar.MONTH)+calendar.get(Calendar.YEAR)+".txt");
			 FileLog.writer(log);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				datumManager.commit();
				datumManager.close();
				urlManager.commit();
				urlManager.close();
			} catch (IOException e) {				
				e.printStackTrace();
			}
			
		}

	}
	
	public static void main(String[] args) {
		CrawlerFetcher crawlerFetcher = new CrawlerFetcher("src/com/az24/crawler/config/jdbm.xml",1,1);
		crawlerFetcher.run();
	}

}
