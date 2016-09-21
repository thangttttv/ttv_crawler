package com.az24.tool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductFieldFix {

	public List<ProductField> getData() {
		List<ProductField> listCats = new ArrayList<ProductField>();
		try {
			PreparedStatement ps = connLog
					.prepareStatement(" SELECT 	id, name FROM 	az24_store.products_field order by id asc  ");

			ResultSet resultSet = ps.executeQuery();
			int i = 0;
			while (resultSet.next()) {
				ProductField cate = new ProductField();
				cate.id = resultSet.getInt(1);
				cate.name = resultSet.getString(2);
				listCats.add(cate);
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listCats;

	}

	private void updatFieldData(int id, String value) {
		PreparedStatement ps;
		try {

			connLog.setAutoCommit(false);
			ps = connLog
					.prepareStatement("Update products_field  SET name = '"
							+ value + "' WHERE id = " + id);
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
		ProductFieldFix productCatHit = new ProductFieldFix();
		productCatHit.openConnection();
		List<ProductField> cats = productCatHit.getData();
		Pattern pattern;
		Matcher matcher;

		for (ProductField productData : cats) {
			System.out.println(productData.name);
			

			pattern = Pattern.compile(":$");
			matcher = pattern.matcher(productData.name.trim());	 
			
			boolean kq = matcher.find();
			if(kq)
			{
				String value = productData.name.trim().replaceAll(":", "");
				System.out.println("id="+productData.id);
				System.out.println("--------------->"+value);
				productCatHit.updatFieldData(productData.id, value);
			}
		}

		productCatHit.closeConnection();

	}
}
