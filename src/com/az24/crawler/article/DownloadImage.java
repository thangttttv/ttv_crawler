package com.az24.crawler.article;

import hdc.crawler.classified.ImageSize;
import hdc.util.html.URLEncoder;
import hdc.util.text.StringUtil;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import jdbm.PrimaryHashMap;
import jdbm.RecordManager;

import com.az24.crawler.model.ImageConfig;
import com.az24.util.Constants;
import com.mortennobel.imagescaling.ThumpnailRescaleOp;

public class DownloadImage implements Runnable {
	public Connection connLog;
	protected RecordManager urlImageManager;
	protected PrimaryHashMap<String, ImageConfig> urlImagePrimary;
	private String src = "";
	private String imagename = "";
	private String baseUrl = "";
	private String date;
	public String destinationFile="";
	public int height;
	public int width;
	public int type_image=0;
	public int download_type = 1;
	public List<ImageConfig> imageList = null;
	public String pre_folder=Constants.ARTICLE_PRE_FOLDER;
	
	

	public DownloadImage(String src, String name, String baseUrl,String date,int type_image,int download_type,List<ImageConfig> imageList) {
		this.baseUrl = baseUrl;
		this.src = src;
		this.imagename = name;
		this.date = date;
		this.type_image = type_image;
		this.download_type = download_type;
		this.imageList = imageList;
	}
	

