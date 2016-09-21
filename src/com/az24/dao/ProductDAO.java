package com.az24.dao;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.util.html.parser.XPathReader;
import hdc.util.text.StringUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathConstants;

import org.apache.http.HttpResponse;

import com.az24.crawler.model.product.Product;
import com.az24.crawler.model.product.ProductData;
import com.az24.crawler.model.product.ProductField;
import com.az24.crawler.model.product.ProductFieldValue;
import com.az24.crawler.model.product.ProductImage;
import com.az24.crawler.store.ProductPicture;
import com.az24.db.pool.C3p0ClassifiedPool;
import com.az24.db.pool.C3p0StorePool;
import com.az24.test.HttpClientUtil;
import com.az24.util.Logger;
import com.az24.util.UTF8Tool;

public class ProductDAO {

	private com.az24.util.Logger logger = new Logger(this.getClass().getName());
	
	
	public int updateImageMain(int product_id,String picture) {
		PreparedStatement ps;
		int id = 0;
		try {
			Connection connection = C3p0StorePool.getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(" Update products set picture = ? where id = ? ");
			ps.setString(1, picture.trim());
			ps.setInt(2, product_id);
			ps.execute();	
			connection.commit();
			connection.setAutoCommit(true);
			C3p0ClassifiedPool.attemptClose(connection);
			C3p0ClassifiedPool.attemptClose(ps);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	public int deletePricture(int product_id) {
		PreparedStatement ps;
		int id = 0;
		try {
			Connection connection = C3p0StorePool.getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(" DELETE from products_picture where product_id = ? ");			
			ps.setInt(1, product_id);
			ps.execute();	
			connection.commit();
			connection.setAutoCommit(true);
			C3p0ClassifiedPool.attemptClose(connection);
			C3p0ClassifiedPool.attemptClose(ps);		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}

	
	public List<Product> getProducts(String sql)
	{
		PreparedStatement ps;
		List<Product> list = new ArrayList<Product>();
		try {
			Connection connection = C3p0StorePool.getConnection();
			ps = connection
					.prepareStatement(sql);
			
			ResultSet resultSet = ps.executeQuery();
			Product product = null;
			
			while (resultSet.next())
			{
				product = new Product();
				product.name = resultSet.getString("name");
				product.id = resultSet.getInt("id");
				product.original_link = resultSet.getString("original_link");
				list.add(product);
			}
			C3p0ClassifiedPool.attemptClose(connection);
			C3p0ClassifiedPool.attemptClose(ps);
			C3p0ClassifiedPool.attemptClose(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public int getProductByName(String name,int cat_id) {
		PreparedStatement ps;
		int id = 0;
		try {
			Connection connection = C3p0StorePool.getConnection();
			ps = connection.prepareStatement(" SELECT id  FROM products WHERE trim(name) = ? And cat_id = ? ");
			ps.setString(1, name.trim());
			ps.setInt(2, cat_id);
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
	
	public Product getProductById(int id) {
		PreparedStatement ps;
		Product product = null;
		try {
			Connection connection = C3p0StorePool.getConnection();
			ps = connection.prepareStatement(" SELECT id,name,price_from,price_to  FROM products WHERE id = ? ");		
			ps.setInt(1, id);
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next())
			{
				id = resultSet.getInt(1);
				product = new Product();
				product.id = id;
				product.name = resultSet.getString(2);
				product.price_from = resultSet.getDouble("price_from");
				product.price_to = resultSet.getDouble("price_to");
			}
			C3p0ClassifiedPool.attemptClose(connection);
			C3p0ClassifiedPool.attemptClose(ps);
			C3p0ClassifiedPool.attemptClose(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return product;
	}

	public int getProductStoreByName(int product_id,int store_id) {
		PreparedStatement ps;
		int id = 0;
		try {
			Connection connection = C3p0StorePool.getConnection();
			ps = connection.prepareStatement(" SELECT id  FROM products_store WHERE product_id = ? And store_id = ? ");
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
	
	public List<Product> getProductNameIsNull() {
		PreparedStatement ps;
		List<Product> list = new ArrayList<Product>();
		try {
			Connection connection = C3p0StorePool.getConnection();
			ps = connection
					.prepareStatement("SELECT id,name,original_link  FROM products WHERE NAME IS NULL OR NAME ='' ");
			
			ResultSet resultSet = ps.executeQuery();
			Product product = null;
			
			while (resultSet.next())
			{
				product = new Product();
				product.name = resultSet.getString("name");
				product.id = resultSet.getInt("id");
				product.original_link = resultSet.getString("original_link");
				list.add(product);
			}
			C3p0ClassifiedPool.attemptClose(connection);
			C3p0ClassifiedPool.attemptClose(ps);
			C3p0ClassifiedPool.attemptClose(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<Product> getProductUrlRewriterIsNull() {
		PreparedStatement ps;
		List<Product> list = new ArrayList<Product>();
		try {
			Connection connection = C3p0StorePool.getConnection();
			ps = connection
					.prepareStatement("SELECT id,name,original_link  FROM products WHERE url_rewrite IS NULL OR url_rewrite ='' ");
			
			ResultSet resultSet = ps.executeQuery();
			Product product = null;
			
			while (resultSet.next())
			{
				product = new Product();
				product.name = resultSet.getString("name");
				product.id = resultSet.getInt("id");
				product.original_link = resultSet.getString("original_link");
				list.add(product);
			}
			C3p0ClassifiedPool.attemptClose(connection);
			C3p0ClassifiedPool.attemptClose(ps);
			C3p0ClassifiedPool.attemptClose(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public boolean checkModel(int product_id) {
		PreparedStatement ps;
		boolean kq = false;
		try {
			Connection connection = C3p0StorePool.getConnection();
			ps = connection
					.prepareStatement("SELECT id FROM products WHERE id = ? ");
			ps.setInt(1, product_id);
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next())
				kq = true;
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return kq;
	}

	public int saveProductData(ProductData data) {
		PreparedStatement preStmt = null;
		StringBuffer strSQL = null;

		int id = 0;
		try {
			Connection conn = C3p0StorePool.getConnection();
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
			C3p0ClassifiedPool.attemptClose(conn);
			C3p0ClassifiedPool.attemptClose(preStmt);
			C3p0ClassifiedPool.attemptClose(resultSet);
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

	public int saveProduct(Product product) {
		PreparedStatement preStmt = null;
		StringBuffer strSQL = null;
		int id = 0;
		try {
			Connection conn = C3p0StorePool.getConnection();
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
			preStmt.setLong(16, System.currentTimeMillis() / 1000);

			preStmt.setString(17, product.original_link);
			preStmt.setString(18, product.url_rewrite);
			preStmt.setString(19, product.description);
			preStmt.execute();

			conn.commit();
			conn.setAutoCommit(true);

			String sql = "SELECT LAST_INSERT_ID()";
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				id = resultSet.getInt(1);
			C3p0ClassifiedPool.attemptClose(conn);
			C3p0ClassifiedPool.attemptClose(preStmt);
			C3p0ClassifiedPool.attemptClose(resultSet);
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
	
	public int updatePrice(Product product) {
		PreparedStatement preStmt = null;
		StringBuffer strSQL = null;
		int id = 0;
		try {
			Connection connection = C3p0StorePool.getConnection();
			connection.setAutoCommit(false);
			strSQL = new StringBuffer(
					" update  products set price_to = ?, price_from = ? where id = ? ");

			preStmt = connection.prepareStatement(strSQL.toString());
			preStmt.setDouble(1, product.price_to);
			preStmt.setDouble(2, product.price_from);
			preStmt.setInt(3, product.id);
			preStmt.execute();
			connection.commit();
			connection.setAutoCommit(true);
			C3p0ClassifiedPool.attemptClose(connection);
			C3p0ClassifiedPool.attemptClose(preStmt);
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

	
	public int updateName(Product product) {
		PreparedStatement preStmt = null;
		StringBuffer strSQL = null;
		int id = 0;
		try {
			Connection conn = C3p0StorePool.getConnection();
			conn.setAutoCommit(false);
			strSQL = new StringBuffer(
					" update  products set name = ?, url_rewrite = ?,modify_date=? where id = ? ");

			preStmt = conn.prepareStatement(strSQL.toString());
			preStmt.setString(1, product.name);
			preStmt.setString(2, product.url_rewrite);
			preStmt.setLong(3, Calendar.getInstance().getTimeInMillis()/1000);
			preStmt.setInt(4, product.id);
			preStmt.execute();
			conn.commit();
			conn.setAutoCommit(true);
			C3p0ClassifiedPool.attemptClose(conn);
			C3p0ClassifiedPool.attemptClose(preStmt);
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

	public boolean savePicture(ProductImage image) {
		PreparedStatement preStmt = null;
		StringBuffer strSQL = null;
		boolean kq = false;
		try {
			Connection conn = C3p0StorePool.getConnection();
			conn.setAutoCommit(false);
			long time = System.currentTimeMillis() / 1000;
			strSQL = new StringBuffer(
					" INSERT INTO products_picture  (product_id,name, path, description, kbyte, "
							+ "  is_main,create_user,modify_user,create_date,modify_date,status)"
							+ "  VALUES (" + image.product_id + ",'"
							+ image.name.trim() + "','" + image.path.trim()
							+ "','',0," + image.is_main
							+ "  ,'thangtt','thangtt'," + time + "," + time
							+ ",1)");
			
			preStmt = conn.prepareStatement(strSQL.toString());
			kq = preStmt.executeUpdate() > 0 ? true : false;
			conn.commit();conn.setAutoCommit(true);
			if (kq == false) {
				System.out.println(image.name.length());
				System.out.println(image.path.length());
			}
			C3p0ClassifiedPool.attemptClose(conn);
			C3p0ClassifiedPool.attemptClose(preStmt);
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

	public List<ProductFieldValue> getFieldValue(int extension_id, int field_id) {

		PreparedStatement preStmt = null;
		StringBuffer strSQL = null;
		ProductFieldValue productFieldValue = null;
		List<ProductFieldValue> list = new ArrayList<ProductFieldValue>();
		try {
			Connection conn = C3p0StorePool.getConnection();
			strSQL = new StringBuffer(
					" Select label,value from products_field_value where  extension_id = ? And field_id = ? ");

			preStmt = conn.prepareStatement(strSQL.toString());
			preStmt.setInt(1, extension_id);
			preStmt.setInt(2, field_id);
			ResultSet rs = preStmt.executeQuery();
			while (rs.next()) {
				productFieldValue = new ProductFieldValue();
				productFieldValue.label = rs.getString(1);
				productFieldValue.value = rs.getLong(2);			
				list.add(productFieldValue);
			}
			C3p0ClassifiedPool.attemptClose(conn);
			C3p0ClassifiedPool.attemptClose(preStmt);
			C3p0ClassifiedPool.attemptClose(rs);
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

	public List<ProductFieldValue> getFieldValue(int extension_id) {

		PreparedStatement preStmt = null;
		StringBuffer strSQL = null;
		ProductFieldValue productFieldValue = null;
		List<ProductFieldValue> list = new ArrayList<ProductFieldValue>();
		try {
			Connection conn = C3p0StorePool.getConnection();
			strSQL = new StringBuffer(
					" Select label,value from products_field_value where  extension_id = ? order by field_id asc  ");

			preStmt = conn.prepareStatement(strSQL.toString());
			preStmt.setInt(1, extension_id);

			ResultSet rs = preStmt.executeQuery();
			while (rs.next()) {
				productFieldValue = new ProductFieldValue();
				productFieldValue.value = rs.getLong(2);
				productFieldValue.label = rs.getString(1);
				list.add(productFieldValue);
			}
			C3p0ClassifiedPool.attemptClose(conn);
			C3p0ClassifiedPool.attemptClose(preStmt);
			C3p0ClassifiedPool.attemptClose(rs);
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
	
	
	public List<ProductField>  getField(int extension_id) {		
		PreparedStatement preStmt = null;
		StringBuffer strSQL = null;
		int id = 0;
		ProductField productField = null;
		List<ProductField> list = new ArrayList<ProductField>();
		try {
			Connection conn = C3p0StorePool.getConnection();
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
			C3p0ClassifiedPool.attemptClose(conn);
			C3p0ClassifiedPool.attemptClose(preStmt);
			C3p0ClassifiedPool.attemptClose(rs);
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
	
	
	public List<Product> getProductByStoreID(int store_id) {
		PreparedStatement ps;
		int id = 0;
		List<Product> products = new ArrayList<Product>();
		try {
			Connection connection = C3p0StorePool.getConnection();
			ps = connection.prepareStatement(" SELECT product_id,original_link  FROM products_store_temp WHERE   store_id = ?  ");
			ps.setInt(1, store_id);
			ResultSet resultSet = ps.executeQuery();
			Product product =  null;
			while (resultSet.next())
			{
				product = new Product();
				id = resultSet.getInt("product_id");
				product.id = id;				
				product.original_link = resultSet.getString("original_link");
				System.out.println(product.original_link);
				products.add(product);
			}
			C3p0ClassifiedPool.attemptClose(connection);
			C3p0ClassifiedPool.attemptClose(ps);
			C3p0ClassifiedPool.attemptClose(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return products;
	}
	
	
	public List<ProductPicture> getPictureProduct(int product_id) {
		PreparedStatement ps;
		int id = 0;
		List<ProductPicture> products = new ArrayList<ProductPicture>();
		try {
			Connection connection = C3p0StorePool.getConnection();
			ps = connection.prepareStatement(" SELECT 	id,	product_id, name, path, description, kbyte, is_main, " +
					" STATUS,create_user,create_date,modify_user,modify_date FROM products_picture_temp Where  product_id = ? ");
			ps.setInt(1, product_id);
			ResultSet resultSet = ps.executeQuery();
			ProductPicture product =  null;
			while (resultSet.next())
			{
				product = new ProductPicture();
				id = resultSet.getInt(1);
				product.id = id;
				product.name = resultSet.getString("name");
				product.path = resultSet.getString("path");
				product.create_date = resultSet.getLong("create_date");
				products.add(product);
			}
			C3p0ClassifiedPool.attemptClose(connection);
			C3p0ClassifiedPool.attemptClose(ps);
			C3p0ClassifiedPool.attemptClose(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return products;
	}
	
	public static void main(String[] args) throws Exception {
		ProductDAO productDAO = new  ProductDAO();
		List<Product> list = productDAO.getProductUrlRewriterIsNull();
		for (Product product : list) {
			HttpClientImpl client = new HttpClientImpl();
			HttpResponse res = client.fetch(product.original_link);
			HttpClientUtil.printResponseHeaders(res);
			String html = HttpClientUtil.getResponseBody(res);

			XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
			// Lay Thong Tin San Pham
			String xpath_name = "//div[@class='product_detail product_detail_v2']/div[2]/h1[1]/text()";
			String name = (String) reader.read(xpath_name, XPathConstants.STRING);
			product.name = name;
			System.out.println("Name=" + name);
			String name_pattern = "\\W";
			Pattern pattern = Pattern.compile(name_pattern);
			name = UTF8Tool.coDau2KoDau(product.name).trim();
			if(StringUtil.isEmpty(name))return;
			Matcher matcher = pattern.matcher(name);
			String url_rewrite = matcher.replaceAll("_");
			product.url_rewrite = url_rewrite;
			productDAO.updateName(product);
		}
	}
}
