package hdc.crawler;

import hdc.crawler.fetcher.HttpClientFactory;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.crawler.fetcher.HttpClientUtil;
import hdc.util.html.A;
import hdc.util.html.HttpURL;
import hdc.util.html.URLEncoder;
import hdc.util.lang.UriID;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;

import jdbm.PrimaryHashMap;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.RecordManagerOptions;

import org.apache.http.HttpResponse;

public abstract class AbstractCrawler implements Runnable {
  private int deep = -1 ; // if deep = -1, crawl total web
  private int currentDeep = 0 ;
  
  protected Collection<Url> inject ;
  protected String dbDir ;
  protected List<DocumentAnalyzer> analyzers = new ArrayList<DocumentAnalyzer>();
  protected DocumentFilter filter ;

  protected RecordManager urlManager ;
  protected PrimaryHashMap<String, Url> urlPrimary ;

  protected RecordManager datumManager ;
  protected PrimaryHashMap<String, Datum> datumPrimary ;

  protected RecordManager injectManager ;
  protected PrimaryHashMap<String, Url> injectPrimary ;

  protected boolean isSlow = false ;
  protected boolean reFetch = false ;
  protected boolean isOpen = false ;

  public AbstractCrawler(String dbDir) throws IOException {
    this.dbDir = dbDir ;
    open() ;
    Iterator<Entry<String, Url>> iterator = injectPrimary.entrySet().iterator() ;
    List<String> holder = new ArrayList<String>();
    while(iterator.hasNext()) {
      try {
        Entry<String, Url> entry = iterator.next() ;
        if(entry.getValue().fetchNumber == 1) {
          holder.add(entry.getKey()) ;
        }
      } catch(InternalError ex) {
        ex.printStackTrace() ;
      }
    }
    for(String key : holder) {
      injectPrimary.remove(key) ;
    }
    injectManager.commit() ;
  }
  
  public synchronized void open() throws IOException {
    Properties options = new Properties();
    options.setProperty(RecordManagerOptions.DISABLE_TRANSACTIONS, "false" );
    urlManager = RecordManagerFactory.createRecordManager(dbDir + "/url", options) ;
    urlPrimary = urlManager.hashMap("Url") ;

    datumManager = RecordManagerFactory.createRecordManager(dbDir + "/datum", options) ;
    datumPrimary = datumManager.hashMap("Datum") ;

    injectManager = RecordManagerFactory.createRecordManager(dbDir + "/inject", options) ;
    injectPrimary = injectManager.hashMap("Inject") ;
    isOpen = true ;
  }
  
  public synchronized void close() throws IOException {
    urlManager.close() ;
    datumManager.close() ;
    injectManager.close() ;
    isOpen = false ;
  }
  
  public void setSlow(boolean isSlow) { this.isSlow = isSlow ; }
  public void setReProcess(boolean reFetch) { this.reFetch = reFetch ; }
  public void setDeep(int deep) { this.deep = deep ; }
  public PrimaryHashMap<String, Url> getInjectUrl() { return injectPrimary ; }

  public void addAnalyzer(DocumentAnalyzer analyzer) {
    analyzers.add(analyzer) ;
  }
  
  public void setFilter(DocumentFilter filter) {
    this.filter = filter ;
  }

  public void injectUrl(Collection<Url> urls) {
    this.inject = urls ;
  }


