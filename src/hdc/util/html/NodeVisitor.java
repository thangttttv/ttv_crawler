package hdc.util.html;

import org.w3c.dom.Node;

public abstract class NodeVisitor {

  public void traverse(Node node) {
	if(node==null)return;
    preTraverse(node) ;
    Node child = node.getFirstChild();
    while (child != null) {
      traverse(child);
      child = child.getNextSibling();
    }
    postTraverse(node) ;
  }
  
  abstract public void preTraverse(Node node) ;
  abstract public void postTraverse(Node node) ;
}
