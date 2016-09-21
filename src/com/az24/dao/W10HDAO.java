package com.az24.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import com.az24.crawler.model.Feed;
import com.az24.crawler.model.KQDienToan123;
import com.az24.crawler.model.KQDienToan6x36;
import com.az24.crawler.model.KQMB;
import com.az24.crawler.model.KQMN;
import com.az24.crawler.model.KQThanTai;
import com.az24.db.pool.C3p010hPool;

public class W10HDAO {

	public int saveKqMienBac(KQMB kqmb) {
		PreparedStatement ps;
		int id = 0;
		try {
			Connection connection = C3p010hPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement("INSERT INTO ketqua_mienbac (province_id,ngay_quay,giai_dacbiet,giai_nhat,giai_nhi_1," +
					"giai_nhi_2,giai_ba_1,giai_ba_2,giai_ba_3,giai_ba_4,giai_ba_5,giai_ba_6,giai_tu_1,giai_tu_2,giai_tu_3,giai_tu_4,giai_nam_1," +
					"giai_nam_2,giai_nam_3,giai_nam_4,giai_nam_5,giai_nam_6,giai_sau_1,giai_sau_2,giai_sau_3,giai_bay_1,giai_bay_2,giai_bay_3," +
					"giai_bay_4,create_user,create_date,modify_user,modify_date) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?" +
					",?,?,?,?,?,?)");
			
			ps.setInt(1,1);
			ps.setString(2,kqmb.ngay_quay);
			ps.setString(3,kqmb.giai_dacbiet.trim());
			ps.setString(4,kqmb.giai_nhat.trim());
			ps.setString(5,kqmb.giai_nhi_1.trim());
			ps.setString(6,kqmb.giai_nhi_2.trim());
			
			ps.setString(7,kqmb.giai_ba_1.trim());
			ps.setString(8,kqmb.giai_ba_2.trim());
			ps.setString(9,kqmb.giai_ba_3.trim());
			ps.setString(10,kqmb.giai_ba_4.trim());
			ps.setString(11,kqmb.giai_ba_5.trim());
			ps.setString(12,kqmb.giai_ba_6.trim());
			
			ps.setString(13,kqmb.giai_tu_1.trim());
			ps.setString(14,kqmb.giai_tu_2.trim());
			ps.setString(15,kqmb.giai_tu_3.trim());
			ps.setString(16,kqmb.giai_tu_4.trim());
			
			ps.setString(17,kqmb.giai_nam_1.trim());
			ps.setString(18,kqmb.giai_nam_2.trim());
			ps.setString(19,kqmb.giai_nam_3.trim());
			ps.setString(20,kqmb.giai_nam_4.trim());
			ps.setString(21,kqmb.giai_nam_5.trim());
			ps.setString(22,kqmb.giai_nam_6.trim());
			
			ps.setString(23,kqmb.giai_sau_1.trim());
			ps.setString(24,kqmb.giai_sau_2.trim());
			ps.setString(25,kqmb.giai_sau_3.trim());
			
			
			ps.setString(26,kqmb.giai_bay_1.trim());
			ps.setString(27,kqmb.giai_bay_2.trim());
			ps.setString(28,kqmb.giai_bay_3.trim());
			ps.setString(29,kqmb.giai_bay_4.trim());
			
			ps.setString(30,kqmb.create_user);
			ps.setString(31,kqmb.create_date);
			ps.setString(32,kqmb.modify_user);
			ps.setString(33,kqmb.modify_date);
			
			ps.execute();
			connection.commit();
			connection.setAutoCommit(true);		
			ps = connection.prepareStatement("SELECT LAST_INSERT_ID()");
			ResultSet rs = ps.executeQuery();
			if(rs.next()) id = rs.getInt(1);
			C3p010hPool.attemptClose(ps);
			C3p010hPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	
	public int saveKqMienNam(KQMN kqmn) {
		PreparedStatement ps;
		int id = 0;
		try {
			Connection connection = C3p010hPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement("INSERT INTO ketqua_miennam (province_id,ngay_quay,giai_dacbiet,giai_nhat,giai_nhi," +
					"giai_ba_1,giai_ba_2,giai_tu_1,giai_tu_2,giai_tu_3,giai_tu_4,giai_tu_5,giai_tu_6,giai_tu_7,giai_nam,giai_sau_1,giai_sau_2," +
					"giai_sau_3, giai_bay, giai_tam, create_user, create_date, modify_user, modify_date ) VALUES " +
					" (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
						
			ps.setInt(1,kqmn.province_id);
			ps.setString(2,kqmn.ngay_quay);
			ps.setString(3,kqmn.giai_dacbiet.trim());
			ps.setString(4,kqmn.giai_nhat.trim());
			ps.setString(5,kqmn.giai_nhi.trim());
			
			
			ps.setString(6,kqmn.giai_ba_1.trim());
			ps.setString(7,kqmn.giai_ba_2.trim());
			
			ps.setString(8,kqmn.giai_tu_1.trim());
			ps.setString(9,kqmn.giai_tu_2.trim());
			ps.setString(10,kqmn.giai_tu_3.trim());
			ps.setString(11,kqmn.giai_tu_4.trim());
			ps.setString(12,kqmn.giai_tu_5.trim());
			ps.setString(13,kqmn.giai_tu_6.trim());
			ps.setString(14,kqmn.giai_tu_7.trim());
			
			ps.setString(15,kqmn.giai_nam.trim());
			
			ps.setString(16,kqmn.giai_sau_1.trim());
			ps.setString(17,kqmn.giai_sau_2.trim());
			ps.setString(18,kqmn.giai_sau_3.trim());
			
			
			ps.setString(19,kqmn.giai_bay.trim());
			ps.setString(20,kqmn.giai_tam.trim());
			
			ps.setString(21,kqmn.create_user);
			ps.setString(22,kqmn.create_date);
			ps.setString(23,kqmn.modify_user);
			ps.setString(24,kqmn.modify_date);
			
			ps.execute();
			connection.commit();
			connection.setAutoCommit(true);		
			ps = connection.prepareStatement("SELECT LAST_INSERT_ID()");
			ResultSet rs = ps.executeQuery();
			if(rs.next()) id = rs.getInt(1);
			C3p010hPool.attemptClose(ps);
			C3p010hPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	public int saveKqMienTrung(KQMN kqmn) {
		PreparedStatement ps;
		int id = 0;
		try {
			Connection connection = C3p010hPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement("INSERT INTO ketqua_mientrung (province_id,ngay_quay,giai_dacbiet,giai_nhat,giai_nhi," +
					"giai_ba_1,giai_ba_2,giai_tu_1,giai_tu_2,giai_tu_3,giai_tu_4,giai_tu_5,giai_tu_6,giai_tu_7,giai_nam,giai_sau_1,giai_sau_2," +
					"giai_sau_3, giai_bay, giai_tam, create_user, create_date, modify_user, modify_date ) VALUES " +
					" (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
						
			ps.setInt(1,kqmn.province_id);
			ps.setString(2,kqmn.ngay_quay);
			ps.setString(3,kqmn.giai_dacbiet.trim());
			ps.setString(4,kqmn.giai_nhat.trim());
			ps.setString(5,kqmn.giai_nhi.trim());
			
			
			ps.setString(6,kqmn.giai_ba_1.trim());
			ps.setString(7,kqmn.giai_ba_2.trim());
			
			ps.setString(8,kqmn.giai_tu_1.trim());
			ps.setString(9,kqmn.giai_tu_2.trim());
			ps.setString(10,kqmn.giai_tu_3.trim());
			ps.setString(11,kqmn.giai_tu_4.trim());
			ps.setString(12,kqmn.giai_tu_5.trim());
			ps.setString(13,kqmn.giai_tu_6.trim());
			ps.setString(14,kqmn.giai_tu_7.trim());
			
			ps.setString(15,kqmn.giai_nam.trim());
			
			ps.setString(16,kqmn.giai_sau_1.trim());
			ps.setString(17,kqmn.giai_sau_2.trim());
			ps.setString(18,kqmn.giai_sau_3.trim());
			
			
			ps.setString(19,kqmn.giai_bay.trim());
			ps.setString(20,kqmn.giai_tam.trim());
			
			ps.setString(21,kqmn.create_user);
			ps.setString(22,kqmn.create_date);
			ps.setString(23,kqmn.modify_user);
			ps.setString(24,kqmn.modify_date);
			
			ps.execute();
			connection.commit();
			connection.setAutoCommit(true);		
			ps = connection.prepareStatement("SELECT LAST_INSERT_ID()");
			ResultSet rs = ps.executeQuery();
			if(rs.next()) id = rs.getInt(1);
			C3p010hPool.attemptClose(ps);
			C3p010hPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	
	public int update(KQMB kqmb,String ngay_quay) {
		PreparedStatement ps;
		int id = 0;
		try {
			Connection connection = C3p010hPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement("UPDATE ketqua_mienbac SET giai_dacbiet = ? , giai_nhat = ? ,giai_nhi_1 = ? , " +
					" giai_nhi_2 = ? ,giai_ba_1 = ? ,giai_ba_2 = ? ,giai_ba_3 = ? ,giai_ba_4 = ?,giai_ba_5 = ? , giai_ba_6 = ? , giai_tu_1 = ? , " +
					" giai_tu_2 = ? , giai_tu_3 = ? , giai_tu_4 = ? ,	giai_nam_1 = ? , giai_nam_2 = ? ,giai_nam_3 = ? , giai_nam_4 = ? ," +
					" giai_nam_5 = ? , giai_nam_6 = ? , giai_sau_1 = ? , giai_sau_2 = ? , giai_sau_3 = ? , giai_bay_1 = ? , giai_bay_2 = ?," +
					" giai_bay_3 = ? , giai_bay_4 = ? , modify_user = ? , modify_date = ? WHERE ngay_quay = ? ");

			ps.setString(1,kqmb.giai_dacbiet);
			ps.setString(2,kqmb.giai_nhat.trim());
			ps.setString(3,kqmb.giai_nhi_1.trim());
			ps.setString(4,kqmb.giai_nhi_2.trim());
			
			ps.setString(5,kqmb.giai_ba_1.trim());
			ps.setString(6,kqmb.giai_ba_2.trim());
			ps.setString(7,kqmb.giai_ba_3.trim());
			ps.setString(8,kqmb.giai_ba_4.trim());
			ps.setString(9,kqmb.giai_ba_5.trim());
			ps.setString(10,kqmb.giai_ba_6.trim());
			
			ps.setString(11,kqmb.giai_tu_1.trim());
			ps.setString(12,kqmb.giai_tu_2.trim());
			ps.setString(13,kqmb.giai_tu_3.trim());
			ps.setString(14,kqmb.giai_tu_4.trim());
			
			ps.setString(15,kqmb.giai_nam_1.trim());
			ps.setString(16,kqmb.giai_nam_2.trim());
			ps.setString(17,kqmb.giai_nam_3.trim());
			ps.setString(18,kqmb.giai_nam_4.trim());
			ps.setString(19,kqmb.giai_nam_5.trim());
			ps.setString(20,kqmb.giai_nam_6.trim());
			
			ps.setString(21,kqmb.giai_sau_1.trim());
			ps.setString(22,kqmb.giai_sau_2.trim());
			ps.setString(23,kqmb.giai_sau_3.trim());
			
			
			ps.setString(24,kqmb.giai_bay_1.trim());
			ps.setString(25,kqmb.giai_bay_2.trim());
			ps.setString(26,kqmb.giai_bay_3.trim());
			ps.setString(27,kqmb.giai_bay_4.trim());
			
			ps.setString(28,kqmb.modify_user);
			ps.setString(29,kqmb.modify_date);
			ps.setString(30,kqmb.ngay_quay);
			
			ps.executeUpdate();
			connection.commit();
			connection.setAutoCommit(true);		
			
			C3p010hPool.attemptClose(ps);
			C3p010hPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	public int updateKqMienNam(KQMN kqmn) {
		PreparedStatement ps;
		int id = 0;
		try {
			Connection connection = C3p010hPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement("UPDATE ketqua_miennam SET  giai_dacbiet = ? , giai_nhat = ? , giai_nhi = ? ," +
					" giai_ba_1 = ? , giai_ba_2 = ? , giai_tu_1 = ? , giai_tu_2 = ? , giai_tu_3 = ? , giai_tu_4 = ? , giai_tu_5 = ? , " +
					" giai_tu_6 = ? , giai_tu_7 = ? , giai_nam = ? , giai_sau_1 = ? , giai_sau_2 = ? , giai_sau_3 = ? , giai_bay = ? , giai_tam = ? ," +
					" modify_user = ? , modify_date = ? WHERE  province_id = ? AND ngay_quay = ?  ");
						
			
			ps.setString(1,kqmn.giai_dacbiet.trim());
			ps.setString(2,kqmn.giai_nhat.trim());
			ps.setString(3,kqmn.giai_nhi.trim());

			ps.setString(4,kqmn.giai_ba_1.trim());
			ps.setString(5,kqmn.giai_ba_2.trim());
			
			ps.setString(6,kqmn.giai_tu_1.trim());
			ps.setString(7,kqmn.giai_tu_2.trim());
			ps.setString(8,kqmn.giai_tu_3.trim());
			ps.setString(9,kqmn.giai_tu_4.trim());
			ps.setString(10,kqmn.giai_tu_5.trim());
			ps.setString(11,kqmn.giai_tu_6.trim());
			ps.setString(12,kqmn.giai_tu_7.trim());
			
			ps.setString(13,kqmn.giai_nam.trim());
			
			ps.setString(14,kqmn.giai_sau_1.trim());
			ps.setString(15,kqmn.giai_sau_2.trim());
			ps.setString(16,kqmn.giai_sau_3.trim());
			
			
			ps.setString(17,kqmn.giai_bay.trim());
			ps.setString(18,kqmn.giai_tam.trim());
			
			
			ps.setString(19,kqmn.modify_user);
			ps.setString(20,kqmn.modify_date);
			
			ps.setInt(21,kqmn.province_id);
			ps.setString(22,kqmn.ngay_quay);
			
			ps.execute();
			connection.commit();
			connection.setAutoCommit(true);		
			C3p010hPool.attemptClose(ps);
			C3p010hPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	public int updateKqMienTrung(KQMN kqmn) {
		PreparedStatement ps;
		int id = 0;
		try {
			Connection connection = C3p010hPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement("UPDATE ketqua_mientrung SET  giai_dacbiet = ? , giai_nhat = ? , giai_nhi = ? ," +
					" giai_ba_1 = ? , giai_ba_2 = ? , giai_tu_1 = ? , giai_tu_2 = ? , giai_tu_3 = ? , giai_tu_4 = ? , giai_tu_5 = ? , " +
					" giai_tu_6 = ? , giai_tu_7 = ? , giai_nam = ? , giai_sau_1 = ? , giai_sau_2 = ? , giai_sau_3 = ? , giai_bay = ? , giai_tam = ? ," +
					" modify_user = ? , modify_date = ? WHERE  province_id = ? AND ngay_quay = ?  ");
						
			
			ps.setString(1,kqmn.giai_dacbiet.trim());
			ps.setString(2,kqmn.giai_nhat.trim());
			ps.setString(3,kqmn.giai_nhi.trim());

			ps.setString(4,kqmn.giai_ba_1.trim());
			ps.setString(5,kqmn.giai_ba_2.trim());
			
			ps.setString(6,kqmn.giai_tu_1.trim());
			ps.setString(7,kqmn.giai_tu_2.trim());
			ps.setString(8,kqmn.giai_tu_3.trim());
			ps.setString(9,kqmn.giai_tu_4.trim());
			ps.setString(10,kqmn.giai_tu_5.trim());
			ps.setString(11,kqmn.giai_tu_6.trim());
			ps.setString(12,kqmn.giai_tu_7.trim());
			
			ps.setString(13,kqmn.giai_nam.trim());
			
			ps.setString(14,kqmn.giai_sau_1.trim());
			ps.setString(15,kqmn.giai_sau_2.trim());
			ps.setString(16,kqmn.giai_sau_3.trim());
			
			
			ps.setString(17,kqmn.giai_bay.trim());
			ps.setString(18,kqmn.giai_tam.trim());
			
			
			ps.setString(19,kqmn.modify_user);
			ps.setString(20,kqmn.modify_date);
			
			ps.setInt(21,kqmn.province_id);
			ps.setString(22,kqmn.ngay_quay);
			
			ps.execute();
			connection.commit();
			connection.setAutoCommit(true);		
			C3p010hPool.attemptClose(ps);
			C3p010hPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	public int checkKqMB(String ngay_quay) {
		PreparedStatement ps;
		int kq = 0; // 0 chua co, 1 dang tuong thuat, 2 ket thuc
		try {
			Connection connection = C3p010hPool.getConnection();
			ps = connection.prepareStatement("SELECT giai_dacbiet FROM ketqua_mienbac   " +	" WHERE ngay_quay = ? ");
			ps.setString(1, ngay_quay);
			ResultSet resultSet =	ps.executeQuery();
			if(resultSet.next())
			{
				String giai_dacbiet = resultSet.getString(1);
				if(!giai_dacbiet.isEmpty()&&!"".equalsIgnoreCase(giai_dacbiet.trim())) kq = 2; else kq = 1;
					
			}
			C3p010hPool.attemptClose(resultSet);
			C3p010hPool.attemptClose(ps);
			C3p010hPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return kq;
	}
	
	public int checkKqMN(String ngay_quay,int province_id) {
		PreparedStatement ps;
		int kq = 0; // 0 chua co, 1 dang tuong thuat, 2 ket thuc
		try {
			Connection connection = C3p010hPool.getConnection();
			ps = connection.prepareStatement("SELECT giai_dacbiet FROM ketqua_miennam  " +	" WHERE ngay_quay = ? And province_id = ? ");
			ps.setString(1, ngay_quay);
			ps.setInt(2, province_id);
			
			ResultSet resultSet =	ps.executeQuery();
			if(resultSet.next())
			{
				String giai_dacbiet = resultSet.getString(1);
				if(!giai_dacbiet.isEmpty()&&!"".equalsIgnoreCase(giai_dacbiet.trim())) kq = 2; else kq = 1;
					
			}
			C3p010hPool.attemptClose(resultSet);
			C3p010hPool.attemptClose(ps);
			C3p010hPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return kq;
	}
	
	public int checkKqMT(String ngay_quay,int province_id) {
		PreparedStatement ps;
		int kq = 0; // 0 chua co, 1 dang tuong thuat, 2 ket thuc
		try {
			Connection connection = C3p010hPool.getConnection();
			ps = connection.prepareStatement("SELECT giai_dacbiet FROM ketqua_mientrung  " +	" WHERE ngay_quay = ? And province_id = ? ");
			ps.setString(1, ngay_quay);
			ps.setInt(2, province_id);
			ResultSet resultSet =	ps.executeQuery();
			if(resultSet.next())
			{
				String giai_dacbiet = resultSet.getString(1);
				if(!giai_dacbiet.isEmpty()&&!"".equalsIgnoreCase(giai_dacbiet.trim())) kq = 2; else kq = 1;
					
			}
			C3p010hPool.attemptClose(resultSet);
			C3p010hPool.attemptClose(ps);
			C3p010hPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return kq;
	}
	
	
	public int getProvinceIdByCode(String code) {
		PreparedStatement ps;
		int kq = 0; // 0 chua co, 1 dang tuong thuat, 2 ket thuc
		try {
			Connection connection = C3p010hPool.getConnection();
			ps = connection.prepareStatement("SELECT 	id, CODE, NAME	FROM province WHERE UPPER(CODE) = '"+code.toUpperCase()+"'");
			//ps.setString(1, code.toUpperCase());
			
			ResultSet resultSet =	ps.executeQuery();
			if(resultSet.next())
			{
				 kq = resultSet.getInt(1);
			}
			C3p010hPool.attemptClose(resultSet);
			C3p010hPool.attemptClose(ps);
			C3p010hPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return kq;
	}
	
	
	public int saveKqDienToan6x36(KQDienToan6x36 kqmb) {
		PreparedStatement ps;
		int id = 0;
		try {
			Connection connection = C3p010hPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement("INSERT INTO ketqua_dientoan6x36 ( ngay_quay,ketqua_1,ketqua_2,ketqua_3,ketqua_4," +
					" ketqua_5,ketqua_6,create_user,create_date,modify_user,modify_date )" +
					" VALUES (?,?,?,?,?,?,?,?,?,?,?)");
			
			ps.setString(1,kqmb.ngay_quay);
			ps.setString(2,kqmb.ketqua_1.trim());
			ps.setString(3,kqmb.ketqua_2.trim());
			ps.setString(4,kqmb.ketqua_3.trim());
			ps.setString(5,kqmb.ketqua_4.trim());
			ps.setString(6,kqmb.ketqua_5.trim());
			ps.setString(7,kqmb.ketqua_6.trim());
			
			ps.setString(8,kqmb.create_user);
			ps.setString(9,kqmb.create_date);
			ps.setString(10,kqmb.modify_user);
			ps.setString(11,kqmb.modify_date);
			
			ps.execute();
			connection.commit();
			connection.setAutoCommit(true);		
			ps = connection.prepareStatement("SELECT LAST_INSERT_ID()");
			ResultSet rs = ps.executeQuery();
			if(rs.next()) id = rs.getInt(1);
			C3p010hPool.attemptClose(ps);
			C3p010hPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	public int saveKqDienToan123(KQDienToan123 kqmb) {
		PreparedStatement ps;
		int id = 0;
		try {
			Connection connection = C3p010hPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement("INSERT INTO ketqua_dientoan123 " +
					"(ngay_quay, ketqua_1, ketqua_2, ketqua_3, create_user, create_date, modify_user, modify_date) " +
					"	  VALUES(?,?,?,?,?,?,?,?)");
			
			ps.setString(1,kqmb.ngay_quay);
			ps.setString(2,kqmb.ketqua_1.trim());
			ps.setString(3,kqmb.ketqua_2.trim());
			ps.setString(4,kqmb.ketqua_3.trim());
			
			ps.setString(5,kqmb.create_user);
			ps.setString(6,kqmb.create_date);
			ps.setString(7,kqmb.modify_user);
			ps.setString(8,kqmb.modify_date);
			
			ps.execute();
			connection.commit();
			connection.setAutoCommit(true);		
			ps = connection.prepareStatement("SELECT LAST_INSERT_ID()");
			ResultSet rs = ps.executeQuery();
			if(rs.next()) id = rs.getInt(1);
			C3p010hPool.attemptClose(ps);
			C3p010hPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	
	public int saveKqDienToanThanTai(KQThanTai kqmb) {
		PreparedStatement ps;
		int id = 0;
		try {
			Connection connection = C3p010hPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement("INSERT INTO ketqua_thantai" +
					"(ngay_quay, ketqua, create_user, create_date, modify_user, modify_date )" +
					"VALUES (?,?,?,?,?,?)");
			
			ps.setString(1,kqmb.ngay_quay);
			ps.setString(2,kqmb.ketqua.trim());
			
			ps.setString(3,kqmb.create_user);
			ps.setString(4,kqmb.create_date);
			ps.setString(5,kqmb.modify_user);
			ps.setString(6,kqmb.modify_date);
			
			ps.execute();
			connection.commit();
			connection.setAutoCommit(true);		
			ps = connection.prepareStatement("SELECT LAST_INSERT_ID()");
			ResultSet rs = ps.executeQuery();
			if(rs.next()) id = rs.getInt(1);
			C3p010hPool.attemptClose(ps);
			C3p010hPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	public int checkKqDT6x36(String ngay_quay) {
		PreparedStatement ps;
		int kq = 0; // 0 chua co, 1 dang tuong thuat, 2 ket thuc
		try {
			Connection connection = C3p010hPool.getConnection();
			ps = connection.prepareStatement("SELECT id,ngay_quay,ketqua_1,ketqua_2,ketqua_3,ketqua_4," +
					"ketqua_5,ketqua_6  FROM ketqua_dientoan6x36  WHERE ngay_quay = ? ");
			ps.setString(1, ngay_quay);
			ResultSet resultSet =	ps.executeQuery();
			if(resultSet.next())
			{
				String ketqua_1 = resultSet.getString("ketqua_1");
				String ketqua_2 = resultSet.getString("ketqua_2");
				String ketqua_3 = resultSet.getString("ketqua_3");
				String ketqua_4 = resultSet.getString("ketqua_4");
				String ketqua_5 = resultSet.getString("ketqua_5");
				String ketqua_6 = resultSet.getString("ketqua_6");
				if(!ketqua_1.isEmpty()&&!"".equalsIgnoreCase(ketqua_1.trim())&&
				   !ketqua_2.isEmpty()&&!"".equalsIgnoreCase(ketqua_2.trim())&&
				   !ketqua_3.isEmpty()&&!"".equalsIgnoreCase(ketqua_3.trim())&&
				   !ketqua_4.isEmpty()&&!"".equalsIgnoreCase(ketqua_4.trim())&&
				   !ketqua_5.isEmpty()&&!"".equalsIgnoreCase(ketqua_5.trim())&&
				   !ketqua_6.isEmpty()&&!"".equalsIgnoreCase(ketqua_6.trim())) 
					kq = 2; else kq = 1;
					
			}
			C3p010hPool.attemptClose(resultSet);
			C3p010hPool.attemptClose(ps);
			C3p010hPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return kq;
	}
	
	public int checkKqDT123(String ngay_quay) {
		PreparedStatement ps;
		int kq = 0; // 0 chua co, 1 dang tuong thuat, 2 ket thuc
		try {
			Connection connection = C3p010hPool.getConnection();
			ps = connection.prepareStatement("SELECT id,ngay_quay,ketqua_1,ketqua_2,ketqua_3 FROM ketqua_dientoan123  WHERE ngay_quay = ? ");
			ps.setString(1, ngay_quay);
			ResultSet resultSet =	ps.executeQuery();
			if(resultSet.next())
			{
				String ketqua_1 = resultSet.getString("ketqua_1");
				String ketqua_2 = resultSet.getString("ketqua_2");
				String ketqua_3 = resultSet.getString("ketqua_3");
				if(!ketqua_1.isEmpty()&&!"".equalsIgnoreCase(ketqua_1.trim())&&
				   !ketqua_2.isEmpty()&&!"".equalsIgnoreCase(ketqua_2.trim())&&
				   !ketqua_3.isEmpty()&&!"".equalsIgnoreCase(ketqua_3.trim())) 
					kq = 2; else kq = 1;
					
			}
			C3p010hPool.attemptClose(resultSet);
			C3p010hPool.attemptClose(ps);
			C3p010hPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return kq;
	}
	
	public int checkKqThanTai(String ngay_quay) {
		PreparedStatement ps;
		int kq = 0; // 0 chua co, 1 dang tuong thuat, 2 ket thuc
		try {
			Connection connection = C3p010hPool.getConnection();
			ps = connection.prepareStatement("SELECT id,ngay_quay,ketqua FROM ketqua_thantai  WHERE ngay_quay = ? ");
			ps.setString(1, ngay_quay);
			ResultSet resultSet =	ps.executeQuery();
			if(resultSet.next())
			{
				String ketqua_1 = resultSet.getString("ketqua");
				if(!ketqua_1.isEmpty()&&!"".equalsIgnoreCase(ketqua_1.trim())) 
					kq = 2; else kq = 1;
					
			}
			C3p010hPool.attemptClose(resultSet);
			C3p010hPool.attemptClose(ps);
			C3p010hPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return kq;
	}
	
	public int updateKqDienToan6x36(KQDienToan6x36 kqmb,String ngay_quay) {
		PreparedStatement ps;
		int id = 0;
		try {
			Connection connection = C3p010hPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement("UPDATE ketqua_dientoan6x36 " +
					" SET ketqua_1 = ? ,ketqua_2 = ? ,ketqua_3 = ?," +
					" ketqua_4 = ? ,ketqua_5 = ? ,ketqua_6 = ? ," +
					" modify_user = ? , modify_date = ? WHERE ngay_quay = ?  ;");

			ps.setString(1,kqmb.ketqua_1.trim());
			ps.setString(2,kqmb.ketqua_2.trim());
			ps.setString(3,kqmb.ketqua_3.trim());
			ps.setString(4,kqmb.ketqua_4.trim());
			
			ps.setString(5,kqmb.ketqua_5.trim());
			ps.setString(6,kqmb.ketqua_6.trim());
			ps.setString(7,kqmb.modify_user.trim());
			ps.setString(8,kqmb.modify_date.trim());
			ps.setString(9,kqmb.ngay_quay.trim());
		
			
			ps.executeUpdate();
			connection.commit();
			connection.setAutoCommit(true);		
			
			C3p010hPool.attemptClose(ps);
			C3p010hPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	public int updateKqDienToan123(KQDienToan123 kqmb,String ngay_quay) {
		PreparedStatement ps;
		int id = 0;
		try {
			Connection connection = C3p010hPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement("UPDATE ketqua_dientoan123 " +
					" SET ketqua_1 = ? ,ketqua_2 = ? ,ketqua_3 = ?," +
					" modify_user = ? , modify_date = ? WHERE ngay_quay = ?  ;");

			ps.setString(1,kqmb.ketqua_1.trim());
			ps.setString(2,kqmb.ketqua_2.trim());
			ps.setString(3,kqmb.ketqua_3.trim());
			ps.setString(4,kqmb.modify_user.trim());
			ps.setString(5,kqmb.modify_date.trim());
			ps.setString(6,kqmb.ngay_quay.trim());
			
			ps.executeUpdate();
			connection.commit();
			connection.setAutoCommit(true);		
			
			C3p010hPool.attemptClose(ps);
			C3p010hPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	public int updateKqThanTai(KQThanTai kqmb,String ngay_quay) {
		PreparedStatement ps;
		int id = 0;
		try {
			Connection connection = C3p010hPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement("UPDATE ketqua_thantai  " +
					" SET ketqua = ? ,modify_user=?, modify_date = ? WHERE ngay_quay = ?  ;");

			ps.setString(1,kqmb.ketqua.trim());
			ps.setString(2,kqmb.modify_user.trim());
			ps.setString(3,kqmb.modify_date.trim());
			ps.setString(4,kqmb.ngay_quay.trim());
			
			ps.executeUpdate();
			connection.commit();
			connection.setAutoCommit(true);		
			
			C3p010hPool.attemptClose(ps);
			C3p010hPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	public boolean saveFeed(Feed feed) {
		Connection conn = null;
		PreparedStatement preStmt = null;
		StringBuffer strSQL = null;
		boolean result = false;
		try {
			conn = C3p010hPool.getConnection();		
			conn.setAutoCommit(false);
			strSQL = new StringBuffer(
					" INSERT INTO xs_feed (title,image,url,description,type,create_date, create_user,isMobile) VALUES " +
					"  (?, ?, ?, ?,? ,NOW(),?,?)");

			preStmt = conn.prepareStatement(strSQL.toString());
			preStmt.setString(1,feed.title);
			preStmt.setString(2,feed.image);
			preStmt.setString(3,feed.url);
			preStmt.setString(4,feed.description);
			preStmt.setInt(5,feed.type);
			preStmt.setString(6,feed.create_user);
			preStmt.setInt(7,feed.isMobile);

			if (preStmt.executeUpdate() == 1) {
				conn.commit();
				result = true;
			} else {
				conn.rollback();
			}
			conn.setAutoCommit(true);
		} catch (NoSuchElementException nse) {
			nse.printStackTrace();
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {			
			C3p010hPool.attemptClose(preStmt);
			C3p010hPool.attemptClose(conn);
		}
		return result;
	}
	
	public boolean deleteFeed(int id) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement preStmt = null;
		StringBuffer strSQL = null;
		try {
			conn = C3p010hPool.getConnection();	
			conn.setAutoCommit(false);
			strSQL = new StringBuffer("DELETE FROM xs_feed WHERE id = ? ");
			preStmt = conn.prepareStatement(strSQL.toString());
			preStmt.setInt(1, id);
			if (preStmt.executeUpdate() == 1) {
				result = true;
				conn.commit();
			} else {
				conn.rollback();
			}
			conn.setAutoCommit(true);
		} catch (NoSuchElementException nse) {
			nse.printStackTrace();
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {			
			C3p010hPool.attemptClose(preStmt);
			C3p010hPool.attemptClose(conn);
		}
		return result;
	}
	
	public boolean deleteFeedByType(int type) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement preStmt = null;
		StringBuffer strSQL = null;
		try {
			conn = C3p010hPool.getConnection();
			conn.setAutoCommit(false);
			strSQL = new StringBuffer("DELETE FROM xs_feed WHERE type = ? ");
			preStmt = conn.prepareStatement(strSQL.toString());
			preStmt.setInt(1, type);
			if (preStmt.executeUpdate()>0) {
				result = true;
				conn.commit();
			} else {
				conn.rollback();
			}
			conn.setAutoCommit(true);
		} catch (NoSuchElementException nse) {
			nse.printStackTrace();
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {			
			C3p010hPool.attemptClose(preStmt);
			C3p010hPool.attemptClose(conn);
		}
		return result;
	}
	
	
	public ArrayList<Feed> getNewFeed(int isMobile) {
		PreparedStatement ps;
		ArrayList<Feed> feeds = new ArrayList<Feed>();
		
		try {
			Connection connection = C3p010hPool.getConnection();
			ps = connection.prepareStatement("SELECT id,title,image, url, description, TYPE FROM xs_feed Where isMobile = ? ORDER BY id DESC LIMIT 0, 10");
			ps.setInt(1, isMobile);
			
			ResultSet rs =	ps.executeQuery();
			while(rs.next())
			{
				Feed feed = new Feed();
				feed.id = rs.getInt("id");
				feed.title = rs.getString("title");
				feed.image = rs.getString("image");
				feed.url = rs.getString("url");
				feed.description = rs.getString("description");
				feed.type = rs.getInt("type");
				feeds.add(feed);
			}
			C3p010hPool.attemptClose(rs);
			C3p010hPool.attemptClose(ps);
			C3p010hPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return feeds;
	}
	
	
	public boolean saveNotify(int object_id,String open_date,String content,int type) {
		Connection conn = null;
		PreparedStatement preStmt = null;
		StringBuffer strSQL = null;
		boolean result = false;
		try {
			conn = C3p010hPool.getConnection();		
			conn.setAutoCommit(false);
			strSQL = new StringBuffer(
					"INSERT INTO x_notice (object_id,to_user,from_user,content,icon,"
					+ "url,TYPE,STATUS,time_sent,create_date,create_user,open_date) VALUES (?,?,?,?,?,?,?,?,NOW(),NOW(),?,?);");

			preStmt = conn.prepareStatement(strSQL.toString());
			preStmt.setInt(1,object_id);
			preStmt.setInt(2,0);
			preStmt.setInt(3,0);
			preStmt.setString(4,content);
			preStmt.setString(5,"");
			preStmt.setString(6,"");
			preStmt.setInt(7,type);
			preStmt.setInt(8,1);
			preStmt.setString(9,"crawler");
			preStmt.setString(10,open_date);


			if (preStmt.executeUpdate() == 1) {
				conn.commit();
				result = true;
			} else {
				conn.rollback();
			}
			conn.setAutoCommit(true);
		} catch (NoSuchElementException nse) {
			nse.printStackTrace();
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {			
			C3p010hPool.attemptClose(preStmt);
			C3p010hPool.attemptClose(conn);
		}
		return result;
	}
	
	
	public int checkNotify(int object_id,String open_date) {
		PreparedStatement ps;
		int kq =0;
		try {
			Connection connection = C3p010hPool.getConnection();
			ps = connection.prepareStatement("SELECT * FROM x_notice Where object_id = ?"
					+ " AND open_date = ? ");
			ps.setInt(1, object_id);
			ps.setString(2, open_date);
			
			ResultSet rs =	ps.executeQuery();
			if(rs.next())
			{
				kq =1;
			}
			C3p010hPool.attemptClose(rs);
			C3p010hPool.attemptClose(ps);
			C3p010hPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return kq;
	}
	
	
	public static void main(String[] args) {
		W10HDAO w10hdao = new W10HDAO();
		//w10hdao.deleteFeedByType(3);
		w10hdao.saveNotify(1,"2015-05-28","kfdfdfd", 1);
		
	}
	
}
