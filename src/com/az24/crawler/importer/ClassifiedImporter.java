package com.az24.crawler.importer;

import hdc.crawler.ExtractEntity;
import hdc.crawler.AbstractCrawler.Url;
import hdc.util.text.HtmlUtil;
import hdc.util.text.StringUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.az24.crawler.AbstractImporter;
import com.az24.crawler.config.BeanXmlConfig;
import com.az24.crawler.config.JdbmXmlConfig;
import com.az24.crawler.config.SourceXmlConfig;
import com.az24.crawler.model.Classified;
import com.az24.crawler.model.DatabaseConfig;
import com.az24.crawler.store.City;
import com.az24.dao.ClassifiedDAO;
import com.az24.dao.CrawlerLogDAO;
import com.az24.util.FileLog;
import com.az24.util.UTF8Tool;

public class ClassifiedImporter extends AbstractImporter {
	
	public String subcate="";	
	public ClassifiedImporter(String filebeanconfig,String filejdbmconfig) {
		super(filebeanconfig,filejdbmconfig);
	}

	protected void processAfterSave(int id, int cat_id) {

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
	
	public void run() {
		BeanXmlConfig beanXmlConfig = new BeanXmlConfig(this.filebeanconfig);
		beanXmlConfig.parseConfig();
		Random random = new Random();
		ExtractEntity entity = null;
		CrawlerLogDAO crawlerLogDAO = new CrawlerLogDAO();
		ClassifiedDAO classifiedDAO = new ClassifiedDAO();
		Pattern pattern  = null;
		Matcher matcher = null;
		List<City> citys = classifiedDAO.getCitis();
		List<City> city_quans = classifiedDAO.getCitisQuan();
		try {
			int i = 0;			
			openData();
			openConnection();
			Iterator<ExtractEntity> iterator = extractPrimary.values()
					.iterator();
			int cla_district = 0;
			Map<Integer, Integer> cat_holder = null;String arrDes[]=null;
			String short_content="";
			int count = 0;
			int empty=0;
			int log_id = 0;
			String content="";
			Classified classified = null;
			City city = null;
			while (iterator.hasNext()) {
				entity = iterator.next();	
				// Check Content Null
 				System.out.println(new Date()+" Imported Entity Name "+entity.getProperty("cla_name"));
				System.out.println(new Date()+" Imported Entity URL "+entity.getUrl());
				if(StringUtil.isEmpty((String) entity.getProperty("cla_name"))||StringUtil.isEmpty((String) entity.getProperty("cla_description")))
				{
					empty++;
					continue;
				}
				// Check Classified
				//if(classifiedDAO.getClassified((String)entity.getProperty("cla_name"))>0) continue;
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

					int	cla_id = classifiedDAO.saveClassified(classified);
					// Save content	
					// save description
					if (cla_id>0)
					{					
						int idTable = cla_id%1000;	
						classifiedDAO.saveClassifiedDesc(cla_id, content, idTable);
						
					}
					count++;	
					System.out.println(new Date()+" Imported Entity Thu "+count+" :"+ entity.getProperty("cla_name"));
				
					}									
					i++;
					
			}
			 System.out.println(new Date()+" Tong Data Imported="+(count));					
			 System.out.println(new Date()+" Tong Data Loi-->"+empty);
			 
			 Calendar calendar = Calendar.getInstance();
			 String log=new Date()+" Tong Data Imported="+(count)+"\r\n";	
			 log +=new Date()+" Tong Data Loi-->"+empty+"\r\n";	
			 FileLog.createFileLog(JdbmXmlConfig.file_log+"_log_"+calendar.get(Calendar.DAY_OF_MONTH)+calendar.get(Calendar.MONTH)+calendar.get(Calendar.YEAR)+".txt");
			 FileLog.writer(log);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				commitData();			
				closeData();
				conn.commit();				
				conn.close();				
				connLog.commit();				
				connLog.close();
			} catch (Exception e) {
				
			}
		}
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
	
	
	public static void main(String[] args) throws ClassNotFoundException {
		ClassifiedImporter crawlerExtracter = new ClassifiedImporter(
		"src/com/az24/crawler/config/beanQAYahoo.xml","src/com/az24/crawler/config/jdbm.xml");
		try {			 
			crawlerExtracter.subcate ="1069,";			
			System.out.println("thhis---->"+crawlerExtracter.subcate);
		}  catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
