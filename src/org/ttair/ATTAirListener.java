package org.ttair;

import java.util.ArrayList;

public class ATTAirListener {

	private ArrayList<TTAirIListener> listeners = new ArrayList<TTAirIListener>();

	public synchronized boolean addListener(TTAirIListener l) {
		boolean b = false;
		if(!listeners.contains(l)) {
			b = listeners.add(l);
		}
		return b;
	}

	public ArrayList<TTAirIListener> getListeners(){
		return this.listeners;
	}

	public synchronized boolean removeListener(TTAirIListener l) {
		return listeners.remove(l);
	}

}
