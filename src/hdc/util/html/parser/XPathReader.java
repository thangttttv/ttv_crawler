package hdc.util.html.parser;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

public class XPathReader {

  private Document doc ;
  private XPath xpath ;
  
  public XPathReader(Document doc) {
    this.doc = doc ;
    this.xpath = XPathFactory.newInstance().newXPath() ;
  }
  
  public Document getDocument() { return doc ; }
  
  public Object read(String expression, QName returnType) throws XPathExpressionException {
    XPathExpression xPathExpression = xpath.compile(expression) ;
    return xPathExpression.evaluate(doc, returnType) ;
  }
}
