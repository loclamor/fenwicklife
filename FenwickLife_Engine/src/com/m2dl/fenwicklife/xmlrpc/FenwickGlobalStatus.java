package com.m2dl.fenwicklife.xmlrpc;

import java.util.ArrayList;
import java.util.List;

import com.m2dl.fenwicklife.engine.Tile;
import com.m2dl.fenwicklife.engine.TileType;
import com.m2dl.fenwicklife.engine.Main;

public class FenwickGlobalStatus implements IFenwickGlobalStatus{

	public String getAllPositions(int i1) {
		System.out.println("[Service] : All positions requested.");
		List<Tile> fieldState = Main.field.getFieldState(); 
		String returnedValue = "";
		for(Tile c : fieldState) {
			if(c.getType() == TileType.WALL) {
				returnedValue+=(int)c.getX()+":"+(int)c.getY()+":WALL\n";
			}
		}
//		returnedValue+=(int)(Math.random()*60)+":"+(int)(Math.random()*40)+":FENWICK\n";
//		returnedValue+=(int)(Math.random()*60)+":"+(int)(Math.random()*40)+":FENWICKFULL\n";
//		returnedValue+=(int)(Math.random()*60)+":"+(int)(Math.random()*40)+":WALL\n";
//		returnedValue+=(int)(Math.random()*60)+":"+(int)(Math.random()*40)+":BOX\n";
		return returnedValue;
	}

	public String getSize(int i1) {
		return "60:40";
	}
}
