package com.az24.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.az24.crawler.model.NoticeQueue;
import com.az24.db.pool.C3p0GameStorePool;

public class GameStoreDAO {
	public  ArrayList<NoticeQueue> getListNotify() {
		PreparedStatement ps;
		NoticeQueue noticeQueue = null;
		ArrayList<NoticeQueue> listMatch = new ArrayList<NoticeQueue>();
		try {
			Connection connection = C3p0GameStorePool.getConnection();
			ps = connection.prepareStatement("SELECT id,notice_id,device_token, payload, time_queued, "
					+ "time_sent,os_type, channel FROM vtc_game_store.g_notice_queue LIMIT 0, 100");
			ResultSet rs =	ps.executeQuery();
			while(rs.next())
			{
				noticeQueue = new NoticeQueue();
				noticeQueue.id = rs.getInt(1);
				noticeQueue.notice_id = rs.getInt("notice_id");
				noticeQueue.channel = rs.getInt("channel");
				noticeQueue.os_type = rs.getInt("os_type");
				noticeQueue.time_sent = rs.getString("time_sent");
				noticeQueue.time_queued = rs.getString("time_queued");
				noticeQueue.payload = rs.getString("payload");
				noticeQueue.device_token = rs.getString("device_token");
				
				listMatch.add(noticeQueue);
				
			}
			C3p0GameStorePool.attemptClose(rs);
			C3p0GameStorePool.attemptClose(ps);
			C3p0GameStorePool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listMatch;
	}
	
	public  int deleteNotice (int notice_id) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0GameStorePool.getConnection(); 
			conn.setAutoCommit(false);
			
			ps = conn.prepareStatement("DELETE FROM vtc_game_store.g_notice_queue  WHERE id = ? ");
			
			ps.setInt(1, notice_id);
			
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
			C3p0GameStorePool.attemptClose(ps);
			C3p0GameStorePool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	public  int deleteDeviceToken (String token) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0GameStorePool.getConnection(); 
			conn.setAutoCommit(false);
			
			ps = conn.prepareStatement("DELETE FROM vtc_game_store.g_notice_user  WHERE device_token = ? ");
			
			ps.setString(1, token);
			
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
			C3p0GameStorePool.attemptClose(ps);
			C3p0GameStorePool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	public  int updateDeviceTokenChannel (String token, int channel) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0GameStorePool.getConnection(); 
			conn.setAutoCommit(false);
			
			ps = conn.prepareStatement("Update vtc_game_store.g_notice_user set channel = ? WHERE device_token = ? ");
			
			ps.setInt(1, channel);
			ps.setString(2, token);
			
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
			C3p0GameStorePool.attemptClose(ps);
			C3p0GameStorePool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
}
