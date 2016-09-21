package com.az24.crawler;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.ExtractEntity;
import hdc.crawler.AbstractCrawler.Datum;
import hdc.crawler.AbstractCrawler.Url;
import hdc.util.html.HttpURL;
import hdc.util.html.NodeDeleteVisitor;
import hdc.util.html.NodeNormalizeVisitor;
import hdc.util.html.NodeVisitor;
import hdc.util.html.parser.DomWriter;
import hdc.util.html.parser.XPathReader;
import hdc.util.io.FileUtil;
import hdc.util.lang.UriID;
import hdc.util.text.HtmlUtil;
import hdc.util.text.StringUtil;

import java.io.IOException;
import java.util.ArrayList;
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
import com.az24.crawler.fiter.ContentFilter;
import com.az24.crawler.model.BeanConfig;
import com.az24.crawler.model.ImageConfig;
import com.az24.crawler.model.Property;
import com.az24.util.FileLog;
import com.az24.util.Logger;

public class CrawlerExtracter implements Runnable {
	
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

	public CrawlerExtracter(String filebeanconfig,String filejdbmconfig) {
		this.filebeanconfig = filebeanconfig;
		this.filejdbmconfig = filejdbmconfig;
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
	
	
	
	private List<hdc.crawler.Node> nodesDel = null;
	
	private NodeDeleteVisitor deleteVisitor = new NodeDeleteVisitor() {
	    protected boolean shouldDelete(Node node) {
	      if(node.getNodeName().equalsIgnoreCase("meta")) return true ;
	      else if(node.getNodeName().equalsIgnoreCase("link")) return true ;
	      else if(node.getNodeName().equalsIgnoreCase("style")) return true ;
	      else if(node.getNodeName().equalsIgnoreCase("script")) return true ;
	      
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
	        if(attributes.getNamedItem("id") != null) attributes.removeNamedItem("id");
	        if(attributes.getNamedItem("style") != null) attributes.removeNamedItem("style");
	        if(attributes.getNamedItem("height") != null) attributes.removeNamedItem("height");
	        if(attributes.getNamedItem("width") != null) attributes.removeNamedItem("width");
	        if(attributes.getNamedItem("onclick") != null) attributes.removeNamedItem("onclick");
	        if(attributes.getNamedItem("title") != null) attributes.removeNamedItem("title");
	        if(attributes.getNamedItem("rel") != null) attributes.removeNamedItem("rel") ;
	        if(attributes.getNamedItem("target") != null) attributes.removeNamedItem("target") ;
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
			Node node = null;
			DomWriter writer = new DomWriter();
			
			for (Property property : propeties) {
				if(property.getNode_type().equalsIgnoreCase("nodeset"))
				{
					
					// Phai Xoa Node O Cuoi Cua Process
					List<hdc.crawler.Node> nodeDelByXpath = property.getNodedelByXpaths();
					if(nodeDelByXpath!=null)
					{
						for (hdc.crawler.Node node2 : nodeDelByXpath) {
							System.out.println(node2.xpath);
							CrawlerUtil.removeNodeByXpath(reader, node2.xpath);
						}
					}
					
					String xpath[] = property.getXpath().split(";");
					for (String string : xpath) {
						if(!StringUtil.isEmpty(string))
						{
						nodes = (NodeList) reader.read(string,
								XPathConstants.NODESET);
						if(nodes.item(0)!=null) break;
						}
					}
					
					this.nodesDel = property.getNodedels();
					if(nodes==null) continue;
					deleteVisitor.traverse(nodes.item(0));
					normalVisitor.traverse(nodes.item(0));
					str = writer.toXMLString(nodes
							.item(0));
					
					if("1".equalsIgnoreCase(property.getFilter()))
						filter = ContentFilter.filter(str);
					if(filter) return true;
					
					if("1".equalsIgnoreCase(property.getChangeLink()))
					{
						
						str = ContentFilter.changeLink(str);
						
					}
					
					entity.addProperty(property.getName(), str);
					//logger.log("---->Process Property:"+property.getName()+"="+ str);
				}  
				else 
				{
					if(property.getNode_type().equalsIgnoreCase("node"))
					{
						node = (Node)  reader.read(property.getXpath(),
								XPathConstants.NODE);
						str = HtmlUtil.removeTag(node.getTextContent());
					}
					if(property.getNode_type().equalsIgnoreCase("string"))
					{
						str = (String) reader.read(property.getXpath(),
								XPathConstants.STRING);
					
					}
					
					if("1".equalsIgnoreCase(property.getFilter()))
						filter = ContentFilter.filter(str);
					if(filter) return true;	
					
					if("1".equalsIgnoreCase(property.getChangeLink()))
						str = ContentFilter.changeLink(str);
					
					entity.addProperty(property.getName(), str.trim());
					logger.log("---->Process Property:"+property.getName()+"="+ str);
				}

			}
		}}catch (Exception e) {
			e.printStackTrace();
		}
		return filter;	
	}

