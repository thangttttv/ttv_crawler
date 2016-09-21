package hdc.crawler.qa.model;

import java.util.Date;

public class AnswerModel extends QAModel {
  private static final long serialVersionUID = 1L;
  
  private long createDate ;
  private String questionID ;
  private String content ;
  
  public void setQuestionID(String questionID) { this.questionID = questionID ; }
  public String getQuestionID() { return questionID ; }
  
  public void setContent(String content) { this.content = content ; }
  public String getContent() { return content ; }
  
  public void setCreateDate(long createDate) { this.createDate = createDate ; }
  public Date getCreateDate() {
    return new Date(createDate);
  }
}
