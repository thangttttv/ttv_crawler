package com.ttv.football;


import hdc.crawler.CrawlerUtil;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.crawler.fetcher.HttpClientUtil;
import hdc.util.html.parser.XPathReader;
import hdc.util.text.StringUtil;

import javax.xml.xpath.XPathConstants;

import org.apache.http.HttpResponse;
import org.w3c.dom.NodeList;

import com.az24.crawler.model.FBChart;
import com.az24.crawler.model.FBClub;
import com.az24.dao.FootBallDAO;

public class BangXepHangL2Crawler {
	
	public static void crawlerBangXepHang(String url,int cup_id) throws Exception {
			HttpClientImpl client = new HttpClientImpl();
		
		 	HttpResponse res = client.fetch(url);
			HttpClientUtil.printResponseHeaders(res);
			String html = HttpClientUtil.getResponseBody(res);
			
		
			XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
			CrawlerUtil.analysis(reader.getDocument());

			String xpath_node_content = "/html/body/center/div/div[5]/div[5]/table/TBODY/tr";

			NodeList nodes = (NodeList) reader.read(xpath_node_content,
					XPathConstants.NODESET);
			if (nodes != null) {
				int node_one_many = nodes.getLength();
				
				
				if(node_one_many==0) {////div[@class='menu_trd']/a
					xpath_node_content = "//div[@class='New_col-centre']/div[5]/table/TBODY/tr";
					 nodes = (NodeList) reader.read(xpath_node_content,
							XPathConstants.NODESET);
					 node_one_many = nodes.getLength();
				}
				
				if(node_one_many==0) {
					xpath_node_content = "/html/body/center/div/div[5]/div[6]/table/TBODY/tr";
					 nodes = (NodeList) reader.read(xpath_node_content,
							XPathConstants.NODESET);
					 node_one_many = nodes.getLength();
				}
				
				int i = 2;String group_name ="";

				while (i <= node_one_many) {
					String colspan  = (String) reader.read(xpath_node_content + "[" + i + "]"
							+ "/td[1]/@colspan", XPathConstants.STRING);
					
					if(!StringUtil.isEmpty(colspan)){
						String t_group_name =  (String) reader.read(xpath_node_content + "[" + i + "]"
								+ "/td[1]/span/text()", XPathConstants.STRING);
						if(t_group_name!="")group_name =t_group_name;

					}
					
					String stt  = (String) reader.read(xpath_node_content + "[" + i + "]"
							+ "/td[1]/text()", XPathConstants.STRING);
					String doi  = (String) reader.read(xpath_node_content + "[" + i + "]"
							+ "/td[2]", XPathConstants.STRING);
					String doiUrl  = (String) reader.read(xpath_node_content + "[" + i + "]"
							+ "/td[2]/a/@href", XPathConstants.STRING);
					String tran  = (String) reader.read(xpath_node_content + "[" + i + "]"
							+ "/td[3]", XPathConstants.STRING);
					String thang  = (String) reader.read(xpath_node_content + "[" + i + "]"
							+ "/td[4]", XPathConstants.STRING);	
					String hoa  = (String) reader.read(xpath_node_content + "[" + i + "]"
							+ "/td[5]", XPathConstants.STRING);
					String bai  = (String) reader.read(xpath_node_content + "[" + i + "]"
							+ "/td[6]", XPathConstants.STRING);
					String bt  = (String) reader.read(xpath_node_content + "[" + i + "]"
							+ "/td[7]", XPathConstants.STRING);
					String bb  = (String) reader.read(xpath_node_content + "[" + i + "]"
							+ "/td[8]", XPathConstants.STRING);
					String hs  = (String) reader.read(xpath_node_content + "[" + i + "]"
							+ "/td[9]", XPathConstants.STRING);
					String diem  = (String) reader.read(xpath_node_content + "[" + i + "]"
							+ "/td[10]", XPathConstants.STRING);
					if("".compareToIgnoreCase(stt.trim())==0) {i++;continue;	}
					
					FBChart fbchart = new FBChart();
					fbchart.rate = Integer.parseInt(stt.trim());
					fbchart.cup_id = cup_id;
					fbchart.so_tran = Integer.parseInt(tran.trim());
					fbchart.so_tran_thang = Integer.parseInt(thang.trim());
					fbchart.so_tran_hoa = Integer.parseInt(hoa.trim());
					fbchart.so_tran_bai = Integer.parseInt(bai.trim());
					fbchart.ban_thang = Integer.parseInt(bt.trim());
					fbchart.ban_bai = Integer.parseInt(bb.trim());
					fbchart.hieu_so = Integer.parseInt(hs.trim());
					fbchart.diem = Integer.parseInt(diem.trim());
					doi = doi.trim().replaceAll("\\s+", " ");
					fbchart.name = doi.trim();
					fbchart.season = "2016";
					fbchart.cup_group = group_name;
					if("Uzbekistan U19".equalsIgnoreCase(doi)){
						System.out.println(doiUrl);
					}
					FBClub club  = FootBallDAO.getClub(doi.trim().toUpperCase());
					int club_id = 0;
					
					if(club==null) {club_id = BangXepHangCrawler.crawlerClup(doiUrl);
						club  = FootBallDAO.getClubByID(club_id);
					} else club_id = club.id;
					
					if(club!=null){
						fbchart.club_id =club_id;
						fbchart.code = club.code;
						int chart_id = FootBallDAO.checkChart(cup_id,club_id, fbchart.season);
						if(chart_id==0)
							FootBallDAO.saveChart(fbchart);
						else{
							fbchart.id = chart_id;
							FootBallDAO.updateChart(fbchart);
						}
							
					}
					
					System.out.println("----------------");
					System.out.println(stt.trim()+"-"+doi.trim()+"---"+tran.trim()+"-"+thang.trim());
					System.out.println("----------------");
					i++;
					}
					
			}
	}
	
	public static void main(String args[]) throws Exception{
		//BangXepHangL2Crawler.crawlerBangXepHang("http://bongda.wap.vn/bang-xep-hang-vong-loai-u19-chau-a-6526.html",367);
		BangXepHangL2Crawler.crawlerBangXepHang("http://bongda.wap.vn/bang-xep-hang-copa-america-2016-cmx.html",247);
		//BangXepHangL2Crawler.crawlerBangXepHang("http://bongda.wap.vn/bang-xep-hang-cup-c2-chau-au-c2.html",7);
		//BangXepHangL2Crawler.crawlerBangXepHang("http://bongda.wap.vn/bang-xep-hang-vlwc-kv-chau-a-1088.html",208);
	}
}
