package com.az24.crawler.article;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.ExtractEntity;
import hdc.crawler.AbstractCrawler.Datum;
import hdc.crawler.AbstractCrawler.Url;
import hdc.util.html.NodeDeleteVisitor;
import hdc.util.html.NodeNormalizeVisitor;
import hdc.util.html.ObjectVideo;
import hdc.util.html.SelectImgVisitor;
import hdc.util.html.SelectOjbectVisitor;
import hdc.util.html.parser.DomWriter;
import hdc.util.html.parser.XPathReader;
import hdc.util.io.FileUtil;
import hdc.util.text.HtmlUtil;
import hdc.util.text.StringUtil;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathConstants;

import jdbm.PrimaryHashMap;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.az24.crawler.config.BeanXmlConfig;
import com.az24.crawler.config.JdbmXmlConfig;
import com.az24.crawler.config.UrlInjectXmlConfig;
import com.az24.crawler.fiter.ContentFilter;
import com.az24.crawler.model.ImageConfig;
import com.az24.crawler.model.Property;
import com.az24.util.FileLog;
import com.az24.util.Logger;

public class ArticleCrawlerExtracter implements Runnable {
	
	private com.az24.util.Logger logger = new Logger(this.getClass().getName());
	
	
	protected RecordManager extracterManager;
	protected PrimaryHashMap<String, ExtractEntity> extractPrimary;
	protected RecordManager urlManager;
	protected PrimaryHashMap<String, Url> urlPrimary;
	protected RecordManager datumManager;
	protected PrimaryHashMap<String, Datum> datumPrimary;
	
	
	protected RecordManager urlImageManager;
	protected PrimaryHashMap<String, ImageConfig> urlImagePrimary;

	private String filebeanconfig = "";
	private String filejdbmconfig = "";
	private String fileUrlConfig="";
	
	private List<hdc.crawler.Node> nodesDel = null;
	

	public ArticleCrawlerExtracter(String filebeanconfig,String filejdbmconfig,String fileUrlConfig) {
		this.filebeanconfig = filebeanconfig;
		this.filejdbmconfig = filejdbmconfig;
		this.fileUrlConfig = fileUrlConfig;
	}

	private void openData() throws IOException {
		JdbmXmlConfig.parseConfig(filejdbmconfig);
		FileUtil.deleteFile(JdbmXmlConfig.extract);
		datumManager = RecordManagerFactory.createRecordManager(JdbmXmlConfig.datum
				+ "/datum");
		datumPrimary = datumManager.hashMap("datum");

		urlManager = RecordManagerFactory.createRecordManager(JdbmXmlConfig.url
				+ "/url");
		urlPrimary = urlManager.hashMap("url");

	
		extracterManager = RecordManagerFactory.createRecordManager(JdbmXmlConfig.extract
				+ "/extract");
		extractPrimary = extracterManager.hashMap("extract");

		urlImageManager = RecordManagerFactory.createRecordManager(JdbmXmlConfig.url_image
				+ "/image");
		urlImagePrimary = urlImageManager.hashMap("image");
	}

	private void commitData() throws IOException {
		urlManager.commit();
		datumManager.commit();		
		extracterManager.commit();
		urlImageManager.commit();
	}

	private void closeData() throws IOException {
		urlManager.close();
		datumManager.close();		
		urlImageManager.close();		
		extracterManager.close();
	}
	
	private NodeDeleteVisitor deleteVisitor = new NodeDeleteVisitor() {
	    protected boolean shouldDelete(Node node) {
	      if(node.getNodeName().equalsIgnoreCase("meta")) return true ;
	      else if(node.getNodeName().equalsIgnoreCase("link")) return true ;
	      else if(node.getNodeName().equalsIgnoreCase("style")) return true ;
	      else if(node.getNodeName().equalsIgnoreCase("script")) return true ;
	      else if(node.getNodeName().equalsIgnoreCase("iframe")) return true ;
	      
	      if(nodesDel!=null)
	      {
	    	  int i =0;
	    	  while(i<nodesDel.size())
	    	  {
	    		 if(nodesDel.get(i).name.equalsIgnoreCase(node.getNodeName()))
	    		 {
	    			 int j = 0;
	    			 while(j<node.getAttributes().getLength())
	    			 {
	    				 if(node.getAttributes().item(j).getTextContent().equalsIgnoreCase(nodesDel.get(i).attribue))
	    					 return true;
	    				 	 
	    				 j++;
	    			 }
	    		 }
	    		  i++;
	    	  }
	      }
	      return false ;
	    }
	  } ;
	  
