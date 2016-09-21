package hdc.util.html;

import hdc.util.html.parser.DomWriter;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class SelectOjbectVisitor extends NodeVisitor {
  private String pattern = null ;
  private List<ObjectVideo> linkHolder = new ArrayList<ObjectVideo>();
  
  public SelectOjbectVisitor(String pattern) {
    super() ;
    this.pattern = pattern ;
  }
  
  public SelectOjbectVisitor() { super() ; }

  public void preTraverse(Node node) {
    String tag = node.getNodeName() ;
    String width="";
    String height="";
    if(node instanceof Element && "object".equalsIgnoreCase(tag)) {
      Element object = (Element) node ;
      if(!select(object)) return ;
      	String object_code = "";
      	String object_source ="";
		try {
			DomWriter writer = new DomWriter();
			object_code=writer.toXMLString(node);
			object_code =object_code.substring("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
					.length());
			width = node.getAttributes().getNamedItem("width")!=null?node.getAttributes().getNamedItem("width").getTextContent():"0";
			height = node.getAttributes().getNamedItem("height")!=null?node.getAttributes().getNamedItem("height").getTextContent():"0";
		} catch (Exception e) {
		
			e.printStackTrace();
		}
		if(node.hasChildNodes())
		{
			int length = node.getChildNodes().getLength();
			int i = 0;
			while(i<length)
			{
				Node subnode =  node.getChildNodes().item(i);
				if(subnode.hasAttributes())
				{
					if(subnode.getAttributes().getNamedItem("name")!=null)
					{	
						if(subnode.getAttributes().getNamedItem("name").getTextContent().equalsIgnoreCase("Movie"))
						object_source = subnode.getAttributes().getNamedItem("value").getTextContent();
					}
				}
				i++;
			}
			
		}
        linkHolder.add(new ObjectVideo("","",object_code,object_source,Integer.parseInt(width),Integer.parseInt(height),0,"","")) ;
      }    
  }

  public void postTraverse(Node node) {
  }

  protected boolean select(Element a) { return true ; }

  public List<ObjectVideo> getLinks() { return linkHolder ; }
}
