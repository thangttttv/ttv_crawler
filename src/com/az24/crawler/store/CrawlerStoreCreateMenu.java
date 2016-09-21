package com.az24.crawler.store;

import com.az24.dao.StoreCagegoryDAO;
import com.az24.dao.StoreDAO;

public class CrawlerStoreCreateMenu {
	public static void main(String[] args) throws Exception {
		CrawlerStore crawlerStore = new CrawlerStore();
		StoreCagegoryDAO cagegoryDAO = new StoreCagegoryDAO();
		CategoryExtractor categoryExtractor = new CategoryExtractor();
		//String urlGH = args[0];
		String urlGH = "http://vatgia.com/bacninh301";
		// Tao danh muc san pham	
		//String store_alias =args[1];
		String store_alias ="bacninh301";
		StoreDAO storeDAO = new StoreDAO();
		int store_id = storeDAO.checkStore(store_alias);
		categoryExtractor.getCategoryInAz24("az24_cate.xls");
		//int type = Integer.parseInt(args[2]);
		int type=1;
		switch (type) {
			case 1:
				categoryExtractor.extractCategoryGHType1(urlGH, store_id,true);
				break;
			case 2:
				categoryExtractor.extractCategoryGHType2(urlGH, store_id,true);
				break;
			case 3:
				categoryExtractor.extractCategoryGHType3(urlGH, store_id,true);
				break;
			case 4:
				categoryExtractor.extractCategoryGHType4(urlGH, store_id,true);
				break;
			default:
				break;
		}
		
		// Tao support online			
		crawlerStore.extractSupportGH(urlGH, store_id);
		crawlerStore.export_category(cagegoryDAO.getCategoryByStoreId(store_id), store_id,store_alias,type);
	}
}
