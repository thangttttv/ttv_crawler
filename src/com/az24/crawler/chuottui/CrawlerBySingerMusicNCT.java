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
import java.util.HashMap;
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

public class CrawlerBySingerMusicNCT {
	private String url_zing="";
	private String lyric_zing="";
	private String tacgia="";
	private int parent_cat_id=0;
	private int cat_id=0;
	private MusicArtist  musicArtist = null;
	
	public static HashMap<String,Integer> catHashMap =null;
	
	public static void  intCatMap()
	{
		catHashMap = new HashMap<String, Integer>();
		catHashMap.put("nhac au my", 34);
		catHashMap.put("country", 65);
		catHashMap.put("pop", 35);
		catHashMap.put("new age world music", 41);
		catHashMap.put("rock", 36);
		catHashMap.put("electronic dance", 37);
		catHashMap.put("rap hip hop", 38);
		catHashMap.put("blues jazz", 39);
		catHashMap.put("r b soul", 40);
		catHashMap.put("folk", 42);
		catHashMap.put("trance house techno", 43);
		catHashMap.put("indie", 44);
		
		catHashMap.put("nhac han quoc", 17);
		catHashMap.put("trot", 18);
		catHashMap.put("blues jazz", 23);
		catHashMap.put("electronic dance", 21);
		catHashMap.put("r b", 24);
		catHashMap.put("rock", 20);
		catHashMap.put("rap hip hop", 22);
		catHashMap.put("pop ballad", 19);
		
		catHashMap.put("nhac nhat ban", 26);
		catHashMap.put("pop dance", 33);
		catHashMap.put("blues jazz", 31);
		catHashMap.put("electronic dance", 29);
		catHashMap.put("r b", 32);
		catHashMap.put("rock", 28);
		catHashMap.put("rap hip hop", 30);
		catHashMap.put("pop ballad", 27);
		
		catHashMap.put("nhac hoa", 46);
		catHashMap.put("malaysia", 50);
		catHashMap.put("hong kong", 49);
		catHashMap.put("singapore", 51);
		catHashMap.put("dai loan", 47);
		catHashMap.put("trung quoc", 48);
		
		
		catHashMap.put("nhac phim", 52);
		catHashMap.put("nhac viet nam", 56);
		catHashMap.put("trailer", 62);
		catHashMap.put("anime", 59);
		catHashMap.put("nhac hong kong", 61);
		catHashMap.put("hoat hinh", 58);
		catHashMap.put("nhac dai loan", 60);
		catHashMap.put("nhac nhat", 55);
		catHashMap.put("nhac trung quoc", 53);
		catHashMap.put("nhac au my", 57);
		catHashMap.put("nhac han", 54);
		
		
		
		catHashMap.put("nhac viet nam", 1);
		catHashMap.put("cai luong", 14);
		catHashMap.put("nhac dance", 15);
		catHashMap.put("nhac thieu nhi", 7);
		catHashMap.put("nhac che", 8);
		catHashMap.put("nhac cach mang", 5);
		catHashMap.put("nhac vang", 3);
		catHashMap.put("nhac que huong", 13);
		catHashMap.put("nhac trinh", 4);
		catHashMap.put("nhac tru tinh", 12);
		catHashMap.put("r b", 16);
		
		catHashMap.put("acoustic", 11);
		catHashMap.put("rock viet", 9);
		catHashMap.put("rap viet", 10);
		catHashMap.put("nhac tre", 2);
		
		
	}
	
	public static void  intCatMapVietNam()
	{
		catHashMap = new HashMap<String, Integer>();
		catHashMap.put("nhac viet nam", 1);
		catHashMap.put("cai luong", 14);
		catHashMap.put("nhac dance", 15);
		catHashMap.put("thieu nhi", 7);
		catHashMap.put("nhac che", 8);
		catHashMap.put("cach mang", 5);
		catHashMap.put("nhac vang", 3);
		catHashMap.put("nhac que huong", 13);
		catHashMap.put("nhac trinh", 4);
		catHashMap.put("tru tinh", 12);
		catHashMap.put("r b", 16);
		
		catHashMap.put("acoustic", 11);
		catHashMap.put("rock viet", 9);
		catHashMap.put("rap viet", 10);
		catHashMap.put("nhac tre", 2);
		
		
	}
	
	public static void  intCatMapAumi()
	{   
		catHashMap = new HashMap<String, Integer>();
		catHashMap.put("au my", 34);
		catHashMap.put("au, my", 34);
		catHashMap.put("country", 65);
		catHashMap.put("pop", 35);
		catHashMap.put("new age world music", 41);
		catHashMap.put("rock", 36);
		catHashMap.put("electronic dance", 37);
		catHashMap.put("rap hip hop", 38);
		catHashMap.put("blues jazz", 39);
		catHashMap.put("r b soul", 40);
		catHashMap.put("folk", 42);
		catHashMap.put("trance house techno", 43);
		catHashMap.put("indie", 44);
		
		
	}
	
