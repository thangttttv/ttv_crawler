package com.az24.dao;

import hdc.util.html.ObjectVideo;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.az24.crawler.model.Article;
import com.az24.crawler.model.ArticleImage;
import com.az24.db.pool.C3p0BaloteenPool;
import com.mortennobel.imagescaling.DimensionConstrain;
import com.mortennobel.imagescaling.ThumpnailRescaleOp;

public class ArticleBaloteenDAO {
	
	public int saveNews( int cat_root_id, Article article) {
		int id = 0;
		String table = "tbl_articles_" + cat_root_id;
		PreparedStatement ps;		
		try {
			Connection conn = C3p0BaloteenPool.getConnection(); 
			conn.setAutoCommit(false);
			ps = conn
					.prepareStatement("INSERT INTO "
							+ table
							+ "(title, alias, category_id, introtext, content, tags"
							+ ",picture,  status, is_image,  create_date," +
							"  edit_date, publish_date,user_create,user_edit,source,subcategory_id,original_link)"
							+ "	VALUES	(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
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
			ps.execute();

			conn.commit();
			conn.setAutoCommit(true);

			String sql = "SELECT LAST_INSERT_ID()";
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				id = resultSet.getInt(1);
			C3p0BaloteenPool.attemptClose(conn);
			C3p0BaloteenPool.attemptClose(resultSet);
			C3p0BaloteenPool.attemptClose(ps);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	public Article getArticle(int cat_root_id, int id) {		
		String table = "tbl_articles_" + cat_root_id;
		PreparedStatement ps;	Article article = null;	
		try {
			Connection conn = C3p0BaloteenPool.getConnection(); 
			ps = conn.prepareStatement("Select id,title from  "
							+ table
							+ " Where id = ? ");
			ps.setInt(1, id);
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next())
			{
				id = resultSet.getInt(1);
				article = new Article();
				article.id = id;
				article.title = resultSet.getString("title");
			}
			C3p0BaloteenPool.attemptClose(conn);
			C3p0BaloteenPool.attemptClose(ps);
			C3p0BaloteenPool.attemptClose(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return article;
	}
	
	public void saveNewsShort(int cat_root_id, Article article) {

		String table = "tbl_articles_short_" + cat_root_id;
		PreparedStatement ps;		
		try {
			Connection conn = C3p0BaloteenPool.getConnection(); 
			conn.setAutoCommit(false);
			ps = conn
					.prepareStatement("INSERT INTO "
							+ table
							+ "(id,title, alias, category_id, introtext, tags,picture, "
							+ " status, is_image,   create_date, edit_date, " +
							  " publish_date,user_create,user_edit,source,subcategory_id,original_link )"
							+ "	VALUES	(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
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
			ps.setString(17, article.original_link);
			
			ps.execute();

			conn.commit();
			conn.setAutoCommit(true);
			C3p0BaloteenPool.attemptClose(conn);
			C3p0BaloteenPool.attemptClose(ps);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	
	public void saveImage( int article_id,int category_id, String url) {

		String table = "tbl_article_image" ;
		PreparedStatement ps;		
		try {
			Connection conn = C3p0BaloteenPool.getConnection(); 
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
			C3p0BaloteenPool.attemptClose(conn);
			C3p0BaloteenPool.attemptClose(ps);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void saveNewsShortLastest( int cat_root_id,
			Article article) {
		String table = "tbl_articles_short_lastest";
		PreparedStatement ps;
		
		try {
			Connection conn = C3p0BaloteenPool.getConnection(); 
			conn.setAutoCommit(false);
			ps = conn
					.prepareStatement("INSERT INTO "
							+ table
							+ "(article_id,title, alias, category_id, introtext, tags,picture, "
							+ " status, is_image,  create_date, edit_date, publish_date," +
							" user_create,user_edit,source,subcategory_id,original_link)"
							+ "	VALUES	(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
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
			ps.setString(17, article.original_link);
			
			ps.execute();

			conn.commit();
			conn.setAutoCommit(true);
			C3p0BaloteenPool.attemptClose(conn);
			C3p0BaloteenPool.attemptClose(ps);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public void saveVideo(ObjectVideo video) {		
		PreparedStatement ps;		
		try {
			Connection conn=C3p0BaloteenPool.getConnection();
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
			C3p0BaloteenPool.attemptClose(ps);
			C3p0BaloteenPool.attemptClose(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public Map<Integer, Integer> getCategoriesParent(int cat,Map<Integer, Integer> holder) throws Exception {
		int cat_parent = 0;
		Connection conn = C3p0BaloteenPool.getConnection();
		ResultSet rs = conn.createStatement().executeQuery(
				"SELECT parent_id FROM tbl_categories WHERE id = " + cat);
		if (rs.next()) {
			cat_parent = rs.getInt(1);
			
		}

		if (cat_parent == 0)
		{
			holder.put(1,cat);
			C3p0BaloteenPool.attemptClose(conn);
			return holder;
		}
		else
		{
			holder.put(2,cat);
			getCategoriesParent(cat_parent,holder);
		}
		C3p0BaloteenPool.attemptClose(conn);
		return holder;

	}
	
	public List<Article> getArticle(int cat_id) {
		PreparedStatement ps;	
		List<Article>  listArtilce = new ArrayList<Article>();
		Calendar calendar = Calendar.getInstance();
		long currentTime = calendar.getTimeInMillis()/1000;
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.SECOND,0);
		long startTime = calendar.getTimeInMillis()/1000;
		try {
			Connection conn = C3p0BaloteenPool.getConnection();
			ps = conn.prepareStatement("SELECT 	id,article_id,title,alias,category_id,subcategory_id,introtext," +
					" tags,meta_title,meta_description,meta_keyword,meta_noindex,meta_nofollow,autolink,picture,create_date," +
					" edit_date,publish_date,source,status,is_image,news_hot,user_create,user_edit,is_seo," +
					" original_link FROM tbl_articles_short_lastest Where ( publish_date BETWEEN ? And ?)   " +
					" And category_id = ? ");
			ps.setLong(1, startTime);
			ps.setLong(2,currentTime);
			ps.setInt(3,cat_id);
			ResultSet resultSet = ps.executeQuery();
			Article article = null;
			while (resultSet.next())
			{
				article = new Article();
				article.id = resultSet.getInt("article_id");
				article.title = resultSet.getString("title");
				article.alias = resultSet.getString("alias");
				article.category_id = resultSet.getInt("category_id");
				article.subcategory_id = resultSet.getInt("subcategory_id");
				article.tags = resultSet.getString("tags");
				article.meta_title = resultSet.getString("meta_title");
				article.meta_description = resultSet.getString("meta_description");
				article.meta_keyword = resultSet.getString("meta_keyword");
				article.meta_noindex = resultSet.getString("meta_noindex");
				article.meta_nofollow = resultSet.getString("meta_nofollow");
				article.autolink = resultSet.getString("autolink");
				article.picture =  resultSet.getString("picture");
				article.create_date = resultSet.getInt("create_date");
				article.edit_date = resultSet.getInt("edit_date");
				article.publish_date = resultSet.getInt("publish_date");
				article.source = resultSet.getInt("source");
				article.status = resultSet.getString("status");
				article.is_image = resultSet.getInt("is_image");
				article.news_hot = resultSet.getInt("news_hot");			
				article.introtext = resultSet.getString("introtext");
				listArtilce.add(article);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return listArtilce;
	}
	
	public int saveHotTopic(Article article) {
		int id = 0;
		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0BaloteenPool.getConnection(); 
			conn.setAutoCommit(false);
			ps = conn
					.prepareStatement("INSERT INTO tbl_hot_topic (article_id,title,alias," +
							"	introtext,category_id,picture,	create_date,publish_date," +
							"   status,ordering,is_image,create_date_set,is_video,is_home_news) 	VALUES" +
							" (?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
			ps.setInt(1, article.id);
			ps.setString(2, article.title);
			ps.setString(3, article.alias);
			ps.setString(4, article.introtext);
			ps.setInt(5, article.category_id);
			
			ps.setString(6, article.picture);		
			ps.setLong(7,article.create_date);
			ps.setLong(8, article.publish_date);			
			ps.setString(9, article.status );
			ps.setInt(10, 0);
			ps.setInt(11, article.is_image);
			ps.setLong(12,Calendar.getInstance().getTimeInMillis()/1000);
			ps.setInt(13, 0);
			ps.setInt(14, 1);
			ps.execute();

			conn.commit();
			conn.setAutoCommit(true);
			C3p0BaloteenPool.attemptClose(conn);
			C3p0BaloteenPool.attemptClose(ps);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}	
	
	
	public int saveArticleFeature (Article article) {
		int id = 0;
		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0BaloteenPool.getConnection(); 
			conn.setAutoCommit(false);
			ps = conn
					.prepareStatement("INSERT INTO hdc_Baloteen.tbl_articles_feature (" +
							" article_id, title, alias, introtext, category_id, picture," +
							" create_date, publish_date, status, is_video, is_image, " +
							" create_date_set,ordering)	VALUES	(?,?,?,?,?,?,?,?,?,?,?,?,1)");
			ps.setInt(1, article.id);
			ps.setString(2, article.title);
			ps.setString(3, article.alias);
			ps.setString(4, article.introtext);
			ps.setInt(5, article.category_id);
			
			ps.setString(6, article.picture);		
			ps.setLong(7,article.create_date);
			ps.setLong(8, article.publish_date);			
			ps.setString(9, article.status );
			ps.setInt(10, article.is_video);
			ps.setInt(11, article.is_image);
			ps.setLong(12,Calendar.getInstance().getTimeInMillis()/1000);			
			ps.execute();

			conn.commit();
			conn.setAutoCommit(true);
			C3p0BaloteenPool.attemptClose(conn);
			C3p0BaloteenPool.attemptClose(ps);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}	
	
	
	public int updateNewsHot(Article article) {
		int id = 0;
		String table = "tbl_articles_"+article.category_id;
		String table_short = "tbl_articles_short_"+article.category_id;
		String table_lastest = "tbl_articles_short_lastest";
		PreparedStatement ps;		
		try {
			Connection conn = C3p0BaloteenPool.getConnection(); 
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(" Update  "+table+" Set news_hot = 1 Where id= "+article.id);
			ps.execute();
			ps = conn.prepareStatement(" Update  "+table_short+" Set news_hot = 1 Where id= "+article.id);
			ps.execute();
			ps = conn.prepareStatement(" Update  "+table_lastest+" Set news_hot = 1 Where article_id= "+article.id+" And category_id="+article.category_id);
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			C3p0BaloteenPool.attemptClose(conn);
			C3p0BaloteenPool.attemptClose(ps);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	public List<ArticleImage> getArticleImage(int article_id,int cagegory_id) {
		
		PreparedStatement ps;	
		List<ArticleImage>  listArtilce = new ArrayList<ArticleImage>();
		try {
			Connection conn = C3p0BaloteenPool.getConnection();
			ps = conn.prepareStatement("SELECT 	id, articles_id, url FROM 	tbl_article_image 	Where articles_id = ? And category_id = ? ");
			ps.setInt(1, article_id);
			ps.setInt(2, cagegory_id);

			ResultSet resultSet = ps.executeQuery();
			ArticleImage article = null;
			while (resultSet.next())
			{
				article = new ArticleImage();
				article.id = resultSet.getInt("id");
				article.artilce_id = resultSet.getInt("articles_id");
				article.url = resultSet.getString("url");
				listArtilce.add(article);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return listArtilce;
	}
	
	public boolean createImageHot(List<ArticleImage> images,String image_name)
	{
		File file = null;BufferedImage originalImage = null,des=null;
		int width= 0;int height = 0;
		boolean kq = false;
		Calendar calendar = Calendar.getInstance();		
		int month = calendar.get(Calendar.MONTH)+1;
		String strMonth = month<10?"0"+month:month+"";
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		String strDay = day<10?"0"+day:day+"";
		String strYear = calendar.get(Calendar.YEAR)+"";
		for (ArticleImage articleImage : images) {
			try{
				file = new File(articleImage.url);
				InputStream is = new FileInputStream(file);
				originalImage = ImageIO.read(new BufferedInputStream(is));
				width = originalImage.getWidth();
				height = originalImage.getHeight();
				System.out.println(articleImage.url);
				System.out.println(width);
				System.out.println(height);
				if(width>height&&width>=400&&height>=330)
				{
					String duoi = articleImage.url.substring(articleImage.url.lastIndexOf(".") + 1, articleImage.url.length());
					ThumpnailRescaleOp op = new ThumpnailRescaleOp(DimensionConstrain.createAbsolutionDimension(410, 340));
					des = op.filter(originalImage, des);
					String pre_folder = "/usr/src/java/tomcat7/webapps/images.az24.vn/baloteen/picture/article/";
					//pre_folder = "D:/data/picture_auto/";
					String folder = pre_folder + strYear + "/"	+ strMonth	+ strDay + "/";
					file = new File(folder);
					if (!file.exists()) {
						file.mkdir();
						Runtime.getRuntime().exec("chmod 777 " + folder);
					}
					ImageIO.write(des, duoi, new File(folder+"large_"+image_name));
					
					op = new ThumpnailRescaleOp(DimensionConstrain.createAbsolutionDimension(300, 185));
					des = op.filter(originalImage, des);
					file = new File(folder);
					if (!file.exists()) {
						file.mkdir();
						Runtime.getRuntime().exec("chmod 777 " + folder);
					}
					ImageIO.write(des, duoi, new File(folder+"medium_"+image_name));
					
					kq = true; break;
				}
			}catch (Exception e) {
				width = 0;height = 0;
			}
		}
		return kq;
	}
	
	public void getArticleContent(Article article) {
		PreparedStatement ps;	
		try {
			Connection conn = C3p0BaloteenPool.getConnection();
			ps = conn.prepareStatement("SELECT 	id,content,original_link FROM tbl_articles_"+article.category_id+" Where id = ?");
			ps.setInt(1, article.id);
			ResultSet resultSet = ps.executeQuery();
			
			if (resultSet.next())
			{						
				article.content = resultSet.getString("content");
				article.original_link = resultSet.getString("original_link");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	
	public void setUpTinHot()
	{
		List<Article> articles = null;
		int i = 1;
		while(i<=12)
		{
			articles = getArticle(i);
			for (Article article : articles) {				
				getArticleContent(article);	
				System.out.println(article.title);
				List<ArticleImage> listImage = getArticleImage(article.id,article.category_id);
				if(createImageHot(listImage, article.picture))
				{
					saveHotTopic(article);
					saveArticleFeature(article);
					updateNewsHot(article);
					System.out.println(article.title);
					break;
				}
			}
			i++;
		}
	}
	
	public static void main(String[] args) {
		ArticleBaloteenDAO articleBaloteenDAO = new ArticleBaloteenDAO();
		articleBaloteenDAO.setUpTinHot();
	}
	
	
}
