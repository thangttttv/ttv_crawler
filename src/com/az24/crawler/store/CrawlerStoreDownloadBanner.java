package com.az24.crawler.store;

import com.az24.dao.StoreConfigDAO;
import com.az24.dao.StoreDAO;

public class CrawlerStoreDownloadBanner {
	public static void main(String[] args) {
		StoreDAO storeDAO = new StoreDAO();
		CrawlerStore crawlerStore = new CrawlerStore();
		StoreConfigDAO storeConfigDAO = new StoreConfigDAO();
		crawlerStore.downloadImage("http://skinfoodhanquoc.com/Thumbnail.ashx?img=sites/2352/data/images/2011/11/4304250massage%20honey.jpg&width=500&height=500"
				, "d:\\20honey.jpg");
		storeConfigDAO.updateStore(storeDAO.checkStore("giaydantuong"), "/remcuahuyhoang/2012/0222/giaydantuong.swf", 989, 211);
	}
}
