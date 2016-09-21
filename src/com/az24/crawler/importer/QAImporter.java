package com.az24.crawler.importer;

import hdc.crawler.ExtractEntity;
import hdc.crawler.AbstractCrawler.Url;
import hdc.util.text.StringUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.az24.crawler.AbstractImporter;
import com.az24.crawler.config.BeanXmlConfig;
import com.az24.crawler.config.JdbmXmlConfig;
import com.az24.crawler.config.SourceXmlConfig;
import com.az24.crawler.config.UserQaConfig;
import com.az24.crawler.fiter.ContentFilter;
import com.az24.crawler.model.DatabaseConfig;
import com.az24.dao.CrawlerLogDAO;
import com.az24.util.FileLog;
import com.az24.util.UTF8Tool;

public class QAImporter extends AbstractImporter {
	
	
	public QAImporter(String fileconfig,String filejdbmconfig) {
		super(fileconfig,filejdbmconfig);
	}
	
	protected void processAfterSave(int id, int cat_id) {

	}
	
	public int getQuestion(Connection conn,  String subject) {
		int id = 0;
		String table = "qa_question" ;
		PreparedStatement ps;		
		try {
			ps = conn.prepareStatement("Select id from  "
							+ table
							+ " Where LOWER(trim(subject)) = ? ");
			ps.setString(1, subject.toLowerCase().trim());
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next())
				id = resultSet.getInt(1);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}

