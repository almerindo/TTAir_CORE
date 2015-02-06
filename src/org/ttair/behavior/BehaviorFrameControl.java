package org.ttair.behavior;

import java.io.Serializable;

import org.ttair.TTAirControl;
import org.ttair.TTAirObject;
import org.ttair.behavior.architecture.BehaviorFrame;

public class BehaviorFrameControl extends TTAirControl implements Serializable{
	
	private static final long serialVersionUID = -1114085647924277694L;

	private static final BehaviorFrameControl INSTANCE = new BehaviorFrameControl();
	
	public static BehaviorFrameControl getINSTANCE() {
		return INSTANCE;
	}

	@Override
	public void testObjType(TTAirObject obj) throws Exception {
		if (!(obj instanceof BehaviorFrame)) {
			throw new Exception("Objeto não é uma BehaviorFrame");
		}
		
	}
	
}
