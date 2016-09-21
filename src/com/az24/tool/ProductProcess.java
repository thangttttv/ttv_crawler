package com.az24.tool;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import com.az24.crawler.store.Category;
import com.az24.dao.StoreCagegoryDAO;
import com.az24.dao.StoreDAO;

public class ProductProcess {
	public Connection connStore;
	
	public void openConnectionStore() {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			connStore = DriverManager
					.getConnection(
							"jdbc:mysql://210.211.97.16:3306/az24_store?autoReconnect=true&characterEncoding=UTF-8",
							"quangpn", "21quang11");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
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
	
	public void tkStoreByArea()
	{
		this.openConnectionStore();
		try {
			PreparedStatement preparedStatement = connStore.prepareStatement("SELECT cat_id,COUNT(*) as sl FROM store_area GROUP BY cat_id ");
			ResultSet rs = preparedStatement.executeQuery();
			connStore.setAutoCommit(false);
			while(rs.next())
			{
				int cat_id  = rs.getInt(1);				
				int sl = rs.getInt(2);
				System.out.println("id="+cat_id);
				System.out.println("sl="+sl);
				String sqlUpdate  = "update tbl_category set num_of_store = ? where id = ? ";
				PreparedStatement psu = connStore.prepareStatement(sqlUpdate);
				psu.setInt(1, sl);
				psu.setInt(2, cat_id);
				psu.execute();
			}
			connStore.setAutoCommit(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		this.closeConnectionStore();
	}
	
	
	public List<Category> getListCategory()
	{
		this.openConnectionStore();
		List<Category> listCate = new ArrayList<Category>();
		try {
			PreparedStatement preparedStatement = connStore.prepareStatement("SELECT id,NAME,LEVEL  FROM tbl_category");
			ResultSet rs = preparedStatement.executeQuery();
			connStore.setAutoCommit(false);
			int id=0;
			while(rs.next())
			{
				id  = rs.getInt(1);				
				String name = rs.getString(2);
				int level = rs.getInt(3);
				System.out.println("id="+id);
				System.out.println("sl="+name);
				Category category = new Category();
				category.id = id;
				category.level = level;
				category.name = name;
				listCate.add(category);
			}
			connStore.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.closeConnectionStore();
		return listCate;
	}
	
	public List<Category> getListCategoryByLevel(int level)
	{
		this.openConnectionStore();
		List<Category> listCate = new ArrayList<Category>();
		try {
			PreparedStatement preparedStatement = connStore.prepareStatement("SELECT id,NAME,LEVEL  FROM tbl_category Where level =  "+level);
			ResultSet rs = preparedStatement.executeQuery();
			connStore.setAutoCommit(false);
			int id=0;
			while(rs.next())
			{
				id  = rs.getInt(1);				
				String name = rs.getString(2);
				level = rs.getInt(3);
				System.out.println("id="+id);
				System.out.println("sl="+name);
				Category category = new Category();
				category.id = id;
				category.level = level;
				category.name = name;
				listCate.add(category);
			}
			connStore.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.closeConnectionStore();
		return listCate;
	}
	
	public List<Category> getListCategoryByParentID(int parent_id)
	{
		this.openConnectionStore();
		List<Category> listCate = new ArrayList<Category>();
		try {
			PreparedStatement preparedStatement = connStore.prepareStatement("SELECT id,NAME,LEVEL,num_of_products,num_of_store  FROM tbl_category Where parent_id =  "+parent_id);
			ResultSet rs = preparedStatement.executeQuery();
			connStore.setAutoCommit(false);
			int id=0;
			while(rs.next())
			{
				id  = rs.getInt(1);				
				String name = rs.getString(2);
				int level = rs.getInt(3);
				System.out.println("id="+id);
				System.out.println("sl="+name);
				Category category = new Category();
				category.id = id;
				category.level = level;
				category.name = name;
				category.num_of_products = rs.getInt(4);
				category.num_of_store = rs.getInt(5);
				
				listCate.add(category);
			}
			connStore.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.closeConnectionStore();
		return listCate;
	}
	
	
	public  void countProduct(List<Category> listcategory)
	{
		PreparedStatement preparedStatement = null;
		PreparedStatement psu = null;
		for (Category category : listcategory) {
			this.openConnectionStore();
			try {
				String sql = "Select count(*) as sl from products where  cat_id"+category.level + " = " + category.id;
				
				preparedStatement = connStore.prepareStatement(sql);
				ResultSet rs = preparedStatement.executeQuery();
				if(rs.next()){
					int sl =  rs.getInt(1);		
					System.out.println(category.id+"="+sl);					
					connStore.setAutoCommit(false);
					psu = connStore.prepareStatement("update tbl_category set num_of_products = ? where id = ? ");
					psu.setInt(1, sl);
					psu.setInt(2, category.id);
					psu.execute();
					connStore.setAutoCommit(true);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			this.closeConnectionStore();
		
		}
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
	
	public void exportCategory() throws IOException, WriteException
	{
		List<Category> list1 = this.getListCategoryByLevel(1);
		WritableWorkbook workbook = Workbook
		.createWorkbook(new java.io.File("d:/categorys2/store_category"+ ".xls"));
		WritableSheet s = workbook.createSheet("Sheet_" + 1, 0);
	
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
		Label l = new Label(0, 0, "STT", cf);
		s.addCell(l);
		l = new Label(1, 0, "Category_1", cf);
		s.addCell(l);
		l = new Label(2, 0, "Category_2", cf);
		s.addCell(l);
		l = new Label(3, 0, "Product", cf);
		s.addCell(l);
		l = new Label(4, 0, "Store", cf);
		s.addCell(l);
		
		int row = 1;
		int row_2 = 1;
		int products = 0;
		int stores = 0;
		for (Category category : list1) {		
			
			List<Category> list2 = this.getListCategoryByParentID(category.id);
			products = 0;
			stores = 0;
			int sl = 0;
			for (Category category2 : list2) {
				row_2++;				
				l = new Label(1, row_2, "", cf);
				s.addCell(l);
				l = new Label(2, row_2, category2.name, cf);
				s.addCell(l);
				l = new Label(3, row_2, category2.num_of_products+"", cf);
				s.addCell(l);
				l = new Label(4, row_2, category2.num_of_store+"", cf);
				s.addCell(l);
				
				products += category2.num_of_products;
				stores += category2.num_of_store;
				sl++;
			}
			row_2++;
			l = new Label(1, row, category.name, cf);
			s.addCell(l);
			l = new Label(2, row, "", cf);
			s.addCell(l);
			l = new Label(3, row, products+"", cf);
			s.addCell(l);
			l = new Label(4, row,stores+ "", cf);
			s.addCell(l);
			
			row +=sl+1;
		}
		
		workbook.write();
		workbook.close();
		
	}
	public static void main(String[] args) {
		ProductProcess process = new ProductProcess();
		//List<Category> listcategory = process.getListCategory();
		//process.countProduct(listcategory);
		process.tkStoreByArea();
		try {
			process.exportCategory();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
