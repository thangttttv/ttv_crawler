package com.az24.crawler;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.DocumentAnalyzer;
import hdc.crawler.AbstractCrawler.Url;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.util.html.A;
import hdc.util.html.HttpURL;
import hdc.util.html.parser.XPathReader;
import hdc.util.io.FileUtil;
import hdc.util.lang.UriID;
import hdc.util.text.StringUtil;

import java.io.IOException;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import jdbm.PrimaryHashMap;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

import org.apache.http.HttpResponse;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.az24.crawler.article.DownloadImage;
import com.az24.crawler.config.JdbmXmlConfig;
import com.az24.crawler.config.UrlInjectXmlConfig;
import com.az24.crawler.fiter.AbstractFilter;
import com.az24.crawler.model.DatabaseConfig;
import com.az24.crawler.model.ImageConfig;
import com.az24.dao.ConnectionDataLog;
import com.az24.dao.CrawlerLogDAO;
import com.az24.test.HttpClientUtil;
import com.az24.util.FileLog;

public class CrawlerInjectUrl {
	private String fileUrlConfig = "";
	private String fileJdbmConfig = "";
	private String fileBeanConfig = "";
	private int fetch = 1;
	private PrimaryHashMap<String, Url> urlPrimary;
	private RecordManager urlManager = null;

	protected RecordManager urlImageManager;
	protected PrimaryHashMap<String, ImageConfig> urlImagePrimary;

	private Connection conn_log = null;
	private DownloadImage downloadImage = null;
	public CrawlerInjectUrl(String fileUrl, String fileJdbmConfig,
			String fileBeanConfig, int fetch, int is_slow) {
		this.fileUrlConfig = fileUrl;
		this.fileJdbmConfig = fileJdbmConfig;
		this.fileBeanConfig = fileBeanConfig;
		this.fetch = fetch;

	}

