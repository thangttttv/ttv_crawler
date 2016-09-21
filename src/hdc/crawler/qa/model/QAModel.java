package hdc.crawler.qa.model;

import java.io.Serializable;
import java.util.Date;

public abstract class QAModel implements Model, Serializable {
  private static final long serialVersionUID = 1L;

  abstract public Date getCreateDate() ;
}
