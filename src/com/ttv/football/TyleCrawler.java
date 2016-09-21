package com.ttv.football;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.crawler.fetcher.HttpClientUtil;
import hdc.util.html.parser.XPathReader;
import hdc.util.text.StringUtil;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathConstants;

import org.apache.http.HttpResponse;
import org.w3c.dom.NodeList;

import com.az24.crawler.model.FBMatch;
import com.az24.crawler.model.FBTyLe;
import com.az24.dao.FootBallDAO;
import com.az24.test.Base64Coder;

public class TyleCrawler {
	public static void crawlerTyle(String url,int match_id)  {
		try {
			HttpClientImpl client = new HttpClientImpl();
			HttpResponse res = null;
			if(url.indexOf("http")<0)
				res = client.fetch("http://bongda.wap.vn/"+url);
			else
				res = client.fetch(url);
			//HttpClientUtil.printResponseHeaders(res);
			String html = HttpClientUtil.getResponseBody(res);
			
			Pattern r1 = Pattern.compile("(/ket-qua/w-ty-le-tran-dau.*.html)");
			Matcher   m1 = r1.matcher(html);
			String url_tyle = "";
			 if(m1.find())
		      {
			    url_tyle = m1.group(0);
			    res = client.fetch("http://bongda.wap.vn"+url_tyle);
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
						String loai_tyle  = (String) reader.read(xpath_node_content + "[" + i + "]"
								+ "/text()", XPathConstants.STRING);
					
						/*logger.info("------------------+"+i);
						logger.info(loai_tyle.trim());*/
						
						if("Trực tiếp".equalsIgnoreCase(loai_tyle.trim())){
							String chau_a_catran  = (String) reader.read("//DIV[@class='trd_tyle']/DIV" + "[" + i + "]"
									+ "/TABLE[1]/TBODY[1]/TR[2]/TD[2]", XPathConstants.STRING);
							String chau_a_BT_catran  = (String) reader.read("//DIV[@class='trd_tyle']/DIV" + "[" + i + "]"
									+ "/TABLE[1]/TBODY[1]/TR[2]/TD[3]", XPathConstants.STRING);
							/*logger.info(chau_a_catran.trim());
							logger.info(chau_a_BT_catran.trim());*/
							
							tyle.chau_a_tt_catran = chau_a_catran.trim();
							tyle.chau_a_tt_bt = chau_a_BT_catran.trim();
							
						}
						
						if("Châu á".equalsIgnoreCase(loai_tyle.trim())){
							String chau_a_catran  = (String) reader.read("//DIV[@class='trd_tyle']/DIV" + "[" + i + "]"
									+ "/TABLE[1]/TBODY[1]/TR[2]/TD[2]", XPathConstants.STRING);
							String chau_a_bt_catran  = (String) reader.read("//DIV[@class='trd_tyle']/DIV" + "[" + i + "]"
									+ "/TABLE[1]/TBODY[1]/TR[2]/TD[3]", XPathConstants.STRING);
							
							String chau_a_hiep1  = (String) reader.read("//DIV[@class='trd_tyle']/DIV" + "[" + i + "]"
									+ "/TABLE[1]/TBODY[1]/TR[4]/TD[2]", XPathConstants.STRING);
							String chau_a_bt_hiep1  = (String) reader.read("//DIV[@class='trd_tyle']/DIV" + "[" + i + "]"
									+ "/TABLE[1]/TBODY[1]/TR[4]/TD[3]", XPathConstants.STRING);
							
							/*logger.info(chau_a_catran.trim());
							logger.info(chau_a_bt_catran.trim());
							logger.info(chau_a_hiep1.trim());
							logger.info(chau_a_bt_hiep1.trim());*/
							
							tyle.chau_a_bt_catran = chau_a_bt_catran.trim();
							tyle.chau_a_bt_hiep1 = chau_a_bt_hiep1.trim();
							tyle.chau_a_catran = chau_a_catran.trim();
							tyle.chau_a_hiep1 = chau_a_hiep1.trim();
							
						}
						
						
						if("Châu âu".equalsIgnoreCase(loai_tyle.trim())){
							String chau_au  = (String) reader.read("//DIV[@class='trd_tyle']/DIV" + "[" + i + "]"
									+ "", XPathConstants.STRING);
							//logger.info(chau_au.trim());
							tyle.chau_au = chau_au.trim();
						}
								i++;
							
						}
					
					 int tyle_id = FootBallDAO.checkTyLe(match_id);
					 tyle.match_id = match_id;
					 
					 if(FootBallDAO.checkTyLe(match_id)==0){
						
						 FootBallDAO.saveTyle(tyle);
					  }else{
						  tyle.id = tyle_id;
						  FootBallDAO.updateTyle(tyle);
					  }
				}
		      }
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	
	}
	
	
	public static void main(String args[]){
		while(true){
			List<FBMatch> listMatch = FootBallDAO.getListMatchTT();
			for (FBMatch fbMatch : listMatch) {
				try {
					if(!StringUtil.isEmpty(fbMatch.url_tyle)){
						/*logger.info("Crawler Tyle URL--->"+Base64Coder.decodeString(fbMatch.url_tyle));
						logger.info("Crawler TrucTiep Trận đấu --->"+fbMatch.id);*/
					
						TyleCrawler.crawlerTyle(Base64Coder.decodeString(fbMatch.url_tyle), fbMatch.id);}
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
		}
		
	}
}
