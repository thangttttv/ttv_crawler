package com.az24.crawler.store;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.DocumentAnalyzer;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.util.html.A;
import hdc.util.html.HttpURL;
import hdc.util.html.NodeDeleteVisitor;
import hdc.util.html.NodeNormalizeVisitor;
import hdc.util.html.parser.DomWriter;
import hdc.util.html.parser.XPathReader;
import hdc.util.lang.UriID;
import hdc.util.text.HtmlUtil;
import hdc.util.text.StringUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathConstants;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.az24.crawler.config.BeanXmlConfig;
import com.az24.crawler.fiter.ContentFilter;
import com.az24.crawler.model.ImageConfig;
import com.az24.crawler.model.Property;
import com.az24.crawler.model.product.Product;
import com.az24.crawler.model.product.ProductImage;
import com.az24.crawler.product.ProductDownloadImage;
import com.az24.dao.CategoryDAO;
import com.az24.dao.ImporterDAO;
import com.az24.dao.ProductStoreCatTempDAO;
import com.az24.dao.ProductStoreTempDAO;
import com.az24.dao.ProductTempDAO;
import com.az24.dao.StoreCagegoryDAO;
import com.az24.dao.StoreDAO;
import com.az24.test.HttpClientFactory;
import com.az24.test.HttpClientUtil;
import com.az24.util.Constants;
import com.az24.util.Logger;
import com.az24.util.UTF8Tool;

public class CrawlerProductTemp {

	private com.az24.util.Logger logger = new Logger(this.getClass().getName());
	private Product product = null;
	private ProductStore productStore = null;
	public String urlDomain = "";	
	public int type_get_html=1;
	private int time_sleep = 1000;

	private NodeDeleteVisitor deleteVisitor = new NodeDeleteVisitor() {
		protected boolean shouldDelete(Node node) {
			if (node.getNodeName().equalsIgnoreCase("meta"))
				return true;
			else if (node.getNodeName().equalsIgnoreCase("link"))
				return true;
			else if (node.getNodeName().equalsIgnoreCase("style"))
				return true;
			else if (node.getNodeName().equalsIgnoreCase("script"))
				return true;
			return false;
		}
	};

	private NodeNormalizeVisitor normalVisitor = new NodeNormalizeVisitor() {
		protected void normalize(Node node) {
			if (node != null && node.hasAttributes()) {
				NamedNodeMap attributes = node.getAttributes();
				if (attributes.getNamedItem("class") != null)
					attributes.removeNamedItem("class");
				if (attributes.getNamedItem("id") != null)
					attributes.removeNamedItem("id");
				if (attributes.getNamedItem("style") != null)
					attributes.removeNamedItem("style");
				if (attributes.getNamedItem("height") != null)
					attributes.removeNamedItem("height");
				if (attributes.getNamedItem("width") != null)
					attributes.removeNamedItem("width");
				if (attributes.getNamedItem("onclick") != null)
					attributes.removeNamedItem("onclick");
				if (attributes.getNamedItem("title") != null)
					attributes.removeNamedItem("title");
				if (attributes.getNamedItem("rel") != null)
					attributes.removeNamedItem("rel");
				if (attributes.getNamedItem("target") != null)
					attributes.removeNamedItem("target");
				if (node.getNodeName().equalsIgnoreCase("img")
						&& attributes.getNamedItem("src") != null) {
					Pattern pattern = Pattern.compile("http://|www");
					Matcher matcher = pattern.matcher(node.getAttributes()
							.getNamedItem("src").getTextContent());
					if (!matcher.find())
						node.getAttributes().getNamedItem("src")
								.setTextContent(
										urlDomain
												+ node.getAttributes()
														.getNamedItem("src")
														.getTextContent());
				}
			}
		}
	};

