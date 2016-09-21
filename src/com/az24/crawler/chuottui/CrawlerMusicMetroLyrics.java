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

public class CrawlerMusicMetroLyrics {

	
	private String lyric = "";
	private int parent_cat_id=34;
	private int cat_id=0;

	public void collectionLink(String url, int cate_parent_id, int category_id,
			int language, int type_zing, int page) throws Exception {
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);

		String xpath__tag = "//div[@id='starting']/ul[1]/li";
		NodeList linkNodes = (NodeList) reader.read(xpath__tag,
				XPathConstants.NODESET);

		Thread.sleep(1000);

		int node_one_many = linkNodes.getLength();
		int i =0;
		ChuotTuiDAO chuotTuiDAO = new ChuotTuiDAO();
		Music music = null;
		MusicLike musicLike = null;
		MusicHit musicHit = null;
		MusicReport musicReport = null;
		MusicLyrics musicLyrics = null;
		
		String alias = "";
		int singer_id = 0;
		int musicId = 0;
		MusicArtist musicArtist = null;
		i = 1;
		while (i <= node_one_many) {	
			// get link artist			
			String link = (String) reader.read(xpath__tag + "[" + i
					+ "]/h3[1]/a[1]/@href", XPathConstants.STRING);
			
			String m_singer = (String) reader.read(xpath__tag + "[" + i
					+ "]/h3[1]/a[1]/text()", XPathConstants.STRING);
			
			link = "http://www.metrolyrics.com"+link;
			
			System.out.println(m_singer);
			if(StringUtil.isEmpty(m_singer)){i++;continue;};
			m_singer = m_singer.substring(0, m_singer.length()-6);
			
			chuotTuiDAO.openConnection();
			singer_id = chuotTuiDAO.getSinger(m_singer.trim().toLowerCase());
			if (singer_id == 0&&!StringUtil.isEmpty(m_singer)) {
				musicArtist = new MusicArtist();
				musicArtist.name = m_singer;
				musicArtist.type = 0;
				singer_id = chuotTuiDAO.saveSinger(musicArtist);
				
				ArtistRanking artistRanking = new ArtistRanking();
				artistRanking.artistId = singer_id;
				artistRanking.value = 200;
				artistRanking.ranking = 0;
				chuotTuiDAO.saveArtistRanking(artistRanking);
				
			}
			chuotTuiDAO.closeConnection();
			// process link artist
			client = new HttpClientImpl();
			res = client.fetch(link);
			System.out.println(link);
			HttpClientUtil.printResponseHeaders(res);
			html = HttpClientUtil.getResponseBody(res);
			XPathReader readerLyrics = CrawlerUtil.createXPathReaderByData(html);
			if(readerLyrics==null){i++;continue;}
			xpath__tag = "html/body[1]/div[@id='bg-main']/div[@id='main']/div[@id='cols-wrap']/div[@id='col-b']/div[@id='content-wrap']/table[@id='lyrics-list']/TBODY[1]/tr";
			NodeList linkNodesLyrics = (NodeList) readerLyrics.read(xpath__tag,
					XPathConstants.NODESET);
			int node_lyrics = linkNodesLyrics.getLength();
			Thread.sleep(1000);
			int j = 2;
			
			while(j<=node_lyrics)
			{
				chuotTuiDAO.openConnection();
				String link_lyric = (String) readerLyrics.read(xpath__tag + "[" + j
						+ "]/td[1]/a[1]/@href", XPathConstants.STRING);
				link_lyric = "http://www.metrolyrics.com"+link_lyric;
				System.out.println(link_lyric+"-->"+m_singer);
				String song_name = (String) readerLyrics.read(xpath__tag + "[" + j
						+ "]/td[1]/a[1]/text()", XPathConstants.STRING);
				System.out.println("--------------->"+song_name);
				song_name = song_name.substring(0,song_name.length()-6);
				musicId = chuotTuiDAO.checkMusicExits(song_name.trim(), singer_id);
				if(musicId>0)
				{
					if(chuotTuiDAO.checkLyric(musicId)==0)
					{
						// update lyric
						extractMusic(link_lyric);
						musicLyrics = new MusicLyrics();
						musicLyrics.content = this.lyric;
						musicLyrics.create_date = Calendar.getInstance().getTimeInMillis()/1000;
						musicLyrics.userId = 0;
						musicLyrics.username = "auto";
						musicLyrics.musicId = musicId;
						
						chuotTuiDAO.updateMusicLyric(musicLyrics);
						
						
					}
				}else
				{
						// Create Song
						extractMusic(link_lyric);
						music = new Music();			
						music.singerId = singer_id;
						music.singer_name = m_singer.trim();
						music.name = song_name;
						alias = this.getAlias(song_name);
						music.alias = alias;
						music.crow = 1;
						music.language = language;
						music.create_date = Calendar.getInstance().getTimeInMillis()/1000;
						music.update_date = Calendar.getInstance().getTimeInMillis()/1000;
						music.artistId = 0;
						music.parentCategoryId =  this.parent_cat_id;
						music.categoryId = this.cat_id;
						music.url_nct = "";
						music.userId = 0;
						music.username ="auto";
						music.source_nct = "";
						music.type_nct = type_zing;
						music.status =1;
						musicId = chuotTuiDAO.saveMusic(music);
						
						if(musicId>0)
						{
							musicLyrics = new MusicLyrics();
							musicLyrics.content = this.lyric;
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
						
				}
				j++;
				chuotTuiDAO.closeConnection();
			}
			i++;
		}

	}

