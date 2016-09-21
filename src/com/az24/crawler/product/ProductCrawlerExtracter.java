package com.az24.crawler.product;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.ExtractEntity;
import hdc.crawler.AbstractCrawler.Datum;
import hdc.crawler.AbstractCrawler.Url;
import hdc.util.html.HttpURL;
import hdc.util.html.NodeDeleteVisitor;
import hdc.util.html.NodeNormalizeVisitor;
import hdc.util.html.parser.DomWriter;
import hdc.util.html.parser.XPathReader;
import hdc.util.io.FileUtil;
import hdc.util.lang.UriID;
import hdc.util.text.StringUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathConstants;

import jdbm.PrimaryHashMap;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.az24.crawler.config.BeanXmlConfig;
import com.az24.crawler.config.JdbmXmlConfig;
import com.az24.crawler.fiter.ContentFilter;
import com.az24.crawler.model.BeanConfig;
import com.az24.crawler.model.ImageConfig;
import com.az24.crawler.model.Property;
import com.az24.crawler.model.product.ProductField;
import com.az24.util.FileLog;
import com.az24.util.Logger;
import com.az24.util.UTF8Tool;

public class ProductCrawlerExtracter {
	private com.az24.util.Logger logger = new Logger(this.getClass().getName());
	
	
	protected RecordManager extracterManager;
	protected PrimaryHashMap<String, ExtractEntity> extractPrimary;
	protected RecordManager urlManager;
	protected PrimaryHashMap<String, Url> urlPrimary;
	protected RecordManager datumManager;
	protected PrimaryHashMap<String, Datum> datumPrimary;

	protected RecordManager urlStoreManager;
	protected PrimaryHashMap<String, Url> urlStorePrimary;
	protected RecordManager datumStoreManager;
	protected PrimaryHashMap<String, Datum> datumStorePrimary;

	protected RecordManager urlImageManager;
	protected PrimaryHashMap<String, ImageConfig> urlImagePrimary;

	private String filebeanconfig = "";
	private String filejdbmconfig = "";
	private int totalImage=0;
	public ProductCrawlerExtracter(String filebeanconfig,String filejdbmconfig) {
		this.filebeanconfig = filebeanconfig;
		this.filejdbmconfig = filejdbmconfig;
	}

	private void openData() throws IOException {
		JdbmXmlConfig.parseConfig(filejdbmconfig);
		FileUtil.deleteFile(JdbmXmlConfig.extract);
		datumManager = RecordManagerFactory.createRecordManager(JdbmXmlConfig.datum
				+ "/datum");
		datumPrimary = datumManager.hashMap("datum");

		datumStoreManager = RecordManagerFactory
				.createRecordManager(JdbmXmlConfig.datum_store + "/datum");
		datumStorePrimary = datumStoreManager.hashMap("datum");

		urlManager = RecordManagerFactory.createRecordManager(JdbmXmlConfig.url
				+ "/url");
		urlPrimary = urlManager.hashMap("url");

		urlStoreManager = RecordManagerFactory
				.createRecordManager(JdbmXmlConfig.url_store + "/url");
		urlStorePrimary = urlStoreManager.hashMap("url");

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
		urlStoreManager.commit();
		datumStoreManager.commit();
		extracterManager.commit();
		urlImageManager.commit();
	}

	private void closeData() throws IOException {
		urlManager.close();
		datumManager.close();
		urlStoreManager.close();
		urlImageManager.close();
		datumStoreManager.close();
		extracterManager.close();
	}
	
