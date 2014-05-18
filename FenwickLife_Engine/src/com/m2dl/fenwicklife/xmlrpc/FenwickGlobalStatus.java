package com.m2dl.fenwicklife.xmlrpc;

import java.util.ArrayList;

public class FenwickGlobalStatus implements IFenwickGlobalStatus{

	public String getAllPositions(int i1) {
		System.out.println("[Service] : All positions requested.");
		String returnedValue = "";
		returnedValue+=(int)(Math.random()*60)+":"+(int)(Math.random()*40)+":FENWICK\n";
		returnedValue+=(int)(Math.random()*60)+":"+(int)(Math.random()*40)+":FENWICKFULL\n";
		returnedValue+=(int)(Math.random()*60)+":"+(int)(Math.random()*40)+":WALL\n";
		returnedValue+=(int)(Math.random()*60)+":"+(int)(Math.random()*40)+":BOX\n";
		return returnedValue;
	}

	public String getSize(int i1) {
		return "60:40";
	}
}
