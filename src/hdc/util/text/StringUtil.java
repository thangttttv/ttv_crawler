package hdc.util.text;

import hdc.crawler.ExtractEntity;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.az24.util.UTF8Tool;

public class StringUtil {
  final static public  Charset UTF8 = Charset.forName("UTF-8") ;
  final static public  String[] EMPTY_ARRAY = {} ;
  final static public  String SEPARATOR = "," ; 
  
  final static public String toLowcase(String s) {
    if(s == null || s.isEmpty()) return null ;
    int length = s.length() ;
    boolean accent = VietnameseUtil.containVietnameseCharacter(s) ;
    StringBuilder b = new StringBuilder(length) ;
    for(int i = 0; i < length; i++) {
      char c = s.charAt(i) ;
      b.append(toLowcase(c, accent)) ;
    }
    return b.toString() ;
  }
  
  final static char toLowcase(char c, boolean accent) {
    switch (c) {
      case 'A': return 'a';
      case 'B': return 'b';
      case 'C': return 'c';
      case 'D': return 'd';
      case 'E': return 'e';
      case 'F': return 'f';
      case 'G': return 'g';
      case 'H': return 'h';
      case 'I': return 'i';
      case 'J': return 'j';
      case 'K': return 'k';
      case 'L': return 'l';
      case 'N': return 'n';
      case 'M': return 'm';
      case 'O': return 'o';
      case 'P': return 'p';
      case 'Q': return 'q';
      case 'R': return 'r';
      case 'T': return 't';
      case 'S': return 's';
      case 'U': return 'u';
      case 'V': return 'v';
      case 'W': return 'w';
      case 'X': return 'x';
      case 'Y': return 'y';
      case 'Z': return 'z';
      default: {
        if(accent) return VietnameseUtil.toLowcaseVietnamese(c) ;
        else return c ;
      }
    }
  }
  
  static public boolean isEmpty(String s) {
	  if(s == null )return true;
	  s=s.trim();	  
	  return s.length() == 0 ;
  }
  
  static public int compare(String s1, String s2) {
    if(s1 == null && s2 == null) return  0;
    if(s1 == null) return -1 ;
    return s1.compareTo(s2) ;
  }
  
  final static public String joinStringArray(String[] array) {
    return joinStringArray(array, SEPARATOR) ;
  }
  
  final static public String joinStringArray(String[] array, String separator) {
    if(array == null) return null ;
    if( array.length == 0) return "" ;
    StringBuilder b = new StringBuilder() ;
    for(int i = 0; i < array.length; i++) {
      if(array[i] == null) continue ;
      b.append(array[i]) ;
      if(i < array.length - 1) b.append(separator) ;
    }
    return b.toString() ;
  }
  
  final static public String joinStringCollection(Collection<String> collection, String separator) {
    if(collection == null) return null ;
    if(collection.size() == 0) return "" ;
    StringBuilder b = new StringBuilder() ;
    Iterator<String> i = collection.iterator() ;
    while(i.hasNext()) {
    	if(b.length() > 0) b.append(separator) ;
    	b.append(i.next()) ;
    }
    return b.toString() ;
  }

  final static public String joinStringArray(Object[] array, String separator) {
    if(array == null) return null ;
    if( array.length == 0) return "" ;
    StringBuilder b = new StringBuilder() ;
    for(int i = 0; i < array.length; i++) {
      b.append(array[i]) ;
      if(i < array.length - 1) b.append(separator) ;
    }
    return b.toString() ;
  }
  
  final static public String joinIntArray(int[] array, String separator) {
    if(array == null) return null ;
    if( array.length == 0) return "" ;
    StringBuilder b = new StringBuilder() ;
    for(int i = 0; i < array.length; i++) {
      b.append(array[i]) ;
      if(i < array.length - 1) b.append(separator) ;
    }
    return b.toString() ;
  }
  
  final static public String[] join(String[] array1, String[] array2) {
    if(array1 == null && array2 == null) return null ;
    if(array1 == null) return array2 ;
    if(array2 == null) return array1 ;
    String[] newArray = new String[array1.length + array2.length] ;
    System.arraycopy(array1, 0, newArray, 0, array1.length) ;
    System.arraycopy(array2, 0, newArray, array1.length, array2.length) ;
    return newArray;
  }
  
  final static public String[] merge(String[] array1, String[] array2) {
    if(array1 == null && array2 == null) return null ;
    if(array1 == null) return array2 ;
    if(array2 == null) return array1 ;
    HashSet<String> set = new HashSet<String>();
    for (String string : array1) set.add(string);
    for (String string : array2) set.add(string);
    return toArray(set);
  }
  
