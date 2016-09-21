package com.az24.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.az24.crawler.model.CategoryComic;
import com.az24.crawler.model.Comic;
import com.az24.crawler.model.ComicChapter;
import com.az24.crawler.model.ComicChapterFile;
import com.az24.db.pool.C3p0KKTPool;
import com.az24.util.UTF8Tool;

public class KKTDAO {
	
	public int SaveCategoryComic (CategoryComic categoryComic) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0KKTPool.getConnection(); 
			conn.setAutoCommit(false);
			ps = conn.prepareStatement("INSERT INTO c_category_comic (cat_id,comic_id) VALUES (?, ?)" );
			ps.setInt(1, categoryComic.cat_id);
			ps.setInt(2, categoryComic.comic_id);
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			C3p0KKTPool.attemptClose(ps);
			C3p0KKTPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	public int SaveComic (Comic comic) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0KKTPool.getConnection(); 
			conn.setAutoCommit(false);
			ps = conn.prepareStatement("INSERT INTO c_comic (title,image,c_chapter,author,content,tags,hit," +
					" isHot,STATUS,create_date,modify_date,create_user,modify_user,app_ids,title_no_sign,get_link_by,status_view )" +
					" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,1)" );
			
			ps.setString(1, comic.title);
			ps.setString(2, comic.image);
			ps.setInt(3, comic.c_chapter);
			ps.setString(4, comic.author);
			ps.setString(5, comic.content);
			ps.setString(6, comic.tags);
			ps.setInt(7, comic.hit);
			ps.setInt(8, comic.isHot);
			ps.setInt(9, comic.status);
			
			ps.setLong(10, comic.create_date);
			ps.setLong(11, comic.modify_date);
			ps.setString(12, comic.create_user);
			ps.setString(13, comic.modify_user);
			
			ps.setInt(14, comic.app_ids);
			ps.setString(15, comic.title_no_sign);
			ps.setInt(16, comic.get_link_by);
			
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
			String sql = "SELECT LAST_INSERT_ID()";
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				id = resultSet.getInt(1);
			