	public static void  intCatMapNhatban()
	{catHashMap = new HashMap<String, Integer>();
		catHashMap.put("nhac nhat", 26);
		catHashMap.put("pop dance", 33);
		catHashMap.put("blues jazz", 31);
		catHashMap.put("electronic dance", 29);
		catHashMap.put("r b", 32);
		catHashMap.put("rock", 28);
		catHashMap.put("rap hip hop", 30);
		catHashMap.put("pop ballad", 27);
		
		
	}
	
	
	public static void  intCatMapHanQuoc()
	{catHashMap = new HashMap<String, Integer>();
		catHashMap.put("han quoc", 17);
		catHashMap.put("trot", 18);
		catHashMap.put("blues jazz", 23);
		catHashMap.put("electronic dance", 21);
		catHashMap.put("r b", 24);
		catHashMap.put("rock", 20);
		catHashMap.put("rap hip hop", 22);
		catHashMap.put("pop ballad", 19);
		
		
	}
	
	public static void  intCatMapHoa()
	{catHashMap = new HashMap<String, Integer>();
		catHashMap.put("nhac hoa", 46);
		catHashMap.put("malaysia", 50);
		catHashMap.put("hong kong", 49);
		catHashMap.put("singapore", 51);
		catHashMap.put("dai loan", 47);
		catHashMap.put("trung quoc", 48);
		
	}
	
	
	public void collectionLink(String url,int singer_id,String m_singer,int parent_id,int language,int type_zing, int page) throws Exception {
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
		
		String xpath__tag= "//div[@class='content-main']/div";
		NodeList linkNodes = (NodeList) reader.read(xpath__tag, XPathConstants.NODESET);
		
		Thread.sleep(1000);
		
		int node_one_many = linkNodes.getLength();
		int i=1;
		ChuotTuiDAO chuotTuiDAO = new ChuotTuiDAO();
		Music music = null;
		MusicLike musicLike = null;
		MusicHit musicHit = null;
		MusicReport musicReport = null;
		MusicLyrics musicLyrics = null;
		String alias = "";		
		int musicId= 0;
		
		while (i <= node_one_many) {
			
			String link = (String) reader.read(xpath__tag+"["+ i + "]/h3[1]/a[1]/@href", XPathConstants.STRING);			
			String m_title = (String) reader.read(xpath__tag+"["+ i + "]/h3[1]/a[1]/text()", XPathConstants.STRING);
			String m_category = (String) reader.read(xpath__tag+"["+ i + "]/p[1]/a[3]/text()", XPathConstants.STRING);
			System.out.println("http://www.nhaccuatui.com"+link);
			System.out.println(m_title);
			chuotTuiDAO.openConnection();
			musicId = chuotTuiDAO.checkMusicExits(m_title.trim(), singer_id);

			// Get Song		
			if(musicId>0){i++;
			int lyricsId = chuotTuiDAO.checkLyric(musicId);
			 if(lyricsId==0)
			 {
				 this.extractMusic("http://www.nhaccuatui.com"+link);
				 if(!StringUtil.isEmpty(this.lyric_zing))
				 {
					 	musicLyrics = new MusicLyrics();
						musicLyrics.content = this.lyric_zing;
						musicLyrics.create_date = Calendar.getInstance().getTimeInMillis()/1000;
						musicLyrics.userId = 0;
						musicLyrics.username = "auto";
						musicLyrics.musicId = musicId;
						musicLyrics.id = lyricsId;
						chuotTuiDAO.updateMusicLyric(musicLyrics);
						
						
				 }
					
			 }
			 	continue;
			}
		    
			music = new Music();			
			music.singerId = singer_id;
			music.singer_name= m_singer.trim();
			music.artist_name = this.tacgia.trim();
			music.name = m_title;
			alias = this.getAlias(m_title);
			music.alias = alias;
			music.crow = 1;
			music.language = language;
			music.create_date = Calendar.getInstance().getTimeInMillis()/1000;
			music.update_date = Calendar.getInstance().getTimeInMillis()/1000;
			
			this.url_zing ="";
			this.lyric_zing = "";
			this.parent_cat_id= parent_id ;this.cat_id=0;
			
			m_category = UTF8Tool.coDau2KoDau(m_category);
			m_category = m_category.toLowerCase();
			
			if(catHashMap.get(m_category)==null) {i++;continue;}
			
			this.cat_id = catHashMap.get(m_category);
			
			if(parent_id==this.cat_id) this.cat_id = 0;
			
			this.extractMusic("http://www.nhaccuatui.com"+link);
			
			int tacgia_id  = 0;
			musicArtist = null;
			if(!StringUtil.isEmpty(this.tacgia))
			{
				tacgia_id = chuotTuiDAO.getAuthor(this.tacgia.trim());
				musicArtist = null;			
				if(tacgia_id ==0)
				{
					musicArtist = new MusicArtist();
					musicArtist.name = this.tacgia.trim();
					musicArtist.type = 1;
					musicArtist.status = 1;								
					tacgia_id=chuotTuiDAO.saveSinger(musicArtist);	
					ArtistRanking artistRanking = new ArtistRanking();
					artistRanking.artistId = tacgia_id;
					artistRanking.value = 200;
					artistRanking.ranking = 0;
					chuotTuiDAO.saveArtistRanking(artistRanking);
				}
			}
			
			if(this.parent_cat_id==0){i++;continue;}
			
			music.artistId = tacgia_id;
			music.parentCategoryId =  this.parent_cat_id;
			music.categoryId = this.cat_id;
			music.url_nct = this.url_zing;
			music.userId = 0;
			music.username ="auto";
			music.source_nct = "http://www.nhaccuatui.com"+link;
			music.type_nct = type_zing;
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
				musicHit.singer_name= m_singer.trim();
				musicHit.artist_name = this.tacgia.trim();
				musicHit.artistId = tacgia_id;
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
//		String xpath_image = "//span[@class='singer-image']/img[1]/@src";
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
		
//		String image = (String) reader.read(xpath_image, XPathConstants.STRING);
		String name = (String) reader.read(xpath_name, XPathConstants.STRING);
		Node node_full_name = (Node) reader.read(xpath_full_name, XPathConstants.NODE);
		Node node_birthday = (Node) reader.read(xpath_full_birthday, XPathConstants.NODE);
		Node node_company = (Node) reader.read(xpath_full_company, XPathConstants.NODE);
		
	//	System.out.println(image);
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
		CrawlerBySingerMusicNCT crawlerMusicZing = new CrawlerBySingerMusicNCT();
		int types[] = {1,3,4,5,2};
		int parent_id = 1;	
		ChuotTuiDAO  chuotTuiDAO = new ChuotTuiDAO();
		List<MusicArtist> list = new ArrayList<MusicArtist>();
		for (int t : types) {
			int language = 0;		
			switch (t) {
				case 1:
					CrawlerBySingerMusicNCT.intCatMapVietNam();
					parent_id = 1;	
					list = chuotTuiDAO.getSingerByQG("Việt Nam", 1, 100000);
					break;
				case 2:
					CrawlerBySingerMusicNCT.intCatMapAumi();
					parent_id = 34;
					list = chuotTuiDAO.getSingerAuMi(1, 100000);
					language = 1;	
					break;
				case 3:
					CrawlerBySingerMusicNCT.intCatMapHanQuoc();
					list = chuotTuiDAO.getSingerHanQuoc(1, 100000);
					parent_id = 17;language = 1;	
					break;
				case 4:
					CrawlerBySingerMusicNCT.intCatMapNhatban();
					list = chuotTuiDAO.getSingerNhat(1, 100000);
					parent_id = 26;language = 1;	
					break;
				case 5:
					CrawlerBySingerMusicNCT.intCatMapHoa();
					list = chuotTuiDAO.getSingerHoa(1, 100000);
					parent_id = 46;language = 1;	
					break;
				default:
					CrawlerBySingerMusicNCT.intCatMapVietNam();
					list = chuotTuiDAO.getSingerByQG("Việt Nam", 1, 100000);
					parent_id = 1;	language = 0;	
					break;
			}
			
			
			try {
				for (MusicArtist musicArtist : list) {
					
					String m_singer =musicArtist.name; 
					Pattern pattern = Pattern.compile("\\s");
					Matcher matcher = pattern.matcher(m_singer.trim());
					String singer_alias = matcher.replaceAll("+");
					
					String url = "http://www.nhaccuatui.com/tim_kiem/bai_hat?q="+singer_alias+"&b=singer";
					int type_zing = 0;
							
					int i = 1;
					 System.out.println("m_singer="+m_singer);	
					while(i<=25)
					{		
						 System.out.println("Page="+i+m_singer);	
						 crawlerMusicZing.collectionLink(url+"&page="+i,musicArtist.id, m_singer,parent_id, language,type_zing, i);
					 	 i++;
					}
				}
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
}
