package com.az24.crawler.article;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.DocumentAnalyzer;
import hdc.crawler.ExtractEntity;
import hdc.crawler.Node;
import hdc.crawler.AbstractCrawler.Url;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.util.html.A;
import hdc.util.html.HttpURL;
import hdc.util.html.NodeDeleteVisitor;
import hdc.util.html.NodeNormalizeVisitor;
import hdc.util.html.SelectImgArticleVisitor;
import hdc.util.html.parser.DomWriter;
import hdc.util.html.parser.XPathReader;
import hdc.util.lang.UriID;
import hdc.util.text.HtmlUtil;
import hdc.util.text.StringUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONValue;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import com.az24.crawler.config.BeanXmlConfig;
import com.az24.crawler.config.JdbmXmlConfig;
import com.az24.crawler.config.UrlInjectXmlConfig;
import com.az24.crawler.fiter.AbstractFilter;
import com.az24.crawler.fiter.ContentFilter;
import com.az24.crawler.model.Article;
import com.az24.crawler.model.DatabaseConfig;
import com.az24.crawler.model.ImageConfig;
import com.az24.crawler.model.Property;
import com.az24.dao.ConnectionDataLog;
import com.az24.dao.CrawlerLogDAO;
import com.az24.db.pool.C3p0TinhHinhPool;
import com.az24.test.HttpClientFactory;
import com.az24.test.HttpClientUtil;
import com.az24.util.FileLog;
import com.az24.util.UTF8Tool;

public class CrawlerArticleByPage {

	protected String fileUrlConfig;
	protected String fileBeanConfig;
	protected int time_sleep;
	protected List<Node> nodesDel;
	protected int type_collection_link;
	protected HashMap<String, ImageConfig> imageMap;
	protected SelectImgArticleVisitor selectImgArticleVisitor;
	protected int source;
	protected int provice_id = 0;
	protected int type_get_anh = 0;
	protected int type_get_html = 0;
	protected int type_date = 0;
	protected String provice_alias = "";
	protected int type_download = 1;
	public Connection conn;
	public Connection connLog;
	public List<NameValuePair> listParameter = null;
	private String baseUrl="";
	public String charset="utf-8";
	protected XPathReader reader=null;

	public void initialization(String fileUrl, String fileBeanConfig,
			int type_collection_link, int source, int type_download, int provice_id,
			String provice_alias, int type_get_image, int type_get_html, int type_date) {
		fileUrlConfig = fileUrl;
		this.fileBeanConfig = fileBeanConfig;
		this.type_collection_link = type_collection_link;
		this.source = source;
		this.provice_id = provice_id;
		this.provice_alias = provice_alias;
		this.type_download = type_download;
		this.type_get_anh = type_get_image;
		if(this.type_collection_link==3) this.type_get_anh=1;
		this.type_get_html = type_get_html;
		this.type_date = type_date;
		selectImgArticleVisitor = new SelectImgArticleVisitor(
				"http://images.tinhhinh.net/picture/article/", "");
	}

