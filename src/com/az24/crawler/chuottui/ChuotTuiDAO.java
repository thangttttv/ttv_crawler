package com.az24.crawler.chuottui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ChuotTuiDAO {

	Connection conn = null;

	public void openConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			/*conn = DriverManager
					.getConnection(
							"jdbc:mysql://210.211.97.16:3306/hdc_Chuottui?autoReconnect=true&characterEncoding=UTF-8",
							"quangpn", "@!!!quang!(*^");*/
			conn = DriverManager
			.getConnection(
					"jdbc:mysql://192.168.1.103:3306/hdc_Chuottui?autoReconnect=true&characterEncoding=UTF-8",
					"quangpn", "@!!!quang!(*^");
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int saveMusic(Music music) {
		int id = 0;
		String sql = "INSERT INTO hdc_Chuottui.k_music	(categoryId, parentCategoryId,	artistId, singerId,"
				+ " 	list_extra_singer_id, userId, username,	crow, url_zing, url_nct, url_youtube, source_zing,"
				+ "  	source_nct,	source_youtube,	type_zing,	type_nct, name,	alias,	language , tag, status , isHot,"
				+ "   date_of_hot, isGood, date_of_good,create_date, update_date,singer_name,artist_name )  VALUES"
				+ "   (?, ?, ?, ?, ?, ?,"
				+ "?,?,?,?,?,?,?,?,"
				+ "?, ?, ?, ?, ?, ?, ?, ?," + " ?,?,?,?,?,?,? )";

		try {
			conn.setAutoCommit(false);
			PreparedStatement pStatement = conn.prepareStatement(sql);
			pStatement.setInt(1, music.categoryId);
			pStatement.setInt(2, music.parentCategoryId);
			
			pStatement.setInt(3, music.artistId);
			pStatement.setInt(4, music.singerId);

			pStatement.setString(5, music.list_extra_singer_id);
			pStatement.setInt(6, music.userId);
			pStatement.setString(7, music.username);
			pStatement.setInt(8, music.crow);

			pStatement.setString(9, music.url_zing);
			pStatement.setString(10, music.url_nct);
			pStatement.setString(11, music.url_youtube);
			pStatement.setString(12, music.source_zing);
			pStatement.setString(13, music.source_nct);
			pStatement.setString(14, music.source_youtube);
			pStatement.setInt(15, music.type_zing);
			pStatement.setInt(16, music.type_nct);

			pStatement.setString(17, music.name);
			pStatement.setString(18, music.alias);
			pStatement.setInt(19, music.language);

			pStatement.setString(20, music.tag);
			pStatement.setInt(21, music.status);
			pStatement.setInt(22, music.isHot);
			pStatement.setLong(23, music.date_of_hot);

			pStatement.setInt(24, music.isGood);
			pStatement.setLong(25, music.date_of_good);
			pStatement.setLong(26, music.create_date);
			pStatement.setLong(27, music.update_date);
			
			pStatement.setString(28, music.singer_name);
			pStatement.setString(29, music.artist_name);

			pStatement.execute();
			conn.commit();
			conn.setAutoCommit(true);

			sql = "SELECT LAST_INSERT_ID()";
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				id = resultSet.getInt(1);

		} catch (SQLException e) {
			
			//e.printStackTrace();
		}

		return id;

	}
	
	public int saveMusicHit(MusicHit music) {
		int id = 0;
		String sql = "INSERT INTO hdc_Chuottui.k_music_hit 	(musicId, artistId,  singerId,	hit, status, singer_name, artist_name ) VALUES" +
				"  (?,?,?,?,?,?,?)";
		try {
			conn.setAutoCommit(false);
			PreparedStatement pStatement = conn.prepareStatement(sql);
			pStatement.setInt(1, music.musicId);
			pStatement.setInt(2, music.artistId);
			pStatement.setInt(3, music.singerId);
			pStatement.setInt(4, music.hit);
			pStatement.setInt(5, music.status);
			pStatement.setString(6, music.singer_name);
			pStatement.setString(7, music.artist_name);
			pStatement.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;

	}
	
	public int saveMusicLike(MusicLike music) {
		int id = 0;
		String sql = " INSERT INTO hdc_Chuottui.k_like_music( k_like_music.musicId, k_like_music.like)" +
				"   VALUES (?,	? )";
		try {
			conn.setAutoCommit(false);
			PreparedStatement pStatement = conn.prepareStatement(sql);
			pStatement.setInt(1, music.musicId);
			pStatement.setInt(2, music.like);
			pStatement.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;

	}
	
	public int saveMusicReport(MusicReport music) {
		int id = 0;
		String sql = " INSERT INTO hdc_Chuottui.k_report_music	( musicId," +
				" 	report ) VALUES (?,?);";
		try {
			conn.setAutoCommit(false);
			PreparedStatement pStatement = conn.prepareStatement(sql);
			pStatement.setInt(1, music.musicId);
			pStatement.setInt(2, music.report);
			pStatement.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	public int saveMusicLyric(MusicLyrics music) {
		int id = 0;
		String sql = " INSERT INTO hdc_Chuottui.k_lyrics  (musicId," +
				" userId, username,	content, create_date,update_date)  VALUES" +
				" (?,?,?,?,?,?)";
		try {
			conn.setAutoCommit(false);
			PreparedStatement pStatement = conn.prepareStatement(sql);
			pStatement.setInt(1, music.musicId);
			pStatement.setInt(2, music.userId);
			pStatement.setString(3, music.username);
			pStatement.setString(4, music.content);
			pStatement.setLong(5, music.create_date);
			pStatement.setLong(6, music.create_date);
			pStatement.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	public int saveArtistRanking(ArtistRanking music) {
		int id = 0;
		String sql = " INSERT INTO hdc_Chuottui.k_artist_ranking  (artistId," +
				" ranking, value)  VALUES" +
				" (?,?,?)";
		try {
			conn.setAutoCommit(false);
			PreparedStatement pStatement = conn.prepareStatement(sql);
			pStatement.setInt(1, music.artistId);
			pStatement.setInt(2, music.ranking);
			pStatement.setInt(3, 200);
			pStatement.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	public int updateMusicLyric(MusicLyrics music) {
		int id = 0;
		String sql = " Update  hdc_Chuottui.k_lyrics SET content = ? Where musicId = ? ";
		try {
			conn.setAutoCommit(false);
			PreparedStatement pStatement = conn.prepareStatement(sql);
			pStatement.setInt(2, music.musicId);
			pStatement.setString(1, music.content);
			pStatement.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	public int saveMusicTransalte(MusicTranslate music) {
		int id = 0;
		String sql = " INSERT INTO hdc_Chuottui.k_translate (musicId," +
				"  userId, username, content, create_date, status )" +
				" VALUES (?, ?, ?, ?," +
				" ?,?);";
		try {
			conn.setAutoCommit(false);
			PreparedStatement pStatement = conn.prepareStatement(sql);
			pStatement.setInt(1, music.musicId);
			pStatement.setInt(2, music.userId);
			pStatement.setString(3, music.username);
			pStatement.setString(4, music.content);
			pStatement.setLong(5, music.create_date);
			pStatement.setInt(6, music.status);
			pStatement.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
			sql = "SELECT LAST_INSERT_ID()";
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				id = resultSet.getInt(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	public int saveMusicLikeStranslate(MusicLikeTranslate music) {
		int id = 0;
		String sql = " INSERT INTO hdc_Chuottui.k_like_translate ( musicId, " +
				"  translateId,	k_like_translate.like ) VALUES (?,?,?)";
		try {
			conn.setAutoCommit(false);
			PreparedStatement pStatement = conn.prepareStatement(sql);
			pStatement.setInt(1, music.musicId);
			pStatement.setInt(2, music.translateId);
			pStatement.setInt(3, music.like);
			pStatement.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;

	}
	
	public int getSinger(String name ) {
		int id = 0;
		String sql = "SELECT 	id, type, name, full_name,birthday, nationality, represent_company, " +
				" website,	picture, description, value, total_fan,	create_date, status FROM hdc_Chuottui.k_artist  Where" +
				" LOWER(name) = ? And (type=0 or type = 2) ";
		try {
			PreparedStatement pStatement = conn.prepareStatement(sql);			
			pStatement.setString(1,name);
			ResultSet rs =  pStatement.executeQuery();
			if(rs.next()) id = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;

	}
	
	public MusicArtist getSingerByName(String name) {
		String sql = "SELECT 	id, type, name, full_name,birthday, nationality, represent_company, " +
				" website,	picture, description, value, total_fan,	create_date, status FROM hdc_Chuottui.k_artist  Where" +
				" LOWER(name) = ? And (type=0 or type = 2) ";
		MusicArtist musicArtist = null;
		try {
			PreparedStatement pStatement = conn.prepareStatement(sql);			
			pStatement.setString(1,name);
			ResultSet rs =  pStatement.executeQuery();
			if(rs.next()) 
			{
				musicArtist = new MusicArtist();
				musicArtist.id =  rs.getInt(1);
				musicArtist.name = rs.getString("name");
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return musicArtist;

	}
	
	public int getAuthor(String name ) {
		int id = 0;
		String sql = "SELECT 	id, type, name, full_name,birthday, nationality, represent_company, " +
				" website,	picture, description, value, total_fan,	create_date, status FROM hdc_Chuottui.k_artist  Where" +
				" name = ? And (type=1 or type = 2) ";
		try {
			PreparedStatement pStatement = conn.prepareStatement(sql);			
			pStatement.setString(1,name);
			ResultSet rs =  pStatement.executeQuery();
			if(rs.next()) id = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;

	}
	
	public List<MusicArtist> getSingerByQG(String nationality ,int begin,int limit) {
		int id = 0;
		String sql = "SELECT 	id, type, name, full_name,birthday, nationality, represent_company, " +
				" website,	picture, description, value, total_fan,	create_date, status FROM hdc_Chuottui.k_artist  Where" +
				"  nationality = ? And (type=0 or type = 2) And id > "+begin +" order by id limit  0,"+limit;
		List<MusicArtist>  list = new ArrayList<MusicArtist>();
		try {
			this.openConnection();
			PreparedStatement pStatement = conn.prepareStatement(sql);			
			pStatement.setString(1,nationality);
			ResultSet rs =  pStatement.executeQuery();
			while(rs.next())
				{
					id = rs.getInt(1);
					MusicArtist  artist = new MusicArtist();
					artist.id = id;
					artist.name = rs.getString(3);
					list.add(artist);
				}
			this.closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;

	}
	
	public List<MusicArtist> getSingerAuMi(int begin,int limit) {
		int id = 0;
		String sql = "SELECT 	id, type, name, full_name,birthday, nationality, represent_company, " +
				" website,	picture, description, value, total_fan,	create_date, status FROM hdc_Chuottui.k_artist  Where " +
				" (type=0 or type = 2) And id= 52542  And nationality in (SELECT DISTINCT nationality FROM k_artist WHERE nationality NOT IN ('Malaysia','Taiwan','North Korea','Japan','Nhật Bản','Singapore','Trung Quốc','Hàn Quốc','China','Việt Nam','','South Korea'))  And id > "+begin +" order by id limit  0,"+limit;
		List<MusicArtist>  list = new ArrayList<MusicArtist>();
		try {
			this.openConnection();
			PreparedStatement pStatement = conn.prepareStatement(sql);			
			ResultSet rs =  pStatement.executeQuery();
			while(rs.next())
				{
					id = rs.getInt(1);
					MusicArtist  artist = new MusicArtist();
					artist.id = id;
					artist.name = rs.getString(3);
					list.add(artist);
				}
			this.closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;

	}
	
	public List<MusicArtist> getSingerHanQuoc(int begin,int limit) {
		int id = 0;
		String sql = "SELECT 	id, type, name, full_name,birthday, nationality, represent_company, " +
				" website,	picture, description, value, total_fan,	create_date, status FROM hdc_Chuottui.k_artist  Where" +
				" (type=0 or type = 2) And nationality in ('North Korea','South Korea')  And id > "+begin +" order by id limit  0,"+limit;
		List<MusicArtist>  list = new ArrayList<MusicArtist>();
		try {
			this.openConnection();
			PreparedStatement pStatement = conn.prepareStatement(sql);			
			ResultSet rs =  pStatement.executeQuery();
			while(rs.next())
				{
					id = rs.getInt(1);
					MusicArtist  artist = new MusicArtist();
					artist.id = id;
					artist.name = rs.getString(3);
					list.add(artist);
				}
			this.closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;

	}
	
	public List<MusicArtist> getSingerNhat(int begin,int limit) {
		int id = 0;
		String sql = "SELECT 	id, type, name, full_name,birthday, nationality, represent_company, " +
				" website,	picture, description, value, total_fan,	create_date, status FROM hdc_Chuottui.k_artist  Where" +
				" (type=0 or type = 2) And nationality in ('Japan','Nhật Bản')  And id > "+begin +" order by id limit  0,"+limit;
		List<MusicArtist>  list = new ArrayList<MusicArtist>();
		try {
			this.openConnection();
			PreparedStatement pStatement = conn.prepareStatement(sql);			
			ResultSet rs =  pStatement.executeQuery();
			while(rs.next())
				{
					id = rs.getInt(1);
					MusicArtist  artist = new MusicArtist();
					artist.id = id;
					artist.name = rs.getString(3);
					list.add(artist);
				}
			this.closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;

	}
	
	
	public List<MusicArtist> getSingerHoa(int begin,int limit) {
		int id = 0;
		String sql = "SELECT 	id, type, name, full_name,birthday, nationality, represent_company, " +
				" website,	picture, description, value, total_fan,	create_date, status FROM hdc_Chuottui.k_artist  Where" +
				" (type=0 or type = 2) And nationality in ('Singapore','Trung Quốc','China','Malaysia','Taiwan')  And id > "+begin +" order by id limit  0,"+limit;
		List<MusicArtist>  list = new ArrayList<MusicArtist>();
		try {
			this.openConnection();
			PreparedStatement pStatement = conn.prepareStatement(sql);			
			ResultSet rs =  pStatement.executeQuery();
			while(rs.next())
				{
					id = rs.getInt(1);
					MusicArtist  artist = new MusicArtist();
					artist.id = id;
					artist.name = rs.getString(3);
					list.add(artist);
				}
			this.closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;

	}
	
	
	
	public int checkMusicExits(String name,int singerID ) {
		int id = 0;
		String sql = "SELECT id from k_music where name like ? And singerId = ? ";
		try {
			PreparedStatement pStatement = conn.prepareStatement(sql);			
			pStatement.setString(1,"%"+name+"%");
			pStatement.setInt(2,singerID);
			ResultSet rs =  pStatement.executeQuery();
			if(rs.next()) id = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;

	}
	
	public int checkMusicBackExits(int musicId,int type) {
		int id = 0;
		String sql = "SELECT 	id,	musicId, NAME, CODE, supplier, DATE, TYPE, isHot, date_of_hot,	isLastest, " +
				" date_of_lastest,isFunny,	date_of_funny,	isPlayer,date_of_player,create_date,hot_position,lastest_position," +
				" funny_position	FROM hdc_Chuottui.k_ringtone Where type= ? and  musicId = ?  ";
		try {
			PreparedStatement pStatement = conn.prepareStatement(sql);			
			pStatement.setInt(1,type);
			pStatement.setInt(2,musicId);
			ResultSet rs =  pStatement.executeQuery();
			if(rs.next()) id = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;

	}
	
	public int checkLyric(int music_id) {
		int id = 0;
		String sql = "SELECT id from k_lyrics where musicId = ? And content != '' ";
		try {
			PreparedStatement pStatement = conn.prepareStatement(sql);			
			pStatement.setInt(1,music_id);
			ResultSet rs =  pStatement.executeQuery();
			if(rs.next()) id = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;

	}
	
	public int checkMusicTranslate(int musicID) {
		int id = 0;
		String sql = "SELECT id FROM  hdc_Chuottui.k_translate where musicId = ? ";
		try {
			PreparedStatement pStatement = conn.prepareStatement(sql);
			pStatement.setInt(1,musicID);
			ResultSet rs =  pStatement.executeQuery();
			if(rs.next()) id = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;

	}
	
	
	public void gopSinger() {
		int singer_id = 0,singer_gop_id=0;String name = "";
		String sql = "SELECT 	id, type, name, full_name,birthday, nationality, represent_company, " +
		" website,	picture, description, value, total_fan,	create_date, status FROM hdc_Chuottui.k_artist  Where" +
		" id > ? And (type=0 or type = 2) order by id asc limit 100 ";
		
		String sql_2 = "SELECT 	id, type, name, full_name,birthday, nationality, represent_company, " +
		" website,	picture, description, value, total_fan,	create_date, status FROM hdc_Chuottui.k_artist  Where " +
		" id > ? And name = ? And (type=0 or type = 2) ";
		
		String sql_3 = "Update k_music set singerId = ? Where singerId = ? ";
		
		String sql_5 = "UPDATE k_music_hit SET singerId = ? WHERE singerId = ?" ;

		String sql_4 = "Delete From k_artist  Where id = ? ";
		
		int i = 0;int k = 0;
		while(i<100000)
		{
			try {
				this.openConnection();
				PreparedStatement pStatement = conn.prepareStatement(sql);		
				pStatement.setInt(1, i);
				ResultSet rs =  pStatement.executeQuery();
				k=0;
				while(rs.next())
				  {
						singer_id = rs.getInt(1);	
						name = rs.getString(3);	
						System.out.println(new Date()+" Process ID = " + singer_id + " " + name);
						
						PreparedStatement pStatement2 = conn.prepareStatement(sql_2);
						pStatement2.setInt(1,singer_id);
						pStatement2.setString(2, name);
						ResultSet rs2 =  pStatement2.executeQuery();
						
						while(rs2.next())
						{
							this.conn.setAutoCommit(false);
							singer_gop_id = rs2.getInt(1);
							System.out.println(new Date()+" Gop ID = " + singer_gop_id );
							PreparedStatement pStatement3 = conn.prepareStatement(sql_3);
							pStatement3.setInt(1, singer_id);
							pStatement3.setInt(2, singer_gop_id);
							pStatement3.execute();
							
							
							PreparedStatement pStatement5 = conn.prepareStatement(sql_5);
							pStatement5.setInt(1, singer_id);
							pStatement5.setInt(2, singer_gop_id);
							pStatement5.execute();
							
							
							PreparedStatement pStatement4 = conn.prepareStatement(sql_4);
							pStatement4.setInt(1, singer_gop_id);
							pStatement4.execute();
							
							this.conn.commit();
							this.conn.setAutoCommit(true);
						}
						
						i = singer_id;
						k++;
					}
				
				if(k==0)break;
				
			  this.closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void gopTacGia() {
		int singer_id = 0,singer_gop_id=0;String name = "";
		String sql = "SELECT 	id, type, name, full_name,birthday, nationality, represent_company, " +
		" website,	picture, description, value, total_fan,	create_date, status FROM hdc_Chuottui.k_artist  Where" +
		" id > ? And (type=1 or type = 2) order by id asc limit 100 ";
		
		String sql_2 = "SELECT 	id, type, name, full_name,birthday, nationality, represent_company, " +
		" website,	picture, description, value, total_fan,	create_date, status FROM hdc_Chuottui.k_artist  Where" +
		" id > ? And name = ? And (type=1 or type = 2)";
		
		String sql_3 = "Update k_music set artistId = ? Where artistId = ? ";
		
		String sql_5 = "UPDATE k_music_hit SET artistId = ? WHERE artistId = ?" ;

		String sql_4 = "Delete From k_artist  Where id = ? ";
		
		int i = 0;int k = 0;
		while(i<100000)
		{
			try {
				this.openConnection();
				PreparedStatement pStatement = conn.prepareStatement(sql);		
				pStatement.setInt(1, i);
				ResultSet rs =  pStatement.executeQuery();
				k=0;
				while(rs.next())
				  {
						singer_id = rs.getInt(1);	
						name = rs.getString(3);	
						System.out.println(new Date()+" Process ID = " + singer_id + " " + name);
						
						PreparedStatement pStatement2 = conn.prepareStatement(sql_2);
						pStatement2.setInt(1,singer_id);
						pStatement2.setString(2, name);
						ResultSet rs2 =  pStatement2.executeQuery();
						
						while(rs2.next())
						{
							this.conn.setAutoCommit(false);
							singer_gop_id = rs2.getInt(1);
							System.out.println(new Date()+" Gop ID = " + singer_gop_id );
							PreparedStatement pStatement3 = conn.prepareStatement(sql_3);
							pStatement3.setInt(1, singer_id);
							pStatement3.setInt(2, singer_gop_id);
							pStatement3.execute();
							
							
							PreparedStatement pStatement5 = conn.prepareStatement(sql_5);
							pStatement5.setInt(1, singer_id);
							pStatement5.setInt(2, singer_gop_id);
							pStatement5.execute();
							
							
							PreparedStatement pStatement4 = conn.prepareStatement(sql_4);
							pStatement4.setInt(1, singer_gop_id);
							pStatement4.execute();
							
							this.conn.commit();
							this.conn.setAutoCommit(true);
						}
						
						i = singer_id;
						k++;
					}
				
				if(k==0)break;
				
			  this.closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void gopBaiHat() {
		int music_id = 0,music_gop_id=0;String name = "";int singerId = 0;
		String sql_1 = "SELECT id, name,singerId FROM hdc_Chuottui.k_music  Where" +
		" id > ?  order by id asc limit 100 ";
		
		String sql_2 = "SELECT id, name,singerId FROM hdc_Chuottui.k_music  Where  " +
		" id > ? And name = ? And singerId = ? ";
		
		String sql_3 = "Delete From k_lyrics  Where musicId = ? ";	

		String sql_4 = "Delete From k_music_hit  Where musicId = ? ";
		
		String sql_5 = "Delete From k_report_music  Where musicId = ? " ;
		
		String sql_6 = "Delete From k_like_music  Where musicId = ? " ;
		
		String sql_7 = "Delete From k_music  Where id = ? " ;
		
		int i = 0;int k = 0;
		while(i<165413)
		{
			try {
				this.openConnection();
				PreparedStatement pStatement = conn.prepareStatement(sql_1);	
				pStatement.setInt(1,i);
				ResultSet rs =  pStatement.executeQuery();
				k=0;
				while(rs.next())
				  {
						music_id = rs.getInt(1);	
						name = rs.getString(2);	
						singerId = rs.getInt(3);
						
						System.out.println(new Date()+" Process Bai Hat ID = " + music_id + " " + name);
						
						PreparedStatement pStatement2 = conn.prepareStatement(sql_2);
						pStatement2.setInt(1,music_id);
						pStatement2.setString(2, name);
						pStatement2.setInt(3,singerId);
						
						ResultSet rs2 =  pStatement2.executeQuery();
						
						while(rs2.next())
						{
							this.conn.setAutoCommit(false);
							music_gop_id = rs2.getInt(1);
							System.out.println(new Date()+" Gop ID = " + music_gop_id +":"+ rs2.getString(2));
							
							PreparedStatement pStatement3 = conn.prepareStatement(sql_3);
							pStatement3.setInt(1, music_gop_id);
							pStatement3.execute();
							
							
							PreparedStatement pStatement4 = conn.prepareStatement(sql_4);
							pStatement4.setInt(1, music_gop_id);						
							pStatement4.execute();
							
							
							PreparedStatement pStatement5 = conn.prepareStatement(sql_5);
							pStatement5.setInt(1, music_gop_id);
							pStatement5.execute();
							
							PreparedStatement pStatement6 = conn.prepareStatement(sql_6);
							pStatement6.setInt(1, music_gop_id);
							pStatement6.execute();
							
							PreparedStatement pStatement7 = conn.prepareStatement(sql_7);
							pStatement7.setInt(1, music_gop_id);
							pStatement7.execute();
							
							this.conn.commit();
							this.conn.setAutoCommit(true);
						}
					
						i = music_id;
						k++;
					}
				
				if(k==0)break;
				
			  this.closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public int saveSinger(MusicArtist music) {
		int id = 0;
		String sql = "INSERT INTO hdc_Chuottui.k_artist ( type, name, full_name, birthday, nationality, " +
				"	represent_company, website, picture, description,value,	total_fan, create_date, status" +
				"  )	VALUES (?,?,?,?,?,?," +
				"	?,?,?,?,?,?,?)";
		try {
			conn.setAutoCommit(false);
			PreparedStatement pStatement = conn.prepareStatement(sql);
			pStatement.setInt(1, music.type);
			pStatement.setString(2, music.name);
			pStatement.setString(3,music.full_name!=null?music.full_name.trim():"");
			pStatement.setString(4,music.birthday!=null?music.birthday.trim():"");
			pStatement.setString(5,music.nationality!=null? music.nationality.trim():"");
			pStatement.setString(6, music.represent_company!=null?music.represent_company.trim():"");
			pStatement.setString(7, music.website);
			pStatement.setString(8, music.picture);
			pStatement.setString(9, music.description);
			pStatement.setInt(10, 200);
			pStatement.setInt(11, music.total_fan);
			pStatement.setLong(12, music.create_date);
			pStatement.setInt(13, music.status);
			pStatement.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
			sql = "SELECT LAST_INSERT_ID()";
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				id = resultSet.getInt(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;

	}
	
	public int saveNhacCho(RingBack music) {
		int id = 0;
		String sql = "INSERT INTO hdc_Chuottui.k_ringtone ( musicId, NAME, CODE, supplier, DATE, TYPE, isHot, date_of_hot, isLastest," +
				" 	date_of_lastest,isFunny,date_of_funny,isPlayer,date_of_player,create_date,hot_position,lastest_position,funny_position )VALUES" +
				"  (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
		try {
			conn.setAutoCommit(false);
			PreparedStatement pStatement = conn.prepareStatement(sql);
			pStatement.setInt(1, music.musicId);
			pStatement.setString(2, music.name);
			pStatement.setString(3,music.code);
			pStatement.setInt(4,music.supplier);
			pStatement.setLong(5,music.date);
			pStatement.setInt(6, music.type);
			pStatement.setInt(7, music.isHot);
			pStatement.setLong(8, music.date_of_hot);
			pStatement.setInt(9, music.isLastest);
			pStatement.setLong(10, music.date_of_lastest);
			pStatement.setInt(11, music.isFunny);
			pStatement.setLong(12, music.date_of_funny);
			pStatement.setInt(13, music.isPlayer);
			pStatement.setLong(14, music.date_of_player);
			pStatement.setLong(15, music.create_date);
			pStatement.setInt(16, music.hot_position);
			pStatement.setInt(17, music.lastest_position);
			pStatement.setInt(18, music.funny_position);
			pStatement.execute();
			conn.commit();
			conn.setAutoCommit(true);
			sql = "SELECT LAST_INSERT_ID()";
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				id = resultSet.getInt(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;

	}
	
	public static void main(String[] args) {
		ChuotTuiDAO  chuotTuiDAO = new ChuotTuiDAO();
		chuotTuiDAO.gopBaiHat();
	}
}
