package com.az24.crawler.ketban;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.crawler.fetcher.HttpClientUtil;
import hdc.util.html.parser.XPathReader;

import javax.xml.xpath.XPathConstants;

import org.apache.http.HttpResponse;
import org.w3c.dom.NodeList;

import com.az24.db.pool.C3p0DTTTVPool;
import com.az24.db.pool.C3p0XDTPool;
import com.mysql.jdbc.PreparedStatement;

public class TyleTuyensinh {
	
	public  void extract()
			throws Exception {
		
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch("http://tradiemthi.net/ti-le-choi-2012/");
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);	
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		CrawlerUtil.analysis(reader.getDocument());
		
		String xpath_tags = "//div[@id='main-content']/div[1]/div[@id='post-5216']/div[1]/table[1]/tbody[1]/tr";
		NodeList nodes = (NodeList) reader.read(xpath_tags,
				XPathConstants.NODESET);
		if (nodes != null) {
			int node_one_many = nodes.getLength();
			int i = 1;
			while (i <= node_one_many) {
				String code = (String) reader.read(xpath_tags + "[" + i + "]"
						+ "/td[1]", XPathConstants.STRING);
				String name = (String) reader.read(xpath_tags + "[" + i + "]"
						+ "/td[2]", XPathConstants.STRING);
				String tyle = (String) reader.read(xpath_tags + "[" + i + "]"
						+ "/td[4]", XPathConstants.STRING);
				System.out.println(code.trim());
				System.out.println(name.trim());
				System.out.println(tyle.trim());
				insertTyle(code.trim(),tyle,2012,0,0);
				i++;
			}
		}
	}
	
	private void insertTyle(String code,String tyle,int year,int registration_number,int quota)
	{
		
		Connection conn = null;
		try {
			conn = C3p0XDTPool.getConnection();
			String sql1 = "Select id from school where code ='"+code+"'";
			java.sql.PreparedStatement pr1 = conn.prepareStatement(sql1);
			ResultSet rs = pr1.executeQuery(sql1);
			int school_id = 0;
			if(rs.next()) school_id = rs.getInt(1);
			System.out.println(school_id);
			String sql = "INSERT INTO vtc_diemthi.rate_fighting (school_id, quota, registration_number, rate_fighting, " +
			" year,	create_date,create_by) VALUES	(?,?,?,?," +
			" ?,?,?)";
			if(school_id>0)
			{
				conn.setAutoCommit(false);
				java.sql.PreparedStatement pr2 = conn.prepareStatement(sql);
				pr2.setInt(1, school_id);
				pr2.setInt(2, quota);
				pr2.setInt(3, registration_number);
				pr2.setString(4, tyle);
				pr2.setInt(5, year);
				pr2.setLong(6, Calendar.getInstance().getTimeInMillis()/1000);
				pr2.setString(7, "thangtt");
				pr2.execute();
				conn.commit();
				conn.setAutoCommit(true);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			C3p0XDTPool.attemptClose(conn);
		}
		
	}
	
	public static void main(String[] args) {
		TyleTuyensinh tyleTuyensinh = new TyleTuyensinh();
		try {
			tyleTuyensinh.extract();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
