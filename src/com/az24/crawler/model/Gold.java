package com.az24.crawler.model;

import java.sql.Date;

public class Gold {
	public int id; 
	public String provider; 
	public int province_id; 
	public String unit; 
	public String buy; 
	public String sale; 
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public int getProvince_id() {
		return province_id;
	}
	public void setProvince_id(int provinceId) {
		province_id = provinceId;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getBuy() {
		return buy;
	}
	public void setBuy(String buy) {
		this.buy = buy;
	}
	public String getSale() {
		return sale;
	}
	public void setSale(String sale) {
		this.sale = sale;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date createDate) {
		create_date = createDate;
	}
	public Date create_date;
}
