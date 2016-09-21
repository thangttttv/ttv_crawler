package com.az24.crawler.model;

import java.util.Comparator;

public class EventFootballComparator implements Comparator<EventFootball> {
    @Override
    public int compare(EventFootball o1, EventFootball o2) {
    	int kq = 0;
    	if(o1.int_minute < o2.int_minute) kq = -1;
    	else if(o1.int_minute >  o2.int_minute) kq = 1;
    	
    
    	
    	System.out.println(kq);
    	return kq;
    }


}
