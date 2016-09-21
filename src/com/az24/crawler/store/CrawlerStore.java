package com.az24.crawler.store;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.DocumentAnalyzer;
import hdc.crawler.AbstractCrawler.Url;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.util.html.A;
import hdc.util.html.HttpURL;
import hdc.util.html.NodeNormalizeVisitor;
import hdc.util.html.parser.DomWriter;
import hdc.util.html.parser.XPathReader;
import hdc.util.lang.UriID;
import hdc.util.text.HtmlUtil;
import hdc.util.text.StringUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.http.HttpResponse;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.az24.crawler.model.product.Product;
import com.az24.crawler.model.product.ProductData;
import com.az24.crawler.model.product.ProductField;
import com.az24.crawler.model.product.ProductFieldValue;
import com.az24.crawler.model.product.ProductImage;
import com.az24.dao.CategoryDAO;
import com.az24.dao.ImporterDAO;
import com.az24.dao.ProductDAO;
import com.az24.dao.ProductStoreCatDAO;
import com.az24.dao.ProductStoreDAO;
import com.az24.dao.StoreCagegoryDAO;
import com.az24.dao.StoreConfigDAO;
import com.az24.dao.StoreDAO;
import com.az24.dao.StoreMenuDAO;
import com.az24.dao.StoreSupportDAO;
import com.az24.dao.UserDAO;
import com.az24.test.HttpClientUtil;
import com.az24.util.FileLog;
import com.az24.util.MD5Php;
import com.az24.util.UTF8Tool;

public class CrawlerStore {
	private HashMap<Integer, Integer> categoryMap = null;
	public static String url_upload_logo = "/store_upload/logo/";
	public static String url_upload_banner_intro = "/store_upload/intro/";
	public static String url_upload_banner = "/store_upload/intro/";

	public int registerUser(String user_name, String full_name, String email,
			String password, String mobile, String province, String country,
			String address, Date birth_day, int sex, String tel, String yahoo,
			String skype) throws NoSuchAlgorithmException,
			UnsupportedEncodingException {
		int id = 0;
		User user = new User();
		user.username = user_name;
		user.full_name = full_name;
		user.email = email;
		user.salt = "!@#$%^";
		user.password = com.az24.util.MD5Php.MD5(MD5Php.MD5(password)
				+ user.salt);
		user.mobile = mobile;
		user.province = province;
		user.country = country;
		user.address = address;
		user.birthday = birth_day;
		user.sex = sex;
		user.tel = tel;
		user.yahoo = yahoo;
		user.skype = skype;
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		user.create_date = new java.sql.Timestamp((calendar.getTime())
				.getTime());
		user.user_create = "auto";
		user.modify_date = new java.sql.Timestamp((calendar.getTime())
				.getTime());
		user.user_modify = "auto";

		UserDAO userDAO = new UserDAO();
		id = userDAO.saveUser(user);
		user.id = id;
		userDAO.saveUserSysn(user);
		return id;
	}

	public int registerStore(int user_id, String user_name, String store_name,
			String store_mobile, String store_tel, String fax,
			String store_address, String domain, String store_web,
			int store_city, String city, String store_area, String store_alias,
			int type) {
		int id = 0;
		Store store = new Store();
		store.user_id = user_id;
		store.store_alias = store_alias;
		store.name = store_name;
		store.mobile = store_mobile;
		store.tel = store_tel;
		store.fax = fax;
		store.address = store_address;
		store.website = store_web;
		store.domain = domain;
		store.area = store_area;
		store.type = type;
		store.name = store_name;
		store.province_id = store_city;
		store.province_name = city;
		store.province_code = city;
		store.status = 1;

		Calendar calendar = Calendar.getInstance();
		store.create_date = calendar.getTimeInMillis() / 1000;
		store.modify_date = calendar.getTimeInMillis() / 1000;
		store.last_upgrade = calendar.getTimeInMillis() / 1000;
		store.active_date = calendar.getTimeInMillis() / 1000;
		store.end_date = calendar.getTimeInMillis() / 1000 + 90 * 24 * 60 * 60;

		store.create_user = user_name;
		store.modify_user = user_name;
		StoreDAO storeDAO = new StoreDAO();
		id = storeDAO.saveStore(store);
		// cap nhat lai user
		if (id > 0) {
			UserDAO userDAO = new UserDAO();
			User user = new User();
			user.id = user_id;
			user.store_admin = 1;
			user.store_alias = store_alias;
			user.store_id = id;
			userDAO.updateUser(user);
		}
		return id;
	}

	public void createDefaultConfig(int store_id, String footer, String intro,
			String contact) {
		StoreConfig storeConfig = new StoreConfig();
		storeConfig.banner_logo = "";
		storeConfig.store_id = store_id;
		storeConfig.footer_info = footer;
		storeConfig.layout_type = 2;
		storeConfig.module = "{\"header\":{\"header_banner_logo\":\"1\",\"header_top_menu\":\"2\"},\"left\":{\"left_cat_news\":\"0\",\"left_latest_news\":\"1\",\"left_menu_store_cat\":\"2\",\"left_new_cla\":\"3\",\"left_store_gallery\":\"4\",\"left_store_links\":\"5\",\"left_store_video\":\"6\",\"banner_left\":\"7\"},\"center\":{\"center_introduce\":\"0\",\"center_search_form\":\"0\",\"home_new_cla\":\"1\",\"home_latest_news\":\"2\",\"home_hot_new_promotion\":\"3\",\"home_kit\":\"4\",\"home_product_by_category\":\"5\"},\"right\":{\"banner_right\":\"0\",\"right_currency_exchange_rate\":\"1\",\"right_login\":\"2\",\"right_new_faq\":\"3\",\"right_price_download\":\"4\",\"right_store_statistic\":\"5\",\"right_support_online\":\"6\",\"right_weather\":\"7\"}}";
		storeConfig.left_menu_type = 1;
		storeConfig.home_intro_type = 2;
		storeConfig.number_product = 15;
		storeConfig.number_product_home = 5;
		storeConfig.number_classified = 5;
		storeConfig.number_news = 5;
		storeConfig.price_inform = "Vui lòng gọi";
		storeConfig.tab_pro_new = 1;
		storeConfig.tab_sale_off = 1;
		storeConfig.tab_top_view = 1;
		storeConfig.pro_home_type_view = 2;
		storeConfig.pro_type_view = 1;
		storeConfig.theme_id = 25;
		storeConfig.background_image = "";
		storeConfig.intro = intro;
		storeConfig.contact = contact.trim();
		storeConfig.create_user = "auto";
		storeConfig.modify_user = "auto";
		storeConfig.create_date = (int) Calendar.getInstance()
				.getTimeInMillis() / 1000;
		storeConfig.modify_date = (int) Calendar.getInstance()
				.getTimeInMillis() / 1000;
		StoreConfigDAO storeConfigDAO = new StoreConfigDAO();
		storeConfigDAO.saveConfig(storeConfig);
	}

