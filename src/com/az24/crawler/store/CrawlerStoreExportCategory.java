package com.az24.crawler.store;

class CrawlerStoreExportCategory {
	public static void main(String[] args) {
		StoreProductExtract productExtract = new StoreProductExtract();
		try {
			productExtract.export_category("thegioinem1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
