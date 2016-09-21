package com.az24.crawler.store;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.AbstractCrawler.Url;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.util.html.HttpURL;
import hdc.util.html.parser.XPathReader;
import hdc.util.lang.UriID;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathConstants;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.http.HttpResponse;
import org.w3c.dom.NodeList;

import com.az24.dao.StoreCagegoryDAO;
import com.az24.test.HttpClientUtil;

public class CategoryExtractor {

	private HashMap<Integer, Integer> categoryMap = null;

	public void getCategoryInAz24(String urlExcelMap) throws BiffException,
			IOException {
		categoryMap = new HashMap<Integer, Integer>();
		//FileInputStream propsFile = new	 java.io.FileInputStream(realPathFile);
		FileInputStream propsFile = new java.io.FileInputStream("./conf/"+ urlExcelMap);
		Workbook w = Workbook.getWorkbook(propsFile);
		Sheet sheet = w.getSheet(0);
		Cell cell = null;
		// Vat_Gia_Cat| Az24_Cat
		for (int i = 1; i < sheet.getRows(); i++) {
			try {
				cell = sheet.getCell(1, i);
				int vat_cat_id = Integer.parseInt(cell.getContents().trim());
				cell = sheet.getCell(0, i);
				int az24_cat_id = Integer.parseInt(cell.getContents().trim());
				categoryMap.put(vat_cat_id, az24_cat_id);
			} catch (Exception e) {
			}

		}
	}

	public List<Url> extractCategoryGHType1(String urlGH, int store_id,
			boolean create_cate) throws Exception {

		String x_pro_menu = "//div[@class='content menu_product']/ul[1]/li";
		String x_pro_menu_class = "/@class";
		String x_pro_menu_sub_href = "/a[1]/@href";
		String x_pro_menu_sub = "/a[1]/span[1]/text()";

		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(urlGH);
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);

		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		NodeList nodes = (NodeList) reader.read(x_pro_menu,
				XPathConstants.NODESET);
		int node_one_many = nodes.getLength();
		int i = 1;
		ArrayList<Url> urlCats = new ArrayList<Url>();
		System.out.println(html);
		Url urlPro = null;
		StoreCategory storeCategory = null;
		List<StoreCategory> listStoreCate = new ArrayList<StoreCategory>();
		Pattern pattern = null;
		Matcher matcher = null;
		while (i <= node_one_many) {
			try {
				String menu_name = (String) reader.read(x_pro_menu + "[" + i
						+ "]" + x_pro_menu_sub, XPathConstants.STRING);
				String class_menu = (String) reader.read(x_pro_menu + "[" + i
						+ "]" + x_pro_menu_class, XPathConstants.STRING);
				String menu_href = (String) reader.read(x_pro_menu + "[" + i
						+ "]" + x_pro_menu_sub_href, XPathConstants.STRING);

				urlPro = new Url();
				urlPro.url =menu_href;
				pattern = Pattern.compile("http://.*");
				matcher = pattern.matcher(urlPro.url);

				boolean kq = matcher.find();
				if (!kq)
					urlPro.url = "http://www.vatgia.com/" + menu_href;

				storeCategory = new StoreCategory();
				if ("level_1".equalsIgnoreCase(class_menu)) {
					storeCategory.parent_id = 1;
					urlCats.add(urlPro);
				}
				try {
					storeCategory.cat_id = Integer.parseInt(menu_href
							.substring(menu_href.lastIndexOf("=") + 1));
				} catch (Exception e) {
					storeCategory.cat_id = 0;
				}
				storeCategory.level = 0;
				storeCategory.name = menu_name;
				storeCategory.url_map_md5 = new UriID(new HttpURL(urlPro.url))
						.getIdAsString();
				
				//StoreCagegoryDAO cagegoryDAO = new StoreCagegoryDAO();
				//cagegoryDAO.updateMD5(cagegoryDAO.getCateByName(menu_name, store_id).id, storeCategory.url_map_md5 );
				listStoreCate.add(storeCategory);
				// }
				System.out.println("-->menu_name=" + menu_name);
				System.out.println("-->menu_href=" + menu_href);

			} catch (Exception e) {
				e.printStackTrace();
			}

			i++;

		}
		
