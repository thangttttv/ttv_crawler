package com.az24.test;

import hdc.crawler.CrawlerUtil;
import hdc.util.html.parser.XPathReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.xml.xpath.XPathConstants;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.NodeList;

public class HttpClientJsonPost {
	public static void main(String[] args) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost httpost = new HttpPost("http://www.mbabyshop.com/eStore/eStore.asmx/GeteStoreProduct");
		JSONObject holder = new JSONObject();
		
		holder.put("categoryId", "85");
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
		try {
			se = new StringEntity(holder.toString());
			System.out.println(holder.toString());
			httpost.setEntity(se);
			httpost.setHeader("Accept", "application/json");
			httpost.setHeader("Content-type", "application/json");
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String response = httpclient.execute(httpost, responseHandler);
	        System.out.println(response);
	        response = response.replaceAll("\\\\u003c", "<");
	        response = response.replaceAll("\\\\u0027", "'");
	        response = response.replaceAll("\\\\u003e", ">");
	        JSONObject json = (JSONObject) JSONSerializer.toJSON(response);
	        String altitude = json.getString("d");
	        json = (JSONObject) JSONSerializer.toJSON(altitude);
	        altitude = json.getString("HTML");
	        System.out.println(altitude);
	        XPathReader reader = CrawlerUtil.createXPathReaderByData(response);
	    	NodeList nodes = (NodeList) reader.read("//A", XPathConstants.NODESET);
			if (nodes != null) {
				int node_one_many = nodes.getLength();
				int i = 0;

				while (i < node_one_many) {
					String tag = nodes.item(i).getTextContent();
					String link = nodes.item(i).getAttributes().getNamedItem("href").getTextContent();
					System.out.println("I=" + i + "-----" + link);

					i++;
				}
			}
			
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
	
	
	public static void main2(String[] args) {
		  try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet getRequest = new HttpGet("http://www.mbabyshop.com/eStore/eStore.asmx/GeteStoreProduct?%7B'storeGuid'%3A%20'323559aa-a629-43df-ac55-0ec7512779d5'%2C%20'categoryId'%3A%20'85'%2C%20'pageId'%3A%20'1'%2C%20'moduleId'%3A%20'4'%2C%20'pageNumber'%3A%20'3'%2C%20'pageSize'%3A%20'6'%2C%20'sortValue'%3A%20''%2C%20'strArrProperty'%3A%20''%2C%20'isSpecial'%3A%20'False'%2C%20'isDonation'%3A%20'False'%7D=");
			getRequest.addHeader("accept", "application/json");
			HttpResponse response = httpClient.execute(getRequest);
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
				   + response.getStatusLine().getStatusCode());
			}
			BufferedReader br = new BufferedReader(
	                         new InputStreamReader((response.getEntity().getContent())));
			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}
			httpClient.getConnectionManager().shutdown();
		  } catch (ClientProtocolException e) {
			e.printStackTrace();
		  } catch (IOException e) {
			e.printStackTrace();
		  }
	 
		}
	
	

	public static void main1(String[] args) { 
	        DefaultHttpClient httpClient = new DefaultHttpClient(); 
	        HttpPost post = new HttpPost("http://www.mbabyshop.com/eStore/eStore.asmx/GeteStoreProduct"); 
	        post.setHeader("Content-Type", "application/json"); 
	        try {
				post.setEntity(new StringEntity("{'storeGuid': '323559aa-a629-43df-ac55-0ec7512779d5', 'categoryId': '85', 'pageId': '1', 'moduleId': '4', 'pageNumber': '3', 'pageSize': '6', 'sortValue': '', 'strArrProperty': '', 'isSpecial': 'False', 'isDonation': 'False'}","UTF-8"));
				 ResponseHandler<String> responseHandler = new BasicResponseHandler(); 
			        String response = httpClient.execute(post,responseHandler); 
			        System.out.println(response); 
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 

	       
	    } 
	 
}
