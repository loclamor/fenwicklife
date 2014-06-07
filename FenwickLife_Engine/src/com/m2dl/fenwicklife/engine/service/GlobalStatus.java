package com.m2dl.fenwicklife.engine.service;

import java.util.Map;
import java.util.Set;

import com.m2dl.fenwicklife.Position;
import com.m2dl.fenwicklife.engine.Engine;
import com.m2dl.fenwicklife.engine.Tile;

public class GlobalStatus implements IGlobalStatus{
	
	

	public String getAllPositions(int i1) {
		Map<Position, Tile> fieldState = Engine.getInstance().getField().getGrid();
		Set<Position> positions = fieldState.keySet();
		StringBuilder returnedValue = new StringBuilder();
		synchronized (positions) {
			for(Position p : positions) {
				Tile t = fieldState.get(p);
				String box = t.hasBox() || t.hasAgentWithBox()?"1":"0";
				String agent = t.hasAgent()?"1":"0";
				returnedValue.append((int)p.getX()+":"+(int)p.getY()+":"+t.getClass().getSimpleName()+":"+agent+":"+box+"\n");
			}
		}
		return returnedValue.toString();
	}

	public String getSize(int i1) {
		return Engine.getInstance().getWidth()+":"+Engine.getInstance().getHeight();
	}
}
