package hdc.util.html;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class HttpURL {
  private String protocol ;
  private String host ;
  private String port = "80";
  private String path ;
  private String ref;

  private Set<String> query;
  
  public HttpURL(String url) {
    this(url, null);
  }
  
  public HttpURL(String url, String ignoreParam) {
    query = new HashSet<String>();
    
    String string = url.trim().toLowerCase() ;
    
    int idx = string.indexOf("://") ;
    if(idx > 0) {
      this.protocol = string.substring(0, idx + 3);
      string = string.substring(idx + 3, string.length());
    }
    
    idx = string.indexOf('/') ;
    
    String qpath = null;
    if(idx > 0) {
      qpath = string.substring(idx, string.length()) ;
      string = string.substring(0, idx) ;
    }
    
    int questionMark = string.indexOf('?') ;
    if(questionMark > 0) {
      qpath = string.substring(questionMark, string.length()) ;
      string = string.substring(0, questionMark) ;
    }

    if(string.startsWith("www")) {
      if(string.length() > 3 && string.charAt(3) != '.') {
        idx = string.indexOf('.') ;
        string = "www." + string.substring(idx + 1, string.length()) ;
      }
    } else {
      string = "www." + string ;
    }
    
    idx = string.indexOf(':') ;
    if(idx > 0) {
      this.port = string.substring(idx + 1, string.length()) ;
      this.host = string.substring(0, idx) ;
    }
    
    if(host == null) host = string ;
    
    if(qpath == null ||  qpath.length() <= 0) return;
    
    int index = qpath.indexOf('#');
    //parse ref
    ref = index < 0 ? null: qpath.substring(index + 1);
    qpath = index < 0 ? qpath: qpath.substring(0, index);
    
    index = qpath.indexOf('?');
    if (index > -1) {
      String queryValue = qpath.substring(index+1);
      
      String [] elements = queryValue.split("\\&");
      for(String element : elements) {
        if(element.length() == 0) continue;
        query.add(element);
      }
      
      if(ignoreParam != null) {
        Iterator<String> iterator = query.iterator();
        while(iterator.hasNext()) {
          String param = iterator.next();
          if(param.startsWith(ignoreParam)) iterator.remove();
        }
      }
      
      path = parse(/*url,*/ qpath.substring(0, index), '/');
    } else {
      path = parse(/*url,*/ qpath, '/');
    }
  }
  
  String parse(/*String url,*/ String value, char separator) {
    int index = 1; 
    
    while(index < value.length()) {
      int end  = value.indexOf(separator, index);
      if(end < 0) return value;
      
      if(end == index) {
        index = end+1;
        continue;
      }
      
      String pattern = value.substring(index, end);
      int newEnd = value.indexOf(pattern, end);
      if(newEnd < 0) {
        index = end+1;
        continue;
      }
      
//      try {
        pattern = value.substring(index, newEnd);
//        int time = 0;
        while(count(/*url,*/ value, pattern) >= 3 && newEnd  < value.length()) {
//          time++;
//          if(time >= 1000) {
//            LogService.getInstance().setMessage("V_URL", null, value + " and "+ pattern+" and  "+index+" and "+newEnd);
//            LogService.getInstance().setMessage("V_URL", null, "bad url: "+ url);
//            break;
//          }
          value = value.substring(0, index) + value.substring(Math.min(newEnd, value.length()));
        }
//      } catch (Exception e) {
//        LogService.getInstance().setThrowable("APPLICATION", e, " 0 : "+ index+ " : "+ newEnd + " : "+ value.length());
//        break;
//      }
      index = end+1;
    }
    
    return value;
  }
  
  public int count(/*String url,*/ String value, String pattern) {
    int count = 0;
    int start = 0;
    
//    int time = 0;
    while(start < value.length()) {
     int index = value.indexOf(pattern, start);
//     time++;
//     if(time >= 1000) {
//       LogService.getInstance().setMessage("V_URL", null, value + " and "+ pattern);
//       LogService.getInstance().setMessage("V_URL", null, "bad url2 : "+ url);
//       break;
//     }
     if(index < 0) {
//       System.out.println(" thay co counter la "+ pattern+ " : "+ count);
       return count; 
     }
     start = index + pattern.length();
     count++;
    }
//    System.out.println(" thay co counter la "+ pattern+ " : "+ count);
    return count;
  }
  
  
  public String getProtocol() { return protocol ; }
  public String getHost() { return host ; }
  public String getPort() { return port ; }
  public String getRef() { return ref; }
  
  public String getNormalizeURL() {
    StringBuilder b = new StringBuilder() ;
    b.append(this.protocol).append(this.host) ;
    
    if(this.port != null && !"80".equals(this.port)) {
      b.append(':').append(this.port) ;
    }
    
    if(path != null) b.append(path);

    Iterator<String> iterator = query.iterator();
    char ch = '?';
    while(iterator.hasNext()) {
      b.append(ch);
      b.append(iterator.next());
      ch = '&';
    }
    
//    if(ref != null) b.append('#').append(ref) ;
    return b.toString() ;
  }
  
  static public String getSource(String url) {
    int idx1  = url.indexOf("://") ;
    if(idx1 < 1) idx1 = 0 ;
    else idx1 += 3 ;
    
    int idx2  = url.indexOf('/', idx1) ;
    if(idx2 < 0) idx2 = url.indexOf('?') ;
    if(idx2 < 0) idx2 = url.length() ;
    
    String source = url.substring(idx1, idx2) ;
    source = source.toLowerCase() ;
    if(source.startsWith("www")) {
      int dotIdx = source.indexOf('.') ;
      source = source.substring(dotIdx + 1) ;
    }
    return source ;
  }
}
