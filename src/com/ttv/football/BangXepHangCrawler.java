package com.ttv.football;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.crawler.fetcher.HttpClientUtil;
import hdc.util.html.parser.XPathReader;
import hdc.util.text.StringUtil;

import javax.xml.xpath.XPathConstants;

import org.apache.http.HttpResponse;
import org.w3c.dom.NodeList;

import com.az24.crawler.model.FBChart;
import com.az24.crawler.model.FBClub;
import com.az24.crawler.model.FBCup;
import com.az24.dao.FootBallDAO;

public class BangXepHangCrawler {
	
	public static void crawlerBangXepHang(String url,int cup_id) throws Exception {
			HttpClientImpl client = new HttpClientImpl();
		
		 	HttpResponse res = client.fetch(url);
			//HttpClientUtil.printResponseHeaders(res);
			String html = HttpClientUtil.getResponseBody(res);
			
		
			XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
			CrawlerUtil.analysis(reader.getDocument());

			String xpath_node_content = "/html/body/center/div/div[5]/div[6]/table/TBODY/tr";

			NodeList nodes = (NodeList) reader.read(xpath_node_content,
					XPathConstants.NODESET);
			if (nodes != null) {
				int node_one_many = nodes.getLength();
				
				if(node_one_many==0) {
					xpath_node_content = "/html/body/center/div/div[5]/div[5]/table/TBODY/tr";
					 nodes = (NodeList) reader.read(xpath_node_content,
							XPathConstants.NODESET);
					 node_one_many = nodes.getLength();
				}
				
				if(node_one_many==0) {////div[@class='menu_trd']/a
					xpath_node_content = "//div[@class='New_col-centre']/div[5]/table/TBODY/tr";
					 nodes = (NodeList) reader.read(xpath_node_content,
							XPathConstants.NODESET);
					 node_one_many = nodes.getLength();
				}
				
				int i = 2;

				while (i <= node_one_many) {
					
					String stt  = (String) reader.read(xpath_node_content + "[" + i + "]"
							+ "/td[1]/text()", XPathConstants.STRING);
					String doi  = (String) reader.read(xpath_node_content + "[" + i + "]"
							+ "/td[2]", XPathConstants.STRING);
					doi = doi.trim().replaceAll("\\s+", " ");
					String doiUrl  = (String) reader.read(xpath_node_content + "[" + i + "]"
							+ "/td[2]/a/@href", XPathConstants.STRING);
					String tran  = (String) reader.read(xpath_node_content + "[" + i + "]"
							+ "/td[3]", XPathConstants.STRING);
					String thang  = (String) reader.read(xpath_node_content + "[" + i + "]"
							+ "/td[4]", XPathConstants.STRING);	
					String hoa  = (String) reader.read(xpath_node_content + "[" + i + "]"
							+ "/td[5]", XPathConstants.STRING);
					String bai  = (String) reader.read(xpath_node_content + "[" + i + "]"
							+ "/td[6]", XPathConstants.STRING);
					String bt  = (String) reader.read(xpath_node_content + "[" + i + "]"
							+ "/td[7]", XPathConstants.STRING);
					String bb  = (String) reader.read(xpath_node_content + "[" + i + "]"
							+ "/td[8]", XPathConstants.STRING);
					String hs  = (String) reader.read(xpath_node_content + "[" + i + "]"
							+ "/td[9]", XPathConstants.STRING);
					String diem  = (String) reader.read(xpath_node_content + "[" + i + "]"
							+ "/td[10]", XPathConstants.STRING);
					if("".compareToIgnoreCase(stt.trim())==0) {i++;continue;	}
					FBChart fbchart = new FBChart();
					fbchart.rate = Integer.parseInt(stt.trim());
					fbchart.cup_id = cup_id;
					fbchart.so_tran = Integer.parseInt(tran.trim());
					fbchart.so_tran_thang = Integer.parseInt(thang.trim());
					fbchart.so_tran_hoa = Integer.parseInt(hoa.trim());
					fbchart.so_tran_bai = Integer.parseInt(bai.trim());
					fbchart.ban_thang = Integer.parseInt(bt.trim());
					fbchart.ban_bai = Integer.parseInt(bb.trim());
					fbchart.hieu_so = Integer.parseInt(hs.trim());
					fbchart.diem = Integer.parseInt(diem.trim());
					doi = doi.trim().replaceAll("\\s+", " ");
					fbchart.name = doi.trim();
					fbchart.season = "2016";
					fbchart.cup_group = "";
					FBClub club  = FootBallDAO.getClub(doi.trim().toUpperCase());
					
					int club_id = 0;
					if(club==null) {club_id = BangXepHangCrawler.crawlerClup(doiUrl);
						club  = FootBallDAO.getClubByID(club_id);
					} else club_id = club.id;
					
					if(club!=null){
						fbchart.club_id = club.id;
						fbchart.code = club.code;
						int chart_id = FootBallDAO.checkChart(cup_id,club.id, fbchart.season);
						if(chart_id==0)
							FootBallDAO.saveChart(fbchart);
						else{
							fbchart.id = chart_id;
							FootBallDAO.updateChart(fbchart);
						}
							
					}
					
					System.out.println("----------------");
					System.out.println(stt.trim()+"-"+doi.trim()+"---"+tran.trim()+"-"+thang.trim());
					System.out.println("----------------");
					i++;
					}
			}
	}
	
