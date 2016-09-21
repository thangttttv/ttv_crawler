package com.az24.crawler.model;

import java.util.Comparator;

public class FBMatchComparator  implements Comparator<FBMatch> {
    @Override
    public int compare(FBMatch o1, FBMatch o2) {
    	int kq = 0;
    	if(o1.match_rate > o2.match_rate) kq = -1;
    	else if(o1.match_rate <  o2.match_rate) kq = 1;
    	
    
    	
    //	System.out.println(kq);
    	return kq;
    }


}