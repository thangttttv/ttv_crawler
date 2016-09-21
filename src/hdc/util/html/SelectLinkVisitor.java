package hdc.util.html;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class SelectLinkVisitor extends NodeVisitor {
  private String pattern = null ;
  private List<A> linkHolder = new ArrayList<A>();
  
  public SelectLinkVisitor(String pattern) {
    super() ;
    this.pattern = pattern ;
  }
  
  public SelectLinkVisitor() { super() ; }

  public void preTraverse(Node node) {
    String tag = node.getNodeName() ;
    String img = "";
    if(node instanceof Element && "a".equalsIgnoreCase(tag)) {
      Element a = (Element) node ;
      if(!select(a)) return ;
      String href = a.getAttribute("href") ;
      
      if(node.getChildNodes()!=null)
      {	int i = 0;
      	while(i<node.getChildNodes().getLength())
      	{
      		Node nodeImage = node.getChildNodes().item(i);
      		if("img".equalsIgnoreCase(nodeImage.getNodeName()))
      		{
      			img = nodeImage.getAttributes().getNamedItem("src").getTextContent();
      			
      		}
      		i++;
      	}
      }
      if(href != null && !href.isEmpty()) {
        if(pattern != null) {
          if(href.startsWith(pattern)) linkHolder.add(new A(a.getTextContent(), href, null,img)) ;
          return ;
        }
        
        linkHolder.add(new A(a.getTextContent(), href, null,img)) ;
      }
    }
  }

  public void postTraverse(Node node) {
  }

  protected boolean select(Element a) { return true ; }

  public List<A> getLinks() { return linkHolder ; }
}
