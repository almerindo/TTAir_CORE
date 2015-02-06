package org.ttair.presentation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.ttair.dataaccess.TTAirDevice.EUpdateStream;
import org.ttair.dataaccess.kinect.utils.KinectTransformations;
import org.ttair.presentation.architecture.AKinectStreamLayer;


public class LayerDepth extends AKinectStreamLayer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9171882208289054171L;
	private static final LayerDepth INSTANCE = new LayerDepth();
	
	public LayerDepth(){
		super(EUpdateStream.DEPTH);			
	}
	

	
	@Override
	public String toString() {
		return "Layer TTAir DEPTH";
	}



	@Override
	public void draw(Graphics g) {
		
		/*try {
			Thread.sleep((long) 35); //( pinta mais rápido que calcula - resolvido com uma Thread.sleep(35) no draw da LayerDepth)
										
		} catch (InterruptedException e) {
			//e.printStackTrace();
		}*/
		if (!this.isNotified()){ //Soh pinta a imagem se a layer foi notificada de um novo frame
			return;
			
		}
		BufferedImage swap = (KinectTransformations.updateDepth(this.getLastFrame(),10000));
		
		
		if (swap != null){
			this.setImg(swap);
		}
		

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



	public static LayerDepth getINSTANCE() {
		return INSTANCE;
	}		
	



}
