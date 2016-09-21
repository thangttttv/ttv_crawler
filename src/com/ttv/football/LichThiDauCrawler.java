package com.ttv.football;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.crawler.fetcher.HttpClientUtil;
import hdc.util.html.parser.XPathReader;
import hdc.util.text.StringUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathConstants;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.NodeList;

import com.az24.crawler.model.FBClub;
import com.az24.crawler.model.FBMatch;
import com.az24.dao.FootBallDAO;
import com.az24.test.Base64Coder;
import com.az24.test.HttpClientFactory;



public class LichThiDauCrawler {
	
	public static String listMatchID = "";
	public static void crawlerLicThiDauBongDaWapNHA()
			throws Exception {
		HttpClientImpl client = new HttpClientImpl();
		int sv = 1;
		while(sv<=38){
			System.out.println("Vong Thu:"+sv);
			HttpResponse res = client.fetch("http://bongda.wap.vn/ket-qua-ngoai-hang-anh-anh-vong-"+sv+".html");
			//HttpClientUtil.printResponseHeaders(res);
			String html = HttpClientUtil.getResponseBody(res);
			XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
			//CrawlerUtil.analysis(reader.getDocument());

			String xpath_node_content = "//table/TBODY/tr";

			NodeList nodes = (NodeList) reader.read("//table/TBODY/tr",
					XPathConstants.NODESET);
			if (nodes != null) {
				int node_one_many = nodes.getLength();
				int i = 2;
			
				String match_time = "";
				while (i <= node_one_many) {
					String text_colspan = (String) reader.read(xpath_node_content + "[" + i + "]"
							+ "/td[1]/@colspan", XPathConstants.STRING);
				//	System.out.println("text_colspan:"+text_colspan.trim());
					
					String txt_vong = (String) reader.read(xpath_node_content + "[" + i + "]"
							+ "/td[1]/strong/text()", XPathConstants.STRING);
					
					if("Vòng: ".equalsIgnoreCase(txt_vong)) break;
					
					if("6".equalsIgnoreCase(text_colspan))
					{
						String time = (String) reader.read(xpath_node_content + "[" + i + "]"
								+ "/td[1]", XPathConstants.STRING);
						System.out.println("Vong Dau: "+sv+" - Ngay Dau:"+time.trim());
						String[] arrTime = time.split(",");
						//String thu = arrTime[0].replace("Thứ", "").trim();
						String ngay_thang[] = arrTime[1].replace("ngày", "").trim().split("/");
						String year = "2014";
						if(sv>19) year = "2015";
						match_time =  year+"-"+ngay_thang[1]+"-"+ngay_thang[0];
						
						System.out.println("Vong Dau: "+sv+" - Ngay Dau:"+match_time.trim());
					}else{
						String time = (String) reader.read(xpath_node_content + "[" + i + "]"
								+ "/td[1]", XPathConstants.STRING);
						time = time.trim();
						if(!"".equalsIgnoreCase(time))
						{
							System.out.println("Gio Dau:"+time.trim());
							String status = (String) reader.read(xpath_node_content + "[" + i + "]"
									+ "/td[2]", XPathConstants.STRING);
							status = status.trim();
							
							String club_name = (String) reader.read(xpath_node_content + "[" + i + "]"
									+ "/td[3]/table/TBODY/tr/td/span[1]/a/text()", XPathConstants.STRING);
							club_name = club_name.trim();
							club_name = club_name.trim().replaceAll("\\s+", " ");
							
							String club1_url = (String) reader.read(xpath_node_content + "[" + i + "]"
									+ "/td[3]/table/TBODY/tr/td/span[1]/a/@href", XPathConstants.STRING);
							club1_url = club1_url.trim();
							
							
							String ketqua = (String) reader.read(xpath_node_content + "[" + i + "]"
									+ "/td[4]/table/TBODY/tr/td/a/b/text()", XPathConstants.STRING);
							ketqua = ketqua.trim();
							
							String club2_name = (String) reader.read(xpath_node_content + "[" + i + "]"
									+ "/td[5]/table/TBODY/tr/td/span[1]/a/text()", XPathConstants.STRING);
							club2_name = club2_name.trim();
							club2_name = club2_name.trim().replaceAll("\\s+", " ");
							
							String club2_url = (String) reader.read(xpath_node_content + "[" + i + "]"
									+ "/td[5]/table/TBODY/tr/td/span[1]/a/@href", XPathConstants.STRING);
							club2_url = club2_url.trim();
							
							String url_code =  (String) reader.read(xpath_node_content + "[" + i + "]"
									+ "/td[4]/table/TBODY/tr/td/a/@href", XPathConstants.STRING);
							
							String kqHiep1 = (String) reader.read(xpath_node_content + "[" + i + "]"
									+ "/td[6]/b/text()", XPathConstants.STRING);
							kqHiep1 = kqHiep1.trim();
							
							System.out.println("Time:"+time.trim());
							System.out.println("Trang Thai:"+status.trim());
							System.out.println("Chua nha:"+club_name.trim());
							System.out.println("Kết quả:"+ketqua.trim());
							System.out.println("Doi khách:"+club2_name.trim());
							System.out.println("KQ hiệp 1:"+kqHiep1.trim());
							System.out.println("----------------");
							
							FBMatch match = new FBMatch();
							FBClub club1 = FootBallDAO.getClub(club_name);
							if(club1==null) LiveScoreCrawler.crawlerClup_2(club1_url, url_code);
							
							FBClub club2 = FootBallDAO.getClub(club2_name);
							if(club2==null) LiveScoreCrawler.crawlerClup_2(club2_url, url_code);
							
							String url_encode =   "http://bongda.wap.vn/"+url_code;
							match.url_code =Base64Coder.encodeString(url_encode);
							
							match.club_id_1 = club1.id;
							match.club_id_2 = club2.id;
							
							match.club_code_1 = club1.code;
							match.club_code_2 = club2.code;
							
							match.status = status;
							match.result = ketqua.replaceAll("[^\\d-]","");
							match.result_1 = kqHiep1.replaceAll("[^\\d-]","");
						 
							match.round = sv+"";
							match.cup_id = 1;
							match.match_time = match_time+" "+time+":00";
							//if(StringUtil.isEmpty(match.match_minute)) match.match_minute = gio;
							
							FootBallDAO.saveMatch(match);
						}
					}
					i++;
				}
			}
			
			sv++;
		}
	}	
	
	
	public static void crawlerLicThiDauBongDaWap(String url_short,int tong_sv,int cup_id,int year1,int year2, int svchuyennam,int isQuet1Trang,String season)
			throws Exception {
		HttpClientImpl client = new HttpClientImpl();
		int sv = 1;
		while(sv<=tong_sv){
			System.out.println("Vong Thu:"+sv);
			String urlquet =url_short;
			if(isQuet1Trang==0)
			 urlquet =url_short+sv+".html";
			urlquet =url_short+".html";
			urlquet = "http://bongda.wap.vn/ket-qua-euro-2016-505.html";
			HttpResponse res = client.fetch(urlquet);
			//HttpClientUtil.printResponseHeaders(res);
			String html = HttpClientUtil.getResponseBody(res);
			XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
			//CrawlerUtil.analysis(reader.getDocument());

			String xpath_node_content = "//table/TBODY/tr";

			NodeList nodes = (NodeList) reader.read("//table/TBODY/tr",
					XPathConstants.NODESET);
			if (nodes != null) {
				int node_one_many = nodes.getLength();
				int i = 2;
				//int i = 18;
				String match_time = "";
				while (i <= node_one_many) {
					String text_colspan = (String) reader.read(xpath_node_content + "[" + i + "]"
							+ "/td[1]/@colspan", XPathConstants.STRING);
				//	System.out.println("text_colspan:"+text_colspan.trim());
					
					String txt_vong = (String) reader.read(xpath_node_content + "[" + i + "]"
							+ "/td[1]/strong/text()", XPathConstants.STRING);
					
					if("Vòng: ".equalsIgnoreCase(txt_vong)) break;
					
					if("6".equalsIgnoreCase(text_colspan))
					{
						String time = (String) reader.read(xpath_node_content + "[" + i + "]"
								+ "/td[1]", XPathConstants.STRING);
						System.out.println("Vong Dau: "+sv+" - Ngay Dau:"+time.trim());
						String[] arrTime = time.split(",");
						//String thu = arrTime[0].replace("Thứ", "").trim();
						String ngay_thang[] = arrTime[1].replace("ngày", "").trim().split("/");
						String year = String.valueOf(year1);
						if(sv>=svchuyennam&&svchuyennam>0) year = String.valueOf(year2);
						
						match_time =  year+"-"+ngay_thang[1]+"-"+ngay_thang[0];
						
						System.out.println("Vong Dau: "+sv+" - Ngay Dau:"+match_time.trim());
					}else{
						String time = (String) reader.read(xpath_node_content + "[" + i + "]"
								+ "/td[1]", XPathConstants.STRING);
						time = time.trim();
						if(!"".equalsIgnoreCase(time))
						{
							System.out.println("Gio Dau:"+time.trim());
							String status = (String) reader.read(xpath_node_content + "[" + i + "]"
									+ "/td[2]", XPathConstants.STRING);
							status = status.trim();
							
							String club_name = (String) reader.read(xpath_node_content + "[" + i + "]"
									+ "/td[3]/table/TBODY/tr/td/span[1]/a/text()", XPathConstants.STRING);
							club_name = club_name.trim();
							club_name = club_name.trim().replaceAll("\\s+", " ");
							
							String club1_url = (String) reader.read(xpath_node_content + "[" + i + "]"
									+ "/td[3]/table/TBODY/tr/td/span[1]/a/@href", XPathConstants.STRING);
							club1_url = club1_url.trim();
							
							
							String ketqua = (String) reader.read(xpath_node_content + "[" + i + "]"
									+ "/td[4]/table/TBODY/tr/td/a/b/text()", XPathConstants.STRING);
							ketqua = ketqua.trim();
							
							String club2_name = (String) reader.read(xpath_node_content + "[" + i + "]"
									+ "/td[5]/table/TBODY/tr/td/span[1]/a/text()", XPathConstants.STRING);
							club2_name = club2_name.trim();
							club2_name = club2_name.trim().replaceAll("\\s+", " ");
							
							String club2_url = (String) reader.read(xpath_node_content + "[" + i + "]"
									+ "/td[5]/table/TBODY/tr/td/span[1]/a/@href", XPathConstants.STRING);
							club2_url = club2_url.trim();
							
							String url_code =  (String) reader.read(xpath_node_content + "[" + i + "]"
									+ "/td[4]/table/TBODY/tr/td/a/@href", XPathConstants.STRING);
							
							String kqHiep1 = (String) reader.read(xpath_node_content + "[" + i + "]"
									+ "/td[6]/b/text()", XPathConstants.STRING);
							kqHiep1 = kqHiep1.trim();
							
							System.out.println("Time:"+time.trim());
							System.out.println("Trang Thai:"+status.trim());
							System.out.println("Chua nha:"+club_name.trim());
							System.out.println("Kết quả:"+ketqua.trim());
							System.out.println("Doi khách:"+club2_name.trim());
							System.out.println("KQ hiệp 1:"+kqHiep1.trim());
							System.out.println("----------------");
							
							if(StringUtil.isEmpty(club_name)) continue;
							
							FBMatch match = new FBMatch();
							FBClub club1 = FootBallDAO.getClub(club_name);
							if(club1==null) LiveScoreCrawler.crawlerClup_1(club1_url, url_code);
							else{
								if(StringUtil.isEmpty(club1.bdwap_id )){
								Pattern r =   Pattern.compile("-(\\d+)\\.html");   
							    Matcher  m = r.matcher(club1_url);
							    String bdwap_id = null;
					    		if(m.find())
							      { bdwap_id = m.group(1);
							      }
					    		club1.bdwap_id =bdwap_id;
					    		FootBallDAO.updateBDWapIDClub(club1.id,club1.bdwap_id);
								}
							}
							
							
							FBClub club2 = FootBallDAO.getClub(club2_name);
							if(club2==null) LiveScoreCrawler.crawlerClup_2(club2_url, url_code);
							else{
								if(StringUtil.isEmpty(club2.bdwap_id )){
								Pattern r =   Pattern.compile("-(\\d+)\\.html");   
							    Matcher  m = r.matcher(club2_url);
							    String bdwap_id = null;
					    		if(m.find())
							      { bdwap_id = m.group(1);
							      }
					    		club2.bdwap_id =bdwap_id;
					    		FootBallDAO.updateBDWapIDClub(club2.id,club2.bdwap_id);
								}
							}
							
							club1 = FootBallDAO.getClubJoinCoach(club_name);
							club2 = FootBallDAO.getClubJoinCoach(club2_name);
							
							match.club_id_1 = club1.id;
							match.club_id_2 = club2.id;
							
							match.club_code_1 = club1.code;
							match.club_code_2 = club2.code;
							
							match.club_name_1  = club_name;
							match.club_name_2  = club2_name;
							
							match.coach_name_1  = club1.coachname;
							match.coach_name_2  = club2.coachname;
							
							if(club1.logo!=null)
							match.club_logo_1 = club1.ymd+club1.logo;
							if(club2.logo!=null)
							match.club_logo_2 = club2.ymd+club2.logo;
								
							match.status = status;
							match.result = ketqua.replaceAll("[^\\d-]","");
							match.result_1 = kqHiep1.replaceAll("[^\\d-]","");
							match.round = sv+"";
							match.cup_id = cup_id;
							match.match_time = match_time+" "+time+":00";
							match.match_minute = status;
							if(StringUtil.isEmpty(match.match_minute)) match.match_minute = time;
							
							if(url_code.indexOf("http")<0)
								url_code =   "http://bongda.wap.vn/"+url_code;
							
							Pattern r =   Pattern.compile("-(\\d+)\\.html");   
						    Matcher m = r.matcher(url_code);
						    String bdwap_id = null;
				    		if(m.find()) { bdwap_id = m.group(1);}
							match.bdwap_id =bdwap_id;
							
							match.url_code =Base64Coder.encodeString(url_code);
							if(year1==year2) season =String.valueOf(year1); else
								season =String.valueOf(year1)+"-"+String.valueOf(year2); 
								
							match.season=season;
						
							
							int match_id = FootBallDAO.checkMatch(match.club_id_1, match.club_id_2, match_time);
							
							if(match_id==0)
								 match_id = FootBallDAO.checkMatchByUrlCode(match.url_code);
							
							if(match_id==0)
								 match_id = FootBallDAO.checkMatchByBDWapID(bdwap_id);
							
							System.out.println("match_id="+match_id);
							match.id = match_id;
							if(match_id==0)
								FootBallDAO.saveMatch(match);
							else{
								FootBallDAO.updateMatch(match);
								LiveScoreCrawler.crawlerUrlTuongThuatAjax(url_code, match_id,cup_id, match.club_id_1,match.club_id_2);

							}
						}
					}
					i++;
				}
			}
			sv++;
		}
	}
	
	
	public static void crawlerLicThiDauBongDaWapPost(String url_short,int tong_sv,int cup_id,int year1,int year2, int svchuyennam,int isQuet1Trang,String season)
			throws Exception {
		 DefaultHttpClient client = HttpClientFactory.getInstance() ;
		int sv = 7;
		while(sv<=tong_sv){
			System.out.println("Vong Thu:"+sv);
			
	        client.getParams().setParameter("http.protocol.expect-continue",false) ;
	    	HttpPost post = new HttpPost(url_short) ;
	        List<NameValuePair> list = new ArrayList<NameValuePair>() ;
	        list.add(new BasicNameValuePair("slRound", String.valueOf(sv))) ;
	        post.setEntity(new UrlEncodedFormEntity(list)) ;
	        HttpResponse res = client.execute(post) ;
	        String html = HttpClientUtil.getResponseBody(res) ;
	        System.out.println(html);
		        
		
			XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
			CrawlerUtil.analysis(reader.getDocument());

			String xpath_node_content = "//table/TBODY/tr";

			NodeList nodes = (NodeList) reader.read("//table/TBODY/tr",
					XPathConstants.NODESET);
			if (nodes != null) {
				int node_one_many = nodes.getLength();
				int i = 2;
				//int i = 18;
				String match_time = "";
				while (i <= node_one_many) {
					String text_colspan = (String) reader.read(xpath_node_content + "[" + i + "]"
							+ "/td[1]/@colspan", XPathConstants.STRING);
				//	System.out.println("text_colspan:"+text_colspan.trim());
					
					String txt_vong = (String) reader.read(xpath_node_content + "[" + i + "]"
							+ "/td[1]/strong/text()", XPathConstants.STRING);
					
					if("Vòng: ".equalsIgnoreCase(txt_vong)) break;
					
					if("6".equalsIgnoreCase(text_colspan))
					{
						String time = (String) reader.read(xpath_node_content + "[" + i + "]"
								+ "/td[1]", XPathConstants.STRING);
						System.out.println("Vong Dau: "+sv+" - Ngay Dau:"+time.trim());
						String[] arrTime = time.split(",");
						//String thu = arrTime[0].replace("Thứ", "").trim();
						String ngay_thang[] = arrTime[1].replace("ngày", "").trim().split("/");
						String year = String.valueOf(year1);
						if(sv>=svchuyennam&&svchuyennam>0) year = String.valueOf(year2);
						
						match_time =  year+"-"+ngay_thang[1]+"-"+ngay_thang[0];
						
						System.out.println("Vong Dau: "+sv+" - Ngay Dau:"+match_time.trim());
					}else{
						String time = (String) reader.read(xpath_node_content + "[" + i + "]"
								+ "/td[1]", XPathConstants.STRING);
						time = time.trim();
						if(!"".equalsIgnoreCase(time))
						{
							System.out.println("Gio Dau:"+time.trim());
							String status = (String) reader.read(xpath_node_content + "[" + i + "]"
									+ "/td[2]", XPathConstants.STRING);
							status = status.trim();
							
							String club_name = (String) reader.read(xpath_node_content + "[" + i + "]"
									+ "/td[3]/table/TBODY/tr/td/span[1]/a/text()", XPathConstants.STRING);
							club_name = club_name.trim();
							club_name = club_name.trim().replaceAll("\\s+", " ");
							
							String club1_url = (String) reader.read(xpath_node_content + "[" + i + "]"
									+ "/td[3]/table/TBODY/tr/td/span[1]/a/@href", XPathConstants.STRING);
							club1_url = club1_url.trim();
							
							
							String ketqua = (String) reader.read(xpath_node_content + "[" + i + "]"
									+ "/td[4]/table/TBODY/tr/td/a/b/text()", XPathConstants.STRING);
							ketqua = ketqua.trim();
							
							String club2_name = (String) reader.read(xpath_node_content + "[" + i + "]"
									+ "/td[5]/table/TBODY/tr/td/span[1]/a/text()", XPathConstants.STRING);
							club2_name = club2_name.trim();
							club2_name = club2_name.trim().replaceAll("\\s+", " ");
							
							String club2_url = (String) reader.read(xpath_node_content + "[" + i + "]"
									+ "/td[5]/table/TBODY/tr/td/span[1]/a/@href", XPathConstants.STRING);
							club2_url = club2_url.trim();
							
							String url_code =  (String) reader.read(xpath_node_content + "[" + i + "]"
									+ "/td[4]/table/TBODY/tr/td/a/@href", XPathConstants.STRING);
							
							String kqHiep1 = (String) reader.read(xpath_node_content + "[" + i + "]"
									+ "/td[6]/b/text()", XPathConstants.STRING);
							kqHiep1 = kqHiep1.trim();
							
							System.out.println("Time:"+time.trim());
							System.out.println("Trang Thai:"+status.trim());
							System.out.println("Chua nha:"+club_name.trim());
							System.out.println("Kết quả:"+ketqua.trim());
							System.out.println("Doi khách:"+club2_name.trim());
							System.out.println("KQ hiệp 1:"+kqHiep1.trim());
							System.out.println("----------------");
							
							if(StringUtil.isEmpty(club_name)) continue;
							
							FBMatch match = new FBMatch();
							FBClub club1 = FootBallDAO.getClub(club_name);
							if(club1==null) LiveScoreCrawler.crawlerClup_1(club1_url, url_code);
							else{
								if(StringUtil.isEmpty(club1.bdwap_id )){
								Pattern r =   Pattern.compile("-(\\d+)\\.html");   
							    Matcher  m = r.matcher(club1_url);
							    String bdwap_id = null;
					    		if(m.find())
							      { bdwap_id = m.group(1);
							      }
					    		club1.bdwap_id =bdwap_id;
					    		FootBallDAO.updateBDWapIDClub(club1.id,club1.bdwap_id);
								}
							}
							
							
							FBClub club2 = FootBallDAO.getClub(club2_name);
							if(club2==null) LiveScoreCrawler.crawlerClup_2(club2_url, url_code);
							else{
								if(StringUtil.isEmpty(club2.bdwap_id )){
								Pattern r =   Pattern.compile("-(\\d+)\\.html");   
							    Matcher  m = r.matcher(club2_url);
							    String bdwap_id = null;
					    		if(m.find())
							      { bdwap_id = m.group(1);
							      }
					    		club2.bdwap_id =bdwap_id;
					    		FootBallDAO.updateBDWapIDClub(club2.id,club2.bdwap_id);
								}
							}
							
							club1 = FootBallDAO.getClubJoinCoach(club_name);
							club2 = FootBallDAO.getClubJoinCoach(club2_name);
							
							match.club_id_1 = club1.id;
							match.club_id_2 = club2.id;
							
							match.club_code_1 = club1.code;
							match.club_code_2 = club2.code;
							
							match.club_name_1  = club_name;
							match.club_name_2  = club2_name;
							
							match.coach_name_1  = club1.coachname;
							match.coach_name_2  = club2.coachname;
							
							if(club1.logo!=null)
							match.club_logo_1 = club1.ymd+club1.logo;
							if(club2.logo!=null)
							match.club_logo_2 = club2.ymd+club2.logo;
								
							match.status = status;
							match.result = ketqua.replaceAll("[^\\d-]","");
							match.result_1 = kqHiep1.replaceAll("[^\\d-]","");
							match.round = sv+"";
							match.cup_id = cup_id;
							match.match_time = match_time+" "+time+":00";
							match.match_minute = status;
							if(StringUtil.isEmpty(match.match_minute)) match.match_minute = time;
							
							if(url_code.indexOf("http")<0)
								url_code =   "http://bongda.wap.vn/"+url_code;
							
							Pattern r =   Pattern.compile("-(\\d+)\\.html");   
						    Matcher m = r.matcher(url_code);
						    String bdwap_id = null;
				    		if(m.find()) { bdwap_id = m.group(1);}
							match.bdwap_id =bdwap_id;
							
							match.url_code =Base64Coder.encodeString(url_code);
							if(year1==year2) season =String.valueOf(year1); else
								season =String.valueOf(year1)+"-"+String.valueOf(year2); 
								
							match.season=season;
						
							
							int match_id = FootBallDAO.checkMatch(match.club_id_1, match.club_id_2, match_time);
							
							if(match_id==0)
								 match_id = FootBallDAO.checkMatchByUrlCode(match.url_code);
							
							match.id = match_id;
							if(match_id==0)
								FootBallDAO.saveMatch(match);
							else{
								FootBallDAO.updateMatch(match);
								LiveScoreCrawler.crawlerUrlTuongThuatAjax(url_code, match_id,cup_id, match.club_id_1,match.club_id_2);

							}
						}
					}
					i++;
				}
			}
			
			sv++;
		}
	}	
	
	
	public static String crawlerLicThiDauBongDaWapMoi(String url_lichtd,String vong,int cup_id,int year1,String strdate_before,String season)
			throws Exception {
			
		 	System.out.println("Vong Thu:"+vong);
		
			HttpClientImpl client = new HttpClientImpl();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			if(url_lichtd.indexOf("http")<0) url_lichtd ="http://bongda.wap.vn/"+url_lichtd;
			HttpResponse res = client.fetch(url_lichtd);
			//HttpClientUtil.printResponseHeaders(res);
			String html = HttpClientUtil.getResponseBody(res);
		
			XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
			//CrawlerUtil.analysis(reader.getDocument());
			String xpath_node_content = "//table/TBODY/tr";
			NodeList nodes = (NodeList) reader.read("//table/TBODY/tr",
					XPathConstants.NODESET);
			
			String match_time = "";
			
			if (nodes != null) {
				int node_one_many = nodes.getLength();
				int i = 2;
				
				Date dateBefore = null;
				if(!StringUtil.isEmpty(strdate_before))
				 dateBefore = formatter.parse(strdate_before);
				
				Date dateVong = null;
				while (i <= node_one_many) {
					String text_colspan = (String) reader.read(xpath_node_content + "[" + i + "]"
							+ "/td[1]/@colspan", XPathConstants.STRING);
					
					String txt_vong = (String) reader.read(xpath_node_content + "[" + i + "]"
							+ "/td[1]/strong/text()", XPathConstants.STRING);
					
					if("Vòng: ".equalsIgnoreCase(txt_vong)) break;
					
					if("6".equalsIgnoreCase(text_colspan))
					{
						String time = (String) reader.read(xpath_node_content + "[" + i + "]"
								+ "/td[1]", XPathConstants.STRING);
						System.out.println("Vong Dau: "+vong+" - Ngay Dau:"+time.trim());
						String[] arrTime = time.split(",");
					
						String ngay_thang[] = arrTime[1].replace("ngày", "").trim().split("/");
						int year = year1;
						
						//if(sv>=svchuyennam&&svchuyennam>0) year = String.valueOf(year2);
						
						match_time =  year+"-"+ngay_thang[1]+"-"+ngay_thang[0];
						dateVong = formatter.parse(match_time);
						
						if(dateBefore!=null&&dateBefore.after(dateVong)){ year = year1+1;
							match_time =  year+"-"+ngay_thang[1]+"-"+ngay_thang[0];
						}
						
						System.out.println("Vong Dau: "+vong+" - Ngay Dau:"+match_time.trim());
					}else{
						String time = (String) reader.read(xpath_node_content + "[" + i + "]"
								+ "/td[1]", XPathConstants.STRING);
						time = time.trim();
						if(!"".equalsIgnoreCase(time))
						{
							System.out.println("Gio Dau:"+time.trim());
							String status = (String) reader.read(xpath_node_content + "[" + i + "]"
									+ "/td[2]", XPathConstants.STRING);
							status = status.trim();
							
							String club_name = (String) reader.read(xpath_node_content + "[" + i + "]"
									+ "/td[3]/table/TBODY/tr/td/span[1]/a/text()", XPathConstants.STRING);
							club_name = club_name.trim();
							club_name = club_name.trim().replaceAll("\\s+", " ");
							
							String club1_url = (String) reader.read(xpath_node_content + "[" + i + "]"
									+ "/td[3]/table/TBODY/tr/td/span[1]/a/@href", XPathConstants.STRING);
							club1_url = club1_url.trim();
							
							
							String ketqua = (String) reader.read(xpath_node_content + "[" + i + "]"
									+ "/td[4]/table/TBODY/tr/td/a/b/text()", XPathConstants.STRING);
							ketqua = ketqua.trim();
							
							String club2_name = (String) reader.read(xpath_node_content + "[" + i + "]"
									+ "/td[5]/table/TBODY/tr/td/span[1]/a/text()", XPathConstants.STRING);
							club2_name = club2_name.trim();
							club2_name = club2_name.trim().replaceAll("\\s+", " ");
							
							String club2_url = (String) reader.read(xpath_node_content + "[" + i + "]"
									+ "/td[5]/table/TBODY/tr/td/span[1]/a/@href", XPathConstants.STRING);
							club2_url = club2_url.trim();
							
							String url_code =  (String) reader.read(xpath_node_content + "[" + i + "]"
									+ "/td[4]/table/TBODY/tr/td/a/@href", XPathConstants.STRING);
							
							String kqHiep1 = (String) reader.read(xpath_node_content + "[" + i + "]"
									+ "/td[6]/b/text()", XPathConstants.STRING);
							kqHiep1 = kqHiep1.trim();
							
							System.out.println("Time:"+time.trim());
							System.out.println("Trang Thai:"+status.trim());
							System.out.println("Chua nha:"+club_name.trim());
							System.out.println("Kết quả:"+ketqua.trim());
							System.out.println("Doi khách:"+club2_name.trim());
							System.out.println("KQ hiệp 1:"+kqHiep1.trim());
							System.out.println("----------------");
							
							if(StringUtil.isEmpty(club_name)) continue;
							
							FBMatch match = new FBMatch();
							FBClub club1 = FootBallDAO.getClub(club_name);
							if(club1==null) LiveScoreCrawler.crawlerClup_1(club1_url, url_code);
							else{
								if(StringUtil.isEmpty(club1.bdwap_id )){
								Pattern r =   Pattern.compile("-(\\d+)\\.html");   
							    Matcher  m = r.matcher(club1_url);
							    String bdwap_id = null;
					    		if(m.find())
							      { bdwap_id = m.group(1);
							      }
					    		club1.bdwap_id =bdwap_id;
					    		FootBallDAO.updateBDWapIDClub(club1.id,club1.bdwap_id);
								}
							}
							
							
							FBClub club2 = FootBallDAO.getClub(club2_name);
							if(club2==null) LiveScoreCrawler.crawlerClup_2(club2_url, url_code);
							else{
								if(StringUtil.isEmpty(club2.bdwap_id )){
								Pattern r =   Pattern.compile("-(\\d+)\\.html");   
							    Matcher  m = r.matcher(club2_url);
							    String bdwap_id = null;
					    		if(m.find())
							      { bdwap_id = m.group(1);
							      }
					    		club2.bdwap_id =bdwap_id;
					    		FootBallDAO.updateBDWapIDClub(club2.id,club2.bdwap_id);
								}
							}
							
							club1 = FootBallDAO.getClubJoinCoach(club_name);
							club2 = FootBallDAO.getClubJoinCoach(club2_name);
							
							match.club_id_1 = club1.id;
							match.club_id_2 = club2.id;
							
							match.club_code_1 = club1.code;
							match.club_code_2 = club2.code;
							
							match.club_name_1  = club_name;
							match.club_name_2  = club2_name;
							
							match.coach_name_1  = club1.coachname;
							match.coach_name_2  = club2.coachname;
							
							if(club1.logo!=null)
							match.club_logo_1 = club1.ymd+club1.logo;
							if(club2.logo!=null)
							match.club_logo_2 = club2.ymd+club2.logo;
								
							match.status = status;
							match.result = ketqua.replaceAll("[^\\d-]","");
							match.result_1 = kqHiep1.replaceAll("[^\\d-]","");
							match.round = vong+"";
							match.cup_id = cup_id;
							match.match_time = match_time+" "+time+":00";
							match.match_minute = status;
							if(StringUtil.isEmpty(match.match_minute)) match.match_minute = time;
							
							Pattern r =   Pattern.compile("-(\\d+)\\.html");   
						    Matcher m = r.matcher(url_code);
						    String bdwap_id = null;
				    		if(m.find()) { bdwap_id = m.group(1);}
							match.bdwap_id =bdwap_id;
							
							if(url_code.indexOf("http")<0)
								url_code =   "http://bongda.wap.vn/"+url_code;
							
							
							match.url_code =Base64Coder.encodeString(url_code);
						
							match.season=String.valueOf(year1);
						
							
							int match_id = FootBallDAO.checkMatch(match.club_id_1, match.club_id_2, match_time);
							
							if(match_id==0)
								 match_id = FootBallDAO.checkMatchByUrlCode(match.url_code);
							
							if(match_id==0)
								 match_id = FootBallDAO.checkMatchByBDWapID(bdwap_id);
							
							match.id = match_id;
							if(match_id==0)
								match_id=FootBallDAO.saveMatch(match);
							else{
								FootBallDAO.updateMatch(match);
								LiveScoreCrawler.crawlerUrlTuongThuatAjax(url_code, match_id,cup_id, match.club_id_1,match.club_id_2);
							}
							listMatchID += match_id+",";
						}
					}
					i++;
				}
			}
			
			return match_time;
		
	}
		
