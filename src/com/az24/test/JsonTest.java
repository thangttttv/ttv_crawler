package com.az24.test;

import hdc.crawler.fetcher.HttpClientImpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.json.simple.JSONValue;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonTest {
	
	
	public static void main(String[] args) {
		 String jsonText = "{\"first\": 123, \"second\": {\"1\":\"1\",\"2\":\"2\"}, \"third\": 789}";
		 // jsonText ="[{\"numFound\":1846,\"start\":12,\"maxScore\":4.6449256,\"docs\":[{\"id\":\"498622\"}]},{\"numFound\":1846,\"start\":12,\"maxScore\":4.6449256,\"docs\":[{\"id\":\"498622\"}]}]";
		 
		 HttpClientImpl client = new HttpClientImpl();
			HttpResponse res = client.fetch("http://baoquangninh.com.vn/search/select/?sort=date%20desc&q=siteid:249%20AND%20cateid:7016&start=26&rows=12&r=&wt=json&json.wrf=getSearchData&cp=0.3564357274756996");
			HttpClientUtil.printResponseHeaders(res);
			String html=jsonText;
			try {
				html = HttpClientUtil.getResponseBody(res);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Tieu De = "+ html);
			
			
		  JSONParser parser = new JSONParser();
		  ContainerFactory containerFactory = new ContainerFactory(){
		    public List creatArrayContainer() {
		      return new LinkedList();
		    }

		    public Map createObjectContainer() {
		      return new LinkedHashMap();
		    }
		                        
		  };
	
		  jsonText ="{\"string_time_detail\":[{\"time_study\":\"1\",\"time_day\":\"1,2,3,4,5,6,7\"},{\"time_study\":\"2\",\"time_day\":\"1,2,3,4,5,6,7\"}]}";
		 
		  try{
		    Map json = (Map)parser.parse(jsonText, containerFactory);
		    Iterator iter = json.entrySet().iterator();
		    System.out.println("==iterate result==");
		    while(iter.hasNext()){
		      Map.Entry entry = (Map.Entry)iter.next();
		      System.out.println(entry.getKey() + "=>" + entry.getValue());
		      
			    
		    }
		                        
		    System.out.println("==toJSONString()==");
		    System.out.println(JSONValue.toJSONString(json));
		  }
		  catch(ParseException pe){
		    System.out.println(pe);
		  }
		  
		  String s="[0,{\"1\":{\"2\":{\"3\":{\"4\":[5,{\"6\":7}]}}}}]";
		  Object obj=JSONValue.parse(s);
		  org.json.simple.JSONArray array=( org.json.simple.JSONArray)obj;
		  System.out.println("======the 2nd element of array======");
		  System.out.println(array.get(1));
		  System.out.println();
		                
		  org.json.simple.JSONObject obj2=(org.json.simple.JSONObject)array.get(1);
		  System.out.println("======field \"1\"==========");
		  System.out.println(obj2.get("1"));    

		                
		  s="{}";
		  obj=JSONValue.parse(s);
		  System.out.println(obj);
		                
		  s="[5,]";
		  obj=JSONValue.parse(s);
		  System.out.println(obj);
		                
		  s="[5,,2]";
		  obj=JSONValue.parse(s);
		  System.out.println(obj);
		  
		  s="[{\"id\":\"498621\", \"url\":\"http://baoquangninh.com.vn/the-thao/201203/Messi-tien-cu-nguoi-thay-Guardiola-2162292/\", \"avatar\":\"/dataimages/201202/normal/images627598_a.jpg\",   \"date\":\"2012-03-01T01:31:19Z\",  \"site\":\"baoquangninh.com.vn\",  \"cate\":\"Thể thao\", \"title\":\"Messi tiến cử người thay Guardiola\",}," +
		  		"{\"id\":\"498622\", \"url\":\"http://baoquangninh.com.vn/the-thao/201203/Messi-tien-cu-nguoi-thay-Guardiola-2162292/\", \"avatar\":\"/dataimages/201202/normal/images627598_a.jpg\",   \"date\":\"2012-03-01T01:31:19Z\",  \"site\":\"baoquangninh.com.vn\",  \"cate\":\"Thể thao\", \"title\":\"Messi tiến cử người thay Guardiola\",}]";
		 
		  s = "[{\"time_study\":\"1\",\"time_day\":\"1,2,3,4,5,6,7\"},{\"time_study\":\"2\",\"time_day\":\"1,2,3,4,5,6,7\"}]";
		  obj=JSONValue.parse(s);
		  
		  array=( org.json.simple.JSONArray)obj;
		  System.out.println("======the 2nd element of array======");
		  System.out.println(array.get(1));
		  System.out.println();
		 
		  try{
			    Map json = (Map)parser.parse(array.get(1).toString(), containerFactory);
			    Iterator iter = json.entrySet().iterator();
			    System.out.println("==iterate result==");
			    while(iter.hasNext()){
			      Map.Entry entry = (Map.Entry)iter.next();
			      System.out.println(entry.getKey() + "=>" + entry.getValue());
			    }
		 }catch (Exception e) {
			// TODO: handle exception
		}
	    
	    s="[{\"id\":\"498621\", \"url\":\"http://baoquangninh.com.vn/the-thao/201203/Messi-tien-cu-nguoi-thay-Guardiola-2162292/\", \"avatar\":\"/dataimages/201202/normal/images627598_a.jpg\",   \"date\":\"2012-03-01T01:31:19Z\",  \"site\":\"baoquangninh.com.vn\",  \"cate\":\"Thể thao\", \"title\":\"Messi tiến cử người thay Guardiola\",}," +
  		"{\"id\":\"498622\", \"url\":\"http://baoquangninh.com.vn/the-thao/201203/Messi-tien-cu-nguoi-thay-Guardiola-2162292/\", \"avatar\":\"/dataimages/201202/normal/images627598_a.jpg\",   \"date\":\"2012-03-01T01:31:19Z\",  \"site\":\"baoquangninh.com.vn\",  \"cate\":\"Thể thao\", \"title\":\"Messi tiến cử người thay Guardiola\",}]";
	    obj=JSONValue.parse(s);
	    
	    String kq = "55645";
	    System.out.println(kq.substring(kq.length()-2));
	    
	    ArrayList<String> boso = new ArrayList<String>();
	    boso.add("01");
	    boso.add("05");
	    boso.add("89");
	    boso.add("02");
	    boso.add("03");
	 
	    int i = 0;
	    for(i = 1;i<boso.size();i++)
	    {
	    	String tem = boso.get(i);
	    	for(int j=0;j<boso.size()-1;j++){
	    		if(Integer.parseInt(boso.get(j))>Integer.parseInt(boso.get(j+1))){
	    			tem = boso.get(j);
	    			boso.set(j, boso.get(j+1));
	    			boso.set(j+1,tem);
	    		}
	    	}
	    }
	    
	    for (String integer : boso) {
			System.out.println(integer);
		}
	    
	    
	}
}
