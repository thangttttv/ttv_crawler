package hdc.util.html.parser;

import hdc.util.html.NodeVisitor;

import java.util.HashSet;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class XPathNodeWriter extends NodeVisitor {
  private StringBuilder b ;
  private int level = 0;
  private HashSet<String> writeTag ;

  public XPathNodeWriter() {
    b = new StringBuilder() ;
  }

  public XPathNodeWriter(String[] tag) {
    b = new StringBuilder() ;
    writeTag = new HashSet<String>() ;
    for(int i = 0; i < tag.length; i++) {
      writeTag.add(tag[i].toUpperCase()) ;
    }
  }

  public String getString() { return this.b.toString() ; }

  public void preTraverse(Node node) {
    if(writeTag != null) {
      if(writeTag.contains(node.getNodeName())) {
        b.append("<").append(node.getNodeName()); 
        NamedNodeMap map = node.getAttributes() ;
        for(int i = 0; i < map.getLength(); i++) {
          Node attr = map.item(i) ;
          b.append(" ").append(attr.getNodeName()).append("=\"").append(attr.getNodeValue()).append("\"") ;
        }
        b.append(">") ;
      }
    }
    if(node.getNodeValue() != null) {
      b.append(node.getNodeValue()) ;
    }
    level++ ;
  }

  public void postTraverse(Node node) {
    level-- ;
    if(writeTag == null) return ;
    if(writeTag.contains(node.getNodeName())) {
      b.append("</").append(node.getNodeName()).append(">") ;
    } 
    b.append("\n") ;
  }

  public void reset() { b = new StringBuilder(); }

  public String write(XPathNode[] node) {
    for(int i = 0; i < node.length; i++) {
      Node selNode = node[i].getNode() ;
      if(selNode instanceof Text) selNode = selNode.getParentNode() ;
      if(i > 0) b.append("\n") ;
      traverse(selNode) ;
    }
    String string = b.toString().trim() ;
    reset() ;
    return string ; 
  }
}