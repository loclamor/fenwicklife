package com.m2dl.fenwicklife.ui.drawables.object;

import java.awt.Color;

import com.m2dl.fenwicklife.ui.drawables.BasicDrawable;
import com.m2dl.fenwicklife.ui.drawables.Drawable;

public class WallDrawable extends Drawable{
	public WallDrawable(int x, int y) {
		super(x, y, BasicDrawable.RECTANGLE, Color.BLACK);
	}
}
