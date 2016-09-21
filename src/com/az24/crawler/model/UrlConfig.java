package com.az24.crawler.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.http.NameValuePair;

public class UrlConfig implements Serializable {
	 	private static final long serialVersionUID = 1L;
	 	public String id ;
	    public String url ;
	    public int fetchNumber ;
	    public String method;	    
	    public ArrayList<ArrayList<NameValuePair>> list = null;
}
