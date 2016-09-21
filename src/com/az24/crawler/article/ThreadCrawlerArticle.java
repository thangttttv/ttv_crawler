package com.az24.crawler.article;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadCrawlerArticle {

	public ExecutorService threadPool;

	private class Job implements Runnable {

		private int fetch;
		private String fileBean;
		private String fileUrl;
		private String date;
		private int type;
		private int source;
		private int province_id;
		private String province_alias;

		public Job(String fileBean, String fileUrl, int fetch, String date,int type, int source,int province_id,String province_alias) {
			this.fetch = fetch;
			this.fileBean = fileBean;
			this.fileUrl = fileUrl;
			this.date = date;
			this.type = type;
			this.source = source;
			this.province_id = province_id;
			this.province_alias = province_alias;
		}

		public void run() {
			CrawlerArticleByPage crawlerArticleByPage = new CrawlerArticleByPage();
			crawlerArticleByPage.initialization(fileUrl, fileBean, fetch,source,2,province_id,province_alias,0,1,0);
			if(type==0)
			{
				crawlerArticleByPage.collectionData();
			}else
			{
				crawlerArticleByPage.collectionDataByDate(date);
			}
		}
	}

	public ThreadCrawlerArticle(int numThreads, int maxQueueSize)
			throws IOException {

		threadPool = new ThreadPoolExecutor(numThreads, numThreads, 0,
				TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(
						maxQueueSize, false),
				new ThreadPoolExecutor.CallerRunsPolicy());

	}

	public void addQuery(String fileBean, String fileUrl, int fetch, int page,
			String date,int type,int source,int province_id,String province_alias) {
		threadPool.execute(new Job(fileBean, fileUrl, fetch, date,type,source,province_id,province_alias));
	}

	public void close() {
		finish();
	}

	public void rollback() throws IOException {
		finish();
	}

	private void finish() { // E
		threadPool.shutdown();
		while (true) {
			try {
				if (threadPool.awaitTermination(Long.MAX_VALUE,
						TimeUnit.SECONDS)) {
					break;
				}
			} catch (InterruptedException ie) {
				Thread.currentThread().interrupt();
				throw new RuntimeException(ie);
			}
		}
	}

}
