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

public class ScanMobileVnexpress {
	public void extractLinkVnexpress(String url) throws Exception {
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);
		//String html = getHTML(url);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		
		Document doc = reader.getDocument();
		CrawlerUtil.analysis(doc);
		
		int i = 1;		
		String xlinkVong = "//UL[@id='list-folder']/LI";
		NodeList nodesChapter = (NodeList) reader.read(xlinkVong, XPathConstants.NODESET);
		int node_chapter_one_many = nodesChapter.getLength();
		
		System.out.println("node_chapter_one_many==> "+node_chapter_one_many);
		
		while (i <= node_chapter_one_many) {
			String link = (String) reader.read(xlinkVong + "["
					+ i + "]" + "/A[1]/@href", XPathConstants.STRING);
			System.out.println("link==> "+link);
			extractMobileMuaBanNet("http://raovat.vnexpress.net"+link);
			i++;
		}
		
	}
	
	
	public void extractMobileMuaBanNet(String url) throws Exception {
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);
		
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
	        // FileUtil.writeToFile("C:/Projects/mobile_express.txt", mobile.trim()+"\r\n", true);
	         FileUtil.writeToFile("/home/crawler/mobile/mobile_express.txt", mobile.trim()+"\r\n", true);
		      }
	      } 
			
	}
	
	
	public static void main(String[] args) {
		ScanMobileVnexpress scanMobileMuaban = new ScanMobileVnexpress();
		List<String> listUrl = new ArrayList<String>();
		listUrl.add("http://raovat.vnexpress.net/mua-ban-nha-dat/page/");
		listUrl.add("http://raovat.vnexpress.net/oto/page/");
		listUrl.add("http://raovat.vnexpress.net/xe-may-xe-dap/page/");
		listUrl.add("http://raovat.vnexpress.net/thue-nha-dat/page/");
		listUrl.add("http://raovat.vnexpress.net/tuyen-sinh-tuyen-dung/page/");
		listUrl.add("http://raovat.vnexpress.net/dien-thoai-sim/page/");
		listUrl.add("http://raovat.vnexpress.net/pc-laptop/page/");
		listUrl.add("http://raovat.vnexpress.net/dien-tu-ky-thuat-so/page/");
		listUrl.add("http://raovat.vnexpress.net/thoi-trang-lam-dep/page/");
		listUrl.add("http://raovat.vnexpress.net/am-thuc-du-lich/page/");
		listUrl.add("http://raovat.vnexpress.net/dich-vu/page/");
		listUrl.add("http://raovat.vnexpress.net/khac/page/");
		
		
		int j = 0;
		while(j<listUrl.size()){
			try {
				int i = 1;
				while(i<=20){
					scanMobileMuaban.extractLinkVnexpress(listUrl.get(j)+i+".html");
					i++;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		j++;
		}
	}
}
