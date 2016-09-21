package com.az24.dao;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.imageio.ImageIO;

import com.az24.crawler.article.ArticleImporter;
import com.az24.crawler.model.Article;
import com.az24.crawler.model.ArticleImage;
import com.az24.crawler.model.City;
import com.az24.db.pool.C3p0TinhHinhPool;
import com.mortennobel.imagescaling.DimensionConstrain;
import com.mortennobel.imagescaling.ThumpnailRescaleOp;

public class ArticleDAO {
	
	public List<Article> getArticle() {
		PreparedStatement ps;	
		List<Article>  listArtilce = new ArrayList<Article>();
		Calendar calendar = Calendar.getInstance();
		long currentTime = calendar.getTimeInMillis()/1000;
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.SECOND,0);
		long startTime = calendar.getTimeInMillis()/1000;
		try {
			Connection conn = C3p0TinhHinhPool.getConnection();
			ps = conn.prepareStatement("SELECT 	id,article_id,title,alias,category_id,subcategory_id,introtext," +
					" tags,meta_title,meta_description,meta_keyword,meta_noindex,meta_nofollow,autolink,picture,create_date," +
					" edit_date,publish_date,source,status,is_image,news_hot,user_create,user_edit,is_seo,city_id,city_alias," +
					" original_link FROM hdc_Tinhhinh.tbl_articles_short_lastest Where ( publish_date BETWEEN ? And ?) And  news_hot = 0 And city_id = 0");
			ps.setLong(1, startTime);
			ps.setLong(2,currentTime);
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
				article.city_id = resultSet.getInt("city_id");
				article.city_alias = resultSet.getString("city_alias");
				article.introtext = resultSet.getString("introtext");
				listArtilce.add(article);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return listArtilce;
	}
	
	
	public List<Article> getArticle(int source,int category_sub_id) {
		PreparedStatement ps;	
		List<Article>  listArtilce = new ArrayList<Article>();
		try {
			Connection conn = C3p0TinhHinhPool.getConnection();
			ps = conn.prepareStatement("SELECT 	id,article_id,title,alias,category_id,subcategory_id,introtext," +
					" tags,meta_title,meta_description,meta_keyword,meta_noindex,meta_nofollow,autolink,picture,create_date," +
					" edit_date,publish_date,source,status,is_image,news_hot,user_create,user_edit,is_seo,city_id,city_alias," +
					" original_link FROM hdc_Tinhhinh.tbl_articles_short_lastest Where  source = ?  And subcategory_id = ?");
			ps.setInt(1, source);
			ps.setInt(2,category_sub_id);
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
				article.city_id = resultSet.getInt("city_id");
				article.city_alias = resultSet.getString("city_alias");
				article.introtext = resultSet.getString("introtext");
				article.user_create = resultSet.getString("user_create");
				article.user_edit = resultSet.getString("user_edit");
				listArtilce.add(article);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return listArtilce;
	}
	
	public List<Article> getArticleByCity(int city_id,int category_id) {
		PreparedStatement ps;	
		List<Article>  listArtilce = new ArrayList<Article>();
		Calendar calendar = Calendar.getInstance();
		//long currentTime = calendar.getTimeInMillis()/1000;
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.SECOND,0);
		//long startTime = calendar.getTimeInMillis()/1000;
		try {
			Connection conn = C3p0TinhHinhPool.getConnection();
			ps = conn.prepareStatement("SELECT 	id,article_id,title,alias,category_id,subcategory_id,introtext," +
					" tags,meta_title,meta_description,meta_keyword,meta_noindex,meta_nofollow,autolink,picture,create_date," +
					" edit_date,publish_date,source,status,is_image,news_hot,user_create,user_edit,is_seo,city_id,city_alias," +
					" original_link FROM hdc_Tinhhinh.tbl_articles_short_lastest Where   city_id = ? And  news_hot = 0  And category_id=? order by id desc limit 1000 ");
			
			//ps.setLong(1, startTime);
			//ps.setLong(2,currentTime);
			ps.setInt(1, city_id);
			ps.setInt(2, category_id);
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
				article.city_id = resultSet.getInt("city_id");
				article.city_alias = resultSet.getString("city_alias");
				article.introtext = resultSet.getString("introtext");
				listArtilce.add(article);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return listArtilce;
	}
	
	
	public void getArticleContent(Article article) {
		PreparedStatement ps;	
		try {
			Connection conn = C3p0TinhHinhPool.getConnection();
			ps = conn.prepareStatement("SELECT 	id,content,original_link FROM hdc_Tinhhinh.tbl_articles_"+article.category_id+" Where id = ?");
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
	
	public int saveHotTopic(Article article) {
		int id = 0;
		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0TinhHinhPool.getConnection(); 
			conn.setAutoCommit(false);
			ps = conn
					.prepareStatement("INSERT INTO tbl_hot_topic	(article_id,title,alias," +
							"	introtext,category_id,	city_id,city_alias,picture,	create_date,publish_date," +
							"   status,ordering,is_image,create_date_set) 	VALUES" +
							" (?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
			ps.setInt(1, article.id);
			ps.setString(2, article.title);
			ps.setString(3, article.alias);
			ps.setString(4, article.introtext);
			ps.setInt(5, article.category_id);
			ps.setInt(6, article.city_id);
			ps.setString(7, article.city_alias);
			ps.setString(8, article.picture);		
			ps.setLong(9,article.create_date);
			ps.setLong(10, article.publish_date);			
			ps.setString(11, article.status );
			ps.setInt(12, 0);
			ps.setInt(13, article.is_image);
			ps.setLong(14,Calendar.getInstance().getTimeInMillis()/1000);
			ps.execute();

			conn.commit();
			conn.setAutoCommit(true);
			C3p0TinhHinhPool.attemptClose(conn);
			C3p0TinhHinhPool.attemptClose(ps);
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
			Connection conn = C3p0TinhHinhPool.getConnection(); 
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(" Update  "+table+" Set news_hot = 1 Where id= "+article.id);
			ps.execute();
			ps = conn.prepareStatement(" Update  "+table_short+" Set news_hot = 1 Where id= "+article.id);
			ps.execute();
			ps = conn.prepareStatement(" Update  "+table_lastest+" Set news_hot = 1 Where article_id= "+article.id+" And category_id="+article.category_id);
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			C3p0TinhHinhPool.attemptClose(conn);
			C3p0TinhHinhPool.attemptClose(ps);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	
	public List<ArticleImage> getArticleImage(int article_id,int cagegory_id) {
		
		PreparedStatement ps;	
		List<ArticleImage>  listArtilce = new ArrayList<ArticleImage>();
		try {
			Connection conn = C3p0TinhHinhPool.getConnection();
			ps = conn.prepareStatement("SELECT 	id, articles_id, url FROM 	hdc_Tinhhinh.tbl_article_image 	Where articles_id = ? And category_id = ? ");
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
	
	public List<City> getCity() {
		
		PreparedStatement ps;	
		List<City>  listArtilce = new ArrayList<City>();
		try {
			Connection conn = C3p0TinhHinhPool.getConnection();
			ps = conn.prepareStatement("SELECT 	cit_id, cit_name, cit_parent_id, cit_order, lang_id, cit_short, admin_id, " +
					"cit_description,	cit_show,	cit_rewrite FROM tbl_city Where cit_parent_id= 0 ");
			

			ResultSet resultSet = ps.executeQuery();
			City article = null;
			while (resultSet.next())
			{
				article = new City();
				article.id = resultSet.getInt("cit_id");
				article.name = resultSet.getString("cit_name");				
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
				if(width>height&&width>=360&&height>=280)
				{
					String duoi = articleImage.url.substring(articleImage.url.lastIndexOf(".") + 1, articleImage.url.length());
					ThumpnailRescaleOp op = new ThumpnailRescaleOp(DimensionConstrain.createAbsolutionDimension(360, 280));
					des = op.filter(originalImage, des);
					String pre_folder = "/data/website/images.tinhhinh.net/picture/article/";
					String folder = pre_folder + strYear + "/"	+ strMonth	+ strDay + "/";
					file = new File(folder);
					if (!file.exists()) {
						file.mkdir();
						Runtime.getRuntime().exec("chmod 777 " + folder);
					}
					ImageIO.write(des, duoi, new File(folder+"large_"+image_name));
					kq = true; break;
				}
			}catch (Exception e) {
				width = 0;height = 0;
			}
		}
		return kq;
	}
	
	private String kw_1[] = null; 
	private String kw_2[] = null;
	private String kw_3[] = null;
	private String kw_4[] = null;
	private String kw_5[] = null;
	private String kw_6[] = null;
	private String kw_7[] = null;
	private String kw_8[] = null;
	private String kw_9[] = null;
	private String kw_10[] = null;
	private String kw_11[] = null;
	private String kw_12[] = null;
	
	public void initKeyword()
	{
		 try{
			  FileInputStream fstream = new FileInputStream("./conf/Thkeywords.txt");
			  // Get the object of DataInputStream
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  int i = 1;
			  while ((strLine = br.readLine()) != null)   {
				  switch (i) {
					case 1:
						kw_1 = strLine.split(",");
						break;	
					case 2:
						kw_2 = strLine.split(",");
						break;
					case 3:
						kw_3 = strLine.split(",");
						break;
					case 4:
						kw_4 = strLine.split(",");
						break;
					case 5:
						kw_5 = strLine.split(",");
						break;
					case 6:
						kw_6 = strLine.split(",");
						break;
					case 7:
						kw_7 = strLine.split(",");						
						break;
					case 8:
						kw_8 = strLine.split(",");						
						break;
					case 9:
						kw_9 = strLine.split(",");						
						break;
					case 10:
						kw_10 = strLine.split(",");						
						break;
					case 11:
						kw_11 = strLine.split(",");						
						break;
					case 12:
						kw_12 = strLine.split(",");						
						break;
					default:
						break;
					}
				  i++;
			  }
			  in.close();
		}catch (Exception e){
			  System.err.println("Error: " + e.getMessage());
		}
	}
	
	public void setUpTinHot(List<Article> articles)
	{
		initKeyword();
		for (Article article : articles) {
			
			getArticleContent(article);
			if(checkHotKeyword(article))
			{
				List<ArticleImage> listImage = getArticleImage(article.id,article.category_id);
				if(createImageHot(listImage, article.picture))
				{
					saveHotTopic(article);
					updateNewsHot(article);
					System.out.println(article.title);
				}
			}
		}
	}
	
	public void setUpTinHotProvince(List<Article> articles)
	{
		
		List<ArticleImage> listImage = null;
		for (Article article : articles) {
			listImage = getArticleImage(article.id,article.category_id);
			if(createImageHot(listImage, article.picture))
			{
				saveHotTopic(article);
				updateNewsHot(article);	
				System.out.println("title----->"+article.title);
				break;
			}
			
		}
	}
	
	public void deleteArticle(int id,int category_id)
	{

		String tbl="tbl_articles_"+category_id;
		String tbl_short="tbl_articles_short_"+category_id;
		String tbl_short_last="tbl_articles_short_lastest";
		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0TinhHinhPool.getConnection(); 
			conn.setAutoCommit(false);
			
				ps = conn.prepareStatement("Delete from "+tbl +" Where id = "+id);		
				ps.execute();
				ps = conn.prepareStatement("Delete from "+tbl_short +" Where id = "+id);
				ps.execute();
				ps = conn.prepareStatement("Delete from "+tbl_short_last +" Where article_id = "+id +" And category_id = "+category_id);
				ps.execute();
				
			conn.commit();
			conn.setAutoCommit(true);
			C3p0TinhHinhPool.attemptClose(conn);
			C3p0TinhHinhPool.attemptClose(ps);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	
	}
	
	public void copyArticleFromCat(int source,int subcat_id,int category_new_id,int subcat_new_id)
	{
		List<Article> listArticles = getArticle(source, subcat_id);
		ArticleImporter articleImporter = new ArticleImporter("","");
		int cat_older_id=0;
		for (Article article : listArticles) {			
			cat_older_id = article.category_id;
			int older_id = article.id;
			
			getArticleContent(article);
			article.category_id=category_new_id;
			article.subcategory_id = subcat_new_id;
			article.user_create="Auto";
			article.author="Auto";
			
			int id = articleImporter.saveNews(category_new_id, article);
			article.id= id;
			articleImporter.saveNewsShort(category_new_id, article);
			
			deleteArticle(older_id, cat_older_id);			
			articleImporter.saveNewsShortLastest(category_new_id, article);
		}
	}
	private boolean checkHotKeyword(Article article)
	{
		String keywordTmp[] =null;
		boolean kq = false;
			switch (article.category_id) {
			case 1:
				keywordTmp = kw_1;
				break;	
			case 2:
				keywordTmp = kw_2;
				break;
			case 3:
				keywordTmp = kw_3;
				break;
			case 4:
				keywordTmp = kw_4;
				break;
			case 5:
				keywordTmp = kw_5;
				break;
			case 6:
				keywordTmp = kw_6;
				break;
			case 7:
				keywordTmp = kw_7;						
				break;
			case 8:
				keywordTmp = kw_8;						
				break;
			case 9:
				keywordTmp = kw_9;						
				break;
			case 10:
				keywordTmp = kw_10;						
				break;
			case 11:
				keywordTmp = kw_11;						
				break;
			case 12:
				keywordTmp = kw_12;						
				break;
			default:
				break;
			}
		
		for (String string : keywordTmp) {
				if(article.content.toLowerCase().indexOf(string.toLowerCase().trim())>-1)
				{
					kq = true;
					break;
				}
		}
		
		return kq;
	}
	
	public static void main(String[] args) {
		ArticleDAO articleDAO = new ArticleDAO();
		List<Article> articles= articleDAO.getArticle();		
		articleDAO.setUpTinHot(articles);
		//articleDAO.copyArticleFromCat(26, 63, 10, 61);
	}
	
}

