package org.ttair.behavior.architecture;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.ttair.TTAirEvent;
import org.ttair.TTAirObject;
import org.ttair.action.ActionControl;
import org.ttair.action.architecture.ANodeAction;
import org.ttair.proccess.RecognizerControl;
import org.ttair.proccess.architecture.ANodeRecognizer;


public  class BehaviorFrame extends TTAirObject implements Serializable{

	private static final long serialVersionUID = -5701368283927361869L;
	
	//private ANodeRecognizer rec = null;
    private List<ANodeAction> listAction = new ArrayList<ANodeAction>();
    private ActionControl actionControl = ActionControl.getINSTANCE();
   // private RecognizerControl recControl = RecognizerControl.getINSTANCE();
    private TTAirEvent evt = null;
    
    public BehaviorFrame(TTAirEvent evt, List<ANodeAction> listAction) throws Exception{
    	this.assigne(evt, listAction);
    }
    
    
	public void assigne (TTAirEvent evt, List<ANodeAction> listAction) throws Exception{
		if (evt != null) {
    		this.setListAction(listAction);
    		this.setEvt(evt);
    	
    	}else {
    		throw new Exception("Evento Nulo");
    	}
	}


	public ANodeRecognizer getRecognizerInteraction() throws Exception {
		if (this.evt != null) {
			return (ANodeRecognizer) this.evt.getSource();
		}else {
			throw new Exception("Evento ainda não setado");
		}
		
	}


	public List<ANodeAction> getListAction() {
		return listAction;
	}


	private void setListAction(List<ANodeAction> listAction2) throws Exception {
		//Só permite setar as ações existentes no barramento de ações
		for (ANodeAction aNodeAction : listAction2) {
			if (!this.actionControl.exist(aNodeAction)){
				throw new Exception("Ação " + aNodeAction.getName() +" não cadastrada no barramento de ações!");
			}
		}
		this.listAction = listAction2;
	}


	public TTAirEvent getEvt() {
		return evt;
	}


	public void setEvt(TTAirEvent evt) throws Exception {
		if (evt != null) {
			if (evt.getSource() instanceof  ANodeRecognizer) {
				if (evt.getCOD() == null || evt.getCOD().isEmpty()) {
					throw new Exception("Evento não possui um Código!");
				}
				//Verificar se Existe no Recognizer Control
				RecognizerControl rec = RecognizerControl.getINSTANCE();
				if (!rec.exist((ANodeRecognizer) evt.getSource())){
					throw new Exception("Recognizer não pertence ao barramento de Reconhecedores. Use RecognizerControl.add() primeiro");
				}
				this.evt = evt;
			}else{
				throw new Exception("Evento não possui um ANodeRecognizer. TTAirEvent.getSource() é diferente de um ANodeRecognizer");
			}
			
		}else {
			throw new Exception("Evento Nulo");
		}
		
	}




	


	
	

}
