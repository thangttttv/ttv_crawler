package com.az24.crawler.model;

import java.sql.Date;

public class Weather {
	public int id; 
	public int province_id; 
	public float temperature_f; 
	public float temperature_t; 
	public float humidity; 
	public String info;
	public String wind;
	public Date create_date;
}
