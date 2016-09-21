package com.az24.crawler.store;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import com.az24.dao.ProvinceDAO;
import com.az24.dao.StoreCagegoryDAO;
import com.az24.dao.StoreDAO;

public class CrawlerStoreCreateStore {

	public List<Store> getStoreFromExcel(String urlExcelMap)
			throws BiffException, IOException {
		//URL url = CrawlerStore.class.getResource(urlExcelMap);
		//String realPathFile = url.getFile().replaceAll("%20", " ");
		FileInputStream propsFile = new java.io.FileInputStream(urlExcelMap);
		Workbook w = Workbook.getWorkbook(propsFile);
		int sls = w.getNumberOfSheets();
		int k=0;
		List<Store> lisStore = new ArrayList<Store>();
		Pattern pattern = Pattern.compile("^\\W|\\W$");
		
		while(k<sls)
		{
			Sheet sheet = w.getSheet(k);
			Cell cell = null;
			Store store = null;
			
			int type_menu = 1;
			for (int i = 1; i < sheet.getRows(); i++) {
				try {
					store = new Store();
					cell = sheet.getCell(1, i);
					String name = cell.getContents();
					Matcher matcher = pattern.matcher(name);
					name = matcher.replaceAll("");
					
					store.name = name.trim();
					cell = sheet.getCell(3, i);
					
					type_menu = Integer.parseInt(cell.getContents());
					store.type_menu = type_menu;
					
					cell = sheet.getCell(4, i);
					String store_alias = cell.getContents();
					matcher = pattern.matcher(store_alias);
					store_alias = matcher.replaceAll("");
					
					store.store_alias = store_alias.trim();
					System.out.println(cell.getContents());
					cell = sheet.getCell(5, i);
					String areas = cell.getContents();
					
					matcher = pattern.matcher(areas);
					areas = matcher.replaceAll("");
					
					store.area = areas;
	
					cell = sheet.getCell(6, i);
					String email = cell.getContents();
					matcher = pattern.matcher(email);
					email = matcher.replaceAll("");
					
					store.email = email;
	
					cell = sheet.getCell(7, i);
					String mobile = cell.getContents();
					matcher = pattern.matcher(mobile);
					mobile = matcher.replaceAll("");
					
					store.mobile = mobile;
	
					cell = sheet.getCell(8, i);
					String address = cell.getContents();
					matcher = pattern.matcher(address);
					address = matcher.replaceAll("");
					store.address = address;
	
					cell = sheet.getCell(9, i);
					String city = cell.getContents();
					matcher = pattern.matcher(city);
					city = matcher.replaceAll("");
					
					store.province_code = city;
	
					lisStore.add(store);
				} catch (Exception e) {
					// TODO: handle exception
				}
	
			}
			k++;
		}
		return lisStore;
	}

	public static void main(String[] args) throws Exception {
		CrawlerStore crawlerStore = new CrawlerStore();
		ProvinceDAO provinceDAO = new ProvinceDAO();
		CategoryExtractor categoryExtractor = new CategoryExtractor();
		StoreCagegoryDAO cagegoryDAO = new StoreCagegoryDAO();
		CrawlerStoreCreateStore crawlerStoreCreateStore = new CrawlerStoreCreateStore();
		StoreDAO storeDAO = new StoreDAO();
		List<Store> listStore = crawlerStoreCreateStore
				.getStoreFromExcel("C:/Users/Tran The Thang/Desktop/500 GH/estore_9.xls");
		
		int i = 884;
		for (Store store : listStore) {
			if(storeDAO.checkStore(store.store_alias.trim())>0) continue;
			
			int store_id = storeDAO.checkStore(store.store_alias.trim());
			
			String urlGH = "http://www.vatgia.com/" + store.store_alias;
			String store_alias = store.store_alias.trim();;
			//int user_id = 0;
			String user_name = "sell2011_"+i;
			String full_name = "sell2011_"+i;
			String user_email = store.email;
			String user_password = "az@$2011";
			String user_address = store.address;
			Date user_birthday = null;
			int user_sex = 1;
			String user_skype = "";
			String store_name = store.name.trim();
			String store_fax = " ";
			String domain = "";

			String store_mobile = store.mobile.trim();;
			String store_tel = "";
			String store_address = store.address.trim();;
			String store_web = "";
			String city_code = store.province_code.trim();;
			String store_area = "";
			int type = 1;// 0.Cơ bản,1.Chuyên nghiệp,2.Đảm bảo;
			System.out.println(store.name);
			//if(storeDAO.checkStore(store.name.trim())>0){i++; continue;}
			City city = provinceDAO.getCity(city_code);
			if (city == null) {
				System.out.println("Khong Tim Thay Tinh Thanh ");
				return;
			}
			
			System.out.println(store.name);System.out.println(store.email);
			System.out.println(store.mobile);System.out.println(store.address);
			System.out.println(store.province_code);System.out.println(store.type_menu);
			
			
			// BUOC 1 TAO USER
			// Dang ki user -> Cac thong tin nay nhap tu file excel
			int user_id = crawlerStore.registerUser(user_name, full_name,
					user_email, user_password, store_mobile, city.name,
					"Viet Nam", user_address, user_birthday, user_sex,
					store_tel, store_tel, user_skype);
			
			if (user_id == 0) {
				System.out.println("Dang ki user khong thanh cong ");
				return;
			}

			// BUOC 2 DANG KI STORE
			// get city_id

			// Tao store

			store_id = crawlerStore.registerStore(user_id, user_name,
					store_name, store_mobile, store_tel, store_fax,
					store_address, domain, store_web, city.id, city.name,
					store_area, store_alias, type);
					

			
			if (store_id == 0) {
				System.out.println("Dang ki store khong thanh  cong ");
				return;
			}
			
			String arrAreas[] = store.area.split(",");
			for (String string : arrAreas) {
				try{
					int catId = Integer.parseInt(string);
					storeDAO.saveStoreCategory(store_id, catId);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}

			// Config mac dinh

			StoreConfig storeConfig = crawlerStore.extractBaseInfoGH(urlGH,
					store_alias);
			crawlerStore.createDefaultConfig(store_id, storeConfig.footer_info,
					storeConfig.intro, storeConfig.contact);
			// Tao menu default
			crawlerStore.createMenuDefault(store_id, store_alias);
			Thread.sleep(10);
			// Tao danh muc san pham
			categoryExtractor.getCategoryInAz24("az24_cate.xls");
			switch (store.type_menu) {
			case 1:
				categoryExtractor.extractCategoryGHType1(urlGH, store_id, true);
				break;
			case 2:
				categoryExtractor.extractCategoryGHType2(urlGH, store_id, true);
				break;
			case 3:
				categoryExtractor.extractCategoryGHType3(urlGH, store_id, true);
				break;
			case 4:
				categoryExtractor.extractCategoryGHType4(urlGH, store_id, true);
				break;
			default:
				break;
			}
			Thread.sleep(10);
			// Tao support online
			crawlerStore.extractSupportGH(urlGH, store_id);
			Thread.sleep(10);
			crawlerStore.export_category(cagegoryDAO
					.getCategoryByStoreId(store_id), store_id, store_alias,store.type_menu);
			i++;
			System.out.println("I = " +i);
		}
	}
}