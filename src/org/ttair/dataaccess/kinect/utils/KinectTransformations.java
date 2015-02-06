/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ttair.dataaccess.kinect.utils;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.openni.PixelFormat;
import org.openni.VideoFrameRef;

import com.primesense.nite.UserTrackerFrameRef;

/**
 *
 * @author Almerindo Rehem
 */
public final class KinectTransformations implements Serializable {

	private static final long serialVersionUID = -4677673235597422596L;


	public enum EUserEfectType{
		RGB,UNI_COLOR,EFFECT_COLOR,DEPTH
	}


	//------------------------------------ 
	private static final KinectTransformations INSTANCE = new KinectTransformations();

	//----------------------------------

	private static final int MAX_DEPTH_SIZE = 10000;
	private static float histogram[] = new float[MAX_DEPTH_SIZE];        // for the depth values


	//private static Skeletons skels;
	//---------------------------------
	private static int[] userColors = new int[]{0xFFFF0000, 0xFF00FF00, 0xFF0000FF, 0xFFFFFF00, 0xFFFF00FF, 0xFF00FFFF};

	private static int hideBGPixel;
	
	//-------------------------------Hands


	private static BufferedImage imageIR = null;
	private static BufferedImage imageDEPTH = null;
	private static BufferedImage imageRGB = null;
	private static BufferedImage imageUser = null;


	private static int[] irPixels = null;
	private static int[] depthPixels = null;
	private static int[] rgbPixels = null;
	private static int[] userImagePixels = null;
	
	

	public  KinectTransformations() {

	}
	public static float[] getHistogram() {
		return null;
	}

	/**
	 * captura os dados da imageIR e transforma em uma Image
	 */
	public static BufferedImage updateIR(VideoFrameRef irMap) // get imageIR data as bytes; convert to an imageIR
	{
		try {
			int width = irMap.getWidth();
			int height = irMap.getHeight();
			PixelFormat pixelFormat = irMap.getVideoMode().getPixelFormat();
			ByteBuffer imageBB = irMap.getData().order(ByteOrder.LITTLE_ENDIAN);
			imageIR = createIRIm(imageBB,width,height,pixelFormat);
			return imageIR;
		} catch (Exception e) {
			return null;
		}
	}  // end of getImageRGB24()

	public static BufferedImage updateDepth(VideoFrameRef depthFrame, int maxPixelValue) {
		try {
			int width = depthFrame.getWidth();
			int height = depthFrame.getHeight();
			PixelFormat pixelFormat = depthFrame.getVideoMode().getPixelFormat();
			ByteBuffer imageBB = depthFrame.getData().order(ByteOrder.LITTLE_ENDIAN);
			imageDEPTH = createDEPTHIm(imageBB,width,height,pixelFormat, maxPixelValue);

			return imageDEPTH;
		}catch (Exception e){
			return null;
		}
	}

	public static BufferedImage updateRGB (VideoFrameRef rgbFrame) {
		try {
			int width = rgbFrame.getWidth();
			int height = rgbFrame.getHeight();
			PixelFormat pixelFormat = rgbFrame.getVideoMode().getPixelFormat();
			ByteBuffer imageBB = rgbFrame.getData().order(ByteOrder.LITTLE_ENDIAN);
			imageRGB = createRGBIm(imageBB,width,height,pixelFormat);

			return imageRGB;
		}catch (Exception e){
			return null;
		}
	}

	public  static BufferedImage updateUser(UserTrackerFrameRef userLastFrame, VideoFrameRef RGB , EUserEfectType typeUserFrame, int maxPixelValue){
		try {


			VideoFrameRef videoframe = null;
			if (userLastFrame == null){
				return null;
			}

			switch (typeUserFrame) {
			case RGB:
				videoframe = RGB;
				break;
			case EFFECT_COLOR:
			case UNI_COLOR:
			case DEPTH:
			default:
				videoframe = userLastFrame.getDepthFrame();
				break;
			}
			
			if (videoframe == null) {
				return null;
			}

			int width = videoframe.getWidth();
			int height = videoframe.getHeight();
			ByteBuffer frameData = videoframe.getData().order(ByteOrder.LITTLE_ENDIAN);
			ByteBuffer usersFrame = userLastFrame.getUserMap().getPixels().order(ByteOrder.LITTLE_ENDIAN);
			//hideBackground(userImagePixels, lastFrame);
			imageUser = createUserIm(frameData,width, height, usersFrame,typeUserFrame, maxPixelValue);
			return imageUser;
		} catch (Exception e) {
			//e.printStackTrace();
			return null;
		}
	}

