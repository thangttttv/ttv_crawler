package com.az24.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

public class GCMSendServer {

	public static void main(String[] args) {
		  String date_s = "2016-06-23 00:00:00"; 
	      SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
	      Date date;
		try {
			date = dt.parse(date_s);
			SimpleDateFormat dt1 = new SimpleDateFormat("u");
		    System.out.println(dt1.format(date));
		    dt1 = new SimpleDateFormat("yyyy-mm-dd");
		    System.out.println(dt1.format(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	//	GCMSendServer.main2();
		/*try {
			URL url = new URL("https://android.googleapis.com/gcm/send");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "key="
					+ "AIzaSyARWYEwVGtawyWLP1MTkrTdkSvnLBQo_Ak");
			conn.setDoOutput(true);
			String input = "{\"registration_ids\" : [\"APA91bGqMtX1qufhFbhZEgk7r8jkRyYK6HLMkpXQ_ZbdP8wXHKBbv1XumXxsRl8Zm1Aysp6w2YUwxtAR4gXDdAAuz1igDDa73TVaxmJ5eLSsDDM5au0DtasOMpJobV-6nuBD3vgxVHXA\"],\"data\" : {\"message\": \"hai  welcome\"},}";
			
			InputStream is =	conn.getInputStream();
				OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			byte[] br = new byte[Short.MAX_VALUE];
			int length = 0;int totalRead = 0;
			while((length=is.read(br))>0){
				b.write(br, totalRead, length);
				totalRead += length;
			}
			String string = new String(b.toByteArray());
			System.out.println(string);
		    
		} catch (IOException e) {
			e.printStackTrace();
		}*/

	}
	
	public static void main2() {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost httpost = new HttpPost("https://android.googleapis.com/gcm/send");
		JSONObject holder = new JSONObject();
		
		holder.put("registration_ids", "85");
		holder.put("isDonation","False");
		holder.put("isSpecial","False");
		holder.put("moduleId","4");
		holder.put("pageId","1");
		holder.put("pageNumber","3");
		holder.put("pageSize","6");
		holder.put("sortValue","");
		holder.put("storeGuid","323559aa-a629-43df-ac55-0ec7512779d5");
		holder.put("strArrProperty","");
		StringEntity se;
		
		String data = "\"aps\":{\"alert\":{\"body\":\"HOT HOT: \",\"action-loc-key\":\"view\",\"cdata\":{\"type\":\"3\",\"oid\":\"132\",\"fid\":\"0\",\"icon\":\"\"}},\"sound\":\"default\"}";
		
		String input = "{\"registration_ids\" : [\"APA91bGqMtX1qufhFbhZEgk7r8jkRyYK6HLMkpXQ_ZbdP8wXHKBbv1XumXxsRl8Zm1Aysp6w2YUwxtAR4gXDdAAuz1igDDa73TVaxmJ5eLSsDDM5au0DtasOMpJobV-6nuBD3vgxVHXA\"],\"data\" : {"+data+"},}";

		try {
			se = new StringEntity(input);
			System.out.println(holder.toString());
			httpost.setEntity(se);
			httpost.setHeader("Content-type", "application/json");
			httpost.setHeader("Authorization", " key=AIzaSyB591JNcRpDMJrtPMKE3wYJVTM11dVEqBk");
			
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String response = httpclient.execute(httpost, responseHandler);
	        System.out.println(response);
	        
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
}
