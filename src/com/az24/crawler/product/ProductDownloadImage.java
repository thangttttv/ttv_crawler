package com.az24.crawler.product;

import hdc.crawler.classified.ImageSize;
import hdc.util.html.URLEncoder;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import javax.imageio.ImageIO;

import jdbm.PrimaryHashMap;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

import com.az24.crawler.config.JdbmXmlConfig;
import com.az24.crawler.model.DatabaseConfig;
import com.az24.crawler.model.ImageConfig;
import com.az24.util.FileLog;
import com.mortennobel.imagescaling.ThumpnailRescaleOp;

public class ProductDownloadImage implements Runnable {
	public Connection connLog;
	protected RecordManager urlImageManager;
	protected PrimaryHashMap<String, ImageConfig> urlImagePrimary;
	private String fileJdbmConfig = "";
	private String baseUrl = "";

	public ProductDownloadImage(String fileJdbmConfig, String baseUrl) {
		this.fileJdbmConfig = fileJdbmConfig;
		this.baseUrl = baseUrl;
	}

	public void openConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connLog = DriverManager.getConnection(
					DatabaseConfig.url_log.trim(), DatabaseConfig.user_log
							.trim(), DatabaseConfig.password_log);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void closeConnection() {
		try {
			if (connLog != null)
				connLog.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void saveImage(Connection conn, ImageConfig imageConfig) {
		try {
			PreparedStatement ps = conn
					.prepareStatement("INSERT INTO tbl_image ("
							+ " url_id,url,url_content ) VALUES (?,?,?)");
			ps.setString(1, imageConfig.id);
			ps.setString(2, imageConfig.src);
			ps.setString(3, imageConfig.url_content);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public boolean checkEmage(Connection conn, String url_id) {

		try {
			PreparedStatement ps = conn
					.prepareStatement("select id from tbl_image where url_id = ? ");
			ps.setString(1, url_id);
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
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
			imageUrl = imageUrl.replaceAll(" ", "%20");
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
			openConnection();
			Calendar calendar = Calendar.getInstance();
			if(urlImagePrimary.values()==null) return; 
			
			Iterator<ImageConfig> iterator = urlImagePrimary.values()
					.iterator();
			int i = 0;
			ImageConfig imageConfig = null;

			String folder = "", pre_folder = "/usr/src/java/tomcat7/webapps/images.az24.vn/picture_model/";
			// pre_folder="d:/data/picture_auto/";
			File file = new File(pre_folder + +calendar.get(Calendar.YEAR));
			if (!file.exists())
				file.mkdir();

			while (iterator.hasNext()) {
				imageConfig = iterator.next();
			//	if (!this.checkEmage(connLog, imageConfig.id)) {
				if (1==1) {
					folder = imageConfig.getFolder(pre_folder);
					file = new File(folder);
					if (!file.exists())
						file.mkdir();

					// small Image
					if (imageConfig.src.indexOf("http") == -1) {
						imageConfig.src = baseUrl + "/" + imageConfig.src;
						downloadImage(imageConfig.src, folder, imageConfig.name);
						System.out
								.println(folder + "small_" + imageConfig.name);
					} else {
						downloadImage(imageConfig.src, folder, imageConfig.name);
						System.out
								.println(folder + "small_" + imageConfig.name);
					}

					System.out.println("Download-->i=" + i + imageConfig.src);

					//this.saveImage(connLog, imageConfig);
				}
				i++;
			}

			// log file
			calendar = Calendar.getInstance();
			String log = calendar.getTime().toString() + "-->Tong Download:"
					+ i;
			FileLog.createFileLog(JdbmXmlConfig.file_log + "/download.txt");
			FileLog.writer(log);
			// xoa file
			closeData();
			hdc.util.io.FileUtil.deleteFile(JdbmXmlConfig.url_image);
			closeConnection();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static ImageSize getImageSize(int width, int height) {
		ImageSize imageSize = new ImageSize();
		int w = 120, h = 120;
		double tyle = 0;
		if (width > height) {
			if (width > 550) {
				w = 550;
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
			if (height > 550) {
				h = 550;
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
		int w = 150, h = 150;
		double tyle = 0;
		imageSize.h = height;
		imageSize.w = width;
		if (width > height && width > 150) {

			w = 150;
			tyle = (double) height / width;
			h = Integer.parseInt(Math.round(w * tyle) + "");
			imageSize.w = w;
			imageSize.h = h;
			return imageSize;

		} else {
			if (height > 150) {
				h = 150;
				tyle = (double) width / height;
				w = Integer.parseInt(Math.round(h * tyle) + "");
				imageSize.w = w;
				imageSize.h = h;
				return imageSize;
			}
		}

		return imageSize;

	}

	public  void downloadImage(String src, String filePath, String name) {
		System.out.println(new Date() + " - fetch image: " + src);
		BufferedImage originalImage;
		BufferedImage des = null;
		try {
			String duoi = src.substring(src.lastIndexOf(".") + 1, src.length());
			src = src.replaceAll(" ", "%20");
			src=URLEncoder.encode(src);
			URL url = new URL(src);
			InputStream is = url.openStream();

			originalImage = ImageIO.read(new BufferedInputStream(is));
			//ImageIO.w
			int type = originalImage.getType() > 0 ? originalImage.getType(): 5;
			int width = originalImage.getWidth();
			int height = originalImage.getHeight();

			// download image
			BufferedImage resizeImage = new BufferedImage(width, height, type);
			Graphics2D g = resizeImage.createGraphics();
			g.drawImage(originalImage, 0, 0, width, height, null);
			g.dispose();
			g.setComposite(AlphaComposite.Src);
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.setRenderingHint(RenderingHints.KEY_RENDERING,
					RenderingHints.VALUE_RENDER_QUALITY);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			ImageIO.write(resizeImage, "PNG", new File(filePath + name));
			

			ImageSize imageSize = new ImageSize();
			imageSize = getImageSize(width, height);
			ThumpnailRescaleOp op = new ThumpnailRescaleOp(imageSize.w,
					imageSize.h);
			des = op.filter(originalImage, des);
			ImageIO.write(des, duoi, new File(filePath + "medium_" + name));

			
			imageSize = getImageSizeSmall(width, height);
			op = new ThumpnailRescaleOp(imageSize.w, imageSize.h);
			des = op.filter(originalImage, des);
			ImageIO.write(des, duoi, new File(filePath + "small_" + name));

		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void downloadFormFile(String file,String folderDis) throws Exception { 		
		FileReader fr = new FileReader( file); 
		BufferedReader br = new BufferedReader(fr); 
		String s=""; 
		String arrstr[] = null;
		ProductDownloadImage downloadImage = new ProductDownloadImage("","http://www.abm.com.vn");
		while((s = br.readLine()) != null) { 
			arrstr = s.split(";");
			String fileName =arrstr[1];
			downloadImage.downloadImage(arrstr[0],folderDis,fileName);
		} 
		fr.close();		
	} 
	
	/*public static void main(String[] args) {	
		try {
			ProductDownloadImage.downloadFormFile("http://www.abm.com.vn/Upload/Large/2010/3/2532010152124906.jpg", "d:/amjh.jpg");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	public static void main(String[] args) {
		
		File file = new File(args[0]);
		if(file.isDirectory())
		{
			for (File child : file.listFiles()) {
				System.out.println("Filename-------------->"+child.getName());
				String arrFileName[] = child.getName().split("_");
				if(arrFileName[2].length()<2)arrFileName[2] = "0"+arrFileName[2];
				if(arrFileName[1].length()<2)arrFileName[2] = "0"+arrFileName[1];
				String disFolder ="/usr/src/java/tomcat7/webapps/images.az24.vn/picture_model/"+arrFileName[0]+"/"+arrFileName[1]+arrFileName[2]+"/" ;
				if(child.isFile())
				{
					try {
						ProductDownloadImage.downloadFormFile(child.getPath(), disFolder);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}
