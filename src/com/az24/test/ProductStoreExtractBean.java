package com.az24.test;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.util.html.parser.DomWriter;
import hdc.util.html.parser.XPathReader;
import hdc.util.text.StringUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathConstants;

import org.apache.http.HttpResponse;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ProductStoreExtractBean {

	/**
	 * @param args
	 */

	public void extract_store_product(String url,String xpath_price,  String xpath_soluong,	String token) throws Exception {
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);

		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		CrawlerUtil.analysis(reader.getDocument(),token);
		
		 
		
		
		String node_name = (String) reader.read(this.xpath_name, XPathConstants.STRING);
		DomWriter writer = new DomWriter();
		System.out.println("Name--------->"+node_name);
		
		String soluong = (String) reader.read(this.xpath_soluong, XPathConstants.STRING);
		System.out.println("So Luong------->"+soluong);
		
		String flash = (String) reader.read("//div[@class='header_banner']/div[1]/script[1]/text()", XPathConstants.STRING);
		String nflash = (String) reader.read("//DIV[@id='header']/TABLE[1]/TBODY[1]/TR[1]/TD[1]/DIV[1]/SCRIPT[2]/text()", XPathConstants.STRING);
		// nflash = (String) reader.read("//div[@class='header_banner']/div[1]/img[1]/@src", XPathConstants.STRING);
		////div[@id='header']/table[1]/tbody[1]/tr[1]/td[1]/div[1]/script[2]/text()
		if(nflash!=null)
		System.out.println("flash------->"+nflash);
		
		
		Node price = (Node) reader.read(this.xpath_price, XPathConstants.NODE);
		if(price!=null)
		System.out.println("Gia------->"+price.getTextContent());
		
		String ensure = (String) reader.read(this.xpath_ensuare, XPathConstants.STRING);
		System.out.println("Bao Hanh------->"+ensure);
		
		String quanlity = (String) reader.read(this.xpath_quanlity, XPathConstants.STRING);
		System.out.println("Tinh Trang------->"+quanlity);
		
		
		String madein = (String) reader.read(this.xpath_madein, XPathConstants.STRING);
		System.out.println("Xuat xu------->"+madein);
		
		String website = (String) reader.read(this.xpath_website, XPathConstants.STRING);
		System.out.println("Website------->"+website);
		
		//Node node_picture = (Node) reader.read(this.xpath_image, XPathConstants.NODE);
		
		//System.out.println("Image------->"+writer.toXMLString(node_picture));
	
		String node_picture = (String) reader.read(this.xpath_image, XPathConstants.STRING);
		
		System.out.println("Image------->"+node_picture);
	
		
		
		Node description = (Node) reader.read(this.xpath_description, XPathConstants.NODE);
		
	//	System.out.println("description:"+writer.toXMLString(description));
		
		String xpath_anh = "HTML/BODY[@id='topcategoriesdisplay']/DIV[@id='container']/DIV[@id='pagebody']/DIV[@id='content']/DIV[@id='home_lifestyle']/TABLE[1]/TBODY[1]/TR[1]/TD[1]/TABLE[1]/FORM[@name='cart_quantity']/TBODY[1]/TR[1]/TD[1]/TABLE[1]/TBODY[1]/TR[3]/TD[1]/TABLE[1]/TBODY[1]/TR[2]/TD[1]/TABLE[1]/TBODY[1]/TR[1]/TD[1]/TABLE[1]/TBODY[1]/TR[1]/TD";
		NodeList nodes_image = (NodeList) reader.read(xpath_anh, XPathConstants.NODESET);
		int node_one_many = nodes_image.getLength();
		int i = 1;
		String sub_xpath ="";	
		while (i <= node_one_many) {
			String image_path = (String) reader.read(xpath_anh + "["
					+ i + "]" + "/A[1]/IMG[1]/@src", XPathConstants.STRING);
			System.out.println(image_path);
			i++;
		}
		
		String xpath_tech =this.xpath_tech;
		NodeList node_techs = (NodeList) reader.read(xpath_tech, XPathConstants.NODESET);
		node_one_many = node_techs.getLength();
		i = 1;
		sub_xpath ="";
		Pattern pattern = Pattern.compile("\\•", Pattern.CASE_INSENSITIVE);
		Matcher matcher = null;
		while (i <= node_one_many) {
			String tech_lable = (String) reader.read(xpath_tech + "["
					+ i + "]" + this.xpath_tech_label, XPathConstants.STRING);
			if(!StringUtil.isEmpty(tech_lable))
			{
				Node tech_value = (Node) reader.read(xpath_tech + "["
					+ i + "]" + xpath_tech_value, XPathConstants.NODE);
				//System.out.println(tech_lable);
				if(tech_value!=null)
				{
				matcher = pattern.matcher(tech_value.getTextContent().trim());
				String value = matcher.replaceAll(";");
				System.out.println(tech_lable+"="+tech_value.getTextContent().trim());
				System.out.println("-->"+value.trim());
				System.out.println("-->"+ writer.toXMLString(tech_value));
				}
			}
			
			i++;
		}
		
	}
	
	
	public void extract_store_product_cb(String url, String xpath_price, String xpath_soluong,
			String token) throws Exception {
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);

		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
	//	CrawlerUtil.analysis(reader.getDocument());
		
		String node_name = (String) reader.read(xpath_name, XPathConstants.STRING);
		DomWriter writer = new DomWriter();
		System.out.println(node_name);		
		
		Node node_picture = (Node) reader.read(xpath_image, XPathConstants.NODE);		
	//	System.out.println((node_picture.getTextContent()));	
		
		NodeList nodes = null;
		int node_one_many = 0;
		nodes = (NodeList) reader.read("HTML/BODY[1]/CENTER[1]/TABLE[@id='content']/TBODY[1]/TR[1]/TD[@id='content_center']/DIV[2]/DIV[3]/TABLE[1]/TBODY[1]/TR[2]/TD[2]/DIV[3]/TABLE[1]/TBODY[1]/TR", XPathConstants.NODESET);
		node_one_many = nodes.getLength();
		int i = 1;
		
		while (i <= node_one_many) {
			String price_label = (String) reader.read("HTML/BODY[1]/CENTER[1]/TABLE[@id='content']/TBODY[1]/TR[1]/TD[@id='content_center']/DIV[2]/DIV[3]/TABLE[1]/TBODY[1]/TR[2]/TD[2]/DIV[3]/TABLE[1]/TBODY[1]/TR" + "["
					+ i + "]" + "/TD[1]/text()", XPathConstants.STRING);
			System.out.println(price_label.trim());
			Node price_value = (Node) reader.read("HTML/BODY[1]/CENTER[1]/TABLE[@id='content']/TBODY[1]/TR[1]/TD[@id='content_center']/DIV[2]/DIV[3]/TABLE[1]/TBODY[1]/TR[2]/TD[2]/DIV[3]/TABLE[1]/TBODY[1]/TR" + "["
					+ i + "]" + "/TD[2]", XPathConstants.NODE);
			System.out.println(price_value.getTextContent().trim());
			
			i++;
		}
		
		nodes = null;
		node_one_many = 0;
		nodes = (NodeList) reader.read("HTML/BODY[1]/CENTER[1]/TABLE[@id='content']/TBODY[1]/TR[1]/TD[@id='content_center']/DIV[2]/DIV[3]/TABLE[1]/TBODY[1]/TR[2]/TD[1]/DIV[4]/TABLE[1]/TBODY[1]/TR", XPathConstants.NODESET);
		node_one_many = nodes.getLength();
		 i = 1;
		
		while (i <= node_one_many) {
			String picture = (String) reader.read("HTML/BODY[1]/CENTER[1]/TABLE[@id='content']/TBODY[1]/TR[1]/TD[@id='content_center']/DIV[2]/DIV[3]/TABLE[1]/TBODY[1]/TR[2]/TD[1]/DIV[4]/TABLE[1]/TBODY[1]/TR" + "["
					+ i + "]" + "/TD[1]/DIV[1]/IMG/@src", XPathConstants.STRING);
			picture = (String) reader.read("HTML/BODY[1]/CENTER[1]/TABLE[@id='content']/TBODY[1]/TR[1]/TD[@id='content_center']/DIV[2]/DIV[3]/TABLE[1]/TBODY[1]/TR[2]/TD[1]/DIV[4]/TABLE[1]/TBODY[1]/TR" + "["
					+ i + "]" + "/TD[2]/DIV[1]/IMG/@src", XPathConstants.STRING);
			picture = (String) reader.read("HTML/BODY[1]/CENTER[1]/TABLE[@id='content']/TBODY[1]/TR[1]/TD[@id='content_center']/DIV[2]/DIV[3]/TABLE[1]/TBODY[1]/TR[2]/TD[1]/DIV[4]/TABLE[1]/TBODY[1]/TR" + "["
					+ i + "]" + "/TD[3]/DIV[1]/IMG/@src", XPathConstants.STRING);
			System.out.println(picture.trim());
			
			i++;
		}

		
		                                                                                                                                                         
		String pice = (String) reader.read(this.xpath_price, XPathConstants.STRING);		
		System.out.println((pice));
		
		String transport = (String) reader.read(this.xpath_transport, XPathConstants.STRING);		
		System.out.println((transport));		
		
	
		
		xpath_soluong = "HTML/BODY[1]/CENTER[1]/TABLE[@id='content']/TBODY[1]/TR[1]/TD[@id='content_center']/DIV[2]/DIV[3]/TABLE[1]/TBODY[1]/TR[2]/TD[2]/DIV[3]/DIV";
		nodes = (NodeList) reader.read(xpath_soluong, XPathConstants.NODESET);
		node_one_many = nodes.getLength();
		i = 1;
	
		
		while (i <= node_one_many) {
			String price_label = (String) reader.read(xpath_soluong + "["
					+ i + "]" + "/text()", XPathConstants.STRING);
			System.out.println(price_label.trim());
			
			i++;
		}
		
		Node description = (Node) reader.read(xpath_description, XPathConstants.NODE);
		
	//	System.out.println(description.getTextContent());	
		
		String xpath_tech =this.xpath_tech;
		NodeList node_techs = (NodeList) reader.read(xpath_tech, XPathConstants.NODESET);
		node_one_many = node_techs.getLength();
		i = 1;
		String sub_xpath ="";
		Pattern pattern = Pattern.compile("\\•", Pattern.CASE_INSENSITIVE);
		Matcher matcher = null;
		while (i <= node_one_many) {
			String tech_lable = (String) reader.read(xpath_tech + "["
					+ i + "]" + xpath_tech_label, XPathConstants.STRING);
			if(!StringUtil.isEmpty(tech_lable))
			{
				Node tech_value = (Node) reader.read(xpath_tech + "["
					+ i + "]" + xpath_tech_value, XPathConstants.NODE);
				//System.out.println(tech_lable);
				if(tech_value!=null)
				{
				matcher = pattern.matcher(tech_value.getTextContent().trim());
				String value = matcher.replaceAll(";");
				System.out.println(tech_lable+"="+tech_value.getTextContent().trim());
				System.out.println("-->"+value.trim());
				System.out.println("-->"+ tech_value.getNodeValue());
				}
			}
			
			i++;
		}
		
	}
	
	
	public String xpath_name ="";
	public String xpath_price = "";
	public String xpath_soluong = "";
	public String xpath_quanlity ="";
	public String xpath_transport ="";
	public String xpath_madein ="";
	public String xpath_ensuare ="";
	public String xpath_website ="";
	public String xpath_image ="";
	public String xpath_tech ="";
	public String xpath_tech_label ="";
	public String xpath_tech_value ="";
	public String xpath_description="";
	public static void main(String[] args) throws Exception {
		String url ="http://vietlinkshn.com/tours-min-trung/679-vlmt17";		
		ProductStoreExtractBean extractBean = new ProductStoreExtractBean();
		extractBean.xpath_name = "//div[@id='hta-maincol']/table[1]/TBODY[1]/tr[1]/td[1]/text()";
		extractBean.xpath_soluong ="//div[@id='product_detail_text']/table[1]/tbody[1]/tr[3]/td[2]/text()";
		extractBean.xpath_price = "//div[@id='product_detail_text']/table[1]/tbody[1]/tr[2]/td[2]/div[1]/span[1]/span[1]/text()";
		extractBean.xpath_quanlity = "//div[@class='tomtat']/p[1]/span[1]/text()";
		extractBean.xpath_transport = "//div[@class='boxModule blckPrice']/div[1]/div[1]/div[1]/p[3]/span[2]/text()";
		extractBean.xpath_madein = "HTML/BODY[1]/CENTER[1]/TABLE[@id='content']/TBODY[1]/TR[1]/TD[@id='content_center']/DIV[2]/DIV[3]/TABLE[1]/TBODY[1]/TR[2]/TD[2]/DIV[3]/DIV[5]/text()";
		extractBean.xpath_ensuare = "HTML/BODY[1]/CENTER[1]/TABLE[@id='content']/TBODY[1]/TR[1]/TD[@id='content_center']/DIV[2]/DIV[3]/TABLE[1]/TBODY[1]/TR[2]/TD[2]/DIV[3]/DIV[5]/text()";
		extractBean.xpath_website = "HTML/BODY[1]/CENTER[1]/TABLE[@id='content']/TBODY[1]/TR[1]/TD[@id='content_center']/DIV[2]/DIV[3]/TABLE[1]/tbody[1]/TR[2]/TD[2]/DIV[3]/DIV[5]/text()";
		extractBean.xpath_image = "//div[@id='hta-maincol']/table[2]/TBODY[1]/tr[1]/td[1]/table[1]/tbody[1]/tr[1]/td[1]/a[1]/img[1]/@src";
		extractBean.xpath_tech = "html/BODY[1]/div[@id='wrapper']/div[1]/div[2]/div[5]/div[2]/ul[1]/li";
		extractBean.xpath_tech_label = "/div[1]/text()";
		extractBean.xpath_tech_value = "/div[2]";
		extractBean.xpath_description ="//div[@id='hta-maincol']/table[2]";
		extractBean.extract_store_product(url, "", "", "");
	//	extractBean.extract_store_product_cb(url, "", "", "Thiền Viện Cái Bầu - Thánh Địa Bên Vịnh Bái Tử Long");
		
		
		
		// TODO Auto-generated method stub

	}

}
