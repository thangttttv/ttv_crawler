package com.az24.test;

import hdc.crawler.AbstractCrawler.Datum;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import junit.framework.TestCase;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class HttpClientPost extends TestCase {
    
    public static void test() throws Exception {
    	Calendar cal = Calendar.getInstance() ;      
        Calendar currentCal = Calendar.getInstance() ;
        DefaultHttpClient client = HttpClientFactory.getInstance() ;
        client.getParams().setParameter("http.protocol.expect-continue",false) ;
    	String end_date =  cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
    	HttpPost post = new HttpPost("http://ketqua.net/xo-so/so-ket-qua.php") ;
        List<NameValuePair> list = new ArrayList<NameValuePair>() ;
        list.add(new BasicNameValuePair("end_date", end_date)) ;
        list.add(new BasicNameValuePair("txtNumber", "30")) ;
        list.add(new BasicNameValuePair("slcTinh", "mb")) ;
        list.add(new BasicNameValuePair("cmdView", "Xem kết quả")) ;
        post.setEntity(new UrlEncodedFormEntity(list)) ;
        HttpResponse res = client.execute(post) ;
        Datum datum = new Datum() ;
        datum.id = "mb" + ":" + end_date ;
        datum.data = HttpClientUtil.getResponseBody(res) ;
        System.out.println(datum.data);
    }
    	
    public static void testBongDaWap() throws Exception {
    	Calendar cal = Calendar.getInstance() ;      
        DefaultHttpClient client = HttpClientFactory.getInstance() ;
        client.getParams().setParameter("http.protocol.expect-continue",false) ;
    	String end_date =  cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
    	HttpPost post = new HttpPost("http://bongda.wap.vn/ket-qua-vong-loai-euro-2016-6985.html") ;
        List<NameValuePair> list = new ArrayList<NameValuePair>() ;
        list.add(new BasicNameValuePair("slRound", "1")) ;
        post.setEntity(new UrlEncodedFormEntity(list)) ;
        HttpResponse res = client.execute(post) ;
        Datum datum = new Datum() ;
        datum.id = "mb" + ":" + end_date ;
        datum.data = HttpClientUtil.getResponseBody(res) ;
        System.out.println(datum.data);
    }
    	
    	
}
