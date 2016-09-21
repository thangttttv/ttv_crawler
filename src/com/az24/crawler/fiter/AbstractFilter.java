package com.az24.crawler.fiter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractFilter {
	private static String REGEX = "";
    private static String INPUT = "";
    private static Pattern pattern;    
    private static Matcher matcher;
    
    public AbstractFilter(String regex,String url)
	{
		INPUT = url;
		REGEX = regex;
	}
    
    public static boolean find(String regex,String url)
    {
    	pattern = Pattern.compile(regex);
	    matcher = pattern.matcher(url);	 
	   // System.out.println(url);
	   // System.out.println(regex);
	    boolean kq = matcher.find();
	  //  System.out.println(kq);
	    return kq;
    }

	public static String getREGEX() {
		return REGEX;
	}

	public static void setREGEX(String regex) {
		REGEX = regex;
	}

	public static String getINPUT() {
		return INPUT;
	}

	public static void setINPUT(String input) {
		INPUT = input;
	}

	public static Pattern getPattern() {
		return pattern;
	}

	public static void setPattern(Pattern pattern) {
		AbstractFilter.pattern = pattern;
	}

	public static Matcher getMatcher() {
		return matcher;
	}

	public static void setMatcher(Matcher matcher) {
		AbstractFilter.matcher = matcher;
	}
    
	public static void main(String[] args) {
		AbstractFilter.find("", "http://vn.answers.yahoo.com/question/index;_ylt=Ak0OaK2himbeMbYbedBYZBhvdXRG;_ylv=3?qid=20110629213114AAoTVVw");
	}
}
