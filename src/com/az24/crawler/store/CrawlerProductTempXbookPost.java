package com.az24.crawler.store;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.az24.crawler.config.BeanXmlConfig;
import com.az24.crawler.model.ImageConfig;
import com.az24.crawler.model.Property;
import com.az24.dao.StoreDAO;


public class CrawlerProductTempXbookPost {
	
	public static void main(String[] args) throws Exception {

		StoreDAO storeDAO = new StoreDAO();
		CrawlerProductTemp productExtract = new CrawlerProductTemp();

		List<Category> listCate = productExtract.getCategory("/data/crawler/conf/stores/store_category_sachvidan_5865.xls");
		BeanXmlConfig beanXmlConfig = new BeanXmlConfig("/data/crawler/conf/stores/beanProductStoreSachvidan.xml");

		String store_alias = "sachvidan";
		String urlDomain = "http://xbook.com.vn/";
		String urlDomainRewrite = "http://xbook.com.vn/";
		productExtract.urlDomain = urlDomain;
		beanXmlConfig.parseConfig();
		List<Property> propeties = beanXmlConfig.bean.getProperties();
		List<ImageConfig> images = beanXmlConfig.bean.getImages();
		
		int store_id = 0;
		store_id = storeDAO.checkStore(store_alias);
		if (store_id == 0) {
			System.out.println("Khong Tim Thay Store");
			return;
		}
		// Process Category
		List<NameValuePair> list = null;
		for (Category category : listCate) {
			int i = 0;
			while (i <= 25) {
				list = new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("page","" + i ));
				list.add(new BasicNameValuePair("tform","0"));
				list.add(new BasicNameValuePair("tsort","NewsID desc"));
				list.add(new BasicNameValuePair("tto", "0"));
				productExtract.processCategory(category, urlDomainRewrite,
						store_alias, store_id, propeties, images, list);
				i++;
			}
		}

	}

}
