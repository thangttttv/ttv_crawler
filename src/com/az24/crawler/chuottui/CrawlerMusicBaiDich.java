package com.az24.crawler.chuottui;

import hdc.crawler.CrawlerUtil;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.util.html.NodeDeleteVisitor;
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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.az24.test.HttpClientUtil;
import com.az24.util.UTF8Tool;

public class CrawlerMusicBaiDich {

	private String url_zing = "";
	private String lyric = "";
	private String lyric_transalte = "";

	public void collectionLink(String url, int cate_parent_id, int category_id,
			int language, int type_zing, int page) throws Exception {
		HttpClientImpl client = new HttpClientImpl();
		HttpResponse res = client.fetch(url);
		HttpClientUtil.printResponseHeaders(res);
		String html = HttpClientUtil.getResponseBody(res);
		XPathReader reader = CrawlerUtil.createXPathReaderByData(html);

		String xpath__tag = "HTML/BODY[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/TABLE[1]/TBODY[1]/TR[1]/TD[1]/TABLE[1]/TBODY[1]/TR[3]/TD[1]/TABLE[1]/TBODY[1]/TR";
		NodeList linkNodes = (NodeList) reader.read(xpath__tag,
				XPathConstants.NODESET);

		Thread.sleep(1000);

		int node_one_many = linkNodes.getLength();
		int i = 2;
		ChuotTuiDAO chuotTuiDAO = new ChuotTuiDAO();
		Music music = null;
		MusicLike musicLike = null;
		MusicHit musicHit = null;
		MusicReport musicReport = null;
		MusicLyrics musicLyrics = null;
		MusicTranslate musicTranslate = null;
		MusicLikeTranslate musicLikeTranslate = null;
		String alias = "";
		int singer_id = 0;
		int musicId = 0;
		MusicArtist musicArtist = null;
		while (i < node_one_many) {
			if (i % 2 != 0) {
				i++;
				continue;
			}
			String link = (String) reader.read(xpath__tag + "[" + i
					+ "]/TD[2]/A[1]/@href", XPathConstants.STRING);
			String m_title = (String) reader.read(xpath__tag + "[" + i
					+ "]/TD[2]/A[1]/text()", XPathConstants.STRING);
			String m_singer = (String) reader.read(xpath__tag + "[" + i
					+ "]/TD[2]/FONT[1]/A[1]/text()", XPathConstants.STRING);
			System.out.println(m_title);
			chuotTuiDAO.openConnection();
			singer_id = chuotTuiDAO.getSinger(m_singer.trim());
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
			if(StringUtil.isEmpty(m_title)){i++; continue;}
			musicId = chuotTuiDAO.checkMusicExits(m_title.trim(), singer_id);
			if(musicId==0)
			{
					music = new Music();
					music.categoryId = category_id;
					music.parentCategoryId =  cate_parent_id;
					music.singerId = singer_id;
					music.singer_name = m_singer.trim();
					music.name = m_title;
					alias = this.getAlias(m_title);
					music.alias = alias;
					music.crow = 1;
					music.language = language;
					music.create_date = Calendar.getInstance().getTimeInMillis() / 1000;
					music.update_date = Calendar.getInstance().getTimeInMillis() / 1000;
					this.url_zing = "";
					this.lyric = "";
					this.lyric_transalte = "";
					this.extractMusic(link);
					if(StringUtil.isEmpty(this.url_zing)) {i++;continue;}
					music.url_zing = this.url_zing;
					music.userId = 0;
					music.username = "auto";
					music.source_zing = link;
					music.type_zing = type_zing;
					music.status =0;
		
					musicId = chuotTuiDAO.saveMusic(music);
		
					if (musicId > 0) {
						musicLyrics = new MusicLyrics();
						musicLyrics.content = this.lyric;
						musicLyrics.create_date = Calendar.getInstance()
								.getTimeInMillis() / 1000;
						musicLyrics.userId = 0;
						musicLyrics.musicId = musicId;
						musicLyrics.username = "auto";
		
						chuotTuiDAO.saveMusicLyric(musicLyrics);
		
						musicLike = new MusicLike();
						musicLike.like = 0;
						musicLike.musicId = musicId;
		
						chuotTuiDAO.saveMusicLike(musicLike);
		
						musicHit = new MusicHit();
						musicHit.artistId = 0;
						musicHit.musicId = musicId;
						musicHit.singerId = singer_id;
						musicHit.singer_name = m_singer.trim();
						musicHit.status = 0;
						musicHit.hit = 0;
		
						chuotTuiDAO.saveMusicHit(musicHit);
		
						musicReport = new MusicReport();
						musicReport.musicId = musicId;
						musicReport.report = 0;
						chuotTuiDAO.saveMusicReport(musicReport);
						
						musicTranslate = new MusicTranslate();
						musicTranslate.content = this.lyric_transalte;
						musicTranslate.create_date =  Calendar.getInstance()
						.getTimeInMillis() / 1000;
						musicTranslate.musicId = musicId;
						musicTranslate.status = 0;
						musicTranslate.userId=0;
						musicTranslate.username="auto";
						
						int id_translate = chuotTuiDAO.saveMusicTransalte(musicTranslate);
						
						musicLikeTranslate = new MusicLikeTranslate();
						musicLikeTranslate.like = 0;
						musicLikeTranslate.musicId = musicId;
						musicLikeTranslate.translateId = id_translate;
						chuotTuiDAO.saveMusicLikeStranslate(musicLikeTranslate);
					}
					chuotTuiDAO.closeConnection();
			}else
			{
				int id_translate =  chuotTuiDAO.checkMusicTranslate(musicId);
				if(id_translate == 0)
				{
					this.extractMusic(link);
					musicTranslate = new MusicTranslate();
					musicTranslate.content = this.lyric_transalte;
					musicTranslate.create_date =  Calendar.getInstance()
					.getTimeInMillis() / 1000;
					musicTranslate.musicId = musicId;
					musicTranslate.status = 0;
					musicTranslate.userId=0;
					musicTranslate.username="auto";
					
					id_translate = chuotTuiDAO.saveMusicTransalte(musicTranslate);
					musicLikeTranslate = new MusicLikeTranslate();
					musicLikeTranslate.like = 0;
					musicLikeTranslate.musicId = musicId;
					musicLikeTranslate.translateId = id_translate;
					chuotTuiDAO.saveMusicLikeStranslate(musicLikeTranslate);
				}
			}
			i++;
			Thread.sleep(1000);

			
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
		String xpath_url_file = "//DIV[@id='audio']/OBJECT[@id='obj1']/EMBED[1]/@src";
		String xpath_lyric = "HTML/BODY[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/TABLE[1]/TBODY[1]/TR[1]/TD[1]/TABLE[1]/TBODY[1]/TR[4]/TD[1]/TABLE[1]/TBODY[1]/TR[1]/TD[1]/DIV[1]";
		String xpath_lyric_dich = "HTML/BODY[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/TABLE[1]/TBODY[1]/TR[1]/TD[1]/TABLE[1]/TBODY[1]/TR[4]/TD[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/DIV[1]";

		
		try {
			HttpClientImpl client = new HttpClientImpl();
			HttpResponse res = client.fetch(url);
			HttpClientUtil.printResponseHeaders(res);
			String html="";
			html = HttpClientUtil.getResponseBody(res);
			XPathReader reader = CrawlerUtil.createXPathReaderByData(html);
			String url_file = (String) reader.read(xpath_url_file,XPathConstants.STRING);
			if(StringUtil.isEmpty(url_file)) return;
			//System.out.println(url_file);
			Pattern pattern = Pattern.compile("xmlURL=(.*)&songID");
			Matcher m = pattern.matcher(url_file);
			if(m.find())
			{
				url_file = m.group(1) ;
				this.url_zing =url_file;
			}
			//String url_files[] = url_file.split("&");		
			//System.out.println(url_file);
			//this.url_zing = url_file.split("=")[1];
			
			//System.out.println(this.url_zing);
			
			Node content = (Node) reader.read(xpath_lyric, XPathConstants.NODE);
			DomWriter writer = new DomWriter();
			deleteVisitor.traverse(content);
		//	System.out.println(writer.toXMLString(content));
			
			String lyric = writer.toXMLString(content);
			if (lyric != null
					&& lyric.length() > "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
							.length()) {
				lyric = lyric
						.substring("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
								.length());
			}
		//	System.out.println(lyric);
			this.lyric = lyric;

			content = (Node) reader.read(xpath_lyric_dich, XPathConstants.NODE);
			writer = new DomWriter();
			deleteVisitor.traverse(content);
			lyric = writer.toXMLString(content);
			if (lyric != null
					&& lyric.length() > "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
							.length()) {
				lyric = lyric
						.substring("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
								.length());
			}
			this.lyric_transalte = lyric;
			
			//System.out.println(writer.toXMLString(content));
			Thread.sleep(100);
		} catch (Exception e) {
			// TODO Auto-generated catch block
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

	public static void main(String[] args) {
		CrawlerMusicBaiDich crawlerMusicZing = new CrawlerMusicBaiDich();
		try {
			int type_zing = 0;
			int language = 1;
			int cat_id = 0;
			int parent_cat_id = 34;
			int i = 1;
			String kt[] = {"D","E","F","G","H","I","J","K","L","M","N","P","Q","R","S","T","V","W","Z","X","U","Y"};
			for (String cc : kt) {
				i = 0;
				while(i<=200)
				{
					crawlerMusicZing.collectionLink(
						"http://baidich.com/index.php?page="+cc+"-"+i, parent_cat_id,
						cat_id, language, type_zing, 1);
					System.out.println("Page="+i);
					i++;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
