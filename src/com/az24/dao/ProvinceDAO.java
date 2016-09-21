package com.az24.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.az24.crawler.store.City;
import com.az24.db.pool.C3p0ClassifiedPool;
import com.az24.db.pool.C3p0StorePool;
import com.az24.db.pool.C3p0TienIchPool;

public class ProvinceDAO {
	public int getProvince(String province_code) {
		PreparedStatement ps;
		int provice_id = 0;
		try {
			Connection connection = C3p0StorePool.getConnection();
			ps = connection.prepareStatement("SELECT  id FROM tbl_provinces   " +	" WHERE code = ? ");
			ps.setString(1, province_code);
			ResultSet resultSet =	ps.executeQuery();
			if(resultSet.next())
			{
				provice_id= resultSet.getInt(1);
			}
			C3p0ClassifiedPool.attemptClose(connection);
			C3p0ClassifiedPool.attemptClose(ps);
			C3p0ClassifiedPool.attemptClose(resultSet);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return provice_id;
	}
	
	public City getCity(String province_code) {
		PreparedStatement ps;
		City city = new City();
		try {
			Connection connection = C3p0StorePool.getConnection();
			ps = connection.prepareStatement("SELECT  id,name,code FROM tbl_provinces   " +	" WHERE code = ? ");
			ps.setString(1, province_code);
			ResultSet resultSet =	ps.executeQuery();
			if(resultSet.next())
			{
				city.id = resultSet.getInt(1);;
				city.name = resultSet.getString(2);;
				city.code = resultSet.getString(3);;
			}
			C3p0ClassifiedPool.attemptClose(connection);
			C3p0ClassifiedPool.attemptClose(ps);
			C3p0ClassifiedPool.attemptClose(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return city;
	}
	
	public int saveCity10h(com.az24.crawler.model.City city) {
		PreparedStatement ps;
		int id = 0;
		try {
			Connection connection = C3p0TienIchPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement("INSERT INTO daily_province (name, province_id, region, create_date, agribank_id) " +
					" VALUES(?,?,?,?,?)");	
			ps.setString(1,city.name);
			ps.setInt(2, city.province_id);
			ps.setInt(3,city.region);
			ps.setDate(4, city.create_date);
			ps.setInt(5,city.agribank_id);
			ps.execute();
			connection.commit();
			connection.setAutoCommit(true);		
			ps = connection.prepareStatement("SELECT LAST_INSERT_ID()");
			ResultSet rs = ps.executeQuery();
			if(rs.next()) id = rs.getInt(1);
			C3p0ClassifiedPool.attemptClose(ps);
			C3p0ClassifiedPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	
	public int saveCityBidvBank10h(com.az24.crawler.model.City city) {
		PreparedStatement ps;
		int id = 0;
		try {
			Connection connection = C3p0TienIchPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement("INSERT INTO daily_province (name, province_id, region, create_date, bidvbank_id) " +
					" VALUES(?,?,?,?,?)");	
			ps.setString(1,city.name);
			ps.setInt(2, city.province_id);
			ps.setInt(3,city.region);
			ps.setDate(4, city.create_date);
			ps.setInt(5,city.bidvbank_id);
			ps.execute();
			connection.commit();
			connection.setAutoCommit(true);		
			ps = connection.prepareStatement("SELECT LAST_INSERT_ID()");
			ResultSet rs = ps.executeQuery();
			if(rs.next()) id = rs.getInt(1);
			C3p0ClassifiedPool.attemptClose(ps);
			C3p0ClassifiedPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	
	public int updateCity10h(com.az24.crawler.model.City city) {
		PreparedStatement ps;
		int id = 0;
		try {
			Connection connection = C3p0TienIchPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement("UPDATE daily_province Set vietcombank_id = ? Where id = ? ");	
			ps.setInt(1,city.vietcombank_id);
			ps.setInt(2, city.id);			
			ps.execute();
			connection.commit();
			connection.setAutoCommit(true);		
			C3p0ClassifiedPool.attemptClose(ps);
			C3p0ClassifiedPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	public int updateCityAgribank10h(com.az24.crawler.model.City city) {
		PreparedStatement ps;
		int id = 0;
		try {
			Connection connection = C3p0TienIchPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement("UPDATE daily_province Set agribank_id = ? Where id = ? ");	
			ps.setInt(1,city.agribank_id);
			ps.setInt(2, city.id);			
			ps.execute();
			connection.commit();
			connection.setAutoCommit(true);		
			C3p0ClassifiedPool.attemptClose(ps);
			C3p0ClassifiedPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	public int updateCityBidvbank10h(com.az24.crawler.model.City city) {
		PreparedStatement ps;
		int id = 0;
		try {
			Connection connection = C3p0TienIchPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement("UPDATE daily_province Set bidvbank_id = ? Where id = ? ");	
			ps.setInt(1,city.bidvbank_id);
			ps.setInt(2, city.id);			
			ps.execute();
			connection.commit();
			connection.setAutoCommit(true);		
			C3p0ClassifiedPool.attemptClose(ps);
			C3p0ClassifiedPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	
	public int updateCityPhuongDongBank10h(com.az24.crawler.model.City city) {
		PreparedStatement ps;
		int id = 0;
		try {
			Connection connection = C3p0TienIchPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement("UPDATE daily_province Set phuongdongbank_id = ? Where id = ? ");	
			ps.setString(1,city.phuongdongbank_id);
			ps.setInt(2, city.id);			
			ps.execute();
			connection.commit();
			connection.setAutoCommit(true);		
			C3p0ClassifiedPool.attemptClose(ps);
			C3p0ClassifiedPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	public int updateCity10hTechcomBank(com.az24.crawler.model.City city) {
		PreparedStatement ps;
		int id = 0;
		try {
			Connection connection = C3p0TienIchPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement("UPDATE daily_province Set techcombank_id = ? Where id = ? ");	
			ps.setString(1,city.techcombank_id);
			ps.setInt(2, city.id);			
			ps.execute();
			connection.commit();
			connection.setAutoCommit(true);		
			C3p0ClassifiedPool.attemptClose(ps);
			C3p0ClassifiedPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	
	public int updateCity10hAnbinhBank(com.az24.crawler.model.City city) {
		PreparedStatement ps;
		int id = 0;
		try {
			Connection connection = C3p0TienIchPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement("UPDATE daily_province Set anbinh_id = ? Where id = ? ");	
			ps.setInt(1,city.anbinh_id);
			ps.setInt(2, city.id);			
			ps.execute();
			connection.commit();
			connection.setAutoCommit(true);		
			C3p0ClassifiedPool.attemptClose(ps);
			C3p0ClassifiedPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	public int updateCity10hOceanBank(com.az24.crawler.model.City city) {
		PreparedStatement ps;
		int id = 0;
		try {
			Connection connection = C3p0TienIchPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement("UPDATE daily_province Set oceanbank_id = ? Where id = ? ");	
			ps.setInt(1,city.oceanbank_id);
			ps.setInt(2, city.id);			
			ps.execute();
			connection.commit();
			connection.setAutoCommit(true);		
			C3p0ClassifiedPool.attemptClose(ps);
			C3p0ClassifiedPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	public int updateCity10hMBBank(com.az24.crawler.model.City city) {
		PreparedStatement ps;
		int id = 0;
		try {
			Connection connection = C3p0TienIchPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement("UPDATE daily_province Set mbbank_id = ? Where id = ? ");	
			ps.setString(1,city.mbbank_id);
			ps.setInt(2, city.id);			
			ps.execute();
			connection.commit();
			connection.setAutoCommit(true);		
			C3p0ClassifiedPool.attemptClose(ps);
			C3p0ClassifiedPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	public List<com.az24.crawler.model.City> getProvinceByVietcomBank() {
		PreparedStatement ps;
		List<com.az24.crawler.model.City> list = new ArrayList<com.az24.crawler.model.City>();
		com.az24.crawler.model.City city = null;
		try {
			Connection connection = C3p0TienIchPool.getConnection();
			ps = connection.prepareStatement("SELECT id,name,province_id,region,create_date,dongabank_id,vietcombank_id FROM daily_province" +
					" WHERE vietcombank_id >0 ");
			ResultSet resultSet =	ps.executeQuery();
			while(resultSet.next())
			{
				city = new com.az24.crawler.model.City();
				city.id =   resultSet.getInt(1);
				city.name =   resultSet.getString(2);
				city.province_id =   resultSet.getInt(3);
				city.region =   resultSet.getInt(4);
				city.vietcombank_id = resultSet.getInt("vietcombank_id");
				list.add(city);
			}
			
			C3p0ClassifiedPool.attemptClose(ps);
			C3p0ClassifiedPool.attemptClose(resultSet);
			C3p0ClassifiedPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	public List<com.az24.crawler.model.City> getProvinceByTechcomBank() {
		PreparedStatement ps;
		List<com.az24.crawler.model.City> list = new ArrayList<com.az24.crawler.model.City>();
		com.az24.crawler.model.City city = null;
		try {
			Connection connection = C3p0TienIchPool.getConnection();
			ps = connection.prepareStatement("SELECT id,name,province_id,region,create_date,dongabank_id,techcombank_id FROM daily_province" +
					" WHERE techcombank_id IS NOT NULL  ");
			ResultSet resultSet =	ps.executeQuery();
			while(resultSet.next())
			{
				city = new com.az24.crawler.model.City();
				city.id =   resultSet.getInt(1);
				city.name =   resultSet.getString(2);
				city.province_id =   resultSet.getInt(3);
				city.region =   resultSet.getInt(4);
				city.techcombank_id = resultSet.getString("techcombank_id");
				list.add(city);
			}
			C3p0ClassifiedPool.attemptClose(resultSet);
			C3p0ClassifiedPool.attemptClose(ps);			
			C3p0ClassifiedPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<com.az24.crawler.model.City> getProvinceByAnbinhBank() {
		PreparedStatement ps;
		List<com.az24.crawler.model.City> list = new ArrayList<com.az24.crawler.model.City>();
		com.az24.crawler.model.City city = null;
		try {
			Connection connection = C3p0TienIchPool.getConnection();
			ps = connection.prepareStatement("SELECT id,name,province_id,region,create_date,dongabank_id,anbinh_id FROM daily_province" +
					" WHERE anbinh_id IS NOT NULL  ");
			ResultSet resultSet =	ps.executeQuery();
			while(resultSet.next())
			{
				city = new com.az24.crawler.model.City();
				city.id =   resultSet.getInt(1);
				city.name =   resultSet.getString(2);
				city.province_id =   resultSet.getInt(3);
				city.region =   resultSet.getInt(4);
				city.anbinh_id = resultSet.getInt("anbinh_id");
				list.add(city);
			}
			C3p0ClassifiedPool.attemptClose(resultSet);
			C3p0ClassifiedPool.attemptClose(ps);			
			C3p0ClassifiedPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	public List<com.az24.crawler.model.City> getProvinceByAgriBank() {
		PreparedStatement ps;
		List<com.az24.crawler.model.City> list = new ArrayList<com.az24.crawler.model.City>();
		com.az24.crawler.model.City city = null;
		try {
			Connection connection = C3p0TienIchPool.getConnection();
			ps = connection.prepareStatement("SELECT id,name,province_id,region,create_date,dongabank_id,anbinh_id,agribank_id FROM daily_province" +
					" WHERE agribank_id IS NOT NULL And province_id = 0 ");
			ResultSet resultSet =	ps.executeQuery();
			while(resultSet.next())
			{
				city = new com.az24.crawler.model.City();
				city.id =   resultSet.getInt(1);
				city.name =   resultSet.getString(2);
				city.province_id =   resultSet.getInt(3);
				city.region =   resultSet.getInt(4);
				city.agribank_id = resultSet.getInt("agribank_id");
				list.add(city);
			}
			C3p0ClassifiedPool.attemptClose(resultSet);
			C3p0ClassifiedPool.attemptClose(ps);			
			C3p0ClassifiedPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	public List<com.az24.crawler.model.City> getProvinceByBIDVBank() {
		PreparedStatement ps;
		List<com.az24.crawler.model.City> list = new ArrayList<com.az24.crawler.model.City>();
		com.az24.crawler.model.City city = null;
		try {
			Connection connection = C3p0TienIchPool.getConnection();
			ps = connection.prepareStatement("SELECT id,name,province_id,region,create_date,dongabank_id,anbinh_id,bidvbank_id FROM daily_province" +
					" WHERE bidvbank_id IS NOT NULL And province_id = 0 ");
			ResultSet resultSet =	ps.executeQuery();
			while(resultSet.next())
			{
				city = new com.az24.crawler.model.City();
				city.id =   resultSet.getInt(1);
				city.name =   resultSet.getString(2);
				city.province_id =   resultSet.getInt(3);
				city.region =   resultSet.getInt(4);
				city.bidvbank_id = resultSet.getInt("bidvbank_id");
				list.add(city);
			}
			C3p0ClassifiedPool.attemptClose(resultSet);
			C3p0ClassifiedPool.attemptClose(ps);			
			C3p0ClassifiedPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<com.az24.crawler.model.City> getProvinceByPhuongDongBank() {
		PreparedStatement ps;
		List<com.az24.crawler.model.City> list = new ArrayList<com.az24.crawler.model.City>();
		com.az24.crawler.model.City city = null;
		try {
			Connection connection = C3p0TienIchPool.getConnection();
			ps = connection.prepareStatement("SELECT id,name,province_id,region,create_date,dongabank_id,anbinh_id,phuongdongbank_id FROM daily_province" +
					" WHERE phuongdongbank_id IS NOT NULL And province_id = 0 ");
			ResultSet resultSet =	ps.executeQuery();
			while(resultSet.next())
			{
				city = new com.az24.crawler.model.City();
				city.id =   resultSet.getInt(1);
				city.name =   resultSet.getString(2);
				city.province_id =   resultSet.getInt(3);
				city.region =   resultSet.getInt(4);
				city.phuongdongbank_id = resultSet.getString("phuongdongbank_id");
				list.add(city);
			}
			C3p0ClassifiedPool.attemptClose(resultSet);
			C3p0ClassifiedPool.attemptClose(ps);			
			C3p0ClassifiedPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	
	public List<com.az24.crawler.model.City> getProvinceByMBBank() {
		PreparedStatement ps;
		List<com.az24.crawler.model.City> list = new ArrayList<com.az24.crawler.model.City>();
		com.az24.crawler.model.City city = null;
		try {
			Connection connection = C3p0TienIchPool.getConnection();
			ps = connection.prepareStatement("SELECT id,name,province_id,region,create_date,dongabank_id,mbbank_id FROM daily_province" +
					" WHERE mbbank_id IS NOT NULL  ");
			ResultSet resultSet =	ps.executeQuery();
			while(resultSet.next())
			{
				city = new com.az24.crawler.model.City();
				city.id =   resultSet.getInt(1);
				city.name =   resultSet.getString(2);
				city.province_id =   resultSet.getInt(3);
				city.region =   resultSet.getInt(4);
				city.mbbank_id = resultSet.getString("mbbank_id");
				list.add(city);
			}
			C3p0ClassifiedPool.attemptClose(resultSet);
			C3p0ClassifiedPool.attemptClose(ps);			
			C3p0ClassifiedPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	public List<com.az24.crawler.model.City> getProvinceByOceanBank() {
		PreparedStatement ps;
		List<com.az24.crawler.model.City> list = new ArrayList<com.az24.crawler.model.City>();
		com.az24.crawler.model.City city = null;
		try {
			Connection connection = C3p0TienIchPool.getConnection();
			ps = connection.prepareStatement("SELECT id,name,province_id,region,create_date,dongabank_id,oceanbank_id FROM daily_province" +
					" WHERE oceanbank_id > 0 ");
			ResultSet resultSet =	ps.executeQuery();
			while(resultSet.next())
			{
				city = new com.az24.crawler.model.City();
				city.id =   resultSet.getInt(1);
				city.name =   resultSet.getString(2);
				city.province_id =   resultSet.getInt(3);
				city.region =   resultSet.getInt(4);
				city.oceanbank_id = resultSet.getInt("oceanbank_id");
				list.add(city);
			}
			C3p0ClassifiedPool.attemptClose(resultSet);
			C3p0ClassifiedPool.attemptClose(ps);			
			C3p0ClassifiedPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<com.az24.crawler.model.City> getDistrictByTechcomBank(int province_id) {
		PreparedStatement ps;
		List<com.az24.crawler.model.City> list = new ArrayList<com.az24.crawler.model.City>();
		com.az24.crawler.model.City city = null;
		try {
			Connection connection = C3p0TienIchPool.getConnection();
			ps = connection.prepareStatement( "SELECT id,name,province_id,region,create_date,dongabank_id,techcombank_id FROM daily_province" +
					" WHERE techcombank_id IS NOT NULL And province_id =  "+province_id );
			ResultSet resultSet =	ps.executeQuery();
			while(resultSet.next())
			{
				city = new com.az24.crawler.model.City();
				city.id =   resultSet.getInt(1);
				city.name =   resultSet.getString(2);
				city.province_id =   resultSet.getInt(3);
				city.region =   resultSet.getInt(4);
				city.techcombank_id = resultSet.getString("techcombank_id");
				list.add(city);
			}
			C3p0ClassifiedPool.attemptClose(resultSet);
			C3p0ClassifiedPool.attemptClose(ps);			
			C3p0ClassifiedPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	public List<com.az24.crawler.model.City> getDistrictByAnbinhBank(int province_id) {
		PreparedStatement ps;
		List<com.az24.crawler.model.City> list = new ArrayList<com.az24.crawler.model.City>();
		com.az24.crawler.model.City city = null;
		try {
			Connection connection = C3p0TienIchPool.getConnection();
			ps = connection.prepareStatement( "SELECT id,name,province_id,region,create_date,dongabank_id,anbinh_id FROM daily_province" +
					" WHERE anbinh_id IS NOT NULL And province_id =  "+province_id );
			ResultSet resultSet =	ps.executeQuery();
			while(resultSet.next())
			{
				city = new com.az24.crawler.model.City();
				city.id =   resultSet.getInt(1);
				city.name =   resultSet.getString(2);
				city.province_id =   resultSet.getInt(3);
				city.region =   resultSet.getInt(4);
				city.anbinh_id = resultSet.getInt("anbinh_id");
				list.add(city);
			}
			C3p0ClassifiedPool.attemptClose(resultSet);
			C3p0ClassifiedPool.attemptClose(ps);			
			C3p0ClassifiedPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<com.az24.crawler.model.City> getDistrictByAgriBank(int province_id) {
		PreparedStatement ps;
		List<com.az24.crawler.model.City> list = new ArrayList<com.az24.crawler.model.City>();
		com.az24.crawler.model.City city = null;
		try {
			Connection connection = C3p0TienIchPool.getConnection();
			ps = connection.prepareStatement( "SELECT id,name,province_id,region,create_date,dongabank_id,anbinh_id,agribank_id FROM daily_province" +
					" WHERE agribank_id IS NOT NULL And province_id =  "+province_id );
			ResultSet resultSet =	ps.executeQuery();
			while(resultSet.next())
			{
				city = new com.az24.crawler.model.City();
				city.id =   resultSet.getInt(1);
				city.name =   resultSet.getString(2);
				city.province_id =   resultSet.getInt(3);
				city.region =   resultSet.getInt(4);
				city.agribank_id = resultSet.getInt("agribank_id");
				list.add(city);
			}
			C3p0ClassifiedPool.attemptClose(resultSet);
			C3p0ClassifiedPool.attemptClose(ps);			
			C3p0ClassifiedPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<com.az24.crawler.model.City> getDistrictByBIDVBank(int province_id) {
		PreparedStatement ps;
		List<com.az24.crawler.model.City> list = new ArrayList<com.az24.crawler.model.City>();
		com.az24.crawler.model.City city = null;
		try {
			Connection connection = C3p0TienIchPool.getConnection();
			ps = connection.prepareStatement( "SELECT id,name,province_id,region,create_date,dongabank_id,anbinh_id,bidvbank_id FROM daily_province" +
					" WHERE bidvbank_id IS NOT NULL And province_id =  "+province_id );
			ResultSet resultSet =	ps.executeQuery();
			while(resultSet.next())
			{
				city = new com.az24.crawler.model.City();
				city.id =   resultSet.getInt(1);
				city.name =   resultSet.getString(2);
				city.province_id =   resultSet.getInt(3);
				city.region =   resultSet.getInt(4);
				city.bidvbank_id = resultSet.getInt("bidvbank_id");
				list.add(city);
			}
			C3p0ClassifiedPool.attemptClose(resultSet);
			C3p0ClassifiedPool.attemptClose(ps);			
			C3p0ClassifiedPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	public List<com.az24.crawler.model.City> getDistrictByPhuongDongBank(int province_id) {
		PreparedStatement ps;
		List<com.az24.crawler.model.City> list = new ArrayList<com.az24.crawler.model.City>();
		com.az24.crawler.model.City city = null;
		try {
			Connection connection = C3p0TienIchPool.getConnection();
			ps = connection.prepareStatement( "SELECT id,name,province_id,region,create_date,dongabank_id,anbinh_id,phuongdongbank_id FROM daily_province" +
					" WHERE phuongdongbank_id IS NOT NULL And province_id =  "+province_id );
			ResultSet resultSet =	ps.executeQuery();
			while(resultSet.next())
			{
				city = new com.az24.crawler.model.City();
				city.id =   resultSet.getInt(1);
				city.name =   resultSet.getString(2);
				city.province_id =   resultSet.getInt(3);
				city.region =   resultSet.getInt(4);
				city.phuongdongbank_id = resultSet.getString("phuongdongbank_id");
				list.add(city);
			}
			C3p0ClassifiedPool.attemptClose(resultSet);
			C3p0ClassifiedPool.attemptClose(ps);			
			C3p0ClassifiedPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public com.az24.crawler.model.City getProvinceByID(int province_id) {
		PreparedStatement ps;
		com.az24.crawler.model.City city = null;
		try {
			Connection connection = C3p0TienIchPool.getConnection();
			ps = connection.prepareStatement( "SELECT id,name,province_id,region,create_date,dongabank_id,anbinh_id FROM daily_province" +
					" WHERE  province_id =  "+province_id );
			ResultSet resultSet =	ps.executeQuery();
			if(resultSet.next())
			{
				city = new com.az24.crawler.model.City();
				city.id =   resultSet.getInt(1);
				city.name =   resultSet.getString(2);
				city.province_id =   resultSet.getInt(3);
				city.region =   resultSet.getInt(4);
				city.anbinh_id = resultSet.getInt("anbinh_id");
				
			}
			C3p0ClassifiedPool.attemptClose(resultSet);
			C3p0ClassifiedPool.attemptClose(ps);			
			C3p0ClassifiedPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return city;
	}
	
	public List<com.az24.crawler.model.City> getDistrictByMBBank(int province_id) {
		PreparedStatement ps;
		List<com.az24.crawler.model.City> list = new ArrayList<com.az24.crawler.model.City>();
		com.az24.crawler.model.City city = null;
		try {
			Connection connection = C3p0TienIchPool.getConnection();
			ps = connection.prepareStatement( "SELECT id,name,province_id,region,create_date,dongabank_id,mbbank_id FROM daily_province" +
					" WHERE mbbank_id IS NOT NULL And province_id =  "+province_id );
			ResultSet resultSet =	ps.executeQuery();
			while(resultSet.next())
			{
				city = new com.az24.crawler.model.City();
				city.id =   resultSet.getInt(1);
				city.name =   resultSet.getString(2);
				city.province_id =   resultSet.getInt(3);
				city.region =   resultSet.getInt(4);
				city.mbbank_id = resultSet.getString("mbbank_id");
				list.add(city);
			}
			C3p0ClassifiedPool.attemptClose(resultSet);
			C3p0ClassifiedPool.attemptClose(ps);			
			C3p0ClassifiedPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<com.az24.crawler.model.City> getProvinceByRegion(int region) {
		PreparedStatement ps;
		List<com.az24.crawler.model.City> list = new ArrayList<com.az24.crawler.model.City>();
		com.az24.crawler.model.City city = null;
		try {
			Connection connection = C3p0TienIchPool.getConnection();
			ps = connection.prepareStatement("SELECT id,name,province_id,region,create_date,dongabank_id FROM daily_province" +
					" WHERE region = ? and province_id = 0 ");
			ps.setInt(1, region);
			ResultSet resultSet =	ps.executeQuery();
			while(resultSet.next())
			{
				city = new com.az24.crawler.model.City();
				city.id =   resultSet.getInt(1);
				city.name =   resultSet.getString(2);
				city.province_id =   resultSet.getInt(3);
				city.region =   resultSet.getInt(4);
				city.dongabank_id = resultSet.getInt("dongabank_id");
				list.add(city);
			}
			
			C3p0ClassifiedPool.attemptClose(ps);
			C3p0ClassifiedPool.attemptClose(resultSet);
			C3p0ClassifiedPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<com.az24.crawler.model.City> getDistrictByProvince(int province_id) {
		PreparedStatement ps;
		List<com.az24.crawler.model.City> list = new ArrayList<com.az24.crawler.model.City>();
		com.az24.crawler.model.City city = null;
		try {
			Connection connection = C3p0TienIchPool.getConnection();
			ps = connection.prepareStatement("SELECT id,name,province_id,region,create_date,dongabank_id FROM daily_province" +
					" WHERE  province_id = ? ");
			ps.setInt(1, province_id);
			ResultSet resultSet =	ps.executeQuery();
			while(resultSet.next())
			{
				city = new com.az24.crawler.model.City();
				city.id =   resultSet.getInt(1);
				city.name =   resultSet.getString(2);
				city.province_id =   resultSet.getInt(3);
				city.region =   resultSet.getInt(4);
				city.dongabank_id = resultSet.getInt("dongabank_id");
				list.add(city);
			}
			
			C3p0ClassifiedPool.attemptClose(ps);
			C3p0ClassifiedPool.attemptClose(resultSet);
			C3p0ClassifiedPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public com.az24.crawler.model.City getProvinceByName(String name) {
		PreparedStatement ps;
		com.az24.crawler.model.City city = null;
		try {
			Connection connection = C3p0TienIchPool.getConnection();
			ps = connection.prepareStatement("SELECT id,name,province_id,region,create_date,dongabank_id FROM daily_province" +
					" WHERE  trim(name)like ? And province_id =0  ");
			ps.setString(1,"%"+ name.trim()+"%");
			ResultSet resultSet =	ps.executeQuery();
			if(resultSet.next())
			{
				city = new com.az24.crawler.model.City();
				city.id =   resultSet.getInt(1);
				city.name =   resultSet.getString(2);
				city.province_id =   resultSet.getInt(3);
				city.region =   resultSet.getInt(4);
				city.dongabank_id = resultSet.getInt("dongabank_id");
			}
			C3p0ClassifiedPool.attemptClose(ps);
			C3p0ClassifiedPool.attemptClose(resultSet);
			C3p0ClassifiedPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return city;
	}
	
	
	public com.az24.crawler.model.City getDistrictByName(String name) {
		PreparedStatement ps;
		com.az24.crawler.model.City city = null;
		try {
			Connection connection = C3p0TienIchPool.getConnection();
			ps = connection.prepareStatement("SELECT id,name,province_id,region,create_date,dongabank_id FROM daily_province" +
					" WHERE  trim(name)like ? And province_id > 0  ");
			ps.setString(1,"%"+ name.trim()+"%");
			ResultSet resultSet =	ps.executeQuery();
			if(resultSet.next())
			{
				city = new com.az24.crawler.model.City();
				city.id =   resultSet.getInt(1);
				city.name =   resultSet.getString(2);
				city.province_id =   resultSet.getInt(3);
				city.region =   resultSet.getInt(4);
				city.dongabank_id = resultSet.getInt("dongabank_id");
			}
			C3p0ClassifiedPool.attemptClose(ps);
			C3p0ClassifiedPool.attemptClose(resultSet);
			C3p0ClassifiedPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return city;
	}
	
	public com.az24.crawler.model.City getProvinceByName(String name,int province_id) {
		PreparedStatement ps;
		com.az24.crawler.model.City city = null;
		try {
			Connection connection = C3p0TienIchPool.getConnection();
			ps = connection.prepareStatement("SELECT id,name,province_id,region,create_date,dongabank_id FROM daily_province" +
					" WHERE  trim(name)like ? and province_id = "+province_id);
			ps.setString(1,"%"+ name.trim()+"%");
			ResultSet resultSet =	ps.executeQuery();
			while(resultSet.next())
			{
				city = new com.az24.crawler.model.City();
				city.id =   resultSet.getInt(1);
				city.name =   resultSet.getString(2);
				city.province_id =   resultSet.getInt(3);
				city.region =   resultSet.getInt(4);
				city.dongabank_id = resultSet.getInt("dongabank_id");
			}
			C3p0ClassifiedPool.attemptClose(ps);
			C3p0ClassifiedPool.attemptClose(resultSet);
			C3p0ClassifiedPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return city;
	}
}
