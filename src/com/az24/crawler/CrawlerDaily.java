package com.az24.crawler;

import java.util.List;

import com.az24.crawler.DownloadImageThread;
import com.az24.crawler.config.ThreadXmlConfig;
import com.az24.crawler.importer.ClassifiedImporter;
import com.az24.crawler.importer.QAImporter;
import com.az24.crawler.model.Thread;

public class CrawlerDaily implements Runnable {

	private String beanconfig = "";
	private String jdbmconfig = "";
	private String urlconfig = "";
	private int importer = 0;
	private int step=0;
	private int stepTo=4;
	private int fetch = 1;
	private int is_slow = 1;
	
	public CrawlerDaily(String beanconfig, String jdbmconfig, String urlconfig,
			int importer,int stepTo,int step, int fetch,int is_slow) {
		this.beanconfig = beanconfig;
		this.jdbmconfig = jdbmconfig;
		this.urlconfig = urlconfig;
		this.importer = importer;
		this.step = step;
		this.stepTo = stepTo;
		this.fetch = fetch;
		this.is_slow = is_slow;
	}

	public void run() {
		
		if(stepTo>0)
		{
			runStepTo(stepTo);
		}else
		{
			runStepOne(step);
		}

	}
	private synchronized void runStepOne(int step)
	{
		if(step==0||step==1){
			CrawlerInjectUrl crawlerInjectUrl = new CrawlerInjectUrl(urlconfig,jdbmconfig,beanconfig,fetch,is_slow);
			crawlerInjectUrl.collectionUrl();
		}
		if(step==0||step==2)
		{
			CrawlerFetcher crawlerFetcher = new CrawlerFetcher(jdbmconfig,fetch,is_slow);
			crawlerFetcher.run();
		}
		try {
			java.lang.Thread.sleep(100);
		} catch (InterruptedException e) {			
			e.printStackTrace();
		}
		if(step==0||step==3){
			CrawlerExtracter crawlerExtracter = new CrawlerExtracter(beanconfig,
				jdbmconfig);
			crawlerExtracter.run();
		}
		try {
			java.lang.Thread.sleep(100);
		} catch (InterruptedException e) {			
			e.printStackTrace();
		}
		if(step==0||step==4)
		switch (importer) {
		case 1:
			ClassifiedImporter classifiedImporter = new ClassifiedImporter(
					beanconfig, jdbmconfig);
			classifiedImporter.run();
			break;
		case 2:
			QAImporter qImporter = new QAImporter(beanconfig, jdbmconfig);
			qImporter.run();
			break;
		default:
			// AbstractImporter abstractImporter = new
			// AbstractImporter(beanconfig,jdbmconfig);
			// abstractImporter.run();
			break;
		}
		
		if(step==0||step==5){
			DownloadImageThread downloadImageThread = new DownloadImageThread(beanconfig,
				jdbmconfig);
			downloadImageThread.run();
		}
	}
	
