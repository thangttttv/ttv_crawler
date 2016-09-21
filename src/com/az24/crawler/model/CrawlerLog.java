package com.az24.crawler.model;

import java.sql.Timestamp;

public class CrawlerLog {
	public int id; 
	public String title; 
	public int result_id; 
	public int cat_id; 
	public String url_id; 
	public String url; 
	public String source; 
	public Object entity; 
	public  Timestamp create_date;
}
