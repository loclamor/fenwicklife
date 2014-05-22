package com.m2dl.fenwicklife.xmlrpc;

import java.util.List;

import com.m2dl.fenwicklife.engine.Case;
import com.m2dl.fenwicklife.engine.CaseType;
import com.m2dl.fenwicklife.engine.Main;

public class FenwickGlobalStatus implements IFenwickGlobalStatus{

	public String getAllPositions(int i1) {

		List<Case> fieldState = Main.field.getFieldState(); 
		String returnedValue = "";
		for(Case c : fieldState) {
			if(c.getType() == CaseType.WALL) {
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
