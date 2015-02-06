package org.ttair.util;

import java.util.ArrayList;
import java.util.List;

import org.ttair.TTAirEvent;
import org.ttair.action.ActionControl;
import org.ttair.action.architecture.ANodeAction;
import org.ttair.behavior.BehaviorFrameControl;
import org.ttair.behavior.ExpectancyControl;
import org.ttair.behavior.architecture.BehaviorChain;
import org.ttair.behavior.architecture.BehaviorFrame;
import org.ttair.behavior.architecture.Expectancy;
import org.ttair.proccess.RecognizerControl;
import org.ttair.proccess.architecture.ANodeRecognizer;
import org.ttair.util.xml.XMLTypeAction;
import org.ttair.util.xml.XMLTypeBehaviorChain;
import org.ttair.util.xml.XMLTypeBehaviorFrame;
import org.ttair.util.xml.XMLTypeExpectancy;
import org.ttair.util.xml.XMLTypeExpectancyTransition;
import org.ttair.util.xml.XMLTypeInteraction;
import org.ttair.util.xml.XMLTypeInteractionEvent;

public class ParserXMLtoTTAir {
	private TTAirXML ttairXml = TTAirXML.getINSTANCE();
	private RecognizerControl recControl =  RecognizerControl.getINSTANCE();
	private ActionControl actControl = ActionControl.getINSTANCE();
	private ExpectancyControl expControl = ExpectancyControl.getINSTANCE();
	private BehaviorFrameControl bfControl = BehaviorFrameControl.getINSTANCE();

	
	private static final ParserXMLtoTTAir INSTANCE = new ParserXMLtoTTAir();
	

	public ANodeRecognizer xmlToRecognizer(XMLTypeInteraction xmlInte) throws Exception{
		ANodeRecognizer aRec = null;
		try {
			aRec =  (ANodeRecognizer) ttairXml.loadClass(xmlInte.getClassName());
			aRec.setID(xmlInte.getID());
			aRec.setDesc(xmlInte.getDesc());
			aRec.setName(xmlInte.getName());
			
		} catch (Exception e) {
		  throw new Exception("Erro no parser xmlToRecognizer(). Class: ParserXMLtoTTAir " ); 	
		}
		return aRec;
	}
	
	public ANodeAction xmlToAction(XMLTypeAction xmlAct) throws Exception{
		ANodeAction aAct = null;
		try {
			aAct =  (ANodeAction) ttairXml.loadClass(xmlAct.getClassName());
			aAct.setID(xmlAct.getID());
			aAct.setDesc(xmlAct.getDesc());
			aAct.setName(xmlAct.getName());
			
		} catch (Exception e) {
		  throw new Exception("Erro no parser xmlToAction(). Class: ParserXMLtoTTAir " ); 	
		}
		return aAct;
	}
	
	private TTAirEvent xmlToEvent(XMLTypeInteractionEvent xmlEvt) throws Exception{

		ANodeRecognizer arec = (ANodeRecognizer) recControl.getByID(xmlEvt.getIdRecognizer());
		
		TTAirEvent evt = new TTAirEvent(arec); //Capturando o Recognizer.
		evt.setCOD(xmlEvt.getCod());
		evt.setID(xmlEvt.getID());
		return evt;
	}
	
	public BehaviorFrame xmlToBehaviorFrame(XMLTypeBehaviorFrame xmlBF) throws Exception{
		BehaviorFrame bf = null;
		try {
			
			//Procurar as acoes no barramento e criar uma lista com as ações. Se não encontrar dar erro no parser
			//AÇÕES
			List<ANodeAction> listAct = new ArrayList<ANodeAction>();
			for (String actID : xmlBF.getListActionID()) {
				ANodeAction aAct = (ANodeAction) actControl.getByID(actID);
				if (aAct == null) {
					throw new Exception("Action informada não existe no barramento de ações. BehaviorFrame: " + xmlBF.getID());
				}
				listAct.add(aAct); //Capturando a Lista de Ações do BehaviorFrame
			} 
			
			//EVENTO
			XMLTypeInteractionEvent xmlEvent = xmlBF.getEvent(); //Se evento for nulo retorna erro
			if (xmlEvent == null) {
				throw new Exception("Evento não Informado do BehaviorFrame: " + xmlBF.getID());
			}
			TTAirEvent evt = this.xmlToEvent(xmlEvent); //Criar um Evento. Capturar o Codigo e o Recognizer do Evento

						
			bf = new BehaviorFrame(evt, listAct); //Setando o Evento e as Ações do Behavior frame
			bf.setID(xmlBF.getID());
			bf.setDesc(xmlBF.getDesc());
			bf.setName(xmlBF.getName());
		} catch (Exception e) {
		  throw new Exception("Erro no parser xmlToBehaviorFrame(). Class: ParserXMLtoTTAir " + e.getMessage()); 	
		}
		return bf;
	}
	
