package org.ttair.util.xml;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("ExpectancyTransition")
public class XMLTypeExpectancyTransition extends XMLTypeObject{

	@XStreamAsAttribute
	@XStreamAlias("Source")
	private String source = null;
	
	@XStreamAsAttribute
	@XStreamAlias("Target")
	private String target = null;
	
	
	@XStreamImplicit(itemFieldName="CausedBy")
	private List<String> listBehaviorFrameID = new ArrayList<String>();

	
	
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public List<String> getCausedBy() {
		return listBehaviorFrameID;
	}
	public void addCausedBy(String causedBy) {
		this.listBehaviorFrameID.add(causedBy);
	}
	
}
