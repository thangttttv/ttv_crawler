package hdc.util.html;

import java.io.Serializable;

public class ObjectVideo implements Serializable {
	public String title;
	public String alias;
	public String embed_code;
	public String embed_source;
	public int width;
	public int height;
	public int cat_id;
	public int article_id;
	public int length_time;
	public String picture="";
	public String tags="";
	public ObjectVideo(String title,String alias,String embed_code,String embed_source,int width,int height,int length_time,String picture,String tags)
	{
		this.title = title;
		this.alias = alias;
		this.embed_code = embed_code;
		this.embed_source = embed_source;
		this.length_time = length_time;
		this.picture = picture;
		this.width = width;
		this.height = height;
		this.tags = tags;
	}
}
