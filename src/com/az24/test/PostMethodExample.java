package com.az24.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.client.HttpClient;

public class PostMethodExample {
	 public static void main(String args[]) {

		    org.apache.commons.httpclient.HttpClient client = new org.apache.commons.httpclient.HttpClient();
		    client.getParams().setParameter("http.useragent", "Test Client");

		    BufferedReader br = null;

		    PostMethod method = new PostMethod("http://www.baophuyen.com.vn/NewsList.aspx?menu=Trong-tinh-130&page=3");
		    method.addParameter("__ASYNCPOST", "true");
		    method.addParameter("__EVENTARGUMENT", "3");
		    
		    method.addParameter("__EVENTTARGET", "ctl00$ContentPlaceHolder1$ctlPaging");
		    method.addParameter("__EVENTVALIDATION", "/wEWIAL+raDpAgLE5ML0AgLEw828AgLa+Kq1BgKB3p/aBwKv66w/AvTjsfMOApTGo7kEAqeF+bkBArCp1YMCAoio1pIIApXTqqwMAsP6yRECuZTu9wIClZOtmgMC48Ln5A0CmtWsvwYC47OtuQwCv7j4gAgC5/bdvQcCo8PcAQK9irybCwKVqrqxCALpp6+6CwLRvfOeAQLuuPSUDQKh896XCALB8N/qDwLmn+nlCALwjtiwBAK6p4jJAgK0xdrtAg0ivhw0j4VuhD8xMKO8IuP7PDLI");
		    method.addParameter("__SCROLLPOSITIONX", "0");
		    method.addParameter("__SCROLLPOSITIONY", "0");
		    method.addParameter(".Refresh", "4");
		    method.addParameter(".Title", "Phú Yên Online - Kinh tế");
		    method.addParameter("ctl00$scriptMan", "ctl00$ContentPlaceHolder1$uplNews|ctl00$ContentPlaceHolder1$ctlPaging");
		    

		    try{
		      HostConfiguration configuration = new HostConfiguration();
		      HttpStatus status = new HttpStatus();
		      
		      int returnCode = client.executeMethod(method);

		      if(returnCode == HttpStatus.SC_NOT_IMPLEMENTED) {
		        System.err.println("The Post method is not implemented by this URI");
		        // still consume the response body
		        method.getResponseBodyAsString();
		      } else {
		        br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
		        String readLine;
		        while(((readLine = br.readLine()) != null)) {
		          System.err.println(readLine);
		      }
		      }
		    } catch (Exception e) {
		      System.err.println(e);
		    } finally {
		      method.releaseConnection();
		      if(br != null) try { br.close(); } catch (Exception fe) {}
		    }

		  }
}