  public synchronized void fetch() throws Exception {
    if(!isOpen) open() ;
    System.out.println("Inject total urls: " + inject.size());
    int counter = 0 ;
    Iterator<Url> iterator = inject.iterator() ;
    while(iterator.hasNext()) {
      Url url = iterator.next() ;
      String id = new UriID(new HttpURL(url.url)).getIdAsString() ;
      
      if(filter == null) throw new RuntimeException("Document filter cannot null ...") ;
      else if(!filter.acceptFetch(url.url)) {
        url.fetchNumber = 1 ;
        urlPrimary.put(id, url) ;
        injectPrimary.put(id, url) ;
        continue ;
      }
      
      if(urlPrimary.containsKey(id) && currentDeep > 0) {
        if(urlPrimary.get(id).fetchNumber > 0) continue ;
      }
      
      if(url.url.indexOf(".jpg") != -1 || url.url.indexOf(".jpeg") != -1 || url.url.indexOf(".gif") != -1 ||
          url.url.indexOf(".tif") != -1 || url.url.indexOf(".bmp") != -1 || url.url.indexOf(".png") != -1) {
        url.fetchNumber = 1 ;
        urlPrimary.put(id, url) ;
        injectPrimary.put(id, url) ;
        continue ;
      }
      
      HttpClientImpl client = new HttpClientImpl();
      HttpResponse res = null ;
      try {
        res = client.fetch(URLEncoder.encode(url.url)) ;
        url.fetchNumber = 1 ;
      } catch (IllegalArgumentException ex) {
        if(ex.getMessage().equals("Host name may not be null")) {
          datumPrimary.remove(id) ;
          url.fetchNumber = 1 ;
          injectPrimary.put(id, url) ;
          continue ;
        } else throw new RuntimeException(ex) ;
      }

      if(isSlow) Thread.sleep(1000) ;
      System.out.println(new Date() + ": fetch url " + url.url);

      if(res.getStatusLine().getStatusCode() == 200) {
        Url urlFetch = new Url() ;
        urlFetch.id =  id ;
        urlFetch.url = url.url ;
        urlFetch.fetchNumber = 1 ;
        urlPrimary.put(urlFetch.id, urlFetch) ;
        System.out.println(new Date() + ": put url   " + url.url);

        Datum dataFetch = new Datum() ;
        dataFetch.id = urlFetch.id ;
        dataFetch.url = urlFetch.url ;
        dataFetch.data = HttpClientUtil.getResponseBody(res) ;
        dataFetch.processed = false ;
        datumPrimary.put(dataFetch.id, dataFetch) ;
        System.out.println(new Date() + ": put datum " + url.url);
        injectPrimary.put(id, url) ;
        counter++ ;
        if(counter > 0 && counter % 10 == 0) {
          urlManager.commit() ;
          datumManager.commit() ;
          injectManager.commit() ;
        }
      } else if(res.getStatusLine().getStatusCode() == 404) {
        datumPrimary.remove(id) ;
        injectPrimary.put(id, url) ;
        System.out.println("Link 404: " + url.url + " using client-hashcode: " + client.getDefaultHttpClient().hashCode());
        HttpClientFactory.reload() ;
        continue ;
      }
    }
    
    injectManager.commit() ;
    urlManager.commit() ;
    datumManager.commit() ;

    try {
      processDatum() ;
    } catch(Throwable ex) {
      throw new RuntimeException(ex) ;
    }
  }

  public synchronized void processDatum() throws Exception {
    if(currentDeep == deep) {
      close() ;
      currentDeep = 0 ;
      return ;
    }
    Iterator<Url> i = urlPrimary.values().iterator() ;
    int count = 0;
    while(i.hasNext()) {
      Url url = i.next() ;
      if(filter != null && !filter.acceptProcess(url.url)) continue ;
      if(url.url.endsWith(".jpg") || url.url.endsWith(".jpeg") || url.url.endsWith(".gif")
          || url.url.endsWith(".tif") || url.url.endsWith(".bmp")) continue ;
      
      Datum dataFetch = datumPrimary.get(url.id) ;
      if(dataFetch == null) {
        datumPrimary.remove(url.id) ;
        datumManager.commit() ;
        continue ;
      }
      if(!reFetch && dataFetch.processed) continue ;
      
      System.out.println(new Date() + " process " + dataFetch.url) ;
      
      if(analyzers == null || analyzers.size() == 0) throw new RuntimeException("Document Analyzer require one .... ") ;
      for(DocumentAnalyzer analyzer : analyzers) {
        List<A> list = analyzer.analyze(dataFetch.data) ;
        if(list == null) continue ;
        for(int j = 0; j < list.size(); j++) {
          A a = list.get(j) ;
          if(filter != null && !filter.acceptCollect(a.getURL())) continue ;
          else if(urlPrimary.containsKey(new UriID(new HttpURL(a.getURL())).getIdAsString())) continue ;
          else if(datumPrimary.containsKey(new UriID(new HttpURL(a.getURL())).getIdAsString())) continue ;

          Url injectUrl = new Url() ;
          injectUrl.id = new UriID(new HttpURL(a.getURL())).getIdAsString() ;
          injectUrl.url = a.getURL() ;
          injectPrimary.put(injectUrl.id, injectUrl) ;
        }
        dataFetch.processed = true ;
        datumPrimary.put(dataFetch.id, dataFetch) ;
        count++ ;
        if(count > 0 && count % 10 == 0) {
          injectManager.commit() ;
          datumManager.commit() ;
        }
        
        System.out.println(new Date() + " collect total urls: " + injectPrimary.size());
      }
    }
    
    if(injectPrimary.size() == 0) {
      close() ;
      currentDeep = 0 ;
      return ;
    }
    injectUrl(injectPrimary.values()) ;
    currentDeep ++ ;
    fetch() ;
  }

  public void run() {
    try {
      fetch() ;
    } catch (Exception e) {
      throw new RuntimeException(e) ;
    }
  }

  public static class Url implements Serializable {
    private static final long serialVersionUID = 1L;

    public String id ;
    public String url ;
    public int cat_id;
    public int fetchNumber ;
    public int fetchFrom ;
    public int fetchTo ;
    public String pagePara;
    public int collection=1;
    public String regex;
    public String url_1;
    public String url_2;
    public String title;
    public String avatar;
    public String lead;
    public String create_date;
  }

  public static class Datum implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public String id ;
    public String url ;
    public String data ;
    public int cat_id;
    public String date ="";
    public boolean processed = false;
  }
}