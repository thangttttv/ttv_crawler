package hdc.util.lang;

import hdc.util.html.HttpURL;

import java.util.Comparator;

public class UriID {
  final static public int DATA_LENGTH = MD5.DATA_LENGTH  + 4 + 8; 
  final static public Comparator<UriID> COMPARATOR = new UriIDComparator() ;

  private int hostHashCode = 0 ;
  private MD5 md5Hash ;

  public UriID() {
  }
  
  public UriID(HttpURL url) {
    this.hostHashCode = url.getHost().hashCode() ;
    this.md5Hash = MD5.digest(url.getNormalizeURL()) ;
    if(hostHashCode == 0) {
      System.out.println("url: " + url.getNormalizeURL() + " produce hash code " + this.hostHashCode);
    }
  }
  
  public UriID(int hostHashCode, MD5 md5hash) {
    this.hostHashCode = hostHashCode ;
    this.md5Hash = md5hash ;
  }
  
  public UriID(String idExp) {
    int idx = idExp.indexOf(':') ;
    this.hostHashCode = Integer.parseInt(idExp.substring(0, idx)) ;
    this.md5Hash = new MD5(idExp.substring(idx + 1)) ;
  }
  
  
  public int getHostHashCode() { return hostHashCode ; }

  public MD5 getMD5() { return md5Hash ; }

  public int compareTo(UriID other) {
    if(this.hostHashCode < other.hostHashCode) return -1 ;
    if(this.hostHashCode > other.hostHashCode) return 1 ;
    return this.md5Hash.compareTo(other.md5Hash) ;
  }
  
  public String getIdAsString() {
    StringBuilder b = new StringBuilder() ;
    b.append(Integer.toHexString(this.hostHashCode)).append(":").append(md5Hash.toString());
    return b.toString() ;
  }

  public String toString() {
    StringBuilder b = new StringBuilder() ;
    b.append(Integer.toString(this.hostHashCode)).append(":").append(md5Hash.toString());
    return b.toString() ;
  }

  public static class UriIDComparator implements Comparator<UriID> {
    public int compare(UriID id0, UriID id1) { return id0.compareTo(id1) ; }
  }
}