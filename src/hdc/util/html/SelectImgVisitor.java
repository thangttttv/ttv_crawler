package hdc.util.html;

import hdc.util.text.StringUtil;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.az24.crawler.article.DownloadImage;
import com.az24.crawler.config.UrlInjectXmlConfig;
import com.az24.util.UTF8Tool;

public class SelectImgVisitor extends NodeVisitor {
	String pre_folder ="";
	public String title="";
	public String date="";
	public SelectImgVisitor(String pre_folder,String title) {
		super();
		this.pre_folder = pre_folder;
		this.title = title;
	}

	public SelectImgVisitor() {
		super();
	}

	public void preTraverse(Node node) {
		Calendar calendar = Calendar.getInstance();
		String strMonth;
		String strDay;String year;
		if(StringUtil.isEmpty(date))
		{
			year = calendar.get(Calendar.YEAR)+"";
			int month = calendar.get(Calendar.MONTH)+1;
			strMonth = month<10?"0"+month:month+"";
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			strDay = day<10?"0"+day:day+"";
		}else
		{
			String dates[] = date.split("/");			
			int intmonth = Integer.parseInt(dates[1]);
			int intday = Integer.parseInt(dates[0]);
			strMonth = intmonth < 10 ? "0" + intmonth
					: "" + intmonth;
			strDay = intday < 10 ? "0" + intday
					: "" + intday;
			year = dates[2];
		}
		String tag = node.getNodeName();
		if(StringUtil.isEmpty(title))  return;
		if (node instanceof Element && "img".equalsIgnoreCase(tag)) {
			Element img = (Element) node;
			if (!select(img))
				return;
			String src = img.getAttribute("src");
			String img_src = src;
			img.setAttribute("alt", title+" "+  src.substring(src.lastIndexOf("/")+1));
			
			String  alias = UTF8Tool.coDau2KoDau(title).trim();						
			String pattern = "\\W";				   
		    Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(alias);
			alias = m.replaceAll("_");
			String imgalias="";
			System.out.println(alias);
			System.out.println(alias.substring(0,alias.length()-1));
			if(alias.length()<=15) imgalias = alias.substring(0,alias.length()-1); 
			else imgalias = alias.substring(0,14); 
			if (src != null && !src.isEmpty()) {
				src = src.substring(src.lastIndexOf("/")+1);
				String arrSrc[] = src.split("\\.");
				r = Pattern.compile(pattern);
				m = r.matcher(arrSrc[0]);
				arrSrc[0] = m.replaceAll("_");
				src = arrSrc[0]+"."+arrSrc[1].substring(0,3);
							
				node.getAttributes().getNamedItem("src").setTextContent(pre_folder+year+"/"+strMonth+strDay+"/"+imgalias+src);				
				DownloadImage downloadImage = new DownloadImage(img_src,imgalias+src,UrlInjectXmlConfig.baseUrl,date,0,1,null);
				downloadImage.run();
				
			}
		}
	}

	public void postTraverse(Node node) {
	}

	protected boolean select(Element a) {
		return true;
	}

}
