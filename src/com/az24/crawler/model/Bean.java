package com.az24.crawler.model;

import hdc.crawler.ExtractEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;


public class Bean {
	private String table;
	private List<Property> properties = null;
	private Connection conn = null;
	private ExtractEntity entity = null;
	
	public Bean(String table,List<Property> properties,ExtractEntity entity,Connection conn)
	{
		this.table = table;
		this.properties = properties;
		this.conn = conn;
		this.entity = entity;
	}
	
	public int save()
	{
		int id = 0;
		String sql = "INSERT INTO "+table;
		String fields=" (";
		String paras=" (";
		int i = 0;Property property = null;		
		while(i<properties.size())
		{
			property = properties.get(i);			
			fields +=i==properties.size()-1? property.getName(): property.getName()+",";
			paras  +=i==properties.size()-1?"?":"?,";
			i++;
		}
		fields=")";
		paras=")";
		try {
			conn.setAutoCommit(false);
			PreparedStatement statement = conn.prepareStatement(sql);
			i=1;Calendar calendar = null;String date="";
			while(i<=properties.size())
			{
				property = properties.get(i);			
				switch (property.getName().hashCode()) {
				case -1325958191: //double
					statement.setDouble(i,Double.parseDouble((String)entity.getProperty(property.getName())) );
					break;
				case 104431: //int
					statement.setInt(i,Integer.parseInt((String)entity.getProperty(property.getName())) );
					break;
				case -891985903: //String
					statement.setString(i,(String)entity.getProperty(property.getName())) ;
					break;
				case 3076014: //date
					calendar = Calendar.getInstance();
					date = (String)entity.getProperty(property.getName());
					calendar.set(Calendar.YEAR, Integer.parseInt(date.split("/")[2]));
					calendar.set(Calendar.MONTH, Integer.parseInt(date.split("/")[1]));
					calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date.split("/")[0]));					
					statement.setDate(i,new java.sql.Date(calendar.getTimeInMillis()) );
					break;
				case 1793702779: //datetime
					calendar = Calendar.getInstance();
					date = (String)entity.getProperty(property.getName());
					calendar.set(Calendar.YEAR, Integer.parseInt(date.split("/")[2]));
					calendar.set(Calendar.MONTH, Integer.parseInt(date.split("/")[1]));
					calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date.split("/")[0]));					
					statement.setTimestamp(i,new Timestamp(calendar.getTimeInMillis()) );
					break;
				default:
					break;
				}
				i++;
			}
			
			statement.execute();			
			conn.commit();
			sql = "SELECT LAST_INSERT_ID()";
			statement = conn.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			if(resultSet.next()) id = resultSet.getInt(1);
			conn.setAutoCommit(true);
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		
		return id;
	}
}
