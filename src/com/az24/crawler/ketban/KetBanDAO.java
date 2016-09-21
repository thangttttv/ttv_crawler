package com.az24.crawler.ketban;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.az24.db.pool.C3p0KetbanPool;

public class KetBanDAO {
	
	public int saveUser(User user) {
		PreparedStatement ps;
		int id = 0;
		try {
			Connection connection = C3p0KetbanPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement("INSERT INTO ketban.kb_user (fullname,username," +
					" avatar,password,birthday,	sex,height,weight,religious,education,job,income," +
					" married,email,nick_yahoo,nick_sky,province,address,mobile,preference,hate,desire,create_date)" +
					" VALUES ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				
				ps.setString(1, user.fullname);
				ps.setString(2, user.username);
				ps.setString(3,user.avatar);
				ps.setString(4, user.password);
				ps.setDate(5, user.birthday);
				ps.setInt(6, user.sex);
				ps.setString(7, user.height);
				ps.setString(8, user.weight);
				ps.setString(9, user.religious);
				ps.setString(10, user.education);
				ps.setString(11, user.job);
				
				ps.setString(12, user.income);
				ps.setString(13, user.married);
				ps.setString(14, user.email);
				ps.setString(15, user.nick_yahoo);
				
				ps.setString(16, user.nick_sky);
				ps.setString(17, user.province);
				ps.setString(18, user.address);
				ps.setString(19, user.mobile);
				ps.setString(20, user.preference);				
				ps.setString(21, user.hate);
				ps.setString(22, user.desire);
				ps.setDate(23, user.create_date);
				ps.execute();
				connection.commit();
				connection.setAutoCommit(true);
				String sql = "SELECT LAST_INSERT_ID()";
				PreparedStatement statement = connection.prepareStatement(sql);
				ResultSet resultSet = statement.executeQuery();
				if (resultSet.next())
					id = resultSet.getInt(1);
				connection.close();
				C3p0KetbanPool.attemptClose(ps);
		} catch (SQLException e) {
			//e.printStackTrace();
		}
		return id;
	}
	
	
	public int saveAlbum(Album album) {
		PreparedStatement ps;
		int id = 0;
		try {
			Connection connection = C3p0KetbanPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement("INSERT INTO ketban.kb_album" +
					" 	(user_id,image_name,create_date ) VALUES" +
					"   (?,?,?)");
				ps.setInt(1, album.user_id);
				ps.setString(2, album.image_name);
				ps.setDate(3,album.create_date);
				ps.execute();
				connection.commit();
				connection.setAutoCommit(true);
				connection.close();
				C3p0KetbanPool.attemptClose(ps);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
}
