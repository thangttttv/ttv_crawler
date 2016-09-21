package com.az24.crawler.model;

import java.io.Serializable;

public class ImageConfig implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String column="";
	public String name="";
	public String name_n="";
	public String xpath="";
	public String xpath_sub="";
	public String regex="";
	public String src="";
	public String src_small="";
	public String src_medium="";
	public String src_big="";
	public String dateProcess="";
	public String id="";
	public String node_type="";
	public String url_content="";
	public int is_main=0;
	
	
	public  String getPath(String prefixPath,String fileName)
	{
		String folder ="";
		String dateProcess[] = this.dateProcess.split("/");
		folder = prefixPath+dateProcess[2]+"/"+dateProcess[1]+dateProcess[0]+"/"+ fileName;
		return folder;
	}
	
	public String getFolder(String prefixPath)
	{
		String folder ="";
		String dateProcess[] = this.dateProcess.split("/");
		folder = prefixPath+dateProcess[2]+"/"+dateProcess[1]+dateProcess[0]+"/";
		return folder;
	}
}
