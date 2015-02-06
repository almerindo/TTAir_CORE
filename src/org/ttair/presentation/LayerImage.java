/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ttair.presentation;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.ttair.presentation.architecture.ALayer;

/**
 *
 * @author Almerindo Rehem
 */
public class LayerImage extends ALayer {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6069538241070518677L;
	private BufferedImage img;
    private int xPosition = 0;
    private int yPosition = 0;
    private int widht = 0;
    private int height = 0;

    public void setImage(String uri) {
        try {
            img = ImageIO.read(new File(uri));
            if (widht + height == 0) {
                widht = img.getWidth();
                height = img.getHeight();
            }
        } catch (IOException ex) {
            System.out.println("Não foi possível carregar a imagem");
        }
    }

    public BufferedImage getImage() {
        return img;
    }

    public void setImagePosition(int x, int y) {
        xPosition = x;
        yPosition = y;
    }

    public void setImageSize(int widht, int height) {
        this.widht = widht;
        this.height = height;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img, xPosition, yPosition, widht, height, this);
    }

    @Override
    public String toString() {
        return getLabel();
    }

}
