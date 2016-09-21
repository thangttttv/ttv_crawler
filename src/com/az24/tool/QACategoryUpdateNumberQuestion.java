package com.az24.tool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class QACategoryUpdateNumberQuestion {

	public Map<Integer, Integer> getCategoriesParent(int cat,
			Map<Integer, Integer> holder, Connection conn) throws Exception {
		int id, cat_parent = 0;
		int level = 0;
		ResultSet rs = conn.createStatement().executeQuery(
				"SELECT id,id_parent,level FROM qa_category WHERE id = " + cat);
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

	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager
					.getConnection(
							"jdbc:mysql://192.168.1.101:3306/qa?characterEncoding=UTF-8",
							"synuser", "SynUser2011");

			String sql_cat = "select id,number_question,level from qa_category order by id desc";
			String sql_update = "UPDATE qa_category SET  number_question=? Where id = ? ";
			String sql_count = "select count(id) from qa_question where  ";

			int i = 0;

			PreparedStatement pupdate = null, pStatement = null, pCount = null;

			pupdate = conn.prepareStatement(sql_update);
			ResultSet set = null;

			pStatement = conn.prepareStatement(sql_cat);
			set = pStatement.executeQuery();
			i = 0;
			conn.setAutoCommit(false);
			int number = 0;
			while (set.next()) {
				sql_count="";
				int id = set.getInt(1);
				int number_older = set.getInt(2);
				int level = set.getInt(3);

				// get count
				switch (level) {
				case 0:
					sql_count = "select count(id) from qa_question where id_cat_parent0 =   "
							+ id;
					break;
				case 1:
					sql_count = "select count(id) from qa_question where id_cat_parent1 =   "
							+ id;
					break;
				case 2:
					sql_count = "select count(id) from qa_question where id_cat_parent2 =   "
							+ id;
					break;
				default:
					
					break;
				}
				if(sql_count=="") continue;
				pCount = conn.prepareStatement(sql_count);
				ResultSet resultSet = pCount.executeQuery();
				if (resultSet.next())
					number = resultSet.getInt(1);
				// set para
				pupdate.setInt(1, number);
				pupdate.setInt(2, id);
				pupdate.addBatch();

				if (i % 30 == 0) {
					pupdate.executeBatch();
					conn.commit();
				}
				i++;

				System.out.println(id + "=" + number_older + "-->" + number);
			}
			
			pupdate.executeBatch();
			conn.commit();
			conn.setAutoCommit(true);

			if (pStatement != null)
				pStatement.close();
			if (pupdate != null)
				pupdate.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