	public void collectionUrl() {
		try {
			conn_log = ConnectionDataLog.connection(fileBeanConfig);
			JdbmXmlConfig.parseConfig(fileJdbmConfig);
			// delete file url
			FileUtil.deleteFile(JdbmXmlConfig.url);
			urlManager = RecordManagerFactory
					.createRecordManager(JdbmXmlConfig.url + "/url");
			urlPrimary = urlManager.hashMap("url");

			urlImageManager = RecordManagerFactory
					.createRecordManager(JdbmXmlConfig.url_image + "/image");
			urlImagePrimary = urlImageManager.hashMap("image");

			UrlInjectXmlConfig urlInjectXmlConfig = new UrlInjectXmlConfig(
					this.fileUrlConfig);
			urlInjectXmlConfig.parseConfig();
			// Get Link Crawler
			List<Url> urls = UrlInjectXmlConfig.urlConfigs;
			List<Object> urlRegex = UrlInjectXmlConfig.regrexConfigs;
			String baseUrl = UrlInjectXmlConfig.baseUrl;
			String rewriterUrl = UrlInjectXmlConfig.rewriterUrl;
			
			int totalUrl = 0;
			for (Url url : urls) {							
				conn_log = ConnectionDataLog.connection(fileBeanConfig);				
				totalUrl += putLink(url, baseUrl, rewriterUrl, urlRegex,
						this.fetch);				
				if(totalUrl>5000) break;
				Thread.sleep(1000);
				conn_log.close();
			}
			
			
			urlManager.close();
			urlImageManager.close();
			
			Calendar calendar = Calendar.getInstance();
			String log = calendar.getTime().toString() + "-->Tong Url:"
					+ totalUrl;
			System.out.println(log);
			FileLog.createFileLog(JdbmXmlConfig.file_log + "_log_"
					+ calendar.get(Calendar.DAY_OF_MONTH)
					+ calendar.get(Calendar.MONTH)
					+ calendar.get(Calendar.YEAR) + ".txt");
			FileLog.writer(log);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void collectionUrl(String date) {
		try {
			conn_log = ConnectionDataLog.connection(fileBeanConfig);
			JdbmXmlConfig.parseConfig(fileJdbmConfig);
			// delete file url
			FileUtil.deleteFile(JdbmXmlConfig.url);			
			urlManager = RecordManagerFactory
					.createRecordManager(JdbmXmlConfig.url + "/url");
			urlPrimary = urlManager.hashMap("url");

			urlImageManager = RecordManagerFactory
					.createRecordManager(JdbmXmlConfig.url_image + "/image");
			urlImagePrimary = urlImageManager.hashMap("image");

			UrlInjectXmlConfig urlInjectXmlConfig = new UrlInjectXmlConfig(
					this.fileUrlConfig);
			urlInjectXmlConfig.parseConfig();
			// Get Link Crawler
			List<Url> urls = UrlInjectXmlConfig.urlConfigs;
			List<Object> urlRegex = UrlInjectXmlConfig.regrexConfigs;
			String baseUrl = UrlInjectXmlConfig.baseUrl;
			String rewriterUrl = UrlInjectXmlConfig.rewriterUrl;
			
			int totalUrl = 0;
			for (Url url : urls) {			
				conn_log = ConnectionDataLog.connection(fileBeanConfig);				
				totalUrl += putLink(url, baseUrl, rewriterUrl, urlRegex,
						this.fetch);				
				if(totalUrl>5000) break;
				Thread.sleep(1000);
				conn_log.close();
			}
			
			
			urlManager.close();
			urlImageManager.close();
			
			Calendar calendar = Calendar.getInstance();
			String log = calendar.getTime().toString() + "-->Tong Url:"
					+ totalUrl;
			System.out.println(log);
			FileLog.createFileLog(JdbmXmlConfig.file_log + "_log_"
					+ calendar.get(Calendar.DAY_OF_MONTH)
					+ calendar.get(Calendar.MONTH)
					+ calendar.get(Calendar.YEAR) + ".txt");
			FileLog.writer(log);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private int putLink(Url url, String baseUrl, String rewriterUrl,
			List<Object> urlRegex, int fetch) throws Exception {
		String html = "";

		if (fetch == 1) {			HttpClientImpl client = new HttpClientImpl();
			HttpResponse res = client.fetch(url.url);
			html = HttpClientUtil.getResponseBody(res);
		} else {
			html = HttpClientUtil.getHtml(url.url);
		}	System.out.println("Crawler-->"+url.url);
		int count = 0;
		switch (url.collection) {
		case 1:
			count = crawlerLink(url, baseUrl, rewriterUrl, urlRegex, html);
			break;
		case 2:
			count = crawlerLink(url, urlRegex, html);
			break;
		case 3:
			count = crawlerLinkAfamily(url, urlRegex, html);
			break;
		case 4:
			count = crawlerLinkZing(url, baseUrl, rewriterUrl, urlRegex, html);
			break;
		case 5:
			count = crawlerLinkZingTV(url, urlRegex, html);
			break;
		case 6:
			count = crawlerLinkZingVL(url, urlRegex, html);
			break;
		case 7:
			count = crawlerLinkKenh14(url, urlRegex, html);
			break;
		default:
			count = crawlerLink(url, baseUrl, rewriterUrl, urlRegex, html);
			break;
		}

		return count;
	}

	private int crawlerLink(Url url, String baseUrl, String rewriterUrl,
			List<Object> urlRegex, String html) {
		CrawlerLogDAO crawlerLogDAO = new CrawlerLogDAO();
		DocumentAnalyzer analyzer;
		int i = 0;
		try {
			analyzer = new DocumentAnalyzer.DefaultDocumentAnayzer(baseUrl,
					rewriterUrl);
			System.out.println("Crawler-->"+url.url);
			List<A> list= null;
			try {
			 list = analyzer.analyze(html, url.url);
			}catch (Exception e) {
				list= null;
			}
			Url injectUrl = null;
			String id = "";
			if (list != null)
				for (A a : list) {
					System.out.println(new Date()+" Colection Link :"+ a.getURL());
					id = new UriID(new HttpURL(a.getURL())).getIdAsString();
					//Check Link Exist In Log Crawler
					if (crawlerLogDAO.checkEntity(DatabaseConfig.table_entity_log, id))
						continue;
					//Validate link
					boolean kt = AbstractFilter.find((String) url.regex, a.getURL());
					if (kt&&!urlPrimary.containsKey(id)) {
						injectUrl = new Url();
						injectUrl.id = id;
						injectUrl.cat_id = url.cat_id;
						if(a.getURL().indexOf("http")==-1)
							a.setUrl(baseUrl+"/"+a.getURL());
						injectUrl.url = a.getURL();
						urlPrimary.put(id, injectUrl);
						
						System.out.println(new Date()+" Inject Link Thu: "+i+" "+ a.getURL());
						i++;
					}
					if (i % 10 == 0)
						urlManager.commit();
					
				}
			urlManager.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return 0 ;
		}
		return i ;
	}

	public int crawlerLink(Url url, List<Object> urlRegex, String html) {
		CrawlerLogDAO crawlerLogDAO = new CrawlerLogDAO();
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		
		Calendar calendar = Calendar.getInstance();
		int count = 0;
		try {		
			XPath xpath = XPathFactory.newInstance().newXPath();
			XPathExpression expr = xpath.compile("//a");
			NodeList nodes = (NodeList) expr.evaluate(reader.getDocument(),
					XPathConstants.NODESET);
			Node nodeA = null;
			String imgSrc = "", href = "";
			Url injectUrl = null;
			String id = "";

			String day = calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0"
					+ calendar.get(Calendar.DAY_OF_MONTH) : ""
					+ calendar.get(Calendar.DAY_OF_MONTH);
			int intmonth = calendar.get(Calendar.MONTH) + 1;
			String month = intmonth < 10 ? "0" + intmonth
					: "" + intmonth;

			for (int i = 0; i < nodes.getLength(); i++) {
				imgSrc = "";
				href = "";
				nodeA = nodes.item(i);
				if (nodeA.getAttributes().getNamedItem("href") != null)
					href = nodeA.getAttributes().getNamedItem("href").getTextContent();
					System.out.println(href);
					
					if (nodeA.hasChildNodes()) {
						NodeList list = nodeA.getChildNodes();
						int k = 0;						
						while (k < list.getLength()) {
							Node nodeImg = list.item(k);						
							k++;
							if (nodeImg.getAttributes() != null) {
								if (nodeImg.getAttributes().getNamedItem("src") != null) {
									imgSrc = nodeImg.getAttributes()
											.getNamedItem("src")
											.getTextContent();									
									href = nodeA.getAttributes().getNamedItem(
											"href").getTextContent();
									System.out.println(imgSrc);
									System.out.println(href);
									break;
								}
							}
						}
					}
					
				if(!StringUtil.isEmpty(href))
					id = new UriID(new HttpURL(href)).getIdAsString();
				else continue;
				
				if (crawlerLogDAO.checkEntity(DatabaseConfig.table_entity_log, id))
					continue;
			
				boolean kt = AbstractFilter.find((String) url.regex, href);
				if (!StringUtil.isEmpty(href)&&urlRegex.size() > 0&&!StringUtil.isEmpty(imgSrc)	&& kt&&!urlPrimary.containsKey(id)) {
					id = new UriID(new HttpURL(href)).getIdAsString();
					injectUrl = new Url();
					injectUrl.id = id;
					injectUrl.cat_id = url.cat_id;
					if (href.indexOf("http") < 0) {
						href =  UrlInjectXmlConfig.baseUrl + href;
					}
					injectUrl.url = href;System.out.println(href);
					urlPrimary.put(id, injectUrl);
					System.out.println("Inject-->i=" + count + href);					
					if(!StringUtil.isEmpty(imgSrc))
					{
					ImageConfig imageConfig = new ImageConfig();
				
					imageConfig.id = id;
					imageConfig.src = imgSrc;
					imageConfig.name ="small_"+System.currentTimeMillis()+imgSrc
							.substring(imgSrc.lastIndexOf("/") + 1);
					imageConfig.dateProcess = day + "/" + month + "/"
							+ calendar.get(Calendar.YEAR);
					imageConfig.url_content = href;
					urlImagePrimary.put(id, imageConfig);
					count++;
					downloadImage = new DownloadImage(imgSrc,imageConfig.name,UrlInjectXmlConfig.baseUrl,"",1,1,null);
					downloadImage.run();					
					System.out.println("Image-->i=" + count + imageConfig.name);
					}
				}
				if (count % 30 == 0) {
					urlManager.commit();
					urlImageManager.commit();
				}
				i++;

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;

	}
	
	
	public int crawlerLinkZingTV(Url url, List<Object> urlRegex, String html) {
		CrawlerLogDAO crawlerLogDAO = new CrawlerLogDAO();
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		
		Calendar calendar = Calendar.getInstance();
		int count = 0;
		try {		
			XPath xpath = XPathFactory.newInstance().newXPath();
			XPathExpression expr = xpath.compile("//a");
			NodeList nodes = (NodeList) expr.evaluate(reader.getDocument(),
					XPathConstants.NODESET);
			Node nodeA = null;
			String imgSrc = "", href = "";
			Url injectUrl = null;
			String id = "";

			String day = calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0"
					+ calendar.get(Calendar.DAY_OF_MONTH) : ""
					+ calendar.get(Calendar.DAY_OF_MONTH);
			int intmonth = calendar.get(Calendar.MONTH) + 1;
			String month = intmonth < 10 ? "0" + intmonth
					: "" + intmonth;

			for (int i = 0; i < nodes.getLength(); i++) {
				imgSrc = "";
				href = "";
				nodeA = nodes.item(i);
				if (nodeA.getAttributes().getNamedItem("href") != null)
					href = nodeA.getAttributes().getNamedItem("href").getTextContent();
					System.out.println(href);					
					if (nodeA.hasChildNodes()) {
						NodeList list = nodeA.getChildNodes();
						int k = 0;						
						while (k < list.getLength()) {
							Node nodeImg = list.item(k);						
							k++;
							if (nodeImg.getAttributes() != null) {
								if (nodeImg.getAttributes().getNamedItem("src") != null) {
									imgSrc = nodeImg.getAttributes()
											.getNamedItem("src")
											.getTextContent();									
									href = nodeA.getAttributes().getNamedItem(
											"href").getTextContent();
							
									break;
								}
							}
						}
					}
					
				if(!StringUtil.isEmpty(href))
					id = new UriID(new HttpURL(href)).getIdAsString();
				else continue;
				
				if (crawlerLogDAO.checkEntity(
						DatabaseConfig.table_entity_log, id))
					continue;
			
				boolean kt = AbstractFilter.find(url.regex, href);
				if (!StringUtil.isEmpty(href)&&urlRegex.size() > 0&&kt&&!urlPrimary.containsKey(id)) {
					id = new UriID(new HttpURL(href)).getIdAsString();
					injectUrl = new Url();
					injectUrl.id = id;
					injectUrl.cat_id = url.cat_id;
					if (href.indexOf("http") < 0) {
						href =  UrlInjectXmlConfig.baseUrl + href;
					}
					injectUrl.url = href;System.out.println(href);
					urlPrimary.put(id, injectUrl);
					System.out.println("Inject-->i=" + count + href);					
					if(!StringUtil.isEmpty(imgSrc))
					{
					ImageConfig imageConfig = new ImageConfig();
				
					imageConfig.id = id;
					imageConfig.src = imgSrc;
					imageConfig.name ="small_"+System.currentTimeMillis()+imgSrc
							.substring(imgSrc.lastIndexOf("/") + 1);
					imageConfig.dateProcess = day + "/" + month + "/"
							+ calendar.get(Calendar.YEAR);
					imageConfig.url_content = href;
					urlImagePrimary.put(id, imageConfig);
					count++;
					downloadImage = new DownloadImage(imgSrc,imageConfig.name,UrlInjectXmlConfig.baseUrl,"",1,1,null);
					downloadImage.run();					
					System.out.println("Image-->i=" + count + imageConfig.name);
					}
				}
				if (count % 30 == 0) {
					urlManager.commit();
					urlImageManager.commit();
				}
				i++;

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;

	}
	
	
	public int crawlerLinkZingVL(Url url, List<Object> urlRegex, String html) {
		CrawlerLogDAO crawlerLogDAO = new CrawlerLogDAO();
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		
		Calendar calendar = Calendar.getInstance();
		int count = 0;
		try {		
			XPath xpath = XPathFactory.newInstance().newXPath();
			XPathExpression expr = xpath.compile("//div[@class='cont09im']/a");
			NodeList nodes = (NodeList) expr.evaluate(reader.getDocument(),
					XPathConstants.NODESET);
			Node nodeA = null;
			String imgSrc = "", href = "";
			Url injectUrl = null;
			String id = "";

			String day = calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0"
					+ calendar.get(Calendar.DAY_OF_MONTH) : ""
					+ calendar.get(Calendar.DAY_OF_MONTH);
			int intmonth = calendar.get(Calendar.MONTH) + 1;
			String month = intmonth < 10 ? "0" + intmonth
					: "" + intmonth;

			for (int i = 0; i < nodes.getLength(); i++) {
				imgSrc = "";
				href = "";
				nodeA = nodes.item(i);
				if (nodeA.getAttributes().getNamedItem("href") != null)
					href = nodeA.getAttributes().getNamedItem("href").getTextContent();
					System.out.println(href);
					
					if (nodeA.hasChildNodes()) {
						NodeList list = nodeA.getChildNodes();
						int k = 0;						
						while (k < list.getLength()) {
							Node nodeImg = list.item(k);						
							k++;
							if (nodeImg.getAttributes() != null) {
								if (nodeImg.getAttributes().getNamedItem("src") != null) {
									imgSrc = nodeImg.getAttributes()
											.getNamedItem("src")
											.getTextContent();									
									href = nodeA.getAttributes().getNamedItem(
											"href").getTextContent();
									System.out.println(imgSrc);
									System.out.println(href);
									break;
								}
							}
						}
					}
					
				if(!StringUtil.isEmpty(href))
					id = new UriID(new HttpURL(href)).getIdAsString();
				else continue;
				
				if (crawlerLogDAO.checkEntity(
						DatabaseConfig.table_entity_log, id))
					continue;
			
				boolean kt = AbstractFilter.find((String) url.regex, href);
				if (!StringUtil.isEmpty(href)&&!StringUtil.isEmpty(imgSrc)	&& kt&&!urlPrimary.containsKey(id)) {
					id = new UriID(new HttpURL(href)).getIdAsString();
					injectUrl = new Url();
					injectUrl.id = id;
					injectUrl.cat_id = url.cat_id;
					if (href.indexOf("http") < 0) {
						href =  UrlInjectXmlConfig.baseUrl + href;
					}
					injectUrl.url = href;System.out.println(href);
					urlPrimary.put(id, injectUrl);
					System.out.println("Inject-->i=" + count + href);					
					if(!StringUtil.isEmpty(imgSrc))
					{
					ImageConfig imageConfig = new ImageConfig();
				
					imageConfig.id = id;
					imageConfig.src = imgSrc;
					imageConfig.name ="small_"+System.currentTimeMillis()+imgSrc
							.substring(imgSrc.lastIndexOf("/") + 1);
					imageConfig.dateProcess = day + "/" + month + "/"
							+ calendar.get(Calendar.YEAR);
					imageConfig.url_content = href;
					urlImagePrimary.put(id, imageConfig);
					count++;
					downloadImage = new DownloadImage(imgSrc,imageConfig.name,UrlInjectXmlConfig.baseUrl,"",1,1,null);
					downloadImage.run();					
					System.out.println("Image-->i=" + count + imageConfig.name);
					}
				}
				if (count % 30 == 0) {
					urlManager.commit();
					urlImageManager.commit();
				}
				i++;

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;

	}
	
	public int crawlerLinkKenh14(Url url, List<Object> urlRegex, String html) {
		CrawlerLogDAO crawlerLogDAO = new CrawlerLogDAO();
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		
		Calendar calendar = Calendar.getInstance();
		int count = 0;
		try {		
			XPath xpath = XPathFactory.newInstance().newXPath();
			//| //div[@id='otherfeature']/div[@class='item']/a
			// or @class='imgwrapper' 
			XPathExpression expr = xpath.compile("//div[@class='img']/a");
			NodeList nodes = (NodeList) expr.evaluate(reader.getDocument(),
					XPathConstants.NODESET);
			Node nodeA = null;
			String imgSrc = "", href = "";
			Url injectUrl = null;
			String id = "";

			String day = calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0"
					+ calendar.get(Calendar.DAY_OF_MONTH) : ""
					+ calendar.get(Calendar.DAY_OF_MONTH);
			int intmonth = calendar.get(Calendar.MONTH) + 1;
			String month = intmonth < 10 ? "0" + intmonth
					: "" + intmonth;

			for (int i = 0; i < nodes.getLength(); i++) {
				imgSrc = "";
				href = "";
				nodeA = nodes.item(i);
				if (nodeA.getAttributes().getNamedItem("href") != null)
					href = nodeA.getAttributes().getNamedItem("href").getTextContent();
					System.out.println(href);
					
					if (nodeA.hasChildNodes()) {
						NodeList list = nodeA.getChildNodes();
						int k = 0;						
						while (k < list.getLength()) {
							Node nodeImg = list.item(k);						
							k++;
							if (nodeImg.getAttributes() != null) {
								if (nodeImg.getAttributes().getNamedItem("src") != null) {
									imgSrc = nodeImg.getAttributes()
											.getNamedItem("src")
											.getTextContent();									
									href = nodeA.getAttributes().getNamedItem(
											"href").getTextContent();
									System.out.println(imgSrc);
									System.out.println(href);
									break;
								}
							}
						}
					}
					
				if(!StringUtil.isEmpty(href))
					id = new UriID(new HttpURL(href)).getIdAsString();
				else continue;
				
				if (crawlerLogDAO.checkEntity(
						DatabaseConfig.table_entity_log, id))
					continue;
			
				boolean kt = AbstractFilter.find((String) url.regex, href);
				if (!StringUtil.isEmpty(href)&&!StringUtil.isEmpty(imgSrc)	&& kt&&!urlPrimary.containsKey(id)) {
					id = new UriID(new HttpURL(href)).getIdAsString();
					injectUrl = new Url();
					injectUrl.id = id;
					injectUrl.cat_id = url.cat_id;
					if (href.indexOf("http") < 0) {
						href =  UrlInjectXmlConfig.baseUrl + href;
					}
					injectUrl.url = href;System.out.println(href);
					urlPrimary.put(id, injectUrl);
					System.out.println("Inject-->i=" + count + href);					
					if(!StringUtil.isEmpty(imgSrc))
					{
					ImageConfig imageConfig = new ImageConfig();
				
					imageConfig.id = id;
					imageConfig.src = imgSrc;
					imageConfig.name ="small_"+System.currentTimeMillis()+imgSrc
							.substring(imgSrc.lastIndexOf("/") + 1);
					imageConfig.dateProcess = day + "/" + month + "/"
							+ calendar.get(Calendar.YEAR);
					imageConfig.url_content = href;
					urlImagePrimary.put(id, imageConfig);
					count++;
					downloadImage = new DownloadImage(imgSrc,imageConfig.name,UrlInjectXmlConfig.baseUrl,"",1,1,null);
					downloadImage.run();					
					System.out.println("Image-->i=" + count + imageConfig.name);
					}
				}
				if (count % 30 == 0) {
					urlManager.commit();
					urlImageManager.commit();
				}
				i++;

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;

	}
	
	public int crawlerLinkIone(Url url, List<Object> urlRegex, String html) {
		CrawlerLogDAO crawlerLogDAO = new CrawlerLogDAO();
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		
		Calendar calendar = Calendar.getInstance();
		int count = 0;
		try {		
			XPath xpath = XPathFactory.newInstance().newXPath();
			XPathExpression expr = xpath.compile("//div[@class='highlight1 subject_list_2']/a");
			NodeList nodes = (NodeList) expr.evaluate(reader.getDocument(),
					XPathConstants.NODESET);
			Node nodeA = null;
			String imgSrc = "", href = "";
			Url injectUrl = null;
			String id = "";

			String day = calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0"
					+ calendar.get(Calendar.DAY_OF_MONTH) : ""
					+ calendar.get(Calendar.DAY_OF_MONTH);
			int intmonth = calendar.get(Calendar.MONTH) + 1;
			String month = intmonth < 10 ? "0" + intmonth
					: "" + intmonth;

			for (int i = 0; i < nodes.getLength(); i++) {
				imgSrc = "";
				href = "";
				nodeA = nodes.item(i);
				if (nodeA.getAttributes().getNamedItem("href") != null)
					href = nodeA.getAttributes().getNamedItem("href").getTextContent();
					System.out.println(href);
					
					if (nodeA.hasChildNodes()) {
						NodeList list = nodeA.getChildNodes();
						int k = 0;						
						while (k < list.getLength()) {
							Node nodeImg = list.item(k);						
							k++;
							if (nodeImg.getAttributes() != null) {
								if (nodeImg.getAttributes().getNamedItem("src") != null) {
									imgSrc = nodeImg.getAttributes()
											.getNamedItem("src")
											.getTextContent();									
									href = nodeA.getAttributes().getNamedItem(
											"href").getTextContent();
									System.out.println(imgSrc);
									System.out.println(href);
									break;
								}
							}
						}
					}
					
				if(!StringUtil.isEmpty(href))
					id = new UriID(new HttpURL(href)).getIdAsString();
				else continue;
				
				if (crawlerLogDAO.checkEntity(
						DatabaseConfig.table_entity_log, id))
					continue;
			
				boolean kt = AbstractFilter.find((String) url.regex, href);
				if (!StringUtil.isEmpty(href)&&!StringUtil.isEmpty(imgSrc)	&& kt&&!urlPrimary.containsKey(id)) {
					id = new UriID(new HttpURL(href)).getIdAsString();
					injectUrl = new Url();
					injectUrl.id = id;
					injectUrl.cat_id = url.cat_id;
					if (href.indexOf("http") < 0) {
						href =  UrlInjectXmlConfig.baseUrl + href;
					}
					injectUrl.url = href;System.out.println(href);
					urlPrimary.put(id, injectUrl);
					System.out.println("Inject-->i=" + count + href);					
					if(!StringUtil.isEmpty(imgSrc))
					{
					ImageConfig imageConfig = new ImageConfig();
				
					imageConfig.id = id;
					imageConfig.src = imgSrc;
					imageConfig.name ="small_"+System.currentTimeMillis()+imgSrc
							.substring(imgSrc.lastIndexOf("/") + 1);
					imageConfig.dateProcess = day + "/" + month + "/"
							+ calendar.get(Calendar.YEAR);
					imageConfig.url_content = href;
					urlImagePrimary.put(id, imageConfig);
					count++;
					downloadImage = new DownloadImage(imgSrc,imageConfig.name,UrlInjectXmlConfig.baseUrl,"",1,1,null);
					downloadImage.run();					
					System.out.println("Image-->i=" + count + imageConfig.name);
					}
				}
				if (count % 30 == 0) {
					urlManager.commit();
					urlImageManager.commit();
				}
				i++;

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;

	}
	
	public int crawlerLinkZing(Url url, String baseUrl, String rewriterUrl,
			List<Object> urlRegex, String html) {
		CrawlerLogDAO crawlerLogDAO = new CrawlerLogDAO();
		DocumentAnalyzer analyzer;
		int i = 0;
		Calendar calendar = Calendar.getInstance();
		String day = calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0"
				+ calendar.get(Calendar.DAY_OF_MONTH) : ""
				+ calendar.get(Calendar.DAY_OF_MONTH);
		int intmonth = calendar.get(Calendar.MONTH) + 1;
		String month = intmonth < 10 ? "0" + intmonth
				: "" + intmonth;
		try {
			Pattern comment = Pattern
			.compile("\\w'\\w");
			Matcher mcomment = comment.matcher(html);
			while (mcomment.find()) {
				html = mcomment.replaceAll("");
				
			}
			analyzer = new DocumentAnalyzer.DefaultDocumentAnayzer(baseUrl,
					rewriterUrl);
			System.out.println(url.url);
			List<A> list= null;
			try {
			 list = analyzer.analyze(html, url.url);
			}catch (Exception e) {
				list= null;
			}
			Url injectUrl = null;
			String id = "";
			if (list != null)
				for (A a : list) {
					System.out.println(a.getURL());
					id = new UriID(new HttpURL(a.getURL())).getIdAsString();					
					if (crawlerLogDAO.checkEntity(
							DatabaseConfig.table_entity_log, id))
						continue;
					boolean kt = AbstractFilter.find((String) url.regex, a.getURL());
					if (kt&&!urlPrimary.containsKey(id)&&!StringUtil.isEmpty(a.getImg())) {
						injectUrl = new Url();
						injectUrl.id = id;
						injectUrl.cat_id = url.cat_id;
						injectUrl.url = a.getURL();
						urlPrimary.put(id, injectUrl);
						
						if(!StringUtil.isEmpty(a.getImg()))
						{
						ImageConfig imageConfig = new ImageConfig();
					
						imageConfig.id = id;
						imageConfig.src = a.getImg();
						imageConfig.name ="small_"+System.currentTimeMillis()+a.getImg()
								.substring(a.getImg().lastIndexOf("/") + 1);
						imageConfig.dateProcess = day + "/" + month + "/"
								+ calendar.get(Calendar.YEAR);
						imageConfig.url_content = a.getURL();
						urlImagePrimary.put(id, imageConfig);						
						downloadImage = new DownloadImage(a.getImg(),imageConfig.name,UrlInjectXmlConfig.baseUrl,"",1,1,null);
						downloadImage.run();					
						}
						
						System.out.println("Inject-->i=" + i + a.getURL());
						i++;
					}
					if (i % 10 == 0)
						urlManager.commit();
					
				}
			urlManager.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return 0 ;
		}
		return i ;
	}
	
		
	public int crawlerLinkAfamily(Url url, List<Object> urlRegex, String html) {
		CrawlerLogDAO crawlerLogDAO = new CrawlerLogDAO();
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		
		Calendar calendar = Calendar.getInstance();
		int count = 0;
		try {		
			XPath xpath = XPathFactory.newInstance().newXPath();
			XPathExpression expr = xpath.compile("//a");
			NodeList nodes = (NodeList) expr.evaluate(reader.getDocument(),
					XPathConstants.NODESET);
			Node nodeA = null;
			String imgSrc = "", href = "";
			Url injectUrl = null;
			String id = "";
			String day = calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0"
					+ calendar.get(Calendar.DAY_OF_MONTH) : ""
					+ calendar.get(Calendar.DAY_OF_MONTH);
			int intmonth = calendar.get(Calendar.MONTH) + 1;
			String month = intmonth < 10 ? "0" + intmonth
					: "" + intmonth;

			for (int i = 0; i < nodes.getLength(); i++) {
				imgSrc = "";
				href = "";
				nodeA = nodes.item(i);
				if (nodeA.getAttributes().getNamedItem("href") != null)
					href = nodeA.getAttributes().getNamedItem("href").getTextContent();
					//System.out.println(href);
					if (nodeA.hasChildNodes()) {
						NodeList list = nodeA.getChildNodes();
						int k = 0;						
						while (k < list.getLength()) {
							Node nodeImg = list.item(k);						
							k++;
							if (nodeImg.getAttributes() != null) {
								if (nodeImg.getAttributes().getNamedItem("src") != null) {
									imgSrc = nodeImg.getAttributes()
											.getNamedItem("src")
											.getTextContent();									
									href = nodeA.getAttributes().getNamedItem(
											"href").getTextContent();
									if(imgSrc.indexOf("icon_")>0) imgSrc ="";
									System.out.println("ImageAvatear---------------------->"+imgSrc);
									
									break;
								}
							}
						}
					}
					
				if(!StringUtil.isEmpty(href))
					id = new UriID(new HttpURL(href)).getIdAsString();
				else continue;
				
				if (crawlerLogDAO.checkEntity(
						DatabaseConfig.table_entity_log, id))
					continue;
			
				boolean kt = AbstractFilter.find((String) url.regex, href);
				if (!StringUtil.isEmpty(href)&&!StringUtil.isEmpty(imgSrc)	&& kt&&!urlPrimary.containsKey(id)) {
					id = new UriID(new HttpURL(href)).getIdAsString();
					injectUrl = new Url();
					injectUrl.id = id;
					injectUrl.cat_id = url.cat_id;
					if (href.indexOf("http") < 0) {
						href =  UrlInjectXmlConfig.baseUrl + href;
					}
					injectUrl.url = href;System.out.println(href);
					urlPrimary.put(id, injectUrl);
					System.out.println("Inject-->i=" + count + href);					
					if(!StringUtil.isEmpty(imgSrc))
					{
					ImageConfig imageConfig = new ImageConfig();
				
					imageConfig.id = id;
					imageConfig.src = imgSrc;
					imageConfig.name ="small_"+System.currentTimeMillis()+imgSrc
							.substring(imgSrc.lastIndexOf("/") + 1);
					imageConfig.dateProcess = day + "/" + month + "/"
							+ calendar.get(Calendar.YEAR);
					imageConfig.url_content = href;
					urlImagePrimary.put(id, imageConfig);
					count++;
					downloadImage = new DownloadImage(imgSrc,imageConfig.name,UrlInjectXmlConfig.baseUrl,"",1,1,null);
					downloadImage.run();					
					System.out.println("Image-->i=" + count + imageConfig.name);
					}
				}
				if (count % 30 == 0) {
					urlManager.commit();
					urlImageManager.commit();
				}
				i++;

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;

	}

	public static void main(String[] args) {
		CrawlerInjectUrl crawlerInjectUrl = new CrawlerInjectUrl(
				"src/com/az24/crawler/config/urlsKenh14.xml",
				"src/com/az24/crawler/config/jdbm.xml",
				"src/com/az24/crawler/config/beanKenh14.xml", 1, 0);
		crawlerInjectUrl.collectionUrl();

		
	
	}

}
