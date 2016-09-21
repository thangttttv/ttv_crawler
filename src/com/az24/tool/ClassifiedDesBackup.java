package com.az24.tool;

import hdc.util.text.HtmlUtil;
import hdc.util.text.StringUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassifiedDesBackup {
	public static Connection connection;
	public static Connection connection2;
	public static Connection connection_older;
	
	public static Connection connection()
	{
		try {
				Class.forName("com.mysql.jdbc.Driver");				
				connection = DriverManager.getConnection(
						"jdbc:mysql://210.211.97.11:3306/raovat2011?autoReconnect=true&characterEncoding=UTF-8","quangpn","QuangPN2011@!!!*^");
			//	connection = DriverManager.getConnection(
				//		"jdbc:mysql://localhost:3306/raovat2011?autoReconnect=true&characterEncoding=UTF-8","root","");
			} catch (ClassNotFoundException e) {			
				e.printStackTrace();
			} catch (SQLException e) {		
				e.printStackTrace();
			}      
	        
			return connection;
		
	}
	
	public static Connection connection_older()
	{
		try {
				Class.forName("com.mysql.jdbc.Driver");				
				connection_older = DriverManager.getConnection(
						"jdbc:mysql://192.168.1.103:3306/raovat_old?autoReconnect=true&characterEncoding=UTF-8","quangpn","21quang11");
			} catch (ClassNotFoundException e) {			
				e.printStackTrace();
			} catch (SQLException e) {		
				e.printStackTrace();
			}      
	        
			return connection_older;
		
	}
	
	public static Connection connection2()
	{
		try {
				Class.forName("com.mysql.jdbc.Driver");				
				connection2 = DriverManager.getConnection(
						"jdbc:mysql://192.168.1.101:3306/raovat_description?autoReconnect=true&characterEncoding=UTF-8","quangpn","QuangPN2011@!!!*^");
				//connection2 = DriverManager.getConnection(
				//		"jdbc:mysql://localhost:3306/raovat_description?autoReconnect=true&characterEncoding=UTF-8","root","");
			} catch (ClassNotFoundException e) {			
				e.printStackTrace();
			} catch (SQLException e) {		
				e.printStackTrace();
			}      
	        
			return connection2;
		
	}
	
	public List<ClassifiedDes> getData(int last_id) {
		List<ClassifiedDes> listCats = new ArrayList<ClassifiedDes>();
		try {
			PreparedStatement ps = connection
					.prepareStatement(" SELECT 	cla_id,cla_description FROM classified Where cla_id > "+last_id+"  ORDER BY cla_id ASC limit 0,1000 ");

			ResultSet resultSet = ps.executeQuery();
			int i = 0;
			while (resultSet.next()) {
				ClassifiedDes classifiedDes = new ClassifiedDes();
				classifiedDes.id = resultSet.getInt(1);
				classifiedDes.des = resultSet.getString(2);				
				listCats.add(classifiedDes);
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listCats;

	}
	
	public boolean checkData(int id,int id_table) {
		boolean kq = false;
		
		try {
			ClassifiedDesBackup.connection2();
			PreparedStatement ps = connection2
					.prepareStatement(" SELECT 	id FROM classified_description_"+id_table
							+" Where id=  "+id);			
			ResultSet resultSet = ps.executeQuery();			
			if (resultSet.next()) {
				kq = true;
			}
			ClassifiedDesBackup.connection2.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return kq;

	}
	
	public List<ClassifiedDes> getData_older(int last_id) {
		List<ClassifiedDes> listCats = new ArrayList<ClassifiedDes>();
		try {
			connection_older();
			PreparedStatement ps = connection_older
					.prepareStatement(" SELECT 	cla_id,cla_description FROM classified Where cla_id > "+last_id+"  ORDER BY cla_id ASC limit 0,1000 ");

			ResultSet resultSet = ps.executeQuery();
			int i = 0;
			while (resultSet.next()) {
				ClassifiedDes classifiedDes = new ClassifiedDes();
				classifiedDes.id = resultSet.getInt(1);
				classifiedDes.des = resultSet.getString(2);				
				listCats.add(classifiedDes);
				i++;
			}
			connection_older.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listCats;

	}
	
	
	public int count() {
		int i = 0;int count = 0;
		while(i<=999)
		{			
			try {
				PreparedStatement ps = connection2
						.prepareStatement(" SELECT 	count(*) FROM classified_description_"+i+" ");
	
				ResultSet resultSet = ps.executeQuery();			
				if (resultSet.next()) {
					count += resultSet.getInt(1);				
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			i++;
		}
		return count;

	}
	
	
	public List<ClassifiedDes> getDataUser(int last_id) {
		List<ClassifiedDes> listCats = new ArrayList<ClassifiedDes>();
		try {
			/*PreparedStatement ps = connection
					.prepareStatement(" SELECT 	c.cla_id,c.cla_userid,u.username FROM classified c left join forum_user u on c.cla_userid =" +
							" u.userid  Where cla_id > "+last_id+"  ORDER BY cla_id ASC limit 0,1000 ");
			*/
			PreparedStatement ps = connection
			.prepareStatement(" SELECT 	c.cla_id,c.cla_userid,u.username FROM classified c left join forum_user u on c.cla_userid =" +
					" u.userid  Where cla_username IS null ");

			
			
			ResultSet resultSet = ps.executeQuery();
			int i = 0;
			ClassifiedDes classifiedDes = null;
			while (resultSet.next()) {
				classifiedDes = new ClassifiedDes();
				classifiedDes.id = resultSet.getInt("cla_id");
				classifiedDes.userid = resultSet.getInt("cla_userid");
				classifiedDes.username =resultSet.getString("username");
				listCats.add(classifiedDes);
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listCats;

	}
	
	public List<User> getUser(String  userids) {
		List<User> listCats = new ArrayList<User>();
		try {
			PreparedStatement ps = connection
					.prepareStatement(" SELECT 	userid,username FROM forum_user Where userid in ("+userids+") ");

			ResultSet resultSet = ps.executeQuery();
			int i = 0;
			while (resultSet.next()) {
				User classifiedDes = new User();
				classifiedDes.userid = resultSet.getInt(1);
				classifiedDes.username = resultSet.getString(2);				
				listCats.add(classifiedDes);
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listCats;

	}
	
	public void saveAccount(List<ClassifiedDes> datas) {
		PreparedStatement ps;
		try {
			int i = 0;
			connection.setAutoCommit(false);
			while(i<datas.size())
			{
				ps = connection
						.prepareStatement("INSERT INTO classified_description (cla_id,cla_description)" +
								" VALUES (?,?)");
				
				ps.setInt(1, datas.get(i).id);
				ps.setString(2, datas.get(i).des);
				ps.execute();
				if(i%100==0)
					connection.commit();
				i++;
			}
			connection.commit();
			connection.setAutoCommit(true);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	
	public void updateDescription(List<ClassifiedDes> datas) {
		PreparedStatement ps;
		String description = "";
		String arrDes[]=null;
		int length = 50;
		try {
			int i = 0,j=0;
			connection.setAutoCommit(false);
			while(i<datas.size())
			{
				ps = connection
						.prepareStatement("UPDATE  classified SET cla_description = ? " +
								" WHERE cla_id = ? ");
				description = "";
				if(!StringUtil.isEmpty(datas.get(i).des))
				{
					arrDes = HtmlUtil.removeTag(datas.get(i).des).split(" ");
					length = arrDes.length<50? arrDes.length: 50;
					j=0;
					while(j<length)
					{ 
						description +=" "+	arrDes[j];					
						j++;
					}
					
					System.out.println(description);	
					Pattern pattern =  Pattern.compile("\\t\\n");
				 	Matcher matcher = pattern.matcher(description);
				 	description = matcher.replaceAll(" ");
				 	
					ps.setString(1, description.trim());
					ps.setInt(2, datas.get(i).id);
					ps.execute();
				}
				
				if(i%100==0)
					connection.commit();
				i++;
			}
			connection.commit();
			connection.setAutoCommit(true);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	
	public void updateUser(List<ClassifiedDes> datas) {
		PreparedStatement ps;
		try {
			int i = 0;
			connection.setAutoCommit(false);
			while(i<datas.size())
			{
				ps = connection
						.prepareStatement("UPDATE  classified SET cla_username = ? " +
								" WHERE cla_id = ? ");
				
							
				ps.setString(1, StringUtil.isEmpty(datas.get(i).username)?"Khách vãng lai":datas.get(i).username);
				ps.setInt(2, datas.get(i).id);	
				ps.execute();
				if(i%100==0)
					connection.commit();
				i++;
			}
			connection.commit();
			connection.setAutoCommit(true);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	
	public void saveDescription(List<ClassifiedDes> datas) {
		PreparedStatement ps;
		try {
			int i = 0;
			connection2.setAutoCommit(false);
			while(i<datas.size())
			{
			    int idTable = datas.get(i).id%1000;
				ps = connection2
						.prepareStatement("INSERT INTO classified_description_"+idTable+" (id,description)" +
								" VALUES (?,?)");
				ps.setInt(1, datas.get(i).id);
				ps.setString(2, datas.get(i).des);
				ps.execute();
				if(i%100==0)
					connection2.commit();
				i++;
			}
			connection2.commit();
			connection2.setAutoCommit(true);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public void saveDescription(ClassifiedDes classifiedDes) {
		PreparedStatement ps;
		try {
			ClassifiedDesBackup.connection2();
			connection2.setAutoCommit(false);
		    int idTable = classifiedDes.id%1000;
			ps = connection2
					.prepareStatement("INSERT INTO classified_description_"+idTable+" (id,description)" +
							" VALUES (?,?)");
			ps.setInt(1, classifiedDes.id);
			ps.setString(2, classifiedDes.des);
			ps.execute();
			connection2.commit();
			connection2.setAutoCommit(true);
			connection2.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	/*public void createTable() {
		PreparedStatement ps;
		try {
			int i = 10;
			connection2.setAutoCommit(false);
			while(i<1000)
			{
				ps = connection2
						.prepareStatement("CREATE TABLE classified_description_"+i+" (id BIGINT(20) NOT NULL," +
								"  description LONGTEXT, PRIMARY KEY (id)) ENGINE=INNODB DEFAULT CHARSET=utf8");
				ps.execute();
				connection2.commit();
				i++;
			}			
			connection2.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}*/
	
	/*public void dropTable() {
		PreparedStatement ps;
		try {
			int i = 0;
			connection.setAutoCommit(false);
			while(i<1000)
			{
				ps = connection
						.prepareStatement("DROP TABLE classified_description_"+i);
				ps.execute();
				connection.commit();
				i++;
			}			
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}*/
	
	
	public void deleteTable() {
		PreparedStatement ps;
		try {
			int i = 0;
			connection2.setAutoCommit(false);
			while(i<1000)
			{
				ps = connection2
						.prepareStatement("delete from classified_description_"+i);
				ps.execute();
				connection2.commit();
				i++;
			}			
			connection2.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public static void main(String[] args) {
		ClassifiedDesBackup classifiedDesBackup = new ClassifiedDesBackup();
		classifiedDesBackup.connection2();
		System.out.println(classifiedDesBackup.count());
	}
	public static void main_1(String[] args) throws SQLException, InterruptedException {
		int last_id = 0;
		ClassifiedDesBackup classifiedDesBackup = new ClassifiedDesBackup();
		List<ClassifiedDes>  datas = null;
		last_id=19504;
	
		//while(last_id<2161190)
		//{
			ClassifiedDesBackup.connection();
			
			System.out.println(last_id);
			
			datas = classifiedDesBackup.getDataUser(last_id);

			if(datas.size()>0)
			{
				classifiedDesBackup.updateUser(datas);
				last_id = datas.get(datas.size()-1).id;
			}
			
			ClassifiedDesBackup.connection.close();
			Thread.sleep(1);
		//}
	}
	
	public static void main_des(String[] args) throws SQLException, InterruptedException {
		int last_id = 0;
		ClassifiedDesBackup classifiedDesBackup = new ClassifiedDesBackup();
		
		//last_id = Integer.parseInt(args[0]);
		List<ClassifiedDes>  datas = null;
		last_id=0;
		while(last_id<2161290)
		{
			ClassifiedDesBackup.connection();
			System.out.println(last_id);
			datas = classifiedDesBackup.getData(last_id);
			if(datas.size()>0)
			{
				classifiedDesBackup.updateDescription(datas);
				last_id = datas.get(datas.size()-1).id;
			}
			
			ClassifiedDesBackup.connection.close();
			Thread.sleep(1);
		}
		
	}
	
	public static void main_copydescription(String[] args) throws SQLException, InterruptedException {
		int last_id = 0;
		ClassifiedDesBackup classifiedDesBackup = new ClassifiedDesBackup();
		
		//last_id = Integer.parseInt(args[0]);
		List<ClassifiedDes>  datas = null;
		last_id=2145283;
		while(last_id<2148283)
		{
			ClassifiedDesBackup.connection();
			ClassifiedDesBackup.connection2();
			System.out.println(last_id);
			datas = classifiedDesBackup.getData(last_id);
			if(datas.size()>0)
			{
				classifiedDesBackup.saveDescription(datas);
				last_id = datas.get(datas.size()-1).id;
			}
			ClassifiedDesBackup.connection2.close();
			ClassifiedDesBackup.connection.close();
			Thread.sleep(1);
		}
		
	}
	
	public static void main_bk(String[] args) throws SQLException, InterruptedException {
		int last_id = 0;
		ClassifiedDesBackup classifiedDesBackup = new ClassifiedDesBackup();
		
		//last_id = Integer.parseInt(args[0]);
		List<ClassifiedDes>  datas = null;
		last_id=0;
		int i = 0;
		while(last_id<2161156)
		{
		
			System.out.println("Last Cla_ID="+last_id);
			
			datas = classifiedDesBackup.getData_older(last_id);
			if(datas.size()>0)
			{
				for (ClassifiedDes classifiedDes : datas) {
					int id_table = classifiedDes.id%1000;
					System.out.println(classifiedDes.id);
					if(!classifiedDesBackup.checkData(classifiedDes.id, id_table))
					{
						
						classifiedDesBackup.saveDescription(classifiedDes);
						i++;
						System.out.println("Dong Bo Cla Thu "+i+" Cla_id = "+classifiedDes.id);
						
					}
				}
				
				last_id = datas.get(datas.size()-1).id;
			}
			
			Thread.sleep(1);
		
		}
		
	}
	
	
	
	
	
	
	
}
