package com.az24.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.az24.util.UTF8Tool;

public class RegexMatches3 {
	public static void main( String args[] ){

	      // String to be scanned to find the pattern.
	      String line = "<a href=\"/topsites/countries/VN\" title=\"Vietnam\">VN</a>:10";
	      String pattern = "(<a.*</a>:)(\\d+)";
	      
	      line = "javascript:show_picture('pictures_fullsize', 'fov1320926616.png')";
	      pattern = "(.*')(.*)('.*)";
	      
	      line = "AC_FL_RunContent( 'codebase','http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,29,0','width','780','height','200','title','Nh?p kh?u ,phân ph?i ,s?a ch?a các lo?i máy thi?t b? v?n phòng','src','/banner_user/xwh1253783883.swf','quality','high','pluginspage','http://www.macromedia.com/go/getflashplayer','movie','/banner_user/xwh1253783883','wmode','transparent' );";
	      pattern = "'src','(.*)','quality'";
	      //pattern = "'width','(.*)','height'";
	      pattern = "'height','(.*)','title'";
	      // Create a Pattern object
	      Pattern r = Pattern.compile(pattern);

	      // Now create matcher object.
	      Matcher m = r.matcher(line);
	      if (m.find( )) {
	         System.out.println("Found value: " + m.group(0) );
	         System.out.println("Found value: " + m.group(1) );
	        // System.out.println("Found value: " + m.group(2) );
	      } else {
	         System.out.println("NO MATCH");
	      }
	      
	      line = "load(\"/ket-qua/w-ty-le-tran-dau-Shandong.Luneng-vs-Kashiwa.Reysol-310557.html\");";
	      pattern = "'src','(.*)','quality'";
	      //pattern = "'width','(.*)','height'";
	      pattern = "(/ket-qua/w-ty-le-tran-dau.*.html)";
	      // Create a Pattern object
	       r = Pattern.compile(pattern);

	      // Now create matcher object.
	       m = r.matcher(line);
	      if (m.find( )) {
	         System.out.println("Found value: " + m.group(0) );
	         System.out.println("Found value: " + m.group(1) );
	        // System.out.println("Found value: " + m.group(2) );
	      } else {
	         System.out.println("NO MATCH");
	      }
	      
	      
	      
	      
	      r = Pattern.compile("\\W+");

	      // Now create matcher object.
	      m = r.matcher("Acer beTouch E100 (Acer C1)".trim());
	      String name = m.replaceAll("-");
	      System.out.println(name);
	      r = Pattern.compile("-$");
	      m = r.matcher(name.trim());
	      name = m.replaceAll("");
	      
	      
	      System.out.println(name);
	      
	      Pattern pattern2 = Pattern.compile("\\W");
	      String keyword = UTF8Tool.coDau2KoDau("Xe máy ôtô");
		  m = pattern2.matcher(keyword);
		  keyword = m.replaceAll(" ");
		  System.out.println(keyword);
	     /* String abc = "<p class=\"giasanpham\"><span>Giá bán:</span> 18,300,000 V.N.D</p>";
	       r = Pattern.compile("\\D");m = r.matcher(abc);
	       abc= m.replaceAll(" ");
	       System.out.println(abc);*/
		  String short_content = "dd            ssssssss d   d";
		  pattern2 = Pattern.compile("\\s{2,}");
			m = pattern2.matcher(short_content);
			short_content = m.replaceAll(" ");
			System.out.println(short_content);
			
			String url ="http://www.tienphong.vn/The-Thao/147925/Rong-cua-nhung-con qua-som-de-an-mung.html";
			  pattern2 = Pattern.compile("h^[\\w-]");
				m = pattern2.matcher(url);
				url = m.replaceAll("-");System.out.println(url);
				
				String tomtat = "http://img1.thethaovanhoa.vn/GetThumbNail.ashx?ImgFilePath=http://media.thethaovanhoa.vn/Images/Uploaded/Share/2010/01/01/02.jpg&width=120";

				pattern2 = Pattern.compile("ImgFilePath=(.*)&");
				m = pattern2.matcher(tomtat);
				if(m.find())
				{
					System.out.println(m.group(1)); 
				}
				 String abc = "abc..jpg";
				    String name1 = abc.substring(0,abc.lastIndexOf("."));
				    String duoi1 = abc.substring(abc.lastIndexOf(".")+1,abc.lastIndexOf(".")+4);
				    System.out.println(name1);
				    System.out.println(duoi1);	
				    
				    tomtat = "Dien thoai: 08. 39290549";
				    pattern2 = Pattern.compile("[a-z,A-Z,\\s+,:]");
					m = pattern2.matcher(tomtat);
					
						System.out.println(m.replaceAll("")); 
						
						 tomtat = "email: thangtt@az24.vn";
						    pattern2 = Pattern.compile(" (.*@.*[.vn|.com])");
							m = pattern2.matcher(tomtat);
						
					  if(m.find())
						{
							System.out.println(m.group(0)); 
						}
						/*
						 * Mobifone 090, 093, 0122, 0126, 0121, 0128, 0120 
						Vinaphone 091, 094, 0123, 0125, 0127 
						Viettel 097, 098, 0168, 0169, 0166, 0167, 0165 
						EVN - Telecom 096, Cố định S-fone 095 Vietnamobile 092 Beeline 0199 
						 * 
						 */	
				      line = " 0945 794 490  0945.794.497 (Mr. Thao) - 0945.098.890  0974838181  091 596 2200 (Mr. Hải) ";
				      pattern = "\\d{4}\\.\\d{3}\\.\\d{3}|\\d{4}\\s\\d{3}\\s\\d{3}|\\d{10,11}";
				      pattern = "\\d{4}\\.\\d{3}\\.\\d{3}|\\d{4}\\s\\d{3}\\s\\d{3}|\\d{10,11}|\\d{4}\\.\\d{2}\\.\\d{2}\\.\\d{3}|\\d{4}\\.\\d{3}\\.\\d{4}|\\d{3}\\s\\d{3}\\s\\d{4}|\\d{3}\\.\\d{3}\\.\\d{4}";
				      r = Pattern.compile(pattern);
				      m = r.matcher(line);
				      while (m.find()) {
				    	  Pattern r1 = Pattern.compile("^090|^093|^0122|^0126|^0121|^0128|^0120|^091|^094|^0123|^0125|^0127|^097|^098|^0168|^0169|^0166|^0167|^0165|^096|^095|^092|^0199");
					      Matcher m1 = r1.matcher( m.group(0) );
					      if(m1.find())
					      {
				         System.out.println("Found value: " + m.group(0) );
					      }
				      } 			 
					
				      
				      Pattern r1 = Pattern.compile("\\s\\w+=\"\\w+\\s|\\s\\w+=\\w+\"\\s");
				      String conntent ="tran the height=25\"    ";
				      Matcher m1 = r1.matcher(conntent);
				      if(m1.find())
				      {
				    	  conntent = m1.replaceAll(" ");
				    	  System.out.println("Found value: "+conntent);
				      }
				      
				      r1 = Pattern.compile("alt=([^\"].+[^\"])");
				      conntent ="alt=Đọc truyện tranh Angel Sanctuary - Chapter 1";
				      m1 = r1.matcher(conntent);
				      if(m1.find())
				      {
				    	  //
				    	 System.out.println("Found value: "+m1.group(1));
				    	  conntent = m1.replaceAll("\""+m1.group(1)+"\"");
				    	  System.out.println("Found value: "+conntent);
				      }
				      
				  	  r1 = Pattern.compile("^\\.method protected|public onCreate(.*)V$");
					   m1 = r1.matcher(".method public onCreate(Landroid/os/Bundle;)V");
						 if(m1.find())
					      {
						 System.out.println("ok");
					      }else{
					    	  System.out.println("not ok");
					      }
						 
						 r1 = Pattern.compile("[^\\.]");
						   m1 = r1.matcher("method public .onCreate(Landroid/os/Bundle;)V");
							 if(m1.find())
						      {
							 System.out.println("ok");
						      }else{
						    	  System.out.println("not ok");
						      }
					
							   r1 = Pattern.compile(".* (\\d{2}/\\d{2}/\\d{4})");
							   System.out.println("Thứ 2, 19/05/2014");
							   m1 = r1.matcher("Thứ 2 19/05/2014");
							   if(m1.find()){
								   String ngay_quay_vn = m1.group(1);
								   System.out.println(ngay_quay_vn);
							   }
							   
							   //        29'       NGUY HIỂM !!! Clichy thoát xuống bên cánh trái đón đường chuyền của Silva, rất tiếc cú đá ngay ở góc hẹp đưa bóng đi chệch cột dọc.
								
							   r1 = Pattern.compile("^\\s+(\\d+.*')\\s+(.*$)");
							      conntent ="alt=Đọc truyện tranh Angel Sanctuary - Chapter 1";
							      m1 = r1.matcher("     90'       NGUY HIỂM !!! Clichy thoát xuống bên cánh trái đón đường chuyền của Silva, rất tiếc cú ");
							      if(m1.find())
							      {
							    	  //i 
							    	 int i = 0;
							    	 while(i<m1.group().length()){
							    		// System.out.println("Found value: "+m1.group(i));
							    		 i++;
							    	 }
							    	 
							    	  conntent = m1.replaceAll("\""+m1.group(1)+"\"");
							    	  System.out.println("Found value: "+conntent);
							      }
							      
							      r1 = Pattern.compile("-(\\d+)\\.html");   
							      conntent ="phong-do-karsiyaka-vs-samsunspor-4705-345373.html";
							      m1 = r1.matcher(conntent);
							      if(m1.find())
							      {
							    	  //i 
							    	 int i = 0;
							    	// while(i<m1.group().length()){
							    		 System.out.println("Found value 1: "+m1.group(1));
							    		// i++;
							    	 //}
							      }
							
	   }
}
