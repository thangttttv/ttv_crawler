package hdc.crawler;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ExtractEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	
	private String name;

	private String url;

	private int cat_id;
	
	public String date ="";
	
	private Map<String, Object> properties;

	public ExtractEntity() {
		//Default Contructor for serialize
	}

	public ExtractEntity(String id) {
		this.id = id;
	}

	public void setID(String id) {
		this.id = id;
	}

	public String getID() {
		return id;
	}

	public String addProperty(String name, Object value) {
		if (properties == null)
			properties = new HashMap<String, Object>();
		properties.put(name, value);
		return name;
	}

	public String removeProperty(String name) {
		if (properties == null)
			return null;
		properties.remove(name);
		return name;
	}

	public Object getProperty(String name) {
		if (properties == null)
			return null;
		Object value = properties.get(name);
		if (value == null) {
			properties.remove(name);
			return null;
		} else
			return value;
	}

	public Collection<String> getKeys() {
		if (properties == null)
			return null;
		return properties.keySet();
	}

	public int getCat_id() {
		return cat_id;
	}

	public void setCat_id(int cat_id) {
		this.cat_id = cat_id;
	}

	public String toString() {
		Collection<String> collection = this.getKeys();
		StringBuffer stringBuffer = new StringBuffer("Entity=");
		stringBuffer.append("id=" + this.id + "\n");
		stringBuffer.append("cat_id=" + this.cat_id + "\n");
		if (collection != null)
			for (String property : collection) {
				stringBuffer.append(property + "=" + this.getProperty(property)
						+ "\n");
			}
		return stringBuffer.toString();

	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
