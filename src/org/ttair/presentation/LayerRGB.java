package org.ttair.presentation;

import java.awt.Color;
import java.awt.Graphics;

import org.ttair.dataaccess.TTAirDevice.EUpdateStream;
import org.ttair.dataaccess.kinect.utils.KinectTransformations;
import org.ttair.presentation.architecture.AKinectStreamLayer;

public class LayerRGB extends AKinectStreamLayer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9171882208289054171L;

	
	public LayerRGB(){
		super(EUpdateStream.RGB);
		
		
	}

	@Override
	public String toString() {
		return "TTAir RGB Layer";
	}

	@Override
	public  void draw(Graphics g) {
		this.setImg(KinectTransformations.updateRGB(this.getLastFrame()));

		if (this.getImg()== null) {
			return;
		}
		int framePosX = (getWidth() - this.getImg().getWidth()) / 2;
		int framePosY = (getHeight() - this.getImg().getHeight()) / 2;
		
		g.drawImage(this.getImg(), 0, 0, getWidth(), getHeight(),null);
		if (this.getLabel()!=null) {
			Color c = g.getColor();
			g.setColor(color);
			g.drawString(this.getLabel(),framePosX , framePosY);
			g.setColor(c);
		}		
	}

	



}
