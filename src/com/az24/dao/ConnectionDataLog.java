package com.az24.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.az24.crawler.config.BeanXmlConfig;
import com.az24.crawler.model.DatabaseConfig;

public class ConnectionDataLog {
	public static Connection connection;
	
	public static Connection connection(String path)
	{
		BeanXmlConfig beanXmlConfig = new BeanXmlConfig(path);
		beanXmlConfig.parseConfig();
		try {
				Class.forName(DatabaseConfig.driver_log.trim());
				
				connection = DriverManager.getConnection(
						DatabaseConfig.url_log.trim(),
						DatabaseConfig.user_log,
						DatabaseConfig.password_log);
		
				
			} catch (ClassNotFoundException e) {			
				e.printStackTrace();
			} catch (SQLException e) {		
				e.printStackTrace();
			}      
	        
			return connection;
		
	}
	
	
	
}
