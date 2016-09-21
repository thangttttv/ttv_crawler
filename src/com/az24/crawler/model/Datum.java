package com.az24.crawler.model;

import java.io.Serializable;

public class Datum implements Serializable {
	private static final long serialVersionUID = 1L;
	public String id;
	public String url;
	public String data;
	public boolean processed = false;
}
