package com.ttv.football;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.crawler.fetcher.HttpClientUtil;
import hdc.util.html.parser.XPathReader;
import hdc.util.text.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathConstants;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.w3c.dom.NodeList;

import com.az24.crawler.model.EventFootball;
import com.az24.crawler.model.EventFootballComparator;
import com.az24.crawler.model.EventFootballSub;
import com.az24.crawler.model.FBCup;
import com.az24.crawler.model.FBFootballer;
import com.az24.crawler.model.FBMatch;
import com.az24.crawler.model.FBMatchLive;
import com.az24.crawler.model.FBMatchStatictis;
import com.az24.crawler.model.FBMatchTeam;
import com.az24.dao.FootBallDAO;
import com.az24.test.Base64Coder;

public class TrucTiepTranDauCrawler {
	public static void crawlerTuongThuatTranDau(String urlTT,int match_id,int fbcup_id,int club_id_1,int club_id_2)throws Exception {
			HttpClientImpl client = new HttpClientImpl();
		
			HttpResponse res = client.fetch(urlTT);
			//HttpClientUtil.printResponseHeaders(res);
			String html = HttpClientUtil.getResponseBody(res);
			
		
			XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
			//CrawlerUtil.analysis(reader.getDocument());

			String xpath_node_content = "//TABLE[1]/TBODY/TR";

			NodeList nodes = (NodeList) reader.read(xpath_node_content,XPathConstants.NODESET);
			HashMap<String,String> mapItemWeb = new HashMap<String, String>();
			String domain = "http://www.kenhkiemtien.com";
			mapItemWeb.put("out_web out_item_web", domain+"/upload/bongda/icon/out.png");
			mapItemWeb.put("in_web in_item_web", domain+"/upload/bongda/icon/in.png");
			mapItemWeb.put("card_web red_card_web", domain+"/upload/bongda/icon/rc.png");
			
			mapItemWeb.put("card_web yellow_card_web", domain+"/upload/bongda/icon/yc.png");
			mapItemWeb.put("card_web two_yellow_card_web", domain+"/upload/bongda/icon/2yc.png");
			mapItemWeb.put("goal_web goal_item_web", domain+"/upload/bongda/icon/g.png");
			mapItemWeb.put("goal_web goalpenalty_item_web", domain+"/upload/bongda/icon/gp.png");
			mapItemWeb.put("goal_web ownergoal_item_web", domain+"/upload/bongda/icon/go.png");
			
			if (nodes != null) {
				int node_one_many = nodes.getLength();
				
				String xpath_node_doi1 = "//DIV[@class='DH_left']/SPAN/text()";
				String doi1  = (String) reader.read(xpath_node_doi1, XPathConstants.STRING);
				//logger.info(doi1.trim());
				
				String xpath_node_doi2 = "//DIV[@class='DH_right']/SPAN/text()";
				String doi2  = (String) reader.read(xpath_node_doi2, XPathConstants.STRING);
				//logger.info(doi2.trim());
				
				int i = 1;
				// Đội hình
				List<String> listMainTeam1 = new ArrayList<String>();
				List<String> listMainTeam2 = new ArrayList<String>();
				List<EventFootball> events1 = new ArrayList<EventFootball>();
				List<EventFootball> events2 = new ArrayList<EventFootball>();
				
			    // Lay danh sach cau thu thi dau
				while (i <= node_one_many) {
					
					String colspan  = (String) reader.read(xpath_node_content + "[" + i + "]"
							+ "/TD[1]/@colspan", XPathConstants.STRING);
					
						if(!"6".equalsIgnoreCase(colspan)){
							String text_so = (String) reader.read(xpath_node_content + "[" + i + "]"
									+ "/TD[1]/DIV/text()", XPathConstants.STRING);
							String txt_ten = (String) reader.read(xpath_node_content + "[" + i + "]"
									+ "/TD[2]/SPAN/text()", XPathConstants.STRING);
							
							String txt_ten2  = (String) reader.read(xpath_node_content + "[" + i + "]"
									+ "/TD[3]/SPAN/text()", XPathConstants.STRING);
							String text_so2 = (String) reader.read(xpath_node_content + "[" + i + "]"
									+ "/TD[4]/DIV/text()", XPathConstants.STRING);
							
							//logger.info("----------------");
							FBFootballer footer1 = FootBallDAO.getFootballerInerJoinClub(text_so, club_id_1);
							
							String cauthu1 = txt_ten.trim()+"|"+text_so.trim();
							if(footer1!=null)
							 cauthu1 = txt_ten.trim()+"|"+text_so.trim()+"|"+footer1.id+"|"+footer1.avatar.trim();
							
							
							FBFootballer footer2 = FootBallDAO.getFootballerInerJoinClub(text_so2, club_id_2);
							String cauthu2 = txt_ten2.trim()+"|"+text_so2.trim();
							if(footer2!=null)
								cauthu2 = txt_ten2.trim()+"|"+text_so2.trim()+"|"+footer2.id+"|"+footer2.avatar.trim();
							
							listMainTeam1.add(cauthu1);
							listMainTeam2.add(cauthu2);
							
							//logger.info(text_so.trim()+":"+txt_ten.trim()+"---"+text_so2.trim()+":"+txt_ten2.trim());
							
							
							int stt_tt = 2;
							
							NodeList node_span_1 = (NodeList) reader.read(xpath_node_content + "[" + i + "]"
									+ "/TD[2]/SPAN", XPathConstants.NODESET);
							
							// Lay thong tin tuong thuat tran dau club 1
							while(stt_tt<=node_span_1.getLength()){
								String span_item_webclass = (String) reader.read(xpath_node_content + "[" + i + "]"
										+ "/TD[2]/SPAN["+stt_tt+"]/@class", XPathConstants.STRING);
								stt_tt++;
								String span_item_minute = (String) reader.read(xpath_node_content + "[" + i + "]"
										+ "/TD[2]/SPAN["+stt_tt+"]/text()", XPathConstants.STRING);
								
								if(mapItemWeb.get(span_item_webclass)!=null){
									EventFootball eventFootball = new EventFootball();
									eventFootball.footballer = txt_ten;
									eventFootball.icon = mapItemWeb.get(span_item_webclass);
									eventFootball.match_id = match_id;
									eventFootball.club_id = club_id_1;
									eventFootball.minute = span_item_minute.trim();
									eventFootball.cup_id  = fbcup_id;
									try {
										eventFootball.int_minute = Float.parseFloat(span_item_minute.replace("+", "."));
										events1.add(eventFootball);
									} catch (Exception e) {
									}
									
									if(span_item_webclass.indexOf("red_card_web")>0) FootBallDAO.updateMatchRedCard1(match_id, 1);
									if(span_item_webclass.indexOf("two_yellow_card_web")>0) FootBallDAO.updateMatchRedCard1(match_id, 1);
								}
								//logger.info("----------------"+span_item_webclass);
								stt_tt++;
							}
							
							// Lay thong tin tuong thuat tran dau club 2
							stt_tt = 2;
							NodeList node_span_2 = (NodeList) reader.read(xpath_node_content + "[" + i + "]"
									+ "/TD[3]/SPAN", XPathConstants.NODESET);
							while(stt_tt<=node_span_2.getLength()){
								String span_item_webclass = (String) reader.read(xpath_node_content + "[" + i + "]"
										+ "/TD[3]/SPAN["+stt_tt+"]/@class", XPathConstants.STRING);
								stt_tt++;
								String span_item_minute = (String) reader.read(xpath_node_content + "[" + i + "]"
										+ "/TD[3]/SPAN["+stt_tt+"]/text()", XPathConstants.STRING);
								
								if(mapItemWeb.get(span_item_webclass)!=null){
									EventFootball eventFootball = new EventFootball();
									eventFootball.footballer = txt_ten2;
									eventFootball.icon = mapItemWeb.get(span_item_webclass);
									eventFootball.match_id = match_id;
									eventFootball.club_id = club_id_2;
									eventFootball.minute = span_item_minute.trim();
									eventFootball.cup_id  = fbcup_id;
									try {
										eventFootball.int_minute = Float.parseFloat(span_item_minute.replace("+", "."));
										events2.add(eventFootball);
									} catch (Exception e) {
									}
									if(span_item_webclass.indexOf("red_card_web")>0) FootBallDAO.updateMatchRedCard2(match_id, 1);
									if(span_item_webclass.indexOf("two_yellow_card_web")>0) FootBallDAO.updateMatchRedCard2(match_id, 1);
								}
								//logger.info("----------------"+span_item_webclass);
								stt_tt++;
							}
							//logger.info("----------------");
						}else{
							/*String text_so = (String) reader.read(xpath_node_content + "[" + i + "]"
									+ "/TD[1]/B/text()", XPathConstants.STRING);*/
							//logger.info(text_so.trim());
						}
							i++;
						
					}
				
			// Gop va sap xep thong tin tuong thuat	
			   events1 = groupEvent(events1);
			   events2 = groupEvent(events2);
			   i = 0;
			   while(i<events1.size()){
				   EventFootball eventFootball = events1.get(i);
				  // logger.info(eventFootball.int_minute);
				  // logger.info(eventFootball.event);
				   if(FootBallDAO.checkMatchSummary(eventFootball.match_id, eventFootball.minute,eventFootball.club_id)>0){
					   FootBallDAO.updateMatchSummary(eventFootball);
				   }else{
					   FootBallDAO.saveMatchSummary(eventFootball);
				   }
				   i++;
			   }
			   i = 0;
			   while(i<events2.size()){
				   EventFootball eventFootball = events2.get(i);
				  // logger.info(eventFootball.int_minute);
				 //  logger.info(eventFootball.event);
				   if(FootBallDAO.checkMatchSummary(eventFootball.match_id, eventFootball.minute,eventFootball.club_id)>0){
					   FootBallDAO.updateMatchSummary(eventFootball);
				   }else{
					   FootBallDAO.saveMatchSummary(eventFootball);
				   }
				   i++;
			   }
			   
			  // Luu thong tin doi hinh 
			   if(listMainTeam1.size()>0){
					
					JSONArray jsonA = JSONArray.fromObject(listMainTeam1);
					String team1 = jsonA.toString();
					jsonA = JSONArray.fromObject(listMainTeam2);
					String team2 = jsonA.toString();
					
					FBMatchTeam team = new FBMatchTeam();
					team.id = 0;
					team.match_id = match_id;
					team.team_1 = team1;
					team.team_2 = team2;
					
					team.formation_1 = doi1.replaceAll("\\(", "").replaceAll("\\)", "").trim();
					team.formation_2 = doi2.replaceAll("\\(", "").replaceAll("\\)", "").trim();
					
					int teamId = FootBallDAO.checkMatchTeam(team.match_id);
					team.id = teamId;
					if(teamId==0)
						FootBallDAO.saveMatchTeam(team);
					else
						FootBallDAO.updateMatchTeam(team);
				}
				
				
				// Crawler Thong ke
				String xpath_node_ = "//DIV[@class='TK_moi']";
				i = 0;
				NodeList nodesTK = (NodeList) reader.read(xpath_node_,
						XPathConstants.NODESET);
				if (nodesTK != null) {
						int node_one_tk = nodesTK.getLength();
						FBMatchStatictis statistic = new FBMatchStatictis();
						statistic.match_id = match_id;

						while (i <= node_one_tk) {
							String gt1  = (String) reader.read(xpath_node_+ "[" + i + "]"
									+ "/DIV[1]", XPathConstants.STRING);
							String gt2  = (String) reader.read(xpath_node_+ "[" + i + "]"
									+ "/DIV[2]", XPathConstants.STRING);
							String gt3  = (String) reader.read(xpath_node_+ "[" + i + "]"
									+ "/DIV[3]", XPathConstants.STRING);
							
							gt1 = gt1.trim();
							gt2 = gt2.trim();
							gt3 = gt3.trim();
							
					/*		logger.info(gt1.trim());
							logger.info(gt2.trim());
							logger.info(gt3.trim());
							logger.info("----------------");*/
							if("Sút bóng".equals(gt2)) statistic.goal_attempts=gt1+"-"+gt3;
							if("Trúng đích".equals(gt2)) statistic.shots_on_goal=gt1+"-"+gt3;
							if("Phạm lỗi".equals(gt2)) statistic.fouls=gt1+"-"+gt3;
							if("Thẻ đỏ".equals(gt2)) statistic.red_card=gt1+"-"+gt3;
							if("Thẻ vàng".equals(gt2)) statistic.yellow_card=gt1+"-"+gt3;
							if("Phạt góc".equals(gt2)) statistic.corner_kicks=gt1+"-"+gt3;
							if("Việt vị".equals(gt2)) statistic.offsides=gt1+"-"+gt3;
							if("Cầm bóng".equals(gt2)) statistic.ball_possession=gt1+"-"+gt3;
							
							i++;
						}
						
						int statictisId = FootBallDAO.checkStatictis(statistic.match_id);
						if(statictisId==0)
							FootBallDAO.saveMatchStatistic(statistic);
						else
							FootBallDAO.updateMatchStatistic(statistic);
					
					}
				
				// Crawler Tuong Thuat
				String xpath_node_tt_ = "//UL";
				i = 1;
				NodeList nodesTT = (NodeList) reader.read(xpath_node_tt_,
						XPathConstants.NODESET);
				if (nodesTT != null) {
						int node_one_tk = nodesTT.getLength();
						
						
						List<FBMatchLive> listLive = new ArrayList<FBMatchLive>();
						while (i <= node_one_tk) {
							String img  = (String) reader.read(xpath_node_tt_+ "[" + i + "]"
									+ "/LI[1]/IMG/@src", XPathConstants.STRING);
							String content  = (String) reader.read(xpath_node_tt_+ "[" + i + "]"
									+ "/LI[1]", XPathConstants.STRING);
							String video  = (String) reader.read(xpath_node_tt_+ "[" + i + "]"
									+ "/LI[1]/SPAN/CENTER[1]/DIV[1]/VIDEO[1]/SOURCE/@src", XPathConstants.STRING);
							String color  = (String) reader.read(xpath_node_tt_+ "[" + i + "]"
									+ "/LI[1]/A/@style", XPathConstants.STRING);
							String image_event  = (String) reader.read(xpath_node_tt_+ "[" + i + "]"
									+ "/LI[1]/SPAN/IMG/@src", XPathConstants.STRING);
							
							//logger.info(img.trim());
						
							/*logger.info(color.trim());
							logger.info("video"+video.trim());
							logger.info("----------------");*/
							if(color.indexOf("background-color: ")>0)
							color = color.substring(color.lastIndexOf("background-color: ")+"background-color: ".length());
							FBMatchLive matchLive = new FBMatchLive();
							matchLive.match_id = match_id;
							if("#FF7E00".equalsIgnoreCase(color)) matchLive.club_id = club_id_1;
							if("#C71883".equalsIgnoreCase(color))  matchLive.club_id = club_id_2;
							matchLive.color = color;
							matchLive.video = video.trim();
							matchLive.image_event = image_event.trim();
							content = content.replaceAll("\t", " ");
							content = content.replaceAll("\n", " ");
							
							
							String arr[] = content.split("'\\s\\s");
							
							//logger.info(content.trim());
							
							Pattern r1 = Pattern.compile("^\\s+(\\d+.*')\\s+(.*$)");
						    Matcher  m1 = r1.matcher(content);
						     
						    if(m1.find())
						      {
						    	 matchLive.time=m1.group(1);
						    	 matchLive.content=m1.group(2);
						    	
						      }
						      
							if(arr.length>1){
								matchLive.content = arr[1].trim().replaceAll("^\\s+|\\s+$|\\s*(\n)\\s*|(\\s)\\s*", "$1$2")
									     .replace("\t"," ");
								matchLive.content = matchLive.content.replaceAll("\t","");
								matchLive.content = matchLive.content.replaceAll("\\n","");;
								matchLive.time = arr[0].trim().replaceAll("^\\s+|\\s+$|\\s*(\n)\\s*|(\\s)\\s*", "$1$2")
									     .replace("\t"," ");
								
								matchLive.time  = matchLive.time.trim();
								matchLive.content = matchLive.content.trim();
								matchLive.content = matchLive.content.replaceAll("^\\W+","");
								
								matchLive.time = matchLive.time.replaceAll("\\t","");
								matchLive.time = matchLive.time.replaceAll("\\n","");
								matchLive.time = matchLive.time.replaceAll("[^\\d\\+]","");
								matchLive.img = img.trim();
								matchLive.image_event = image_event.trim();
								matchLive.video = video.trim();
								
								if(!StringUtil.isEmpty(video)) FootBallDAO.updateMatchHadVideo(match_id, 1);
								
								listLive.add(matchLive);
							}
							
							i++;
						}
						
						i = listLive.size()-1;
						
						while(i>=0){
							int liveId = FootBallDAO.checkMatchLive(match_id, listLive.get(i).time);
							if(liveId==0)
							FootBallDAO.saveMatchLive(listLive.get(i));
							else 
								FootBallDAO.updateMatchLive(listLive.get(i),liveId);
							i--;
						}
					
					}
				
					
				}
			}
			
