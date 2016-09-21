package hdc.util.html;

import org.w3c.dom.Node;

abstract public class NodeDeleteVisitor extends NodeVisitor {
  public void preTraverse(Node node) {}
  
  public void postTraverse(Node node) {}
  
  final public void traverse(Node node) {
	if(node==null)return; 
    Node child = node.getFirstChild();
    Node nextChild = null ;
    while (child != null) {
      nextChild = child.getNextSibling() ;
      if(shouldDelete(child)) {
        Node parent = child.getParentNode() ;
        parent.removeChild(child) ;
      } else {
        traverse(child);
      }
      child = nextChild ;
    }
  }
  
  abstract protected boolean shouldDelete(Node node) ;
}