	private void setProductField(String value, Property property) {
		if ("name".equalsIgnoreCase(property.getName())) {
			product.name = value.trim();
			product.url_rewrite = StringUtil.getAlias(value);
		}
		if ("description".equalsIgnoreCase(property.getName())) {
				product.description = value;
		}
		if ("price".equalsIgnoreCase(property.getName())&& !StringUtil.isEmpty(value)) {
			String name_pattern = "\\D";
			Pattern pattern = Pattern.compile(name_pattern);
			Matcher matcher = pattern.matcher(value);
			value = matcher.replaceAll("");			
			try {
				product.price_from = Double.parseDouble(value);
			} catch (Exception e) {
				product.price_from = 0;
			}
		}

	}

	private void setProductStoreField(String value, Property property) {

		if ("price".equalsIgnoreCase(property.getName())
				&& !StringUtil.isEmpty(value)) {
			String name_pattern = "\\D";
			Pattern pattern = Pattern.compile(name_pattern);
			Matcher matcher = pattern.matcher(value);
			value = matcher.replaceAll("");
			try {
				productStore.price = Double.parseDouble(value);
			} catch (Exception e) {
				productStore.price = 0;
			}
			productStore.price_type = 1;
			productStore.vat = 0;
		}
		
		
		if ("ensure_month".equalsIgnoreCase(property.getName())
				&& !StringUtil.isEmpty(value)) {
			String name_pattern = "\\D";
			Pattern pattern = Pattern.compile(name_pattern);
			Matcher matcher = pattern.matcher(value);
			value = matcher.replaceAll("");
			try {
				productStore.ensure_month = Integer.parseInt(value);
			} catch (Exception e) {
				productStore.ensure_month = 0;
			}
		}

		if ("quantity".equalsIgnoreCase(property.getName())
				&& !StringUtil.isEmpty(value)) {
			String name_pattern = "\\D";
			Pattern pattern = Pattern.compile(name_pattern);
			Matcher matcher = pattern.matcher(value);
			value = matcher.replaceAll("");
			try {
				productStore.quantity = Integer.parseInt(value);
			} catch (Exception e) {
				productStore.quantity = 0;
			}
		}

		if ("quanlity".equalsIgnoreCase(property.getName())) {
			String name_pattern = "\\Wmoi\\W ";
			Pattern pattern = Pattern.compile(name_pattern);
			Matcher matcher = pattern.matcher(UTF8Tool.coDau2KoDau(value)
					.trim());

			if (matcher.find()) {
				productStore.is_new = 1;
				productStore.quanlity = 1;
			} else {
				productStore.is_new = 0;
				productStore.quanlity = 0;
			}
		}

		if ("transport".equalsIgnoreCase(property.getName())) {
			if ("lien he".equalsIgnoreCase(UTF8Tool.coDau2KoDau(value)))
				productStore.ship_type = 4;
			if ("co dinh".equalsIgnoreCase(UTF8Tool.coDau2KoDau(value)))
				productStore.ship_type = 3;
			if ("mien phi noi thanh".equalsIgnoreCase(UTF8Tool
					.coDau2KoDau(value)))
				productStore.ship_type = 1;
			if ("mien phi".equalsIgnoreCase(UTF8Tool.coDau2KoDau(value)))
				productStore.ship_type = 1;
		}

		if ("madein".equalsIgnoreCase(property.getName())) {
			if ("chinh hang".equalsIgnoreCase(UTF8Tool.coDau2KoDau(value)))
				productStore.origin = 1;
			if ("xach tay".equalsIgnoreCase(UTF8Tool.coDau2KoDau(value)))
				productStore.origin = 2;
			if ("hang cong ty".equalsIgnoreCase(UTF8Tool.coDau2KoDau(value)))
				productStore.origin = 3;
		}

		if ("website".equalsIgnoreCase(property.getName())) {
			productStore.url_link_web = value;
		}

		if ("description".equalsIgnoreCase(property.getName())) {
				productStore.description = value;
		}

	}

