package com.az24.test;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.DocumentAnalyzer;
import hdc.crawler.AbstractCrawler.Datum;
import hdc.crawler.AbstractCrawler.Url;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.util.html.A;
import hdc.util.html.HttpURL;
import hdc.util.html.NodeDeleteVisitor;
import hdc.util.html.ObjectVideo;
import hdc.util.html.SelectImgVisitor;
import hdc.util.html.SelectOjbectVisitor;
import hdc.util.html.parser.DomWriter;
import hdc.util.html.parser.XPathReader;
import hdc.util.lang.UriID;
import hdc.util.text.HtmlUtil;
import hdc.util.text.StringUtil;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import net.sf.json.JSONArray;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.az24.crawler.fiter.AbstractFilter;
import com.az24.crawler.fiter.ContentFilter;
import com.az24.crawler.store.Store;
import com.az24.dao.FootBallDAO;
import com.az24.db.pool.C3p0FacePool;
import com.az24.db.pool.C3p0StorePool;
import com.az24.util.io.FileUtil;
import com.az24.util.io.IOUtil;


public class ClassifieExtractBean {

	public String getHTML(String urlToRead) {
		URL url; // The URL to read
		HttpURLConnection conn; // The actual connection to the web page
		BufferedReader rd; // Used to read results from the web page
		String line; // An individual line of the web page HTML
		String result = ""; // A long string containing all the HTML
		try {
			url = new URL(urlToRead);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "UTF-8"));
			while ((line = rd.readLine()) != null) {
				result += line+"\n";
			}
			rd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*FileUtil fileUtil = new FileUtil();
		try {
			fileUtil.writeToFile("d:/giay.html", result, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(result);*/
		return result;
	}

	public void extract_1(String url, String xpathsubject, String xpathContent,
			String xpathImage, String xpathcity, String xpathcontact,
			String token) throws Exception {
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);
		html = getHTML(url);
		//html = IOUtil.getFileContenntAsString("d:/giay.html", "utf-8");
		
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		CrawlerUtil.analysis(reader.getDocument(), token);
		// Title
		String nodeList = (String) reader.read(xpathsubject,
				XPathConstants.STRING);

		System.out.println("Title=" + nodeList);

		// Image
		reader = CrawlerUtil.createXPathReaderByData(html);
		nodeList = (String) reader.read(xpathImage, XPathConstants.STRING);

		System.out.println("Image=" + nodeList);

		// xpathcity
		reader = CrawlerUtil.createXPathReaderByData(html);
		nodeList = (String) reader.read(xpathcity, XPathConstants.STRING);

		System.out.println("City=" + nodeList);

		// contact
		reader = CrawlerUtil.createXPathReaderByData(html);
		nodeList = (String) reader.read(xpathcontact, XPathConstants.STRING);

		System.out.println("Contact=" + nodeList);

		// Content
		reader = CrawlerUtil.createXPathReaderByData(html);
		NodeList nodeList1 = (NodeList) reader.read(xpathContent,
				XPathConstants.NODESET);
		DomWriter writer = new DomWriter();

		//deleteVisitor.traverse(nodeList1.item(0));
		System.out.println(ContentFilter.changeLink(writer
				.toXMLString(nodeList1.item(0))));

		System.out.println("Content" + writer.toXMLString(nodeList1.item(0)));
	}

	public void extractBaloTeen(String url, String xTitle, String xDesc,
			String xContent, String xpathImage, String xTags, String xAuthor,
			String token) throws Exception {
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);

		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		CrawlerUtil.analysis(reader.getDocument(), token);
		// Title
		String nodeList = (String) reader.read(xTitle, XPathConstants.STRING);

		System.out.println("Title=" + nodeList);

		// Description
		nodeList = (String) reader.read(xDesc, XPathConstants.STRING);

		System.out.println("xDesc=" + nodeList);

		// Image
		reader = CrawlerUtil.createXPathReaderByData(html);
		nodeList = (String) reader.read(xpathImage, XPathConstants.STRING);

		System.out.println("Image=" + nodeList);

		// Author
		reader = CrawlerUtil.createXPathReaderByData(html);
		nodeList = (String) reader.read(xAuthor, XPathConstants.STRING);

		System.out.println("Author=" + nodeList);

		// xTags
		DomWriter writer = new DomWriter();

		reader = CrawlerUtil.createXPathReaderByData(html);
		String nodeList11 = (String) reader.read(xTags, XPathConstants.STRING);

		System.out.println("xTags=" + nodeList11);

		// Content
		reader = CrawlerUtil.createXPathReaderByData(html);
		NodeList nodeList1 = (NodeList) reader.read(xContent,
				XPathConstants.NODESET);

		//deleteVisitor.traverse(nodeList1.item(0));
		System.out.println("Content="
				+ ContentFilter.changeLink(writer
						.toXMLString(nodeList1.item(0))));
		SelectOjbectVisitor selectOjbectVisitor = new SelectOjbectVisitor("");
		selectOjbectVisitor.traverse(nodeList1.item(0));
		List<ObjectVideo> list = selectOjbectVisitor.getLinks();
		for (ObjectVideo object : list) {
			System.out.println(object.embed_source);
		}