	@SuppressWarnings("unchecked")
	public void run() {
		CrawlerLogDAO crawlerLogDAO = new CrawlerLogDAO();
		BeanXmlConfig beanXmlConfig = new BeanXmlConfig(this.filebeanconfig);
		beanXmlConfig.parseConfig();		
		ExtractEntity entity = null;
		UserQaConfig.getUsers();   
		this.getRandomUserID(UserQaConfig.users);
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH-1));
			int i = 0;
			int id = 0;
			openData();
			openConnection();
			conn.setAutoCommit(false);
			String INSERT_QUESTION = "INSERT INTO qa.qa_question" +
		    "(id_user, id_category, subject, content, active, number_answer, create_date, source, hashmd5, is_auto,id_cat_parent0,id_cat_parent1,id_cat_parent2,user_created,modify_date,user_modify,alias)" +
		    " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?)" ;
			PreparedStatement ps = conn.prepareStatement(INSERT_QUESTION) ;
			
			String INSERT_ANSWER = "INSERT INTO qa.qa_answer" +
			    "(id_user, id_question, answer, create_date, is_auto, hashmd5)" +
			    " VALUES(?, ?, ?, ?, ?, ?)" ;
			
			PreparedStatement psAnswer = conn.prepareStatement(INSERT_ANSWER) ;
			
			
			Iterator<ExtractEntity> iterator = extractPrimary.values()
					.iterator();
			
			Map<Integer, Integer> cat_holder = null;
			int count = 0;
			String content="";
			List<ExtractEntity> listExtractEntityAnswer = null;
			int empty=0;
			while (iterator.hasNext()) {
				entity = iterator.next();				
				id=0;
				if(this.getQuestion(conn, (String)entity.getProperty("subject"))>0) continue;
				
				if (!crawlerLogDAO.checkEntity(DatabaseConfig.table_entity_log, entity.getID())) {
					
					if(StringUtil.isEmpty((String)entity.getProperty("subject")))
					{
						empty++;
						continue;
					}
					cat_holder = new HashMap<Integer, Integer>();
					getCategoriesParent(entity.getCat_id(), cat_holder);
					ps.setInt(1, this.getRandomUserID(UserQaConfig.users));
					ps.setInt(2, entity.getCat_id());
					ps.setString(3, (String)entity.getProperty("subject"));
					content = (String) entity.getProperty("cla_description");
					if(content!=null&&content.length()>"<?xml version=\"1.0\" encoding=\"UTF-8\"?>".length())
					{
						content = content.substring("<?xml version=\"1.0\" encoding=\"UTF-8\"?>".length());
						ps.setString(4, ContentFilter.changeLink(content) );
					}
					else ps.setString(4,"" );
					
					ps.setInt(5, 1);
					
					listExtractEntityAnswer = (List<ExtractEntity>)entity.getProperty("qa_answer");					
					
					ps.setInt(6, listExtractEntityAnswer!=null?listExtractEntityAnswer.size():0);
					ps.setTimestamp(7, new Timestamp(calendar.getTimeInMillis()));
					ps.setInt(8, getSource(entity.getUrl()));
					ps.setString(9, entity.getID());
					ps.setInt(10, 1);
					
					ps.setInt(11, cat_holder.get(0));
					incrementClassified(cat_holder.get(0));
					if (cat_holder.get(1) != null)
					{
						ps.setInt(12, cat_holder.get(1)) ;
						incrementClassified(cat_holder.get(1));
					}
					else ps.setInt(12, 0);
					
					if (cat_holder.get(2) != null)
					{
						ps.setInt(13, cat_holder.get(2)) ;
						incrementClassified(cat_holder.get(2));
					}else ps.setInt(13, 0);
					
					ps.setString(14,"thangtt");
					ps.setTimestamp(15, new Timestamp(calendar.getTimeInMillis()));
					ps.setString(16,"thangtt");
					
					Pattern pattern = Pattern.compile("\\W+");
					Pattern pattern2 = Pattern.compile("-$");
					String  alias = UTF8Tool.coDau2KoDau( (String)entity.getProperty("subject")).trim();
					Matcher m = pattern.matcher(alias);
					String url_rewrite=m.replaceAll("-");
					m = pattern2.matcher(url_rewrite);
					alias = m.replaceAll("");
					ps.setString(17, alias);
					ps.execute();
					conn.commit();	
					
					crawlerLogDAO.saveEntity(DatabaseConfig.table_entity_log, "", 0, entity.getCat_id(), entity);
					
					String sql = "SELECT LAST_INSERT_ID()";
					PreparedStatement statement = conn.prepareStatement(sql);
					ResultSet resultSet = statement.executeQuery();
					if(resultSet.next()) id = resultSet.getInt(1);
					
					if(listExtractEntityAnswer!=null)
					for (ExtractEntity extractEntity : listExtractEntityAnswer) {
						try{
							if(extractEntity.getProperty("answer")!=null)
							{
							psAnswer.setInt(1, this.getRandomUserID(UserQaConfig.users));
							psAnswer.setInt(2, id);
							String content_answer = (String) extractEntity.getProperty("answer");
							if(content_answer!=null&&content_answer.length()>"<?xml version=\"1.0\" encoding=\"UTF-8\"?>".length())
							{
								content_answer = content_answer.substring("<?xml version=\"1.0\" encoding=\"UTF-8\"?>".length());
								
							}
							psAnswer.setString(3,content_answer);
							psAnswer.setTimestamp(4, new Timestamp(calendar.getTimeInMillis()));
							psAnswer.setInt(5, 1);
							psAnswer.setString(6,extractEntity.getID());
							psAnswer.execute();
							conn.commit();
							}
						}catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					count++;

				}			
				if (i % 30 == 0) {
					commitData();
				}
				i++;
			}
			 System.out.println("Tong Data Imported ="+count);
			 System.out.println("Tong Data ="+i);
			
			 calendar = Calendar.getInstance();
			 String log=calendar.getTime().toString()+ "-->Tong Data:"+i+"\r\n";	
			 log +=calendar.getTime().toString()+ "-->Tong Imported:"+count+"\r\n";	
			 log +=calendar.getTime().toString()+ "-->Tong Data Loi:"+empty+"\r\n";
			 
			 FileLog.createFileLog(JdbmXmlConfig.file_log+"_log_"+calendar.get(Calendar.DAY_OF_MONTH)+calendar.get(Calendar.MONTH)+calendar.get(Calendar.YEAR)+".txt");
			 FileLog.writer(log);
			 
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				commitData();
				closeData();
				conn.setAutoCommit(true);
				conn.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {				
				e.printStackTrace();
			}			
			
		}
	}
	
	private int getSource(String url)
	{
		SourceXmlConfig beanXmlConfig = new SourceXmlConfig("./conf/source_qa.xml");
		beanXmlConfig.parseConfig();
		List<Url> urls = SourceXmlConfig.urlConfigs;
		for (Url url2 : urls) {
			if(url.indexOf(url2.url)>-1) return Integer.parseInt(url2.id);
		}
		return -1;
	}

	
	private int getRandomUserID(List<Integer> user) {
	    Random random = new Random() ;
	    return user.get(random.nextInt(user.size())) ;
	  }
	
	public Map<Integer, Integer> getCategoriesParent(int cat,
			Map<Integer, Integer> holder) throws Exception {
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
		
		if (cat_parent == 0)
			return holder;
		else
			getCategoriesParent(cat_parent, holder);
		return null;
	}

	public void incrementClassified(int cat_id) {
		try {
			/*ResultSet tmpRs = conn.createStatement().executeQuery(
					"SELECT number_question FROM qa_category WHERE id = "
							+ cat_id);			
			tmpRs.next();
			int numberLv1 = tmpRs.getInt(1);
			tmpRs.close();*/
			conn.createStatement().executeUpdate(
					"UPDATE qa_category SET number_question = number_question + 1 WHERE id = " + cat_id);
			//tmpRs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void main(String[] args) {
		QAImporter crawlerExtracter = new QAImporter(
		"src/com/az24/crawler/config/beanQAYahoo.xml","src/com/az24/crawler/config/jdbm.xml");
		crawlerExtracter.run();
	}

}
