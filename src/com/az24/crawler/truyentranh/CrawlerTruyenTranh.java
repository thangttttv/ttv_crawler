package com.az24.crawler.truyentranh;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.util.html.parser.XPathReader;
import hdc.util.text.StringUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathConstants;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.NodeList;

import com.az24.crawler.model.CategoryComic;
import com.az24.crawler.model.Comic;
import com.az24.crawler.model.ComicChapter;
import com.az24.crawler.model.ComicChapterFile;
import com.az24.dao.KKTDAO;
import com.az24.test.HttpClientFactory;
import com.az24.test.HttpClientUtil;
import com.az24.util.UTF8Tool;

public class CrawlerTruyenTranh {
	
	public String getHTML(String urlToRead) {
		URL url; // The URL to read
		HttpURLConnection conn; // The actual connection to the web page
		BufferedReader rd; // Used to read results from the web page
		String line; // An individual line of the web page HTML
		String result = ""; // A long string containing all the HTML
		try {
			url = new URL(urlToRead);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "UTF-8"));
			while ((line = rd.readLine()) != null) {
				result += line+"\n";
			}
			rd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*FileUtil fileUtil = new FileUtil();
		try {
			fileUtil.writeToFile("d:/giay.html", result, false);
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		System.out.println(result);
		return result;
	}
	
