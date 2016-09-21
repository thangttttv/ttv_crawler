package hdc.util.html;

import hdc.util.text.StringUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.az24.crawler.model.ImageConfig;
import com.az24.util.UTF8Tool;

public class SelectImgArticleVisitor extends NodeVisitor {
	String pre_folder ="";
	public String title="";
	public String date="";
	public String urlDomain ="";
	public List<ImageConfig> imageList = new ArrayList<ImageConfig>();
	
	public SelectImgArticleVisitor(String pre_folder,String title) {
		super();
		this.pre_folder = pre_folder;
		this.title = title;
	}

	public SelectImgArticleVisitor() {
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
			strMonth = intmonth < 10 ? "0" + intmonth: "" + intmonth;
			strDay = intday < 10 ? "0" + intday	: "" + intday;
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
			//img.setAttribute("alt", title+" "+  src.substring(src.lastIndexOf("/")+1));
			
			String  alias = UTF8Tool.coDau2KoDau(title).trim();						
			String pattern = "\\W";				   
		    Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(alias);
			alias = m.replaceAll("_");
			String imgalias="";
			//System.out.println(alias);
			//System.out.println(alias.substring(0,alias.length()-1));
			
			if(alias.length()<=15) imgalias = alias.substring(0,alias.length()-1); 
			else imgalias = alias.substring(0,14); 
			if (src != null && !src.isEmpty()) {
				src = src.substring(src.lastIndexOf("/")+1);
				r = Pattern.compile("\\s+");
				m = r.matcher(src);
				src = m.replaceAll("_");
				String name = "";
				String duoi = "";
				if(!(src.indexOf(".")==-1))
				{
				 name = src.substring(0,src.lastIndexOf("."));
				 duoi = src.substring(src.lastIndexOf(".")+1,src.lastIndexOf(".")+4);
				}else{
					name = Calendar.getInstance().getTimeInMillis()+"";
					duoi = "jpg";
				}
				src = name+"."+duoi;
				r = Pattern.compile("jpg|png|gif|bmp");
				m = r.matcher(duoi);
				int stt=0;
				if(this.imageList!=null)
				 stt = this.imageList.size();
				try {
					Thread.sleep(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(!m.find())src = System.currentTimeMillis()+"_stt"+ stt+ ".jpg";
				//change node			
				r = Pattern.compile("http");
				m = r.matcher(img_src);
				if(!m.find()) img_src =this.urlDomain+ "/"+img_src;
				
				String image_name = imgalias+src;
				r = Pattern.compile("%20");
				m = r.matcher(image_name);
				image_name = m.replaceAll("_");
				
				node.getAttributes().getNamedItem("src").setTextContent(pre_folder+year+"/"+strMonth+strDay+"/"+image_name);	
				putImage("",image_name,img_src,strDay,strMonth,year);
				
			}
		}
	}

	public void postTraverse(Node node) {
	}

	protected boolean select(Element a) {
		return true;
	}
	
	private void putImage(String idUrl,String image_name,String src,String day,String month,String year)
	{
		ImageConfig imageConfig = new ImageConfig();						
		imageConfig.id = idUrl;
		imageConfig.src = src;
		imageConfig.name =image_name;		
		imageConfig.dateProcess = day + "/" + month + "/"+ year;		
		imageList.add(imageConfig);
	}
}
