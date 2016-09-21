package hdc.util.text;


import java.io.CharArrayWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

final public class TokenParser {
	final static public TokenParser INSTANCE = new TokenParser() ;
	private List<Token> list = new ArrayList<Token>(1500) ;

	public List<Token> parse(char[] buf) { 
	  list.clear() ;
    TokenizerImpl tokenizer = new TokenizerImpl(buf) ;
    Token currentToken = null ;
    while(tokenizer.next()) {
      if(tokenizer.getType() == TokenizerImpl.XML_TAG) {
        currentToken = new Token.DefaultToken(tokenizer.getCurrentToken()) ;
//        list.add(currentToken) ;
      } else if(tokenizer.getType() == TokenizerImpl.PUNCTUATION) {
        currentToken = new Token.DefaultToken(tokenizer.getCurrentToken()) ;
//        list.add(currentToken) ;
      } else if(tokenizer.getType() == TokenizerImpl.NEW_LINE) {
        currentToken = new Token.DefaultToken(tokenizer.getCurrentToken(), "new_line") ;
        list.add(currentToken) ;
      } else if(tokenizer.getType() == TokenizerImpl.BLANK) {
        if(currentToken == null) {
          currentToken = new Token.DefaultToken(" ", "blank") ;
          list.add(currentToken) ;
        } 
      } else {
        currentToken = new Token.DefaultToken(tokenizer.getCurrentToken()) ;
        list.add(currentToken) ;
      }
    }
    return new ArrayList<Token>(list) ;
  }
	
	final public List<Token> parse(String text) { 
	  return parse(text.toCharArray()) ;
	}
	
	final public List<Token> parse(Reader reader) {
	  try {
	    char[] data  = new char[4912];      
	    int available = -1 ;
	    CharArrayWriter w = new CharArrayWriter(4912) ;
	    while((available = reader.read(data)) > -1){
	      w.write(data, 0, available);
	    }
	    return parse(w.toCharArray()) ;
	  } catch(Exception ex) {
	    throw new RuntimeException(ex) ;
	  }
	}
}
