package com.m2dl.fenwicklife.xmlrpc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.m2dl.fenwicklife.engine.Position;
import com.m2dl.fenwicklife.engine.Tile;
import com.m2dl.fenwicklife.engine.TileType;
import com.m2dl.fenwicklife.engine.Main;

public class FenwickGlobalStatus implements IFenwickGlobalStatus{

	public String getAllPositions(int i1) {
		System.out.println("[Service] : All positions requested.");
		Map<Position, TileType> fieldState = Main.field.getFieldState(); 
		Set<Position> positions = fieldState.keySet();
		String returnedValue = "";
		for(Position p : positions) {
			if(fieldState.get(p) == TileType.WALL) {
				returnedValue+=(int)p.getX()+":"+(int)p.getY()+":WALL\n";
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