	public void downloadImage(String imageUrl, String destinationFile) {
		URL url;
		try {
			if(imageUrl.indexOf("http://img1.thethaovanhoa.vn/GetThumbNail.ashx?ImgFilePath=")>0)
			{
				Pattern p = Pattern.compile("ImgFilePath=(.*)&");
				Matcher m = p.matcher(imageUrl);
				if(m.find())
				{
					imageUrl = m.group(1); 
				}
			}
			if(src.indexOf("http://k142.vcmedia.vn/GetThumbNail.ashx?ImgFilePath=")>0)
			{
				Pattern p = Pattern.compile("ImgFilePath=(.*)&");
				Matcher m = p.matcher(src);
				if(m.find())
				{
					src = m.group(1); 
				}
			}
			
			if(src.indexOf("http://baobinhduong.org.vn/printThumbImg.aspx")>-1)
			{
				src = src.replaceAll("~", "");
				Pattern r = Pattern.compile("[^a-z,^A-Z,^\\d+,^&,^.,^=,^/,^:,^\\?]");
				Matcher	m = r.matcher(src);
				src = m.replaceAll("/");
			}
			
			url = new URL(imageUrl);
			InputStream is = url.openStream();
			File file = new File(destinationFile);			
			OutputStream os = new FileOutputStream(file);
			byte[] b = new byte[2048];
			int length;
			while ((length = is.read(b)) != -1) {
				os.write(b, 0, length);
			}
			this.destinationFile = destinationFile;
			is.close();
			os.close();
			//Runtime.getRuntime().exec("chmod 777 "+destinationFile);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public  void downloadImageResize(String src, String destinationFile) {
		BufferedImage originalImage=null;
		BufferedImage des = null;
		try {
			if(src.indexOf("http://img1.thethaovanhoa.vn/GetThumbNail.ashx?ImgFilePath=")>0)
			{
				Pattern p = Pattern.compile("ImgFilePath=(.*)&");
				Matcher m = p.matcher(src);
				if(m.find())
				{
					src = m.group(1); 
				}
			}
			if(src.indexOf("http://k142.vcmedia.vn/GetThumbNail.ashx?ImgFilePath=")>0)
			{
				Pattern p = Pattern.compile("ImgFilePath=(.*)&");
				Matcher m = p.matcher(src);
				if(m.find())
				{
					src = m.group(1); 
				}
			}
			
			if(src.indexOf("http://baobinhduong.org.vn/printThumbImg.aspx")>-1)
			{
				src = src.replaceAll("~", "");
				Pattern r = Pattern.compile("[^a-z,^A-Z,^\\d+,^&,^.,^=,^/,^:,^\\?]");
				Matcher	m = r.matcher(src);
				src = m.replaceAll("/");
			}
			
			
			String duoi = src.substring(src.lastIndexOf(".") + 1, src.length());
			URL url = null;
			InputStream is =  null;int width=0;int height=0;
			try{
				url = new URL(src);
				is = url.openStream();
				originalImage = ImageIO.read(new BufferedInputStream(is));
				width = originalImage.getWidth();
				height = originalImage.getHeight();
			}catch (Exception e) {
				width = 0;height = 0;
			}
			
			boolean edit_anh = false;
			ImageSize imageSize = new ImageSize();
			if(type_image==1)
			{
				if(height>width)
				{
					if(height < 100&&this.imageList!=null&&this.imageList.size()>0)
						edit_anh = true;
				}else
				{
					if(width<130&&this.imageList!=null&&this.imageList.size()>0)
						edit_anh = true;
				}
				
				if(!edit_anh)
					imageSize = getImageSizeSmall(width, height);
				else
				{
					int i = 0;
					while(i<imageList.size())
					{
						try{
						ImageConfig imageConfig = imageList.get(i);
						String src1 = imageConfig.src.replaceAll(" ", "%20");
						URL url1 = new URL(src1);
						is = url1.openStream();
						originalImage = ImageIO.read(new BufferedInputStream(is));
						width = originalImage.getWidth();
						height = originalImage.getHeight();
						if(width>=130||height>=100)
						{
							imageSize = getImageSizeSmall(width, height);
							break;
						}
						}catch (Exception e) {							
						}
						i++;
					}
				}
			}
			else
			{
				imageSize = getImageSize(width, height);
			}
			this.destinationFile = destinationFile;
			if(originalImage!=null)
			{
				ThumpnailRescaleOp op = new ThumpnailRescaleOp(imageSize.w,	imageSize.h);
				des = op.filter(originalImage, des);
				ImageIO.write(des, duoi, new File(destinationFile));
			//	Runtime.getRuntime().exec("chmod 777 "+destinationFile);
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static ImageSize getImageSize(int width, int height) {
		ImageSize imageSize = new ImageSize();
		int w = 150, h = 150;
		double tyle = 0;
		if (width > height) {
			if (width > 450) {
				w = 450;
				tyle = (double) height / width;
				h = Integer.parseInt(Math.round(w * tyle) + "");
				imageSize.w = w;
				imageSize.h = h;
				return imageSize;
			}

			if (width > 400) {
				w = width;
				tyle = (double) height / width;
				h = Integer.parseInt(Math.round(w * tyle) + "");
				imageSize.w = w;
				imageSize.h = h;
				return imageSize;
			}

			if (width > 300) {
				w = width;
				tyle = (double) height / width;
				h = Integer.parseInt(Math.round(w * tyle) + "");
				imageSize.w = w;
				imageSize.h = h;
				return imageSize;
			}

			if (width > 200) {
				w = width;
				tyle = (double) height / width;
				h = Integer.parseInt(Math.round(w * tyle) + "");
				imageSize.w = w;
				imageSize.h = h;
				return imageSize;
			}
		} else {
			if (height > 450) {
				h = 450;
				tyle = (double) width / height;
				w = Integer.parseInt(Math.round(h * tyle) + "");
				imageSize.w = w;
				imageSize.h = h;
				return imageSize;
			}

			if (height > 400) {
				h = height;
				tyle = (double) width / height;
				w = Integer.parseInt(Math.round(h * tyle) + "");
				imageSize.w = w;
				imageSize.h = h;
				return imageSize;
			}

			if (height > 300) {
				h = height;
				tyle = (double) width / height;
				w = Integer.parseInt(Math.round(h * tyle) + "");
				imageSize.w = w;
				imageSize.h = h;
				return imageSize;
			}

			if (height > 200) {
				h = height;
				tyle = (double) width / height;
				w = Integer.parseInt(Math.round(h * tyle) + "");
				imageSize.w = w;
				imageSize.h = h;
				return imageSize;
			}
		}
		imageSize.w = width;
		imageSize.h = height;
		return imageSize;
	}

	public static ImageSize getImageSizeSmall(int width, int height) {
		ImageSize imageSize = new ImageSize();
		int w = 155, h = 155;
		double tyle = 0;
		imageSize.h = height;
		imageSize.w = width;
		if (width > height && width > w) {

			//w = 150;
			tyle = (double) height / width;
			h = Integer.parseInt(Math.round(w * tyle) + "");
			imageSize.w = w;
			imageSize.h = h;
			return imageSize;

		} else {
			if (height > h) {
				//h = 150;
				tyle = (double) width / height;
				w = Integer.parseInt(Math.round(h * tyle) + "");
				imageSize.w = w;
				imageSize.h = h;
				return imageSize;
			}
		}

		return imageSize;

	}
	
	
	public void run() {
		try {
			String strDay="",strMonth="",strYear="";
			if(StringUtil.isEmpty(this.date))
			{
				Calendar calendar = Calendar.getInstance();		
				int month = calendar.get(Calendar.MONTH)+1;
				strMonth = month<10?"0"+month:month+"";
				int day = calendar.get(Calendar.DAY_OF_MONTH);
				strDay = day<10?"0"+day:day+"";
				strYear = calendar.get(Calendar.YEAR)+"";
			}else
			{
				String dates[] = this.date.split("/");
				int intmonth = Integer.parseInt(dates[1]);
				int intday = Integer.parseInt(dates[0]);
				strMonth = intmonth < 10 ? "0" + intmonth
						: "" + intmonth;
				strDay = intday < 10 ? "0" + intday
						: "" + intday;
				strYear = dates[2];
			}
			String folder = "";			
			File file = new File(pre_folder + strYear);
			if (!file.exists())
				file.mkdir();
			
			folder = pre_folder + strYear + "/"
					+ strMonth
					+ strDay + "/";
			file = new File(folder);
			if (!file.exists()) {
				file.mkdir();
			//	Runtime.getRuntime().exec("chmod 777 " + folder);
			}
			
			if (src.indexOf("http") == -1) {
				src = baseUrl + "/" + src;
			}
			
			src = src.trim();
			Pattern  p = Pattern.compile("\\s+");
			Matcher m = p.matcher(src);
			src = m.replaceAll("%20");
			
			p = Pattern.compile("\\.\\./");
			m = p.matcher(src);
			src = m.replaceAll("/");
			
			
			p = Pattern.compile("jpg\\?|png\\?|gif\\?|bmp\\?|jpg&|png&|gif&|bmp&");
			m = p.matcher(src);
			if(m.find())download_type=0;
			src=URLEncoder.encode(src);
			if(download_type==1)
			downloadImageResize(src, folder + imagename);
			else downloadImage(src, folder + imagename);
			
			System.out.println(src);
			System.out.println("Download-->i=" +  imagename);

		} catch (Exception e) {
		 e.printStackTrace();
		}

	}

	

	public static void main(String[] args) {

		DownloadImage downloadImage = new DownloadImage(
				"http://images.az24.vn/2012/0507/img_7033.jpg",
				"vie777tnam.jpg", "http://mayvanphongmienbac.vn","",0,1,null);
		downloadImage.pre_folder="d:/";
		downloadImage.run();
		
	}

}
