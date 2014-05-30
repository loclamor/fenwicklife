package com.m2dl.fenwicklife.xmlrpc.messages;

import java.io.Serializable;

import com.m2dl.fenwicklife.engine.Tile;

public class TileArrayMessage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Tile[][] tileArray = null; 
}
