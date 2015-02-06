package org.ttair;


public interface TTAirIListener extends java.util.EventListener {


	void notifyWasExecuted(TTAirEvent event) throws Exception;
}
