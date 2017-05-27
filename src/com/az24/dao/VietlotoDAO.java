package com.az24.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;


import com.az24.db.pool.C3p0VietlotoPool;

public class VietlotoDAO {
	public int countSolanVe645(String boso) {
		PreparedStatement ps;
		int kq =0;
		try {
			Connection connection = C3p0VietlotoPool.getConnection();
			ps = connection.prepareStatement("SELECT COUNT(*) as sl FROM ketqua_645 WHERE bo_1 = ? OR  bo_2 = ? OR bo_3 = ? OR  bo_4 = ? OR bo_5 = ? OR  bo_1 = ? ");
			ps.setString(1, boso);
			ps.setString(2, boso);
			ps.setString(3, boso);
			ps.setString(4, boso);
			ps.setString(5, boso);
			ps.setString(6, boso);
			
			ResultSet rs =	ps.executeQuery();
			if(rs.next())
			{
				kq = rs.getInt("sl");
			}
			C3p0VietlotoPool.attemptClose(rs);
			C3p0VietlotoPool.attemptClose(ps);
			C3p0VietlotoPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return kq;
	}
	
	public int checkXoSo645(String ngay_mo) {
		PreparedStatement ps;
		int kq =0;
		try {
			Connection connection = C3p0VietlotoPool.getConnection();
			ps = connection.prepareStatement("SELECT * FROM ketqua_645 Where ngay_mo = ?");
			ps.setString(1, ngay_mo);
			
			ResultSet rs =	ps.executeQuery();
			if(rs.next())
			{
				kq = rs.getInt("id");
			}
			C3p0VietlotoPool.attemptClose(rs);
			C3p0VietlotoPool.attemptClose(ps);
			C3p0VietlotoPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return kq;
	}
	
	public int checkXoSoMax4d(String ngay_mo) {
		PreparedStatement ps;
		int kq =0;
		try {
			Connection connection = C3p0VietlotoPool.getConnection();
			ps = connection.prepareStatement("SELECT * FROM ketqua_max4d Where ngay_mo = ?");
			ps.setString(1, ngay_mo);
			
			ResultSet rs =	ps.executeQuery();
			if(rs.next())
			{
				kq = rs.getInt("id");
			}
			C3p0VietlotoPool.attemptClose(rs);
			C3p0VietlotoPool.attemptClose(ps);
			C3p0VietlotoPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return kq;
	}
	
	
	
	public boolean saveXoSo645(String ky_ve,String ngay_mo,String bo_1,String bo_2,String bo_3,String bo_4,String bo_5,String bo_6,int jackpot_so_giai
			,double jackpot_gia_tri,int giainhat_so_giai,double giainhat_gia_tri,int giainhi_so_giai,double giainhi_gia_tri,
			int giaiba_so_giai,double giaiba_gia_tri) {
		Connection conn = null;
		PreparedStatement preStmt = null;
		StringBuffer strSQL = null;
		boolean result = false;
		try {
			conn = C3p0VietlotoPool.getConnection();		
			conn.setAutoCommit(false);
			strSQL = new StringBuffer(
					" INSERT INTO vtc_vietlott.ketqua_645 (ky_ve,ngay_mo,bo_1,bo_2,bo_3,bo_4,bo_5,bo_6,jackpot_so_giai,jackpot_gia_tri,"
					+ "nhat_so_giai,nhat_gia_tri,nhi_so_giai,nhi_gia_tri,ba_so_giai,ba_gia_tri,create_date)"
					+ " VALUES(?,?,?,?,?,?,?,?,?,?,"
					+ "?,?,?,?,?,?,NOW());");

			preStmt = conn.prepareStatement(strSQL.toString());
			preStmt.setString(1,ky_ve);
			preStmt.setString(2,ngay_mo);
			preStmt.setString(3,bo_1);
			preStmt.setString(4,bo_2);
			preStmt.setString(5,bo_3);
			preStmt.setString(6,bo_4);
			preStmt.setString(7,bo_5);
			preStmt.setString(8,bo_6);
			preStmt.setInt(9,jackpot_so_giai);
			preStmt.setDouble(10,jackpot_gia_tri);
			preStmt.setInt(11,giainhat_so_giai);
			preStmt.setDouble(12,giainhat_gia_tri);
			preStmt.setInt(13,giainhi_so_giai);
			preStmt.setDouble(14,giainhi_gia_tri);
			preStmt.setInt(15,giaiba_so_giai);
			preStmt.setDouble(16,giaiba_gia_tri);


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
			C3p0VietlotoPool.attemptClose(preStmt);
			C3p0VietlotoPool.attemptClose(conn);
		}
		return result;
	}
	
	public boolean updateXoSo645(int id , String ky_ve,String ngay_mo,String bo_1,String bo_2,String bo_3,String bo_4,String bo_5,String bo_6
			,int jackpot_so_giai
			,double jackpot_gia_tri,int giainhat_so_giai,double giainhat_gia_tri,int giainhi_so_giai,double giainhi_gia_tri,
			int giaiba_so_giai,double giaiba_gia_tri) {
		Connection conn = null;
		PreparedStatement preStmt = null;
		StringBuffer strSQL = null;
		boolean result = false;
		try {
			conn = C3p0VietlotoPool.getConnection();		
			conn.setAutoCommit(false);
			strSQL = new StringBuffer(
					" UPDATE vtc_vietlott.ketqua_645 SET ky_ve = ? ,ngay_mo = ? ,bo_1 = ? ,"
					+ "bo_2 = ? ,bo_3 = ? ,bo_4 = ? ,bo_5 = ? ,bo_6 = ? ,jackpot_so_giai = ? ,"
					+ "jackpot_gia_tri = ? ,nhat_so_giai = ? ,nhat_gia_tri = ? ,"
					+ "nhi_so_giai = ? ,nhi_gia_tri = ? ,ba_so_giai = ? ,"
					+ "ba_gia_tri = ?  WHERE id = ? ;");

			preStmt = conn.prepareStatement(strSQL.toString());
			preStmt.setString(1,ky_ve);
			preStmt.setString(2,ngay_mo);
			preStmt.setString(3,bo_1);
			preStmt.setString(4,bo_2);
			preStmt.setString(5,bo_3);
			preStmt.setString(6,bo_4);
			preStmt.setString(7,bo_5);
			preStmt.setString(8,bo_6);
			preStmt.setInt(9,jackpot_so_giai);
			preStmt.setDouble(10,jackpot_gia_tri);
			preStmt.setInt(11,giainhat_so_giai);
			preStmt.setDouble(12,giainhat_gia_tri);
			preStmt.setInt(13,giainhi_so_giai);
			preStmt.setDouble(14,giainhi_gia_tri);
			preStmt.setInt(15,giaiba_so_giai);
			preStmt.setDouble(16,giaiba_gia_tri);
			preStmt.setInt(17,id);

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
			C3p0VietlotoPool.attemptClose(preStmt);
			C3p0VietlotoPool.attemptClose(conn);
		}
		return result;
	}
	
	public boolean saveMax4d(String ngay_mo,String nhat,String nhi_1,String nhi_2,String ba_1
			,String ba_2,String ba_3,String kk_1,String kk_2,int nhat_so_giai, int nhi_so_giai,
			int ba_so_gai, int kk_1_so_giai, int kk_2_so_giai,double nhat_giai_tri, double nhi_giai_tri, 
			double ba_giai_tri, double kk_1_giai_tri, double kk_2_giai_tri) {
		Connection conn = null;
		PreparedStatement preStmt = null;
		StringBuffer strSQL = null;
		boolean result = false;
		try {
			conn = C3p0VietlotoPool.getConnection();		
			conn.setAutoCommit(false);
			strSQL = new StringBuffer(
					" INSERT INTO vtc_vietlott.ketqua_max4d (ngay_mo,nhat,nhi_1,nhi_2,ba_1,ba_2,ba_3,kk_1,kk_2,nhat_so_giai,nhi_so_giai,"
					+ "ba_so_giai,kk_1_so_giai,kk_2_so_giai,nhat_giai_tri,nhi_giai_tri,ba_giai_tri,kk_1_giai_tri,kk_2_giai_tri,create_date)"
					+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW());");

			preStmt = conn.prepareStatement(strSQL.toString());
			preStmt.setString(1,ngay_mo);
			preStmt.setString(2,nhat);
			preStmt.setString(3,nhi_1);
			preStmt.setString(4,nhi_2);
			preStmt.setString(5,ba_1);
			preStmt.setString(6,ba_2);
			preStmt.setString(7,ba_3);
			preStmt.setString(8,kk_1);
			preStmt.setString(9,kk_2);
			
			preStmt.setInt(10,nhat_so_giai);
			preStmt.setInt(11,nhi_so_giai);
			preStmt.setInt(12,ba_so_gai);
			preStmt.setInt(13,kk_1_so_giai);
			preStmt.setInt(14,kk_2_so_giai);
			
			preStmt.setDouble(15,nhat_giai_tri);
			preStmt.setDouble(16,nhi_giai_tri);
			preStmt.setDouble(17,ba_giai_tri);
			preStmt.setDouble(18,kk_1_giai_tri);
			preStmt.setDouble(19,kk_2_giai_tri);
			
			
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
			C3p0VietlotoPool.attemptClose(preStmt);
			C3p0VietlotoPool.attemptClose(conn);
		}
		return result;
	}
	
	public boolean updateMax4d(int id,String ngay_mo,String nhat,String nhi_1,String nhi_2,String ba_1,String ba_2,String ba_3,String kk_1
			,String kk_2,int nhat_so_giai,int  nhi_so_giai ,int ba_so_giai,
					int kk_1_so_giai,int kk_2_so_giai,double nhat_giai_tri,double nhi_giai_tri,double ba_giai_tri,
					double kk_1_giai_tri,double kk_2_giai_tri) {
		Connection conn = null;
		PreparedStatement preStmt = null;
		StringBuffer strSQL = null;
		boolean result = false;
		try {
			conn = C3p0VietlotoPool.getConnection();		
			conn.setAutoCommit(false);
			strSQL = new StringBuffer(
					" UPDATE vtc_vietlott.ketqua_max4d  SET ngay_mo = ?, nhat = ? , nhi_1 = ? "
					+ ",nhi_2 = ? , ba_1 = ? , ba_2 = ? , ba_3 = ? ,kk_1 = ? , kk_2 = ?, nhat_so_giai=?, nhi_so_giai =?,ba_so_giai=?,"
					+ "kk_1_so_giai=?,kk_2_so_giai=?,nhat_giai_tri=?,nhi_giai_tri=?,ba_giai_tri=?,"
					+ "kk_1_giai_tri=?,kk_2_giai_tri=? WHERE id = ? ;");

			preStmt = conn.prepareStatement(strSQL.toString());
			preStmt.setString(1,ngay_mo);
			preStmt.setString(2,nhat);
			preStmt.setString(3,nhi_1);
			preStmt.setString(4,nhi_2);
			preStmt.setString(5,ba_1);
			preStmt.setString(6,ba_2);
			preStmt.setString(7,ba_3);
			preStmt.setString(8,kk_1);
			preStmt.setString(9,kk_2);
			
			preStmt.setInt(10,nhat_so_giai);
			preStmt.setInt(11,nhi_so_giai);
			preStmt.setInt(12,ba_so_giai);
			preStmt.setInt(13,kk_1_so_giai);
			preStmt.setInt(14,kk_2_so_giai);
			
			preStmt.setDouble(15,nhat_giai_tri);
			preStmt.setDouble(16,nhi_giai_tri);
			preStmt.setDouble(17,ba_giai_tri);
			preStmt.setDouble(18,kk_1_giai_tri);
			preStmt.setDouble(19,kk_2_giai_tri);
			
			preStmt.setInt(20,id);
			
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
			C3p0VietlotoPool.attemptClose(preStmt);
			C3p0VietlotoPool.attemptClose(conn);
		}
		return result;
	}
	
	
	public String lastdateByBoSo(String boso) {
		PreparedStatement ps;
		String ngay_mo ="";
		try {
			Connection connection = C3p0VietlotoPool.getConnection();
			ps = connection.prepareStatement("SELECT ngay_mo FROM ketqua_645 WHERE bo_1 = ? OR  bo_2 = ? OR bo_3 = ? OR  bo_4 = ? OR bo_5 = ? OR  bo_1 = ? ORDER BY ngay_mo DESC LIMIT 1");
			ps.setString(1, boso);
			ps.setString(2, boso);
			ps.setString(3, boso);
			ps.setString(4, boso);
			ps.setString(5, boso);
			ps.setString(6, boso);
			
			ResultSet rs =	ps.executeQuery();
			if(rs.next())
			{
				ngay_mo = rs.getString("ngay_mo");
			}
			C3p0VietlotoPool.attemptClose(rs);
			C3p0VietlotoPool.attemptClose(ps);
			C3p0VietlotoPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ngay_mo;
	}
	
	public boolean saveThongKe645(String boso,int so_lan,String ngay_ve_cuoi) {
		Connection conn = null;
		PreparedStatement preStmt = null;
		StringBuffer strSQL = null;
		boolean result = false;
		try {
			conn = C3p0VietlotoPool.getConnection();		
			conn.setAutoCommit(false);
			strSQL = new StringBuffer(
					" INSERT INTO vtc_vietlott.thongke_645 (bo_so, so_lan, ngay_ve_cuoi, update_date) "
					+ "VALUES (?, ?, ?, NOW());");

			preStmt = conn.prepareStatement(strSQL.toString());
			preStmt.setString(1,boso);
			preStmt.setInt(2,so_lan);
			preStmt.setString(3,ngay_ve_cuoi);
			
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
			C3p0VietlotoPool.attemptClose(preStmt);
			C3p0VietlotoPool.attemptClose(conn);
		}
		return result;
	}
	
	public boolean updateThongKe645(int id,int so_lan,String ngay_ve_cuoi) {
		Connection conn = null;
		PreparedStatement preStmt = null;
		StringBuffer strSQL = null;
		boolean result = false;
		try {
			conn = C3p0VietlotoPool.getConnection();		
			conn.setAutoCommit(false);
			strSQL = new StringBuffer(
					" Update  vtc_vietlott.thongke_645 SET so_lan = ? , ngay_ve_cuoi = ? , update_date = NOW() Where id = ?");

			preStmt = conn.prepareStatement(strSQL.toString());
			preStmt.setInt(1,so_lan);
			preStmt.setString(2,ngay_ve_cuoi);
			preStmt.setInt(3,id);
			
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
			C3p0VietlotoPool.attemptClose(preStmt);
			C3p0VietlotoPool.attemptClose(conn);
		}
		return result;
	}
	
	public int checkThongKe645(String boso) {
		PreparedStatement ps;
		int kq =0;
		try {
			Connection connection = C3p0VietlotoPool.getConnection();
			ps = connection.prepareStatement("SELECT * FROM thongke_645 Where bo_so = ?");
			ps.setString(1, boso);
			
			ResultSet rs =	ps.executeQuery();
			if(rs.next())
			{
				kq = rs.getInt("id");
			}
			C3p0VietlotoPool.attemptClose(rs);
			C3p0VietlotoPool.attemptClose(ps);
			C3p0VietlotoPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return kq;
	}
	
	
	
	
	public static void main(String[] args) {
		VietlotoDAO w10hdao = new VietlotoDAO();
		//w10hdao.deleteFeedByType(3);
		w10hdao.checkXoSo645("2015-05-28");
		
	}
}
