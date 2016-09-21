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

public class ProductDataField22356  {
	
	public List<ProductData> getData() {
		List<ProductData> listCats = new ArrayList<ProductData>();
		try {
			PreparedStatement ps = connLog
					.prepareStatement(" SELECT * FROM products_data WHERE field_id = 22356 ORDER BY id DESC  ");

			ResultSet resultSet = ps.executeQuery();
			int i = 0;
			while (resultSet.next()) {
				ProductData cate = new ProductData();
				cate.id = resultSet.getInt(1);
				cate.field_id = resultSet.getInt(2);
				cate.product_id = resultSet.getInt(3);
				cate.value = resultSet.getString(4);
				listCats.add(cate);
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listCats;

	}

	
	private void updatFieldData(int id,String value) {
		PreparedStatement ps;
		try {
			
			connLog.setAutoCommit(false);
			ps = connLog.prepareStatement("Update products_data  SET value = '"+value+"' WHERE id = "+id);		
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
		ProductDataField22356 productCatHit = new ProductDataField22356();
		productCatHit.openConnection();
		List<ProductData> cats = productCatHit.getData();
		
		String REGEX = "";
	    String INPUT = "";
	    Pattern pattern;    
	    Matcher matcher;
	    
		for (ProductData productData : cats) {
			
			/*String value = "<a href=\""+productData.value+"\" target=\"_blank\">Chi tiết</a>";
			productCatHit.updatFieldData(productData.id, value);*/
			
			pattern = Pattern.compile("<a.*href=\"Chi tiết\".*");
		    matcher = pattern.matcher(productData.value.trim());	 
		 
		    boolean kq = matcher.find();
			if(kq)
			{
				
				System.out.println(productData.value);	
				String value = "";
				System.out.println("id="+productData.id);
				System.out.println("--------------->value="+value);
				productCatHit.updatFieldData(productData.id, value);
			}
		}
		
		productCatHit.closeConnection();
		
	}
}
