package org.ttair.presentation.architecture;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.ttair.dataaccess.DeviceManager;
import org.ttair.dataaccess.TTAirDevice.EUpdateStream;

import com.primesense.nite.UserTracker;
import com.primesense.nite.UserTrackerFrameRef;

public abstract class AKinectUserStreamLayer extends ALayer{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9171882208289054171L;

	private BufferedImage img = null;

	private UserTracker userstream = null;
	private UserTrackerFrameRef lastUserFrame = null;
	private EUpdateStream typeStream = EUpdateStream.USER;


	public AKinectUserStreamLayer(){	
		this.userstream= DeviceManager.getTTAirDevice().getUserTracker();
		//DeviceManager.getTTAirDevice().addListenerUserStream(this); //Se adiciona como escutador
	}


	/*public List<UserData> getUsersTracked() {
		List<UserData> usersTracked = new LinkedList<UserData>();

		if (getLastUserFrame() != null) {
			for (UserData user : getLastUserFrame().getUsers()) {

				if (user.getSkeleton().getState() == SkeletonState.TRACKED) {
					usersTracked.add(user);
				}
			}
		}
		return usersTracked;
	}*/




	public  UserTrackerFrameRef getLastUserFrame() {
		this.lastUserFrame = this.userstream.readFrame(); //Acesso direto ao frame do device

		//this.lastUserFrame = DeviceManager.getTTAirDevice().getLastUserFrame(); //Pega o ultimo frame capturado
		return lastUserFrame;
	}




	@Override
	public  void paint(Graphics g) {


		if (this.isVisible()) {
			try {
				Thread.sleep(5); // Resolver o problema de pintar antes de acabar a leitura.
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			this.draw(g);

		}
		repaint();
	}



	public abstract void draw(Graphics g);




	public EUpdateStream getTypeStream() {
		return typeStream;
	}


	public  BufferedImage getImg() {
		return img;
	}


	public void setImg(BufferedImage img) {
		this.img = img;
	}




}