			C3p0KKTPool.attemptClose(ps);
			C3p0KKTPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	public void UpdateComic (int id, int c_chapter) {
		PreparedStatement ps;		
		try {
			Connection conn = C3p0KKTPool.getConnection(); 
			conn.setAutoCommit(false);
		
			ps = conn.prepareStatement("UPDATE c_comic SET c_chapter = ? Where id = ? " );
			ps.setInt(2, id);
			ps.setInt(1, c_chapter);
			ps.executeUpdate();
			conn.commit();
			conn.setAutoCommit(true);

			C3p0KKTPool.attemptClose(ps);
			C3p0KKTPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void UpdateComic (int id, String image) {
		PreparedStatement ps;		
		try {
			Connection conn = C3p0KKTPool.getConnection(); 
			conn.setAutoCommit(false);
		
			ps = conn.prepareStatement("UPDATE c_comic SET image = ? Where id = ? " );
			ps.setInt(2, id);
			ps.setString(1, image);
			ps.executeUpdate();
			conn.commit();
			conn.setAutoCommit(true);

			C3p0KKTPool.attemptClose(ps);
			C3p0KKTPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void UpdateComicChapter (int id, int total_file) {
		PreparedStatement ps;		
		try {
			Connection conn = C3p0KKTPool.getConnection(); 
			conn.setAutoCommit(false);
		
			ps = conn.prepareStatement("UPDATE c_comic_chapter SET total_file = ? Where id = ? " );
			ps.setInt(1, total_file);
			ps.setInt(2, id);
			ps.executeUpdate();
			conn.commit();
			conn.setAutoCommit(true);

			C3p0KKTPool.attemptClose(ps);
			C3p0KKTPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public int SaveComicChapter (ComicChapter comicChapter) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0KKTPool.getConnection(); 
			conn.setAutoCommit(false);
			ps = conn.prepareStatement("INSERT INTO c_comic_chapter (comic_id,title,create_date,modify_date" +
					",create_user,modify_user)	VALUES  (?,?,?,?,?,?)" );
			
			ps.setInt(1, comicChapter.comic_id);
			ps.setString(2, comicChapter.title);
			ps.setLong(3, comicChapter.create_date);
			ps.setLong(4, comicChapter.modify_date);
			ps.setString(5, comicChapter.create_user);
			ps.setString(6, comicChapter.modify_user);
			
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
			String sql = "SELECT LAST_INSERT_ID()";
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				id = resultSet.getInt(1);
			
			C3p0KKTPool.attemptClose(ps);
			C3p0KKTPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	
	public int SaveComicChapterFile (ComicChapterFile comicChapterFile) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0KKTPool.getConnection(); 
			conn.setAutoCommit(false);
			ps = conn.prepareStatement("INSERT INTO c_comic_chapter_file (link,link_source,chapter_id,comic_id," +
					"create_date,modify_date,create_user, modify_user ) " +
					" VALUES  (?,?,?,?,?,?,?,?)" );
			ps.setString(1, comicChapterFile.link);
			ps.setString(2, comicChapterFile.link_source);
			ps.setInt(3, comicChapterFile.chapter_id);
			ps.setInt(4, comicChapterFile.comic_id);
			ps.setLong(5, comicChapterFile.create_date);
			ps.setLong(6, comicChapterFile.modify_date);
			
			ps.setString(7, comicChapterFile.create_user);
			ps.setString(8, comicChapterFile.modify_user);
			
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			C3p0KKTPool.attemptClose(ps);
			C3p0KKTPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	public Comic getComic (int id) {
		PreparedStatement ps;	
		Comic comic = null;
		try {
			Connection conn = C3p0KKTPool.getConnection(); 
			ps = conn.prepareStatement("SELECT * FROM c_comic Where id =  "+id );
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()){
				comic = new Comic(); 
				comic.id = rs.getInt("id");
				comic.title = rs.getString("title");
			}
			
			C3p0KKTPool.attemptClose(ps);
			C3p0KKTPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return comic;
	}
	
	
	public int countFileInChapter (int chapter_id) {
		PreparedStatement ps;	
		int sl = 0;
		try {
			Connection conn = C3p0KKTPool.getConnection(); 
			ps = conn.prepareStatement("SELECT count(*) as sl FROM c_comic_chapter_file Where chapter_id =  "+chapter_id );
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()){
				sl = rs.getInt(1);
			}
			
			C3p0KKTPool.attemptClose(ps);
			C3p0KKTPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return sl;
	}
	
	
	public void updateChapter () {
		PreparedStatement ps;	
		try {
			Connection conn = C3p0KKTPool.getConnection(); 
			ps = conn.prepareStatement("SELECT id FROM c_comic_chapter Where total_file = 0  " );
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				int sl = countFileInChapter(rs.getInt("id"));
				System.out.println("Chuong "+rs.getInt("id") +":"+sl);
				UpdateComicChapter(rs.getInt("id"), sl);
			}
			
			C3p0KKTPool.attemptClose(ps);
			C3p0KKTPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public void updatAudioAlias () {
		PreparedStatement ps;	
		try {
			Connection conn = C3p0KKTPool.getConnection(); 
			ps = conn.prepareStatement("SELECT id,title FROM c_story_audio   " );
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				System.out.println("title "+rs.getString("title"));
				conn.setAutoCommit(false);
				ps = conn.prepareStatement("UPDATE c_story_audio SET title_no_sign = ? Where id = ? " );
				ps.setString(1, UTF8Tool.coDau2KoDau(rs.getString("title")));
				ps.setInt(2, rs.getInt("id"));
				ps.executeUpdate();
				conn.commit();
				conn.setAutoCommit(true);
			}
			
			C3p0KKTPool.attemptClose(ps);
			C3p0KKTPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		KKTDAO kktdao = new KKTDAO();
		kktdao.updatAudioAlias();
	}
}
