package com.az24.crawler.classified;

import com.az24.crawler.CrawlerFetcher;
import com.az24.crawler.CrawlerInjectUrl;
import com.az24.crawler.DownloadImageThread;
import com.az24.crawler.config.UrlInjectXmlConfig;
import com.az24.crawler.importer.ClassifiedImporter;
import com.az24.crawler.CrawlerExtracter ;

public class ClassifiedCrawlerDaily implements Runnable {

	private String beanconfig = "";
	private String jdbmconfig = "";
	private String urlconfig = "";
	private int step;
	private int fetch;

	public ClassifiedCrawlerDaily(String beanconfig, String jdbmconfig,
			String urlconfig, int step, int fetch) {
		this.beanconfig = beanconfig;
		this.jdbmconfig = jdbmconfig;
		this.urlconfig = urlconfig;
		this.step = step;
		this.fetch = fetch;

	}

	public void run() {

		switch (step) {
		case 1:
			CrawlerInjectUrl crawlerInjectUrl = new CrawlerInjectUrl(urlconfig,
					jdbmconfig, beanconfig, this.fetch, 1);
			crawlerInjectUrl.collectionUrl();
			break;
		case 2:
			CrawlerFetcher crawlerFetcher = new CrawlerFetcher(jdbmconfig, this.fetch, 1);
			crawlerFetcher.run();
			break;
		case 3:
			CrawlerExtracter crawlerExtracter = new CrawlerExtracter(beanconfig, jdbmconfig);
			crawlerExtracter.run();
			break;
		case 4:
			ClassifiedImporter productImporter = new ClassifiedImporter(beanconfig,jdbmconfig);
			productImporter.run();
			break;
		case 5:
			UrlInjectXmlConfig xmlConfig = new UrlInjectXmlConfig(urlconfig);
			xmlConfig.parseConfig();
			DownloadImageThread downloadImage = new DownloadImageThread(
					this.jdbmconfig, UrlInjectXmlConfig.baseUrl);
			downloadImage.run();
			break;
		default:
			crawlerInjectUrl = new CrawlerInjectUrl(urlconfig, jdbmconfig,
					beanconfig, this.fetch, 1);
			crawlerInjectUrl.collectionUrl();
			try {
				java.lang.Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			crawlerFetcher = new CrawlerFetcher(jdbmconfig, this.fetch, 1);
			crawlerFetcher.run();
			try {
				java.lang.Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			crawlerExtracter = new CrawlerExtracter(beanconfig,	jdbmconfig);
			crawlerExtracter.run();
			try {
				java.lang.Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			productImporter = new ClassifiedImporter(beanconfig, jdbmconfig);
			productImporter.run();
			
			downloadImage = new DownloadImageThread(this.jdbmconfig, UrlInjectXmlConfig.baseUrl);
			downloadImage.run();
			break;
		}

	}

	public static void main(String[] args) {
		//String beanconfig = "./conf/beanEnbacxml";
		/*String beanconfig = "src/com/az24/crawler/config/beanBatDongSan.xml";
		String urlconfig = "src/com/az24/crawler/config/batdongsan.xml";
		String jdmconfig = "src/com/az24/crawler/config/jdbm.xml";*/
		
		String urlconfig = args[0];
		String jdmconfig = args[1];
		String beanconfig = args[2];
		String step = args[3];
		String strfetch = args[4];
		int intstep = Integer.parseInt(step);
		int fetch = Integer.parseInt(strfetch);
		ClassifiedCrawlerDaily crawlerDaily = new ClassifiedCrawlerDaily(beanconfig,
				jdmconfig, urlconfig,intstep,fetch);
		crawlerDaily.run();

	}

}
