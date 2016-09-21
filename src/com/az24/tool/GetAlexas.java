package com.az24.tool;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.crawler.fetcher.HttpClientUtil;
import hdc.util.html.parser.DomWriter;
import hdc.util.html.parser.XPathReader;
import hdc.util.text.HtmlUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathConstants;

import org.apache.http.HttpResponse;
import org.w3c.dom.NodeList;

public class GetAlexas {
	public int extracAlexa(String url, String xContent) {
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		String html;int intalexa = 0;
		try {
			html = HttpClientUtil.getResponseBody(res);
			XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
			// Content
			reader = CrawlerUtil.createXPathReaderByData(html);
			NodeList nodeList1 = (NodeList) reader.read(xContent,
					XPathConstants.NODESET);
			DomWriter writer = new DomWriter();
			String content = writer.toXMLString(nodeList1.item(0));
			content = HtmlUtil.removeTag(content);
			System.out.println("Content=" + content.split(":")[1].trim());
			String alexa = content.split(":")[1].trim();
			alexa=alexa.replaceAll(",", "");
			intalexa = Integer.parseInt(alexa);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return intalexa;

	}

	public Connection connLog;

	public void openConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connLog = DriverManager
					.getConnection(
							"jdbc:mysql://210.211.97.11:3306/az24_newspost?characterEncoding=UTF-8",
							"quangpn", "QuangPN2011@!!!*^");
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

	public List<Website> getWebsites() {
		List<Website> listProduct = new ArrayList<Website>();
		try {
			PreparedStatement ps = connLog
					.prepareStatement("select id,website,alexa from post_website Where alexa = 0 ");

			ResultSet resultSet = ps.executeQuery();
			int i = 0;
			while (resultSet.next()) {
				Website website = new Website();
				website.id = resultSet.getInt(1);
				website.website = resultSet.getString(2);
				listProduct.add(website);
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listProduct;
	}

	private void updateWebsite(Website website, int alexa) {
		PreparedStatement ps;
		try {
			connLog.setAutoCommit(false);
			ps = connLog
					.prepareStatement("Update az24_newspost.post_website  SET alexa = ? WHERE id = ?");
			ps.setInt(1, alexa);
			ps.setInt(2, website.id);
			ps.execute();
			connLog.commit();
			connLog.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void updateAlexa() throws Exception {
		openConnection();
		List<Website> webs = this.getWebsites();
		for (Website website : webs) {
			openConnection();
			String web = website.website.trim();
			String url = "http://www.alexa.com/search?q="+web+"&r=home_home&p=bigtop";
			System.out.println(url);
			String xpathContent = "html/body[1]/div[@id='pageContainer']/div[@id='page']/div[@id='threecolwrap']/div[@id='onecolwrap']/div[@id='content']/div[@id='content-inner']/div[@id='main']/div[@id='search']/div[@id='results']/div[1]/div[2]/span[2]";
			updateWebsite(website, extracAlexa(url, xpathContent));
			closeConnection();
		}
		closeConnection();
	}

	public static void main(String[] args) {
		GetAlexas classifieExtractBean = new GetAlexas();
		try {
			classifieExtractBean.updateAlexa();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*String url = "http://www.alexa.com/search?q=az24.vn&r=home_home&p=bigtop";
		String xpathContent = "html/body[1]/div[@id='pageContainer']/div[@id='page']/div[@id='threecolwrap']/div[@id='onecolwrap']/div[@id='content']/div[@id='content-inner']/div[@id='main']/div[@id='search']/div[@id='results']/div[1]/div[2]/span[2]";
		try {
			classifieExtractBean.extracAlexa(url, xpathContent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

	}
}
