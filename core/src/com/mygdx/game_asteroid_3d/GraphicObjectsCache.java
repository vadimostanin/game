package com.mygdx.game_asteroid_3d;

import java.util.ArrayList;

public class GraphicObjectsCache
{
	private ArrayList<IDraw> mObjects = new ArrayList<IDraw>();
	private static GraphicObjectsCache mInstance = new GraphicObjectsCache();
	public static GraphicObjectsCache getInstance()
	{
		return mInstance;
	}
	
	public void add( IDraw obj )
	{
		mObjects.add( obj );
	}
	
	public IDraw get( int index )
	{
		return mObjects.get( index );
	}
	
}
