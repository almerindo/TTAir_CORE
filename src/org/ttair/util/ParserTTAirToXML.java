package org.ttair.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ttair.TTAirObject;
import org.ttair.action.ActionControl;
import org.ttair.action.architecture.ANodeAction;
import org.ttair.behavior.BehaviorChainControl;
import org.ttair.behavior.BehaviorFrameControl;
import org.ttair.behavior.ExpectancyControl;
import org.ttair.behavior.architecture.BehaviorChain;
import org.ttair.behavior.architecture.BehaviorFrame;
import org.ttair.behavior.architecture.Expectancy;
import org.ttair.proccess.RecognizerControl;
import org.ttair.proccess.architecture.ANodeRecognizer;
import org.ttair.util.xml.XMLTypeAction;
import org.ttair.util.xml.XMLTypeBehavior;
import org.ttair.util.xml.XMLTypeBehaviorChain;
import org.ttair.util.xml.XMLTypeBehaviorFrame;
import org.ttair.util.xml.XMLTypeExpectancy;
import org.ttair.util.xml.XMLTypeExpectancyTransition;
import org.ttair.util.xml.XMLTypeInteraction;
import org.ttair.util.xml.XMLTypeInteractionEvent;
import org.ttair.util.xml.XMLTypeObject;

public class ParserTTAirToXML {
	private RecognizerControl recControl = RecognizerControl.getINSTANCE();
	private ActionControl actControl = ActionControl.getINSTANCE();
	private BehaviorFrameControl bfControl = BehaviorFrameControl.getINSTANCE();
	private ExpectancyControl expControl = ExpectancyControl.getINSTANCE();
	private BehaviorChainControl bcControl = BehaviorChainControl.getINSTANCE();

	
	private int idET = 0;

	public String getXML() throws Exception{
		XMLTypeBehavior xmlBH;

		xmlBH = this.generate();
		TTAirXML xml = new TTAirXML();
		String enconding = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n";
		return (enconding + xml.getXML(xmlBH));
	}

	private void testList(List<TTAirObject> list, String nameList) throws Exception{
		if (list==null){
			throw new Exception("A Lista" + nameList +" está NULA!");
		}
		if (list.isEmpty()) {
			throw new Exception("A Lista" + nameList +" está VAZIA!");
		}
	}
	
	public XMLTypeBehavior generate() throws Exception{
		XMLTypeBehavior behavior = new XMLTypeBehavior();
		//generate Interactions
		List<TTAirObject> listRec =recControl.getList();
		this.testList(listRec, "Recognizer Buss");
		for (TTAirObject obj : listRec) {
			ANodeRecognizer rec = (ANodeRecognizer) obj;
			behavior.addInteraction(this.recognizerToXml(rec));
		}

		List<TTAirObject> listAct =actControl.getList();
		this.testList(listAct, "Action Buss");
		for (TTAirObject obj : listAct) {
			ANodeAction act = (ANodeAction) obj;
			behavior.addAction(this.actionToXml(act));
		}

		List<TTAirObject> listBF = bfControl.getList();
		this.testList(listBF, "BehaviorFrame Buss");
		for (TTAirObject obj : listBF) {
			BehaviorFrame bf = (BehaviorFrame) obj;
			behavior.addBehaviorFrame(this.behaviorFrameToXML(bf));

		}

		List<TTAirObject> listExp = expControl.getList();
		this.testList(listExp, "Expectancy Buss");
		for (TTAirObject obj : listExp) {
			Expectancy exp = (Expectancy) obj;
			behavior.addExpectancy(this.expectancyToXML(exp));
		}

		
		List<TTAirObject> listBC = bcControl.getList();
		this.testList(listBC, "BehaviorChain Buss");
		for (TTAirObject obj : listBC) {
			BehaviorChain bc = (BehaviorChain) obj;
			behavior.addBehaviorChain(this.behaviorChainToXML(bc));
		}

		behavior.setLog(bcControl.isLog());


		return behavior;
	}

