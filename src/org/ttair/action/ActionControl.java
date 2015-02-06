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
			}else { //Não Achou
				throw new Exception("Ação não encontrada no barramento de ações - ActionControl");
			}	
		}else {
			throw new Exception("Não é possível executar uma ação nula");
		}
	}
	
	
	@Override
	public void testObjType(TTAirObject obj) throws Exception {
		if (!(obj instanceof ANodeAction)) {
			throw new Exception("Objeto não é um Action");
		}		
	}
	
    


}
