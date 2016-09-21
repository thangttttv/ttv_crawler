package com.az24.crawler.article;

import hdc.crawler.ExtractEntity;
import hdc.util.html.ObjectVideo;
import hdc.util.text.HtmlUtil;
import hdc.util.text.StringUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.az24.crawler.AbstractImporter;
import com.az24.crawler.config.BeanXmlConfig;
import com.az24.crawler.config.JdbmXmlConfig;
import com.az24.crawler.model.Article;
import com.az24.crawler.model.DatabaseConfig;
import com.az24.dao.CrawlerLogDAO;
import com.az24.db.pool.C3p0TinhHinhPool;
import com.az24.util.FileLog;
import com.az24.util.UTF8Tool;

public class ArticleImporter extends AbstractImporter {

	public ArticleImporter(String filebeanconfig, String filejdbmconfig) {
		super(filebeanconfig, filejdbmconfig);
	}

	protected void processAfterSave(int id, int cat_id) {

	}

	public void run() {
		BeanXmlConfig beanXmlConfig = new BeanXmlConfig(this.filebeanconfig);
		beanXmlConfig.parseConfig();
		JdbmXmlConfig.parseConfig(this.filejdbmconfig);
		ExtractEntity entity = null;
		CrawlerLogDAO crawlerLogDAO = new CrawlerLogDAO();
		Map<Integer, Integer> cats = new HashMap<Integer, Integer>();
		try {
			int i = 0;
			openData();
			openConnection();

			Iterator<ExtractEntity> iterator = extractPrimary.values()
					.iterator();
			int count = 0;
			int empty = 0;int source=0;
			Article article = null;
			String content = "";
			while (iterator.hasNext()) {
				entity = iterator.next();
				cats = new HashMap<Integer, Integer>();
				getCategoriesParent(entity.getCat_id(),cats);
				int cat_root_id = cats.get(1);				
				boolean loged =	crawlerLogDAO.saveEntityCheck(DatabaseConfig.table_entity_log, "", 0, cat_root_id, entity);
			if (loged) {
					System.out.println(new Date()+" Imported Thu "+i+": "+(String)entity.getProperty("title"));				
					if(source==0)
					source = getSource(entity.getUrl());
					article = new Article();
					
					//Process Title
					article.title = HtmlUtil.toText((String) entity.getProperty("title"));					
					if (article.title != null
							&& article.title.length() > "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
									.length()&&article.title.indexOf("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")>=0) {
						article.title = article.title
								.substring("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
										.length());

					}					
					content = (String) entity.getProperty("content");
					if (content != null
							&& content.length() > "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
									.length()) {
						content = content
								.substring("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
										.length());

					}
					article.introtext = (String) entity
							.getProperty("introtext");
					article.tags = (String) entity.getProperty("tags");				    
				    Pattern pattern = Pattern.compile("\\W+");
					Pattern pattern2 = Pattern.compile("-$");
					String  alias = UTF8Tool.coDau2KoDau(article.title).trim();
					Matcher m = pattern.matcher(alias);
					String url_rewrite=m.replaceAll("-");
					m = pattern2.matcher(url_rewrite);
					url_rewrite = m.replaceAll("");
					article.alias = url_rewrite;
					
					if(StringUtil.isEmpty(article.title)||StringUtil.isEmpty(content)){empty++; continue;}
										
					article.content = content;
					article.picture = (String) entity.getProperty("picture")!=null?(String) entity.getProperty("picture"):"";
					//if(StringUtil.isEmpty(article.picture)) continue;
					article.author = (String) entity.getProperty("author");
					article.status = "active";
					article.is_image = 0;
					article.news_album = 0;
					
					if(entity.getProperty("numbervideo")!=null)
					{
						article.is_video = 1;
					}
					
					if(entity.getProperty("numberimage")!=null)
					{
						int numberimage = (Integer)entity.getProperty("numberimage");
						if(numberimage>=7)
						article.is_image = 1;
					}
					
					article.news_hot = 0;
					article.news_focus = 0;
					article.has_seo = 0;
					article.tags = (String) entity.getProperty("tags");
					article.original_link = entity.getUrl();					
					article.category_id = cat_root_id;
					if(cats.get(2)!=null)
					article.subcategory_id = cats.get(2);
					else article.subcategory_id = 0;
					article.source = source;
					if(StringUtil.isEmpty(article.author)) article.author="Auto";
					article.city_alias="toan-quoc";
					article.city_id=0;
					
					// Save Article
					Calendar calendar = Calendar.getInstance();
					
					if(!StringUtil.isEmpty(entity.date))
					{
						String dates[] = entity.date.split("/");			
						int intmonth = Integer.parseInt(dates[1]);
						int intday = Integer.parseInt(dates[2]);
						int year = Integer.parseInt(dates[0]);
						calendar.set(year, intmonth-1, intday);
						System.out.println(calendar.getTime().toString());
					}
					article.create_date = calendar.getTimeInMillis()/1000;
					article.publish_date = calendar.getTimeInMillis()/1000;
					article.edit_date = calendar.getTimeInMillis()/1000;
					
					int id = this.saveNews(cat_root_id, article);
					article.id = id;
					if(id>0)
					{
					this.saveNewsShort(cat_root_id, article);
					this.saveNewsShortLastest(cat_root_id, article);
					
					// save video
					/*if(entity.getProperty("videos")!=null)
					{
						List<ObjectVideo> videos =(List<ObjectVideo>)entity.getProperty("videos");
						if(videos!=null)
							for (ObjectVideo objectVideo : videos) {
								objectVideo.article_id=id;
								objectVideo.cat_id = cat_root_id;
								objectVideo.picture=article.picture;
								objectVideo.alias=article.alias;
								objectVideo.title = article.title;
								this.saveVideo(conn, objectVideo);
							}
					}*/
					}
					count++;
				}
				
				i++;
			}

			System.out.println(new Date()+"Tong Data =" + i);
			System.out.println(new Date()+"Tong Imported =" + count);
			System.out.println(new Date()+"Tong Data Loi " + empty);

			Calendar calendar = Calendar.getInstance();
			String log = calendar.getTime().toString() + "-->Tong Data:" + i
					+ "\r\n";
			log += calendar.getTime().toString() + "-->Tong Imported:" + count
					+ "\r\n";
			log += calendar.getTime().toString() + "-->Tong Data Loi:" + empty
					+ "\r\n";

			FileLog.createFileLog(JdbmXmlConfig.file_log + "_log_"
					+ calendar.get(Calendar.DAY_OF_MONTH)
					+ calendar.get(Calendar.MONTH)
					+ calendar.get(Calendar.YEAR) + ".txt");
			FileLog.writer(log);
			hdc.util.io.FileUtil.deleteFile(JdbmXmlConfig.url_image);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				commitData();
				closeData();
				conn.close();
				connLog.close();
			} catch (Exception e) {

			}
		}
	}
	
	public int getSource(String url)
	{
		int source =0 ;
		if(url.indexOf("vietnamnet")>0) return 1;
		if(url.indexOf("tienphong")>0) return 2;
		if(url.indexOf("vneconomy")>0) return 3;
		if(url.indexOf("zing")>0) return 5;
		if(url.indexOf("vnexpress")>0) return 6;
		if(url.indexOf("thegioidienanh")>0) return 7;
		if(url.indexOf("muctim")>0) return 8;
		if(url.indexOf("dantri")>0) return 9;
		if(url.indexOf("dep")>0) return 10;
		if(url.indexOf("2sao")>0) return 11;
		return source;
	}
	
	public int saveNews( int cat_root_id, Article article) {
		int id = 0;
		String table = "tbl_articles_" + cat_root_id;
		PreparedStatement ps;		
		try {
			Connection conn = C3p0TinhHinhPool.getConnection(); 
			conn.setAutoCommit(false);
			ps = conn
					.prepareStatement("INSERT INTO "
							+ table
							+ "(title, alias, category_id, introtext, content, tags"
							+ ",picture,  status, is_image,  create_date," +
							"  edit_date, publish_date,user_create,user_edit,source,subcategory_id,original_link,city_id,city_alias )"
							+ "	VALUES	(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
			ps.setString(1, article.title);
			ps.setString(2, article.alias);
			ps.setInt(3, article.category_id);
			ps.setString(4, article.introtext);
			ps.setString(5, article.content);
			ps.setString(6, article.tags);
			ps.setString(7, article.picture);
			ps.setInt(8, 1);
			ps.setInt(9, article.is_image);
			
			ps.setLong(10,article.create_date);
			ps.setLong(11, article.edit_date);
			ps.setLong(12, article.publish_date );
			ps.setString(13, article.author);
			ps.setString(14, article.author);
			ps.setInt(15, article.source);
			ps.setInt(16, article.subcategory_id);
			ps.setString(17, article.original_link);
			ps.setInt(18,article.city_id);
			ps.setString(19, article.city_alias);
			
			ps.execute();

			conn.commit();
			conn.setAutoCommit(true);

			String sql = "SELECT LAST_INSERT_ID()";
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				id = resultSet.getInt(1);
			C3p0TinhHinhPool.attemptClose(conn);
			C3p0TinhHinhPool.attemptClose(resultSet);
			C3p0TinhHinhPool.attemptClose(ps);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	public int getArticle(Connection conn, int cat_root_id, String title) {
		int id = 0;
		String table = "tbl_articles_" + cat_root_id;
		PreparedStatement ps;		
		try {
			ps = conn.prepareStatement("Select id from  "
							+ table
							+ " Where LOWER(trim(title)) = ? ");
			ps.setString(1, title.toLowerCase().trim());
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next())
				id = resultSet.getInt(1);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	public void saveNewsShort(int cat_root_id, Article article) {

		String table = "tbl_articles_short_" + cat_root_id;
		PreparedStatement ps;		
		try {
			Connection conn = C3p0TinhHinhPool.getConnection(); 
			conn.setAutoCommit(false);
			ps = conn
					.prepareStatement("INSERT INTO "
							+ table
							+ "(id,title, alias, category_id, introtext, tags,picture, "
							+ " status, is_image,   create_date, edit_date, " +
							  " publish_date,user_create,user_edit,source,subcategory_id,city_id,city_alias )"
							+ "	VALUES	(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
			ps.setInt(1, article.id);
			ps.setString(2, article.title);
			ps.setString(3, article.alias);
			ps.setInt(4, article.category_id);
			ps.setString(5, article.introtext);

			ps.setString(6, article.tags);
			ps.setString(7, article.picture);
			ps.setInt(8, 1);
			ps.setInt(9, article.is_image);
		
			ps.setLong(10,article.create_date);
			ps.setLong(11, article.edit_date);
			ps.setLong(12, article.publish_date );
			
			ps.setString(13, article.author);
			ps.setString(14,article.author);
			ps.setInt(15, article.source);
			ps.setInt(16, article.subcategory_id);
			
			ps.setInt(17,article.city_id);
			ps.setString(18, article.city_alias);
			ps.execute();

			conn.commit();
			conn.setAutoCommit(true);
			C3p0TinhHinhPool.attemptClose(conn);
			C3p0TinhHinhPool.attemptClose(ps);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	
	public void saveImage( int article_id,int category_id, String url) {

		String table = "tbl_article_image" ;
		PreparedStatement ps;		
		try {
			Connection conn = C3p0TinhHinhPool.getConnection(); 
			conn.setAutoCommit(false);
			ps = conn
					.prepareStatement("INSERT INTO "
							+ table
							+ "(articles_id,category_id, url)"
							+ "	VALUES	(?,?,?) ");
			ps.setInt(1, article_id);
			ps.setInt(2, category_id);
			ps.setString(3, url);
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			C3p0TinhHinhPool.attemptClose(conn);
			C3p0TinhHinhPool.attemptClose(ps);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void saveNewsShortLastest( int cat_root_id,
			Article article) {
		String table = "tbl_articles_short_lastest";
		PreparedStatement ps;
		
		try {
			Connection conn = C3p0TinhHinhPool.getConnection(); 
			conn.setAutoCommit(false);
			ps = conn
					.prepareStatement("INSERT INTO "
							+ table
							+ "(article_id,title, alias, category_id, introtext, tags,picture, "
							+ " status, is_image,  create_date, edit_date, publish_date," +
							" user_create,user_edit,source,subcategory_id,city_id,city_alias )"
							+ "	VALUES	(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
			ps.setInt(1, article.id);
			ps.setString(2, article.title);
			ps.setString(3, article.alias);
			ps.setInt(4, article.category_id);
			ps.setString(5, article.introtext);

			ps.setString(6, article.tags);
			ps.setString(7, article.picture);
			ps.setInt(8, 1);
			ps.setInt(9, article.is_image);		

			ps.setLong(10,article.create_date);
			ps.setLong(11, article.edit_date);
			ps.setLong(12, article.publish_date );
			
			ps.setString(13, article.author);
			ps.setString(14, article.author);
			ps.setInt(15, article.source);
			ps.setInt(16,article.subcategory_id);
			
			ps.setInt(17,article.city_id);
			ps.setString(18, article.city_alias);
			ps.execute();

			conn.commit();
			conn.setAutoCommit(true);
			C3p0TinhHinhPool.attemptClose(conn);
			C3p0TinhHinhPool.attemptClose(ps);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public void saveVideo(Connection conn,  ObjectVideo video) {		
		PreparedStatement ps;		
		try {
			conn.setAutoCommit(false);
			ps = conn
					.prepareStatement("INSERT INTO  tbl_videos(article_id,category_id,title," +
							"	alias,embed_code,embed_source,picture,length_time,width,height,tags," +
							"   create_date,edit_date,user_create" +
							"   ,user_edit,hot)" +
							" 	VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			ps.setInt(1, video.article_id);
			ps.setInt(2, video.cat_id);			
			ps.setString(3, video.title);
			ps.setString(4, video.alias);
			ps.setString(5, video.embed_code);
			ps.setString(6, video.embed_source);
			ps.setString(7, video.picture);
			ps.setInt(8, video.length_time);
			ps.setInt(9, video.width);
			ps.setInt(10, video.height);
			ps.setString(11, video.tags);
			ps.setLong(12,System.currentTimeMillis() / 1000 );
			ps.setLong(13,System.currentTimeMillis() / 1000 );
			ps.setString(14, "thangtt");
			ps.setString(15, "thangtt");
			ps.setInt(16, 0);
			ps.execute();

			conn.commit();
			conn.setAutoCommit(true);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public Map<Integer, Integer> getCategoriesParent(int cat,Map<Integer, Integer> holder) throws Exception {
		int cat_parent = 0;
		ResultSet rs = conn.createStatement().executeQuery(
				"SELECT parent_id FROM tbl_categories WHERE id = " + cat);
		if (rs.next()) {
			cat_parent = rs.getInt(1);
			
		}

		if (cat_parent == 0)
		{
			holder.put(1,cat);
			return holder;
		}
		else
		{
			holder.put(2,cat);
			getCategoriesParent(cat_parent,holder);
		}
		return holder;

	}

	public static void main(String[] args) {
		ArticleImporter crawlerExtracter = new ArticleImporter(
				"src/com/az24/crawler/config/beanZing.xml",
				"src/com/az24/crawler/config/jdbm.xml");
		crawlerExtracter.run();
	}

}