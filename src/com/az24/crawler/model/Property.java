package com.az24.crawler.model;

import hdc.crawler.Node;

import java.util.List;

public class Property {
	private String name;
	private String type;
	private String xpath;
	private String xpath_sub;
	private String value;
	private String node_type;
	private String filter;
	private String changeLink;
	
	private List<Node> nodedels = null;
	private List<Node> nodedelByXpaths = null;
	
	public List<Node> getNodedels() {
		return nodedels;
	}
	public void setNodedels(List<Node> nodedels) {
		this.nodedels = nodedels;
	}

	public List<Node> getNodedelByXpaths() {
		return nodedelByXpaths;
	}
	public void setNodedelByXpaths(List<Node> nodedelByXpaths) {
		this.nodedelByXpaths = nodedelByXpaths;
	}
	
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
	public String getNode_type() {
		return node_type;
	}
	public void setNode_type(String node_type) {
		this.node_type = node_type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getXpath() {
		return xpath;
	}
	public void setXpath(String xpath) {
		this.xpath = xpath;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getChangeLink() {
		return changeLink;
	}
	public void setChangeLink(String changeLink) {
		this.changeLink = changeLink;
	}
	public String getXpath_sub() {
		return xpath_sub;
	}
	public void setXpath_sub(String xpath_sub) {
		this.xpath_sub = xpath_sub;
	}
}
