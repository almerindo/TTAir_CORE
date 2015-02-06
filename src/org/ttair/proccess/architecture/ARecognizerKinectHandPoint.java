package org.ttair.proccess.architecture;

import org.ttair.TTAirEvent;
import org.ttair.dataaccess.DeviceManager;
import org.ttair.presentation.architecture.IUserStreamListener;

import com.primesense.nite.JointType;
import com.primesense.nite.Point2D;
import com.primesense.nite.SkeletonJoint;
import com.primesense.nite.SkeletonState;
import com.primesense.nite.UserData;
import com.primesense.nite.UserTracker;
import com.primesense.nite.UserTrackerFrameRef;

public abstract class ARecognizerKinectHandPoint extends ANodeRecognizer implements IUserStreamListener{


	private static final long serialVersionUID = 8721791713670709259L;

	

	private SkeletonJoint handJoint = null;
	private SkeletonJoint referenceJoint = null;
	private SkeletonJoint referenceJoint2 = null;
	private Point2D<Float> handPos = null;
	private Point2D<Float> referencePos = null;


	private UserTracker tracker = null;


	JointType jointType= null; 
	JointType jointTypeReference = null;
	JointType jointTypeReference2 = null;
	
	private boolean captured = false;



	
	public ARecognizerKinectHandPoint(){
		DeviceManager.getTTAirDevice().addListenerUserStream(this);
		this.tracker = DeviceManager.getTTAirDevice().getUserTracker();
		

	}


	@Override
	public synchronized TTAirEvent toExecute() {

		if (this.captured) {
			this.captured = false;
			try {
				return test();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}


	public abstract boolean toTest() throws Exception ;


	private void capture(UserData user, JointType point, JointType reference, JointType reference2){
		this.captured = false;
		this.handJoint = user.getSkeleton().getJoint(point);
		this.referenceJoint = user.getSkeleton().getJoint(reference);
		if (reference2 != null){
			this.referenceJoint2 = user.getSkeleton().getJoint(reference2);
		}
		if (this.referenceJoint2!=null && this.referenceJoint!=null && this.handJoint!=null) {
			if (referenceJoint.getPositionConfidence() == 0.0 || 
					handJoint.getPositionConfidence() == 0.0 ||
					this.referenceJoint2.getPositionConfidence()==0.0 ){
				return;
			}
		}else if (this.referenceJoint!=null && this.handJoint!=null) {
			if (referenceJoint.getPositionConfidence() == 0.0 || 
					handJoint.getPositionConfidence() == 0.0 ){
				return;
			}
		}else if (this.handJoint!=null){
			if (handJoint.getPositionConfidence() == 0.0 ){
				return;
			}
		}
		
		
		if (this.handJoint!=null && referenceJoint!=null){
			this.setReferencePos(this.tracker.convertJointCoordinatesToDepth(referenceJoint.getPosition()));
			this.setHandPos(this.tracker.convertJointCoordinatesToDepth(handJoint.getPosition()));
			this.captured = true;
		}

	}


	@Override
	public synchronized void notify(UserTrackerFrameRef frame) {

		for (UserData user : frame.getUsers()) {
			if (user.getSkeleton().getState() == SkeletonState.TRACKED) {
				this.capture(user, this.getJointType(), this.getJointTypeReference(), this.getJointTypeReference2());
			}
		}
	}


	
	public TTAirEvent test() throws Exception {
		TTAirEvent event = null;
		boolean ok = false;
		if (this.toTest()) {
			if (this.getEvtType() != EEvt.SUCESS){
				this.setEvtType(EEvt.SUCESS);
				ok = true;
			}
		}else {
			this.setEvtType(EEvt.UNSUCESS);
			ok = false;
		}

		if (ok){
			event = new TTAirEvent(this);
			System.out.println(this.getClass().getName()+ " - " + this.getEvtType().name());
			
			event.setCOD(this.getEvtType().name());
			event.setParam(this.getHandPos());
		}
		
		return event;
	}


	public JointType getJointTypeReference() {
		return this.jointTypeReference;
	}





	public JointType getJointType() {
		return  this.jointType;
	}





	public void setJointType(JointType jointType) {
		this.jointType = jointType;
	}





	public void setJointTypeReference(JointType jointTypeReference) {
		this.jointTypeReference = jointTypeReference;
	}





	public Point2D<Float> getHandPos() {
		return handPos;
	}





	public void setHandPos(Point2D<Float> handPos) {
		this.handPos = handPos;
	}





	public Point2D<Float> getReferencePos() {
		return referencePos;
	}





	public void setReferencePos(Point2D<Float> referencePos) {
		this.referencePos = referencePos;
	}






	public SkeletonJoint getReferenceJoint2() {
		return referenceJoint2;
	}


	public JointType getJointTypeReference2() {
		return jointTypeReference2;
	}


	public void setJointTypeReference2(JointType jointTypeReference2) {
		this.jointTypeReference2 = jointTypeReference2;
	}


	public SkeletonJoint getHandJoint() {
		return handJoint;
	}


	public SkeletonJoint getReferenceJoint() {
		return referenceJoint;
	}

	



}