	public static void crawlerCup(String url,String code) throws Exception {
		HttpClientImpl client = new HttpClientImpl();
	
	    //http://bongda.wap.vn/ket-qua/tuong-thuat-tran-dau-Man.City-vs-West.Brom-MC-WBA-266995-0.html
		//"http://bongda.wap.vn/bang-xep-hang-ngoai-hang-anh-anh.html"
		HttpResponse res = client.fetch(url);
		//HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);
		
	
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		//CrawlerUtil.analysis(reader.getDocument());

		String xpath_country = "//form[@name=\"frmControl\"]/div/b";
		String country  = (String) reader.read(xpath_country , XPathConstants.STRING);
		
		//String xpath_name = "//form[@name=\"formKetQua\"]/div/h2";
		//String name  = (String) reader.read(xpath_name , XPathConstants.STRING);
		
		//logger.info(country);
		//logger.info(name);
		String xpath_node_content = "//select[@name=\"code\"]/option";
		NodeList nodes = (NodeList) reader.read(xpath_node_content,
				XPathConstants.NODESET);
		if (nodes != null) {
			int node_one_many = nodes.getLength();
			
			int i = 2;

			while (i <= node_one_many) {
				
				
				String value  = (String) reader.read(xpath_node_content + "[" + i + "]"
						+ "/@value", XPathConstants.STRING);
				
				String tengiai  = (String) reader.read(xpath_node_content + "[" + i + "]"
						+ "/text()", XPathConstants.STRING);
				//logger.info(value);
				
				
				if(url.indexOf(value)>0){
					//name = tengiai;	
				//	logger.info(tengiai);
					FBCup cup = new FBCup();
					cup.code = code;
					cup.name = tengiai;
					cup.country = country;
					FootBallDAO.saveCup(cup);
				}
					
						i++;
					
				}
				
		}
}
	public static List<EventFootball> groupEvent(List<EventFootball> events2){
		HashMap<String,EventFootball> mapEvent = new HashMap<String, EventFootball>();
		for (EventFootball eventFootball : events2) {
			if(mapEvent.containsKey(eventFootball.minute))
			{
				EventFootballSub eventFootballSub = new EventFootballSub();
				eventFootballSub.footballer = eventFootball.footballer;
				eventFootballSub.icon = eventFootball.icon;
				mapEvent.get(eventFootball.minute).subEvents.add(eventFootballSub);
				
				JSONArray jsonArray = new JSONArray();
				for (EventFootballSub eventFootballSub2 : mapEvent.get(eventFootball.minute).subEvents) {
					 JSONObject formDetailsJson = new JSONObject();
				     formDetailsJson.put("footballer", eventFootballSub2.footballer);
				     formDetailsJson.put("icon", eventFootballSub2.icon);
				     jsonArray.add(formDetailsJson);
				}
			//	logger.info(eventFootball.minute);
				mapEvent.get(eventFootball.minute).event = jsonArray.toString();
				mapEvent.put(eventFootball.minute, mapEvent.get(eventFootball.minute));
				
			}else{
				eventFootball.subEvents = new ArrayList<EventFootballSub>();
				EventFootballSub eventFootballSub = new EventFootballSub();
				eventFootballSub.footballer = eventFootball.footballer;
				eventFootballSub.icon = eventFootball.icon;
				eventFootball.subEvents.add(eventFootballSub);
				
				JSONArray jsonArray = new JSONArray();
				for (EventFootballSub eventFootballSub2 : eventFootball.subEvents) {
					 JSONObject formDetailsJson = new JSONObject();
				     formDetailsJson.put("footballer", eventFootballSub2.footballer);
				     formDetailsJson.put("icon", eventFootballSub2.icon);
				     jsonArray.add(formDetailsJson);
				}
				
				eventFootball.event = jsonArray.toString();
				mapEvent.put(eventFootball.minute, eventFootball);
			}
		}
		
		 Iterator<EventFootball> iterator = mapEvent.values().iterator();
		  
		 events2 = new ArrayList<EventFootball>();

		   while(iterator.hasNext()){
			   events2.add(iterator.next());
		   }
		   
		   Collections.sort(events2, new EventFootballComparator());
		   
		return events2;
		
	}
	
