package com.m2dl.fenwicklife.ui.drawables.object;

import java.awt.Color;

import com.m2dl.fenwicklife.ui.drawables.BasicDrawable;
import com.m2dl.fenwicklife.ui.drawables.Drawable;

public class FenwickDrawable extends Drawable {
	
	public FenwickDrawable(int x, int y, boolean isEmpty) {
		super(x, y, BasicDrawable.ROUNDRECTANGLE, Color.GRAY);
		if(!isEmpty) {
			this.setColor(Color.ORANGE);
		}
	}
	
	public FenwickDrawable(int x, int y) {
		this(x, y, true);
	}

}
