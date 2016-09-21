package com.az24.crawler.article;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.ExtractEntity;
import hdc.util.html.ObjectVideo;
import hdc.util.html.SelectImgArticleVisitor;
import hdc.util.html.SelectOjbectVisitor;
import hdc.util.html.parser.DomWriter;
import hdc.util.text.HtmlUtil;
import hdc.util.text.StringUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathConstants;

import org.w3c.dom.NodeList;

import com.az24.crawler.config.UrlInjectXmlConfig;
import com.az24.crawler.fiter.ContentFilter;
import com.az24.crawler.model.Article;
import com.az24.crawler.model.DatabaseConfig;
import com.az24.crawler.model.ImageConfig;
import com.az24.crawler.model.Property;
import com.az24.dao.ArticleBaloteenDAO;
import com.az24.dao.CrawlerLogDAO;
import com.az24.db.pool.C3p0BaloteenPool;
import com.az24.util.Constants;
import com.az24.util.UTF8Tool;

public class CrawlerArticleBaloteenByPage  extends CrawlerArticleByPage{
	
	@Override
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
				"http://images.az24.vn/baloteen/picture/article/", "");
	}
	
	@SuppressWarnings("unused")
	@Override
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
							//get video
							SelectOjbectVisitor selectOjbectVisitor = new SelectOjbectVisitor("");
							selectOjbectVisitor.traverse(nodes.item(0));
							List<ObjectVideo> listVideo =selectOjbectVisitor.getLinks();
							if(listVideo!=null&&listVideo.size()>0)
							{
								for (ObjectVideo object : listVideo) {
									System.out.println(object.embed_source);
								}
								entity.addProperty("videos", listVideo);
								entity.addProperty("numbervideo", listVideo.size());
							}
							
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

	
	@SuppressWarnings({ "unused", "unchecked" })
	@Override
	protected void saveData(ExtractEntity entity) throws Exception {
		CrawlerLogDAO crawlerLogDAO;
		ArticleBaloteenDAO articleImporter;
		crawlerLogDAO = new CrawlerLogDAO();
		articleImporter = new ArticleBaloteenDAO();
		HashMap<Integer, Integer> cats = new HashMap<Integer, Integer>();
		Article article = new Article();
		String content = "";
		String picture = "";
		ImageConfig imageConfigAvatar = null;

		getCategoriesParent(entity.getCat_id(), cats);

		article.title = HtmlUtil.toText((String) entity.getProperty("title"));
		if (article.title != null
				&& article.title.length() > "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
						.length()
				&& article.title
						.indexOf("<?xml version=\"1.0\" encoding=\"UTF-8\"?>") >= 0)
			article.title = article.title
					.substring("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
							.length());

		content = (String) entity.getProperty("content");
		if (content != null
				&& content.length() > "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
						.length())
			content = content
					.substring("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
							.length());

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
							downloadImage.pre_folder="/usr/src/java/tomcat7/webapps/images.az24.vn/baloteen/picture/article/";
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
						downloadImage.pre_folder=Constants.BALOTEEN_PRE_FOLDER;
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
							downloadImage.pre_folder=Constants.BALOTEEN_PRE_FOLDER;
							downloadImage.run();
							articleImporter.saveImage(id,article.category_id,
									downloadImage.destinationFile);

						}

					}
					
					// save video
					if(entity.getProperty("videos")!=null)
					{
						List<ObjectVideo> videos =(List<ObjectVideo>)entity.getProperty("videos");
						if(videos!=null)
							for (ObjectVideo objectVideo : videos) {
								objectVideo.article_id=id;
								objectVideo.cat_id = cat_root_id;
								objectVideo.picture=article.picture;
								objectVideo.alias=article.alias;
								objectVideo.title = article.title;
								articleImporter.saveVideo(objectVideo);
							}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}
	
	@Override
	protected Map<Integer, Integer> getCategoriesParent( int cat,
			Map<Integer, Integer> holder) throws Exception {
		int cat_parent = 0;
		Connection conn = C3p0BaloteenPool.getConnection();
		ResultSet rs = conn.createStatement().executeQuery(
				(new StringBuilder(
						"SELECT parent_id FROM tbl_categories WHERE id = "))
						.append(cat).toString());
		if (rs.next())
			cat_parent = rs.getInt(1);
		if (cat_parent == 0) {
			holder.put(Integer.valueOf(1), Integer.valueOf(cat));
			C3p0BaloteenPool.attemptClose(conn);
			return holder;
		} else {
			holder.put(Integer.valueOf(2), Integer.valueOf(cat));
			getCategoriesParent( cat_parent, holder);
			C3p0BaloteenPool.attemptClose(conn);
			return holder;
		}
		
	}
	
	public static void main(String args[]) {
		CrawlerArticleBaloteenByPage crawlerArticleByPage = new CrawlerArticleBaloteenByPage();
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

	}
}
