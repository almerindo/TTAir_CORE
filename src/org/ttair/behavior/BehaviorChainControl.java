package org.ttair.behavior;

import java.io.Serializable;

import org.ttair.TTAirControl;
import org.ttair.TTAirObject;
import org.ttair.action.ActionControl;
import org.ttair.behavior.architecture.BehaviorChain;
import org.ttair.proccess.RecognizerControl;



public class BehaviorChainControl  extends TTAirControl implements Serializable{

	private static final long serialVersionUID = 7829602551438681141L;
	
	//private TTAirIControl control = new TTAirControl();

	private static final BehaviorChainControl INSTANCE = new BehaviorChainControl();	

	public static BehaviorChainControl getINSTANCE() {
		return INSTANCE;
	}
	
	@Override
	public boolean next(){
		BehaviorChain bcAnterior = (BehaviorChain) this.getObjAtual(); //captura a anterior
		boolean ok = super.next();
		BehaviorChain bcAtual = (BehaviorChain) this.getObjAtual(); //captura a atual
		if (ok) { //Se houve mudança, deixe a atual no mesmo estado que a anterior e páre a anterior
			this.swap(bcAnterior, bcAtual);
		}
		
		return ok; 
	}
	
	@Override
	public boolean back(){
		BehaviorChain bcAnterior = (BehaviorChain) this.getObjAtual(); //captura a anterior
		boolean ok = super.back();
		BehaviorChain bcAtual = (BehaviorChain) this.getObjAtual(); //captura a atual
		if (ok) { //Se houve mudança, deixe a atual no mesmo estado que a anterior e páre a anterior
			this.swap(bcAnterior, bcAtual);
		}
		return ok; 
	}
	
	/**
	 * Metodo utilizado para manter a BehaviorChain atual iniciada, se a anterior estiver iniciada.
	 * Se não estiver iniciada, coloca a atual para não iniciada
	 * @param bcAnterior <tt>BehaviorChain</tt> anterior
	 * @param bcAtual <tt>BehaviorChain</tt> atual
	 */
	private void swap(BehaviorChain bcAnterior, BehaviorChain bcAtual){
		boolean sta = bcAnterior.isStart(); //Se estiver iniciada. Pára, muda e inicia.
		try {
			bcAtual.init(sta);
			bcAnterior.init(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	@Override
	public boolean first() {
		BehaviorChain bcAnterior = (BehaviorChain) this.getObjAtual(); //captura a anterior
		boolean ok = super.first();
		BehaviorChain bcAtual = (BehaviorChain) this.getObjAtual(); //captura a atual
		if (ok) { //Se houve mudança, deixe a atual no mesmo estado que a anterior e pára a anterior
			this.swap(bcAnterior, bcAtual);
		}
		return ok;
	}

	@Override
	public boolean last() {
		BehaviorChain bcAnterior = (BehaviorChain) this.getObjAtual(); //captura a anterior
		boolean ok = super.last();
		BehaviorChain bcAtual = (BehaviorChain) this.getObjAtual(); //captura a atual
		if (ok) { //Se houve mudança, deixe a atual no mesmo estado que a anterior e páre a anterior
			this.swap(bcAnterior, bcAtual);
		}
		return ok;
	}

	@Override
	public void goTo(int index) throws Exception {
		BehaviorChain bcAnterior = (BehaviorChain) this.getObjAtual(); //captura a anterior
		boolean ok = false;
		try {
			super.goTo(index);
			ok = true;
		} catch (Exception e) {
			ok = false;
			throw new Exception("Erro Goto: " + e.getMessage());
		}
		BehaviorChain bcAtual = (BehaviorChain) this.getObjAtual(); //captura a atual
		if (ok) { //Se houve mudança, deixe a atual no mesmo estado que a anterior e páre a anterior
			this.swap(bcAnterior, bcAtual);
		}
		 
		
	}
	
	public void init(boolean start) throws Exception{
		BehaviorChain bc  = (BehaviorChain) this.getObjAtual();
		bc.init(start);
	}

	@Override
	public void testObjType(TTAirObject obj) throws Exception {
		if (!(obj instanceof BehaviorChain)) {
			throw new Exception("Objeto não é uma BehaviorChain");
		}		
	}
	
	@Override
	public void setLog(boolean log){
		super.setLog(log);
		ActionControl actControl = ActionControl.getINSTANCE();
		actControl.setLog(log);
		BehaviorFrameControl bfControl = BehaviorFrameControl.getINSTANCE(); 
		bfControl.setLog(log);
		ExpectancyControl expControl = ExpectancyControl.getINSTANCE();
		expControl.setLog(log);
		RecognizerControl recControl = RecognizerControl.getINSTANCE();
		recControl.setLog(log);
	}

	
}
