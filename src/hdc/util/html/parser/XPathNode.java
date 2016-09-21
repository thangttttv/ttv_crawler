package hdc.util.html.parser;

import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Pattern;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XPathNode {
  static Pattern JUNK_CHAR = Pattern.compile("\\s+|\\n|\\t") ;

  private String xpath ;
  private Node   node  ;
  private String normalizedText ;

  public XPathNode(String xpath, Node node) {
    this.xpath = xpath ;
    this.node = node ;
  }

  public XPathNode(Node node) {
    this.xpath = getXPath(node) ;
    this.node = node ;
    this.normalizedText = normalize(node.getTextContent()) ;
  }

  public String getXPath() { return this.xpath ; }

  public Node getNode() { return this.node ; }

  public String getNodeName() { return this.node.getNodeName() ; }

  public String getNormalizedText() { return this.normalizedText ;}

  static public String normalize(String text) {
    if(text == null) return null ;
    text = text.trim() ;
    char[] buf = text.toCharArray() ;
    char preChar = 0 ;
    StringBuilder b = new StringBuilder() ;
    for(int i = 0; i < buf.length; i++) {
      char c = buf[i] ;
      if(c == '\t') c = ' ' ;
      else if(c == '\n') c = ' ' ;
      else if(c == '\r') c = ' ' ;
      else if(c == 160) c = ' ' ;

      if(preChar == ' ' && c == ' ') {
      } else {
        b.append(c) ;
      }
      preChar = c ;
    }
    return b.toString().trim() ;
  }

  //  final static public String getXPath(Node node) {
  //    if (null == node) return null;
  //    ArrayList<Node> hierarchy = new ArrayList<Node>(50);
  //    while (node != null && node.getNodeType() != Node.DOCUMENT_NODE) {
  //      hierarchy.add(node);
  //      node = node.getParentNode();
  //    }
  //
  //    StringBuilder b = new StringBuilder();
  //    for(int i = hierarchy.size() - 1; i >= 0; i--) {
  //      node = hierarchy.get(i) ;
  //      int index = 0 ;
  //      int nodeType = node.getNodeType() ;
  //      String nodeName = node.getNodeName() ;
  //      NodeList list = node.getParentNode().getChildNodes() ;
  //      for(int j = 0; j < list.getLength(); j++) {
  //        Node prevSibling = list.item(j);
  //        if(prevSibling == node) break ;
  //        if (prevSibling.getNodeType() == nodeType) {
  //          if (prevSibling.getNodeName().equals(nodeName)) {
  //            index++;
  //          }
  //        }
  //        index++ ;
  //      }
  //      b.append('/').append(node.getNodeName()).append('[').append(index).append(']');
  //    }
  //    return b.toString() ;
  //  }
  final static public String getXPath(Node node) {
    if (null == node) return null;

    // declarations
    Node parent = null;
    Stack<Node> hierarchy = new Stack<Node>();
    StringBuffer buffer = new StringBuffer();

    // push element on stack
    hierarchy.push(node);

    parent = node.getParentNode();
    while (null != parent && parent.getNodeType() != Node.DOCUMENT_NODE) {
      // push on stack
      hierarchy.push(parent);

      // get parent of parent
      parent = parent.getParentNode();
    }

    // construct xpath
    Object obj = null;
    while (!hierarchy.isEmpty() && null != (obj = hierarchy.pop())) {
      Node n = (Node) obj;
      boolean handled = false;

      // only consider elements
      if (n.getNodeType() == Node.ELEMENT_NODE) {
        Element e = (Element) n;

        // is this the root element?
        if (buffer.length() == 0) {
          // root element - simply append element name
          buffer.append(n.getNodeName());
        } else {
          // child element - append slash and element name
          buffer.append("/");
          buffer.append(n.getNodeName());

          if (n.hasAttributes()) {
            // see if the element has a name or id attribute
            if (e.hasAttribute("id")) {
              // id attribute found - use that
              buffer.append("[@id='" + e.getAttribute("id") + "']");
              handled = true;
            } else if (e.hasAttribute("name")) {
              // name attribute found - use that
              buffer.append("[@name='" + e.getAttribute("name") + "']");
              handled = true;
            }
          }

          if (!handled) {
            // no known attribute we could use - get sibling index
            int prev_siblings = 1;
            Node prev_sibling = n.getPreviousSibling();
            while (null != prev_sibling) {
              if (prev_sibling.getNodeType() == n.getNodeType()) {
                if (prev_sibling.getNodeName().equalsIgnoreCase(n.getNodeName())) {
                  prev_siblings++;
                }
              }
              prev_sibling = prev_sibling.getPreviousSibling();
            }
            buffer.append("[" + prev_siblings + "]");
          }
        }
      }
    }

    // return buffer
    return buffer.toString();

  }

  //  final static public String getXPath(Node node) {
  //    if (null == node) return null;
  //    ArrayList<Node> hierarchy = new ArrayList<Node>(50);
  //    while (node != null && node.getNodeType() != Node.DOCUMENT_NODE) {
  //      hierarchy.add(node);
  //      node = node.getParentNode();
  //    }
  //
  //    StringBuilder b = new StringBuilder();
  //    for(int i = hierarchy.size() - 1; i >= 0; i--) {
  //      node = hierarchy.get(i) ;
  //      int index = 0 ;
  //      Node prevSibling = node.getPreviousSibling();
  //      int nodeType = node.getNodeType() ;
  //      String nodeName = node.getNodeName() ;
  //      while(prevSibling != null) {
  //        if (prevSibling.getNodeType() == nodeType) {
  //          if (prevSibling.getNodeName().equals(nodeName)) {
  //            if(nodeName.equals("form")) {
  //              System.out.println(prevSibling.getNodeName() + " " + index);
  //            }
  //            index++;
  //          }
  //        }
  //        index++ ;
  //        prevSibling = prevSibling.getPreviousSibling();
  //      }
  //      b.append('/').append(node.getNodeName()).append('[').append(index).append(']');
  //    }
  //    return b.toString() ;
  //  }
}
