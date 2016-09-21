package com.az24.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.az24.crawler.model.ATM;
import com.az24.crawler.model.Bus;
import com.az24.crawler.model.Gold;
import com.az24.crawler.model.Money;
import com.az24.crawler.model.Taxi;
import com.az24.crawler.model.Tivi;
import com.az24.crawler.model.Weather;
import com.az24.db.pool.C3p0TienIchPool;
import com.az24.db.pool.C3p0TinhHinhPool;

public class TienIchDAO {
	
	public void saveWeather(Weather weather) {		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0TienIchPool.getConnection(); 
				conn.setAutoCommit(false);
				
				ps = conn.prepareStatement("INSERT INTO daily_weather (province_id,temperature_f," +
						" temperature_t,humidity,info,create_date,wind )	VALUES (?,?,?,?,?,?,?)");
				ps.setInt(1, weather.province_id);
				ps.setFloat(2, weather.temperature_f);
				ps.setFloat(3, weather.temperature_t);
				ps.setFloat(4, weather.humidity);
				ps.setString(5, weather.info);
				ps.setDate(6, weather.create_date);
				ps.setString(7, weather.wind);
				ps.execute();
				conn.commit();
				conn.setAutoCommit(true);
				
			C3p0TinhHinhPool.attemptClose(ps);
			C3p0TinhHinhPool.attemptClose(conn);			
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}	
	
	
	public void saveGold(Gold gold) {		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0TienIchPool.getConnection(); 
				conn.setAutoCommit(false);
				ps = conn.prepareStatement("INSERT INTO 10h_tienich.daily_gold  (" +
						" provider,province_id,unit,buy,sale,create_date) VALUES " +
						" (?,?,?,?,?,?)");
				ps.setString(1, gold.provider);
				ps.setInt(2, gold.getProvince_id());
				ps.setString(3, gold.getUnit());
				ps.setString(4, gold.getBuy());
				ps.setString(5, gold.getSale());				
				ps.setDate(6, gold.create_date);
				ps.execute();
				conn.commit();
				conn.setAutoCommit(true);
			C3p0TinhHinhPool.attemptClose(ps);
			C3p0TinhHinhPool.attemptClose(conn);			
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}	
	
	public void saveMoney(Money money) {		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0TienIchPool.getConnection(); 
				conn.setAutoCommit(false);
				ps = conn.prepareStatement("INSERT INTO daily_foreign_currency (unit,buy,sale,transfer,create_date)" +
						" VALUES (?,?,?,?,?);");
				ps.setString(1, money.unit);
				ps.setFloat(2, money.buy);
				ps.setFloat(3, money.sale);
				ps.setFloat(4, money.tranfer);								
				ps.setDate(5, money.create_date);
				ps.execute();
				conn.commit();
				conn.setAutoCommit(true);
			C3p0TinhHinhPool.attemptClose(ps);
			C3p0TinhHinhPool.attemptClose(conn);			
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}	
	
	public void saveTivi(Tivi tivi) {		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0TienIchPool.getConnection(); 
				conn.setAutoCommit(false);
				ps = conn.prepareStatement("INSERT INTO daily_tivi(provider,channel,program,time_play,create_date)" +
						" 	VALUES (?,?,?,?,?);");
				ps.setString(1, tivi.provider);
				ps.setString(2, tivi.channel);
				ps.setString(3, tivi.program);
				ps.setString(4, tivi.time_play);								
				ps.setDate(5, tivi.create_date);
				ps.execute();
				conn.commit();
				conn.setAutoCommit(true);
			C3p0TinhHinhPool.attemptClose(ps);
			C3p0TinhHinhPool.attemptClose(conn);			
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}	
	
	public void saveTaxi(Taxi taxi) {		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0TienIchPool.getConnection(); 
				conn.setAutoCommit(false);
				ps = conn.prepareStatement("INSERT INTO 10h_tienich.daily_taxi (firm,phone" +
						",province_id,create_date ) VALUES (?,?,?,?)");
				ps.setString(1, taxi.firm);
				ps.setString(2, taxi.phone);
				ps.setInt(3, taxi.province_id);
				ps.setDate(4, taxi.create_date);
				ps.execute();
				conn.commit();
				conn.setAutoCommit(true);
			C3p0TinhHinhPool.attemptClose(ps);
			C3p0TinhHinhPool.attemptClose(conn);			
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}	
	
	public void saveATM(ATM atm) {		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0TienIchPool.getConnection(); 
				conn.setAutoCommit(false);
				ps = conn.prepareStatement("INSERT INTO daily_atm " +
						" (province_id, POSITION, address, has_send_money, bank,atm_number, create_date,district_id) VALUES (?,?,?,?,?,?,?,?) ");
				ps.setInt(1, atm.province_id);
				ps.setString(2, atm.position);
				ps.setString(3, atm.address);
				ps.setInt(4, atm.has_send_money);
				ps.setString(5, atm.bank);
				ps.setInt(6, atm.atm_number);
				ps.setDate(7, atm.create_date);
				ps.setInt(8, atm.district_id);
				ps.execute();
				conn.commit();
				conn.setAutoCommit(true);
			C3p0TinhHinhPool.attemptClose(ps);
			C3p0TinhHinhPool.attemptClose(conn);			
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	
	public void saveBUS(Bus bus) {		
		PreparedStatement ps;		
		try {
			Connection conn = C3p0TienIchPool.getConnection(); 
				conn.setAutoCommit(false);
				ps = conn.prepareStatement("INSERT INTO daily_bus (CODE, NAME, uptime, frequency, path_1, path_2, create_date,other)	" +
						" VALUES (?,?,?,?,?,?,?,?);");
				ps.setString(1, bus.code);
				ps.setString(2, bus.name);
				ps.setString(3, bus.uptime);
				ps.setString(4, bus.frequency);
				ps.setString(5, bus.path_1);
				ps.setString(6, bus.path_2);
				ps.setDate(7, bus.create_date);
				ps.setString(8, bus.other);
				ps.execute();
				conn.commit();
				conn.setAutoCommit(true);
			C3p0TinhHinhPool.attemptClose(ps);
			C3p0TinhHinhPool.attemptClose(conn);			
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	
}