	public static void crawlerListBXH() throws Exception {
		HttpClientImpl client = new HttpClientImpl();
	
	 	HttpResponse res = client.fetch("http://bongda.wap.vn/bang-xep-hang-bong-da.html");
		//HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);
		
	
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		//CrawlerUtil.analysis(reader.getDocument());

		String xpath_node_content = "//ul[@class='countries']/li";

		NodeList nodes = (NodeList) reader.read(xpath_node_content,
				XPathConstants.NODESET);
		if (nodes != null) {
			int node_one_many = nodes.getLength();
			
			int i = 1;

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
					int cup_id = FootBallDAO.checkCupByName(tetGiai);
					if(cup_id==0){
						System.out.println("Khong tiem thay cup:"+tetGiai);
						BangXepHangCrawler.crawlerCup(link);
						j++;
						continue;
					}
					//get link bxh
					res = client.fetch(link);
					// HttpClientUtil.printResponseHeaders(res);
					 html = HttpClientUtil.getResponseBody(res);
					XPathReader readerBXH = CrawlerUtil.createXPathReaderByData(html);
					//CrawlerUtil.analysis(readerBXH.getDocument());
					NodeList nodes2 = (NodeList) readerBXH.read("//div[@class='menu_trd']/a",
							XPathConstants.NODESET);
					int k = 1;
					int node_one_many3 = nodes2.getLength();
					while (k <= node_one_many3) {
						String linkBXH = "";
						String labelBXH = (String) readerBXH.read("//div[@class='menu_trd']/a"+"["+k+"]/text()", XPathConstants.STRING);
						if("BXH".equalsIgnoreCase(labelBXH)){
							 linkBXH = (String) readerBXH.read("//div[@class='menu_trd']/a"+"["+k+"]/@href", XPathConstants.STRING);
							 
							 // check loai bang xh
							 res = client.fetch("http://bongda.wap.vn/"+linkBXH);
							// HttpClientUtil.printResponseHeaders(res);
							 html = HttpClientUtil.getResponseBody(res);
							 XPathReader readerCTBXH = CrawlerUtil.createXPathReaderByData(html);
						     String xpath_node_content_colspan = "//table[1]/TBODY/tr[2]/td/@colspan";
						 	 String colspan = (String) readerCTBXH.read(xpath_node_content_colspan, XPathConstants.STRING);
							 System.out.println(colspan);
							 
							 if(!"6".equalsIgnoreCase(colspan))
								 BangXepHangCrawler.crawlerBangXepHang("http://bongda.wap.vn/"+linkBXH,cup_id);
							 else 
								 BangXepHangL2Crawler.crawlerBangXepHang("http://bongda.wap.vn/"+linkBXH,cup_id);
							 System.out.println("---------->CUP"+tetGiai+cup_id);
							 System.out.println("---------->"+linkBXH);
						}
						
						k++;
					}
					
					j++;
				}
				i++;
			}
		}
	}
	
	
	public static int crawlerClup(String url)  {
		CountryUnit.getCountryTxt();
		FBClub club = new FBClub();
		int club_id = 0;
		try {
			HttpClientImpl client = new HttpClientImpl();
			HttpResponse res = null;
			if(url.indexOf("http")<0)
				res = client.fetch("http://bongda.wap.vn/"+url);
			else
				res = client.fetch(url);
			
			//HttpClientUtil.printResponseHeaders(res);
			String html = HttpClientUtil.getResponseBody(res);
			
		    
			XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
			CrawlerUtil.analysis(reader.getDocument());

			String xpath_country = "//FORM[@name='formCLB']/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]";
			String country  = (String) reader.read(xpath_country , XPathConstants.STRING);
			country = country.replaceAll("\t", "");
			
			String xpath_logo = "//IMG[@class='CLB_logo']/@src";
			String logo  = (String) reader.read(xpath_logo , XPathConstants.STRING);
			logo = logo.trim().replaceAll("\t", "");
			
			String gt[] = country.split("\n");
			
			
			int i = 0;
			for (String string : gt) {
				System.out.println(i+string.trim());
				if(i==1) club.name = string.replace("Đội:", "").trim();
				if(i==2) club.city = string.replace("Thành phố:", "").trim();
				
				if(StringUtil.isEmpty(club.city )){
					if(i==5) club.country = string.trim();
					if(i==8) club.info = string.replace("TT Khác:", "").trim();
				}else{
					if(i==4) club.country = string.trim();
					if(i==7) club.info = string.replace("TT Khác:", "").trim();
				}
				i++;
			}
			
			String url_code =  (String) reader.read("//DIV[@id=\"trandau\"]/DIV/TABLE/TBODY/TR[2]/TD[5]/TABLE/TBODY/TR[1]/TD/B/A/@href" , XPathConstants.STRING);
			if(StringUtil.isEmpty(url_code)) url_code =  (String) reader.read("//DIV[@id=\"trandau\"]/DIV/TABLE/TBODY/TR[3]/TD[5]/TABLE/TBODY/TR[1]/TD/B/A/@href" , XPathConstants.STRING);
			if(url_code.indexOf("http")<0)
				res = client.fetch("http://bongda.wap.vn/"+url_code);
			else
				res = client.fetch(url_code);
			
		//	HttpClientUtil.printResponseHeaders(res);
			html = HttpClientUtil.getResponseBody(res);
			
		
			reader = CrawlerUtil.createXPathReaderByData(html);
			CrawlerUtil.analysis(reader.getDocument());
			String code ="";
			String xpath_code_1 = "/html/body/center/div/div[5]/div[1]/center/div[3]/table/TBODY/tr/td[1]/b[1]/text()";
			String xpath_name_1 = "/html/body/center/div/div[5]/div[1]/center/div[3]/table/TBODY/tr/td[1]/b[2]/text()";
			String xpath_code_2 = "/html/body/center/div/div[5]/div[1]/center/div[3]/table/TBODY/tr/td[3]/b[2]/text()";
			//String xpath_name_2 = "/html/body/center/div/div[5]/div[1]/center/div[3]/table/TBODY/tr/td[3]/b[1]/text()";
			String code_1  = (String) reader.read(xpath_code_1 , XPathConstants.STRING);
			String code_2  = (String) reader.read(xpath_code_2, XPathConstants.STRING);
			
			String name_1  = (String) reader.read(xpath_name_1 , XPathConstants.STRING);
			//String name_2  = (String) reader.read(xpath_name_2, XPathConstants.STRING);
			
			if("".equalsIgnoreCase(name_1)){
				 xpath_code_1 = "/html/body/center/div/div[5]/div[1]/center/div[4]/table/TBODY/tr/td[1]/b[1]/text()";
				 xpath_name_1 = "/html/body/center/div/div[5]/div[1]/center/div[4]/table/TBODY/tr/td[1]/b[2]/text()";
				 xpath_code_2 = "/html/body/center/div/div[5]/div[1]/center/div[4]/table/TBODY/tr/td[3]/b[2]/text()";
				 //xpath_name_2 = "/html/body/center/div/div[5]/div[1]/center/div[4]/table/TBODY/tr/td[3]/b[1]/text()";
				 code_1  = (String) reader.read(xpath_code_1 , XPathConstants.STRING);
				 code_2  = (String) reader.read(xpath_code_2, XPathConstants.STRING);
				
				 name_1  = (String) reader.read(xpath_name_1 , XPathConstants.STRING);
				 //name_2  = (String) reader.read(xpath_name_2, XPathConstants.STRING);
			}
		    
			if(name_1.equalsIgnoreCase(club.name)) code =code_1; else code =code_2;
			System.out.println(i+code.trim());
			
			club.code = code.replace("(", "");
			club.code = club.code.replaceAll("\\W", "");
			club.code = club.code.replace(")", "").trim();
			club.logo_source = logo;
			
			if(!StringUtil.isEmpty(logo)){
				String ymd ="";
				Date date_match = new Date(Calendar.getInstance().getTime().getTime());
				String pattern = "yyyy/MMdd";
			    SimpleDateFormat format = new SimpleDateFormat(pattern);
			    ymd = format.format(date_match);
			    
				String pathFolder = "/home/kktien/domains/kenhkiemtien.com/public_html/kenhkiemtien.com/upload/bongda/club/"+ymd+"/";
			    //String pathFolder = "C:/Projects/footballer/"+ymd+"/";
			    File file = new File(pathFolder);
				if(!file.exists()){file.mkdirs();
				Runtime.getRuntime().exec("chown -R 507:509  "  + pathFolder);

				}

				hdc.util.io.FileUtil.saveImage(club.logo_source, pathFolder+club.logo_source.substring(club.logo_source.lastIndexOf("/")));
				club.logo = club.logo_source.substring(club.logo_source.lastIndexOf("/")+1);
			}
			
			
			
			/* xpath_code = "/html/body/center/div/div[5]/div[1]/center/div[3]/table/TBODY/tr/td[3]/b[2]/text()";
			String code2  = (String) reader.read(xpath_code , XPathConstants.STRING);
			System.out.println(i+code2.trim());*/
			club.name =  club.name.replaceAll("\\s+", " ");
			club.name_en = club.name;
			String country_en = CountryUnit.countryMap.get(club.country.trim().toLowerCase());
			country_en =  (StringUtil.isEmpty(country_en))?club.country:country_en;
			club.country_en = country_en;
			
			Pattern r =   Pattern.compile("-(\\d+)\\.html");   
		    Matcher  m = r.matcher(url);
		    String bdwap_id = null;
    		if(m.find())
		      { bdwap_id = m.group(1);
		      }
    		club.bdwap_id =bdwap_id;
			
			System.out.println("----------->"+url);
			club_id=FootBallDAO.saveClupFromBDWap(club,url);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Khong crawler dc doi code:"+club.code+" Url:"+url);
			e.printStackTrace();
		}
		
		return club_id;
	}
	
	
	public static void crawlerCup(String url) {
		try {
			HttpClientImpl client = new HttpClientImpl();
			
			HttpResponse res = null;

			if(url.indexOf("http")<0)
				res = client.fetch("http://bongda.wap.vn/"+url);
			else
				res = client.fetch(url);
			
			//HttpClientUtil.printResponseHeaders(res);
			String html = HttpClientUtil.getResponseBody(res);
			
		
			XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
			CrawlerUtil.analysis(reader.getDocument());

			String xpath_country = "//form[@name=\"frmControl\"]/div/b";
			String country  = (String) reader.read(xpath_country , XPathConstants.STRING);
			
			String xpath_name = "//form[@name=\"formKetQua\"]/div/h2";
			String name  = (String) reader.read(xpath_name , XPathConstants.STRING);
			
			String code = (String) reader.read("//div[@class=\"smsGiaidau_1\"]/div/span" , XPathConstants.STRING);
					
			System.out.println(country);
			System.out.println(name);
			String xpath_node_content = "//select[@name=\"code\"]/option";
			NodeList nodes = (NodeList) reader.read(xpath_node_content,
					XPathConstants.NODESET);
			if (nodes != null) {
				int node_one_many = nodes.getLength();
				
				int i = 1;

				while (i <= node_one_many) {
					
					
					String value  = (String) reader.read(xpath_node_content + "[" + i + "]"
							+ "/@value", XPathConstants.STRING);
					
					String tengiai  = (String) reader.read(xpath_node_content + "[" + i + "]"
							+ "/text()", XPathConstants.STRING);
					System.out.println(value);
					
					
					if(url.indexOf(value)>0){
						name = tengiai;	
						System.out.println(tengiai);
						FBCup cup = new FBCup();
						cup.code = code.trim();
						cup.code = cup.code.replaceAll("\\W", "").replaceFirst("BD", "");
						cup.name = tengiai.trim();
						cup.name_en = tengiai.trim();
						cup.country = country.replace(":", "");
						
						String country_en = CountryUnit.countryMap.get(cup.country.trim().toLowerCase());
						country_en =  (StringUtil.isEmpty(country_en))?cup.country:country_en;
						cup.country_en = country_en;

						FootBallDAO.saveCup(cup);
					}
						
							i++;
						
					}
					
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	public static void main(String args[]) throws Exception{
		CountryUnit.getCountryTxt();
		//BangXepHangCrawler.crawlerBangXepHang("http://bongda.wap.vn/bang-xep-hang-ngoai-hang-anh-anh.html",1);
		/*BangXepHangCrawler.crawlerBangXepHang("http://bongda.wap.vn/bang-xep-hang-vdqg-tay-ban-nha-tbn.html",2);
		BangXepHangCrawler.crawlerBangXepHang("http://bongda.wap.vn/bang-xep-hang-vdqg-duc-duc.html",3);
		BangXepHangCrawler.crawlerBangXepHang("http://bongda.wap.vn/bang-xep-hang-vdqg-italia-ita.html",4);
		BangXepHangCrawler.crawlerBangXepHang("http://bongda.wap.vn/bang-xep-hang-vdqg-phap-pha.html",5);
		BangXepHangCrawler.crawlerBangXepHang("http://bongda.wap.vn/bang-xep-hang-vdqg-viet-nam-vqg.html",8);
		BangXepHangCrawler.crawlerBangXepHang("http://bongda.wap.vn/bang-xep-hang-vdqg-scotland-sco.html",127);
		BangXepHangCrawler.crawlerBangXepHang("http://bongda.wap.vn/bang-xep-hang-vdqg-ha-lan-hlan.html",176);
		BangXepHangCrawler.crawlerBangXepHang("http://bongda.wap.vn/bang-xep-hang-vdqg-nga-485.html",109);
		BangXepHangCrawler.crawlerBangXepHang("http://bongda.wap.vn/bang-xep-hang-vdqg-tho-nhi-ky-506.html",66);*/
		//BangXepHangCrawler.crawlerBangXepHang("http://bongda.wap.vn/bang-xep-hang-vdqg-argentina-5725.html",131);
		BangXepHangCrawler.crawlerListBXH();
		//BangXepHangCrawler.crawlerCup("http://bongda.wap.vn/ket-qua-vdqg-kuwait-2065.html");
		//BangXepHangCrawler.crawlerBangXepHang("http://bongda.wap.vn/bang-xep-hang-euro-2016-505.html",395);
	}
}
