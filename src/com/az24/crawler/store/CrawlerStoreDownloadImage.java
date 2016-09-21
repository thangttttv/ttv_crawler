package com.az24.crawler.store;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.util.html.parser.XPathReader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathConstants;

import org.apache.http.HttpResponse;
import org.w3c.dom.NodeList;

import com.az24.crawler.model.product.Product;
import com.az24.crawler.model.product.ProductImage;
import com.az24.crawler.product.ProductDownloadImage;
import com.az24.dao.ProductDAO;
import com.az24.test.HttpClientUtil;

public class CrawlerStoreDownloadImage {
	
	public void extract_product_cb(String url, int product_id) throws Exception
	{
		System.out.println("-->URL SAN Pham " + url);
		
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);
		Thread.sleep(1000);	
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		Product product = new Product();
		Pattern pattern = null;
		Matcher matcher = null;
		String pre_folder = "/picture_model/";
		String picture_main = "";
		
		String image_path = "";
		int i = 1;
		List<ProductImage> listImage = new ArrayList<ProductImage>();
		ProductImage productImage = null;
		ProductDownloadImage downloadImage = new ProductDownloadImage(	"http://www.vatgia.com/", "http://www.vatgia.com/");
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH) + 1;
		String day = calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0"
				+ calendar.get(Calendar.DAY_OF_MONTH) : ""
				+ calendar.get(Calendar.DAY_OF_MONTH);
		String strmonth = month < 10 ? "0" + month : "" + month;
		
		String dayPath = calendar.get(Calendar.YEAR) + "/" + strmonth + day;

		NodeList nodes = (NodeList) reader			.read(
						"HTML/BODY[1]/CENTER[1]/TABLE[@id='content']/TBODY[1]/TR[1]/TD[@id='content_center']/DIV[2]/DIV[3]/TABLE[1]/TBODY[1]/TR[2]/TD[1]/DIV[4]/TABLE[1]/TBODY[1]/TR",
						XPathConstants.NODESET);
		int node_one_many = nodes.getLength();
		i = 1;
		if (node_one_many > 0)
			while (i <= node_one_many) {
				String picture = (String) reader
						.read(
								"HTML/BODY[1]/CENTER[1]/TABLE[@id='content']/TBODY[1]/TR[1]/TD[@id='content_center']/DIV[2]/DIV[3]/TABLE[1]/TBODY[1]/TR[2]/TD[1]/DIV[4]/TABLE[1]/TBODY[1]/TR"
										+ "["
										+ i
										+ "]"
										+ "/TD[1]/DIV[1]/IMG/@src",
								XPathConstants.STRING);
				picture = (String) reader
						.read(
								"HTML/BODY[1]/CENTER[1]/TABLE[@id='content']/TBODY[1]/TR[1]/TD[@id='content_center']/DIV[2]/DIV[3]/TABLE[1]/TBODY[1]/TR[2]/TD[1]/DIV[4]/TABLE[1]/TBODY[1]/TR"
										+ "["
										+ i
										+ "]"
										+ "/TD[2]/DIV[1]/IMG/@src",
								XPathConstants.STRING);
				picture = (String) reader
						.read(
								"HTML/BODY[1]/CENTER[1]/TABLE[@id='content']/TBODY[1]/TR[1]/TD[@id='content_center']/DIV[2]/DIV[3]/TABLE[1]/TBODY[1]/TR[2]/TD[1]/DIV[4]/TABLE[1]/TBODY[1]/TR"
										+ "["
										+ i
										+ "]"
										+ "/TD[3]/DIV[1]/IMG/@src",
								XPathConstants.STRING);
				System.out.println(picture.trim());

				productImage = new ProductImage();
				String fileName = image_path.substring(image_path
						.lastIndexOf("/") + 1);
				fileName = fileName.replace("small_", "");
				fileName = Calendar.getInstance().getTimeInMillis() + fileName;
				productImage.name = "small_" + fileName;
				productImage.path = pre_folder + dayPath + "/"
						+ productImage.name;
				productImage.product_id = 0;
				pattern = Pattern.compile("http://");
				matcher = pattern.matcher(image_path);
				if (!matcher.find())
					image_path = "http://www.vatgia.com" + image_path;
				
				//FileLog.writerLine(image_path+";"+fileName);
				downloadImage.downloadImage(image_path, "/usr/src/java/tomcat7/webapps/images.az24.vn/picture_model/"+dayPath+"/", fileName);
				//ProductDownloadImage.downloadImage(image_path, "d:/anh2/", fileName);
				if (i == 1) {
					product.pictrue = productImage.path;
					picture_main = productImage.path;
					productImage.is_main = 1;
				}
				listImage.add(productImage);

				i++;
			}
		else {
			image_path = (String) reader
					.read(
							"HTML/BODY[1]/CENTER[1]/TABLE[@id='content']/TBODY[1]/TR[1]/TD[@id='content_center']/DIV[2]/DIV[3]/TABLE[1]/TBODY[1]/TR[2]/TD[1]/DIV[@id='view_larger']/A[1]/@href",
							XPathConstants.STRING);
			
 			 String  patternstr = "(.*')(.*)('.*)";
		     // Create a Pattern object
		      Pattern r = Pattern.compile(patternstr);
		     // Now create matcher object.
		     Matcher m = r.matcher(image_path);
		     if (m.find( )) {
		    	 
			image_path ="/pictures_fullsize/"+ m.group(2);
		    //	 image_path ="/user_product_fullsize/"+ m.group(2);
			productImage = new ProductImage();
			String fileName = image_path
					.substring(image_path.lastIndexOf("/") + 1);
			
			fileName = Calendar.getInstance().getTimeInMillis() + fileName;
			fileName = fileName.replace("small_", "");
			productImage.name = "small_" + fileName;
			productImage.path = pre_folder + dayPath + "/" + productImage.name;
			productImage.product_id = 0;
			pattern = Pattern.compile("http://");
			matcher = pattern.matcher(image_path);
			if (!matcher.find())
				image_path = "http://www.vatgia.com" + image_path;
			//FileLog.writerLine(image_path+";"+fileName);
			downloadImage.downloadImage(image_path, "/usr/src/java/tomcat7/webapps/images.az24.vn/picture_model/"+dayPath+"/", fileName);
			//ProductDownloadImage.downloadImage(image_path, "d:/anh2/", fileName);
		
			product.pictrue = productImage.path;
			picture_main = productImage.path;
			productImage.is_main = 1;
			listImage.add(productImage);
		     }
			i++;
		}
		
		ProductDAO productDAO = new ProductDAO();
		productDAO.deletePricture(product_id);
		
		product_id = productDAO.updateImageMain(product_id, picture_main);
			// Luu Hinh Anh
		for (ProductImage productImageT : listImage) {
				productImageT.product_id = product_id;
				productDAO.savePicture(productImageT);
		}
		
		Thread.sleep(1000);	
	}
	
	public int extract_product(String url, int product_id)
			throws Exception {
		
		System.out.println("-->URL SAN Pham " + url);
	
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);
		Thread.sleep(1000);	
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
	
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
		
		List<ProductImage> listImage = new ArrayList<ProductImage>();
		ProductImage productImage = null;
		ProductDownloadImage downloadImage = new ProductDownloadImage("http://www.vatgia.com/", "http://www.vatgia.com/");
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH) + 1;
		String day = calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0"
				+ calendar.get(Calendar.DAY_OF_MONTH) : ""
				+ calendar.get(Calendar.DAY_OF_MONTH);
		String strmonth = month < 10 ? "0" + month : "" + month;
		String dayPath =  calendar.get(Calendar.YEAR)+  "/" + strmonth+day; 
		Product product = new Product();
		Pattern pattern = null;
		Matcher matcher = null;
		String pre_folder = "/picture_model/";
		String picture_main = "";
		while (i <= node_one_many) {
			productImage = new ProductImage();
			String image_path = (String) reader.read(xpath_anh + "[" + i + "]"
					+ "/a[1]/@href", XPathConstants.STRING);
			System.out.println(image_path);
			String fileName = image_path
					.substring(image_path.lastIndexOf("/") + 1);
			fileName = Calendar.getInstance().getTimeInMillis()+fileName;
			productImage.name = "small_" + fileName;

			productImage.path = pre_folder + dayPath + "/" + productImage.name;
			productImage.product_id = 0;
			pattern = Pattern.compile("http://");
			matcher = pattern.matcher(image_path);
			if(!matcher.find()) image_path = "http://www.vatgia.com"+ image_path;
			downloadImage.downloadImage(image_path, "/usr/src/java/tomcat7/webapps/images.az24.vn/picture_model/"+dayPath+"/", fileName);
			//ProductDownloadImage.downloadImage(image_path, "d:/anh2/", fileName);
			if (i == 1) {
				product.pictrue = productImage.path;
				picture_main = productImage.path;
				productImage.is_main = 1;
			}
			listImage.add(productImage);
			i++;
		}
		
		if(listImage.size()==0)
		{
			xpath_anh = "html/body[1]/div[@id='body']/div[@id='container_body']/div[@id='container_content']/div[1]/div[1]/div[1]/div[2]/div[1]/table[1]/TBODY[1]/tr[1]/td";
			NodeList nodes = (NodeList) reader.read(xpath_anh,
					XPathConstants.NODESET);
			node_one_many = nodes.getLength();
			
			i = 1;
			
			while(i<=node_one_many)
			{
				productImage = new ProductImage();
				String	image_path = (String) reader.read(xpath_anh
						+ "[" + i + "]" + "/a[1]//@href",
						XPathConstants.STRING);
				System.out.println(image_path);
				String fileName = image_path.substring(image_path.lastIndexOf("/") + 1);
				fileName = Calendar.getInstance().getTimeInMillis()+fileName;
				productImage.name = "small_" + fileName;

				productImage.path = pre_folder + dayPath + "/" + productImage.name;
				productImage.product_id = 0;
				pattern = Pattern.compile("http://");
				matcher = pattern.matcher(image_path);
				if(!matcher.find()) image_path = "http://www.vatgia.com"+ image_path;
				downloadImage.downloadImage(image_path, "/usr/src/java/tomcat7/webapps/images.az24.vn/picture_model/"+dayPath+"/", fileName);
				//ProductDownloadImage.downloadImage(image_path, "d:/anh2/", fileName);
				if (i == 1) {
					product.pictrue = productImage.path;
					picture_main = productImage.path;
					productImage.is_main = 1;
				}
				listImage.add(productImage);
				
			}
		}
		
		if(listImage.size()==0)
		{
			xpath_anh = "html/body[1]/div[@id='body']/div[@id='container_body']/div[@id='container_content']/div[1]/div[1]/div[1]/div[2]/table[1]/TBODY[1]/tr[1]/td";
			NodeList nodes = (NodeList) reader.read(xpath_anh,
					XPathConstants.NODESET);
			node_one_many = nodes.getLength();
			
			i = 1;
			
			while(i<=node_one_many)
			{
				productImage = new ProductImage();
				String	image_path = (String) reader.read(xpath_anh
						+ "[" + i + "]" + "/a[1]//@href",
						XPathConstants.STRING);
				System.out.println(image_path);
				String fileName = image_path.substring(image_path.lastIndexOf("/") + 1);
				fileName = Calendar.getInstance().getTimeInMillis()+fileName;
				productImage.name = "small_" + fileName;

				productImage.path = pre_folder + dayPath + "/" + productImage.name;
				productImage.product_id = 0;
				pattern = Pattern.compile("http://");
				matcher = pattern.matcher(image_path);
				if(!matcher.find()) image_path = "http://www.vatgia.com"+ image_path;
				downloadImage.downloadImage(image_path, "/usr/src/java/tomcat7/webapps/images.az24.vn/picture_model/"+dayPath+"/", fileName);
				//ProductDownloadImage.downloadImage(image_path, "d:/anh2/", fileName);
				if (i == 1) {
					product.pictrue = productImage.path;
					picture_main = productImage.path;
					productImage.is_main = 1;
				}
				listImage.add(productImage);
				
			}
		}
		
		if(listImage.size()==0)
		{
			i = 1;
			productImage = new ProductImage();
			String	image_path = (String) reader.read("html/body[1]/div[@id='body']/div[@id='container_body']/div[@id='container_content']/div[1]/div[1]/div[1]/div[1]/table[1]/TBODY[1]/tr[1]/td[1]/a[1]//@href",
					XPathConstants.STRING);  
			
			System.out.println(image_path);
			String fileName = image_path.substring(image_path.lastIndexOf("/") + 1);
			fileName = Calendar.getInstance().getTimeInMillis()+fileName;
			productImage.name = "small_" + fileName;

			productImage.path = pre_folder + dayPath + "/" + productImage.name;
			productImage.product_id = 0;
			pattern = Pattern.compile("http://");
			matcher = pattern.matcher(image_path);
			if(!matcher.find()) image_path = "http://www.vatgia.com"+ image_path;
			downloadImage.downloadImage(image_path, "/usr/src/java/tomcat7/webapps/images.az24.vn/picture_model/"+dayPath+"/", fileName);
			//ProductDownloadImage.downloadImage(image_path, "d:/anh2/", fileName);
			
			product.pictrue = productImage.path;
			picture_main = productImage.path;
			productImage.is_main = 1;
			
			listImage.add(productImage);
				
			
		}
		
		
		ProductDAO productDAO = new ProductDAO();
		productDAO.deletePricture(product_id);
		
		productDAO.updateImageMain(product_id, picture_main);
			// Luu Hinh Anh
		for (ProductImage productImageT : listImage) {
				productImageT.product_id = product_id;
				productDAO.savePicture(productImageT);
		}
	
			
		Thread.sleep(1000);	
	
		return 	1;
	}
	
	public static void main(String[] args) {
		CrawlerStoreDownloadImage crawlerStoreDownloadImage = new CrawlerStoreDownloadImage();
		ProductDAO productDAO = new ProductDAO();
		String sql =args[0];
		String type =args[1];
		
		//String sql = "SELECT * FROM products WHERE id IN (SELECT product_id FROM products_store_cat WHERE store_cat_id2 in (12945))";
		//String sql = "SELECT * FROM products WHERE id in (541046,541035,541032)";
		List<Product> products = productDAO.getProducts(sql);
		for (Product product : products) {
			try {
				if("1".equalsIgnoreCase(type))
					crawlerStoreDownloadImage.extract_product(product.original_link, product.id);
				else
					crawlerStoreDownloadImage.extract_product_cb(product.original_link, product.id);
					
				
			} catch (Exception e) {				
				e.printStackTrace();
			}
		}
	}
	
	
}
