/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ttair.dataaccess;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.openni.Device;
import org.openni.SensorType;
import org.openni.VideoFrameRef;
import org.openni.VideoStream;
import org.ttair.presentation.architecture.IUserStreamListener;
import org.ttair.presentation.architecture.IVideostreamListener;
import org.ttair.presentation.architecture.PointJoint;
import org.ttair.presentation.architecture.SkeletonBone;
import org.ttair.presentation.architecture.SkeletonUser;

import com.primesense.nite.SkeletonJoint;
import com.primesense.nite.SkeletonState;
import com.primesense.nite.UserData;
import com.primesense.nite.UserTracker;
import com.primesense.nite.UserTrackerFrameRef;

/**
 *
 * @author Lucas - Member TTAir Research Group
 * @author Almerindo Rehem - Member TTAir Research Group
 */
public class TTAirDevice implements Serializable, VideoStream.NewFrameListener, UserTracker.NewFrameListener{

	private static final long serialVersionUID = 3870289018142875338L;
	private Device device;
	private int id_sensor;
	private String vendor;
	private String name;
	private String uri;
	private VideoStream RGBStream = null;
	private VideoFrameRef lastFrameRGB = null;
	
	private VideoStream DepthStream = null;
	private VideoFrameRef lastFrameDepth = null;
	
	private VideoStream IRStream = null;
	private VideoFrameRef lastFrameIR = null;
	

	private UserTracker userTracker = null;
	private UserTrackerFrameRef userLastFrame = null;
	
	private ArrayList<SkeletonBone> listSkeletons = new ArrayList<SkeletonBone>();
	private SkeletonBone sb = null;
	
	private ArrayList<IVideostreamListener> listenerStream = new ArrayList<IVideostreamListener>();
	private ArrayList<IUserStreamListener> listenerUserStream = new ArrayList<IUserStreamListener>();
	
	public enum EUpdateStream{
		RGB,DEPTH,IR,USER
	}
	
	public TTAirDevice(String uri) {

		device = Device.open(uri);
		id_sensor = device.getDeviceInfo().getUsbProductId();
		vendor = device.getDeviceInfo().getVendor();
		name = device.getDeviceInfo().getName();
		this.uri = device.getDeviceInfo().getUri();
		

	}

	public VideoStream getRGB() {
		if (this.RGBStream == null) {
			this.RGBStream = createStream(SensorType.COLOR);
			this.RGBStream.addNewFrameListener(this);
		}
		return this.RGBStream ;
	}

	public VideoStream getDepth() {
		if (this.DepthStream == null) {
			this.DepthStream = createStream(SensorType.DEPTH);
			this.DepthStream.addNewFrameListener(this);
		}
		return this.DepthStream;
	}

	public Device getDevice() {
		return device;
	}

	public VideoStream getIR() {
		if (this.IRStream == null) {
			this.IRStream = createStream(SensorType.IR);
			this.IRStream.addNewFrameListener(this);
		}
		return this.IRStream;
	}
	
	public UserTracker getUserTracker() {
		if (this.userTracker == null){
			this.userTracker = UserTracker.create();
			this.userTracker.addNewFrameListener(this);
		}
		return userTracker;
	}

	public String getVendor() {
		return vendor;
	}

	String getName() {
		return name;
	}

	String getURI() {
		return uri;
	}

	private VideoStream createStream(SensorType type) {
		switch (type) {
		case COLOR:
			RGBStream = VideoStream.create(device, SensorType.COLOR);
			//   RGBStream.setVideoMode(new VideoMode(640, 480, 30, PixelFormat.RGB888.toNative()));
			RGBStream.start();
			return RGBStream;
		case DEPTH:
			DepthStream = VideoStream.create(device, SensorType.DEPTH);
			//  DepthStream.setVideoMode(new VideoMode(640, 480, 30, PixelFormat.DEPTH_1_MM.toNative()));
			DepthStream.start();
			return DepthStream;
		case IR:
			IRStream = VideoStream.create(device, SensorType.IR);
			//IRStream.setVideoMode(new VideoMode(640, 480, 30, PixelFormat.RGB888.toNative()));
			IRStream.start();
			return IRStream;
		}
		return VideoStream.create(device, type);
	}

