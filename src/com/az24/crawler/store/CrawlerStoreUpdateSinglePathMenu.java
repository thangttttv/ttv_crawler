package com.az24.crawler.store;

import java.io.File;

import com.az24.dao.StoreCagegoryDAO;

public class CrawlerStoreUpdateSinglePathMenu {
	public static void main(String[] args) throws Exception {
		CrawlerStore crawlerStore = new CrawlerStore();
		StoreCagegoryDAO cagegoryDAO = new StoreCagegoryDAO();
		// Tao danh muc san pham
		//String store_alias ="artflower";
		//StoreDAO storeDAO = new StoreDAO();
		//int store_id = storeDAO.checkStore(store_alias);
		//cagegoryDAO.update(crawlerStore.readExcelCategory("d:\\categorys\\store_category_"+store_alias+"_"+store_id+".xls"));
		File file = new File("C:/Users/Tran The Thang/Desktop/500 GH/12_12_2011/");
		if(file.isDirectory())
		{
			for (File child : file.listFiles()) {
				System.out.println("Filename-------------->"+child.getName());
				if(child.isFile())
				{
					cagegoryDAO.updateSinglePath(crawlerStore.readExcelCategory("C:/Users/Tran The Thang/Desktop/500 GH/12_12_2011/"+child.getName()));
				}
			}
		}
	}
}
