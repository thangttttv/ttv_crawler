package com.ttv.football;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.crawler.fetcher.HttpClientUtil;
import hdc.util.html.parser.XPathReader;
import hdc.util.text.StringUtil;

import java.util.ArrayList;
import java.util.Calendar;

import javax.xml.xpath.XPathConstants;

import org.apache.http.HttpResponse;

import com.az24.crawler.model.FBMatch;
import com.az24.dao.FootBallDAO;
import com.az24.test.Base64Coder;

public class UpdateMatch {
	
	public static void crawlerMatch(String url,int match_id)  {
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
				//CrawlerUtil.analysis(reader.getDocument());
				
				
				String status  = (String) reader.read("/html/body/center/div/div[5]/div[1]/center/div[2]/table/TBODY/tr/td[2]/table/TBODY/tr[1]/td", XPathConstants.STRING);
				String tyso  = (String) reader.read("//div[@id=\"match_tiso\"]", XPathConstants.STRING);
				String tyso_h1  = (String) reader.read("/html/body/center/div/div[5]/div[1]/center/div[2]/table/TBODY/tr/td[2]/table/TBODY/tr[3]/td", XPathConstants.STRING);
				System.out.println(status.trim());
				System.out.println(tyso.trim());
				System.out.println(tyso_h1.trim());
				tyso = tyso.replaceAll("[^\\d-]","");
				tyso_h1 = tyso_h1.replaceAll("[^\\d-]","");
				
				String match_minute = status.trim();
				System.out.println(match_minute.trim());
				
				if("".equalsIgnoreCase("")&&tyso_h1.equalsIgnoreCase("")&&match_minute.equalsIgnoreCase(""))
					FootBallDAO.deleteMatch(match_id) ;
				
				if(StringUtil.isEmpty(tyso)&&!"Hoãn".equalsIgnoreCase(status)){
					String date_time  = (String) reader.read("/html/body/center/div/div[5]/div[1]/center/div[2]/table/TBODY/tr/td[2]/table/TBODY/tr[1]/td", XPathConstants.STRING);
					date_time = date_time.trim();
					System.out.println("Node Date:"+date_time);
					status = "";tyso  ="";tyso_h1="";match_minute="";
					String dd_mm[] = date_time.substring(5).split("/");
					String match_date =Calendar.getInstance().get(Calendar.YEAR)+"-"+dd_mm[1]+"-"+dd_mm[0]+" "+date_time.substring(0, 5)+":00";
					System.out.println("match_date:"+match_date);
					FootBallDAO.updateMatchCrawlerFormUrlCode(match_id, tyso,tyso_h1,status,match_minute,match_date);
				}else {
					try {
						if(Integer.parseInt(status)>0||"HT".equalsIgnoreCase(status)) status="Live";
					} catch (Exception e) {
					}
					FootBallDAO.updateMatchCrawlerFormUrlCode(match_id, tyso,tyso_h1,status,match_minute);
				}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	
	}
	
	public static void main(String[] args) {
		ArrayList<FBMatch>   list = FootBallDAO.getListMatchErro();
		for (FBMatch fbMatch : list) {
			System.out.println(Base64Coder.decodeString(fbMatch.url_code));
			UpdateMatch.crawlerMatch(Base64Coder.decodeString(fbMatch.url_code), fbMatch.id);
		}
		
	}
}
