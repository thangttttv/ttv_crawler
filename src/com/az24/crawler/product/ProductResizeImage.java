package com.az24.crawler.product;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import com.az24.crawler.model.ImageSize;
import com.az24.crawler.model.product.Product;
import com.mortennobel.imagescaling.ThumpnailRescaleOp;


public class ProductResizeImage implements Runnable {
	public Connection connLog;
	public int cat_id;
	public ProductResizeImage(int cat_id) {
		this.cat_id = cat_id;
	}

	public void openConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connLog = DriverManager.getConnection("jdbc:mysql://192.168.1.102:3306/az24_store?characterEncoding=UTF-8",
					"product", "product!@#$%^");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void closeConnection() {
		try {
			if (connLog != null)
				connLog.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Product> getImages(String table) {
		List<Product> listProduct = new ArrayList<Product>();
		try {
			PreparedStatement ps = connLog
					.prepareStatement("select id,picture from " + table
							+ " where cat_id = ? ");
			ps.setInt(1, cat_id);
			ResultSet resultSet = ps.executeQuery();
			int i = 0;
			while (resultSet.next()) {
				Product product = new Product();
				product.id = resultSet.getInt(1);
				product.pictrue = resultSet.getString(2);
				listProduct.add(product);
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listProduct;
	}
	
	public List<Product> getImages(String table,int cate_id) {
		List<Product> listProduct = new ArrayList<Product>();
		try {
			PreparedStatement ps = connLog
					.prepareStatement("select id,picture from " + table
							+ " where cat_id = ? ");
			ps.setInt(1, cate_id);
			ResultSet resultSet = ps.executeQuery();
			int i = 0;
			while (resultSet.next()) {
				Product product = new Product();
				product.id = resultSet.getInt(1);
				product.pictrue = resultSet.getString(2);
				listProduct.add(product);
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listProduct;
	}
	
	
	public List<Integer> getCates(int parent_id) {
		List<Integer> listProduct = new ArrayList<Integer>();
		try {
			PreparedStatement ps = connLog
					.prepareStatement("SELECT id FROM tbl_category WHERE id NOT IN (SELECT parent_id FROM tbl_category) AND parent_id NOT IN (80,3,6,4,5,90,88,85,92,83,81,86,3256,84,3255,82,87,89,91,594,597,595,598,596,599) AND parent_id = ? ");
			ps.setInt(1,parent_id);
			ResultSet resultSet = ps.executeQuery();
			int i = 0;
			while (resultSet.next()) {
				int id = resultSet.getInt(1);
				
				listProduct.add(id);
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listProduct;
	}
	
	public void run() {
		try {
			openConnection();
			List<Integer> listcat = this.getCates(cat_id);
			for (Integer integer : listcat) {
				this.openConnection();
				List<Product> listProduct = this.getImages("products",integer);
				System.out.println("SL="+listProduct.size());
				int i = 0;
				while (i < listProduct.size()) {
					Product product = listProduct.get(i);
					System.out.println(new Date().toString()+"-->"+product.pictrue);
					resizeImage(product);
					i++;
				}
				Thread.sleep(5);
				closeConnection();
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void resizeImage(Product product) {

		BufferedImage source;
		try {
			String pic = product.pictrue.replaceFirst("small_", "");
			
			String duoi = pic.substring(pic.lastIndexOf(".")+1, pic.length());
			source = ImageIO.read(new File(
					"/usr/src/java/tomcat7/webapps/images.az24.vn/" + pic));
			BufferedImage des = null;

			ImageSize imageSize = getImageSizeSmall(source.getWidth(),
					source.getHeight());
			ThumpnailRescaleOp op = new ThumpnailRescaleOp(imageSize.w,
					imageSize.h);
			des = op.filter(source, des);
			ImageIO.write(des, duoi, new File(
					"/usr/src/java/tomcat7/webapps/images.az24.vn/"
							+ product.pictrue));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static ImageSize getImageSizeSmall(int width, int height) {
		ImageSize imageSize = new ImageSize();
		int w = 125, h = 125;
		double tyle = 0;
		if (width > height) {

			w = 150;
			tyle = (double) height / width;
			h = Integer.parseInt(Math.round(w * tyle) + "");
			imageSize.w = w;
			imageSize.h = h;
			return imageSize;

		} else {
			h = 150;
			tyle = (double) width / height;
			w = Integer.parseInt(Math.round(h * tyle) + "");
			imageSize.w = w;
			imageSize.h = h;
			return imageSize;
		}

	}

	public static void main(String[] args) {
		int cat_id = Integer.parseInt(args[0]);
		System.out.println(cat_id);
		ProductResizeImage resizeImage = new ProductResizeImage(cat_id);
		resizeImage.run();
	}

}
