package com.az24.test;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.AbstractCrawler.Datum;
import hdc.util.html.parser.XPathReader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.xpath.XPathConstants;

import junit.framework.TestCase;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.NodeList;

public class HttpClientPostEbon extends TestCase {
   
    public static void test() throws Exception {
    	Calendar cal = Calendar.getInstance() ;      
        Calendar currentCal = Calendar.getInstance() ;
        DefaultHttpClient client = HttpClientFactory.getInstance() ;
        //client.getParams().setParameter("application/x-www-form-urlencoded",true) ;
        client.getParams().setParameter("application/json; charset=utf-8",true) ;
        client.getParams().setParameter("accept", "application/json");

    	String end_date =  cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
    	HttpPost post = new HttpPost("http://www.mbabyshop.com/eStore/eStore.asmx/GeteStoreProduct") ;
        List<NameValuePair> list = new ArrayList<NameValuePair>() ;
        list.add(new BasicNameValuePair("categoryId", "85"));
        list.add(new BasicNameValuePair("isDonation", "False"));	    
        list.add(new BasicNameValuePair("isSpecial", "False"));
	    list.add(new BasicNameValuePair("moduleId", "4"));
	    list.add(new BasicNameValuePair("pageId", "1"));
	    list.add(new BasicNameValuePair("pageNumber", "3"));
	    list.add(new BasicNameValuePair("pageSize", "6"));
	    list.add(new BasicNameValuePair("sortValue", ""));
	    list.add(new BasicNameValuePair("storeGuid", "323559aa-a629-43df-ac55-0ec7512779d5"));
	    list.add(new BasicNameValuePair("strArrProperty", ""));
	    post.setEntity(new UrlEncodedFormEntity(list)) ;
        HttpResponse res = client.execute(post) ;
        
        
		
        Datum datum = new Datum() ;
        datum.id = "mb" + ":" + end_date ;
        datum.data = HttpClientUtil.getResponseBody(res) ;
        System.out.println(datum.data);
    	XPathReader reader = CrawlerUtil.createXPathReaderByData(datum.data);
    	NodeList nodes = (NodeList) reader.read("//A", XPathConstants.NODESET);
		if (nodes != null) {
			int node_one_many = nodes.getLength();
			int i = 0;

			while (i < node_one_many) {
				String tag = nodes.item(i).getTextContent();
				System.out.println("I=" + i + "-----" + tag.trim());

				i++;
			}
		}
    }
    
    	
    	
}
