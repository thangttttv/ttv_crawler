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

public class ProductFieldValueFix {
	
	public List<ProductField> getFields(int extension_id) {
		List<ProductField> listCats = new ArrayList<ProductField>();
		try {
			PreparedStatement ps = connLog
					.prepareStatement(" SELECT 	id, name FROM 	az24_store.products_field Where extension_id  = "+extension_id
							+"  order by id asc  ");
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

	
	public List<ProductFieldValue> getData(int field_id) {
		List<ProductFieldValue> listCats = new ArrayList<ProductFieldValue>();
		try {
			PreparedStatement ps = connLog
					.prepareStatement(" SELECT 	id,field_id,extension_id,label,VALUE,filter_value FROM" +
							" 	az24_store.products_field_value WHERE field_id  = ?  order by id asc  ");
			ps.setInt(1, field_id);
			ResultSet resultSet = ps.executeQuery();
			int i = 0;ProductFieldValue cate = null;
			while (resultSet.next()) {
				cate = new ProductFieldValue();
				cate.id = resultSet.getInt(1);
				cate.label = resultSet.getString("label");				
				listCats.add(cate);
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listCats;

	}

	private void updatFieldData(int id, int value) {
		PreparedStatement ps;
		try {

			connLog.setAutoCommit(false);
			ps = connLog
					.prepareStatement("Update products_field_value  SET filter_value = "
							+ value + " WHERE id = " + id);
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
		ProductFieldValueFix productFieldValueFix = new ProductFieldValueFix();
		productFieldValueFix.openConnection();
		
		List<ProductField> fields = productFieldValueFix.getFields(3160);
		
		for (ProductField productField : fields) {
			
			List<ProductFieldValue> fievaluList = productFieldValueFix.getData(productField.id);
			int value_filter = 100;
			for (ProductFieldValue productData : fievaluList) {
					System.out.println(productData.label);
					productFieldValueFix.updatFieldData(productData.id, value_filter);
					value_filter +=100;
			}
		}
		

		productFieldValueFix.closeConnection();

	}
}
