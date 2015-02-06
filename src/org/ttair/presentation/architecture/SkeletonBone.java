package org.ttair.presentation.architecture;

import java.util.ArrayList;
import java.util.Iterator;




/**
 *
 * @author Almerindo Rehem
 * @author Lucas Aragão
 */
public class SkeletonBone {

    private Bone[] bones = new Bone[17];
    private ArrayList<PointJoint> listPoints = new ArrayList<PointJoint>();
    private int idUser;
    private long timeCapture;
    private boolean flag = false;
    private PointJoint[] skeJoints = new PointJoint[25];

    public SkeletonBone(SkeletonUser skeUser) throws Exception {
        if (!flag) {
            this.initBones();
            flag = true;
        }
        if (skeUser != null) {
            Iterator<PointJoint> it = skeUser.getListPoints().iterator();
            
            
            while (it.hasNext()) {
                PointJoint pjT = it.next();
                //System.out.println("POS : "+pjT.getCod());
                skeJoints[pjT.getCod()] = pjT;
            }
            
            this.createAllBones();
            this.timeCapture = System.currentTimeMillis();
        }


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

    private void createAllBones() throws Exception {
    	

    	/*
    	bones[1].setJoints(skeJoints[0], skeJoints[1]);
        
    	bones[2].setJoints(skeJoints[2], skeJoints[4]);
        bones[3].setJoints(skeJoints[4], skeJoints[6]);
        
        bones[4].setJoints(skeJoints[3], skeJoints[5]);
        bones[5].setJoints(skeJoints[5], skeJoints[7]);
        
        bones[6].setJoints(skeJoints[2], skeJoints[3]);
        
        bones[7].setJoints(skeJoints[2], skeJoints[8]);
        bones[8].setJoints(skeJoints[3], skeJoints[8]);
        
        bones[9].setJoints(skeJoints[9], skeJoints[8]);
        bones[10].setJoints(skeJoints[10], skeJoints[8]);
        bones[11].setJoints(skeJoints[9], skeJoints[10]);
        
        bones[12].setJoints(skeJoints[9], skeJoints[11]);
        bones[13].setJoints(skeJoints[11], skeJoints[13]);
        
        bones[14].setJoints(skeJoints[10], skeJoints[12]);
        bones[15].setJoints(skeJoints[12], skeJoints[14]);
        //bones[16].setJoints(skeJoints[18], skeJoints[20]);*/
    	
    	bones[1].setJoints(skeJoints[0], skeJoints[1]);
        bones[2].setJoints(skeJoints[1], skeJoints[3]);
        bones[3].setJoints(skeJoints[1], skeJoints[2]);
        
        bones[4].setJoints(skeJoints[3], skeJoints[5]);
        bones[5].setJoints(skeJoints[2], skeJoints[4]);
        
        bones[6].setJoints(skeJoints[5], skeJoints[7]);
        bones[7].setJoints(skeJoints[4], skeJoints[6]);
       
        bones[8].setJoints(skeJoints[3], skeJoints[8]);
        bones[9].setJoints(skeJoints[2], skeJoints[8]);
        
        bones[10].setJoints(skeJoints[8], skeJoints[10]);
        bones[11].setJoints(skeJoints[8], skeJoints[9]);
        
        bones[12].setJoints(skeJoints[9], skeJoints[10]);
        
        bones[13].setJoints(skeJoints[10], skeJoints[12]);
        bones[14].setJoints(skeJoints[9], skeJoints[11]);
        bones[15].setJoints(skeJoints[12], skeJoints[14]);
        bones[16].setJoints(skeJoints[11], skeJoints[13]);

    }

    private void initBones() throws Exception {
    	
    	
       /* bones[1] = new Bone(EBone.NECK, skeJoints[0], skeJoints[1]);
        bones[2] = new Bone(EBone.RIGHT_CLAVICLE, skeJoints[2], skeJoints[4]);
        bones[3] = new Bone(EBone.LEFT_CLAVICLE, skeJoints[4], skeJoints[6]);
        bones[4] = new Bone(EBone.LEFT_ARM, skeJoints[3], skeJoints[5]);
        bones[5] = new Bone(EBone.RIGHT_ARM, skeJoints[5], skeJoints[7]);
        bones[6] = new Bone(EBone.RIGHT_FOREARM, skeJoints[2], skeJoints[3]);
        bones[7] = new Bone(EBone.LEFT_FOREARM, skeJoints[2], skeJoints[8]);
        bones[8] = new Bone(EBone.RIGHT_RIBS, skeJoints[3], skeJoints[8]);
        
        bones[9] = new Bone(EBone.LEFT_RIBS, skeJoints[9], skeJoints[8]);
        bones[10] = new Bone(EBone.RIGHT_WAIST, skeJoints[10], skeJoints[8]);
        bones[11] = new Bone(EBone.LEFT_WAIST, skeJoints[9], skeJoints[10]);
        
        bones[12] = new Bone(EBone.PELVIS, skeJoints[9], skeJoints[11]);
        bones[13] = new Bone(EBone.RIGHT_FEMORAL, skeJoints[11], skeJoints[13]);
        bones[14] = new Bone(EBone.LEFT_FEMORAL, skeJoints[10], skeJoints[12]);
        bones[15] = new Bone(EBone.RIGHT_SHIN, skeJoints[12], skeJoints[14]);*/
    	
    	bones[1] = new Bone(EBone.NECK, skeJoints[0], skeJoints[1]);
        bones[2] = new Bone(EBone.RIGHT_CLAVICLE, skeJoints[1], skeJoints[3]);
        bones[3] = new Bone(EBone.LEFT_CLAVICLE, skeJoints[1], skeJoints[2]);
        
        bones[4] = new Bone(EBone.RIGHT_ARM, skeJoints[3], skeJoints[5]);
        bones[5] = new Bone(EBone.LEFT_ARM, skeJoints[2], skeJoints[4]);
        
        bones[6] = new Bone(EBone.RIGHT_FOREARM, skeJoints[5], skeJoints[7]);
        bones[7] = new Bone(EBone.LEFT_FOREARM, skeJoints[4], skeJoints[6]);
       
        bones[8] = new Bone(EBone.RIGHT_RIBS, skeJoints[3], skeJoints[8]);
        bones[9] = new Bone(EBone.LEFT_RIBS, skeJoints[2], skeJoints[8]);
        
        bones[10] = new Bone(EBone.RIGHT_WAIST, skeJoints[8], skeJoints[10]);
        bones[11] = new Bone(EBone.LEFT_WAIST, skeJoints[8], skeJoints[9]);
        
        bones[12] = new Bone(EBone.PELVIS, skeJoints[9], skeJoints[10]);
        
        bones[13] = new Bone(EBone.RIGHT_FEMORAL, skeJoints[10], skeJoints[12]);
        bones[14] = new Bone(EBone.LEFT_FEMORAL, skeJoints[9], skeJoints[11]);
        bones[15] = new Bone(EBone.RIGHT_SHIN, skeJoints[12], skeJoints[14]);
        bones[16] = new Bone(EBone.LEFT_SHIN, skeJoints[11], skeJoints[13]);
        
       /* ALL, NECK, RIGHT_CLAVICLE, LEFT_CLAVICLE, RIGHT_ARM, LEFT_ARM, RIGHT_FOREARM, 
 	   LEFT_FOREARM, RIGHT_RIBS, LEFT_RIBS, RIGHT_WAIST, LEFT_WAIST, PELVIS, 
 	   RIGHT_FEMORAL, LEFT_FEMORAL, RIGHT_SHIN, LEFT_SHIN
        */
        //bones[16] = new Bone(EBone.LEFT_SHIN, skeJoints[18], skeJoints[20]);
        for (int i = 1; i <= 16; i++) {
            bones[i].setVisible(false);
        }

    }

    public Bone getBone(EBone id) {
        return bones[id.ordinal()];
    }

    public Bone[] getBones() {
        return bones;
    }
}