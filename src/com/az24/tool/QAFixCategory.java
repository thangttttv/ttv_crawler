package com.az24.tool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class QAFixCategory {
	
	public Map<Integer, Integer> getCategoriesParent(int cat,
			Map<Integer, Integer> holder,Connection conn) throws Exception {
		int id, cat_parent =0;int level=0;
		ResultSet rs = conn.createStatement().executeQuery(
				"SELECT id,id_parent,level FROM qa_category WHERE id = "
						+ cat);
		if(rs.next())
		{
		 id = 	rs.getInt(1);
		 cat_parent = rs.getInt(2);
		 level = rs.getInt(3);
		 holder.put(level, id);
		}
		rs.close();
		rs = null;
		if (cat_parent == 0)
			return holder;
		else
			getCategoriesParent(cat_parent, holder,conn);
		return null;
	}
	
	public static Connection getConnection()
	{
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
		    conn= DriverManager.getConnection(
				"jdbc:mysql://192.168.1.101:3306/qa?characterEncoding=UTF-8","synuser","SynUser2011");
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		} catch (SQLException e) {			
			e.printStackTrace();
		}		
		return conn;
		
	}
	
	public static void main(String[] args) {
		QAFixCategory qFixCategory = new QAFixCategory();
		try {
			//String sql_count ="select count(*) from qa_question where id_category = 275";
			String sql_count ="select count(*) from qa_question";			
			ResultSet rs = getConnection().createStatement().executeQuery(sql_count);
			int count =0;
			if(rs.next())
			 count = 	rs.getInt(1);
			rs.close();
			
			int item = 10000;
			String sql_update ="UPDATE qa_question SET  id_cat_parent0=?,id_cat_parent1=?,id_cat_parent2=? Where id = ? ";
			String sql_questions ="select id,id_category from qa_question  order by id desc limit ?, "+item;
			
			int p = 1,i=0;
			int totalPage = count/item;
			totalPage +=count%item>0?1:0;
			
			Map<Integer, Integer> holder = null;
			PreparedStatement pupdate =null,pStatement=null;
			
			
			ResultSet set =null;
			System.out.println("TotalPage="+totalPage);
			while(p<=totalPage)
			{
				Connection conn = getConnection();
				pStatement = conn.prepareStatement(sql_questions);
				pStatement.setInt(1, (p-1)*item);
				set = pStatement.executeQuery();
				i=0;
				
				conn.setAutoCommit(false);
				while(set.next())
				{
					int id=set.getInt(1);
					int cat=set.getInt(2);
					
					holder = new HashMap<Integer, Integer>();
					qFixCategory.getCategoriesParent(cat, holder, conn);
					
					pupdate = conn.prepareStatement(sql_update);
					pupdate.setInt(1, holder.get(0));
					if(holder.get(1)!=null) pupdate.setInt(2, holder.get(1)); else pupdate.setInt(2,0);
					if(holder.get(2)!=null) pupdate.setInt(3, holder.get(2)); else pupdate.setInt(3,0);
					pupdate.setInt(4, id);
					//pupdate.executeUpdate();
					
					pupdate.addBatch();
					
					if(i%500==0){pupdate.executeBatch();conn.commit();}
					
											
					//pupdate.close();
					holder=null;
					i++;
				}	
				
				if(pupdate!=null){pupdate.executeBatch();conn.commit();pupdate.close();}
				pStatement.close();
				conn.setAutoCommit(true);	
				conn.close();
				System.out.println("Page="+p);
				p++;
			}
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
