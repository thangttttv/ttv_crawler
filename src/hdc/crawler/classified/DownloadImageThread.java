package hdc.crawler.classified;

import hdc.crawler.AbstractCrawler.Datum;
import hdc.crawler.CrawlerUtil;
import hdc.crawler.fetcher.HttpClientFactory;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.util.html.parser.XPathReader;
import hdc.util.lang.MD5;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.xml.xpath.XPathConstants;

import jdbm.PrimaryHashMap;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.w3c.dom.NodeList;

public class DownloadImageThread extends Thread {
	private List<String> src;
	private List<String> fileName;
	private String desc;

	public DownloadImageThread(List<String> src, List<String> fileName,
			String desc) throws Exception {
		this.src = src;
		this.fileName = fileName;
		this.desc = desc;
	}

	public void run() {
		for (int i = 0; i < src.size(); i++) {
			try {
				HttpClientImpl client = new HttpClientImpl();
				HttpResponse res = null;
				res = client.fetch(src.get(i));
				System.out
						.println(new Date() + " - fetch image: " + src.get(i));
				if (res.getStatusLine().getStatusCode() == 404) {
					System.out.println("Link 404: " + src.get(i));
					HttpClientFactory.reload();
					continue;
				}
				BufferedImage originalImage = ImageIO
						.read(new BufferedInputStream(res.getEntity()
								.getContent()));
				int type = originalImage.getType();
				int width = originalImage.getWidth();
				int height = originalImage.getHeight();

				if (width > 250) {
					width = 250;
					height = height * 250 / width;
				}
				//          BufferedImage resizeImage = new BufferedImage(width, height, type) ;
				BufferedImage resizeImage = new BufferedImage(120, 113, type);
				Graphics2D g = resizeImage.createGraphics();
				g.drawImage(originalImage, 0, 0, 120, 113, null);
				g.dispose();
				g.setComposite(AlphaComposite.Src);

				g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
						RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				g.setRenderingHint(RenderingHints.KEY_RENDERING,
						RenderingHints.VALUE_RENDER_QUALITY);
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				ImageIO.write(resizeImage, "PNG", new File(desc + "/"
						+ fileName.get(i)));
				sleep(500);
			} catch (Exception e) {
				System.out.println("image error: " + src.get(i));
				e.printStackTrace();
				continue;
			}
		}
	}

