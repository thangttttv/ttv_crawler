package com.az24.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.az24.crawler.store.ProductStore;
import com.az24.db.pool.C3p0ClassifiedPool;
import com.az24.db.pool.C3p0StorePool;

public class ProductStoreDAO {
	
	public int saveProductStore(ProductStore productStore) {
		PreparedStatement ps;
		int id = 0;
		try {
			Connection connection = C3p0StorePool.getConnection();
			connection.setAutoCommit(false);
			ps = connection
						.prepareStatement("INSERT INTO az24_store.products_store ( product_id, store_id, store_alias, " +
								"   status,  price,	price_type,	quality, quantity, vat,	ship_type," +
								"	is_new, description, url_link_web,  ensure_month, origin," +
								"   create_user, create_date, modify_user, modify_date,last_update,original_link ) " +
								"	VALUES(?, ?, ?, ?, ?, ?, ?, ?,	?,	?, ?, ?, ?, ?, ?, ?,?,?,?,?,?)");
				
				ps.setInt(1, productStore.product_id);
				ps.setInt(2, productStore.store_id);
				ps.setString(3, productStore.store_alias);
				ps.setInt(4, productStore.status);
				ps.setDouble(5, productStore.price);
				ps.setInt(6,productStore.price_type);
				ps.setInt(7, productStore.quanlity);
				ps.setInt(8, productStore.quantity);
				
				ps.setInt(9, productStore.vat);
				ps.setInt(10, productStore.ship_type);
				ps.setInt(11, productStore.is_new);
				
				ps.setString(12, productStore.description);
				ps.setString(13, productStore.url_link_web);
				
				ps.setInt(14, productStore.ensure_month);
				ps.setInt(15, productStore.origin);
				
				ps.setString(16, productStore.create_user);
				ps.setLong(17, productStore.create_date);
				ps.setString(18, productStore.modify_user);
				ps.setLong(19, productStore.modify_date);
				ps.setLong(20, productStore.modify_date);
				ps.setString(21, productStore.original_link);
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
			System.out.println("Do dai cua description = "+productStore.description.length());
			e.printStackTrace();
		}
		return id;
	}
	
	
	public void updateProductStorePrice(ProductStore productStore) {
		PreparedStatement ps;	
		try {
			Connection connection = C3p0StorePool.getConnection();
			connection.setAutoCommit(false);
			ps = connection
						.prepareStatement("Update  az24_store.products_store set price = ? , price_type=?, vat=? Where id = ?");				
				ps.setDouble(1, productStore.price);
				ps.setInt(2, productStore.price_type);
				ps.setInt(3, productStore.vat);
				ps.setInt(4, productStore.id);
				ps.execute();
				connection.commit();
				connection.setAutoCommit(true);
				C3p0ClassifiedPool.attemptClose(connection);
				C3p0ClassifiedPool.attemptClose(ps);
				
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		
	}
	
	
	public int getProductByProductID(int product_id,int store_id) {
		PreparedStatement ps;
		int id = 0;
		try {
			Connection connection = C3p0StorePool.getConnection();
			ps = connection.prepareStatement(" SELECT id  FROM products WHERE product_id = ? and store_id = ? ");
			ps.setInt(1, product_id);
			ps.setInt(2, store_id);
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next())
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
	
	public List<ProductStore> getProduct(int begin,int store_id) {
		PreparedStatement ps;
		int id = 0;
		List<ProductStore> productstores =  new ArrayList<ProductStore>();
		try {
			Connection connection = C3p0StorePool.getConnection();
			ps = connection.prepareStatement("SELECT	id, product_id,	kit_id,admin_id,store_id,store_alias,STATUS," +
					"is_edit,is_resubmit,price,price_foreign,price_type,quality,quantity,unit,vat,ship_type,ship_cost," +
					"ship_volume,ship_height,ship_width,ship_length,is_ship_volume,is_new,is_hot,is_promotions,	news_promotions," +
					"date_begin_promotions,date_end_promotions,description,url_link_web,ensure_month,origin,number_order,num_of_buy," +
					"info_review,type_account,reason,create_user,create_date,modify_user,modify_date,last_update FROM	az24_store.products_store" +
					" Where id > ? And store_id = ? Order by id asc  LIMIT 1000 ");
			ps.setInt(1, begin);
			ps.setInt(2, store_id);
			ResultSet resultSet = ps.executeQuery();
			ProductStore productStore = null;
			while (resultSet.next())
			{
				productStore = new ProductStore();
				id = resultSet.getInt(1);
				productStore.id = id;
				productStore.price = resultSet.getDouble("price");
				productStore.price_foreign = resultSet.getDouble("price_foreign");
				productStore.price_type = resultSet.getInt("price_type");
				productStore.vat = resultSet.getInt("vat");
				productstores.add(productStore);
			}
			C3p0ClassifiedPool.attemptClose(connection);
			C3p0ClassifiedPool.attemptClose(ps);
			C3p0ClassifiedPool.attemptClose(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return productstores;
	}
	
	public int getMinIdProductStore(int store_id) {
		PreparedStatement ps;
		int id = 0;		
		try {
			Connection connection = C3p0StorePool.getConnection();
			ps = connection.prepareStatement("SELECT min(id) FROM	az24_store.products_store" +
					" Where store_id = ? ");
			ps.setInt(1, store_id);
			ResultSet resultSet = ps.executeQuery();			
			if (resultSet.next())
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
	
	public int getMaxIdProductStore(int store_id) {
		PreparedStatement ps;
		int id = 0;		
		try {
			Connection connection = C3p0StorePool.getConnection();
			ps = connection.prepareStatement("SELECT max(id) FROM	az24_store.products_store" +
					" Where store_id = ? ");
			ps.setInt(1, store_id);
			ResultSet resultSet = ps.executeQuery();			
			if (resultSet.next())
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
}
