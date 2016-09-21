package hdc.crawler.qa.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ConfidenceModel implements Model, Serializable {

  private static final long serialVersionUID = 1L;
  
  private String id ;
  private String url ;
  private QuestionModel question ;
  private List<AnswerModel> answers ;
  private boolean isImportDB = false;
  
  public void setID(String id) { this.id = id ; }
  public String getID() { return id ; }
  
  public void setURL(String url) { this.url = url ; }
  public String getURL() { return url ; }
  
  public void setQuestion(QuestionModel question) { this.question = question ; }
  public QuestionModel getQuestion() { return question ; }
  
  public void setAnswer(List<AnswerModel> answers) { this.answers = answers ; }
  public List<AnswerModel> getAnswers() { return answers ; }
  public void addAnswer(AnswerModel answer) {
    if(answers == null) answers = new ArrayList<AnswerModel>() ;
    answers.add(answer) ;
  }
  
  public boolean isImportDB() { return isImportDB ; }
  public void setImportDB(boolean isImportDB) { this.isImportDB = isImportDB ; }
}
