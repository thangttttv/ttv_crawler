package com.az24.crawler.article;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class CrawlerArticleByAjaxPhuyen {
	public static void main(String args[]) {
		CrawlerArticleByPage crawlerArticleByPage = new CrawlerArticleByPage();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("__ASYNCPOST", "true"));
		list.add(new BasicNameValuePair("__EVENTTARGET",
				"ctl00$ContentPlaceHolder1$ctlPaging"));
		list.add(new BasicNameValuePair(
						"__EVENTVALIDATION",
						"/wEWIAL+raDpAgLE5ML0AgLEw828AgLa+Kq1BgKB3p/aBwKv66w/AvTjsfMOApTGo7kEAqeF+bkBArCp1YMCAoio1pIIApXTqqwMAsP6yRECuZTu9wIClZOtmgMC48Ln5A0CmtWsvwYC47OtuQwCv7j4gAgC5/bdvQcCo8PcAQK9irybCwKVqrqxCALpp6+6CwLRvfOeAQLuuPSUDQKh896XCALB8N/qDwLmn+nlCALwjtiwBAK6p4jJAgK0xdrtAg0ivhw0j4VuhD8xMKO8IuP7PDLI"));
		list.add(new BasicNameValuePair("__SCROLLPOSITIONX", "0"));
		list.add(new BasicNameValuePair("__SCROLLPOSITIONY", "0"));
		list.add(new BasicNameValuePair(".Refresh", "5"));
		list.add(new BasicNameValuePair(".Title", "Phú Yên Online - Kinh tế"));
		list.add(new BasicNameValuePair("ctl00$scriptMan",
						"ctl00$ContentPlaceHolder1$uplNews|ctl00$ContentPlaceHolder1$ctlPaging"));
		crawlerArticleByPage.listParameter = list;
		//Phân trang Ajax
		crawlerArticleByPage.initialization(
				"/data/crawler/conf/urlsBaophuyenTH.xml",
				"/data/crawler/conf/beanBaophuyen.xml", 2, 1519, 0, 1519,
				"Phu-Yen", 0, 3,0);
		crawlerArticleByPage.collectionData();

	}
}
