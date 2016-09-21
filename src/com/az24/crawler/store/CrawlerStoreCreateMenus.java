package com.az24.crawler.store;

import java.io.FileInputStream;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import com.az24.dao.StoreCagegoryDAO;
import com.az24.dao.StoreDAO;

public class CrawlerStoreCreateMenus {
	public static void main(String[] args) throws Exception {
		CrawlerStore crawlerStore = new CrawlerStore();
		StoreCagegoryDAO cagegoryDAO = new StoreCagegoryDAO();
		CategoryExtractor categoryExtractor = new CategoryExtractor();
		FileInputStream propsFile = new java.io.FileInputStream("C:/Users/Tran The Thang/Desktop/500 GH/estores_8.xls");
		Workbook w = Workbook.getWorkbook(propsFile);
		Sheet sheet = w.getSheet(0);
		Cell cell = null;
		categoryExtractor.getCategoryInAz24("az24_cate.xls");

		for (int i = 1; i < sheet.getRows(); i++) {
			try {
				cell = sheet.getCell(1, i);
				String store_alias = cell.getContents().trim();

				cell = sheet.getCell(2, i);
				String urlGH = cell.getContents().trim();

				cell = sheet.getCell(3, i);
				int type = Integer.parseInt(cell.getContents().trim());

				// Tao danh muc san pham

				StoreDAO storeDAO = new StoreDAO();
				int store_id = storeDAO.checkStore(store_alias);

				switch (type) {
				case 1:
					categoryExtractor.extractCategoryGHType1(urlGH, store_id,
							true);
					break;
				case 2:
					categoryExtractor.extractCategoryGHType2(urlGH, store_id,
							true);
					break;
				case 3:
					categoryExtractor.extractCategoryGHType3(urlGH, store_id,
							true);
					break;
				case 4:
					categoryExtractor.extractCategoryGHType4(urlGH, store_id,
							true);
					break;
				default:
					break;
				}

				// Tao support online
				// crawlerStore.extractSupportGH(urlGH, store_id);
				crawlerStore.export_category(cagegoryDAO
						.getCategoryByStoreId(store_id), store_id, store_alias,
						type);
				Thread.sleep(500);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}
}
