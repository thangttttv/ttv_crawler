package hdc.crawler.fetcher;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpClientImpl {
  private DefaultHttpClient client ;
  
  synchronized public HttpResponse fetch(String uri) {
    HttpGet get = new HttpGet(uri) ;
    if(client == null) client = HttpClientFactory.getInstance() ;
    try {
		return client.execute(get) ;
	} catch (ClientProtocolException e) {	
		//e.printStackTrace();
		return null;
	} catch (IOException e) {		
		//e.printStackTrace();
		return null;
	}	
  }
  
  synchronized public HttpResponse fetch(String uri,List<NameValuePair> list) throws Exception {
	    HttpPost post = new HttpPost(uri) ;
	    if(client == null) client = HttpClientFactory.getInstance() ;
	    post.setEntity(new UrlEncodedFormEntity(list)) ;
        HttpResponse res = client.execute(post) ;
	    return res;
	  }
	  
  
  public DefaultHttpClient getDefaultHttpClient() { return client ; }
  
  public void setCookies(Cookie[] cookies) throws Exception {
    if(client == null) client = HttpClientFactory.getInstance() ;
    for(Cookie cookie : cookies) client.getCookieStore().addCookie(cookie) ;
  }
  
  public void setProxy(String proxy, int port) {
    if(client == null) client = HttpClientFactory.getInstance() ;
    HttpHost host = new HttpHost(proxy, port, "http") ;
    client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, host) ;
    ConnRouteParams.setDefaultProxy(client.getParams(), host) ;
//    ProxySelectorRoutePlanner planner = new ProxySelectorRoutePlanner(client.getConnectionManager().getSchemeRegistry(), ProxySelector.getDefault()) ;
//    client.setRoutePlanner(planner) ;
  }
}
