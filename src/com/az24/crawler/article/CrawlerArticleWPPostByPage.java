package com.az24.crawler.article;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.ExtractEntity;
import hdc.util.html.SelectImgArticleVisitor;
import hdc.util.html.parser.DomWriter;
import hdc.util.text.HtmlUtil;
import hdc.util.text.StringUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
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
import com.az24.crawler.model.DatabaseConfig;
import com.az24.crawler.model.ImageConfig;
import com.az24.crawler.model.Property;
import com.az24.crawler.model.WPPost;
import com.az24.dao.CrawlerLogDAO;
import com.az24.dao.WPPostDAO;
import com.az24.db.pool.C3p0BaloteenPool;
import com.az24.util.Constants;

public class CrawlerArticleWPPostByPage  extends CrawlerArticleByPage{
	
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
		selectImgArticleVisitor = new SelectImgArticleVisitor("http://xemdiemthi.info/wp-content/uploads/", "");
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
						List<hdc.crawler.Node> nodeDelByXpath = property.getNodedelByXpaths();
						if (nodeDelByXpath != null) {
							for (hdc.crawler.Node node2 : nodeDelByXpath) {
								CrawlerUtil.removeNodeByXpath(reader,node2.xpath);
							}
						}

						if (StringUtil.isEmpty(property.getXpath_sub())) {
							String xpath[] = property.getXpath().split(";");
							for (String string : xpath) {
								if (!StringUtil.isEmpty(string)) {
									nodes = (NodeList) reader.read(string,XPathConstants.NODESET);
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

								

							}
							str = writer.toXMLString(nodes.item(0));
							
							entity.addProperty(property.getName(), str);
							// System.out.println(new Date()+" Process Property:"+property.getName()+"="+ str);
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

	
	@SuppressWarnings("unused")
	@Override
	protected void saveData(ExtractEntity entity) throws Exception {
		WPPostDAO  postDAO = new WPPostDAO();
		CrawlerLogDAO crawlerLogDAO = new CrawlerLogDAO();
		HashMap<Integer, Integer> cats = new HashMap<Integer, Integer>();
		WPPost wPost = new WPPost();
		String picture = "";
		ImageConfig imageConfigAvatar = null;
		//getCategoriesParent(entity.getCat_id(), cats);
		//String scon =(String) entity.getProperty("content");
		//System.out.println(entity.getProperty("content"));
		wPost.post_title = StringUtil.getTextFromNode((String)entity.getProperty("title"));
		wPost.post_content = StringUtil.getTextFromNode((String)entity.getProperty("content"));
		if (StringUtil.isEmpty(wPost.post_title) || StringUtil.isEmpty(wPost.post_content))
			return;

		if (imageMap != null && imageMap.containsKey(entity.getID())) {
			imageConfigAvatar = (ImageConfig) imageMap.get(entity.getID());
			picture = imageConfigAvatar.name.substring(6,
					imageConfigAvatar.name.length());
		}

		try {
			int cat_root_id =entity.getCat_id();
			boolean loged = crawlerLogDAO
					.saveEntityCheck(DatabaseConfig.table_entity_log,
							"", 0, cat_root_id, entity);
			if (loged) {
				wPost.post_author = 1;
				wPost.post_date = new Timestamp(Calendar.getInstance().getTimeInMillis());
				wPost.post_date_gmt = new Timestamp(Calendar.getInstance().getTimeInMillis());
				wPost.post_modified = new Timestamp(Calendar.getInstance().getTimeInMillis());
				wPost.post_modified_gmt = new Timestamp(Calendar.getInstance().getTimeInMillis());
				wPost.post_excerpt = "";
				wPost.ping_status ="publish";
				wPost.post_status = "publish";
				wPost.comment_status = "open";				
				wPost.ping_status = "open";
				wPost.post_password ="";
				wPost.post_name = StringUtil.getAlias(wPost.post_title);
				wPost.to_ping = "";
				wPost.pinged = "";
				wPost.post_content_filtered="";
				wPost.post_parent=0;
				wPost.guid = "";
				wPost.menu_order = 0;
				wPost.post_type = "post";
				wPost.comment_count = 0;
				wPost.post_mime_type = "";
				System.out.println(wPost.post_content);
				Calendar calendar = Calendar.getInstance();
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
				
				int id = postDAO.savePost(wPost);
				wPost.id = id;
				
				DownloadImage downloadImage = null;
				if (selectImgArticleVisitor.imageList.size() > 0&&this.type_get_anh==1) {
					for (int i = 0; i < selectImgArticleVisitor.imageList.size(); i++) {
						ImageConfig imageConfig = (ImageConfig) selectImgArticleVisitor.imageList.get(i);
						// Get Small Image Null
						if (imageConfigAvatar == null && i == 0) {
							/*downloadImage = new DownloadImage(
									imageConfig.src, "small_"+imageConfig.name,
									UrlInjectXmlConfig.baseUrl, "", 1,
									this.type_download,
									selectImgArticleVisitor.imageList);
							downloadImage.pre_folder=Constants.XDT_PRE_FOLDER;
							downloadImage.run();*/
							
							wPost.post_content="";
							wPost.comment_status="open";
							wPost.post_status="inherit";
							wPost.ping_status="open";
							wPost.post_type="attachment";
							wPost.post_mime_type = "image/jpeg";
							wPost.post_parent = wPost.id;
							
							
							calendar = Calendar.getInstance();		
							int month = calendar.get(Calendar.MONTH)+1;
							String strMonth = month<10?"0"+month:month+"";
							int day = calendar.get(Calendar.DAY_OF_MONTH);
							String strDay = day<10?"0"+day:day+"";
							String strYear = calendar.get(Calendar.YEAR)+"";
							
							wPost.guid=imageConfig.src;
							
							String meta_value=postDAO.savePost(wPost)+"";
							postDAO.savePostMeta(wPost.id, "_thumbnail_id", meta_value);
							
							break;
						}

					}

				}
				
				
				if (id > 0) {
					postDAO.saveTermRelationships(id, entity.getCat_id());
					
					if (imageConfigAvatar != null) {
						/*downloadImage = new DownloadImage(
								imageConfigAvatar.src, imageConfigAvatar.name,
								UrlInjectXmlConfig.baseUrl, "", 1,
								this.type_download,
								selectImgArticleVisitor.imageList);
						downloadImage.pre_folder=Constants.XDT_PRE_FOLDER;
						downloadImage.run();*/
						wPost.post_content="";
						wPost.comment_status="open";
						wPost.post_status="inherit";
						wPost.ping_status="open";
						wPost.post_type="attachment";
						wPost.post_mime_type = "image/jpeg";
						wPost.post_parent = wPost.id;
						
						
						calendar = Calendar.getInstance();		
						int month = calendar.get(Calendar.MONTH)+1;
						String strMonth = month<10?"0"+month:month+"";
						int day = calendar.get(Calendar.DAY_OF_MONTH);
						String strDay = day<10?"0"+day:day+"";
						String strYear = calendar.get(Calendar.YEAR)+"";
						
						//wPost.guid="http://xemdiemthi.info/wp-content/uploads/"+strYear+"/"+strMonth+strDay+"/"+imageConfigAvatar.name;
						wPost.guid = imageConfigAvatar.src;
						
						
						if(!StringUtil.isEmpty(wPost.guid )&&wPost.guid.length()<512)
						{
							String meta_value=postDAO.savePost(wPost)+"";
							postDAO.savePostMeta(wPost.id, "_thumbnail_id", meta_value);
						}
						
						// Save Tag
						String tags = StringUtil.getAttribute("tags", entity);
						if(!StringUtil.isEmpty(tags))
						{
							String[] arrTag = tags.split(",");
							for (String string : arrTag) {
								int tag_id=postDAO.getIDTerm(string);
								int term_id = 0;
								if(tag_id==0)
								{
									tag_id = postDAO.save_wp_terms(string, StringUtil.getAlias(string), 0);								
									term_id= postDAO.save_wp_term_taxonomy(tag_id, "post_tag", "", 0, 0);
								}else{
									 term_id = postDAO.getIDTermtaxonomy(tag_id);
								}
								postDAO.saveTermRelationships(wPost.id, term_id);
							}
						}
						// Meta title
						String title = wPost.post_title + " | " + postDAO.getCategory(entity.getCat_id());
						title = title.length()>70?title.substring(0,70):title;
						 
						postDAO.save_wp_postmeta(wPost.id, "_yoast_wpseo_title", title);
						postDAO.save_wp_postmeta(wPost.id, "_yoast_wpseo_focuskw", "tuyen sinh 2012, tra cu diem thi 2012,tin tuyen sinh vao 10,tin tuyen sinh cao hoc,tin tuyen sinh thuc tap,tin tuyen sinh du hoc");
						//String content = HtmlUtil.removeTag(StringUtil.getTextFromNode((String)entity.getProperty("content")));
						String _yoast_wpseo_metadesc = "xem diem thi: "+wPost.post_title;
						postDAO.save_wp_postmeta(wPost.id, "_yoast_wpseo_metadesc", _yoast_wpseo_metadesc);
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
							downloadImage.pre_folder=Constants.XDT_PRE_FOLDER;
							downloadImage.run();
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
		CrawlerArticleWPPostByPage crawlerArticleByPage = new CrawlerArticleWPPostByPage();
		/* String urls = args[0];
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
*/
		 crawlerArticleByPage.initialization("d:/urlsTinTuyenSinh2012.xml","d:/beanTinTuyenSinh2012.xml", 4,16,1,0,"Toan-Quoc",1,1,0);
		 crawlerArticleByPage.collectionData();

	}
}
