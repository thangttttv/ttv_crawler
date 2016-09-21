package com.az24.crawler.ketban;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.DocumentAnalyzer;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.util.html.A;
import hdc.util.html.parser.DomWriter;
import hdc.util.html.parser.XPathReader;

import java.util.List;

import javax.xml.xpath.XPathConstants;

import org.apache.http.HttpResponse;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.az24.crawler.DownloadImage;
import com.az24.test.HttpClientUtil;

public class CrawlerKetBanNoi {

	public String  baseUrl = "";
	public String  rewriterUrl = "";
	public String  urlRegex="";
	
	public void collectionLink(String url) throws Exception {
		DocumentAnalyzer analyzer;
		int i = 1;
		try {
			HttpClientImpl client = new HttpClientImpl();
			HttpResponse res = client.fetch(url);
			String html = HttpClientUtil.getResponseBody(res);
			analyzer = new DocumentAnalyzer.DefaultDocumentAnayzer(baseUrl,	rewriterUrl);			
			List<A> list= null;		
			
			XPathReader reader = CrawlerUtil.createXPathReaderByData(html);			
			String xpath__tag= "//div[@id='divallonlineuser']/div[1]/div[2]/div[1]/div[3]/div";
			String xpath__tag_a= "/a[1]/@href";
			NodeList linkNodes = (NodeList) reader.read(xpath__tag, XPathConstants.NODESET);
			System.out.println(linkNodes.getLength());
			
			while(i<linkNodes.getLength())
			{
				String url_nick = (String) reader.read(xpath__tag+"["+i+"]"+xpath__tag_a, XPathConstants.STRING);
				String url_nick_info = "http://noi.vn/trang-ca-nhan-cua-thanh-vien/xem-ho-so-cua-thanh-vien"+url_nick+".noi";
				System.out.println(url_nick_info);
				String url_nick_album = "http://noi.vn/trang-ca-nhan-cua-thanh-vien/gallery"+url_nick+".noi";
				extractUser(url_nick_info);
				Thread.sleep(1000);
				System.out.println(url_nick);
				i++;
			}
			
		
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
	
	public void extractUser(String url) 
	{
		String xpath_url_file = "//TABLE[@id='thongtintvien']/TBODY[1]/TR[2]/TD[1]/IMG[1]/@src";
		String xpath_url_fullname = "html/body[1]/div[@id='mainContent']/div[@id='DivBody']/div[@id='DivBodyCenter']/div[@id='DivBodyMain']/form[@id='aspnetForm']/div[@id='mainwrap']/div[@id='ctl00_ContentPlaceHolder_pnlMainContent']/div[@id='Div2Column']/div[1]/div[1]/div[1]/div[1]/div[1]/span[@id='ctl00_ContentPlaceHolder_ctl00_UserProfile1_lbFirstname']/text()";
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
		
		
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		String html;
		try {
			html = HttpClientUtil.getResponseBody(res);
			XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
			String image = (String) reader.read(xpath_url_file, XPathConstants.STRING);
			System.out.println("image="+image);
			
			
			
			Node fullname = (Node) reader.read("//div[@id='Div2Column']", XPathConstants.NODE);
			DomWriter writer = new DomWriter();
			System.out.println("fullname="+writer.toXMLString(fullname));
			
			DownloadImage downloadImage = new com.az24.crawler.DownloadImage(this.baseUrl,this.rewriterUrl);
			//downloadImage.downloadImage(image, "D:/anh/"+fullname.trim()+".jpg");
			
			String address = (String) reader.read(xpath_url_address, XPathConstants.STRING);
			System.out.println("address="+address);
			
			String age = (String) reader.read(xpath_url_age, XPathConstants.STRING);
			System.out.println("age"+age);
			
			String sex = (String) reader.read(xpath_url_sex, XPathConstants.STRING);
			System.out.println("sex"+sex);
			String education = (String) reader.read(xpath_url_education, XPathConstants.STRING);
			System.out.println("education"+education);
			String height = (String) reader.read(xpath_url_height, XPathConstants.STRING);
			System.out.println("height"+height);
			String weight = (String) reader.read(xpath_url_weight, XPathConstants.STRING);
			System.out.println("weight"+weight);
			
			String religirous = (String) reader.read(xpath_url_religious, XPathConstants.STRING);
			System.out.println("religirous"+religirous);
			
			String reference = (String) reader.read(xpath_url_reference, XPathConstants.STRING);
			System.out.println("reference"+reference);
			
			String hate = (String) reader.read(xpath_url_hate, XPathConstants.STRING);
			System.out.println("hate"+hate);
			
			String job = (String) reader.read(xpath_url_job, XPathConstants.STRING);
			System.out.println("job"+job);
			
			String info = (String) reader.read(xpath_url_info, XPathConstants.STRING);
			System.out.println("info"+info);
			Thread.sleep(100);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	
	
	public static void main(String[] args) {
		CrawlerKetBanNoi crawlerKetBan  = new CrawlerKetBanNoi();
		try {
			crawlerKetBan.baseUrl="http://noi.vn";
			crawlerKetBan.rewriterUrl="http://noi.vn";
			crawlerKetBan.urlRegex="\\w+";
			int total_page = 153,i=1;
			while(i<=total_page)
			{
				String url = "http://noi.vn/tim-mot-nua/useronline.noi";
				crawlerKetBan.collectionLink(url);
				Thread.sleep(1000);
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
