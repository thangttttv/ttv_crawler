package com.az24.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.az24.crawler.store.ProductStoreCat;
import com.az24.db.pool.C3p0ClassifiedPool;
import com.az24.db.pool.C3p0StorePool;

public class ProductStoreCatTempDAO {
	public int saveProductStoreCate(ProductStoreCat productStoreCat) {
		PreparedStatement ps;
		int id = 0;
		try {
			Connection connection = C3p0StorePool.getConnection();
			connection.setAutoCommit(false);
			ps = connection
						.prepareStatement("INSERT INTO az24_store.products_store_cat_temp ( product_store_id,product_id,	store_id," +
								" store_cat_id1, store_cat_id2 ) VALUES (?,	?,	?," +
								" ?, ?)");
				
				ps.setInt(1, productStoreCat.product_store_id);
				ps.setInt(2, productStoreCat.product_id);
				ps.setInt(3, productStoreCat.store_id);
				ps.setInt(4, productStoreCat.store_cat_id1);
				ps.setInt(5,productStoreCat.store_cat_id2);
				
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
}