		SelectImgVisitor imgVisitor = new SelectImgVisitor();
		imgVisitor.traverse(nodeList1.item(0));
		String content = writer.toXMLString(nodeList1.item(0));
		content = HtmlUtil.removeTag(content);
		System.out.println("Content=" + content.split(":")[1].trim());
		//System.out.println("Content"+writer.toXMLString(nodeList1.item(0)));
	}

	private NodeDeleteVisitor deleteVisitor = new NodeDeleteVisitor() {
		protected boolean shouldDelete(Node node) {
			if (node.getNodeName().equalsIgnoreCase("meta"))
				return true;
			else if (node.getNodeName().equalsIgnoreCase("link"))
				return true;
			else if (node.getNodeName().equalsIgnoreCase("style"))
				return true;
			else if (node.getNodeName().equalsIgnoreCase("script"))
				return true;
			else if (node.getNodeName().equalsIgnoreCase("a"))
				return true;
			return false;
		}
	};

	public void extract_2(String url, String xpathsubject, String xpathContent,
			String xpathImage, String xpathcity, String xpathcontact,
			String token) throws Exception {
		XPathReader reader = CrawlerUtil.createXPathReaderByData(getHTML(url));
		CrawlerUtil.analysis(reader.getDocument(), token);
		// Title
		String nodeList = (String) reader.read(xpathsubject,
				XPathConstants.STRING);

		System.out.println("Title=" + nodeList);

		// Image
		reader = CrawlerUtil.createXPathReaderByData(getHTML(url));
		nodeList = (String) reader.read(xpathImage, XPathConstants.STRING);

		System.out.println("Image=" + nodeList);

		// xpathcity
		reader = CrawlerUtil.createXPathReaderByData(getHTML(url));
		nodeList = (String) reader.read(xpathcity, XPathConstants.STRING);
		System.out.println("City=" + nodeList);
		// contact
		reader = CrawlerUtil.createXPathReaderByData(getHTML(url));
		nodeList = (String) reader.read(xpathcontact, XPathConstants.STRING);
		System.out.println("Contact=" + nodeList);
		// content
		reader = CrawlerUtil.createXPathReaderByData(getHTML(url));
		NodeList nodeList1 = (NodeList) reader.read(xpathContent,
				XPathConstants.NODESET);
		DomWriter writer = new DomWriter();
		System.out.println("Content=" + writer.toXMLString(nodeList1.item(0)));
	}

	public static void mainVatGia(String[] args) {
		ClassifieExtractBean classifieExtractBean = new ClassifieExtractBean();

		String url = "http://www.vatgia.com/vienthinh&module=product&view=detail&record_id=679338";
		String xpathsubject = "html/body[1]/div[1]/div[1]/table[1]/TBODY[1]/tr[1]/td[1]/table[3]/div[1]/div[1]/div[1]/table[4]/table[1]/TBODY[1]/tr[1]/td[1]/table[1]/TBODY[1]/tr[1]/td[1]/div[@id='posts']/div[1]/div[1]/div[1]/div[1]/table[1]/TBODY[1]/tr[2]/td[2]/div[1]/strong[1]/text()";
		xpathsubject = "html/body[1]/div[@id='body']/div[@id='container_body']/div[@id='container_header']/div[5]/a[1]/b[1]/text()";
		xpathsubject = "//div[@class='content raovat_detail']/h1[1]/text()";

		String xpathContent = "//div[@class='content raovat_detail']/h1";
		String xpathContact = "html/body[1]/center[1]/div[@id='ContentLayout']/table[@id='table2']/TBODY[1]/tr[1]/td[@id='ContentLeft']/div[@id='NewsDetail']/table[1]/TBODY[1]/tr[1]/td[1]/div[@id='NewsLayout']/div[@id='NewsInfo']/table[1]/TBODY[1]/tr[1]/td[1]/div[@id='searchArea']/div[1]/div[2]/div[1]/a[1]/text()";
		xpathContact = "html/body[1]/div[@id='body']/div[@id='container_body']/div[@id='container_content']/div[1]/div[3]/div[1]/div[2]/div[2]/table[1]/TBODY[1]/tr[4]/td[2]/text()";
		String xpathImage = "html/BODY[1]/center[1]/div[@id='container']/div[@id='main-pg']/span[2]/table[3]/TBODY[1]/tr[4]/td[1]/table[1]/TBODY[1]/tr[4]/td[2]/a[1]/text()";
		xpathImage = "//a[@rel='raovat_picture']/img/@src";
		String city = "html/body[1]/center[1]/div[@id='ContentLayout']/table[@id='table2']/TBODY[1]/tr[1]/td[@id='ContentLeft']/div[@id='NewsDetail']/table[1]/TBODY[1]/tr[1]/td[1]/div[@id='NewsLayout']/div[@id='NewsInfo']/table[1]/TBODY[1]/tr[1]/td[1]/div[@id='searchArea']/div[1]/div[1]/div[1]/a[1]/b[1]/text()";
		city = "html/body[1]/div[@id='body']/div[@id='container_body']/div[@id='container_content']/div[1]/div[3]/div[1]/div[1]/div[2]/table[1]/TBODY[1]/tr[3]/td[1]/table[2]/TBODY[1]/tr[4]/td[2]/strong/text()";

		try {
			classifieExtractBean.extract_1(url, xpathsubject, xpathContent,
					xpathImage, city, xpathContact, "Nơi đăng :");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main_enbac(String[] args) {
		ClassifieExtractBean classifieExtractBean = new ClassifieExtractBean();

		String url = "http://enbac.com/Thoi-trang-Nam/p1128223/Ao-vest-Han-Quoc-gia-cuc-soc-350k.html";
		String xpathsubject = "html/BODY[1]/center[1]/div[@id='container']/div[@id='main-pg']/span[2]/table[3]/TBODY[1]/tr[1]/td[1]/table[1]/TBODY[1]/tr[2]/td[2]/h3[1]/a[1]//@title";

		//xpathsubject="html/body[1]/div[1]/div[1]/table[1]/TBODY[1]/tr[1]/td[1]/table[2]/div[1]/div[1]/div[1]/table[4]/table[1]/TBODY[1]/tr[1]/td[1]/table[1]/TBODY[1]/tr[1]/td[1]/div[@id='posts']/div[1]/div[1]/div[1]/div[1]/table[1]/TBODY[1]/tr[2]/td[2]/div[1]/strong[1]/text()";
		String xpathContent = "html/body[1]/div[1]/div[1]/table[1]/TBODY[1]/tr[1]/td[1]/table[3]/div[1]/div[1]/div[1]/table[4]/table[1]/TBODY[1]/tr[1]/td[1]/table[1]/TBODY[1]/tr[1]/td[1]/div[@id='posts']/div[1]/div[1]/div[1]/div[1]/table[1]/TBODY[1]/tr[2]/td[2]/div[2]";
		xpathContent = "html/BODY[1]/center[1]/div[@id='container']/div[@id='main-pg']/span[2]/table[3]";
		String xpathContact = "html/body[1]/center[1]/div[@id='ContentLayout']/table[@id='table2']/TBODY[1]/tr[1]/td[@id='ContentLeft']/div[@id='NewsDetail']/table[1]/TBODY[1]/tr[1]/td[1]/div[@id='NewsLayout']/div[@id='NewsInfo']/table[1]/TBODY[1]/tr[1]/td[1]/div[@id='searchArea']/div[1]/div[2]/div[1]/a[1]/text()";
		String xpathImage = "//div[@id='gallery']/div[@class='img_body']/a[1]/img[1]/@src";
		String city = "html/body[1]/center[1]/div[@id='ContentLayout']/table[@id='table2']/TBODY[1]/tr[1]/td[@id='ContentLeft']/div[@id='NewsDetail']/table[1]/TBODY[1]/tr[1]/td[1]/div[@id='NewsLayout']/div[@id='NewsInfo']/table[1]/TBODY[1]/tr[1]/td[1]/div[@id='searchArea']/div[1]/div[1]/div[1]/a[1]/b[1]/text()";

		try {
			classifieExtractBean.extract_1(url, xpathsubject, xpathContent,
					xpathImage, city, xpathContact,
					"Nhân viên Call Center làm việc tại công ty");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void extract_store_product(String url, String xpath_price, String xpath_soluong,
			String token) throws Exception {
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		//String html = HttpClientUtil.getResponseBody(res);
		String html = IOUtil.getFileContenntAsString("d:/giay2.html", "utf-8");
		//html = html.replaceAll("\"", ""); 
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		CrawlerUtil.analysis(reader.getDocument());
		//CrawlerUtil.removeNodeByXpath(reader, "HTML/BODY[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/TABLE[1]/TBODY[1]/TR[6]/TD[1]/TABLE[1]/TBODY[1]/TR[2]/TD[1]/TABLE[1]/TBODY[1]/TR[1]/TD[3]/TABLE[1]/TBODY[1]/TR[3]/TD[1]/TABLE[1]/TBODY[1]/TR[1]/TD[1]/TABLE[2]/TBODY[1]/TR[1]/TD[1]/B[1]/FONT[1]");
		Node node_name = (Node) reader.read("HTML/BODY[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/TABLE[1]/TBODY[1]/TR[6]/TD[1]/TABLE[1]/TBODY[1]/TR[2]/TD[1]/TABLE[1]/TBODY[1]/TR[1]/TD[3]/TABLE[1]/TBODY[1]/TR[3]/TD[1]/TABLE[1]/TBODY[1]/TR[1]/TD[1]/TABLE[2]", XPathConstants.NODE);
		DomWriter writer = new DomWriter();
		
		//System.out.println(writer.toXMLString(node_name));
				
		String node_picture = (String) reader.read("HTML/BODY[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/TABLE[1]/TBODY[1]/TR[6]/TD[1]/TABLE[1]/TBODY[1]/TR[2]/TD[1]/TABLE[1]/TBODY[1]/TR[1]/TD[3]/TABLE[1]/TBODY[1]/TR[3]/TD[1]/TABLE[1]/TBODY[1]/TR[1]/TD[1]/TABLE[2]/TBODY[1]/TR[1]/TD[1]/DIV[1]/FONT[1]/text()", XPathConstants.STRING);
		
		System.out.println((node_picture));
		
		
		node_picture = (String) reader.read("HTML/BODY[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/TABLE[1]/TBODY[1]/TR[6]/TD[1]/TABLE[1]/TBODY[1]/TR[2]/TD[1]/TABLE[1]/TBODY[1]/TR[1]/TD[3]/TABLE[1]/TBODY[1]/TR[3]/TD[1]/TABLE[1]/TBODY[1]/TR[1]/TD[1]/TABLE[2]/TBODY[1]/TR[1]/TD[1]/DIV[1]/FONT[1]/text()", XPathConstants.STRING);
		
		System.out.println((node_picture));
		
		String node_picture2 = (String) reader.read("HTML/BODY[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/TABLE[1]/TBODY[1]/TR[6]/TD[1]/TABLE[1]/TBODY[1]/TR[2]/TD[1]/TABLE[1]/TBODY[1]/TR[1]/TD[3]/TABLE[1]/TBODY[1]/TR[3]/TD[1]/TABLE[1]/TBODY[1]/TR[1]/TD[1]/TABLE[2]/TBODY[1]/TR[1]/TD[1]/B[1]/FONT[1]/text()", XPathConstants.STRING);

		System.out.println(node_picture2);
		
		
		NodeList nodes = (NodeList) reader.read(xpath_price, XPathConstants.NODESET);
		int node_one_many = nodes.getLength();
		int i = 1;
		String sub_xpath ="";
		while (i <= node_one_many) {
			String price_label = (String) reader.read(xpath_price + "["
					+ i + "]" + "/td[1]/text()", XPathConstants.STRING);
			sub_xpath =  "/td[2]/text()";
			if("Giá chưa VAT:".equalsIgnoreCase(price_label)) sub_xpath = "/td[2]/b[1]/text()";
			if("Giá bán cuối:".equalsIgnoreCase(price_label)) sub_xpath = "/td[2]/b[1]/text()";
				
			String price_value = (String) reader.read(xpath_price + "[" + i
					+ "]" + sub_xpath, XPathConstants.STRING);
			
			System.out.println(price_label.trim());
			System.out.println(price_value.trim());
			i++;
		}
		
		nodes = (NodeList) reader.read(xpath_soluong, XPathConstants.NODESET);
		node_one_many = nodes.getLength();
		i = 1;
		sub_xpath ="";
		int is_node = 0;
		while (i <= node_one_many) {
			String price_label = (String) reader.read(xpath_soluong + "["
					+ i + "]" + "/td[1]/span[1]/text()", XPathConstants.STRING);
			sub_xpath =  "/td[2]/text()";is_node = 0;
			if("Đặt hàng".equalsIgnoreCase(price_label)) {
				sub_xpath = "/td[2]";is_node = 1;
				}
			String price_value ="";
			if(is_node==0)
			{
				price_value = (String) reader.read(xpath_soluong + "[" + i
						+ "]" + sub_xpath, XPathConstants.STRING);
			}else
			{
				Node price_value_node = (Node) reader.read(xpath_soluong + "[" + i
						+ "]" + sub_xpath, XPathConstants.NODE);
				price_value = price_value_node.getTextContent();
			}
				
			System.out.println(price_label.trim());
			System.out.println(price_value.trim());
			i++;
		}
		String xpath__description = "//div[@class='product_detail_description']";
		Node description = (Node) reader.read(xpath__description, XPathConstants.NODE);
		
		System.out.println(writer.toXMLString(description));
		String xpath_name ="//div[@class='product_detail product_detail_v2']/div[2]/h1[1]/text()";
		//String name = (String) reader.read(xpath_name, XPathConstants.STRING);
	//	System.out.println("Name="+name);
		// Anh
		String xpath_anh = "//div[@class='picture_thumbnail_list']/div[1]/table[@class='picture_thumbnail']/TBODY/tr[1]/td[1]/a[1]/@href";
		String node = (String) reader.read(xpath_anh, XPathConstants.STRING);
		
		
		
		System.out.println(node);
		
		xpath_anh = "//div[@class='picture_thumbnail_list']/div[1]/table[@class='picture_thumbnail']/TBODY/tr[1]/td";
		NodeList nodes_image = (NodeList) reader.read(xpath_anh, XPathConstants.NODESET);
		node_one_many = nodes_image.getLength();
		i = 1;
		sub_xpath ="";
	
		while (i <= node_one_many) {
			String image_path = (String) reader.read(xpath_anh + "["
					+ i + "]" + "/a[1]/@href", XPathConstants.STRING);
			System.out.println(image_path);
			i++;
		}
		
		String xpath_tech ="//div[@id='technical_product']/table[@class='product_technical_table']/TBODY[1]/tr";
		NodeList node_techs = (NodeList) reader.read(xpath_tech, XPathConstants.NODESET);
		node_one_many = node_techs.getLength();
		i = 1;
		sub_xpath ="";
		Pattern pattern = Pattern.compile("\\•", Pattern.CASE_INSENSITIVE);
		Matcher matcher = null;
		while (i <= node_one_many) {
			String tech_lable = (String) reader.read(xpath_tech + "["
					+ i + "]" + "/td[1]/span[1]/text()", XPathConstants.STRING);
			if(!StringUtil.isEmpty(tech_lable))
			{
				Node tech_value = (Node) reader.read(xpath_tech + "["
					+ i + "]" + "/td[2]", XPathConstants.NODE);
				//System.out.println(tech_lable);
				matcher = pattern.matcher(tech_value.getTextContent().trim());
				String value = matcher.replaceAll(";");
				System.out.println(tech_lable+"="+tech_value.getTextContent().trim());
				System.out.println("-->"+value.trim());
				System.out.println("-->"+ writer.toXMLString(tech_value));
			}
			
			i++;
		}
		
		String xpath__tag= "//div[@class='product_keyword_relate']";
		Node tags = (Node) reader.read(xpath__tag, XPathConstants.NODE);
		System.out.println(writer.toXMLString(tags));
		node_techs = (NodeList) reader.read(xpath__tag+"/a", XPathConstants.NODESET);
		node_one_many = node_techs.getLength();
		i=0;
		while (i <= node_one_many) {
			String keyword = (String) reader.read(xpath__tag + "/a["
					+ i + "]" + "/text()", XPathConstants.STRING);
			System.out.println(keyword);
			
			i++;
		}
	}
	
	
	public void extract_thoitiet(String url, String xpath_price, String xpath_soluong,
			String token) throws Exception {
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		CrawlerUtil.analysis(reader.getDocument());
		DomWriter writer = new DomWriter();
		Node node_picture = (Node) reader.read("//TABLE[@id='_ctl1__ctl0__ctl0_dl_Bantindubao']", XPathConstants.NODE);
		System.out.println(writer.toXMLString(node_picture));
		
	}
	
	public static void main_ghcn(String[] args) {
		ClassifieExtractBean classifieExtractBean = new ClassifieExtractBean();

		String url = "http://vatgia.com/Phuongvy_kts&module=product&view=listudv&record_id=6018";
		 url ="http://www.vatgia.com/vienthinh&module=product&view=detail&record_id=1302904";
		 url ="http://www.vatgia.com/viettruck&module=product&view=detail&record_id=1037051";
		 url ="http://www.vatgia.com/QUOCTE_ACHAU&module=product&view=detail&record_id=257728";
		 url ="http://vatgia.com/kcshop&module=product&view=detail&record_id=1288246";
		 url = "http://vatgia.com/hoasaigon";
		String xpath_price = "//table[@class='product_detail_table']/TBODY[1]/tr[1]/td[1]/table[1]/TBODY[1]/tr";
		String xpath_soluong = "//div[@id='container_content_center_right']/div[4]/div[1]/div[2]/table[2]/TBODY[1]/tr[1]/td[1]/table[1]/TBODY[1]/tr";
		try {
			classifieExtractBean.extract_store_product(url, xpath_price, xpath_soluong, "Có");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void mainBaiDich(String[] args) {
		ClassifieExtractBean classifieExtractBean = new ClassifieExtractBean();

		String url = "http://baidich.com/index.php?page=A-1";
		 url ="http://www.vatgia.com/vienthinh&module=product&view=detail&record_id=1302904";
		 url ="http://www.vatgia.com/viettruck&module=product&view=detail&record_id=1037051";
		 url ="http://www.vatgia.com/QUOCTE_ACHAU&module=product&view=detail&record_id=257728";
		 url ="http://vatgia.com/kcshop&module=product&view=detail&record_id=1288246";
		 url = "http://baidich.com/12212-A20ans-Lorie";
		 
		String xpath_price = "//div[@class='tab_content']/table[1]/TBODY[1]/tr[2]/td[2]/div";
		String xpath_soluong = "//div[@id='container_content_center_right']/div[4]/div[1]/div[2]/table[2]/TBODY[1]/tr[1]/td[1]/table[1]/TBODY[1]/tr";
		try {
			classifieExtractBean.extract_store_product(url, xpath_price, xpath_soluong, "Khi ta 20, ta cảm thấy");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	private void extractBaseInfoGH()
	{
		
	}
	
	private void extractProductGH(List<Url> urlProcates)
	{
		
	}
	
	public static void mainAlexa(String[] args) {
		ClassifieExtractBean classifieExtractBean = new ClassifieExtractBean();

		String url = "http://www.alexa.com/search?q=vatgia.com&r=home_home&p=bigtop";
		String xTitle = "html/body[1]/div[@id='pageContainer']/div[@id='page']/div[@id='threecolwrap']/div[@id='onecolwrap']/div[@id='content']/div[@id='content-inner']/div[@id='main']/div[@id='search']/div[@id='results']/div[1]/div[2]/span[2]/text()";
		String xpathdesc = "//h2[@class='Lead']/text()";
		String xpathContent = "html/body[1]/div[@id='pageContainer']/div[@id='page']/div[@id='threecolwrap']/div[@id='onecolwrap']/div[@id='content']/div[@id='content-inner']/div[@id='main']/div[@id='search']/div[@id='results']/div[1]/div[2]/span[2]";

		String xpathAuthor = "//div[@class='author']/a[1]/text()";
		String xpathImage = "html/BODY[1]/center[1]/div[@id='container']/div[@id='main-pg']/span[2]/table[3]/TBODY[1]/tr[4]/td[1]/table[1]/TBODY[1]/tr[4]/td[2]/a[1]/text()";
		String xpathTag = "//div[@class='post-tags clearfix']/div[@class='list']/a[1]/text()";

		try {
			classifieExtractBean.extractBaloTeen(url, xTitle, xpathdesc,
					xpathContent, xpathImage, xpathTag, xpathAuthor, ": 10");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main1(String[] args) {
		ClassifieExtractBean classifieExtractBean = new ClassifieExtractBean();

		String url = "http://vnexpress.net/gl/the-thao/bong-da/2011/10/man-city-giam-nhe-cao-buoc-voi-tevez/";
		String xTitle = "//h1[@class='title']/text()";
		String xpathdesc = "//h2[@class='Lead']/text()";
		String xpathContent = "//div[@class='content']";

		String xpathAuthor = "//div[@class='author']/a[1]/text()";
		String xpathImage = "html/BODY[1]/center[1]/div[@id='container']/div[@id='main-pg']/span[2]/table[3]/TBODY[1]/tr[4]/td[1]/table[1]/TBODY[1]/tr[4]/td[2]/a[1]/text()";
		String xpathTag = "//h3[@class='tag-text']/a[1]/text()";

		try {
			classifieExtractBean.extractBaloTeen(url, xTitle, xpathdesc,
					xpathContent, xpathImage, xpathTag, xpathAuthor,
					"Thanh Bùi trình làng ca khúc mới trong minishow");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void extract_3(String url) {
		XPathReader reader = CrawlerUtil.createXPathReaderByData(getHTML(url));
		try {
			CrawlerUtil.analysis(reader.getDocument());
			XPath xpath = XPathFactory.newInstance().newXPath();

			XPathExpression expr = xpath.compile("//a");
			NodeList nodes = (NodeList) expr.evaluate(reader.getDocument(),
					XPathConstants.NODESET);
			Node node = null;
			for (int i = 0; i < nodes.getLength(); i++) {
				if (nodes.item(i).getAttributes().getNamedItem("href") != null)
					//System.out.println(nodes.item(i).getAttributes().getNamedItem("href").getTextContent());

					if (nodes.item(i).hasChildNodes()) {

						NodeList list = nodes.item(i).getChildNodes();
						int k = 0;
						while (k < list.getLength()) {
							Node node2 = list.item(k);
							k++;
							if (node2.getAttributes() != null) {
								if (node2.getAttributes().getNamedItem("src") != null) {
									System.out.println(node2.getAttributes()
											.getNamedItem("src")
											.getTextContent());
								}
							}
						}
					}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void extract_video(String url) {
		XPathReader reader = CrawlerUtil.createXPathReaderByData(getHTML(url));
		try {
			CrawlerUtil.analysis(reader.getDocument());
			XPath xpath = XPathFactory.newInstance().newXPath();

			XPathExpression expr = xpath.compile("//a");
			NodeList nodes = (NodeList) expr.evaluate(reader.getDocument(),
					XPathConstants.NODESET);
			Node node = null;
			for (int i = 0; i < nodes.getLength(); i++) {
				if (nodes.item(i).getAttributes().getNamedItem("href") != null)
					//System.out.println(nodes.item(i).getAttributes().getNamedItem("href").getTextContent());

					if (nodes.item(i).hasChildNodes()) {

						NodeList list = nodes.item(i).getChildNodes();
						int k = 0;
						while (k < list.getLength()) {
							Node node2 = list.item(k);
							k++;
							if (node2.getAttributes() != null) {
								if (node2.getAttributes().getNamedItem("src") != null) {
									System.out.println(node2.getAttributes()
											.getNamedItem("src")
											.getTextContent());
								}
							}
						}
					}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void mainLink(String[] args) {
		ClassifieExtractBean classifieExtractBean = new ClassifieExtractBean();

		String url = "http://kenh14.vn/teeniscover.chn";
		classifieExtractBean.extract_3(url);

	}

	public static void mainZing(String[] args) {
		ClassifieExtractBean classifieExtractBean = new ClassifieExtractBean();

		String url = "http://www.zing.vn/news/dep/nguoi-dep.html?p=2";
		String xName = "html/body[1]/div[@id='body']/div[@id='container_body']/div[@id='container_content']/div[1]/h1[1]/text()";
		String xPrice = "html/body[1]/div[@id='body']/div[@id='container_body']/div[@id='container_content']/div[1]/div[1]/div[2]/table[1]/TBODY[1]/tr[1]/td[2]/b[1]/text()";
		String xImage1 = "html/body[1]/div[@id='body']/div[@id='container_body']/div[@id='container_content']/div[1]/div[1]/div[1]/div[2]/table[1]/TBODY[1]/tr[1]/td[1]/a[1]/img[1]//@src";
		String xmainpicture = "html/body[1]/div[@id='body']/div[@id='container_body']/div[@id='container_content']/div[1]/div[1]/div[1]/div[2]/table[1]/TBODY[1]/tr[1]/td";
		xmainpicture = "html/body[1]/div[@id='body']/div[@id='container_body']/div[@id='container_content']/div[1]/div[1]/div[1]/div[2]/div[1]/table[1]/TBODY[1]/tr[1]/td";
		xmainpicture = "html/body[1]/div[@id='body']/div[@id='container_body']/div[@id='container_content']/div[1]/div[1]/div[1]/div[1]/table[1]/TBODY[1]/tr[1]/td[1]/h1";
		//xmainpicture ="html/body[1]/div[@id='body']/div[@id='container_body']/div[@id='container_content']/div[1]/div[1]/div[1]/div[1]/table[1]/TBODY[1]/tr[1]/td[1]/h1[1]/img[1]";
		String xbigpicture = "html/body[1]/div[@id='body']/div[@id='container_body']/div[@id='container_content']/div[1]/div[1]/div[1]/div[2]/table[1]/TBODY[1]/tr[1]/td[1]/a[1]//@href";
		String xProdata = "html/body[1]/div[@id='body']/div[@id='container_body']/div[@id='container_content']/div[1]/div[1]/div[@id='product_technical']/table[1]/TBODY[1]/tr";

		String xpathContent = "html/body[1]/div[1]/div[1]/table[1]/TBODY[1]/tr[1]/td[1]/table[3]/div[1]/div[1]/div[1]/table[4]/table[1]/TBODY[1]/tr[1]/td[1]/table[1]/TBODY[1]/tr[1]/td[1]/div[@id='posts']/div[1]/div[1]/div[1]/div[1]/table[1]/TBODY[1]/tr[2]/td[2]/div[2]";
		xpathContent = "html/body[1]/div[1]/div[1]/table[1]/TBODY[1]/tr[1]/td[1]/table[2]/div[1]/div[1]/div[1]/table[4]/table[1]/TBODY[1]/tr[1]/td[1]/table[1]/TBODY[1]/tr[1]/td[1]/div[@id='posts']/div[1]/div[1]/div[1]/div[1]/table[1]/TBODY[1]/tr[2]/td[2]/div[2]/font[1]/font[1]";
		String xpathContact = "html/body[1]/center[1]/div[@id='ContentLayout']/table[@id='table2']/TBODY[1]/tr[1]/td[@id='ContentLeft']/div[@id='NewsDetail']/table[1]/TBODY[1]/tr[1]/td[1]/div[@id='NewsLayout']/div[@id='NewsInfo']/table[1]/TBODY[1]/tr[1]/td[1]/div[@id='searchArea']/div[1]/div[2]/div[1]/a[1]/text()";

		String city = "html/body[1]/center[1]/div[@id='ContentLayout']/table[@id='table2']/TBODY[1]/tr[1]/td[@id='ContentLeft']/div[@id='NewsDetail']/table[1]/TBODY[1]/tr[1]/td[1]/div[@id='NewsLayout']/div[@id='NewsInfo']/table[1]/TBODY[1]/tr[1]/td[1]/div[@id='searchArea']/div[1]/div[1]/div[1]/a[1]/b[1]/text()";

		try {
			classifieExtractBean.extractProduct(url, xName, xPrice,
					xpathContent, xImage1, xmainpicture, xbigpicture, xProdata,
					city, xpathContact, "nhất thế giới ");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void mainBaLo(String[] args) {
		ClassifieExtractBean classifieExtractBean = new ClassifieExtractBean();
		//classifieExtractBean.extract_3("http://vatgia.com/776/1225019/gi%C6%B0%E1%BB%9Dng-ng%E1%BB%A7-g210.html");
		String url = "http://vatgia.com/776/1225019/gi%C6%B0%E1%BB%9Dng-ng%E1%BB%A7-g210.html";
		url = "http://vatgia.com/568/1133169/thong_so_ky_thuat/daewoo-lacetti-se-1-6-mt-2011.html";
		String xName = "//div[@class='information']/h1[1]/text()";
		String xPrice = "//b[@class='price']";
		String xImage1 = "html/body[1]/div[@id='body']/div[@id='container_body']/div[@id='container_content']/div[1]/div[1]/div[1]/div[2]/table[1]/TBODY[1]/tr[1]/td[1]/a[1]/img[1]//@src";
		String xmainpicture = "html/body[1]/div[@id='body']/div[@id='container_body']/div[@id='container_content']/div[1]/div[1]/div[1]/div[2]/table[1]/TBODY[1]/tr[1]/td";
		xmainpicture = "html/body[1]/div[@id='body']/div[@id='container_body']/div[@id='container_content']/div[1]/div[1]/div[1]/div[2]/div[1]/table[1]/TBODY[1]/tr[1]/td";
		xmainpicture = "html/body[1]/div[@id='body']/div[@id='container_body']/div[@id='container_content']/div[1]/div[1]/div[1]/div[1]/table[1]/TBODY[1]/tr[1]/td[1]/h1";
		xmainpicture = "//div[@class='picture_thumbnail_list']/table[1]/TBODY[1]/tr[1]/td";
		String xbigpicture = "//div[class='picture_thumbnail_list']/table[1]/TBODY[1]/tr[1]/td[1]/a[1]//@href";
		String xProdata = "//div[@id='product_technical']/table[1]/TBODY[1]/tr";
		xImage1 = "html/body[1]/div[@id='body']/div[@id='container_body']/div[@id='container_content']/div[1]/div[1]/div[1]/div[1]/table[1]/TBODY[1]/tr[1]/td[1]/a[1]//@href";
		String xpathContent = "//div[@class='product_description']";

		String xpathContact = "html/body[1]/center[1]/div[@id='ContentLayout']/table[@id='table2']/TBODY[1]/tr[1]/td[@id='ContentLeft']/div[@id='NewsDetail']/table[1]/TBODY[1]/tr[1]/td[1]/div[@id='NewsLayout']/div[@id='NewsInfo']/table[1]/TBODY[1]/tr[1]/td[1]/div[@id='searchArea']/div[1]/div[2]/div[1]/a[1]/text()";

		String city = "html/body[1]/center[1]/div[@id='ContentLayout']/table[@id='table2']/TBODY[1]/tr[1]/td[@id='ContentLeft']/div[@id='NewsDetail']/table[1]/TBODY[1]/tr[1]/td[1]/div[@id='NewsLayout']/div[@id='NewsInfo']/table[1]/TBODY[1]/tr[1]/td[1]/div[@id='searchArea']/div[1]/div[1]/div[1]/a[1]/b[1]/text()";

		try {
			classifieExtractBean.extractProduct(url, xName, xPrice,
					xpathContent, xImage1, xmainpicture, xbigpicture, xProdata,
					city, xpathContact, "iPhone 4 (Trung Quốc)");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void extractProduct(String url, String xpathsubject, String xPrice,
			String xpathContent, String xpathImage, String xmainpictrue,
			String xbigpictrue, String xProdata, String xpathcity,
			String xpathcontact, String token) throws Exception {
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);
		DomWriter writer = new DomWriter();
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		CrawlerUtil.analysis(reader.getDocument(), token);
		// Title
		String nodeList = (String) reader.read(xpathsubject,
				XPathConstants.STRING);

		System.out.println("Title=" + nodeList);

		// Price
		nodeList = (String) reader.read(xPrice, XPathConstants.STRING);

		System.out.println("Price=" + nodeList);

		// Image
		reader = CrawlerUtil.createXPathReaderByData(html);
		nodeList = (String) reader.read(xpathImage, XPathConstants.STRING);

		System.out.println("Image=" + nodeList);

		// Image
		reader = CrawlerUtil.createXPathReaderByData(html);
		nodeList = (String) reader.read(xmainpictrue, XPathConstants.STRING);

		System.out.println("Image=" + nodeList);

		// Image
		reader = CrawlerUtil.createXPathReaderByData(html);
		nodeList = (String) reader.read(xbigpictrue, XPathConstants.STRING);

		System.out.println("Image=" + nodeList);

		NodeList nodes = (NodeList) reader
				.read(
						"//div[@class='picture_thumbnail_list']/table[1]/TBODY[1]/tr[1]/td",
						XPathConstants.NODESET);
		int node_one_many = nodes.getLength();
		int i = 1;
		while (i <= node_one_many) {
			String mainpictrue = (String) reader.read(
					"//div[@class='picture_thumbnail_list']/table[1]/TBODY[1]/tr[1]/td"
							+ "[" + i + "]" + "/a[1]//@mainpicture",
					XPathConstants.STRING);
			String bigpictrue = (String) reader.read(xmainpictrue + "[" + i
					+ "]" + "/a[1]//@href", XPathConstants.STRING);
			String smallpictrue = (String) reader.read(xmainpictrue + "[" + i
					+ "]" + "/a[1]/img[1]//@src", XPathConstants.STRING);

			System.out.println("-->Small=" + smallpictrue);
			System.out.println("-->Main=" + mainpictrue);
			System.out.println("-->Big=" + bigpictrue);
			i++;
		}
		// xpath property data

		nodes = (NodeList) reader.read(xProdata, XPathConstants.NODESET);
		node_one_many = nodes.getLength();

		i = 1;
		int j = 1;
		while (i <= node_one_many) {
			j = 1;

			Node oresult = (Node) reader.read(xProdata + "[" + i + "]"
					+ "/td[1]", XPathConstants.NODE);
			// la group field

			if (oresult.getAttributes() != null
					&& oresult.getAttributes().getNamedItem("colspan") != null) {
				System.out.println("-->Group Name=" + oresult.getTextContent());
			} else {
				String str = (String) reader.read(xProdata + "[" + i + "]"
						+ "/td[1]/text()", XPathConstants.STRING);

				Node value = (Node) reader.read(xProdata + "[" + i + "]"
						+ "/td[2]", XPathConstants.NODE);
				/*if(StringUtil.isEmpty(value.trim()))
				{
					value = (String) reader.read(xProdata
							+ "[" + i + "]" + "/td[2]/a[1]/text()",
							XPathConstants.STRING);
				}*/
				String strValue = writer.toXMLString(value);

				strValue = strValue.substring(57).replaceAll("</td>", "");
				strValue = strValue.replaceAll("</a>", "");
				String pattern = "<a.*href=\".*\">";

				// Create a Pattern object
				Pattern r = Pattern.compile(pattern);
				Matcher m = r.matcher(strValue);
				strValue = m.replaceAll("").trim();

				System.out.println("---------->Field Name=" + str
						+ "-----: Field value=" + strValue);

				str = (String) reader.read(xProdata + "[" + i + "]"
						+ "/td[3]/text()", XPathConstants.STRING);
				if (!StringUtil.isEmpty(str)) {
					value = (Node) reader.read(xProdata + "[" + i + "]"
							+ "/td[4]", XPathConstants.NODE);

					strValue = writer.toXMLString(value);

					strValue = strValue.substring(57).replaceAll("</td>", "");
					strValue = strValue.replaceAll("</a>", "");
					pattern = "<a.*href=\".*\">";

					// Create a Pattern object
					r = Pattern.compile(pattern);
					m = r.matcher(strValue);
					strValue = m.replaceAll("").trim();

					System.out.println("---------->Field Name=" + str
							+ "-----: Field value=" + strValue);
				}
			}

			i++;
		}

		// xpathcity
		reader = CrawlerUtil.createXPathReaderByData(html);
		nodeList = (String) reader.read(xpathcity, XPathConstants.STRING);

		System.out.println("City=" + nodeList);

		// contact
		reader = CrawlerUtil.createXPathReaderByData(html);
		nodeList = (String) reader.read(xpathcontact, XPathConstants.STRING);

		System.out.println("Contact=" + nodeList);

		// Content
		reader = CrawlerUtil.createXPathReaderByData(html);
		NodeList nodeList1 = (NodeList) reader.read(xpathContent,
				XPathConstants.NODESET);

		//deleteVisitor.traverse(nodeList1.item(0));
		System.out.println(ContentFilter.changeLink(writer
				.toXMLString(nodeList1.item(0))));

		System.out.println("Content" + writer.toXMLString(nodeList1.item(0)));
	}

	public List<Customer> extractKH(String url, String xpath, String token)
			throws Exception {

		List<Customer> listCus = new ArrayList<Customer>();

		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);
		DomWriter writer = new DomWriter();
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		//CrawlerUtil.analysis(reader.getDocument(),token);

		NodeList nodes = (NodeList) reader.read(xpath, XPathConstants.NODESET);
		int node_one_many = nodes.getLength();

		int i = 1;
		int j = 1;
		String xpathName = "/td[3]/div[2]/a[1]/text()";
		String xpathAddress = "/td[3]/div[@class='address']";
		String xpathLink = "/td[3]/div[@class='fl policy']/a[1]/@href";

		String xpathTel = "/td[4]/div[1]/text()";
		String xpathWeb = "/td[4]/div[@class='website']/a[1]/text()";
		String xpathYahoo = "/td[4]/div[@class='yahoo']/a[1]/@href";
		while (i <= node_one_many) {
			Customer customer = new Customer();
			String name = (String) reader.read(xpath + "[" + i + "]"
					+ xpathName, XPathConstants.STRING);

			Node address = (Node) reader.read(xpath + "[" + i + "]"
					+ xpathAddress, XPathConstants.NODE);
			String link = (String) reader.read(xpath + "[" + i + "]"
					+ xpathLink, XPathConstants.STRING);

			String tel = (String) reader.read(xpath + "[" + i + "]" + xpathTel,
					XPathConstants.STRING);
			String web = (String) reader.read(xpath + "[" + i + "]" + xpathWeb,
					XPathConstants.STRING);
			String yahoo = (String) reader.read(xpath + "[" + i + "]"
					+ xpathYahoo, XPathConstants.STRING);

			System.out.println("Name=" + name);
			customer.name = name;
			System.out.println("address=" + address.getTextContent());
			customer.address = address.getTextContent().trim();
			System.out.println("LInk=" + link);
			customer.link = link;
			System.out.println("Tel=" + tel);
			customer.mobile = tel;
			System.out.println("web=" + web);
			customer.website = web;
			System.out.println("yahoo=" + yahoo);
			customer.yahoo = yahoo;
			listCus.add(customer);
			i++;
		}
		return listCus;

	}

	public List<Customer> extractKHCho(String url, String xpath, String token)
			throws Exception {

		List<Customer> listCus = new ArrayList<Customer>();

		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client
				.fetch("http://chodientu.vn/?portal=proestore&page=list_store_landing&page_no=1");
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);		
		DomWriter writer = new DomWriter();
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		CrawlerUtil.analysis(reader.getDocument(), "Xem thông tin");
		int i = 1;		
		String xpathName = "HTML/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/H4[1]/A[1]/text()";
		String xpathAddress = "HTML/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/P[1]/text()";
		String xpathLink = "HTML/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/H4[1]/A[1]/@href";

		String xpathTel = "HTML/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/P[2]/SPAN[1]/SPAN[1]/text()";
		String xpathWeb = "/td[4]/div[@class='website']/a[1]/text()";
		String xpathYahoo = "HTML/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/P[2]/SPAN[2]/SPAN[1]/A[1]/text()";

		NodeList nodes = (NodeList) reader.read(
						"html/body[1]/div[@id='wrapper']/div[1]/div[2]/div[1]/div[3]/div[1]/ul[1]/li",
						XPathConstants.NODESET);
		int node_one_many = nodes.getLength();
		while (i < node_one_many) {
			String url2 = (String) reader.read(
					"html/body[1]/div[@id='wrapper']/div[1]/div[2]/div[1]/div[3]/div[1]/ul[1]/li["
							+ i + "]"
							+ "/div[2]/div[1]/div[1]/div[1]/a[1]/@href",
					XPathConstants.STRING);

			res = client.fetch("http://chodientu.vn/" + url2);
			HttpClientUtil.printResponseHeaders(res);
			html = HttpClientUtil.getResponseBody(res);
			//System.out.println(UTF8Tool.coDau2KoDau(html));
			writer = new DomWriter();
			XPathReader reader2 = CrawlerUtil.createXPathReaderByData(html);

			Customer customer = new Customer();
			String name = (String) reader2.read(xpathName,
					XPathConstants.STRING);
			System.out.println(name);
			Node address = (Node) reader2.read(xpathAddress,
					XPathConstants.NODE);
			String link = (String) reader2.read(xpathLink,
					XPathConstants.STRING);

			String tel = (String) reader2.read(xpathTel, XPathConstants.STRING);
			String web = (String) reader2.read(xpathWeb, XPathConstants.STRING);
			String yahoo = (String) reader2.read(xpathYahoo,
					XPathConstants.STRING);

			System.out.println("Name=" + name);
			customer.name = name;
			System.out.println("address=" + address.getTextContent());
			customer.address = address.getTextContent().trim();
			System.out.println("LInk=" + link);
			customer.link = "http://chodientu.vn/" + link;
			System.out.println("Tel=" + tel);
			customer.mobile = tel;
			System.out.println("web=" + web);
			customer.website = web;
			System.out.println("yahoo=" + yahoo);
			customer.yahoo = yahoo;
			listCus.add(customer);
			i++;
		}
		return listCus;

	}
	
	
	public List<Customer> extractKHDen(String url, String xpath, String token,List<Customer> listCus)
			throws Exception {
		
		
		
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);
		
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		//CrawlerUtil.analysis(reader.getDocument(),"Cửa hàng điện thoại phumymobile");
		
		
		int i = 1;		
		String xpathName = "html/body[1]/div[@id='tb_global']/div[@id='merchant_header']/div[@id='template_store_name']/text()";
		String xpathAddress = "html/body[1]/div[@id='tb_global']/div[@id='footer']";
		String xpathLink = "html/body[1]/div[@id='tb_global']/div[@id='merchant_header']/div[@id='template_store_domain']/text()";
		
		String xpathTel = "html/body[1]/div[@id='tb_global']/div[@id='merchant_left_column']/div[@id='box_contact']/table[1]/TBODY[1]/tr[2]/td[2]/text()";
		String xpathWeb = "/td[4]/div[@class='website']/a[1]/text()";
		String xpathYahoo = "/td[4]/div[@class='yahoo']/a[1]/@href";
		
		Customer customer = new Customer();
		
			String name = (String) reader.read(
					 xpathName, XPathConstants.STRING);
			
			if(!StringUtil.isEmpty(name))
			{
				Node address = (Node) reader.read( xpathAddress, XPathConstants.NODE);
				String link = (String) reader.read( xpathLink, XPathConstants.STRING);		
				String tel = (String) reader.read(xpathTel,	XPathConstants.STRING);
				String web = (String) reader.read(xpathWeb,XPathConstants.STRING);
				String yahoo = (String) reader.read(xpathYahoo, XPathConstants.STRING);
			
				System.out.println("Name=" + name);
				customer.name = name;
				if(address!=null)
				{
				System.out.println("address=" + address.getTextContent().trim());
				customer.address = HtmlUtil.removeTag(address.getTextContent().trim());
				}
				System.out.println("LInk=" + link);
				customer.link = link;
				System.out.println("Tel=" + tel);
				customer.mobile = tel;
				System.out.println("web=" + web);
				customer.website = web;
				System.out.println("yahoo=" + yahoo);
				customer.yahoo = yahoo;
				listCus.add(customer);
			}
			i++;
		
		return listCus;
	
	}

	public List<Url> getLinkGianHang() throws Exception {
		DocumentAnalyzer analyzer = new DocumentAnalyzer.DefaultDocumentAnayzer(
				"http://www.vatgia.com", "http://www.vatgia.com");
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch("http://vatgia.com/home/shop.php");
		String html = HttpClientUtil.getResponseBody(res);
		List<A> list = null;
		List<Url> listUrl = new ArrayList<Url>();
		try {
			list = analyzer.analyze(html, "http://vatgia.com/home/shop.php");
		} catch (Exception e) {
			list = null;
		}
		Url injectUrl = null;
		String id = "";
		if (list != null)

			for (A a : list) {
				System.out.println(a.getURL());
				id = new UriID(new HttpURL(a.getURL())).getIdAsString();

				boolean kt = AbstractFilter.find(
						"http://www.vatgia.com/home/shop.php.*view=list&iCat",
						a.getURL());
				if (kt) {
					injectUrl = new Url();
					injectUrl.id = id;
					injectUrl.url = a.getURL();
					listUrl.add(injectUrl);
				}
			}
		return listUrl;
	}
	
	public List<Url> getLinkGHDen() throws Exception {
	int i = 1;
	List<Url> listUrl = new ArrayList<Url>();
	while(i<28)	
	{
		
		DocumentAnalyzer analyzer = new DocumentAnalyzer.DefaultDocumentAnayzer(
				"http://www.denthan.com", "http://www.denthan.com");
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch("http://www.denthan.com/store_index.php?page="+i);
		String html = HttpClientUtil.getResponseBody(res);
		List<A> list = null;
		
		try {
			list = analyzer.analyze(html, "http://www.denthan.com/store_index.php?page="+i);
		} catch (Exception e) {
			list = null;
		}
		Url injectUrl = null;
		String id = "";
		if (list != null)

			for (A a : list) {
				System.out.println(a.getURL());
				id = new UriID(new HttpURL(a.getURL())).getIdAsString();

				boolean kt = AbstractFilter.find(
						"http://www.denthan.com/.*",
						a.getURL());
				if (kt) {
					injectUrl = new Url();
					injectUrl.id = id;
					injectUrl.url = a.getURL();
					listUrl.add(injectUrl);
				}
			}
		i++;
	}
		return listUrl;
		
	}

	public static void mainKHVatGia(String[] args) {
		ClassifieExtractBean classifieExtractBean = new ClassifieExtractBean();
		String url = "http://vatgia.com/home/shop.php?view=list&iCat=433";

		try {

			List<Url> urls = classifieExtractBean.getLinkGianHang();
			int j = 1;
			for (Url url2 : urls) {
				if (j > 129) {
					WritableWorkbook workbook = Workbook
							.createWorkbook(new java.io.File("d:/customer" + j
									+ ".xls"));
					WritableSheet s = workbook.createSheet("Sheet_" + j, 0);
					List<Customer> listcusTotal = new ArrayList<Customer>();
					for (int i = 1; i < 50; i++) {

						String xpath = "//table[@class='shop_table']/TBODY[1]/tr[@class='tr']";
						System.out.println("-------------------------->"
								+ url2.url + "&page=" + i);
						List<Customer> listcus = classifieExtractBean
								.extractKH(url2.url + "&page=" + i, xpath, "");
						listcusTotal.addAll(listcus);
						Thread.sleep(2000);

					}
					writeDataSheet(s, listcusTotal);
					workbook.write();
					workbook.close();
				}
				j++;

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void writeDataSheet(WritableSheet s, List<Customer> customers)
			throws WriteException {

		/* Format the Font */
		WritableFont wf = new WritableFont(WritableFont.ARIAL, 10,
				WritableFont.BOLD);
		WritableCellFormat cf = new WritableCellFormat(wf);
		cf.setWrap(true);
		WritableFont wf1 = new WritableFont(WritableFont.ARIAL, 10,
				WritableFont.NO_BOLD);
		WritableCellFormat cf2 = new WritableCellFormat(wf1);
		cf2.setWrap(false);

		/* Creates Label and writes date to one cell of sheet*/
		Label l = new Label(0, 0, "Cong Ty", cf);
		s.addCell(l);
		l = new Label(1, 0, "Dia Chi", cf);
		s.addCell(l);
		l = new Label(2, 0, "Tel", cf);
		s.addCell(l);
		l = new Label(3, 0, "Link", cf);
		s.addCell(l);
		l = new Label(4, 0, "Website", cf);
		s.addCell(l);
		l = new Label(5, 0, "Yahoo", cf);
		s.addCell(l);

		int row = 1;
		for (Customer customer : customers) {
			l = new Label(0, row, customer.name, cf2);
			s.addCell(l);
			l = new Label(1, row, customer.address, cf2);
			s.addCell(l);
			String dt = customer.mobile.replaceAll("Điện thoại :", "");
			l = new Label(2, row, dt, cf2);
			s.addCell(l);
			l = new Label(3, row, customer.link, cf2);
			s.addCell(l);
			l = new Label(4, row, customer.website, cf2);
			s.addCell(l);
			l = new Label(5, row, customer.yahoo, cf2);
			s.addCell(l);
			row++;

		}

	}

	public static void mainCho(String[] args) {
		ClassifieExtractBean classifieExtractBean = new ClassifieExtractBean();
		/*try {
			classifieExtractBean.extractKHCho("http://chodientu.vn/ajax.php?fnc=storeLandingShop&path=store_landing_shop&width=500&store_id=7815",
					"", "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		String url = "http://chodientu.vn/?portal=proestore&page=list_store_landing&page_no=";

		try {

			List<Customer> listcusTotal = new ArrayList<Customer>();
			int j = 1;
			for (j = 1; j <= 35; j++) {
				System.out
						.println("-------------------------->" + url + "" + j);
				List<Customer> listcus = classifieExtractBean.extractKHCho(url
						+ "" + j, "", "");
				listcusTotal.addAll(listcus);
				Thread.sleep(2000);

			}
			WritableWorkbook workbook = Workbook
					.createWorkbook(new java.io.File("d:/customer_cho" + 0
							+ ".xls"));
			WritableSheet s = workbook.createSheet("Sheet_" + j, 0);
			writeDataSheet(s, listcusTotal);
			workbook.write();
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void main_denthan(String[] args) {
		ClassifieExtractBean classifieExtractBean = new ClassifieExtractBean();
		try {
			List<Url> listUrl = classifieExtractBean.getLinkGHDen();
			List<Customer> listCus = new ArrayList<Customer>();
			for (Url url : listUrl) {
				try{
				classifieExtractBean.extractKHDen(url.url, "", "0909554005",listCus);
				}catch (Exception e) {
				 continue;
				}
			}
			System.out.println(listCus.size());
			WritableWorkbook workbook = Workbook
			.createWorkbook(new java.io.File("d:/customer_denthan" 	+ ".xls"));
			WritableSheet s = workbook.createSheet("Sheet_1" , 0);
			writeDataSheet(s, listCus);
			workbook.write();
			workbook.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void extractTruyenTranh(String url) throws Exception {
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);
		//String html = getHTML(url);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		
		int i = 1;		
		String xpathYahoo = "//div[@id='contentWapper']/img";
		String xpathChapter = "//select[@id='rdChapter']/option";
		NodeList nodesChapter = (NodeList) reader.read(xpathChapter, XPathConstants.NODESET);
		int node_chapter_one_many = nodesChapter.getLength();
		
		System.out.println("node_chapter_one_many==> "+node_chapter_one_many);
		
		while (i <= node_chapter_one_many) {
			String chapter_value = (String) reader.read(xpathChapter + "["
					+ i + "]" + "/@value", XPathConstants.STRING);
			String chapter_text = (String) reader.read(xpathChapter + "["
					+ i + "]" + "/text()", XPathConstants.STRING);
			
			chapter_text = chapter_text.replace("Chapter ", "").trim();
			String dau_url  = url.substring(0,url.lastIndexOf("-")+1);
			System.out.println("dau link-------->"+dau_url);
			String link =dau_url+chapter_text+"/40/"+chapter_value+"/2/";
			
			System.out.println("chapter_value==> "+chapter_value.trim());
			System.out.println("chapter_text==> "+chapter_text.trim());
			System.out.println("link==> "+link.trim());
			
			
			String html2 = getHTML(link.trim());
			XPathReader reader2 = CrawlerUtil.createXPathReaderByData(html2);
			int j = 1;	
			NodeList nodes = (NodeList) reader2.read(xpathYahoo, XPathConstants.NODESET);
			int node_one_many = nodes.getLength();
			System.out.println(node_one_many);
			
			File file = new File("D:\\image\\"+i);
			if(!file.exists())file.mkdir();
			
			while (j <= node_one_many) {
				String urlImage = (String) reader2.read(xpathYahoo + "["
						+ j + "]" + "/@data-original", XPathConstants.STRING);
				
				
				downloadImage(urlImage,"D:\\image\\"+i+"\\"+urlImage.substring(urlImage.lastIndexOf("/")));
				System.out.println("price_label==> "+urlImage.trim());
				j++;
			}
			
			i++;
		}
		
	}
	
	public void extractTruyenTranhInfo(String url,int page) throws Exception {
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		//String html = hdc.util.io.IOUtil.getFileContenntAsString("d:/giay.html");
		//String html = HttpClientUtil.getResponseBody(res);;
		String html = getHTML(url);
		//html = html.replaceAll("=28\"", "='28'");
		//html = html.replaceAll("29\"", "'24'");
		//html = html.replaceAll("Đọc truyện tranh Angel Sanctuary - Chapter 1", "'fdll'");
		
		Pattern r1 = Pattern.compile("\\s\\w\\w+=\\w\\w+\"\\s");
	    Matcher m1 = r1.matcher(html);
	     if(m1.find())
	      {
	    	  html = m1.replaceAll(" alt='Tran The Thang' ");
	      }
	     r1 = Pattern.compile("alt=([^\"].+[^\"])");
         m1 = r1.matcher(html);
	     if(m1.find())
	      {
	    	 html = m1.replaceAll("\""+m1.group(1)+"\"");
	      }
	      
		try {
			FileUtil.writeToFile("d:/giay_1.html", html, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//System.out.println(html);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
	
		String xpath_title = "//div[@class='span7 des-story']/h1[1]";
		String xpath_author = "//div[@class='span7 des-story']/p[1]/strong[1]/a[1]/text()";
		String xpath_status = "//div[@class='span7 des-story']/p[2]";
		String xpath_des = "//div[@class='span7 des-story']/div[@class='description']";
		String xpath_img = "//div[@class='span3 story-detail']/img[1]/@src";
		
		String chapter_value = (String) reader.read(xpath_title, XPathConstants.STRING);
		String author = (String) reader.read(xpath_author, XPathConstants.STRING);
		String status = (String) reader.read(xpath_status, XPathConstants.STRING);
		String description = (String) reader.read(xpath_des, XPathConstants.STRING);
		String img = (String) reader.read(xpath_img, XPathConstants.STRING);
		
		System.out.println(chapter_value);
		System.out.println(author);
		System.out.println(status);
		System.out.println(description);
		System.out.println(img);
		saveImage(img,"d://"+img.substring(img.lastIndexOf("/")));
		
		int i = page;
		while(i>=1){
			client = new HttpClientImpl();
			res = client.fetch(url+"page/"+i);
			String html_page = HttpClientUtil.getResponseBody(res);;
			html_page = html_page.replaceAll("29\"", "'24'");
			html_page = html_page.replaceAll("Đọc truyện tranh Angel Sanctuary - Chapter 1", "'fdll'");
			XPathReader reader_page = CrawlerUtil.createXPathReaderByData(html_page);
			
			String xpath_chapter_tr="//div[@id='list-chapter']/div[@class='tr']";
			NodeList nodes = (NodeList) reader_page.read(xpath_chapter_tr, XPathConstants.NODESET);
			int node_one_many = nodes.getLength();
			System.out.println(node_one_many);
			int j=node_one_many;
			while (j >= 1) {
				String xpath_chapter_tr_td = "//div[@id='list-chapter']/div[@class='tr']" + "["
				+ j + "]/div";
				NodeList nodesTD = (NodeList) reader_page.read(xpath_chapter_tr_td, XPathConstants.NODESET);
				int node_one_many_td = nodesTD.getLength();
				int q = node_one_many_td;
				while(q>=1){
				
					String td_alink = (String) reader_page.read(xpath_chapter_tr_td + "["
							+ q + "]/a[1]/@href"  , XPathConstants.STRING);
					System.out.println("td content==> "+td_alink.trim());
					//extractTruyenTranh(td_alink);
					q--;
				}
				
				j--;
			}
			
			i--;
		}
		
		
		
		
	
	}
	
	 public  String getHTMLPage(String url,String storyID,String page) throws Exception {
	    	
	        DefaultHttpClient client = HttpClientFactory.getInstance() ;
	        client.getParams().setParameter("application/x-www-form-urlencoded",true) ;
	        client.getParams().setParameter("accept", "	application/json, text/javascript, */*; q=0.01");
	        client.getParams().setParameter("Content-Type","application/x-www-form-urlencoded; charset=UTF-8") ;
	        client.getParams().setParameter("X-Requested-With","XMLHttpRequest") ;
	        client.getParams().setParameter("Referer","http://vuitruyentranh.vn/truyen-tranh/angel-sanctuary/40/page/2") ;
	        client.getParams().setParameter("User-Agent","Mozilla/5.0 (Windows NT 6.2; rv:20.0) Gecko/20100101 Firefox/20.0") ;
	        client.getParams().setParameter("Cookie","__utma=124071821.1544760243.1368608515.1369112191.1369120719.11; __utmz=124071821.1368608515.1.1.utmgclid=CMrZ6YLgl7cCFWlT4godBHoAMg|utmccn=(not%20set)|utmcmd=(not%20set)|utmctr=truyen%20tranh%20vui; __utmb=124071821.8.10.1369120719; PHPSESSID=g2vuobf1clbjf44nmg9thddpi6; __utmc=124071821; v_a_ban_id=null; v_a_event_id=null; v_a_p_id=null; v_a_type_id=null") ;
	        
	        
	        client.getParams().setParameter("Accept-Encoding","	gzip, deflate") ;
	        client.getParams().setParameter("Accept-Language","en-US,en;q=0.5") ;
	        client.getParams().setParameter("Cache-Control","no-cache") ;
	        
	        client.getParams().setParameter("Connection","keep-alive") ;
	        client.getParams().setParameter("Content-Length","20") ;
	        client.getParams().setParameter("Host","vuitruyentranh.vn") ;
	        client.getParams().setParameter("Pragma","no-cache") ;
	        
	    	HttpPost post = new HttpPost(url) ;
	        List<NameValuePair> list = new ArrayList<NameValuePair>() ;
	        list.add(new BasicNameValuePair("id", storyID));
	        list.add(new BasicNameValuePair("order", "0"));	    
	        list.add(new BasicNameValuePair("page", page));
		
		    post.setEntity(new UrlEncodedFormEntity(list)) ;
	        HttpResponse res = client.execute(post) ;
		    String html = HttpClientUtil.getResponseBody(res) ;
	       // System.out.println(html);
	    	
	        return html;
	    }
	 
	public static void saveImage(String imageUrl, String destinationFile) throws IOException {
		URL url = new URL(imageUrl);
		InputStream is = url.openStream();
		OutputStream os = new FileOutputStream(destinationFile);

		byte[] b = new byte[2048];
		int length;

		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}

		is.close();
		os.close();
	}
	
	public void downloadImage(String imageUrl, String destinationFile) {
		URL url;
		try {
			imageUrl = imageUrl.replaceAll(" ", "%20");
			url = new URL(imageUrl);
			InputStream is = url.openStream();
			OutputStream os = new FileOutputStream(destinationFile);

			byte[] b = new byte[2048];
			int length;

			while ((length = is.read(b)) != -1) {
				os.write(b, 0, length);
			}

			is.close();
			os.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void extract_facebookImage(String url) {
		XPathReader reader = CrawlerUtil.createXPathReaderByData(getHTML(url));
		try {
			CrawlerUtil.analysis(reader.getDocument());
			XPath xpath = XPathFactory.newInstance().newXPath();

			XPathExpression expr = xpath.compile("//IMG[@id='fbPhotoImage']/@src");
			Node nodeImage = (Node) expr.evaluate(reader.getDocument(),
					XPathConstants.NODE);
			
			System.out.println(nodeImage.getTextContent());
		    expr = xpath.compile("//DIV[@id='fbPhotoPageAuthorName']/SPAN");
			Node nodeName = (Node) expr.evaluate(reader.getDocument(),
					XPathConstants.NODE);
			
			if(nodeName == null){
				expr = xpath.compile("//DIV[@id='fbPhotoPageAuthorName']/A");
				 nodeName = (Node) expr.evaluate(reader.getDocument(),
						XPathConstants.NODE);
			}
			
			System.out.println(nodeName.getTextContent());
			SaveImageFromUrl.saveImage(nodeImage.getTextContent(), "/home/crawler/12/"+nodeName.getTextContent()+".jpg");
					

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	
	public void getListIDFriend1(int id) {
		PreparedStatement ps=null;
		
		try {
				Connection connection = C3p0FacePool.getConnection();
				ps = connection.prepareStatement("SELECT id,fid  FROM facebook_user_invittable WHERE account_id =  12 And id >=1121743 order by id asc");
				ResultSet rs = ps.executeQuery();
				while(rs.next())
				{
					String fid = rs.getString("fid");
					String url = "https://www.facebook.com/photo.php?fbid="+fid;
					System.out.println("id -->"+ rs.getInt("id")+"---"+url);
					extract_facebookImage(url);
				}
				C3p0StorePool.attemptClose(connection);
				C3p0StorePool.attemptClose(ps);
				
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public ArrayList<Object> getListIDFriend(int cid) {
		PreparedStatement ps=null;
		
		ArrayList<Object> ds= new ArrayList<Object>();
		try {
				Connection connection = C3p0FacePool.getConnection();
				ps = connection.prepareStatement("SELECT id,fid  FROM facebook_user_invittable WHERE account_id =  12 AND id >= 1123330 order by id asc");
				ResultSet rs = ps.executeQuery();
				while(rs.next())
				{
					String fid = rs.getString("fid");
					int id = rs.getInt("id");
					String arr[] = new  String[2];
					arr[0] = ""+id;
					arr[1] = ""+fid;
					ds.add(arr);
					
					
				}
				
				C3p0StorePool.attemptClose(ps);
				C3p0StorePool.attemptClose(connection);
				
				for (Object object : ds) {
					String arr[] =	(String[])object;
					System.out.println(arr[0]);
					
					
					String url = "https://www.facebook.com/photo.php?fbid="+arr[1] ;
					System.out.println("id -->"+ arr[0] +"---"+url);
					extract_facebookImage(url);
					
				}
				
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ds;
	}
	
	
	public void extractSoVongBongDa(String url) throws Exception {
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);
		//String html = getHTML(url);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		
		Document doc = reader.getDocument();
		CrawlerUtil.analysis(doc);
		
	
	      
		int i = 1;		
		String xlinkVong = "/html/body[1]/center[1]/div/div[5]/div[5]/table/TBODY/tr[25]/td/a";
		NodeList nodesChapter = (NodeList) reader.read(xlinkVong, XPathConstants.NODESET);
		int node_chapter_one_many = nodesChapter.getLength();
		
		System.out.println("node_chapter_one_many==> "+node_chapter_one_many);
		
		while (i <= node_chapter_one_many) {
			String link = (String) reader.read(xlinkVong + "["
					+ i + "]" + "/@href", XPathConstants.STRING);
			String vong = (String) reader.read(xlinkVong + "["
					+ i + "]" + "/text()", XPathConstants.STRING);
			System.out.println("vong==> "+vong);
			
			System.out.println("link==> "+link);
	
			
			i++;
		}
		
	}
	
	public void extractLinkMuaBanNET(String url) throws Exception {
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);
		//String html = getHTML(url);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		
		Document doc = reader.getDocument();
		CrawlerUtil.analysis(doc);
		
		int i = 1;		
		String xlinkVong = "//DIV[@class='mbn-box-list']/DIV[@class='mbn-box-list-content']";
		NodeList nodesChapter = (NodeList) reader.read(xlinkVong, XPathConstants.NODESET);
		int node_chapter_one_many = nodesChapter.getLength();
		
		System.out.println("node_chapter_one_many==> "+node_chapter_one_many);
		
		while (i <= node_chapter_one_many) {
			String link = (String) reader.read(xlinkVong + "["
					+ i + "]" + "/A[1]/@href", XPathConstants.STRING);
			System.out.println("link==> "+link);
			extractMobileMuaBanNet(link);
			i++;
		}
		
	}
	
	
	public void extractMobileMuaBanNet(String url) throws Exception {
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);
		//String html = getHTML(url);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		
		Document doc = reader.getDocument();
		CrawlerUtil.analysis(doc);
		
		//String line = " 0945 794 490  0945.794.497 (Mr. Thao) - 0945.098.890  0974838181 (Mr. Hải) ";
	    String pattern = "\\d{4}\\.\\d{3}\\.\\d{3}|\\d{4}\\s\\d{3}\\s\\d{3}|\\d{10,11}|\\d{4}\\.\\d{2}\\.\\d{2}\\.\\d{3}";
	    Pattern  r = Pattern.compile(pattern);
	    Matcher  m = r.matcher(html);
	    String mobile = "";
	      while (m.find()) {
	    	  Pattern r1 = Pattern.compile("^090|^093|^0122|^0126|^0121|^0128|^0120|^091|^094|^0123|^0125|^0127|^097|^098|^0168|^0169|^0166|^0167|^0165|^096|^095|^092|^0199|^0186");
		      Matcher m1 = r1.matcher( m.group(0) );
		      if(m1.find())
		      {
		    	  mobile =   m.group(0);
	         System.out.println("Found value: " + m.group(0) );
	         FileUtil.writeToFile("c://Projects/mobile_muabannet.txt", mobile.trim()+"\r\n", true);
		      }
	      } 	
	      
		String xPathMobile = "//SPAN[@data-bind='visible:ShowMobile']/text()";
		mobile = (String) reader.read(xPathMobile, XPathConstants.STRING);
		System.out.println("MObile:"+mobile.trim());
		FileUtil.writeToFile("c://Projects/mobile_muabannet.txt", mobile.trim()+"\r\n", true);
			
	}
	
	
	public void extractProductMuaHanhNhanh(String url,int cate_id,int user_id,int product_type) {
		try {
			HttpClientImpl client = new HttpClientImpl();
			HttpResponse res = client.fetch(url);
			HttpClientUtil.printResponseHeaders(res);
			String html = HttpClientUtil.getResponseBody(res);
			XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
			CrawlerUtil.analysis(reader.getDocument());
			List<String> lisString = new ArrayList<String>();
			String xpathUrl = "//DIV[@class='block-post']/UL[1]/LI";
			NodeList nodes1 = (NodeList) reader
					.read(xpathUrl,
							XPathConstants.NODESET);
			int node_one_many1 = nodes1.getLength();int i = 1;
			while (i <= node_one_many1) {
				
				String urlp = (String) reader.read(xpathUrl + "[" + i
						+ "]/A/@href", XPathConstants.STRING);
				lisString.add(urlp);
				i++;
			}	
			
			for (String urlItemPro : lisString) {
				 client = new HttpClientImpl();
				 res = client.fetch(urlItemPro);
				 HttpClientUtil.printResponseHeaders(res);
				 html = HttpClientUtil.getResponseBody(res);
				
				 reader = CrawlerUtil.createXPathReaderByData(html);
				
				// Title
				String title = (String) reader.read("/html/body/div[3]/div[1]/div/div[1]/div/div[2]/div/div/div[1]/a/h3".toUpperCase(),
						XPathConstants.STRING);

				System.out.println("Title=" + title);

				// Price
				String price =   (String) reader.read("/html/body/div[3]/div[1]/div/div[1]/div/div[2]/div/div/div[2]/div[1]/div[2]/div".toUpperCase(), XPathConstants.STRING);

				price =  price.replaceAll("VND","");
				price =  price.replaceAll("\\.","");
				System.out.println("Price=" + price.trim());

				// Image
				//reader = CrawlerUtil.createXPathReaderByData(html);
				String description =   (String) reader.read("/html/body/div[3]/div[1]/div/div[2]/div[1]/div[1]/div[1]/p[1]".toUpperCase(), XPathConstants.STRING);

				System.out.println("Content=" + description.trim());

				String xmainpictrue = "//DIV[@class='list-thumbnail']/A";
				NodeList nodes = (NodeList) reader
						.read(xmainpictrue,
								XPathConstants.NODESET);
				int node_one_many = nodes.getLength();
				 i = 1;
				String folderUpFooterImage = "/home/kktien/domains/kenhkiemtien.com/public_html/kenhkiemtien.com/upload/shop/product/";
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MMdd");
				String ymd = formatter.format(Calendar.getInstance().getTime());
				
				List<String> lisStringImage = new ArrayList<String>();
				while (i <= node_one_many) {
					
					String bigpictrue = (String) reader.read(xmainpictrue + "[" + i
							+ "]/@href", XPathConstants.STRING);
					
					String pathFolder = folderUpFooterImage+ymd+"/";
					File file = new File(pathFolder);
					if (!file.exists())
						file.mkdirs();
					
					lisStringImage.add(bigpictrue.substring(bigpictrue.lastIndexOf("/")+1));
					
					

					String nameimage = bigpictrue.substring(bigpictrue.lastIndexOf("/"));
					hdc.util.io.FileUtil.saveImage(bigpictrue,
							pathFolder + nameimage);
					
					System.out.println("-->Big=" + bigpictrue);
					i++;
				}
				JSONArray jsonA = JSONArray.fromObject(lisStringImage);
				String images = jsonA.toString();
				
				try{
				FootBallDAO.saveProductSwap(title, description, images, user_id, cate_id, Float.parseFloat(price), product_type);
				}catch(Exception e){
					e.printStackTrace();
				}
				
				System.out.println(jsonA);
				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}
	
	public static void main(String[] args) {
		ClassifieExtractBean classifieExtractBean = new ClassifieExtractBean();
		String url = "http://vuitruyentranh.vn/truyen-tranh/angel-sanctuary/chapter-64/40/64/2/";
		try {
			//classifieExtractBean.extractTruyenTranhInfo("http://vuitruyentranh.vn/truyen-tranh/angel-sanctuary/40/",2);
			//classifieExtractBean.getHTMLPage("http://vuitruyentranh.vn/truyen-tranh/angel-sanctuary/40"+"/page/1", "40", "1");
			//classifieExtractBean.extractTruyenTranh(url);
			//classifieExtractBean.getListIDFriend(12);
			//classifieExtractBean.extractSoVongBongDa("http://bongda.wap.vn/ket-qua-vdqg-venezuela-1205.html");
			
			int user_id = 2; int type_product = 4;
		
			/*classifieExtractBean.extractProductMuaHanhNhanh("https://muabannhanh.com/do-choi-tre-em?page=4",1,user_id,type_product);
			classifieExtractBean.extractProductMuaHanhNhanh("https://muabannhanh.com/mua-ban-dien-thoai?page=4",2,user_id,type_product);
			classifieExtractBean.extractProductMuaHanhNhanh("https://muabannhanh.com/dong-ho-mat-kinh?page=4",3,user_id,type_product);
			classifieExtractBean.extractProductMuaHanhNhanh("https://muabannhanh.com/do-dung-gia-dinh?page=4",4,user_id,type_product);
			classifieExtractBean.extractProductMuaHanhNhanh("https://muabannhanh.com/the-thao-da-ngoai-may-tap-the-duc?page=4",5,user_id,type_product);
			classifieExtractBean.extractProductMuaHanhNhanh("https://muabannhanh.com/thoi-trang-nam?page=4",6,user_id,type_product);
			classifieExtractBean.extractProductMuaHanhNhanh("https://muabannhanh.com/thoi-trang-nu?page=4",7,user_id,type_product);
			classifieExtractBean.extractProductMuaHanhNhanh("https://muabannhanh.com/mua-ban-o-to?page=4",8,user_id,type_product);*/
			
			classifieExtractBean.extractMobileMuaBanNet("http://raovat.vnexpress.net/thue-nha-dat/cho-thue/cho-thue-phong-va-villa-du-lich-tai-tp-da-lat-585887.html");
			
			int i = 1;
			while(i<=200){
				classifieExtractBean.extractLinkMuaBanNET("https://muaban.net/thoi-trang-my-pham-toan-quoc-l0-c2?cp=3");
				i++;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
}