	public void createMenuDefault(int store_id, String store_alias) {
		StoreMenu storeMenu = new StoreMenu();
		StoreMenuDAO storeMenuDAO = new StoreMenuDAO();
		storeMenu.store_id = store_id;
		storeMenu.create_user = "auto";
		storeMenu.create_date = (int) Calendar.getInstance().getTimeInMillis() / 1000;
		storeMenu.modify_user = "auto";
		storeMenu.modify_date = (int) Calendar.getInstance().getTimeInMillis() / 1000;
		storeMenu.status = 1;
		storeMenu.order_menu = 1;
		storeMenu.level = 1;
		storeMenu.parent_id = 0;
		storeMenu.is_bottom = 0;
		storeMenu.is_top_menu = 1;
		storeMenu.open_type = 0;
		storeMenu.name = "Az24.vn";
		storeMenu.url = "http://az24.vn";
		storeMenuDAO.saveMenuStore(storeMenu);

		storeMenu.name = "Trang chủ";
		storeMenu.url = "/" + store_alias;
		storeMenuDAO.saveMenuStore(storeMenu);

		storeMenu.name = "Giới thiệu";
		storeMenu.url = "/" + store_alias + "/gioi-thieu";
		storeMenuDAO.saveMenuStore(storeMenu);

		storeMenu.name = "Đánh giá";
		storeMenu.url = "/" + store_alias + "/danh-gia";
		storeMenuDAO.saveMenuStore(storeMenu);

		storeMenu.name = "Hỏi đáp";
		storeMenu.url = "/" + store_alias + "/hoi-dap";
		storeMenuDAO.saveMenuStore(storeMenu);

		storeMenu.name = "Liên hệ";
		storeMenu.url = "/" + store_alias + "/lien-he";
		storeMenuDAO.saveMenuStore(storeMenu);

		storeMenu.name = "Bản đồ";
		storeMenu.url = "/" + store_alias + "/ban-do";
		storeMenuDAO.saveMenuStore(storeMenu);

		storeMenu.name = "Chính sách";
		storeMenu.url = "/" + store_alias + "/chinh-sach";
		storeMenuDAO.saveMenuStore(storeMenu);

	}

	
	public void downloadImage(String imageUrl, String destinationFile) {
		URL url;
		try {
			url = new URL(imageUrl);
			System.out.println(url);
			InputStream is = url.openStream();
			OutputStream os = new FileOutputStream(destinationFile);

			byte[] b = new byte[2048];
			int length;

			while ((length = is.read(b)) != -1) {
				os.write(b, 0, length);
			}

			is.close();
			os.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

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

	public List<StoreSupport> extractSupportGH(String urlGH, int store_id)
			throws Exception {

		String x_support_online = "//div[@class='estore_support_online']/div";
		String x_support_online_sub = "/a[1]/@href";
		String x_support_online_lable_sub = "/a[1]";

		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(urlGH);
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);

		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);

		List<StoreSupport> listSupport = new ArrayList<StoreSupport>();

		NodeList nodes = (NodeList) reader.read(x_support_online,
				XPathConstants.NODESET);
		int node_one_many = nodes.getLength();
		int i = 1;
		StoreSupport storeSupport = null;
		// Type 1.Yaoo,2.skype,3.mobile
		while (i <= node_one_many) {
			storeSupport = new StoreSupport();

			String online_nick = (String) reader.read(x_support_online + "["
					+ i + "]" + x_support_online_sub, XPathConstants.STRING);
			Node online_label = (Node) reader.read(x_support_online + "[" + i
					+ "]" + x_support_online_lable_sub, XPathConstants.NODE);
			// http://www.vatgia.com/ymsgr:SendIM?viettruckKD1
			Pattern pattern = Pattern.compile("ymsgr:SendIM?.*");
			Matcher matcher = pattern.matcher(online_nick);
			boolean kq = matcher.find();
			if (kq) {
				storeSupport.type = 1;
				online_nick = online_nick.replaceAll("ymsgr:SendIM\\?", "");
			}

			// http://www.vatgia.com/skype:viettruckKD1?chat
			pattern = Pattern.compile("skype.*");
			matcher = pattern.matcher(online_nick);
			kq = matcher.find();
			if (kq) {
				storeSupport.type = 2;
				online_nick = online_nick.replaceAll("skype:", "");
				online_nick = online_nick.replaceAll("\\?chat", "");
			}

			storeSupport.store_id = store_id;
			storeSupport.status = 1;
			String label = HtmlUtil.removeTag(online_label.getTextContent());
			storeSupport.display = label.trim();
			storeSupport.nick = online_nick;
			storeSupport.create_date = (int) Calendar.getInstance()
					.getTimeInMillis() / 1000;
			storeSupport.modify_date = (int) Calendar.getInstance()
					.getTimeInMillis() / 1000;
			storeSupport.create_user = "auto";
			storeSupport.modify_user = "auto";

			System.out.println("-->online_nick=" + online_nick);
			System.out.println("-->online_label=" + label.trim());
			listSupport.add(storeSupport);
			i++;

		}
		StoreSupportDAO storeSupportDAO = new StoreSupportDAO();
		for (StoreSupport storeSupportT : listSupport) {
			if (!storeSupportDAO.checkSupport(storeSupportT.nick,
					storeSupportT.type))
				storeSupportDAO.saveSupport(storeSupportT);
		}
		return listSupport;
	}

