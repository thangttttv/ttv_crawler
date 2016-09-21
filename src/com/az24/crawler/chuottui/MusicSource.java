package com.az24.crawler.chuottui;

import java.util.ArrayList;
import java.util.List;

public class MusicSource {
	int cat_id;
	int parent_cat_id;
	String url;
	String url_1;
	int language;
	int page = 5;
	
	public static List<MusicSource> muList = null; 
	
	public static void initSourceLinkZing()
	{
		muList = new ArrayList<MusicSource>();
		// Nhac Viet Nam - Nhac Tre
		MusicSource musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 2;
		musicSource.language = 0;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/Nhac-Tre/IWZ9Z088.html";
		muList.add(musicSource);
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 10;
		musicSource.language = 0;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/Rap-Viet/IWZ9Z089.html";
		muList.add(musicSource);
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 9;
		musicSource.language = 0;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/Rock-Viet/IWZ9Z08A.html";
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 12;
		musicSource.language = 0;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/Nhac-Tru-Tinh/IWZ9Z08B.html";
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 5;
		musicSource.language = 0;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/Nhac-Cach-Mang/IWZ9Z08C.html";
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 13;
		musicSource.language = 0;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/Nhac-Que-Huong/IWZ9Z08D.html";
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 4;
		musicSource.language = 0;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/Nhac-Trinh/IWZ9Z08E.html";
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 7;
		musicSource.language = 0;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/Nhac-Thieu-Nhi/IWZ9Z08F.html";
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 15;
		musicSource.language = 0;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/Nhac-Dance/IWZ9Z0CW.html";
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 14;
		musicSource.language = 0;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/Cai-Luong/IWZ9Z0C6.html";
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 16;
		musicSource.language = 0;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/R-B/IWZ9Z0C8.html";
		muList.add(musicSource);
		
		// Nhac Au Mi
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 34;
		musicSource.cat_id = 65;
		musicSource.language = 1;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/Country/IWZ9Z096.html";
		muList.add(musicSource);
		
		// Nhac Au Mi
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 34;
		musicSource.cat_id = 35;
		musicSource.language = 1;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/Pop/IWZ9Z097.html";
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 34;
		musicSource.cat_id = 41;
		musicSource.language = 1;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/New-Age-World-Music/IWZ9Z098.html";
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 34;
		musicSource.cat_id = 36;
		musicSource.language = 1;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/Rock/IWZ9Z099.html";
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 34;
		musicSource.cat_id = 37;
		musicSource.language = 1;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/Electronic-Dance/IWZ9Z09A.html";
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 34;
		musicSource.cat_id = 38;
		musicSource.language = 1;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/Rap-Hip-Hop/IWZ9Z09B.html";
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 34;
		musicSource.cat_id = 39;
		musicSource.language = 1;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/Blues-Jazz/IWZ9Z09C.html";
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 34;
		musicSource.cat_id = 40;
		musicSource.language = 1;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/R-B-Soul/IWZ9Z09D.html";
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 34;
		musicSource.cat_id = 43;
		musicSource.language = 1;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/Trance-House-Techno/IWZ9Z0C7.html";
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 34;
		musicSource.cat_id = 44;
		musicSource.language = 1;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/Indie/IWZ9Z0CA.html";
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 34;
		musicSource.cat_id = 42;
		musicSource.language = 1;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/Folk/IWZ9Z09E.html";
		muList.add(musicSource);
		
		// Han Quoc
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 17;
		musicSource.cat_id = 19;
		musicSource.language = 1;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/Pop-Ballad/IWZ9Z09W.html";
		muList.add(musicSource);
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 17;
		musicSource.cat_id = 22;
		musicSource.language = 1;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/Rap-Hip-Hop/IWZ9Z09I.html";
		muList.add(musicSource);
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 17;
		musicSource.cat_id = 21;
		musicSource.language = 1;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/Electronic-Dance/IWZ9Z09O.html";
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 17;
		musicSource.cat_id = 23;
		musicSource.language = 1;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/R-B/IWZ9Z09U.html";
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 17;
		musicSource.cat_id = 20;
		musicSource.language = 1;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/Rock/IWZ9Z09Z.html";
		muList.add(musicSource);
		
		// Nhac Nhat
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 26;
		musicSource.cat_id = 30;
		musicSource.language = 1;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/Rap-Hip-Hop/IWZ9Z0AU.html";
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 26;
		musicSource.cat_id = 27;
		musicSource.language = 1;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/Pop-Ballad/IWZ9Z0AZ.htm";
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 26;
		musicSource.cat_id = 33;
		musicSource.language = 1;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/Pop-Dance/IWZ9Z0A6.html";
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 26;
		musicSource.cat_id = 28;
		musicSource.language = 1;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/Rock/IWZ9Z0A8.html";
		muList.add(musicSource);
		
		// Nhac Hoa
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 46;
		musicSource.cat_id = 47;
		musicSource.language = 1;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/Dai-Loan/IWZ9Z09F.html";
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 46;
		musicSource.cat_id = 48;
		musicSource.language = 1;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/Trung-Quoc/IWZ9Z0A0.html";
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 46;
		musicSource.cat_id = 49;
		musicSource.language = 1;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/Hong-Kong/IWZ9Z0AI.html";
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 46;
		musicSource.cat_id = 51;
		musicSource.language = 1;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/Singapore/IWZ9Z0AW.html";
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 46;
		musicSource.cat_id = 50;
		musicSource.language = 1;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/Malaysia/IWZ9Z0AO.html";
		muList.add(musicSource);
		
		// Nhac Phim
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 52;
		musicSource.cat_id = 53;
		musicSource.language = 1;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/Nhac-Trung-Quoc/IWZ9Z0B9.html";
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 52;
		musicSource.cat_id = 56;
		musicSource.language = 1;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/Nhac-Viet-Nam/IWZ9Z0BA.html";
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 52;
		musicSource.cat_id = 54;
		musicSource.language = 1;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/Nhac-Han/IWZ9Z0BB.html";
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 52;
		musicSource.cat_id = 58;
		musicSource.language = 1;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/Hoat-Hinh/IWZ9Z0BC.html";
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 52;
		musicSource.cat_id = 57;
		musicSource.language = 1;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/Nhac-Au-My/IWZ9Z0BD.html";
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 52;
		musicSource.cat_id = 59;
		musicSource.language = 1;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/Anime/IWZ9Z0BE.html";
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 52;
		musicSource.cat_id = 60;
		musicSource.language = 1;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/Nhac-Dai-Loan/IWZ9Z0BF.html";
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 52;
		musicSource.cat_id = 61;
		musicSource.language = 1;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/Nhac-Hong-Kong/IWZ9Z0C0.html";
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 52;
		musicSource.cat_id = 55;
		musicSource.language = 1;
		musicSource.url = "http://mp3.zing.vn/the-loai-bai-hat/Nhac-Nhat/IWZ9Z0CI.html";
		muList.add(musicSource);
	}
	
