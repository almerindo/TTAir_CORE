package org.ttair.behavior.architecture;

import java.io.IOException;
import java.util.List;

import org.ttair.TTAirObject;
import org.ttair.action.ActionControl;
import org.ttair.action.architecture.ANodeAction;
import org.ttair.behavior.BehaviorChainControl;
import org.ttair.behavior.BehaviorFrameControl;
import org.ttair.behavior.ExpectancyControl;
import org.ttair.proccess.RecognizerControl;
import org.ttair.proccess.architecture.ANodeRecognizer;
import org.ttair.util.LoggerManager;
import org.ttair.util.ParserXMLtoTTAir;
import org.ttair.util.TTAirXML;
import org.ttair.util.xml.XMLTypeAction;
import org.ttair.util.xml.XMLTypeBehavior;
import org.ttair.util.xml.XMLTypeBehaviorChain;
import org.ttair.util.xml.XMLTypeBehaviorFrame;
import org.ttair.util.xml.XMLTypeExpectancy;
import org.ttair.util.xml.XMLTypeInteraction;

public class Behavior {

	private String pathFile = "./BehaviorConf02.xml";
	private XMLTypeBehavior xmlBehavior = null;
	private RecognizerControl recControl = RecognizerControl.getINSTANCE();
	private ActionControl actControl = ActionControl.getINSTANCE();
	private BehaviorFrameControl bfControl = BehaviorFrameControl.getINSTANCE();
	private ExpectancyControl expControl = ExpectancyControl.getINSTANCE();
	private BehaviorChainControl bcControl = BehaviorChainControl.getINSTANCE();
	
	private boolean log = false;
	private String pathLogFile = "./BehaviorLog.log";
	
	private TTAirXML ttairXml = TTAirXML.getINSTANCE();
	private ParserXMLtoTTAir parserXMltoTTAir = ParserXMLtoTTAir.getINSTANCE();

	private void load(String pathFile){
		this.setPathFile(pathFile);
		//List<BehaviorFrame> listBF = new ArrayList<BehaviorFrame>();

		try {
			xmlBehavior = ttairXml.loader(pathFile);
			
			
			//Barramento de Interacoes (Recognizers)
			for (XMLTypeInteraction xmlInte : xmlBehavior.getListInteraction()) {	
				ANodeRecognizer aRec = parserXMltoTTAir.xmlToRecognizer(xmlInte); //Cria o Objeto e adiciona no barramento
				recControl.add(aRec);
			}

			//Barramento de Ações
			for (XMLTypeAction xmlAct : xmlBehavior.getListAction()) {
				ANodeAction aAct = parserXMltoTTAir.xmlToAction(xmlAct);
				actControl.add(aAct);
			}

			//Barramento de BehaviorFrames
			for (XMLTypeBehaviorFrame xmlBF : xmlBehavior.getListBehaviorFrame()) {
				BehaviorFrame bf = parserXMltoTTAir.xmlToBehaviorFrame(xmlBF); 
				bfControl.add(bf);
			}

			//barramento de Expectativas
			for (XMLTypeExpectancy xmlExp : xmlBehavior.getListExpectancy()) {
				Expectancy exp = parserXMltoTTAir.xmlToExpectancy(xmlExp); 
				expControl.add(exp);
			}

			//Behaviorchain
			for (XMLTypeBehaviorChain xmlBC: xmlBehavior.getListBehaviorChain()){
				BehaviorChain bc = parserXMltoTTAir.xmlToBehaviorChain(xmlBC);
				bcControl.add(bc);
			}

			//Configurando o Behavior
			this.setPathLogFile(xmlBehavior.getPathLog());
			this.setLog(xmlBehavior.isLog());
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	
	
	
	
	public Behavior(String pathFile){
		this.load(pathFile);
	}
	
	
	
	
	
	
	public void start () throws Exception{
		BehaviorChain bc = (BehaviorChain) this.bcControl.getObjAtual();
		System.out.println("Behaviorchain["+bc.getID()+"] : STARTED");
		this.start(bc);
	}
	
	private void start(BehaviorChain bc) throws Exception{
		bc.setLog(xmlBehavior.isLog()); //Deverá passar essa variável para todas as classes. informando que é para logar
		bc.init(true);
		
	}
	public void stop () throws Exception{
		BehaviorChain bc = (BehaviorChain) this.bcControl.getObjAtual();
		System.out.println("Behaviorchain["+bc.getID()+"] : STOPPED");
		this.stop(bc);
	}
	private void stop(BehaviorChain bc) throws Exception{
		bc.init(false);
	}
	
	public boolean nextBehaviorChain() throws Exception{
		BehaviorChain bc = (BehaviorChain) this.bcControl.getObjAtual();
		boolean ok = this.bcControl.next();
		if (ok){
			if (bc.isStart()) {//Se a anterior estava iniciada. Pare
				this.stop(bc);	
			}
		}
		
		return ok;
	}
	public boolean backBehaviorChain() throws Exception{
		BehaviorChain bc = (BehaviorChain) this.bcControl.getObjAtual();
		boolean ok = this.bcControl.back();
		if (ok){
			if (bc.isStart()) {//Se a anterior estava iniciada. Pare
				this.stop(bc);	
			}
		}
		
		return ok;
		
	}
	public boolean firstBehaviorChain() throws Exception{
		BehaviorChain bc = (BehaviorChain) this.bcControl.getObjAtual();
		boolean ok = this.bcControl.first();
		if (ok){
			if (bc.isStart()) {//Se a anterior estava iniciada. Pare
				this.stop(bc);	
			}
		}
		
		return ok;
	}
	public boolean lastBehaviorChain() throws Exception{
		BehaviorChain bc = (BehaviorChain) this.bcControl.getObjAtual();
		boolean ok = this.bcControl.last();
		if (ok){
			if (bc.isStart()) {//Se a anterior estava iniciada. Pare
				this.stop(bc);	
			}
		}
		
		return ok;
	}

	public String getPathFile() {
		return pathFile;
	}

	public void setPathFile(String pathFile) {
		this.pathFile = pathFile;
	}

	public boolean isLog() {
		return log;
	}

	public void setLog(boolean log) {
		this.log = log;
		this.bcControl.setLog(log);
	}

	public String getPathLogFile() {
		return pathLogFile;
	}

	private void setPathLogFile(String pathLogFile) {
		System.out.println("Arquivo de Log armazenado em: " + pathLogFile);
		LoggerManager.setURIFile(this.getPathLogFile());
		this.pathLogFile = pathLogFile;
	}
	
	public void shutdow() throws Exception{
		List<TTAirObject> listBC = this.bcControl.getList();
		for (int i = 0; i < listBC.size(); i++) {
			BehaviorChain bc = (BehaviorChain)listBC.get(i);
			this.stop(bc);
		}
		recControl.clear();
		
		actControl.clear();
		bfControl.clear();
		expControl.clear();
		bcControl.clear();
		
		//recControl.shutdown();
	}
	

}
