package com.az24.crawler.model.product;

import java.io.Serializable;

public class ProductField implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int id;
	public int extention_id;
	public int group_field_id;
	public String name;
	public String value;
	public int type;
	/*  0. Input text ;1. Combo box - cho phép thêm ;
	 *	2. Multi choice ;3. True / false ;
	 *	4. Radio button ;5. Text area ;
	 *	6. Input number;7. Combo box - chỉ chọn
	 */
	public int is_group;
	public int is_require;
	public int is_brand;
	public int is_main;
	public int is_filter;
	public int is_input;
	public int order;
}
