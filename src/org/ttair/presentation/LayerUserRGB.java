package org.ttair.presentation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.ttair.dataaccess.DeviceManager;
import org.ttair.dataaccess.kinect.utils.KinectTransformations;
import org.ttair.dataaccess.kinect.utils.KinectTransformations.EUserEfectType;
import org.ttair.presentation.architecture.AKinectUserStreamLayer;

public class LayerUserRGB extends AKinectUserStreamLayer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8152010778262970485L;

	

	@Override
	public void draw(Graphics g) {
		
		BufferedImage swap = (BufferedImage) KinectTransformations.updateUser(getLastUserFrame(),
				DeviceManager.getTTAirDevice().getRGB().readFrame(),
				EUserEfectType.RGB, 10000);
		
		/*this.setImg((BufferedImage) KinectTransformations.updateUser(getLastUserFrame(),
					DeviceManager.getTTAirDevice().getRGB().readFrame(),
					EUserEfectType.RGB, 10000)); //Forcando a leitura do Device
		*/
		
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

	@Override
	public String toString() {
		return null;
	}
}
