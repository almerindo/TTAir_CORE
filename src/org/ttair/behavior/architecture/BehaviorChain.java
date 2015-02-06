package org.ttair.behavior.architecture;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

import org.ttair.TTAirIExpectancyNavigator;
import org.ttair.TTAirObject;
import org.ttair.behavior.BehaviorChainControl;
import org.ttair.behavior.ExpectancyControl;
import org.ttair.proccess.RecognizerControl;
import org.ttair.proccess.architecture.ANodeRecognizer;
import org.ttair.util.LoggerManager;

public class BehaviorChain extends TTAirObject implements Serializable, TTAirIExpectancyNavigator{
	private static final long serialVersionUID = -2064192413165116301L;
	
	private RecognizerControl recControl = RecognizerControl.getINSTANCE();
	private List<Expectancy> listExpectancy = new ArrayList<Expectancy>();
	private ExpectancyControl expControl = ExpectancyControl.getINSTANCE();	
	
	
    private ExecutorService executorRecControl = Executors.newFixedThreadPool(1);

	private Map<BehaviorFrame,Expectancy> mapBehaviorTarget = new HashMap<BehaviorFrame,Expectancy>();

	
	private boolean start = false;

	@Override
	public void setLog(boolean log){
		super.setLog(log);
		expControl.setLog(log);
	}
	
	
	private void writeLog(Expectancy e){
		BehaviorChainControl bcControl = BehaviorChainControl.getINSTANCE();
		
			String msg = "Behavior Chain ["+bcControl.getObjAtual().getID()+"], Expectancy: ["+e.getID()+"] WAITING-> " ;
			List<BehaviorFrame> listBehavioFrame = e.getListBehaviorFrame();
			for (int i = 0; i < listBehavioFrame.size(); i++) {
				BehaviorFrame bfAux = listBehavioFrame.get(i);
				ANodeRecognizer aRecAux = (ANodeRecognizer) bfAux.getEvt().getSource();
				msg = msg + "[" +bfAux.getID() + " Event Cod: " +bfAux.getEvt().getCOD() +" From: " + aRecAux.getID() ;
				if(i+1 == listBehavioFrame.size()){
					msg = msg + "];";
				}else {
					msg=msg+"], ";
				}
			}
			LoggerManager.log(Level.INFO, msg);
		
	}
	
	/**
	 * Método responsável por trocar a extectativa
	 */
	public synchronized void gotoExpectancy(Expectancy e) throws Exception{
		
		if(this.isLog()){
			this.writeLog(e);
		}
		
			
		if (this.expControl.exist(e)){
			this.initializeExpectancy(false); //Parar todos os recognizers da expectancy atual
			this.expControl.goTo(e); //Seta a atual para a nova expectancy
			this.initializeExpectancy(true); //Start em todos os recognizers da expectancy atual

		}else{
			throw new Exception ("Expectancy NÃO existe");
		}
	}
	
	
	public synchronized void assignBF_ExpectancyTarget(Expectancy source, BehaviorFrame causedBybf, Expectancy target) throws Exception{
		boolean achou =false;
		for (BehaviorFrame lbf : source.getListBehaviorFrame()) {
			if(lbf.getID().equalsIgnoreCase(causedBybf.getID())) {
				achou = true;
			}
		}
		if (!achou){
			throw new Exception("Behavior Frame não encontrado!. Utilize primeiro o método addBehaviorFrame");
		}
		if (target == null) {
			throw new Exception("Expectancy destino Nulla!");
		}
		//BehaviorChain bc = (BehaviorChain) bcControl.getObjAtual();
		this.getMapBehaviorTarget().put(causedBybf, target);
		//this.mapBehaviorTarget.put(bf, target);
	}
	
	public void add(Expectancy e) throws Exception{
		if (!this.expControl.exist(e)){
			throw new Exception("Expectancy ID: " + e.getID() +" Não cadastrada no barramento");
		}
		this.listExpectancy.add(e);
		
		//Expectancy.setExpectancyNavigator(this);
	}
	
	public void remove(Expectancy e) throws Exception{
		this.expControl.remove(e);
		this.listExpectancy.remove(e);
		//Expectancy.setExpectancyNavigator(null);
	}
	
	private void setFirst() throws Exception{
		if (!this.listExpectancy.isEmpty()) {
			this.gotoExpectancy(this.listExpectancy.get(0));
		}
		
	}

	/*public  static Expectancy getExpectancyAtual2() {
		
		return (Expectancy) ExpectancyControl.getINSTANCE().getObjAtual();
	}*/

	
	
	private synchronized void initializeExpectancy(boolean start) throws Exception{
		Expectancy  expectancyAtual = (Expectancy) this.expControl.getObjAtual();
		if (expectancyAtual!=null) {
			
			List<BehaviorFrame> listbf =  expectancyAtual.getListBehaviorFrame();
			if (listbf != null) {
				for (BehaviorFrame bf : listbf) {
					if (start) {
						//Fazer a expectativa atual escutar os reconhecedores
						bf.getRecognizerInteraction().addListener(expectancyAtual);
						
					}else {
						//Fazer a expectativa atual parar de escutar os reconhecedores e parar cada um deles
						bf.getRecognizerInteraction().removeListener(expectancyAtual);
					}
					bf.getRecognizerInteraction().start(start);
				}
				recControl.init(true);
			}
		}
	}

	public boolean isStart() {
		return this.start;
	}

	public void init(boolean start) throws Exception {
		
		this.start = start;
		if (start){
			this.setFirst();
			recControl.init(true);
			executorRecControl.execute(recControl);
		}else {
			try {
				this.initializeExpectancy(false);
				recControl.init(false);
				
			} catch (Exception e) {
				System.out.println(e.getMessage());
			} finally {
				//
			}
		}
	}

	public void shutdown() {
		recControl.init(false);
		recControl.shutdown();
		executorRecControl.shutdown();
	}

	public List<Expectancy> getListExpectancy() {
		return listExpectancy;
	}


	public Map<BehaviorFrame,Expectancy> getMapBehaviorTarget() {
		return mapBehaviorTarget;
	}


	




	
	
}
