package org.ttair;

import java.io.Serializable;

import org.ttair.proccess.architecture.ANodeRecognizer;

public class TTAirEvent extends java.util.EventObject implements Serializable{

	private Object param = null;
	private String COD = null;
	private String ID = null;
	private static final long serialVersionUID = -5866460317432003270L;

	public TTAirEvent(ANodeRecognizer  source) {
	    super(source);
	}
	
	public String getCOD(){
		return this.COD;
	}
	public void setCOD(String cod) throws Exception{
		if (cod==null) {
			throw new Exception("Não é possível informar um código nulo");
		}else {
			this.COD = cod;
		}
	}
	
	public Object getParam() {
		return this.param;
	}
	
	public void setParam(Object param){
		this.param = param;
	}

	
	@Override
	public boolean equals(Object o){

		if ( o instanceof TTAirEvent) {
			TTAirEvent evt = (TTAirEvent) o;
			if (evt.getCOD() != null) {
				if (this.getCOD().equalsIgnoreCase(evt.getCOD())) {
					return true;
				}
			}
		}
		return false;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}
	
}





