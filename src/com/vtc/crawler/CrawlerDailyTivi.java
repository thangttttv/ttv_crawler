package com.vtc.crawler;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.util.html.parser.XPathReader;
import hdc.util.text.StringUtil;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.xpath.XPathConstants;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.az24.crawler.model.Tivi;
import com.az24.dao.TienIchDAO;
import com.az24.test.HttpClientFactory;
import com.az24.test.HttpClientUtil;
import com.az24.util.io.FileUtil;

public class CrawlerDailyTivi {
	
	 public  void kplusTivi() throws Exception {
	    	Calendar cal = Calendar.getInstance() ;      
	        DefaultHttpClient client = HttpClientFactory.getInstance() ;
	        client.getParams().setParameter("http.protocol.expect-continue",false) ;
	        String day = cal.get(Calendar.DAY_OF_MONTH)<10?"0"+cal.get(Calendar.DAY_OF_MONTH):cal.get(Calendar.DAY_OF_MONTH)+"";
	        String month = cal.get(Calendar.MONTH)<10?"0"+cal.get(Calendar.MONTH):cal.get(Calendar.MONTH)+"";
	    	String date =  day + "-" + month + "-" + cal.get(Calendar.YEAR);
	    	HttpPost post = new HttpPost("http://www.kplus.vn/lich-phat-song.htm") ;
	        List<NameValuePair> list = new ArrayList<NameValuePair>() ;
	        list.add(new BasicNameValuePair("__EVENTARGUMENT", "")) ;
	        list.add(new BasicNameValuePair("__EVENTTARGET", "ctl01$ctl00$ddlChannels")) ;
	        list.add(new BasicNameValuePair("__EVENTVALIDATION", "MBRDqoBzgPEmTIOH0nO+WhJvEwkN9L7kvCV0ylT8zcVqFve0Sg54cwNRruIiHIvq6Q4NxikUBR6OLGwG1SjKC1A82GTJDyBJN22hMifjEeBDX0Zas1R5pjNkEAqpMBNR5BTEKt4redzF/aufPG/tO7Tb4+M4xiV8Jmv+F4Kmd2uj6KylzfKhBQtcUhPH47L8ZYOxJDWOykC45Y8gUU3Nj5mq182pFVUq81pGGNKjyIyR8woLVbbbEPkO8RiDJQO96kp40feBb5D7SSmu3AGygQjIA3+ZinPkbioTbFyioSys2h058dyDS+cy3lLcq4grljI9jPLZWO8Hf1bgicxOm8G+gILkuWH5/g0oZg7Tq8Rwb+aEBBtZyyQ3v6Usv2q7sZMLubfAvzdt9e2tGC12kvFWvAkVnCoVTxK55zR/9dfjc8niH0U705c+Sf2bOm+CqIqPgjixncluUZLmHgbCNkZoUDsjVfnG2W34z4c2XiLe19eDIwdbbi/Ahz5gQMXlSql48N/SJ+lbCRIAnGnbXQBqi2647agpBre9+581Aluy9DZKOx0o9vNN+trJzQ0Y190tszjGgcyx44Rl2tYiotO6/C385jeFDDCW88B6i58sqrEHr3jTdkISO3K1gxSRYkBNJoP/xGBqBzkmZk+FqLuvOmaIFPbw5P1XAoy33hjwTcW6CfTIIWOY+/dtJFKVkjqz4KXeLdzHcKesgU8QrLT2Hp0=")) ;
	        list.add(new BasicNameValuePair("__LASTFOCUS", "")) ;
	        list.add(new BasicNameValuePair("__VIEWSTATE", "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")) ;
	        list.add(new BasicNameValuePair("__VIEWSTATEENCRYPTED", "")) ;
	        list.add(new BasicNameValuePair("ctl01$ctl00$ddlChannels","119")) ;
	        list.add(new BasicNameValuePair("ctl01$ctl00$txtDay2", date)) ;
	        list.add(new BasicNameValuePair("pageHeader$ctl00$txtSearc...", "Tìm kiếm")) ;
	        post.setEntity(new UrlEncodedFormEntity(list)) ;
	        HttpResponse res = client.execute(post) ;
	        String html = HttpClientUtil.getResponseBody(res) ;
	        XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
			String xpath__weather_td= "//table[@class='fix']/TBODY[1]/tr";
			NodeList linkNodes = (NodeList) reader.read(xpath__weather_td, XPathConstants.NODESET);
			int i = 1;
			CrawlerUtil.analysis(reader.getDocument(), "");
			TienIchDAO tienIchDAO = new TienIchDAO();
			if(linkNodes!=null)
			{
				while(i<=linkNodes.getLength())
				{
					Tivi tivi = new Tivi();
					tivi.provider = "K+";
					String xpath_channel = "html/body[1]/form[@id='form1']/div[@id='wrapper']/div[@id='main']/div[4]/table[1]/TBODY[1]/tr["+i+"]/td[1]";
					Node channel =  (Node) reader.read(xpath_channel, XPathConstants.NODE);
					tivi.channel = StringUtil.trim(channel.getTextContent()).trim();;		
					String xpath_time = "html/body[1]/form[@id='form1']/div[@id='wrapper']/div[@id='main']/div[4]/table[1]/TBODY[1]/tr["+i+"]/td[2]";
					Node time =  (Node) reader.read(xpath_time, XPathConstants.NODE);
					tivi.time_play =  StringUtil.trim(time.getTextContent()).trim();
					String xpath_program = "html/body[1]/form[@id='form1']/div[@id='wrapper']/div[@id='main']/div[4]/table[1]/TBODY[1]/tr["+i+"]/td[3]";
					
					Node program =  (Node) reader.read(xpath_program, XPathConstants.NODE);
					tivi.program = StringUtil.trim(program.getTextContent()).trim();
					tivi.create_date = new Date(Calendar.getInstance().getTimeInMillis());
					tienIchDAO.saveTivi(tivi);
					
					System.out.println(i +"  "+time.getTextContent().trim()+"  " +program.getTextContent());
					i++;
				}
			}
			
	        FileUtil.writeToFile("d:/kplus.html", html, false);
	        //System.out.println(html);
	 }
	 
