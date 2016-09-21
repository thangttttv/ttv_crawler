package hdc.util.html;

import org.w3c.dom.Node;

abstract public class NodeNormalizeVisitor extends NodeVisitor {

  public void preTraverse(Node node) {
    normalize(node) ;
  }
  
  public void postTraverse(Node node) {
    
  }
  
  abstract protected void normalize(Node node) ;
}
