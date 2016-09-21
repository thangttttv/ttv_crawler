package com.az24.crawler.model;

public class DatabaseConfig {
	public static String driver;
	public static String url;
	public static String user;
	public static String password;
	public static String connections;
	
	public static String driver_log;
	public static String url_log;
	public static String user_log;
	public static String password_log;
	public static String table_entity_log;
	public static String table_image_log;
	public static String connections_log;
	
	public static String getDriver() {
		return driver;
	}
	public static void setDriver(String driver) {
		DatabaseConfig.driver = driver;
	}
	public static String getUrl() {
		return url;
	}
	public static void setUrl(String url) {
		DatabaseConfig.url = url;
	}
	public static String getUser() {
		return user;
	}
	public static void setUser(String user) {
		DatabaseConfig.user = user;
	}
	public static String getPassword() {
		return password;
	}
	public static void setPassword(String password) {
		DatabaseConfig.password = password;
	}
	public static String getConnections() {
		return connections;
	}
	public static void setConnections(String connections) {
		DatabaseConfig.connections = connections;
	}
}
