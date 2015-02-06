package org.ttair.behavior;

import java.io.Serializable;
import java.util.List;

import org.ttair.TTAirControl;
import org.ttair.TTAirObject;
import org.ttair.behavior.architecture.BehaviorFrame;
import org.ttair.behavior.architecture.Expectancy;

public class ExpectancyControl extends TTAirControl implements Serializable{
	
	private static final long serialVersionUID = -1719440030144723670L;

	private static final ExpectancyControl INSTANCE = new ExpectancyControl();	
	
	private BehaviorFrameControl bfControl = BehaviorFrameControl .getINSTANCE();
	
	public static ExpectancyControl getINSTANCE() {
		return INSTANCE;
	}
	
	@Override
	public void testeAdd(TTAirObject obj) throws Exception{
		super.testeAdd(obj);
		Expectancy exp = (Expectancy) obj;
		List<BehaviorFrame> lbf = exp.getListBehaviorFrame();
		for (BehaviorFrame behaviorFrame : lbf) {
			if (!this.bfControl.exist(behaviorFrame)) {
				throw new Exception("Expectacy ID["+exp.getID()+"] contém BehaviorFrame ["+
						behaviorFrame.getID()+"] não cadastrado no barramento de BehaviorFrames" );
			}
			
		}
	}

	
	
	@Override
	public void testObjType(TTAirObject obj) throws Exception {
		if (!(obj instanceof Expectancy)) {
			throw new Exception("Objeto não é uma Expectancy");
		}
		
	}

}
