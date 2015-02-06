package org.ttair.proccess.architecture;

import java.io.IOException;

import org.ttair.TTAirEvent;
import org.ttair.dataaccess.socketHttp.IListenerServerSocketCmd;
import org.ttair.dataaccess.socketHttp.TTAirServerTCP;

public abstract  class ARecognizerSocketCMD extends ANodeRecognizer implements IListenerServerSocketCmd{

	
	private static final long serialVersionUID = 1870427986105790425L;
	public static final TTAirServerTCP serverTCP = TTAirServerTCP.getINSTANCE();
	public boolean notified = false;
	public String currentCmd = "NONE";
	
	
	@Override
	public void notifyCMDReceived(String cmd) {
		if (!this.currentCmd.equalsIgnoreCase(cmd)) {
			this.currentCmd = cmd;
			this.notified =true;
		}else {
			this.notified = false;
		}
	}

	@Override
	public TTAirEvent toExecute() {
		if (this.notified) {
			this.notified = false;
			try {
				return test();
			} catch (Exception e) {
				return null;
			}			
		}
		return null;
	}
	
	
	
	public TTAirEvent test() throws Exception {
		TTAirEvent event = null;
		boolean ok = false;
		if (this.toTest()) {
			if (this.getEvtType() != EEvt.SUCESS){
				this.setEvtType(EEvt.SUCESS);
				ok = true;
			}
		}else {
			this.setEvtType(EEvt.UNSUCESS);
			ok = false;
		}

		if (ok){
			event = new TTAirEvent(this);
			System.out.println(this.getClass().getName()+ " - " + this.getEvtType().name());
			
			event.setCOD(this.getEvtType().name());
			event.setParam(this.currentCmd);
		}
		
		return event;
	}

	


	public abstract boolean toTest() ;

	@Override
	public  void start(boolean start) {
		super.start(start);
		if (start) {
			ARecognizerSocketCMD.serverTCP.addListenerCMD(this);
			try {
				if (ARecognizerSocketCMD.serverTCP.isStopped()) {
					ARecognizerSocketCMD.serverTCP.start();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			ARecognizerSocketCMD.serverTCP.removeListenerCMD(this);
		}
	}

	public String getCurrentCmd() {
		return currentCmd;
	}

}
