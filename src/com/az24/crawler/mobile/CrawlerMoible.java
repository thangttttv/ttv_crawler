package com.az24.crawler.mobile;

import hdc.crawler.DocumentAnalyzer;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.crawler.fetcher.HttpClientUtil;
import hdc.util.html.A;
import hdc.util.html.HttpURL;
import hdc.util.io.FileUtil;
import hdc.util.lang.UriID;
import hdc.util.text.StringUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;

import com.az24.db.pool.C3p0LogPool;

public class CrawlerMoible {
	
	private HashMap<String,String> urlMap = new HashMap<String, String>();
	private HashMap<String,String> urlMaped = new HashMap<String, String>();
	private HashMap<String,String> mobileMap = new HashMap<String, String>();
	private HttpClientImpl client = new HttpClientImpl();
	private String baseUrl; 
	private String fileName;
	private String rewriterUrl;
	private int limit = 30000;
	
	public void init(String baseUrl,String rewriterUrl,String fileName,int limit){
		this.baseUrl = baseUrl;
		this.rewriterUrl =  rewriterUrl;
		this.limit = limit;
		this.fileName = fileName;
		this.limit=limit;
	}
	
	public void extracter(String url)
			throws Exception {
		System.out.println(new Date()+"-->"+url);	
		HttpResponse res = null;
		try{
		 res = client.fetch(url);
		}catch (Exception e) {
			System.out.println(new Date()+"Loi-->"+url);	
		}
		if(res!=null)
			{
			HttpClientUtil.printResponseHeaders(res);
			String html = HttpClientUtil.getResponseBody(res);
			// Get Mobile
			List<String> mobileList = StringUtil.getMobile(html);	
			for (String mobile : mobileList) {
				if(!mobileMap.containsKey(mobile))
				{
					saveMobile(mobile);
				}
				mobileMap.put(mobile,mobile);
			}
			
			DocumentAnalyzer analyzer = new hdc.crawler.DocumentAnalyzer.DefaultDocumentAnayzer(baseUrl, rewriterUrl);
			// Get URL
			List<A> list = null;
			try {
				list = analyzer.analyze(html,url.trim());
				for (A a : list) {
					String idURL = (new UriID(new HttpURL(a.getURL()))).getIdAsString();
					if(!urlMaped.containsKey(idURL))
					urlMap.put(idURL, a.getUrl());
				}
			} catch (Exception e) {
				list = null;
			}
		}
		
		urlMap.remove((new UriID(new HttpURL(url))).getIdAsString());
		urlMaped.put((new UriID(new HttpURL(url))).getIdAsString(),url);
		if(urlMap.isEmpty()||mobileMap.size()>=limit) 
		{
			exportFile();	
			urlMap.clear();
			return;
		}
		
		
		Thread.sleep(1000);
		// Call Back
		Iterator<String> iterator = urlMap.values().iterator();
		while(iterator.hasNext())
		{
			if(mobileMap.size()>=limit) break;
			extracter(iterator.next());
		}
		
		
	}
	
	public int saveMobile(String mobile) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0LogPool.getConnection(); 
			conn.setAutoCommit(false);
			ps = conn
					.prepareStatement("INSERT INTO crawler_mobile	(mobile)VALUES(?)");
			ps.setString(1,mobile);
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			C3p0LogPool.attemptClose(conn);
			C3p0LogPool.attemptClose(ps);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}	
	
	public void exportFile()
	{
		Iterator<String> iterator = mobileMap.values().iterator();
		Pattern pattern = Pattern.compile("\\D");
		
		while(iterator.hasNext())
		{
			try {
				Matcher matcher = pattern.matcher(iterator.next());
				String mobile = matcher.replaceAll("");
				FileUtil.writeToFile("/data/crawler/data/mobile/mobile_"+this.fileName+".txt", mobile+"\n", true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		CrawlerMoible crawlerMoible = new CrawlerMoible();
		String domain = args[0];
		String domain_rewrite = args[1];
		String fileName = args[2];
		int limit = Integer.parseInt(args[3]);
		String url = args[4];
		
		crawlerMoible.init(domain, domain_rewrite,fileName, limit);
		try {
			crawlerMoible.extracter(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
