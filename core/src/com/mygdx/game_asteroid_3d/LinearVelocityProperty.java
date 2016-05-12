package com.mygdx.game_asteroid_3d;

import com.badlogic.gdx.math.Vector2;

import properties.IProperty;

public class LinearVelocityProperty implements IProperty
{
	private Vector2 mLinearVelocity = new Vector2();
	
	public LinearVelocityProperty()
	{
		
	}
	
	public Vector2 getLinearVelocity()
	{
		return mLinearVelocity;
	}
	
	public void setLinearVelocity( Vector2 velocity )
	{
		mLinearVelocity.set( velocity );
	}
}