	private boolean processProperty(XPathReader reader, String url,
			List<Property> propeties, int store_id, String store_alias,
			int is_multi, int cat_az_id, int store_cat_id_1,
			int store_cat_id_2, int vat, Map<Integer, Integer> holder) {
		logger.log("---->Process Property:");
		String str = "";
		boolean filter = false;

		try {

			if (propeties != null) {
				NodeList nodes = null;
				DomWriter writer = new DomWriter();

				for (Property property : propeties) {

					if (property.getNode_type().equalsIgnoreCase("nodeset")) {
						// Phai Xoa Node O Cuoi Cua Process
						List<hdc.crawler.Node> nodeDelByXpath = property.getNodedelByXpaths();
						if (nodeDelByXpath != null) {
							for (hdc.crawler.Node node2 : nodeDelByXpath) {
								CrawlerUtil.removeNodeByXpath(reader,
										node2.xpath);
							}
						}

						String xpath = property.getXpath();
						if (!StringUtil.isEmpty(xpath)) {
							nodes = (NodeList) reader.read(xpath,XPathConstants.NODESET);
						}

						if (nodes == null)
							continue;
						deleteVisitor.traverse(nodes.item(0));
						normalVisitor.traverse(nodes.item(0));
						str = writer.toXMLString(nodes.item(0));
						str = StringUtil.getTextFromNode(str);
						int length = str.length();
						if(length>60530)
							str = HtmlUtil.removeTag(str);
						
					} else if (property.getNode_type().equalsIgnoreCase("node")) {
						
						String xpath = property.getXpath();
						if (!StringUtil.isEmpty(xpath)) {
							Node node = (Node) reader.read(xpath,XPathConstants.NODE);
							str = writer.toXMLString(node);
						}
						logger.log("---->Process Property:"	+ property.getName() + "=" + str);
						str = StringUtil.getTextFromNode(str);
						int length = str.length();
						if(length>60530)
							str = HtmlUtil.removeTag(str);
						
					} else {
						
						if (property.getNode_type().equalsIgnoreCase("string"))
							str = (String) reader.read(property.getXpath(),	XPathConstants.STRING);
						if ("1".equalsIgnoreCase(property.getFilter()))
							filter = ContentFilter.filter(str);
						if (filter)
							return true;

						if ("1".equalsIgnoreCase(property.getChangeLink()))
							str = ContentFilter.changeLink(str);
						logger.log("---->Process Property:"+ property.getName() + "=" + str);
						
					}

					setProductField(str, property);
					setProductStoreField(str, property);

				}

				product.status = 0;
				product.is_multi = is_multi;
				product.cat_id = cat_az_id;
				product.original_link = url;

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
				productStore.vat = vat;

				Calendar calendar = Calendar.getInstance();
				productStore.create_date = calendar.getTimeInMillis() / 1000;
				productStore.modify_date = calendar.getTimeInMillis() / 1000;
				productStore.create_user = "auto";
				productStore.modify_user = "auto";
				productStore.price_type = 1;
				productStore.status = 0;
				productStore.store_id = store_id;
				productStore.store_alias = store_alias;
				productStore.url_link_web = url;
				productStore.original_link = url;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filter;
	}

	private List<ProductImage> processImage(XPathReader reader,
			List<ImageConfig> images, String url_content, String urlDomain)
			throws Exception {

		if (images.size() == 0 || images == null)
			return null;

		logger.log("---->Process Images:");
		String day = "";
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH) + 1;
		day = calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0"
				+ calendar.get(Calendar.DAY_OF_MONTH) : ""
				+ calendar.get(Calendar.DAY_OF_MONTH);
		String strmonth = month < 10 ? "0" + month : "" + month;
		long timeInMilis = Calendar.getInstance().getTimeInMillis();

		String dayPath = calendar.get(Calendar.YEAR) + "/" + strmonth + day;
		ProductDownloadImage downloadImage = new ProductDownloadImage(
				urlDomain, urlDomain);
		String dis_folder = Constants.STORE_DIS_FOLDER + dayPath + "/";
		String pre_folder = "/picture_model/";
		List<ProductImage> listImage = new ArrayList<ProductImage>();
		String name = "";
		NodeList nodes = null;
		ProductImage productImage = null;
		Pattern p = null;
		Matcher m = null;
		for (ImageConfig imageConfig : images) {

			if (imageConfig.node_type.equalsIgnoreCase("nodeset")) {
				nodes = (NodeList) reader.read(imageConfig.xpath,
						XPathConstants.NODESET);
				int node_one_many = nodes.getLength();

				int i = 1;

				while (i <= node_one_many) {
					String src = (String) reader.read(imageConfig.xpath + "["
							+ i + "]" + imageConfig.xpath_sub,
							XPathConstants.STRING);

					String image_name = "";
					image_name = src.substring(src.lastIndexOf("/") + 1);
					p = Pattern.compile("\\s+");
					m = p.matcher(image_name);
					image_name = m.replaceAll("_");
					String dau = image_name.substring(0, image_name
							.lastIndexOf("."));
					String duoi = image_name.substring(image_name
							.lastIndexOf(".") + 1,
							image_name.lastIndexOf(".") + 4);
					p = Pattern.compile("jpg|png|gif|bmp");
					m = p.matcher(duoi);
					if (!m.find()) {
						duoi = "jpg";
					}
					name = new StringBuilder(String.valueOf(dau)).append(".")
							.append(duoi).toString();

					productImage = new ProductImage();
					productImage.name = "small_" + timeInMilis + name;
					productImage.path = pre_folder + dayPath + "/"
							+ productImage.name;
					productImage.product_id = 0;

					if (i == 1 && !StringUtil.isEmpty(src)) {
						productImage.is_main = 1;
						product.pictrue = pre_folder + dayPath + "/"
								+ productImage.name;
					}

					Pattern pattern = Pattern.compile("http://");
					Matcher matcher = pattern.matcher(src);
					if (!matcher.find())
						src = urlDomain + "/" + src;
					downloadImage.downloadImage(src, dis_folder, timeInMilis
							+ name);
					listImage.add(productImage);

					i++;

				}
				if (node_one_many > 0)
					break;
			}
			if (listImage.size() > 0)
				return listImage;
			if (imageConfig.node_type.equalsIgnoreCase("string")) {

				String src = (String) reader.read(imageConfig.xpath,
						XPathConstants.STRING);

				/*
				 * String spattern = "=(.*)&width"; // Create a Pattern object
				 * Pattern r = Pattern.compile(spattern); // Now create matcher
				 * object. Matcher m = r.matcher(src); if (m.find( )) {
				 * System.out.println("Found value: " + m.group(0) );
				 * System.out.println("Found value: " + m.group(1) ); } else {
				 * System.out.println("NO MATCH"); } src = m.group(1);
				 */

				name = src.substring(src.lastIndexOf("/") + 1);

				productImage = new ProductImage();
				productImage.name = "small_" + timeInMilis + name;
				productImage.path = pre_folder + dayPath + "/"
						+ productImage.name;
				productImage.product_id = 0;
				productImage.is_main = 1;

				product.pictrue = pre_folder + dayPath + "/"
						+ productImage.name;
				Pattern pattern = Pattern.compile("\\.\\.");
				Matcher matcher = pattern.matcher(src);
				src = matcher.replaceAll("");

				pattern = Pattern.compile("http://");
				matcher = pattern.matcher(src);
				if (!matcher.find())
					src = urlDomain + "/" + src;
				downloadImage
						.downloadImage(src, dis_folder, timeInMilis + name);
				listImage.add(productImage);
			}

		}

		return listImage;
	}

	public void export_category(String store_alias) throws Exception {
		StoreCagegoryDAO categoryDAO = new StoreCagegoryDAO();
		StoreDAO storeDAO = new StoreDAO();
		int store_id = storeDAO.checkStore(store_alias);
		WritableWorkbook workbook = Workbook.createWorkbook(new java.io.File(
				"d:/categorys2/store_category_" + store_alias + "_" + store_id
						+ ".xls"));
		WritableSheet s = workbook.createSheet("Sheet_" + 1, 0);
		List<com.az24.crawler.store.Category> listCat = categoryDAO
				.getCategoryByStoreId(store_id);
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
		l = new Label(1, 0, "Parent ID", cf);
		s.addCell(l);
		l = new Label(2, 0, "Cate Az24 ID", cf);
		s.addCell(l);
		l = new Label(3, 0, "Name", cf);
		s.addCell(l);
		l = new Label(4, 0, "URL", cf);
		s.addCell(l);
		l = new Label(5, 0, "Multi Sell", cf);
		s.addCell(l);
		int row = 1;

		for (Category category : listCat) {
			l = new Label(0, row, category.id + "", cf2);
			s.addCell(l);
			l = new Label(1, row, category.parent_id + "", cf2);
			s.addCell(l);
			l = new Label(2, row, category.cat_id + "", cf2);
			s.addCell(l);
			l = new Label(3, row, category.name + "", cf2);
			s.addCell(l);
			l = new Label(4, row, category.url != null ? category.url : "", cf2);
			s.addCell(l);
			l = new Label(5, row, category.is_multi_sell + "", cf2);
			s.addCell(l);
			row++;
		}
		workbook.write();
		workbook.close();
	}

	public List<Category> getCategory(String path_cate_excel)
			throws BiffException, IOException {
		List<Category> listCate = new ArrayList<Category>();
		// URL url = CrawlerStore.class.getResource(path_cate_excel);
		// String realPathFile = url.getFile().replaceAll("%20", " ");
		// FileInputStream propsFile = new
		// java.io.FileInputStream(realPathFile);
		FileInputStream propsFile = new java.io.FileInputStream(path_cate_excel);
		Workbook w = Workbook.getWorkbook(propsFile);
		Sheet sheet = w.getSheet(0);
		Cell cell = null;
		// Vat_Gia_Cat| Az24_Cat
		Category category = null;
		for (int i = 1; i < sheet.getRows(); i++) {
			try {
				category = new Category();
				// Cate_store_id
				cell = sheet.getCell(0, i);
				int cate_store_id = Integer.parseInt(cell.getContents().trim());
				// Cate Parent Store ID
				cell = sheet.getCell(1, i);
				int cate_parent_store_id = Integer.parseInt(cell.getContents()
						.trim());
				// Cate Az ID
				cell = sheet.getCell(2, i);
				int cate_az_id = Integer.parseInt(cell.getContents().trim());
				// Name
				cell = sheet.getCell(3, i);
				String name = cell.getContents().trim();
				// Url
				// Name
				cell = sheet.getCell(4, i);
				String url_product = cell.getContents().trim();
				// is multi sell
				cell = sheet.getCell(5, i);
				int is_multi_sell = Integer.parseInt(cell.getContents().trim());

				cell = sheet.getCell(6, i);
				String regex = cell.getContents().trim();

				
				cell = sheet.getCell(7, i);
				if(!StringUtil.isEmpty(cell.getContents())) {
					String url_page =
					cell.getContents().trim(); category.url_page = url_page; 
				}
				 
				category.cat_id = cate_az_id;
				category.id = cate_store_id;
				category.parent_id = cate_parent_store_id;
				category.name = name;
				category.url = url_product;
				category.is_multi_sell = is_multi_sell;
				category.regex = regex;

				listCate.add(category);
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
		return listCate;
	}

	private String getHTML_Get(String url)
	{
		String html="";
		try {
			HttpClientImpl client = new HttpClientImpl();
			HttpResponse res =  client.fetch(url);	
			html = HttpClientUtil.getResponseBody(res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return html;	
	}
	
	private String getHTML_Post(String url,List<NameValuePair> list)
	{
		 	String html="";
			try {
				DefaultHttpClient client = HttpClientFactory.getInstance() ;
		        client.getParams().setParameter("application/x-www-form-urlencoded",true) ;
		    	HttpPost post = new HttpPost(url) ;
		        post.setEntity(new UrlEncodedFormEntity(list)) ;
		        HttpResponse res;
				res = client.execute(post);
				html = HttpClientUtil.getResponseBody(res) ;
			} catch (ClientProtocolException e) {				
				e.printStackTrace();
			} catch (IOException e) {				
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return html;
	      
	}
	
	private void processLink(List<A> listLinkProduct,Category category,int store_id,String store_alias,Map<Integer, Integer> holder,
			List<Property> propeties,List<ImageConfig> images)
	{
		
		Pattern pattern = null;
		Matcher matcher = null;
		int store_cat_id_1 = category.parent_id;
		int store_cat_id_2 = category.id;
		int is_multi = category.is_multi_sell;
		int cat_az_id = category.cat_id;
		int k = 0;int number_product = 0,page=1;
		HttpResponse res = null;
		HttpClientImpl client = new HttpClientImpl();
		
		ProductTempDAO productTempDAO = new ProductTempDAO();
		ProductStoreTempDAO productStoreTempDAO = new ProductStoreTempDAO();
		ProductStoreCatTempDAO productStoreCatTempDAO = new ProductStoreCatTempDAO();
		ImporterDAO importerDAO = new ImporterDAO();
		
		try{			
			for (A a : listLinkProduct) {
				if (StringUtil.isEmpty(category.regex))
					break;
				a.setUrl(a.getUrl().replaceAll("ampamp;", "&"));
				System.out.println(k + "-" + a.getUrl());
				k++;
				pattern = Pattern.compile(category.regex.toLowerCase());
				matcher = pattern.matcher(a.getUrl().toLowerCase());
				if (!matcher.find())
					continue;
	
				String id_url = new UriID(new HttpURL(a.getUrl())).getIdAsString();
				if (!importerDAO.checkProductStore(id_url,store_cat_id_1)) {
					
					res = client.fetch(a.getUrl());
					String html = HttpClientUtil.getResponseBody(res);
					XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
					this.product = new Product();
					this.productStore = new ProductStore();
					this.processProperty(reader, a.getUrl(),
							propeties, store_id, store_alias, is_multi,
							cat_az_id, store_cat_id_1, store_cat_id_2, 0,
							holder);
	
					if (StringUtil.isEmpty(product.name))
						continue;
					reader = CrawlerUtil.createXPathReaderByData(html);
					List<ProductImage> listImagePro = processImage(reader, images, a.getUrl(),urlDomain);	
					// Save Product
					int product_id = productTempDAO
							.saveProduct(product);
					// Save Product Image
					for (ProductImage productImage : listImagePro) {
						productImage.product_id = product_id;
						productTempDAO.savePicture(productImage);
					}
					if (product_id > 0) {
						productStore.product_id = product_id;
						// Save Product Store
						int product_store_id = productStoreTempDAO.saveProductStore(productStore);	
						ProductStoreCat productStoreCat = new ProductStoreCat();
						productStoreCat.product_id = product_id;
						productStoreCat.product_store_id = product_store_id;
						productStoreCat.store_id = store_id;
						productStoreCat.store_cat_id1 = store_cat_id_1;
						productStoreCat.store_cat_id2 = store_cat_id_2;
						productStoreCatTempDAO.saveProductStoreCate(productStoreCat);
						// Save Log
						importerDAO.saveLog(product_id, product_store_id,
								store_cat_id_1, id_url, a.getUrl(),
								urlDomain, store_id);
	
						// IF Save Product Success
						number_product++;
					}
					try {
						Thread.sleep(time_sleep);
					} catch (InterruptedException e) {					
						e.printStackTrace();
					}
				}
				}		
			page++;
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void processCategory(List<Category> listCate,String urlDomainRewrite,String store_alias, int store_id,List<Property> propeties
			,List<ImageConfig> images,int max_page) throws Exception
	{
		int cat_az_id = 0;
		DocumentAnalyzer analyzer;
		String html = "";
		List<A> listLinkProduct = null;
		CategoryDAO categoryDAO = new CategoryDAO();
		
		for (int i = 0; i < listCate.size(); i++) {
			
			Category category = listCate.get(i);
			System.out.println("Process Category Name = " + category.url);
			cat_az_id = category.cat_id;			
			if (StringUtil.isEmpty(category.url))
			{
				System.out.println("Process Category Null URL ");
				continue;
			}
			
			Map<Integer, Integer> holder = new HashMap<Integer, Integer>();
			categoryDAO.getCategoriesParent(cat_az_id, holder);
			
			// Process Data In Category
			int page = 1;
			while (page <= max_page) {
				// Process Page
				String url_pate = category.url_page != null ? category.url_page.trim() : "";
				String url = category.url.trim() + page + url_pate;
				System.out.println("Process Page  = " + url);
				try {
					// Get Link Product
					html = getHTML_Get(url);						
					analyzer = new DocumentAnalyzer.DefaultDocumentAnayzer(
							urlDomain, urlDomainRewrite);
					listLinkProduct = analyzer.analyze(html, url);
					Thread.sleep(time_sleep);
					if (listLinkProduct.size() == 0)break;
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				processLink(listLinkProduct, category, store_id, store_alias, holder, propeties, images);
				page++;
			}
		}
	}
	
	
	public void processCategory(Category category,String urlDomainRewrite,String store_alias, int store_id,List<Property> propeties
			,List<ImageConfig> images,List<NameValuePair> listParameter ) throws Exception
	{
			int cat_az_id = 0;
			DocumentAnalyzer analyzer;
			String html = "";
			List<A> listLinkProduct = null;
			CategoryDAO categoryDAO = new CategoryDAO();		
		
			System.out.println("Process Category Name = " + category.url);
			cat_az_id = category.cat_id;			
			if (StringUtil.isEmpty(category.url))
			{
				System.out.println("Process Category Null URL ");
				return;
			}			
			Map<Integer, Integer> holder = new HashMap<Integer, Integer>();
			categoryDAO.getCategoriesParent(cat_az_id, holder);			
			// Process Data In Category			
			try {
				// Get Link Product
				html = getHTML_Post(category.url,listParameter);						
				analyzer = new DocumentAnalyzer.DefaultDocumentAnayzer(
						urlDomain, urlDomainRewrite);
				listLinkProduct = analyzer.analyze(html, category.url);
				Thread.sleep(time_sleep);
				if (listLinkProduct.size() == 0)return;
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			processLink(listLinkProduct, category, store_id, store_alias, holder, propeties, images);
				
		
	}
	
	public static void main(String[] args) throws Exception {

		
		StoreDAO storeDAO = new StoreDAO();
		CrawlerProductTemp productExtract = new CrawlerProductTemp();

		String store_alias = args[0];
		String urlDomain = args[1];
		String urlDomainRewrite = args[2];
		String beanconfig = args[3];
		String catefile = args[4];

		/*List<Category> listCate = productExtract.getCategory("D:/store_category_mayvanphongmb_3632.xls");
		BeanXmlConfig beanXmlConfig = new
		BeanXmlConfig("D:/beanProductStoreMayvanphongmb.xml");
		
		  String store_alias = "mayvanphongmb"; String urlDomain =
		  "http://mayvanphongmienbac.vn/"; String urlDomainRewrite =
		  "http://mayvanphongmienbac.vn/";*/
		 
		
		BeanXmlConfig beanXmlConfig = new BeanXmlConfig(beanconfig);
		List<Category> listCate = productExtract.getCategory(catefile);
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
		productExtract.processCategory(listCate, urlDomainRewrite, store_alias, store_id, propeties, images, 10);
	

		
	}

	
}
