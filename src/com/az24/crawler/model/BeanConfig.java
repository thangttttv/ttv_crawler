package com.az24.crawler.model;

import java.util.List;


public class BeanConfig {
	private String table;
	private String pkey;
	private String id;
	private String xpath;
	private String xpath_sub;
	private String xpath_sub_label;
	private String xpath_sub_value;
	
	private List<Property> properties;
	private List<BeanConfig> beans;	
	private List<ImageConfig> images;	
	
	public String getXpath() {
		return xpath;
	}
	public void setXpath(String xpath) {
		this.xpath = xpath;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<Property> getProperties() {
		return properties;
	}
	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}
	public List<BeanConfig> getBeans() {
		return beans;
	}
	public void setBeans(List<BeanConfig> beans) {
		this.beans = beans;
	}
	public String getPkey() {
		return pkey;
	}
	public void setPkey(String pkey) {
		this.pkey = pkey;
	}
	public List<ImageConfig> getImages() {
		return images;
	}
	public void setImages(List<ImageConfig> images) {
		this.images = images;
	}
	public String getXpath_sub() {
		return xpath_sub;
	}
	public void setXpath_sub(String xpath_sub) {
		this.xpath_sub = xpath_sub;
	}
	public String getXpath_sub_value() {
		return xpath_sub_value;
	}
	public void setXpath_sub_value(String xpath_sub_value) {
		this.xpath_sub_value = xpath_sub_value;
	}
	public String getXpath_sub_label() {
		return xpath_sub_label;
	}
	public void setXpath_sub_label(String xpath_sub_label) {
		this.xpath_sub_label = xpath_sub_label;
	}
	
}
