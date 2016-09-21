package hdc.crawler;

import java.util.List;

import hdc.util.text.StringUtil;

public class CrawlerConfig {

  private String pathPatter ;
  private int indexStart ;
  private int indexIdentity ;
  
  public CrawlerConfig(String pathPatter) {
    List<String> list = StringUtil.split(pathPatter, '$') ;
    String start = list.get(0).trim() ;
    indexStart = Integer.parseInt(start.substring(start.indexOf("=") + 1)) ;
    String identity = list.get(1).trim() ;
    indexIdentity = Integer.parseInt(identity.substring(identity.indexOf("=") + 1)) ;
    String path = list.get(2).trim() ;
    this.pathPatter = path.substring(path.indexOf("=") + 1) ;
  }
  
  public void setPathPatter(String pathPatter) { this.pathPatter = pathPatter ; }
  public String getPathPatter() { return this.pathPatter ; }
  
  public String getXPath(int index) {
    int i = pathPatter.indexOf("*") ;
    StringBuilder b = new StringBuilder() ;
    b.append(pathPatter.substring(0, i)).append(Integer.toString(index)).append(pathPatter.substring(i + 1)) ;
    return b.toString() ;
  }
  
  public void setIndexStart(int indexStart) { this.indexStart = indexStart ; }
  public int getIndexStart() { return indexStart ; }
  
  public void setIndexIdentity(int identity) { this.indexIdentity = identity ; }
  public int getIndexIdentity() { return indexIdentity ; }
}
