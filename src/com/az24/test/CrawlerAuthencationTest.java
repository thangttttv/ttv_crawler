package com.az24.test;

import hdc.crawler.fetcher.HttpClientFactory;
import hdc.crawler.fetcher.HttpClientUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.az24.util.io.FileUtil;

public class CrawlerAuthencationTest {
	public void fetch() throws Exception {
	    DefaultHttpClient client = HttpClientFactory.getInstance() ;
	    client.getParams().setParameter("http.protocol.expect-continue",true) ;
	        HttpPost post = new HttpPost("http://passport.yan.vn/dologin.ashx?returnURL=http%3a%2f%2fserviceprovider.yan.vn%3a80%2fauthorize.aspx%3foauth_callback%3dhttp%253A%252F%252Fconnectyanpassport.noi.vn%252FServiceLogin.aspx%253Fsp%253Dyanpassport%2526ckey%253DNoi%2526showlogin%253D1%2526nextGuest%253Dhttp%25253a%25252f%25252fnoi.vn%25253a80%25252fHome%25252fMemberView.aspx%25253fusername%25253dtronxoena%2526nextMember%253Dhttp%25253a%25252f%25252fnoi.vn%25253a80%25252fHome%25252fMemberView.aspx%25253fusername%25253dtronxoena%26oauth_token%3d0e2d60a4-334d-4e9f-bada-e6f0b3de290f%26consumerServiceId%3d18%26sid%3d18%26showlogin%3d1&s=18") ;
	        List<NameValuePair> list = new ArrayList<NameValuePair>() ;
	        list.add(new BasicNameValuePair("inputEmail", "thangttnd@gmail.com")) ;
	        list.add(new BasicNameValuePair("inputPassword", "thangtt123")) ;	       
	        list.add(new BasicNameValuePair("Submit2", "Đăng nhập")) ;
	        post.setEntity(new UrlEncodedFormEntity(list)) ;
	        HttpResponse res = client.execute(post) ;
	        HttpGet get = new HttpGet("http://passport.yan.vn/Home/Member.aspx?cmd=member") ;
	        res = client.execute(get) ;
	        
	        FileUtil fileUtil = new FileUtil();
	        fileUtil.writeToFile("d:/az24.html", HttpClientUtil.getResponseBody(res), false);
	        
	        get = new HttpGet("http://noi.vn/trang-ca-nhan-cua-thanh-vien/xem-ho-so-cua-thanh-vien/kute_bjve.noi") ;
	        res = client.execute(get) ;
	        
	        fileUtil.writeToFile("d:/az22224.html", HttpClientUtil.getResponseBody(res), false);
	  }
	
	public void blue() throws Exception {
		URL myURL = new URL("http://sms.8x77.vn:8077/mt-services/MTService?operation.view=sendMT");
		HttpURLConnection myURLConnection = (HttpURLConnection)myURL.openConnection();
		String userCredentials = "tritueviet:tritueviet@!231";
		String basicAuth = "Basic " + new String(new Base64().encode(userCredentials.getBytes()));
		
		
		myURLConnection.setRequestProperty ("Authorization", basicAuth);
		myURLConnection.setRequestMethod("POST");
		//myURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		//myURLConnection.setRequestProperty("Content-Length", "" + Integer.toString(list.));
		myURLConnection.setRequestProperty("Content-Language", "en-US");
		myURLConnection.setUseCaches(false);
		myURLConnection.setDoInput(true);
		myURLConnection.setDoOutput(true);
		//userMobile.mobile, message, Service_ID, Command_Code, Message_Type, userMobile.id+"", "1"
		//, "1", "0", Content_Type
		
		String Service_ID = "BLUESEA";
		String Command_Code = "TraoDoiDi";
		String Message_Type = "0";
		String Content_Type ="0";
		
		myURLConnection.addRequestProperty("string","84974838181" );
		myURLConnection.addRequestProperty("string0","c28gbGE=");
		myURLConnection.addRequestProperty("string1",Service_ID );
		myURLConnection.addRequestProperty("string2",Command_Code);
		myURLConnection.addRequestProperty("string3",Message_Type );
		myURLConnection.addRequestProperty("string4","212");
		myURLConnection.addRequestProperty("string5","1" );
		myURLConnection.addRequestProperty("string6","1" );
		myURLConnection.addRequestProperty("string7","0" );
		myURLConnection.addRequestProperty("string8",Content_Type );
		
		int responseCode = myURLConnection.getResponseCode();
		//System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(myURLConnection.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());
	  }
	
	
	public static void main(String[] args) {
		CrawlerAuthencationTest authencationTest = new CrawlerAuthencationTest();
		try {
			authencationTest.blue();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
