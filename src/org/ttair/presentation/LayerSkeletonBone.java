package org.ttair.presentation;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import org.ttair.dataaccess.DeviceManager;
import org.ttair.presentation.architecture.AKinectUserStreamLayer;
import org.ttair.presentation.architecture.EBone;
import org.ttair.presentation.architecture.SkeletonBone;

/**
 *
 * @author Almerindo Rehem
 * @author Lucas Aragão
 */
public class LayerSkeletonBone extends AKinectUserStreamLayer{

    /**
	 * 
	 */
	private static final long serialVersionUID = -3541245880502168781L;
	SkeletonBone ske;
    EBone bone;
    boolean[] bones = new boolean[17];
    List<EBone> listBones;
    float densityLine = 7.0f;
    Color colorLine = Color.RED;

    public LayerSkeletonBone() throws Exception {
    }

    public boolean[] getBones() {
        return bones;
    }

    @Override
    public String toString() {
        return this.getLabel();
    }

    public void setVisibleBoneByID(EBone eBone) {
        if (eBone == EBone.ALL) {
            for (int i = 1; i <= 16; i++) {
                bones[i] = true;
            }
        } else {
            bones[eBone.ordinal()] = true;
        }
    }

    public void setInvisibleBoneByID(EBone eBone) {
        if (eBone == EBone.ALL) {
            for (int i = 1; i <= 16; i++) {
                bones[i] = false;
            }
        } else {
            bones[eBone.ordinal()] = false;
        }
    }


	@Override
	public void draw(Graphics g) {
		try {
			
			ske = DeviceManager.getTTAirDevice().getSkeletonsBone();
			if (ske==null) {
				return;
			}
			if (this.isVisible()) {
				for (int i = 1; i < ske.getBones().length; i++) {
					ske.getBones()[i].setVisible(bones[i]);
					ske.getBones()[i].paint(g);
				}
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
				
	}

	
	
}