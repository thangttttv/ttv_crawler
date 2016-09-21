package com.az24.crawler.classified;

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
import hdc.util.html.parser.DomWriter;
import hdc.util.html.parser.XPathReader;
import hdc.util.lang.UriID;
import hdc.util.text.HtmlUtil;
import hdc.util.text.StringUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathConstants;

import org.apache.http.HttpResponse;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import com.az24.crawler.config.BeanXmlConfig;
import com.az24.crawler.config.SourceXmlConfig;
import com.az24.crawler.config.UrlInjectXmlConfig;
import com.az24.crawler.fiter.AbstractFilter;
import com.az24.crawler.fiter.ContentFilter;
import com.az24.crawler.model.Classified;
import com.az24.crawler.model.DatabaseConfig;
import com.az24.crawler.model.ImageConfig;
import com.az24.crawler.model.Property;
import com.az24.crawler.store.City;
import com.az24.dao.ClassifiedDAO;
import com.az24.dao.CrawlerLogDAO;
import com.az24.test.HttpClientUtil;
import com.az24.util.UTF8Tool;

public class CrawlerClassified {
		
	private String fileUrlConfig = "";
	private String fileBeanConfig = "";
	private int type_fetch_html = 1;
	private int sleep = 1000;	
	private String baseUrl = "";
	protected List<Node> nodesDel;
	private List<ImageConfig> imagesCla;
	
	public CrawlerClassified(String fileUrlConfig,String fileBeanConfig,int type_fetch_html )
	{
		this.fileBeanConfig = fileBeanConfig;
		this.fileUrlConfig=fileUrlConfig;
		this.type_fetch_html = type_fetch_html;
	}
	
