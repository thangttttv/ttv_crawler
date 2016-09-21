package hdc.crawler.qa.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuestionModel extends QAModel {
  private static final long serialVersionUID = 1L;
  
  private String id ;
  private List<AnswerModel> answers ;
  private String subject ;
  private String content ;
  private String category ;
  private long createDate ;
  private int numberAnswer ;
  private String url ;
  private String urlListPage ;
  
  public void setID(String id) { this.id = id ; }
  public String getID() { return id ; }
  
  public void setSubject(String subject) { this.subject = subject ; }
  public String getSubject() { return this.subject ; }
  
  public void setContent(String content) { this.content = content ; }
  public String getContent() { return content ; }
  
  public void setCategory(String category) { this.category = category ; }
  public String getCategory() { return category ; }
  
  public void setURL(String url) { this.url = url ; }
  public String getURL() { return this.url ; }
  
  public void setURLListPage(String urlListPage) { this.urlListPage = urlListPage ; }
  public String getURLListPage() { return this.urlListPage ; }
  
  public void setNumberAnswer(int numberAnswer) { this.numberAnswer = numberAnswer ; }
  public int getNumberAnswer() { return this.numberAnswer ; }
  
  public void addAnswer(AnswerModel answer) {
    if(answers == null) answers = new ArrayList<AnswerModel>() ;
    answers.add(answer) ;
  }
  
  public List<AnswerModel> getAnswers() { return answers ; }

  public void setCreateDate(long createDate) { this.createDate = createDate ; }
  public Date getCreateDate() {
    return new Date(createDate);
  }
}
