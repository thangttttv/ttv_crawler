package com.az24.crawler.store;

import java.io.File;

import com.az24.dao.StoreCagegoryDAO;

public class CrawlerStoreUpdateMenu {
	public static void main(String[] args) throws Exception {
		CrawlerStore crawlerStore = new CrawlerStore();
		StoreCagegoryDAO cagegoryDAO = new StoreCagegoryDAO();
		// Tao danh muc san pham
		File file = new File("D:/categorys/new");
		if(file.isDirectory())
		{
			for (File child : file.listFiles()) {
				System.out.println("Filename-------------->"+child.getName());
				if(child.isFile())
				{
					cagegoryDAO.update(crawlerStore.readExcelCategory("D:/categorys/new/"+child.getName()));
				}
			}
		}
	}
}
