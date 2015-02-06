/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ttair.presentation.architecture;

import java.io.Serializable;

/**
 *
 * @author Lucas
 */
public interface IControlLayers extends Serializable{

    public void addLayer(ALayer layer);

    public void removeLayer(ALayer layer);

    public ALayer getCurrentLayer();
    
    public ALayer getLayer(int id_layer);
    
    public ALayer nextLayer();
    
    public ALayer forwardLayer();
}
