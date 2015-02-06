package org.ttair.presentation.architecture;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.openni.VideoFrameRef;
import org.openni.VideoStream;
import org.ttair.dataaccess.DeviceManager;
import org.ttair.dataaccess.TTAirDevice.EUpdateStream;

public  abstract class AKinectStreamLayer extends ALayer implements IVideostreamListener{

	/**
	 * @author Almerindo Rehem
	 */
	private static final long serialVersionUID = 3957111440557426181L;

	private BufferedImage img = null;
	private VideoStream videostream = null;
	private VideoFrameRef lastFrame = null;

	private EUpdateStream typeStream = null;

	private boolean notified =false;


	public AKinectStreamLayer(EUpdateStream typeStream){
		this.typeStream = typeStream;
		DeviceManager.getTTAirDevice().addListenerVideoStream(this); //Se adiciona como escutador
		initialize(typeStream);
	}

	public AKinectStreamLayer(){

	}

	public void initialize(EUpdateStream typeStream){
		switch (typeStream) {
		case DEPTH:
			this.videostream = DeviceManager.getTTAirDevice().getDepth();

			break;
		case RGB:
			this.videostream = DeviceManager.getTTAirDevice().getRGB();

			break;
		case IR:
			this.videostream = DeviceManager.getTTAirDevice().getIR();

			break;
		default:
			break;
		}
	}

	@Override
	public void paint(Graphics g) {
		if (this.isVisible()) {
			try {
				Thread.sleep((long) 120); //( pinta mais rápido que calcula - resolvido com uma Thread.sleep(35) no draw da LayerDepth)

			} catch (InterruptedException e) {
				//e.printStackTrace();
			}
			this.draw(g);
			

		}
		repaint();
	}




	public abstract void draw(Graphics g) ;


	public VideoFrameRef getLastFrame() {
		VideoFrameRef swap = null;
		if (this.notified){
			this.notified = false;
			return this.lastFrame;
		}
		switch (typeStream) {
		case DEPTH:
			swap = DeviceManager.getTTAirDevice().getDepth().readFrame(); //Forcando a leitura do device
			//swap = DeviceManager.getTTAirDevice().getLastFrameDepth(); //Pegando o ultimo lido -  melhor performance 

			break;
		case RGB:
			//swap  = DeviceManager.getTTAirDevice().getRGB().readFrame(); //Forcando a leitura do device
			swap = DeviceManager.getTTAirDevice().getLastFrameRGB();//Pegando o ultimo lido -  melhor performance
			break;
		case IR:
			swap  = DeviceManager.getTTAirDevice().getIR().readFrame(); //Forcando a leitura do device
			//swap  = DeviceManager.getTTAirDevice().getLastFrameIR();//Pegando o ultimo lido -  melhor performance
			break;
		default:
			break;
		}
		if (swap!=null) {
			this.lastFrame = swap;
		}

		return this.lastFrame;
	}


	public VideoStream getVideostream() {
		return videostream;
	}


	public BufferedImage getImg() {
		return img;
	}

	public void setImg(BufferedImage img) {
		this.img = img;
	}

	public void setLastFrame(VideoFrameRef lastFrame) {
		this.lastFrame = lastFrame;
	}

	public EUpdateStream getTypeStream() {
		return typeStream;
	}

	public void setTypeStream(EUpdateStream typeStream) {
		this.typeStream = typeStream;
	}

	@Override
	public void notify(VideoFrameRef frame) {
		lastFrame = frame;
		this.notified = true;
		this.repaint();
		
	}

	public boolean isNotified() {
		return notified;
	}

	public void setNotified(boolean notified) {
		this.notified = notified;
	}




}