	public void collectionData() {
		try {
			UrlInjectXmlConfig urlInjectXmlConfig = new UrlInjectXmlConfig(
					fileUrlConfig);
			urlInjectXmlConfig.parseConfig();
			List<Url> urls = UrlInjectXmlConfig.urlConfigs;
			String baseUrl = UrlInjectXmlConfig.baseUrl;
			String rewriterUrl = UrlInjectXmlConfig.rewriterUrl;
			BeanXmlConfig beanXmlConfig = new BeanXmlConfig(fileBeanConfig);
			beanXmlConfig.parseConfig();
			int totalUrl = 0;
			for (Iterator<Url> iterator = urls.iterator(); iterator.hasNext();) {
				hdc.crawler.AbstractCrawler.Url url = (hdc.crawler.AbstractCrawler.Url) iterator
						.next();
				int page = url.fetchNumber;
				this.baseUrl = baseUrl;
				while (page > 0) {
					try {
						imageMap = null;
						System.out.println(new Date() + " Crawler Page Thu="
								+ page);
						HashMap<String, Url> urlMap = new HashMap<String, Url>();
						
						url.url = (new StringBuilder(String.valueOf(url.url_1)))
								.append(page).append(url.url_2).toString();
						System.out.println(new Date() + " Crawler Page URL="
								+ url.url);
						totalUrl += putLink(urlMap, url, baseUrl, rewriterUrl,
								type_collection_link);
						Thread.sleep(time_sleep);
						processData(urlMap, type_collection_link);
						page--;

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			Calendar calendar = Calendar.getInstance();
			String log = (new StringBuilder(String.valueOf(calendar.getTime()
					.toString()))).append("-->Tong Url:").append(totalUrl)
					.toString();
			System.out.println(log);
			FileLog.createFileLog((new StringBuilder(String
					.valueOf(JdbmXmlConfig.file_log))).append("_log_").append(
					calendar.get(5)).append(calendar.get(2)).append(
					calendar.get(1)).append(".txt").toString());
			FileLog.writer(log);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public void collectionDataFrom(int rows) {
		try {
			UrlInjectXmlConfig urlInjectXmlConfig = new UrlInjectXmlConfig(
					fileUrlConfig);
			urlInjectXmlConfig.parseConfig();
			List<Url> urls = UrlInjectXmlConfig.urlConfigs;
			String baseUrl = UrlInjectXmlConfig.baseUrl;
			String rewriterUrl = UrlInjectXmlConfig.rewriterUrl;
			BeanXmlConfig beanXmlConfig = new BeanXmlConfig(fileBeanConfig);
			beanXmlConfig.parseConfig();
			int totalUrl = 0;
			this.baseUrl = baseUrl;
			for (Iterator<Url> iterator = urls.iterator(); iterator.hasNext();) {
				hdc.crawler.AbstractCrawler.Url url = (hdc.crawler.AbstractCrawler.Url) iterator
						.next();
				int page = url.fetchNumber;
				while (page > 0) {
					try {
						imageMap = null;
						System.out.println(new Date() + " Crawler Page Thu="
								+ page);
						int start = (page-1)*rows;
						
						HashMap<String, Url> urlMap = new HashMap<String, Url>();
						url.url = (new StringBuilder(String.valueOf(url.url_1)))
								.append(start).append(url.url_2).toString();
						System.out.println(new Date() + " Crawler Page URL="
								+ url.url);
						totalUrl += putLink(urlMap, url, baseUrl, rewriterUrl,
								type_collection_link);
						Thread.sleep(time_sleep);						
						processData(urlMap, type_collection_link);
						page--;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			Calendar calendar = Calendar.getInstance();
			String log = (new StringBuilder(String.valueOf(calendar.getTime()
					.toString()))).append("-->Tong Url:").append(totalUrl)
					.toString();
			System.out.println(log);
			FileLog.createFileLog((new StringBuilder(String
					.valueOf(JdbmXmlConfig.file_log))).append("_log_").append(
					calendar.get(5)).append(calendar.get(2)).append(
					calendar.get(1)).append(".txt").toString());
			FileLog.writer(log);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public void collectionDataNoPage() {
		try {
			UrlInjectXmlConfig urlInjectXmlConfig = new UrlInjectXmlConfig(
					fileUrlConfig);
			urlInjectXmlConfig.parseConfig();
			List<Url> urls = UrlInjectXmlConfig.urlConfigs;
			String baseUrl = UrlInjectXmlConfig.baseUrl;
			String rewriterUrl = UrlInjectXmlConfig.rewriterUrl;
			BeanXmlConfig beanXmlConfig = new BeanXmlConfig(fileBeanConfig);
			beanXmlConfig.parseConfig();
			this.baseUrl = baseUrl;
			int totalUrl = 0;
			for (Iterator<Url> iterator = urls.iterator(); iterator.hasNext();) {
				hdc.crawler.AbstractCrawler.Url url = (hdc.crawler.AbstractCrawler.Url) iterator
						.next();
				int page = url.fetchNumber;
				try {
					imageMap = null;
					System.out
							.println(new Date() + " Crawler Page Thu=" + page);

					HashMap<String, Url> urlMap = new HashMap<String, Url>();
					System.out.println(new Date() + " Crawler Page URL="
							+ url.url);
					totalUrl += putLink(urlMap, url, baseUrl, rewriterUrl,
							type_collection_link);
					Thread.sleep(time_sleep);		
					processData(urlMap, type_collection_link);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			Calendar calendar = Calendar.getInstance();
			String log = (new StringBuilder(String.valueOf(calendar.getTime()
					.toString()))).append("-->Tong Url:").append(totalUrl)
					.toString();
			System.out.println(log);
			FileLog.createFileLog((new StringBuilder(String
					.valueOf(JdbmXmlConfig.file_log))).append("_log_").append(
					calendar.get(5)).append(calendar.get(2)).append(
					calendar.get(1)).append(".txt").toString());
			FileLog.writer(log);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void collectionDataByDate(String date) {
		try {
			UrlInjectXmlConfig urlInjectXmlConfig = new UrlInjectXmlConfig(
					fileUrlConfig);
			urlInjectXmlConfig.parseConfig();
			List<Url> urls = UrlInjectXmlConfig.urlConfigs;
			String baseUrl = UrlInjectXmlConfig.baseUrl;
			String rewriterUrl = UrlInjectXmlConfig.rewriterUrl;
			int totalUrl = 0;
			this.baseUrl = baseUrl;
			BeanXmlConfig beanXmlConfig = new BeanXmlConfig(fileBeanConfig);
			beanXmlConfig.parseConfig();
			for (Iterator<Url> iterator = urls.iterator(); iterator.hasNext();) {
				hdc.crawler.AbstractCrawler.Url url = (hdc.crawler.AbstractCrawler.Url) iterator
						.next();

				HashMap<String, Url> urlMap = new HashMap<String, Url>();
				imageMap = null;
				try {
					url.url = (new StringBuilder(String.valueOf(url.url_1)))
							.append(date).append(url.url_2).toString();
					totalUrl += putLink(urlMap, url, baseUrl, rewriterUrl,
							type_collection_link);
					Thread.sleep(time_sleep);		
					processData(urlMap, type_collection_link);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			Calendar calendar = Calendar.getInstance();
			String log = (new StringBuilder(String.valueOf(calendar.getTime()
					.toString()))).append("-->Tong Url:").append(totalUrl)
					.toString();
			System.out.println(log);
			FileLog.createFileLog((new StringBuilder(String
					.valueOf(JdbmXmlConfig.file_log))).append("_log_").append(
					calendar.get(5)).append(calendar.get(2)).append(
					calendar.get(1)).append(".txt").toString());
			FileLog.writer(log);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int putLink(HashMap<String, Url> urlMap,
			hdc.crawler.AbstractCrawler.Url url, String baseUrl,
			String rewriterUrl, int type_collection_link) throws Exception {
		String html = "";
		HttpClientImpl client = new HttpClientImpl();
		org.apache.http.HttpResponse res = null;
		switch (type_get_html) {
		case 1:
			client = new HttpClientImpl();
			if(url.url.indexOf("http://")==-1) url.url = "http://"+url.url.trim();
			res = client.fetch(url.url.trim());
			html = HttpClientUtil.getResponseBody(res);
			break;
		case 2:
			html = HttpClientUtil.getHtml(url.url.trim());
			break;
		case 3:// Ajax Post
			DefaultHttpClient client2 = HttpClientFactory.getInstance();
			client2.getParams().setParameter(
					"application/x-www-form-urlencoded", true);
			HttpPost post = new HttpPost(url.url.trim());
			post.setEntity(new UrlEncodedFormEntity(listParameter));
			res = client2.execute(post);
			html = HttpClientUtil.getResponseBody(res);
			break;
		default:
			client = new HttpClientImpl();
			res = client.fetch(url.url.trim());
			html = HttpClientUtil.getResponseBody(res);
			break;
		}

		// System.out.println((new
		// StringBuilder("Crawler-->")).append(url.url).toString());
		int count = 0;
		switch (type_collection_link) {
		case 1:
			count = crawlerLink_1(urlMap, url, baseUrl, rewriterUrl, html);
			break;
		case 2:
			count = crawlerLink_2(urlMap, url, baseUrl, rewriterUrl, html);
			break;
		case 3:
			count = crawlerLink_3(urlMap, url, baseUrl, rewriterUrl, html);
			break;
		case 4:
			count = crawlerLink_4(urlMap, url, baseUrl, rewriterUrl, html);
			break;
		case 5:
			count = crawlerLink_5(urlMap, url, baseUrl, rewriterUrl, html);
			break;
		case 6:
			count = crawlerLink_6(urlMap, url, baseUrl, rewriterUrl, html);
			break;
		case 7:
			count = crawlerLink_7(urlMap, url, baseUrl, rewriterUrl, html);
			break;
		default:
			count = crawlerLink_1(urlMap, url, baseUrl, rewriterUrl, html);
			break;
		}
		
		return count;
	}

	private int crawlerLink_1(HashMap<String, Url> urlMap,
			hdc.crawler.AbstractCrawler.Url url, String baseUrl,
			String rewriterUrl, String html) {
		CrawlerLogDAO crawlerLogDAO = new CrawlerLogDAO();
		int i = 0;
		Calendar calendar = Calendar.getInstance();
		String day = calendar.get(5) >= 10 ? (new StringBuilder()).append(
				calendar.get(5)).toString() : (new StringBuilder("0")).append(
				calendar.get(5)).toString();
		int intmonth = calendar.get(2) + 1;
		String month = intmonth >= 10 ? (new StringBuilder()).append(intmonth)
				.toString() : (new StringBuilder("0")).append(intmonth)
				.toString();
		String year = (new StringBuilder(String.valueOf(calendar.get(1))))
				.toString();
		imageMap = new HashMap<String, ImageConfig>();
		try {
			Pattern comment = Pattern.compile("\\w'\\w");
			for (Matcher mcomment = comment.matcher(html); mcomment.find();)
				html = mcomment.replaceAll("");
			
			html = StringUtil.stripNonValidXMLCharacters(html);
			
			DocumentAnalyzer analyzer = new hdc.crawler.DocumentAnalyzer.DefaultDocumentAnayzer(
					baseUrl, rewriterUrl);
			List<A> list = null;
			try {
				list = analyzer.analyze(html, url.url.trim());
			} catch (Exception e) {
				list = null;
			}
			hdc.crawler.AbstractCrawler.Url injectUrl = null;
			String id = "";
			if (list != null) {
				for (Iterator<A> iterator = list.iterator(); iterator.hasNext();) {
					A a = (A) iterator.next();
					System.out.println(i+""+a.getUrl());
					id = (new UriID(new HttpURL(a.getURL()))).getIdAsString();
					if (!crawlerLogDAO.checkEntity(DatabaseConfig.table_entity_log, id)) {
					
						boolean kt = AbstractFilter.find(url.regex, UTF8Tool.coDau2KoDau(a.getURL()));
						System.out.println(kt);
						if (kt && !urlMap.containsKey(id)) {
							injectUrl = new hdc.crawler.AbstractCrawler.Url();
							injectUrl.id = id;
							injectUrl.cat_id = url.cat_id;
							injectUrl.url = a.getURL();

						//	System.out.println(a.getURL());

							if (!StringUtil.isEmpty(a.getImg())) {
								if (checkImage(a.getImg()))
									continue;
								ImageConfig imageConfig = new ImageConfig();
								imageConfig.id = id;
								imageConfig.src = a.getImg();
								Pattern r = Pattern.compile("http");
								Matcher m = r.matcher(imageConfig.src);
								if (!m.find())
									imageConfig.src = UrlInjectXmlConfig.rewriterUrl
											+ "/" + imageConfig.src;

								String image_name = "";
								image_name = a.getImg().substring(
										a.getImg().lastIndexOf("/") + 1);								
								r = Pattern.compile("\\s+");
								m = r.matcher(image_name);
								image_name = m.replaceAll("_");						
								String name = image_name.substring(0,image_name.lastIndexOf("."));
								String duoi = image_name.substring(image_name.lastIndexOf(".")+1,image_name.lastIndexOf(".")+4);
								    
								r = Pattern.compile("jpg|png|gif|bmp");
								m = r.matcher(duoi);
								if (!m.find())
									duoi = "jpg";

								image_name = (new StringBuilder(String
										.valueOf(name))).append(".")
										.append(duoi).toString();
								
								r = Pattern.compile("%20");
								m = r.matcher(image_name);
								image_name = m.replaceAll("_");
								
								imageConfig.name = (new StringBuilder("small_"))
										.append(System.currentTimeMillis())
										.append(image_name).toString();
								imageConfig.dateProcess = (new StringBuilder(
										String.valueOf(day))).append("/")
										.append(month).append("/").append(year)
										.toString();
								imageConfig.url_content = a.getURL();
								imageMap.put(imageConfig.id, imageConfig);
								urlMap.put(id, injectUrl);							
								System.out.println((new StringBuilder())
										.append(new Date()).append(
												"Inject Link i:").append(i)
										.append(a.getURL()).toString());
								i++;
							}

						}
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return i;
	}

	// Get Link By Tag A
	private int crawlerLink_2(HashMap<String, Url> urlMap,
			hdc.crawler.AbstractCrawler.Url url, String baseUrl,
			String rewriterUrl, String html) {
		CrawlerLogDAO crawlerLogDAO = new CrawlerLogDAO();
		int i = 0;
		Calendar calendar = Calendar.getInstance();
		String day = calendar.get(5) >= 10 ? (new StringBuilder()).append(
				calendar.get(5)).toString() : (new StringBuilder("0")).append(
				calendar.get(5)).toString();
		int intmonth = calendar.get(2) + 1;
		String month = intmonth >= 10 ? (new StringBuilder()).append(intmonth)
				.toString() : (new StringBuilder("0")).append(intmonth)
				.toString();
		String year = (new StringBuilder(String.valueOf(calendar.get(1))))
				.toString();
		imageMap = new HashMap<String, ImageConfig>();
		String id = "";
		try {
			Pattern comment = Pattern.compile("\\w'\\w");
			for (Matcher mcomment = comment.matcher(html); mcomment.find();)
				html = mcomment.replaceAll("");
			XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
			XPath xpath = XPathFactory.newInstance().newXPath();
			XPathExpression expr = xpath.compile("//A");
			NodeList nodes = (NodeList) expr.evaluate(reader.getDocument(),
					XPathConstants.NODESET);
			org.w3c.dom.Node nodeA = null;
			Url injectUrl = null;
			for (i = 0; i < nodes.getLength(); i++) {
				String imgSrc = "";
				String href = "";
				nodeA = nodes.item(i);
				if (nodeA.getAttributes().getNamedItem("href") != null)
					href = nodeA.getAttributes().getNamedItem("href")
							.getTextContent();
				// System.out.println(href);

				if (nodeA.hasChildNodes()) {
					NodeList list = nodeA.getChildNodes();
					int k = 0;
					while (k < list.getLength()) {
						org.w3c.dom.Node nodeImg = list.item(k);
						k++;
						if (nodeImg.getAttributes() != null) {
							if (nodeImg.getAttributes().getNamedItem("src") != null) {
								if (nodeA.getAttributes().getNamedItem("href") != null) {
									imgSrc = nodeImg.getAttributes()
											.getNamedItem("src")
											.getTextContent();
									href = nodeA.getAttributes().getNamedItem(
											"href").getTextContent();
									System.out.println(imgSrc);
									System.out.println(href);
								}
								break;
							}
						}
					}
				}

				if (!StringUtil.isEmpty(href))
					id = new UriID(new HttpURL(href)).getIdAsString();
				else
					continue;

				if (crawlerLogDAO.checkEntity(DatabaseConfig.table_entity_log, id))
					continue;

				boolean kt = AbstractFilter.find((String) url.regex, href);
				System.out.println(kt);
				if (kt && !StringUtil.isEmpty(href)
						&& !StringUtil.isEmpty(imgSrc)) {
					injectUrl = new hdc.crawler.AbstractCrawler.Url();
					injectUrl.id = id;
					injectUrl.cat_id = url.cat_id;
					Pattern r = Pattern.compile("http");
					Matcher m = comment.matcher(href);
					/*
					 * if(!m.find()){ System.out.println(m.find()); href
					 * =baseUrl+href; };
					 */
					injectUrl.url = href;

					if (checkImage(imgSrc))
						continue;

					urlMap.put(id, injectUrl);

					r = Pattern.compile("http");
					m = r.matcher(imgSrc);
					if (!m.find())
						imgSrc = UrlInjectXmlConfig.rewriterUrl + "/" + imgSrc;

					System.out.println(imgSrc);
					ImageConfig imageConfig = new ImageConfig();
					imageConfig.id = id;
					imageConfig.src = imgSrc;
					
					String image_name = "";
					image_name = imgSrc.substring(imgSrc.lastIndexOf("/") + 1);
			
					r = Pattern.compile("\\s+");
					m = r.matcher(image_name);
					image_name= m.replaceAll("_");
					String name = image_name.substring(0,image_name.lastIndexOf("."));
					String duoi = image_name.substring(image_name.lastIndexOf(".")+1,image_name.lastIndexOf(".")+4);
					
					r = Pattern.compile("jpg|png|gif|bmp");
					m = r.matcher(duoi);
					
					if (!m.find())
						duoi = "jpg";

					image_name = (new StringBuilder(String.valueOf(name)))
							.append(".").append(duoi).toString();
					
					r = Pattern.compile("%20");
					m = r.matcher(image_name);
					image_name = m.replaceAll("_");
					
					imageConfig.name = (new StringBuilder("small_")).append(
							System.currentTimeMillis()).append(image_name)
							.toString();
					imageConfig.dateProcess = (new StringBuilder(String
							.valueOf(day))).append("/").append(month).append(
							"/").append(year).toString();
					imageConfig.url_content = href;
					imageMap.put(imageConfig.id, imageConfig);
					System.out.println((new StringBuilder()).append(new Date())
							.append("Inject Link i:").append(i).append(href)
							.toString());

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	// Get Link By Tag a
	private int crawlerLink_4(HashMap<String, Url> urlMap,
			hdc.crawler.AbstractCrawler.Url url, String baseUrl,
			String rewriterUrl, String html) {
		CrawlerLogDAO crawlerLogDAO = new CrawlerLogDAO();
		int i = 0;
		Calendar calendar = Calendar.getInstance();
		String day = calendar.get(5) >= 10 ? (new StringBuilder()).append(
				calendar.get(5)).toString() : (new StringBuilder("0")).append(
				calendar.get(5)).toString();
		int intmonth = calendar.get(2) + 1;
		String month = intmonth >= 10 ? (new StringBuilder()).append(intmonth)
				.toString() : (new StringBuilder("0")).append(intmonth)
				.toString();
		String year = (new StringBuilder(String.valueOf(calendar.get(1))))
				.toString();
		imageMap = new HashMap<String, ImageConfig>();
		String id = "";
		try {
			Pattern comment = Pattern.compile("\\w'\\w");
			for (Matcher mcomment = comment.matcher(html); mcomment.find();)
				html = mcomment.replaceAll("");
			XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
			XPath xpath = XPathFactory.newInstance().newXPath();
			XPathExpression expr = xpath.compile("//a");
			NodeList nodes = (NodeList) expr.evaluate(reader.getDocument(),
					XPathConstants.NODESET);
			org.w3c.dom.Node nodeA = null;
			Url injectUrl = null;
			for (i = 0; i < nodes.getLength(); i++) {
				String imgSrc = "";
				String href = "";
				nodeA = nodes.item(i);
				if (nodeA.getAttributes().getNamedItem("href") != null)
					href = nodeA.getAttributes().getNamedItem("href")
							.getTextContent();
				// System.out.println(href);

				if (nodeA.hasChildNodes()) {
					NodeList list = nodeA.getChildNodes();
					int k = 0;
					while (k < list.getLength()) {
						org.w3c.dom.Node nodeImg = list.item(k);
						k++;
						if (nodeImg.getAttributes() != null) {
							if (nodeImg.getAttributes().getNamedItem("src") != null) {
								if (nodeA.getAttributes().getNamedItem("href") != null) {
									imgSrc = nodeImg.getAttributes()
											.getNamedItem("src")
											.getTextContent();
									href = nodeA.getAttributes().getNamedItem(
											"href").getTextContent();
									System.out.println(imgSrc);
									System.out.println(href);
								}
								break;
							}
						}
					}
				}

				if (!StringUtil.isEmpty(href))
					id = new UriID(new HttpURL(href)).getIdAsString();
				else
					continue;

				if (crawlerLogDAO.checkEntity(DatabaseConfig.table_entity_log, id))
					continue;

				boolean kt = AbstractFilter.find((String) url.regex, href);
				System.out.println(kt+"-"+imgSrc);
				if (kt && !StringUtil.isEmpty(imgSrc)) {
					injectUrl = new hdc.crawler.AbstractCrawler.Url();
					injectUrl.id = id;
					injectUrl.cat_id = url.cat_id;
					Pattern r = Pattern.compile("http");
					Matcher m = comment.matcher(href);
					/*
					 * if(!m.find()){ System.out.println(m.find()); href
					 * =baseUrl+href; };
					 */
					injectUrl.url = href;

					if (checkImage(imgSrc))
						continue;

					urlMap.put(id, injectUrl);

					/*r = Pattern.compile("http");
					m = r.matcher(imgSrc);
					if (!m.find())
						imgSrc = UrlInjectXmlConfig.rewriterUrl + "/" + imgSrc;*/

					System.out.println(imgSrc);
					ImageConfig imageConfig = new ImageConfig();
					imageConfig.id = id;
					imageConfig.src = imgSrc;
					String image_name = "";
					image_name = imgSrc.substring(imgSrc.lastIndexOf("/") + 1);
					
					r = Pattern.compile("\\s+");
					m = r.matcher(image_name);
					image_name = m.replaceAll("_");
					String name = "";
					String duoi = "jpg"; 
					if(!(image_name.indexOf(".")==-1))
					{
					 name = image_name.substring(0,image_name.lastIndexOf("."));
					 duoi = image_name.substring(image_name.lastIndexOf(".")+1,image_name.lastIndexOf(".")+4);
					}else
					{
						name = Calendar.getInstance().getTimeInMillis()+"";
						duoi = "jpg"; 
					}
					r = Pattern.compile("jpg|png|gif|bmp");
					m = r.matcher(duoi);
					if (!m.find())
						duoi = "jpg";

					image_name = (new StringBuilder(String.valueOf(name)))
							.append(".").append(duoi).toString();
					
					r = Pattern.compile("%20");
					m = r.matcher(image_name);
					image_name = m.replaceAll("_");
					
					imageConfig.name = (new StringBuilder("small_")).append(
							System.currentTimeMillis()).append(image_name)
							.toString();
					imageConfig.dateProcess = (new StringBuilder(String
							.valueOf(day))).append("/").append(month).append(
							"/").append(year).toString();
					imageConfig.url_content = href;
					imageMap.put(imageConfig.id, imageConfig);
					System.out.println((new StringBuilder()).append(new Date())
							.append("Inject Link i:").append(i).append(href)
							.toString());

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	// Don't Get Image avatar
	private int crawlerLink_3(HashMap<String, Url> urlMap,
			hdc.crawler.AbstractCrawler.Url url, String baseUrl,
			String rewriterUrl, String html) {
		CrawlerLogDAO crawlerLogDAO = new CrawlerLogDAO();
		int i = 0;
		imageMap = new HashMap<String, ImageConfig>();
		try {
			Pattern comment = Pattern.compile("\\w'\\w");
			for (Matcher mcomment = comment.matcher(html); mcomment.find();)
				html = mcomment.replaceAll("");

			DocumentAnalyzer analyzer = new hdc.crawler.DocumentAnalyzer.DefaultDocumentAnayzer(
					baseUrl, rewriterUrl);
			List<A> list = null;
			try {
				list = analyzer.analyze(html, url.url.trim());
			} catch (Exception e) {
				list = null;
			}
			hdc.crawler.AbstractCrawler.Url injectUrl = null;
			String id = "";
			if (list != null) {
				for (Iterator<A> iterator = list.iterator(); iterator.hasNext();) {
					A a = (A) iterator.next();
					System.out.println(a.getUrl());
					id = (new UriID(new HttpURL(a.getURL()))).getIdAsString();
					if (!crawlerLogDAO.checkEntity(DatabaseConfig.table_entity_log, id)) {
						boolean kt = AbstractFilter.find(url.regex, a.getURL());
						System.out.println(kt);
						if (kt && !urlMap.containsKey(id)) {
							injectUrl = new hdc.crawler.AbstractCrawler.Url();
							injectUrl.id = id;
							injectUrl.cat_id = url.cat_id;
							injectUrl.url =a.getURL().trim();

							System.out.println(a.getURL());
							urlMap.put(id, injectUrl);
							System.out.println((new StringBuilder()).append(
									new Date()).append("Inject Link i:")
									.append(i).append(a.getURL()).toString());
							i++;

						}
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return i;
	}

	// Get Link Form Json Vsolutions
	private int crawlerLink_5(HashMap<String, Url> urlMap,
			hdc.crawler.AbstractCrawler.Url url, String baseUrl,
			String rewriterUrl, String html) {
		CrawlerLogDAO crawlerLogDAO = new CrawlerLogDAO();
		int i = 0;
		Calendar calendar = Calendar.getInstance();
		String day = calendar.get(5) >= 10 ? (new StringBuilder()).append(
				calendar.get(5)).toString() : (new StringBuilder("0")).append(
				calendar.get(5)).toString();
		int intmonth = calendar.get(2) + 1;
		String month = intmonth >= 10 ? (new StringBuilder()).append(intmonth)
				.toString() : (new StringBuilder("0")).append(intmonth)
				.toString();
		String year = (new StringBuilder(String.valueOf(calendar.get(1))))
				.toString();
		imageMap = new HashMap<String, ImageConfig>();
		String id = "";

		Pattern pattern = Pattern.compile("\\s{2}");
		Matcher m = pattern.matcher(html);
		html = m.replaceAll(" ");
		pattern = Pattern.compile("getSearchData.*\"docs\":");
		m = pattern.matcher(html);

		if (m.find()) {
			html = m.replaceAll("");
			pattern = Pattern.compile("},\\s+\"highlighting\":.*");
			m = pattern.matcher(html);
			html = m.replaceAll("");
			Object obj = JSONValue.parse(html);
			org.json.simple.JSONArray array = (org.json.simple.JSONArray) obj;
			Url injectUrl = null;
			i = 0;
			String href = "", title = "", lead = "", imgSrc = "";
			while (i < array.size()) {
				org.json.simple.JSONObject obj2 = (org.json.simple.JSONObject) array
						.get(i);

				href = (String) obj2.get("url");
				if (!StringUtil.isEmpty(href))
					id = new UriID(new HttpURL(href)).getIdAsString();
				else
				{	
					i++;
					continue;
				}
				if (crawlerLogDAO.checkEntity(DatabaseConfig.table_entity_log, id))
				{
				 i++;	continue;
				}

				System.out.println(href);
				title = (String) obj2.get("title");
				System.out.println(title);
				lead = (String) obj2.get("lead");
				System.out.println(lead);
				imgSrc = (String) obj2.get("avatar");
				System.out.println(imgSrc);

				if (!StringUtil.isEmpty(imgSrc)) {
					injectUrl = new hdc.crawler.AbstractCrawler.Url();
					injectUrl.id = id;
					injectUrl.cat_id = url.cat_id;
					injectUrl.title = title;
					injectUrl.lead = lead;
					injectUrl.avatar = imgSrc;

					Pattern r = Pattern.compile("(\\d+-\\d+-\\d+)");
					m = r.matcher((String) obj2.get("date"));
					if (m.find()) {
						injectUrl.create_date = m.group(1);
					}

					r = Pattern.compile("http");
					m = r.matcher(href);
					if (!m.find()) {
						href = baseUrl + href;
					}

					injectUrl.url = href;

					if (checkImage(imgSrc))
					{
						i++;continue;
					}

					urlMap.put(id, injectUrl);

					r = Pattern.compile("http");
					m = r.matcher(imgSrc);
					if (!m.find())
						imgSrc = UrlInjectXmlConfig.rewriterUrl + "/" + imgSrc;

					System.out.println(imgSrc);
					ImageConfig imageConfig = new ImageConfig();
					imageConfig.id = id;
					imageConfig.src = imgSrc;
					String image_name = "";
					image_name = imgSrc.substring(imgSrc.lastIndexOf("/") + 1);
					
					r = Pattern.compile("\\s+");
					m = r.matcher(image_name);
					image_name = m.replaceAll("_");
					String name ="";String duoi="";
					if(image_name.lastIndexOf(".")>0)
					{
					name = image_name.substring(0,image_name.lastIndexOf("."));
					duoi = image_name.substring(image_name.lastIndexOf(".")+1,image_name.lastIndexOf(".")+4);
					}
					r = Pattern.compile("jpg|png|gif|bmp");
					m = r.matcher(duoi);
					if (!m.find())
						duoi = "jpg";

					image_name = (new StringBuilder(String.valueOf(name)))
							.append(".").append(duoi).toString();
					
					r = Pattern.compile("%20");
					m = r.matcher(image_name);
					image_name = m.replaceAll("_");
					
					imageConfig.name = (new StringBuilder("small_")).append(
							System.currentTimeMillis()).append(image_name)
							.toString();
					imageConfig.dateProcess = (new StringBuilder(String
							.valueOf(day))).append("/").append(month).append(
							"/").append(year).toString();
					imageConfig.url_content = href;
					imageMap.put(imageConfig.id, imageConfig);
					System.out.println((new StringBuilder()).append(new Date())
							.append("Inject Link i:").append(i).append(href)
							.toString());

				}
				i++;
			}

		}

		return i;
	}

	private int crawlerLink_6(HashMap<String, Url> urlMap,
			hdc.crawler.AbstractCrawler.Url url, String baseUrl,
			String rewriterUrl, String html) {
		CrawlerLogDAO crawlerLogDAO = new CrawlerLogDAO();
		int i = 0;
		Calendar calendar = Calendar.getInstance();
		String day = calendar.get(5) >= 10 ? (new StringBuilder()).append(
				calendar.get(5)).toString() : (new StringBuilder("0")).append(
				calendar.get(5)).toString();
		int intmonth = calendar.get(2) + 1;
		String month = intmonth >= 10 ? (new StringBuilder()).append(intmonth)
				.toString() : (new StringBuilder("0")).append(intmonth)
				.toString();
		String year = (new StringBuilder(String.valueOf(calendar.get(1))))
				.toString();
		imageMap = new HashMap<String, ImageConfig>();
		try {
			Pattern comment = Pattern.compile("\\w'\\w");
			for (Matcher mcomment = comment.matcher(html); mcomment.find();)
				html = mcomment.replaceAll("");
			
			html = StringUtil.stripNonValidXMLCharacters(html);
			
			DocumentAnalyzer analyzer = new hdc.crawler.DocumentAnalyzer.DefaultDocumentAnayzer(
					baseUrl, rewriterUrl);
			List<A> list = null;
			try {
				list = analyzer.analyze(html, url.url.trim());
			} catch (Exception e) {
				list = null;
			}
			hdc.crawler.AbstractCrawler.Url injectUrl = null;
			String id = "";
			if (list != null) {
				for (Iterator<A> iterator = list.iterator(); iterator.hasNext();) {
					A a = (A) iterator.next();
					System.out.println(i+""+a.getUrl());
					id = (new UriID(new HttpURL(a.getURL()))).getIdAsString();
					if (!crawlerLogDAO.checkEntity(DatabaseConfig.table_entity_log, id)) {
					
						boolean kt = AbstractFilter.find(url.regex, UTF8Tool.coDau2KoDau(a.getURL()));
						System.out.println(kt);
						if (kt && !urlMap.containsKey(id)) {
							injectUrl = new hdc.crawler.AbstractCrawler.Url();
							injectUrl.id = id;
							injectUrl.cat_id = url.cat_id;
							injectUrl.url = a.getURL();

						//	System.out.println(a.getURL());

							if (!StringUtil.isEmpty(a.getImg())) {
								if (checkImage(a.getImg()))
									continue;
								ImageConfig imageConfig = new ImageConfig();
								imageConfig.id = id;
								imageConfig.src = a.getImg();
								Pattern r = Pattern.compile("http");
								Matcher m = r.matcher(imageConfig.src);
								if (!m.find())
									imageConfig.src = UrlInjectXmlConfig.rewriterUrl
											+ "/" + imageConfig.src;

								String image_name = "";
								image_name = a.getImg().substring(
										a.getImg().lastIndexOf("/") + 1);								
								r = Pattern.compile("\\s+");
								m = r.matcher(image_name);
								image_name = m.replaceAll("_");						
								String name = image_name.substring(0,image_name.lastIndexOf("."));
								String duoi = image_name.substring(image_name.lastIndexOf(".")+1,image_name.lastIndexOf(".")+4);
								    
								r = Pattern.compile("jpg|png|gif|bmp");
								m = r.matcher(duoi);
								if (!m.find())
									duoi = "jpg";

								image_name = name;
								
								r = Pattern.compile("%20");
								m = r.matcher(image_name);
								image_name = m.replaceAll("_");
								imageConfig.name = image_name + "_"+System.currentTimeMillis()+"_small."+duoi;
								imageConfig.name_n = image_name + "_"+System.currentTimeMillis()+"."+duoi;
								
								/*imageConfig.name = (new StringBuilder(image_name))										
										.append("_"+System.currentTimeMillis()).append("_small.")
										.append(duoi).toString();*/
								
								imageConfig.dateProcess = (new StringBuilder(
										String.valueOf(day))).append("/")
										.append(month).append("/").append(year)
										.toString();
								imageConfig.url_content = a.getURL();
								imageMap.put(imageConfig.id, imageConfig);
								urlMap.put(id, injectUrl);							
								System.out.println((new StringBuilder())
										.append(new Date()).append(
												"Inject Link i:").append(i)
										.append(a.getURL()).toString());
								i++;
							}

						}
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return i;
	}
	
	
	// Get Link By Tag a don't get image
	private int crawlerLink_7(HashMap<String, Url> urlMap,
			hdc.crawler.AbstractCrawler.Url url, String baseUrl,
			String rewriterUrl, String html) {
		CrawlerLogDAO crawlerLogDAO = new CrawlerLogDAO();
		int i = 0;
		String id = "";
		try {
			Pattern comment = Pattern.compile("\\w'\\w");
			for (Matcher mcomment = comment.matcher(html); mcomment.find();)
				html = mcomment.replaceAll("");
			XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
			XPath xpath = XPathFactory.newInstance().newXPath();
			XPathExpression expr = xpath.compile("//a");
			NodeList nodes = (NodeList) expr.evaluate(reader.getDocument(),
					XPathConstants.NODESET);
			org.w3c.dom.Node nodeA = null;
			Url injectUrl = null;
			for (i = 0; i < nodes.getLength(); i++) {				
				String href = "";
				nodeA = nodes.item(i);
				if (nodeA.getAttributes().getNamedItem("href") != null)
					href = nodeA.getAttributes().getNamedItem("href").getTextContent();
				if (crawlerLogDAO.checkEntity(DatabaseConfig.table_entity_log, id))
					continue;

				boolean kt = AbstractFilter.find((String) url.regex, href);
				System.out.println(kt);
				System.out.println(href);
				if (kt && !StringUtil.isEmpty(href)) {
					injectUrl = new hdc.crawler.AbstractCrawler.Url();
					injectUrl.id = id;
					injectUrl.cat_id = url.cat_id;
					injectUrl.url = href;
					urlMap.put(id, injectUrl);

					
					System.out.println((new StringBuilder()).append(new Date())
							.append("Inject Link i:").append(i).append(href)
							.toString());

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
	
	public void openConnection() {
		try {
			Class.forName(DatabaseConfig.driver.trim());
			System.out.println(DatabaseConfig.url.trim());
			conn = DriverManager.getConnection(DatabaseConfig.url.trim(),
					DatabaseConfig.user, DatabaseConfig.password);
			connLog = ConnectionDataLog.connection(fileBeanConfig);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void closeConnection() {
		try {
			conn.close();
			connLog.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void processData(HashMap<String, Url> urlMap, int type_get_html) {
		Iterator<Url> urls = urlMap.values().iterator();
		hdc.crawler.AbstractCrawler.Url urlFetch = null;
		HttpClientImpl client = new HttpClientImpl();
		org.apache.http.HttpResponse res = null;
		String html = "";
		try {
			while (urls.hasNext()) {
				urlFetch = (hdc.crawler.AbstractCrawler.Url) urls.next();
				// if (type_get_html == 1) {
				//Pattern pattern = Pattern.compile("[^\\.\\w:/-\\?&=]");
				Pattern pattern = Pattern.compile("\\s+");
				Matcher m = pattern.matcher(urlFetch.url);				
				urlFetch.url = m.replaceAll("%20");
				System.out.println("Extract url=" + urlFetch.url);
				res = client.fetch(urlFetch.url);
				html = HttpClientUtil.getResponseBody(res);
				/*
				 * } else { html = HttpClientUtil.getHtml(urlFetch.url); }
				 */
				
				selectImgArticleVisitor.imageList = new ArrayList<ImageConfig>();
				ExtractEntity entity = extractHTML(urlFetch, html);				
				saveData(entity);				
				selectImgArticleVisitor.imageList = null;
				Thread.sleep(time_sleep);
			}
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private ExtractEntity extractHTML(hdc.crawler.AbstractCrawler.Url url,
			String html) {
		ExtractEntity entity = new ExtractEntity();
		//html = StringUtil.stripNonValidXMLCharacters(html);
		try {
			UrlInjectXmlConfig urlInjectXmlConfig = new UrlInjectXmlConfig(
					fileUrlConfig);
			urlInjectXmlConfig.parseConfig();
			BeanXmlConfig beanXmlConfig = new BeanXmlConfig(fileBeanConfig);
			beanXmlConfig.parseConfig();
			List<Property> propeties = beanXmlConfig.bean.getProperties();
			XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
			entity.setID(url.id);
			entity.setCat_id(url.cat_id);
			entity.setUrl(url.url);

			Calendar calendar = Calendar.getInstance();
			String day = calendar.get(5) >= 10 ? (new StringBuilder()).append(
					calendar.get(5)).toString() : (new StringBuilder("0"))
					.append(calendar.get(5)).toString();
			int intmonth = calendar.get(2) + 1;
			String month = intmonth >= 10 ? (new StringBuilder()).append(
					intmonth).toString() : (new StringBuilder("0")).append(
					intmonth).toString();
			entity.date = (new StringBuilder(String.valueOf(day))).append("/")
					.append(month).append("/").append(calendar.get(1))
					.toString();
			this.reader = reader;
			processProperty(propeties, entity);

			if (!StringUtil.isEmpty((String) entity.getProperty("create_date")))
				try {
					Pattern r = Pattern.compile("(\\d+/\\d+/\\d+)");
					Matcher m = r.matcher((String) entity
							.getProperty("create_date"));
					if (m.find())
						entity.date = (new StringBuilder(String.valueOf(m
								.group(1).split("/")[0]))).append("/").append(
								m.group(1).split("/")[1]).append("/").append(
								m.group(1).split("/")[2]).toString();
				} catch (Exception e) {
					e.printStackTrace();
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entity;
	}

	protected boolean processProperty(List<Property> propeties, ExtractEntity entity) {
		String str = "";
		boolean filter = false;
		try {
			if (propeties != null) {
				NodeList nodes = null;
				DomWriter writer = new DomWriter();

				for (Property property : propeties) {
					if (property.getNode_type().equalsIgnoreCase("nodeset")) {
						// Phai Xoa Node O Cuoi Cua Process
						List<hdc.crawler.Node> nodeDelByXpath = property
								.getNodedelByXpaths();
						if (nodeDelByXpath != null) {
							for (hdc.crawler.Node node2 : nodeDelByXpath) {
								System.out.println(node2.xpath);
								CrawlerUtil.removeNodeByXpath(reader,
										node2.xpath);
							}
						}

						if (StringUtil.isEmpty(property.getXpath_sub())) {
							String xpath[] = property.getXpath().split(";");
							for (String string : xpath) {
								if (!StringUtil.isEmpty(string)) {
									nodes = (NodeList) reader.read(string,
											XPathConstants.NODESET);
									if (nodes.item(0) != null)
										break;
								}
							}
							if (nodes == null)
								continue;
							this.nodesDel = property.getNodedels();
							if (!property.getName().equalsIgnoreCase("title")) {
								deleteVisitor.traverse(nodes.item(0));
								normalVisitor.traverse(nodes.item(0));
							}
							if ("1".equalsIgnoreCase(property.getChangeLink())) {
								String title = HtmlUtil.toText((String) entity
										.getProperty("title"));
								String pattern = "<.*xml.*>";
								Pattern r = Pattern.compile(pattern);
								Matcher m = r.matcher(title);
								title = m.replaceAll("");

								if (!StringUtil.isEmpty((String) entity
										.getProperty("create_date"))) {
									r = Pattern.compile("(\\d+/\\d+/\\d+)");
									m = r.matcher((String) entity
											.getProperty("create_date"));
									if (m.find())
										selectImgArticleVisitor.date = (new StringBuilder(
												String.valueOf(m.group(1)
														.split("/")[0])))
												.append("/")
												.append(
														m.group(1).split("/")[1])
												.append("/")
												.append(
														m.group(1).split("/")[2])
												.toString();
								}
								selectImgArticleVisitor.urlDomain = UrlInjectXmlConfig.rewriterUrl;
								selectImgArticleVisitor.title = title;
								selectImgArticleVisitor.traverse(nodes.item(0));

							}
							str = writer.toXMLString(nodes.item(0));
							if ("1".equalsIgnoreCase(property.getFilter()))
								filter = ContentFilter.filter(str);
							if (filter)
								return true;
							if("utf-8".equalsIgnoreCase(this.charset))
								entity.addProperty(property.getName(), str);
							else
							{
								byte[] utf8 = new String(str.getBytes(), "ISO-8859-1").getBytes("UTF-8");
								entity.addProperty(property.getName(),new String(utf8));
							}
							// System.out.println(new Date()+" Process
							// Property:"+property.getName()+"="+ str);
						} else {
							nodes = (NodeList) reader.read(property.getXpath(),
									XPathConstants.NODESET);
							int node_one_many = nodes.getLength();
							int j = 1;
							str = "";
							while (j <= node_one_many) {
								String result = (String) reader.read(property
										.getXpath()
										+ "["
										+ j
										+ "]"
										+ property.getXpath_sub(),
										XPathConstants.STRING);
								str += result.trim() + ",";
								j++;
							}
							if("utf-8".equalsIgnoreCase(this.charset))
								entity.addProperty(property.getName(), str);
							else
							{
								byte[] utf8 = new String(str.getBytes(), "ISO-8859-1").getBytes("UTF-8");
								entity.addProperty(property.getName(),new String(utf8));
							}
							System.out.println(new Date()
									+ " Process Property:" + property.getName()
									+ "=" + str);
						}
					} else {
						if (property.getNode_type().equalsIgnoreCase("string"))
							str = (String) reader.read(property.getXpath(),
									XPathConstants.STRING);
						if ("1".equalsIgnoreCase(property.getFilter()))
							filter = ContentFilter.filter(str);
						if (filter)
							return true;

						if ("1".equalsIgnoreCase(property.getChangeLink()))
							str = ContentFilter.changeLink(str);

						if("utf-8".equalsIgnoreCase(this.charset))
							entity.addProperty(property.getName(), str);
						else
						{
							byte[] utf8 = new String(str.getBytes(), "ISO-8859-1").getBytes("UTF-8");
							entity.addProperty(property.getName(),new String(utf8));
						}
						System.out.println(new Date() + " Process Property:"
								+ property.getName() + "=" + str);
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filter;
	}

	protected void saveData(ExtractEntity entity) throws Exception {
		CrawlerLogDAO crawlerLogDAO;
		ArticleImporter articleImporter;
		crawlerLogDAO = new CrawlerLogDAO();
		articleImporter = new ArticleImporter("", "");
		HashMap<Integer, Integer> cats = new HashMap<Integer, Integer>();
		Article article = new Article();
		String content = "";
		String picture = "";
		ImageConfig imageConfigAvatar = null;

		getCategoriesParent(entity.getCat_id(), cats);

		article.title = HtmlUtil.toText((String) entity.getProperty("title"));
		article.title  = StringUtil.getTextFromNode(article.title);
		article.content  = StringUtil.getTextFromNode((String) entity.getProperty("content"));
		
		if (StringUtil.isEmpty(article.title) || StringUtil.isEmpty(content))
			return;

		if (imageMap != null && imageMap.containsKey(entity.getID())) {
			imageConfigAvatar = (ImageConfig) imageMap.get(entity.getID());
			picture = imageConfigAvatar.name.substring(6,
					imageConfigAvatar.name.length());
		} else {
			if (type_get_anh == 0)
				return;
		}

		try {
			int cat_root_id = ((Integer) cats.get(Integer.valueOf(1)))
					.intValue();
			boolean loged = crawlerLogDAO
					.saveEntityCheck(DatabaseConfig.table_entity_log,
							"", 0, cat_root_id, entity);
			if (loged) {
				article.introtext = (String) entity.getProperty("introtext");
				if (StringUtil.isEmpty(article.introtext)) {
					String tomtat = HtmlUtil.removeTag(content).trim();					
					if(tomtat.length()>510)tomtat = tomtat.substring(0, 510);					 
					if (tomtat.lastIndexOf(".") > 0)
						article.introtext = tomtat.substring(0, tomtat
								.lastIndexOf(".") + 1);
				}else
				{
					article.introtext = HtmlUtil.removeTag(article.introtext);
					if(article.introtext.length()>512)
					{
						article.introtext = article.introtext.substring(0,510);
					}
				}
				if(article.introtext==null)article.introtext=" ";
				article.tags = (String) entity.getProperty("tags");
				Pattern pattern = Pattern.compile("\\W+");
				Pattern pattern2 = Pattern.compile("-$");
				String alias = UTF8Tool.coDau2KoDau(article.title).trim();
				Matcher m = pattern.matcher(alias);
				String url_rewrite = m.replaceAll("-");
				m = pattern2.matcher(url_rewrite);
				url_rewrite = m.replaceAll("");
				article.alias = url_rewrite;
				article.content = content;
				article.picture = picture;
				article.author = (String) entity.getProperty("author");
				article.status = "active";
				article.is_image = 0;
				article.news_album = 0;
				if (entity.getProperty("numberimage") != null) {
					int numberimage = ((Integer) entity
							.getProperty("numberimage")).intValue();
					if (numberimage >= 7)
						article.is_image = 1;
				}
				article.news_hot = 0;
				article.news_focus = 0;
				article.has_seo = 0;
				article.tags = (String) entity.getProperty("tags");
				article.original_link = entity.getUrl();
				article.category_id = cat_root_id;
				if (cats.get(Integer.valueOf(2)) != null)
					article.subcategory_id = ((Integer) cats.get(Integer
							.valueOf(2))).intValue();
				else
					article.subcategory_id = 0;
				article.source = source;
				if (StringUtil.isEmpty(article.author))
					article.author = "Auto";
				article.city_alias = this.provice_alias;
				article.city_id = this.provice_id;
				Calendar calendar = Calendar.getInstance();
				article.create_date = calendar.getTimeInMillis() / 1000L;
				article.edit_date = calendar.getTimeInMillis() / 1000L;
				if (!StringUtil.isEmpty(entity.date)) {
					switch (type_date) {
					case 0: //dd/mm/yyyy
						String dates[] = entity.date.split("/");
						int intmonth = Integer.parseInt(dates[1]);
						int intday = Integer.parseInt(dates[0]);
						int year = Integer.parseInt(dates[2]);
						calendar.set(year, intmonth - 1, intday);	
						break;
					case 1: //yyyy/mm/dd
						dates = entity.date.split("/");
						intday = Integer.parseInt(dates[2]);
						intmonth = Integer.parseInt(dates[1]);						
						year = Integer.parseInt(dates[0]);
						calendar.set(year, intmonth - 1, intday);	
						break;
					case 2: //mm/dd/yyyy
						dates = entity.date.split("/");
						intday = Integer.parseInt(dates[1]);
						intmonth = Integer.parseInt(dates[0]);						
						year = Integer.parseInt(dates[2]);
						calendar.set(year, intmonth - 1, intday);	
						break;
					default:
						dates = entity.date.split("/");
						intmonth = Integer.parseInt(dates[1]);
						intday = Integer.parseInt(dates[0]);
						year = Integer.parseInt(dates[2]);
						calendar.set(year, intmonth - 1, intday);	
						break;
					}
				
					
				}
				article.publish_date = calendar.getTimeInMillis() / 1000L;
				
				DownloadImage downloadImage = null;
				if (selectImgArticleVisitor.imageList.size() > 0&&this.type_get_anh==1) {
					for (int i = 0; i < selectImgArticleVisitor.imageList
							.size(); i++) {
						ImageConfig imageConfig = (ImageConfig) selectImgArticleVisitor.imageList
								.get(i);
						// Get Small Image Null
						if (imageConfigAvatar == null && i == 0) {
							downloadImage = new DownloadImage(
									imageConfig.src, "small_"+imageConfig.name,
									UrlInjectXmlConfig.baseUrl, "", 1,
									this.type_download,
									selectImgArticleVisitor.imageList);
							downloadImage.run();
							article.picture = imageConfig.name;
						}

					}

				}
				
				int id = articleImporter.saveNews(cat_root_id, article);
				article.id = id;
				if (id > 0) {
					articleImporter.saveNewsShort( cat_root_id, article);
					articleImporter.saveNewsShortLastest( cat_root_id,
							article);					
					if (imageConfigAvatar != null) {
						downloadImage = new DownloadImage(
								imageConfigAvatar.src, imageConfigAvatar.name,
								UrlInjectXmlConfig.baseUrl, "", 1,
								this.type_download,
								selectImgArticleVisitor.imageList);
						downloadImage.run();
					}
					if (selectImgArticleVisitor.imageList.size() > 0) {
						for (int i = 0; i < selectImgArticleVisitor.imageList
								.size(); i++) {
							ImageConfig imageConfig = (ImageConfig) selectImgArticleVisitor.imageList
									.get(i);
							downloadImage = new DownloadImage(imageConfig.src,
									imageConfig.name,
									UrlInjectXmlConfig.baseUrl,
									imageConfig.dateProcess, 0,
									this.type_download, null);
							downloadImage.run();
							articleImporter.saveImage(id,article.category_id,
									downloadImage.destinationFile);

						}

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}

	protected Map<Integer, Integer> getCategoriesParent( int cat,
			Map<Integer, Integer> holder) throws Exception {
		int cat_parent = 0;
		Connection conn = C3p0TinhHinhPool.getConnection();
		ResultSet rs = conn.createStatement().executeQuery(
				(new StringBuilder(
						"SELECT parent_id FROM tbl_categories WHERE id = "))
						.append(cat).toString());
		if (rs.next())
			cat_parent = rs.getInt(1);
		if (cat_parent == 0) {
			holder.put(Integer.valueOf(1), Integer.valueOf(cat));
			C3p0TinhHinhPool.attemptClose(conn);
			return holder;
		} else {
			holder.put(Integer.valueOf(2), Integer.valueOf(cat));
			getCategoriesParent( cat_parent, holder);
			C3p0TinhHinhPool.attemptClose(conn);
			return holder;
		}
		
	}

	protected NodeDeleteVisitor deleteVisitor = new NodeDeleteVisitor() {
		protected boolean shouldDelete(org.w3c.dom.Node node) {
			if (node.getNodeName().equalsIgnoreCase("meta"))
				return true;
			else if (node.getNodeName().equalsIgnoreCase("link"))
				return true;
			else if (node.getNodeName().equalsIgnoreCase("style"))
				return true;
			else if (node.getNodeName().equalsIgnoreCase("script"))
				return true;
			else if (node.getNodeName().equalsIgnoreCase("iframe"))
				return true;
			else if (node.getNodeName().equalsIgnoreCase("xml"))
				return true;
			if (nodesDel != null) {
				int i = 0;
				while (i < nodesDel.size()) {
					if (nodesDel.get(i).name.equalsIgnoreCase(node
							.getNodeName())) {
						int j = 0;
						while (j < node.getAttributes().getLength()) {
							if (node.getAttributes().item(j).getTextContent()
									.equalsIgnoreCase(nodesDel.get(i).attribue))
								return true;

							j++;
						}
					}
					i++;
				}
			}
			return false;
		}
	};

	protected NodeNormalizeVisitor normalVisitor = new NodeNormalizeVisitor() {
		protected void normalize(org.w3c.dom.Node node) {
			if (node != null && node.hasAttributes()) {
				NamedNodeMap attributes = node.getAttributes();
				if (attributes.getNamedItem("class") != null)
					attributes.removeNamedItem("class");
				if (attributes.getNamedItem("id") != null
						&& !"object".equalsIgnoreCase(node.getNodeName()))
					attributes.removeNamedItem("id");
				if (attributes.getNamedItem("style") != null)
					attributes.removeNamedItem("style");
				if (attributes.getNamedItem("height") != null
						&& (!"object".equalsIgnoreCase(node.getNodeName()) || !"embed"
								.equalsIgnoreCase(node.getNodeName())))
					attributes.removeNamedItem("height");
				if (attributes.getNamedItem("width") != null
						&& (!"object".equalsIgnoreCase(node.getNodeName()) || !"embed"
								.equalsIgnoreCase(node.getNodeName())))
					attributes.removeNamedItem("width");
				if (attributes.getNamedItem("onclick") != null)
					attributes.removeNamedItem("onclick");
				if (attributes.getNamedItem("title") != null)
					attributes.removeNamedItem("title");
				if (attributes.getNamedItem("rel") != null)
					attributes.removeNamedItem("rel");
				if (attributes.getNamedItem("target") != null)
					attributes.removeNamedItem("target");
				String tag = node.getNodeName();
				if (node instanceof Element && "a".equalsIgnoreCase(tag)) {
					/*org.w3c.dom.Node nodeText = reader.getDocument().createTextNode(node.getTextContent());
					node.getParentNode().replaceChild(nodeText, node);*/
					Element a = (Element) node;
					a.setAttribute("rel", "nofollow");
					a.setAttribute("target", "_blank");
					String href = a.getAttribute("href");
					Pattern r = Pattern.compile("http");
					Matcher m = r.matcher(href);
					if(!m.find()){  
						href=baseUrl+href;
						a.setAttribute("href", href);
					}
					
				}
				if (node instanceof Element && "font".equalsIgnoreCase(tag)) {
					Element font = (Element) node;
					font.removeAttribute("size");
					font.removeAttribute("color");
				}
			}

		}
	};
	
	private boolean checkImage(String img)
	{
		boolean kq = false;
		if (img.indexOf("/li.png") > 0
				|| img.indexOf("/ico_1.gif") > 0
				|| img.indexOf("/ico_2.gif") > 0
				|| img.indexOf("/icon_0.gif") > 0
				|| img.indexOf("/hasVideo.gif") > 0
				|| img.indexOf("/hasImage.gif") > 0
				|| img.indexOf("/video_icon.gif") > 0
				|| img.indexOf("photo_icon.gif") > 0|| img.indexOf("Doti.png") > 0
				|| img.indexOf("icon_bullet.gif") > 0||img.indexOf("0Icon_1.jpg") > 0
				|| img.indexOf("icon_2.gif")>=0
				|| img.indexOf("Next.gif")>=0)
		{
			kq=true;
		}
		return kq;
	}
	
	public static void main(String args[]) {
		 CrawlerArticleByPage crawlerArticleByPage = new CrawlerArticleByPage();
		 String urls = args[0];
		 String bean = args[1];
		 int fetch = Integer.parseInt(args[2]);
		 int source = Integer.parseInt(args[3]);
		 int download = Integer.parseInt(args[4]);
		 int province_id = Integer.parseInt(args[5]);
		 String province = args[6];
		 int type_image = Integer.parseInt(args[7]);
		 int type_get_html = Integer.parseInt(args[8]);
		 int type_date = Integer.parseInt(args[9]);
		 crawlerArticleByPage.initialization(urls,bean,
				 fetch,source,download,province_id,province,type_image,type_get_html,type_date);
		 crawlerArticleByPage.collectionData();
		
		/* crawlerArticleByPage.initialization("d:/categorys2/urlsTinthethaoTH.xml","d:/categorys2/beanTinthethaoTH.xml", 1,16,1,0,"Toan-Quoc",0,1,0);
		 crawlerArticleByPage.collectionData();*/

	}

}
