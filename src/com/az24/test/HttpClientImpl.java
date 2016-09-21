package com.az24.test;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpClientImpl {
  private DefaultHttpClient client ;
  
  synchronized public HttpResponse fetch(String uri) throws Exception {
    HttpGet get = new HttpGet(uri) ;
    if(client == null) client = HttpClientFactory.getInstance() ;
    return client.execute(get) ;
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
