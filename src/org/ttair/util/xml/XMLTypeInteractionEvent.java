package org.ttair.util.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Event")
public class XMLTypeInteractionEvent extends XMLTypeObject{

	
	@XStreamAsAttribute
	@XStreamAlias("RecognizerID")
	private String idRecognizer = null;
	
	@XStreamAsAttribute
	@XStreamAlias("Cod")
	private String cod = null;

	
	public String getIdRecognizer() {
		return idRecognizer;
	}
	public void setIdRecognizer(String idRecognizer) {
		this.idRecognizer = idRecognizer;
	}
	
	public String getCod() {
		return cod;
	}
	public void setCod(String cod) {
		this.cod = cod;
	}
	
	
}
