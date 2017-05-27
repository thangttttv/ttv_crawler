package com.az24.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.json.simple.parser.JSONParser;

import com.az24.crawler.model.FBMatch;
import com.az24.dao.FootBallDAO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;


public class JsonParsing {
	
	public static Date addDays(Date date, int days)
	    {
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(date);
	        cal.add(Calendar.DATE, days); //minus number would decrement the days
	        return cal.getTime();
	}
	 
	public static void main(String[] args) throws Exception {
		String jsonTxt = "{'foo':'bar','coolness':2.0,'altitude':39000," +
					"'pilot':{'firstName':'Buzz', 'lastName':'Aldrin'}," +
					"'mission':'apollo 11'}";
		
	
		
		JSONObject json = (JSONObject) JSONSerializer.toJSON(jsonTxt);
		double coolness = json.getDouble("coolness");
		int altitude = json.getInt("altitude");
		JSONObject pilot = json.getJSONObject("pilot");
		String firstName = pilot.getString("firstName");
		String lastName = pilot.getString("lastName");

		System.out.println("Coolness: " + coolness);
		System.out.println("Altitude: " + altitude);
		System.out.println("Pilot: " + lastName);
		
		Iterator<Object> ds = json.keys();
		while(ds.hasNext()){
			System.out.println(ds.next());
		}
		
		List<String> mybeanList = new ArrayList<String>();
		mybeanList.add("S");
		mybeanList.add("b");

		JSONArray jsonA = JSONArray.fromObject(mybeanList);
		System.out.println(jsonA);

		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	Date date1 = sdf.parse("2009-12-31");
    	Date date2 = sdf.parse("2010-01-31");
    	while(date1.compareTo(date2)<0){
    		String key = sdf.format(date1);
    		System.out.println(key);
    		date1 = addDays(date1,1);
    	}

    	JsonParsing.getCartList();
	}
	
	public static String getCartList() throws ParseException {
	    JSONArray jsonArray = new JSONArray();

	    List<FBMatch> cartList = FootBallDAO.getListMatchByDate("03-09-2015");
	    
	    for(FBMatch p : cartList) {
	        JSONObject formDetailsJson = new JSONObject();
	        formDetailsJson.put("id", p.id);
	        formDetailsJson.put("club_id_1", p.club_id_1);
	        formDetailsJson.put("club_id_2", p.club_id_2);
	        formDetailsJson.put("club_code_1", p.club_code_1);
	        formDetailsJson.put("club_code_2", p.club_code_2);
	        formDetailsJson.put("club_logo_1", p.club_logo_1);
	        formDetailsJson.put("club_logo_2", p.club_logo_2);
	        formDetailsJson.put("cup_id", p.cup_id);
	        formDetailsJson.put("cup", p.cup);
	        formDetailsJson.put("logo", p.logo);
	        formDetailsJson.put("result", p.result);
	        formDetailsJson.put("result_1", p.result_1);
	        formDetailsJson.put("round", p.round);
	        formDetailsJson.put("status", p.status);
	        
	        formDetailsJson.put("match_minute", p.match_minute);
	        formDetailsJson.put("rate", p.cup_rate);
	        formDetailsJson.put("match_rate", p.match_rate);
	        
	       jsonArray.add(formDetailsJson);
	    }
	    JSONObject formDetailsJson = new JSONObject();
	    formDetailsJson.put("v", formDetailsJson.values());
	    System.out.println(formDetailsJson.toString());
	    return jsonArray.toString();
	}
}
