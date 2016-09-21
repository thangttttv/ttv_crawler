package com.az24.crawler.model.product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import com.az24.tool.Category;
import com.az24.util.Logger;

public class BaseDao {
	private com.az24.util.Logger logger = new Logger(this.getClass().getName());

	public int saveProduct(Product product, Connection conn) {
		PreparedStatement preStmt = null;
		StringBuffer strSQL = null;
		int id = 0;	
		try {
			conn.setAutoCommit(false);
			strSQL = new StringBuffer(
					" INSERT INTO products (cat_id,cat_id1,cat_id2,cat_id3,cat_id4,cat_id5,name,picture,"
							+ "  is_multi,status,price_from,price_to,create_user,create_date, modify_user,modify_date,original_link,url_rewrite,description)"
							+ "  VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

			preStmt = conn.prepareStatement(strSQL.toString());
			preStmt.setInt(1, product.cat_id);
			preStmt.setInt(2, product.cat_id1);
			preStmt.setInt(3, product.cat_id2);
			preStmt.setInt(4, product.cat_id3);
			preStmt.setInt(5, product.cat_id4);
			preStmt.setInt(6, product.cat_id5);

			preStmt.setString(7, product.name);
			preStmt.setString(8, product.pictrue);
			preStmt.setInt(9, product.is_multi);
			preStmt.setInt(10, product.status);

			preStmt.setDouble(11, product.price_from);
			preStmt.setDouble(12, product.price_to);

			preStmt.setString(13, "thangtt");
			preStmt.setLong(14, System.currentTimeMillis() / 1000);
			preStmt.setString(15, "thangtt");
			preStmt.setLong(16,  System.currentTimeMillis() / 1000);
			
			preStmt.setString(17,product.original_link);
			preStmt.setString(18,product.url_rewrite);
			preStmt.setString(19,product.description);
			preStmt.execute();

			conn.commit();
			conn.setAutoCommit(true);

			String sql = "SELECT LAST_INSERT_ID()";
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				id = resultSet.getInt(1);

		} catch (NoSuchElementException nse) {
			logger.error("save: Error executing >>>" + nse.toString());
		} catch (SQLException se) {
			logger.error("save: Error executing SQL >>>" + strSQL.toString()
					+ se.toString());
		} catch (Exception e) {
			logger.error("save: Error executing >>>" + e.toString());
		}
		return id;
	}

	public int saveProductData(ProductData data, Connection conn) {
		PreparedStatement preStmt = null;
		StringBuffer strSQL = null;

		int id = 0;
		try {
			conn.setAutoCommit(false);
			strSQL = new StringBuffer(
					" INSERT INTO products_data (field_id, product_id, VALUE, is_other )"
							+ " VALUES (?,?,?,?)");

			preStmt = conn.prepareStatement(strSQL.toString());
			preStmt.setInt(1, data.field_id);
			preStmt.setInt(2, data.product_id);
			preStmt.setString(3, data.value);
			preStmt.setInt(4, 0);
			preStmt.execute();

			conn.commit();
			conn.setAutoCommit(true);

			String sql = "SELECT LAST_INSERT_ID()";
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				id = resultSet.getInt(1);

		} catch (NoSuchElementException nse) {
			logger.error("save: Error executing >>>" + nse.toString());
		} catch (SQLException se) {
			logger.error("save: Error executing SQL >>>" + strSQL.toString()
					+ se.toString());
		} catch (Exception e) {
			logger.error("save: Error executing >>>" + e.toString());
		}
		return id;
	}

	public boolean savePicture(ProductImage image,Connection conn) {		
		PreparedStatement preStmt = null;
		StringBuffer strSQL = null;				
		boolean kq = false;
		try {
			conn.setAutoCommit(false);
			long time  =System.currentTimeMillis()/1000;
			strSQL = new StringBuffer(
					" INSERT INTO products_picture  (product_id,name, path, description, kbyte, "
							+ "  is_main,create_user,modify_user,create_date,modify_date,status)"
							+ "  VALUES ("+image.product_id+",'"+image.name.trim()+"','"+image.path.trim()+"','',0,"+image.is_main +
							  "  ,'thangtt','thangtt',"+time+","+time+",1)");
			
			System.out.println(strSQL.toString());
			preStmt = conn.prepareStatement(strSQL.toString());
			/*preStmt.setInt(1, image.product_id);
			preStmt.setString(2, image.name.trim());

			preStmt.setString(3, image.path.trim());
			preStmt.setString(4, "");
			preStmt.setDouble(5, image.kbyte);
			preStmt.setInt(6, image.is_main);

			preStmt.setString(7, "thangtt");
			preStmt.setInt(8, (int) calendar.getTimeInMillis() / 1000);
			preStmt.setString(9, "thangtt");
			preStmt.setInt(10, (int) calendar.getTimeInMillis() / 1000);*/

			kq = preStmt.executeUpdate()>0?true:false;

			conn.commit();
			conn.setAutoCommit(true);
			
			if(kq==false){
				System.out.println(image.name.length());
				System.out.println(image.path.length());
			}
			

		} catch (NoSuchElementException nse) {
			logger.error("save: Error executing >>>" + nse.toString());
		} catch (SQLException se) {
			logger.error("save: Error executing SQL >>>" + strSQL.toString()
					+ se.toString());
		} catch (Exception e) {
			logger.error("save: Error executing >>>" + e.toString());
		}
		return kq;
	}

	public ProductField getField(String name, int extension_id,Connection conn ) {		
		PreparedStatement preStmt = null;
		StringBuffer strSQL = null;
		int id = 0;
		ProductField productField = null;
		try {

			strSQL = new StringBuffer(
					" Select id,type,is_group,name from products_field where name=? And extension_id = ?  And is_group = 0 ");

			preStmt = conn.prepareStatement(strSQL.toString());
			preStmt.setString(1, name);
			preStmt.setInt(2, extension_id);
			ResultSet rs = preStmt.executeQuery();
			if (rs.next())
			{
				productField = new ProductField();
				id = rs.getInt(1);
				productField.id = id;
				productField.type = rs.getInt(2);
				productField.is_group =  rs.getInt(3);
				productField.name = rs.getString(4);
				
			}

		} catch (NoSuchElementException nse) {
			logger.error("save: Error executing >>>" + nse.toString());
		} catch (SQLException se) {
			logger.error("save: Error executing SQL >>>" + strSQL.toString()
					+ se.toString());
		} catch (Exception e) {
			logger.error("save: Error executing >>>" + e.toString());
		}
		return productField;
	}
	
	public List<ProductField>  getField(int extension_id,Connection conn ) {		
		PreparedStatement preStmt = null;
		StringBuffer strSQL = null;
		int id = 0;
		ProductField productField = null;
		List<ProductField> list = new ArrayList<ProductField>();
		try {

			strSQL = new StringBuffer(
					" Select id,type,is_group,name from products_field where  extension_id = ?  And is_group = 0 ");

			preStmt = conn.prepareStatement(strSQL.toString());		
			preStmt.setInt(1, extension_id);
			ResultSet rs = preStmt.executeQuery();
			while (rs.next())
			{
				productField = new ProductField();
				id = rs.getInt(1);
				productField.id = id;
				productField.type = rs.getInt(2);
				productField.is_group =  rs.getInt(3);
				productField.name = rs.getString(4);
				list.add(productField);
			}

		} catch (NoSuchElementException nse) {
			logger.error("save: Error executing >>>" + nse.toString());
		} catch (SQLException se) {
			logger.error("save: Error executing SQL >>>" + strSQL.toString()
					+ se.toString());
		} catch (Exception e) {
			logger.error("save: Error executing >>>" + e.toString());
		}
		return list;
	}
	
	public int getFieldValue(String label, int extension_id,int field_id,Connection conn ) {
		
		PreparedStatement preStmt = null;
		StringBuffer strSQL = null;
		int value = 0;
		
		try {

			strSQL = new StringBuffer(
					" Select value from products_field_value where UPPER(label)=? And extension_id = ? And field_id = ? ");

			preStmt = conn.prepareStatement(strSQL.toString());
			preStmt.setString(1, label);
			preStmt.setInt(2, extension_id);
			preStmt.setInt(3, field_id);
			ResultSet rs = preStmt.executeQuery();
			if (rs.next())			{
				
				value = rs.getInt(1);				
			}

		} catch (NoSuchElementException nse) {
			logger.error("save: Error executing >>>" + nse.toString());
		} catch (SQLException se) {
			logger.error("save: Error executing SQL >>>" + strSQL.toString()
					+ se.toString());
		} catch (Exception e) {
			logger.error("save: Error executing >>>" + e.toString());
		}
		return value;
	}
	
	public List<ProductFieldValue> getFieldValue(int extension_id,int field_id,Connection conn ) {
		
		PreparedStatement preStmt = null;
		StringBuffer strSQL = null;		
		ProductFieldValue productFieldValue = null;
		List<ProductFieldValue> list = new ArrayList<ProductFieldValue>();
		try {

			strSQL = new StringBuffer(
					" Select label,value from products_field_value where  extension_id = ? And field_id = ? ");

			preStmt = conn.prepareStatement(strSQL.toString());		
			preStmt.setInt(1, extension_id);
			preStmt.setInt(2, field_id);
			ResultSet rs = preStmt.executeQuery();
			while (rs.next())			{
				productFieldValue = new ProductFieldValue();			
				productFieldValue.value = rs.getInt(2);
				productFieldValue.label = rs.getString(1);		
				list.add(productFieldValue);
			}

		} catch (NoSuchElementException nse) {
			logger.error("save: Error executing >>>" + nse.toString());
		} catch (SQLException se) {
			logger.error("save: Error executing SQL >>>" + strSQL.toString()
					+ se.toString());
		} catch (Exception e) {
			logger.error("save: Error executing >>>" + e.toString());
		}
		return list;
	}
	
	public List<ProductFieldValue> getFieldValue(int extension_id,Connection conn ) {
		
		PreparedStatement preStmt = null;
		StringBuffer strSQL = null;		
		ProductFieldValue productFieldValue = null;
		List<ProductFieldValue> list = new ArrayList<ProductFieldValue>();
		try {

			strSQL = new StringBuffer(
					" Select label,value from products_field_value where  extension_id = ? order by field_id asc  ");

			preStmt = conn.prepareStatement(strSQL.toString());		
			preStmt.setInt(1, extension_id);
			
			ResultSet rs = preStmt.executeQuery();
			while (rs.next())
			{
				productFieldValue = new ProductFieldValue();			
				productFieldValue.value = rs.getInt(2);
				productFieldValue.label = rs.getString(1);		
				list.add(productFieldValue);
			}

		} catch (NoSuchElementException nse) {
			logger.error("save: Error executing >>>" + nse.toString());
		} catch (SQLException se) {
			logger.error("save: Error executing SQL >>>" + strSQL.toString()
					+ se.toString());
		} catch (Exception e) {
			logger.error("save: Error executing >>>" + e.toString());
		}
		return list;
	}

	public int getExtensionID(int cat_id,Connection conn ) {
		
		PreparedStatement preStmt = null;
		StringBuffer strSQL = null;
		int value = 0;
		
		try {

			strSQL = new StringBuffer(
					" Select extension_id from tbl_category where  id = ? ");

			preStmt = conn.prepareStatement(strSQL.toString());
			preStmt.setInt(1, cat_id);
			ResultSet rs = preStmt.executeQuery();
			if (rs.next())			{
				
				value = rs.getInt(1);				
			}

		} catch (NoSuchElementException nse) {
			logger.error("save: Error executing >>>" + nse.toString());
		} catch (SQLException se) {
			logger.error("save: Error executing SQL >>>" + strSQL.toString()
					+ se.toString());
		} catch (Exception e) {
			logger.error("save: Error executing >>>" + e.toString());
		}
		return value;
	}
	
	public Map<Integer,Integer> getCats(Connection conn ) {
		
		PreparedStatement preStmt = null;
		StringBuffer strSQL = null;
		int value = 0;
		Map<Integer,Integer> cats = new HashMap<Integer,Integer>();
		try {

			strSQL = new StringBuffer(
					" SELECT DISTINCT cat_id FROM products ");

			preStmt = conn.prepareStatement(strSQL.toString());
		
			ResultSet rs = preStmt.executeQuery();
			while(rs.next())
			{
				
				value = rs.getInt(1);	
				cats.put(value,value);
			}

		} catch (NoSuchElementException nse) {
			logger.error("save: Error executing >>>" + nse.toString());
		} catch (SQLException se) {
			logger.error("save: Error executing SQL >>>" + strSQL.toString()
					+ se.toString());
		} catch (Exception e) {
			logger.error("save: Error executing >>>" + e.toString());
		}
		return cats;
	}
	
	public Category getCattegory(Connection conn ) {
		
		PreparedStatement preStmt = null;
		StringBuffer strSQL = null;
		Category category = null;
		try {

			strSQL = new StringBuffer(
					" SELECT id,extension_id,is_multi_sell FROM tbl_category");

			preStmt = conn.prepareStatement(strSQL.toString());
		
			ResultSet rs = preStmt.executeQuery();
			while(rs.next())
			{
				category = new Category();
				category.id = rs.getInt(1);
				category.is_multi_sell =  rs.getInt(2);
			
			}

		} catch (NoSuchElementException nse) {
			logger.error("save: Error executing >>>" + nse.toString());
		} catch (SQLException se) {
			logger.error("save: Error executing SQL >>>" + strSQL.toString()
					+ se.toString());
		} catch (Exception e) {
			logger.error("save: Error executing >>>" + e.toString());
		}
		return category;
	}


	public Map<Integer, Integer> getCategoriesParent(int cat,
			Map<Integer, Integer> holder, Connection conn) throws Exception {
		int id, cat_parent = 0;
		int level = 0;
		ResultSet rs = conn.createStatement()
				.executeQuery("SELECT id,parent_id,level " +
						"FROM tbl_category WHERE id = "	+ cat);
		if (rs.next()) {
			id = rs.getInt(1);
			cat_parent = rs.getInt(2);
			level = rs.getInt(3);
			holder.put(level, id);
		}

		if (cat_parent == 0)
			return holder;
		else
			getCategoriesParent(cat_parent, holder, conn);
		return null;
	}
	
	
}
