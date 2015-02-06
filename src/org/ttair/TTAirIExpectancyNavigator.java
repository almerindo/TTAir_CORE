package org.ttair;

import org.ttair.behavior.architecture.Expectancy;

public interface TTAirIExpectancyNavigator {
	
	void gotoExpectancy(Expectancy e) throws Exception;

	String getID();
}