	public static void downloadImageBig(String imageUrl, String destinationFile) {
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
		int w = 125, h = 125;
		double tyle = 0;
		if (width > height) {

			w = 250;
			tyle = (double) height / width;
			h = Integer.parseInt(Math.round(w * tyle) + "");
			imageSize.w = w;
			imageSize.h = h;
			return imageSize;

		} else {
			h = 250;
			tyle = (double) width / height;
			w = Integer.parseInt(Math.round(h * tyle) + "");
			imageSize.w = w;
			imageSize.h = h;
			return imageSize;
		}

	}
	
	
	public static void saveImage(String src,String filePath,String name) {
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = null;
		src = src.replaceAll(" ", "%20");
		res = client.fetch(src);
		System.out.println(new Date() + " - fetch image: " + src);
		if (res.getStatusLine().getStatusCode() == 404) {
			System.out.println("Link 404: " + src);
			HttpClientFactory.reload();

		}

		BufferedImage originalImage;
		try {
			src=src.replaceAll(" ", "%20");
			URL url = new URL(src);
			InputStream is = url.openStream();
			
			originalImage = ImageIO.read(new BufferedInputStream(is));
			
			
			int type = originalImage.getType()>0?originalImage.getType():5;
			int width = originalImage.getWidth();
			int height = originalImage.getHeight();
			
			// download image
			BufferedImage resizeImage = new BufferedImage(width,
					height, type);
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
			ImageIO.write(resizeImage, "PNG", new File(filePath+name));
			
			// dowload image medium
			ImageSize imageSize = new ImageSize();
			imageSize = getImageSize(width, height);
			resizeImage = new BufferedImage(imageSize.w,
					imageSize.h, type);
			g = resizeImage.createGraphics();
			g.drawImage(originalImage, 0, 0, imageSize.w, imageSize.h, null);
			g.dispose();
			g.setComposite(AlphaComposite.Src);
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.setRenderingHint(RenderingHints.KEY_RENDERING,
					RenderingHints.VALUE_RENDER_QUALITY);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			ImageIO.write(resizeImage, "JPG", new File(filePath+"medium_"+name));
			
			// dowload image small
			imageSize = getImageSizeSmall(width, height);
			resizeImage = new BufferedImage(imageSize.w, imageSize.h, type);
			g = resizeImage.createGraphics();
			g.drawImage(originalImage, 0, 0, imageSize.w, imageSize.h, null);
			g.dispose();
			g.setComposite(AlphaComposite.Src);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.setRenderingHint(RenderingHints.KEY_RENDERING,
					RenderingHints.VALUE_RENDER_QUALITY);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			ImageIO.write(resizeImage, "JPG", new File(filePath+"small_"+name));

		} catch (IllegalStateException e) {			
			e.printStackTrace();
		} catch (IOException e) {		
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		try {
			String src = "http://vatgia.com/banner_user/jii1318930739.swf";
			DownloadImageThread.saveImage(src,"d:/","jii1318930739.swf");
		} catch (Exception e) {
			//System.out.println("image error: " + src);
			e.printStackTrace();

		}
	}

	public static void main1(String[] args) throws Exception {
		RecordManager importManager = RecordManagerFactory
				.createRecordManager("/data/modules/search/data_crawler/enbac/daily/import");
		PrimaryHashMap<String, String> importPrimary = importManager
				.hashMap("Import");

		RecordManager datumManager = RecordManagerFactory
				.createRecordManager("/data/modules/search/data_crawler/enbac/daily/datum");
		PrimaryHashMap<String, Datum> datumPrimary = datumManager
				.hashMap("Datum");

		RecordManager thumbManager = RecordManagerFactory
				.createRecordManager("/data/modules/search/data_crawler/enbac/daily/thumb");
		PrimaryHashMap<String, String> thumbPrimary = thumbManager
				.hashMap("Thumb");

		Iterator<String> i = importPrimary.keySet().iterator();
		List<String> src = new ArrayList<String>();
		List<String> fileName = new ArrayList<String>();
		int count = 0;
		while (i.hasNext()) {
			String id = i.next();
			//        System.out.println(id); 
			if (thumbPrimary.containsKey(id))
				continue;
			XPathReader reader = CrawlerUtil
					.createXPathReaderByData(datumPrimary.get(id).data);
			if (reader == null)
				continue;
			NodeList nodeDetail = (NodeList) reader
					.read(
							"/html/body/div[1]/center/div[5]/div[2]/div[2]/div[2]/div[1]/div/a",
							XPathConstants.NODESET);
			if (nodeDetail == null)
				continue;
			else if (nodeDetail.getLength() == 0)
				continue;
			String href = nodeDetail.item(0).getAttributes().getNamedItem(
					"lang").getTextContent();
			src.add(href);
			String fname = MD5.digest(id).toString() + ".PNG";
			fileName.add(fname);
			thumbPrimary.put(id,
					"/data/modules/search/data_crawler/enbac/daily/image/"
							+ fname);
			count++;
			System.out.println("collect " + count + " : " + href);
			//        if(src.size() % 5000 == 0) {
			//          DownloadImageThread thread = new DownloadImageThread(new ArrayList<String>(src), new ArrayList<String>(fileName), "/data/modules/search/data_crawler/enbac/daily/image") ;
			//          src.clear() ;
			//          fileName.clear() ;
			//          count = 0 ;
			//          thumbManager.commit() ;
			//          thread.start() ;
			//        }
		}

		thumbManager.commit();
		DownloadImageThread thread = new DownloadImageThread(
				new ArrayList<String>(src), new ArrayList<String>(fileName),
				"/data/modules/search/data_crawler/enbac/daily/image");
		thread.start();
	}
}