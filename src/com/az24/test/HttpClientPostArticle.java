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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.NodeList;

public class HttpClientPostArticle extends TestCase {
    
    public static void test() throws Exception {
    	Calendar cal = Calendar.getInstance() ;      
        Calendar currentCal = Calendar.getInstance() ;
        DefaultHttpClient client = HttpClientFactory.getInstance() ;
        client.getParams().setParameter("application/x-www-form-urlencoded",true) ;
    	String end_date =  cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
    	HttpPost post = new HttpPost("http://www.baophuyen.com.vn/NewsList.aspx?menu=Trong-tinh-130&page=8") ;
        List<NameValuePair> list = new ArrayList<NameValuePair>() ;
        
        
        list.add(new BasicNameValuePair("__ASYNCPOST", "true"));
        list.add(new BasicNameValuePair("__EVENTARGUMENT", "4"));
	    
        list.add(new BasicNameValuePair("__EVENTTARGET", "ctl00$ContentPlaceHolder1$ctlPaging"));
	    list.add(new BasicNameValuePair("__EVENTVALIDATION", "/wEWIAL+raDpAgLE5ML0AgLEw828AgLa+Kq1BgKB3p/aBwKv66w/AvTjsfMOApTGo7kEAqeF+bkBArCp1YMCAoio1pIIApXTqqwMAsP6yRECuZTu9wIClZOtmgMC48Ln5A0CmtWsvwYC47OtuQwCv7j4gAgC5/bdvQcCo8PcAQK9irybCwKVqrqxCALpp6+6CwLRvfOeAQLuuPSUDQKh896XCALB8N/qDwLmn+nlCALwjtiwBAK6p4jJAgK0xdrtAg0ivhw0j4VuhD8xMKO8IuP7PDLI"));
	    list.add(new BasicNameValuePair("__SCROLLPOSITIONX", "0"));
	    list.add(new BasicNameValuePair("__SCROLLPOSITIONY", "0"));
	    list.add(new BasicNameValuePair(".Refresh", "5"));
	    list.add(new BasicNameValuePair(".Title", "Phú Yên Online - Kinh tế"));
	    list.add(new BasicNameValuePair("ctl00$scriptMan", "ctl00$ContentPlaceHolder1$uplNews|ctl00$ContentPlaceHolder1$ctlPaging"));

      
   //     list.add(new BasicNameValuePair("cmdView", "Xem kết quả")) ;
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