	public void vtvTivi(String url) throws Exception {
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		String xpath__weather_td= "/HTML[1]/BODY[1]/TABLE[1]/TBODY[1]/TR";
		NodeList linkNodes = (NodeList) reader.read(xpath__weather_td, XPathConstants.NODESET);
		int i = 1;
		CrawlerUtil.analysis(reader.getDocument(), "");
		TienIchDAO tienIchDAO = new TienIchDAO();
		if(linkNodes!=null)
		{
			while(i<=linkNodes.getLength())
			{
				Tivi tivi = new Tivi();
				tivi.provider = "VTV";
				tivi.channel = "VTV3";				
				String xpath_province = "/HTML[1]/BODY[1]/TABLE[1]/TBODY[1]/TR["+i+"]/TD[1]";
				Node time =  (Node) reader.read(xpath_province, XPathConstants.NODE);
				tivi.time_play =  StringUtil.trim(time.getTextContent()).trim();
				String xpath_program = "/HTML[1]/BODY[1]/TABLE[1]/TBODY[1]/TR["+i+"]/TD[2]";
				Node program =  (Node) reader.read(xpath_program, XPathConstants.NODE);
				System.out.println(i +"  "+time.getTextContent().trim()+"  " + program.getTextContent().trim() );
				tivi.program = StringUtil.trim(program.getTextContent()).trim();
				tivi.create_date = new Date(Calendar.getInstance().getTimeInMillis());
				tienIchDAO.saveTivi(tivi);
				i++;
			}
		}
	}
	
	public void yanTivi() throws Exception {
		String url ="http://www.yantv.vn/Home/Default.aspx?cmd=broadcasttoday";
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		String xpath__weather_td= "//div[@id='center']/div[1]/div[1]/div[1]/div";
		NodeList linkNodes = (NodeList) reader.read(xpath__weather_td, XPathConstants.NODESET);
		int i = 2;
		//CrawlerUtil.analysis(reader.getDocument(), "");
		TienIchDAO tienIchDAO = new TienIchDAO();
		if(linkNodes!=null)
		{
			while(i<=linkNodes.getLength())
			{
				Tivi tivi = new Tivi();
				tivi.provider = "yan";
				tivi.channel = "yan";				
				String xpath_province = "//div[@id='center']/div[1]/div[1]/div[1]/div["+i+"]/div[1]/p[1]";
				Node time =  (Node) reader.read(xpath_province, XPathConstants.NODE);
				tivi.time_play = StringUtil.trim(time.getTextContent()).trim();
				String xpath_program = "//div[@id='center']/div[1]/div[1]/div[1]/div["+i+"]/div[1]/p[2]";
				Node program =  (Node) reader.read(xpath_program, XPathConstants.NODE);
				tivi.program = StringUtil.trim(program.getTextContent()).trim();
				System.out.println(i +"  "+time.getTextContent().trim()+"  "+program.getTextContent().trim());
				tivi.create_date = new Date(Calendar.getInstance().getTimeInMillis());
				tienIchDAO.saveTivi(tivi);
				i++;
			}
		}
	}
	
	public static void main(String[] args) {
		CrawlerDailyTivi crawlerDailyTivi = new CrawlerDailyTivi();
		try {
			//String url = "http://vtv.vn/LichPS/GetLichPhatsong?nam=2012&thang=10&ngay=9&kenh=VTV4";
			//crawlerDailyTivi.vtvTivi(url);
			//crawlerDailyTivi.yanTivi();
			crawlerDailyTivi.kplusTivi();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
