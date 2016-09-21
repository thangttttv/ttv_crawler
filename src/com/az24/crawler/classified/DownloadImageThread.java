package com.az24.crawler.classified;

import hdc.util.html.URLEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.az24.crawler.model.ImageConfig;
import com.az24.util.Constants;

public class DownloadImageThread implements Runnable{
	
	private String baseUrl="";
	private List<ImageConfig> images; 

	public DownloadImageThread(String baseUrl,List<ImageConfig> images)	{
		
		this.baseUrl = baseUrl;
		this.images = images;
	}
	
	
	
	public void downloadImage(String imageUrl, String destinationFile)  {
		try {
			imageUrl=URLEncoder.encode(imageUrl);
			URL url = new URL(imageUrl);
			System.out.println(url);
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
			Calendar calendar = Calendar.getInstance();			
			int i = 0;
			ImageConfig imageConfig = null;
			String[]  dateProcess = null;
			String fileName ="",folder="";			
			File file = new File(Constants.CLASSIFIED_PRE_FOLDER +calendar.get(Calendar.YEAR));
			if(!file.exists())
				file.mkdir();
			file = new File(Constants.CLASSIFIED_PRE_FOLDER +calendar.get(Calendar.YEAR)+"/thumb");
			if(!file.exists())
				file.mkdir();
			while(i<images.size())
			{		
				imageConfig = images.get(i);
				if(imageConfig!=null&&imageConfig.dateProcess!=null)
				{				
					dateProcess = imageConfig.dateProcess.split("/");				
					folder =Constants.CLASSIFIED_PRE_FOLDER +dateProcess[2]+"/thumb/"+dateProcess[1]+dateProcess[0]+"/";
					file = new File(folder);
					if(!file.exists())
						file.mkdir();
					fileName = imageConfig.src.substring(imageConfig.src.lastIndexOf("/")+1);
					
					if(imageConfig.src.indexOf("http")==-1)
						imageConfig.src = baseUrl+"/"+imageConfig.src;
					
					downloadImage(imageConfig.src,folder+fileName);				
					System.out.println(new Date()+" Download Image Thu:"+i+" "+imageConfig.src);
				}
				i++;
			}
			
		}catch (Exception e) {			
			e.printStackTrace();
		}
		
		
	}
	
	

}
