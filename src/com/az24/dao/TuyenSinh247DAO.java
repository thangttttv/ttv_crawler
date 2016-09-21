package com.az24.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.az24.crawler.model.ArticleTS;
import com.az24.crawler.model.CoursesLong;
import com.az24.crawler.model.CoursesLongCategory;
import com.az24.crawler.model.CoursesLongCity;
import com.az24.crawler.model.CoursesShort;
import com.az24.crawler.model.CoursesShortCity;
import com.az24.crawler.model.CoursesShortSubject;
import com.az24.crawler.model.Trainer;
import com.az24.crawler.model.TrainerAddess;
import com.az24.crawler.model.TrainerContact;
import com.az24.db.pool.C3p0TuyensinhPool;

public class TuyenSinh247DAO {
	
	public int saveCoursesShort(CoursesShort courses) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0TuyensinhPool.getConnection(); 
			conn.setAutoCommit(false);
			ps = conn.prepareStatement("INSERT INTO hdc_Tuyensinh247.tbl_courses_short ( cat_id, cat_id1, cat_id2, cat_id3, cat_id4, cat_id5, " +
					" cat_id6, title,alias, introtext,	content, tags, meta_keyword, meta_title, meta_description, meta_noindex, meta_nofollow,picture," +
					" file_name,c_type,	is_hot,	is_edit, user_id, username, trainer_id, create_date, edit_date, publish_date, expired_date,	endvip_date," +
					" time_show_vip,status,fee, price,price_usd,price_type,	training_method, training_method_expand, open_date,	open_date_expand,frequency_open," +
					" string_time,	string_time_detail, string_time_detail_expand, incentive, start_date, end_date, info_expand, admin_id, admin_name" +
					",contact_name,contact_email,contact_tel,contact_yahoo,contact_skype)" +
					" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
					" ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			
			ps.setInt(1, courses.cat_id);
			ps.setInt(2, courses.cat_id1);
			ps.setInt(3, courses.cat_id2);
			ps.setInt(4, courses.cat_id3);
			ps.setInt(5, courses.cat_id4);
			ps.setInt(6, courses.cat_id5);
			ps.setInt(7, courses.cat_id6);
			
			ps.setString(8, courses.title);	
			ps.setString(9, courses.alias);
			ps.setString(10, courses.introtext);
			ps.setString(11, courses.content);
			ps.setString(12, courses.tags);
			ps.setString(13, courses.meta_keyword);
			
			ps.setString(14,courses.meta_title);
			ps.setString(15, courses.meta_description);
			ps.setInt(16, courses.meta_noindex );
			ps.setInt(17, courses.meta_nofollow);
			ps.setString(18, courses.picture);
			ps.setString(19, courses.file_name);
			ps.setInt(20, courses.c_type);
			ps.setInt(21, courses.is_hot);
			ps.setInt(22,courses.is_edit);
			ps.setInt(23, courses.user_id);
			
			ps.setString(24, courses.username);
			ps.setInt(25, courses.trainer_id);
			ps.setLong(26, courses.create_date);
			ps.setLong(27, courses.edit_date);
			ps.setLong(28, courses.publish_date);
			ps.setLong(29, courses.expired_date);
			ps.setLong(30, courses.endvip_date);
			
			ps.setInt(31, courses.time_show_vip);
			ps.setInt(32, courses.status);
			ps.setDouble(33, courses.fee);
			ps.setString(34, courses.price);
			ps.setDouble(35, courses.price_usd);
			ps.setInt(36, courses.price_type);
			ps.setInt(37, courses.training_method);
			ps.setString(38, courses.training_method_expand);
			ps.setLong(39, courses.open_date);
			
			ps.setString(40, courses.open_date_expand);
			ps.setString(41, courses.frequency_open );
			ps.setString(42, courses.string_time);
			
			
			ps.setString(43, courses.string_time_detail);
			ps.setString(44, courses.string_time_detail_expand);
			ps.setString(45, courses.incentive);
			
			
			ps.setLong(46, courses.start_date);
			ps.setLong(47, courses.end_date);
			
			ps.setString(48, courses.info_expand);
			ps.setInt(49, courses.admin_id);
			ps.setString(50, courses.admin_name);
			
			ps.setString(51, courses.contact_name);
			ps.setString(52, courses.contact_email);
			ps.setString(53, courses.contact_tel);
			ps.setString(54, courses.contact_yahoo);
			ps.setString(55, courses.contact_skype);
			
			
			ps.execute();

			conn.commit();
			conn.setAutoCommit(true);

			String sql = "SELECT LAST_INSERT_ID()";
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				id = resultSet.getInt(1);
			C3p0TuyensinhPool.attemptClose(conn);
			C3p0TuyensinhPool.attemptClose(resultSet);
			C3p0TuyensinhPool.attemptClose(ps);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	
	public int saveCoursesLong(CoursesLong courses) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0TuyensinhPool.getConnection(); 
			conn.setAutoCommit(false);
			ps = conn.prepareStatement("INSERT INTO hdc_Tuyensinh247.tbl_courses_long (cat_id,level_id,title,alias,introtext," +
					" content,tags,meta_keyword,meta_title,	meta_description,meta_noindex,meta_nofollow, picture, file_name, c_type, is_hot," +
					" is_edit,user_id,username,trainer_id,create_date,edit_date,publish_date,expired_date,endvip_date,time_show_vip,status," +
					" fee,price,form_id,form_training_expand,form_enrollment,string_time,start_date,end_date,info_expand,admin_id,admin_name," +
					" contact_name,contact_email,contact_tel,contact_yahoo,contact_skype)" +
					" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			
			ps.setInt(1, courses.cat_id);
			ps.setInt(2, courses.level_id);
			ps.setString(3, courses.title);
			ps.setString(4, courses.alias);
			ps.setString(5, courses.introtext);
			ps.setString(6, courses.content);
			ps.setString(7, courses.tags);
			ps.setString(8, courses.meta_keyword);
			
			ps.setString(9,courses.meta_title);
			ps.setString(10, courses.meta_description);
			ps.setInt(11, courses.meta_noindex );
			ps.setInt(12, courses.meta_nofollow);
			ps.setString(13, courses.picture);
			ps.setString(14, courses.file_name);
			ps.setInt(15, courses.c_type);
			ps.setInt(16, courses.is_hot);
			ps.setInt(17,courses.is_edit);
			ps.setInt(18, courses.user_id);
			
			ps.setString(19, courses.username);
			ps.setInt(20, courses.trainer_id);
			ps.setLong(21, courses.create_date);
			ps.setLong(22, courses.edit_date);
			ps.setLong(23, courses.publish_date);
			ps.setLong(24, courses.expired_date);
			ps.setLong(25, courses.endvip_date);
			
			ps.setInt(26, courses.time_show_vip);
			ps.setInt(27, courses.status);
			ps.setDouble(28, courses.fee);
			ps.setString(29, courses.price);
			ps.setInt(30, courses.form_training);
			ps.setString(31, courses.form_training_expand);
			ps.setString(32, courses.form_enrollment);
			ps.setString(33, courses.string_time);
			
			ps.setLong(34, courses.start_date);
			ps.setLong(35, courses.end_date);
			ps.setString(36, courses.info_expand);
			ps.setInt(37, courses.admin_id);
			ps.setString(38, courses.admin_name);
			
			ps.setString(39, courses.contact_name);
			ps.setString(40, courses.contact_email);
			ps.setString(41, courses.contact_tel);
			ps.setString(42, courses.contact_yahoo);
			ps.setString(43, courses.contact_skype);
			
			ps.execute();

			conn.commit();
			conn.setAutoCommit(true);

			String sql = "SELECT LAST_INSERT_ID()";
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				id = resultSet.getInt(1);
			C3p0TuyensinhPool.attemptClose(conn);
			C3p0TuyensinhPool.attemptClose(resultSet);
			C3p0TuyensinhPool.attemptClose(ps);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	public int saveTrainer(Trainer trainer) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0TuyensinhPool.getConnection(); 
			conn.setAutoCommit(false);
			ps = conn.prepareStatement("INSERT INTO tbl_trainer ( title, title_short, alias,  " +
					" training_method,logo,website,phone,hotline," +
					" fax,email,description,hot_courses,is_hot,STATUS,create_date,admin_id,admin_name,is_auto,is_member,original_link)" +
					" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			ps.setString(1, trainer.title);
			ps.setString(2, trainer.title_short);
			ps.setString(3, trainer.alias);
			ps.setString(4, trainer.training_method);
			
			ps.setString(5, trainer.logo);
			ps.setString(6, trainer.website);
			ps.setString(7, trainer.phone);
			ps.setString(8, trainer.hotline);
			ps.setString(9, trainer.fax);
			ps.setString(10, trainer.email);			
			ps.setString(11, trainer.description);
			ps.setString(12, trainer.hot_courses);
			ps.setInt(13, trainer.is_hot);
			ps.setInt(14, trainer.status);
			ps.setLong(15, trainer.create_date);
			ps.setInt(16, trainer.admin_id);
			ps.setString(17, trainer.admin_name);
			ps.setInt(18, trainer.is_auto);
			ps.setInt(19, trainer.is_member);
			ps.setString(20, trainer.original_link);
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
			String sql = "SELECT LAST_INSERT_ID()";
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				id = resultSet.getInt(1);
			C3p0TuyensinhPool.attemptClose(conn);
			C3p0TuyensinhPool.attemptClose(resultSet);
			C3p0TuyensinhPool.attemptClose(ps);
			
			C3p0TuyensinhPool.attemptClose(conn);			
			C3p0TuyensinhPool.attemptClose(ps);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	public int saveCoursesLongCategory (CoursesLongCategory courses) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0TuyensinhPool.getConnection(); 
			conn.setAutoCommit(false);
			ps = conn.prepareStatement("INSERT INTO hdc_Tuyensinh247.tbl_courses_long_category 	(course_id,cat_id )" +
					" VALUES (?,?)");
			ps.setInt(1, courses.course_id);
			ps.setInt(2, courses.cat_id);
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			C3p0TuyensinhPool.attemptClose(conn);			
			C3p0TuyensinhPool.attemptClose(ps);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	public int saveCoursesLongCity  (CoursesLongCity courses) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0TuyensinhPool.getConnection(); 
			conn.setAutoCommit(false);
			ps = conn.prepareStatement("INSERT INTO tbl_courses_long_city (course_id, level_id, city_id, " +
					" district_id,create_date,form_id ) VALUES (?,?,?,?,?,?)");
			ps.setInt(1, courses.course_id);
			ps.setInt(2, courses.level_id);
			ps.setInt(3, courses.city_id);
			ps.setInt(4, courses.district_id);
			ps.setLong(5, courses.create_date);
			ps.setInt(6, courses.form_id);
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			C3p0TuyensinhPool.attemptClose(conn);			
			C3p0TuyensinhPool.attemptClose(ps);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	public int saveCoursesShortCity(CoursesShortCity courses) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0TuyensinhPool.getConnection(); 
			conn.setAutoCommit(false);
			ps = conn.prepareStatement("INSERT INTO hdc_Tuyensinh247.tbl_courses_short_city (course_id, cat_id, city_id, district_id, create_date" +
					" )  VALUES (?,?,?,?,?)");
			ps.setInt(1, courses.course_id);
			ps.setInt(2, courses.cat_id);
			ps.setInt(3, courses.city_id);
			ps.setInt(4, courses.district_id);
			ps.setLong(5, courses.create_date);			
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			C3p0TuyensinhPool.attemptClose(conn);			
			C3p0TuyensinhPool.attemptClose(ps);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	public int saveCoursesShortSubject (CoursesShortSubject courses) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0TuyensinhPool.getConnection(); 
			conn.setAutoCommit(false);
			ps = conn.prepareStatement("INSERT INTO hdc_Tuyensinh247.tbl_courses_short_subject ( course_id," +
					"	subject_id,create_date) VALUES (?,?,?);");
			ps.setInt(1, courses.course_id);
			ps.setInt(2, courses.subject_id);
			ps.setLong(3, courses.create_date);
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			C3p0TuyensinhPool.attemptClose(conn);			
			C3p0TuyensinhPool.attemptClose(ps);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	public int getSubject (String subject) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0TuyensinhPool.getConnection(); 
			ps = conn.prepareStatement("SELECT 	id, title, alias, description,create_date, ordering	FROM hdc_tuyensinh247.tbl_subject " +
					"	Where title like '%"+subject+"%'");
			ResultSet rs = ps.executeQuery();
			
			if(rs.next())
			{
				id = rs.getInt("id");
			}
			C3p0TuyensinhPool.attemptClose(conn);			
			C3p0TuyensinhPool.attemptClose(ps);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	public int getCity (String city) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0TuyensinhPool.getConnection(); 
			ps = conn.prepareStatement("SELECT 	cit_id,	cit_name, cit_parent_id, cit_order, cit_short, admin_id, cit_description, cit_show, cit_rewrite	FROM  tbl_city " +
					"	Where cit_name like '%"+city+"%'");
			ResultSet rs = ps.executeQuery();
			
			if(rs.next())
			{
				id = rs.getInt("id");
			}
			C3p0TuyensinhPool.attemptClose(conn);			
			C3p0TuyensinhPool.attemptClose(ps);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	public int getTrainer (String trainner) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0TuyensinhPool.getConnection(); 
			ps = conn.prepareStatement("SELECT 	id 	FROM  tbl_trainer " +
					"	Where title like '%"+trainner+"%'");
			ResultSet rs = ps.executeQuery();
			
			if(rs.next())
			{
				id = rs.getInt("id");
			}
			C3p0TuyensinhPool.attemptClose(conn);			
			C3p0TuyensinhPool.attemptClose(ps);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	public Map<Integer, Integer> getCategoriesParent(int cat_id,
			Map<Integer, Integer> holder) {
		int cat_parent = 0;
		int level = 0;		
		try {
			Connection conn = C3p0TuyensinhPool.getConnection();
			ResultSet rs = conn.createStatement().executeQuery(
					"SELECT parent_id,level FROM tbl_categories WHERE id = "
							+ cat_id);
			if (rs.next()) {
				cat_parent = rs.getInt(1);
				level = rs.getInt(2);
				holder.put(level, cat_id);
			}
			if (cat_parent == 0)
				return holder;
			else
				getCategoriesParent(cat_parent, holder);
			C3p0TuyensinhPool.attemptClose(conn);
			C3p0TuyensinhPool.attemptClose(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}
	
	

	public int saveNews(ArticleTS article) {
		int id = 0;
		PreparedStatement ps;		
		try {
			Connection conn = C3p0TuyensinhPool.getConnection(); 
			conn.setAutoCommit(false);
			ps = conn
					.prepareStatement("INSERT INTO tbl_articles(cat_id,cat_id1,cat_id2,title,alias,introtext," +
							" content,tags,	meta_keyword,meta_title,meta_description,meta_noindex,meta_nofollow,picture,youtube_link," +
							" file_name,TYPE,user_id,username,admin_id,admin_name,create_date,edit_date,publish_date,expired_date,endvip_date," +
							" STATUS,is_hot,original_link,source,visible_user ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		
			ps.setInt(1, article.cat_id);
			ps.setInt(2, article.cat_id1);
			ps.setInt(3, article.cat_id2);
			ps.setString(4, article.title);
			ps.setString(5, article.alias);
			ps.setString(6, article.introtext);
			ps.setString(7, article.content);
			ps.setString(8, article.tags);
			ps.setString(9, article.meta_keyword);
			
			ps.setString(10,article.meta_title);
			ps.setString(11, article.meta_description);
			ps.setInt(12, article.meta_noindex );
			ps.setInt(13, article.meta_nofollow);
			ps.setString(14, article.picture);
			ps.setString(15, article.youtube_link);
			ps.setString(16, article.file_name);
			ps.setInt(17, article.type);
			ps.setInt(18,article.user_id);
			ps.setString(19, article.username);
			ps.setInt(20,article.admin_id);
			ps.setString(21, article.admin_name);
			
			ps.setLong(22,article.create_date);
			ps.setLong(23,article.edit_date);
			ps.setLong(24,article.publish_date);
			ps.setLong(25,article.expired_date);
			ps.setLong(26,article.endvip_date);
			ps.setInt(27,article.status);
			ps.setInt(28,article.is_hot);
			ps.setString(29, article.original_link);
			ps.setInt(30, article.source);
			ps.setInt(31, article.visible_user);
			ps.execute();

			conn.commit();
			conn.setAutoCommit(true);

			String sql = "SELECT LAST_INSERT_ID()";
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				id = resultSet.getInt(1);
			C3p0TuyensinhPool.attemptClose(conn);
			C3p0TuyensinhPool.attemptClose(resultSet);
			C3p0TuyensinhPool.attemptClose(ps);
			
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
	
	
	public void saveNewsShort(ArticleTS article) {
		PreparedStatement ps;		
		try {
			Connection conn = C3p0TuyensinhPool.getConnection(); 
			conn.setAutoCommit(false);
			ps = conn
					.prepareStatement("INSERT INTO tbl_articles_short(id,cat_id,cat_id1,cat_id2,title,alias,introtext," +
							" tags,	meta_keyword,meta_title,meta_description,meta_noindex,meta_nofollow,picture,youtube_link," +
							" file_name,TYPE,user_id,username,admin_id,admin_name,create_date,edit_date,publish_date,expired_date,endvip_date," +
							" STATUS,is_hot,original_link,source,visible_user ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			ps.setInt(1, article.id);
			ps.setInt(2, article.cat_id);
			ps.setInt(3, article.cat_id1);
			ps.setInt(4, article.cat_id2);
			ps.setString(5, article.title);
			ps.setString(6, article.alias);
			ps.setString(7, article.introtext);
			
			ps.setString(8, article.tags);
			ps.setString(9, article.meta_keyword);
			
			ps.setString(10,article.meta_title);
			ps.setString(11, article.meta_description);
			ps.setInt(12, article.meta_noindex );
			ps.setInt(13, article.meta_nofollow);
			ps.setString(14, article.picture);
			ps.setString(15, article.youtube_link);
			ps.setString(16, article.file_name);
			ps.setInt(17, article.type);
			ps.setInt(18,article.user_id);
			ps.setString(19, article.username);
			ps.setInt(20,article.admin_id);
			ps.setString(21, article.admin_name);
			
			ps.setLong(22,article.create_date);
			ps.setLong(23,article.edit_date);
			ps.setLong(24,article.publish_date);
			ps.setLong(25,article.expired_date);
			ps.setLong(26,article.endvip_date);
			ps.setInt(27,article.status);
			ps.setInt(28,article.is_hot);
			ps.setString(29, article.original_link);
			ps.setInt(30, article.source);
			ps.setInt(31, article.visible_user);
			ps.execute();

			conn.commit();
			conn.setAutoCommit(true);

			C3p0TuyensinhPool.attemptClose(conn);			
			C3p0TuyensinhPool.attemptClose(ps);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		
	}
	
	
	public void saveImage( int article_id,int category_id, String url) {

		String table = "tbl_article_image" ;
		PreparedStatement ps;		
		try {
			Connection conn = C3p0TuyensinhPool.getConnection(); 
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
			C3p0TuyensinhPool.attemptClose(conn);
			C3p0TuyensinhPool.attemptClose(ps);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void saveNewsShortLastest(ArticleTS article) {
		PreparedStatement ps;		
		try {
			Connection conn = C3p0TuyensinhPool.getConnection(); 
			conn.setAutoCommit(false);
			ps = conn
					.prepareStatement("INSERT INTO tbl_articles_short_latest(id,cat_id,cat_id1,cat_id2,title,alias,introtext," +
							" tags,	meta_keyword,meta_title,meta_description,meta_noindex,meta_nofollow,picture,youtube_link," +
							" file_name,TYPE,user_id,username,admin_id,admin_name,create_date,edit_date,publish_date,expired_date,endvip_date," +
							" STATUS,is_hot,original_link,source,visible_user ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			ps.setInt(1, article.id);
			ps.setInt(2, article.cat_id);
			ps.setInt(3, article.cat_id1);
			ps.setInt(4, article.cat_id2);
			ps.setString(5, article.title);
			ps.setString(6, article.alias);
			ps.setString(7, article.introtext);
			
			ps.setString(8, article.tags);
			ps.setString(9, article.meta_keyword);
			
			ps.setString(10,article.meta_title);
			ps.setString(11, article.meta_description);
			ps.setInt(12, article.meta_noindex );
			ps.setInt(13, article.meta_nofollow);
			ps.setString(14, article.picture);
			ps.setString(15, article.youtube_link);
			ps.setString(16, article.file_name);
			ps.setInt(17, article.type);
			ps.setInt(18,article.user_id);
			ps.setString(19, article.username);
			ps.setInt(20,article.admin_id);
			ps.setString(21, article.admin_name);
			
			ps.setLong(22,article.create_date);
			ps.setLong(23,article.edit_date);
			ps.setLong(24,article.publish_date);
			ps.setLong(25,article.expired_date);
			ps.setLong(26,article.endvip_date);
			ps.setInt(27,article.status);
			ps.setInt(28,article.is_hot);
			ps.setString(29, article.original_link);
			ps.setInt(30, article.source);
			ps.setInt(31, article.visible_user);
			ps.execute();

			conn.commit();
			conn.setAutoCommit(true);
		
			C3p0TuyensinhPool.attemptClose(conn);
			C3p0TuyensinhPool.attemptClose(ps);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		
	}
	
	
	public int saveTrainerAddress(TrainerAddess courses) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0TuyensinhPool.getConnection(); 
			conn.setAutoCommit(false);
			ps = conn.prepareStatement("INSERT INTO tbl_trainer_address (trainer_id,address,city_id,district_id," +
					" 	map_lat,map_long) VALUES (?,?,?,?,?,?);");
			ps.setInt(1, courses.trainer_id);
			ps.setString(2, courses.address);
			ps.setInt(3, courses.city_id);
			ps.setInt(4, courses.district_id);
			ps.setString(5, courses.map_lat);
			ps.setString(6, courses.map_long);
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			C3p0TuyensinhPool.attemptClose(conn);			
			C3p0TuyensinhPool.attemptClose(ps);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	public int saveTrainerContact (TrainerContact courses) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0TuyensinhPool.getConnection(); 
			conn.setAutoCommit(false);
			ps = conn.prepareStatement("INSERT INTO tbl_trainer_contact (trainer_id,name,email,tel,	fax," +
					" address,yahoo,skype,create_date) VALUES (?,?,?,?,?,?,?,?,?)");
			ps.setInt(1, courses.trainer_id);
			ps.setString(2, courses.name);
			ps.setString(3, courses.email);
			ps.setString(4, courses.tel);
			ps.setString(5, courses.fax);
			ps.setString(6, courses.address);
			ps.setString(7, courses.yahoo);
			ps.setString(8, courses.skype);
			ps.setLong(9, courses.create_date);
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			C3p0TuyensinhPool.attemptClose(conn);			
			C3p0TuyensinhPool.attemptClose(ps);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	

}
