package com.az24.test;

import hdc.util.text.StringUtil;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexMatches2 {
	 	private static final String REGEX = "foo";
	    private static final String INPUT = "fooooooooooooooooo";
	    private static Pattern pattern;
	    private static Matcher matcher;

	    public static void main( String args[] ){
	       pattern = Pattern.compile(REGEX);
	       matcher = pattern.matcher(INPUT);

	       System.out.println("Current REGEX is: "+REGEX);
	       System.out.println("Current INPUT is: "+INPUT);

	       System.out.println("lookingAt(): "+matcher.lookingAt());
	       System.out.println("lookingAt(): "+matcher.find(1));
	       System.out.println("matches(): "+matcher.matches());
	       
	       String url = "http://thegioinem.com/home/modules.php?name=Shop&go=goods&pid=180";
	       String REGEX2 ="modules.php\\?name=Shop&go=goods&pid=\\d+";
	       pattern = Pattern.compile(REGEX2);
	       matcher = pattern.matcher(url);
	       
	       System.out.println("lookingAt(): "+matcher.find());
	       int s1 =  131080;
	       int s2= 8;
	       BigInteger bigInteger1 = new BigInteger("131074");
	       BigInteger bigInteger2 = new BigInteger("2");
	       BigInteger is = bigInteger1.and(bigInteger2);
	       System.out.println("lookingAt(): "+is);
	       
	       String file = "small_132971584423520120111161719_0.jpg?w=55&h=38";
	       pattern = Pattern.compile(".");
	       matcher = pattern.matcher(file);
	       String files[] = file.split("\\.");
	       if(files!=null)
	       {	int i = 0;
	    	   for (String string : files) {
				System.out.println(files[i].substring(0,3));i++;
			}
	       }
	    String src  ="http://imgs.vietnamnet.vn/Images/2012/02/20/14/20120220142745_Foxconn.jpg?w=190&h=125"; 
	   	String image_name =src.substring(src.lastIndexOf("/")+1);
		String arrSrc[] =image_name.split("\\.");
		
		Pattern r = Pattern.compile("\\W");
		Matcher m = r.matcher( arrSrc[0]);
		 arrSrc[0] = m.replaceAll("_");
		 image_name = arrSrc[0]+"."+arrSrc[1].substring(0,3);
		System.out.println(image_name);
	
		String date  ="Th? m?c: Bóng ?á Châu Âu . Ngày g?i: Th? n?m, 14:21, 16/9/2010 fdf 16/9/2010  .... ph?n h?i";
		r = Pattern.compile("\\d+/\\d+/\\d+");
		m = r.matcher(date);
		if(m.find()){
		//System.out.println(m.group(1));
	   }
	    
	    String url1  ="http://baobinhduong.org.vn/printThumbImg.aspx?imgurl=\\resources\\newsimg\\08062011/34129.jpg&width=150" ;
		r = Pattern.compile("[^a-z,^A-Z,^\\d+,^&,^.,^=,^/,^:,^\\?]");
		m = r.matcher(url1);
		if(m.find())
		 System.out.println(m.replaceAll("/"));
		
		url1 =".gif? d ";
		
		url1 = url1.trim();
		r = Pattern.compile("\\s+");
		m = r.matcher(url1);
		if(m.find()) url1 = m.replaceAll("%20");
		System.out.println(url1);
		
		r = Pattern.compile("jpg\\?|png\\?|gif\\?|bmp\\?|jpg&|png&|gif&|bmp&");
		m = r.matcher(url1);
		System.out.println(m.find());
		
		url ="http://tuoitre.vn/Nhip-song-tre/Lam-dep/My-pham/382895/Mau-mat-nao len-ngoi_ he-nay.html";
		r = Pattern.compile("[^\\.\\w:/-_]");
		m = r.matcher(url);
		//if(m.find())
		url = m.replaceAll("%20");
		System.out.println("Extract url=" + url);

		System.out.println("66="+url.substring(66,69));
		pattern = Pattern.compile("\\D");
		matcher = pattern.matcher("545.445 vnd");
		System.out.println("66="+matcher.replaceAll(""));
		
		char chars[] =  {60,97,32,104,114,101,102,61,34,109,97,105,108,116,111,58,109,121,108,97,110,100,115,97,105,103,111,110,50,48,49,50,64,103,109,97,105,108,46,99,111,109,34,62,109,121,108,97,110,100,115,97,105,103,111,110,50,48,49,50,64,103,109,97,105,108,46,99,111,109,60,47,97,62,32};
		String s = new String(chars);
		System.out.println(s);
		
		src ="../../fafd.jp";
		r = Pattern.compile("\\.\\./");
		m = r.matcher(src);
		src = m.replaceAll("/");
		System.out.println(src);
		String ss=" document.write(String.fromCharCode(60,97,32,104,114,101,102,61,34,109,97,105,108,116,111,58,109,121,108,97,110,100,115,97,105,103,111,110,50,48,49,50,64,103,109,97,105,108,46,99,111,109,34,62,109,121,108,97,110,100,115,97,105,103,111,110,50,48,49,50,64,103,109,97,105,108,46,99,111,109,60,47,97,62,32));";
		
			if(StringUtil.isEmpty(ss))
			{
				r = Pattern.compile("[^\\d,]");
				m = r.matcher(ss);System.out.println(m.replaceAll(""));
				String strChar[]=m.replaceAll("").split(",");
				int i = 0;
				for (String string : strChar) {
					chars[i]= new Character((char)Integer.parseInt(string));
					i++;
				}
				
				s = new String(chars);
				r = Pattern.compile(">(.*)<");
				m = r.matcher(s);				
				if(m.find())
				System.out.println(m.group(1));
			}
			
			
			r = Pattern.compile("!^(84)");
			m = r.matcher("2384");
			
			System.out.println(m.find());
	   }
	    
	   
	   
	    
}
