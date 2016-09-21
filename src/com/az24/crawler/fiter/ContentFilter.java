package com.az24.crawler.fiter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.az24.crawler.config.JdbmXmlConfig;
import com.az24.crawler.config.KeywordFilterConfig;

public class ContentFilter {	
	
	public static String fileJdbmConfig="";	
	public static List<String> keywords=null;

	public static void initData() throws IOException {
		if(ContentFilter.keywords==null){
		JdbmXmlConfig.parseConfig(fileJdbmConfig);
		KeywordFilterConfig.getKeyword();
		keywords = KeywordFilterConfig.keywords;
		}
	}

	public static boolean filter(String content) {
		boolean result = false;		
		Pattern pattern = null;
		Matcher matcher;		
		int i = 0;
		if(keywords!=null)
			while (i<keywords.size()) {
				pattern = Pattern.compile(KeywordFilterConfig.keywords.get(i));
			    matcher = pattern.matcher(content);	 
			    if(matcher.find()) return true;
			    i++;
			}
		return result;
	}
	
	private static final String REGEX = "<a.*>.*</a>";
	

	public static String changeLink(String content) {
		List<String> strs= Arrays.asList(Pattern.compile("</a>").split(content));
		String result ="";
		String input="";
		Pattern p=null;
		Matcher m=null;
		for (String string : strs) {			
			input = string+"</a>";
			p = Pattern.compile(REGEX,Pattern.CASE_INSENSITIVE);
			m = p.matcher(input); // get a matcher object
			int count = 0;
			String keysearch = "";			
			String output = "";
			String a="";
			String replacementStr = "";
			if (m.find()) {
				count++;
				a = input.substring(m.start(), m.end());
				p = Pattern.compile(">.*<");
				Matcher m1 = p.matcher(a);
				if (m1.find()) {
					keysearch = a.substring(m1.start() + 1, m1.end() - 1);
					replacementStr = "<a href='http://az24.vn/raovat/search/index?r=search%2Findex&keyword="
							+ keysearch + "&category=0'>" + keysearch + "</a>";
					// Compile regular expression
					p = Pattern.compile(REGEX);
					// Replace all occurrences of pattern in input
					m = p.matcher(input);
					output = m.replaceAll(replacementStr);
				}
				result +=output;
			}else
			{
				result +=string;
			}
	
			//System.out.println(output);
		}
		//System.out.println(result);
		return result;
	}
	
	public static String changeLinkArticle(String content) {
		List<String> strs= Arrays.asList(Pattern.compile("</a>").split(content));
		String result ="";
		String input="";
		Pattern p=null;
		Matcher m=null;
		for (String string : strs) {			
			input = string+"</a>";
			p = Pattern.compile(REGEX,Pattern.CASE_INSENSITIVE);
			m = p.matcher(input); // get a matcher object
			int count = 0;
			String keysearch = "";			
			String output = "";
			String a="";
			String replacementStr = "";
			if (m.find()) {
				count++;
				a = input.substring(m.start(), m.end());
				p = Pattern.compile(">.*<");
				Matcher m1 = p.matcher(a);
				if (m1.find()) {
					keysearch = a.substring(m1.start() + 1, m1.end() - 1);
					replacementStr = "<a href='http://az24.vn/raovat/search/index?r=search%2Findex&keyword="
							+ keysearch + "&category=0'>" + keysearch + "</a>";
					// Compile regular expression
					p = Pattern.compile(REGEX);
					// Replace all occurrences of pattern in input
					m = p.matcher(input);
					output = m.replaceAll(replacementStr);
				}
				result +=output;
			}else
			{
				result +=string;
			}
	
			//System.out.println(output);
		}
		//System.out.println(result);
		return result;
	}
	
	public static void main(String[] args) {
		ContentFilter.changeLink("Tran Van An <a href='acer'>acer1</a> Cac ban thu click vao day <a href='acer'>acer2</a> Tran Van Quang");
	}
}