	public int getId_sensor() {
		return id_sensor;
	}



	private  void updateUser(){
		if (userLastFrame!= null) {
			userLastFrame.release();
			userLastFrame = null;
		}

		userLastFrame = userTracker.readFrame();
		//List<ALayer> layers=   LayerManager.getLayersUser();

	}	

	private  void updateSkeleton(){
		//System.out.println("UPDATE SKEL");
		for (UserData user : userLastFrame.getUsers()) {
			if (user.isNew()) {
				// start skeleton tracking
				userTracker.startSkeletonTracking(user.getId());

			}
			if (user.getSkeleton().getState() == SkeletonState.TRACKED) {
				ArrayList<PointJoint> joints = new ArrayList<PointJoint>();
				SkeletonJoint[] sj = user.getSkeleton().getJoints();
				for (int i = 0; i < sj.length; i++) {
					PointJoint pj = new PointJoint(sj[i],this.userTracker.convertJointCoordinatesToDepth(sj[i].getPosition()));
					
					joints.add(pj);
				}
				SkeletonUser sku = joints.size() <=0  ? new SkeletonUser(null, 1) : new SkeletonUser(joints, user.getId());

				try {
					this.updateSkeletonBone(sku,user.isNew());
					//System.out.println("ADD SKL: " + user.getId());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}	


	}
	
	private  void updateSkeletonBone( SkeletonUser sku, boolean newUser){
		try {
			this.sb = new SkeletonBone(sku);
			if (newUser){
				this.listSkeletons.add(sb);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public  SkeletonBone getSkeletonsBone(){
		return this.sb;
		//return this.listSkeletons.get(0);
	}
	
	public List<UserData> getUsers(){
		return userLastFrame.getUsers();
		
	}

	

	private  void updateDepth(){
		if (DepthStream==null){
			return;
		}
		
		if(lastFrameDepth!=null) {
			lastFrameDepth.release();
			lastFrameDepth = null;
		}
		lastFrameDepth = DepthStream.readFrame();
		
	}
	
	private  void updateRGB(){
		if (RGBStream == null) {
			return;
		}
		
		if(lastFrameRGB!=null) {
			lastFrameRGB.release();
			lastFrameRGB = null;
		}
		lastFrameRGB = RGBStream.readFrame();
		
	}
	
	private  void updateIR(){
		if (IRStream == null) {
			return;
		}
		
		if(lastFrameIR!=null) {
			lastFrameIR.release();
			lastFrameIR = null;
		}
		lastFrameIR = IRStream.readFrame();
		
	}
	
	
	
	public  VideoFrameRef getLastFrameRGB() {
		return lastFrameRGB;
	}

	public  VideoFrameRef getLastFrameDepth() {
		return lastFrameDepth;
	}

	public  VideoFrameRef getLastFrameIR() {
		return lastFrameIR;
	}

	public  UserTrackerFrameRef getLastUserFrame() {
		return userLastFrame;
	}

	public  ArrayList<SkeletonBone> getListSkeletons() {
		return listSkeletons;
	}

	
	public void addListenerVideoStream(IVideostreamListener layer){
		if (layer == null){
			return;
		}
		if (!this.listenerStream.contains(layer)) {
			this.listenerStream.add(layer);
		}
		
	}
	
	public void addListenerUserStream(IUserStreamListener layer){
		
		if (layer == null){
			return;
		}
		if (!this.listenerUserStream.contains(layer)){
			this.listenerUserStream.add(layer);
		}
	}



	@Override
	public  void onFrameReady(VideoStream arg0) {
		this.updateDepth();
		this.updateRGB();
		this.updateIR();
		for (int i = 0; i < this.listenerStream.size(); i++) {
			this.listenerStream.get(i).notify(arg0.readFrame());
		}
	}

	
	@Override
	public synchronized  void onNewFrame(UserTracker arg0) {
		this.updateUser();
		
		for (int i = 0; i < this.listenerUserStream.size(); i++) {
			this.listenerUserStream.get(i).notify(this.userLastFrame);
		}
		this.updateSkeleton();
		//System.out.println("UPDATE USER");
	}
	
  


}
