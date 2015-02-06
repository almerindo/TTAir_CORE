package org.ttair.presentation.architecture;

import java.io.Serializable;

import com.primesense.nite.Point2D;
import com.primesense.nite.SkeletonJoint;

/**
 *
 * @author Almerindo Rehem
 * @author Lucas Aragão
 * @since 2012
 * @version 2.0
 */
public class PointJoint implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7629878577048099080L;
	private SkeletonJoint skeletonJoint;

	/**
     * Código referente ao ENUM "SkeletonJoint.java" definido pela biblioteca do            
     * OpenNI
     */
    private int cod;
    

    /**
     * variáveis referentes a localização da junta no plano cartesiano,
     * coordenadas x, y, z
     */
    private float x = 0;
    private float y = 0;
    
    /**
     * Caso a junta não seja reconhecida, os dados cartesianos referentes a
     * mesma serão zerados para todos os pontos. (0.0)
     
     * @param skeletonJoint
     * @param point
     */
    public PointJoint(SkeletonJoint skeletonJoint,  Point2D<Float> point) {
        try {

        	this.skeletonJoint = skeletonJoint;
        	
        	this.cod = skeletonJoint.getJointType().toNative();

        	
            this.x = point.getX();
            this.y = point.getY();
            
        } catch (NullPointerException ex) {
         //   System.out.println("\n>>>>>>>>>>>>>>>>>Exceção disparada<<<<<<<<<<<<<<<<<<<<<<<\n");
        }

    }

    

   


	public SkeletonJoint getJoint() {
    	
        return this.skeletonJoint;
    }

    public int getCod() {
        return cod;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public PointJoint getPoint() {
        return this;
    }

    @Override
    public String toString() {
        //  return "Joint: " + SkeletonJoint.fromNative(this.cod) + "   X: " + this.x + "   Y: " + this.y + "   Z: " + this.z;
        return this.cod + "      " + this.x + "  " + this.y +" || ";
    }
}