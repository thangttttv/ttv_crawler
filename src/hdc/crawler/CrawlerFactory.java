package hdc.crawler;

import hdc.crawler.AbstractCrawler.Url;
import hdc.util.html.HttpURL;
import hdc.util.lang.UriID;

import java.util.ArrayList;
import java.util.Collection;

public class CrawlerFactory {
  final public static CrawlerFactory factory = new CrawlerFactory() ;

  private CrawlerFactory() {
    
  }
  
  final public static CrawlerFactory getInstance() { return factory ; }
  
  public AbstractCrawler createCrawler(String dbDir, String injectUrl, String baseUrl, String rewriteUrl) throws Exception {
    AbstractCrawler crawler = new AbstractCrawler(dbDir) {};
    Collection<Url> inject = null ;
    
    if(crawler.getInjectUrl().size() == 0) {
      inject = new ArrayList<Url>(1) ;
      Url url = new Url() ;
      url.id = new UriID(new HttpURL(injectUrl)).getIdAsString() ;
      url.url = injectUrl ;
      inject.add(url) ;
    } else  inject = crawler.getInjectUrl().values() ;
    
    crawler.injectUrl(inject) ;
    DocumentAnalyzer analyzer = new DocumentAnalyzer.DefaultDocumentAnayzer(baseUrl, rewriteUrl) ;
    crawler.addAnalyzer(analyzer) ;
    return crawler ;
  }
}
