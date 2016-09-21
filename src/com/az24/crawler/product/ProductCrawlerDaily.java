package com.az24.crawler.product;

import hdc.crawler.AbstractCrawler.Url;

import java.util.List;

import com.az24.crawler.CrawlerFetcher;
import com.az24.crawler.config.BeanXmlConfig;
import com.az24.crawler.config.UrlInjectXmlConfig;

public class ProductCrawlerDaily implements Runnable {

	private String beanconfig = "";
	private String jdbmconfig = "";	
	private String urlconfig="";
	private int step=0;
	private int recolletion=0;
	private Url url = null;

	public ProductCrawlerDaily(String beanconfig, String jdbmconfig,
			String urlconfig,int step,int recolletion) {
		this.beanconfig = beanconfig;
		this.jdbmconfig = jdbmconfig;		
		this.step = step;
		this.urlconfig = urlconfig;
		this.recolletion = recolletion;

	}

	public  void run() {
		
		BeanXmlConfig beanXmlConfig = new BeanXmlConfig(this.beanconfig);		
		beanXmlConfig.parseConfig();
		
		switch (step)
		{
			case 1:
				ProductCrawlerInjectUrl productCrawlerInjectUrl = new ProductCrawlerInjectUrl(url, jdbmconfig,beanconfig, 1, 1,this.recolletion);
				productCrawlerInjectUrl.collectionUrl();
				try {
					java.lang.Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			case 2:
				CrawlerFetcher crawlerFetcher = new CrawlerFetcher(jdbmconfig, 1, 1);
				crawlerFetcher.run();
				try {
					java.lang.Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			case 3:
				ProductCrawlerExtracter crawlerExtracter = new ProductCrawlerExtracter(
						beanconfig, jdbmconfig);
				crawlerExtracter.run();
				try {
					java.lang.Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			case 4:
				ProductImporter  productImporter = new ProductImporter(beanconfig,jdbmconfig);
				productImporter.run();
			
				try {
					java.lang.Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			case 5: 
				ProductDownloadImage productDownloadImage = new ProductDownloadImage(jdbmconfig,"http://www.vatgia.com");
				productDownloadImage.run();
				break;
			default :

				UrlInjectXmlConfig urlInjectXmlConfig = new UrlInjectXmlConfig(urlconfig);
				urlInjectXmlConfig.parseConfig();
				List<Url> urls = UrlInjectXmlConfig.urlConfigs;
				
				for (Url url : urls) {				
						productCrawlerInjectUrl = new ProductCrawlerInjectUrl(
								url, jdbmconfig,beanconfig, 1, 1,this.recolletion);
						productCrawlerInjectUrl.collectionUrl();
						try {
							java.lang.Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
						crawlerFetcher = new CrawlerFetcher(jdbmconfig, 1, 1);
						crawlerFetcher.run();
						try {
							java.lang.Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
						crawlerExtracter = new ProductCrawlerExtracter(
								beanconfig, jdbmconfig);
						crawlerExtracter.run();
						try {
							java.lang.Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
						productImporter = new ProductImporter(beanconfig,jdbmconfig);
						productImporter.run();
					
						try {
							java.lang.Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
						
						try {
							productDownloadImage = new ProductDownloadImage(jdbmconfig,"http://www.vatgia.com");
							productDownloadImage.run();
							java.lang.Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}catch (Exception e)
						{
							e.printStackTrace();
						}
						
						
				}
		}
		
		
	}

	public static void main(String[] args) {
		String beanconfig = "./conf/beanProductVatGia.xml";
		String urlconfig = args[0];
		String jdmconfig = args[1];
		String step = args[2];
		String recollection = args[3];
		ProductCrawlerDaily crawlerDaily = null;		
		crawlerDaily = new ProductCrawlerDaily(beanconfig,
					jdmconfig, urlconfig,Integer.parseInt(step),Integer.parseInt(recollection));
		crawlerDaily.run();
	}

}
