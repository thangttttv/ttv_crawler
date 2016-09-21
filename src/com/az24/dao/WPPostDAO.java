package com.az24.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.az24.crawler.model.WPPost;
import com.az24.db.pool.C3p0XDTPool;

public class WPPostDAO {
	
	public int savePost(WPPost wPost) {
		int id = 0;
		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0XDTPool.getConnection(); 
			conn.setAutoCommit(false);
			ps = conn
					.prepareStatement("INSERT INTO wp_posts (post_author,post_date,post_date_gmt,post_content,post_title,post_excerpt,post_status," +
							" comment_status,ping_status,post_password,post_name,to_ping,pinged,post_modified,post_modified_gmt,post_content_filtered," +
							" post_parent,guid,menu_order,post_type,post_mime_type,comment_count) VALUES" +
							" (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
			ps.setInt(1, wPost.post_author);
			ps.setTimestamp(2, wPost.post_date);
			ps.setTimestamp(3, wPost.post_date_gmt);
			System.out.println(wPost.post_content);
			ps.setString(4, wPost.post_content);
			ps.setString(5, wPost.post_title);
			ps.setString(6, wPost.post_excerpt);
			ps.setString(7, wPost.post_status);
			ps.setString(8, wPost.comment_status);		
			ps.setString(9,wPost.ping_status);
			ps.setString(10, wPost.post_password);			
			ps.setString(11, wPost.post_name );
			ps.setString(12,wPost.to_ping);
			ps.setString(13, wPost.pinged);
			ps.setTimestamp(14,wPost.post_modified);
			ps.setTimestamp(15,wPost.post_modified_gmt);
			ps.setString(16,wPost.post_content_filtered);
			ps.setInt(17, wPost.post_parent);
			ps.setString(18, wPost.guid);
			ps.setInt(19, wPost.menu_order);
			ps.setString(20,wPost.post_type);
			ps.setString(21,wPost.post_mime_type);
			ps.setInt(22, wPost.comment_count);
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
			String sql = "SELECT LAST_INSERT_ID()";
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				id = resultSet.getInt(1);

			C3p0XDTPool.attemptClose(conn);
			C3p0XDTPool.attemptClose(resultSet);
			C3p0XDTPool.attemptClose(ps);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}	
	
	
	public void saveTermRelationships (int object_id,int term_id) {
		PreparedStatement ps=null;	Connection conn = null;	
		try {
			conn = C3p0XDTPool.getConnection(); 
			conn.setAutoCommit(false);
			ps = conn.prepareStatement("INSERT INTO wp_term_relationships (object_id,term_taxonomy_id,term_order" +
							" 	)VALUES(?,?,?) ");
			ps.setInt(1, object_id);
			ps.setInt(2, term_id);
			ps.setInt(3,0);
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
		} catch (SQLException e) {
			//e.printStackTrace();
		}finally{
			C3p0XDTPool.attemptClose(ps);
			C3p0XDTPool.attemptClose(conn);
			
		}
	}	
	
	
	public int savePostMeta(int object_id,String meta_key,String meta_value) {
		PreparedStatement ps=null;	int id = 0;	
		try {
			Connection conn = C3p0XDTPool.getConnection(); 
			conn.setAutoCommit(false);
			ps = conn.prepareStatement("INSERT INTO wp_postmeta (post_id,meta_key,meta_value)" +
					" VALUES 	(?,	?,?); ");
			ps.setInt(1, object_id);
			ps.setString(2, meta_key);
			ps.setString(3,meta_value);
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
			String sql = "SELECT LAST_INSERT_ID()";
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				id = resultSet.getInt(1);
			C3p0XDTPool.attemptClose(conn);
			C3p0XDTPool.attemptClose(ps);
			C3p0XDTPool.attemptClose(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return id;
	}	
	
	
	public int save_wp_terms(String name,String slug,int term_group) {
		PreparedStatement ps=null;	int id = 0;	
		try {
			Connection conn = C3p0XDTPool.getConnection(); 
			conn.setAutoCommit(false);
			ps = conn.prepareStatement("INSERT INTO wp_terms (NAME, slug, term_group)" +
					" VALUES (?,?,?);");
			ps.setString(1, name);
			ps.setString(2, slug);
			ps.setInt(3,term_group);
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
			String sql = "SELECT LAST_INSERT_ID()";
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				id = resultSet.getInt(1);
			C3p0XDTPool.attemptClose(conn);
			C3p0XDTPool.attemptClose(ps);
			C3p0XDTPool.attemptClose(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return id;
	}
	
	
	public int save_wp_term_taxonomy(int term_id,String taxonomy,String description,int parent,int count ) {
		PreparedStatement ps=null;	int id = 0;	
		try {
			Connection conn = C3p0XDTPool.getConnection(); 
			conn.setAutoCommit(false);
			ps = conn.prepareStatement("INSERT INTO wp_term_taxonomy ( term_id, taxonomy, description, " +
					" parent,	COUNT ) VALUES (?,?,?,?,?);");
			ps.setInt(1, term_id);
			ps.setString(2, taxonomy);
			ps.setString(3,description);
			ps.setInt(4, parent);
			ps.setInt(5, count);
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
			String sql = "SELECT LAST_INSERT_ID()";
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				id = resultSet.getInt(1);
			C3p0XDTPool.attemptClose(conn);
			C3p0XDTPool.attemptClose(ps);
			C3p0XDTPool.attemptClose(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return id;
	}
	
	
	public int save_wp_postmeta(int post_id,String meta_key,String meta_value) {
		PreparedStatement ps=null;	int id = 0;	
		try {
			Connection conn = C3p0XDTPool.getConnection(); 
			conn.setAutoCommit(false);
			ps = conn.prepareStatement("INSERT INTO wp_postmeta	(post_id,meta_key,meta_value)" +
					"  VALUES (?,?,?)");
			ps.setInt(1, post_id);
			ps.setString(2, meta_key);
			ps.setString(3,meta_value);
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
			String sql = "SELECT LAST_INSERT_ID()";
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				id = resultSet.getInt(1);
			C3p0XDTPool.attemptClose(conn);
			C3p0XDTPool.attemptClose(ps);
			C3p0XDTPool.attemptClose(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return id;
	}
	
	public String getCategory(int id) {
		PreparedStatement ps=null;		
		String name = "";
		try {
			Connection conn = C3p0XDTPool.getConnection(); 
			ps = conn.prepareStatement("SELECT term_id,	name,slug,term_group FROM wp_terms WHERE term_id = ? ");
			ps.setInt(1, id);
			ResultSet rs=  ps.executeQuery();
			if (rs.next()){
				name = rs.getString("name");
				
			}
			C3p0XDTPool.attemptClose(rs);
			C3p0XDTPool.attemptClose(ps);
			C3p0XDTPool.attemptClose(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return name;
	}
	
	public int getIDTerm(String name) {
		PreparedStatement ps=null;		
		int id = 0;
		try {
			Connection conn = C3p0XDTPool.getConnection(); 
			ps = conn.prepareStatement("SELECT term_id,	name,slug,term_group FROM wp_terms WHERE name = ? ");
			ps.setString(1, name);
			ResultSet rs=  ps.executeQuery();
			if (rs.next()){
				id = rs.getInt("term_id");
				
			}
			C3p0XDTPool.attemptClose(rs);
			C3p0XDTPool.attemptClose(ps);
			C3p0XDTPool.attemptClose(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return id;
	}
	
	public int getIDTermtaxonomy(int term_id) {
		PreparedStatement ps=null;		
		int id = 0;
		try {
			Connection conn = C3p0XDTPool.getConnection(); 
			ps = conn.prepareStatement("SELECT term_taxonomy_id FROM wp_term_taxonomy WHERE term_id = ? ");
			ps.setInt(1, term_id);
			ResultSet rs=  ps.executeQuery();
			if (rs.next()){
				id = rs.getInt("term_taxonomy_id");
				
			}
			C3p0XDTPool.attemptClose(rs);
			C3p0XDTPool.attemptClose(ps);
			C3p0XDTPool.attemptClose(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return id;
	}
}
