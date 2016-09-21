package com.az24.crawler.store;

public class ProductStore {
	public int id;
	public int product_id;
	public int store_id;
	public String store_alias;
	public double price;
	public double price_foreign;
	public int price_type;//1.VND,2.USD,3,JSC
	public int quantity;
	public int quanlity;//1.Mới,2.Cũ
	public int ship_type;//Phi van chuyen 1. Mien Phi 2. Theo bang phi az 3. Co dinh, 4. Lien he
	public double ship_cost;
	public double ship_volume;
	public double ship_height;
	public double ship_width;
	public double ship_length;
	public int is_ship_volume;//0: Nhap khoi luong; 1 Nhap the tich
	public int is_hot;
	public int is_edit;
	public int is_new;
	public int is_resubmit;
	public int is_promotions;
	public int status; //0.Chờ duyệt(chưa kích hoạt),1: Đã duyệt( kích hoạt),2. bị xóa, 3: không được duyệt;
	public int vat;//1.Có vat.không có vat.không có thuế
	public String description;
	public String url_link_web;
	public String original_link; // link nguon de cap nhat gia
	public int ensure_month;
	public int origin;//1.Chính hãng,2.Xách tay.3.Hàng công ty
	
	public String news_promotions; // khuyen mai
	public int date_begin_promotions;
	public int date_end_promotions;
	public String unit;
	public int number_order;
	public int num_of_buy;
	public String info_review;
	public int type_account;
	public String reason;
	public String create_user;
	public long create_date;
	public String modify_user;
	public long modify_date;
	
}
