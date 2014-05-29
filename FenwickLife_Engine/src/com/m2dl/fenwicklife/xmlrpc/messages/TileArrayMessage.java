package com.m2dl.fenwicklife.xmlrpc.messages;

import java.io.Serializable;

import com.m2dl.fenwicklife.engine.Tile;

public class TileArrayMessage implements Serializable{
	public Tile[][] tileArray = null; 
}
