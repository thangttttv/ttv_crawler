package hdc.util.text;


public interface Token {

  public String getTokenType() ;
  
  public String getNormalizeString() ;
  
  public String getOriginalString() ;
  
  public static class DefaultToken implements Token {
    private String type = "default" ;
    private String value ;
    
    public DefaultToken(String value) {
      this.value = value ;
    }
    
    public DefaultToken(String value, String type) {
      this.value = value ;
      this.type = type ;
    }
    
    public String getTokenType() { return type ; }

    public String getNormalizeString() { return  StringUtil.toLowcase(value) ; }

    public String getOriginalString() { return value ; }
  }
}
