package com.ttv.football;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import hdc.crawler.fetcher.HttpClientImpl;
import hdc.crawler.fetcher.HttpClientUtil;
import hdc.util.text.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.http.HttpResponse;

import com.az24.crawler.model.FBClub;
import com.az24.crawler.model.FBCoach;
import com.az24.crawler.model.FBFootballer;
import com.az24.dao.FootBallDAO;
import com.az24.util.io.FileUtil;
import com.az24.util.io.IOUtil;


public class ClubAndFootballer7mCrawler {
	// Get Danh Sach Cau Lac bo theo giai
	// Cap nhat thong tin cau lac bo de ID goc de tranh nham
	// Lay danh sach cau thu va cap nhap danh sach cau thu de ID goc de tranh bi
	// nham
	 
	public static String folderUpCoachImage ="/home/kktien/domains/kenhkiemtien.com/public_html/kenhkiemtien.com/upload/bongda/coach/";
	public static String folderUpFooterImage ="/home/kktien/domains/kenhkiemtien.com/public_html/kenhkiemtien.com/upload/bongda/footballer/";
	public static String folderUpLog ="/home/crawler/log/";
	
	public static void crawlerClubFootballer(int team_id) throws Exception {

		HttpClientImpl client = new HttpClientImpl();
		String url_team = "http://team.7m.cn/" + team_id + "/data/info_en.js";
		HttpResponse res = client.fetch(url_team);

		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);
		String club_info = html.replaceFirst("var teamInfo =", "");

		JSONObject json = (JSONObject) JSONSerializer.toJSON(club_info);
		FBClub fbClub = new FBClub();
		FBCoach fbCoach = new FBCoach();

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		// Coach info
		int coach_id = 0;
		try {
			if(json.getString("c_id")!=null){
			fbCoach.id_7m = Integer.parseInt(json.getString("c_id"));
			fbCoach.name = json.getString("c_name");
			fbCoach.name_en = json.getString("c_name_en");

			try {
				Date date = formatter.parse(json.getString("c_birthday"));
				fbCoach.birthday = formatter.format(date);
			} catch (ParseException e) {
				e.printStackTrace();
				fbCoach.birthday = null;
			}

			fbCoach.height = json.getString("c_height");
			fbCoach.weight = json.getString("c_weight");
			fbCoach.joindate = json.getString("c_joindate");
			fbCoach.avatar = json.getString("c_logo");

			if ("1".equalsIgnoreCase(fbCoach.avatar))
				fbCoach.avatar_source = "http://data.7m.cn/player_data/"
						+ fbCoach.id_7m + "/logo_img/player_photo.jpg";
			else
				fbCoach.avatar_source = "http://data.7m.cn/player_data/"
						+ fbCoach.id_7m + "/logo_Img/player_photo.gif";

			fbCoach.country = json.getString("c_country_en");
			fbCoach.formerclub = json.getString("c_formerclub_en");
			fbCoach.formerclub = json.getString("c_formerclub_en");
			fbCoach.onceclub_en = json.getString("c_onceclub_en");
			
				Date date_match = new Date(Calendar.getInstance().getTime()
						.getTime());
				String pattern = "yyyy/MMdd";
				SimpleDateFormat format = new SimpleDateFormat(pattern);
				String ymd = format.format(date_match);
			
			String pathFolder = folderUpCoachImage+ymd+"/";
			File file = new File(pathFolder);
			if (!file.exists())
				file.mkdirs();

			String nameimage = fbCoach.id_7m
					+ fbCoach.avatar_source
							.substring(fbCoach.avatar_source
									.lastIndexOf("."));
			hdc.util.io.FileUtil.saveImage(fbCoach.avatar_source,
					pathFolder + nameimage);
			fbCoach.avatar = nameimage;

			FBCoach fbCoachCheck = FootBallDAO.getCoachByID7m(fbCoach.id_7m);
			coach_id = fbCoachCheck.id;
			if (fbCoachCheck != null)
				coach_id = fbCoachCheck.id;
			}else{
				coach_id = FootBallDAO.saveCoach(fbCoach);
			}

		} catch (Exception exception) {
			System.out.println("Loi Team:" + url_team);
			FileUtil.writeToFile(folderUpLog+"logCoach.txt", "," + team_id
					+ "-" + json.getString("name"), true);
			 exception.printStackTrace();
		}

