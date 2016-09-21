package com.az24.crawler.store;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.util.html.parser.XPathReader;
import hdc.util.text.StringUtil;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathConstants;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.http.HttpResponse;

import com.az24.crawler.model.product.Product;
import com.az24.dao.ProductDAO;
import com.az24.dao.ProductStoreDAO;
import com.az24.test.HttpClientUtil;

public class CrawlerPriceStore {
	private double USD=0;//2
	private double JPY=0;
	private double AUD=0;
	private double GBP=0;
	private double EUR=0;
	public String fileName="";
	
	private List<Store> getStores(String fileName) {
		List<Store> listStores = new ArrayList<Store>();
		try {
			FileInputStream propsFile = new java.io.FileInputStream("./conf/"+ fileName);
			Workbook w = Workbook.getWorkbook(propsFile);
			Sheet sheet = w.getSheet(0);
			Cell cell = null;
			// STT | Store_ID | StoreAlias | Xpath Price | Xpath Exchange | Price Type
			Store store = null;
			for (int i = 1; i < sheet.getRows(); i++) {
				
					store = new Store();
					cell = sheet.getCell(1, i);
					int id = Integer.parseInt(cell.getContents().trim());
					cell = sheet.getCell(2, i);
					String store_alias = cell.getContents().trim();
					cell = sheet.getCell(3, i);
					String xpath_price = cell.getContents().trim();
					cell = sheet.getCell(4, i);
					String xpath_exchage = cell.getContents().trim();
					cell = sheet.getCell(5, i);
					String price_type = cell.getContents().trim();
					store.id = id;
					store.store_alias = store_alias;
					store.xpath_price = xpath_price;
					store.price_type = price_type;
					store.xpath_exchange = xpath_exchage;
					listStores.add(store);
					
			}
		 } catch (Exception e) {
		}	
		return listStores;
	}
	
