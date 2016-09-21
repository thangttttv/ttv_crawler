package hdc.util.text;

public class HtmlUtil {
  final static public String escDoubleQuote(String s) {
    if(s == null || s.length() == 0) return s ;
    return s.replaceAll("\"", "&quot;") ;
  }
  
  final static public String toText(String text) {
    char[] buf = text.toCharArray() ;
    StringBuilder b = new StringBuilder(text.length()) ;
    int idx = 0 ;
    while(idx < buf.length) {
      if(buf[idx] == '<') {
        int endTagPos = 0 ;
        if(idx + 2 < buf.length && buf[idx + 1] == '!' && buf[idx + 2] == '-') {
          endTagPos = endTagPosition(buf, idx, 3000, false) ;
        } else {
          endTagPos = endTagPosition(buf, idx, 300, false) ;
        }
        if(endTagPos > 0) {
          //System.out.println(new String(buf, idx, endTagPos - idx + 1));
          String string = replaceTag((new String(buf, idx, endTagPos - idx + 1)));
          if(string != null) b.append(string) ;
          idx = endTagPos + 1 ;
        } else {
          b.append(buf[idx]) ;
          idx++ ;
        }
      } else {
        b.append(buf[idx]) ;
        idx++ ;
      }
    }
    return b.toString() ; 
  }
  
  final static public String replaceTag(String tag) {
    tag = tag.toLowerCase() ;
    if(tag.startsWith("<p")) return " . " ;
    else if(tag.startsWith("<br")) return " . " ;
    else if(tag.startsWith("<tr")) return " . " ;
    else if(tag.startsWith("<li")) return " . " ;
    return null ;
  }
  
  final static public String removeTag(String text) { 
    char[] buf = text.toCharArray() ;
    StringBuilder b = new StringBuilder(text.length()) ;
    int idx = 0 ;
    while(idx < buf.length) {
      if(buf[idx] == '<') {
        int endTagPos = 0 ;
        if(idx + 2 < buf.length && buf[idx + 1] == '!' && buf[idx + 2] == '-') {
          endTagPos = endTagPosition(buf, idx, 3000, false) ;
        } else {
          endTagPos = endTagPosition(buf, idx, 300, false) ;
        }
        if(endTagPos > 0) {
          idx = endTagPos + 1 ;
        } else {
          b.append(buf[idx]) ;
          idx++ ;
        }
      } else {
        b.append(buf[idx]) ;
        idx++ ;
      }
    }
    return b.toString() ; 
  }
  
  private static boolean isEndSentenceTag(char[] buf, int pos) {
    char ch = buf[pos] ;
    if(ch == 'p' || ch == 'P') return true ;
    int nextPos = pos + 1 ;
    if(nextPos == buf.length) return false ;
    char nextChar = buf[nextPos] ;
    if(ch == 'b' || ch == 'B') {
      if(nextChar == 'r' || nextChar == 'R') return true ;
    } else if(ch == 'l' || ch == 'L') {
      if(nextChar == 'i' || nextChar == 'I') return true ;
    } else if(ch == 't' || ch == 'T') {
      if(nextChar == 'r' || nextChar == 'R') return true ;
    }
    return false ;
  }
  
  private static int endTagPosition(char[] buf, int pos, int maxLookup, boolean ignoreEndSentence) {
    if(pos + 1 == buf.length) return -1 ;
    char nextChar = buf[pos + 1] ;
    if(ignoreEndSentence && isEndSentenceTag(buf, pos + 1)) return -1;
    if(nextChar == '!' || nextChar == '/' || Character.isLetter(nextChar)) {
      int limit = pos + maxLookup ;
      if(limit > buf.length) limit = buf.length ;
      for(int i = pos; i < limit; i++) {
        if(buf[i] == '>') return i ;
      }
    }
    return -1 ;
  }
  
  public static void main(String[] args) {
	System.out.println(HtmlUtil.toText("<h1><b><font size=\"4\">Vitamin nào ti?p nang lu?ng cho làn da?</font></b></h1>"));
  }
}
