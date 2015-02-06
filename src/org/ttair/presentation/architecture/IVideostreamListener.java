package org.ttair.presentation.architecture;

import org.openni.VideoFrameRef;

public interface IVideostreamListener {
	
	public void notify(VideoFrameRef frame);

}
