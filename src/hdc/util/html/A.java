package hdc.util.html;

import java.util.HashSet;
import java.util.Set;

public class A {
	private String label;
	private String url;
	private String img;
	private String xpath;
	private Set<String> attributes;

	public A(String textContent, String href, String xpath) {
		this.label = textContent;
		this.url = href;
		this.xpath = xpath;
	}

	public A(String textContent, String href, String xpath, String img) {
		this.label = textContent;
		this.url = href;
		this.xpath = xpath;
		this.img = img;
	}

	public A() {
	}

	public void addAttribute(String string) {
		if (attributes == null)
			attributes = new HashSet<String>();
		attributes.add(string);
	}

	public String[] getAttributes() {
		if (attributes == null || attributes.size() == 0)
			return null;
		return attributes.toArray(new String[attributes.size()]);
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabel() {
		return this.label;
	}

	public void setURL(String url) {
		this.url = url;
	}

	public String getURL() {
		return this.url;
	}

	public void setXPath(String xpath) {
		this.xpath = xpath;
	}

	public String getXPath() {
		return this.xpath;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

}