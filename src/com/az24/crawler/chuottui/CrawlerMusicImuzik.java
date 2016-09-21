package com.az24.crawler.chuottui;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.util.html.NodeDeleteVisitor;
import hdc.util.html.NodeNormalizeVisitor;
import hdc.util.html.parser.DomWriter;
import hdc.util.html.parser.XPathReader;
import hdc.util.text.StringUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathConstants;

import org.apache.http.HttpResponse;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.az24.test.HttpClientUtil;
import com.az24.util.UTF8Tool;

public class CrawlerMusicImuzik {
	private String url_zing="";
	private String lyric_zing="";
	private MusicArtist  musicArtist = null;
	public int type  = 0;
	public void collectionLink(String url,int cate_parent_id,int category_id, int language,int type_zing,String nationality, int page) throws Exception {
		
		HttpClientImpl client = new HttpClientImpl();
		if(url.indexOf("http://")==-1) url = "http://imuzik.com.vn/"+url;
		System.out.println(url);
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		String xpath__tag= "//div[@id='song-lists-container_v2']/div[1]/div";
		NodeList linkNodes = (NodeList) reader.read(xpath__tag, XPathConstants.NODESET);
		int node_one_many = linkNodes.getLength();
		int i=1;
		ChuotTuiDAO chuotTuiDAO = new ChuotTuiDAO();
		Music music = null;
		MusicLike musicLike = null;
		MusicHit musicHit = null;
		MusicReport musicReport = null;
		MusicLyrics musicLyrics = null;
		String alias = "";
		int singer_id = 0;
		int musicId= 0;
		
		while (i <= node_one_many) {
			String m_title = (String) reader.read(xpath__tag+"["+ i + "]/div[1]/p[1]/a[1]/text()", XPathConstants.STRING);
			String m_singer = (String) reader.read(xpath__tag+"["+ i + "]/div[1]/p[2]/a[1]/text()", XPathConstants.STRING);
			String m_code_mback = (String) reader.read(xpath__tag+"["+ i + "]/div[1]/p[3]/b[1]/text()", XPathConstants.STRING);
			System.out.println(m_title+m_singer+m_code_mback);
			chuotTuiDAO.openConnection();
			String m_singer_arr[] = m_singer.split("&");
			int sttcs=1;
			int temp_singer_id = 0;
			for (String string : m_singer_arr) {				
				temp_singer_id = chuotTuiDAO.getSinger(string.trim());
				musicArtist = null;				
				if(temp_singer_id ==0&&!StringUtil.isEmpty(m_singer))
				{
					musicArtist = new MusicArtist();
					musicArtist.name = m_singer.trim();
					musicArtist.type = 0;
					musicArtist.status = 1;
					musicArtist.value = 200;
					musicArtist.nationality =nationality;
					musicArtist.create_date = Calendar.getInstance().getTimeInMillis()/1000;
					temp_singer_id=chuotTuiDAO.saveSinger(musicArtist);
					ArtistRanking artistRanking = new ArtistRanking();
					artistRanking.artistId = temp_singer_id;
					artistRanking.value = 200;
					artistRanking.ranking = 0;
					chuotTuiDAO.saveArtistRanking(artistRanking);
				
				}
				if(sttcs==1) singer_id = temp_singer_id;
				sttcs++;
			}
			musicId = chuotTuiDAO.checkMusicExits(m_title.trim(), singer_id);
			
			if(musicId>0)
			{
				if(chuotTuiDAO.checkMusicBackExits(musicId, type)==0)
				{
					RingBack ringBack = new RingBack();
					ringBack.code = m_code_mback.trim();
					ringBack.create_date = Calendar.getInstance().getTimeInMillis()/1000;
					ringBack.musicId = musicId;
					ringBack.name=m_title;
					ringBack.supplier = 1;
					//0 là Viettel, 1 là vinaphone, 2 là mobiphone, 3 là beeline, 4 là vietnammobile
					ringBack.type=type;
					chuotTuiDAO.saveNhacCho(ringBack);
				}
				i++;
				continue;
			}
			
			music = new Music();
			music.parentCategoryId =  cate_parent_id;
			music.categoryId = category_id;
			music.singerId = singer_id;
			music.singer_name = m_singer.trim();
			music.name = m_title.trim();
			alias = this.getAlias(m_title.trim());
			music.alias = alias;
			music.crow = 1;
			music.language = language;
			music.create_date = Calendar.getInstance().getTimeInMillis()/1000;
			music.update_date = Calendar.getInstance().getTimeInMillis()/1000;
			this.url_zing ="";
			this.lyric_zing = "";
			music.url_nct = this.url_zing;
			music.userId = 0;
			music.username ="auto";
			music.source_nct = "";
			music.type_zing = type_zing;
			music.status =1;
			
			musicId = chuotTuiDAO.saveMusic(music);
			
			if(musicId>0)
			{
				musicLyrics = new MusicLyrics();
				musicLyrics.content = this.lyric_zing;
				musicLyrics.create_date = Calendar.getInstance().getTimeInMillis()/1000;
				musicLyrics.userId = 0;
				musicLyrics.username = "auto";
				musicLyrics.musicId = musicId;
				
				chuotTuiDAO.saveMusicLyric(musicLyrics);
				
				musicLike = new MusicLike();
				musicLike.like = 0;
				musicLike.musicId = musicId;
				
				chuotTuiDAO.saveMusicLike(musicLike); 
				
				musicHit = new MusicHit();
				musicHit.artistId = 0;
				musicHit.musicId =  musicId;
				musicHit.singerId = singer_id;
				musicHit.singer_name = m_singer.trim();
				musicHit.status = 1;
				musicHit.hit = 0;
				
				chuotTuiDAO.saveMusicHit(musicHit);
				
				musicReport = new MusicReport();
				musicReport.musicId = musicId;
				musicReport.report = 0;
				
				chuotTuiDAO.saveMusicReport(musicReport);
				
				RingBack ringBack = new RingBack();
				ringBack.code = m_code_mback.trim();
				ringBack.create_date = Calendar.getInstance().getTimeInMillis()/1000;
				ringBack.musicId = musicId;
				ringBack.name=m_title.trim();
				ringBack.supplier = 1;
				//0 là Viettel, 1 là vinaphone, 2 là mobiphone, 3 là beeline, 4 là vietnammobile
				ringBack.type=type;
				chuotTuiDAO.saveNhacCho(ringBack);
				
			}
			chuotTuiDAO.closeConnection();
			i++;
			
			Thread.sleep(1000);
		}
		
	}
	
