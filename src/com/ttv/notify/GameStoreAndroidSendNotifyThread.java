package com.ttv.notify;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import com.az24.crawler.model.NoticeQueue;
import com.az24.dao.GameStoreDAO;

public class GameStoreAndroidSendNotifyThread extends Thread {
		
		private String[] keys = {"AIzaSyARWYEwVGtawyWLP1MTkrTdkSvnLBQo_Ak","AIzaSyB591JNcRpDMJrtPMKE3wYJVTM11dVEqBk","AIzaSyAYpUP9Y0R2jnShn1TfZYm0aHoWU-goyHk"};
		@Override
		public void run() {
			GameStoreDAO gameStoreDAO = new GameStoreDAO();
			while(true){
				ArrayList<NoticeQueue> listNoticeQueues = gameStoreDAO.getListNotify();
				for (NoticeQueue noticeQueue : listNoticeQueues) {
					int i = 0;
					while(i<keys.length){
						if(this.sendNotify(noticeQueue, keys[i])==1){
							gameStoreDAO.updateDeviceTokenChannel(noticeQueue.device_token, i);
							break;
						}
						i++;
					}
					
					gameStoreDAO.deleteNotice(noticeQueue.id);
					 try {
						sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
		}
		
		public  int sendNotify(NoticeQueue noticeQueue,String key) {
			GameStoreDAO gameStoreDAO = new GameStoreDAO();
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpPost httpost = new HttpPost("https://android.googleapis.com/gcm/send");
			StringEntity se;
			
			String data = noticeQueue.payload;
			int success = 0;
			String input = "{\"registration_ids\" : [\""+noticeQueue.device_token+"\"],\"data\" : "+data+",}";
			
			

			try {
				se = new StringEntity(input);
				httpost.setEntity(se);
				httpost.setHeader("Content-type", "application/json");
				httpost.setHeader("Authorization", " key="+key);
				
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				String response = httpclient.execute(httpost, responseHandler);
		        
		        JSONObject json = (JSONObject) JSONSerializer.toJSON(response);
		         success = json.getInt("success");
		         System.out.println("Send To: " + noticeQueue.device_token + ", kq="+success);
		         if(success==0) {
		        	 JSONArray results = (JSONArray) json.getJSONArray("results");
		        	 JSONObject result = (JSONObject) results.get(0);
		        	 String errorCode = result.getString("error");
		        	 if("MissingRegistration".equalsIgnoreCase(errorCode)||
		        			 "InvalidRegistration".equalsIgnoreCase(errorCode)||
		        			 "NotRegistered".equalsIgnoreCase(errorCode)){
		        		 gameStoreDAO.deleteDeviceToken(noticeQueue.device_token);
		        		 System.out.println("deleteDeviceToken: " + noticeQueue.device_token );
		        	 }
		        	 
		         }
		        
			} catch (UnsupportedEncodingException e) {
				System.out.println("" + e.getMessage()); 
			} catch (ClientProtocolException e) {
				System.out.println("" + e.getMessage());
			} catch (IOException e) {
				System.out.println("" + e.getMessage());
			} catch (Exception e) {
				System.out.println("" + e.getMessage());
			}
			
			return success ;
		}
		
		public static void main(String[] args) {
			GameStoreAndroidSendNotifyThread sendNotifyThread = new GameStoreAndroidSendNotifyThread();
			sendNotifyThread.run();
			
		}
}
