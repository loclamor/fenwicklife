package com.m2dl.fenwicklife.ui.drawables;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;

public class Drawable {
	private int x = 0;
	private int y = 0;
	private Shape shape;
	private Color color;
	private BasicDrawable type;
	
	public Drawable(int x, int y, Shape shape, Color color) {
		super();
		this.x = x;
		this.y = y;
		this.shape = shape;
		this.color = color;
		this.type = null;
	}
	
	public Drawable(int x, int y, Shape shape) {
		super();
		this.x = x;
		this.y = y;
		this.shape = shape;
		this.color = Color.BLACK;
		this.type = null;
	}
	
	public Drawable(int x, int y, BasicDrawable type) {
		super();
		this.x = x;
		this.y = y;
		this.type = type;
		this.shape = null;
		this.color = Color.BLACK;
	}
	
	public Drawable(int x, int y, BasicDrawable type, Color color) {
		super();
		this.x = x;
		this.y = y;
		this.type = type;
		this.shape = null;
		this.color = color;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Shape getShape() {
		return shape;
	}

	public void setShape(Shape shape) {
		this.shape = shape;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public void draw(Graphics2D g, int x, int y, int width, int height) {
				
		g.setColor(color);
		
		if(shape != null) {
			shape.getBounds2D().setRect(x, y, width, height);
			g.draw(shape);
		}
		else {
			if(type != null) {
				switch (type) {
				case RECTANGLE:
					g.fillRect(x, y, width, height);
					g.setColor(Color.black);
					g.drawRect(x, y, width, height);
					break;
				case ROUNDRECTANGLE:
					g.fillRoundRect(x, y, width, height, width/3, height/3);
					g.setColor(Color.black);
					g.drawRoundRect(x, y, width, height, width/3, height/3);
					break;
				case OVAL : 
					g.fillOval(x, y, width, height);
					g.setColor(Color.black);
					g.drawOval(x, y, width, height);
					break;
				case TRIANGLE :
					Polygon polygon = new Polygon();
					polygon.addPoint(x + width/ 2, y);
					polygon.addPoint(x, y + height);
					polygon.addPoint(x + width, y + height);
					g.fillPolygon(polygon);
					g.setColor(Color.black);
					g.drawPolygon(polygon);
					break;
				}
			}
			else {
				g.setColor(Color.black);
				g.drawRect(x, y, width, height);
				g.setColor(Color.red);
				g.drawLine(x, y, x + width, y + height);
				g.drawLine(x, y + height, x + width, y);
			}
		}
	}
}
