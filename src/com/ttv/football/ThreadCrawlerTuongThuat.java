package com.ttv.football;

public class 	ThreadCrawlerTuongThuat {
	   private String url;
	   private int match_id;
	   private int club_id_1;
	   private int club_id_2;
	   private int cup_id;
	   
	   ThreadCrawlerTuongThuat(String _url,int _match_id,int fbcup_id,int _club_id_1,int _club_id_2){
		   url = _url;
		   match_id = _match_id;
		   club_id_1 = _club_id_1;
		   club_id_2 = _club_id_2;
		   cup_id =fbcup_id;
	       System.out.println("thread url " +  url );
	   }
	   
	   public void run() {
	      System.out.println("Running " +  url );
	      try {
			LiveScoreCrawler.crawlerUrlTuongThuatAjax(url, match_id,cup_id,club_id_1,club_id_2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	     // System.out.println("Thread " +  url + " exiting.");
	   }
	   
	   public void start ()
	   {
	      run();
	   }
}
