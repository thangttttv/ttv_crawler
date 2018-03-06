package com.vtc.crawler;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.util.html.parser.XPathReader;
import hdc.util.text.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.xpath.XPathConstants;

import org.apache.http.HttpResponse;
import org.w3c.dom.NodeList;

import com.az24.dao.VietlotoDAO;
import com.az24.test.HttpClientUtil;

public class CrawlerXoSoMega645 extends CrawlerDailyXoSo {
	
	public void crawlerXoSoMeGa() throws Exception {
		String url ="http://www.minhngoc.com.vn/ket-qua-xo-so/mega-6x45.html";
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		String html = HttpClientUtil.getResponseBody(res);
		
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		
		
	//	CrawlerUtil.analysis(reader.getDocument(), "");
		
		String xpath__result= "//div[@class='boxkqxsdientoan']";
		NodeList nodeResult = (NodeList) reader.read(xpath__result, XPathConstants.NODESET);
		
		int i = nodeResult.getLength();
		VietlotoDAO vietlotoDAO = new VietlotoDAO();
		while(i>=1){
			xpath__result= "//div[@class='boxkqxsdientoan']["+i+"]/div[1]/div[1]/div[1]/div[1]/div[1]/p";
			String ngay_quay = (String) reader.read(xpath__result, XPathConstants.STRING);
			
			ngay_quay = ngay_quay.trim();
			String s_nga_quay[] = ngay_quay.split("\\|");
			System.out.println(i+"-"+ngay_quay.trim());
			System.out.println(s_nga_quay.length);
			System.out.println(s_nga_quay[0]);
			System.out.println(s_nga_quay[1]);
			String ky_ve = s_nga_quay[0].replaceAll("\\s", "");
			 ky_ve = ky_ve.replaceAll(".*Kỳvé:", "");
			String ngay_mo = s_nga_quay[1].replaceAll("Ngày quay thưởng ", "").trim();
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
			Date date = sdf.parse(ngay_mo);
			sdf = new SimpleDateFormat("yyyy-M-dd");
			ngay_mo = sdf.format(date);
			
			
			
			xpath__result= "//div[@class='boxkqxsdientoan']["+i+"]/div[1]/div[1]/div[1]/div[1]/div[1]/ul/li[1]";
			String so_1 = (String) reader.read(xpath__result, XPathConstants.STRING);
			xpath__result= "//div[@class='boxkqxsdientoan']["+i+"]/div[1]/div[1]/div[1]/div[1]/div[1]/ul/li[2]";
			String so_2 = (String) reader.read(xpath__result, XPathConstants.STRING);
			xpath__result= "//div[@class='boxkqxsdientoan']["+i+"]/div[1]/div[1]/div[1]/div[1]/div[1]/ul/li[3]";
			String so_3 = (String) reader.read(xpath__result, XPathConstants.STRING);
			xpath__result= "//div[@class='boxkqxsdientoan']["+i+"]/div[1]/div[1]/div[1]/div[1]/div[1]/ul/li[4]";
			String so_4 = (String) reader.read(xpath__result, XPathConstants.STRING);
			xpath__result= "//div[@class='boxkqxsdientoan']["+i+"]/div[1]/div[1]/div[1]/div[1]/div[1]/ul/li[5]";
			String so_5 = (String) reader.read(xpath__result, XPathConstants.STRING);
			xpath__result= "//div[@class='boxkqxsdientoan']["+i+"]/div[1]/div[1]/div[1]/div[1]/div[1]/ul/li[6]";
			String so_6 = (String) reader.read(xpath__result, XPathConstants.STRING);
			/*System.out.println(so_1);
			System.out.println(so_2);
			System.out.println(so_3);
			System.out.println(so_4);
			System.out.println(so_5);
			System.out.println(so_6);*/
			
			
			xpath__result= "//div[@class='boxkqxsdientoan']["+i+"]/div[1]/div[1]/div[1]/div[1]/div[2]/table[1]/tbody/tr/td/table/tbody/tr/td[1]";
			String Jackpot = (String) reader.read(xpath__result, XPathConstants.STRING);
			System.out.println(Jackpot.trim());
			
			xpath__result= "//div[@class='boxkqxsdientoan']["+i+"]/div[1]/div[1]/div[1]/div[1]/div[2]/table[1]/tbody/tr/td/table/tbody/tr/td[3]";
			String jackpot_so_giai = (String) reader.read(xpath__result, XPathConstants.STRING);
			System.out.println(jackpot_so_giai.trim());
			xpath__result= "//div[@class='boxkqxsdientoan']["+i+"]/div[1]/div[1]/div[1]/div[1]/div[2]/table[1]/tbody/tr/td/table/tbody/tr/td[4]";
			String jackpot_gia_tri = (String) reader.read(xpath__result, XPathConstants.STRING);
			System.out.println(jackpot_gia_tri.trim());
			
			xpath__result= "//div[@class='boxkqxsdientoan']["+i+"]/div[1]/div[1]/div[1]/div[1]/div[2]/table[1]/tbody/tr/td/table/tbody/tr[2]/td[1]";
			 Jackpot = (String) reader.read(xpath__result, XPathConstants.STRING);
			System.out.println(Jackpot.trim());
			
			xpath__result= "//div[@class='boxkqxsdientoan']["+i+"]/div[1]/div[1]/div[1]/div[1]/div[2]/table[1]/tbody/tr/td/table/tbody/tr[2]/td[3]";
			String giainhat_so_giai = (String) reader.read(xpath__result, XPathConstants.STRING);
			System.out.println(giainhat_so_giai.trim());
			xpath__result= "//div[@class='boxkqxsdientoan']["+i+"]/div[1]/div[1]/div[1]/div[1]/div[2]/table[1]/tbody/tr/td/table/tbody/tr[2]/td[4]";
			String giainhat_gia_tri = (String) reader.read(xpath__result, XPathConstants.STRING);
			System.out.println(giainhat_gia_tri.trim());
			
			xpath__result= "//div[@class='boxkqxsdientoan']["+i+"]/div[1]/div[1]/div[1]/div[1]/div[2]/table[1]/tbody/tr/td/table/tbody/tr[3]/td[1]";
			 Jackpot = (String) reader.read(xpath__result, XPathConstants.STRING);
			System.out.println(Jackpot.trim());
			
			xpath__result= "//div[@class='boxkqxsdientoan']["+i+"]/div[1]/div[1]/div[1]/div[1]/div[2]/table[1]/tbody/tr/td/table/tbody/tr[3]/td[3]";
			String giainhi_so_giai = (String) reader.read(xpath__result, XPathConstants.STRING);
			System.out.println(giainhi_so_giai.trim());
			xpath__result= "//div[@class='boxkqxsdientoan']["+i+"]/div[1]/div[1]/div[1]/div[1]/div[2]/table[1]/tbody/tr/td/table/tbody/tr[3]/td[4]";
			String giainhi_gia_tri = (String) reader.read(xpath__result, XPathConstants.STRING);
			System.out.println(giainhi_gia_tri.trim());
			
			xpath__result= "//div[@class='boxkqxsdientoan']["+i+"]/div[1]/div[1]/div[1]/div[1]/div[2]/table[1]/tbody/tr/td/table/tbody/tr[4]/td[1]";
			 Jackpot = (String) reader.read(xpath__result, XPathConstants.STRING);
			System.out.println(Jackpot.trim());
			
			xpath__result= "//div[@class='boxkqxsdientoan']["+i+"]/div[1]/div[1]/div[1]/div[1]/div[2]/table[1]/tbody/tr/td/table/tbody/tr[4]/td[3]";
			String giaiba_so_giai = (String) reader.read(xpath__result, XPathConstants.STRING);
			System.out.println(giaiba_so_giai.trim());
			xpath__result= "//div[@class='boxkqxsdientoan']["+i+"]/div[1]/div[1]/div[1]/div[1]/div[2]/table[1]/tbody/tr/td/table/tbody/tr[4]/td[4]";
			String giaiba_gia_tri = (String) reader.read(xpath__result, XPathConstants.STRING);
			System.out.println(giaiba_gia_tri.trim());
			int id = vietlotoDAO.checkXoSo645(ngay_mo);
			
			jackpot_so_giai = jackpot_so_giai.trim();
			jackpot_so_giai = StringUtil.isEmpty(jackpot_so_giai)?"0":jackpot_so_giai;
			
			giainhat_so_giai = giainhat_so_giai.trim();
			giainhat_so_giai = StringUtil.isEmpty(giainhat_so_giai)?"0":giainhat_so_giai;
			
			
			
			giainhi_so_giai = giainhi_so_giai.trim();
			giainhi_so_giai = StringUtil.isEmpty(giainhi_so_giai)?"0":giainhi_so_giai;
			
			giaiba_so_giai = giaiba_so_giai.trim();
			giaiba_so_giai = StringUtil.isEmpty(giaiba_so_giai)?"0":giaiba_so_giai;
			
			if(id==0){
			vietlotoDAO.saveXoSo645(ky_ve, ngay_mo, so_1.trim(), so_2.trim(), so_3.trim()
					, so_4.trim(), so_5.trim(), so_6.trim()
					, Integer.parseInt(jackpot_so_giai.replaceAll("\\D","").trim()), Double.parseDouble(jackpot_gia_tri.replaceAll("\\D","").trim())
					,Integer.parseInt( giainhat_so_giai.replaceAll("\\D","").trim()), Double.parseDouble(giainhat_gia_tri.replaceAll("\\D","").trim())
					,Integer.parseInt( giainhi_so_giai.replaceAll("\\D","").trim()),Double.parseDouble( giainhi_gia_tri.replaceAll("\\D","").trim())
					,Integer.parseInt( giaiba_so_giai.replaceAll("\\D","").trim()),Double.parseDouble( giaiba_gia_tri.replaceAll("\\D","").trim()));
			}else{
				vietlotoDAO.updateXoSo645(id, ky_ve, ngay_mo, so_1.trim(), so_2.trim(), so_3.trim()
						, so_4.trim(), so_5.trim(), so_6.trim(), Integer.parseInt(jackpot_so_giai.replaceAll("\\D","").trim()), Double.parseDouble(jackpot_gia_tri.replaceAll("\\D","").trim())
						,Integer.parseInt( giainhat_so_giai.replaceAll("\\D","").trim()), Double.parseDouble(giainhat_gia_tri.replaceAll("\\D","").trim())
						,Integer.parseInt( giainhi_so_giai.replaceAll("\\D","").trim()),Double.parseDouble( giainhi_gia_tri.replaceAll("\\D","").trim())
						,Integer.parseInt( giaiba_so_giai.replaceAll("\\D","").trim()),Double.parseDouble( giaiba_gia_tri.replaceAll("\\D","").trim()));
			}
			
			i--;
		}
		
		
	}
	
	public static void main(String[] args) {
		CrawlerDailyXoSoTrucTiep.filePID = "./conf/pidMega.txt";
		//if(CrawlerDailyXoSoMega.existPID()) return; else CrawlerDailyXoSoMega.createPID();
		CrawlerXoSoMega645 xoSoMega = new CrawlerXoSoMega645();
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
