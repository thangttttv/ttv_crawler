package com.az24.crawler.article;

import hdc.util.io.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Iterator;

import jdbm.PrimaryHashMap;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

import com.az24.crawler.config.JdbmXmlConfig;
import com.az24.crawler.config.UrlInjectXmlConfig;
import com.az24.crawler.model.DatabaseConfig;
import com.az24.crawler.model.ImageConfig;
import com.az24.dao.ConnectionDataLog;
import com.az24.dao.CrawlerLogDAO;
import com.az24.util.FileLog;

public class ArticleDownloadImage implements Runnable {
	public Connection connLog;
	protected RecordManager urlImageManager;
	protected PrimaryHashMap<String, ImageConfig> urlImagePrimary;
	private String fileJdbmConfig = "";
	private String fileBeanConfig = "";
	private String baseUrl = "";

	public ArticleDownloadImage(String fileJdbmConfig, String fileBeanConfig,
			String baseUrl) {
		this.fileJdbmConfig = fileJdbmConfig;
		this.fileBeanConfig = fileBeanConfig;
		this.baseUrl = baseUrl;
	}

	private void openData() throws IOException {
		JdbmXmlConfig.parseConfig(fileJdbmConfig);
		urlImageManager = RecordManagerFactory
				.createRecordManager(JdbmXmlConfig.url_image + "/image");
		urlImagePrimary = urlImageManager.hashMap("image");
	}
	
	private void closeData() throws IOException {
		urlImageManager.close();
	}

	public void downloadImage(String imageUrl, String destinationFile) {
		URL url;
		try {
			url = new URL(imageUrl);
			InputStream is = url.openStream();
			File file = new File(destinationFile);
			Runtime.getRuntime().exec("chmod 777 "+destinationFile);
			OutputStream os = new FileOutputStream(file);

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

	public void run() {
		try {
			openData();
			connLog = ConnectionDataLog.connection(this.fileBeanConfig);
			System.out.println(this.fileBeanConfig);
			System.out.println(connLog);
			CrawlerLogDAO crawlerLogDAO = new CrawlerLogDAO();
			Calendar calendar = Calendar.getInstance();
			Iterator<ImageConfig> iterator = urlImagePrimary.values()
					.iterator();
			int i = 0;
			ImageConfig imageConfig = null;
			String folder = "", pre_folder = "/data/website/images.tinhhinh.net/picture/article/";
			//pre_folder="d:/data/picture_auto/";
			File file = new File(pre_folder +calendar.get(Calendar.YEAR));
			if (!file.exists())
				file.mkdir();
			System.out.println(pre_folder);
			while (iterator.hasNext()) {
				imageConfig = iterator.next();
				if (!crawlerLogDAO.checkEmage(DatabaseConfig.table_image_log, imageConfig.id)) {

					folder = imageConfig.getFolder(pre_folder);

					file = new File(folder);
					if (!file.exists()) {
						file.mkdir();
						Runtime.getRuntime().exec("chmod 777 " + folder);
					}
					// small Image
					if (imageConfig.src.indexOf("http") == -1) {
						imageConfig.src = baseUrl + "/" + imageConfig.src;
					}
					System.out.println(folder + imageConfig.name);
					downloadImage(imageConfig.src, folder + imageConfig.name);
					crawlerLogDAO.saveImage(DatabaseConfig.table_image_log, imageConfig);
					System.out.println("Download-->i=" + i + imageConfig.src);
				}
				i++;
			}

			// log file
			calendar = Calendar.getInstance();
			String log = calendar.getTime().toString() + "-->Tong Download:"
					+ i;
			FileLog.createFileLog(JdbmXmlConfig.keyword_filter
					+ "/download.txt");
			FileLog.writer(log);

			closeData();
			connLog.close();
			// xoa file
			FileUtil.deleteFile(JdbmXmlConfig.url_image);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		UrlInjectXmlConfig urlInjectXmlConfig = new UrlInjectXmlConfig(
				"src/com/az24/crawler/config/urlsKenh14.xml");
		urlInjectXmlConfig.parseConfig();
		ArticleDownloadImage downloadImage = new ArticleDownloadImage(
				"src/com/az24/crawler/config/jdbm.xml",
				"src/com/az24/crawler/config/beanKenh14.xml",
				UrlInjectXmlConfig.baseUrl);
		downloadImage.run();
	}

}