	private synchronized void runStepTo(int to)
	{
		CrawlerInjectUrl crawlerInjectUrl = null;
		CrawlerFetcher crawlerFetcher =  null;
		CrawlerExtracter crawlerExtracter = null;
		
		switch (to) {
			case 1:	
				crawlerInjectUrl = new CrawlerInjectUrl(urlconfig,jdbmconfig,beanconfig,fetch,is_slow);
				crawlerInjectUrl.collectionUrl();
				break;
			case 2:	
				crawlerInjectUrl = new CrawlerInjectUrl(urlconfig,jdbmconfig,beanconfig,fetch,is_slow);
				crawlerInjectUrl.collectionUrl();
				try {
					java.lang.Thread.sleep(100);
				} catch (InterruptedException e) {			
					e.printStackTrace();
				}
				crawlerFetcher = new CrawlerFetcher(jdbmconfig,fetch,is_slow);
				crawlerFetcher.run();
				break;
			case 3:	
				crawlerInjectUrl = new CrawlerInjectUrl(urlconfig,jdbmconfig,beanconfig,fetch,is_slow);
				crawlerInjectUrl.collectionUrl();
				try {
					java.lang.Thread.sleep(100);
				} catch (InterruptedException e) {			
					e.printStackTrace();
				}
				crawlerFetcher = new CrawlerFetcher(jdbmconfig,fetch,is_slow);
				crawlerFetcher.run();
				try {
					java.lang.Thread.sleep(100);
				} catch (InterruptedException e) {			
					e.printStackTrace();
				}
				crawlerExtracter = new CrawlerExtracter(beanconfig,
						jdbmconfig);
				crawlerExtracter.run();
				
				break;
			case 4:	
				crawlerInjectUrl = new CrawlerInjectUrl(urlconfig,jdbmconfig,beanconfig,fetch,is_slow);
				crawlerInjectUrl.collectionUrl();
				try {
					java.lang.Thread.sleep(100);
				} catch (InterruptedException e) {			
					e.printStackTrace();
				}
				crawlerFetcher = new CrawlerFetcher(jdbmconfig,fetch,is_slow);
				crawlerFetcher.run();
				try {
					java.lang.Thread.sleep(100);
				} catch (InterruptedException e) {			
					e.printStackTrace();
				}
				crawlerExtracter = new CrawlerExtracter(beanconfig,
						jdbmconfig);
				crawlerExtracter.run();
				try {
					java.lang.Thread.sleep(100);
				} catch (InterruptedException e) {			
					e.printStackTrace();
				}
				switch (importer) {
				case 1:
					ClassifiedImporter classifiedImporter = new ClassifiedImporter(
							beanconfig, jdbmconfig);
					classifiedImporter.run();
					break;
				case 2:
					QAImporter qImporter = new QAImporter(beanconfig, jdbmconfig);
					qImporter.run();
					break;
				default:
					// AbstractImporter abstractImporter = new
					// AbstractImporter(beanconfig,jdbmconfig);
					// abstractImporter.run();
					break;
				}

				break;
			case 5:	
				crawlerInjectUrl = new CrawlerInjectUrl(urlconfig,jdbmconfig,beanconfig,fetch,is_slow);
				crawlerInjectUrl.collectionUrl();
				try {
					java.lang.Thread.sleep(100);
				} catch (InterruptedException e) {			
					e.printStackTrace();
				}
				crawlerFetcher = new CrawlerFetcher(jdbmconfig,fetch,is_slow);
				crawlerFetcher.run();
				try {
					java.lang.Thread.sleep(100);
				} catch (InterruptedException e) {			
					e.printStackTrace();
				}
				crawlerExtracter = new CrawlerExtracter(beanconfig,
						jdbmconfig);
				crawlerExtracter.run();
				try {
					java.lang.Thread.sleep(100);
				} catch (InterruptedException e) {			
					e.printStackTrace();
				}
				switch (importer) {
				case 1:
					ClassifiedImporter classifiedImporter = new ClassifiedImporter(
							beanconfig, jdbmconfig);
					classifiedImporter.run();
					break;
				case 2:
					QAImporter qImporter = new QAImporter(beanconfig, jdbmconfig);
					qImporter.run();
					break;
				default:
					// AbstractImporter abstractImporter = new
					// AbstractImporter(beanconfig,jdbmconfig);
					// abstractImporter.run();
					break;
				}
				try {
					java.lang.Thread.sleep(100);
				} catch (InterruptedException e) {			
					e.printStackTrace();
				}
				DownloadImageThread downloadImageThread = new DownloadImageThread(beanconfig,
						jdbmconfig);
					downloadImageThread.run();
				break;
			default:
				break;
		}
	}
	
	public static void main(String[] args) {
		ThreadXmlConfig.parseConfig("src/com/az24/crawler/config/threads.xml");
		List<Thread> threads = ThreadXmlConfig.threads;		
		for (Thread thread : threads) {
			CrawlerDaily crawlerDaily = new CrawlerDaily(thread.getBean_config(),
		    		thread.getJdbm_config(),thread.getUrl_config(),thread.getImporter_config(),thread.getStepTo(),thread.getStep()
		    		,thread.getFetch(),thread.getIs_slow());
		    crawlerDaily.run() ;
		}
	}

}