	private NodeDeleteVisitor deleteVisitor = new NodeDeleteVisitor() {
	    protected boolean shouldDelete(Node node) {
	      if(node.getNodeName().equalsIgnoreCase("meta")) return true ;
	      else if(node.getNodeName().equalsIgnoreCase("link")) return true ;
	      else if(node.getNodeName().equalsIgnoreCase("style")) return true ;
	      else if(node.getNodeName().equalsIgnoreCase("script")) return true ;
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
			DomWriter writer = new DomWriter();
			
			for (Property property : propeties) {
				if(property.getNode_type().equalsIgnoreCase("nodeset"))
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
					logger.log("---->Process Property:"+property.getName()+"="+ str);
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
					logger.log("---->Process Property:"+property.getName()+"="+ str);
				}

			}
		}}catch (Exception e) {
			e.printStackTrace();
		}
		return filter;	
	}

	private void processImage(XPathReader reader, List<ImageConfig> images,
			ExtractEntity entity,String url_content) throws Exception {
		logger.log("---->Process Images:");
		List<ImageConfig> listImage = new ArrayList<ImageConfig>();
		String id = "";String day="";
		Calendar calendar = Calendar.getInstance();	
		int month = calendar.get(Calendar.MONTH)+1;
		day = calendar.get(Calendar.DAY_OF_MONTH)<10?"0"+calendar.get(Calendar.DAY_OF_MONTH):""+calendar.get(Calendar.DAY_OF_MONTH);
		String strmonth = month<10?"0"+month:""+month;
		
		String dayPath= day	+ "/"	+ strmonth	+ "/"+ calendar.get(Calendar.YEAR);
		//String pre_folder="d:/data/picture_auto/";
		String pre_folder="/picture_model/";
		String name="";
		for (ImageConfig imageConfig : images) {
			NodeList nodes = (NodeList) reader.read(imageConfig.xpath,
					XPathConstants.NODESET);
			int node_one_many = nodes.getLength();
			
			int i = 1;
			id = "";
			while(i<=node_one_many)
			{
				String	src = (String) reader.read(imageConfig.xpath
						+ "[" + i + "]" + "/a[1]//@href",
						XPathConstants.STRING);
				id = new UriID(new HttpURL(src)).getIdAsString();				
				
				name = src.substring(src.lastIndexOf("/")+1);
				
				ImageConfig image  = new ImageConfig();
				image.id=id;
				image.name=name;
				image.src = src;
				image.src_big = src;
				image.src_medium=src;
				image.src_small = src;
				image.dateProcess = dayPath;		
				image.url_content = url_content;
				
				
				if(i==1&&!StringUtil.isEmpty(src)){
					entity.addProperty("picture", image.getFolder(pre_folder)+"small_"+name);
					image.is_main = 1;
				}
				
				urlImagePrimary.put(id, image);
				listImage.add(image);
				
				i++;
				totalImage++;
			}
			
			if(node_one_many>0)break;
		}
		if(listImage.size()==0)
		{
			String src = (String) reader.read("html/body[1]/div[@id='body']/div[@id='container_body']/div[@id='container_content']/div[1]/div[1]/div[1]/div[1]/table[1]/TBODY[1]/tr[1]/td[1]/a[1]//@href",
					XPathConstants.STRING);   
			
			if(src.lastIndexOf(".")>0) 
			{
			name = src.substring(src.lastIndexOf("/")+1);
			id = new UriID(new HttpURL(src)).getIdAsString();		
			
			ImageConfig image  = new ImageConfig();
			image.id=id;
			image.name=name;
			image.src = src;
			image.src_big = src;
			image.src_medium=src;
			image.src_small = src;
			image.dateProcess =dayPath;	
			image.url_content = url_content;
			
			if(!StringUtil.isEmpty(src)){
				entity.addProperty("picture", image.getFolder(pre_folder)+"small_"+name);
				image.is_main = 1;
			}
			
			urlImagePrimary.put(id, image);
			listImage.add(image);
			}
			
			totalImage++;
		}
		if(listImage.size()==0) 
		{
			System.out.println("Khong co hinh anh");
			FileLog.createFileLog(JdbmXmlConfig.file_log+"_log_"+calendar.get(Calendar.DAY_OF_MONTH)+calendar.get(Calendar.MONTH)+calendar.get(Calendar.YEAR)+"_"+entity.getCat_id()+"_noimages_.txt");
			String log=calendar.getTime().toString()+ "-->Url:"+entity.getUrl();	
			FileLog.writer(log);
		}
		entity.addProperty("images", listImage);
		urlImageManager.commit();
	}

	private void processField(XPathReader reader, List<BeanConfig> beans,
			ExtractEntity entity) throws Exception {
		logger.log("---->Process Product Data:");
		try
		{
			if (beans == null)	return;
			NodeList nodes = null;			
			int node_one_many = 0;
			List<ProductField> listField = null;
			ProductField field = null;
			DomWriter writer = new DomWriter();
			
			for (BeanConfig beanConfig : beans) {
				nodes = (NodeList) reader.read(beanConfig.getXpath(),
						XPathConstants.NODESET);
				node_one_many = nodes.getLength();
				listField = new ArrayList<ProductField>();
				String xProdata = beanConfig.getXpath();
				if (nodes != null && nodes.getLength() > 0) {
					int i = 1;
					
					if(node_one_many==0) System.out.println("Khong co thuoc tinh");
					
					while (i <= node_one_many) {
						field = new ProductField();
						Node	oresult = (Node) reader.read(xProdata
								+ "[" + i + "]" + "/td[1]",
								XPathConstants.NODE);
						// la group field
						
						if(oresult.getAttributes()!=null&&oresult.getAttributes().getNamedItem("colspan")!=null)
						{
							//System.out.println("-->Group Name="+oresult.getTextContent());
							field.name=oresult.getTextContent();
							field.is_group=1;
						}else{
							String str = (String) reader.read(xProdata
									+ "[" + i + "]" + "/td[1]/text()",
									XPathConstants.STRING);
							
							Node value = (Node) reader.read(xProdata
									+ "[" + i + "]" + "/td[2]",
									XPathConstants.NODE);
							
							 String strValue = writer.toXMLString(value);
							
							 strValue = strValue.substring(57).replaceAll("</td>","");
							 strValue = strValue.replaceAll("</a>","");
							 String pattern = "<a.*href=\".*\">";

						      // Create a Pattern object
						      Pattern r = Pattern.compile(pattern);
						      Matcher m = r.matcher(strValue);
						      strValue=m.replaceAll("").trim();
						      
						      field.name=str;
						      field.value= strValue;
						     
							 System.out.println("---------->Field Name="+str+"-----: Field value="+strValue);
						}
						listField.add(field);
						i++;
					}
				}
				logger.log("----------->Product Data="+beanConfig.getTable());
				if(listField.size()==0)
				{
					System.out.println("Khong co thuoc tinh");
					Calendar calendar = Calendar.getInstance();	
					FileLog.createFileLog(JdbmXmlConfig.file_log+"_log_"+calendar.get(Calendar.DAY_OF_MONTH)+calendar.get(Calendar.MONTH)+calendar.get(Calendar.YEAR)+"_"+entity.getCat_id()+"_nofields_.txt");
					String log=calendar.getTime().toString()+ "-->Url:"+entity.getUrl();
					FileLog.writer(log);
				}
				entity.addProperty("field", listField);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void run() {

		try {
			totalImage=0;
			logger.log("Extract Data:");
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
				if(reader==null) continue;
				url = new Url();
				url.id = datum.id;
				url.url = datum.url;
				url.cat_id = datum.cat_id;
				entity.setID(url.id);
				entity.setCat_id(datum.cat_id);
				entity.setUrl(datum.url);
				
				// process extract					
				if(processProperty(reader, propeties, entity)) continue;
				// process images
				String name = (String)entity.getProperty("name");
				name = name.replaceAll("- Thông số kỹ thuật", "");
				name = UTF8Tool.coDau2KoDau(name);
				name = name.trim().toLowerCase();
				
				String pattern = "\\W";
			      // Create a Pattern object
			    Pattern r = Pattern.compile(pattern);
			      // Now create matcher object.
			    Matcher m = r.matcher(name);
			    name = m.replaceAll("_");

			    processImage(reader, images, entity,datum.url);
				// process sub Bean
				processField(reader, beans, entity);

				// store url
				//urlStorePrimary.put(datum.id, url);
				// store datum
				//datumStorePrimary.put(datum.id, datum);
								
				extractPrimary.put(datum.id, entity);
				if (i % 10 == 0) {
					commitData();
				}
				System.out.println("Extract-->"+i + "=" + datum.url);
				i++;
			}
			
			 logger.log("Tong Data="+i);	
			 Calendar calendar = Calendar.getInstance();
			 String log=calendar.getTime().toString()+ "-->Tong Extract:"+i+"\n";
			 log += calendar.getTime().toString()+ "-->Tong Image:"+totalImage;
			 FileLog.createFileLog(JdbmXmlConfig.file_log+"_log_"+calendar.get(Calendar.DAY_OF_MONTH)+calendar.get(Calendar.MONTH)+calendar.get(Calendar.YEAR)+".txt");
			 FileLog.writer(log);
			 System.out.println("Tong so iamge="+totalImage);
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
		ProductCrawlerExtracter crawlerExtracter = new ProductCrawlerExtracter(
				"src/com/az24/crawler/config/beanProductVatGia.xml","src/com/az24/crawler/config/jdbm.xml");
		crawlerExtracter.run();
	}

}
