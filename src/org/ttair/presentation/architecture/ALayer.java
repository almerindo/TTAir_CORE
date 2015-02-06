/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ttair.presentation.architecture;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.io.Serializable;

/**
 *
 * @author Almerindo Rehe
 */
public abstract class ALayer extends Component implements Serializable{

	private static final long serialVersionUID = 2586667828688642314L;
	public Color color;
    public Point[] coordinate;
    private long timestampBegin;
    private long timestampEnd;
    private long currentTimestamp;
    private String label;
    private boolean visible = true;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Inicializa o shape (timeStamps == -1, será desenhado até o fim do vídeo)
     *
     * @roseuid 41DF39D80018
     */
    public ALayer() {
        this.setSize(640,480);
        this.setTimestampBegin(-1);
        this.setTimestampEnd(-1);
        this.setLabel("");
        this.color = Color.RED;
    }

    /**
     * Responsável por desenhar o shape na tela
     *
     * @param g
     * @roseuid 41BF8B3601B5
     */
    @Override
    public abstract void paint(Graphics g);

    /**
     * Método para transformar o shape em uma string
     *
     * @return String
     * @roseuid 41BF8B4E02DE
     */
    public abstract String toString();

    /**
     * Configura o tempo inicial em que vai aparecer o shape
     *
     * @param timeStamp
     * @roseuid 41C0C46C0228
     */
    public void setTimestampBegin(long timeStamp) {

        this.timestampBegin = timeStamp;
    }

    /**
     * Configura o tempo final. Tempo em que irá sumir o shape
     *
     * @param timeStamp
     * @roseuid 41C0C4930116
     */
    public void setTimestampEnd(long timeStamp) {

        this.timestampEnd = timeStamp;
    }

    /**
     * Configura o titulo do shape
     *
     * @param label
     * @roseuid 41C0C52D01CC
     */
    public void setLabel(String label) {

        this.label = label;
    }

    /**
     * Retorna o tempo inicio do shape
     *
     * @return int
     * @roseuid 41C0C53D02C9
     */
    public long getTimestampBegin() {
        return this.timestampBegin;
    }

    /**
     * Retorna o tempo fim do Shape
     *
     * @return int
     * @roseuid 41C0C55002A8
     */
    public long getTimestampEnd() {
        return this.timestampEnd;
    }

    /**
     * Retorna o t�tulo do shape
     *
     * @return String
     * @roseuid 41C0C55C0115
     */
    public String getLabel() {
        return this.label;
    }

	public long getCurrentTimestamp() {
		this.currentTimestamp = System.currentTimeMillis();
		return currentTimestamp;
	}

	
}
