package com.az24.tool;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CreateFileCategoryRaoVatXML {

	public static String subcate = "";

	public void getSubCategory(java.sql.Connection conn, int catid)
			throws SQLException {

		ResultSet rs = conn
				.createStatement()
				.executeQuery(
						"SELECT cat_id FROM categories_multi WHERE cat_active = 1 And cat_type ='raovat' And cat_parent_id =  "
								+ catid);
		while (rs.next()) {
			CreateFileCategoryRaoVatXML.subcate += rs.getInt(1) + ",";
			getSubCategory(conn, rs.getInt(1));

		}

	}
	
	public List<Category> getCateRoot(java.sql.Connection conn)	throws SQLException {
		   List<Category> cats = new ArrayList<Category>(); 
		   Category category = null;
		   ResultSet rs = conn
				.createStatement()
				.executeQuery(" SELECT cat_id,cat_name FROM categories_multi WHERE cat_active = 1 And cat_parent_id = 0 ");
			while (rs.next()) {
				category = new Category();
				category.id = rs.getInt(1);
				category.name = rs.getString(2);
				cats.add(category);
			
			}
			return cats;		
		}


	public static void main(String[] args) throws ClassNotFoundException {

		try {

			CreateFileCategoryRaoVatXML crawlerExtracter = new CreateFileCategoryRaoVatXML();
			Class.forName("com.mysql.jdbc.Driver");
			java.sql.Connection connection = DriverManager
					.getConnection(
							"jdbc:mysql://210.211.97.11:3306/raovat2011?autoReconnect=true&characterEncoding=UTF-8",
							"quangpn", "QuangPN2011@!!!*^");
			java.io.File file = new File(
					"src/com/az24/crawler/config/raovat_cate.xml");
			//FileWriter outputStream = new FileWriter(file);
			Writer outputStream = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file), "UTF8"));
			
			List<Category> listcate = crawlerExtracter.getCateRoot(connection);
			StringBuffer stringb = new StringBuffer(
					"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n  <cates>\n");
			for (Category cat : listcate) {			
				stringb.append("\n     <cate ");
				stringb.append(" value=\"" + cat.id + "\"");

				stringb.append(" name=\"" + cat.name + "\" >\n");
				CreateFileCategoryRaoVatXML.subcate="";
				crawlerExtracter.getSubCategory(connection, cat.id);
				stringb.append("            <subcat>" + CreateFileCategoryRaoVatXML.subcate +
						"</subcat>\n     </cate>");

			}
			stringb.append("\n </cates> ");
			outputStream.write(stringb.toString());
			outputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		} catch (SQLException e) {			
			e.printStackTrace();
		}
	}
}
