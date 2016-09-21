package com.az24.tool;

import hdc.crawler.ExtractEntity;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;

import jdbm.PrimaryHashMap;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

public class BackUpEntity {
	protected static RecordManager extracterManager;
	protected static PrimaryHashMap<String, ExtractEntity> extractPrimary;

	
	public static  void backup(Connection conn,String table, String source,String file_store) throws IOException {
		extracterManager = RecordManagerFactory.createRecordManager(file_store
				+ "/extract");
		extractPrimary = extracterManager.hashMap("extract");
		
		Iterator<ExtractEntity> iterator = extractPrimary.values().iterator();
		String sql ="INSERT INTO "+table+"(	result_id,cat_id,url_id,url,source )" +
				"  VALUES	(?,?,?,?,?)";
		try {
			conn.setAutoCommit(false);
			PreparedStatement preparedStatement = conn.prepareStatement(sql);		
			int i = 0;
			
			while(iterator.hasNext())
			{
				ExtractEntity entity = iterator.next();
				String url = entity.getUrl();
				String url_id = entity.getID();
				int cat_id = entity.getCat_id();
				preparedStatement.setInt(1, 0);
				preparedStatement.setInt(2, cat_id);
				preparedStatement.setString(3,url_id);
				preparedStatement.setString(4,url);
				preparedStatement.setString(5,source);
			//	preparedStatement.setObject(6, entity);
				preparedStatement.addBatch();
				preparedStatement.clearParameters();
				
				i++;
				if(i%30==0){ preparedStatement.executeBatch();conn.commit();}
				
			}
			preparedStatement.executeBatch();
			conn.commit();
			conn.setAutoCommit(true);
		} catch (SQLException e) {			
			e.printStackTrace();
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		Connection conn = null;
		String table = "crawler_muaban_log";
		String source = "muaban";
		String file_store = "/data/crawler/data/enbac/store";
		if(args!=null&&args.length>1)
		{
			 table = args[0]!=null?args[0]:"tbl_muaban_log";
			 source = args[1]!=null?args[1]:"muaban";
			 file_store = args[2]!=null?args[2]:"d:/data/store";
		}
		try {
			Class.forName("com.mysql.jdbc.Driver");			
			conn = DriverManager.getConnection(
					"jdbc:mysql://192.168.1.101:3306/crawler?characterEncoding=UTF-8",
					"synuser","SynUser2011");
			BackUpEntity.backup(conn, table, source, file_store);
			
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		} catch (SQLException e) {		
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}      
        
	}
}
