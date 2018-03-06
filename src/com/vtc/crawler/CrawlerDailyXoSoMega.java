package com.vtc.crawler;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.util.html.parser.XPathReader;

import javax.xml.xpath.XPathConstants;

import org.apache.http.HttpResponse;
import org.w3c.dom.NodeList;

import com.az24.dao.W10HDAO;
import com.az24.test.HttpClientUtil;

public class CrawlerDailyXoSoMega  extends CrawlerDailyXoSo {
	
	public void crawlerXoSoMeGa() throws Exception {
		String url ="http://xoso.me/ket-qua-xo-so-tu-chon-viettlot-mega-6-45-ngay-hom-nay.html";
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		String html = HttpClientUtil.getResponseBody(res);
		
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		
		
		CrawlerUtil.analysis(reader.getDocument(), "");
		
		String xpath__result= "//UL[@class='results']/LI";
		NodeList nodeResult = (NodeList) reader.read(xpath__result, XPathConstants.NODESET);
		
		int i = 1;
		W10HDAO w10hdao = new W10HDAO();
		while(i<=nodeResult.getLength()){
			xpath__result= "//UL[@class='results']/LI["+i+"]/DIV/DIV/TIME/text()";
			String ngay_quay = (String) reader.read(xpath__result, XPathConstants.STRING);
			
		    String s_n_q[] = ngay_quay.split("-");
		    
		    ngay_quay = s_n_q[2]+"-"+s_n_q[1]+"-"+s_n_q[0];
		    System.out.println(ngay_quay);
		    
			xpath__result= "//UL[@class='results']/LI["+i+"]/DIV/TABLE[1]/TBODY[1]/TR[1]/TD[1]/I[1]";
			String so_1 = (String) reader.read(xpath__result, XPathConstants.STRING);
			xpath__result= "//UL[@class='results']/LI["+i+"]/DIV/TABLE[1]/TBODY[1]/TR[1]/TD[2]/I[1]";
			String so_2 = (String) reader.read(xpath__result, XPathConstants.STRING);
			xpath__result= "//UL[@class='results']/LI["+i+"]/DIV/TABLE[1]/TBODY[1]/TR[1]/TD[3]/I[1]";
			String so_3 = (String) reader.read(xpath__result, XPathConstants.STRING);
			xpath__result= "//UL[@class='results']/LI["+i+"]/DIV/TABLE[1]/TBODY[1]/TR[1]/TD[4]/I[1]";
			String so_4 = (String) reader.read(xpath__result, XPathConstants.STRING);
			xpath__result= "//UL[@class='results']/LI["+i+"]/DIV/TABLE[1]/TBODY[1]/TR[1]/TD[5]/I[1]";
			String so_5 = (String) reader.read(xpath__result, XPathConstants.STRING);
			xpath__result= "//UL[@class='results']/LI["+i+"]/DIV/TABLE[1]/TBODY[1]/TR[1]/TD[6]/I[1]";
			String so_6 = (String) reader.read(xpath__result, XPathConstants.STRING);
			
			if(w10hdao.checkXoSoMega(ngay_quay)==0){
				w10hdao.saveXoSoMega(ngay_quay,Integer.parseInt(so_1) , Integer.parseInt(so_2) , Integer.parseInt(so_3) 
						, Integer.parseInt(so_4) , Integer.parseInt(so_5) , Integer.parseInt(so_6) );
			}
			System.out.println(ngay_quay);
			i++;
		}
		
		
	}
	
	public static void main(String[] args) {
		CrawlerDailyXoSoTrucTiep.filePID = "./conf/pidMega.txt";
		//if(CrawlerDailyXoSoMega.existPID()) return; else CrawlerDailyXoSoMega.createPID();
		CrawlerDailyXoSoMega xoSoMega = new CrawlerDailyXoSoMega();
		try {
			xoSoMega.crawlerXoSoMeGa();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			CrawlerDailyXoSoKQVS.deletePID();
		}
		
	}
	
	
}