	public String getAlias(String name)
	{
		Pattern pattern = Pattern.compile("\\W+");
		Pattern pattern2 = Pattern.compile("-$");				
		name = UTF8Tool.coDau2KoDau(name).trim();
		Matcher matcher = pattern.matcher(name);
		String url_rewrite=matcher.replaceAll("-");
		matcher = pattern2.matcher(url_rewrite);
		url_rewrite = matcher.replaceAll("");		
		return url_rewrite;
	}
	
	public void extractMusic(String url) 
	{
		String xpath_url_file = "//div[@id='flash-player-holder']/object[1]/embed[1]/@flashvars";
		String xpath_lyric ="//div[@id='lyric-info']/div[2]/div[@id='lyric']";
		
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		String html;
		try {
			html = HttpClientUtil.getResponseBody(res);
			XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
			String url_file = (String) reader.read(xpath_url_file, XPathConstants.STRING);
			
			String url_files[] = url_file.split("&");
			url_file = url_files[3] ;
			this.url_zing = url_file.substring(5);
			System.out.println(this.url_zing);
			
			Node content = (Node) reader.read(xpath_lyric, XPathConstants.NODE);
			DomWriter writer = new DomWriter();
			deleteVisitor.traverse(content);
			String lyric = writer.toXMLString(content);
			if (lyric != null
					&& lyric.length() > "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
							.length()) {
				lyric = lyric
						.substring("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
								.length());
			}
			//System.out.println(lyric);
			this.lyric_zing = lyric;
			Thread.sleep(100);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void extractSinger(String url) throws Exception
	{

		String xpath_name ="//div[@class='singer-info-block']/h1[1]/a[1]/text()";
		String xpath_full_name ="//div[@class='singer-info-block']/p[1]";
		String xpath_full_birthday ="//div[@class='singer-info-block']/p[2]";
		String xpath_full_company ="//div[@class='singer-info-block']/p[3]";
		String xpath_description = "//div[@class='singer-info-block']";
		DomWriter writer = new DomWriter();
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		
		String name = (String) reader.read(xpath_name, XPathConstants.STRING);
		Node node_full_name = (Node) reader.read(xpath_full_name, XPathConstants.NODE);
		Node node_birthday = (Node) reader.read(xpath_full_birthday, XPathConstants.NODE);
		Node node_company = (Node) reader.read(xpath_full_company, XPathConstants.NODE);
		
		if(!StringUtil.isEmpty(name))
		this.musicArtist.name = name.trim();
		if(node_full_name!=null&&!StringUtil.isEmpty(node_full_name.getTextContent()))
		this.musicArtist.full_name = node_full_name.getTextContent().split(":")[1];
		if(node_birthday!=null&&!StringUtil.isEmpty(node_birthday.getTextContent()))
		this.musicArtist.birthday = node_birthday.getTextContent().split("-")[0].split(":")[1];
		if(node_birthday!=null&&!StringUtil.isEmpty(node_birthday.getTextContent()))
		this.musicArtist.nationality = node_birthday.getTextContent().split("-")[1].split(":")[1];
		if(node_company!=null&&!StringUtil.isEmpty(node_company.getTextContent()))
		this.musicArtist.represent_company = node_company.getTextContent().split(":")[1];
		
	/*	System.out.println(name);
		System.out.println(node_full_name.getTextContent());
		System.out.println(node_birthday.getTextContent());
		System.out.println(node_company.getTextContent());*/
		
		Node content = (Node) reader.read(xpath_description, XPathConstants.NODE);
	
		
		deleteVisitor.traverse(content);
		normalVisitor.traverse(content);
		String description = writer.toXMLString(content);
		if (description != null
				&& description.length() > "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
						.length()) {
			description = description
					.substring("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
							.length());
		}
		this.musicArtist.description = description;
		//System.out.println(description);		
		Thread.sleep(100);
	}
	
	
	
	private NodeDeleteVisitor deleteVisitor = new NodeDeleteVisitor() {
	    protected boolean shouldDelete(Node node) {
	      if(node.getNodeName().equalsIgnoreCase("meta")) return true ;
	      else if(node.getNodeName().equalsIgnoreCase("link")) return true ;
	      else if(node.getNodeName().equalsIgnoreCase("style")) return true ;
	      else if(node.getNodeName().equalsIgnoreCase("script")) return true ;
	      else if(node.getNodeName().equalsIgnoreCase("iframe")) return true ;
	      List<hdc.crawler.Node>  nodesDel = new ArrayList<hdc.crawler.Node>();
	      hdc.crawler.Node node2 = new hdc.crawler.Node();
	      node2.name = "div";
	      node2.attribue = "lyric-content-add";
	      nodesDel.add(node2);
	     
	      node2 = new hdc.crawler.Node();
	      node2.name = "span";
	      node2.attribue = "lyric-more";
	      nodesDel.add(node2);
	  
	      if(nodesDel!=null)
	      {
	    	  int i =0;
	    	  while(i<nodesDel.size())
	    	  {
	    		 if(nodesDel.get(i).name.equalsIgnoreCase(node.getNodeName()))
	    		 {
	    			 int j = 0;
	    			 while(j<node.getAttributes().getLength())
	    			 {
	    				 if(node.getAttributes().item(j).getTextContent().equalsIgnoreCase(nodesDel.get(i).attribue))
	    					 return true;
	    				 	 
	    				 j++;
	    			 }
	    		 }
	    		  i++;
	    	  }
	      }
	      return false ;
	    }
	  } ;
	
	  private NodeNormalizeVisitor normalVisitor = new NodeNormalizeVisitor() {
		    protected void normalize(Node node) {
		      if(node!=null&&node.hasAttributes()) {
		        NamedNodeMap attributes = node.getAttributes();
		        if(attributes.getNamedItem("class") != null) attributes.removeNamedItem("class");
		        if(attributes.getNamedItem("id") != null&&!"object".equalsIgnoreCase(node.getNodeName())) attributes.removeNamedItem("id");
		        if(attributes.getNamedItem("style") != null) attributes.removeNamedItem("style");
		        if(attributes.getNamedItem("height") != null&&(!"object".equalsIgnoreCase(node.getNodeName())||!"embed".equalsIgnoreCase(node.getNodeName()))) attributes.removeNamedItem("height");
		        if(attributes.getNamedItem("width") != null&&(!"object".equalsIgnoreCase(node.getNodeName())||!"embed".equalsIgnoreCase(node.getNodeName()))) attributes.removeNamedItem("width");
		        if(attributes.getNamedItem("onclick") != null) attributes.removeNamedItem("onclick");
		        if(attributes.getNamedItem("title") != null) attributes.removeNamedItem("title");
		        if(attributes.getNamedItem("rel") != null) attributes.removeNamedItem("rel") ;
		        if(attributes.getNamedItem("target") != null) attributes.removeNamedItem("target") ;
		        String tag = node.getNodeName();
		        if (node instanceof Element && "a".equalsIgnoreCase(tag)) {
					Element a = (Element) node;
					a.setAttribute("rel", "nofollow");
					a.setAttribute("href", "#");
		        }
		      } 
		      
		    }
		  } ;
		  
	public static void main(String[] args) {
		CrawlerMusicImuzik crawlerMusicZing = new CrawlerMusicImuzik();
		MusicSource.initSourceLinkIMuzik();
		List<MusicSource> mList = MusicSource.muList;
		try {
			int type_zing = 0;
			for (MusicSource musicSource : mList) {
				int i = Integer.parseInt(args[0]);
				while(i<=musicSource.page)
				{		
				 System.out.println("Page="+i);	
				 crawlerMusicZing.collectionLink(musicSource.url+i+musicSource.url_1, musicSource.parent_cat_id, musicSource.cat_id, musicSource.language,type_zing,"Việt Nam", i);
			 	 i++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
