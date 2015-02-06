package org.ttair.presentation.architecture;

import java.io.Serializable;
import java.util.ArrayList;


/**
 *
 * @author lucas
 * @author almerindo rehem
 */
public class SkeletonUser implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4366360349326309788L;
	private ArrayList<PointJoint> listPoints = new ArrayList<PointJoint>();
    private int idUser;
    private long timeCapture;

    public SkeletonUser(ArrayList<PointJoint> joints, int idUser) {
        this.timeCapture = System.currentTimeMillis();
        this.listPoints = joints;
        this.idUser = idUser;
    }

    public long getTimeCapture() {
        return timeCapture;
    }

    public ArrayList<PointJoint> getListPoints() {
        return listPoints;
    }

    public int getIdUser() {
        return idUser;
    }
}