		// Club Info
		fbClub.id_7m = Integer.parseInt(json.getString("id"));
		fbClub.name = json.getString("name");
		fbClub.name_en = json.getString("name_en");
		fbClub.established_date = json.getString("established_date");
		fbClub.stadium = json.getString("stadium_en");
		fbClub.stadium_capacity = json.getString("capacity");
		fbClub.website = json.getString("website");
		fbClub.email = json.getString("email");
		fbClub.map = json.getString("map");
		fbClub.address = json.getString("address_en");
		try {
			fbClub.avgage = Float.parseFloat(json.getString("avgage"));
		} catch (Exception e) {

		}

		String logo = json.getString("logo");

		if ("1".equalsIgnoreCase(logo))
			fbClub.logo_source = "http://data.7m.cn/team_data/" + fbClub.id_7m
					+ "/logo_Img/club_logo.jpg";
		else
			fbClub.logo_source = "http://data.7m.cn/team_data/" + fbClub.id_7m
					+ "/logo_Img/club_logo.gif";

		fbClub.city = json.getString("city_en");
		String country_en = json.getString("country_en");
		fbClub.country = country_en;
		fbClub.country_en = country_en;
		fbClub.coach_id = coach_id;

		int club_id = 0;
		FBClub club1 = FootBallDAO.getClubByNamen(fbClub.name.toUpperCase());
		if (club1 == null)
		{
			FileUtil.writeToFile(folderUpLog+"/logclubKhongTimThayKKT.txt", "," + team_id
					+ "-" + fbClub.name, true);
			return;
		} else {
			fbClub.id = club1.id;
			club_id = club1.id;
			FootBallDAO.updateClubNoAvatar(fbClub);
		}

		// Footballer
		res = client.fetch("http://team.7m.cn/" + team_id
				+ "/data/player_vn.js");
		HttpClientUtil.printResponseHeaders(res);
		html = HttpClientUtil.getResponseBody(res);
		String footballer_info = html.replaceFirst("var teamPlayer =", "");


		json = (JSONObject) JSONSerializer.toJSON(footballer_info.substring(0,
				footballer_info.length() - 1));
		// Lay Tien Dao
		JSONObject player = json.getJSONObject("player");
		JSONArray tienDao = player.getJSONArray("0");
		JSONArray tienve = player.getJSONArray("1");
		JSONArray hauve = player.getJSONArray("2");
		JSONArray thumon = player.getJSONArray("3");
		int i = 0;
		// Dua ve trang thai inactive
		FootBallDAO.updateAllFootballerClub(club_id, 0);

		while (i < tienDao.size()) {
			System.out.println(tienDao.get(i));
			JSONArray cauthu = (JSONArray) tienDao.get(i);
			System.out.println(cauthu.get(0));
			String url_footballer = "http://player.7m.cn/" +cauthu.get(0)
					+ "/data/info_en.js";
			String soao =(String) cauthu.get(2);
			crawlerFootballer(url_footballer, club_id,soao);
			i++;
		}

		i = 0;
		while (i < tienve.size()) {
			System.out.println(tienve.get(i));
			JSONArray cauthu = (JSONArray) tienve.get(i);
			System.out.println(cauthu.get(0));
			String url_footballer = "http://player.7m.cn/" + cauthu.get(0)
					+ "/data/info_en.js";
			String soao =(String) cauthu.get(2);
			crawlerFootballer(url_footballer, club_id,soao);
			i++;
		}

		i = 0;
		while (i < hauve.size()) {
			System.out.println(hauve.get(i));
			JSONArray cauthu = (JSONArray) hauve.get(i);
			System.out.println(cauthu.get(0));
			String url_footballer = "http://player.7m.cn/" + cauthu.get(0)
					+ "/data/info_en.js";
			String soao =(String) cauthu.get(2);
			crawlerFootballer(url_footballer, club_id,soao);
			i++;
		}

