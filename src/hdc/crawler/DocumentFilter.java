package hdc.crawler;

public interface DocumentFilter {

  public boolean acceptCollect(String url) ;
  
  public boolean acceptFetch(String url) ;
  
  public boolean acceptProcess(String url) ;
}
