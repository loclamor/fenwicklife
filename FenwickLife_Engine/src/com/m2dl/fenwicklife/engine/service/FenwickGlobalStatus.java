package com.m2dl.fenwicklife.engine.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.m2dl.fenwicklife.Position;
import com.m2dl.fenwicklife.engine.Tile;
import com.m2dl.fenwicklife.engine.TileType;
import com.m2dl.fenwicklife.engine.EngineMain;

public class FenwickGlobalStatus implements IFenwickGlobalStatus{

	public String getAllPositions(int i1) {
		System.out.println("[Service] : All positions requested.");
		Map<Position, TileType> fieldState = EngineMain.engine.getFieldState();
		Set<Position> positions = fieldState.keySet();
		String returnedValue = "";
		for(Position p : positions) {
			if(fieldState.get(p) == TileType.WALL) {
				returnedValue+=(int)p.getX()+":"+(int)p.getY()+":WALL\n";
			}
		}
		return returnedValue;
	}

	public String getSize(int i1) {
		return "60:40";
	}
}