	public void extractTruyenTranh(int comic_id,String title,int story_source_id,String url_info,String url,String downloadFolder,int chapterFrom) throws Exception {
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		KKTDAO kktdao = new KKTDAO();
		
		int i = chapterFrom;		
		String xpathYahoo = "//div[@id='contentWapper']/img";
		String xpathChapter = "//select[@id='rdChapter']/option";
		NodeList nodesChapter = (NodeList) reader.read(xpathChapter, XPathConstants.NODESET);
		int node_chapter_one_many = nodesChapter.getLength();
		
		System.out.println("node_chapter_one_many==> "+node_chapter_one_many);
		
		ComicChapter comicChapter = new ComicChapter();
		comicChapter.comic_id = comic_id;
		
		while (i <= node_chapter_one_many) {
			String chapter_value = (String) reader.read(xpathChapter + "["
					+ i + "]" + "/@value", XPathConstants.STRING);
			String chapter_text = (String) reader.read(xpathChapter + "["
					+ i + "]" + "/text()", XPathConstants.STRING);
			
			
			
			String chapter_text1 = chapter_text.trim();//replace("Chapter", "").trim();
			chapter_text1 = StringUtil.getAlias(chapter_text1);
			
			String dau_url  = url_info;//url.substring(0,url.indexOf(story_source_id+"")-1);
			System.out.println("dau link-------->"+dau_url);
			String link =dau_url+chapter_text1+"/"+story_source_id+"/"+chapter_value+"/2/";
			
			comicChapter.title  = chapter_text.trim();
			comicChapter.create_user ="admin";
			comicChapter.modify_user = "admin";
			comicChapter.create_date =Calendar.getInstance().getTimeInMillis()/1000;
			comicChapter.modify_date =Calendar.getInstance().getTimeInMillis()/1000;
			int comic_chapter_id = kktdao.SaveComicChapter(comicChapter); 
			
			System.out.println("chapter_value==> "+chapter_value.trim());
			System.out.println("chapter_text==> "+chapter_text.trim());
			System.out.println("link==> "+link.trim());
			
			
			String html2 = getHTML(link.trim());
			XPathReader reader2 = CrawlerUtil.createXPathReaderByData(html2);
			int j = 1;	
			NodeList nodes = (NodeList) reader2.read(xpathYahoo, XPathConstants.NODESET);
			int node_one_many = nodes.getLength();
			System.out.println(node_one_many);
			
			Calendar calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH)+1;
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			
			String strM = month<10? "0"+month:month+"";
			String strD = day<10? "0"+day:day+"";
			
			
			String pathFolder = downloadFolder+year+"/"+strM+strD+"/"+StringUtil.getAlias(title)+"-"+comic_id;
			pathFolder =pathFolder +"/" + StringUtil.getAlias(chapter_text)+"-"+comic_chapter_id+"/";
			
			String linkImage = "/upload/comic/"+year+"/"+strM+strD+"/"+StringUtil.getAlias(title)+"-"+comic_id+"/";
			linkImage += StringUtil.getAlias(chapter_text)+"-"+comic_chapter_id+"/";
			
			File file = new File(pathFolder);
			if(!file.exists())file.mkdirs();
			
			
			while (j <= node_one_many) {
				String urlImage = (String) reader2.read(xpathYahoo + "["
						+ j + "]" + "/@data-original", XPathConstants.STRING);
				
				// cau truc thu muc
				// comic/year/mmdd/trentruyen-id/chaptername/image-stt.png
				ComicChapterFile chapterFile = new ComicChapterFile();
				chapterFile.link =linkImage+"image_"+comic_chapter_id+"_"+j+urlImage.substring(urlImage.lastIndexOf("."));
				chapterFile.link_source = urlImage;
				chapterFile.chapter_id = comic_chapter_id;
				chapterFile.comic_id = comic_id;
				chapterFile.create_user ="admin";
				chapterFile.modify_user = "admin";
				chapterFile.create_date =Calendar.getInstance().getTimeInMillis()/1000;
				chapterFile.modify_date =Calendar.getInstance().getTimeInMillis()/1000;
				kktdao.SaveComicChapterFile(chapterFile);
				
				//downloadImage(urlImage,pathFolder+"image_"+comic_chapter_id+"_"+j+urlImage.substring(urlImage.lastIndexOf(".")));
				System.out.println("price_label==> "+urlImage.trim());
				j++;
			}                                                                                                                                                                                                                         
			kktdao.UpdateComicChapter(comic_chapter_id, node_one_many);
			i++;
		}
		
	}
	
	public int extractTruyenTranhInfo(int[] arrCategory,int app_id,String url,String url_chapter,int page,String downloadFolder) throws Exception {
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		///String html = hdc.util.io.IOUtil.getFileContenntAsString("d:/giay.html");
		String html = HttpClientUtil.getResponseBody(res);;
		/*html = html.replaceAll("29\"", "'24'");
		html = html.replaceAll("Đọc truyện tranh Angel Sanctuary - Chapter 1", "'fdll'");*/
		
		Pattern r1 = Pattern.compile("\\s\\w\\w+=\\w\\w+\"\\s");
	    Matcher m1 = r1.matcher(html);
	     if(m1.find())
	      {
	    	  html = m1.replaceAll(" alt='Tran The Thang' ");
	      }
	     r1 = Pattern.compile("alt=([^\"].+[^\"])");
         m1 = r1.matcher(html);
	     if(m1.find())
	      {
	    	 html = m1.replaceAll("\""+m1.group(1)+"\"");
	      }
	      
		/*try {
			FileUtil.writeToFile("d:/giay_1.html", html, false);
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		
		String xpath_title = "//div[@class='span7 des-story']/h1[1]";
		String xpath_author = "//div[@class='span7 des-story']/p[1]/strong[1]/a[1]/text()";
		String xpath_status = "//div[@class='span7 des-story']/p[2]/strong[1]/text()";
		String xpath_des = "//div[@class='span7 des-story']/div[@class='description']";
		String xpath_img = "//div[@class='span3 story-detail']/img[1]/@src";
		
		
		String title = (String) reader.read(xpath_title, XPathConstants.STRING);
		String author = (String) reader.read(xpath_author, XPathConstants.STRING);
		String status = (String) reader.read(xpath_status, XPathConstants.STRING);
		String description = (String) reader.read(xpath_des, XPathConstants.STRING);
		String img = (String) reader.read(xpath_img, XPathConstants.STRING);
		
		KKTDAO kktdao = new KKTDAO();
		Comic comic = new Comic();
		ComicChapter comicChapter = new ComicChapter();
		
		
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH)+1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		
		String strM = month<10? "0"+month:month+"";
		String strD = day<10? "0"+day:day+"";
		
		comic.title = title;
		comic.title_no_sign =UTF8Tool.coDau2KoDau(title);
		comic.author = author;
		comic.c_chapter = 0;
		comic.content = description;
		comic.image = img;
		comic.create_user ="admin";
		comic.modify_user = "admin";
		comic.tags = "";
		comic.create_date =Calendar.getInstance().getTimeInMillis()/1000;
		comic.modify_date =Calendar.getInstance().getTimeInMillis()/1000;
		comic.app_ids = app_id;
		if("Hoàn thành".equalsIgnoreCase(status)){
			comic.status=1;
		}
		comic.get_link_by = 2;
		int comic_id = kktdao.SaveComic(comic);
		comicChapter.comic_id = comic_id;
		
		System.out.println(title);
		System.out.println(author);
		System.out.println(status);
		System.out.println(description);
		System.out.println(img);
		
		String pathFolder = downloadFolder+year+"/"+strM+strD+"/"+StringUtil.getAlias(title)+"-"+comic_id;
		String linkImage = "/upload/comic/"+year+"/"+strM+strD+"/"+StringUtil.getAlias(title)+"-"+comic_id;
		File file = new File(pathFolder);
		if(!file.exists())file.mkdirs();
		
		
		saveImage(img,pathFolder+ img.substring(img.lastIndexOf("/")));
		kktdao.UpdateComic(comic_id, linkImage+ img.substring(img.lastIndexOf("/")));
		
		
		client = new HttpClientImpl();
		res = client.fetch(url_chapter);
		String htmlChapter = HttpClientUtil.getResponseBody(res);;
		XPathReader reader_page = CrawlerUtil.createXPathReaderByData(htmlChapter);
		String xpath_chapter_tr="//select[@id='rdChapter']/option";
		NodeList nodes = (NodeList) reader_page.read(xpath_chapter_tr, XPathConstants.NODESET);
		int node_one_many = nodes.getLength();
		System.out.println(node_one_many);
		
		comic.c_chapter = node_one_many;
		kktdao.UpdateComic(comic_id, node_one_many);
		
		for (int c : arrCategory) {
			CategoryComic categoryComic = new CategoryComic(); 
			categoryComic.cat_id = c;
			categoryComic.comic_id = comic_id;
			kktdao.SaveCategoryComic(categoryComic);
		}
		
		return comic_id;
		
		
	
	}
	
	 public  String getHTMLPage(String url,String storyID,String page) throws Exception {
	    	
	        DefaultHttpClient client = HttpClientFactory.getInstance() ;
	        client.getParams().setParameter("application/x-www-form-urlencoded",true) ;
	        client.getParams().setParameter("accept", "	application/json, text/javascript, */*; q=0.01");
	        client.getParams().setParameter("Content-Type","application/x-www-form-urlencoded; charset=UTF-8") ;
	        client.getParams().setParameter("X-Requested-With","XMLHttpRequest") ;
	        client.getParams().setParameter("Referer","http://vuitruyentranh.vn/truyen-tranh/angel-sanctuary/40/page/2") ;
	        client.getParams().setParameter("User-Agent","Mozilla/5.0 (Windows NT 6.2; rv:20.0) Gecko/20100101 Firefox/20.0") ;
	        client.getParams().setParameter("Cookie","__utma=124071821.1544760243.1368608515.1369112191.1369120719.11; __utmz=124071821.1368608515.1.1.utmgclid=CMrZ6YLgl7cCFWlT4godBHoAMg|utmccn=(not%20set)|utmcmd=(not%20set)|utmctr=truyen%20tranh%20vui; __utmb=124071821.8.10.1369120719; PHPSESSID=g2vuobf1clbjf44nmg9thddpi6; __utmc=124071821; v_a_ban_id=null; v_a_event_id=null; v_a_p_id=null; v_a_type_id=null") ;
	        
	        
	        client.getParams().setParameter("Accept-Encoding","	gzip, deflate") ;
	        client.getParams().setParameter("Accept-Language","en-US,en;q=0.5") ;
	        client.getParams().setParameter("Cache-Control","no-cache") ;
	        
	        client.getParams().setParameter("Connection","keep-alive") ;
	        client.getParams().setParameter("Content-Length","20") ;
	        client.getParams().setParameter("Host","vuitruyentranh.vn") ;
	        client.getParams().setParameter("Pragma","no-cache") ;
	        
	    	HttpPost post = new HttpPost(url) ;
	        List<NameValuePair> list = new ArrayList<NameValuePair>() ;
	        list.add(new BasicNameValuePair("id", storyID));
	        list.add(new BasicNameValuePair("order", "0"));	    
	        list.add(new BasicNameValuePair("page", page));
		
		    post.setEntity(new UrlEncodedFormEntity(list)) ;
	        HttpResponse res = client.execute(post) ;
		    String html = HttpClientUtil.getResponseBody(res) ;
	       // System.out.println(html);
	    	
	        return html;
	    }
	 
	public static void saveImage(String imageUrl, String destinationFile) throws IOException {
		URL url = new URL(imageUrl);
		InputStream is = url.openStream();
		OutputStream os = new FileOutputStream(destinationFile);

		byte[] b = new byte[2048];
		int length;

		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}

		is.close();
		os.close();
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
	
	public static void main(String[] args) {
		CrawlerTruyenTranh classifieExtractBean = new CrawlerTruyenTranh();
		String url_chapter = "http://vuitruyentranh.vn/truyen-tranh/loi-nguyen-one-shot/chapter-1/16333/1/2/";
		String url_truyen = "http://vuitruyentranh.vn/truyen-tranh/loi-nguyen-one-shot/";
		String downloadFolder ="d:/comic/";
		int[] arrCategory = {49};
		int app_id = 64;int content_id = 16333;
		int chapterFrom = 1;
		
		/*if(args!=null&&args.length>0){
			String arrcate[] = args[0].split(",");
			arrCategory = new int[arrcate.length];
			int i = 0;
			for (String string : arrcate) {
				arrCategory[i] = Integer.parseInt(string);
				i++;
			}
			
			// Comic ID
			content_id = Integer.parseInt(args[1]);
			downloadFolder="/home/webhome/kenhkiemtien.com/upload/comic/";
			url_truyen = args[2];
			url_chapter = args[3];
			chapterFrom = Integer.parseInt(args[4]);
		}
		*/
		downloadFolder="/home/webhome/kenhkiemtien.com/upload/comic/";
		ArrayList<String> arrUrls = hdc.util.io.FileUtil.readFileComicUrls();
		String arrPara[] = new String[5];
		for (String line : arrUrls) {
			arrPara = line.split(" ");
			
			// Category
			String arrcate[] = arrPara[0].split(",");
			arrCategory = new int[arrcate.length];
			int i = 0;
			for (String string : arrcate) {
				arrCategory[i] = Integer.parseInt(string);
				i++;
			}
			// comic id
			content_id = Integer.parseInt(arrPara[1]);
			// url 
			url_truyen = arrPara[2];
			url_chapter = arrPara[3];
			chapterFrom = Integer.parseInt(arrPara[4]);
			
			System.out.println("Category:"+arrPara[0]);
			System.out.println("content_id:"+arrPara[1]);
			System.out.println("url_truyen:"+arrPara[2]);
			System.out.println("url_chapter:"+arrPara[3]);
			System.out.println("chapterFrom:"+arrPara[4]);
			

			try {
				//page so page chuong
				 int comic_id = classifieExtractBean.extractTruyenTranhInfo(arrCategory,app_id,url_truyen+content_id+"/",url_chapter,2,downloadFolder);
				 KKTDAO kktdao = new KKTDAO();
				 Comic comic = kktdao.getComic(comic_id);
				 classifieExtractBean.extractTruyenTranh(comic_id,comic.title,content_id,url_truyen,url_chapter,downloadFolder,chapterFrom);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		

	}
}
