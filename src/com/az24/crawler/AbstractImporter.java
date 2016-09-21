package com.az24.crawler;

import hdc.crawler.ExtractEntity;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import jdbm.PrimaryHashMap;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

import com.az24.crawler.config.JdbmXmlConfig;
import com.az24.crawler.model.DatabaseConfig;
import com.az24.dao.ConnectionDataLog;

public abstract class AbstractImporter implements Runnable {
	public Connection conn;
	public Connection connLog;
	public String filebeanconfig="";
	public String filejdbmconfig="";
	protected RecordManager extracterManager;
	protected PrimaryHashMap<String, ExtractEntity> extractPrimary;
	
	
		
	public AbstractImporter(String filebeanconfig,String filejdbmconfig )
	{
		this.filebeanconfig = filebeanconfig;
		this.filejdbmconfig = filejdbmconfig;
	}
	
	public void openConnection()
	{
		try {
			Class.forName(DatabaseConfig.driver.trim());
			System.out.println(DatabaseConfig.url.trim());
			conn = DriverManager.getConnection(
					DatabaseConfig.url.trim(),
					DatabaseConfig.user,
					DatabaseConfig.password);
			
			connLog = ConnectionDataLog.connection(filebeanconfig);
			
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		} catch (SQLException e) {		
			e.printStackTrace();
		}      
        	
	}
	
	
	
	public void closeConnection()
	{		
			try {
				if(conn!=null)
				conn.close();
				if(connLog!=null)
					connLog.close();
			} catch (SQLException e) {			
				e.printStackTrace();
			}
	}
	
	
	protected void openData() throws IOException {
		JdbmXmlConfig.parseConfig(filejdbmconfig);
	
		extracterManager = RecordManagerFactory.createRecordManager(JdbmXmlConfig.extract
				+ "/extract");
		extractPrimary = extracterManager.hashMap("extract");

		
	}

	protected void commitData() throws IOException {		
		extracterManager.commit();
		
	}

	protected void closeData() throws IOException {
		extracterManager.close();		
	}
	
	protected void processAfterSave(int id,int cat_id)
	{
		
	}
	
	public void run() {
		
	}
	
	
}
