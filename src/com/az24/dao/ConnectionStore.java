package com.az24.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionStore {
	private static Connection connection;
	
	public static Connection connection_store()
	{
		try {
				Class.forName("com.mysql.jdbc.Driver");				
				connection = DriverManager.getConnection(
						"jdbc:mysql://210.211.97.11:3306/az24_store?autoReconnect=true&characterEncoding=UTF-8","quangpn","QuangPN2011@!!!*^");
			} catch (ClassNotFoundException e) {			
				e.printStackTrace();
			} catch (SQLException e) {		
				e.printStackTrace();
			}      
	        
			return connection;
		
	}
}
