package com.az24.crawler.model;

public class Thread {
	private String time;
	private String bean_config;
	private String url_config;
	private String jdbm_config;
	private int importer_config=0;
	private int step=0;
	private int stepTo=0;
	private int fetch;	
	private int is_slow=0;
	
	public int getFetch() {
		return fetch;
	}
	public void setFetch(int fetch) {
		this.fetch = fetch;
	}
	public int getStepTo() {
		return stepTo;
	}
	public void setStepTo(int stepTo) {
		this.stepTo = stepTo;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getBean_config() {
		return bean_config;
	}
	public void setBean_config(String bean_config) {
		this.bean_config = bean_config;
	}
	public String getUrl_config() {
		return url_config;
	}
	public void setUrl_config(String url_config) {
		this.url_config = url_config;
	}
	public String getJdbm_config() {
		return jdbm_config;
	}
	public void setJdbm_config(String jdbm_config) {
		this.jdbm_config = jdbm_config;
	}
	public int getImporter_config() {
		return importer_config;
	}
	public void setImporter_config(int importer_config) {
		this.importer_config = importer_config;
	}
	public int getStep() {
		return step;
	}
	public void setStep(int step) {
		this.step = step;
	}
	public int getIs_slow() {
		return is_slow;
	}
	public void setIs_slow(int is_slow) {
		this.is_slow = is_slow;
	}
	
}
