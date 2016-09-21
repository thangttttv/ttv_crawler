package com.ttv.football;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.crawler.fetcher.HttpClientUtil;
import hdc.util.html.parser.XPathReader;
import hdc.util.text.StringUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathConstants;

import org.apache.http.HttpResponse;
import org.w3c.dom.NodeList;

import com.az24.crawler.model.FBClub;
import com.az24.crawler.model.FBCup;
import com.az24.crawler.model.FBMatch;
import com.az24.crawler.model.FBTyLe;
import com.az24.dao.FootBallDAO;
import com.az24.test.Base64Coder;

public class LiveScoreCrawler {
	public static HashMap<String, String> checkTT = new HashMap<String, String>();
	public static HashMap<String, String> mapSV = new HashMap<String, String>();
	public static HashMap<String, String> mapUrlTyle = new HashMap<String, String>();
	public static HashMap<String, FBClub> mapClub = new HashMap<String, FBClub>();
	public static HashMap<String, String> mapCup = new HashMap<String, String>();
	public static HashMap<String, String> mapMatch = new HashMap<String, String>();
	public static HashMap<String, String> mapMatchTime = new HashMap<String, String>();
	public static HashMap<String, String> mapMatchStatus = new HashMap<String, String>();
	
	public static String getHTML(String urlToRead) {
		URL url; // The URL to read
		HttpURLConnection conn; // The actual connection to the web page
		BufferedReader rd; // Used to read results from the web page
		String line; // An individual line of the web page HTML
		String result = ""; // A long string containing all the HTML
		try {
			url = new URL(urlToRead);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "UTF-8"));
			while ((line = rd.readLine()) != null) {
				result += line + "\n";
			}
			rd.close();

		} catch (Exception e) {
			System.out.println("Dung lai co loi getHTML: " + e.getMessage());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				System.out.println("" + e1.getMessage());
			}
		}

		return result;
	}

	public static void crawlerLiveScoreBongDaWap() {
		try {

			String html = getHTML("http://bongda.wap.vn/process-web-livescore.jsp");
			//String html = getHTML("http://bongda.wap.vn/livescore.html");

			XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
			CrawlerUtil.analysis(reader.getDocument());

			String xpath_node_content = "//div[@id=\"ajax-liveform\"]/div[2]/table/TBODY/tr";
			xpath_node_content = "HTML/BODY[1]/DIV[2]/TABLE[1]/TBODY[1]/TR";

			NodeList nodes = (NodeList) reader.read(xpath_node_content,
					XPathConstants.NODESET);
			Pattern r = Pattern.compile("\\s{2,}|\\n||\\t");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			if (nodes != null) {
				int node_one_many = nodes.getLength();
				int i = 2;
				String giai_dau = "";
				Date date_match = new Date(Calendar.getInstance().getTime().getTime()+ 24 * 60 * 60 * 1000);
				String pattern = "yyyy-MM-dd";
				SimpleDateFormat format = new SimpleDateFormat(pattern);
				String str_curent_date = format.format(date_match);
				Date date_match_last = Calendar.getInstance().getTime();
				date_match_last = new Date(date_match_last.getTime() - 24 * 60* 60 * 1000);
				String str_date_match_last = format.format(date_match_last);
				
				while (i <= node_one_many) {
					String text_colspan = (String) reader.read(
							xpath_node_content + "[" + i + "]"
									+ "/TD[1]/@colspan", XPathConstants.STRING);

					if ("8".equalsIgnoreCase(text_colspan)
							|| "10".equalsIgnoreCase(text_colspan)) {

					} else {
						String tem_giai_dau = (String) reader.read(
								xpath_node_content + "[" + i + "]"
										+ "/TD[1]/SPAN[2]/A/text()",
								XPathConstants.STRING);
						String url_cup = (String) reader.read(
								xpath_node_content + "[" + i + "]"
										+ "/TD[1]/SPAN[2]/A/@href",
								XPathConstants.STRING);
						tem_giai_dau = tem_giai_dau.trim();

						//logger.info("Giai dau: " + tem_giai_dau.trim());

						if (!"-".equalsIgnoreCase(tem_giai_dau)) {
							giai_dau = tem_giai_dau.replaceAll("\\s+", " ");
							if (mapCup.get(giai_dau.toUpperCase()) == null) {
								if (FootBallDAO.checkCup(giai_dau) == 0) {
									crawlerCup(url_cup, giai_dau);
								}
							}
						}
						String gio = (String) reader.read(xpath_node_content
								+ "[" + i + "]" + "/TD[2]",
								XPathConstants.STRING);
						gio = gio.trim();

						String phut = (String) reader.read(xpath_node_content
								+ "[" + i + "]" + "/TD[3]",
								XPathConstants.STRING);
						phut = phut.trim();

						String club = (String) reader.read(xpath_node_content
								+ "[" + i + "]"
								+ "/TD[4]/TABLE/TBODY/TR/TD/SPAN[1]/A/text()",
								XPathConstants.STRING);
						club = club.trim().replaceAll("\\s+", " ");

						if (StringUtil.isEmpty(club)) {
							i++;
							continue;
						}
						;
						String url_club1 = (String) reader
								.read(xpath_node_content
										+ "["+ i+ "]"
										+ "/TD[4]/TABLE/TBODY/TR/TD/SPAN[1]/A/@href",
										XPathConstants.STRING);
						String url_code = (String) reader.read(
								xpath_node_content + "[" + i + "]"
										+ "/TD[5]/TABLE/TBODY/TR/TD/A/@href",
								XPathConstants.STRING);

						String strMapStatus = mapMatchStatus.get(Base64Coder
								.encodeString(url_code));

						if ("FT".equalsIgnoreCase(strMapStatus)
								|| "Hoãn".equalsIgnoreCase(phut)) {
							i++;
							/*logger.info("------------->Tiep tuc tran" + i
									+ ":url->"
									+ Base64Coder.encodeString(url_code));*/
							continue;
						}
						;

						Matcher m = r.matcher(club);

						FBClub fbclub = mapClub.get(club.toUpperCase());
						if (fbclub == null)
							fbclub = FootBallDAO.getClub(club.toUpperCase());
						else {
							r = Pattern.compile("-(\\d+)\\.html");
							m = r.matcher(url_club1);
							String bdwap_id = null;
							if (m.find()) {
								bdwap_id = m.group(1);
							}
							fbclub.bdwap_id = bdwap_id;
							FootBallDAO.updateBDWapIDClub(fbclub.id,
									fbclub.bdwap_id);
						}

						if (fbclub == null)
							crawlerClup_1(url_club1, url_code);

						String ket_qua = (String) reader.read(
								xpath_node_content + "[" + i + "]"
										+ "/TD[5]/TABLE/TBODY/TR/TD/A",
								XPathConstants.STRING);
						ket_qua = ket_qua.trim();

						String ket_qua_url = (String) reader.read(
								xpath_node_content + "[" + i + "]"
										+ "/TD[5]/TABLE/TBODY/TR/TD/A/@href",
								XPathConstants.STRING);
						ket_qua_url = ket_qua_url.trim();

						String club_2 = (String) reader.read(xpath_node_content
								+ "[" + i + "]"
								+ "/TD[6]/TABLE/TBODY/TR/TD/SPAN[1]/A/text()",
								XPathConstants.STRING);
						club_2 = club_2.trim().replaceAll("\\s+", " ");

						// if(club.indexOf("Đài Loan Nữ")<0){ i++;continue;};

						String url_club2 = (String) reader
								.read(xpath_node_content
										+ "["
										+ i
										+ "]"
										+ "/TD[6]/TABLE/TBODY/TR/TD/SPAN[1]/A/@href",
										XPathConstants.STRING);

						if (StringUtil.isEmpty(club_2)) {
							i++;
							continue;
						}

						FBClub fbclub2 = mapClub.get(club_2.toUpperCase());
						if (fbclub2 == null)
							fbclub2 = FootBallDAO.getClub(club_2.toUpperCase());
						else {
							r = Pattern.compile("-(\\d+)\\.html");
							m = r.matcher(url_club2);
							String bdwap_id = null;
							if (m.find()) {
								bdwap_id = m.group(1);
							}
							fbclub2.bdwap_id = bdwap_id;
							FootBallDAO.updateBDWapIDClub(fbclub2.id,
									fbclub2.bdwap_id);
						}

						if (fbclub2 == null)
							crawlerClup_2(url_club2, url_code);

					/*	String tile = (String) reader.read(xpath_node_content
								+ "[" + i + "]" + "/TD[7]",
								XPathConstants.STRING);*/
						club_2 = club_2.trim();

						String kqhiep1 = (String) reader.read(
								xpath_node_content + "[" + i + "]" + "/TD[8]",
								XPathConstants.STRING);
						club_2 = club_2.trim();

						if (mapClub.get(club.toUpperCase()) == null)
							fbclub = FootBallDAO.getClubJoinCoach(club);

						if (mapClub.get(club_2.toUpperCase()) == null)
							fbclub2 = FootBallDAO.getClubJoinCoach(club_2);

						int fbcup_id = 0;
						if (mapCup.get(giai_dau.toUpperCase()) == null)
							fbcup_id = FootBallDAO.checkCup(giai_dau);
						else
							fbcup_id = Integer.parseInt(mapCup.get(giai_dau
									.toUpperCase()));

						if (fbcup_id > 0 && fbclub != null & fbclub2 != null) {
							mapCup.put(giai_dau.toUpperCase(),
									String.valueOf(fbcup_id));
							mapClub.put(club.toUpperCase(), fbclub);
							mapClub.put(club_2.toUpperCase(), fbclub2);

							FBMatch match = new FBMatch();
							match.club_id_1 = fbclub.id;
							match.club_id_2 = fbclub2.id;
							match.club_code_1 = fbclub.code;
							match.club_code_2 = fbclub2.code;
							match.club_name_1 = fbclub.name;
							match.club_name_2 = fbclub2.name;
							match.coach_name_1 = fbclub.coachname;
							match.coach_name_2 = fbclub2.coachname;

							match.cup_id = fbcup_id;
							match.result = ket_qua.replaceAll("[^\\d-]", "");
							match.result_1 = kqhiep1.replaceAll("[^\\d-]", "");

							if (fbclub.logo != null)
								match.club_logo_1 = fbclub.ymd + fbclub.logo;
							if (fbclub2.logo != null)
								match.club_logo_2 = fbclub2.ymd + fbclub2.logo;

							match.match_minute = phut;
							phut = phut.replace("'", "");

							try {
								if (Integer.parseInt(phut) > 0
										|| "HT".equalsIgnoreCase(phut))
									match.status = "Live";
								else
									match.status = phut;
							} catch (Exception e) {
								match.status = phut;
							}

							if (StringUtil.isEmpty(match.match_minute))
								match.match_minute = gio;

							r = Pattern.compile("-(\\d+)\\.html");
							m = r.matcher(ket_qua_url);
							String bdwap_id = null;
							if (m.find()) {
								bdwap_id = m.group(1);
							}
							match.bdwap_id = bdwap_id;

							if (ket_qua_url.indexOf("http") < 0)
								ket_qua_url = "http://bongda.wap.vn/"
										+ ket_qua_url;

							match.url_code = Base64Coder
									.encodeString(ket_qua_url);

							int match_id = 0;
							if (mapMatch
									.get(Base64Coder.encodeString(url_code)) != null)
								match_id = Integer
										.parseInt(mapMatch.get(Base64Coder
												.encodeString(url_code)));

							if (match_id == 0)
								match_id = FootBallDAO.checkMatch(
										match.club_id_1, match.club_id_2,
										str_date_match_last, str_curent_date
												+ " 23:59:59");

							// int match_id = 0;
							if (match_id == 0)
								match_id = FootBallDAO
										.checkMatchByBDWapID(bdwap_id);

							String sovong = "", url_tyle = "";
							if (match_id == 0
									|| mapSV.get(Base64Coder
											.encodeString(url_code)) == null) {
								sovong = crawlerVongDau(url_code);

							} else {
								sovong = mapSV.get(Base64Coder
										.encodeString(url_code));
							}
							sovong = sovong.replaceAll("^\\W+", "");
							url_tyle = mapUrlTyle.get(Base64Coder
									.encodeString(url_code));

							match.id = match_id;
							match.url_tyle = Base64Coder.encodeString(url_tyle);
							match.match_time = mapMatchTime.get(Base64Coder
									.encodeString(url_code))
									+ " "
									+ gio
									+ ":00";

							if (match_id == 0) {
								match.round = sovong;
								match_id = FootBallDAO.saveMatch(match);
								mapMatch.put(
										Base64Coder.encodeString(url_code),
										String.valueOf(match_id));
							} else {
								mapMatch.put(
										Base64Coder.encodeString(url_code),
										String.valueOf(match_id));
								if (!StringUtil.isEmpty(sovong))
									match.round = sovong;
								FootBallDAO.updateMatchLive(match);
								mapMatchStatus.put(
										Base64Coder.encodeString(url_code),
										phut);
							}
						}

						List<FBMatch> listMatch = FootBallDAO
								.getListMatchByDate(formatter.format(Calendar
										.getInstance().getTime()));
						FootBallDAO.createFileJSONFBMatch(listMatch, formatter
								.format(Calendar.getInstance().getTime()));

						listMatch = FootBallDAO.getListMatchLive();
						FootBallDAO.createFileJSONFBMatchLive(listMatch);

					/*	logger.info("----------------" + i + "/"
								+ node_one_many);
						logger.info("Giai Dau" + giai_dau.trim());
						logger.info("Gio:" + gio.trim());
						logger.info("Phut:" + phut.trim());
						logger.info("club:" + club.trim());
						logger.info("Ket qua:" + ket_qua.trim());
						logger.info("club_2:" + club_2.trim());
						logger.info("tile:" + tile.trim());
						logger.info("kqhiep1:" + kqhiep1.trim());*/
					}

					Thread.sleep(50);
					i++;
				}
			}

		} catch (Exception e) {
			System.out.println(e.getStackTrace());
			try {
				Thread.sleep(150);
			} catch (InterruptedException e1) {
				System.out.println(e1.getStackTrace());
			}
		}

	}

	public static void crawlerClup_1(String url, String url_code) {
		try {
			HttpClientImpl client = new HttpClientImpl();
			HttpResponse res = null;
			if (url.indexOf("http") < 0)
				res = client.fetch("http://bongda.wap.vn/" + url);
			else
				res = client.fetch(url);

			//HttpClientUtil.printResponseHeaders(res);
			String html = HttpClientUtil.getResponseBody(res);

			XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
			//CrawlerUtil.analysis(reader.getDocument());

			String xpath_country = "//FORM[@name='formCLB']/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]";
			String country = (String) reader.read(xpath_country,
					XPathConstants.STRING);
			country = country.replaceAll("\t", "");

			String xpath_logo = "//IMG[@class='CLB_logo']/@src";
			String logo = (String) reader.read(xpath_logo,
					XPathConstants.STRING);
			logo = logo.trim().replaceAll("\t", "");

			String gt[] = country.split("\n");

			FBClub club = new FBClub();
			int i = 0;
			for (String string : gt) {

				if (i == 1)
					club.name = string.replace("Đội:", "").trim();
				if (i == 2)
					club.city = string.replace("Thành phố:", "").trim();

				if (StringUtil.isEmpty(club.city)) {
					if (i == 5)
						club.country = string.trim();
					if (i == 8)
						club.info = string.replace("TT Khác:", "").trim();
				} else {
					if (i == 4)
						club.country = string.trim();
					if (i == 7)
						club.info = string.replace("TT Khác:", "").trim();
				}

				i++;
			}

			if (url_code.indexOf("http") < 0)
				res = client.fetch("http://bongda.wap.vn/" + url_code);
			else
				res = client.fetch(url_code);

			//HttpClientUtil.printResponseHeaders(res);
			html = HttpClientUtil.getResponseBody(res);

			reader = CrawlerUtil.createXPathReaderByData(html);
			//CrawlerUtil.analysis(reader.getDocument());
			
			String xpath_code = "/html/BODY/center/div/div[5]/div[1]/center/div[3]/table/TBODY/tr/td[1]/b[1]/text()";
			xpath_code =  "html/BODY[1]/center[1]/div[1]/div[6]/div[1]/center[1]/div[3]/table[1]/TBODY[1]/tr[1]/td[1]/b[1]/text()";
			String code = (String) reader.read(xpath_code,
					XPathConstants.STRING);

			club.code = code.replace("(", "");
			club.code = club.code.replaceAll("\\W", "");
			club.code = club.code.replace(")", "").trim();
			club.logo_source = logo;

			if (!StringUtil.isEmpty(logo)) {
				String ymd = "";
				Date date_match = new Date(Calendar.getInstance().getTime()
						.getTime());
				String pattern = "yyyy/MMdd";
				SimpleDateFormat format = new SimpleDateFormat(pattern);
				ymd = format.format(date_match);

				String pathFolder = "/home/kktien/domains/kenhkiemtien.com/public_html/kenhkiemtien.com/upload/bongda/club/"
						+ ymd + "/";
				// String pathFolder = "C:/Projects/footballer/"+ymd+"/";
				File file = new File(pathFolder);
				if (!file.exists()) {
					file.mkdirs();
					Runtime.getRuntime().exec("chown -R 507:509  " + pathFolder);

				}

				hdc.util.io.FileUtil.saveImage(
						club.logo_source,
						pathFolder
								+ club.logo_source.substring(club.logo_source
										.lastIndexOf("/")));
				club.logo = club.logo_source.substring(club.logo_source
						.lastIndexOf("/") + 1);
			}

			club.name_en = club.name;
			String country_en = CountryUnit.countryMap.get(club.country.trim()
					.toLowerCase());
			country_en = (StringUtil.isEmpty(country_en)) ? club.country
					: country_en;
			club.country_en = country_en;

			Pattern r = Pattern.compile("-(\\d+)\\.html");
			Matcher m = r.matcher(url);
			String bdwap_id = null;
			if (m.find()) {
				bdwap_id = m.group(1);
			}
			club.bdwap_id = bdwap_id;

			//logger.info("----------->" + url);
			FootBallDAO.saveClupFromBDWap(club, url_code);
		} catch (Exception e) {
			System.out.println("URL loi->"+url_code);
			System.out.println(e.getStackTrace());
		}

	}

	public static void crawlerClup_2(String url, String url_code) {
		try {
			HttpClientImpl client = new HttpClientImpl();

			HttpResponse res = null;

			if (url.indexOf("http") < 0)
				res = client.fetch("http://bongda.wap.vn/" + url);
			else
				res = client.fetch(url);

			//HttpClientUtil.printResponseHeaders(res);
			String html = HttpClientUtil.getResponseBody(res);

			XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
			//CrawlerUtil.analysis(reader.getDocument());

			String xpath_country = "//FORM[@name='formCLB']/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]";
			String country = (String) reader.read(xpath_country,
					XPathConstants.STRING);
			String gt[] = country.split("\n");

			String xpath_logo = "//IMG[@class='CLB_logo']/@src";
			String logo = (String) reader.read(xpath_logo,
					XPathConstants.STRING);
			logo = logo.trim().replaceAll("\t", "");

			FBClub club = new FBClub();
			int i = 0;
			for (String string : gt) {

				if (i == 1)
					club.name = string.replace("Đội:", "").trim();
				if (i == 2)
					club.city = string.replace("Thành phố:", "").trim();

				if (StringUtil.isEmpty(club.city)) {
					if (i == 5)
						club.country = string.trim();
					if (i == 8)
						club.info = string.replace("TT Khác:", "").trim();
				} else {
					if (i == 4)
						club.country = string.trim();
					if (i == 7)
						club.info = string.replace("TT Khác:", "").trim();
				}

				i++;
			}

			if (url_code.indexOf("http") < 0)
				res = client.fetch("http://bongda.wap.vn/" + url_code);
			else
				res = client.fetch(url_code);

			//HttpClientUtil.printResponseHeaders(res);
			html = HttpClientUtil.getResponseBody(res);

			reader = CrawlerUtil.createXPathReaderByData(html);
			//CrawlerUtil.analysis(reader.getDocument());

			String xpath_code = "/html/BODY/center/div/div[6]/div[1]/center/div[3]/table/TBODY/tr/td[3]/b[2]/text()";
			String code = (String) reader.read(xpath_code,
					XPathConstants.STRING);

			club.code = code.replace("(", "");
			club.code = club.code.replaceAll("\\W", "");
			club.code = club.code.replace(")", "").trim();
			club.logo_source = logo;

			if (!StringUtil.isEmpty(logo)) {
				String ymd = "";
				Date date_match = new Date(Calendar.getInstance().getTime()
						.getTime());
				String pattern = "yyyy/MMdd";
				SimpleDateFormat format = new SimpleDateFormat(pattern);
				ymd = format.format(date_match);

				String pathFolder = "/home/kktien/domains/kenhkiemtien.com/public_html/kenhkiemtien.com/upload/bongda/club/"
						+ ymd + "/";
				// String pathFolder = "C:/Projects/footballer/"+ymd+"/";
				File file = new File(pathFolder);
				if (!file.exists()) {
					file.mkdirs();
					Runtime.getRuntime()
							.exec("chown -R 507:509  " + pathFolder);
				}

				hdc.util.io.FileUtil.saveImage(
						club.logo_source,
						pathFolder
								+ club.logo_source.substring(club.logo_source
										.lastIndexOf("/")));
				club.logo = club.logo_source.substring(club.logo_source
						.lastIndexOf("/") + 1);
			}

			club.name_en = club.name;

			String country_en = CountryUnit.countryMap.get(club.country.trim()
					.toLowerCase());
			country_en = (StringUtil.isEmpty(country_en)) ? club.country
					: country_en;
			club.country_en = country_en;

			Pattern r = Pattern.compile("-(\\d+)\\.html");
			Matcher m = r.matcher(url);
			String bdwap_id = null;
			if (m.find()) {
				bdwap_id = m.group(1);
			}
			club.bdwap_id = bdwap_id;

			//logger.info("----------->" + url);
			FootBallDAO.saveClupFromBDWap(club, url_code);

		} catch (Exception e) {
			System.out.println("URL loi->"+url_code);
			System.out.println(e.getStackTrace());
		}
	}

	public static String crawlerVongDau(String url) {
		String vong = "";
		try {

			HttpClientImpl client = new HttpClientImpl();

			HttpResponse res = null;

			if (url.indexOf("http") < 0)
				res = client.fetch("http://bongda.wap.vn/" + url);
			else
				res = client.fetch(url);

			//HttpClientUtil.printResponseHeaders(res);
			String html = HttpClientUtil.getResponseBody(res);

			XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
			//CrawlerUtil.analysis(reader.getDocument());

			String xpath_code = "/html/body/center/div/div[5]/div[1]/center/div[1]/a/span/text()";
			vong = (String) reader.read(xpath_code, XPathConstants.STRING);

			String xtime = "/html/body/center/div/div[5]/div[1]/center/div[2]/table/TBODY/tr[1]/td[2]/table/TBODY/tr[1]/td[1]";
			String time = (String) reader.read(xtime, XPathConstants.STRING);

			String match_time;
			Pattern p = Pattern.compile("\\d{2}:\\d{4}/\\d{2}");
			Matcher m = p.matcher(time.trim()); // get a matcher object
			if (m.find()) {
				time = time.trim();
				String ngay = time.substring(5);
				String arNgay[] = ngay.split("/");
				match_time = Calendar.getInstance().get(Calendar.YEAR) + "-"
						+ arNgay[1] + "-" + arNgay[0];

			} else {
				match_time = Calendar.getInstance().get(Calendar.YEAR) + "-"
						+ (Calendar.getInstance().get(Calendar.MONTH) + 1)
						+ "-"
						+ Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
			}

			String xpath_tyle = "//a[@id='tl_m']/@href";
			String url_tyle = (String) reader.read(xpath_tyle,
					XPathConstants.STRING);

			if (url_tyle.indexOf("http") < 0)
				url_tyle = "http://bongda.wap.vn/" + url_tyle;

			vong = vong.replaceAll(", Vòng", "");
			vong = vong.replaceAll("Vòng", "");
			mapSV.put(Base64Coder.encodeString(url), vong);
			mapUrlTyle.put(Base64Coder.encodeString(url), url_tyle);
			mapMatchTime.put(Base64Coder.encodeString(url), match_time);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return vong.replaceAll("^\\s+|\\s+$|\\s*(\n)\\s*|(\\s)\\s*", "$1$2")
				.replace("\t", " ");
	}

	public static void crawlerCup(String url, String code) {
		try {
			HttpClientImpl client = new HttpClientImpl();

			HttpResponse res = null;

			if (url.indexOf("http") < 0)
				res = client.fetch("http://bongda.wap.vn/" + url);
			else
				res = client.fetch(url);

			//HttpClientUtil.printResponseHeaders(res);
			String html = HttpClientUtil.getResponseBody(res);

			XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
			//CrawlerUtil.analysis(reader.getDocument());

			String xpath_country = "//form[@name=\"frmControl\"]/div/b";
			String country = (String) reader.read(xpath_country,
					XPathConstants.STRING);

			// String xpath_name = "//form[@name=\"formKetQua\"]/div/h2";
			// String name = (String) reader.read(xpath_name ,
			// XPathConstants.STRING);

			String xpath_node_content = "//select[@name=\"code\"]/option";
			NodeList nodes = (NodeList) reader.read(xpath_node_content,
					XPathConstants.NODESET);
			if (nodes != null) {
				int node_one_many = nodes.getLength();

				int i = 1;

				while (i <= node_one_many) {
					String value = (String) reader.read(xpath_node_content
							+ "[" + i + "]" + "/@value", XPathConstants.STRING);
					String tengiai = (String) reader.read(xpath_node_content
							+ "[" + i + "]" + "/text()", XPathConstants.STRING);

					if (url.indexOf(value) > 0) {
						FBCup cup = new FBCup();
						cup.code = code.trim();
						cup.code = cup.code.replaceAll("\\W", "");
						cup.name = tengiai.trim();
						cup.name_en = tengiai.trim();
						cup.country = country.replace(":", "");

						String country_en = CountryUnit.countryMap
								.get(cup.country.trim().toLowerCase());
						country_en = (StringUtil.isEmpty(country_en)) ? cup.country
								: country_en;
						cup.country_en = country_en;
						FootBallDAO.saveCup(cup);
					}
					i++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void crawlerTyle(String url, int match_id) {
		try {
			HttpClientImpl client = new HttpClientImpl();

			HttpResponse res = null;

			if (url.indexOf("http") < 0)
				res = client.fetch("http://bongda.wap.vn/" + url);
			else
				res = client.fetch(url);

			//HttpClientUtil.printResponseHeaders(res);
			String html = HttpClientUtil.getResponseBody(res);

			Pattern r1 = Pattern.compile("(/ket-qua/w-ty-le-tran-dau.*.html)");
			Matcher m1 = r1.matcher(html);
			String url_tyle = "";
			if (m1.find()) {
				url_tyle = m1.group(0);
				//logger.info(url_tyle);

				res = client.fetch("http://bongda.wap.vn" + url_tyle);
				//HttpClientUtil.printResponseHeaders(res);
				html = HttpClientUtil.getResponseBody(res);
				XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
				//CrawlerUtil.analysis(reader.getDocument());

				String xpath_node_content = "//DIV[@class='trd_tyle']/H2";

				NodeList nodes = (NodeList) reader.read(xpath_node_content,
						XPathConstants.NODESET);
				if (nodes != null) {
					int node_one_many = nodes.getLength();
					int i = 1;

					FBTyLe tyle = new FBTyLe();

					while (i <= node_one_many) {
						String loai_tyle = (String) reader.read(
								xpath_node_content + "[" + i + "]" + "/text()",
								XPathConstants.STRING);

						//logger.info("------------------+" + i);
						//logger.info(loai_tyle.trim());

						if ("Trực tiếp".equalsIgnoreCase(loai_tyle.trim())) {
							String chau_a_catran = (String) reader.read(
									"//DIV[@class='trd_tyle']/DIV" + "[" + i
											+ "]"
											+ "/TABLE[1]/TBODY[1]/TR[2]/TD[2]",
									XPathConstants.STRING);
							String chau_a_BT_catran = (String) reader.read(
									"//DIV[@class='trd_tyle']/DIV" + "[" + i
											+ "]"
											+ "/TABLE[1]/TBODY[1]/TR[2]/TD[3]",
									XPathConstants.STRING);
							//logger.info(chau_a_catran.trim());
							//logger.info(chau_a_BT_catran.trim());

							tyle.chau_a_tt_catran = chau_a_catran.trim();
							tyle.chau_a_tt_bt = chau_a_BT_catran.trim();

						}

						if ("Châu á".equalsIgnoreCase(loai_tyle.trim())) {
							String chau_a_catran = (String) reader.read(
									"//DIV[@class='trd_tyle']/DIV" + "[" + i
											+ "]"
											+ "/TABLE[1]/TBODY[1]/TR[2]/TD[2]",
									XPathConstants.STRING);
							String chau_a_bt_catran = (String) reader.read(
									"//DIV[@class='trd_tyle']/DIV" + "[" + i
											+ "]"
											+ "/TABLE[1]/TBODY[1]/TR[2]/TD[3]",
									XPathConstants.STRING);

							String chau_a_hiep1 = (String) reader.read(
									"//DIV[@class='trd_tyle']/DIV" + "[" + i
											+ "]"
											+ "/TABLE[1]/TBODY[1]/TR[4]/TD[2]",
									XPathConstants.STRING);
							/*String chau_a_bt_hiep1 = (String) reader.read(
									"//DIV[@class='trd_tyle']/DIV" + "[" + i
											+ "]"
											+ "/TABLE[1]/TBODY[1]/TR[4]/TD[3]",
									XPathConstants.STRING);*/

						/*	logger.info(chau_a_catran.trim());
							logger.info(chau_a_bt_catran.trim());
							logger.info(chau_a_hiep1.trim());
							logger.info(chau_a_bt_hiep1.trim());*/

							tyle.chau_a_bt_catran = chau_a_bt_catran.trim();
							tyle.chau_a_bt_hiep1 = chau_a_bt_catran.trim();
							tyle.chau_a_catran = chau_a_catran.trim();
							tyle.chau_a_hiep1 = chau_a_hiep1.trim();

						}

						if ("Châu âu".equalsIgnoreCase(loai_tyle.trim())) {
							String chau_au = (String) reader.read(
									"//DIV[@class='trd_tyle']/DIV" + "[" + i
											+ "]" + "", XPathConstants.STRING);
							//logger.info(chau_au.trim());
							tyle.chau_au = chau_au.trim();
						}
						i++;

					}

					int tyle_id = FootBallDAO.checkTyLe(match_id);
					tyle.match_id = match_id;

					if (FootBallDAO.checkTyLe(match_id) == 0) {

						FootBallDAO.saveTyle(tyle);
					} else {
						tyle.id = tyle_id;
						FootBallDAO.updateTyle(tyle);
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public static void crawlerUrlTuongThuatAjax(String url, int match_id,
			int fbcup_id, int club_id_1, int club_id_2) {
		try {
			HttpClientImpl client = new HttpClientImpl();

			HttpResponse res = null;

			if (url.indexOf("http") < 0)
				res = client.fetch("http://bongda.wap.vn/" + url);
			else
				res = client.fetch(url);

			//HttpClientUtil.printResponseHeaders(res);
			String html = HttpClientUtil.getResponseBody(res);

			Pattern r1 = Pattern
					.compile("(/ket-qua/tuong-thuat-tran-dau-.*.html)|(/ket-qua/tt.jsp?.*showAds=0)|(/ket-qua/tt.jsp?.*showAds=1)");
			Matcher m1 = r1.matcher(html);
			String url_tt = "";
			if (m1.find()) {
				url_tt = m1.group(0);
				if (!StringUtil.isEmpty(url_tt)) {
					TrucTiepTranDauCrawler.crawlerTuongThuatTranDau(
							"http://bongda.wap.vn" + url_tt, match_id,
							fbcup_id, club_id_1, club_id_2);
					checkTT.put(String.valueOf(match_id), "co");
				} else {
					checkTT.put(String.valueOf(match_id), "ko");
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public static void main(String args[]) {
		CountryUnit.getCountryTxt();
		Date bdChay = Calendar.getInstance().getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		while (1 > 0) {
			bdChay = Calendar.getInstance().getTime();
			try {
				LiveScoreCrawler.crawlerLiveScoreBongDaWap();

				/*
				 * List<FBMatch> listMatch =
				 * FootBallDAO.getListMatchByDate(formatter
				 * .format(Calendar.getInstance().getTime()));
				 * FootBallDAO.createFileJSONFBMatch(listMatch,
				 * formatter.format(Calendar.getInstance().getTime()));
				 * 
				 * listMatch = FootBallDAO.getListMatchLive();
				 * FootBallDAO.createFileJSONFBMatchLive(listMatch);
				 */
				List<FBMatch> listMatch = null;
				int i = 1;
				while (i <= 7) {
					Date date_match = new Date(Calendar.getInstance().getTime()
							.getTime()
							- (24 * 60 * 60 * 1000 * i));
					listMatch = FootBallDAO.getListMatchByDate(formatter
							.format(date_match));
					FootBallDAO.createFileJSONFBMatch(listMatch,
							formatter.format(date_match));
					i++;
				}
				i = 1;
				while (i <= 7) {
					Date date_match = new Date(Calendar.getInstance().getTime()
							.getTime()
							+ (24 * 60 * 60 * 1000 * i));
					listMatch = FootBallDAO.getListMatchByDate(formatter
							.format(date_match));
					FootBallDAO.createFileJSONFBMatch(listMatch,
							formatter.format(date_match));
					i++;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				Date kTchay = Calendar.getInstance().getTime();
				long timechay = (kTchay.getTime() - bdChay.getTime()) / 1000;
				System.out.println("-------------->Cuoi Dong roi chay mat:" + timechay
						+ "<-------------------");
				Thread.sleep(5000);

			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
		}

	}
}
