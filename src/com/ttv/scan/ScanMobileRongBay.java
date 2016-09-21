package com.ttv.scan;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.util.html.parser.XPathReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathConstants;

import org.apache.http.HttpResponse;
import org.w3c.dom.NodeList;

import com.az24.test.Base64Coder;
import com.az24.test.HttpClientUtil;
import com.az24.util.io.FileUtil;

public class ScanMobileRongBay {
	
	public String getHTML(String urlToRead) {
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
				result += line+"\n";
			}
			rd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public int extractLinkMuaBanNET(String url){
		int node_chapter_one_many = 0;
		try {
			
			HttpClientImpl client = new HttpClientImpl();
			HttpResponse res = client.fetch(url);
			HttpClientUtil.printResponseHeaders(res);
			String html = HttpClientUtil.getResponseBody(res);
			XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
			
		//	Document doc = reader.getDocument();
		//	CrawlerUtil.analysis(doc);
			
			int i = 2;		
			String xlinkVong = "//div[@id='search_area']/table[1]/TBODY/tr";
			NodeList nodesChapter = (NodeList) reader.read(xlinkVong, XPathConstants.NODESET);
			node_chapter_one_many = nodesChapter.getLength();
			
			System.out.println("node_chapter_one_many==> "+node_chapter_one_many);
			String link = "";
			while (i <= node_chapter_one_many) {
				link = (String) reader.read(xlinkVong + "["
						+ i + "]" + "/td[1]/a[1]/@href", XPathConstants.STRING);
				System.out.println("link==> "+link);
				extractMobileMuaBanNet(link);
				Thread.sleep(300);
				i++;
			}
			
			link = Base64Coder.encodeString(link);
			
		} catch (Exception e) {
			System.out.println("extractLinkMuaBanNET Loi link--------->"+url);
			e.printStackTrace();
		}
	
		return node_chapter_one_many;
	}
	
	
	public void extractMobileMuaBanNet(String url)  {
		try {
			/*HttpClientImpl client = new HttpClientImpl();
			HttpResponse res = client.fetch(url);
			HttpClientUtil.printResponseHeaders(res);
			String html = HttpClientUtil.getResponseBody(res);*/
			String html = getHTML(url);
			//XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
			
	//		Document doc = reader.getDocument();
	//		CrawlerUtil.analysis(doc);
			
			String pattern = "\\d{4}\\.\\d{3}\\.\\d{3}|\\d{4}\\s\\d{3}\\s\\d{3}|\\d{10,11}|\\d{4}\\.\\d{2}\\.\\d{2}\\.\\d{3}|\\d{4}\\.\\d{3}\\.\\d{4}|\\d{3}\\s\\d{3}\\s\\d{4}|\\d{3}\\.\\d{3}\\.\\d{4}|\\d{5}\\.\\d{3}\\.\\d{3}|\\d{5}\\s\\d{3}\\s\\d{3}|\\d{4}\\s\\d{3}\\s\\d{4}|\\d{4}\\.\\d{3}\\.\\d{4}";
		    Pattern  r = Pattern.compile(pattern);
		    Matcher  m = r.matcher(html);
		    String mobile = "";
		    HashMap<String,String> mappMobile = new HashMap<String, String>();
		      while (m.find()) {
		    	  Pattern r1 = Pattern.compile("^090|^093|^0122|^0126|^0121|^0128|^0120|^091|^094|^0123|^0125|^0127|^097|^098|^0168|^0169|^0166|^0167|^0165|^096|^095|^092|^0199|^0186");
			      Matcher m1 = r1.matcher( m.group(0) );
			      if(m1.find())
			      {
			    	  mobile =   m.group(0);
		         System.out.println("Found value: " + m.group(0) );
		        
		         if(mobile.compareTo("01276606303")==0||mobile.compareTo("0904604493")==0||mobile.compareTo("0933177955")==0||mobile.compareTo("0123.888.0123")==0||mobile.compareTo("0123.888.012")==0||mobile.compareTo("0129.233.3555")==0) continue;
		         mappMobile.put(mobile.trim(), mobile.trim());
			      }
		      } 
		      
		   /* if(reader!=null){  
			String xPathMobile = "//div[@class='cl_333 user_phone font_14 font_700']/p/text()";
		    mobile = (String) reader.read(xPathMobile, XPathConstants.STRING);
			System.out.println("MObile:"+mobile.trim());
			mappMobile.put(mobile, mobile);
		    }*/
			System.out.println("MObile:--------------------->");
			for (String strM : mappMobile.keySet()) {
				System.out.println("MObile:"+strM.trim());
				FileUtil.writeToFile("/home/crawler/mobile/mobile_rongbay.txt", strM.trim()+"\r\n", true);
	        }
			
		} catch (Exception e) {
			System.out.println("extractMobileMuaBanNet Loi link--------->"+url);
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		ScanMobileRongBay scanMobileMuaban = new ScanMobileRongBay();
		List<String> listUrl = new ArrayList<String>();
		//listUrl.add("http://rongbay.com/Ha-Noi/Nha-mat-pho-Mua-Ban-nha-dat-c15-t639-trang");
		//listUrl.add("http://rongbay.com/Ha-Noi/Cua-hang-mat-bang-Mua-Ban-nha-dat-c15-t2-trang");
		//listUrl.add("http://rongbay.com/Ha-Noi/Khac-Mua-Ban-nha-dat-c15-t158-trang3");
		/*listUrl.add("http://rongbay.com/Ha-Noi/Nha-trong-ngo-Mua-Ban-nha-dat-c15-t4-trang");
		listUrl.add("http://rongbay.com/Ha-Noi/Nha-tap-the-Mua-Ban-nha-dat-c15-t6-trang");
		listUrl.add("http://rongbay.com/Ha-Noi/Dat-o-Mua-Ban-nha-dat-c15-t626-trang");
		listUrl.add("http://rongbay.com/Ha-Noi/Can-ho-chung-cu-Mua-Ban-nha-dat-c15-t5-trang");
		listUrl.add("http://rongbay.com/Ha-Noi/Biet-thu-lien-ke-phan-lo-Mua-Ban-nha-dat-c15-t1-trang");
		listUrl.add("http://rongbay.com/Ha-Noi/Xuong-trang-trai-Mua-Ban-nha-dat-c15-t3-trang");
		listUrl.add("http://rongbay.com/Ha-Noi/Nha-tap-the-Chung-cu-Cho-thue-nha-c272-t787-trang");
		listUrl.add("http://rongbay.com/Ha-Noi/Nha-tro-Phong-tro-Cho-thue-nha-c272-t788-trang");
		listUrl.add("http://rongbay.com/Ha-Noi/Khac-Cho-thue-nha-c272-t789-trang");
		listUrl.add("http://rongbay.com/Ha-Noi/Nha-rieng-nguyen-can-Cho-thue-nha-c272-t786-trang");
		listUrl.add("http://rongbay.com/Ha-Noi/Can-ho-Chung-cu-cao-cap-Cho-thue-nha-c272-t852-trang");
		listUrl.add("http://rongbay.com/Ha-Noi/O-ghep-Cho-thue-nha-c272-t638-trang");
		listUrl.add("http://rongbay.com/Ha-Noi/Nha-trong-ngo-hem-kiet-Cho-thue-nha-c272-t791-trang");*/
		
		listUrl.add("http://rongbay.com/Ha-Noi/Nha-cho-nguoi-nuoc-ngoai-thue-Cho-thue-nha-c272-t861-trang");
		listUrl.add("http://rongbay.com/Ha-Noi/Can-thue-Cho-thue-nha-c272-t243-trang");
		listUrl.add("http://rongbay.com/Ha-Noi/Vay-Dam-Thoi-trang-nu-c266-t807-trang");
		listUrl.add("http://rongbay.com/Ha-Noi/Do-lot-ngu-boi-Thoi-trang-nu-c266-t686-trang");
		listUrl.add("http://rongbay.com/Ha-Noi/Ao-khoac-Do-dong-Do-len-Thoi-trang-nu-c266-t685-trang");
		listUrl.add("http://rongbay.com/Ha-Noi/Vest-Blazer-Cong-so-Thoi-trang-nu-c266-t683-trang");
		listUrl.add("http://rongbay.com/Ha-Noi/Do-the-thao-Thoi-trang-nu-c266-t810-trang");
		
		listUrl.add("http://rongbay.com/Ha-Noi/Quan-Thoi-trang-nu-c266-t684-trang");
		listUrl.add("http://rongbay.com/Ha-Noi/So-mi-ao-phong-pull-Thoi-trang-nu-c266-t682-trang");
		listUrl.add("http://rongbay.com/Ha-Noi/Do-nu-khac-Thoi-trang-nu-c266-t699-trang");
		/*
		listUrl.add("https://muabannhanh.com/lo-vi-song-lo-nuong?page=");
		listUrl.add("https://muabannhanh.com/noi-com-dien-bep-dien-binh-dun-nuoc?page=");
		listUrl.add("https://muabannhanh.com/thoi-trang-nam?page=");
		listUrl.add("https://muabannhanh.com/thoi-trang-nu?page=");
		listUrl.add("https://muabannhanh.com/thoi-trang-tre-em?page=");
		listUrl.add("https://muabannhanh.com/thoi-trang-cong-so-dong-phuc?page=");
		listUrl.add("https://muabannhanh.com/thoi-trang-the-thao?page=");
		listUrl.add("https://muabannhanh.com/sach?page=");
		listUrl.add("https://muabannhanh.com/qua-tang-do-luu-niem?page=");
		listUrl.add("https://muabannhanh.com/hoa-tuoi?page=");
		listUrl.add("https://muabannhanh.com/do-choi-tre-em?page=");
		listUrl.add("https://muabannhanh.com/do-choi-hang-doc-la?page=");
		listUrl.add("https://muabannhanh.com/do-dung-gia-dinh?page=");
		listUrl.add("https://muabannhanh.com/do-dung-van-phong?page=");
		listUrl.add("https://muabannhanh.com/me-va-be?page=");
		listUrl.add("https://muabannhanh.com/the-thao-da-ngoai?page=");
		listUrl.add("https://muabannhanh.com/may-tap-the-duc?page=");
		listUrl.add("https://muabannhanh.com/thuc-pham-chuc-nang?page=");
		listUrl.add("https://muabannhanh.com/thuc-pham-dong-goi?page=");
		listUrl.add("https://muabannhanh.com/thuc-an-nhanh?page=");
		listUrl.add("https://muabannhanh.com/dac-san?page=");
		listUrl.add("https://muabannhanh.com/trai-cay-sinh-to-kem-xoi-che?page=");
		listUrl.add("https://muabannhanh.com/xay-dung-sua-chua-nha-thep-tien-che?page=");
		listUrl.add("https://muabannhanh.com/nguyen-phu-lieu-san-xuat?page=");
		listUrl.add("https://muabannhanh.com/vat-lieu-xay-dung?page=");
		listUrl.add("https://muabannhanh.com/vat-tu-vat-lieu-cong-nghiep?page=");
		listUrl.add("https://muabannhanh.com/thiet-bi-cong-nghiep-san-xuat?page=");
		listUrl.add("https://muabannhanh.com/ca-canh-thuy-sinh-thu-nuoi?page=");
		listUrl.add("https://muabannhanh.com/phan-bon-thuoc-tru-sau?page=");
		listUrl.add("https://muabannhanh.com/cay-giong-con-giong?page=");
		
		listUrl.add("https://muabannhanh.com/su-kien-khuyen-mai?page=");
		listUrl.add("https://muabannhanh.com/quay-phim-chup-anh?page=");
		listUrl.add("https://muabannhanh.com/thu-gian-giai-tri?page=");
		listUrl.add("https://muabannhanh.com/in-an-quang-cao?page=");
		listUrl.add("https://muabannhanh.com/thong-cao-bo-cao?page=");
		
		
		listUrl.add("https://muabannhanh.com/thiet-bi-am-thanh-loa-tai-nghe?page=");
		listUrl.add("https://muabannhanh.com/tivi-lcd?page=");
		listUrl.add("https://muabannhanh.com/may-anh-may-quay-phim?page=");
		listUrl.add("https://muabannhanh.com/dau-hd-tv-box-dvd?page=");
		listUrl.add("https://muabannhanh.com/nhac-cu?page=");
		
		
		listUrl.add("https://muabannhanh.com/tour-du-lich-trong-nuoc?page=");
		listUrl.add("https://muabannhanh.com/tour-du-lich-nuoc-ngoai?page=");
		listUrl.add("https://muabannhanh.com/ve-may-bay-noi-dia?page=");
		listUrl.add("https://muabannhanh.com/ve-may-bay-quoc-te?page=");
		
		
		listUrl.add("https://muabannhanh.com/nhan-vien-kinh-doanh?page=");
		listUrl.add("https://muabannhanh.com/viec-lam-ban-hang?page=");
		listUrl.add("https://muabannhanh.com/lao-dong-pho-thong?page=");
		listUrl.add("https://muabannhanh.com/viec-lam-bao-ve-ve-si-an-ninh?page=");
		listUrl.add("https://muabannhanh.com/giup-viec-gia-dinh?page=");
		
		
		listUrl.add("https://muabannhanh.com/dao-tao-ke-toan-dich-vu-ke-toan?page=");
		listUrl.add("https://muabannhanh.com/gia-su-day-nghe?page=");
		listUrl.add("https://muabannhanh.com/ky-nang?page=");
		listUrl.add("https://muabannhanh.com/do-hoa-kien-truc-xay-dung?page=");
		listUrl.add("https://muabannhanh.com/dao-tao-cong-nghe-thong-tin?page=");
		listUrl.add("https://muabannhanh.com/dich-vu-nha-dat?page=");
		listUrl.add("https://muabannhanh.com/camera-giam-sat-thiet-bi-an-ninh?page=");*/
		
		
		int j = 0;
		while(j<listUrl.size()){
			System.out.println("Chay Toi Link:---->"+j+" "+listUrl.get(j));
			try {
				int i = 1;
				while(i<=70){
					int soNut = scanMobileMuaban.extractLinkMuaBanNET(listUrl.get(j)+i+".html");
					if(soNut==0)break;
					i++;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		j++;
		}
	}
	
}
