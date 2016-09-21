package com.ttv.scan;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.util.html.parser.XPathReader;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathConstants;

import org.apache.http.HttpResponse;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.az24.test.HttpClientUtil;
import com.az24.util.io.FileUtil;

public class ScanMobileMuaban {
	public void extractLinkMuaBanNET(String url) throws Exception {
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);
		//String html = getHTML(url);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		
		Document doc = reader.getDocument();
		CrawlerUtil.analysis(doc);
		
		int i = 1;		
		String xlinkVong = "//DIV[@class='mbn-box-list']/DIV[@class='mbn-box-list-content']";
		NodeList nodesChapter = (NodeList) reader.read(xlinkVong, XPathConstants.NODESET);
		int node_chapter_one_many = nodesChapter.getLength();
		
		System.out.println("node_chapter_one_many==> "+node_chapter_one_many);
		
		while (i <= node_chapter_one_many) {
			String link = (String) reader.read(xlinkVong + "["
					+ i + "]" + "/A[1]/@href", XPathConstants.STRING);
			System.out.println("link==> "+link);
			extractMobileMuaBanNet(link);
			i++;
		}
		
	}
	
	
	public void extractMobileMuaBanNet(String url) throws Exception {
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);
		//String html = getHTML(url);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		
		Document doc = reader.getDocument();
		CrawlerUtil.analysis(doc);
		
		String pattern = "\\d{4}\\.\\d{3}\\.\\d{3}|\\d{4}\\s\\d{3}\\s\\d{3}|\\d{10,11}|\\d{4}\\.\\d{2}\\.\\d{2}\\.\\d{3}|\\d{4}\\.\\d{3}\\.\\d{4}|\\d{3}\\s\\d{3}\\s\\d{4}|\\d{3}\\.\\d{3}\\.\\d{4}|\\d{5}\\.\\d{3}\\.\\d{3}|\\d{5}\\s\\d{3}\\s\\d{3}|\\d{4}\\s\\d{3}\\s\\d{4}|\\d{4}\\.\\d{3}\\.\\d{4}";
	    Pattern  r = Pattern.compile(pattern);
	    Matcher  m = r.matcher(html);
	    String mobile = "";
	      while (m.find()) {
	    	  Pattern r1 = Pattern.compile("^090|^093|^0122|^0126|^0121|^0128|^0120|^091|^094|^0123|^0125|^0127|^097|^098|^0168|^0169|^0166|^0167|^0165|^096|^095|^092|^0199|^0186");
		      Matcher m1 = r1.matcher( m.group(0) );
		      if(m1.find())
		      {
		    	  mobile =   m.group(0);
	         System.out.println("Found value: " + m.group(0) );
	        
	         if(mobile.compareTo("01276606303")==0||mobile.compareTo("0904604493")==0||mobile.compareTo("0933177955")==0||mobile.compareTo("0123.888.0123")==0||mobile.compareTo("0123.888.012")==0||mobile.compareTo("0129.233.3555")==0) continue;
	         	FileUtil.writeToFile("/home/crawler/mobile/mobile_muabannet.txt", mobile.trim()+"\r\n", true);
		      }
	      } 
	      
		String xPathMobile = "//SPAN[@data-bind='visible:ShowMobile']/text()";
	    mobile = (String) reader.read(xPathMobile, XPathConstants.STRING);
		System.out.println("MObile:"+mobile.trim());
		FileUtil.writeToFile("/home/crawler/mobile/mobile_muabannet.txt", mobile.trim()+"\r\n", true);
			
	}
	
	
	public static void main(String[] args) {
		ScanMobileMuaban scanMobileMuaban = new ScanMobileMuaban();
		List<String> listUrl = new ArrayList<String>();
		listUrl.add("https://muaban.net/ban-dat-toan-quoc-l0-c31?cp=");
		listUrl.add("https://muaban.net/ban-nha-can-ho-toan-quoc-l0-c32?cp=");
		listUrl.add("https://muaban.net/sang-nhuong-cua-hang-toan-quoc-l0-c33?cp=");
		listUrl.add("https://muaban.net/cho-thue-nha-dat-toan-quoc-l0-c34?cp=");
		listUrl.add("https://muaban.net/can-mua-nha-dat-toan-quoc-l0-c35?cp=");
		listUrl.add("https://muaban.net/can-thue-nha-dat-toan-quoc-l0-c36?cp=");
		listUrl.add("https://muaban.net/dich-vu-nha-dat-toan-quoc-l0-c37?cp=");
		listUrl.add("https://muaban.net/viec-tim-nguoi-toan-quoc-l0-c11?cp=");
		listUrl.add("https://muaban.net/nguoi-tim-viec-toan-quoc-l0-c12?cp=");
		listUrl.add("https://muaban.net/dich-vu-lao-dong-toan-quoc-l0-c13?cp=");
		listUrl.add("https://muaban.net/tuyen-sinh-du-hoc-dao-tao-toan-quoc-l0-c14?cp=");
		listUrl.add("https://muaban.net/ban-o-to-toan-quoc-l0-c41?cp=");
		listUrl.add("https://muaban.net/mua-o-to-toan-quoc-l0-c42?cp=");
		listUrl.add("https://muaban.net/cho-thue-o-to-toan-quoc-l0-c43?cp=");
		listUrl.add("https://muaban.net/can-thue-o-to-toan-quoc-l0-c44?cp=");
		listUrl.add("https://muaban.net/cac-phu-kien-o-to-toan-quoc-l0-c45?cp=");
		listUrl.add("https://muaban.net/dich-vu-sua-chua-toan-quoc-l0-c49?cp=");
		listUrl.add("https://muaban.net/xe-may-toan-quoc-l0-c5?cp=");
		listUrl.add("https://muaban.net/dich-vu-toan-quoc-l0-c9?cp=");
		listUrl.add("https://muaban.net/do-dien-tu-toan-quoc-l0-c6?cp=");
		listUrl.add("https://muaban.net/do-dien-may-toan-quoc-l0-c71?cp=");
		listUrl.add("https://muaban.net/do-nha-bep-toan-quoc-l0-c72?cp=");
		listUrl.add("https://muaban.net/noi-that-ngoai-that-toan-quoc-l0-c73?cp=");
		listUrl.add("https://muaban.net/do-dung-gia-dinh-toan-quoc-l0-c74?cp=");
		listUrl.add("https://muaban.net/ban-do-dung-van-phong-toan-quoc-l0-c75?cp=");
		listUrl.add("https://muaban.net/cho-thue-toan-quoc-l0-c76?cp=");
		listUrl.add("https://muaban.net/can-mua-toan-quoc-l0-c77?cp=");
		listUrl.add("https://muaban.net/so-thich-mat-hang-khac-toan-quoc-l0-c8?cp=");
		listUrl.add("https://muaban.net/quan-ao-toan-quoc-l0-c21?cp=");
		listUrl.add("https://muaban.net/giay-dep-tui-xach-toan-quoc-l0-c22?cp=");
		listUrl.add("https://muaban.net/my-pham-toan-quoc-l0-c23?cp=");
		listUrl.add("https://muaban.net/trang-suc-phu-kien-toan-quoc-l0-c24?cp=");
		listUrl.add("https://muaban.net/can-mua-toan-quoc-l0-c25?cp=");
		listUrl.add("https://muaban.net/cho-thue-toan-quoc-l0-c26?cp=");
		listUrl.add("https://muaban.net/doi-tac-cong-dong-toan-quoc-l0-ca?cp=");
		
		int j = 0;
		while(j<listUrl.size()){
		try {
			int i = 1;
			while(i<=200){
				scanMobileMuaban.extractLinkMuaBanNET(listUrl.get(j)+i);
				i++;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		j++;
		}
	}
	
}
