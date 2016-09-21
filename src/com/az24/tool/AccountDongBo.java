package com.az24.tool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.az24.crawler.model.product.Product;

public class AccountDongBo {
	
	public Connection connLog;
	public Connection connAccount;
	public Connection connStore;
	public Connection connProduct;

	public List<UserAccount> getData() {
		List<UserAccount> listCats = new ArrayList<UserAccount>();
		try {
			PreparedStatement ps = connLog
					.prepareStatement(" SELECT 	u.userid, u.username, a.use_account FROM forum_user u LEFT JOIN  users_account  a ON u.userid = a.use_id ORDER BY u.userid ASC ");

			ResultSet resultSet = ps.executeQuery();
			int i = 0;
			while (resultSet.next()) {
				UserAccount account = new UserAccount();
				account.user_id = resultSet.getInt(1);
				account.username = resultSet.getString(2);
				account.account = resultSet.getDouble(3);
				listCats.add(account);
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listCats;

	}
	
	public List<Product> getProducts(int id) {
		List<Product> listCats = new ArrayList<Product>();
		try {
			PreparedStatement ps = connStore
					.prepareStatement(" SELECT id from products where id > "+id+"  order by id asc limit 0,100 ");

			ResultSet resultSet = ps.executeQuery();
			int i = 0;
			while (resultSet.next()) {
				Product account = new Product();
					account.id = resultSet.getInt("id");					
					listCats.add(account);
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listCats;

	}
	
	
	
	
	public List<User> getHDCUSER(int id) {
		List<User> listCats = new ArrayList<User>();
		try {
			PreparedStatement ps = connAccount
					.prepareStatement(" SELECT id,username,province from hdc_user where id > "+id+"  order by id asc limit 0,100 ");

			ResultSet resultSet = ps.executeQuery();
			int i = 0;
			while (resultSet.next()) {
				User account = new User();
					account.userid = resultSet.getInt("id");
					account.username = resultSet.getString("id");
					account.provice = resultSet.getString("province");
					listCats.add(account);
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listCats;

	}

	private void updatAccount(int user_id,double account) {
		PreparedStatement ps;
		try {

			connAccount.setAutoCommit(false);
			ps = connAccount
					.prepareStatement("Update hdc_account  SET main_account = "
							+ account + " WHERE user_id = " + user_id);
			ps.execute();
			connAccount.commit();
			connAccount.setAutoCommit(true);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	private void saveAccount(int user_id,String username,double account) {
		PreparedStatement ps;
		try {

			connAccount.setAutoCommit(false);
			ps = connAccount
					.prepareStatement("INSERT INTO account.hdc_account (user_id,username,main_account,sub_account)" +
							" VALUES (?,?,?,?)");
			ps.setInt(1, user_id);
			ps.setString(2, username);
			ps.setDouble(3, account);
			ps.setDouble(4,0);
			ps.execute();
			connAccount.commit();
			connAccount.setAutoCommit(true);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	private void saveProductHit(int product_id,long time) {
		PreparedStatement ps;
		try {

			connStore.setAutoCommit(false);
			ps = connStore
					.prepareStatement("INSERT INTO az24_store.products_hit 	(product_id, views, last_date" +
							") VALUES 	(?,1,?)");
			ps.setInt(1, product_id);		
			ps.setLong(2, time);
			ps.execute();
			connStore.commit();
			connStore.setAutoCommit(true);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}


	public void openConnectionProduct() {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			connLog = DriverManager
					.getConnection(
							"jdbc:mysql://210.211.97.16:3306/az24_store?autoReconnect=true&characterEncoding=UTF-8",
							"quangpn", "21quang11");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	

	public void openConnection() {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			connLog = DriverManager
					.getConnection(
							"jdbc:mysql://192.168.1.101:3306/raovat2011?characterEncoding=UTF-8",
							"synuser", "SynUser2011");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public void openConnectionAccount() {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			connAccount = DriverManager
					.getConnection(
							"jdbc:mysql://210.211.97.11:3306/account?characterEncoding=UTF-8",
							"quangpn", "QuangPN2011@!!!*^");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	
	public void openConnectionStore() {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			connStore = DriverManager
					.getConnection(
							"jdbc:mysql://192.168.1.103:3306/az24_store?autoReconnect=true&characterEncoding=UTF-8",
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
	public void closeConnection() {
		try {
			if (connLog != null)
				connLog.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void closeConnectionAccount() {
		try {
			if (connAccount != null)
				connAccount.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public int  getProvinceID(String province_name)
	{
		PreparedStatement ps;
		int province_id = 0;
		try {
			ps = connStore.prepareStatement("SELECT 	id,name,parent_id,	order_province,	code FROM	az24_store.tbl_provinces WHERE name = '"+province_name+"'");
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				province_id = rs.getInt("id");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return province_id;

	}
	
	
	public List<UserAccount> getAccounts(int id)
	{
		PreparedStatement ps;
		List<UserAccount> listAccount = new ArrayList<UserAccount>();
		try {
			UserAccount userAccount = null;
			ps = connAccount.prepareStatement("SELECT user_id,username, main_account,sub_account FROM account.hdc_account " +
					" WHERE user_id > ? LIMIT 0, 100");
			System.out.println("SELECT user_id,username, main_account,sub_account FROM account.hdc_account " +
					" WHERE user_id > ? LIMIT 0, 100");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				userAccount = new UserAccount();
				userAccount.user_id = rs.getInt("user_id");
				userAccount.main_account = rs.getDouble("main_account");
				userAccount.sub_account = rs.getDouble("sub_account");
				userAccount.username = rs.getString("username");
				listAccount.add(userAccount);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listAccount;

	}
	
	public double getSpend(int id)
	{
		PreparedStatement ps;
		double spend_money = 0;
		try {
			ps = connAccount.prepareStatement("SELECT SUM(spend_money_main)-SUM(charge_money)  FROM hdc_transaction WHERE user_id = ? AND type_account_change = 1");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				spend_money = rs.getDouble(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return spend_money;

	}
	
	
	public void insertTransaction(int user_id,String username,double charge_money, long time) {
		PreparedStatement ps;
		try {
			connAccount.setAutoCommit(false);
			ps = connAccount
					.prepareStatement("insert into account.hdc_transaction " +
							" 	( user_id, username,	" +						
							"	  charge_money, charge_method,  main_account, transaction_type," +
							"  	  type_account_change, reason,	transaction_time )" +
							"   values (?,?,?,?,?,?,?,?,?)");
			ps.setInt(1, user_id);
			ps.setString(2, username);
			ps.setDouble(3, charge_money);
			ps.setString(4,"directly");
			ps.setDouble(5, charge_money);
			ps.setString(6, "1");
			ps.setString(7, "1");
			ps.setString(8, "Cộng chuyển sổ");
			ps.setLong(9, time);
			ps.execute();
			connAccount.commit();
			connAccount.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	private void updatTBLUser(int user_id,int province_id) {
		PreparedStatement ps;
		try {

			connStore.setAutoCommit(false);
			ps = connStore
					.prepareStatement("Update tbl_user  SET province_id = "
							+ province_id + " WHERE id = " + user_id);
			ps.execute();
			connStore.commit();
			connStore.setAutoCommit(true);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void main3(String[] args) {
		AccountDongBo productCatHit = new AccountDongBo();		
		int id = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH,8);
		calendar.set(Calendar.YEAR,2011);
		
		while(id<=485402)
		{
			productCatHit.openConnectionStore();			
			List<Product> accounts = productCatHit.getProducts(id);
			for (Product product : accounts) {
				System.out.println("id="+product.id);		
				productCatHit.saveProductHit(product.id, calendar.getTimeInMillis()/1000);
				id = product.id;
			}
			productCatHit.closeConnectionStore();			
			if(accounts.size()==0)break;
		}	
	}
	
		
	public static void main(String[] args) {
		AccountDongBo productCatHit = new AccountDongBo();		
		int id = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH,8);
		calendar.set(Calendar.YEAR,2011);
		
		while(id<=89855)
		{
			productCatHit.openConnectionAccount();
			productCatHit.openConnectionStore();
			List<User> accounts = productCatHit.getHDCUSER(id);
			for (User account : accounts) {
				System.out.println("id="+account.username);		
				int province_id = productCatHit.getProvinceID(account.provice);
				if(province_id>0) productCatHit.updatTBLUser(account.userid, province_id);
				id = account.userid;
			}
			productCatHit.closeConnectionAccount();
			productCatHit.closeConnectionStore();
			if(accounts.size()==0)break;
		}	
	}
	
	
	
	public static void main2(String[] args) {
		AccountDongBo productCatHit = new AccountDongBo();		
		int id = 0;
		double money = 0;
		double spend_nap = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH,8);
		calendar.set(Calendar.YEAR,2011);
		long time  = calendar.getTimeInMillis()/1000;
		while(id<=86855)
		{
			productCatHit.openConnectionAccount();
			List<UserAccount> accounts = productCatHit.getData();
			for (UserAccount account : accounts) {
				   
					System.out.println("id="+account.username);		
					spend_nap = productCatHit.getSpend(account.user_id);
					money =account.main_account + spend_nap;
					if(money>0)
					productCatHit.insertTransaction(account.user_id, account.username,money,time);
					id = account.user_id;
			}
			
			productCatHit.closeConnectionAccount();
		
		}	
	}
	
	

	
	
	
	public static void main1(String[] args) {
		AccountDongBo productCatHit = new AccountDongBo();
		productCatHit.openConnection();
		productCatHit.openConnectionAccount();
		List<UserAccount> accounts = productCatHit.getData();
		

		for (UserAccount account : accounts) {
			   
				System.out.println("id="+account.username);				
				productCatHit.saveAccount(account.user_id, account.username, account.account);
 
		}

		productCatHit.closeConnection();
		productCatHit.closeConnectionAccount();

	}
}
