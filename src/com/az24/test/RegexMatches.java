package com.az24.test;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexMatches {
	private static final String REGEX = "<a.*>.*</a>";
	private static  String INPUT = " <a href='acer'>acer1</a>fdfd fd fdf<a href='acer'>acer1</a>fd";

	public static void main(String args[]) {
		List<String> strs= Arrays.asList(Pattern.compile("</a>").split(INPUT));
		String result ="";
		for (String string : strs) {			
		INPUT = string+"</a>";
		Pattern p = Pattern.compile(REGEX,Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(INPUT); // get a matcher object
		int count = 0;
		String keysearch = "";
		String patternStr = "<a.*\\W*>.*</a>";
		String output = "";
		
		String replacementStr = "";
		if (m.find()) {
			count++;
			System.out.println("Match number " + count);
			System.out.println("start(): " + m.start());
			
			System.out.println("end(): " + m.end());
			String a = INPUT.substring(m.start(), m.end());
			p = Pattern.compile(">.*<");
			Matcher m1 = p.matcher(a);
			if (m1.find()) {
				keysearch = a.substring(m1.start() + 1, m1.end() - 1);
				replacementStr = "<a href='http://az24.vn/raovat/search/index?r=search%2Findex&keyword="
						+ keysearch + "&category=0'>" + keysearch + "</a>";
				// Compile regular expression
				p = Pattern.compile(patternStr);
				// Replace all occurrences of pattern in input
				m = p.matcher(INPUT);
				output = m.replaceAll(replacementStr);
			}
			result +=output;
		}else
		{
			result +=string;
		}
		

		System.out.println(output);
		}
		System.out.println(result);
		
		Pattern p = Pattern.compile("\\d{2}:\\d{4}/\\d{2}");
		Matcher m = p.matcher("16:0029/05  "); // get a matcher object
		if(m.find()) System.out.println("co"); else System.out.println("kco"); 
		System.out.println("1 - 0".replaceAll("\\s", ""));
		System.out.println("2 - 10".replaceAll("[^\\d-]", ""));
		System.out.println("vs".replaceAll("[^\\d-]", ""));
		
		
		System.out.println("Tran The   Thang \t ND \n ffdf");
	    System.out.println("Tran The   Thang \t ND \n ffdf".replaceAll("\\s+", " "));
	    System.out.println("***&^%JPN3".replaceAll("\\W", ""));
	}
}
