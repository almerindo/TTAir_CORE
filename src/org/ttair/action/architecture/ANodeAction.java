package org.ttair.action.architecture;

import org.ttair.TTAirObject;

public abstract class ANodeAction extends TTAirObject {

	private static final long serialVersionUID = -7031524298538979664L;
	private String path = null;
	
	

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public abstract Object toExecute(Object param);

	
	
	
	
}