	private XMLTypeBehaviorChain behaviorChainToXML(BehaviorChain bc) throws Exception {
		XMLTypeBehaviorChain xmlBC = new XMLTypeBehaviorChain();
		xmlBC = (XMLTypeBehaviorChain) this.setXmlObj(bc, xmlBC);
		List<Expectancy> listExp = bc.getListExpectancy();

		for (Expectancy exp : listExp) {
			xmlBC.addExpectancyID(exp.getID());


			Map<BehaviorFrame, Expectancy> trans = bc.getMapBehaviorTarget();
			List<Expectancy> listExpTarget = new ArrayList<Expectancy>();


			List<BehaviorFrame> listBF = exp.getListBehaviorFrame();
			int i = 0; 
			while (i < listBF.size() ) {
				BehaviorFrame bf = listBF.get(i);
				if(trans.containsKey(bf)){
					Expectancy expAux = trans.get(bf);
					if (expAux!=null){
						if (!listExpTarget.contains(expAux)){
							listExpTarget.add(expAux); //So adiciona se não existe
						}
					}

				}
				i++;
			}

			//listExpTarget
			for (Expectancy exptarget : listExpTarget) {
				XMLTypeExpectancyTransition et = new XMLTypeExpectancyTransition();
				et.setID("ET"+idET++);
				et.setSource(exp.getID());
				et.setTarget(exptarget.getID());
				for (BehaviorFrame bf : listBF) {
					Expectancy expAux = trans.get(bf);
					if (expAux!=null){
						if (expAux.equals(exptarget)){
							et.addCausedBy(bf.getID());
						}
					}
				}
				xmlBC.addExpectancyTransition(et);
			}



		}





		return xmlBC;
	}

	private XMLTypeExpectancy expectancyToXML(Expectancy exp) throws Exception {
		XMLTypeExpectancy xmlExp = new XMLTypeExpectancy();
		xmlExp = (XMLTypeExpectancy) this.setXmlObj(exp, xmlExp);
		List<BehaviorFrame> listBF = exp.getListBehaviorFrame();
		for (BehaviorFrame bf : listBF) {
			xmlExp.addBehaviorFrameID(bf.getID());
		}

		return xmlExp;
	}

	private XMLTypeBehaviorFrame behaviorFrameToXML(BehaviorFrame bf) {
		try {
			XMLTypeBehaviorFrame xmlBF = new XMLTypeBehaviorFrame();
			xmlBF = (XMLTypeBehaviorFrame) this.setXmlObj(bf, xmlBF);

			XMLTypeInteractionEvent xmlEvt = new XMLTypeInteractionEvent();
			xmlEvt.setCod(bf.getEvt().getCOD());

			xmlEvt.setID(bf.getEvt().getID());
			ANodeRecognizer recObj =  (ANodeRecognizer) bf.getEvt().getSource();
			xmlEvt.setIdRecognizer(recObj.getID());
			xmlBF.setEvent(xmlEvt);
			List<ANodeAction> listAct = bf.getListAction();
			for (ANodeAction act : listAct) {
				xmlBF.addActionID(act.getID());
			}
			return xmlBF;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private XMLTypeAction actionToXml(ANodeAction act) throws Exception {
		XMLTypeAction xmlAct = new XMLTypeAction();
		xmlAct = (XMLTypeAction) this.setXmlObj(act, xmlAct);; 
		if (act.getPath()==null){
			act.setPath(act.getClass().getCanonicalName());
		}
		xmlAct.setClassName(act.getPath());

		return xmlAct;
	}

	private XMLTypeObject setXmlObj(TTAirObject source, XMLTypeObject target) throws Exception{

		target.setDesc(source.getDesc());
		target.setID(source.getID());
		target.setName(source.getName());
		return target;
	}

	private XMLTypeInteraction recognizerToXml(ANodeRecognizer rec) throws Exception{
		XMLTypeInteraction xmlInte = new XMLTypeInteraction();
		this.setXmlObj(rec, xmlInte);
		if (rec.getPath()==null){
			rec.setPath(rec.getClass().getCanonicalName());
		}
		//System.out.println(rec.getPath());
		xmlInte.setClassName(rec.getPath());
		return xmlInte;
	}

	

}