	public static void initSourceLinkNCT()
	{
		muList = new ArrayList<MusicSource>();
		// Nhac Viet Nam - Nhac Tre
		MusicSource musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 2;
		musicSource.language = 0;
		musicSource.url = "http://www.nhaccuatui.com/tim_kiem/bai_hat?g=nhactre";
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 10;
		musicSource.language = 0;
		musicSource.url = "http://www.nhaccuatui.com/tim_kiem/bai_hat?g=hiphop";
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 4;
		musicSource.language = 0;
		musicSource.url = "http://www.nhaccuatui.com/tim_kiem/bai_hat?g=trinh";
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 5;
		musicSource.language = 0;
		musicSource.url = "http://www.nhaccuatui.com/tim_kiem/bai_hat?g=cachmang";
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 12;
		musicSource.language = 0;
		musicSource.url = "http://www.nhaccuatui.com/tim_kiem/bai_hat?g=trutinh";
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 9;
		musicSource.language = 0;
		musicSource.url = "http://www.nhaccuatui.com/tim_kiem/bai_hat?g=rock-viet";
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 7;
		musicSource.language = 0;
		musicSource.url = "http://www.nhaccuatui.com/tim_kiem/bai_hat?g=thieunhi";
		muList.add(musicSource);
		
		// Nhac Au Mi
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 34;
		musicSource.cat_id = 0;
		musicSource.language = 1;
		musicSource.url = "http://www.nhaccuatui.com/tim_kiem/bai_hat?g=anh";
		muList.add(musicSource);
		// Nhac Han
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 17;
		musicSource.cat_id = 0;
		musicSource.language = 1;
		musicSource.url = "http://www.nhaccuatui.com/tim_kiem/bai_hat?g=han";
		muList.add(musicSource);
		
		// Nhac Hoa
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 46;
		musicSource.cat_id = 0;
		musicSource.language = 1;
		musicSource.url = "http://www.nhaccuatui.com/tim_kiem/bai_hat?g=hoa";
		muList.add(musicSource);
		
		// Nhac Nhat
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 26;
		musicSource.cat_id = 0;
		musicSource.language = 1;
		musicSource.url = "http://www.nhaccuatui.com/tim_kiem/bai_hat?g=nhat";
		muList.add(musicSource);
	}
	
	
	public static void initSourceLinkIMuzik()
	{
		muList = new ArrayList<MusicSource>();
		MusicSource musicSource = new MusicSource();
		//Nhac cho tong hop
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 69;
		musicSource.language = 0;
		musicSource.url = "http://imuzik.com.vn/Nhac-cho_";
		musicSource.url_1 = ".html";
		musicSource.page=47090;
		muList.add(musicSource);
		
		// Nhac Cach Manh
		
		/*musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 5;
		musicSource.language = 0;
		musicSource.url = "http://imuzik.com.vn/Nhac-cho_";
		musicSource.url_1 = "_72_2.html";
		musicSource.page=12;
		muList.add(musicSource);
		// Dan ca
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 13;
		musicSource.language = 0;
		musicSource.url = "http://imuzik.com.vn/Nhac-cho_";
		musicSource.url_1 = "_60_2.html";
		musicSource.page=3;
		muList.add(musicSource);
		// Rock Dance
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 34;
		musicSource.cat_id = 36;
		musicSource.language = 1;
		musicSource.url = "http://imuzik.com.vn/Nhac-cho_";
		musicSource.url_1 = "_56_2.html";
		musicSource.page=3;
		muList.add(musicSource);
		// Rock Viet
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 9;
		musicSource.language = 0;
		musicSource.url = "http://imuzik.com.vn/Nhac-cho_";
		musicSource.url = "_54_2.html";
		musicSource.page=2;*/
		//muList.add(musicSource);
		
	}
	
