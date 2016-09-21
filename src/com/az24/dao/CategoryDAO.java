package com.az24.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.az24.crawler.store.Category;
import com.az24.db.pool.C3p0ClassifiedPool;
import com.az24.db.pool.C3p0StorePool;

public class CategoryDAO {
	
	public Category getCate(int id) {
		PreparedStatement ps;		
		Category category = null;
		try {
			Connection connection = C3p0StorePool.getConnection();
			ps = connection.prepareStatement("SELECT id,name,extension_id,is_multi_sell FROM tbl_category " +
					"  WHERE id  = ? ");
			ps.setInt(1, id);
			ResultSet resultSet =	ps.executeQuery();
			if(resultSet.next())
			{
				category = new Category();
				category.id = resultSet.getInt(1);
				category.name = resultSet.getString(2);
				category.extension_id = resultSet.getInt(3);
				category.is_multi_sell = resultSet.getInt(4);
			}
			C3p0ClassifiedPool.attemptClose(connection);
			C3p0ClassifiedPool.attemptClose(ps);
			C3p0ClassifiedPool.attemptClose(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return category;
	}
	
	public Map<Integer, Integer> getCategoriesParent(int cat,
			Map<Integer, Integer> holder) throws Exception {
		int id, cat_parent = 0;
		int level = 0;
		Connection connection = C3p0StorePool.getConnection();
		ResultSet rs = connection.createStatement()
				.executeQuery("SELECT id,parent_id,level " +
						"FROM tbl_category WHERE id = "	+ cat);
		if (rs.next()) {
			id = rs.getInt(1);
			cat_parent = rs.getInt(2);
			level = rs.getInt(3);
			holder.put(level, id);
		}

		if (cat_parent == 0){
			C3p0ClassifiedPool.attemptClose(connection);
			return holder;
		}
		else
			getCategoriesParent(cat_parent, holder);
		C3p0ClassifiedPool.attemptClose(connection);
		return null;
	}
	
	
}
