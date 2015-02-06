package org.ttair.action;

import java.io.Serializable;

import org.ttair.TTAirControl;
import org.ttair.TTAirObject;
import org.ttair.action.architecture.ANodeAction;

public class ActionControl extends TTAirControl implements Serializable{

	private static final long serialVersionUID = -2111200556160575696L;
	
	private static final ActionControl INSTANCE = new ActionControl();	
    

	public static ActionControl getINSTANCE() {
		return INSTANCE;
	}

	public void execute(ANodeAction act) throws Exception{
		this.execute(act,null);
	}

	
	public void execute(ANodeAction act, Object param) throws Exception{
		if (act!=null) {
			if (this.exist(act)){ //Achou
				act.toExecute(param);
			}else { //N�o Achou
				throw new Exception("A��o n�o encontrada no barramento de a��es - ActionControl");
			}	
		}else {
			throw new Exception("N�o � poss�vel executar uma a��o nula");
		}
	}
	
	
	@Override
	public void testObjType(TTAirObject obj) throws Exception {
		if (!(obj instanceof ANodeAction)) {
			throw new Exception("Objeto n�o � um Action");
		}		
	}
	
    


}
