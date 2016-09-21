package com.az24.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.az24.crawler.model.Feed;
import com.az24.db.pool.C3p010hPool;

public class FaceBookDAO {

	public static ArrayList<Feed> getAccount() {
		PreparedStatement ps,ps1,ps2;
		ArrayList<Feed> feeds = new ArrayList<Feed>();
		
		try {
			Connection connection = C3p010hPool.getConnection();
			ps = connection.prepareStatement("SELECT * FROM  vtc_adv_facebook.facebook_account ");
			ps1 = connection.prepareStatement("SELECT COUNT(*) as sl FROM facebook_user_invittable WHERE account_id = ?");
			ps2 = connection.prepareStatement("Update  facebook_account set count_friend  = ? WHERE id = ?");
			
			
			ResultSet rs =	ps.executeQuery();
			while(rs.next())
			{
				System.out.println(rs.getString("username"));
				ps1.setInt(1, rs.getInt("id"));
				ResultSet rs1 =	ps1.executeQuery();
				if(rs1.next()){
					System.out.println(rs1.getInt("sl"));
					ps2.setInt(1, rs1.getInt("sl"));
					ps2.setInt(2, rs.getInt("id"));
					ps2.execute();
				}
				
			}
			C3p010hPool.attemptClose(rs);
			C3p010hPool.attemptClose(ps);
			C3p010hPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return feeds;
	}
	
	
	public static void insertUserApp(int app_id) {
		PreparedStatement ps,ps2;
		
		
		try {
			Connection connection = C3p010hPool.getConnection();
			ps = connection.prepareStatement("SELECT * FROM  vtc_adv_facebook.facebook_account  where count_friend > 300 And status = 0");
		
			ps2 = connection.prepareStatement("INSERT INTO vtc_adv_facebook.facebook_app_account (app_id,account_id,STATUS)  VALUES (?,?,0);");
			
			
			ResultSet rs =	ps.executeQuery();
			while(rs.next())
			{
				System.out.println(rs.getString("username"));
					ps2.setInt(1, app_id);
					ps2.setInt(2, rs.getInt("id"));
					ps2.execute();
				
				
			}
			C3p010hPool.attemptClose(rs);
			C3p010hPool.attemptClose(ps);
			C3p010hPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		//FaceBookDAO.getAccount();
		FaceBookDAO.insertUserApp(2);
	}
	
}