	public void collectionUrl() {
		try {			
			UrlInjectXmlConfig urlInjectXmlConfig = new UrlInjectXmlConfig(
					this.fileUrlConfig);
			urlInjectXmlConfig.parseConfig();
			// Get Link Crawler
			List<Url> urls = UrlInjectXmlConfig.urlConfigs;
			String baseUrl = UrlInjectXmlConfig.baseUrl;
			String rewriterUrl = UrlInjectXmlConfig.rewriterUrl;
			BeanXmlConfig beanXmlConfig = new BeanXmlConfig(fileBeanConfig);
			beanXmlConfig.parseConfig();
			this.baseUrl = baseUrl;
			int totalUrl = 0;
			HashMap<String, Url> urlMap = null;
			for (Url url : urls) {	
				urlMap = new HashMap<String, Url>();
				System.out.println("Process----->"+url.url);
				totalUrl += putLink(urlMap, url, baseUrl, rewriterUrl,this.type_fetch_html);	
				Thread.sleep(sleep);
				processData(urlMap, type_fetch_html);
				Thread.sleep(sleep);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	
	private int putLink(HashMap<String, Url> urlMap,
			hdc.crawler.AbstractCrawler.Url url, String baseUrl,
			String rewriterUrl, int fetch_type) throws Exception {
		String html = "";

		if (this.type_fetch_html == 1) {
			HttpClientImpl client = new HttpClientImpl();
			HttpResponse res = client.fetch(url.url);
			html = HttpClientUtil.getResponseBody(res);
		} else {
			html = HttpClientUtil.getHtml(url.url);
		}	
		
		System.out.println("Crawler-->"+url.url);
		//System.out.println("Crawler-->"+html);
		int count = 0;
		switch (url.collection) {
		case 1:
			count = crawlerLink_1(urlMap, url, baseUrl, rewriterUrl, html);			
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
					boolean kt = AbstractFilter.find(url.regex, a.getURL());
					System.out.println(url.regex+"="+kt);
					if(!kt){continue;}
					if (!crawlerLogDAO.checkEntity(DatabaseConfig.table_entity_log, id)) {
						if (kt && !urlMap.containsKey(id)) {
								injectUrl = new hdc.crawler.AbstractCrawler.Url();
								injectUrl.id = id;
								injectUrl.cat_id = url.cat_id;
								injectUrl.url = a.getURL();
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

		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return i;
	}
	
	
	private void processData(HashMap<String, Url> urlMap, int fetch_type) {
		Iterator<Url> urls = urlMap.values().iterator();
		hdc.crawler.AbstractCrawler.Url urlFetch = null;
		HttpClientImpl client = new HttpClientImpl();
		org.apache.http.HttpResponse res = null;
		String html = "";
		try {
			while (urls.hasNext()) {
				urlFetch = (hdc.crawler.AbstractCrawler.Url) urls.next();
				System.out.println("Extract url=" + urlFetch.url);
				if (type_fetch_html == 1) {					
					Pattern pattern = Pattern.compile("[^\\.\\w:/-]");
					Matcher m = pattern.matcher(urlFetch.url);				
					urlFetch.url = m.replaceAll("%20");					
					res = client.fetch(urlFetch.url);
					html = HttpClientUtil.getResponseBody(res);					
				} else
				{ 
					html = HttpClientUtil.getHtml(urlFetch.url); 
				}
				ExtractEntity entity = extractHTML(urlFetch, html);					
				saveData(entity);	
				Thread.sleep(sleep);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private ExtractEntity extractHTML(hdc.crawler.AbstractCrawler.Url url,
			String html) {
		ExtractEntity entity = new ExtractEntity();
		html = StringUtil.stripNonValidXMLCharacters(html);
		try {
			UrlInjectXmlConfig urlInjectXmlConfig = new UrlInjectXmlConfig(
					fileUrlConfig);
			urlInjectXmlConfig.parseConfig();
			BeanXmlConfig beanXmlConfig = new BeanXmlConfig(fileBeanConfig);
			beanXmlConfig.parseConfig();
			List<Property> propeties = beanXmlConfig.bean.getProperties();
			List<ImageConfig> images = beanXmlConfig.bean.getImages();
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
			processProperty(reader, propeties, entity);
			processImage(reader, images, entity);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entity;
	}

	protected boolean processProperty(XPathReader reader,
			List<Property> propeties, ExtractEntity entity) {
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
										.getProperty("cla_name"));
								String pattern = "<.*xml.*>";
								Pattern r = Pattern.compile(pattern);
								Matcher m = r.matcher(title);
								title = m.replaceAll("");
							}
							str = writer.toXMLString(nodes.item(0));
							if ("1".equalsIgnoreCase(property.getFilter()))
								filter = ContentFilter.filter(str);
							if (filter)
								return true;
							entity.addProperty(property.getName(), str);
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
							entity.addProperty(property.getName(), str);
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

						entity.addProperty(property.getName(), str.trim());
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
	
	
	private void processImage(XPathReader reader, List<ImageConfig> images,ExtractEntity entity) throws Exception {
		if (images == null)
			return;		
		try
		{			
			imagesCla = new ArrayList<ImageConfig>();
			Calendar calendar = Calendar.getInstance();
			String id = "";String month="";String day="";String fileImage="";String src = "";
			for (ImageConfig imageConfig : images) {							
				src = (String) reader.read(imageConfig.xpath,XPathConstants.STRING);
				if(src.indexOf("background")>-1) src = src.substring(src.indexOf("http"),src.indexOf("')"));
				id = new UriID(new HttpURL(src)).getIdAsString();
				if(!StringUtil.isEmpty(src))
				{
					imageConfig.id=id;
					imageConfig.src = src;						
					day = calendar.get(Calendar.DAY_OF_MONTH)<10?"0"+calendar.get(Calendar.DAY_OF_MONTH):""+calendar.get(Calendar.DAY_OF_MONTH);
					month = calendar.get(Calendar.MONTH)<10?"0"+calendar.get(Calendar.MONTH):""+calendar.get(Calendar.MONTH);
					imageConfig.dateProcess = day	+ "/"	+ month	+ "/"+ calendar.get(Calendar.YEAR);
					fileImage= "/picture_auto/"+calendar.get(Calendar.YEAR)+"/thumb/"+month+day+"/"+src.substring(src.lastIndexOf("/")+1);
					imagesCla.add(imageConfig);					
					entity.addProperty("cla_picture", fileImage);
				}
			
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void saveData(ExtractEntity entity) {
		BeanXmlConfig beanXmlConfig = new BeanXmlConfig(this.fileBeanConfig);
		beanXmlConfig.parseConfig();
		Random random = new Random();
		
		CrawlerLogDAO crawlerLogDAO = new CrawlerLogDAO();
		ClassifiedDAO classifiedDAO = new ClassifiedDAO();
		Pattern pattern  = null;
		Matcher matcher = null;
		List<City> citys = classifiedDAO.getCitis();
		List<City> city_quans = classifiedDAO.getCitisQuan();
		try {
				
				int cla_district = 0;
				Map<Integer, Integer> cat_holder = null;String arrDes[]=null;
				String short_content="";
				int log_id = 0;
				String content="";
				Classified classified = null;
				City city = null;	
				
				// Check Content Null
 				System.out.println(new Date()+" Imported Entity Name "+entity.getProperty("cla_name"));
				System.out.println(new Date()+" Imported Entity URL "+entity.getUrl());
				if(StringUtil.isEmpty((String) entity.getProperty("cla_name"))||StringUtil.isEmpty((String) entity.getProperty("cla_description")))
				{					
					return;
				}
				
				// Save Log Before Insert Content
				entity.setName((String)entity.getProperty("cla_name"));
				log_id = crawlerLogDAO.saveEntity(DatabaseConfig.table_entity_log, "", 0, entity.getCat_id(), entity);
				if(log_id>0)
				{	
					classified = new Classified();
					// Get Category
					cat_holder = new HashMap<Integer, Integer>();
					classifiedDAO.getCategoriesParent(entity.getCat_id(), cat_holder);					
					classified.cla_category = entity.getCat_id();
					// Get City
					city = this.getCity((String)entity.getProperty("cla_city"), citys);
					if(city!=null)
					{
						classified.cla_cityname = city.name;					
						classified.cla_city = city.id;
					}				
					// Get District
					cla_district = this.getCityId((String)entity.getProperty("cla_district"), city_quans);
					System.out.println((String)entity.getProperty("cla_district"));
					System.out.println((String)entity.getProperty("cla_city"));
					classified.cla_district = cla_district;
					classified.cla_date = (System.currentTimeMillis() / 1000 - (24 * random.nextInt(60) * 60));
					classified.cla_expired = (System.currentTimeMillis() / 1000 + (30 * 24 * 60 * 60));
					classified.cla_update_date = (System.currentTimeMillis() / 1000 - (24 * random.nextInt(60) * 60));
					classified.cla_auto = getSource(entity.getUrl());	
					
					if(entity.getProperty("cla_price")!=null)
					{
						String price = (String)entity.getProperty("cla_price");
						if(!StringUtil.isEmpty(price))
						{
						pattern = Pattern.compile("\\D");
						matcher = pattern.matcher(price); 
						classified.cla_price = Double.parseDouble(matcher.replaceAll(""));
						}
					}
					
					if(this.baseUrl.indexOf("vatgia")>0)
					{
						classified.cla_email = getEmailVatGia((String)entity.getProperty("cla_email"));
					}else
					{
						classified.cla_email = (String)entity.getProperty("cla_email");
					}
					
					if(StringUtil.isEmpty(classified.cla_contact)) classified.cla_contact =classified.cla_email; 
					
					classified.cate_parent1 = cat_holder.get(1);					
					classifiedDAO.incrementClassified(cat_holder.get(1));
					if (cat_holder.get(2) != null)
					{
						classified.cate_parent2 = cat_holder.get(2);
						classifiedDAO.incrementClassified(cat_holder.get(2));
					}
					if (cat_holder.get(3) != null)
					{
						classified.cate_parent3 = cat_holder.get(3);
						classifiedDAO.incrementClassified(cat_holder.get(3));
					}
					if (cat_holder.get(4) != null)
					{	
						classified.cate_parent4 = cat_holder.get(4);
						classifiedDAO.incrementClassified(cat_holder.get(4));
					}
					
					
					classified.cla_picture = (String) entity.getProperty("cla_picture");
					classified.cla_name = (String) entity.getProperty("cla_name");					
					content = (String) entity.getProperty("cla_description");
					
					// Set Content
					if(content!=null&&content.length()>"<?xml version=\"1.0\" encoding=\"UTF-8\"?>".length())
					{
						content=content.substring("<?xml version=\"1.0\" encoding=\"UTF-8\"?>".length());
						short_content="";
						String content_removehtml = HtmlUtil.removeTag(content).trim();
						pattern = Pattern.compile("\\s{2,}");
						matcher = pattern.matcher(content_removehtml);
						content_removehtml = matcher.replaceAll(" ");
						
						arrDes = content_removehtml.split(" ");
						int length = arrDes.length<50? arrDes.length: 50;
						int j=0;
						while(j<length)
						{ 
							short_content +=" "+	arrDes[j];					
							j++;
						}						
						classified.cla_description = short_content;
						// Set Contact
						String cla_contact = (String) entity.getProperty("cla_contact");
						matcher = pattern.matcher(cla_contact);
						cla_contact = matcher.replaceAll(" ");
						if(cla_contact.length()>255) cla_contact = cla_contact.substring(0,255);
						classified.cla_contact = cla_contact;
					}
					
					classified.cla_userid=0;
					classified.cla_username="Khách vãng lai";
					
					//Rewrite Link
					pattern = Pattern.compile("\\W+");
					Pattern pattern2 = Pattern.compile("-$");
					String  alias = UTF8Tool.coDau2KoDau((String)entity.getProperty("cla_name")).trim();
					matcher = pattern.matcher(alias);
					String url_rewrite=matcher.replaceAll("-");
					matcher = pattern2.matcher(url_rewrite);
					url_rewrite = matcher.replaceAll("");					
					classified.cla_rewrite = url_rewrite;
					classified.cla_active = 0;
					int	cla_id = classifiedDAO.saveClassified(classified);
					// Save content	
					// save description
					if (cla_id>0)
					{					
						int idTable = cla_id%1000;	
						classifiedDAO.saveClassifiedDesc(cla_id, content, idTable);
						// Download Image
						if(imagesCla!=null)
						{
							DownloadImageThread downloadImageThread = new DownloadImageThread(this.baseUrl,imagesCla);
							downloadImageThread.run();
						}
					}					
				 }									
					
			}catch (Exception e) {
				e.printStackTrace();
			}
			 
	}

	private int getCityId(String city_name,List<City> citys )
	{
		int city_id  = 0;
		if(city_name!=null)
		{
			city_name = UTF8Tool.coDau2KoDau(city_name.trim());
			for (City city : citys) {
				String name = UTF8Tool.coDau2KoDau(city.name.trim());
				name = name.substring(2);
				if(city_name.indexOf(name)>-1) {city_id = city.id ;break;}
			}
		}
		return city_id;
	}

	

	private City getCity(String city_name,List<City> citys )
	{
		int id = 0;		
		City cityR = null;		
		if(StringUtil.isEmpty(city_name)) id=0; else
		{
			city_name = UTF8Tool.coDau2KoDau(city_name.trim().toLowerCase());
			if (city_name.indexOf("ha noi")>-1||city_name.indexOf("hn")>-1) {
				id = 1473;cityR = new City();cityR.id = id ;cityR.name = "Hà nội";
			} else if (city_name.indexOf("ho chi minh")>-1||city_name.indexOf("hcm")>-1||city_name.indexOf("tp.hcm")>-1) {
				id = 1474;cityR = new City();cityR.id = id ;cityR.name = "Hồ Chí Minh";
			} else {						
				
				for (City cityTmp : citys) {
					String name = UTF8Tool.coDau2KoDau(cityTmp.name.trim());
					if(city_name.indexOf(name)>-1) { cityR =  cityTmp;break;}
				}
			}
		}
		return cityR;
	}
	
	private int getSource(String url)
	{
		SourceXmlConfig beanXmlConfig = new SourceXmlConfig("./conf/source_raovat.xml");
		beanXmlConfig.parseConfig();
		List<Url> urls = SourceXmlConfig.urlConfigs;
		for (Url url2 : urls) {
			if(url.indexOf(url2.url)>-1) return Integer.parseInt(url2.id);
		}
		return 2;
	}
	
	private String getEmailVatGia(String cla_email)
	{
		String email ="";
		try{
			if(!StringUtil.isEmpty(cla_email))
			{
				Pattern r = Pattern.compile("[^\\d,]");
				Matcher m = r.matcher(cla_email);System.out.println(m.replaceAll(""));
				String strChar[]=m.replaceAll("").split(",");
				int i = 0;
				char chars[] = new char[2000];
				for (String string : strChar) {
					chars[i]= new Character((char)Integer.parseInt(string));
					i++;
				}
				
				email = new String(chars);
				r = Pattern.compile(">(.*)<");
				m = r.matcher(email);				
				if(m.find())
					email=m.group(1);
			}
		}catch (Exception e) {			
		}		
		return email;
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
	
	
	public static void main(String[] args) {		
		CrawlerClassified classified = new CrawlerClassified(args[0],args[1],Integer.parseInt(args[2]));
		classified.collectionUrl();
	}
	
	
}