	  private NodeNormalizeVisitor normalVisitor = new NodeNormalizeVisitor() {
	    protected void normalize(Node node) {
	      if(node!=null&&node.hasAttributes()) {
	        NamedNodeMap attributes = node.getAttributes();
	        if(attributes.getNamedItem("class") != null) attributes.removeNamedItem("class");
	        if(attributes.getNamedItem("id") != null&&!"object".equalsIgnoreCase(node.getNodeName())) attributes.removeNamedItem("id");
	        if(attributes.getNamedItem("style") != null) attributes.removeNamedItem("style");
	        if(attributes.getNamedItem("height") != null&&(!"object".equalsIgnoreCase(node.getNodeName())||!"embed".equalsIgnoreCase(node.getNodeName()))) attributes.removeNamedItem("height");
	        if(attributes.getNamedItem("width") != null&&(!"object".equalsIgnoreCase(node.getNodeName())||!"embed".equalsIgnoreCase(node.getNodeName()))) attributes.removeNamedItem("width");
	        if(attributes.getNamedItem("onclick") != null) attributes.removeNamedItem("onclick");
	        if(attributes.getNamedItem("title") != null) attributes.removeNamedItem("title");
	        if(attributes.getNamedItem("rel") != null) attributes.removeNamedItem("rel") ;
	        if(attributes.getNamedItem("target") != null) attributes.removeNamedItem("target") ;
	        String tag = node.getNodeName();
	        if (node instanceof Element && "a".equalsIgnoreCase(tag)) {
				Element a = (Element) node;
				a.setAttribute("rel", "nofollow");
				//a.setAttribute("href", "#");
	        }
	      } 
	      
	    }
	  } ;
	  
	private boolean processProperty(XPathReader reader, List<Property> propeties,
			ExtractEntity entity) {
		logger.log("---->Process Property:");
		String str="";
		boolean filter = false;
		
		
		try{
		if (propeties != null) {
			NodeList nodes = null;
			DomWriter writer = new DomWriter();
			
			for (Property property : propeties) {
				if(property.getNode_type().equalsIgnoreCase("nodeset"))
				{
					if(StringUtil.isEmpty(property.getXpath_sub()))
					{
					String xpath[] = property.getXpath().split(";");
					for (String string : xpath) {
						if(!StringUtil.isEmpty(string))
						{
						nodes = (NodeList) reader.read(string,
								XPathConstants.NODESET);
						if(nodes.item(0)!=null) break;
						}
					}
					if(nodes==null) continue;
					this.nodesDel = property.getNodedels();
					if(!property.getName().equalsIgnoreCase("title"))
					{
					deleteVisitor.traverse(nodes.item(0));
					normalVisitor.traverse(nodes.item(0));
					}
					if("1".equalsIgnoreCase(property.getChangeLink()))
					{
						String title =  HtmlUtil.toText((String) entity.getProperty("title"));						
						String pattern = "<.*xml.*>";				   
					    Pattern r = Pattern.compile(pattern);
						Matcher m = r.matcher(title);
						title = m.replaceAll("");
						    
						SelectImgVisitor imgVisitor = new SelectImgVisitor("http://images.tinhhinh.net/picture/article/",title.trim());
						imgVisitor.date = entity.date;
						imgVisitor.traverse(nodes.item(0));
						
					}
					str = writer.toXMLString(nodes
							.item(0));
					if("1".equalsIgnoreCase(property.getFilter()))
						filter = ContentFilter.filter(str);
					if(filter) return true;
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
					//String pattern = "<a href=\"#\".*/>";				   
				    //Pattern r = Pattern.compile(pattern);
					//Matcher m = r.matcher(str);
					//str = m.replaceAll(" ");					
					entity.addProperty(property.getName(), str);					
					//logger.log("---->Process Property:"+property.getName()+"="+ str);
					}else
					{
						nodes = (NodeList) reader.read(property.getXpath(),
								XPathConstants.NODESET);
						int node_one_many = nodes.getLength();
						int j = 1;
						str="";
						while (j <= node_one_many) {
							String result = (String) reader.read(property.getXpath()+"["+j+"]"+property.getXpath_sub(),
									XPathConstants.STRING);
							str += result.trim()+",";
							j++;
						}
						
						//String pattern = "<a href=\"#\".*/>";				   
					    //Pattern r = Pattern.compile(pattern);
						//Matcher m = r.matcher(str);
						//str = m.replaceAll("");						
						entity.addProperty(property.getName(), str);
						//logger.log("---->Process Property:"+property.getName()+"="+ str);
					}
				}
				else
				{
					if(property.getNode_type().equalsIgnoreCase("string"))
						str = (String) reader.read(property.getXpath(),
								XPathConstants.STRING);	
					if("1".equalsIgnoreCase(property.getFilter()))
						filter = ContentFilter.filter(str);
					if(filter) return true;	
					
					if("1".equalsIgnoreCase(property.getChangeLink()))
						str = ContentFilter.changeLink(str);
					
					entity.addProperty(property.getName(), str.trim());
					//logger.log("---->Process Property:"+property.getName()+"="+ str);
				}

			}
		}}catch (Exception e) {
			e.printStackTrace();
		}
		return filter;	
	}


