package hdc.crawler.qa.model;

import java.io.Serializable;

public class URLFetchModel implements Serializable {

  private static final long serialVersionUID = 1L;
  
  private String id ;
  private String url ;
  private int fetch_number ;
  private int error_code ;
  private String error_stacktrace ;
  private String attributes ;
  private String hostname ;
  
  public URLFetchModel() {}
  
  public void setID(String id) { this.id = id ; }
  public String getID() { return id ; }
  
  public void setURL(String url) { this.url = url ; }
  public String getURL() { return url ; }
  
  public void setFetchNumber(int fetch_number) { this.fetch_number = fetch_number ; }
  public int getFetchNumber() { return fetch_number ; }
  
  public void setErrorCode(int error_code) { this.error_code = error_code ; }
  public int getErrorCode() { return error_code ; }
  
  public void setErrorStackTrace(String error_stacktrace) { this.error_stacktrace = error_stacktrace ; }
  public String getErrorStackTrace() { return error_stacktrace ; }
  
  public void setAttributes(String attributes) { this.attributes = attributes ; }
  public String getAttributes() { return attributes ; }
  
  public void setHostname(String hostname) { this.hostname = hostname ; }
  public String getHostname() { return hostname ; }
}
