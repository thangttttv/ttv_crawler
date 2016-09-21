package com.az24.crawler.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.az24.util.UTF8Tool;

public class CityConfig {
	@SuppressWarnings("unchecked")
	public static int getCityId(String city) {
		int cityid = 0;
		ObjectInputStream oi;
		try {
			city = UTF8Tool.coDau2KoDau(city.trim().toLowerCase().replaceAll(" ", ""));
			/*oi = new ObjectInputStream(new FileInputStream(
					"src/com/az24/crawler/config/user.dat"));*/
			oi = new ObjectInputStream(new FileInputStream("./conf/city.dat"));			
			Map<String, Integer> map = (Map<String, Integer>)oi.readObject();
			if (map.containsKey(city))
				cityid = map.get(city);
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		}

		return cityid;
	}
	
	public static int getCityIdByPost(String city) {
		int cityid = 0;
		ObjectInputStream oi;
		try {
			city = UTF8Tool.coDau2KoDau(city.trim().toLowerCase().replaceAll(" ", ""));		
			oi = new ObjectInputStream(new FileInputStream("./conf/city.dat"));			
			Map<String, Integer> map = (Map<String, Integer>)oi.readObject();
			Set s = map.entrySet();
			Iterator i = s.iterator();
			
			while (i.hasNext()) {
			    Map.Entry entry = (Map.Entry) i.next();
			    String key = (String)entry.getKey();
			    Integer value = (Integer)entry.getValue();
			    if(key.indexOf(city)>-1)
			    {
			    	cityid = value;break;
			    }
			    System.out.println("Key = " + key + ", Value = " + value);
			}
		
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		}

		return cityid;
	}
	
	public static void main(String[] args) {
		System.out.println(CityConfig.getCityId("ha  nội"));
	}
}
