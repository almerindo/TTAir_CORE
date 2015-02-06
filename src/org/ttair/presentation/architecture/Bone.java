package org.ttair.presentation.architecture;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;


/**
 *
 * @author Almerindo Rehem
 */
public class Bone extends ALayer {
    
    /**
	 * 
	 */ 
	private static final long serialVersionUID = 9220241966839253937L;
	private EBone cod;
    private PointJoint j1;
    private PointJoint j2;
    // private Color color = Color.RED;
    private float density = 7.0f;
    
    public Color getColor() {
        return color;
    }
    
    public void setColor(Color color) {
        this.color = color;
    }
    
    public Bone(EBone cod, PointJoint j1, PointJoint j2) throws Exception {
        this.cod = cod;
        this.j1 = j1;
        this.j2 = j2;
    }
    
    public PointJoint getJ1() {
        return j1;
    }
    
    public void setJ1(PointJoint j1) {
        this.j1 = j1;
    }
    
    public PointJoint getJ2() {
        return j2;
    }
    
    public EBone getCOD() {
        return cod;
    }
    
    public void setJ2(PointJoint j2) {
        this.j2 = j2;
    }
    
    public void setJoints(PointJoint j1, PointJoint j2) {
        this.setJ1(j1);
        this.setJ2(j2);
    }
    
    public float getDensity() {
        return density;
    }
    
    public void setDensity(float density) {
        this.density = density;
    }
    
    
    
    @Override
    public String toString() {
        return this.getLabel();
    }

	@Override
	public void paint(Graphics g) {
		if (this.j1.getJoint().getPositionConfidence() == 0.0 || this.j2.getJoint().getPositionConfidence() == 0.0) {
			return;
		}
		if (isVisible()) {
            g.setColor(this.color);
            Graphics2D g2d = (Graphics2D) g;
            
            g2d.drawOval((int) j1.getX(), (int) j1.getY(), 10, 10);
            g2d.drawOval((int) j2.getX(), (int) j2.getY(), 10, 10);
            
            g2d.setStroke(new BasicStroke(density));
            g.drawLine((int) j1.getX(), (int) j1.getY(), (int) j2.getX(), (int) j2.getY());
            g.drawString(this.cod.name(), (int)j1.getX(), (int) j1.getY());
        }
	}
}