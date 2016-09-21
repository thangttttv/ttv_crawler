package com.ttv.football;

class ThreadCrawlerTyle extends Thread {
	   private String url;
	   private int match_id;
	   
	   ThreadCrawlerTyle(String _url,int _match_id){
		   url = _url;
		   match_id = _match_id;
	       System.out.println("thread url " +  url );
	   }
	   
	   public void run() {
	      System.out.println("Running " +  url );
	      try {
			LiveScoreCrawler.crawlerTyle(url, match_id);
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