	public String getAlias(String name) {
		Pattern pattern = Pattern.compile("\\W+");
		Pattern pattern2 = Pattern.compile("-$");
		name = UTF8Tool.coDau2KoDau(name).trim();
		Matcher matcher = pattern.matcher(name);
		String url_rewrite = matcher.replaceAll("-");
		matcher = pattern2.matcher(url_rewrite);
		url_rewrite = matcher.replaceAll("");
		return url_rewrite;
	}

	public void extractMusic(String url)  {
		String xpath_lyric = "//div[@id='lyrics-body']";
		try {
			DomWriter writer = new DomWriter();
			HttpClientImpl client = new HttpClientImpl();
			HttpResponse res = client.fetch(url);
			HttpClientUtil.printResponseHeaders(res);
			String html="";
			html = HttpClientUtil.getResponseBody(res);
			
			XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
			Node nodeLyrics=null;
			try{
				 nodeLyrics = (Node) reader.read(xpath_lyric , XPathConstants.NODE);
				 deleteVisitor.traverse(nodeLyrics);
					normalVisitor.traverse(nodeLyrics);
				String lyric = writer.toXMLString(nodeLyrics);
				if (lyric != null
						&& lyric.length() > "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
								.length()) {
					lyric = lyric
							.substring("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
									.length());
				}
				
				this.lyric = lyric.replaceAll("<img src=\"/images/n2/fbshare.gif\"/>", "");	
				//System.out.println(this.lyric );
				Thread.sleep(100);
			}catch (Exception e) {
				System.out.println(html);
			}
			
			
		} catch (Exception e) {			
			e.printStackTrace();
		}
		
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
			else if (node.getNodeName().equalsIgnoreCase("iframe"))
				return true;
			List<hdc.crawler.Node> nodesDel = new ArrayList<hdc.crawler.Node>();
			hdc.crawler.Node node2 = new hdc.crawler.Node();
			node2.name = "span";
			node2.attribue = "lyric-author";
			nodesDel.add(node2);
			if (nodesDel != null) {
				int i = 0;
				while (i < nodesDel.size()) {
					if (nodesDel.get(i).name.equalsIgnoreCase(node
							.getNodeName())) {
						int j = 0;
						while (j < node.getAttributes().getLength()) {
							if (node.getAttributes().item(j).getTextContent()
									.equalsIgnoreCase(nodesDel.get(i).attribue))
								return true;

							j++;
						}
					}
					i++;
				}
			}
			return false;
		}
	};
	
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
		CrawlerMusicMetroLyrics crawlerMusicZing = new CrawlerMusicMetroLyrics();
		try {
			int type_zing = 0;
			int language = 1;
			int cat_id = 0;
			int parent_cat_id = 34;
			int i = 1;
			String kt[] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","P","Q","R","S","T","V","W","Z","X","U","Y"};
			for (String cc : kt) {
				i = 1;
				while(i<=25)
				{
					System.out.println("Page="+i);
					try{
						crawlerMusicZing.collectionLink(
								"http://www.metrolyrics.com/artists-"+cc+"-"+i+".html", parent_cat_id,
								cat_id, language, type_zing, 1);
							
					}catch (Exception e) {						
					}
					
					i++;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
