package org.ttair.util.xml;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("BehaviorChain")
public class XMLTypeBehaviorChain extends XMLTypeObject{
	@XStreamImplicit(itemFieldName="ExpectancyID")
	private List<String> listExpectancyID = new ArrayList<String>();
	
	@XStreamImplicit(itemFieldName="ExpectancyTransition")
	private List<XMLTypeExpectancyTransition> listExpectancyTransition = new ArrayList<XMLTypeExpectancyTransition>();
	

	
	public void addExpectancyID (String ID){
		this.listExpectancyID.add(ID);
	}
	
	public void addExpectancyTransition(XMLTypeExpectancyTransition et) throws Exception{
		for (XMLTypeExpectancyTransition xet : listExpectancyTransition) {
			if (xet.getID().equalsIgnoreCase(et.getID())) {
				throw new Exception("Expectancy Transition já cadastrada!");
			}
		}
		this.listExpectancyTransition.add(et);
	}
	
	public List<String> getExpectanciesId(){
		return this.listExpectancyID;
	}
	public List<XMLTypeExpectancyTransition> getExpectancyTransitions(){
		return this.listExpectancyTransition;
	}

	
}
