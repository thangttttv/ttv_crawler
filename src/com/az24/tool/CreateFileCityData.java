package com.az24.tool;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.az24.util.UTF8Tool;

public class CreateFileCityData {
	public static void main(String[] args) {
		 	String driver = "com.mysql.jdbc.Driver";
		    Connection con;
			try {
				Class.forName(driver);
				con = DriverManager.getConnection("jdbc:mysql://localhost/az24?user=root&password=");
				StringBuilder b = new StringBuilder() ;
				    b.append("SELECT cit_id,cit_name,cit_parent_id FROM city WHERE cit_parent_id =0 ") ;
				    String city = null ;
				    String query = b.toString() ;
				    Statement st = con.createStatement() ;
				    ResultSet rs = st.executeQuery(query) ;
				    Map<String, Integer> map = new HashMap<String, Integer>() ;
				    int idx = 0 ;
				    while(rs.next()) {
				      city =UTF8Tool.coDau2KoDau(rs.getString(2).trim().toLowerCase().replaceAll(" ", "")); 
				      int id = rs.getInt(1) ;
				      map.put(city, id) ;
				      idx++ ;
				    }
				    ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("src/com/az24/crawler/config/user.dat", false)) ;
				    os.writeObject(map) ;
				    ObjectInputStream oi = new ObjectInputStream(new FileInputStream("src/com/az24/crawler/config/user.dat"));
				    map  = (Map<String, Integer>)oi.readObject();
				    Iterator<Integer> iterator = map.values().iterator();
				    while(iterator.hasNext())
				    {
				    	System.out.println(iterator.next());
				    }
				    Iterator<String> iterator2 =  map.keySet().iterator();
				    while(iterator2.hasNext())
				    {
				    	System.out.println(iterator2.next());
				    }
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   
	}
}
