package com.az24.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BasicReplace {
	public static void main(String[] args) {
        
        CharSequence inputStr = "Tran the thang fdf <a href='acer'>acer1</a>fdfd fd fdf";
        String[] x = 
            Pattern.compile("^<a.*>|<").split(inputStr);
          for (int i=0; i<x.length; i++) {
            System.out.println(i + " \"" + x[i] + "\"");
          }
        String patternStr = "<a .*\\W*>.*</a>";
        String replacementStr = "<a href='http://az24.vn/raovat/search/index?r=search%2Findex&keyword="+x[2]+"&category=0'>"+x[2]+"</a>";
        
        
        // Compile regular expression
        Pattern pattern = Pattern.compile(patternStr);
        
        // Replace all occurrences of pattern in input
        Matcher matcher = pattern.matcher(inputStr);
        
        String output = matcher.replaceAll(replacementStr);
        System.out.println(output);
        // p s r p s r
        
        String statement = "Tran the thang <a href='acer'>acer</a>fdfd";

        String tokens[] = null;

        String splitPattern = "<a .*\\W*>|</a>";

        tokens = statement.split(splitPattern);

        System.out.println("REGEX PATTERN:\n" + splitPattern + "\n");

        System.out.println("STATEMENT:\n" + statement + "\n");
        System.out.println("\nTOKENS");
        for (int i = 0; i < tokens.length; i++) {
          System.out.println(tokens[i]);
        }
        
         x = 
            Pattern.compile(">|<").split(
              "<a href='acer'>acer</a>");
          for (int i=0; i<x.length; i++) {
            System.out.println(i + " \"" + x[i] + "\"");
          }
         
          String REGEX = "a*b";

          String INPUT = "Tran the thang <a href='acer'>acer</a>fdfd ";

          String REPLACE = "-";
          Pattern p = Pattern.compile(REGEX);
          Matcher m = p.matcher(INPUT); // get a matcher object
          StringBuffer sb = new StringBuffer();
          while (m.find()) {
            m.appendReplacement(sb, REPLACE);
          }
          m.appendTail(sb);
          System.out.println(sb.toString());
    }
}