  final static public String[] merge(String[] array, String string) {
    if(array == null && string == null) return null ;
    if(array == null) return new String[] { string } ;
    if(string == null) return array ;
    for(int i = 0; i < array.length; i++) {
      if(string.equals(array[i])) return array ;
    }
    String[] narray = new String[array.length + 1] ;
    for(int i = 0; i < array.length; i++) narray[i] = array[i] ;
    narray[array.length] = string ;
    return narray ;
  }
  
  final static public String[] toStringArray(String s) {
    return toStringArray(s, SEPARATOR) ;
  }
  
  final static public String[] toStringArray(String s, String separator) {
  	if(s == null || s.length() == 0) return EMPTY_ARRAY ;
    String[] array = s.split(separator) ;
    for(int i = 0; i < array.length; i++) array[i] = array[i].trim() ;
    return array ;
  }
  
  final static public int[] toIntArray(String s, String separator) {
    if(s == null) return null ;
    String[] array = s.split(separator) ;
    for(int i = 0; i < array.length; i++) array[i] = array[i].trim() ;
    int[] value = new int[array.length] ;
    for(int i = 0; i < value.length; i++) value[i] = Integer.parseInt(array[i].trim()) ;
    return value ;
  }

  static public HashSet<String> toStringHashSet(String s) {
    HashSet<String> set = new HashSet<String>() ;
    if(s == null || s.length() == 0) return set ;
    String[] array = s.split(SEPARATOR) ;
    for(int i = 0; i < array.length; i++) set.add(array[i].trim()) ;
    return set ;
  }
  
  static public String[]  toArray(java.util.Collection<String> collection) {
    String[] array = new String[collection.size()] ;
    Iterator<String> i = collection.iterator() ;
    int index = 0 ;
    while(i.hasNext()) {
      array[index] = i.next() ;
      index++ ;
    }
    return array ;
  }
  
  static public List<String> toList(String[] array) {
    List<String> list = new ArrayList<String>() ;
    for(String s :  array) list.add(s) ;
    return list ;
  }
  
  static public String eatCharacter(String string, char ignoreChar) {
    if(string == null || string.length() == 0) return string; 
    char[] array = string.toCharArray() ;
    StringBuilder b  = new StringBuilder() ;
    for(char c : array) {
      if(c != ignoreChar) b.append(c) ;
    }
    return b.toString() ;
  }
  
  final static public List<String> split(char[] buf, char separator) {
  	List<String> holder = new ArrayList<String>() ;
  	int idx = 0, start = 0 ;
    while(idx < buf.length) {
      if(buf[idx] == separator) {
        if(idx - start > 0) {
          holder.add(new String(buf, start, idx - start).trim()) ;
        }
        idx++ ;
        start = idx ;
      } else {
        idx++ ;
      }
    }
    if(start < buf.length) {
      String s = new String(buf, start, buf.length - start) ;
      holder.add(s.trim()) ;
    }
    return holder;
  }
  
  final static public List<String> split(String string, char separator) {
    char[] buf = string.toCharArray() ;
    return split(buf, separator) ;
  }
  
  final static public List<String> split(String string, String[] separator, boolean includeSeparator) throws Exception {
    List<String> holder = new ArrayList<String>() ;
    final char[][] separatorBuf = new char[separator.length][] ;
    for(int i = 0; i < separator.length; i++) {
      separatorBuf[i] = separator[i].toCharArray() ;
    }
    
    char[] buf = string.toCharArray() ;
    int idx = 0, start = 0 ;
    while(idx < buf.length) {
      boolean match = false ;
      for(int j = 0; j < separatorBuf.length; j++) {
        match = matchAt(buf, idx, separatorBuf[j]) ; 
        if(match) {
          holder.add(new String(buf, start, idx)) ;
          if(includeSeparator) holder.add(separator[j]) ;
          idx += separatorBuf[j].length;
          start = idx ;
        }
      }
      if(!match) idx++ ;
    }
    if(start < buf.length) holder.add(new String(buf, start, buf.length)) ;
    return holder;
  }
  
  static public boolean isIn(String string, String[] set) {
    if(set == null || set.length == 0) return false ;
    if(string == null) return false ;
    for(String sel : set) if(string.equals(sel)) return true;
    return false ;
  }
  
  static public boolean startWithIn(String string, String[] set) {
    if(set == null || set.length == 0) return false ;
    if(string == null) return false ;
    for(String sel : set) if(string.startsWith(sel)) return true;
    return false ;
  }
  
  final static boolean matchAt(char[] buf, int position, char[] bufToMatch) {
    if(position + bufToMatch.length > buf.length) return false ;
    for(int i = 0; i < bufToMatch.length; i++) {
      if(buf[position + i] != bufToMatch[i]) return false ;
    }
    return true ;
  }
  
