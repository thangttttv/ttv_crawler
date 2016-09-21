package com.az24.crawler.model;

import java.sql.Timestamp;

public class WPPost {
	public int id; 
	public int post_author; 
	public Timestamp post_date; 
	public Timestamp post_date_gmt; 
	public String post_content; 
	public String post_title; 
	public String post_excerpt; // trich doan
	public String post_status; //publish ,auto-draft, inherit
	public String comment_status; // open trang thai mo comment
	public String ping_status; // open
	public String post_password; 
	public String post_name; 
	public String to_ping; 
	public String pinged; 
	public Timestamp post_modified; 
	public Timestamp post_modified_gmt; 
	public String post_content_filtered; 
	public int post_parent; 
	public String guid; 
	public int menu_order; 
	public String post_type; 
	public String post_mime_type; 
	public int comment_count;
}
