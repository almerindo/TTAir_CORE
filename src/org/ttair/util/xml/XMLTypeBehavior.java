package org.ttair.util.xml;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;



@XStreamAlias("Behavior")
public class XMLTypeBehavior extends XMLTypeObject {
	@XStreamAsAttribute
	@XStreamAlias("xmlns:vc")
	String vc="http://www.w3.org/2007/XMLSchema-versioning";

	@XStreamAsAttribute
	@XStreamAlias("xmlns:xsi")
	String xsi="http://www.w3.org/2001/XMLSchema-instance";
	
	@XStreamAsAttribute
	@XStreamAlias("xsi:noNamespaceSchemaLocation")
	String noNamespaceSchemaLocation="TTAirBehaviorSchema.xsd";
	
	
	@XStreamImplicit(itemFieldName="Interaction")
	private List<XMLTypeInteraction> listInteraction = new ArrayList<XMLTypeInteraction>();
	
	@XStreamImplicit(itemFieldName="Action")
	private List<XMLTypeAction> listAction = new ArrayList<XMLTypeAction>();
	
	@XStreamImplicit(itemFieldName="BehaviorFrame")
	private List<XMLTypeBehaviorFrame> listBehaviorFrame = new ArrayList<XMLTypeBehaviorFrame>();

	@XStreamImplicit(itemFieldName="Expectancy")
	private List<XMLTypeExpectancy> listExpectancy = new ArrayList<XMLTypeExpectancy>();
	
	@XStreamImplicit(itemFieldName="BehaviorChain")
	private List<XMLTypeBehaviorChain> listBehaviorChain = new ArrayList<XMLTypeBehaviorChain>();

	@XStreamAsAttribute
	@XStreamAlias("Log")
	private boolean log = false;
	
	@XStreamAsAttribute
	@XStreamAlias("FilePathLog")
	private String pathLog = "./behavior.log";

	public void addExpectancy(XMLTypeExpectancy exp) {
		this.listExpectancy.add(exp);
	}

    
	public void addInteraction(XMLTypeInteraction inte){
		this.listInteraction.add(inte);
	}

	public void addAction(XMLTypeAction act){
		this.listAction.add(act);
	}
	
	public void addBehaviorFrame(XMLTypeBehaviorFrame bf){
		this.listBehaviorFrame.add(bf);
	}
	
	public void addBehaviorChain(XMLTypeBehaviorChain bc){
	
		this.listBehaviorChain.add(bc);
	}



	public List<XMLTypeInteraction> getListInteraction() {
		return listInteraction;
	}


	public List<XMLTypeAction> getListAction() {
		return listAction;
	}


	public List<XMLTypeBehaviorFrame> getListBehaviorFrame() {
		return listBehaviorFrame;
	}


	public List<XMLTypeExpectancy> getListExpectancy() {
		return listExpectancy;
	}


	public List<XMLTypeBehaviorChain> getListBehaviorChain() {
		return listBehaviorChain;
	}


	public boolean isLog() {
		return log;
	}


	public void setLog(boolean log) {
		this.log = log;
	}


	public String getPathLog() {
		return pathLog;
	}


	public void setPathLog(String pathLog) {
		this.pathLog = pathLog;
	}
	
	public XMLTypeExpectancy getExpByID(String ID){
		for (int i = 0; i < listExpectancy.size(); i++) {
			XMLTypeExpectancy exp = listExpectancy.get(i);
			if (exp.getID().equalsIgnoreCase(ID)) {
				return exp;
			}
		}
		return null;
	}
	
	public XMLTypeBehaviorFrame getBFByID(String ID){
		for (int i = 0; i < listBehaviorFrame.size(); i++) {
			XMLTypeBehaviorFrame bf = listBehaviorFrame.get(i);
			if (bf.getID().equalsIgnoreCase(ID)) {
				return bf;
			}
		}
		return null;
	}
	
	public XMLTypeBehaviorChain getBCByID(String ID){
		for (int i = 0; i < listBehaviorChain.size(); i++) {
			XMLTypeBehaviorChain bc = listBehaviorChain.get(i);
			if (bc.getID().equalsIgnoreCase(ID)) {
				return bc;
			}
		}
		return null;
	}
	
	public XMLTypeAction getActByID(String ID){
		for (int i = 0; i < listAction.size(); i++) {
			XMLTypeAction act = listAction.get(i);
			if (act.getID().equalsIgnoreCase(ID)) {
				return act;
			}
		}
		return null;
	}
	public XMLTypeInteraction getInteByID(String ID){
		for (int i = 0; i < listInteraction.size(); i++) {
			XMLTypeInteraction inte = listInteraction.get(i);
			if (inte.getID().equalsIgnoreCase(ID)) {
				return inte;
			}
		}
		return null;
	}
}
