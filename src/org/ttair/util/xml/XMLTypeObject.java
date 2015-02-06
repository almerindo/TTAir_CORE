package org.ttair.util.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;


public class XMLTypeObject {
	
	
	@XStreamAsAttribute
	@XStreamAlias("ID")
	private String ID = null;
	
	@XStreamAlias("Name")
	private String name = null;
	
	@XStreamAlias("Description")
	private String desc = null;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getID() {
		return ID;
	}
	public void setID(String ID) throws Exception {
		if (ID==null) {
			throw new Exception ("Não é possivel informar um ID NULL");
		}
		this.ID = ID;
	}
	
}