	public static void crawlerListLichThiDau(int year1,String cup_name) throws Exception {
		HttpClientImpl client = new HttpClientImpl();
	 	HttpResponse res = client.fetch("http://bongda.wap.vn/");
		//HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);
	
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		//CrawlerUtil.analysis(reader.getDocument());
		String xpath_node_content = "//ul[@class='countries']/li";

		// Lay List Quoc Gia
		NodeList nodes = (NodeList) reader.read(xpath_node_content,XPathConstants.NODESET);
      
		if (nodes != null) {
			int node_one_many = nodes.getLength();
			int i = 1;
			// Lay List Giai Dau Theo Quoc Gia
			while (i <= node_one_many) {
				NodeList nodes1 = (NodeList) reader.read(xpath_node_content+"["+i+"]/ul[1]/li",
						XPathConstants.NODESET);
				int j = 1;
				int node_one_many2 = nodes1.getLength();
				while (j <= node_one_many2) {
					String link = (String) reader.read(xpath_node_content+"["+i+"]/ul[1]/li" + "[" + j + "]"+ "/a[1]/@href", XPathConstants.STRING);
					System.out.println(link);
					String tetGiai = (String) reader.read(xpath_node_content+"["+i+"]/ul[1]/li" + "[" + j + "]"+ "/a[1]/text()", XPathConstants.STRING);
					System.out.println(tetGiai);
					tetGiai = tetGiai.trim().replaceAll("\\s+", " ");
					if(!cup_name.equalsIgnoreCase(tetGiai)) {j++;continue;};
					
					int cup_id = FootBallDAO.checkCupByName(tetGiai);
					if(cup_id==0){
						System.out.println("Khong tiem thay cup:"+tetGiai);
						j++;
						continue;
					}

					//get link danh sach theo vong dau
					res = client.fetch(link);
				//	HttpClientUtil.printResponseHeaders(res);
					html = HttpClientUtil.getResponseBody(res);
					XPathReader readerBXH = CrawlerUtil.createXPathReaderByData(html);
					String xpathVongDau = "/html/body/center/div/div[5]/div[5]/table/TBODY/tr";
					NodeList nodes2 = (NodeList) readerBXH.read("/html/body/center/div/div[5]/div[5]/table/TBODY/tr",
							XPathConstants.NODESET);
					int k = 1;
					int node_one_many3 = nodes2.getLength();
					listMatchID = "";
					String strdate_before_last ="";
					while (k <= node_one_many3) {
						String labelVong = (String) readerBXH.read(xpathVongDau+"["+k+"]/td[1]/strong/text()", XPathConstants.STRING);
						
						if(!StringUtil.isEmpty(labelVong)&&"Vòng:".equalsIgnoreCase(labelVong.trim())){
							 NodeList nodes3 = (NodeList) readerBXH.read(xpathVongDau+"["+k+"]/td[1]/a",XPathConstants.NODESET);
							 int q = 1;
							 int node_one_many4 = nodes3.getLength();
							 String strdate_before ="";
							 while (q <= node_one_many4) {
								String linkVongDau = (String) readerBXH.read(xpathVongDau+"["+k+"]/td[1]/a["+q+"]/@href", XPathConstants.STRING);
								String vongThu = (String) readerBXH.read(xpathVongDau+"["+k+"]/td[1]/a["+q+"]/text()", XPathConstants.STRING);
								System.out.println("---------->"+linkVongDau);
								 
								String tem_strdate_before = LichThiDauCrawler.crawlerLicThiDauBongDaWapMoi(linkVongDau,vongThu, cup_id,year1, strdate_before, "");
								if(!StringUtil.isEmpty(tem_strdate_before)) strdate_before = tem_strdate_before;
								strdate_before_last = strdate_before;
								q++;
							 }
							 System.out.println("---------->CUP"+tetGiai+cup_id);
							
						}
						k++;
					}
					try {// update season 
						int yearLast = Integer.parseInt(strdate_before_last.split("-")[0]);
						System.out.println("---------->yearLast"+yearLast);
						System.out.println("---------->listMatchID"+listMatchID);
						
						if(year1<yearLast&&!StringUtil.isEmpty(listMatchID)){
							String season = year1+"-"+yearLast;
							listMatchID = listMatchID.substring(0, listMatchID.length()-1);
							System.out.println("---------->listMatchID"+listMatchID);
							FootBallDAO.updateMatchSeson(listMatchID, season);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					j++;
				}
				i++;
			}
		}
	}
	
	
	public static void main(String args[]) throws Exception{
		//LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-copa-america-2015-cmx-vong-", 3,247);
		int giai_dau = 395;
		//if(args!=null) giai_dau =  Integer.parseInt(args[0]);
		String season = "2014-2015";
		
		CountryUnit.getCountryTxt();
		LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-euro-2016-505", 5,395,2016,2016,0,0,"2016");
		//LichThiDauCrawler.crawlerListLichThiDau(2016,"Euro 2016");
		
		switch(giai_dau){
			case 1: 
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-ngoai-hang-anh-anh-vong-", 38,1,2015,2016,20,0,season);
				break;
			case 2: 
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-vdqg-tay-ban-nha-tbn-vong-", 38,2,2015,2016,18,0,season);
				break;
			case 3: 
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-vdqg-duc-duc-vong-", 34,3,2015,2016,18,0,season);
				break;
			case 4: 
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-vdqg-italia-ita-vong-", 38,4,2015,2016,18,0,season);
				break;
			case 5: 
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-vdqg-phap-pha-vong-", 38,5,2015,2016,20,0,season);
				break;
			case 8: 
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-vdqg-viet-nam-vqg-vong-", 26,8,2015,2015,0,0,season);
				break;
			case 127: 
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-vdqg-scotland-sco-vong-", 33,127,2015,2016,22,0,season);
				break;
			case 176: 
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-vdqg-ha-lan-hlan-vong-", 34,176,2015,2016,18,0,season);
				break;
			case 109: 
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-vdqg-nga-485-vong-", 30,109,2015,2016,19,0,season);
				break;
			case 66: 
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-vdqg-tho-nhi-ky-506-vong-", 34,66,2015,2016,18,0,season);
				break;
			case 157: 
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-vdqg-thuy-sy-985-vong-", 36,157,2015,2016,19,0,season);
				break;
			case 183: 
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-vdqg-thuy-dien-565-vong-", 30,183,2015,2015,0,0,season);
				break;
			case 119: 
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-vdqg-dan-mach-515-vong-", 33,119,2015,2016,19,0,season);
				break;
			case 81: 
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-vdqg-nhat-ban-585-vong-", 17,81,2015,2015,0,0,season);
				break;
			case 144: 
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-vdqg-han-quoc-765-vong-", 33,144,2015,2015,0,0,season);
				break;
			case 255: 
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-vdqg-thai-lan-2025-vong-", 34,255,2015,2015,0,0,season);
				break;
			case 149: 
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-vdqg-an-do-5025-vong-", 26,149,2015,2015,0,0,season);
				break;
			case 162: 
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-vdqg-brazil-bra-vong-", 38,162,2015,2015,0,0,season);
				break;
			case 131: 
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-vdqg-argentina-5725-vong-", 30,131,2015,2015,0,0,season);
				break;
			case 78: 
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-vdqg-my-fifa-vong-", 34,78,2015,2015,0,0,season);
				break;
			case 7: 
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-cup-c2-chau-au-c2.html", 1,7,2015,2016,0,1,season);
				break;
			case 6:
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-cup-c1-chau-au-c1.html", 1,6,2015,2016,0,1,season);
				break;
			case 248:
				LichThiDauCrawler.crawlerLicThiDauBongDaWapPost("http://bongda.wap.vn/ket-qua-vong-loai-euro-2016-6985.html", 9,248,2014,2015,5,1,season);
				break;
			case 136:
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-vdqg-trung-quoc-2246-vong-", 30,136,2015,2015,0,0,season);
				break;
			/*case 173:// VOng 13 co 1 tran van la nam 2015 can sua sau khi crawler
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-vdqg-australia-1285-vong-", 27,173,2015,2016,13,0,season);
				break;*/
			case 82:
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-vdqg-singapore-2805-vong-", 22,82,2015,2015,0,0,season);
				break;
			case 165:
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-vdqg-israel-1945-vong-", 27,165,2015,2016,17,0,season);
				break;
			case 166:
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG Bỉ");
				break;
			case 360:
				LichThiDauCrawler.crawlerListLichThiDau(2015,"C1 U19 Châu Âu");
				break;
			case 134://
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG Canada");
				break;
			case 228://VĐQG Nam Phi 
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG Nam Phi");
				break;
			case 178://
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG Albania");
				break;
			case 153://
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG Armenia");
				break;
			case 104://
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG Azerbaijan");
				break;
			case 110://
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG Ba Lan");
				break;
			case 121://
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG Belarus");
				break;
			case 650://
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG BOSNIA");
				break;
			case 111://
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG BULGARY");
				break;
			case 112://
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG CROATIA");
				break;
			case 113://
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG ESTONIA");
				break;
			case 65://
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG GEORGIA");
				break;
			case 122://
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG HUNGARY");
				break;
			case 192://
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG Hy Lạp");
				break;
			case 180://
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG ICELAND");
				break;
			case 128://
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG Ireland");
				break;
			case 123://
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG KAZAKHSTAN");
				break;
			case 148://
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG LATVIA");
				break;
			case 87://
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG LITHUANIA");
				break;
			case 649://
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG LUXEMBOURG");
				break;
			case 171://
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG MACEDONIA");
				break;
			case 316://
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG MALTA");
				break;
			case 107://
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG MOLDOVA");
				break;
			case 172://
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG MONTENEGRO");
				break;
			case 71://
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG NA UY");
				break;
			case 83://
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG PHẦN LAN");
				break;
			case 108://
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG ROMANIA");
				break;
			case 67://
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG SERBIA");
				break;
			case 140://
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG SLOVAKIA");
				break;
			case 155://
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG SLOVENIA");
				break;
			case 114://
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG SÉC");
				break;
			case 137://
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG SÍP");
				break;
			case 175://
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG WALES");
				break;
			case 156://
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG ÁO");
				break;
			case 205://
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG HỒNG KÔNG");
				break;
			case 105://
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG IRAN");
				break;
			case 241://
				LichThiDauCrawler.crawlerListLichThiDau(2015,"MALAY SUPER LEAGUE");
				break;
			case 341://
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG QATAR");
				break;
			case 69://
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG OMAN");
				break;
			case 92://
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG UZBEKISTAN");
				break;
			case 574://
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG YEMEN");
				break;
			case 645://
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG NEW ZEALAND");
				break;
			case 313://
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG UAE");
				break;
			case 643://VĐQG CANADA
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG ECUADOR");
				break;
			case 159://
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG ALGERIA");
				break;
			case 68://
				LichThiDauCrawler.crawlerListLichThiDau(2014,"VĐQG AI CẬP");
				break;
			case 139://
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG MARỐC");
				break;
			case 70://
				LichThiDauCrawler.crawlerListLichThiDau(2014,"VĐQG TUNISIA");
				break;
			case 372://
				LichThiDauCrawler.crawlerListLichThiDau(2014,"VĐQG KUWAIT");
				break;
			case 115://VĐQG UKRAINA
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG UKRAINA");
				break;
			case 173://VĐQG UKRAINA
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG AUSTRALIA");
				break;
			case 21://VĐQG UKRAINA
				LichThiDauCrawler.crawlerListLichThiDau(2015,"HẠNG 2 UKRAINE");
				break;
			case 30://VĐQG UKRAINA
				LichThiDauCrawler.crawlerListLichThiDau(2015,"U21 HÀ LAN");
				break;
			case 395://VĐQG EURo 2016
				LichThiDauCrawler.crawlerListLichThiDau(2016,"Euro 2016");
				break;
			default:
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-ngoai-hang-anh-anh-vong-", 38,1,2015,2016,20,0,season);
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-vdqg-tay-ban-nha-tbn-vong-", 38,2,2015,2016,18,0,season);
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-vdqg-duc-duc-vong-", 34,3,2015,2016,18,0,season);
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-vdqg-italia-ita-vong-", 38,4,2015,2016,18,0,season);
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-vdqg-phap-pha-vong-", 38,5,2015,2016,20,0,season);
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-vdqg-viet-nam-vqg-vong-", 26,8,2015,2015,0,0,season);
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-vdqg-scotland-sco-vong-", 33,127,2015,2016,22,0,season);
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-vdqg-ha-lan-hlan-vong-", 34,176,2015,2016,18,0,season);
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-vdqg-nga-485-vong-", 30,109,2015,2016,19,0,season);
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-vdqg-tho-nhi-ky-506-vong-", 34,66,2015,2016,18,0,season);
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-vdqg-thuy-sy-985-vong-", 36,157,2015,2016,19,0,season);
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-vdqg-thuy-dien-565-vong-", 30,183,2015,2015,0,0,season);
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-vdqg-dan-mach-515-vong-", 33,119,2015,2016,19,0,season);
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-vdqg-nhat-ban-585-vong-", 17,81,2015,2015,0,0,season);
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-vdqg-han-quoc-765-vong-", 33,144,2015,2015,0,0,season);
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-vdqg-thai-lan-2025-vong-", 34,255,2015,2015,0,0,season);
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-vdqg-an-do-5025-vong-", 22,149,2015,2015,0,0,season);
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-vdqg-brazil-bra-vong-", 38,162,2015,2015,0,0,season);
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-vdqg-argentina-5725-vong-", 30,131,2015,2015,0,0,season);
				LichThiDauCrawler.crawlerLicThiDauBongDaWap("http://bongda.wap.vn/ket-qua-vdqg-my-fifa-vong-", 34,78,2015,2015,0,0,season);

				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG Bỉ");
				LichThiDauCrawler.crawlerListLichThiDau(2015,"C1 U19 Châu Âu");
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG Canada");
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG Nam Phi");
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG Albania");
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG Armenia");
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG Azerbaijan");
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG Ba Lan");
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG Belarus");
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG BOSNIA");
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG BULGARY");
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG CROATIA");
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG ESTONIA");				
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG GEORGIA");				
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG HUNGARY");				
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG Hy Lạp");				
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG ICELAND");			
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG Ireland");				
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG KAZAKHSTAN");				
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG LATVIA");				
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG LITHUANIA");				
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG LUXEMBOURG");				
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG MACEDONIA");
				
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG MALTA");
				
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG MOLDOVA");
				
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG MONTENEGRO");
				
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG NA UY");
				
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG PHẦN LAN");
				
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG ROMANIA");
				
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG SERBIA");
				
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG SLOVAKIA");
				
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG SLOVENIA");
				
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG SÉC");
				
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG SÍP");
				
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG WALES");
				
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG ÁO");
				
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG HỒNG KÔNG");
				
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG IRAN");
				
				LichThiDauCrawler.crawlerListLichThiDau(2015,"MALAY SUPER LEAGUE");
			
			
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG QATAR");
				
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG OMAN");
				
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG UZBEKISTAN");
				
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG YEMEN");
				
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG NEW ZEALAND");
				
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG UAE");
				
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG ECUADOR");
				
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG ALGERIA");
				
				LichThiDauCrawler.crawlerListLichThiDau(2014,"VĐQG AI CẬP");
				
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG MARỐC");
				
				LichThiDauCrawler.crawlerListLichThiDau(2014,"VĐQG TUNISIA");
				
				LichThiDauCrawler.crawlerListLichThiDau(2014,"VĐQG KUWAIT");
				
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG UKRAINA");
				
				LichThiDauCrawler.crawlerListLichThiDau(2015,"VĐQG AUSTRALIA");
		}
	}
	
	
}
