/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ttair.presentation.architecture;

import java.util.ArrayList;
import java.util.List;

import org.ttair.presentation.LayerDepth;
import org.ttair.presentation.LayerIR;
import org.ttair.presentation.LayerImage;
import org.ttair.presentation.LayerRGB;
import org.ttair.presentation.LayerSkeletonBone;
import org.ttair.presentation.LayerUserDepth;
import org.ttair.presentation.LayerUserEffectColor;
import org.ttair.presentation.LayerUserRGB;
import org.ttair.presentation.LayerUserUniColor;

import com.primesense.nite.NiTE;


/**
 *
 * @author Almerindo Rehem
 * @author Lucas Aragão
 */
public class LayerManager {

    private static final LayerManager INSTANCE = new LayerManager();
    private static int countLayersRGB = 0;
    private static int countLayersDepth = 0;
    private static int countLayersIR = 0;
    private static int countLayersSkeleton = 0;
    private static int countLayersUSER = 0;
    private static int countLayersImg = 0;

    private static int countLayers = 0;
    private static List<ALayer> layers = new ArrayList<ALayer>();
    private static List<ALayer> layersRGB = new ArrayList<ALayer>();
    private static List<ALayer> layersDepth = new ArrayList<ALayer>();
    private static List<ALayer> layersIR = new ArrayList<ALayer>();
    private static List<ALayer> layersSkeleton = new ArrayList<ALayer>();
    private static List<ALayer> layersUser = new ArrayList<ALayer>();
    private static List<ALayer> layersIMG = new ArrayList<ALayer>();

    

    
    
    public static int getQtdLayers() {
		return countLayers;
	}
    
    

    public static boolean addLayer(ALayer layer) {
        return layers.add(layer);
    }

    public static LayerManager getINSTANCE() {
        return INSTANCE;
    }

    public static List<ALayer> getLayers() {
        return layers;
    }

    public static ALayer getLayerByID(int ID) {
    	ALayer l = null;
    	for (int i = 0; i < layers.size(); i++) {
			l = layers.get(i);
			if (l.getId() == ID) {
				return l;
			}
			
		}
        return l;
    }
   

    public static ALayer createLayer(ELayers layer) {
        
        switch (layer) {
            case TTAIR_RGB:
            	ALayer ttairRgb = new LayerRGB();
            	countLayersRGB++;
            	countLayers++;
            	ttairRgb.setId(countLayers);
            	layers.add(ttairRgb);
            	layersRGB.add(ttairRgb);
                return ttairRgb;
            case TTAIR_DEPTH:
            	ALayer ttairDepth = new LayerDepth();
            	countLayersDepth++;
            	countLayers++;
            	ttairDepth.setId(countLayers);
            	layers.add(ttairDepth);
            	layersDepth.add(ttairDepth);
                return ttairDepth;
            case TTAIR_USER_RGB:
            	if(countLayersUSER==0 && countLayersSkeleton==0){
            		NiTE.initialize();
            	}
            	ALayer ttairUser = new LayerUserRGB();
            	countLayersUSER++;
            	countLayers++;
            	ttairUser.setId(countLayers);
            	layers.add(ttairUser);
                layersUser.add(ttairUser);
                return ttairUser;
            case TTAIR_USER_DEPTH:
            	if(countLayersUSER==0 && countLayersSkeleton==0){
            		NiTE.initialize();
            	}
            	ALayer ttairUserDepth = new LayerUserDepth();
            	countLayersUSER++;
            	countLayers++;
            	ttairUserDepth.setId(countLayers);
            	layers.add(ttairUserDepth);
                layersUser.add(ttairUserDepth);
                return ttairUserDepth;
                
            case TTAIR_USER_EFECT_COLOR:
            	if(countLayersUSER==0 && countLayersSkeleton==0){
            		NiTE.initialize();
            	}
            	ALayer ttairEffectColor = new LayerUserEffectColor();
            	countLayersUSER++;
            	countLayers++;
            	ttairEffectColor.setId(countLayers);
            	layers.add(ttairEffectColor);
                layersUser.add(ttairEffectColor);
                return ttairEffectColor;
                
            case TTAIR_USER_UNICOLOR:
            	if(countLayersUSER==0 && countLayersSkeleton==0){
            		NiTE.initialize();
            	}
            	ALayer ttairUserUnicolor = new LayerUserUniColor();
            	countLayersUSER++;
            	countLayers++;
            	ttairUserUnicolor.setId(countLayers);
            	layers.add(ttairUserUnicolor);
                layersUser.add(ttairUserUnicolor);
                return ttairUserUnicolor;
            
            case TTAIR_IR:
            	//TODO Se existe algum RGB não pode existir uma IR - da conflito.
            	ALayer ttairIR= new LayerIR();
            	countLayersIR++;
            	countLayers++;
            	ttairIR.setId(countLayers);
            	layers.add(ttairIR);
            	layersIR.add(ttairIR);
            	return ttairIR;
            case TTAIR_SKELETON:
            	//NITE INITIALIZE SE AINDA NÃO HOUVE UMA INICIALIZAÇÂO
            	if(countLayersUSER==0 && countLayersSkeleton==0){
            		NiTE.initialize();
            	}
            	ALayer sklt = null;
            	try {
            		sklt = new LayerSkeletonBone();
            		countLayersSkeleton++;
                	countLayers++;
                	sklt.setId(countLayers);
                	layers.add(sklt);
                	layersSkeleton.add(sklt);
            	} catch (Exception e) {
            		// TODO Auto-generated catch block
            		e.printStackTrace();
            	}
            	
            	return sklt;
            case TTAIR_IMAGE:
            	ALayer limg = new LayerImage();
            	countLayersImg++;
            	countLayers++;
            	limg.setId(countLayers);
            	layers.add(limg);
            	layersIMG.add(limg);
                return limg;
		default:
			break;

        }
        return null;
    }



	public static int getQtdLayersRGB() {
		return countLayersRGB;
	}



	public static int getQtdLayersDepth() {
		return countLayersDepth;
	}



	public static int getQtdLayersIR() {
		return countLayersIR;
	}



	public static int getQtdLayersImg() {
		return countLayersImg;
	}



	public static List<ALayer> getLayersRGB() {
		return layersRGB;
	}



	public static List<ALayer> getLayersDepth() {
		return layersDepth;
	}



	public static List<ALayer> getLayersIR() {
		return layersIR;
	}



	public static List<ALayer> getLayersSkeleton() {
		return layersSkeleton;
	}



	public static List<ALayer> getLayersUser() {
		return layersUser;
	}



	public static List<ALayer> getLayersIMG() {
		return layersIMG;
	}

	


}