	public StoreConfig extractBaseInfoGH(String urlGH, String store_alias)
			throws Exception {

		StoreConfig storeConfig = new StoreConfig();

		String x_config_footer = "//div[@class='footer_content']";
		// String x_header_banner_download = "//div[@class='header_banner']";
		String x_config_intro = "//div[@class='static_description']";
		String x_config_contact = "//div[@class='contact_description']";
		// Get Intro Text
		urlGH = urlGH.trim();
		HttpClientImpl client = new HttpClientImpl();
		Pattern pattern = Pattern.compile("^\\W|\\W$");
		Matcher matcher = pattern.matcher(urlGH);
		urlGH = matcher.replaceAll("");
		
		System.out.println(urlGH);
		HttpResponse res = client.fetch(urlGH.trim() + "&module=static&view=about");
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);

		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);

		String intro_text = (String) reader.read(x_config_intro,
				XPathConstants.STRING);
		storeConfig.intro = intro_text;
		System.out.println("intro_text=---------->" + intro_text);

		// Get Contact Text
		Thread.sleep(10);
		res = client.fetch(urlGH + "&module=contact");
		HttpClientUtil.printResponseHeaders(res);
		html = HttpClientUtil.getResponseBody(res);
		reader = CrawlerUtil.createXPathReaderByData(html);
		String contact_text = (String) reader.read(x_config_contact,
				XPathConstants.STRING);
		System.out.println("Contact=---------->" + contact_text);
		storeConfig.contact = contact_text;

		// Get header Banner
		Thread.sleep(10);
		res = client.fetch(urlGH);
		HttpClientUtil.printResponseHeaders(res);
		html = HttpClientUtil.getResponseBody(res);
		reader = CrawlerUtil.createXPathReaderByData(html);
		/*
		 * Node path_header_banner = (Node)
		 * reader.read(x_header_banner_download, XPathConstants.NODE);
		 * System.out.println(path_header_banner.getTextContent());
		 */

		// String banner_logo_name =
		// path_header_banner.substring(path_header_banner.lastIndexOf("."));
		/*
		 * storeConfig.banner_logo = CrawlerStore.url_upload_banner +
		 * store_alias + "/" + "banner_user/eme1270780523.swf";
		 * this.downloadImage(
		 * "http://www.vatgia.com/banner_user/eme1270780523.swf",
		 * "D:/data/eme1270780523.swf");
		 */

		// Get footer
		Thread.sleep(10);
		res = client.fetch(urlGH);
		HttpClientUtil.printResponseHeaders(res);
		html = HttpClientUtil.getResponseBody(res);
		reader = CrawlerUtil.createXPathReaderByData(html);
		String footer = (String) reader.read(x_config_footer,
				XPathConstants.STRING);
		storeConfig.footer_info = footer;
		System.out.println("footer=---------->" + footer);

		return storeConfig;

	}

	public void extractProductGH(List<Url> urlProcates, int store_id,
			String store_alias, String store_alias_n) throws Exception {
		DocumentAnalyzer analyzer;
		String html = "";
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = null;
		List<A> listLinkProduct = null;

		StoreCagegoryDAO storeCagegoryDAO = new StoreCagegoryDAO();
		CategoryDAO categoryDAO = new CategoryDAO();
		ProductDAO productDAO = new ProductDAO();
		StoreCategory storeCategory = null;
		com.az24.crawler.store.Category category = null;
		HashMap<Integer, Integer> holder = null;
		int extention_id = 0;
		int i = 1;
		List<ProductField> listProductField = null;
		List<ProductFieldValue> listProductFieldValue = null;
		int sl = 0;
		int page = 0;
		
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH) + 1;
		String dayPath = calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0"
				+ calendar.get(Calendar.DAY_OF_MONTH) : ""
				+ calendar.get(Calendar.DAY_OF_MONTH);
		String strmonth = month < 10 ? "0" + month : "" + month;
		String day = Calendar.getInstance().get(Calendar.YEAR)+ "_"+strmonth+"_"+dayPath+"_";
		
		FileLog.createFileLog("/data/crawler/data/anh/" + day + store_alias	+ ".txt");
		//FileLog.createFileLog("d:/" + day + store_alias	+ ".txt");
		for (Url url : urlProcates) {
			extention_id = 0;
			sl = 0;
			i = 1;

			String id_url = new UriID(new HttpURL(url.url)).getIdAsString();
			System.out.println("--->Duyet Cate Url = " + url.url);
			System.out.println("--->Duyet Cate Url = " + id_url);
			storeCategory = storeCagegoryDAO.getCate(id_url, store_id);
			if (storeCategory == null)
			{
				System.out.println("Khong Tim Thay Danh Muc Store =" + sl);
				Thread.sleep(30);
				continue;
			}

			category = categoryDAO.getCate(storeCategory.cat_id);
			if (category == null) {
				System.out.println("Khong Tim Thay Danh Muc =" + sl);
				Thread.sleep(30);
				continue;

			}
			holder = new HashMap<Integer, Integer>();
			extention_id = category.extension_id;
			listProductField = productDAO.getField(extention_id);
			listProductFieldValue = productDAO.getFieldValue(extention_id);
			categoryDAO.getCategoriesParent(category.id, holder);
			page = 1;
			sl = sl * 2;
			int sp_page = 0;
			while (true) {
				res = client.fetch(url.url + "&page=" + page);
				System.out.println("--->Duyet Cate Url = " + url.url + "&page="
						+ page);
				Thread.sleep(60);
				try {
					// Duyet link san pham
					html = HttpClientUtil.getResponseBody(res);
					analyzer = new DocumentAnalyzer.DefaultDocumentAnayzer(
							"http://www.vatgia.com", "http://www.vatgia.com");
					listLinkProduct = analyzer.analyze(html, url.url);
					Thread.sleep(60);
					if (listLinkProduct.size() == 0)
						break;
					else {
						// Lay san pham
						sp_page = getProduct(listLinkProduct, store_alias_n,
								store_id, store_alias, storeCategory,
								listProductField, listProductFieldValue,
								category, holder);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				i++;
				page++;
				if (sp_page == 0)
					break;
				sp_page = 0;
			}
		}
		FileLog.close();
	}

	private int getProduct(List<A> listLinkProduct, String store_alias_n,
			int store_id, String store_alias, StoreCategory storeCategory,
			List<ProductField> listProductField,
			List<ProductFieldValue> listProductFieldValue, Category category,
			HashMap<Integer, Integer> holder) throws Exception {
		boolean kq = false;
		int j = 0;
		ImporterDAO importerDAO = new ImporterDAO();
		ProductDAO productDAO = new ProductDAO();
		int sp_page = 0;
		Pattern pattern;
		Matcher matcher;
		for (A a : listLinkProduct) {
			pattern = Pattern.compile("http://www.vatgia.com/"
					+ store_alias_n.toLowerCase()
					+ "&module=product&view=detail&record_id=\\d++");
			matcher = pattern.matcher(a.getUrl().toLowerCase());

			kq = matcher.find();
			if (kq) {

				System.out.println(a.getUrl());			
				String str_product_id = a.getUrl().substring(
						a.getUrl().lastIndexOf("=") + 1);

				// Get Product ID trong tb_import
				int product_id = importerDAO.getProductID(str_product_id);

				// Check Model da ton tail
				// IF da ton tai -> Lay Product Store Va Luu

				boolean is_exist_model = productDAO.checkModel(product_id);
				String ur_pro_store_id = new UriID(new HttpURL(a.getUrl()))
						.getIdAsString();

				boolean is_exist_pro_store = importerDAO.checkProductStore(
						ur_pro_store_id, storeCategory.id);

				// is_exist_pro_store = false;
				if (is_exist_pro_store) {
					System.out.println("Da Ton Tai Trong Crawler Store"+ur_pro_store_id);
					j++;
					continue;
				}
				if (product_id > 0 && is_exist_model) {
					sp_page += extract_product_store(a.getUrl(), store_id,
							store_alias, product_id, storeCategory.parent_id,
							storeCategory.id);

					j++;
				} else {

					// ELSE Lay thong tin model -> Tao Model
					// va Luu Productore
					sp_page += extract_product(a.getUrl(), store_id,
							store_alias, storeCategory.parent_id,
							storeCategory.id, storeCategory.cat_id,
							category.is_multi_sell, listProductField,
							listProductFieldValue, holder);
					j++;
				}

			}
			Thread.sleep(60);
		}
		return sp_page;
	}

	public int getSLSP(String url, String xpath) {
		int sl = 0;
		try {
			HttpClientImpl client = new HttpClientImpl();
			HttpResponse res = client.fetch(url);
			HttpClientUtil.printResponseHeaders(res);
			String html = HttpClientUtil.getResponseBody(res);
			XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
			String str_sl = (String) reader.read(xpath, XPathConstants.STRING);
			Pattern pattern = Pattern.compile("\\W");
			Matcher matcher = pattern.matcher(str_sl);
			str_sl = matcher.replaceAll("");
			try {
				sl = Integer.parseInt(str_sl);
			} catch (Exception e) {
				sl = 0;
			}
			Thread.sleep(10);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sl;
	}

	public void export_category(List<Category> listCat, int store_id,
			String store_alias,int type) throws IOException, RowsExceededException,
			WriteException {
		WritableWorkbook workbook = Workbook.createWorkbook(new java.io.File(
				"d:/" + "store_cate_"+store_alias + "_" + store_id +"_"+type+ ".xls"));
		
		WritableSheet s = workbook.createSheet("Sheet_" + 1, 0);

		/* Format the Font */
		WritableFont wf = new WritableFont(WritableFont.ARIAL, 10,
				WritableFont.BOLD);
		WritableCellFormat cf = new WritableCellFormat(wf);
		cf.setWrap(true);
		WritableFont wf1 = new WritableFont(WritableFont.ARIAL, 10,
				WritableFont.NO_BOLD);
		WritableCellFormat cf2 = new WritableCellFormat(wf1);
		cf2.setWrap(false);

		/* Creates Label and writes date to one cell of sheet */
		Label l = new Label(0, 0, "ID", cf);
		s.addCell(l);
		l = new Label(1, 0, "Name", cf);
		s.addCell(l);
		l = new Label(2, 0, "Cat_ID", cf);
		s.addCell(l);
		l = new Label(3, 0, "Parent_ID", cf);
		s.addCell(l);
		l = new Label(4, 0, "Level", cf);
		s.addCell(l);

		int row = 1;
		for (Category category : listCat) {
			l = new Label(0, row, category.id + "", cf2);
			s.addCell(l);
			l = new Label(1, row, category.name, cf2);
			s.addCell(l);
			l = new Label(2, row, category.cat_id + "", cf2);
			s.addCell(l);
			l = new Label(3, row, category.parent_id + "", cf2);
			s.addCell(l);
			l = new Label(4, row, category.level + "", cf2);
			s.addCell(l);
			row++;
		}
		workbook.write();
		workbook.close();
	}

	public List<Category> readExcelCategory(String fileexcel)
			throws IOException {
		List<Category> examList = null;
		File file = new File(fileexcel);
		Workbook w;
		FileInputStream fileInputStream = new FileInputStream(file);
		Category category = null;
		try {
			examList = new ArrayList<Category>();
			w = Workbook.getWorkbook(fileInputStream);
			Sheet sheet = w.getSheet(0);
			Cell cell = null;
			for (int i = 1; i < sheet.getRows(); i++) {
				category = new Category();
				cell = sheet.getCell(0, i);
				String id = cell.getContents();

				cell = sheet.getCell(1, i);
				String name = cell.getContents();
				cell = sheet.getCell(2, i);
				String cat_id = cell.getContents();
				try {
					category.id = Integer.parseInt(id);
				} catch (Exception e) {
					category.id = 0;
				}
				Pattern pattern = Pattern.compile("\\D");
				Matcher matcher = pattern.matcher(cat_id);
				cat_id = matcher.replaceAll("");
				try {
					category.cat_id = Integer.parseInt(cat_id.trim());
				} catch (Exception e) {
					category.cat_id = 0;
				}
				category.name = name;
				System.out.println(name);
				examList.add(category);
			}
		} catch (BiffException e) {
			e.printStackTrace();
		}

		return examList;
	}

	public int extract_product_store(String url, int store_id,
			String store_alias, int product_id, int store_cat_id_1,
			int store_cat_id_2) throws Exception {
		int kq = 0;
		System.out.println("URL SAN Pham" + url);
		String xpath_price = "//table[@class='product_detail_table']/TBODY[1]/tr[1]/td[1]/table[1]/TBODY[1]/tr";
		String xpath_soluong = "//div[@id='container_content_center_right']/div[4]/div[1]/div[2]/table[2]/TBODY[1]/tr[1]/td[1]/table[1]/TBODY[1]/tr";

		ProductStore productStore = new ProductStore();
		productStore.product_id = product_id;

		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);

		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);

		NodeList nodes = (NodeList) reader.read(xpath_price,
				XPathConstants.NODESET);
		int node_one_many = nodes.getLength();
		int i = 1;
		String sub_xpath = "";
		java.util.regex.Pattern pattern_price = Pattern.compile("\\D");
		Matcher matcher = null;
		double price = 0;
		while (i <= node_one_many) {
			String price_label = (String) reader.read(xpath_price + "[" + i
					+ "]" + "/td[1]/text()", XPathConstants.STRING);
			sub_xpath = "/td[2]/text()";
			if ("Giá chưa VAT:".equalsIgnoreCase(price_label))
				sub_xpath = "/td[2]/b[1]/text()";
			if ("Giá bán cuối:".equalsIgnoreCase(price_label)||"Giá sản phẩm:".equalsIgnoreCase(price_label))
				sub_xpath = "/td[2]/b[1]/text()";

			String price_value = (String) reader.read(xpath_price + "[" + i
					+ "]" + sub_xpath, XPathConstants.STRING);
			System.out.println(price_value);
			System.out.println(price_value.trim());
			matcher = pattern_price.matcher(price_value);
			price_value = matcher.replaceAll("");
			if (!StringUtil.isEmpty(price_value)) {

				price = Double.parseDouble(price_value);
			}

			Pattern pattern = Pattern.compile("\\+10%");
			matcher = pattern.matcher(price_label);
			if (matcher.find())
				productStore.vat = 1;

			if ("Giá chưa VAT:".equalsIgnoreCase(price_label)) {
				productStore.price = price;
			}
			if ("Giá bán cuối:".equalsIgnoreCase(price_label)||"Giá sản phẩm:".equalsIgnoreCase(price_label)) {
				productStore.price = price;
			}

			i++;
		}
		productStore.price_type = 1;

		nodes = (NodeList) reader.read(xpath_soluong, XPathConstants.NODESET);
		node_one_many = nodes.getLength();
		i = 1;
		sub_xpath = "";
		int is_node = 0;
		while (i <= node_one_many) {
			String price_label = (String) reader.read(xpath_soluong + "[" + i
					+ "]" + "/td[1]/span[1]/text()", XPathConstants.STRING);
			sub_xpath = "/td[2]/text()";
			is_node = 0;
			if ("Đặt hàng".equalsIgnoreCase(price_label)) {
				sub_xpath = "/td[2]";
				is_node = 1;
			}
			if ("Xem thêm".equalsIgnoreCase(price_label)) {
				sub_xpath = "/td[2]/a[1]/@href";
				is_node = 0;
			}
			String price_value = "";
			if (is_node == 0) {
				price_value = (String) reader.read(xpath_soluong + "[" + i
						+ "]" + sub_xpath, XPathConstants.STRING);
			} else {
				Node price_value_node = (Node) reader.read(xpath_soluong + "["
						+ i + "]" + sub_xpath, XPathConstants.NODE);
				price_value = price_value_node.getTextContent();
			}

			price_value = price_value.trim();

			if ("Đặt hàng".equalsIgnoreCase(price_label)) {
				pattern_price = Pattern.compile("\\D");
				matcher = pattern_price.matcher(price_value);
				int sl = Integer.parseInt(matcher.replaceAll(""));
				productStore.quantity = sl;
				System.out.println("So Luong SP" + sl);
			}
			if ("Bảo hành".equalsIgnoreCase(price_label)) {
				pattern_price = Pattern.compile("\\D");
				matcher = pattern_price.matcher(price_value);
				int bh = Integer.parseInt(matcher.replaceAll(""));
				productStore.ensure_month = bh;
			}
			if ("Tình trạng".equalsIgnoreCase(price_label)) {
				if ("Moi".equalsIgnoreCase(UTF8Tool.coDau2KoDau(price_value))) {
					productStore.is_new = 1;
					productStore.quanlity = 1;
				} else {
					productStore.is_new = 0;
					productStore.quanlity = 0;
				}
			}

			if ("Xuất xứ".equalsIgnoreCase(price_label)) {
				if ("chinh hang".equalsIgnoreCase(UTF8Tool
						.coDau2KoDau(price_value)))
					productStore.origin = 1;
				if ("xach tay".equalsIgnoreCase(UTF8Tool
						.coDau2KoDau(price_value)))
					productStore.origin = 2;
				if ("hang cong ty".equalsIgnoreCase(UTF8Tool
						.coDau2KoDau(price_value)))
					productStore.origin = 3;
			}

			if ("Vận chuyển".equalsIgnoreCase(price_label)) {
				if ("lien he".equalsIgnoreCase(UTF8Tool
						.coDau2KoDau(price_value)))
					productStore.ship_type = 4;
				if ("co dinh".equalsIgnoreCase(UTF8Tool
						.coDau2KoDau(price_value)))
					productStore.ship_type = 3;
				if ("mien phi noi thanh".equalsIgnoreCase(UTF8Tool
						.coDau2KoDau(price_value)))
					productStore.ship_type = 1;
				if ("mien phi".equalsIgnoreCase(UTF8Tool
						.coDau2KoDau(price_value)))
					productStore.ship_type = 1;
			}

			if ("Xem thêm".equalsIgnoreCase(price_label)) {
				productStore.url_link_web = price_value;
				System.out.println("url_link_web" + price_value);
			}

			System.out.println(price_label.trim());
			System.out.println(price_value.trim());
			i++;
		}
		String xpath__description = "//div[@class='product_detail_description']";
		Node description = (Node) reader.read(xpath__description,
				XPathConstants.NODE);
		normalVisitor.traverse(description);
		DomWriter writer = new DomWriter();
		String strDes = writer.toXMLString(description);
		if (strDes != null
				&& strDes.length() > "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
						.length()) {
			strDes = strDes
					.substring("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
							.length());
			if (strDes.length() > 60530)
				strDes = HtmlUtil.removeTag(strDes);
		}
		Calendar calendar = Calendar.getInstance();
		int length = strDes.length() > 60530 ? 60530 : strDes.length();
		productStore.description = strDes.substring(0, length);
		productStore.create_date = calendar.getTimeInMillis() / 1000;
		productStore.modify_date = calendar.getTimeInMillis() / 1000;
		productStore.create_user = "auto";
		productStore.modify_user = "auto";

		productStore.status = 1;
		productStore.store_id = store_id;
		productStore.store_alias = store_alias;
		productStore.original_link = url;

		// Luu san pham
		ProductStoreDAO productStoreDAO = new ProductStoreDAO();
		ProductStoreCatDAO productStoreCatDAO = new ProductStoreCatDAO();
		int product_store_id = productStoreDAO.saveProductStore(productStore);

		if (product_store_id > 0) {
			kq = 1;
			ProductStoreCat productStoreCat = new ProductStoreCat();
			productStoreCat.product_id = product_id;
			productStoreCat.product_store_id = product_store_id;
			productStoreCat.store_id = store_id;
			productStoreCat.store_cat_id1 = store_cat_id_1;
			productStoreCat.store_cat_id2 = store_cat_id_2;
			productStoreCatDAO.saveProductStoreCate(productStoreCat);
			ImporterDAO importerDAO = new ImporterDAO();
			String id_url = new UriID(new HttpURL(url)).getIdAsString();
			importerDAO.saveLog(product_id, product_store_id, store_cat_id_2,
					id_url, url, "vat gia",store_id);
			//Update Price Model
			ProductDAO productDAO = new ProductDAO();
			Product product2 = productDAO.getProductById(product_id);
			if(productStore.price>0)
			{
				//update Min
				if(productStore.price<product2.price_from)
				{
					product2.price_from = productStore.price;
					productDAO.updatePrice(product2);
				}
				
				if(productStore.price>product2.price_to)
				{
					product2.price_to = productStore.price;
					productDAO.updatePrice(product2);
				}
			}
		}
		return kq;
	}

	private NodeNormalizeVisitor normalVisitor = new NodeNormalizeVisitor() {
		protected void normalize(Node node) {
			if (node != null && node.hasAttributes()) {
				NamedNodeMap attributes = node.getAttributes();
				if (attributes.getNamedItem("class") != null)
					attributes.removeNamedItem("class");
				if (attributes.getNamedItem("id") != null
						&& !"object".equalsIgnoreCase(node.getNodeName()))
					attributes.removeNamedItem("id");
				if (attributes.getNamedItem("style") != null)
					attributes.removeNamedItem("style");
				if (attributes.getNamedItem("height") != null
						&& (!"object".equalsIgnoreCase(node.getNodeName()) || !"embed"
								.equalsIgnoreCase(node.getNodeName())))
					attributes.removeNamedItem("height");
				if (attributes.getNamedItem("width") != null
						&& (!"object".equalsIgnoreCase(node.getNodeName()) || !"embed"
								.equalsIgnoreCase(node.getNodeName())))
					attributes.removeNamedItem("width");
				if (attributes.getNamedItem("onclick") != null)
					attributes.removeNamedItem("onclick");
				if (attributes.getNamedItem("title") != null)
					attributes.removeNamedItem("title");
				if (attributes.getNamedItem("rel") != null)
					attributes.removeNamedItem("rel");
				if (attributes.getNamedItem("target") != null)
					attributes.removeNamedItem("target");
				String tag = node.getNodeName();
				if (node instanceof Element && "a".equalsIgnoreCase(tag)) {
					Element a = (Element) node;
					a.setAttribute("rel", "nofollow");
					a.setAttribute("href", "#");
				}
			}

		}
	};

	public int extract_product(String url, int store_id, String store_alias,
			int store_cat_id_1, int store_cat_id_2, int cat_az_id,
			int is_multi, List<ProductField> listProductField,
			List<ProductFieldValue> listProductFieldValue,
			HashMap<Integer, Integer> holder) throws Exception {

		System.out.println("-->URL SAN Pham " + url);
		String xpath_price = "//table[@class='product_detail_table']/TBODY[1]/tr[1]/td[1]/table[1]/TBODY[1]/tr";
		String xpath_soluong = "//div[@id='container_content_center_right']/div[4]/div[1]/div[2]/table[2]/TBODY[1]/tr[1]/td[1]/table[1]/TBODY[1]/tr";

		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);

		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);

		// Lay Thong Tin San Pham
		String pre_folder = "/picture_model/";
		Product product = new Product();
		ProductStore productStore = new ProductStore();

		String xpath__description = "//div[@class='product_detail_description']";
		Node description = (Node) reader.read(xpath__description,
				XPathConstants.NODE);
		normalVisitor.traverse(description);
		DomWriter writer = new DomWriter();

		String xpath_name = "//div[@class='product_detail product_detail_v2 product_detail_v2_exclusive']/div[2]/h1[1]/text()";

		String name = (String) reader.read(xpath_name, XPathConstants.STRING);
		System.out.println("Name=" + name);
		product.name = name;

		if (StringUtil.isEmpty(name)) {
			xpath_name = "//div[@class='product_detail product_detail_v2']/div[2]/h1[1]/text()";
			name = (String) reader.read(xpath_name, XPathConstants.STRING);
			product.name = name;
		}

		
		if (StringUtil.isEmpty(name))
			return 0;
		
		Pattern pattern = Pattern.compile("\\W+");
		Pattern pattern2 = Pattern.compile("-$");				
		name = UTF8Tool.coDau2KoDau(name).trim();
		Matcher matcher = pattern.matcher(name);
		String url_rewrite=matcher.replaceAll("-");
		matcher = pattern2.matcher(url_rewrite);
		url_rewrite = matcher.replaceAll("");		
		product.url_rewrite = url_rewrite;
		
		String strDes = writer.toXMLString(description);
		if (strDes != null
				&& strDes.length() > "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
						.length()) {
			strDes = strDes
					.substring("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
							.length());
			if (strDes.length() > 60530)
				strDes = HtmlUtil.removeTag(strDes);
		}
		int length = strDes.length() > 60530 ? 60530 : strDes.length();
		product.description = strDes.substring(0, length);
		product.status = 1;
		product.is_multi = is_multi;
		product.cat_id = cat_az_id;
		product.original_link = url;

		// Lay Hinh Anh
		String xpath_anh = "//div[@class='picture_thumbnail_list']/div[1]/table[@class='picture_thumbnail']/TBODY/tr[1]/td[1]/a[1]/@href";
		String node = (String) reader.read(xpath_anh, XPathConstants.STRING);
		System.out.println(node);

		xpath_anh = "//div[@class='picture_thumbnail_list']/div[1]/table[@class='picture_thumbnail']/TBODY/tr[1]/td";
		NodeList nodes_image = (NodeList) reader.read(xpath_anh,
				XPathConstants.NODESET);
		int node_one_many = nodes_image.getLength();
		if (node_one_many == 0) {
			xpath_anh = "//div[@class='picture_thumbnail_list']/table[@class='picture_thumbnail']/TBODY/tr[1]/td";
			nodes_image = (NodeList) reader.read(xpath_anh,
					XPathConstants.NODESET);
			node_one_many = nodes_image.getLength();
		}
		int i = 1;
		String sub_xpath = "";
		List<ProductImage> listImage = new ArrayList<ProductImage>();
		ProductImage productImage = null;
		/*ProductDownloadImage downloadImage = new ProductDownloadImage(
				"http://www.vatgia.com/", "http://www.vatgia.com/");*/
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH) + 1;
		String day = calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0"
				+ calendar.get(Calendar.DAY_OF_MONTH) : ""
				+ calendar.get(Calendar.DAY_OF_MONTH);
		String strmonth = month < 10 ? "0" + month : "" + month;
		String dayPath = calendar.get(Calendar.YEAR) + "/" + strmonth + day;

		while (i <= node_one_many) {
			productImage = new ProductImage();
			String image_path = (String) reader.read(xpath_anh + "[" + i + "]"
					+ "/a[1]/@href", XPathConstants.STRING);
			System.out.println(image_path);
			String fileName = image_path
					.substring(image_path.lastIndexOf("/") + 1);
			fileName = Calendar.getInstance().getTimeInMillis() + fileName;
			productImage.name = "small_" + fileName;

			productImage.path = pre_folder + dayPath + "/" + productImage.name;
			productImage.product_id = 0;
			pattern = Pattern.compile("http://");
			matcher = pattern.matcher(image_path);
			if (!matcher.find())
				image_path = "http://www.vatgia.com" + image_path;
			 FileLog.writerLine(image_path + ";" + fileName);

  		    // ProductDownloadImage.downloadImage(image_path, "/usr/src/java/tomcat7/webapps/images.az24.vn/picture_model/"+dayPath+"/", fileName);
			// /ProductDownloadImage.downloadImage(image_path,
			// "/data/crawler/data/anh/"+dayPath+"/", fileName);
			// ProductDownloadImage.downloadImage(image_path, "d:\\anh2\\",
			// fileName);
			 
			if (i == 1) {
				product.pictrue = productImage.path;
				productImage.is_main = 1;
			}
			listImage.add(productImage);
			i++;
		}

		// Xu Ly Thuoc Tinh
		String xpath_tech = "//div[@id='technical_product']/table[@class='product_technical_table']/TBODY[1]/tr";
		NodeList node_techs = (NodeList) reader.read(xpath_tech,
				XPathConstants.NODESET);
		node_one_many = node_techs.getLength();
		i = 1;
		sub_xpath = "";
		pattern = Pattern.compile("\\•", Pattern.CASE_INSENSITIVE);
		matcher = null;
		List<ProductField> productFields = new ArrayList<ProductField>();
		ProductField productField = null;
		while (i <= node_one_many) {
			String tech_lable = (String) reader.read(xpath_tech + "[" + i + "]"
					+ "/td[1]/span[1]/text()", XPathConstants.STRING);
			if (!StringUtil.isEmpty(tech_lable)) {
				productField = new ProductField();
				productField.name = tech_lable;

				Node tech_value = (Node) reader.read(xpath_tech + "[" + i + "]"
						+ "/td[2]", XPathConstants.NODE);

				matcher = pattern.matcher(tech_value.getTextContent().trim());
				String value = matcher.replaceAll(";");

				productField.value = value;
				productFields.add(productField);

				System.out.println(tech_lable + "="
						+ tech_value.getTextContent().trim());
				System.out.println("-->" + value.trim());
				System.out.println("-->" + writer.toXMLString(tech_value));
			}

			i++;
		}

		// Xu ly Product store
		NodeList nodes = (NodeList) reader.read(xpath_price,
				XPathConstants.NODESET);
		node_one_many = nodes.getLength();
		i = 1;
		sub_xpath = "";
		java.util.regex.Pattern pattern_price = Pattern.compile("\\D");
		matcher = null;
		double price = 0;
		while (i <= node_one_many) {
			String price_label = (String) reader.read(xpath_price + "[" + i
					+ "]" + "/td[1]/text()", XPathConstants.STRING);
			sub_xpath = "/td[2]/text()";
			if ("Giá chưa VAT:".equalsIgnoreCase(price_label))
				sub_xpath = "/td[2]/b[1]/text()";
			if ("Giá bán cuối:".equalsIgnoreCase(price_label)||"Giá sản phẩm:".equalsIgnoreCase(price_label))
				sub_xpath = "/td[2]/b[1]/text()";

			String price_value = (String) reader.read(xpath_price + "[" + i
					+ "]" + sub_xpath, XPathConstants.STRING);
			matcher = pattern_price.matcher(price_value);
			price_value = matcher.replaceAll("");
			if (!StringUtil.isEmpty(price_value)) {

				price = Double.parseDouble(price_value);
			}

			pattern = Pattern.compile("\\+10%");
			matcher = pattern.matcher(price_label);
			if (matcher.find())
				productStore.vat = 1;
			if ("Giá chưa VAT:".equalsIgnoreCase(price_label)) {
				productStore.price = price;
				productStore.vat = 0;
				product.price_from = price;
			}
			if ("Giá bán cuối:".equalsIgnoreCase(price_label)||"Giá sản phẩm:".equalsIgnoreCase(price_label)) {
				productStore.price = price;
				productStore.vat = 1;
				product.price_from = price;
			}

			i++;
		}
		productStore.price_type = 1;

		nodes = (NodeList) reader.read(xpath_soluong, XPathConstants.NODESET);
		node_one_many = nodes.getLength();
		i = 1;
		sub_xpath = "";
		int is_node = 0;
		while (i <= node_one_many) {
			String price_label = (String) reader.read(xpath_soluong + "[" + i
					+ "]" + "/td[1]/span[1]/text()", XPathConstants.STRING);
			sub_xpath = "/td[2]/text()";
			is_node = 0;
			if ("Đặt hàng".equalsIgnoreCase(price_label)) {
				sub_xpath = "/td[2]";
				is_node = 1;
			}
			if ("Xem thêm".equalsIgnoreCase(price_label)) {
				sub_xpath = "/td[2]/a[1]/@href";
				is_node = 0;
			}
			String price_value = "";
			if (is_node == 0) {
				price_value = (String) reader.read(xpath_soluong + "[" + i
						+ "]" + sub_xpath, XPathConstants.STRING);
			} else {
				Node price_value_node = (Node) reader.read(xpath_soluong + "["
						+ i + "]" + sub_xpath, XPathConstants.NODE);
				price_value = price_value_node.getTextContent();
			}

			price_value = price_value.trim();

			if ("Đặt hàng".equalsIgnoreCase(price_label)) {
				pattern_price = Pattern.compile("\\D");
				matcher = pattern_price.matcher(price_value);
				int sl = Integer.parseInt(matcher.replaceAll(""));
				productStore.quantity = sl;
				System.out.println("So Luong SP" + sl);
			}
			if ("Bảo hành".equalsIgnoreCase(price_label)) {
				pattern_price = Pattern.compile("\\D");
				matcher = pattern_price.matcher(price_value);
				int bh = Integer.parseInt(matcher.replaceAll(""));
				productStore.ensure_month = bh;
			}
			if ("Tình trạng".equalsIgnoreCase(price_label)) {
				if ("Moi".equalsIgnoreCase(UTF8Tool.coDau2KoDau(price_value))) {
					productStore.is_new = 1;
					productStore.quanlity = 1;
				} else {
					productStore.is_new = 0;
					productStore.quanlity = 0;
				}
			}

			if ("Xuất xứ".equalsIgnoreCase(price_label)) {
				if ("chinh hang".equalsIgnoreCase(UTF8Tool
						.coDau2KoDau(price_value)))
					productStore.origin = 1;
				if ("xach tay".equalsIgnoreCase(UTF8Tool
						.coDau2KoDau(price_value)))
					productStore.origin = 2;
				if ("hang cong ty".equalsIgnoreCase(UTF8Tool
						.coDau2KoDau(price_value)))
					productStore.origin = 3;
			}

			if ("Vận chuyển".equalsIgnoreCase(price_label)) {
				if ("lien he".equalsIgnoreCase(UTF8Tool
						.coDau2KoDau(price_value)))
					productStore.ship_type = 4;
				if ("co dinh".equalsIgnoreCase(UTF8Tool
						.coDau2KoDau(price_value)))
					productStore.ship_type = 3;
				if ("mien phi noi thanh".equalsIgnoreCase(UTF8Tool
						.coDau2KoDau(price_value)))
					productStore.ship_type = 1;
				if ("mien phi".equalsIgnoreCase(UTF8Tool
						.coDau2KoDau(price_value)))
					productStore.ship_type = 1;
			}

			if ("Xem thêm".equalsIgnoreCase(price_label)) {
				productStore.url_link_web = price_value;
				System.out.println("url_link_web" + price_value);
			}

			System.out.println(price_label.trim());
			System.out.println(price_value.trim());
			i++;
		}

		xpath__description = "//div[@class='product_detail_description']";
		Node node_description = (Node) reader.read(xpath__description,
				XPathConstants.NODE);

		strDes = writer.toXMLString(node_description);
		if (strDes != null
				&& strDes.length() > "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
						.length()) {
			strDes = strDes
					.substring("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
							.length());
			if (strDes.length() > 60530)
				strDes = HtmlUtil.removeTag(strDes);
		}
		calendar = Calendar.getInstance();
		length = strDes.length() > 60530 ? 60530 : strDes.length();
		productStore.description = strDes.substring(0, length);
		productStore.create_date = calendar.getTimeInMillis() / 1000;
		productStore.modify_date = calendar.getTimeInMillis() / 1000;
		productStore.create_user = "auto";
		productStore.modify_user = "auto";

		productStore.status = 1;
		productStore.store_id = store_id;
		productStore.store_alias = store_alias;
		productStore.original_link = url;

		if (holder.containsKey(1))
			product.cat_id1 = holder.get(1);
		else
			product.cat_id1 = 0;
		if (holder.containsKey(2))
			product.cat_id2 = holder.get(2);
		else
			product.cat_id2 = 0;
		if (holder.containsKey(3))
			product.cat_id3 = holder.get(3);
		else
			product.cat_id3 = 0;
		if (holder.containsKey(4))
			product.cat_id4 = holder.get(4);
		else
			product.cat_id4 = 0;
		if (holder.containsKey(5))
			product.cat_id5 = holder.get(5);
		else
			product.cat_id5 = 0;

		ProductDAO productDAO = new ProductDAO();
		int product_id = 0;
		
		product_id = productDAO.getProductByName(name, cat_az_id);
		if(product_id==0)
		{
			product_id = productDAO.saveProduct(product);
			if (product_id < 1)
				return 0;
	
			// Luu Hinh Anh
			for (ProductImage productImageT : listImage) {
				productImageT.product_id = product_id;
				productDAO.savePicture(productImageT);
			}
	
			// Luu Thuoc Tinh
			ProductField field = null;
			ProductData productData = null;
			String fieldName = "";
			String fieldLabel = "";
			Pattern pattern3 = null;
			Matcher matcher2 = null;
			String valueField="";
			for (ProductField productFieldT : productFields) {
				if (StringUtil.isEmpty(productFieldT.value))
					continue;
				fieldLabel = UTF8Tool.coDau2KoDau(productFieldT.name.trim());								
				pattern3 = Pattern.compile("^\\W|\\W$");
				matcher2 = pattern3.matcher(fieldLabel);
				fieldLabel=matcher2.replaceAll("");
				
				i = 0;
				System.out.println("Label=" + fieldLabel);
				while (i < listProductField.size()) {
					fieldName = UTF8Tool.coDau2KoDau(listProductField.get(i).name.trim());
					pattern3 = Pattern.compile("^\\W|\\W$");
					matcher2 = pattern3.matcher(fieldName);
					fieldName=matcher2.replaceAll("");
					
					System.out.println("Fiel Name=" + fieldName);
					if (fieldName.equalsIgnoreCase(fieldLabel)) {
						field = listProductField.get(i);
						break;
					}
					i++;
				}
	
				if (field == null) {
					continue;
				}
	
				productData = new ProductData();
				productData.field_id = field.id;
				productData.product_id = product_id;
				if (field.type == 2) {
					String arrValue[] = productFieldT.value.split(";");
					int intValue = 0;
					for (String string : arrValue) {
						int k = 0;
						pattern3 = Pattern.compile("^\\W|\\W$");
						matcher2 = pattern3.matcher(string);
						string=matcher2.replaceAll("");
						
						while (k < listProductFieldValue.size()) {
							valueField = UTF8Tool.coDau2KoDau(listProductFieldValue.get(k).label.trim());
							pattern3 = Pattern.compile("^\\W|\\W$");
							matcher2 = pattern3.matcher(valueField);
							valueField=matcher2.replaceAll("");
							
							if (valueField.equalsIgnoreCase(string)) {
								intValue += listProductFieldValue.get(k).value;
								break;
							}
							k++;
						}
	
					}
					productData.value = intValue + "";
				} else if (field.type == 1 || field.type == 7 || field.type == 4) {
					int q = 0;
					productFieldT.value = UTF8Tool.coDau2KoDau(productFieldT.value.trim());
					pattern3 = Pattern.compile("^\\W|\\W$");
					matcher2 = pattern3.matcher(productFieldT.value);
					productFieldT.value=matcher2.replaceAll("");
					
					while (q < listProductFieldValue.size()) {
						valueField = UTF8Tool.coDau2KoDau(listProductFieldValue.get(q).label.trim());
						pattern3 = Pattern.compile("^\\W|\\W$");
						matcher2 = pattern3.matcher(valueField);
						valueField=matcher2.replaceAll("");
						
						if (valueField.equalsIgnoreCase(productFieldT.value)) {
							productData.value = listProductFieldValue.get(q).value
									+ "";
							break;
						}
						q++;
					}
	
				} else if (field.type == 3) {
					productData.value = String.valueOf(productFieldT.value
							.indexOf("true") > -1 ? 1 : 0);
				} else {
					productData.value = productFieldT.value;
				}
	
				if (productData.value != null)
					productDAO.saveProductData(productData);
	
			}
		}
		ProductStoreDAO productStoreDAO = new ProductStoreDAO();
		ProductStoreCatDAO productStoreCatDAO = new ProductStoreCatDAO();
		productStore.product_id = product_id;
		int product_store_id = productStoreDAO.saveProductStore(productStore);
		if (product_store_id > 0) {
		ProductStoreCat productStoreCat = new ProductStoreCat();
		productStoreCat.product_id = product_id;
		productStoreCat.product_store_id = product_store_id;
		productStoreCat.store_id = store_id;
		productStoreCat.store_cat_id1 = store_cat_id_1;
		productStoreCat.store_cat_id2 = store_cat_id_2;
		productStoreCatDAO.saveProductStoreCate(productStoreCat);
		}
		ImporterDAO importerDAO = new ImporterDAO();
		String id_url = new UriID(new HttpURL(url)).getIdAsString();
		importerDAO.saveLog(product_id, product_store_id, store_cat_id_2,
				id_url, url, "vat gia",store_id);
		importerDAO.saveLogProduct("vat gia", product_id, product.cat_id,
				id_url, url);
		return 1;
	}

	public static void main(String[] args) throws Exception {
		CrawlerStore crawlerStore = new CrawlerStore();
		CategoryExtractor categoryExtractor = new CategoryExtractor();

		/*String urlGH = args[0];
		String store_alias = args[2];
		String store_alias_n = args[3];
		String type_menu = args[4];*/
		
		StoreDAO storeDAO = new StoreDAO();
		
		String urlGH =  "http://www.vatgia.com/trungtamphanphoionap";
		String store_alias = "congtyphuochung";
		String store_alias_n = "trungtamphanphoionap";
		String type_menu = "1";
		int store_id = storeDAO.checkStore(store_alias);	
		 
		int type = Integer.parseInt(type_menu);
		List<Url> urlCate = null;
		categoryExtractor.getCategoryInAz24("az24_cate.xls");
		switch (type) {
		case 1:
			urlCate = categoryExtractor.extractCategoryGHType1(urlGH, store_id,
					false);
			break;
		case 2:
			urlCate = categoryExtractor.extractCategoryGHType2(urlGH, store_id,
					false);
			break;
		case 3:
			urlCate = categoryExtractor.extractCategoryGHType3(urlGH, store_id,
					false);
			break;
		case 4:
			urlCate = categoryExtractor.extractCategoryGHType4(urlGH, store_id,
					false);
			break;
		default:
			urlCate = categoryExtractor.extractCategoryGHType1(urlGH, store_id,
					false);
			break;
		}
		crawlerStore.extractProductGH(urlCate, store_id, store_alias,store_alias_n);

	}

}
