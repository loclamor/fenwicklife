package com.m2dl.fenwicklife.engine;

public enum TileType {
	EMPTY,
	WALL,
	/**
	 * Home area : agents want to get all boxes here
	 */
	HOME,
	/**
	 * Store area : boxes start here
	 */
	STORE,
	AGENTWITHBOX,
	AGENT,
	BOX

}
