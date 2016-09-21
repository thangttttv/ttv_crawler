package com.az24.crawler.article;

public class CrawlerArticleByPageCanTho {
	public static void main(String args[]) {
		 CrawlerArticleByPage crawlerArticleByPage = new CrawlerArticleByPage();
		 String urls = args[0];
		 String bean = args[1];
		 int fetch = Integer.parseInt(args[2]);
		 int source = Integer.parseInt(args[3]);
		 int download = Integer.parseInt(args[4]);
		 int province_id = Integer.parseInt(args[5]);
		 String province = args[6];
		 int type_image = Integer.parseInt(args[7]);
		 int type_get_html = Integer.parseInt(args[8]);
		 int type_date = Integer.parseInt(args[9]);
		 crawlerArticleByPage.charset = "ISO-8859-1";
		 crawlerArticleByPage.initialization(urls,bean,
				 fetch,source,download,province_id,province,type_image,type_get_html,type_date);
		 crawlerArticleByPage.collectionData();
	}
}
