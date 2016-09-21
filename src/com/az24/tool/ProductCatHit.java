package com.az24.tool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductCatHit  {
	
	public List<Category> getCats() {
		List<Category> listCats = new ArrayList<Category>();
		try {
			PreparedStatement ps = connLog
					.prepareStatement("select id,name,level FROM tbl_category Where is_multi_sell = 1 ");

			ResultSet resultSet = ps.executeQuery();
			int i = 0;
			while (resultSet.next()) {
				Category cate = new Category();
				cate.id = resultSet.getInt(1);
				cate.name = resultSet.getString(2);
				cate.level = resultSet.getInt(3);
				listCats.add(cate);
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listCats;

	}
	
	
	public int count(int catid,int level) {
		int hit=0;
		try {
			PreparedStatement ps = connLog
					.prepareStatement(" SELECT COUNT(id) FROM  products WHERE cat_id"+level+" = ? ");
			ps.setInt(1,catid);
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				hit = resultSet.getInt(1);				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return hit;

	}
	
	private void updatHit(int cat_id,int sl) {
		PreparedStatement ps;
		try {
			connLog.setAutoCommit(false);
			ps = connLog
					.prepareStatement("Update tbl_category  SET num_of_products = ? WHERE id = ?");
			ps.setInt(1, sl);
			ps.setInt(2, cat_id);
			ps.execute();
			connLog.commit();
			connLog.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public Connection connLog;

	public void openConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connLog = DriverManager
					.getConnection(
							"jdbc:mysql://210.211.97.11:3306/az24_store?characterEncoding=UTF-8",
							"quangpn", "QuangPN2011@!!!*^");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void closeConnection() {
		try {
			if (connLog != null)
				connLog.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		ProductCatHit productCatHit = new ProductCatHit();
		productCatHit.openConnection();
		List<Category> cats = productCatHit.getCats();
		
		for (Category category : cats) {
			System.out.println(category.name);
			int hit = productCatHit.count(category.id,category.level);
			productCatHit.updatHit(category.id, hit);
		}
		
		productCatHit.closeConnection();
		
	}
}
