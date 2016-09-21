package com.az24.crawler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import jdbm.PrimaryHashMap;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

import com.az24.crawler.config.JdbmXmlConfig;
import com.az24.crawler.config.ThreadXmlConfig;
import com.az24.crawler.config.UrlInjectXmlConfig;
import com.az24.crawler.model.ImageConfig;
import com.az24.crawler.model.Thread;
import com.az24.util.FileLog;

public class DownloadImage implements Runnable{
	
	protected RecordManager urlImageManager;
	protected PrimaryHashMap<String, ImageConfig> urlImagePrimary;
	private String fileJdbmConfig="";
	private String baseUrl="";

	public DownloadImage(String fileJdbmConfig,String baseUrl)
	{
		this.fileJdbmConfig =fileJdbmConfig;
		this.baseUrl = baseUrl;
	}
	
	private void openData() throws IOException {	
		JdbmXmlConfig.parseConfig(fileJdbmConfig);
		urlImageManager = RecordManagerFactory.createRecordManager(JdbmXmlConfig.url_image
				+ "/image");
		urlImagePrimary = urlImageManager.hashMap("image");
	}

	private void commitData() throws IOException {
		urlImageManager.commit();		
	}

	private void closeData() throws IOException {
		urlImageManager.close();
	}
	
	public void downloadImage(String imageUrl, String destinationFile)  {
		URL url;
		try {
			url = new URL(imageUrl);
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
	
	public void run() {
		try {
			openData();
			Calendar calendar = Calendar.getInstance();
			Iterator<ImageConfig> iterator = urlImagePrimary.values().iterator();
			int i = 0;
			ImageConfig imageConfig = null;
			String[]  dateProcess = null;
			String fileName ="",folder="";			
			File file = new File("/usr/src/java/tomcat7/webapps/images.az24.vn/picture_auto/"+calendar.get(Calendar.YEAR));
			if(!file.exists())
				file.mkdir();
			file = new File("/usr/src/java/tomcat7/webapps/images.az24.vn/picture_auto/"+calendar.get(Calendar.YEAR)+"/thumb");
			if(!file.exists())
				file.mkdir();
			while(iterator.hasNext())
			{				
				imageConfig = iterator.next();
				dateProcess = imageConfig.dateProcess.split("/");				
				folder ="/usr/src/java/tomcat7/webapps/images.az24.vn/picture_auto/"+dateProcess[2]+"/thumb/"+dateProcess[1]+dateProcess[0]+"/";
				System.out.println(folder);
				file = new File(folder);
				if(!file.exists())
					file.mkdir();
				fileName = imageConfig.src.substring(imageConfig.src.lastIndexOf("/")+1);
				
				if(imageConfig.src.indexOf("http")==-1)
					imageConfig.src = baseUrl+"/"+imageConfig.src;
				
				downloadImage(imageConfig.src,folder+fileName);
				System.out.println("Download-->i="+i+imageConfig.src);
				
				i++;
			}
			
			 calendar = Calendar.getInstance();
			 String log=calendar.getTime().toString()+ "-->Tong Download:"+i;			
			 FileLog.createFileLog(JdbmXmlConfig.keyword_filter+"/download.txt");
			 FileLog.writer(log);
			 
			i=0;
			iterator = urlImagePrimary.values().iterator();
			while(iterator.hasNext())
			{	
				imageConfig = iterator.next();
				urlImagePrimary.remove(imageConfig.id);
				if(i%30==0) commitData();
				i++;
			}
			commitData();
			closeData();
			
		} catch (IOException e) {			
			e.printStackTrace();
		} catch (Exception e) {			
			e.printStackTrace();
		}
		
		
	}
	
	public static void main(String[] args) {
		ThreadXmlConfig.parseConfig("./conf/threads.xml");
		List<Thread> threads = ThreadXmlConfig.threads;		
		for (Thread thread : threads) {
			UrlInjectXmlConfig urlInjectXmlConfig = new UrlInjectXmlConfig(thread.getUrl_config());
			urlInjectXmlConfig.parseConfig();
			DownloadImage downloadImage = new DownloadImage(thread.getJdbm_config(),UrlInjectXmlConfig.baseUrl);
			downloadImage.run();
		}
	}

}
