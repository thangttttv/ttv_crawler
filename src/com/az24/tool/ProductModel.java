package com.az24.tool;

import hdc.util.text.StringUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.az24.crawler.model.product.Product;
import com.az24.db.pool.C3p0StorePool;
import com.az24.util.UTF8Tool;

public class ProductModel {
	public Connection connStore;
	
	public void openConnectionStore() {
		try {
			connStore = C3p0StorePool.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void closeConnectionStore() {
		try {
			if (connStore != null)
				connStore.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void processTrungModel(int id)
	{
		String sql = "select id,name,cat_id from products where id = ? ";
		this.openConnectionStore();
		PreparedStatement ps;
		PreparedStatement ps2;
		PreparedStatement ps3;
		PreparedStatement ps4;
		try {
			ps = connStore.prepareStatement(sql);
			ps.setInt(1,id);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
			{
				int cat_id = rs.getInt(3);
				String name = rs.getString(2);
				sql = "select id,name,cat_id from products where id != ? and name = ? and cat_id = ? ";
				ps2 = connStore.prepareStatement(sql);
				ps2.setInt(1,id);
				ps2.setString(2,name);
				ps2.setInt(3,cat_id);
				ResultSet rs2 = ps2.executeQuery();
				while(rs2.next())
				{
					// update table product_store
					connStore.setAutoCommit(false);
					sql = "update products_store set product_id = ? where product_id = ? ";
					System.out.println("update products_store set product_id = "+id+" where product_id = "+rs2.getInt(1));
					ps3 = connStore.prepareStatement(sql);
					ps3.setInt(1, id);
					ps3.setInt(2, rs2.getInt(1));
					try{
					ps3.execute();
					}catch (Exception e) {
						// TODO: handle exception
					}
					// update table product_store_cate
					sql = "update products_store_cat set product_id = ? where product_id = ? ";
					ps4 = connStore.prepareStatement(sql);
					ps4.setInt(1, id);
					ps4.setInt(2, rs2.getInt(1));
					ps4.execute();

					
					// update even_product
					// update order_detail
					// update order_review
					// product_auto_up
					// product_hit
					// ranks

					// xoa produuct
					sql = "delete from products  where  id =  "+rs2.getInt(1);
					connStore.prepareStatement(sql).execute();
					sql = "delete from products_data  where  product_id =  "+rs2.getInt(1);
					connStore.prepareStatement(sql).execute();
					sql = "delete from products_picture  where  product_id =  "+rs2.getInt(1);
					connStore.prepareStatement(sql).execute();
					sql = "delete from products_hit  where  product_id =  "+rs2.getInt(1);
					connStore.prepareStatement(sql).execute();
					
					connStore.commit();
					connStore.setAutoCommit(true);
					//System.out.println(rs2.getInt(1));
				}
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		this.closeConnectionStore();
	}
	
	public void processTrungModel_2(int id)
	{
		String sql = "select id,name,cat_id from products where id = ? ";
		this.openConnectionStore();
		PreparedStatement ps;
		PreparedStatement ps2;
		PreparedStatement ps3;
		PreparedStatement ps4;
		int i = 0;
		try {
			ps = connStore.prepareStatement(sql);
			ps.setInt(1,id);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
			{
				int cat_id = rs.getInt(3);
				String name = rs.getString(2);
				sql = "select id,name,cat_id from products where  name = ? and cat_id = ? order by id asc ";
				ps2 = connStore.prepareStatement(sql);
				ps2.setString(1,name);
				ps2.setInt(2,cat_id);
				
				ResultSet rs2 = ps2.executeQuery();
				i = 0;
				while(rs2.next())
				{
					// update table product_store
					if(i==0){id =rs2.getInt(1);i++;continue;}
					
					connStore.setAutoCommit(false);
					sql = "update products_store set product_id = ? where product_id = ? ";
					System.out.println("update products_store set product_id = "+id+" where product_id = "+rs2.getInt(1));
					ps3 = connStore.prepareStatement(sql);
					ps3.setInt(1, id);
					ps3.setInt(2, rs2.getInt(1));
					try{
					ps3.execute();
					}catch (Exception e) {
						// TODO: handle exception
					}
					// update table product_store_cate
					sql = "update products_store_cat set product_id = ? where product_id = ? ";
					ps4 = connStore.prepareStatement(sql);
					ps4.setInt(1, id);
					ps4.setInt(2, rs2.getInt(1));
					ps4.execute();

					
					// update even_product
					// update order_detail
					// update order_review
					// product_auto_up
					// product_hit
					// ranks

					// xoa produuct
					sql = "delete from products  where  id =  "+rs2.getInt(1);
					connStore.prepareStatement(sql).execute();
					sql = "delete from products_data  where  product_id =  "+rs2.getInt(1);
					connStore.prepareStatement(sql).execute();
					sql = "delete from products_picture  where  product_id =  "+rs2.getInt(1);
					connStore.prepareStatement(sql).execute();
					sql = "delete from products_hit  where  product_id =  "+rs2.getInt(1);
					connStore.prepareStatement(sql).execute();
					
					connStore.commit();
					connStore.setAutoCommit(true);
					//System.out.println(rs2.getInt(1));
					i++;
				}
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		this.closeConnectionStore();
	}
	
	public void addFieldValueTypeSelect(String excel_file)
	{
		FileInputStream propsFile;
		try {
			propsFile = new java.io.FileInputStream(excel_file);
			Workbook w = Workbook.getWorkbook(propsFile);
			Sheet sheet = w.getSheet(0);
			Cell cell = null;
			// ID EXTENTSION | ID FIELD | VALUES
			for (int i = 1; i < sheet.getRows(); i++) {
				try {
					cell = sheet.getCell(0, i);
					
					if(StringUtil.isEmpty(cell.getContents())) continue;
					
					int ext_id = Integer.parseInt(cell.getContents().trim());
					cell = sheet.getCell(1, i);
					int field_id = Integer.parseInt(cell.getContents().trim());
					cell = sheet.getCell(2, i);
					String value = cell.getContents();
					
					cell = sheet.getCell(3, i);
					String operator = cell.getContents();
					
					String values[] = value.split(";");
					this.openConnectionStore();
					String sql = " select max(value),max(filter_value)  from products_field_value where field_id = ?  ";
					PreparedStatement ps = connStore.prepareStatement(sql);
					ps.setInt(1,field_id);
					ResultSet rs = ps.executeQuery();
					if(rs.next())
					{
						int max_value = rs.getInt(1);
						int max_value_filter = rs.getInt(2);
						connStore.setAutoCommit(false);
						
						for (String label : values) {
							sql = "INSERT INTO az24_store.products_field_value ( field_id, extension_id, label, VALUE, filter_value, " +
									"		operator,	is_filter,	is_input,	order_view,	order_input, order_filter," +
									"   create_user, create_date, modify_user,	modify_date ) VALUES " +
									"  (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
							ps =connStore.prepareStatement(sql);
							ps.setInt(1, field_id);
							ps.setInt(2, ext_id);
							ps.setString(3,label);
							
							max_value = max_value+1;
							max_value_filter = max_value_filter+100;
							
							ps.setInt(4, max_value);
							ps.setInt(5, max_value_filter);
							ps.setString(6, operator.trim());
							ps.setInt(7, 1);
							ps.setInt(8, 1);
							ps.setInt(9, 0);
							ps.setInt(10, 0);
							ps.setInt(11, 0);
							ps.setString(12,"thangtt");
							ps.setLong(13, Calendar.getInstance().getTimeInMillis()/1000);
							ps.setString(14,"thangtt");
							ps.setLong(15, Calendar.getInstance().getTimeInMillis()/1000);
							ps.execute();
							connStore.commit();
							
						}
						connStore.setAutoCommit(true);
					}
					this.closeConnectionStore();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public List<Product> getListProducts(int id)
	{
		List<Product> listProduct = new ArrayList<Product>();
		String sql = "SELECT 	id, NAME, is_multi, STATUS, price_from, price_to,cat_id FROM  az24_store.products where id > ? " +
				"  order by id  LIMIT 0, 500;";
		this.openConnectionStore();
		 System.out.println(sql);
		try {
			PreparedStatement ps = connStore.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				Product product = new Product();
				product.id = rs.getInt(1);
				product.name = rs.getString(2);
				product.cat_id = rs.getInt("cat_id");
				listProduct.add(product);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return listProduct;
	}
	
	public int processPrice(List<Product> listProducts)
	{
		String sql_max  = "SELECT price FROM products_store WHERE product_id =  ? And status = 1 ORDER BY  price DESC LIMIT 1";
		String sql_min  = "SELECT price FROM products_store WHERE product_id =  ? AND price >0 And status = 1 ORDER BY  price ASC  LIMIT 1";
		String sql_product  = "update products set price_from = ? , price_to = ?  where id = ? ";
		PreparedStatement ps  = null;
		PreparedStatement ps_product  = null;
		int id = 0;double max_price = 0;
		double min_price = 0;
		for (Product product : listProducts) {
			this.openConnectionStore();
			max_price=0;min_price = 0;
			try {
				
				ps = connStore.prepareStatement(sql_max);
				ps.setInt(1, product.id);
				ResultSet rs = ps.executeQuery();
				if(rs.next())
				{
					max_price = rs.getDouble(1);
				}
				
				ps = connStore.prepareStatement(sql_min);
				ps.setInt(1, product.id);
				rs = ps.executeQuery();
				if(rs.next())
				{
					min_price = rs.getDouble(1);
				}
				
				System.out.println(product.id+":->Price="+min_price+" - "+max_price);
				connStore.setAutoCommit(false);
				ps_product = connStore.prepareStatement(sql_product);
				ps_product.setDouble(1, min_price);
				ps_product.setDouble(2, max_price);
				ps_product.setInt(3, product.id);
				ps_product.execute();
				
				id = product.id;
				connStore.commit();
				connStore.setAutoCommit(true);
				
			} catch (SQLException e) {
				e.printStackTrace();
			}			
			this.closeConnectionStore();
		}
		return id;
	}
	
	public int getProductTrungModel(String name,int cat_id)
	{
		int sl = 0;
		String sql = "select count(id)  from products where trim(name) = ? and cat_id = ? ";
		this.openConnectionStore();
		try {
			PreparedStatement ps = connStore.prepareStatement(sql);
			ps.setString(1, name.trim());
			ps.setInt(2,cat_id);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
			{
				sl = rs.getInt(1);
			}
		} catch (SQLException e) {			
			e.printStackTrace();
		}		
		this.closeConnectionStore();			
		return sl;
	}
	private int stt = 1;

/*	private WritableWorkbook workbook;
	private WritableSheet sheet;
	
	
	public void createFileTM() throws IOException, RowsExceededException,
			WriteException {
		workbook = Workbook.createWorkbook(new java.io.File(
				"/data/crawler/images/product_trungmodel.xls"));
		workbook = Workbook.createWorkbook(new java.io.File(
		"d:/product_trungmodel.xls"));
		sheet = workbook.createSheet("Sheet_" + 1, 0);

		 Format the Font 
		WritableFont wf = new WritableFont(WritableFont.ARIAL, 10,
				WritableFont.BOLD);
		WritableCellFormat cf = new WritableCellFormat(wf);
		cf.setWrap(true);
		WritableFont wf1 = new WritableFont(WritableFont.ARIAL, 10,
				WritableFont.NO_BOLD);
		WritableCellFormat cf2 = new WritableCellFormat(wf1);
		cf2.setWrap(false);

		 Creates Label and writes date to one cell of sheet 
		Label l = new Label(0, 0, "STT", cf);
		sheet.addCell(l);
		l = new Label(1, 0, "ID", cf);
		sheet.addCell(l);
		l = new Label(2, 0, "Name", cf);
		sheet.addCell(l);
		l = new Label(3, 0, "Cat_ID", cf);
		sheet.addCell(l);
		l = new Label(4, 0, "SL", cf);
		sheet.addCell(l);
		workbook.write();
		
	}

	
	public void closeFileTM()
	{
		try {
			workbook.close();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	public void writerModelTrung(WritableWorkbook workbook,WritableSheet sheet, Product product,int sl) throws IOException, RowsExceededException,
			WriteException {
		/* Creates Label and writes date to one cell of sheet */
		
		Label l = null;
		//int row = 1;
		WritableFont wf = new WritableFont(WritableFont.ARIAL, 10,
				WritableFont.BOLD);
		WritableCellFormat cf = new WritableCellFormat(wf);
		cf.setWrap(true);
		WritableFont wf1 = new WritableFont(WritableFont.ARIAL, 10,
				WritableFont.NO_BOLD);
		WritableCellFormat cf2 = new WritableCellFormat(wf1);
		cf2.setWrap(false);
		
		l = new Label(0, stt, stt + "", cf2);
		sheet.addCell(l);
		l = new Label(1, stt, product.id+"", cf2);
		sheet.addCell(l);
		l = new Label(2, stt, product.name+ "", cf2);
		sheet.addCell(l);
		l = new Label(3, stt, product.cat_id + "", cf2);
		sheet.addCell(l);
		l = new Label(4, stt, sl + "", cf2);
		sheet.addCell(l);		
		workbook.write();
		
	
	}

	
	public int processWriteTrungModel(WritableWorkbook workbook,WritableSheet sheet,List<Product> listProducts)
	{
		int id = 0;		
		for (Product product : listProducts) {
			int sl = this.getProductTrungModel(product.name,product.cat_id);
			if(sl==1)
			{
				
				try {
					this.writerModelTrung(workbook,sheet,product, sl);					
				} catch (RowsExceededException e) {
					e.printStackTrace();
				} catch (WriteException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				stt++;
			}
			id =  product.id;
			System.out.println(id+"="+sl);
		}
		return id;
		
	}
	
	
	public  void processModelTrung() {
		ProductModel productModel = new ProductModel();
	
		try {
			WritableWorkbook workbook = Workbook.createWorkbook(new java.io.File("/data/crawler/images/product_trungmodel.xls"));
			
			//WritableWorkbook workbook = Workbook.createWorkbook(new java.io.File(	"d:/product_trungmodel.xls"));
			WritableSheet sheet = workbook.createSheet("Sheet_" + 1, 0);
		
			/* Format the Font */
			WritableFont wf = new WritableFont(WritableFont.ARIAL, 10,
					WritableFont.BOLD);
			WritableCellFormat cf = new WritableCellFormat(wf);
			cf.setWrap(true);
			WritableFont wf1 = new WritableFont(WritableFont.ARIAL, 10,
					WritableFont.NO_BOLD);
			WritableCellFormat cf2 = new WritableCellFormat(wf1);
			cf2.setWrap(false);
		
			/* Creates Label and writes date to one cell of sheet */
			Label l = new Label(0, 0, "STT", cf);
			sheet.addCell(l);
			l = new Label(1, 0, "ID", cf);
			sheet.addCell(l);
			l = new Label(2, 0, "Name", cf);
			sheet.addCell(l);
			l = new Label(3, 0, "Cat_ID", cf);
			sheet.addCell(l);
			l = new Label(4, 0, "SL", cf);
			sheet.addCell(l);
			int stt = 0;
			int id = 0;
			while(id<520000)
			{
			   List<Product> listProducts = productModel.getListProducts(id);
			   if(listProducts.size()==0) break;
			   // check lis product
			   for (Product product : listProducts) {
					int sl = productModel.getProductTrungModel(product.name,product.cat_id);
					if(sl>1)
					{
						// writer ra sheet
						try {
							l = new Label(0, stt, stt + "", cf2);
							sheet.addCell(l);
							l = new Label(1, stt, product.id+"", cf2);
							sheet.addCell(l);
							l = new Label(2, stt, product.name+ "", cf2);
							sheet.addCell(l);
							l = new Label(3, stt, product.cat_id + "", cf2);
							sheet.addCell(l);
							l = new Label(4, stt, sl + "", cf2);
							sheet.addCell(l);		
														
						} catch (RowsExceededException e) {
							e.printStackTrace();
						} catch (WriteException e) {
							e.printStackTrace();
						}
						
						stt++;
					}
					id =  product.id;
					System.out.println(id+"="+sl);
				}
			   System.out.println(id);
			}
			workbook.write();
			workbook.close();
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
		
	}
	
	public void countProductInStore()
	{
		String sql = "SELECT  id,name from store ";
		this.openConnectionStore();
		try {
			PreparedStatement ps = connStore.prepareStatement(sql);		
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				
				int id = rs.getInt(1);
				String name = rs.getString(2);
				System.out.println(name);
				int sl = 0;
				sql = "SELECT  count(id)  from  products_store  where store_id =  " + id;
				PreparedStatement ps1 = connStore.prepareStatement(sql);
				ResultSet rs2 = ps1.executeQuery();
				
				if(rs2.next()) sl = rs2.getInt(1);
				
				connStore.setAutoCommit(false);
				sql = "update  store  set num_of_product = ? where id = ? ";
				PreparedStatement ps2 = connStore.prepareStatement(sql);	
				ps2.setInt(1,sl );
				ps2.setInt(2,id );
				ps2.execute();
				connStore.commit();
				connStore.setAutoCommit(true);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.closeConnectionStore();
	
	}
	
	
	public void countNumberStoreSell()
	{
		String sql_count = "SELECT  count(product_id) from products_store  where product_id = ? ";
		String sql_pro = "SELECT  id,name from products  where id > ? order by id asc LIMIT 0, 500 ";
		int i  = 0;
		while(i<1000000)
		{
			this.openConnectionStore();
			try {
				PreparedStatement ps = connStore.prepareStatement(sql_pro);	
				ps.setInt(1, i);
				ResultSet rs = ps.executeQuery();
				if(!rs.next()) break;
				connStore.setAutoCommit(false);
				while(rs.next())
				{
					int id = rs.getInt(1);
					String name = rs.getString(2);
					System.out.println(name);
					int sl = 0;
					sql_count = "SELECT  count(product_id) from products_store  where product_id =  " + id;
					PreparedStatement ps1 = connStore.prepareStatement(sql_count);
					ResultSet rs2 = ps1.executeQuery();
					if(rs2.next()) sl = rs2.getInt(1);
					
					String sql = "update  products  set num_of_peple_sell = ? where id = ? ";
					PreparedStatement ps2 = connStore.prepareStatement(sql);	
					ps2.setInt(1,sl );
					ps2.setInt(2,id );
					ps2.executeUpdate();
					if(i%30==0)
					connStore.commit();
					
					i = id;
				}
				connStore.commit();
				connStore.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			this.closeConnectionStore();
		}
	}
	
	
	public int processUrlRewrite(List<Product> listProducts)
	{
		
		String sql_product  = "update products set  url_rewrite = ?   where id = ? ";
		PreparedStatement ps_product  = null;
		Pattern pattern = Pattern.compile("\\W+");
		Pattern pattern2 = Pattern.compile("-$");
		int id = 0;
		Matcher matcher = null;
		String name = "";
		for (Product product : listProducts) {
			this.openConnectionStore();		
			try {
				
				connStore.setAutoCommit(false);
				
				name = UTF8Tool.coDau2KoDau(product.name.trim().toLowerCase());
				matcher = pattern.matcher(name);
				String url_rewrite=matcher.replaceAll("-");
				
				matcher = pattern2.matcher(url_rewrite);
				url_rewrite = matcher.replaceAll("");
				
				System.out.println(product.id+"="+url_rewrite);
				
				ps_product = connStore.prepareStatement(sql_product);
				
				ps_product.setString(1, url_rewrite);				
				ps_product.setInt(2, product.id);
				ps_product.execute();
				
				id = product.id;
				connStore.commit();
				connStore.setAutoCommit(true);
				
			} catch (SQLException e) {
				e.printStackTrace();
			}			
			this.closeConnectionStore();
		}
		return id;
	}
	
	
	public void processSignPath()
	{
		int id = 0;
		while(id<523777)
		{
		   List<Product> listProducts = this.getListProducts(id);
		   if(listProducts.size()==0) break;
		   id =	this.processUrlRewrite(listProducts);
		   System.out.println(id);
		}
	}
	
	
	public void processTrungModelFormFile(String urlExcelMap) throws BiffException,	IOException {	
		//URL url = CrawlerStores.class.getResource(urlExcelMap);
		//String realPathFile = url.getFile().replaceAll("%20", " ");
		FileInputStream propsFile = new	 java.io.FileInputStream(urlExcelMap);
		Workbook w = Workbook.getWorkbook(propsFile);
		int sls = w.getNumberOfSheets();
		int j = 0;int id=0;
		Pattern pattern = Pattern.compile("\\D");
		Matcher matcher = null;
		while(j<sls)
		{
			Sheet sheet = w.getSheet(j);
			Cell cell = null;
			// Name| ID
			for (int i = 1; i < sheet.getRows(); i++) {
				try {
					cell = sheet.getCell(0, i);
					cell = sheet.getCell(1, i);
					matcher = pattern.matcher(cell.getContents().trim());					
					String str_id =  matcher.replaceAll("");
					if(StringUtil.isEmpty(str_id))continue;
					id = Integer.parseInt(str_id);	
					processTrungModel(id);
				} catch (Exception e) {
					 System.out.println("Loi KS="+id);
					 e.printStackTrace();
				}
			
			}
			j++;
		}
	}
	
	
	public void processTrungModelFormFile_2(String urlExcelMap) throws BiffException,	IOException {	
		FileInputStream propsFile = new	 java.io.FileInputStream(urlExcelMap);
		Workbook w = Workbook.getWorkbook(propsFile);
		int sls = w.getNumberOfSheets();
		int j = 0;String name="";int id=0;
		Pattern pattern = Pattern.compile("\\D");
		Matcher matcher = null;
		while(j<sls)
		{
			Sheet sheet = w.getSheet(j);
			Cell cell = null;
			// | ID | Name
			for (int i = 1; i < sheet.getRows(); i++) {
				try {
					cell = sheet.getCell(2, i);
					name = cell.getContents().trim();
					
					cell = sheet.getCell(1, i);
					matcher = pattern.matcher(cell.getContents().trim());					
					String str_id =  matcher.replaceAll("");
					if(StringUtil.isEmpty(str_id))continue;
					id = Integer.parseInt(str_id);	
					processTrungModel_2(id);
					System.out.println(i+"_id="+id+"_"+name);
				} catch (Exception e) {
					 System.out.println("Loi KS="+id);
					// e.printStackTrace();
				}
			
			}
			j++;
		}
	}
	
	public void processSetStoreAuto(String urlExcelMap) throws BiffException,	IOException {	
		FileInputStream propsFile = new	 java.io.FileInputStream(urlExcelMap);
		Workbook w = Workbook.getWorkbook(propsFile);
		int sls = w.getNumberOfSheets();
		int j = 0;String name="";int id=0;	
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		while(j<sls)
		{
			Sheet sheet = w.getSheet(j);
			Cell cell = null;
			// | ID | Name
			int k=0;
			for (int i = 1; i < sheet.getRows(); i++) {
				try {
					cell = sheet.getCell(4, i);
					name = cell.getContents().trim();
					
					this.openConnectionStore();
					String sql = "Select id from store where name = ?";
					preparedStatement = connStore.prepareStatement(sql);
					preparedStatement.setString(1, name.trim());
					rs = preparedStatement.executeQuery();
					if(rs.next())
					{
						System.out.println(k+"="+name);
						id = rs.getInt(1);
						connStore.setAutoCommit(false);
						sql = "update store set is_auto = 1 where id = "+id;
						preparedStatement = connStore.prepareStatement(sql);
						preparedStatement.execute();						
						connStore.commit();
						connStore.setAutoCommit(true);
						k++;
					}
					this.closeConnectionStore();
					
				} catch (Exception e) {
					 System.out.println("Loi KS="+id);
				}
			
			}
			j++;
		}
	}
	
	public void processProductStore()
	{
		
		String sql = "SELECT * FROM (SELECT  product_id,store_id,COUNT(product_id) AS sl FROM products_store GROUP BY product_id,store_id ) AS tb WHERE tb.sl > 1 ";
		String sql_store = "SELECT  id,product_id,store_id  FROM products_store WHERE product_id = ? AND store_id = ? ";
		String sql_store_del = "delete    FROM products_store WHERE id = ? ";
		this.openConnectionStore();
		try {
			PreparedStatement ps = connStore.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				PreparedStatement ps2 = connStore.prepareStatement(sql_store);
				ps2.setInt(1, rs.getInt(1));
				ps2.setInt(2, rs.getInt(2));
				ResultSet rs2 = ps2.executeQuery();
				int i = 0;
				connStore.setAutoCommit(false);
				while(rs2.next())
				{
					if(i>0)
					{
						int id = rs2.getInt(1);
						PreparedStatement ps3 = connStore.prepareStatement(sql_store_del);
						ps3.setInt(1,id);
						ps3.execute();
					}
					i++;
				}
				connStore.setAutoCommit(true);
			
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.closeConnectionStore();
		
	}
	
	
	public void processFieldOperator()
	{
		
		String sql_field = "SELECT fv.id FROM products_field_value fv INNER JOIN products_field f ON fv.field_id = f.id WHERE fv.operator  IS NULL  AND (f.type = 1 OR f.type=7)";
		String sql_field_update = "Update products_field_value set operator ='=' WHERE id = ? ";
	
		this.openConnectionStore();
		try {
			PreparedStatement ps = connStore.prepareStatement(sql_field);
			ResultSet rs = ps.executeQuery();
			connStore.setAutoCommit(false);
			int i = 0;
			while(rs.next())
			{
				
				PreparedStatement ps2 = connStore.prepareStatement(sql_field_update);
				ps2.setInt(1, rs.getInt(1));
				ps2.execute();
				System.out.println(new java.util.Date()+": I="+i+" Process ID="+rs.getInt(1));
				if(i%30==0) connStore.commit();
				i++;
			}
			connStore.commit();
			connStore.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.closeConnectionStore();
		
	}
	
	public void processTitleProduct(int begin)
	{
		
		String sql_field = "SELECT id,name FROM products Where id > ? order by id asc  limit 500 ";
		String sql_field_update = "Update products set name = ? WHERE id = ? ";
		int j = begin;
		int max = 600000;
		String name = "";
		Pattern pattern = Pattern.compile("\\s{2,}");
		Matcher matcher = null;
		while(j<max)
		{
			
			try {
				this.openConnectionStore();
				PreparedStatement ps = connStore.prepareStatement(sql_field);
				ps.setInt(1, j);
				ResultSet rs = ps.executeQuery();
				
				connStore.setAutoCommit(false);
				int i = 0;
				while(rs.next())
				{
					name = rs.getString("name");
					matcher = pattern.matcher(name);
					if(matcher.find())
					{
						name = matcher.replaceAll(" ");
						PreparedStatement ps2 = connStore.prepareStatement(sql_field_update);
						ps2.setString(1, name.trim());
						ps2.setInt(2, rs.getInt("id"));
						ps2.execute();
						connStore.commit();
						
						System.out.println(new java.util.Date()+": I="+i+" Process ID="+rs.getInt(1)+" Da xu ly.");
					
					}
					j=rs.getInt("id");
					System.out.println(new java.util.Date()+": I="+i+" Process ID="+rs.getInt(1));
					i++;
				}				
				connStore.setAutoCommit(true);
				this.closeConnectionStore();
				if(i==0)break;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
			
		}
	}
	
	
	
	
	public static void main(String[] args) {
		ProductModel productModel = new ProductModel();
		// Trung model
		//productModel.processTrungModel_2(528970);
		/*try {
			productModel.processTrungModelFormFile_2("/data/crawler/product_trungmodel.xls");
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		// Them field value
		//productModel.addFieldValueTypeSelect("C:/Users/Tran The Thang/Desktop/quanlygianhang/ThuocTinh/thuoc tinh/01/bo sung gia tri TT.xls");
		// Xu ly price
		int id = 0;
		while(id<600000)
		{
		   System.out.println(id);
		   List<Product> listProducts = productModel.getListProducts(id);
		   if(listProducts.size()==0) break;
		   id =	productModel.processPrice(listProducts);
		   System.out.println(id);
		}
		//productModel.processModelTrung();
		//productModel.countProductInStore();
		//productModel.processSignPath();		
		//productModel.processProductStore();
		/*try {
			productModel.processSetStoreAuto("C:/Users/Tran The Thang/Desktop/500 GH/estores_2.xls");
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	//	productModel.processFieldOperator();
		//productModel.processTitleProduct(Integer.parseInt(args[0]));
		//productModel.countNumberStoreSell();
	}
		
	
}
