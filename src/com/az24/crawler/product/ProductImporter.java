package com.az24.crawler.product;

import hdc.crawler.ExtractEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.az24.crawler.AbstractImporter;
import com.az24.crawler.config.BeanXmlConfig;
import com.az24.crawler.config.JdbmXmlConfig;
import com.az24.crawler.model.ImageConfig;
import com.az24.crawler.model.product.BaseDao;
import com.az24.crawler.model.product.Product;
import com.az24.crawler.model.product.ProductData;
import com.az24.crawler.model.product.ProductField;
import com.az24.crawler.model.product.ProductFieldValue;
import com.az24.crawler.model.product.ProductImage;
import com.az24.util.FileLog;
import com.az24.util.UTF8Tool;

public class ProductImporter extends AbstractImporter {

	public ProductImporter(String filebeanconfig, String filejdbmconfig) {
		super(filebeanconfig, filejdbmconfig);
	}

	protected void processAfterSave(int id, int cat_id) {

	}

	@SuppressWarnings("unchecked")
	public void run() {
		BeanXmlConfig beanXmlConfig = new BeanXmlConfig(this.filebeanconfig);
		beanXmlConfig.parseConfig();
		ExtractEntity entity = null;
		try {
			int i = 0;
			openData();
			openConnection();
			conn.setAutoCommit(false);
			connLog.setAutoCommit(false);
			Iterator<ExtractEntity> iterator = extractPrimary.values()
					.iterator();
			int count = 0,cat_id=0;
			int empty = 0, product_id, extention_id = 0;
			String name = "", price = "", pricture = "", is_multi = "1",product_log="",product_data_log="",product_image_log="";
			List<ImageConfig> listImage = null;
			List<ProductField> listField = null;

			Product product = null;
			ProductImage productImage = null;
			ProductData productData = null;
			BaseDao baseDao = new BaseDao();
			Map<Integer, Integer> holder = null;
			String pre_folder="/picture_model/";
			ProductField field = null;
			List<ProductField> prList = null;
			List<ProductFieldValue> prValueList = null;
			while (iterator.hasNext()) {
				entity = iterator.next();
				product_image_log="";
				product_data_log="";
				product_log="";
				if (!this.checkEntity(connLog, entity.getID())) {
				//if(1==1){
					product = new Product();
					holder = new HashMap<Integer, Integer>();

					name = (String) entity.getProperty("name");
					price = (String) entity.getProperty("price");
					product.description = (String) entity.getProperty("description");
					cat_id = entity.getCat_id();
					pricture = (String) entity.getProperty("picture");
					
					is_multi = entity.getProperty("is_multi") != null ? (String) entity
							.getProperty("is_multi")
							: "1";
					System.out.println("is_multi-->=" + is_multi);
					System.out.println("Name-->=" + name);
					System.out.println("Price-->=" + price);
					
					if(holder.size()==0)
					baseDao.getCategoriesParent(cat_id,holder, conn);
					
					product.name =  name.replaceAll("- Thông số kỹ thuật", "");					
					product.pictrue = pricture;
					
					String pattern = "\\W";
				      // Create a Pattern object
				    Pattern r = Pattern.compile(pattern);
				      // Now create matcher object.
				    name = UTF8Tool.coDau2KoDau(product.name).trim();
				    Matcher m = r.matcher(name);
				    String url_rewrite = m.replaceAll("_");
				    product.url_rewrite = url_rewrite;
				    
					price = price.split(" ")[0];
					price = price.replaceAll("\\.", "");
					try{
						product.price_from = Double.valueOf(price).doubleValue();
						product.price_to = Double.valueOf(price).doubleValue();
					}catch (Exception e) {
						product.price_from = 0;
						product.price_to = 0;
					}
					
					product.status = 1;
					product.is_multi = 1;
					product.cat_id = cat_id;
					product.original_link = entity.getUrl();
					
					if(extention_id==0)
					{
						extention_id = baseDao.getExtensionID(cat_id, conn);
						prList = baseDao.getField(extention_id, conn);
						prValueList =  baseDao.getFieldValue(extention_id,  conn);
						
					}
					
					if(extention_id>0)
							{
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
		
							product_id = baseDao.saveProduct(product, conn);
							
							if(product_id==0)
							{
								product_log ="Khong them dc san pham";
								saveEntity(connLog, entity, "VatGia",product_id,cat_id,product_log,product_data_log,product_image_log);
								continue;
							}
							// xu ly image
							listImage = (List<ImageConfig>) entity.getProperty("images");
							for (ImageConfig imageConfig : listImage) {												
								productImage = new ProductImage();
								productImage.name = "small_"+imageConfig.name;
								productImage.path = imageConfig.getPath(pre_folder, productImage.name);
								productImage.product_id = product_id;
								productImage.is_main = imageConfig.is_main;
								if(!baseDao.savePicture(productImage, conn))
								{							
									product_image_log +="Them Image Co Loi;";							
								}
							}
		
							// xu ly product data
							listField = (List<ProductField>) entity.getProperty("field");
							
							for (ProductField productField : listField) {
								product_data_log="";field = null;
								if(productField.value==null) continue;
								System.out.println(UTF8Tool.coDau2KoDau(productField.name));
								/*field = baseDao.getField(
										productField.name.trim(), extention_id, conn);*/
								int j = 0;
								String proFieldName = UTF8Tool.coDau2KoDau(productField.name);
								while(j<prList.size())
								{
									String fieldName = UTF8Tool.coDau2KoDau(prList.get(j).name.trim());
									if(fieldName.equalsIgnoreCase(proFieldName.trim()))
									{
										field = prList.get(j);
										break;
									}
								//	System.out.println(fieldName);
									j++;
								}
							
								if(field == null)					
								{
									product_data_log +="Khong tim thay field --> Field Name = " + productField.name +" Field Value = " + productField.value +";";
									System.out.println("Khong tim thay field --> Field Name = " + productField.name +" Field Value = " + productField.value +";");
									continue;
								}
								
								productData = new ProductData();
								productData.field_id = field.id;
								productData.product_id = product_id;
								if (field.type == 2) {
									String arrValue[] = productField.value
											.split("<br/>");
									int intValue = 0;
									for (String string : arrValue) {
										Pattern p = Pattern.compile("\\W\\s", Pattern.CASE_INSENSITIVE);
										m = p.matcher(string);
										string = m.replaceAll("").toUpperCase();
										int k = 0;
										while(k<prValueList.size())
										{
											if(prValueList.get(k).label.trim().equalsIgnoreCase(string))
											{
												intValue += prValueList.get(k).value;
												break;
											}
											k++;
										}
										/*intValue += baseDao.getFieldValue(string,
												extention_id, field.id, conn);*/
									}
									productData.value = intValue + "";
								} else if (field.type == 1 || field.type == 7
										|| field.type == 4) {						
										int q = 0;
										while(q<prValueList.size())
										{
											if(prValueList.get(q).label.trim().equalsIgnoreCase(productField.value.trim()))
											{
												productData.value = prValueList.get(q).value+"";
												break;
											}
											q++;
										}
									
								/*	productData.value = baseDao.getFieldValue(
											productField.value.trim().toUpperCase(),
											extention_id, field.id, conn)
											+ "";*/
								} else if (field.type == 3) {
									productData.value = String
											.valueOf(productField.value.indexOf("true") > -1 ? 1
													: 0);
								} else {
									productData.value = productField.value;
								}
								if(productData.value!=null)
								if(baseDao.saveProductData(productData, conn)==0)
								{
									product_data_log +="Them productData Co Loi --> Field Name = " + productField.name +" Field Value = " + productField.value +";";;		
								}
		
							}
						
							if(product_id>0)
							saveEntity(connLog, entity, "VatGia",product_id,cat_id,product_log,product_data_log,product_image_log);
					}
					System.out.println("Imported-->count=" + count
							+ entity.getProperty("name"));
					count++;
					Thread.sleep(100);
				}

				if (i % 10 == 0) {
					commitData();
				}
				System.out
						.println("Import-->i=" + i + "--->" + entity.getUrl());
				i++;
			}

			System.out.println("Tong Data-->=" + (i));
			System.out.println("Tong Imported-->=" + count);
			System.out.println("Tong Data Loi-->" + empty);

			Calendar calendar = Calendar.getInstance();
			String log = calendar.getTime().toString() + "-->Tong Data:" + i
					+ "\r\n";
			log += calendar.getTime().toString() + "-->Tong Imported:" + count
					+ "\r\n";
			log += calendar.getTime().toString() + "-->Tong Data Loi:" + empty
					+ "\r\n";

			FileLog.createFileLog(JdbmXmlConfig.file_log + "_log_"
					+ calendar.get(Calendar.DAY_OF_MONTH)
					+ calendar.get(Calendar.MONTH)
					+ calendar.get(Calendar.YEAR) + ".txt");
			FileLog.writer(log);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				commitData();
				closeData();
				conn.commit();
				conn.setAutoCommit(true);
				conn.close();

				connLog.commit();
				connLog.setAutoCommit(true);
				connLog.close();
			} catch (Exception e) {

			}
		}
	}

	public void saveEntity(Connection conn, ExtractEntity entity, String source,int pro_id,int cat_id,String pro_log,String pro_data_log,String pro_image_log) {
		try {
			PreparedStatement ps = conn
					.prepareStatement("INSERT INTO tbl_importer(url_id,url,source,entity,product_id,cat_id,product_log,product_data_log,product_image_log)"
							+ "	VALUES	(?,?,?,?,?,?,?,?,?) ");
			ps.setString(1, entity.getID());
			ps.setString(2, entity.getUrl());
			ps.setString(3, source);
			ps.setObject(4, entity);			
			ps.setInt(5, pro_id);
			ps.setInt(6, cat_id);
			ps.setString(7, pro_log);
			ps.setString(8, pro_data_log);
			ps.setString(9, pro_image_log);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public boolean checkEntity(Connection conn, String url_id) {

		try {
			PreparedStatement ps = conn
					.prepareStatement("select id from tbl_importer where url_id = ? ");
			ps.setString(1, url_id);
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void main(String[] args) {
		ProductImporter crawlerExtracter = new ProductImporter(
				"src/com/az24/crawler/config/beanProductVatGia.xml",
				"src/com/az24/crawler/config/jdbm.xml");
		crawlerExtracter.run();
		
	}
}
