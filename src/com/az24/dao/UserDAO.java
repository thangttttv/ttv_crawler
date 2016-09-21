package com.az24.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.az24.crawler.store.User;
import com.az24.db.pool.C3p0AccountPool;
import com.az24.db.pool.C3p0ClassifiedPool;

public class UserDAO {
	public static Connection connection;
	
	public int saveUser(User user) {
		PreparedStatement ps;
		int id = 0;
		try {
			Connection connection = C3p0AccountPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection
						.prepareStatement("INSERT INTO account.hdc_user	(username,password,full_name,sex," +
								" province,country,	address,mobile, tel, email,	identify_card,	website, yahoo,	skype," +
								" active, active_code,	active_mobile, intro, salt,  pageReg, store_id,	store_alias," +
								" store_admin,create_date,	user_create,modify_date,user_modify" +
								"	) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? ) ");
				
				ps.setString(1, user.username);
				ps.setString(2, user.password);
				ps.setString(3, user.full_name);
				ps.setInt(4, user.sex);
				ps.setString(5,user.province);
				ps.setString(6,user.country);
				ps.setString(7, user.address);
				ps.setString(8,user.mobile);
				ps.setString(9, user.tel);
				ps.setString(10, user.email);
				ps.setString(11, user.identify_card);
				ps.setString(12, user.website);
				ps.setString(13, user.yahoo);
				ps.setString(14, user.skype);
				ps.setInt(15, user.active);
				ps.setString(16, user.active_code);
				ps.setInt(17, user.active_mobile);
				ps.setString(18, user.intro);
				
				ps.setString(19, user.salt);
				ps.setInt(20, user.pageReg);
				ps.setInt(21, user.store_id);
				ps.setString(22, user.store_alias);
				ps.setInt(23, user.store_admin);
				ps.setTimestamp(24, user.create_date);
				ps.setString(25, user.user_create);
				ps.setTimestamp(26, user.modify_date);
				ps.setString(27, user.user_modify);
			
				ps.execute();
				connection.commit();
				connection.setAutoCommit(true);
				String sql = "SELECT LAST_INSERT_ID()";
				PreparedStatement statement = connection.prepareStatement(sql);
				ResultSet resultSet = statement.executeQuery();
				if (resultSet.next())
					id = resultSet.getInt(1);
				C3p0ClassifiedPool.attemptClose(connection);
				C3p0ClassifiedPool.attemptClose(ps);
				C3p0ClassifiedPool.attemptClose(resultSet);
				
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	public void saveUserSysn(User user) {
		PreparedStatement ps;
		try {
			int i = 0;
			Connection connection = C3p0AccountPool.getConnection();
			connection.setAutoCommit(false);
			
			ps = connection
			.prepareStatement("INSERT INTO account.hdc_user_syn	(username,password,full_name,sex," +
					" province,country,	address,mobile, tel, email,	identify_card,	website, yahoo,	skype," +
					" active, active_code,	active_mobile, intro, salt,  pageReg, store_id,	store_alias," +
					" store_admin,create_date,	user_create,modify_date,user_modify,id" +
					"	) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? ) ");
			
				ps.setString(1, user.username);
				ps.setString(2, user.password);
				ps.setString(3, user.full_name);
				ps.setInt(4, user.sex);
				ps.setString(5,user.province);
				ps.setString(6,user.country);
				ps.setString(7, user.address);
				ps.setString(8,user.mobile);
				ps.setString(9, user.tel);
				ps.setString(10, user.email);
				ps.setString(11, user.identify_card);
				ps.setString(12, user.website);
				ps.setString(13, user.yahoo);
				ps.setString(14, user.skype);
				ps.setInt(15, user.active);
				ps.setString(16, user.active_code);
				ps.setInt(17, user.active_mobile);
				ps.setString(18, user.intro);
				
				ps.setString(19, user.salt);
				ps.setInt(20, user.pageReg);
				ps.setInt(21, user.store_id);
				ps.setString(22, user.store_alias);
				ps.setInt(23, user.store_admin);
				ps.setTimestamp(24, user.create_date);
				ps.setString(25, user.user_create);
				ps.setTimestamp(26, user.modify_date);
				ps.setString(27, user.user_modify);
				ps.setInt(28, user.id);
			
				ps.execute();
				if(i%100==0)
					connection.commit();
				i++;
			
			connection.commit();
			connection.setAutoCommit(true);
			C3p0ClassifiedPool.attemptClose(connection);
			C3p0ClassifiedPool.attemptClose(ps);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	
	public void updateUser(User user) {
		PreparedStatement ps;		
		try {
			Connection connection = C3p0AccountPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement("UPDATE  account.hdc_user SET  store_id = ?,	store_alias = ? , store_admin = 1  " +
								" WHERE id = ? ");
			ps.setInt(1, user.store_id);
			ps.setString(2, user.store_alias);
			ps.setInt(3, user.id);
			ps.execute();	
			
			ps = connection.prepareStatement("UPDATE  account.hdc_user_modified_syn SET  store_id = ?,	store_alias = ? , store_admin = 1  " +
			" WHERE id = ? ");
			ps.setInt(1, user.store_id);
			ps.setString(2, user.store_alias);
			ps.setInt(3, user.id);
			ps.execute();	

			connection.commit();
			connection.setAutoCommit(true);
			C3p0ClassifiedPool.attemptClose(connection);
			C3p0ClassifiedPool.attemptClose(ps);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