	public void run() {

		try {
			logger.log("Extract Data:");
			openData();
			Iterator<Datum> iterator = datumPrimary.values().iterator();
			Datum datum = null;
			Url url = null;
			int i = 0;
			UrlInjectXmlConfig urlInjectXmlConfig = new UrlInjectXmlConfig(
					this.fileUrlConfig);
			urlInjectXmlConfig.parseConfig();
			BeanXmlConfig beanXmlConfig = new BeanXmlConfig(this.filebeanconfig);
			beanXmlConfig.parseConfig();
			ExtractEntity entity = null;
			XPathReader reader;
			List<Property> propeties = beanXmlConfig.bean.getProperties();
			
			ContentFilter.fileJdbmConfig=filejdbmconfig;
			try {			
				ContentFilter.initData();
			} catch (IOException e1) {			
				e1.printStackTrace();
			}
			
			while (iterator.hasNext()) {
				
				entity = new ExtractEntity();
				datum = iterator.next();
				reader = CrawlerUtil.createXPathReaderByData(datum.data);
				
				System.out.println(new Date() +": Extract-->"+i + "=" + datum.url);
				
				url = new Url();
				url.id = datum.id;
				url.url = datum.url;
				url.cat_id = datum.cat_id;
				entity.setID(url.id);
				entity.setCat_id(datum.cat_id);
				entity.setUrl(datum.url);	
				
				entity.date = datum.date;
				// process extract					
				if(processProperty(reader, propeties, entity)) continue;
				reader = CrawlerUtil.createXPathReaderByData(datum.data);
				if(StringUtil.isEmpty((String)entity.getProperty("create_date")))
				{
					//2000/02/01
					try{
					Pattern r = Pattern.compile("(\\d+/\\d+/\\d+)");
					Matcher m = r.matcher((String)entity.getProperty("create_date"));
					if(m.find())
					{
						entity.date=m.group(1).split("/")[2]+"/"+m.group(1).split("/")[1]+"/"+m.group(1).split("/")[0];
					}}catch (Exception e) {
						entity.date = datum.date;
					}
				}
					
				// process images				
				//processImage(reader, images, entity);
				// get avatar image				
				ImageConfig imageConfig = urlImagePrimary.get(datum.id);				
				if(imageConfig!=null) 
				{
					String fileImage=imageConfig.name.substring(6, imageConfig.name.length());
					entity.addProperty("picture", fileImage);
					System.out.println(imageConfig.name);
				}
				// process sub Bean
				//processSubBean(reader, beans, entity);								
				extractPrimary.put(datum.id, entity);
				if (i % 10 == 0) {
					commitData();
				}
				
				i++;
			}
			 logger.log("Tong Data="+i);	
			 Calendar calendar = Calendar.getInstance();
			 String log=calendar.getTime().toString()+ "-->Tong Extract:"+i;			
			 FileLog.createFileLog(JdbmXmlConfig.file_log+"_log_"+calendar.get(Calendar.DAY_OF_MONTH)+calendar.get(Calendar.MONTH)+calendar.get(Calendar.YEAR)+".txt");
			 FileLog.writer(log);
			 commitData();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {			
			logger.log(e.toString());
		}finally
		{
			try {
				commitData();
				closeData();
			} catch (Exception e) {
				
			}
		}

	}

	public static void main(String[] args) {
		ArticleCrawlerExtracter crawlerExtracter = new ArticleCrawlerExtracter(
				"src/com/az24/crawler/config/beanIone.xml","src/com/az24/crawler/config/jdbm.xml","src/com/az24/crawler/config/urlsKenh14.xml");
		crawlerExtracter.run();
	}

}
