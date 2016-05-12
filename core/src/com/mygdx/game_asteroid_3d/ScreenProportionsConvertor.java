package com.mygdx.game_asteroid_3d;

import com.badlogic.gdx.math.Vector2;

public class ScreenProportionsConvertor
{
	private float EtalonScreenWidth = 480.0f;
	private float EtalonScreenHeight = 320.0f;
	private float mScreenWidth;
	private float mScreenHeight;

	public ScreenProportionsConvertor( int screenWidth, int screenHeight )
	{
		mScreenWidth = screenWidth;
		mScreenHeight = screenHeight;
	}
	
//	public float convertX( float x )
//	{
//		return x - mScreenWidth / 2;// * ( mScreenWidth / EtalonScreenWidth );
//	}
//	
//	public float convertY( float y )
//	{
//		return y;// * ( mScreenHeight / EtalonScreenHeight );
//	}
	
	public Vector2 proportionalMove( Vector2 xy )
	{
		float ratioX = mScreenWidth / EtalonScreenWidth;
		float ratioY = mScreenHeight / EtalonScreenHeight;
		Vector2 result = new Vector2( xy.x * ratioX, xy.y * ratioY );
		return result;
	}
	
	private Vector2 center( Vector2[] XYs )
	{
		Vector2 center = new Vector2();
		int count = XYs.length;
		int sumX = 0;
		int sumY = 0;
		
		for( Vector2 xy : XYs )
		{
			sumX += xy.x;
			sumY += xy.y;
		}
		center.x = (float)( sumX / (double)count );
		center.y = (float)( sumY / (double)count );
		return center;
	}
	
	private Vector2[] deltaCenter( Vector2[] XYs )
	{
		Vector2 center = center( XYs );
		Vector2[] deltaCenter = new Vector2[ XYs.length ];
		for( Vector2 xy : XYs )
		{
			xy.x = center.x - xy.x;
			xy.y = center.y - xy.y;
		}
		return deltaCenter;
	}
	
	private void deltaCenter( Vector2 center, Vector2[] XYs, Vector2[] deltasCenter )
	{
		for( int xy_i = 0 ; xy_i < XYs.length ; xy_i++ )
		{
			Vector2 xy = XYs[ xy_i ];
			Vector2 deltaXY = new Vector2();
			deltaXY.x = xy.x - center.x;
			deltaXY.y = xy.y - center.y;
			deltasCenter[ xy_i ] = deltaXY;
		}
	}
	
	private void scaleDeltas( Vector2[] deltasCenter, Vector2[] scaledDeltasCenter )
	{
		float ratioY = Math.min( mScreenHeight / EtalonScreenHeight, mScreenWidth / EtalonScreenWidth );
		for( int xy_i = 0 ; xy_i < deltasCenter.length ; xy_i++ )
		{
			Vector2 deltaXY = deltasCenter[ xy_i ];
			Vector2 scaledDelta = new Vector2();
			scaledDelta.x = deltaXY.x * ratioY;
			scaledDelta.y = deltaXY.y * ratioY;
			scaledDeltasCenter[ xy_i ] = scaledDelta;
		}
	}
	
	private void combineCenterDeltas( Vector2 center, Vector2[] deltasCenter, Vector2[] resultXYs )
	{
		for( int xy_i = 0 ; xy_i < deltasCenter.length ; xy_i++ )
		{
			Vector2 deltaXY = deltasCenter[ xy_i ];
			Vector2 resultXY = new Vector2();
			resultXY.x = center.x + deltaXY.x;
			resultXY.y = center.y + deltaXY.y;
			resultXYs[ xy_i ] = resultXY;
		}
	}
	
	public Vector2[] convert( Vector2[] XYs )
	{
		// Let's be etalon Width always greater than Height
		// and
		// Let's be screen Width always greater than Height
		// Then
		// I compute moving rect along Width
		// and 
		// scale rect along Height
		
		//move along width and height
		
		Vector2[] resultXYs = new Vector2[ XYs.length ];
		Vector2[] deltasCenter = new Vector2[ XYs.length ];
		Vector2[] scaledDeltasCenter = new Vector2[ XYs.length ];
		
		Vector2 center = center( XYs );
		deltaCenter( center, XYs, deltasCenter );
		
		Vector2 movedCenter = proportionalMove( center );
		
		// scale along height
		scaleDeltas( deltasCenter, scaledDeltasCenter );
		
		// combine move and scale
		combineCenterDeltas( movedCenter, scaledDeltasCenter, resultXYs );
		
		return resultXYs;
	}
	
	public float proportionalYscale( float value )
	{
		float ratioY = mScreenHeight / EtalonScreenHeight;
		
		return value * ratioY;
	}
	
	public float proportionalXscale( float value )
	{
		float ratioY = mScreenWidth / EtalonScreenWidth;
		
		return value * ratioY;
	}
}