	public static void initSourceLinkFunring()
	{
		muList = new ArrayList<MusicSource>();
		MusicSource musicSource = new MusicSource();
		
		// Nhac Thieu Nhi
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 7;
		musicSource.language = 0;
		musicSource.url = "http://www.funring.vn/web/media/vn/funring/index.jsp?cid=40055&p=";
		musicSource.page=59;
		muList.add(musicSource);
		
		// Nhac Tre
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 2;
		musicSource.language = 0;
		musicSource.url = "http://www.funring.vn/web/media/vn/funring/index.jsp?cid=40056&p=";
		musicSource.page=9798;
		muList.add(musicSource);
		// Rock 
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 36;
		musicSource.language = 0;
		musicSource.url = "http://www.funring.vn/web/media/vn/funring/index.jsp?cid=40057&p=";
		musicSource.page=9;
		muList.add(musicSource);
		// Dance
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 15;
		musicSource.language = 0;
		musicSource.url = "http://www.funring.vn/web/media/vn/funring/index.jsp?cid=40058&p=";
		musicSource.page=1;
		muList.add(musicSource);
		// Nhac Remix -> Nhac Tre
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 2;
		musicSource.language = 0;
		musicSource.url = "http://www.funring.vn/web/media/vn/funring/index.jsp?cid=40059&p=";
		musicSource.page=43;
		muList.add(musicSource);
		
		// Nhac Tru Tinh 
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 12;
		musicSource.language = 0;
		musicSource.url = "http://www.funring.vn/web/media/vn/funring/index.jsp?cid=40061&p=";
		musicSource.page=1333;
		muList.add(musicSource);
		
		// Nhac Trinh
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 4;
		musicSource.language = 0;
		musicSource.url = "http://www.funring.vn/web/media/vn/funring/index.jsp?cid=40062&p=";
		musicSource.page=17;
		muList.add(musicSource);
		
		// Nhac cach mang
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 5;
		musicSource.language = 0;
		musicSource.url = "http://www.funring.vn/web/media/vn/funring/index.jsp?cid=40063&p=";
		musicSource.page=58;
		muList.add(musicSource);
		
		// Nhac tien chien ->Nhac cach mang
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 5;
		musicSource.language = 0;
		musicSource.url = "http://www.funring.vn/web/media/vn/funring/index.jsp?cid=40064&p=";
		musicSource.page=6;
		muList.add(musicSource);
		
		// Nhac hai ngoai -> Nhac Vang
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 3;
		musicSource.language = 0;
		musicSource.url = "http://www.funring.vn/web/media/vn/funring/index.jsp?cid=40065&p=";
		musicSource.page=66;
		muList.add(musicSource);
		

		// Nhac cai luong
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 14;
		musicSource.language = 0;
		musicSource.url = "http://www.funring.vn/web/media/vn/funring/index.jsp?cid=40066&p=";
		musicSource.page=20;
		muList.add(musicSource);

		// Nhac cai luong
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 13;
		musicSource.language = 0;
		musicSource.url = "http://www.funring.vn/web/media/vn/funring/index.jsp?cid=40067&p=";
		musicSource.page=30;
		muList.add(musicSource);
		
		// Nhac phim -> Nhac tre
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 2;
		musicSource.language = 0;
		musicSource.url = "http://www.funring.vn/web/media/vn/funring/index.jsp?cid=50051&p=";
		musicSource.page=18;
		muList.add(musicSource);
		
	}
	
	
	public static void initSourceLinkRingtunes()
	{
		muList = new ArrayList<MusicSource>();
		// Nhac Tre
		MusicSource musicSource = new MusicSource();
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 2;
		musicSource.language = 0;
		musicSource.url = "http://ringtunes.vinaphone.com.vn/category_fra.jsp?catID=5002685&page=";
		musicSource.page=1422;
		muList.add(musicSource);
		// Nhac Tre
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 2;
		musicSource.language = 0;
		musicSource.url = "http://ringtunes.vinaphone.com.vn/category_fra.jsp?catID=5002683&page=";
		musicSource.page=2944;
		muList.add(musicSource);
		// Trinh 
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 4;
		musicSource.language = 0;
		musicSource.url = "http://ringtunes.vinaphone.com.vn/category_fra.jsp?catID=5125862&page=";
		musicSource.page=1;
		muList.add(musicSource);
		// Thieu Nhi
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 7;
		musicSource.language = 0;
		musicSource.url = "http://ringtunes.vinaphone.com.vn/category_fra.jsp?catID=5125860&page=";
		musicSource.page=31;
		muList.add(musicSource);
		
		// QT
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 34;
		musicSource.cat_id = 35;
		musicSource.language = 1;
		musicSource.url = "http://ringtunes.vinaphone.com.vn/category_fra.jsp?catID=5002682&page=";
		musicSource.page=406;
		muList.add(musicSource);
		
		// Nhac Cach Manh
		musicSource = new MusicSource();		
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 5;
		musicSource.language = 0;
		musicSource.url = "http://ringtunes.vinaphone.com.vn/category_fra.jsp?catID=5124753&page=";
		musicSource.page=3;
		muList.add(musicSource);
		
		// Nhac Tru Tinh
		musicSource = new MusicSource();		
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 12;
		musicSource.language = 0;
		musicSource.url = "http://ringtunes.vinaphone.com.vn/category_fra.jsp?catID=5002684&page=";
		musicSource.page=399;
		muList.add(musicSource);
		
		// Nhac Tru Tinh
		musicSource = new MusicSource();		
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 12;
		musicSource.language = 0;
		musicSource.url = "http://ringtunes.vinaphone.com.vn/category_fra.jsp?catID=5002684&page=";
		musicSource.page=399;
		muList.add(musicSource);
		
		// Nhac Bai ca xuan
		musicSource = new MusicSource();		
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 2;
		musicSource.language = 0;
		musicSource.url = "http://ringtunes.vinaphone.com.vn/category_fra.jsp?catID=5128609&page=";
		musicSource.page=23;
		muList.add(musicSource);
		
		// Nhac Giang xinh va nam moi
		musicSource = new MusicSource();		
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 2;
		musicSource.language = 0;
		musicSource.url = "http://ringtunes.vinaphone.com.vn/category_fra.jsp?catID=5102657&page=";
		musicSource.page=67;
		muList.add(musicSource);
		
		// Nhac Giang xinh va nam moi
		musicSource = new MusicSource();		
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 2;
		musicSource.language = 0;
		musicSource.url = "http://ringtunes.vinaphone.com.vn/category_fra.jsp?catID=5102657&page=";
		musicSource.page=67;
		muList.add(musicSource);
		
		
	}
	
	
	public static void initSourceLinkHappyring()
	{
		muList = new ArrayList<MusicSource>();
		MusicSource musicSource = new MusicSource();
		//Nhac cho tong hop
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 69;
		musicSource.language = 0;
		musicSource.url = "http://happyring.vietnamobile.com.vn/media/lookupMediaList.action?index=0&d-16544-p=";
		musicSource.url_1 = "&action=&feature=&pk=62";
		musicSource.page=253;
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 69;
		musicSource.language = 0;
		musicSource.url = "http://happyring.vietnamobile.com.vn/media/lookupMediaList.action?index=0&d-16544-p=";
		musicSource.url_1 = "&action=&feature=&pk=63";
		musicSource.page=130;
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 69;
		musicSource.language = 0;
		musicSource.url = "http://happyring.vietnamobile.com.vn/media/lookupMediaList.action?index=0&d-16544-p=";
		musicSource.url_1 = "0&action=&feature=&pk=61";
		musicSource.page=36;
		muList.add(musicSource);
		
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 69;
		musicSource.language = 0;
		musicSource.url = "http://happyring.vietnamobile.com.vn/media/lookupMediaList.action?index=0&d-16544-p=";
		musicSource.url_1 = "&action=&feature=&pk=55";
		musicSource.page=13;
		muList.add(musicSource);
		
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 69;
		musicSource.language = 0;
		musicSource.url = "http://happyring.vietnamobile.com.vn/media/lookupMediaList.action?index=0&d-16544-p=";
		musicSource.url_1 = "&action=&feature=&pk=850";
		musicSource.page=30;
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 69;
		musicSource.language = 0;
		musicSource.url = "http://happyring.vietnamobile.com.vn/media/lookupMediaList.action?index=0&d-16544-p=";
		musicSource.url_1 = "&action=&feature=&pk=850";
		musicSource.page=30;
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 17;
		musicSource.cat_id = 70;
		musicSource.language = 1;
		musicSource.url = "http://happyring.vietnamobile.com.vn/media/lookupMediaList.action?index=0&d-16544-p=";
		musicSource.url_1 = "&action=&feature=&pk=1006";
		musicSource.page=443;
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 34;
		musicSource.cat_id = 71;
		musicSource.language = 1;
		musicSource.url = "http://happyring.vietnamobile.com.vn/media/lookupMediaList.action?index=0&d-16544-p=";
		musicSource.url_1 = "&action=&feature=&pk=52";
		musicSource.page=791;
		muList.add(musicSource);
		
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 69;
		musicSource.language = 0;
		musicSource.url = "http://happyring.vietnamobile.com.vn/media/lookupMediaList.action?index=0&d-16544-p=";
		musicSource.url_1 = "&action=&feature=&pk=60";
		musicSource.page=79;
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 69;
		musicSource.language = 0;
		musicSource.url = "http://happyring.vietnamobile.com.vn/media/lookupMediaList.action?index=0&d-16544-p=";
		musicSource.url_1 = "&action=&feature=&pk=104";
		musicSource.page=1532;
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 69;
		musicSource.language = 0;
		musicSource.url = "http://happyring.vietnamobile.com.vn/media/lookupMediaList.action?index=0&d-16544-p=";
		musicSource.url_1 = "&action=&feature=&pk=903";
		musicSource.page=19;
		muList.add(musicSource);
		
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 69;
		musicSource.language = 0;
		musicSource.url = "http://happyring.vietnamobile.com.vn/media/lookupMediaList.action?index=0&d-16544-p=";
		musicSource.url_1 = "&action=&feature=&pk=56";
		musicSource.page=32;
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 69;
		musicSource.language = 0;
		musicSource.url = "http://happyring.vietnamobile.com.vn/media/lookupMediaList.action?index=0&d-16544-p=";
		musicSource.url_1 = "&action=&feature=&pk=51";
		musicSource.page=15391;
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 1;
		musicSource.cat_id = 69;
		musicSource.language = 0;
		musicSource.url = "http://happyring.vietnamobile.com.vn/media/lookupMediaList.action?index=0&d-16544-p=";
		musicSource.url_1 = "&action=&feature=&pk=54";
		musicSource.page=2334;
		muList.add(musicSource);
		
		musicSource = new MusicSource();
		musicSource.parent_cat_id = 34;
		musicSource.cat_id = 71;
		musicSource.language = 1;
		musicSource.url = "http://happyring.vietnamobile.com.vn/media/lookupMediaList.action?index=0&d-16544-p=3";
		musicSource.url_1 = "&action=&feature=&pk=1000";
		musicSource.page=3;
		muList.add(musicSource);
	}
	
}
