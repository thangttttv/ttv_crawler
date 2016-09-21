package hdc.util.text;


import java.io.PrintStream;
import java.util.List;

final public class TokenizerImpl implements Tokenizer {
  final static public byte UNKNOWN = 0 ;   // Unkown Token
  final static public byte BLANK = 1 ;    // Space String, \t ' '
  final static public byte NEW_LINE = 2 ; // New Line String 
  final static public byte PUNCTUATION = 3 ; //PUNCTUATION
  final static public byte XML_TAG = 4 ;  // XML Tag (open, close, comment)
  final static public byte WORD = 5 ;

  char[] buf ;
  int pos = 0;

  int start = 0;
  int end = 0;
  byte type ;
  
  public TokenizerImpl() {}

  public TokenizerImpl(char[] buf) {
    this.buf = buf;
  }

  public byte getType() { return type ; }

  public int getStartToken() { return start ; }

  public int getEndToken() { return end ;}

  public char[] getBuffer() { return buf ;}
  
  private void set(int start, int end, byte type) {
    this.start = start ;
    this.end = end ;
    this.type = type ;
  }

  public boolean next() {
    while(pos < buf.length) {
      char c = buf[pos] ;
      if(c == '<' && pos + 3 < buf.length) {  //  Detect XML token // open charactor
        int start = pos, end = -1 ; 
        if(!CharacterSet.isAlphaDigit(buf[start+1]) && 
            buf[start+1] != '!' && buf[start+1] != '/' && buf[start+1] != '?') {
          set(start, start, PUNCTUATION) ;
          pos = start + 1 ;
        } else if(pos < buf.length - 1) {
          pos++ ;
          while(pos < buf.length) {
            if(buf[pos] == '>') {
              end = pos++ ; 
              break ;
            } else if(buf[pos] == '<') {
              break ;
            }
            pos++ ;
          }
        }
        if(end > 0) { // if close charactor found.
          set(start, end, XML_TAG) ;
        } else {  // only '<' character
          set(start, start, PUNCTUATION) ;
          pos = start + 1 ;
        }
      } else if(CharacterSet.isBlank(c)) {
        int start = pos ;
        while(pos < buf.length && CharacterSet.isBlank(buf[pos])) pos++ ;
        set(start, pos -1, BLANK) ;
      } else if(CharacterSet.isNewLine(c)) {
        int start = pos ;
        while(pos < buf.length && CharacterSet.isNewLine(buf[pos])) pos++ ;
        set(start, pos -1, NEW_LINE) ;
      } else if(CharacterSet.isWordCharacter(buf, pos)) {
        int start = pos ;
        while(pos < buf.length && CharacterSet.isWordCharacter(buf, pos)) pos++ ;
        set(start, pos -1, WORD) ;
      } else if(CharacterSet.isPunctuation(c)) {
        set(pos, pos++	, PUNCTUATION) ;
      }  else {
        set(pos, pos++, UNKNOWN) ;
      }
      return true ;
    }
    return false ;
  }

  public boolean nextMatchToken(char[] matchBuf, CharacterComparator cComparator) {
    if(start + matchBuf.length > buf.length) return false  ;
    for(int i = 0; i < matchBuf.length; i++) {
      int bufIndex = i + start ;
      if(bufIndex < buf.length) {
        if(cComparator.compare(buf[bufIndex], matchBuf[i]) != 0) {
          return false ;
        }
      } else {
        return false ;
      }
    }
    if(start + matchBuf.length == buf.length) {
      end = start + matchBuf.length - 1 ;
      pos = end + 1 ;
      return true ;
    }

    if(!CharacterSet.isWordCharacter(buf, start + matchBuf.length)) {
      end = start + matchBuf.length - 1 ;
      pos = end + 1 ;
      return true ;
    }
    return false ;
  }

  public String getCurrentToken() {
    return new String(buf, start, end - start + 1) ;
  }

  public char[] getCurrentTokenBuf() {
    char[] tmpbuf = new char[end - start + 1] ;
    System.arraycopy(buf, start, tmpbuf, 0, tmpbuf.length) ;
    return tmpbuf ;
  }

  public char getCurrentTokenCharacter() { return buf[start] ; }

  public void getCurrentToken(StringBuilder b) { b.append(buf, start, end - start + 1) ; }

  public void dumpToken(PrintStream out) {
    if(type == NEW_LINE)     out.println("NEW LINE: " + (end - start + 1) + " characters") ;
    else if(type == PUNCTUATION) out.println("PUNCTUATION: " + new String(buf, start, end - start + 1)) ;
    else if(type == XML_TAG) out.println("XML TAG: " + new String(buf, start, end - start + 1)) ;
    else if(type == BLANK)   out.println("BLANK: [" +  new String(buf, start, end - start + 1) + "]") ;
    else out.println("UNKNOWN: " + new String(buf, start, end - start + 1)) ;
  }

  public List<Token> analyze(String src) {
    TokenParser parser = TokenParser.INSTANCE ;
    return parser.parse(src) ;
  }
}
