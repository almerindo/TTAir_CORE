package org.ttair.util.xml;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("BehaviorFrame")
public class XMLTypeBehaviorFrame extends XMLTypeObject{

	@XStreamAlias("Event")
	private XMLTypeInteractionEvent event = new XMLTypeInteractionEvent();
	
	@XStreamImplicit(itemFieldName="ActionID")
	private List<String> listActionID = new ArrayList<String>();
	
	public void setEvent(XMLTypeInteractionEvent event){
		this.event = event;
		
	}
	
	public void addActionID (String ID){
		this.listActionID.add(ID);
	}

	public List<String> getListActionID() {
		return listActionID;
	}

	public XMLTypeInteractionEvent getEvent() {
		return event;
	}
}
