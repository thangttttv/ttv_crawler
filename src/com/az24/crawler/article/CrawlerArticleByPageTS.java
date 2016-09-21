package com.az24.crawler.article;

import hdc.crawler.ExtractEntity;
import hdc.util.html.SelectImgArticleVisitor;
import hdc.util.text.HtmlUtil;
import hdc.util.text.StringUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.az24.crawler.config.UrlInjectXmlConfig;
import com.az24.crawler.model.ArticleTS;
import com.az24.crawler.model.DatabaseConfig;
import com.az24.crawler.model.ImageConfig;
import com.az24.dao.CrawlerLogDAO;
import com.az24.dao.TuyenSinh247DAO;
import com.az24.db.pool.C3p0TuyensinhPool;

public class CrawlerArticleByPageTS extends CrawlerArticleByPage {
	
	
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
				"http://images.tuyensinh247.com/picture/article/", "");
	}
	
	@Override
	protected void saveData(ExtractEntity entity) throws Exception {
		CrawlerLogDAO crawlerLogDAO = new CrawlerLogDAO();
		TuyenSinh247DAO articleImporter = new TuyenSinh247DAO();
		HashMap<Integer, Integer> cats = new HashMap<Integer, Integer>();
		ArticleTS article = new ArticleTS();
		String content = "";
		String picture = "";
		ImageConfig imageConfigAvatar = null;
		getCategoriesParent(entity.getCat_id(), cats);
		article.title = HtmlUtil.toText((String) entity.getProperty("title"));
		article.title = StringUtil.getTextFromNode(article.title);
		content = StringUtil.getTextFromNode((String) entity.getProperty("content"));		
		if (StringUtil.isEmpty(article.title) || StringUtil.isEmpty(content))	return;

		if (imageMap != null && imageMap.containsKey(entity.getID())) {
			imageConfigAvatar = (ImageConfig) imageMap.get(entity.getID());
			picture = imageConfigAvatar.name_n;
		} else {
			if (type_get_anh == 0)
				return;
		}

		try {
			
			int cat_root_id = ((Integer) cats.get(Integer.valueOf(1))).intValue();
			boolean loged = crawlerLogDAO.saveEntityCheck(DatabaseConfig.table_entity_log,	"", 0, cat_root_id, entity);
			
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
				
				article.tags = StringUtil.getAttribute("tags", entity);
				article.alias = StringUtil.getAlias(article.title);
				article.content = content;
				article.picture = picture;				
				article.status = 1;
				article.is_hot = 0;		
				article.original_link = entity.getUrl();
				article.cat_id1 = cat_root_id;
				if (cats.get(Integer.valueOf(2)) != null)
				{
					article.cat_id2 = ((Integer) cats.get(Integer.valueOf(2))).intValue();
					article.cat_id = ((Integer) cats.get(Integer.valueOf(2))).intValue();
				}else {
					article.cat_id = cat_root_id;
				}
				
				article.source = source;				
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
				
				int id = articleImporter.saveNews(article);
				article.id = id;
				if (id > 0) {
					articleImporter.saveNewsShort(article);
					articleImporter.saveNewsShortLastest(article);					
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
							articleImporter.saveImage(id,article.cat_id1,
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

	@Override
	protected Map<Integer, Integer> getCategoriesParent( int cat,
			Map<Integer, Integer> holder) throws Exception {
		int cat_parent = 0;
		Connection conn = C3p0TuyensinhPool.getConnection();
		ResultSet rs = conn.createStatement().executeQuery(
				(new StringBuilder(
						"SELECT parent_id FROM tbl_categories WHERE id = "))
						.append(cat).toString());
		if (rs.next())
			cat_parent = rs.getInt(1);
		if (cat_parent == 0) {
			holder.put(Integer.valueOf(1), Integer.valueOf(cat));
			C3p0TuyensinhPool.attemptClose(conn);
			return holder;
		} else {
			holder.put(Integer.valueOf(2), Integer.valueOf(cat));
			getCategoriesParent( cat_parent, holder);
			C3p0TuyensinhPool.attemptClose(conn);
			return holder;
		}
		
	}
	
	
	public static void main(String args[]) {
		 CrawlerArticleByPageTS cArticleByPageTS = new CrawlerArticleByPageTS();
		 /*  String urls = args[0];
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
		 crawlerArticleByPage.collectionData();*/
		
		 cArticleByPageTS.initialization("d:/urlsdantriTH.xml","d:/beanDantriTH.xml", 6,1,1,0,"",0,1,0);
		 cArticleByPageTS.collectionData();

	}

}
