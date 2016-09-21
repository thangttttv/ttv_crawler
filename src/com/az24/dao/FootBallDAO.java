package com.az24.dao;

import hdc.util.text.StringUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.az24.crawler.model.EventFootball;
import com.az24.crawler.model.FBChart;
import com.az24.crawler.model.FBClub;
import com.az24.crawler.model.FBCoach;
import com.az24.crawler.model.FBCountry;
import com.az24.crawler.model.FBCup;
import com.az24.crawler.model.FBFootballer;
import com.az24.crawler.model.FBMatch;
import com.az24.crawler.model.FBMatchComparator;
import com.az24.crawler.model.FBMatchLive;
import com.az24.crawler.model.FBMatchStatictis;
import com.az24.crawler.model.FBMatchTeam;
import com.az24.crawler.model.FBTyLe;
import com.az24.db.pool.C3p0FootBallPool;
import com.az24.db.pool.C3p0ShopPool;
import com.az24.db.pool.DBConfig;
import com.az24.test.Base64Coder;
import com.az24.util.UTF8Tool;
import com.az24.util.io.FileUtil;
import com.ttv.football.CountryUnit;


public class FootBallDAO {
	
	public static FBClub getClub(String club_name) {
		PreparedStatement ps;
		FBClub club = null;
		try {
			Connection connection = C3p0FootBallPool.getConnection();
			String sql = "SELECT id,CODE,NAME,country,logo,date_format(create_date,'/%Y/%m%d/') as ymd  FROM  vtc_bongda.fb_club WHERE UPPER(TRIM(name))  = ? ";
			ps = connection.prepareStatement(sql);
			ps.setString(1, club_name.toUpperCase());
			ResultSet rs =	ps.executeQuery();
			if(rs.next())
			{
				club = new FBClub();
				club.id = rs.getInt(1);
				club.code = rs.getString(2);
				club.name = rs.getString(3);
				club.country = rs.getString(4);
				club.logo = rs.getString(5);
				club.ymd = rs.getString("ymd");
				
			}
			C3p0FootBallPool.attemptClose(rs);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return club;
	}
	
	public static FBClub getClubJoinCoach(String club_name) {
		PreparedStatement ps;
		FBClub club = null;
		try {
			Connection connection = C3p0FootBallPool.getConnection();
			ps = connection.prepareStatement("SELECT cl.id,cl.code,cl.name,cl.country,cl.logo,date_format(cl.create_date,'/%Y/%m%d/') as ymd,c.name_en as coachname  FROM  vtc_bongda.fb_club cl left join fb_coach"
					+ " c On cl.coach_id=c.id  WHERE UPPER(TRIM(cl.name))  = ?");
			ps.setString(1, club_name.toUpperCase());
			ResultSet rs =	ps.executeQuery();
			if(rs.next())
			{
				club = new FBClub();
				club.id = rs.getInt(1);
				club.code = rs.getString(2);
				club.name = rs.getString(3);
				club.country = rs.getString(4);
				club.logo = rs.getString(5);
				club.ymd = rs.getString("ymd");
				club.coachname = rs.getString("coachname");
				
			}
			C3p0FootBallPool.attemptClose(rs);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return club;
	}
	
	public static FBClub getClubByCode(String code) {
		PreparedStatement ps;
		FBClub club = null;
		try {
			Connection connection = C3p0FootBallPool.getConnection();
			ps = connection.prepareStatement("SELECT id,CODE,NAME,country,logo  FROM  vtc_bongda.fb_club WHERE UPPER(TRIM(CODE))  = ?");
			ps.setString(1, code.toUpperCase());
			ResultSet rs =	ps.executeQuery();
			if(rs.next())
			{
				club = new FBClub();
				club.id = rs.getInt(1);
				club.code = rs.getString(2);
				club.name = rs.getString(3);
				club.country = rs.getString(4);
				club.logo = rs.getString(5);
				
			}
			C3p0FootBallPool.attemptClose(rs);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return club;
	}
	
	public static FBClub getClubByNamen(String club_name) {
		PreparedStatement ps;
		FBClub club = null;
		try {
			Connection connection = C3p0FootBallPool.getConnection();
			ps = connection.prepareStatement("SELECT id,CODE,NAME,country,logo  FROM  vtc_bongda.fb_club WHERE UPPER(TRIM(name_en))  = ?");
			ps.setString(1, club_name.toUpperCase());
			ResultSet rs =	ps.executeQuery();
			if(rs.next())
			{
				club = new FBClub();
				club.id = rs.getInt(1);
				club.code = rs.getString(2);
				club.name = rs.getString(3);
				club.country = rs.getString(4);
				club.logo = rs.getString(5);
				
			}
			C3p0FootBallPool.attemptClose(rs);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return club;
	}
	
	public static FBClub getClubByNamenOrID7M(String id7m,String club_name) {
		PreparedStatement ps;
		FBClub club = null;
		try {
			Connection connection = C3p0FootBallPool.getConnection();
			ps = connection.prepareStatement("SELECT id,CODE,NAME,country,logo  FROM  vtc_bongda.fb_club WHERE UPPER(TRIM(name_en))  = ?"
					+ " OR id_7m ='"+id7m+"'");
			ps.setString(1, club_name.toUpperCase());
			ResultSet rs =	ps.executeQuery();
			if(rs.next())
			{
				club = new FBClub();
				club.id = rs.getInt(1);
				club.code = rs.getString(2);
				club.name = rs.getString(3);
				club.country = rs.getString(4);
				club.logo = rs.getString(5);
				
			}
			C3p0FootBallPool.attemptClose(rs);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return club;
	}
	
	public static FBClub getClubByID(int id) {
		PreparedStatement ps;
		FBClub club = null;
		try {
			Connection connection = C3p0FootBallPool.getConnection();
			ps = connection.prepareStatement("SELECT id,CODE,NAME,country,coach_id,logo,date_format(create_date,'/%Y/%m%d/') as ymd  FROM  vtc_bongda.fb_club WHERE id  = ?");
			ps.setInt(1, id);
			ResultSet rs =	ps.executeQuery();
			if(rs.next())
			{
				club = new FBClub();
				club.id = rs.getInt(1);
				club.coach_id = rs.getInt("coach_id");
				club.code = rs.getString(2);
				club.name = rs.getString(3);
				club.country = rs.getString(4);
				club.logo = rs.getString("logo");
				club.ymd = rs.getString("ymd");
				
			}
			C3p0FootBallPool.attemptClose(rs);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return club;
	}
	
	public static FBCoach getCoach(int coach_id) {
		PreparedStatement ps;
		FBCoach club = null;
		try {
			Connection connection = C3p0FootBallPool.getConnection();
			ps = connection.prepareStatement("SELECT *  FROM  vtc_bongda.fb_coach WHERE id = ?");
			ps.setInt(1, coach_id);
			ResultSet rs =	ps.executeQuery();
			if(rs.next())
			{
				club = new FBCoach();
				club.id = rs.getInt(1);
				club.name = rs.getString("name");
				club.name_en = rs.getString("name_en");
				club.country = rs.getString("country");
				
				
			}
			C3p0FootBallPool.attemptClose(rs);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return club;
	}
	
	public static int countMatch() {
		PreparedStatement ps;
		int kq = 0;
		try {
			Connection connection = C3p0FootBallPool.getConnection();
			ps = connection.prepareStatement("SELECT count(*) as sl  FROM  vtc_bongda.fb_match  ");
			ResultSet rs =	ps.executeQuery();
			if(rs.next())
			{
				kq = rs.getInt("sl");
			}
			C3p0FootBallPool.attemptClose(rs);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return kq;
	}
	
	
	public static ArrayList<FBMatch> getListMatch(int page,int limit) {
		PreparedStatement ps;
		FBMatch club = null;
		ArrayList<FBMatch> listMatch = new ArrayList<FBMatch>();
		try {
			Connection connection = C3p0FootBallPool.getConnection();
			int from = (page-1)*limit;
			ps = connection.prepareStatement("SELECT *,DATE_FORMAT(match_time,'%h:%i') as hi  FROM  vtc_bongda.fb_match  limit "+from+","+limit);
			ResultSet rs =	ps.executeQuery();
			while(rs.next())
			{
				club = new FBMatch();
				club.id = rs.getInt(1);
				club.club_code_1 = rs.getString("club_code_1");
				club.club_code_2 = rs.getString("club_code_2");
				club.club_id_1 = rs.getInt("club_id_1");
				club.club_id_2 = rs.getInt("club_id_2");
				club.result = rs.getString("result");
				club.result_1 = rs.getString("result_1");
				club.round = rs.getString("round");
				club.match_minute = rs.getString("match_minute");
				club.hi = rs.getString("hi");
				listMatch.add(club);
				
			}
			C3p0FootBallPool.attemptClose(rs);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listMatch;
	}
	
	public static ArrayList<FBMatch> getListMatchErroLogo() {
		PreparedStatement ps;
		FBMatch club = null;
		ArrayList<FBMatch> listMatch = new ArrayList<FBMatch>();
		try {
			Connection connection = C3p0FootBallPool.getConnection();
			
			ps = connection.prepareStatement("SELECT *,DATE_FORMAT(match_time,'%h:%i') as hi  FROM  vtc_bongda.fb_match Where"
					+ " (club_logo_1 = NULL OR club_logo_1 =\"\") OR (club_logo_2 = NULL OR club_logo_2 =\"\")   ");
			ResultSet rs =	ps.executeQuery();
			while(rs.next())
			{
				club = new FBMatch();
				club.id = rs.getInt(1);
				club.club_code_1 = rs.getString("club_code_1");
				club.club_code_2 = rs.getString("club_code_2");
				club.club_id_1 = rs.getInt("club_id_1");
				club.club_id_2 = rs.getInt("club_id_2");
				club.result = rs.getString("result");
				club.result_1 = rs.getString("result_1");
				club.round = rs.getString("round");
				club.match_minute = rs.getString("match_minute");
				club.hi = rs.getString("hi");
				listMatch.add(club);
				
			}
			C3p0FootBallPool.attemptClose(rs);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listMatch;
	}
	
	public static ArrayList<FBMatch> getListMatchErro() {
		PreparedStatement ps;
		FBMatch club = null;
		ArrayList<FBMatch> listMatch = new ArrayList<FBMatch>();
		try {
			Connection connection = C3p0FootBallPool.getConnection();
			//
			ps = connection.prepareStatement("SELECT * FROM fb_match WHERE match_time < DATE_SUB(NOW(),INTERVAL 5 MINUTE)  AND ( STATUS =''  ) UNION SELECT * FROM fb_match WHERE match_time < DATE_SUB(NOW(),INTERVAL 2 HOUR)  AND (STATUS ='Live' OR STATUS =''  )");
			ResultSet rs =	ps.executeQuery();
			while(rs.next())
			{
				club = new FBMatch();
				club.id = rs.getInt(1);
				club.club_code_1 = rs.getString("club_code_1");
				club.club_code_2 = rs.getString("club_code_2");
				club.club_id_1 = rs.getInt("club_id_1");
				club.club_id_2 = rs.getInt("club_id_2");
				club.result = rs.getString("result");
				club.result_1 = rs.getString("result_1");
				club.round = rs.getString("round");
				club.match_minute = rs.getString("match_minute");
				club.url_code = rs.getString("url_code");
				listMatch.add(club);
				
			}
			C3p0FootBallPool.attemptClose(rs);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listMatch;
	}
	
	public static ArrayList<FBMatch> processtMatchNullBDWapID() {
		PreparedStatement ps;
		FBMatch club = null;
		ArrayList<FBMatch> listMatch = new ArrayList<FBMatch>();
		try {
			Connection connection = C3p0FootBallPool.getConnection();
			
			ps = connection.prepareStatement("SELECT * FROM fb_match WHERE bdwap_id IS NULL ");
			ResultSet rs =	ps.executeQuery();
			while(rs.next())
			{
				club = new FBMatch();
				club.id = rs.getInt(1);
				club.club_code_1 = rs.getString("club_code_1");
				club.club_code_2 = rs.getString("club_code_2");
				club.club_id_1 = rs.getInt("club_id_1");
				club.club_id_2 = rs.getInt("club_id_2");
				club.result = rs.getString("result");
				club.result_1 = rs.getString("result_1");
				club.round = rs.getString("round");
				club.match_minute = rs.getString("match_minute");
				
				club.url_code = rs.getString("url_code");
				club.url_code = Base64Coder.decodeString(club.url_code );
				
				Pattern r =   Pattern.compile("-(\\d+)\\.html");   
			    Matcher m = r.matcher(club.url_code);
				 String bdwap_id = null;
		    		if(m.find())
				      { bdwap_id = m.group(1);
				      }
		    		club.bdwap_id =bdwap_id;
		    		updateMatchBDWapID(club.id, bdwap_id);
				listMatch.add(club);
				
			}
			C3p0FootBallPool.attemptClose(rs);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listMatch;
	}
	
	public static ArrayList<FBMatch> getListMatchByDate(String match_date) throws ParseException {
		PreparedStatement ps;
		FBMatch club = null;
		ArrayList<FBMatch> listMatch = new ArrayList<FBMatch>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
		try {
			Connection connection = C3p0FootBallPool.getConnection();
			ps = connection.prepareStatement("SELECT c.name as cup,c.logo,m.id,m.cup_id,m.round,m.club_id_1,m.club_id_2,m.club_code_1,m.club_code_2,"
					+ "m.club_name_1,m.club_name_2,m.club_logo_1,m.club_logo_2,m.result,m.result_1,m.status,m.match_time,m.match_minute,c.create_date,c.rate,"
					+ "date_format(c.create_date,'/%Y/%m%d/') as ymd,m.club_redcard_1,m.club_redcard_2,m.had_video   "
					+ "FROM fb_match m  LEFT JOIN fb_cup c ON m.cup_id = c.id  WHERE  date_format(m.match_time,'%d-%m-%Y') = ? ORDER BY c.id    ");
			ps.setString(1,match_date);
			ResultSet rs =	ps.executeQuery();
			while(rs.next())
			{
				club = new FBMatch();
				club.id = rs.getInt("id");
				club.club_code_1 = rs.getString("club_code_1");
				club.club_code_2 = rs.getString("club_code_2");
				club.club_name_1 = rs.getString("club_name_1");
				club.club_name_2 = rs.getString("club_name_2");
				club.club_id_1 = rs.getInt("club_id_1");
				club.club_id_2 = rs.getInt("club_id_2");
				club.result = rs.getString("result");
				club.result_1 = rs.getString("result_1");
				club.round = rs.getString("round");
				club.match_minute = rs.getString("match_minute");
				club.match_time = rs.getString("match_time");
				club.status = rs.getString("status");
				club.ymd = rs.getString("ymd");
				club.cup_rate = rs.getInt("rate");
				club.cup  = rs.getString("cup");
				club.cup_id  = rs.getInt("cup_id");
				
				club.logo = rs.getString("logo");
				club.club_logo_1 = rs.getString("club_logo_1");
				club.club_logo_2 = rs.getString("club_logo_2");
				
				club.club_redcard_1  = rs.getInt("club_redcard_1");
				club.club_redcard_2  = rs.getInt("club_redcard_2");
				//club.had_video  = rs.getInt("had_video");
				club.had_video  = 0;
				
				if(!StringUtil.isEmpty(club.logo))
					club.logo = "http://kenhkiemtien.com/"+"upload/bongda/cup"+club.ymd+"/"+club.logo; 
				if(!StringUtil.isEmpty(club.club_logo_1))
					club.club_logo_1 = "http://kenhkiemtien.com/"+"upload/bongda/club"+club.club_logo_1; 
				if(!StringUtil.isEmpty(club.club_logo_2))
					club.club_logo_2 = "http://kenhkiemtien.com/"+"upload/bongda/club"+club.club_logo_2; 
				
	       	
			    Date parsedDate = dateFormat.parse(rs.getString("match_time"));
			    long beginTT = parsedDate.getTime()-30*60*1000;
			    long endTT = parsedDate.getTime()+140*60*1000;
			    int rateTime = (beginTT<=Calendar.getInstance().getTimeInMillis()&&Calendar.getInstance().getTimeInMillis()<=endTT)?1:0;
				club.match_rate = (4-club.cup_rate)+rateTime*5;
				listMatch.add(club);
				
			}
			C3p0FootBallPool.attemptClose(rs);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		Collections.sort(listMatch, new FBMatchComparator());
		return listMatch;
	}
	
	
	public static ArrayList<FBMatch> getListMatchLive() throws ParseException {
		PreparedStatement ps;
		FBMatch club = null;
		ArrayList<FBMatch> listMatch = new ArrayList<FBMatch>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
		try {
			Connection connection = C3p0FootBallPool.getConnection();
			ps = connection.prepareStatement("SELECT c.name as cup,c.logo,m.id,m.cup_id,m.round,m.club_id_1,m.club_id_2,m.club_code_1,"
					+ "m.club_code_2 ,m.club_name_1,m.club_name_2,m.club_logo_1,m.club_logo_2,m.result,m.result_1,m.status,"
					+ "m.match_minute,m.match_time,c.create_date,c.rate,date_format(c.create_date,'/%Y/%m%d/') as ymd,m.club_redcard_1,m.club_redcard_2,m.had_video  FROM fb_match m LEFT JOIN fb_cup c ON m.cup_id = c.id  "
					+ "WHERE  m.match_time < NOW() AND m.match_time > DATE_SUB(NOW(),INTERVAL 1 DAY) AND (m.status = 'Live' OR m.status = 'HT') "
					+ " ORDER BY m.match_time DESC   ");
			ResultSet rs =	ps.executeQuery();
			while(rs.next())
			{
				club = new FBMatch();
				club.id = rs.getInt("id");
				club.club_code_1 = rs.getString("club_code_1");
				club.club_code_2 = rs.getString("club_code_2");
				club.club_id_1 = rs.getInt("club_id_1");
				club.club_id_2 = rs.getInt("club_id_2");
				club.club_name_1 = rs.getString("club_name_1");
				club.club_name_2 = rs.getString("club_name_2");
				club.result = rs.getString("result");
				club.result_1 = rs.getString("result_1");
				club.round = rs.getString("round");
				club.match_minute = rs.getString("match_minute");
				club.match_time = rs.getString("match_time");
				club.status = rs.getString("status");
				club.ymd = rs.getString("ymd");
				club.cup_rate = rs.getInt("rate");
				club.cup  = rs.getString("cup");
				club.cup_id  = rs.getInt("cup_id");
				
				club.club_redcard_1  = rs.getInt("club_redcard_1");
				club.club_redcard_2  = rs.getInt("club_redcard_2");
				//club.had_video  = rs.getInt("had_video");
				club.had_video  =0;
				club.logo = rs.getString("logo");
				club.club_logo_1 = rs.getString("club_logo_1");
				club.club_logo_2 = rs.getString("club_logo_2");
				
				if(!StringUtil.isEmpty(club.logo))
					club.logo = "http://kenhkiemtien.com/"+"upload/bongda/cup"+club.ymd+"/"+club.logo; 
				if(!StringUtil.isEmpty(club.club_logo_1))
					club.club_logo_1 = "http://kenhkiemtien.com/"+"upload/bongda/club"+club.club_logo_1; 
				if(!StringUtil.isEmpty(club.club_logo_2))
					club.club_logo_2 = "http://kenhkiemtien.com/"+"upload/bongda/club"+club.club_logo_2; 
				
	       	
			    Date parsedDate = dateFormat.parse(rs.getString("match_time"));
			    long beginTT = parsedDate.getTime()-30*60*1000;
			    long endTT = parsedDate.getTime()+140*60*1000;
			    int rateTime = (beginTT<=Calendar.getInstance().getTimeInMillis()&&Calendar.getInstance().getTimeInMillis()<=endTT)?1:0;
				club.match_rate = (4-club.cup_rate)+rateTime*5;
				listMatch.add(club);
				
			}
			C3p0FootBallPool.attemptClose(rs);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		Collections.sort(listMatch, new FBMatchComparator());
		return listMatch;
	}
	
	
	public static String getJSonFBMatch(List<FBMatch> matchList) throws ParseException {
	    JSONArray jsonArray = new JSONArray();

	   // List<FBMatch> cartList = FootBallDAO.getListMatchByDate("03-09-2015");
	    
	    for(FBMatch p : matchList) {
	        JSONObject formDetailsJson = new JSONObject();
	        formDetailsJson.put("id", p.id);
	        formDetailsJson.put("club_id_1", p.club_id_1);
	        formDetailsJson.put("club_id_2", p.club_id_2);
	        formDetailsJson.put("club_code_1",StringUtil.isEmpty( p.club_code_1)?"":p.club_code_1);
	        formDetailsJson.put("club_code_2",StringUtil.isEmpty( p.club_code_2)?"":p.club_code_2);
	        formDetailsJson.put("club_name_1",StringUtil.isEmpty( p.club_name_1)?"":p.club_name_1);
	        formDetailsJson.put("club_name_2",StringUtil.isEmpty( p.club_name_2)?"":p.club_name_2);
	        formDetailsJson.put("club_logo_1",StringUtil.isEmpty( p.club_logo_1)?"":p.club_logo_1);
	        formDetailsJson.put("club_logo_2",StringUtil.isEmpty( p.club_logo_2)?"":p.club_logo_2);
	        formDetailsJson.put("cup_id", p.cup_id);
	        formDetailsJson.put("cup",StringUtil.isEmpty( p.cup)?"":p.cup);
	        formDetailsJson.put("logo",StringUtil.isEmpty( p.logo)?"":p.logo);
	        formDetailsJson.put("result",StringUtil.isEmpty( p.result)?"":p.result);
	        formDetailsJson.put("result_1",StringUtil.isEmpty( p.result_1)?"":p.result_1);
	        formDetailsJson.put("round",StringUtil.isEmpty( p.round)?"":p.round);
	        formDetailsJson.put("status",StringUtil.isEmpty( p.status)?"":p.status);
	        
	        formDetailsJson.put("match_minute",StringUtil.isEmpty( p.match_minute)?"":p.match_minute );
	        formDetailsJson.put("rate", p.cup_rate);
	        formDetailsJson.put("match_rate", p.match_rate);
	        
	        formDetailsJson.put("club_redcard_1", p.club_redcard_1);
	        formDetailsJson.put("club_redcard_2", p.club_redcard_2);
	        formDetailsJson.put("had_video", p.had_video);
	        
	       jsonArray.add(formDetailsJson);
	    }
	    //System.out.println(jsonArray.toString());
	    return jsonArray.toString();
	}
	
	public static void createFileJSONFBMatch(List<FBMatch> listMatch,String ngay_quay_vn ){
		try {
			String arrDate[] = ngay_quay_vn.split("-");
			DBConfig.loadProperties();
			FileUtil.mkdirs(DBConfig.file_json_match_date+"/"+arrDate[2]);
			File file = new File(DBConfig.file_json_match_date+"/"+arrDate[2]+"/match_"+ngay_quay_vn+".html");
			String kq_str=getJSonFBMatch(listMatch);
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(kq_str.getBytes());
			fileOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void createFileJSONFBMatchLive(List<FBMatch> listMatch){
		try {
			DBConfig.loadProperties();
			File file = new File(DBConfig.file_json_match_live);
			String kq_str=getJSonFBMatch(listMatch);
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(kq_str.getBytes());
			fileOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	
	public static int checkChart(int cup_id,int club_id,String season) {
		PreparedStatement ps;
		int kq = 0;
		try {
			Connection connection = C3p0FootBallPool.getConnection();
			ps = connection.prepareStatement("SELECT *  FROM  vtc_bongda.fb_charts WHERE club_id = ? AND season = ?  AND cup_id = ? ");
			ps.setInt(1, club_id);
			ps.setString(2, season);
			ps.setInt(3, cup_id);
			ResultSet rs =	ps.executeQuery();
			if(rs.next())
			{
				kq = rs.getInt("id");
			}
			C3p0FootBallPool.attemptClose(rs);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return kq;
	}
	
	
	public static int checkCup(String cupcode) {
		PreparedStatement ps;
		int kq = 0;
		try {
			Connection connection = C3p0FootBallPool.getConnection();
			ps = connection.prepareStatement("SELECT *  FROM  vtc_bongda.fb_cup WHERE  code = ? ");
			ps.setString(1, cupcode.toUpperCase());
			ResultSet rs =	ps.executeQuery();
			if(rs.next())
			{
				kq = rs.getInt("id");
			}
			C3p0FootBallPool.attemptClose(rs);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return kq;
	}
	
	public static int checkCupByName(String cupcode) {
		PreparedStatement ps;
		int kq = 0;
		try {
			Connection connection = C3p0FootBallPool.getConnection();
			ps = connection.prepareStatement("SELECT *  FROM  vtc_bongda.fb_cup WHERE  name = ? ");
			ps.setString(1, cupcode);
			ResultSet rs =	ps.executeQuery();
			if(rs.next())
			{
				kq = rs.getInt("id");
			}
			C3p0FootBallPool.attemptClose(rs);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return kq;
	}
	
	public static int checkCupMatchError(int cup_id) {
		PreparedStatement ps;
		int kq = 0;
		try {
			Connection connection = C3p0FootBallPool.getConnection();
			ps = connection.prepareStatement("SELECT * FROM fb_cup WHERE id IN (SELECT cup_id FROM fb_match WHERE match_time < NOW() AND STATUS=\"\")");
			ResultSet rs =	ps.executeQuery();
			while(rs.next())
			{
				kq = rs.getInt("id")==cup_id?1:0;
				if(kq==1)break;
			}
			C3p0FootBallPool.attemptClose(rs);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return kq;
	}
	
	public static int getAllMatchErrorLogo() {
		PreparedStatement ps;
		int kq = 0;
		try {
			Connection connection = C3p0FootBallPool.getConnection();
			ps = connection.prepareStatement("SELECT * FROM fb_match WHERE (club_logo_1 = NULL OR club_logo_1 ='') OR (club_logo_2 = NULL OR club_logo_2 ='') ");
			ResultSet rs =	ps.executeQuery();
			while(rs.next())
			{
				if(kq==1)break;
			}
			C3p0FootBallPool.attemptClose(rs);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return kq;
	}
	
	public static int checkFootballClub(int club_id,int footballer_id) {
		PreparedStatement ps;
		int kq = 0;
		try {
			Connection connection = C3p0FootBallPool.getConnection();
			ps = connection.prepareStatement("SELECT footballer_id  FROM  vtc_bongda.fb_footballer_club WHERE  club_id = ? AND footballer_id = ?  ");
			ps.setInt(1, club_id);
			ps.setInt(2, footballer_id);
			ResultSet rs =	ps.executeQuery();
			if(rs.next())
			{
				kq = rs.getInt("footballer_id");
			}
			C3p0FootBallPool.attemptClose(rs);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return kq;
	}
	
	
	
	public static int checkMatch(int club1,int club2,String date_match) {
		PreparedStatement ps;
		int kq = 0;
		try {
			Connection connection = C3p0FootBallPool.getConnection();
			ps = connection.prepareStatement("SELECT * FROM fb_match WHERE club_id_1 = ? AND club_id_2 = ? AND DATE_FORMAT(match_time,\"%Y-%m-%d\")= ?");
			ps.setInt(1, club1);
			ps.setInt(2, club2);
			ps.setString(3, date_match);
			ResultSet rs =	ps.executeQuery();
			if(rs.next())
			{
				kq = rs.getInt("id");
			}
			C3p0FootBallPool.attemptClose(rs);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return kq;
	}
	
	
	public static int checkMatch(int club1,int club2,String date_match,String date_match_next) {
		PreparedStatement ps;
		int kq = 0;
		try {
			Connection connection = C3p0FootBallPool.getConnection();
			ps = connection.prepareStatement("SELECT * FROM fb_match WHERE club_id_1 = ? AND club_id_2 = ? AND match_time BETWEEN  ? AND ? ");
			ps.setInt(1, club1);
			ps.setInt(2, club2);
			ps.setString(3, date_match);
			ps.setString(4, date_match_next);
			ResultSet rs =	ps.executeQuery();
			if(rs.next())
			{
				kq = rs.getInt("id");
			}
			C3p0FootBallPool.attemptClose(rs);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return kq;
	}
	
	public static int checkMatchByUrlCode(String url_code) {
		PreparedStatement ps;
		int kq = 0;
		try {
			Connection connection = C3p0FootBallPool.getConnection();
			ps = connection.prepareStatement("SELECT * FROM fb_match WHERE url_code = ?  ");
			ps.setString(1, url_code);
			ResultSet rs =	ps.executeQuery();
			if(rs.next())
			{
				kq = rs.getInt("id");
			}
			C3p0FootBallPool.attemptClose(rs);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return kq;
	}
	
	public static int checkMatchByBDWapID(String bdwap_id) {
		PreparedStatement ps;
		int kq = 0;
		try {
			Connection connection = C3p0FootBallPool.getConnection();
			ps = connection.prepareStatement("SELECT * FROM fb_match WHERE bdwap_id = ?  ");
			ps.setString(1, bdwap_id);
			ResultSet rs =	ps.executeQuery();
			if(rs.next())
			{
				kq = rs.getInt("id");
			}
			C3p0FootBallPool.attemptClose(rs);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return kq;
	}
	
	public static int saveMatch (FBMatch match) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
			
			ps = conn.prepareStatement("INSERT INTO vtc_bongda.fb_match (cup_id,ROUND,club_id_1,club_id_2,stadium, "
					+ "result,result_1,STATUS,match_time,match_minute,create_date,club_code_1,club_code_2,club_name_1,"
					+ "club_name_2,url_code,club_logo_1,club_logo_2,season,bdwap_id,url_tyle)"
					+ "VALUES (?, ?,?,?,?,?,?,?,?,?,NOW(),?,?,?,?,?,?,?,?,?,?);" );
			ps.setInt(1, match.cup_id);
			ps.setString(2, match.round);
			ps.setInt(3, match.club_id_1);
			ps.setInt(4, match.club_id_2);
			ps.setString(5, match.stadium);
			ps.setString(6, match.result);
			ps.setString(7, match.result_1);
			ps.setString(8, match.status);
			ps.setString(9, match.match_time);
			ps.setString(10, match.match_minute);
			
			ps.setString(11, match.club_code_1);
			ps.setString(12, match.club_code_2);
			
			ps.setString(13, match.club_name_1);
			ps.setString(14, match.club_name_2);
			ps.setString(15, match.url_code);
			ps.setString(16, match.club_logo_1);
			ps.setString(17, match.club_logo_2);
			ps.setString(18, match.season);
			ps.setString(19, match.bdwap_id);
			ps.setString(20, match.url_tyle);
			
			
			ps.execute();
			
			conn.commit();
			conn.setAutoCommit(true);
			
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()){
			    id=rs.getInt(1);
			}
			
			C3p0FootBallPool.attemptClose(rs);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	
	public static int updateMatch (FBMatch match) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
				
			ps = conn.prepareStatement("UPDATE vtc_bongda.fb_match SET cup_id=?,stadium=?, "
					+ "result=?,result_1=?,STATUS=?,match_minute=?,club_name_1 =?,club_name_2=?,"
					+ "ROUND=?,url_code=?,club_logo_1=?,club_logo_2=?,season=?,match_time=?,bdwap_id=? Where id =? " );
			
			ps.setInt(1, match.cup_id);
			
			ps.setString(2, match.stadium);
			ps.setString(3, match.result);
			ps.setString(4, match.result_1);
			ps.setString(5, match.status);
			ps.setString(6, match.match_minute);
			
			ps.setString(7, match.club_name_1);
			ps.setString(8, match.club_name_2);
			ps.setString(9, match.round);
			ps.setString(10, match.url_code);
			ps.setString(11, match.club_logo_1);
			ps.setString(12, match.club_logo_2);
			ps.setString(13, match.season);
			ps.setString(14, match.match_time);
			ps.setString(15, match.bdwap_id);
			
			ps.setInt(16, match.id);
			
			ps.execute();
			
			conn.commit();
			conn.setAutoCommit(true);
			
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			
			e.printStackTrace();
		}

		return id;
	}
	
	public static int updateMatchLive (FBMatch match) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
				
			ps = conn.prepareStatement("UPDATE vtc_bongda.fb_match SET cup_id=?,stadium=?, "
					+ "result=?,result_1=?,status=?,match_minute=?,club_name_1 =?,club_name_2=?,"
					+ "round=?,club_logo_1=?,club_logo_2=?,url_code=?,bdwap_id=?,match_time=?,url_tyle=?Where id =? " );
			
			ps.setInt(1, match.cup_id);
			
			ps.setString(2, match.stadium);
			ps.setString(3, match.result);
			ps.setString(4, match.result_1);
			ps.setString(5, match.status);
			ps.setString(6, match.match_minute);
			
			ps.setString(7, match.club_name_1);
			ps.setString(8, match.club_name_2);
			ps.setString(9, match.round);
			
			ps.setString(10, match.club_logo_1);
			ps.setString(11, match.club_logo_2);
			ps.setString(12, match.url_code);
			ps.setString(13, match.bdwap_id);
			ps.setString(14, match.match_time);
			ps.setString(15, match.url_tyle);
			
			ps.setInt(16, match.id);
			
			ps.execute();
			
			conn.commit();
			conn.setAutoCommit(true);
			
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			System.out.println("----------id:"+match.id);
			e.printStackTrace();
		}

		return id;
	}
	
	
	public static int updateMatchKT (FBMatch match) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
				
			ps = conn.prepareStatement("UPDATE vtc_bongda.fb_match SET  "
					+ "result=?,result_1=?,match_minute=?,ROUND=? Where id =? " );
			
			ps.setString(1, match.result);
			ps.setString(2, match.result_1);
			ps.setString(3, match.match_minute);
			
			ps.setString(4, match.round);
			
			ps.setInt(5, match.id);
			
			ps.execute();
			
			conn.commit();
			conn.setAutoCommit(true);
			
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			System.out.println("----------id:"+match.id);
			e.printStackTrace();
		}

		return id;
	}
	
	
	public static int updateMatchRedCard1 (int match_id,int club_1_redcard) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
				
			ps = conn.prepareStatement("UPDATE vtc_bongda.fb_match SET  "
					+ "club_redcard_1=? Where id =? " );
			
			ps.setInt(1, club_1_redcard);
			ps.setInt(2, match_id);
			
			ps.execute();
			
			conn.commit();
			conn.setAutoCommit(true);
			
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			System.out.println("----------id:"+match_id);
			e.printStackTrace();
		}

		return id;
	}
	
	
	public static int updateMatchRedCard2 (int match_id,int club_2_redcard) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
				
			ps = conn.prepareStatement("UPDATE vtc_bongda.fb_match SET  "
					+ "club_redcard_2=? Where id =? " );
			
			ps.setInt(1, club_2_redcard);
			ps.setInt(2, match_id);
			
			ps.execute();
			
			conn.commit();
			conn.setAutoCommit(true);
			
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			System.out.println("----------id:"+match_id);
			e.printStackTrace();
		}

		return id;
	}
	
	
	public static int updateMatchHadVideo (int match_id,int had_video) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
				
			ps = conn.prepareStatement("UPDATE vtc_bongda.fb_match SET  "
					+ "had_video=? Where id =? " );
			
			ps.setInt(1, had_video);
			ps.setInt(2, match_id);
			
			ps.execute();
			
			conn.commit();
			conn.setAutoCommit(true);
			
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			System.out.println("----------id:"+match_id);
			e.printStackTrace();
		}

		return id;
	}
	
	public static int updateMatchCrawlerFormUrlCode (int match_id,String result,String result_1,String status,String match_minute) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
				
			ps = conn.prepareStatement("UPDATE vtc_bongda.fb_match SET  "
					+ "result=? , result_1=? , match_minute=? , status=? Where id =? " );
			
			ps.setString(1, result);
			ps.setString(2, result_1);
			ps.setString(3, match_minute);
			ps.setString(4, status);
			ps.setInt(5, match_id);
			ps.execute();
			
			conn.commit();
			conn.setAutoCommit(true);
			
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			System.out.println("----------id:"+match_id);
			e.printStackTrace();
		}

		return id;
	}
	
	public static int updateMatchCrawlerFormUrlCode (int match_id,String result,String result_1,String status,String match_minute,String match_time) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
				
			ps = conn.prepareStatement("UPDATE vtc_bongda.fb_match SET  "
					+ "result=? , result_1=? , match_minute=? , status=?,match_time=? Where id =? " );
			
			ps.setString(1, result);
			ps.setString(2, result_1);
			ps.setString(3, match_minute);
			ps.setString(4, status);
			ps.setString(5, match_time);
			ps.setInt(6, match_id);
			ps.execute();
			
			conn.commit();
			conn.setAutoCommit(true);
			
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			System.out.println("----------id:"+match_id);
			e.printStackTrace();
		}

		return id;
	}
	
	public static int updateMatchBDWapID (int match_id,String bdwap_id) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
				
			ps = conn.prepareStatement("UPDATE vtc_bongda.fb_match SET  "
					+ "bdwap_id=? Where id =? " );
			
			ps.setString(1, bdwap_id);
			ps.setInt(2, match_id);
			
			ps.execute();
			
			conn.commit();
			conn.setAutoCommit(true);
			
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			System.out.println("----------id:"+match_id);
			e.printStackTrace();
		}

		return id;
	}
	
	public static int updateMatchSeson (String match_ids,String season) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
				
			ps = conn.prepareStatement("UPDATE vtc_bongda.fb_match SET  "
					+ "season=? Where id in ("+match_ids+") " );
			
			ps.setString(1, season);
			
			ps.execute();
			
			conn.commit();
			conn.setAutoCommit(true);
			
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			System.out.println("----------id:"+match_ids);
			e.printStackTrace();
		}

		return id;
	}
	
	
	
	public static void updateMatch (int match_id,String club_name_1,String club_name_2,String coach_name_1,String coach_name_2) {
		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
				
			ps = conn.prepareStatement("UPDATE vtc_bongda.fb_match SET club_name_1=?,club_name_2=?, "
					+ "coach_name_1=?,coach_name_2=? Where id =? " );
			
			ps.setString(1, club_name_1);
			ps.setString(2, club_name_2);
			ps.setString(3, coach_name_1);
			ps.setString(4, coach_name_2);
			
			ps.setInt(5, match_id);
			
			ps.execute();
			
			conn.commit();
			conn.setAutoCommit(true);
			
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		
	}
	
	public static void updateMatchClubLogo (int match_id,String club_logo_1,String club_logo_2) {
		
		PreparedStatement ps;		
		try {
			
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
				
			ps = conn.prepareStatement("UPDATE vtc_bongda.fb_match SET club_logo_1=?,club_logo_2=?"
					+ " Where id =? " );
			
			ps.setString(1, club_logo_1);
			ps.setString(2, club_logo_2);
			
			ps.setInt(3, match_id);
			
			ps.execute();
			
			conn.commit();
			conn.setAutoCommit(true);
			
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		
	}
	
	public static void updateAvatarCoach (int id,String avatar) {
		
		PreparedStatement ps;		
		try {
			System.out.println("UpdateMatch:"+id);
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
				
			ps = conn.prepareStatement("UPDATE vtc_bongda.fb_coach SET avatar=? Where id =? " );
			
			ps.setString(1, avatar);
			ps.setInt(2, id);
			
			ps.execute();
			
			conn.commit();
			conn.setAutoCommit(true);
			
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		
	}
	
public static void updateAvatarFBaller (int id,String avatar) {
		
		PreparedStatement ps;		
		try {
			System.out.println("UpdateMatch:"+id);
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
				
			ps = conn.prepareStatement("UPDATE vtc_bongda.fb_footballer SET avatar=? Where id =? " );
			
			ps.setString(1, avatar);
			ps.setInt(2, id);
			
			ps.execute();
			
			conn.commit();
			conn.setAutoCommit(true);
			
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		
	}

	
	public static void updateIMinuteMatchSummary (int id,float iminute) {
	
	PreparedStatement ps;		
	try {
		System.out.println("UpdateMatch:"+id);
		Connection conn = C3p0FootBallPool.getConnection(); 
		conn.setAutoCommit(false);
			
		ps = conn.prepareStatement("UPDATE vtc_bongda.fb_match_summary SET i_minute=? Where id =? " );
		
		ps.setFloat(1, iminute);
		ps.setInt(2, id);
		
		ps.execute();
		
		conn.commit();
		conn.setAutoCommit(true);
		
		C3p0FootBallPool.attemptClose(ps);
		C3p0FootBallPool.attemptClose(conn);	
	} catch (SQLException e) {
		e.printStackTrace();
	}

	
}

	public static void updateLogoClub (int id,String avatar) {
		
		PreparedStatement ps;		
		try {
			System.out.println("UpdateMatch:"+id);
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
				
			ps = conn.prepareStatement("UPDATE vtc_bongda.fb_club SET logo=? Where id =? " );
			
			ps.setString(1, avatar);
			ps.setInt(2, id);
			
			ps.execute();
			
			conn.commit();
			conn.setAutoCommit(true);
			
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		
	}
	
	public static void updateBDWapIDClub (int id,String bdwap_id) {
		
		PreparedStatement ps;		
		try {
			//System.out.println("UpdateMatch:"+id);
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
				
			ps = conn.prepareStatement("UPDATE vtc_bongda.fb_club SET bdwap_id=? Where id =? " );
			
			ps.setString(1, bdwap_id);
			ps.setInt(2, id);
			
			ps.execute();
			
			conn.commit();
			conn.setAutoCommit(true);
			
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		
	}
	
	
	public static int saveMatchTeam (FBMatchTeam match) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
			
			ps = conn.prepareStatement("INSERT INTO vtc_bongda.fb_match_team (match_id,team_1,team_2,formation_1,formation_2,create_date)"
					+ " VALUES (?,?,?,?,?,NOW());" );
			
			ps.setInt(1, match.match_id);
			ps.setString(2, match.team_1);
			ps.setString(3, match.team_2);
			ps.setString(4, match.formation_1);
			ps.setString(5, match.formation_2);
			
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	public static int updateMatchTeam (FBMatchTeam match) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
			
			ps = conn.prepareStatement("UPdate vtc_bongda.fb_match_team SET team_1 = ? ,team_2 = ?,formation_1 = ? ,formation_2 = ? Where id = ? ");
			
			
			ps.setString(1, match.team_1);
			ps.setString(2, match.team_2);
			ps.setString(3, match.formation_1);
			ps.setString(4, match.formation_2);
			ps.setInt(5, match.id);
			
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	
	public static int updateFootballer (FBFootballer fbFootballer) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
			
			ps = conn.prepareStatement("UPDATE vtc_bongda.fb_footballer SET NAME = ? , avatar_source = ?, avatar = ? , country = ? , "
					+ "birthday = ? , height = ? , weight = ?, club_id = ? , POSITION = ? , join_date = ? , transfer_free = ?, "
					+ "former_club = ? , one_club = ? , conveniently_foot = ? , achievement = ?, clubshirtno = ?, update_date = NOW(), "
					+ "update_user = 'crawler' , id_7m = ? WHERE id = ? ");
			
			
			ps.setString(1, fbFootballer.fullname);
			ps.setString(2, fbFootballer.avatar_source);
			ps.setString(3, fbFootballer.avatar);
			ps.setString(4, fbFootballer.country);
			ps.setString(5, fbFootballer.birthday);
			ps.setString(6, fbFootballer.height);
			ps.setString(7, fbFootballer.weight);
			ps.setInt(8, fbFootballer.club_id);
			ps.setString(9, fbFootballer.position);
			ps.setString(10, fbFootballer.join_date);
			ps.setString(11, fbFootballer.transfer_free);
			ps.setString(12, fbFootballer.former_club);
			ps.setString(13, fbFootballer.one_club);
			ps.setString(14, fbFootballer.conveniently_foot);
			ps.setString(15, fbFootballer.achievement);
			ps.setInt(16, fbFootballer.clubshirtno);
			ps.setInt(17, fbFootballer.id_7m);
			ps.setInt(18, fbFootballer.id);
			
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	
	public static int updateFootballerNoavatar (FBFootballer fbFootballer) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
			
			ps = conn.prepareStatement("UPDATE vtc_bongda.fb_footballer SET NAME = ? , avatar_source = ?, country = ? , "
					+ "birthday = ? , height = ? , weight = ?, club_id = ? , POSITION = ? , join_date = ? , transfer_free = ?, "
					+ "former_club = ? , one_club = ? , conveniently_foot = ? , achievement = ?, clubshirtno = ?, update_date = NOW(), "
					+ "update_user = 'crawler' , id_7m = ? WHERE id = ? ");
			
			
			ps.setString(1, fbFootballer.fullname);
			ps.setString(2, fbFootballer.avatar_source);
		//	ps.setString(3, fbFootballer.avatar);
			ps.setString(3, fbFootballer.country);
			ps.setString(4, fbFootballer.birthday);
			ps.setString(5, fbFootballer.height);
			ps.setString(6, fbFootballer.weight);
			ps.setInt(7, fbFootballer.club_id);
			ps.setString(8, fbFootballer.position);
			ps.setString(9, fbFootballer.join_date);
			ps.setString(10, fbFootballer.transfer_free);
			ps.setString(11, fbFootballer.former_club);
			ps.setString(12, fbFootballer.one_club);
			ps.setString(13, fbFootballer.conveniently_foot);
			ps.setString(14, fbFootballer.achievement);
			ps.setInt(15, fbFootballer.clubshirtno);
			ps.setInt(16, fbFootballer.id_7m);
			ps.setInt(17, fbFootballer.id);
			
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	public static int saveMatchStatistic (FBMatchStatictis match) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
			
			ps = conn.prepareStatement("INSERT INTO vtc_bongda.fb_match_statistics "
					+ "(match_id,ball_possession,goal_attempts,shots_on_goal,shots_off_goal,"
					+ "blocked_shots,free_kicks,corner_kicks,offsides,throw_in,goalkeeper_Saves,"
					+ "fouls,yellow_card,red_card,create_date) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW());" );
			
			ps.setInt(1, match.match_id);
			ps.setString(2, match.ball_possession);
			ps.setString(3, match.goal_attempts);
			ps.setString(4, match.shots_on_goal);
			ps.setString(5, match.shots_off_goal);
			ps.setString(6, match.blocked_shots);
			ps.setString(7, match.free_kicks);
			ps.setString(8, match.corner_kicks);
			ps.setString(9, match.offsides);
			
			ps.setString(10, match.throw_in);
			ps.setString(11, match.goalkeeper_Saves);
			ps.setString(12, match.fouls);
			ps.setString(13, match.yellow_card);
			ps.setString(14, match.red_card);
			
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	
	public static int updateMatchStatistic (FBMatchStatictis match) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
			
			ps = conn.prepareStatement("Update vtc_bongda.fb_match_statistics SET "
					+ "ball_possession = ? ,goal_attempts = ?  ,shots_on_goal = ?  ,shots_off_goal = ? ,"
					+ "blocked_shots = ?  ,free_kicks = ? ,corner_kicks = ? ,offsides = ? ,throw_in = ? ,goalkeeper_Saves = ? ,"
					+ "fouls = ?  ,yellow_card = ?  ,red_card = ?  Where match_id = ? " );
			
			ps.setString(1, match.ball_possession);
			ps.setString(2, match.goal_attempts);
			ps.setString(3, match.shots_on_goal);
			ps.setString(4, match.shots_off_goal);
			ps.setString(5, match.blocked_shots);
			ps.setString(6, match.free_kicks);
			ps.setString(7, match.corner_kicks);
			ps.setString(8, match.offsides);
			
			ps.setString(9, match.throw_in);
			ps.setString(10, match.goalkeeper_Saves);
			ps.setString(11, match.fouls);
			ps.setString(12, match.yellow_card);
			ps.setString(13, match.red_card);
			
			ps.setInt(14, match.match_id);
			
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	
	public static int saveMatchLive (FBMatchLive match) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
			
			ps = conn.prepareStatement("INSERT INTO vtc_bongda.fb_match_live (match_id,club_id,img,TIME,content,color,video,image_event,create_date)"
					+ " VALUES (?,?,?,?,TRIM(?),TRIM(?),?,?,NOW());");
			
			ps.setInt(1, match.match_id);
			ps.setInt(2, match.club_id);
			ps.setString(3, match.img);
			ps.setString(4, match.time);
			ps.setString(5, match.content);
			ps.setString(6, match.color);
			ps.setString(7, match.video);
			ps.setString(8, match.image_event);
			
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	public static int updateMatchLive (FBMatchLive match,int id) {
		PreparedStatement ps;		
		try {
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
			
			ps = conn.prepareStatement("UPDATE  vtc_bongda.fb_match_live SET match_id=?,club_id=?,img = ?,"
					+ "time=?, content=?,color=?,video=?,image_event=? Where id = ? ");
			
			ps.setInt(1, match.match_id);
			ps.setInt(2, match.club_id);
			ps.setString(3, match.img);
			ps.setString(4, match.time);
			ps.setString(5, match.content);
			ps.setString(6, match.color);
			ps.setString(7, match.video);
			ps.setString(8, match.image_event);
			ps.setInt(9, id);
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			System.out.println(match.video);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	
	public static int saveChart (FBChart chart) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
			
			ps = conn.prepareStatement("INSERT INTO vtc_bongda.fb_charts (rate,club_id,CODE,NAME,"
					+ "so_tran,so_tran_thang,so_tran_hoa,so_tran_bai,ban_thang,ban_bai,"
					+ "hieu_so,diem,season,cup_id,cup_group) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
			
			ps.setInt(1, chart.rate);
			ps.setInt(2, chart.club_id);
			ps.setString(3, chart.code);
			ps.setString(4, chart.name);
			ps.setInt(5, chart.so_tran);
			ps.setInt(6, chart.so_tran_thang);
			ps.setInt(7, chart.so_tran_hoa);
			ps.setInt(8, chart.so_tran_bai);
			ps.setInt(9, chart.ban_thang);
			ps.setInt(10, chart.ban_bai);
			ps.setInt(11, chart.hieu_so);
			ps.setInt(12, chart.diem);
			ps.setString(13, chart.season);
			ps.setInt(14, chart.cup_id);
			ps.setString(15, chart.cup_group);
			
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	
	public static int updateChart (FBChart chart) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
			
			ps = conn.prepareStatement("UPDATE vtc_bongda.fb_charts  SET rate = ? , "
					+ "so_tran =? ,so_tran_thang = ? ,so_tran_hoa = ? , so_tran_bai = ?,"
					+ "ban_thang = ? , ban_bai = ? , hieu_so = ? , diem = ? , cup_id = ?, cup_group = ? "
					+ " WHERE id = ? ;");
			
			ps.setInt(1, chart.rate);
			ps.setInt(2, chart.so_tran);
			ps.setInt(3, chart.so_tran_thang);
			ps.setInt(4, chart.so_tran_hoa);
			ps.setInt(5, chart.so_tran_bai);
			ps.setInt(6, chart.ban_thang);
			ps.setInt(7, chart.ban_bai);
			ps.setInt(8, chart.hieu_so);
			ps.setInt(9, chart.diem);
			ps.setInt(10, chart.cup_id);
			ps.setString(11, chart.cup_group);
			ps.setInt(12, chart.id);
		
			
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	
	public static int saveCup (FBCup cup) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
			
			ps = conn.prepareStatement("INSERT INTO vtc_bongda.fb_cup (CODE,NAME,name_en,"
					+ "country,country_en,continent,create_date) VALUES (?,?,?,?,?,?,NOW());");
			
			ps.setString(1, cup.code.toUpperCase());
			ps.setString(2, cup.name);
			ps.setString(3, cup.name_en);
			ps.setString(4, cup.country);
			ps.setString(5, cup.country_en);
			ps.setString(6, cup.continent);
			
			
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	public static int saveClup (FBClub club) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
			
			ps = conn.prepareStatement("INSERT INTO vtc_bongda.fb_club (CODE,NAME,name_en,country,city,info,logo,"
					+ "stadium,stadium_capacity,address,website,fan_page,email,established_date,coach_id,map,avgage,id_7m,"
					+ "create_date,create_user,logo_source,country_en) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW(),?,?,?);");
			
			ps.setString(1, club.code.toUpperCase());
			ps.setString(2, club.name);
			ps.setString(3, club.name_en);
			ps.setString(4, club.country);
			ps.setString(5, club.city);
			ps.setString(6, club.info);
			ps.setString(7, club.logo);
			ps.setString(8, club.stadium);
			ps.setString(9, club.stadium_capacity);
			ps.setString(10, club.address);
			ps.setString(11, club.website);
			ps.setString(12, club.fan_page);
			ps.setString(13, club.email);
			ps.setString(14, club.established_date);
			ps.setInt(15, club.coach_id);
			ps.setString(16, club.map);
			ps.setFloat(17, club.avgage);
			ps.setInt(18, club.id_7m);
			ps.setString(19, "crawler");
			ps.setString(20, club.logo_source);
			ps.setString(21, club.country_en);
		
			
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()){
			    id=rs.getInt(1);
			}
			
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	public static int saveClupFromBDWap (FBClub club,String url) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
			
			ps = conn.prepareStatement("INSERT INTO vtc_bongda.fb_club (CODE,NAME,name_en,country,city,info,logo,"
					+ "stadium,stadium_capacity,address,website,fan_page,email,established_date,coach_id,map,avgage,"
					+ "create_date,create_user,logo_source,country_en,bdwap_id) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW(),?,?,?,?);");
			
			ps.setString(1, club.code.toUpperCase());
			ps.setString(2, club.name);
			ps.setString(3, club.name_en);
			ps.setString(4, club.country);
			ps.setString(5, club.city);
			ps.setString(6, club.info);
			ps.setString(7, club.logo);
			ps.setString(8, club.stadium);
			ps.setString(9, club.stadium_capacity);
			ps.setString(10, club.address);
			ps.setString(11, club.website);
			ps.setString(12, club.fan_page);
			ps.setString(13, club.email);
			ps.setString(14, club.established_date);
			ps.setInt(15, club.coach_id);
			ps.setString(16, club.map);
			ps.setFloat(17, club.avgage);
			ps.setString(18, "crawler");
			ps.setString(19, club.logo_source);
			ps.setString(20, club.country_en);
			ps.setString(21, club.bdwap_id);
			
			
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()){
			    id=rs.getInt(1);
			}
			
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			System.out.print("Link Cup Loi----->:"+url);
			e.printStackTrace();
			try {
				FileUtil.writeToFile("/home/crawler/log/logCupLoiInsert.txt",
						"," + club.code+"-"+ club.name+"- Link cide:"+url+"\r\n", true);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}

		return id;
	}
	
	
	public static int updateClub (FBClub club) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
			
			ps = conn.prepareStatement("Update vtc_bongda.fb_club SET name_en=?,country=?,city=?,info=?,logo=?,"
					+ "stadium=?,stadium_capacity=?,address=?,website=?,fan_page=?,email=?,established_date=?,coach_id=?,map=?,avgage=?"
					+ ",id_7m=?,"
					+ "update_date=NOW(),update_user =? "
					+ " Where id = ? ");
			
			
			ps.setString(1, club.name_en);
			ps.setString(2, club.country);
			ps.setString(3, club.city);
			ps.setString(4, club.info);
			ps.setString(5, club.logo);
			
			ps.setString(6, club.stadium);
			ps.setString(7, club.stadium_capacity);
			
			ps.setString(8, club.address);
			ps.setString(9, club.website);
			ps.setString(10, club.fan_page);
			ps.setString(11, club.email);
			ps.setString(12, club.established_date);
			ps.setInt(13, club.coach_id);
			ps.setString(14, club.map);
			ps.setFloat(15, club.avgage);
			ps.setInt(16, club.id_7m);
			ps.setString(17, "crawler");
			ps.setInt(18, club.id);
			
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	
	public static int updateClubNoAvatar (FBClub club) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
			
			ps = conn.prepareStatement("Update vtc_bongda.fb_club SET name_en=?,country_en=?,city=?,info=?,"
					+ "stadium=?,stadium_capacity=?,address=?,website=?,fan_page=?,email=?,established_date=?,coach_id=?,map=?,avgage=?"
					+ ",id_7m=?,"
					+ "update_date=NOW(),update_user =? "
					+ " Where id = ? ");
			
			
			ps.setString(1, club.name_en);
			ps.setString(2, club.country_en);
			ps.setString(3, club.city);
			ps.setString(4, club.info);
			
			
			ps.setString(5, club.stadium);
			ps.setString(6, club.stadium_capacity);
			
			ps.setString(7, club.address);
			ps.setString(8, club.website);
			ps.setString(9, club.fan_page);
			ps.setString(10, club.email);
			ps.setString(11, club.established_date);
			ps.setInt(12, club.coach_id);
			ps.setString(13, club.map);
			ps.setFloat(14, club.avgage);
			ps.setInt(15, club.id_7m);
			ps.setString(16, "crawler");
			ps.setInt(17, club.id);
			
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	
	public static int updateClubCountryEn (int club_id,String country_en) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
			
			ps = conn.prepareStatement("Update vtc_bongda.fb_club SET country_en=?  "
					+ " Where id = ? ");
			
			
			ps.setString(1, country_en);
		
			ps.setInt(2, club_id);
			
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	public static int updateCupCountryEn (int id,String country_en) {
		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
			
			ps = conn.prepareStatement("Update vtc_bongda.fb_cup SET country_en=?  "
					+ " Where id = ? ");
			
			
			ps.setString(1, country_en);
		
			ps.setInt(2, id);
			
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}
	
	
	public static int saveCoach(FBCoach coach) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
			
			ps = conn.prepareStatement("INSERT INTO vtc_bongda.fb_coach "
					+ "(NAME, name_en, birthday, height,weight, joindate, avatar, country,"
					+ "formerclub, onceclub, create_date, create_user,id_7m)"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,NOW(),?,?);");
			
			ps.setString(1, coach.name);
			ps.setString(2, coach.name_en);
			ps.setString(3, coach.birthday);
			ps.setString(4, coach.height);
			ps.setString(5, coach.weight);
			ps.setString(6, coach.joindate);
			ps.setString(7, coach.avatar);
			ps.setString(8, coach.country);
			ps.setString(9, coach.formerclub);
			ps.setString(10, coach.onceclub_en);
			ps.setString(11, "crawler");
			ps.setInt(12, coach.id_7m);
			
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
			
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()){
			    id=rs.getInt(1);
			}
			
			C3p0FootBallPool.attemptClose(rs);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	
	public static int saveFootballer(FBFootballer fbFootballer) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
			
			ps = conn.prepareStatement("INSERT INTO vtc_bongda.fb_footballer(NAME,avatar,country,birthday,height,weight"
					+ ",club_id,POSITION,join_date,transfer_free,former_club,one_club,conveniently_foot,"
					+ "achievement,create_date,create_user,id_7m,clubshirtno)"
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW(),?,?,?);");
			
			ps.setString(1, fbFootballer.fullname);
			ps.setString(2, fbFootballer.avatar);
			ps.setString(3, fbFootballer.country);
			ps.setString(4, fbFootballer.birthday);
			ps.setString(5, fbFootballer.height);
			ps.setString(6, fbFootballer.weight);
			ps.setInt(7, fbFootballer.club_id);
			ps.setString(8, fbFootballer.position);
			ps.setString(9, fbFootballer.join_date);
			ps.setString(10, fbFootballer.transfer_free);
			ps.setString(11,fbFootballer.former_club);
			ps.setString(12,fbFootballer.one_club);
			ps.setString(13,fbFootballer.conveniently_foot);
			ps.setString(14,fbFootballer.achievement);
			ps.setString(15,"crawler");
			ps.setInt(16, fbFootballer.id_7m);
			ps.setInt(17, fbFootballer.clubshirtno);
			
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()){
			    id=rs.getInt(1);
			}
			
			C3p0FootBallPool.attemptClose(rs);
			
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	
	public static int saveFootballerClub(int club_id,int footballer_id,int clubshirtno) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
			
			ps = conn.prepareStatement("INSERT INTO vtc_bongda.fb_footballer_club (club_id,footballer_id,"
					+ "clubshirtno,STATUS,create_date,update_date) VALUES(?,?,?,1,NOW(),NOW());");
			
			ps.setInt(1, club_id);
			ps.setInt(2, footballer_id);
			ps.setInt(3, clubshirtno);
		
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()){
			    id=rs.getInt(1);
			}
			
			C3p0FootBallPool.attemptClose(rs);
			
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	
	public static int updateFootballerClub(int club_id,int footballer_id,int clubshirtno,int status) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
			
			ps = conn.prepareStatement("UPDATE vtc_bongda.fb_footballer_club SET  clubshirtno = ? ,STATUS = ? ,update_date = NOW() "
					+ "WHERE club_id = ? AND footballer_id = ? ;");
			
			ps.setInt(1, clubshirtno);
			ps.setInt(2, status);
			ps.setInt(3, club_id);
			ps.setInt(4, footballer_id);
		
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()){
			    id=rs.getInt(1);
			}
			
			C3p0FootBallPool.attemptClose(rs);
			
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	public static int updateAllFootballerClub(int club_id,int status) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
			
			ps = conn.prepareStatement("UPDATE vtc_bongda.fb_footballer_club SET  STATUS = ? ,update_date = NOW() "
					+ "WHERE club_id = ? ");
			
			ps.setInt(1, status);
			ps.setInt(2, club_id);
		
		
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()){
			    id=rs.getInt(1);
			}
			
			C3p0FootBallPool.attemptClose(rs);
			
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	public static int saveTyle (FBTyLe tyle) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
			
			ps = conn.prepareStatement("INSERT INTO vtc_bongda.fb_tyle (match_id,chau_a_catran,"
					+ "chau_a_hiep1,chau_au,chau_a_bt_catran,chau_a_bt_hiep1,chau_a_tt_catran,chau_a_tt_bt,create_date )"
					+ " VALUES (?, ?, ?, ?,?,?,?,?,NOW())");
			
			ps.setInt(1, tyle.match_id);
			ps.setString(2, tyle.chau_a_catran);
			ps.setString(3, tyle.chau_a_hiep1);
			ps.setString(4, tyle.chau_au);
			ps.setString(5, tyle.chau_a_bt_catran);
			ps.setString(6, tyle.chau_a_bt_hiep1);
			
			ps.setString(7, tyle.chau_a_tt_catran);
			ps.setString(8, tyle.chau_a_tt_bt);
			
			
			
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	
	public static int updateTyle (FBTyLe tyle) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
			
			ps = conn.prepareStatement("Update vtc_bongda.fb_tyle SET chau_a_catran = ? ,"
					+ "chau_a_hiep1 = ? ,chau_au = ? ,chau_a_bt_catran = ? ,chau_a_bt_hiep1 = ?,chau_a_tt_catran = ?,chau_a_tt_bt= ?  "
					+ " Where match_id = ? ");
			
			
			ps.setString(1, tyle.chau_a_catran);
			ps.setString(2, tyle.chau_a_hiep1);
			ps.setString(3, tyle.chau_au);
			ps.setString(4, tyle.chau_a_bt_catran);
			ps.setString(5, tyle.chau_a_bt_hiep1);
			
			ps.setString(6, tyle.chau_a_tt_catran);
			ps.setString(7, tyle.chau_a_tt_bt);
			
			ps.setInt(8, tyle.match_id);
			
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	
	public static int checkTyLe(int match_id) {
		PreparedStatement ps;
		int kq = 0;
		try {
			Connection connection = C3p0FootBallPool.getConnection();
			ps = connection.prepareStatement("SELECT id  FROM  vtc_bongda.fb_tyle WHERE match_id = ? ");
			ps.setInt(1, match_id);
			
			ResultSet rs =	ps.executeQuery();
			if(rs.next())
			{
				kq = rs.getInt("id");
			}
			C3p0FootBallPool.attemptClose(rs);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return kq;
	}
	
	public static int checkMatchLive(int match_id, String time) {
		PreparedStatement ps;
		int kq = 0;
		try {
			Connection connection = C3p0FootBallPool.getConnection();
			ps = connection.prepareStatement("SELECT id FROM vtc_bongda.fb_match_live  WHERE match_id = ? AND TIME=?");
			ps.setInt(1, match_id);
			ps.setString(2, time);
			
			ResultSet rs =	ps.executeQuery();
			if(rs.next())
			{
				kq = rs.getInt("id");
			}
			C3p0FootBallPool.attemptClose(rs);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return kq;
	}
	
	public static int checkStatictis(int match_id) {
		PreparedStatement ps;
		int kq = 0;
		try {
			Connection connection = C3p0FootBallPool.getConnection();
			ps = connection.prepareStatement("SELECT id  FROM  vtc_bongda.fb_match_statistics WHERE match_id = ? ");
			ps.setInt(1, match_id);
			
			ResultSet rs =	ps.executeQuery();
			if(rs.next())
			{
				kq = rs.getInt("id");
			}
			C3p0FootBallPool.attemptClose(rs);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return kq;
	}
	
	public static int checkMatchTeam(int match_id) {
		PreparedStatement ps;
		int kq = 0;
		try {
			Connection connection = C3p0FootBallPool.getConnection();
			ps = connection.prepareStatement("SELECT id  FROM  vtc_bongda.fb_match_team WHERE match_id = ? ");
			ps.setInt(1, match_id);
			
			ResultSet rs =	ps.executeQuery();
			if(rs.next())
			{
				kq = rs.getInt("id");
			}
			C3p0FootBallPool.attemptClose(rs);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return kq;
	}
	
	public static FBFootballer getFootballer(String clubshirtno,int club_id) {
		PreparedStatement ps;
		FBFootballer club = null;
		try {
			Connection connection = C3p0FootBallPool.getConnection();
			ps = connection.prepareStatement("SELECT id,NAME,avatar,country,birthday,height,weight,club_id,POSITION,join_date,transfer_free,"
					+ "former_club,one_club,clubshirtno,DATE_FORMAT(create_date,'%Y/%m%d') ymd  FROM  vtc_bongda.fb_footballer "
					+ "WHERE clubshirtno  = ? AND club_id = ? ");
			ps.setString(1, clubshirtno);
			ps.setInt(2, club_id);
			ResultSet rs =	ps.executeQuery();
			if(rs.next())
			{
				club = new FBFootballer();
				club.id = rs.getInt(1);
				club.fullname = rs.getString(2);
				if(!StringUtil.isEmpty(rs.getString(3)))
				club.avatar ="http://kenhkiemtien.com/upload/bongda/footballer/"+rs.getString("ymd")+"/"+ rs.getString(3);
				
				club.country = rs.getString(4);
				club.birthday = rs.getString(5);
				club.height = rs.getString(6);
				club.weight = rs.getString(7);
				club.club_id = rs.getInt("club_id");
				club.position = rs.getString("POSITION");
				club.join_date = rs.getString("join_date");
				club.transfer_free = rs.getString("transfer_free");
				
				club.former_club = rs.getString("former_club");
				club.one_club = rs.getString("one_club");
				club.clubshirtno = rs.getInt("clubshirtno");
				
				
			}
			C3p0FootBallPool.attemptClose(rs);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return club;
	}
	
	
	public static FBFootballer getFootballerInerJoinClub(String clubshirtno,int club_id) {
		PreparedStatement ps;
		FBFootballer club = null;
		try {
			Connection connection = C3p0FootBallPool.getConnection();
			ps = connection.prepareStatement("SELECT f.id,f.name,f.avatar,f.country,f.birthday,f.height,f.weight,f.club_id,f.position"
					+ ",f.join_date,f.transfer_free,"
					+ "f.former_club,f.one_club,c.clubshirtno,DATE_FORMAT(f.create_date,'%Y/%m%d') ymd  FROM  vtc_bongda.fb_footballer "
					+ " f INNER JOIN fb_footballer_club c ON f.id = c.footballer_id  WHERE c.clubshirtno  = ? AND c.club_id = ? AND c.status = 1 ");
			ps.setString(1, clubshirtno);
			ps.setInt(2, club_id);
			ResultSet rs =	ps.executeQuery();
			if(rs.next())
			{
				club = new FBFootballer();
				club.id = rs.getInt(1);
				club.fullname = rs.getString(2);
				if(!StringUtil.isEmpty(rs.getString(3)))
				club.avatar ="http://kenhkiemtien.com/upload/bongda/footballer/"+rs.getString("ymd")+"/"+ rs.getString(3);
				
				club.country = rs.getString(4);
				club.birthday = rs.getString(5);
				club.height = rs.getString(6);
				club.weight = rs.getString(7);
				club.club_id = rs.getInt("club_id");
				club.position = rs.getString("position");
				club.join_date = rs.getString("join_date");
				club.transfer_free = rs.getString("transfer_free");
				
				club.former_club = rs.getString("former_club");
				club.one_club = rs.getString("one_club");
				club.clubshirtno = rs.getInt("clubshirtno");
				
				
			}
			C3p0FootBallPool.attemptClose(rs);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return club;
	}
	
	
	public static FBFootballer getFootballerByID7m(int id_7m) {
		PreparedStatement ps;
		FBFootballer club = null;
		try {
			Connection connection = C3p0FootBallPool.getConnection();
			ps = connection.prepareStatement("SELECT id,NAME,avatar,country,birthday,height,weight,club_id,POSITION,join_date,transfer_free,"
					+ "former_club,one_club,clubshirtno,DATE_FORMAT(create_date,'%Y/%m%d') ymd  FROM  vtc_bongda.fb_footballer "
					+ "WHERE id_7m  = ?  ");
			ps.setInt(1, id_7m);
			ResultSet rs =	ps.executeQuery();
			if(rs.next())
			{
				club = new FBFootballer();
				club.id = rs.getInt(1);
				club.fullname = rs.getString(2);
				if(!StringUtil.isEmpty(rs.getString(3)))
				club.avatar ="http://kenhkiemtien.com/upload/bongda/footballer/"+rs.getString("ymd")+"/"+ rs.getString(3);
				
				club.country = rs.getString(4);
			//	club.birthday = rs.getString(5);
				club.height = rs.getString(6);
				club.weight = rs.getString(7);
				club.club_id = rs.getInt("club_id");
				club.position = rs.getString("POSITION");
				club.join_date = rs.getString("join_date");
				club.transfer_free = rs.getString("transfer_free");
				
				club.former_club = rs.getString("former_club");
				club.one_club = rs.getString("one_club");
				club.clubshirtno = rs.getInt("clubshirtno");
				club.ymd = rs.getString("ymd");
				
				
			}
			C3p0FootBallPool.attemptClose(rs);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return club;
	}
	
	
	public static FBCoach getCoachByID7m(int id_7m) {
		PreparedStatement ps;
		FBCoach club = null;
		try {
			Connection connection = C3p0FootBallPool.getConnection();
			ps = connection.prepareStatement("SELECT *,DATE_FORMAT(create_date,'%Y/%m%d') ymd  FROM  vtc_bongda.fb_coach "
					+ "WHERE id_7m  = ?  ");
			ps.setInt(1, id_7m);
			ResultSet rs =	ps.executeQuery();
			if(rs.next())
			{
				club = new FBCoach();
				club.id = rs.getInt("id");
				club.name = rs.getString("name");
				if(!StringUtil.isEmpty(rs.getString("avatar")))
				club.avatar ="http://kenhkiemtien.com/upload/bongda/footballer/"+rs.getString("ymd")+"/"+ rs.getString("avatar");
				
			
				club.ymd = rs.getString("ymd");
				
				
			}
			C3p0FootBallPool.attemptClose(rs);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return club;
	}
	
	public static List<FBMatch> getListMatchTT() {
		PreparedStatement ps;
		FBMatch match = null;
		List<FBMatch> list = new ArrayList<FBMatch>();
		
		try {
			Connection connection = C3p0FootBallPool.getConnection();
			ps = connection.prepareStatement("SELECT * FROM fb_match WHERE match_time BETWEEN DATE_SUB(NOW(),INTERVAL 230 MINUTE)AND DATE_ADD(NOW(),INTERVAL 100 MINUTE)  ");
			ResultSet rs =	ps.executeQuery();
			while(rs.next())
			{
				match = new FBMatch();
				match.id = rs.getInt("id");
				match.url_tyle = rs.getString("url_tyle");
				match.url_code = rs.getString("url_code");
				match.cup_id = rs.getInt("cup_id");
				match.club_id_1 = rs.getInt("club_id_1");
				match.club_id_2 = rs.getInt("club_id_2");
				list.add(match);
			}
			C3p0FootBallPool.attemptClose(rs);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	public static int saveMatchSummary (EventFootball eventFootball) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
			
			ps = conn.prepareStatement("INSERT INTO vtc_bongda.fb_match_summary (match_id,cup_id,club_id, m_minute, "
					+ "event,create_date,create_user,i_minute) VALUES (?,?,?,?,?,NOW(),?,?);");
			
			ps.setInt(1, eventFootball.match_id);
			ps.setInt(2, eventFootball.cup_id);
			ps.setInt(3, eventFootball.club_id);
			ps.setString(4, eventFootball.minute);
			ps.setString(5, eventFootball.event);
			ps.setString(6, "crawler");
			ps.setFloat(7, eventFootball.int_minute);
			
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	public static int updateMatchSummary (EventFootball eventFootball) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
			
			ps = conn.prepareStatement("Update  vtc_bongda.fb_match_summary Set "
					+ " event = ? Where match_id = ? And m_minute = ? ");
			
			ps.setString(1, eventFootball.event);
			ps.setInt(2, eventFootball.match_id);
			ps.setString(3, eventFootball.minute);
			
			
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	public static int deleteMatch (int match_id) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
			
			ps = conn.prepareStatement("DELETE FROM vtc_bongda.fb_match WHERE id = ? ");
			
			ps.setInt(1, match_id);
			
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	
	public static int checkMatchSummary(int match_id,String minute,int club_id) {
		PreparedStatement ps;
		int kq = 0;
		try {
			Connection connection = C3p0FootBallPool.getConnection();
			ps = connection.prepareStatement("SELECT * FROM fb_match_summary WHERE match_id = ? AND m_minute = ? AND club_id = ? ");
			ps.setInt(1, match_id);
			ps.setString(2, minute);
			ps.setInt(3, club_id);
			ResultSet rs =	ps.executeQuery();
			if(rs.next())
			{
				kq = rs.getInt("id");
			}
			C3p0FootBallPool.attemptClose(rs);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return kq;
	}
	
	public static ArrayList<FBCoach> getAllCoach() {
		PreparedStatement ps;
		FBCoach club = null;
		ArrayList<FBCoach> list = new ArrayList<FBCoach>();
		try {
			Connection connection = C3p0FootBallPool.getConnection();
			ps = connection.prepareStatement("SELECT c.*,DATE_FORMAT(create_date,'%Y/%m%d') ymd  FROM  vtc_bongda.fb_coach c ");
			ResultSet rs =	ps.executeQuery();
			while(rs.next())
			{
				club = new FBCoach();
				club.id = rs.getInt(1);
				club.name = rs.getString("name");
				club.name_en = rs.getString("name_en");
				club.country = rs.getString("country");
				club.avatar_source = rs.getString("avatar_source");
				club.ymd = rs.getString("ymd");
				club.id_7m = rs.getInt("id_7m");
				list.add(club);
				
			}
			C3p0FootBallPool.attemptClose(rs);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static void updateAllMatch(){
		int count = countMatch();int limit = 50;
		int page = count/limit+(count%limit>0?1:0);
		int i = 1;
		while(i<=page){
			ArrayList<FBMatch> list = getListMatch(i, limit);
			for (FBMatch fbMatch : list) {
				FBClub club1 = getClubByID(fbMatch.club_id_1);
				FBClub club2 = getClubByID(fbMatch.club_id_2);
				FBCoach coach1 = null;String coach_name_1="";
				FBCoach coach2 = null;String coach_name_2="";
				
				if(club1.coach_id>0){
				 coach1 = getCoach(club1.coach_id);
				 if(coach1!=null)
				 coach_name_1 = coach1.name_en;
				}
				if(club2.coach_id>0){
					 coach2 = getCoach(club2.coach_id);
					 if(coach2!=null)
					 coach_name_2 = coach2.name_en;
				}
				
				updateMatch(fbMatch.id, club1.name, club2.name, coach_name_1, coach_name_2);
				
			}
			i++;
		}
	}
	
	public static ArrayList<FBFootballer> getListFootBaller(int page,int limit) {
		PreparedStatement ps;
		FBFootballer club = null;
		ArrayList<FBFootballer> listMatch = new ArrayList<FBFootballer>();
		try {
			Connection connection = C3p0FootBallPool.getConnection();
			int from = (page-1)*limit;
			ps = connection.prepareStatement("SELECT b.*,DATE_FORMAT(create_date,'%Y/%m%d') ymd  FROM  vtc_bongda.fb_footballer  b limit "+from+","+limit);
			ResultSet rs =	ps.executeQuery();
			while(rs.next())
			{
				club = new FBFootballer();
				club.id = rs.getInt(1);
				club.avatar = rs.getString("avatar");
				club.avatar_source = rs.getString("avatar_source");
				club.ymd = rs.getString("ymd");;
				club.id_7m = rs.getInt("id_7m");
				listMatch.add(club);
				
			}
			C3p0FootBallPool.attemptClose(rs);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listMatch;
	}
	
	public static ArrayList<FBClub> getListClub1(int page,int limit) {
		PreparedStatement ps;
		FBClub club = null;
		ArrayList<FBClub> listMatch = new ArrayList<FBClub>();
		try {
			Connection connection = C3p0FootBallPool.getConnection();
			int from = (page-1)*limit;
			//ps = connection.prepareStatement("SELECT b.*,DATE_FORMAT(create_date,'%Y/%m%d') ymd  FROM  vtc_bongda.fb_club  b WHERE b.logo_source IS NOT NULL AND b.logo IS NULL ORDER BY id  limit "+from+","+limit);
			
			ps = connection.prepareStatement("SELECT b.*,DATE_FORMAT(create_date,'%Y/%m%d') ymd  FROM  vtc_bongda.fb_club  b WHERE country_en IS NULL  ORDER BY id  limit "+from+","+limit);
			ResultSet rs =	ps.executeQuery();
			while(rs.next())
			{
				club = new FBClub();
				club.id = rs.getInt(1);
				club.logo_source = rs.getString("logo_source");
				club.country = rs.getString("country");
				club.ymd = rs.getString("ymd");;
				club.id_7m = rs.getInt("id_7m");
				listMatch.add(club);
				
			}
			C3p0FootBallPool.attemptClose(rs);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listMatch;
	}
	
	
	public static ArrayList<FBCup> getListCup() {
		PreparedStatement ps;
		FBCup club = null;
		ArrayList<FBCup> listMatch = new ArrayList<FBCup>();
		try {
			Connection connection = C3p0FootBallPool.getConnection();
			
			ps = connection.prepareStatement("SELECT *  FROM  vtc_bongda.fb_cup  ");
			ResultSet rs =	ps.executeQuery();
			while(rs.next())
			{
				club = new FBCup();
				club.id = rs.getInt(1);
				club.country = rs.getString("country");
				
				listMatch.add(club);
				
			}
			C3p0FootBallPool.attemptClose(rs);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listMatch;
	}
	
	public static ArrayList<EventFootball> getAllMatchSummary() {
		PreparedStatement ps;
		EventFootball club = null;
		ArrayList<EventFootball> listMatch = new ArrayList<EventFootball>();
		try {
			Connection connection = C3p0FootBallPool.getConnection();
			ps = connection.prepareStatement("SELECT *  FROM  vtc_bongda.fb_match_summary   ");
			ResultSet rs =	ps.executeQuery();
			while(rs.next())
			{
				club = new EventFootball();
				club.id = rs.getInt(1);
				club.minute = rs.getString("m_minute");
				
				listMatch.add(club);
				
			}
			C3p0FootBallPool.attemptClose(rs);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listMatch;
	}
	
	
	public static void downloadAvatarFBaller(){
		int limit = 100;int i = 1;
		while(i<1000000){
			ArrayList<FBFootballer> list = getListFootBaller(i,limit);
			if(list.size()<1)break;
			for (FBFootballer fbCoach : list) {
				String pathFolder = "/home/kktien/domains/kenhkiemtien.com/public_html/kenhkiemtien.com/upload/bongda/footballer/"+fbCoach.ymd+"/";
				File file = new File(pathFolder);
				if(!file.exists())file.mkdirs();
				hdc.util.io.FileUtil.saveImage(fbCoach.avatar_source, pathFolder+fbCoach.id_7m+fbCoach.avatar_source.substring(fbCoach.avatar_source.lastIndexOf(".")));
				updateAvatarFBaller(fbCoach.id,fbCoach.id_7m+fbCoach.avatar_source.substring(fbCoach.avatar_source.lastIndexOf(".")));
			}
			i++;
		}
	}
	
	public static void downloadAvatarCoach (){
		ArrayList<FBCoach> list = getAllCoach();
		for (FBCoach fbCoach : list) {
			String pathFolder = "C:/Projects/TruyenAudio/"+fbCoach.ymd+"/";
			File file = new File(pathFolder);
			if(!file.exists())file.mkdirs();
			
			
			hdc.util.io.FileUtil.saveImage(fbCoach.avatar_source, pathFolder+fbCoach.id_7m+fbCoach.avatar_source.substring(fbCoach.avatar_source.lastIndexOf(".")));
			updateAvatarCoach(fbCoach.id,fbCoach.id_7m+fbCoach.avatar_source.substring(fbCoach.avatar_source.lastIndexOf(".")));
		}
	}
	
	
	public static void downloadLogoClub(){
		int limit = 100;int i = 1;
		while(i<1000000){
			ArrayList<FBClub> list = getListClub(i,limit);
			if(list.size()<1)break;
			for (FBClub fbCoach : list) {
				//String pathFolder = "/home/kktien/domains/kenhkiemtien.com/public_html/kenhkiemtien.com/upload/bongda/club/"+fbCoach.ymd+"/";
				String pathFolder = "C:/Projects/footballer/"+fbCoach.ymd+"/";
				File file = new File(pathFolder);
				if(!file.exists())file.mkdirs();
				hdc.util.io.FileUtil.saveImage(fbCoach.logo_source, pathFolder+fbCoach.id+fbCoach.logo_source.substring(fbCoach.logo_source.lastIndexOf(".")));
				updateLogoClub(fbCoach.id,fbCoach.id+fbCoach.logo_source.substring(fbCoach.logo_source.lastIndexOf(".")));
			}
			i++;
		}
	}
	
	
	public static void updateAllCountryEnClub(){
		int limit = 100;int i = 1;
		CountryUnit.getCountry();
		HashMap<String,String> countryMap =CountryUnit.countryMap;
		System.out.println(CountryUnit.countryMap.size());
		while(i<1000000){
			ArrayList<FBClub> list = getListClub(i,limit);
			if(list.size()<1)break;
			for (FBClub club : list) {
				//System.out.println(club.id);
					String countryEn = CountryUnit.countryMap.get(UTF8Tool.coDau2KoDau(club.country).toLowerCase());
					if(countryEn!=null){
						System.out.println(club.id+countryEn);
						updateClubCountryEn(club.id, countryEn.trim());
					}

			}
			i++;
		}
	}
	
	
	public static void updateAllMatchClubLogo(){
		
			ArrayList<FBMatch> list = getListMatchErroLogo();
			for (FBMatch match : list) {
				try {
					FBClub club1 = getClubByID(match.club_id_1);
					FBClub club2 = getClubByID(match.club_id_2);
					if(club1.logo!=null)
					match.club_logo_1 = club1.ymd+club1.logo;
					if(club2.logo!=null)
					match.club_logo_2 = club2.ymd+club2.logo;
					updateMatchClubLogo(match.id, match.club_logo_1 , match.club_logo_2);
				} catch (Exception e) {
					// TODO: handle exception
				}
				

			}
			
		
	}
	
	
	public static void updateAllCountryEnCup(){
		CountryUnit.getCountry();
		HashMap<String,String> countryMap =CountryUnit.countryMap;
		System.out.println(CountryUnit.countryMap.size());
		
			ArrayList<FBCup> list = getListCup();
		
			for (FBCup club : list) {
				//System.out.println(club.id);
					String countryEn = CountryUnit.countryMap.get(UTF8Tool.coDau2KoDau(club.country).toLowerCase());
					if(countryEn!=null){
						System.out.println(club.id+countryEn);
						updateCupCountryEn(club.id, countryEn.trim());
					}

			}
			
	}
	
	
	public static void updateAllIMinute(){
		
			ArrayList<EventFootball> list = getAllMatchSummary();
			
			for (EventFootball fbCoach : list) {
				fbCoach.int_minute = Float.parseFloat(fbCoach.minute.replace("+", "."));
				
				updateIMinuteMatchSummary(fbCoach.id, fbCoach.int_minute);
			}
			
	}
	
	public static ArrayList<FBClub> getListClub(int page,int limit) {
		PreparedStatement ps;
		FBClub club = null;
		ArrayList<FBClub> listMatch = new ArrayList<FBClub>();
		try {
			Connection connection = C3p0FootBallPool.getConnection();
			int from = (page-1)*limit;
			//ps = connection.prepareStatement("SELECT b.*,DATE_FORMAT(create_date,'%Y/%m%d') ymd  FROM  vtc_bongda.fb_club  b WHERE b.logo_source IS NOT NULL AND b.logo IS NULL ORDER BY id  limit "+from+","+limit);
			
			//ps = connection.prepareStatement("SELECT b.*,DATE_FORMAT(create_date,'%Y/%m%d') ymd  FROM  vtc_bongda.fb_club  b WHERE country_en IS NULL  ORDER BY id  limit "+from+","+limit);
			ps = connection.prepareStatement("SELECT b.*,DATE_FORMAT(create_date,'%Y/%m%d') ymd  FROM  vtc_bongda.fb_club  b WHERE logo LIKE '%http://data.7m.cn/team_data%'");
			ResultSet rs =	ps.executeQuery();
			while(rs.next())
			{
				club = new FBClub();
				club.id = rs.getInt(1);
				club.logo_source = rs.getString("logo_source");
				club.country = rs.getString("country");
				club.ymd = rs.getString("ymd");;
				club.id_7m = rs.getInt("id_7m");
				listMatch.add(club);
				
			}
			C3p0FootBallPool.attemptClose(rs);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listMatch;
	}
	
	public static int saveCountry (FBCountry country) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0FootBallPool.getConnection(); 
			conn.setAutoCommit(false);
			
			ps = conn.prepareStatement("INSERT INTO vtc_bongda.fb_country (NAME, name_en, flag, create_date) "
					+ " VALUES(?, ?, ?, NOW());");
			
			ps.setString(1, country.name);
			ps.setString(2, country.name_en);
			ps.setString(3, country.flag);
			
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	public static ArrayList<FBCountry> getListCountryFromClubAnSave() {
		PreparedStatement ps;
		FBCountry club = null;
		ArrayList<FBCountry> listMatch = new ArrayList<FBCountry>();
		try {
			Connection connection = C3p0FootBallPool.getConnection();
			ps = connection.prepareStatement("SELECT DISTINCT country,country_en FROM fb_club WHERE country_en IS NOT NULL  ORDER BY country_en");
			ResultSet rs =	ps.executeQuery();
			while(rs.next())
			{
				club = new FBCountry();
				club.flag = "";
				club.name = rs.getString("country");
				club.name_en = rs.getString("country_en");;
				listMatch.add(club);
				saveCountry(club);
				
			}
			C3p0FootBallPool.attemptClose(rs);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listMatch;
	}
	
	public static void processBlankMatch(){
		int limit = 1000;int i = 1;
		while(i<1000000){
			ArrayList<FBMatch> listMatch = getListMatch(i, limit);
			if(listMatch.size()<1)break;
				for (FBMatch match : listMatch) {
					match.round = match.round .replaceAll("^\\W+","");
					match.result = match.result.replaceAll("[^\\d-]","");
					match.result_1 = match.result_1.replaceAll("[^\\d-]","");
					if(StringUtil.isEmpty(match.match_minute)) match.match_minute = match.hi;
					updateMatchKT(match);
				 
					System.out.println(match.id);
				}
				i++;
		}
	}
	
	public static void  createFileCountryTxt() {
		PreparedStatement ps;
		FBCountry club = null;
		
		try {
			Connection connection = C3p0FootBallPool.getConnection();
			ps = connection.prepareStatement("SELECT name,name_en FROM fb_country ORDER BY name_en");
			ResultSet rs =	ps.executeQuery();
			while(rs.next())
			{
				club = new FBCountry();
				club.flag = "";
				club.name = rs.getString("name");
				club.name_en = rs.getString("name_en");;
				try {
					FileUtil.writeToFile("./conf/country.txt", club.name+"_"+club.name_en+"\r\n", true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			C3p0FootBallPool.attemptClose(rs);
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static int saveProductSwap (String title, String description, String images,int user_id, int cate, float price,int product_type) {
		int id = 0;		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0ShopPool.getConnection(); 
			conn.setAutoCommit(false);
			
			ps = conn.prepareStatement("INSERT INTO vtc_swaphub.ms_product (title,cate_id,user_id,price,"
					+ "price_rent,deposit,use_status,use_time,quantity,transport_fee,image,"
					+ "product_type,wish_swap,sta_comment,sta_like,sta_view,description,create_date,"
					+ "create_user,update_date,update_user) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW(),'crawler',NOW(),'crawler');");
			int use_status = 1;
			int use_time = 1;int quantity = 1;int transport_fee = 1;
			String wish_swap="";
			ps.setString(1, title);
			ps.setInt(2, cate);
			ps.setInt(3, user_id);
			ps.setFloat(4, price);
			ps.setString(5, "");
			ps.setFloat(6, 0);
			ps.setInt(7, use_status);
			ps.setInt(8, use_time);
			ps.setInt(9, quantity);
			ps.setInt(10, transport_fee);
			ps.setString(11, images);
			ps.setInt(12, product_type);
			ps.setString(13, wish_swap);
			ps.setInt(14, 0);
			ps.setInt(15, 0);
			ps.setInt(16, 0);
			ps.setString(17,description);
			
			
			ps.execute();
			conn.commit();
			conn.setAutoCommit(true);
			
			C3p0FootBallPool.attemptClose(ps);
			C3p0FootBallPool.attemptClose(conn);	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	
	public static void main(String args[]){
		processtMatchNullBDWapID();
	}
	
}
