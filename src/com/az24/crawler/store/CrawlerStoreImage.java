package com.az24.crawler.store;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.crawler.fetcher.HttpClientUtil;
import hdc.util.html.parser.XPathReader;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.xpath.XPathConstants;
import org.apache.http.HttpResponse;
import org.w3c.dom.NodeList;
import com.az24.crawler.config.BeanXmlConfig;
import com.az24.crawler.model.ImageConfig;
import com.az24.crawler.model.product.Product;
import com.az24.crawler.product.ProductDownloadImage;
import com.az24.dao.ProductDAO;
import com.az24.util.Logger;

public class CrawlerStoreImage {
	
	private com.az24.util.Logger logger = new Logger(this.getClass().getName());
	
	public void processImage(ImageConfig imageConfig, String url, String urlDomain,List<ProductPicture> pictures)
			throws Exception {

		logger.log("---->Process Update Images:");
		String day = "";		
		ProductDownloadImage downloadImage = new ProductDownloadImage(urlDomain, urlDomain);
		NodeList nodes = null;
		
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);
		html=HttpClientUtil.getHtml(url);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		
		if (imageConfig.node_type.equalsIgnoreCase("nodeset")) {
			nodes = (NodeList) reader.read(imageConfig.xpath,
					XPathConstants.NODESET);
			int node_one_many = nodes.getLength();

			int i = 1;

			while (i <= node_one_many) {
				String src = (String) reader.read(imageConfig.xpath + "["
						+ i + "]" + imageConfig.xpath_sub,
						XPathConstants.STRING);
				Pattern pattern = Pattern.compile("http://");
				Matcher matcher = pattern.matcher(src);
				if (!matcher.find())
					src = urlDomain + "/" + src;
				
				ProductPicture picture = pictures.get(i);
				if(picture!=null)
				{
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(picture.create_date*1000);
					int month = calendar.get(Calendar.MONTH) + 1;
					day = calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0"+ calendar.get(Calendar.DAY_OF_MONTH) : ""+ calendar.get(Calendar.DAY_OF_MONTH);
					String strmonth = month < 10 ? "0" + month : "" + month;
					String dayPath = calendar.get(Calendar.YEAR) + "/" + strmonth + day;
					String dis_folder = "/usr/src/java/tomcat7/webapps/images.az24.vn/picture_model/"+ dayPath + "/";
					//String dis_folder = "d:/anh2/"+dayPath+"/";
					File file = new File(dis_folder);
					if(!file.exists()) file.mkdirs();
					File file2 = new File(dis_folder+picture.name);
					if(!file2.exists())
					downloadImage.downloadImage(src, dis_folder, picture.name);
				}
				i++;

			}
		}		
		if (imageConfig.node_type.equalsIgnoreCase("string")) {
				String src = (String) reader.read(imageConfig.xpath,
						XPathConstants.STRING);
				Pattern pattern = Pattern.compile("http://");
				Matcher matcher = pattern.matcher(src);
				if (!matcher.find())
					src = urlDomain + "/" + src;
				ProductPicture picture = pictures.get(0);
				if(picture!=null)
				{
					System.out.println("picture ID = "+picture.name);
					
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(picture.create_date*1000);
					int month = calendar.get(Calendar.MONTH) + 1;
					day = calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0"+ calendar.get(Calendar.DAY_OF_MONTH) : ""+ calendar.get(Calendar.DAY_OF_MONTH);
					String strmonth = month < 10 ? "0" + month : "" + month;
					String dayPath = calendar.get(Calendar.YEAR) + "/" + strmonth + day;
					String dis_folder = "/usr/src/java/tomcat7/webapps/images.az24.vn/picture_model/"+ dayPath + "/";
					//String dis_folder = "d:/anh2/"+dayPath+"/";
					File file = new File(dis_folder);
					if(!file.exists()) file.mkdirs();
					File file2 = new File(dis_folder+picture.name);
					if(!file2.exists())
					downloadImage.downloadImage(src, dis_folder, picture.name);
				}
			}
		
	}
	
	public static void main(String[] args) {
		ProductDAO productDAO = new ProductDAO();
		int store_id = Integer.parseInt(args[1]);
		String beanconfig=args[0];
		
		BeanXmlConfig beanXmlConfig = new BeanXmlConfig(beanconfig);
		beanXmlConfig.parseConfig();
		
		List<ImageConfig> imageConfigs = beanXmlConfig.bean.getImages();
		CrawlerStoreImage crawlerStoreImage = new CrawlerStoreImage();
		List<Product> products =productDAO.getProductByStoreID(store_id);
		
		List<ProductPicture> productPictures  = new ArrayList<ProductPicture>();
		for (Product product : products) {
			productPictures = productDAO.getPictureProduct(product.id);
			try {
				System.out.println("Product ID = "+product.id);				
				crawlerStoreImage.processImage(imageConfigs.get(0), product.original_link, BeanXmlConfig.baseUrl, productPictures);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
}
