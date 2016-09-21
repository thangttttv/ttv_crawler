package com.ttv.scan;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.util.html.parser.XPathReader;

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

public class ScanMobile5Giay {

	public int extractLinkMuaBanNET(String url){
		int node_chapter_one_many = 0;
		try {
			
			HttpClientImpl client = new HttpClientImpl();
			HttpResponse res = client.fetch(url);
			HttpClientUtil.printResponseHeaders(res);
			String html = HttpClientUtil.getResponseBody(res);
			XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
			
			//Document doc = reader.getDocument();
			//CrawlerUtil.analysis(doc);
			
			int i = 2;		
			String xlinkVong = "//OL[@class='discussionListItems']/LI";
			NodeList nodesChapter = (NodeList) reader.read(xlinkVong, XPathConstants.NODESET);
			node_chapter_one_many = nodesChapter.getLength();
			
			System.out.println("node_chapter_one_many==> "+node_chapter_one_many);
			String link = "";
			while (i <= node_chapter_one_many) {
				
				link = (String) reader.read(xlinkVong + "["
							+ i + "]" + "/DIV[2]/DIV[1]/H3[1]/A[@class='PreviewTooltip']/@href", XPathConstants.STRING);
				
				System.out.println("link==> "+link);
				extractMobileMuaBanNet("https://www.5giay.vn/"+link);
				Thread.sleep(100);
				i++;
			}
			
			link = Base64Coder.encodeString(link);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return node_chapter_one_many;
	}
	
	
	public void extractMobileMuaBanNet(String url)  {
		try {
			HttpClientImpl client = new HttpClientImpl();
			HttpResponse res = client.fetch(url);
			HttpClientUtil.printResponseHeaders(res);
			String html = HttpClientUtil.getResponseBody(res);
			//String html = getHTML(url);
			//XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
			
			//Document doc = reader.getDocument();
			//CrawlerUtil.analysis(doc);
			
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
		      
			System.out.println("MObile:--------------------->");
			for (String strM : mappMobile.keySet()) {
				System.out.println("MObile:"+strM.trim());
				FileUtil.writeToFile("/home/crawler/mobile/mobile_5giay.txt", strM.trim()+"\r\n", true);
	        }
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		ScanMobile5Giay scanMobileMuaban = new ScanMobile5Giay();
		List<String> listUrl = new ArrayList<String>();
		listUrl.add("https://www.5giay.vn/thoi-trang-nam/page-");
		listUrl.add("https://www.5giay.vn/thoi-trang-nu/page-");
		listUrl.add("https://www.5giay.vn/forums/giay-dep-balo-tui-xach.97/page-");
		listUrl.add("https://www.5giay.vn/dong-ho-phu-kien-thoi-trang/page-");
		listUrl.add("https://www.5giay.vn/forums/nuoc-hoa-my-pham.109/page-");
		listUrl.add("https://www.5giay.vn/forums/me-va-be.138/page-");
		listUrl.add("https://www.5giay.vn/mobile/page-");
		listUrl.add("https://www.5giay.vn/iphone/page-");
		listUrl.add("https://www.5giay.vn/phu-kien-dien-thoai/page-");
		listUrl.add("https://www.5giay.vn/sim-so/page-");
		listUrl.add("https://www.5giay.vn/forums/may-tinh-bang.145/page-");
		listUrl.add("https://www.5giay.vn/forums/computer.14/page-");
		listUrl.add("https://www.5giay.vn/laptop/page-");
		listUrl.add("https://www.5giay.vn/phu-kien-do-choi-laptop/page-");
		listUrl.add("https://www.5giay.vn/forums/dich-vu-sua-chua-bao-tri.158/page-");
		/*listUrl.add("http://rongbay.com/Ha-Noi/Nha-trong-ngo-hem-kiet-Cho-thue-nha-c272-t791-trang");
		listUrl.add("http://rongbay.com/Ha-Noi/Nha-cho-nguoi-nuoc-ngoai-thue-Cho-thue-nha-c272-t861-trang");
		listUrl.add("http://rongbay.com/Ha-Noi/Can-thue-Cho-thue-nha-c272-t243-trang");
		listUrl.add("http://rongbay.com/Ha-Noi/Vay-Dam-Thoi-trang-nu-c266-t807-trang");
		listUrl.add("http://rongbay.com/Ha-Noi/Do-lot-ngu-boi-Thoi-trang-nu-c266-t686-trang");
		listUrl.add("http://rongbay.com/Ha-Noi/Ao-khoac-Do-dong-Do-len-Thoi-trang-nu-c266-t685-trang");
		listUrl.add("http://rongbay.com/Ha-Noi/Vest-Blazer-Cong-so-Thoi-trang-nu-c266-t683-trang");
		listUrl.add("http://rongbay.com/Ha-Noi/Do-the-thao-Thoi-trang-nu-c266-t810-trang");
		
		listUrl.add("http://rongbay.com/Ha-Noi/Quan-Thoi-trang-nu-c266-t684-trang");
		listUrl.add("http://rongbay.com/Ha-Noi/So-mi-ao-phong-pull-Thoi-trang-nu-c266-t682-trang");
		listUrl.add("http://rongbay.com/Ha-Noi/Do-nu-khac-Thoi-trang-nu-c266-t699-trang");*/
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
				int i = 2;
				while(i<=455){
					int soNut = scanMobileMuaban.extractLinkMuaBanNET(listUrl.get(j)+i);
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
