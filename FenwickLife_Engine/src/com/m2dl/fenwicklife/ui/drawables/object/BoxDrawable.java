package com.m2dl.fenwicklife.ui.drawables.object;

import java.awt.Color;

import com.m2dl.fenwicklife.ui.drawables.BasicDrawable;
import com.m2dl.fenwicklife.ui.drawables.Drawable;

public class BoxDrawable extends Drawable {
	public BoxDrawable(int x, int y) {
		super(x, y, BasicDrawable.OVAL, Color.decode("0xCC6D14"));
	}
}
