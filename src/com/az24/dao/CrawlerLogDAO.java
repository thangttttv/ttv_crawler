package com.az24.dao;

import hdc.crawler.ExtractEntity;
import hdc.util.text.HtmlUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.az24.crawler.model.Article;
import com.az24.crawler.model.CrawlerLog;
import com.az24.crawler.model.ImageConfig;
import com.az24.db.pool.C3p0ClassifiedPool;
import com.az24.db.pool.C3p0LogPool;

public class CrawlerLogDAO {

	public void saveImage(String table, ImageConfig imageConfig) {
		try {
			Connection conn = C3p0LogPool.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement ps = conn.prepareStatement("INSERT INTO " + table
					+ " (" + " url_id,url,url_content ) VALUES (?,?,?)");
			ps.setString(1, imageConfig.id);
			ps.setString(2, imageConfig.src);
			ps.setString(3, imageConfig.url_content);
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			C3p0ClassifiedPool.attemptClose(conn);
			C3p0ClassifiedPool.attemptClose(ps);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public boolean checkEmage(String table, String url_id) {
		try {
			Connection conn = C3p0LogPool.getConnection();
			PreparedStatement ps = conn.prepareStatement("select id from "
					+ table + " where url_id = ? ");
			ps.setString(1, url_id);
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				return true;
			}
			C3p0ClassifiedPool.attemptClose(conn);
			C3p0ClassifiedPool.attemptClose(ps);
			C3p0ClassifiedPool.attemptClose(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public int  saveEntity(String table, String source,
			int result_id, int cat_id, ExtractEntity entity) {
		int id = 0;
		try {		
			Connection conn = C3p0LogPool.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement ps = conn.prepareStatement("INSERT INTO " + table
					+ "(title,url_id,url,source,result_id,cat_id)"
					+ "	VALUES	(?,?,?,?,?,?) ");
			ps.setString(1, entity.getName());
			ps.setString(2, entity.getID());
			ps.setString(3, entity.getUrl());
			ps.setString(4, source);
			ps.setInt(5, result_id);
			ps.setInt(6, cat_id);			
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			String sql = "SELECT LAST_INSERT_ID()";
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();		
			if (resultSet.next())
			{
				id = resultSet.getInt(1);	
			}
			C3p0ClassifiedPool.attemptClose(conn);
			C3p0ClassifiedPool.attemptClose(ps);
			C3p0ClassifiedPool.attemptClose(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
			 
		}
		return id;

	}
	
	public int  saveEntity(String table, CrawlerLog entity) {
		int id = 0;
		try {	
			Connection conn = C3p0LogPool.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement ps = conn.prepareStatement("INSERT INTO " + table
					+ "(title,url_id,url,source,result_id,cat_id,create_date)"
					+ "	VALUES	(?,?,?,?,?,?,?) ");
			ps.setString(1, entity.title);
			ps.setString(2, entity.url_id);
			ps.setString(3, entity.url);
			ps.setString(4, entity.source);
			ps.setInt(5, entity.result_id);
			ps.setInt(6, entity.cat_id);			
			ps.setTimestamp(7, entity.create_date);
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			String sql = "SELECT LAST_INSERT_ID()";
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();		
			if (resultSet.next())
			{
				id = resultSet.getInt(1);	
			}
			C3p0ClassifiedPool.attemptClose(conn);
			C3p0ClassifiedPool.attemptClose(ps);
			C3p0ClassifiedPool.attemptClose(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
			 
		}
		return id;

	}
	
	public boolean saveEntityCheck(String table, String source,
			int result_id, int cat_id, ExtractEntity entity) {
		try {
			Connection conn = C3p0LogPool.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement ps = conn.prepareStatement("INSERT INTO " + table
					+ "(url_id,url,source,result_id,cat_id,title)"
					+ "	VALUES	(?,?,?,?,?,?) ");
			ps.setString(1, entity.getID());
			ps.setString(2, entity.getUrl());
			ps.setString(3, source);
			ps.setInt(4, result_id);
			ps.setInt(5, cat_id);	
			String title = HtmlUtil.toText((String) entity.getProperty("title"));					
			if (title != null
					&& title.length() > "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
							.length()&&title.indexOf("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")>=0) {
				title = title
						.substring("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
								.length());

			}		
			ps.setString(6,title);
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			C3p0LogPool.attemptClose(conn);
			C3p0LogPool.attemptClose(ps);
			return true;
		} catch (SQLException e) {
			System.out.println(new Date()+" Don't Save Log Article");			
			return false;
		}

	}

	public boolean checkEntity(String table, String url_id) {
		Connection connection = null;PreparedStatement ps  = null;
		ResultSet resultSet = null;
		try {
			connection = C3p0LogPool.getConnection();		
			ps = connection.prepareStatement("select id from "
					+ table + " where url_id = ? ");
			ps.setString(1, url_id);
			resultSet = ps.executeQuery();
			if (resultSet.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			C3p0LogPool.attemptClose(connection);
			C3p0LogPool.attemptClose(ps);
			C3p0LogPool.attemptClose(resultSet);
		}
		return false;
	}
	
	public List<CrawlerLog> getAll(String table, int begin) {
		List<CrawlerLog> listLog = new ArrayList<CrawlerLog>();
		try {
			Connection conn = C3p0LogPool.getConnection();
			PreparedStatement ps = conn.prepareStatement("select id,  result_id, cat_id, url_id, url, source, entity, create_date from "
					+ table + " where id> ? order by id limit 1000 ");
			ps.setInt(1, begin);
			ResultSet resultSet = ps.executeQuery();
			CrawlerLog  crawlerLog = null;
			while (resultSet.next()) {
				crawlerLog = new CrawlerLog();
				crawlerLog.id =  resultSet.getInt("id");
				//crawlerLog.title =  resultSet.getString("title");
				crawlerLog.result_id =  resultSet.getInt("result_id");
				crawlerLog.cat_id =  resultSet.getInt("cat_id");
				crawlerLog.url_id =  resultSet.getString("url_id");
				crawlerLog.url =  resultSet.getString("url");
				crawlerLog.source =  resultSet.getString("source");
				crawlerLog.create_date =  resultSet.getTimestamp("create_date");
				listLog.add(crawlerLog);
			}
			C3p0ClassifiedPool.attemptClose(conn);
			C3p0ClassifiedPool.attemptClose(ps);
			C3p0ClassifiedPool.attemptClose(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listLog;
	}
	
	
	
	 
	public static void main(String[] args) {
		int i = 0;
		CrawlerLogDAO crawlerLogDAO = new CrawlerLogDAO();
		ArticleBaloteenDAO classifiedDAO = new ArticleBaloteenDAO();
		while(i<=100000)
		{
			try {						
				List<CrawlerLog> list = crawlerLogDAO.getAll(args[0], i);
				if(list.size()==0)break;
				for (CrawlerLog crawlerLog : list) {
					Article classified = classifiedDAO.getArticle(crawlerLog.cat_id, crawlerLog.id);
					System.out.println(crawlerLog.result_id);
					if(classified!=null)
					{
						crawlerLog.title = classified.title ;
					}else {
						crawlerLog.title = args[1] +crawlerLog.id;
					}
						crawlerLogDAO.saveEntity("crawler_baloteen_log",  crawlerLog);
					
					i = crawlerLog.id;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
