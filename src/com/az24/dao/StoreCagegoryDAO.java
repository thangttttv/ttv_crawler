package com.az24.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.az24.crawler.store.Category;
import com.az24.crawler.store.StoreCategory;
import com.az24.db.pool.C3p0ClassifiedPool;
import com.az24.db.pool.C3p0StorePool;
import com.az24.util.UTF8Tool;

public class StoreCagegoryDAO {
	
	public int saveCategory(StoreCategory storeCategory) {
		PreparedStatement ps;
		int id = 0;
		try {
			Connection connection = C3p0StorePool.getConnection();;
			connection.setAutoCommit(false);
			ps = connection
						.prepareStatement("INSERT INTO az24_store.store_category (name,cat_id,store_id,parent_id,sub_cat," +
								"level,	order_cat,	num_of_products,status,	is_show_home, is_event,	is_default,	create_user," +
								" create_date,	modify_user, modify_date,url_map_md5,single_path ) VALUES (?,?,?,?," +
								" ?,?,?,?,?,?,?,?," +
								" ?,?,?,?,?,?)");
				
				ps.setString(1, storeCategory.name);
				ps.setInt(2, storeCategory.cat_id);
				ps.setInt(3, storeCategory.store_id);
				ps.setInt(4, storeCategory.parent_id);
				ps.setString(5,storeCategory.sub_cat);
				ps.setInt(6,storeCategory.level);
				ps.setInt(7, storeCategory.order_cat);
				ps.setInt(8,storeCategory.num_of_products);
				ps.setInt(9, storeCategory.status);
				ps.setInt(10, storeCategory.is_show_home);
				ps.setInt(11, storeCategory.is_event);
				ps.setInt(12, storeCategory.is_default);
				ps.setString(13, storeCategory.create_user);
				ps.setInt(14, storeCategory.create_date);
				ps.setString(15, storeCategory.modify_user);
				ps.setInt(16, storeCategory.modify_date);
				ps.setString(17, storeCategory.url_map_md5);
				
				Pattern pattern = Pattern.compile("\\W+");
				Pattern pattern2 = Pattern.compile("-$");				
				String name = UTF8Tool.coDau2KoDau(storeCategory.name).trim();
				Matcher matcher = pattern.matcher(name);
				String sign_path=matcher.replaceAll("-");
				matcher = pattern2.matcher(sign_path);
				sign_path = matcher.replaceAll("");		
				
				ps.setString(18, sign_path);
				
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
	
	public StoreCategory getCate(String product_vg_id,int store_id) {
		
		PreparedStatement ps;		
		StoreCategory category = null;
		try {
			Connection connection = C3p0StorePool.getConnection();;
			ps = connection.prepareStatement("SELECT id,parent_id,cat_id FROM store_category  WHERE url_map_md5 = ? and store_id = ? and parent_id>0");
			ps.setString(1, product_vg_id);
			ps.setInt(2,store_id);
			ResultSet resultSet =	ps.executeQuery();
			if(resultSet.next())
			{
				category = new StoreCategory();
				category.id = resultSet.getInt(1);
				category.parent_id = resultSet.getInt(2);
				category.cat_id = resultSet.getInt(3);
			}
			C3p0ClassifiedPool.attemptClose(connection);
			C3p0ClassifiedPool.attemptClose(ps);
			C3p0ClassifiedPool.attemptClose(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return category;
	}
	
	
	public StoreCategory getCateByName(String single_path,int store_id) {
		
		PreparedStatement ps;		
		StoreCategory category = null;
		try {
			Connection connection = C3p0StorePool.getConnection();;
			ps = connection.prepareStatement("SELECT id,parent_id,cat_id FROM store_category  WHERE name = ? and store_id = ? ");
			ps.setString(1, single_path);
			ps.setInt(2,store_id);
			ResultSet resultSet =	ps.executeQuery();
			if(resultSet.next())
			{
				category = new StoreCategory();
				category.id = resultSet.getInt(1);
				category.parent_id = resultSet.getInt(2);
				category.cat_id = resultSet.getInt(3);
			}
			C3p0ClassifiedPool.attemptClose(connection);
			C3p0ClassifiedPool.attemptClose(ps);
			C3p0ClassifiedPool.attemptClose(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return category;
	}
	

	public boolean checkCate(String cate_name,int store_id,int parent_id) {
		
		PreparedStatement ps;		
		boolean kq = false;
		try {
			Connection connection = C3p0StorePool.getConnection();;
			ps = connection.prepareStatement("SELECT id FROM store_category  WHERE name = ? AND store_id = ? AND parent_id = ? ");
			ps.setString(1, cate_name);
			ps.setInt(2, store_id);
			ps.setInt(3, parent_id);
			ResultSet resultSet =	ps.executeQuery();
			if(resultSet.next())
			{
				kq = true;
			}
			C3p0ClassifiedPool.attemptClose(connection);
			C3p0ClassifiedPool.attemptClose(ps);
			C3p0ClassifiedPool.attemptClose(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return kq;
	}
	
	public int checkCate(String cate_name,int store_id) {
		
		PreparedStatement ps;		
		int id = 0;
		try {
			Connection connection = C3p0StorePool.getConnection();;
			ps = connection.prepareStatement("SELECT id FROM store_category  WHERE name = ? AND store_id = ? AND parent_id = 0 ");
			ps.setString(1, cate_name);
			ps.setInt(2, store_id);
			
			ResultSet resultSet =	ps.executeQuery();
			if(resultSet.next())
			{
				id = resultSet.getInt(1);
			}
			C3p0ClassifiedPool.attemptClose(connection);
			C3p0ClassifiedPool.attemptClose(ps);
			C3p0ClassifiedPool.attemptClose(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	
	public StoreCategory getCate(int store_cat_id) {
		
		PreparedStatement ps;		
		StoreCategory category = null;
		try {
			Connection connection = C3p0StorePool.getConnection();;
			ps = connection.prepareStatement("SELECT id,parent_id,cat_id FROM store_category  WHERE id = ? ");
			ps.setInt(1, store_cat_id);
			ResultSet resultSet =	ps.executeQuery();
			if(resultSet.next())
			{
				category = new StoreCategory();
				category.id = resultSet.getInt(1);
				category.parent_id = resultSet.getInt(2);
				category.cat_id = resultSet.getInt(3);
			}
			C3p0ClassifiedPool.attemptClose(connection);
			C3p0ClassifiedPool.attemptClose(ps);
			C3p0ClassifiedPool.attemptClose(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return category;
	}
	
	public List<Category> getCategoryByStoreId(int store_id) throws Exception {
		
		Connection connection = C3p0StorePool.getConnection();;
		ResultSet rs = connection.createStatement()
				.executeQuery("SELECT c.id,c.name,c.cat_id,c.parent_id,c.level " +
						"FROM store_category c WHERE c.store_id = "	+ store_id);
		Category category = null;
		List<Category> listCat = new ArrayList<Category>();
		while(rs.next())
		{
			category = new Category();
			category.id = rs.getInt(1);
			category.name = rs.getString(2);
			category.cat_id = rs.getInt(3);
			category.parent_id = rs.getInt(4);
			category.level = rs.getInt(5);
			listCat.add(category);
		}
		C3p0ClassifiedPool.attemptClose(connection);
		return listCat;
	}
	
	public void update(List<Category> listCate) throws Exception {
		
		Connection connection = C3p0StorePool.getConnection();;
		PreparedStatement ps = connection.prepareStatement("update store_category set cat_id = ?" +
				" where id = ?  ");
		
		int i  =0;
		connection.setAutoCommit(false);
		while(i<listCate.size())
		{
			ps.setInt(1,listCate.get(i).cat_id );
			ps.setInt(2,listCate.get(i).id );
			ps.addBatch();
			if(i%30==0) {ps.executeBatch();connection.commit();}
			i++;
		}
		ps.executeBatch();connection.commit();
		connection.setAutoCommit(true);
		C3p0ClassifiedPool.attemptClose(connection);
		C3p0ClassifiedPool.attemptClose(ps);
	}
	
	public void updateSinglePath(List<Category> listCate) throws Exception {
		
		Connection connection = C3p0StorePool.getConnection();;
		PreparedStatement ps = connection.prepareStatement("update store_category set single_path = ? " +
				" where id = ?  ");
		
		int i  =0;
		connection.setAutoCommit(false);
		while(i<listCate.size())
		{
			Pattern pattern = Pattern.compile("\\W");
			Matcher matcher = pattern.matcher(UTF8Tool.coDau2KoDau(listCate.get(i).name.trim()));
			String sign_path = matcher.replaceAll("-");
			ps.setString(1,sign_path);
			ps.setInt(2,listCate.get(i).id );
			ps.addBatch();
			if(i%30==0) {ps.executeBatch();connection.commit();}
			i++;
		}
		ps.executeBatch();connection.commit();
		connection.setAutoCommit(true);
		C3p0ClassifiedPool.attemptClose(connection);
		C3p0ClassifiedPool.attemptClose(ps);
		
	}
	
	public void updateMD5(int id,String md5)  {
		
		
		try {
			Connection connection = C3p0StorePool.getConnection();;
			PreparedStatement ps;
			ps = connection.prepareStatement("update store_category set url_map_md5 = ? " +
					" where id = ?  ");
			
			connection.setAutoCommit(false);	
			ps.setString(1,md5);
			ps.setInt(2,id );
			ps.execute();
			connection.commit();
			connection.setAutoCommit(true);
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
	}
}
