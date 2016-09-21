package com.az24.test;

import java.util.Arrays;
import java.util.regex.Pattern;

public class StyleSplitExample {
	 public static void main(String[] args) {
		    String input = "This!!unusual use!!of exclamation!!points";
		    System.out.println(Arrays.asList(Pattern.compile("!!").split(input)));
		    // Only do the first three:
		    System.out
		        .println(Arrays.asList(Pattern.compile("!!").split(input, 3)));
		    System.out.println(Arrays.asList("Aha! String has a split() built in!"
		        .split(" ")));
		    //<a href='acer'>acer1</a>
		    input="f <a href='acer'>acer1</a> fdlkkf <a href='acer'>acer1</a> ff";
		    System.out.println(Arrays.asList(Pattern.compile("</a>").split(input)));
		  }
}
