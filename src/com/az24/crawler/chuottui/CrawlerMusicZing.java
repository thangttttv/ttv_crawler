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

public class CrawlerMusicZing {
	private String url_zing="";
	private String lyric_zing="";
	private MusicArtist  musicArtist = null;
	
	public void collectionLink(String url,int cate_parent_id,int category_id, int language,int type_zing, int page) throws Exception {
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		
		String xpath__tag= "//div[@id='_contentLst']/div[2]/div";
		NodeList linkNodes = (NodeList) reader.read(xpath__tag, XPathConstants.NODESET);
		
		Thread.sleep(1000);
		
		int node_one_many = linkNodes.getLength();
		int i=2;
		ChuotTuiDAO chuotTuiDAO = new ChuotTuiDAO();
		Music music = null;
		MusicLike musicLike = null;
		MusicHit musicHit = null;
		MusicReport musicReport = null;
		MusicLyrics musicLyrics = null;
		String alias = "";
		int singer_id = 0;
		int musicId= 0;
		
		while (i < node_one_many) {
			String link = (String) reader.read(xpath__tag+"["+ i + "]/h2[1]/a[1]/@href", XPathConstants.STRING);
			String link_singer = (String) reader.read(xpath__tag+"["+ i + "]/p[1]/a[1]/@href", XPathConstants.STRING);
			String m_title = (String) reader.read(xpath__tag+"["+ i + "]/h2[1]/a[1]/text()", XPathConstants.STRING);
			String m_singer = (String) reader.read(xpath__tag+"["+ i + "]/p[1]/a[1]/text()", XPathConstants.STRING);
			System.out.println("http://www.mp3.zing.vn"+link);
			chuotTuiDAO.openConnection();
			singer_id = chuotTuiDAO.getSinger(m_singer.trim());
			musicArtist = null;
			
			if(singer_id ==0&&!StringUtil.isEmpty(m_singer))
			{
				musicArtist = new MusicArtist();
				musicArtist.name = m_singer.trim();
				musicArtist.type = 0;
				musicArtist.status = 1;
				musicArtist.value = 200;
				musicArtist.create_date = Calendar.getInstance().getTimeInMillis()/1000;
				this.extractSinger("http://www.mp3.zing.vn"+link_singer);				
				singer_id=chuotTuiDAO.saveSinger(musicArtist);
				ArtistRanking artistRanking = new ArtistRanking();
				artistRanking.artistId = singer_id;
				artistRanking.value = 200;
				artistRanking.ranking = 0;
				if(singer_id>0)
				chuotTuiDAO.saveArtistRanking(artistRanking);
				Thread.sleep(1000);
			}
			
			musicId = chuotTuiDAO.checkMusicExits(m_title.trim(), singer_id);
			
			if(musicId>0){
				i++;
				if(chuotTuiDAO.checkLyric(musicId)==0)
				{
					this.lyric_zing = "";
					this.extractMusic("http://www.mp3.zing.vn"+link);
					musicLyrics = new MusicLyrics();
					musicLyrics.content = this.lyric_zing;
					musicLyrics.create_date = Calendar.getInstance().getTimeInMillis()/1000;
					musicLyrics.userId = 0;
					musicLyrics.username = "auto";
					musicLyrics.musicId = musicId;		
					if(!StringUtil.isEmpty(this.lyric_zing))
					chuotTuiDAO.updateMusicLyric(musicLyrics);
				}					
				continue;
			
			}
		    
			music = new Music();
			music.parentCategoryId =  cate_parent_id;
			music.categoryId = category_id;
			music.singerId = singer_id;
			music.singer_name = m_singer.trim();
			music.name = m_title;
			alias = this.getAlias(m_title);
			music.alias = alias;
			music.crow = 1;
			music.language = language;
			music.create_date = Calendar.getInstance().getTimeInMillis()/1000;
			music.update_date = Calendar.getInstance().getTimeInMillis()/1000;
			
			this.url_zing ="";
			this.lyric_zing = "";
			this.extractMusic("http://www.mp3.zing.vn"+link);
			music.url_zing = this.url_zing;
			music.userId = 0;
			music.username ="auto";
			music.source_zing = "http://www.mp3.zing.vn"+link;
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
		String xpath_url_file = "//object[@id='oplayer']/embed[@id='player']/@flashvars";
		String xpath_lyric ="//div[@id='_lyricContainer']/div[1]/div[1]/p[1]";
		
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		String html;
		try {
			html = HttpClientUtil.getResponseBody(res);
			XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
			String url_file = (String) reader.read(xpath_url_file, XPathConstants.STRING);
			
			String url_files[] = url_file.split("&");
			url_file = url_files[2] ;
			this.url_zing = url_file.split("=")[1];
			//System.out.println(this.url_zing);
			
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
		if(node_company!=null&&!StringUtil.isEmpty(node_company.getTextContent())&&node_company.getTextContent().split(":").length>1)
		this.musicArtist.represent_company = node_company.getTextContent().split(":")[1];
		
		
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
	      node2.name = "span";
	      node2.attribue = "singer-image";
	      nodesDel.add(node2);
	     
	      node2 = new hdc.crawler.Node();
	      node2.name = "span";
	      node2.attribue = "lyric-author";
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
		CrawlerMusicZing crawlerMusicZing = new CrawlerMusicZing();
		MusicSource.initSourceLinkZing();
		List<MusicSource> listLink = MusicSource.muList;
		try {
			int type_zing = 0;
			for (MusicSource musicSource : listLink) {
				int i = 1;
				while(i<=20)
				{		
				 System.out.println("Page="+i);	
				 crawlerMusicZing.collectionLink(musicSource.url+"?p="+i, musicSource.parent_cat_id, musicSource.cat_id, musicSource.language,type_zing, i);
			 	 i++;
				}
			}
		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
