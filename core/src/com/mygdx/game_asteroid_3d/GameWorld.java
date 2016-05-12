package com.mygdx.game_asteroid_3d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

import properties.IProperty;
import properties.MoveProperty;

public class GameWorld implements IPropertyChangeListener, Disposable
{
	private float mWidth;
	private float mHeight;
	private World mWorld;
	private GameWorldBounds mGameBounds;

	public GameWorld( float width, float height, boolean withBounds )
	{
		mWidth = width;
		mHeight = height;
		
		init();
	}

	private void init()
	{
		mWorld = new World(new Vector2( 0.0f, 0.0f), true);
		
		mGameBounds = new GameWorldBounds( mWorld, mWidth, mHeight );
	}
	
	public World getWorld()
	{
		return mWorld;
	}
	
	public GameWorldBounds getGameBounds()
	{
		return mGameBounds;
	}
	
	public Vector2[] getBounds()
	{
		return mGameBounds.getBounds();
	}
	
	public Vector2 getTopLeft()
	{
		return mGameBounds.getTopLeft();
	}
	
	public Vector2 getTopRight()
	{
		return mGameBounds.getTopRight();
	}
	
	public Vector2 getBottomLeft()
	{
		return mGameBounds.getBottomLeft();
	}
	
	public Vector2 getBottomRight()
	{
		return mGameBounds.getBottomRight();
	}
	
	public float getWidth()
	{
		return Math.abs( ( ( getBottomRight().x - getBottomLeft().x ) + ( getTopRight().x - getTopLeft().x ) ) / 2.0f );
	}
	
	public float getHeight()
	{
		return Math.abs( ( ( getTopRight().y - getBottomRight().y ) + ( getTopLeft().y - getBottomLeft().y ) ) / 2.0f );
	}
	
	@Override
	public void dispose()
	{
		mGameBounds.dispose();
		mWorld.dispose();
	}

	@Override
	public void onChanged( IProperty prop )
	{
		mGameBounds.onChanged( prop );
	}
}