  public static String stripNonValidXMLCharacters(String in) {
      StringBuffer out = new StringBuffer(); // Used to hold the output.
      char current; // Used to reference the current character.
      if (in == null || ("".equals(in))) return ""; // vacancy test.
      for (int i = 0; i < in.length(); i++) {
          current = in.charAt(i); // NOTE: No IndexOutOfBoundsException caught here; it should not happen.
          if ((current == 0x9) ||
              (current == 0xA) ||
              (current == 0xD) ||
              ((current >= 0x20) && (current <= 0xD7FF)) ||
              ((current >= 0xE000) && (current <= 0xFFFD)) ||
              ((current >= 0x10000) && (current <= 0x10FFFF)))
              out.append(current);
      }
      return out.toString();
  }     
  
  public static String getTextFromNode(String node)
  {
	  String content = node;
		if (content != null	&& content.length() > "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
						.length()&& node.indexOf("<?xml version=\"1.0\" encoding=\"UTF-8\"?>") >-1)
			content = content.substring("<?xml version=\"1.0\" encoding=\"UTF-8\"?>".length());
		return content;
  }
  
  public static String getAttribute(String attribute,ExtractEntity entity)
	{
		String value = "";
		value = StringUtil.isEmpty((String)entity.getProperty(attribute))?"":(String)entity.getProperty(attribute);
		return value.trim();
	}
	
  public static String getPhone(String phone)
  {
	  if(StringUtil.isEmpty(phone))return "";
	  phone = UTF8Tool.coDau2KoDau(phone);
	  Pattern pattern2 = Pattern.compile("[a-z,A-Z,\\s+,:]");
      Matcher m = pattern2.matcher(phone);
      phone = m.replaceAll("");
      return phone;
  }
  
  public static String getAlias(String title)
  {
	    if(StringUtil.isEmpty(title)) return "";
	  	Pattern pattern = Pattern.compile("\\W+");		 
		String alias = UTF8Tool.coDau2KoDau(title).trim();
		Matcher m = pattern.matcher(alias);
		alias = m.replaceAll("-");
		
		pattern = Pattern.compile("-$");
		m = pattern.matcher(alias);
		alias = m.replaceAll("");
		
		return alias.toLowerCase();
  }
  
  public static List<String> getMobile(String str)
  {
	  List<String> list = new ArrayList<String>();
      String pattern = "\\d{4}\\.\\d{3}\\.\\d{3}|\\d{4}\\s\\d{3}\\s\\d{3}|\\d{10,11}";
      Pattern r = Pattern.compile(pattern);
      Matcher m = r.matcher(str);
      while (m.find()) {
    	  Pattern r1 = Pattern.compile("^090|^093|^0122|^0126|^0121|^0128|^0120|^091|^094|^0123|^0125|^0127|^097|^098|^0168|^0169|^0166|^0167|^0165|^096|^095|^092|^0199");
	      Matcher m1 = r1.matcher( m.group(0) );
	      if(m1.find())
	      {
	    	  list.add(m.group(0));
	    	  System.out.println(m.group(0) );
	      }
      } 
      return list;
  }
  
  public static String parseNumber(String str)
  {
      String pattern = "\\D";
      Pattern r = Pattern.compile(pattern);
      Matcher m = r.matcher(str);
      if(m.find())
    	  str = m.replaceAll("");
      return str;
  }
  
  public static String trim(String str)
  {
      String pattern = "\\s{2,}";
      Pattern r = Pattern.compile(pattern);
      Matcher m = r.matcher(str);
      if(m.find())
    	  str = m.replaceAll(" ");
      return str.trim();
  }
  
  public static String parseDate(String str)
  {
	  str = UTF8Tool.coDau2KoDau(str);
      Pattern r = Pattern.compile("[a-z,A-Z,\\s+,(,)]");    
      Matcher m = r.matcher(str);
      if(m.find())
    	  str = m.replaceAll("");
      return str;
  }
  
  public static void main(String[] args) {
	System.out.println("double".hashCode());
	System.out.println("int".hashCode());
	System.out.println("string".hashCode());
	System.out.println("date".hashCode());
	System.out.println("datetime".hashCode());
	System.out.println(StringUtil.parseNumber("23 %"));
	System.out.println(StringUtil.parseDate("Thứ Sáu, 02/11/2012"));
	System.out.println(StringUtil.trim("Thứ Sáu,   02/11/2012"));
	String mobile = "84974838181";
	
	if(mobile.startsWith("0")) mobile ="84"+ mobile.substring(1);
	System.out.println(mobile);
  }
}