		if (create_cate)
			createCategoryProduct(listStoreCate, store_id);
		return urlCats;
	}

	public List<Url> extractCategoryGHType2(String urlGH, int store_id,
			boolean create_cate) throws Exception {

		String x_pro_menu = "//div[@class='content menu_product']/ul[1]/li";
		String x_pro_menu_class = "/@class";
		String x_pro_menu_sub_href = "/a[1]/@href";
		String x_pro_menu_sub = "/a[1]/span[1]/text()";

		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(urlGH);
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);

		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		NodeList nodes = (NodeList) reader.read(x_pro_menu,
				XPathConstants.NODESET);
		int node_one_many = nodes.getLength();
		int i = 1;
		ArrayList<Url> urlCats = new ArrayList<Url>();

		Url urlPro = null;
		StoreCategory storeCategory = null;
		List<StoreCategory> listStoreCate = new ArrayList<StoreCategory>();
		Pattern pattern = Pattern.compile("http://www.vatgia.com/.*");
		Matcher matcher = null;
		while (i <= node_one_many) {
			try {
				String menu_name = (String) reader.read(x_pro_menu + "[" + i
						+ "]" + x_pro_menu_sub, XPathConstants.STRING);
				String class_menu = (String) reader.read(x_pro_menu + "[" + i
						+ "]" + x_pro_menu_class, XPathConstants.STRING);
				String menu_href = (String) reader.read(x_pro_menu + "[" + i
						+ "]" + x_pro_menu_sub_href, XPathConstants.STRING);

				urlPro = new Url();
				urlPro.url = menu_href;
				matcher = pattern.matcher(urlPro.url);
				boolean kq = matcher.find();
				if (!kq)
					urlPro.url = "http://www.vatgia.com/" + menu_href;

				storeCategory = new StoreCategory();
				try {
					storeCategory.cat_id = Integer.parseInt(menu_href
							.substring(menu_href.lastIndexOf("=") + 1));
				} catch (Exception e) {
					storeCategory.cat_id = 0;
				}
				storeCategory.parent_id = 0;
				storeCategory.level = 1;
				storeCategory.name = menu_name;
				storeCategory.url_map_md5 = new UriID(new HttpURL(urlPro.url))
						.getIdAsString();
				listStoreCate.add(storeCategory);

				System.out.println("-->menu_name=" + menu_name);
				System.out.println("-->menu_href=" + menu_href);

				if (class_menu.indexOf("level_0") >= 0) {
					// get Menu Con
					Thread.sleep(30);
					client = new HttpClientImpl();
					res = client.fetch(urlPro.url);
					html = HttpClientUtil.getResponseBody(res);
					XPathReader reader_sub = CrawlerUtil
							.createXPathReaderByData(html);
					NodeList nodeSubs = (NodeList) reader_sub.read(x_pro_menu,
							XPathConstants.NODESET);
					int node_one_sub_many = nodeSubs.getLength();
					int j = 1;
					while (j <= node_one_sub_many) {
						menu_name = (String) reader_sub.read(x_pro_menu + "["
								+ j + "]" + x_pro_menu_sub,
								XPathConstants.STRING);
						class_menu = (String) reader_sub.read(x_pro_menu + "["
								+ j + "]" + x_pro_menu_class,
								XPathConstants.STRING);
						menu_href = (String) reader_sub.read(x_pro_menu + "["
								+ j + "]" + x_pro_menu_sub_href,
								XPathConstants.STRING);
						if (class_menu.indexOf("level_1") >= 0) {

							urlPro = new Url();
							urlPro.url = menu_href;
							matcher = pattern.matcher(urlPro.url);
							kq = matcher.find();
							if (!kq)
								urlPro.url = "http://www.vatgia.com/"
										+ menu_href;

							storeCategory = new StoreCategory();
							storeCategory.parent_id = 1;
							urlCats.add(urlPro);

							try {
								storeCategory.cat_id = Integer
										.parseInt(menu_href.substring(menu_href
												.lastIndexOf("=") + 1));
							} catch (Exception e) {
								storeCategory.cat_id = 0;
							}
							storeCategory.level = 2;
							storeCategory.name = menu_name;
							storeCategory.url_map_md5 = new UriID(new HttpURL(
									urlPro.url)).getIdAsString();
							listStoreCate.add(storeCategory);
							System.out.println("---->menu_name=" + menu_name);
							System.out.println("---->menu_href=" + menu_href);
						}
						j++;
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			i++;
		}

		if (create_cate)
			createCategoryProduct(listStoreCate, store_id);
		return urlCats;
	}

	public List<Url> extractCategoryGHType3(String urlGH, int store_id,
			boolean create_cate) throws Exception {

		String x_pro_menu = "//TABLE[@id='content']/TBODY[1]/TR[1]/TD[@id='content_left']/DIV[1]/TABLE[2]/TBODY[1]/TR";
		String x_pro_menu_class = "/TD[1]/@class";
		String x_pro_menu_sub_href = "/TD[1]/A[1]/@href";
		String x_pro_menu_sub = "/TD[1]/A[1]/text()";

		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(urlGH);
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);

		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		NodeList nodes = (NodeList) reader.read(x_pro_menu,
				XPathConstants.NODESET);
		int node_one_many = nodes.getLength();
		int i = 1;
		ArrayList<Url> urlCats = new ArrayList<Url>();

		Url urlPro = null;
		StoreCategory storeCategory = null;
		List<StoreCategory> listStoreCate = new ArrayList<StoreCategory>();
		Pattern pattern = null;
		Matcher matcher = null;
		while (i <= node_one_many) {
			try {
				String menu_name = (String) reader.read(x_pro_menu + "[" + i
						+ "]" + x_pro_menu_sub, XPathConstants.STRING);
				String class_menu = (String) reader.read(x_pro_menu + "[" + i
						+ "]" + x_pro_menu_class, XPathConstants.STRING);
				String menu_href = (String) reader.read(x_pro_menu + "[" + i
						+ "]" + x_pro_menu_sub_href, XPathConstants.STRING);

				urlPro = new Url();
				urlPro.url = menu_href;
				pattern = Pattern.compile("http://www.vatgia.com/.*");
				matcher = pattern.matcher(urlPro.url);

				boolean kq = matcher.find();
				if (!kq)
					urlPro.url = "http://www.vatgia.com/" + menu_href;

				storeCategory = new StoreCategory();
				if ("left_menu_td".equalsIgnoreCase(class_menu)) {
					storeCategory.parent_id = 1;
					urlCats.add(urlPro);
				}
				try {
					storeCategory.cat_id = Integer.parseInt(menu_href
							.substring(menu_href.lastIndexOf("=") + 1));
				} catch (Exception e) {
					storeCategory.cat_id = 0;
				}
				storeCategory.level = 0;
				storeCategory.name = menu_name;
				storeCategory.url_map_md5 = new UriID(new HttpURL(urlPro.url))
						.getIdAsString();

				listStoreCate.add(storeCategory);
				System.out.println("-->menu_name=" + menu_name);
				System.out.println("-->menu_href=" + menu_href);
				System.out.println("-->url_map_md5="
						+ storeCategory.url_map_md5);

			} catch (Exception e) {
				e.printStackTrace();
			}

			i++;
		}
		if (create_cate)
			createCategoryProduct(listStoreCate, store_id);
		return urlCats;
	}

	public void createCategoryProduct(List<StoreCategory> cats, int store_id) {
		StoreCagegoryDAO storeCagegoryDAO = new StoreCagegoryDAO();
		int parent_id = 0, i = 0;
		while (i < cats.size()) {
			cats.get(i).store_id = store_id;
			cats.get(i).num_of_products = 0;
			cats.get(i).order_cat = i;
			if (categoryMap.containsKey(cats.get(i).cat_id))
				cats.get(i).cat_id = categoryMap.get(cats.get(i).cat_id);
			else
				cats.get(i).cat_id = 0;

			if (cats.get(i).parent_id == 0) {
				if (i == 0)
					cats.get(i).is_default = 1;
				cats.get(i).level = 1;
				cats.get(i).status = 1;
				cats.get(i).cat_id = 0;
				cats.get(i).create_date = (int) Calendar.getInstance()
						.getTimeInMillis() / 1000;
				cats.get(i).modify_date = (int) Calendar.getInstance()
						.getTimeInMillis() / 1000;
				cats.get(i).create_user = "auto";
				cats.get(i).modify_user = "auto";
				parent_id = storeCagegoryDAO.checkCate(cats.get(i).name,
						store_id);
				if (parent_id == 0)
					parent_id = storeCagegoryDAO.saveCategory(cats.get(i));
			} else {
				cats.get(i).parent_id = parent_id;
				cats.get(i).level = 2;
				cats.get(i).status = 1;
				cats.get(i).create_date = (int) Calendar.getInstance()
						.getTimeInMillis() / 1000;
				cats.get(i).modify_date = (int) Calendar.getInstance()
						.getTimeInMillis() / 1000;
				cats.get(i).create_user = "auto";
				cats.get(i).modify_user = "auto";
				if (!storeCagegoryDAO.checkCate(cats.get(i).name, store_id,
						parent_id))
					storeCagegoryDAO.saveCategory(cats.get(i));
			}
			i++;
		}
	}

	public List<Url> extractCategoryGHType4(String urlGH, int store_id,
			boolean create_cate) throws Exception {

		String x_pro_menu = "//TABLE[@id='content']/TBODY[1]/TR[1]/TD[@id='content_left']/DIV[1]/TABLE[2]/TBODY[1]/TR";
		String x_pro_menu_class = "/TD[1]/@class";
		String x_pro_menu_sub_href = "/TD[1]/A[1]/@href";
		String x_pro_menu_sub = "/TD[1]/A[1]/text()";

		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(urlGH);
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);

		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		NodeList nodes = (NodeList) reader.read(x_pro_menu,
				XPathConstants.NODESET);
		int node_one_many = nodes.getLength();
		int i = 1;
		ArrayList<Url> urlCats = new ArrayList<Url>();

		Url urlPro = null;
		StoreCategory storeCategory = null;
		List<StoreCategory> listStoreCate = new ArrayList<StoreCategory>();
		Pattern pattern = Pattern.compile("http://www.vatgia.com/.*");
		Matcher matcher = null;
		while (i <= node_one_many) {
			try {
				String menu_name = (String) reader.read(x_pro_menu + "[" + i
						+ "]" + x_pro_menu_sub, XPathConstants.STRING);
				String class_menu = (String) reader.read(x_pro_menu + "[" + i
						+ "]" + x_pro_menu_class, XPathConstants.STRING);
				String menu_href = (String) reader.read(x_pro_menu + "[" + i
						+ "]" + x_pro_menu_sub_href, XPathConstants.STRING);

				urlPro = new Url();
				urlPro.url = menu_href;
				matcher = pattern.matcher(urlPro.url);
				boolean kq = matcher.find();
				if (!kq)
					urlPro.url = "http://www.vatgia.com/" + menu_href;

				storeCategory = new StoreCategory();
				try {
					storeCategory.cat_id = Integer.parseInt(menu_href
							.substring(menu_href.lastIndexOf("=") + 1));
				} catch (Exception e) {
					storeCategory.cat_id = 0;
				}
				storeCategory.parent_id = 0;
				storeCategory.level = 1;
				storeCategory.name = menu_name;
				storeCategory.url_map_md5 = new UriID(new HttpURL(urlPro.url))
						.getIdAsString();
				listStoreCate.add(storeCategory);

				System.out.println("-->menu_name=" + menu_name);
				System.out.println("-->menu_href=" + menu_href);

				if (class_menu.indexOf("group") >= 0) {
					// get Menu Con
					Thread.sleep(30);
					client = new HttpClientImpl();
					res = client.fetch(urlPro.url);
					html = HttpClientUtil.getResponseBody(res);
					XPathReader reader_sub = CrawlerUtil
							.createXPathReaderByData(html);
					NodeList nodeSubs = (NodeList) reader_sub.read(x_pro_menu,
							XPathConstants.NODESET);
					int node_one_sub_many = nodeSubs.getLength();
					int j = 1;
					while (j <= node_one_sub_many) {
						menu_name = (String) reader_sub.read(x_pro_menu + "["
								+ j + "]" + x_pro_menu_sub,
								XPathConstants.STRING);
						class_menu = (String) reader_sub.read(x_pro_menu + "["
								+ j + "]" + x_pro_menu_class,
								XPathConstants.STRING);
						menu_href = (String) reader_sub.read(x_pro_menu + "["
								+ j + "]" + x_pro_menu_sub_href,
								XPathConstants.STRING);
						if (class_menu.equalsIgnoreCase("left_menu_td")) {

							urlPro = new Url();
							urlPro.url = menu_href;
							matcher = pattern.matcher(urlPro.url);
							kq = matcher.find();
							if (!kq)
								urlPro.url = "http://www.vatgia.com/"
										+ menu_href;

							storeCategory = new StoreCategory();
							storeCategory.parent_id = 1;
							urlCats.add(urlPro);

							try {
								storeCategory.cat_id = Integer
										.parseInt(menu_href.substring(menu_href
												.lastIndexOf("=") + 1));
							} catch (Exception e) {
								storeCategory.cat_id = 0;
							}
							storeCategory.level = 2;
							storeCategory.name = menu_name;
							storeCategory.url_map_md5 = new UriID(new HttpURL(
									urlPro.url)).getIdAsString();
							listStoreCate.add(storeCategory);
							System.out.println("---->menu_name=" + menu_name);
							System.out.println("---->menu_href=" + menu_href);
						}
						j++;
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			i++;
		}

		if (create_cate)
			createCategoryProduct(listStoreCate, store_id);
		return urlCats;
	}

}
