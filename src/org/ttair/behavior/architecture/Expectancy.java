package org.ttair.behavior.architecture;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.ttair.TTAirEvent;
import org.ttair.TTAirIListener;
import org.ttair.TTAirObject;
import org.ttair.action.ActionControl;
import org.ttair.action.architecture.ANodeAction;
import org.ttair.behavior.BehaviorChainControl;
import org.ttair.behavior.ExpectancyControl;
import org.ttair.proccess.RecognizerControl;
import org.ttair.proccess.architecture.ANodeRecognizer;
import org.ttair.util.LoggerManager;

public  class Expectancy extends TTAirObject implements Serializable, TTAirIListener{
	private static final long serialVersionUID = -1787402571646076472L;
	private List<BehaviorFrame> listBehaviorFrame = new ArrayList<BehaviorFrame>();
	
    private List<ANodeRecognizer> listRecognizers = new ArrayList<ANodeRecognizer>();


	
	private RecognizerControl recControl = RecognizerControl.getINSTANCE();
	private ActionControl actControl = ActionControl.getINSTANCE();
	private ExpectancyControl expControl = ExpectancyControl.getINSTANCE();
	private BehaviorChainControl bcControl = BehaviorChainControl.getINSTANCE();
		
	
	public synchronized void addBehaviorFrame (BehaviorFrame bf) throws Exception{
		if (bf!=null){
			if (!this.listBehaviorFrame.contains(bf)) {
				
				for (BehaviorFrame bfAux : this.listBehaviorFrame) {
					if (bfAux.getID().equalsIgnoreCase(bf.getID())) {
						throw new Exception("ID do Behavior Frame já existe");
					}
				}
				this.listBehaviorFrame.add(bf);
				//Verifica se o Recognizer encontra-se cadastrado no barramento de reconhecedores RecongnizerControl
				if (this.recControl.exist(bf.getRecognizerInteraction())) {
					this.listRecognizers.add(bf.getRecognizerInteraction());
				}else {
					throw new Exception("Behavior Frame Possui Recognizer não cadastrado no Barramento de Recognizers ( RecognizerControl )");
				}
			}else{
				throw new Exception("Behavior Frame já existe");
			}
		}else{
			System.out.println("Behavior Frame null");
		}
	}
    
	
	
    public synchronized void removeBehaviorFrame(BehaviorFrame bf) throws Exception{
    	if (this.listBehaviorFrame.contains(bf)) {
    		this.listBehaviorFrame.remove(bf);
    		BehaviorChain bc = (BehaviorChain) bcControl.getObjAtual();
    		bc.getMapBehaviorTarget().remove(bf);//Remove a Expectancy destino
    		this.listRecognizers.remove(bf.getRecognizerInteraction());
    	}else{
    		throw new Exception("Behavior frame NÃO existe");
    	}
    }

    //Para todos os reconhecedores - referentes aos BF iniciar ou parar o calculo
    public void setStart(boolean start){
    	for (ANodeRecognizer recognizer : listRecognizers) {
				recognizer.start(start);
    	}
    }

    /**
     * Andar em todos os BehaviorFrames, verificar se o codEvento recebido
     * é igual ao do BehaviorFrame. Se for, mandar para ActionControl os codigos das ações  para ser executada.
     * @throws Exception 
     *
     */
    @Override
    public  synchronized void notifyWasExecuted(TTAirEvent event) throws Exception {
	
    	if (!expControl.getObjAtual().equals(this)){ //Soh notificar se essa for a atual. 
    		//throw new Exception("Inconsistencia: Expectancy Atual é diferente da Expectancy que recebeu notificação") ;
    	}else {
    		if (event != null) {
    			for (BehaviorFrame bf : this.listBehaviorFrame) {
    				if (bf.getEvt() !=null){
    					if(bf.getEvt().equals(event)){
    						List<ANodeAction> bfListActions = bf.getListAction();
    						for (int i = 0; i < bfListActions.size(); i++) {
    							ANodeAction act = bfListActions.get(i);
    							this.actControl.execute(act, event.getParam());
							}
    						
    						if (this.isLog()){
    							this.writeLogOcurred(event);
    							this.writeLogExecuted(bfListActions,event);
    						}
    						this.notifyBehaviorChain(bf);
    					}
    				}
    			}

    			
    		}
    	}

    }	
    
    private void writeLogExecuted(List<ANodeAction> bfListActions, TTAirEvent event){
    	String msg = "Behavior Chain ["+this.bcControl.getObjAtual().getID()+"] Expectancy: ["+this.getID()+"] EXECUTED-> ";
    	String msg2 = "";
    	for (int i = 0; i < bfListActions.size(); i++) {
    		ANodeAction act = bfListActions.get(i);
    		msg2 = msg2 + "ACT["+act.getID() +"]("+ event.getParam()+")";
    		if (i+1 == bfListActions.size()){
    			msg2=msg2+";";
    		}else{
    			msg2=msg2+", ";
    		}

    	}
    	LoggerManager.log(Level.INFO, msg+msg2);
    }

    private void writeLogOcurred(TTAirEvent event){
    	String msg = "Behavior Chain ["+this.bcControl.getObjAtual().getID()+"] Expectancy: ["+this.getID()+"] OCURRED-> " ;
    	msg = msg + "  Event Cod: [" + event.getCOD() +"] From: ["+ ((ANodeRecognizer)event.getSource()).getID() + "];";
    	LoggerManager.log(Level.INFO, msg);
    }
    
    
    private  void notifyBehaviorChain(BehaviorFrame bf) throws Exception{
    	BehaviorChain bc = (BehaviorChain) bcControl.getObjAtual();
    	Expectancy target = bc.getMapBehaviorTarget().get(bf);
    	
		if (target != null){
		  if (bc!=null) {	
			  bc.gotoExpectancy(target);
		  }
		}
    }
    
   
   
	public  List<BehaviorFrame> getListBehaviorFrame() {
		return listBehaviorFrame;
	}

	public  void setListBehaviorFrame(List<BehaviorFrame> listBehaviorFrame) {
		this.listBehaviorFrame = listBehaviorFrame;
	}


	
}
