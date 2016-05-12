package com.mygdx.game_asteroid_3d;

public class BodyGravityUserData
{
	private GravityBody mGravBody;
	private GravityBodyDraw2D mGravDraw;
	
	BodyGravityUserData( GravityBody gravBody, GravityBodyDraw2D gravDraw )
	{
		mGravBody = gravBody;
		mGravDraw = gravDraw;
	}
	
	public GravityBody getGravBody()
	{
		return mGravBody;
	}
	
	public GravityBodyDraw2D getGravDraw()
	{
		return mGravDraw;
	}
}
