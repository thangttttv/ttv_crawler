package com.az24.crawler.tuyensinh247;

import hdc.crawler.ExtractEntity;
import hdc.util.html.SelectImgArticleVisitor;
import hdc.util.text.HtmlUtil;
import hdc.util.text.StringUtil;

import java.util.Calendar;
import java.util.HashMap;

import com.az24.crawler.article.DownloadImage;
import com.az24.crawler.config.UrlInjectXmlConfig;
import com.az24.crawler.model.CoursesLong;
import com.az24.crawler.model.CoursesLongCategory;
import com.az24.crawler.model.CoursesLongCity;
import com.az24.crawler.model.DatabaseConfig;
import com.az24.crawler.model.ImageConfig;
import com.az24.dao.CrawlerLogDAO;
import com.az24.dao.TuyenSinh247DAO;
import com.az24.util.Constants;

public class CrawlerCoursesLong extends CrawlerCoursesShort{
	
	public void initialization(String fileUrl, String fileBeanConfig,String fileBeanTrainerConfig,
			int type_collection_link, int source, int type_download,  int type_get_image, int type_get_html, int type_date) {
		fileUrlConfig = fileUrl;
		this.fileBeanConfig = fileBeanConfig;
		this.fileBeanTrainerConfig = fileBeanTrainerConfig;
		this.type_collection_link = type_collection_link;
		this.source = source;		
		this.type_download = type_download;
		this.type_get_anh = type_get_image;
		if(this.type_collection_link==3) this.type_get_anh=1;
		this.type_get_html = type_get_html;
		this.type_date = type_date;
		selectImgArticleVisitor = new SelectImgArticleVisitor(
				"http://images.tuyensinh247.com/picture/course/long/", "");
	}

	
	protected void saveData(ExtractEntity entity) throws Exception {
		
		CrawlerLogDAO crawlerLogDAO;
		TuyenSinh247DAO  tuyenSinh247DAO = new TuyenSinh247DAO();
		crawlerLogDAO = new CrawlerLogDAO();
		HashMap<Integer, Integer> cat_holder = new HashMap<Integer, Integer>();
		CoursesLong coursesShort = new CoursesLong();
		String content = "";
		String picture = "";
		ImageConfig imageConfigAvatar = null;		
		CoursesLongCategory coursesLongCategory = new CoursesLongCategory();
		CoursesLongCity coursesLongCity = new CoursesLongCity();
		
		tuyenSinh247DAO.getCategoriesParent(entity.getCat_id(), cat_holder);

		coursesShort.title = StringUtil.getTextFromNode(HtmlUtil.toText((String) entity.getProperty("title")));
		content = StringUtil.getTextFromNode((String) entity.getProperty("content"));
		

		if (StringUtil.isEmpty(coursesShort.title) || StringUtil.isEmpty(content))
			return;

		if (imageMap != null && imageMap.containsKey(entity.getID())) {
			imageConfigAvatar = (ImageConfig) imageMap.get(entity.getID());
			picture = imageConfigAvatar.name.substring(6,imageConfigAvatar.name.length());
		}

		try {
			int cat_root_id = ((Integer) cat_holder.get(Integer.valueOf(1)))
					.intValue();
			boolean loged = crawlerLogDAO
					.saveEntityCheck(DatabaseConfig.table_entity_log,
							"", 0, cat_root_id, entity);
			if (loged) {
				String trainner_name =  (String) entity.getProperty("trainner_name");
				String trainner_url =  (String) entity.getProperty("trainner_url");
				
				if(StringUtil.isEmpty(trainner_name))return;
				
				int trainer_id = tuyenSinh247DAO.getTrainer(trainner_name);
				
				if(trainer_id==0) trainer_id = extractTrainer(trainner_url);
				
				if(trainer_id==0)return;
				
				coursesShort.trainer_id =trainer_id; // Tao Trainner_ID
				
				// courses attribute
				coursesShort.alias = StringUtil.getAlias(coursesShort.title);
				coursesShort.content = content;
				coursesShort.introtext =StringUtil.getAttribute("introtext", entity);
				coursesShort.picture = picture;
				coursesShort.tags = StringUtil.getAttribute("tags", entity); 
				coursesShort.user_id = 0;
				coursesShort.username = "";				
				Calendar calendar = Calendar.getInstance();
				coursesShort.create_date = calendar.getTimeInMillis() / 1000L;
				// category	
				coursesShort.cat_id = entity.getCat_id();
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
			
				coursesShort.publish_date = calendar.getTimeInMillis() / 1000L;
				coursesShort.expired_date = 0;
				coursesShort.endvip_date = 0;
				coursesShort.time_show_vip = 0;
				coursesShort.status = 1;
				coursesShort.fee = 0;
				coursesShort.price =StringUtil.getAttribute("price", entity);  
				
				coursesShort.degree = getDegree((String) entity.getProperty("degree"));
				if(coursesShort.degree==0)
				coursesShort.degree_expand = StringUtil.getAttribute("degree", entity);  
				
				coursesShort.form_training = getFormTraining((String) entity.getProperty("form_training"));
				if(coursesShort.form_training==0)
				coursesShort.form_training_expand =StringUtil.getAttribute("form_training", entity); 
				
				coursesShort.string_time =StringUtil.getAttribute("string_time", entity);
				coursesShort.place =StringUtil.getAttribute("place", entity); 
				coursesShort.info_expand =StringUtil.getAttribute("info_expand", entity);
				
				coursesShort.admin_name ="";				
				coursesShort.admin_id = 0;
				
				coursesShort.contact_name = getAttribute("contact_name",entity); 
				coursesShort.contact_email =getAttribute("contact_email",entity);  
				coursesShort.contact_tel =getAttribute("contact_tel",entity);   
				coursesShort.contact_yahoo = getAttribute("contact_yahoo",entity);   
				coursesShort.contact_skype =getAttribute("contact_skype",entity);
				
				
				DownloadImage downloadImage = null;
				int id = tuyenSinh247DAO.saveCoursesLong(coursesShort);
				coursesShort.id = id;
			
				if (id > 0) {
					coursesLongCategory = new CoursesLongCategory();
					coursesLongCategory.cat_id = coursesShort.cat_id;
					coursesLongCategory.course_id = coursesShort.id;		
					if(coursesShort.id>0)
					tuyenSinh247DAO.saveCoursesLongCategory(coursesLongCategory);
					
					coursesLongCity = new CoursesLongCity();
					coursesLongCity.course_id =  coursesShort.id;
					coursesLongCity.create_date = coursesShort.create_date ;
					coursesLongCity.city_id = getCity((String) entity.getProperty("city_id"));
					coursesLongCity.district_id = getCity((String) entity.getProperty("district_id"));
					if(coursesLongCity.city_id> 0)
					tuyenSinh247DAO.saveCoursesLongCity(coursesLongCity);
				
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
							downloadImage.pre_folder = Constants.TUYENSINH_COURSES_LONG_PRE_FOLDER;
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
	
	
	public static void main(String args[]) {
		 CrawlerCoursesLong crawlerArticleByPage = new CrawlerCoursesLong();
		 /*String urls = args[0];
		 String bean = args[1];
		 int fetch = Integer.parseInt(args[2]);
		 int source = Integer.parseInt(args[3]);
		 int download = Integer.parseInt(args[4]);
		 int type_image = Integer.parseInt(args[7]);
		 int type_get_html = Integer.parseInt(args[8]);
		 int type_date = Integer.parseInt(args[9]);
		 String bean_trainer ="";
		 crawlerArticleByPage.initialization(urls,bean,bean_trainer,
				 fetch,source,download,type_image,type_get_html,type_date);
		 crawlerArticleByPage.collectionData();*/
		
		 crawlerArticleByPage.initialization("d:/urlsKhaigiangTS.xml","d:/beanKhaiGiangTS.xml","d:/beanKhaiGiangTrainerTS.xml", 6,16,1,0,1,0);
		 crawlerArticleByPage.collectionData();

	}

}