	private static void calcHistogram ( ByteBuffer depthBuffer, int maxPixelValue) {
		
		// make sure we have enough room
		if (histogram == null || histogram.length < maxPixelValue) {
			histogram = new float[maxPixelValue];
		}

		// reset
		for (int i = 0; i < histogram.length; ++i) {
			histogram[i] = 0;
		}
		int points = 0;
		while (depthBuffer.remaining() > 0) {
			int depth = depthBuffer.getShort() & 0xFFFF;
			if (depth != 0 && depth < maxPixelValue && depth >0) {
				histogram[depth]++;
				points++;
			}
		}

		for (int i = 1; i < histogram.length; i++) {
			histogram[i] += histogram[i - 1];
		}

		if (points > 0) {
			for (int i = 1; i < histogram.length; i++) {
				histogram[i] = (int) (256 * (1.0f - (histogram[i] / (float) points)));
			}
		}

	}
	private static BufferedImage createRGBIm(ByteBuffer RGBSB, int width, int heigth, PixelFormat pixelFormat ){
		if (RGBSB == null) {
			return null;
		}

		if (rgbPixels == null || rgbPixels.length < width * heigth) {
			rgbPixels = new int[width * heigth];
		}
		RGBSB.rewind();
		int pos=0;
		switch (pixelFormat) {
		case RGB888:
			while (RGBSB.remaining() > 0) {
				int red = (int) RGBSB.get() & 0xFF;
				int green = (int) RGBSB.get() & 0xFF;
				int blue = (int) RGBSB.get() & 0xFF;
				rgbPixels[pos] = 0xFF000000 | (red << 16) | (green << 8) | blue;
				pos++;
			}
			break;
		default:
			break;
		}
		return createBufferedImg(width,heigth, rgbPixels);

	}
	private static BufferedImage createIRIm(ByteBuffer irSB, int width, int heigth, PixelFormat pixelFormat ){
		if (irSB == null) {
			return null;
		}

		if (irPixels == null || irPixels.length < width * heigth) {
			irPixels = new int[width * heigth];
		}
		irSB.rewind();
		int pos=0;
		switch (pixelFormat) {
		case GRAY16:
			while (irSB.remaining() > 0) {
				//short depth = frameData.getShort();
				int red = (int) irSB.get() & 0xFF;
				int green = (int) irSB.get() & 0xFF;
				irPixels[pos] = 0xFF000000 | (red << 16) | (green << 8) ;
				pos++;
			}

			break;
		default:
			break;
		}
		return createBufferedImg(width,heigth, irPixels);

	}
	private static BufferedImage createDEPTHIm(ByteBuffer depthSB, int width, int heigth, PixelFormat pixelFormat, int maxPixelValue ){
		
		if (depthSB == null) {
			return null;
		}

		if (depthPixels == null || depthPixels.length < width * heigth) {
			depthPixels = new int[width * heigth];
		}

		int pos=0;
		switch (pixelFormat) {
		case DEPTH_1_MM:
		case DEPTH_100_UM:
		case SHIFT_9_2:
		case SHIFT_9_3:
			calcHistogram(depthSB, maxPixelValue);
			depthSB.rewind();;
			while (depthSB.remaining() > 0) {
				short depth = depthSB.getShort();
				short pixel = (short) histogram[depth];
				depthPixels[pos] = 0xFF000000 | (pixel << 16) | (pixel << 8);
				pos++;
			}

			break;
		default:
			break;
		}
		return createBufferedImg(width,heigth, depthPixels);

	}
	private  static BufferedImage createBufferedImg(int width, int height,int[] imagePixels ) {
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		img.setRGB(0, 0, width, height, imagePixels, 0, width);
		return img;
	}

	

	private  static BufferedImage createUserIm(ByteBuffer frameData, int width, int height, 
			ByteBuffer usersFrame, EUserEfectType typeUserFrame, int maxPixelValue){
		
		// make sure we have enough room
		if (userImagePixels == null || userImagePixels.length < width * height) {
			userImagePixels = new int[width * height];
		}
		int pos = 0;
		switch (typeUserFrame) {

		case DEPTH:
			calcHistogram(frameData, maxPixelValue);
			frameData.rewind();
			while (frameData.remaining() > 0) {
				short depth = frameData.getShort();
				short userId = usersFrame.getShort();
				short pixel = (short) histogram[depth];
				int color = 0xFFFFFFFF;
				if (userId > 0) {
					color = userColors[userId % userColors.length];
					userImagePixels[pos] = color & (0xFF000000 | (pixel << 16) | (pixel << 8) | pixel);

				}else {
					userImagePixels[pos] = hideBGPixel;
				}
				pos++;
			}
			break;

		case UNI_COLOR:
			frameData.rewind();
			while (frameData.remaining() > 0) {
				frameData.getShort();
				short userId = usersFrame.getShort();
				int color = 0xFFFFFFFF;
				if (userId > 0) {
					color = userColors[userId % userColors.length];
					userImagePixels[pos] = color ;

				}else {
					userImagePixels[pos] = hideBGPixel;
				}
				pos++;
			}
			break;

		case EFFECT_COLOR:
			frameData.rewind();
			while (frameData.remaining() > 0) {
				short depth = frameData.getShort();
				short userId = usersFrame.getShort();
				//short pixel = (short) mHistogram[depth];
				int color = 0xFFFFFFFF;
				if (userId > 0) {
					color = userColors[userId % userColors.length];
					userImagePixels[pos] = color & (0xFF000000 | (depth << 16) | (depth << 8) | depth);

				}else {
					userImagePixels[pos] = hideBGPixel;
				}
				pos++;
			}
			break;
		case RGB:
			while (frameData.remaining() > 0) {
				int red = (int) frameData.get() & 0xFF;
				int green = (int) frameData.get() & 0xFF;
				int blue = (int) frameData.get() & 0xFF;
				short userId = usersFrame.getShort();
				if (userId > 0) {
					userImagePixels[pos] = 0xFF000000 | (red << 16) | (green << 8) | blue;
				}else {
					userImagePixels[pos] = hideBGPixel;
				}

				pos++;
			}
			break;

		default:
			break;
		}


		return createBufferedImg(width,height, userImagePixels);
	}


	public static KinectTransformations getINSTANCE() {
		return INSTANCE;
	}
	public static int[] getUserColors() {
		return userColors;
	}












}