	public Expectancy xmlToExpectancy(XMLTypeExpectancy xmlExp) throws Exception{
		Expectancy exp = new Expectancy();
		try {
			exp.setID(xmlExp.getID());
			exp.setDesc(xmlExp.getDesc());
			exp.setName(xmlExp.getName());

			//capturar os BF dessa Expectancy
			List<String> laux = xmlExp.getListBehaviorFrameID();
			if (laux!=null){
				for (int i = 0; i < laux.size(); i++) {
					String idBF = laux.get(i);
					if (idBF!=null) {
						BehaviorFrame bfAux = (BehaviorFrame) bfControl.getByID(idBF);
						if (bfAux!=null) {
							exp.addBehaviorFrame(bfAux);
						}else {
							throw new Exception("Um dos ID BehaviorFrames Informados na Expectancy "+ xmlExp.getID() +" não foi encontrado");
						}
					}
				}
			}
			
				
		} catch (Exception e) {
		  throw new Exception("Erro no parser xmlToExpectancy(). Class: ParserXMLtoTTAir " + e.getMessage()); 	
		}
		return exp;
	}
	
	
	public  BehaviorChain xmlToBehaviorChain(XMLTypeBehaviorChain xmlBC) throws Exception{
		BehaviorChain bc = null;
		try {
			bc = new BehaviorChain();
			
			//Adicionando as Expectativas
			for (String idExp : xmlBC.getExpectanciesId()) {
				Expectancy expAux = (Expectancy) expControl.getByID(idExp);
				if (expAux!=null) {
					bc.add(expAux);
				}else {
					throw new Exception("Um dos ID Expectancy Informados na BehaviorChain "+ xmlBC.getID() +" não foi encontrado");
				}
			}
			//Configurar as transicoes	
			for (XMLTypeExpectancyTransition et : xmlBC.getExpectancyTransitions()) {
				String expSourceID = et.getSource(); //ID Expectancy origem
				Expectancy expSource = (Expectancy) expControl.getByID(expSourceID);
				String expTargetID = et.getTarget(); //ID Expectancy Destino
				Expectancy expTarget = (Expectancy) expControl.getByID(expTargetID);
				if (expSource==null){
					throw new Exception("ID ["+ expTargetID+"] Expectancy Source informado na transição "+ et.getID()+" não foi encontrada");
				}else if (expTarget==null) {
					throw new Exception("ID ["+ expSourceID+"] Expectancy Target informado na transição "+ et.getID()+" não foi encontrada");
				}
				//Pegar os BF ID e configurar as transitions
				for (String bfID : et.getCausedBy()) {
					BehaviorFrame bfCaused  = (BehaviorFrame) bfControl.getByID(bfID);
					if (bfCaused == null){
						throw new Exception("ID ["+ bfID+"] BehaviorFrame causedBy informado na transição "+ et.getID()+" não foi encontrado");
					}
					//expSource.assignBF_ExpectancyTarget(bfCaused, expTarget);
					bc.assignBF_ExpectancyTarget(expSource,bfCaused, expTarget);
				}
				
				
			}
			//Setar a primeira expectativa.
			bc.setID(xmlBC.getID());
			bc.setName(xmlBC.getName());
			bc.setDesc(xmlBC.getDesc());
			
			//bc.setFirst(expFirst);
			
		} catch (Exception e) {
		  throw new Exception("Erro no parser xmlToBehaviorChain(). Class: ParserXMLtoTTAir " + e.getMessage() ); 	
		}
		return bc;
	}

	public static ParserXMLtoTTAir getINSTANCE() {
		return INSTANCE;
	}
	


}