	private void processImage(XPathReader reader, List<ImageConfig> images,
			ExtractEntity entity) throws Exception {
		logger.log("---->Process Images:");
		try
		{
		if (images == null)
			return;
		Calendar calendar = Calendar.getInstance();
		NodeList nodes = null;
		Pattern pattern = null;
		String id = "";String month="";String day="";String fileImage="";String src = "";
		for (ImageConfig imageConfig : images) {
			final List<Node> rowHolder = new ArrayList<Node>();
			if("nodeset".equalsIgnoreCase(imageConfig.node_type))
			{
				nodes = (NodeList) reader.read(imageConfig.xpath,
						XPathConstants.NODESET);
				pattern = Pattern.compile(imageConfig.regex);
				// process node images
				
				NodeVisitor visitor = new NodeVisitor() {
					public void preTraverse(Node node) {
						String tag = node.getNodeName();
						if (node instanceof Element && "a".equalsIgnoreCase(tag)) {
							rowHolder.add(node);
						}
					}
	
					public void postTraverse(Node node) {
					}
				};
				if(nodes==null) continue;
				visitor.traverse(nodes.item(0));
	
				Node node = null;
				id = "";
				src = ""; fileImage="";
				Matcher matcher = null; month=""; day="";
				if(rowHolder!=null)
				for (int k = 0; k < rowHolder.size(); k++) {
					node = rowHolder.get(k);
					src = node.getAttributes().getNamedItem("src") != null ? node
							.getAttributes().getNamedItem("src").getTextContent()
							: node.getAttributes().getNamedItem("href")
									.getTextContent();
					matcher = pattern.matcher(src);
					if (matcher.find()) {
						id = new UriID(new HttpURL(src)).getIdAsString();
						imageConfig.id=id;
						imageConfig.src = src;
						
						day = calendar.get(Calendar.DAY_OF_MONTH)<10?"0"+calendar.get(Calendar.DAY_OF_MONTH):""+calendar.get(Calendar.DAY_OF_MONTH);
						month = calendar.get(Calendar.MONTH)<10?"0"+calendar.get(Calendar.MONTH):""+calendar.get(Calendar.MONTH);
						imageConfig.dateProcess = day	+ "/"	+ month	+ "/"+ calendar.get(Calendar.YEAR);
						urlImagePrimary.put(id, imageConfig);
						fileImage= "/picture_auto/"+calendar.get(Calendar.YEAR)+"/thumb/"+month+day+"/"+src.substring(src.lastIndexOf("/")+1);
						if (!StringUtil.isEmpty(imageConfig.column))
							entity.addProperty(imageConfig.column, fileImage);
					}
					if (k % 30 == 0)
						urlImageManager.commit();
				}
				logger.log("----------->Property="+imageConfig.column+" Tong So Image="+rowHolder.size());
			}else
			{
				src = (String) reader.read(imageConfig.xpath,
						XPathConstants.STRING);
				if(src.indexOf("background")>-1) src = src.substring(src.indexOf("http"),src.indexOf("')"));
				id = new UriID(new HttpURL(src)).getIdAsString();
				if(!StringUtil.isEmpty(src))
				{
					imageConfig.id=id;
					imageConfig.src = src;
					
					day = calendar.get(Calendar.DAY_OF_MONTH)<10?"0"+calendar.get(Calendar.DAY_OF_MONTH):""+calendar.get(Calendar.DAY_OF_MONTH);
					month = calendar.get(Calendar.MONTH)<10?"0"+calendar.get(Calendar.MONTH):""+calendar.get(Calendar.MONTH);
					imageConfig.dateProcess = day	+ "/"	+ month	+ "/"+ calendar.get(Calendar.YEAR);
					
					urlImagePrimary.put(id, imageConfig);
					
					fileImage= "/picture_auto/"+calendar.get(Calendar.YEAR)+"/thumb/"+month+day+"/"+src.substring(src.lastIndexOf("/")+1);
					if (!StringUtil.isEmpty(imageConfig.column))
						entity.addProperty(imageConfig.column, fileImage);
					logger.log("----------->Property="+imageConfig.column+" Tong So Image=1");
				}
			}
			System.out.println(src);
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
		urlImageManager.commit();
	}

	private void processSubBean(XPathReader reader, List<BeanConfig> beans,
			ExtractEntity entity) throws Exception {
		logger.log("---->Process Subbean:");
		try
		{
			if (beans == null)
				return;
			NodeList nodes = null;
			String result = "";
			boolean filter =false;
			int node_one_many = 0;
			List<ExtractEntity> listExtractEntity = null;
			ExtractEntity subentity = null;
			DomWriter writer = new DomWriter();
			for (BeanConfig beanConfig : beans) {
				nodes = (NodeList) reader.read(beanConfig.getXpath(),
						XPathConstants.NODESET);
				node_one_many = nodes.getLength();
				listExtractEntity = new ArrayList<ExtractEntity>();
	
				if (nodes != null && nodes.getLength() > 0) {
					int j = 1;
					while (j <= node_one_many) {
						subentity = new ExtractEntity();
						if (beanConfig.getProperties() != null
								&& beanConfig.getProperties().size() > 0) {
							for (Property property : beanConfig.getProperties()) {
								if(property.getNode_type().equalsIgnoreCase("nodeset"))
								{
									NodeList	oresult = (NodeList) reader.read(beanConfig.getXpath()
											+ "[" + j + "]" + property.getXpath(),
											XPathConstants.NODESET);
								   
								    deleteVisitor.traverse(nodes.item(0));
									normalVisitor.traverse(nodes.item(0));
									String str = writer.toXMLString(oresult
											.item(0));
									if("1".equalsIgnoreCase(property.getFilter()))
										filter = ContentFilter.filter(str);
									if(filter) continue ;
									
									if("1".equalsIgnoreCase(property.getChangeLink()))
									{
										
										str = ContentFilter.changeLink(str);
										
									}
									
									subentity.addProperty(property.getName(), str);
								
								}else
								{
									result = (String) reader.read(beanConfig.getXpath()
											+ "[" + j + "]" + property.getXpath(),
											XPathConstants.STRING);
									subentity.addProperty(property.getName(), result);
									System.out.println("answer="+result);
								}
							}
						}
						listExtractEntity.add(subentity);
						j++;
					}
				}
				logger.log("----------->SunBean="+beanConfig.getTable());
				entity.addProperty(beanConfig.getTable(), listExtractEntity);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void run() {

		try {
			
			openData();
			Iterator<Datum> iterator = datumPrimary.values().iterator();
			Datum datum = null;
			Url url = null;
			int i = 0;

			BeanXmlConfig beanXmlConfig = new BeanXmlConfig(this.filebeanconfig);
			beanXmlConfig.parseConfig();
			ExtractEntity entity = null;
			XPathReader reader;
			List<Property> propeties = beanXmlConfig.bean.getProperties();
			List<BeanConfig> beans = beanXmlConfig.bean.getBeans();
			List<ImageConfig> images = beanXmlConfig.bean.getImages();
			
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
				url = new Url();
				url.id = datum.id;
				url.url = datum.url;
				url.cat_id = datum.cat_id;
				entity.setID(url.id);
				entity.setCat_id(datum.cat_id);
				entity.setUrl(datum.url);
				System.out.println(new Date()+" Extract Link Thu:"+i + " " + datum.url);
				// process images before process property did removed attribute node 
				processImage(reader, images, entity);
				// process extract					
				if(processProperty(reader, propeties, entity)) continue;				
				// process sub Bean
				processSubBean(reader, beans, entity);
								
				extractPrimary.put(datum.id, entity);
				if (i % 10 == 0) {
					commitData();
				}
				
				i++;
			}
			 logger.log("Tong Data="+i);	
			 Calendar calendar = Calendar.getInstance();
			 String log=calendar.getTime().toString()+ " Tong Extract:"+i;			
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
		CrawlerExtracter crawlerExtracter = new CrawlerExtracter(
				"src/com/az24/crawler/config/bean24h.xml","src/com/az24/crawler/config/jdbm.xml");
		crawlerExtracter.run();
	}

}
