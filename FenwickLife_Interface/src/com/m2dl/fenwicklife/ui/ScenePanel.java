package com.m2dl.fenwicklife.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;

import com.m2dl.fenwicklife.ui.drawables.Drawable;

public class ScenePanel extends JPanel {
	
	private int virtualGridWidth = 100;
	private int virtualGridHeight = 100;
	private List<Drawable> drawables;
	private boolean displayGrid;
	
	
	public ScenePanel(int width, int height) {
		this.virtualGridWidth = width;
		this.virtualGridHeight = height;
		this.drawables = new ArrayList<Drawable>();
		this.displayGrid = true;
	}
	
	private void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setBackground(Color.YELLOW);
        g2d.setColor(Color.green);
        float widthRatio  = ((float)getWidth())  / virtualGridWidth;
        float heightRatio = ((float)getHeight()) / virtualGridHeight;
        if(displayGrid) {
        	for(int x = 1; x < virtualGridWidth + 1; x++) {
        		g2d.drawLine((int)(x * widthRatio), 0,  (int)(x * widthRatio), getHeight());
        	}
        	for(int y = 1; y < virtualGridHeight + 1; y++) {
        		g2d.drawLine(0, (int)(y * heightRatio),  getWidth(), (int)(y * heightRatio));
        	}
        }
        for(Drawable s : drawables) {
        	
        	int sX       = (int) ( s.getX() * widthRatio);
        	int sWidth   =  (int) (widthRatio);
        	int sY 	     = (int) ( s.getY() * heightRatio);
        	int sHeight  = (int) (heightRatio);
        	s.draw(g2d, sX, sY, sWidth, sHeight);
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }
    
    public void addGraphics(Drawable drawable) {
    	this.drawables.add(drawable);
    }

	public void reset() {
		this.drawables.clear();		
	}
	
	public void setVirtualSize(int width, int height) {
		this.virtualGridWidth = width;
		this.virtualGridHeight = height;
	}
}
