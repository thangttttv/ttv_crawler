package hdc.crawler;

import java.sql.Connection;

import hdc.crawler.qa.model.Model;

public interface CrawlerPlugin {

  public void init(Connection con) ;
  public void process(Model model) ;
  public boolean isInitated() ;
}
