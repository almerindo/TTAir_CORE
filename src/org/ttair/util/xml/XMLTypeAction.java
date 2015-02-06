package org.ttair.util.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Action")
public class XMLTypeAction extends XMLTypeObject{
	
	@XStreamAsAttribute
	@XStreamAlias("ClassName")
	private String className=null;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	

}
