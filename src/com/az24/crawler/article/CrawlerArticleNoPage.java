package com.az24.crawler.article;

public class CrawlerArticleNoPage {
	public static void main(String args[]) {
		CrawlerArticleByPage crawlerArticleByPage = new CrawlerArticleByPage();
		crawlerArticleByPage.initialization(
				args[0],args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]),
				args[6],Integer.parseInt(args[7]),Integer.parseInt(args[8]),0);
		crawlerArticleByPage.collectionDataNoPage();

	}

}
