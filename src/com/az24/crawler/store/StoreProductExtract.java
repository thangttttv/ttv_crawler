package com.az24.crawler.store;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.DocumentAnalyzer;
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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathConstants;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.http.HttpResponse;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.az24.crawler.config.BeanXmlConfig;
import com.az24.crawler.fiter.ContentFilter;
import com.az24.crawler.model.BeanConfig;
import com.az24.crawler.model.ImageConfig;
import com.az24.crawler.model.Property;
import com.az24.crawler.model.product.Product;
import com.az24.crawler.model.product.ProductData;
import com.az24.crawler.model.product.ProductField;
import com.az24.crawler.model.product.ProductFieldValue;
import com.az24.crawler.model.product.ProductImage;
import com.az24.crawler.product.ProductDownloadImage;
import com.az24.dao.CategoryDAO;
import com.az24.dao.ImporterDAO;
import com.az24.dao.ProductDAO;
import com.az24.dao.ProductStoreCatDAO;
import com.az24.dao.ProductStoreDAO;
import com.az24.dao.StoreCagegoryDAO;
import com.az24.dao.StoreDAO;
import com.az24.test.HttpClientUtil;
import com.az24.util.Logger;
import com.az24.util.UTF8Tool;

public class StoreProductExtract {
	
	private com.az24.util.Logger logger = new Logger(this.getClass().getName());
	private Product product = null;
	private ProductStore productStore = null;
	public String urlDomain ="";

	
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
	        if(node.getNodeName().equalsIgnoreCase("img")&&attributes.getNamedItem("src") != null) 
	        {
	        	Pattern pattern =  Pattern.compile("http://|www");
	        	Matcher matcher = pattern.matcher(node.getAttributes().getNamedItem("src").getTextContent());
	        	if(!matcher.find())
	        	node.getAttributes().getNamedItem("src").setTextContent(urlDomain+node.getAttributes().getNamedItem("src").getTextContent());
	        }
	      } 
	    }
	  } ;
	  
	private void setProductField(String value,Property property)
	{
		if("name".equalsIgnoreCase(property.getName()))
		{
			product.name = value;
			String name_pattern = "\\W";
			Pattern pattern = Pattern.compile(name_pattern);
			String name = UTF8Tool.coDau2KoDau(product.name).trim();
			Matcher matcher = pattern.matcher(name);
			String url_rewrite = matcher.replaceAll("-");
			product.url_rewrite = url_rewrite;
		}
		
		if("description".equalsIgnoreCase(property.getName()))
		{
		
			if (value != null
					&& value.length() > "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
							.length()) {
				value = value
						.substring("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
								.length());
				if(value.length()>60530) value = HtmlUtil.removeTag(value);
			}
			
			int length = value.length()>60530?60530:value.length();
			product.description = value.substring(0,length);
			
		}
		
		if("price".equalsIgnoreCase(property.getName())&&!StringUtil.isEmpty(value))
		{
			 
			if (value != null
					&& value.length() > "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
							.length()) {
				value = value
						.substring("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
								.length());
			}
			
			String name_pattern = "\\D";
			Pattern pattern = Pattern.compile(name_pattern);
			Matcher matcher = pattern.matcher(value);
			value = matcher.replaceAll("");
			try{
				product.price_from = Double.parseDouble(value);
			}catch (Exception e) {
				product.price_from = 0;
			}
		}
		
		
	}
	
	private void setProductStoreField(String value,Property property)
	{
		
		if("price".equalsIgnoreCase(property.getName())&&!StringUtil.isEmpty(value))
		{
			String name_pattern = "\\D";
			if (value != null
					&& value.length() > "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
							.length()) {
				value = value
						.substring("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
								.length());
			}
			Pattern pattern = Pattern.compile(name_pattern);
			Matcher matcher = pattern.matcher(value);
			value = matcher.replaceAll("");
			try{
			productStore.price = Double.parseDouble(value);
			}catch (Exception e) {
				productStore.price  = 0;
			}
			productStore.price_type = 1;
			productStore.vat = 0;
		}
		
		if("quantity".equalsIgnoreCase(property.getName())&&!StringUtil.isEmpty(value))
		{
			String name_pattern = "\\D";
			Pattern pattern = Pattern.compile(name_pattern);
			Matcher matcher = pattern.matcher(value);
			value = matcher.replaceAll("");
			try{
			productStore.quantity = Integer.parseInt(value);
			}catch (Exception e) {
				productStore.quantity = 0;
			}
		}
		
		if("quanlity".equalsIgnoreCase(property.getName()))
		{
			String name_pattern = "\\Wmoi\\W ";
			Pattern pattern = Pattern.compile(name_pattern);
			Matcher matcher = pattern.matcher(UTF8Tool.coDau2KoDau(value).trim());
			
			if (matcher.find()) {
				productStore.is_new = 1;
				productStore.quanlity = 1;
			} else {
				productStore.is_new = 0;
				productStore.quanlity = 0;
			}
		}
		
		if("transport".equalsIgnoreCase(property.getName()))
		{
			if ("lien he".equalsIgnoreCase(UTF8Tool
					.coDau2KoDau(value)))
				productStore.ship_type = 4;
			if ("co dinh".equalsIgnoreCase(UTF8Tool
					.coDau2KoDau(value)))
				productStore.ship_type = 3;
			if ("mien phi noi thanh".equalsIgnoreCase(UTF8Tool
					.coDau2KoDau(value)))
				productStore.ship_type = 1;
			if ("mien phi".equalsIgnoreCase(UTF8Tool
					.coDau2KoDau(value)))
				productStore.ship_type = 1;
		}
		
		if("madein".equalsIgnoreCase(property.getName()))
		{
			if ("chinh hang".equalsIgnoreCase(UTF8Tool
					.coDau2KoDau(value)))
				productStore.origin = 1;
			if ("xach tay".equalsIgnoreCase(UTF8Tool
					.coDau2KoDau(value)))
				productStore.origin = 2;
			if ("hang cong ty".equalsIgnoreCase(UTF8Tool
					.coDau2KoDau(value)))
				productStore.origin = 3;
		}
		
		if("website".equalsIgnoreCase(property.getName()))
		{
			productStore.url_link_web = value;
		}
		
		if("description".equalsIgnoreCase(property.getName()))
		{
			if (value != null
					&& value.length() > "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
							.length()) {
				value = value
						.substring("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
								.length());
				if(value.length()>60530) value = HtmlUtil.removeTag(value);
			}
			
			int length = value.length()>60530?60530:value.length();
			productStore.description = value.substring(0,length);
		}
		
	}
	
	private boolean processProperty(XPathReader reader,String url, List<Property> propeties,int store_id,String store_alias,
			int is_multi, int cat_az_id,int store_cat_id_1, int store_cat_id_2,int vat,Map<Integer, Integer> holder ) {
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
						String xpath = property.getXpath();
						if(!StringUtil.isEmpty(xpath))
							{
							nodes = (NodeList) reader.read(xpath,
									XPathConstants.NODESET);
							//if(nodes.item(0)!=null) break;
						}
						
						if(nodes==null) continue;
						deleteVisitor.traverse(nodes.item(0));
						normalVisitor.traverse(nodes.item(0));
						str = writer.toXMLString(nodes.item(0));
						
					/*	if("1".equalsIgnoreCase(property.getFilter()))
							filter = ContentFilter.filter(str);
						if(filter) return true;*/
						
						/*if("1".equalsIgnoreCase(property.getChangeLink()))
						{
							
							str = ContentFilter.changeLink(str);
							
						}*/
						logger.log("---->Process Property:"+property.getName()+"="+ str);
					} else if(property.getNode_type().equalsIgnoreCase("node"))
					{
						String xpath = property.getXpath();
						if(!StringUtil.isEmpty(xpath))
						{
							Node node = (Node) reader.read(xpath,
								 XPathConstants.NODE);
	
							str = writer.toXMLString(node);
						}
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
					
						logger.log("---->Process Property:"+property.getName()+"="+ str);
					}
					
					setProductField(str, property);
					setProductStoreField(str,property);
				
	
				 }
				
				product.status = 1;
				product.is_multi = is_multi;
				product.cat_id = cat_az_id;
				product.original_link = url;
				
				if (holder.containsKey(1))
					product.cat_id1 = holder.get(1);
				else
					product.cat_id1 = 0;
				if (holder.containsKey(2))
					product.cat_id2 = holder.get(2);
				else
					product.cat_id2 = 0;
				if (holder.containsKey(3))
					product.cat_id3 = holder.get(3);
				else
					product.cat_id3 = 0;
				if (holder.containsKey(4))
					product.cat_id4 = holder.get(4);
				else
					product.cat_id4 = 0;
				if (holder.containsKey(5))
					product.cat_id5 = holder.get(5);
				else
					product.cat_id5 = 0;
				productStore.vat = vat;
				
				Calendar calendar = Calendar.getInstance();
				productStore.create_date = calendar.getTimeInMillis() / 1000;
				productStore.modify_date = calendar.getTimeInMillis() / 1000;
				productStore.create_user = "auto";
				productStore.modify_user = "auto";
				productStore.price_type = 1;
				productStore.status = 1;
				productStore.store_id = store_id;
				productStore.store_alias = store_alias;
			 }
			}catch (Exception e) {
				e.printStackTrace();
			}
		return filter;	
	}
	
	
	private List<ProductField>  processFieldType_TableLabelValue(XPathReader reader, List<BeanConfig> beans) throws Exception {
		
		logger.log("---->Process Product Data:");
		if (beans == null||beans.size()==0)	return null;
		List<ProductField> productFields = new ArrayList<ProductField>();
		try
		{
			NodeList nodes = null;			
			int node_one_many = 0;
			ProductField field = null;
			DomWriter writer = new DomWriter();
			String label = "";
			String xProdata= "";
			String strValue ="";
			for (BeanConfig beanConfig : beans) {
				nodes = (NodeList) reader.read(beanConfig.getXpath(),XPathConstants.NODESET);
				node_one_many = nodes.getLength();
				xProdata = beanConfig.getXpath();
				if (nodes != null && nodes.getLength() > 0) {
					int i = 1;					
					if(node_one_many==0) System.out.println("Khong co thuoc tinh");
					
					while (i <= node_one_many) {
							 field = new ProductField();

							 label = (String) reader.read(xProdata
									+ "[" + i + "]" + beanConfig.getXpath_sub_label(),XPathConstants.STRING);
							
							 Node value = (Node) reader.read(xProdata
									+ "[" + i + "]" + beanConfig.getXpath_sub_value(),XPathConstants.NODE);
							
							 strValue = writer.toXMLString(value);

							 /* String pattern = "<a.*href=\".*\">";

						      // Create a Pattern object
						      Pattern r = Pattern.compile(pattern);
						      Matcher m = r.matcher(strValue);
						      strValue=m.replaceAll("").trim();*/
						      
							  Pattern r = Pattern.compile("\\•", Pattern.CASE_INSENSITIVE);
							  Matcher m = r.matcher(strValue);
							  strValue = m.replaceAll(";");
								
						      field.name=label.replaceAll(":", "");
						      field.value= strValue;
						     
							  System.out.println("---------->Field Name="+label+"-----: Field value="+strValue);
						
						productFields.add(field);
						i++;
					}
				}
				logger.log("----------->Product Data="+beanConfig.getTable());
				
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return productFields;
	}

	private List<ProductImage> processImage(XPathReader reader, List<ImageConfig> images,String url_content,String urlDomain) throws Exception {
		
		if(images.size()==0||images==null) return null;
		
		logger.log("---->Process Images:");		
		String day="";
		Calendar calendar = Calendar.getInstance();	
		int month = calendar.get(Calendar.MONTH)+1;
		day = calendar.get(Calendar.DAY_OF_MONTH)<10?"0"+calendar.get(Calendar.DAY_OF_MONTH):""+calendar.get(Calendar.DAY_OF_MONTH);
		String strmonth = month<10?"0"+month:""+month;
		long timeInMilis = Calendar.getInstance().getTimeInMillis();
		
		String dayPath =  calendar.get(Calendar.YEAR)+  "/" + strmonth+day; 
		ProductDownloadImage downloadImage = new ProductDownloadImage(urlDomain,urlDomain);
		String dis_folder="/usr/src/java/tomcat7/webapps/images.az24.vn/picture_model/"+dayPath+"/";
		//String dis_folder="d:/anh/";
		String pre_folder="/picture_model/";
		List<ProductImage> listImage = new ArrayList<ProductImage>();
		String name="";
		NodeList nodes = null;
		ProductImage productImage = null;
		for (ImageConfig imageConfig : images) {
			
			if(imageConfig.node_type.equalsIgnoreCase("nodeset"))
			{
				nodes = (NodeList) reader.read(imageConfig.xpath,
						XPathConstants.NODESET);
				int node_one_many = nodes.getLength();
				
				int i = 1;
				
				while(i<=node_one_many)
				{
					String	src = (String) reader.read(imageConfig.xpath
							+ "[" + i + "]" +imageConfig.xpath_sub ,
							XPathConstants.STRING);	
					name = src.substring(src.lastIndexOf("/")+1);
					/*String       spattern = "(.*')(.*)('.*)";
				      // Create a Pattern object
				      Pattern r = Pattern.compile(spattern);

				      // Now create matcher object.
				      Matcher m = r.matcher(src);
				      if (m.find( )) {
				         System.out.println("Found value: " + m.group(0) );
				         System.out.println("Found value: " + m.group(1) );
				         System.out.println("Found value: " + m.group(2) );
				      } else {
				         System.out.println("NO MATCH");
				      }
				    src =  m.group(2);
					name =  m.group(2).substring( m.group(2).lastIndexOf("/")+1);*/
					
					productImage = new ProductImage();
					productImage.name = "small_" + timeInMilis+name;
					productImage.path = pre_folder + dayPath + "/" + productImage.name;
					productImage.product_id = 0;
					
					if(i==1&&!StringUtil.isEmpty(src)){
						productImage.is_main = 1;
						product.pictrue = pre_folder+dayPath+ "/"+productImage.name;
					}
					
					Pattern pattern = Pattern.compile("http://");
					Matcher matcher = pattern.matcher(src);
					if(!matcher.find()) src = urlDomain+"/"+ src;
					downloadImage.downloadImage(src, dis_folder, timeInMilis+name);
					listImage.add(productImage);
				
				i++;
			
				}
				if(node_one_many>0)break;
			}
			if(listImage.size()>0) return listImage;
			if(imageConfig.node_type.equalsIgnoreCase("string"))
			{
				
					String src = (String) reader.read(imageConfig.xpath,XPathConstants.STRING);
					name = src.substring(src.lastIndexOf("/")+1);
					
					productImage = new ProductImage();
					productImage.name = "small_" + timeInMilis+name;
					productImage.path = pre_folder + dayPath + "/" + productImage.name;
					productImage.product_id = 0;
					productImage.is_main = 1;
					
					product.pictrue = pre_folder+dayPath+ "/"+productImage.name;
					Pattern pattern = Pattern.compile("\\.\\.");
					Matcher matcher = pattern.matcher(src);
					src = matcher.replaceAll("");
					
					pattern = Pattern.compile("http://");
					matcher = pattern.matcher(src);
					if(!matcher.find()) src = urlDomain+"/"+ src;
					downloadImage.downloadImage(src, dis_folder, timeInMilis+name);
					listImage.add(productImage);
			}
			
		}
		
		return listImage;
	}
	
	public void export_category(String store_alias) throws Exception
	{
		StoreCagegoryDAO categoryDAO = new StoreCagegoryDAO();
		StoreDAO storeDAO = new StoreDAO();
		int store_id = storeDAO.checkStore(store_alias);
		WritableWorkbook workbook = Workbook
		.createWorkbook(new java.io.File("d:/categorys2/store_category_"+store_alias
				+"_"+store_id+ ".xls"));
		WritableSheet s = workbook.createSheet("Sheet_" + 1, 0);
		List<com.az24.crawler.store.Category> listCat = categoryDAO.getCategoryByStoreId(store_id);
		/* Format the Font */
		WritableFont wf = new WritableFont(WritableFont.ARIAL, 10,
				WritableFont.BOLD);
		WritableCellFormat cf = new WritableCellFormat(wf);
		cf.setWrap(true);
		WritableFont wf1 = new WritableFont(WritableFont.ARIAL, 10,
				WritableFont.NO_BOLD);
		WritableCellFormat cf2 = new WritableCellFormat(wf1);
		cf2.setWrap(false);

		/* Creates Label and writes date to one cell of sheet*/
		Label l = new Label(0, 0, "ID", cf);
		s.addCell(l);
		l = new Label(1, 0, "Parent ID", cf);
		s.addCell(l);
		l = new Label(2, 0, "Cate Az24 ID", cf);
		s.addCell(l);
		l = new Label(3, 0, "Name", cf);
		s.addCell(l);
		l = new Label(4, 0, "URL", cf);
		s.addCell(l);
		l = new Label(5, 0, "Multi Sell", cf);
		s.addCell(l);
		int row = 1;
		
		for (Category category : listCat) {
			l = new Label(0, row, category.id+"", cf2);
			s.addCell(l);
			l = new Label(1, row, category.parent_id+"", cf2);
			s.addCell(l);		
			l = new Label(2, row, category.cat_id+"", cf2);
			s.addCell(l);
			l = new Label(3, row, category.name+"", cf2);
			s.addCell(l);
			l = new Label(4, row, category.url!=null?category.url:"", cf2);
			s.addCell(l);
			l = new Label(5, row, category.is_multi_sell+"", cf2);
			s.addCell(l);
			row++;
		}
		workbook.write();
		workbook.close();
	}	
	
	public List<Category> getCategory(String path_cate_excel) throws BiffException, IOException  {
		List<Category> listCate = new ArrayList<Category>();
		//URL url = CrawlerStore.class.getResource(path_cate_excel);
		//String realPathFile = url.getFile().replaceAll("%20", " ");
		//FileInputStream propsFile = new java.io.FileInputStream(realPathFile);
		FileInputStream propsFile = new java.io.FileInputStream(path_cate_excel); 
		Workbook w = Workbook.getWorkbook(propsFile);
		Sheet sheet = w.getSheet(0);
		Cell cell = null;
		// Vat_Gia_Cat| Az24_Cat
		Category category = null;
		for (int i = 1; i < sheet.getRows(); i++) {
			try {
				category = new Category();
				// Cate_store_id
				cell = sheet.getCell(0, i);
				int cate_store_id = Integer.parseInt(cell.getContents().trim());
				// Cate Parent Store ID
				cell = sheet.getCell(1, i);
				int cate_parent_store_id = Integer.parseInt(cell.getContents().trim());
				// Cate Az ID
				cell = sheet.getCell(2, i);
				int cate_az_id = Integer.parseInt(cell.getContents().trim());
				// Name
				cell = sheet.getCell(3, i);
				String name = cell.getContents().trim();
				// Url 
				// Name
				cell = sheet.getCell(4, i);
				String url_product = cell.getContents().trim();
				// is multi sell
				cell = sheet.getCell(5, i);
				int is_multi_sell = Integer.parseInt(cell.getContents().trim());
				
				cell = sheet.getCell(6, i);
				String regex = cell.getContents().trim();
				
				category.cat_id = cate_az_id;
				category.id = cate_store_id;
				category.parent_id = cate_parent_store_id;
				category.name = name;
				category.url = url_product;
				category.is_multi_sell = is_multi_sell;
				category.regex = regex;
				listCate.add(category);
			} catch (Exception e) {
				// TODO: handle exception
			}
	
		}
		return listCate;
	}
	
	public void saveProductData(List<ProductField> listProductField,List<ProductField> listField,List<ProductFieldValue> listFieldValue,int product_id)
	{
		// Luu Thuoc Tinh
		ProductField field = null;	
		ProductData productData = null;
		String fieldName ="";
		String fieldLabel ="";
		int i = 0;
		ProductDAO productDAO = new ProductDAO();
		if(listProductField==null) return;
		for (ProductField productFieldT : listProductField) {
			if (StringUtil.isEmpty(productFieldT.value))
				continue;
			fieldLabel = UTF8Tool.coDau2KoDau(productFieldT.name.trim());
			i = 0;
			System.out.println("Label="+fieldLabel);
			while (i < listField.size()) {
				fieldName = UTF8Tool.coDau2KoDau(listField.get(i).name.trim());
				System.out.println("Fiel Name="+fieldName);
				if (fieldName.equalsIgnoreCase(fieldLabel)) {
					field = listField.get(i);
					break;
				}
				i++;
			}

			if (field == null) {
				continue;
			}

			productData = new ProductData();
			productData.field_id = field.id;
			productData.product_id = product_id;
			if (field.type == 2) {
				String arrValue[] = productFieldT.value.split(";");
				int intValue = 0;
				for (String string : arrValue) {
					int k = 0;
					while (k < listFieldValue.size()) {
						if (listFieldValue.get(k).label.trim()
								.equalsIgnoreCase(string)) {
							intValue += listFieldValue.get(k).value;
							break;
						}
						k++;
					}

				}
				productData.value = intValue + "";
			} else if (field.type == 1 || field.type == 7 || field.type == 4) {
				int q = 0;
				while (q < listFieldValue.size()) {
					if (listFieldValue.get(q).label.trim()
							.equalsIgnoreCase(productFieldT.value.trim())) {
						productData.value = listFieldValue.get(q).value
								+ "";
						break;
					}
					q++;
				}

			} else if (field.type == 3) {
				productData.value = String.valueOf(productFieldT.value
						.indexOf("true") > -1 ? 1 : 0);
			} else {
				productData.value = productFieldT.value;
			}

			if (productData.value != null)
				productDAO.saveProductData(productData);

		}
	}
	

	public static void main(String[] args) throws Exception {
		
	 	CategoryDAO categoryDAO = new CategoryDAO();
	 	StoreDAO storeDAO = new StoreDAO();
	 	ProductDAO productDAO = new ProductDAO();
	 	ProductStoreDAO productStoreDAO = new ProductStoreDAO();
	 	ProductStoreCatDAO productStoreCatDAO = new ProductStoreCatDAO();
	 	ImporterDAO importerDAO = new ImporterDAO();
	 	StoreProductExtract productExtract = new StoreProductExtract();
		String store_alias = args[0];	 	
	 	String urlDomain =  args[1];
	 	String urlDomainRewrite =  args[2];
	 	String beanconfig =  args[3];
	 	String catefile =  args[4];
	 	productExtract.urlDomain = urlDomain;
		//List<Category> listCate = productExtract.getCategory("D:/categorys2/store_category_kholaptop_626.xls");
	 	List<Category> listCate = productExtract.getCategory(catefile);
		//BeanXmlConfig beanXmlConfig = new BeanXmlConfig("src/com/az24/crawler/config/beanProductStoreNamPhongLaptop.xml");
		BeanXmlConfig beanXmlConfig = new BeanXmlConfig(beanconfig);
		
	 /*	String store_alias = "kholaptop";	 	
	 	String urlDomain =  "http://namphongpc.com";
	 	String urlDomainRewrite =  "http://namphongpc.com";*/
	 	
		
	 	beanXmlConfig.parseConfig();
	 	List<Property> propeties = beanXmlConfig.bean.getProperties();
	 	List<ImageConfig> images = beanXmlConfig.bean.getImages();
	 	List<BeanConfig> beans = beanXmlConfig.bean.getBeans();	 	
	 	int time_sleep = 1000;
	 	int store_id  = 0;	 
	 	int is_multi = 1;
	 	int cat_az_id = 0;
	 	int store_cat_id_1 = 0;
	 	int store_cat_id_2 = 0;
	 	store_id = storeDAO.checkStore(store_alias);
	 	if(store_id==0)return;
	 	HttpResponse res = null;
	 	DocumentAnalyzer analyzer;
	 	HttpClientImpl client = new HttpClientImpl();
	 	String html = "";
	 	List<A> listLinkProduct = null;	 	
	 	Pattern pattern = null;
	 	Matcher matcher = null;
	 	boolean checkLink = false;
	 	boolean checkLog = false;
	 	//Process Category
	 	List<ProductField> listProductField = null;
		List<ProductFieldValue> listProductFieldValue = null;
	 	for (int i = 0; i < listCate.size(); i++) {
	 		
	 		Category category = listCate.get(i);
		 	is_multi = category.is_multi_sell;
		 	cat_az_id = category.cat_id;
		 	store_cat_id_1 = category.parent_id;
		 	store_cat_id_2 = category.id;
		 	System.out.println("Process Category Name = "+category.url);
		 	if(StringUtil.isEmpty(category.url)) continue;
		 	
		 	Map<Integer, Integer> holder =  new HashMap<Integer, Integer>();
		 	categoryDAO.getCategoriesParent(cat_az_id, holder);
		 	
		 	Category categoryAz = categoryDAO.getCate(cat_az_id);
		 	listProductField = productDAO.getField(categoryAz.extension_id);
			listProductFieldValue = productDAO.getFieldValue(categoryAz.extension_id);
		 	// Process Data In Category
		 	int page = 1;
		 	int number_product = 0;
		 	while(true)
		 	{	
		 		// Process Page
		 		res = client.fetch(category.url);
				Thread.sleep(time_sleep);				
				try {
					// Get Link Product
					html = HttpClientUtil.getResponseBody(res);					
					Thread.sleep(time_sleep);
					analyzer = new DocumentAnalyzer.DefaultDocumentAnayzer(urlDomain, urlDomainRewrite);
					listLinkProduct = analyzer.analyze(html, category.url);
					if (listLinkProduct.size() == 0)
						break;
				}catch (Exception e) {
					e.printStackTrace();
				}	
			 	// Process link in page
				number_product = 0;
				int k = 1;
				for (A a : listLinkProduct) {					
					
					if(StringUtil.isEmpty(category.regex)) break;
					System.out.println(k+"-"+a.getUrl());
					k++;
					pattern = Pattern.compile(category.regex.toLowerCase());
					matcher = pattern.matcher(a.getUrl().toLowerCase());
					String id_url = new UriID(new HttpURL(a.getUrl())).getIdAsString();
					checkLink = matcher.find();
					if(!checkLink) continue;
					checkLog = importerDAO.checkProductStore(id_url, store_cat_id_1);
					if (!checkLog) {
						
							res = client.fetch(a.getUrl());
							html = HttpClientUtil.getResponseBody(res);
							XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
							productExtract.product = new Product();
							productExtract.productStore = new ProductStore();
						 	productExtract.processProperty(reader, a.getUrl(), propeties, store_id, store_alias, is_multi, 
						 			cat_az_id, store_cat_id_1, store_cat_id_2,0,holder);
						 	
						 	if(StringUtil.isEmpty(productExtract.product.name)) continue;
						 	
						 	List<ProductImage> listImagePro = productExtract.processImage(reader, images, a.getUrl(),urlDomain);
						 	List<ProductField> listFieldData = productExtract.processFieldType_TableLabelValue(reader, beans);
						 	productExtract.product.original_link = a.getUrl();
						 	// Save Product	
						 	
						 	int product_id = productDAO.getProductByName(productExtract.product.name, productExtract.product.cat_id);
						 	if(product_id==0)
						 	{
						 		product_id = productDAO.saveProduct(productExtract.product);
						 		// Save Product Data
						 		productExtract.saveProductData( listFieldData , listProductField, listProductFieldValue, product_id);
						 		// Save Product Image
							 	for (ProductImage productImage : listImagePro) {
							 		productImage.product_id = product_id;
							 		productDAO.savePicture(productImage);
								}
						 	}
						 	
						 	if(product_id>0)
						 	{
							 	productExtract.productStore.product_id 	= product_id;
								// Save Product Store
							 	int product_store_id = productStoreDAO.saveProductStore(productExtract.productStore);
							 	
							 	ProductStoreCat productStoreCat = new ProductStoreCat();
								productStoreCat.product_id = product_id;
								productStoreCat.product_store_id = product_store_id;
								productStoreCat.store_id = store_id;
								productStoreCat.store_cat_id1 = store_cat_id_1;
								productStoreCat.store_cat_id2 = store_cat_id_2;
								
								productStoreCatDAO.saveProductStoreCate(productStoreCat);
								// Save Log
								importerDAO.saveLog(product_id, product_store_id, store_cat_id_1,id_url, a.getUrl(), urlDomain,store_id);
							 	
							 	// IF Save Product Success
							 	number_product++;
						 	}
					 	Thread.sleep(time_sleep);
					}
				}
				// IF Don't Crawler Product
				if(number_product==0) break;
				page++;
				
		 	}
		}
	}

}