	public static void main1(String args[]){
		try {
			//TrucTiepTranDauCrawler.crawlerTuongThuatTranDau("http://bongda.wap.vn/ket-qua/tt.jsp?firstTeamName=West.Brom&secondTeamName=Man.City&firstSMS=WBA&secondSMS=MC&id=325426&showAds=0",214644,25,38);
			LiveScoreCrawler.crawlerUrlTuongThuatAjax("http://bongda.wap.vn/truc-tiep-ba-lan-vs-ireland-6985-352365.html", 72973,248,31175, 18673);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void main(String args[]){
		while(true){
		List<FBMatch> listMatch = FootBallDAO.getListMatchTT();
			for (FBMatch fbMatch : listMatch) {
				try {
					if(!StringUtil.isEmpty(fbMatch.url_code)){
				//	logger.info("Crawler TrucTiep URL--->"+Base64Coder.decodeString(fbMatch.url_code));
				//	logger.info("Crawler TrucTiep Trận đấu --->"+fbMatch.id);
					LiveScoreCrawler.crawlerUrlTuongThuatAjax(Base64Coder.decodeString(fbMatch.url_code), fbMatch.id,fbMatch.cup_id,fbMatch.club_id_1, fbMatch.club_id_2);
					}
					} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				
				try {
					if(!StringUtil.isEmpty(fbMatch.url_tyle)){
				//		logger.info("Crawler Tyle URL--->"+Base64Coder.decodeString(fbMatch.url_tyle));
				//		logger.info("Crawler TrucTiep Trận đấu --->"+fbMatch.id);
					
						TyleCrawler.crawlerTyle(Base64Coder.decodeString(fbMatch.url_tyle), fbMatch.id);}
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
			}
	}
		
	
}
