package com.mygdx.game_asteroid_3d;

import com.badlogic.gdx.physics.box2d.Body;

import properties.IProperty;

public class GameActiveObject implements IPropertyChangeListener
{
	protected Body mBody;
	
	GameActiveObject( Body body )
	{
		mBody = body;
	}
	
	public Body getBody()
	{
		return mBody;
	}

	@Override
	public void onChanged(IProperty prop)
	{
		;
	}
}
