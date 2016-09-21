package com.az24.crawler.article;


public class CrawlerArticleByJson {
	public static void main(String args[]) {
		CrawlerArticleByPage crawlerArticleByPage = new CrawlerArticleByPage();
		//Json
		crawlerArticleByPage.initialization(
				args[0],args[1], 5, Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]),
				args[5], 0, 1,0);
		/*crawlerArticleByPage.initialization(
				"D:/categorys2/urlsPhapluatvnTH.xml","D:/categorys2/beanPhapluatvnTH.xml", 5, 23, 1, 0,
				"Toan-Quoc", 0, 1);*/
		crawlerArticleByPage.collectionDataFrom(12);

	}
}