	private void getProductPrice(ProductStore productStore,String xpath_price,String xpath_exchange,String url_product,int price_type)
	{
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url_product);
		HttpClientUtil.printResponseHeaders(res);
		String html;
		try {
			html = HttpClientUtil.getResponseBody(res);
			XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
			String price = (String) reader.read(xpath_price, XPathConstants.STRING);
			System.out.println("Price = " + price);
			Pattern pattern = Pattern.compile("\\D+");
			Matcher matcher = pattern.matcher(price);
			price=matcher.replaceAll("");
			
			if(!StringUtil.isEmpty(xpath_exchange))
			{
				String exchange = (String) reader.read(xpath_exchange, XPathConstants.STRING);
				if(!StringUtil.isEmpty(exchange))
				{
					if(exchange.toLowerCase().indexOf("usd")>-1)
					{
						price_type = 2;
					}
					
					if(exchange.toLowerCase().indexOf("eur")>-1)
					{
						price_type = 4;
					}
					
					if(exchange.toLowerCase().indexOf("jpy")>-1)
					{
						price_type = 5;
					}
					
					if(exchange.toLowerCase().indexOf("gbp")>-1)
					{
						price_type = 6;
					}
					
					if(exchange.toLowerCase().indexOf("aud")>-1)
					{
						price_type = 7;
					}
				}
			}
			
			switch (price_type) {
				case 1:
					productStore.price = Double.parseDouble(price);
					productStore.price_foreign = 0;
					productStore.price_type = 1;//VND
					break;
				case 2:
					productStore.price = Double.parseDouble(price)*USD;
					productStore.price_foreign = Double.parseDouble(price);
					productStore.price_type = 2;//USD
					break;
				case 4:
					productStore.price = Double.parseDouble(price)*EUR;
					productStore.price_foreign = Double.parseDouble(price);
					productStore.price_type = 4;//Chau Au
					break;
				case 5:
					productStore.price = Double.parseDouble(price)*JPY;
					productStore.price_foreign = Double.parseDouble(price);
					productStore.price_type = 5;//Nhat
					break;
				case 6:
					productStore.price = Double.parseDouble(price)*GBP;
					productStore.price_foreign = Double.parseDouble(price);
					productStore.price_type = 6;//Anh
					break;
				case 7:
					productStore.price = Double.parseDouble(price)*AUD;
					productStore.price_foreign = Double.parseDouble(price);
					productStore.price_type = 7;//UC
					break;
				default:				
					productStore.price = Double.parseDouble(price);
					productStore.price_foreign = 0;
					productStore.price_type = 1;
					break;
					
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getPriceExchange() throws Exception
	{
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch("http://www.vietcombank.com.vn/");
		HttpClientUtil.printResponseHeaders(res);
		String html;
		
		html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		String price = (String) reader.read("//div[@id='exchangerate']/div[1]/table[1]/TBODY[1]/tr[2]/td[4]/text()", XPathConstants.STRING);
		System.out.println("Price AUD = " + price);
		Pattern pattern = Pattern.compile("\\D+");
		Matcher matcher = pattern.matcher(price);
		price =matcher.replaceAll("");
		AUD = Double.parseDouble(price);
		
		
		price = (String) reader.read("//div[@id='exchangerate']/div[1]/table[1]/TBODY[1]/tr[3]/td[4]/text()", XPathConstants.STRING);
		System.out.println("Price EUR = " + price);
		pattern = Pattern.compile("\\D+");
		matcher = pattern.matcher(price);
		price =matcher.replaceAll("");
		EUR = Double.parseDouble(price);
		
		price = (String) reader.read("//div[@id='exchangerate']/div[1]/table[1]/TBODY[1]/tr[4]/td[4]/text()", XPathConstants.STRING);
		System.out.println("Price GBP = " + price);
		pattern = Pattern.compile("\\D+");
		matcher = pattern.matcher(price);
		price =matcher.replaceAll("");
		GBP = Double.parseDouble(price);
		
		price = (String) reader.read("//div[@id='exchangerate']/div[1]/table[1]/TBODY[1]/tr[5]/td[4]/text()", XPathConstants.STRING);
		System.out.println("Price JPY = " + price);
		pattern = Pattern.compile("\\D+");
		matcher = pattern.matcher(price);
		price =matcher.replaceAll("");
		JPY = Double.parseDouble(price);
		
		price = (String) reader.read("//div[@id='exchangerate']/div[1]/table[1]/TBODY[1]/tr[6]/td[4]/text()", XPathConstants.STRING);
		System.out.println("Price USD = " + price);
		pattern = Pattern.compile("\\D+");
		matcher = pattern.matcher(price);
		price =matcher.replaceAll("");
		USD = Double.parseDouble(price);
	}
	
	private int getPriceType(String price_type)
	{
		int type = 1;
		if("usd".equalsIgnoreCase(price_type))
			type = 2;
			return type;
	}
	
	public void updateProductPrice()
	{
		List<Store> listStore = this.getStores(this.fileName);
		ProductStoreDAO productStoreDAO = new ProductStoreDAO();
		ProductDAO productDAO = new ProductDAO();
		int min_id = 0,max_id=0, price_type=1;
		List<ProductStore> productstores=null;
		try{
			for (Store store : listStore) {
				min_id=productStoreDAO.getMinIdProductStore(store.id);
				max_id = productStoreDAO.getMaxIdProductStore(store.id);
				while(min_id<max_id)
				{
					productstores = productStoreDAO.getProduct(min_id,store.id);
					if(productstores.size()==0)break;
					for (ProductStore productStore : productstores) {
						price_type = getPriceType(store.price_type);
						getProductPrice(productStore, store.xpath_price,store.xpath_exchange, productStore.original_link,price_type);
						
						productStoreDAO.updateProductStorePrice(productStore);
						Product product = productDAO.getProductById(productStore.product_id);
						if(productStore.price>0)
						{
							//update Min
							if(productStore.price<product.price_from)
							{
								product.price_from = productStore.price;
								productDAO.updatePrice(product);
							}
							
							if(productStore.price>product.price_to)
							{
								product.price_to = productStore.price;
								productDAO.updatePrice(product);
							}
						}
						
						min_id = productStore.id;
						Thread.sleep(1000);
					}
				}
				
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		CrawlerPriceStore crawlerPriceStore = new CrawlerPriceStore();
		crawlerPriceStore.fileName = args[0];
		try {
			crawlerPriceStore.getPriceExchange();
			crawlerPriceStore.updateProductPrice();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
