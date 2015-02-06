package org.ttair.util.xml;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("Expectancy")
public class XMLTypeExpectancy extends XMLTypeObject{


	@XStreamImplicit(itemFieldName="BehaviorframeID")
	private List<String> listBehaviorFrameID = new ArrayList<String>();
	
	public void addBehaviorFrameID (String ID){
		this.listBehaviorFrameID.add(ID);
	}

	public List<String> getListBehaviorFrameID() {
		return listBehaviorFrameID;
	}
}
