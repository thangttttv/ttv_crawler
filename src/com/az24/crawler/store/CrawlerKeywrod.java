package com.az24.crawler.store;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.util.html.parser.XPathReader;
import hdc.util.text.StringUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.xml.xpath.XPathConstants;

import org.apache.http.HttpResponse;
import org.w3c.dom.NodeList;

import com.az24.crawler.model.product.Keyword;
import com.az24.crawler.model.product.Product;
import com.az24.db.pool.C3p0ClassifiedPool;
import com.az24.db.pool.C3p0StorePool;
import com.az24.test.HttpClientUtil;

public class CrawlerKeywrod {
	
	public List<Product> getProducts(int begin,int limit) {
		Product product = null;
		Connection conn = null;
		PreparedStatement preStmt = null;
		StringBuffer strSQL = null;
		ResultSet rs = null;
		List<Product> products = new ArrayList<Product>();
		try {
			conn = C3p0StorePool.getConnection();
			strSQL = new StringBuffer("SELECT id,cat_id, cat_id1, cat_id2, cat_id3, cat_id4,cat_id5, brand_id," +
					" name,	is_multi,status, price_from, price_to, market_price, description,picture" +
					" ,url_rewrite,num_of_qa,num_of_peple_sell,num_of_ranks,ranks_average,original_link FROM az24_store.products where id  >  "+begin+"  order by id asc	LIMIT 0, "+limit+"  ");
		
			System.out.println(strSQL.toString());
			preStmt = conn.prepareStatement(strSQL.toString());
			rs = preStmt.executeQuery();
			while (rs.next()) {
				product = new Product();
				product.id = rs.getInt("id");
				product.cat_id = rs.getInt("cat_id");
				product.original_link = rs.getString("original_link");
				products.add(product);
			}
			C3p0ClassifiedPool.attemptClose(conn);
			C3p0ClassifiedPool.attemptClose(preStmt);
			C3p0ClassifiedPool.attemptClose(rs);
			
		} catch (NoSuchElementException nse) {
			nse.printStackTrace();
		} catch (SQLException se) {
			 se.toString();
		} catch (Exception e) {
			 e.toString();
		} 
		return products;
	}
	
	public void saveKeyword(Keyword keyword)
	{
		Connection conn = null;
		PreparedStatement preStmt = null;
		StringBuffer strSQL = null;

		try {
			conn = C3p0StorePool.getConnection();
			strSQL = new StringBuffer("INSERT INTO az24_store.tbl_seo_keyword_search(keyword,content_id,cat_id," +
					"	cat_ids,number_search)	VALUES	(?,?,?,?,1) ");		
		
			preStmt = conn.prepareStatement(strSQL.toString());
			preStmt.setString(1,keyword.keyword);
			preStmt.setInt(2,keyword.content_id);
			preStmt.setInt(3,keyword.cat_id);
			preStmt.setString(4,keyword.cat_ids);
			preStmt.execute();
			C3p0ClassifiedPool.attemptClose(conn);
			C3p0ClassifiedPool.attemptClose(preStmt);
			
		} catch (NoSuchElementException nse) {
			nse.printStackTrace();
		} catch (SQLException se) {
			 se.toString();
		} catch (Exception e) {
			 e.toString();
		} 
	}
	
	public void getKeyword(String url,int cat_id) throws Exception {
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		
		String xpath__tag= "//div[@class='product_keyword_relate']";
		NodeList node_techs = (NodeList) reader.read(xpath__tag+"/a", XPathConstants.NODESET);
		Thread.sleep(1000);
	//	System.out.println(html);
		int node_one_many = node_techs.getLength();
		int i=0;
		Keyword keyword = null;
		while (i <= node_one_many) {
			String tag = (String) reader.read(xpath__tag + "/a["
					+ i + "]" + "/text()", XPathConstants.STRING);
			keyword = new Keyword();
			keyword.keyword = tag;
			keyword.cat_id=cat_id;
			keyword.cat_ids = cat_id+";";
			System.out.println(keyword.keyword);
			this.saveKeyword(keyword);
			i++;
		}
		
	}
	
	public static void main(String[] args) {
		CrawlerKeywrod crawlerKeywrod = new CrawlerKeywrod();
	    int i =Integer.parseInt(args[0]),k=0;
	    int max = 540000;
	    while(k<max)
	    {
	    	    List<Product> products =	crawlerKeywrod.getProducts(k, 500);
	    	    if(products.size()==0)break;
	    	    i=0;
			    while(i<products.size())
				    {
				    	Product product = products.get(i);
				    	try {
				    		if(!StringUtil.isEmpty(product.original_link))
				    		{
				    			crawlerKeywrod.getKeyword(product.original_link.trim(), product.cat_id);
				    		}
						} catch (Exception e) {
							e.printStackTrace();
						}
				    	i++;
				    	k=product.id;
				    }
			    System.out.println("SP Thu "+k);
			    
		}
	}
}
