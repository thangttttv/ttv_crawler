package hdc.util.html;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class HTMLDomUtil {
  static XPathFactory factory = XPathFactory.newInstance();
  static XPath xpath = factory.newXPath();

  static public String getTitle(Document doc)  {
    Node title = findFirstNodeByTagName(doc, "title") ;
    if(title != null) return title.getTextContent();
    return null ;
  }

  static public String getBase(Document doc)  {
    Node base = findFirstNodeByTagName(doc, "base") ;
    if(base == null) return null ;
    Element e = (Element) base ;
    return e.getAttribute("href") ;
  }

  static public void createBase(Document doc, String url)  {
    URLNormalizer urlnorm = new URLNormalizer(url) ;
    Element baseEle = doc.createElement("base") ;
    baseEle.setAttribute("href", urlnorm.getBaseURL()) ;
    Element head = (Element)findFirstNodeByTagName(doc, "head") ;
    Node firstNode = head.getFirstChild() ;
    if(firstNode.getNodeType() != Node.ELEMENT_NODE) {
      head.appendChild(baseEle) ;
    } else {
      head.insertBefore(firstNode, baseEle) ;
    }
    //head.appendChild(baseEle) ;
  }

  static private Node findFirstNodeByTagName(Node node, String tag) {
    if(node == null) return null ;
    if(tag.equalsIgnoreCase(node.getNodeName())) return node ;
    NodeList children = node.getChildNodes() ;
    for(int i = 0; i < children.getLength(); i++) {
      Node child = children.item(i) ;
      Node ret = findFirstNodeByTagName(child, tag) ;
      if(ret != null) return ret ;
    }
    return null ;
  }
}

