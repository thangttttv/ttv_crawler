package hdc.util.text;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public interface Tokenizer {

  public List<Token> analyze(String src) ;
  
  public static class DefaultTokenizer implements Tokenizer {

    public List<Token> analyze(String src) {
      StringTokenizer tokenizer = new StringTokenizer(src) ;
      List<Token> holder = new ArrayList<Token>() ;
      while (tokenizer.hasMoreTokens()) {
        String s = tokenizer.nextToken() ;
        holder.add(new Token.DefaultToken(s)) ;
      }
      if(holder.size() == 0) return null ;
      return holder;
    }
  }
}
