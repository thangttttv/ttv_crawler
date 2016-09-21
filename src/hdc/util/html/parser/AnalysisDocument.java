package hdc.util.html.parser;

import hdc.util.html.HTMLDomUtil;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

public class AnalysisDocument {
  private Document document ;
  private String   documentTitle ;
  private List<XPathNode> candidateXpath ;

  public AnalysisDocument(String html) throws Exception {
    this(createDOMParser(), html) ;
  }

  public AnalysisDocument(DOMParser parser, String html) throws Exception {
    if (html == null || html.isEmpty()) throw new Exception("no data to parse");
    this.candidateXpath = new ArrayList<XPathNode>() ;
    parser.parse(new InputSource(new StringReader(html)));
    this.document = parser.getDocument() ;
    findCandidateXPath(document) ;
  }

  public AnalysisDocument(Document doc) throws Exception {
    this.candidateXpath = new ArrayList<XPathNode>() ;
    this.document = doc ;
    findCandidateXPath(document) ;
  }

  public Document getDocument() { return document ; }

  public String getDocumentTitle() { 
    if(documentTitle == null) {
      this.documentTitle = HTMLDomUtil.getTitle(document);
      this.documentTitle = XPathNode.normalize(this.documentTitle) ;
    }
    return this.documentTitle; 
  }

  public List<XPathNode> getCandidateXPath() { return this.candidateXpath ; }

  final private void findCandidateXPath(Node node) {
    ArrayList<Node> stack = new ArrayList<Node>(10000) ;
    while(node != null) {
      if (node.getNodeName().equals("#text")) {
        if (node.getParentNode() != null) {
          if (isContentNode(node.getParentNode().getNodeName())) {
            if(node.getTextContent().trim().length() > 0 ) {
              XPathNode xpathNode = new XPathNode(node) ;
              candidateXpath.add(xpathNode) ;
            }
          }
        }
      } else if (node.getNodeName().equals("img")) {
        candidateXpath.add(new XPathNode(node)) ;
      } else {
        Node child = node.getLastChild() ;
        while(child != null) {
          if(child.getNodeType() == Node.ELEMENT_NODE || child.getNodeType() == Node.TEXT_NODE) {
            stack.add(child) ;
          }
          child = child.getPreviousSibling() ;
        }
      }
      int size = stack.size() ;
      if(size == 0) node = null ;
      else node = stack.remove(size - 1) ;
    }
  }

  static String[] CONTENT_NODE = { 
    "div", "p", "h", "h1", "h2", "h3", "h4", "h5", "h6", "span", "b", "strong", 
    "em", "td", "li", "ol", "ul", "u", "i", "a", "font", "pre", "blockquote"
  } ;

  static boolean isContentNode(String nodeName) {
    for (String sel : CONTENT_NODE) {
      if (nodeName.equalsIgnoreCase(sel)) return true;
    }
    return false;
  }

  static public DOMParser createDOMParser() throws Exception {
    DOMParser parser = new DOMParser();
//    parser.setFeature("http://cyberneko.org/html/features/augmentations", true);
    parser.setProperty("http://cyberneko.org/html/properties/names/elems", "lower");
    return parser ;
  }
}