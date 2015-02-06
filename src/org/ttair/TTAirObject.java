package org.ttair;

import java.io.Serializable;

public class TTAirObject extends ATTAirListener implements Serializable{

	private static final long serialVersionUID = -1598183408874035968L;
	
	private String name = null;
	private String desc = null;
	private String ID = null;
	private boolean log = false;
	
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


	public void setID(String iD) {
		ID = iD;
	}

	public void setLog(boolean log){
		this.log = log;
	}
	
	public boolean isLog(){
		return this.log;
	}


}
