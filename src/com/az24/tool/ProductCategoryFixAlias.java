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

import com.az24.util.UTF8Tool;

public class ProductCategoryFixAlias {

	public List<Category> getData() {
		List<Category> listCats = new ArrayList<Category>();
		try {
			PreparedStatement ps = connLog
					.prepareStatement(" SELECT id,name,single_path FROM tbl_category WHERE single_path IS NULL order by id asc");

			ResultSet resultSet = ps.executeQuery();
			int i = 0;
			while (resultSet.next()) {
				Category cate = new Category();
				cate.id = resultSet.getInt(1);
				cate.name = resultSet.getString(2);
				cate.sign_path = resultSet.getString(3);
				listCats.add(cate);
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listCats;

	}

	private void updatFieldData(int id, String single_path) {
		PreparedStatement ps;
		try {

			connLog.setAutoCommit(false);
			ps = connLog
					.prepareStatement("Update tbl_category  SET single_path = '"
							+ single_path + "' WHERE id = " + id);
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
		ProductCategoryFixAlias productCatHit = new ProductCategoryFixAlias();
		productCatHit.openConnection();
		List<Category> cats = productCatHit.getData();
		

		for (Category cat : cats) {
				
				
				String pattern = "\\W";				   
			    Pattern r = Pattern.compile(pattern);
				Matcher m = r.matcher(UTF8Tool.coDau2KoDau(cat.name));
				cat.sign_path = m.replaceAll("-");
				
				System.out.println("name="+UTF8Tool.coDau2KoDau(cat.name));
				System.out.println("id="+cat.id);
				System.out.println("--------------->"+cat.sign_path);
				
				productCatHit.updatFieldData(cat.id, cat.sign_path);

		}

		productCatHit.closeConnection();

	}
}
