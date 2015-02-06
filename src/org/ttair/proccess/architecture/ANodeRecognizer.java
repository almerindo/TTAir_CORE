package org.ttair.proccess.architecture;

import java.util.ArrayList;
import java.util.List;

import org.ttair.TTAirEvent;
import org.ttair.TTAirIListener;
import org.ttair.TTAirObject;

public abstract class ANodeRecognizer extends TTAirObject implements Runnable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 605609880179926452L;
	private boolean stop = true;
	private String path = null;

	private List<String> listEvtCod = new ArrayList<String>();
	
	//private enum EEvt 
	private EEvt evtType = EEvt.UNSUCESS;
	

	public EEvt getEvtType() {
		return evtType;
	}





	public void setEvtType(EEvt evtType) {
		this.evtType = evtType;
	}

	
	@Override
	public void run() {
		while (!this.stop) {
			if (this.isLog()) { 	
				//LoggerManager.log(Level.INFO, "RUN: " + this.getName());
			}
			try {
				this.setExecuted(this.toExecute()); 
			} catch (Exception e) {
				//e.printStackTrace();
			}
			
		}
	}


	@SuppressWarnings("unchecked")
	private void setExecuted(TTAirEvent evt) throws Exception {
		
		if (evt!=null){ //Se foi executado com sucesso, retornou um evento. Se retornou evento notifique a todos que estão escutando
			ArrayList<TTAirIListener> tl = ((ArrayList<TTAirIListener>) this.getListeners().clone());
		
			for (TTAirIListener t : tl) {
	        	t.notifyWasExecuted(evt);
	        }
		}
	}
	
	/**
	 * Calculo a ser executado. Retorna um evento se foi executado com êxito. Do contrário retorna null
	 * @return
	 */
	public abstract TTAirEvent toExecute() ;


	public boolean isStop() {
		return this.stop;
	}


	public void start(boolean start) {
		this.stop = !start;
	}

	public boolean isStart(){
		return !this.stop;
	}


	public String getPath() {
		return path;
	}


	public void setPath(String path) {
		this.path = path;
	}


	public  List<String> getListEvtCod() {
		return listEvtCod;
	}




}