		i = 0;
		while (i < thumon.size()) {
			System.out.println(thumon.get(i));
			JSONArray cauthu = (JSONArray) thumon.get(i);
			System.out.println(cauthu.get(0));
			String url_footballer = "http://player.7m.cn/" + cauthu.get(0)
					+ "/data/info_en.js";
			String soao =(String) cauthu.get(2);
			crawlerFootballer(url_footballer, club_id,soao);
			i++;
		}

	}

	public static void crawlerFootballer(String url_short, int club_id,String soao) {
		try {
			int inSoAoDoiHinh = 0;
			try {
				inSoAoDoiHinh = Integer.parseInt(soao);
			} catch (Exception e) {
				// TODO: handle exception
			}
			HttpClientImpl client = new HttpClientImpl();

			HttpResponse res = client.fetch(url_short);
			HttpClientUtil.printResponseHeaders(res);
			String html = HttpClientUtil.getResponseBody(res);
			String footballer_info = html.replaceFirst("var playerInfo =", "");

			JSONObject json = (JSONObject) JSONSerializer
					.toJSON(footballer_info);

			FBFootballer fbFootballer = new FBFootballer();
			// Club Info
			fbFootballer.id_7m = Integer.parseInt(json.getString("id"));
			fbFootballer.fullname = json.getString("name");
			fbFootballer.country = json.getString("nationality_en");
			fbFootballer.former_club = json.getString("formerclub_en");
			fbFootballer.height = json.getString("height");
			fbFootballer.weight = json.getString("weight");
			fbFootballer.position = json.getString("position");
			String clubid7m  = json.getString("clubid");
			String clubNameEn = json.getString("club_en");
			
			if(!StringUtil.isEmpty(clubNameEn))clubNameEn = clubNameEn.toUpperCase().replaceAll("\\[ON LOAN\\]||", "");
			if("Edin Dzeko".equalsIgnoreCase(fbFootballer.fullname))
			System.out.println("---------->"+clubNameEn);
			FBClub club = null;
			if(!StringUtil.isEmpty(clubid7m)||!StringUtil.isEmpty(clubNameEn))
			 club = FootBallDAO.getClubByNamenOrID7M(clubid7m,clubNameEn);

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date date = formatter.parse(json.getString("birthday"));
				fbFootballer.birthday = formatter.format(date);
			} catch (ParseException e) {
				e.printStackTrace();
				fbFootballer.birthday = null;
			}

			fbFootballer.avatar = json.getString("logo");

			if ("1".equalsIgnoreCase(fbFootballer.avatar))
				fbFootballer.avatar_source = "http://data.7m.cn/player_data/"
						+ fbFootballer.id_7m + "/logo_img/player_photo.jpg";
			else
				fbFootballer.avatar_source = "http://data.7m.cn/player_data/"
						+ fbFootballer.id_7m + "/logo_Img/player_photo.gif";

			FBFootballer fbCheck = FootBallDAO
					.getFootballerByID7m(fbFootballer.id_7m);

			String ymd = "";
			if (fbCheck == null) {
				Date date_match = new Date(Calendar.getInstance().getTime()
						.getTime());
				String pattern = "yyyy/MMdd";
				SimpleDateFormat format = new SimpleDateFormat(pattern);
				ymd = format.format(date_match);
			} else
				ymd = fbCheck.ymd;

			
			String pathFolder = folderUpFooterImage+ymd+"/";
			File file = new File(pathFolder);
			if (!file.exists())
				file.mkdirs();

			String nameimage = fbFootballer.id_7m
					+ fbFootballer.avatar_source
							.substring(fbFootballer.avatar_source
									.lastIndexOf("."));
			hdc.util.io.FileUtil.saveImage(fbFootballer.avatar_source,
					pathFolder + nameimage);

			fbFootballer.avatar = nameimage;
			fbFootballer.one_club = json.getString("onceclub_en");
			fbFootballer.transfer_free = json.getString("transferfee_en");
			try {
				fbFootballer.clubshirtno = Integer.parseInt(json
						.getString("clubshirtno"));
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			if(club!=null)
			fbFootballer.club_id = club.id;
			else fbFootballer.club_id = 0;

			int footballer_id = 0;

			if (fbCheck == null) {
				footballer_id = FootBallDAO.saveFootballer(fbFootballer);
			} else {
				fbFootballer.id = fbCheck.id;
				footballer_id = fbCheck.id;
				FootBallDAO.updateFootballerNoavatar(fbFootballer);
			}

			if (FootBallDAO.checkFootballClub(club_id, footballer_id) > 0)
				FootBallDAO.updateFootballerClub(club_id, footballer_id,
						inSoAoDoiHinh, 1);
			else
				FootBallDAO.saveFootballerClub(club_id, footballer_id,
						inSoAoDoiHinh);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public static void main(String args[]) {

		// http://team.7m.cn/567/data/player_vn.js
		// http://player.7m.cn/3641/data/info_en.js
		// http://data2.7m.cn/matches_data/92/vn/fixture.js
		// http://data2.7m.cn/matches_data/92/vn/index.js giai dau
		/*
		 * if (teamInfo["logo"] == 1) $$("logo_img").src =
		 * "http://data.7m.cn/team_data/" + teamId + "/logo_Img/club_logo.jpg";
		 * else if (teamInfo["logo"] == 2) $$("logo_img").src =
		 * "http://data.7m.cn/team_data/" + teamId + "/logo_Img/club_logo.gif";
		 * else $$("logo_img").src =
		 * "http://data.7m.cn/team_data/share_Img/err.gif";
		 * size_Img($$("logo_img"));
		 */
		// $$("e_coach_photo").src ="http://data.7m.cn/player_data/" +
		// teamInfo["c_id"] + "/logo_img/player_photo" + (teamInfo["c_logo"] ==
		// 1 ? ".jpg" : ".gif");

		String dsClub = "264,";
		ClubAndFootballer7mCrawler.folderUpCoachImage = "C:/Projects/footballer/coach/"; 
		ClubAndFootballer7mCrawler.folderUpFooterImage = "C:/Projects/footballer/footballer/"; 
		ClubAndFootballer7mCrawler.folderUpLog="C:/Projects/footballer/";
		try {
			 dsClub = IOUtil.getFileContenntAsString(new File("C:/Projects/footballer/dsclub.txt"));
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		String arrClub[] = dsClub.split(",");
		HashMap<String, String> map = new HashMap<String, String>();
		for (String clubID : arrClub) {
			map.put(clubID, clubID);
		}
		
		Iterator<String> th = map.values().iterator();
		while (th.hasNext()) {
			// String clubID = "20";
			String clubID = th.next();
			try {
				ClubAndFootballer7mCrawler.crawlerClubFootballer(Integer
						.parseInt(clubID));

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					FileUtil.writeToFile("C:/Projects/footballer/logclubLoi.txt",
							"," + clubID, true);
					// FileUtil.writeToFile("C:/Projects/footballer/logclubLoi.txt",
					// ","+clubID, true);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

	}
	
	
	public static void main1(String args[]) {

		// http://team.7m.cn/567/data/player_vn.js
		// http://player.7m.cn/3641/data/info_en.js
		// http://data2.7m.cn/matches_data/92/vn/fixture.js
		// http://data2.7m.cn/matches_data/92/vn/index.js giai dau
		/*
		 * if (teamInfo["logo"] == 1) $$("logo_img").src =
		 * "http://data.7m.cn/team_data/" + teamId + "/logo_Img/club_logo.jpg";
		 * else if (teamInfo["logo"] == 2) $$("logo_img").src =
		 * "http://data.7m.cn/team_data/" + teamId + "/logo_Img/club_logo.gif";
		 * else $$("logo_img").src =
		 * "http://data.7m.cn/team_data/share_Img/err.gif";
		 * size_Img($$("logo_img"));
		 */
		// $$("e_coach_photo").src ="http://data.7m.cn/player_data/" +
		// teamInfo["c_id"] + "/logo_img/player_photo" + (teamInfo["c_logo"] ==
		// 1 ? ".jpg" : ".gif");

		String dsClub = "5,";
		final File folder = new File("/home/crawler/club/");
		 for (final File fileEntry : folder.listFiles()) {
			 		try {
						dsClub = IOUtil.getFileContenntAsString(new File("/home/crawler/club/"+fileEntry.getName()));
					} catch (Exception e2) {
						e2.printStackTrace();
					}
		            System.out.println(fileEntry.getName());
		        	String arrClub[] = dsClub.split(",");
		    		HashMap<String, String> map = new HashMap<String, String>();
		    		for (String clubID : arrClub) {
		    			map.put(clubID, clubID);
		    		}
		    		
		    		Iterator<String> th = map.values().iterator();
		    		String clubID ="";
		    		while (th.hasNext()) {
		    			 clubID += th.next()+",";
		    		}
		    		
		    		try {
						FileUtil.writeToFile("/home/crawler/club/"+fileEntry.getName(), clubID, false);
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
		    		
		    		th = map.values().iterator();
		    		while (th.hasNext()) {
		    			 clubID = th.next();
		    			try {
		    				ClubAndFootballer7mCrawler.crawlerClubFootballer(Integer
		    						.parseInt(clubID));

		    			} catch (Exception e) {
		    				e.printStackTrace();
		    				try {
		    					FileUtil.writeToFile("/home/crawler/log/logclubLoi.txt",
		    							"," + clubID, true);
		    						} catch (IOException e1) {
		    					e1.printStackTrace();
		    				}
		    			}
		    		}
		        
		    }
		 
		
	

	}
}
