package com.az24.crawler.ketban;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.DocumentAnalyzer;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.util.html.A;
import hdc.util.html.parser.XPathReader;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathConstants;

import org.apache.http.HttpResponse;

import com.az24.crawler.DownloadImage;
import com.az24.crawler.fiter.AbstractFilter;
import com.az24.test.HttpClientUtil;
import com.az24.util.UTF8Tool;

public class CrawlerKetBan {

	public String  baseUrl = "";
	public String  rewriterUrl = "";
	public String  urlRegex="";
	
	public void collectionLink(String url) throws Exception {
		DocumentAnalyzer analyzer;
		int i = 0;
		try {
			HttpClientImpl client = new HttpClientImpl();
			HttpResponse res = client.fetch(url);
			String html = HttpClientUtil.getResponseBody(res);
			analyzer = new DocumentAnalyzer.DefaultDocumentAnayzer(baseUrl,	rewriterUrl);			
			List<A> list= null;			
			list = analyzer.analyze(html,url);
			if (list != null)
				for (A a : list) {
					boolean kt = AbstractFilter.find(urlRegex, a.getURL());
					if (kt) {
						System.out.println("Inject-->i=" + i + a.getURL());
						this.extractUser(a.getURL());
						Thread.sleep(1000);
						i++;
					}
				}
		
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
	
	public void extractUser(String url) 
	{
		String xpath_url_file = "//TABLE[@id='thongtintvien']/TBODY[1]/TR[2]/TD[1]/IMG[1]/@src";
		String xpath_url_fullname = "//TABLE[@id='thongtintvien']/TBODY[1]/TR[3]/TD[1]/SPAN[1]/text()";
		String xpath_url_address = "//TABLE[@id='thongtintvien']/TBODY[1]/TR[3]/TD[1]/SPAN[2]/text()";
		String xpath_url_age = "//TABLE[@id='thongtintvien']/TBODY[1]/TR[3]/TD[1]/SPAN[3]/text()";
		
		String xpath_url_sex = "//TABLE[@id='thongtintvien']/TBODY[1]/TR[3]/TD[1]/SPAN[4]/text()";
		String xpath_url_education = "//TABLE[@id='thongtintvien']/TBODY[1]/TR[3]/TD[1]/SPAN[5]/text()";
		String xpath_url_height = "//TABLE[@id='thongtintvien']/TBODY[1]/TR[3]/TD[1]/SPAN[6]/text()";
		String xpath_url_weight = "//TABLE[@id='thongtintvien']/TBODY[1]/TR[3]/TD[1]/SPAN[7]/text()";
		
		String xpath_url_religious = "//TABLE[@id='thongtintvien']/TBODY[1]/TR[3]/TD[1]/SPAN[8]/text()";
		String xpath_url_job = "//TABLE[@id='thongtintvien']/TBODY[1]/TR[3]/TD[1]/SPAN[9]/text()";
		
		String xpath_url_reference = "//TABLE[@id='thongtintvien']/TBODY[1]/TR[3]/TD[1]/SPAN[11]/text()";
		String xpath_url_hate = "//TABLE[@id='thongtintvien']/TBODY[1]/TR[3]/TD[1]/SPAN[12]/text()";
		
		String xpath_url_info = "//TABLE[@id='thongtintvien']/TBODY[1]/TR[3]/TD[2]/SPAN[1]/text()";
		String xpaht_url_married = "//TABLE[@id='thongtintvien']/TBODY[1]/TR[3]/TD[1]/SPAN[10]/text()";
		String xpath_url_friend_desired = "//TABLE[@id='thongtintvien']/TBODY[1]/TR[3]/TD[2]/SPAN[2]/text()";
		
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		String html;
		User user = new User();
		try {
			html = HttpClientUtil.getResponseBody(res);
			XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
			String image = (String) reader.read(xpath_url_file, XPathConstants.STRING);
			System.out.println("image="+image);
			
			String fullname = (String) reader.read(xpath_url_fullname, XPathConstants.STRING);
			System.out.println("fullname="+fullname);
			user.fullname = fullname;
			DownloadImage downloadImage = new com.az24.crawler.DownloadImage(this.baseUrl,this.rewriterUrl);
			
			Pattern pattern = Pattern.compile("\\s+");
			fullname = UTF8Tool.coDau2KoDau(fullname);
			Matcher m = pattern.matcher(fullname);
			fullname = m.replaceAll("_");
			downloadImage.downloadImage(image, "D:/anh/"+fullname.trim()+".jpg");
			
			user.avatar = fullname.trim()+".jpg";
			user.username = fullname;
			
			String address = (String) reader.read(xpath_url_address, XPathConstants.STRING);
			System.out.println("address="+address);
			user.address=address;
			
			String age = (String) reader.read(xpath_url_age, XPathConstants.STRING);
			System.out.println("age"+age);
			user.age = Integer.parseInt(age.trim());
			
			String sex = (String) reader.read(xpath_url_sex, XPathConstants.STRING);
			System.out.println("sex"+sex);
			if("nam".equalsIgnoreCase(sex.trim()))
			user.sex = 1; else user.sex=0;
			String education = (String) reader.read(xpath_url_education, XPathConstants.STRING);
			System.out.println("education"+education);
			user.education = education;
			String height = (String) reader.read(xpath_url_height, XPathConstants.STRING);
			System.out.println("height"+height);
			user.height = height.trim();
			String weight = (String) reader.read(xpath_url_weight, XPathConstants.STRING);
			System.out.println("weight"+weight);
			user.weight = weight.trim();
			
			String religirous = (String) reader.read(xpath_url_religious, XPathConstants.STRING);
			System.out.println("religirous"+religirous);
			user.religious = religirous;
			
			
			String married = (String) reader.read(xpaht_url_married, XPathConstants.STRING);
			System.out.println("married"+married);
			user.married = married;
			
			String preference = (String) reader.read(xpath_url_reference, XPathConstants.STRING);
			System.out.println("preference"+preference);
			user.preference = preference;
			
			String hate = (String) reader.read(xpath_url_hate, XPathConstants.STRING);
			System.out.println("hate"+hate);
			user.hate = hate;
			String job = (String) reader.read(xpath_url_job, XPathConstants.STRING);
			System.out.println("job"+job);
			user.job = job;
			
			String info = (String) reader.read(xpath_url_info, XPathConstants.STRING);
			System.out.println("info"+info);
			user.info = info;
			
			String friend_desired = (String) reader.read(xpath_url_friend_desired, XPathConstants.STRING);
			System.out.println("info"+friend_desired);
			user.friend_desired = friend_desired;
			KetBanDAO ketBanDAO = new KetBanDAO();
			user.create_date = new Date(Calendar.getInstance().getTimeInMillis());
			int id  =ketBanDAO.saveUser(user);
			if(id>0)
			{
				Album album = new Album();
				album.user_id = id;
				album.image_name = user.avatar;
				album.create_date = new Date(Calendar.getInstance().getTimeInMillis());
				ketBanDAO.saveAlbum(album);
			}
			Thread.sleep(100);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	

	
	public static void main(String[] args) {
		CrawlerKetBan crawlerKetBan  = new CrawlerKetBan();
		try {
			crawlerKetBan.baseUrl="http://kbol.sime.vn";
			crawlerKetBan.rewriterUrl="http://kbol.sime.vn";
			crawlerKetBan.urlRegex="act=detail&id=\\d+";
			int total_page = 153,i=1;
			while(i<=total_page)
			{
				String url = "http://kbol.sime.vn/default.aspx?act=2&PageIndex="+i;
				crawlerKetBan.collectionLink(url);
				Thread.sleep(1000);
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
