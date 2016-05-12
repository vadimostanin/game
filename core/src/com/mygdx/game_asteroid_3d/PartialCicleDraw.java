package com.mygdx.game_asteroid_3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

import properties.IProperty;
import properties.PositionProperty;

public class PartialCicleDraw implements IDraw, IDisposablePropertyListener
{
	SpriteBatch mBatch;
	
	static ParticleEffect mPe;
	
	public PartialCicleDraw( int x, int y )
	{
		mBatch = new SpriteBatch();
		mPe = new ParticleEffect();
		try
		{
			mPe.load( Gdx.files.absolute( Gdx.files.getLocalStoragePath() + "partial_circle" ), Gdx.files.absolute( Gdx.files.getLocalStoragePath() + "" ) );
			mPe.getEmitters().first().setPosition( x, y );
			mPe.start();
		}
		catch( Exception e )
		{
			String s = e.getMessage();
		}
	}
	@Override
	public void draw(Batch batch, float deltaTime)
	{
		mBatch.begin();
		mPe.draw( mBatch, Gdx.graphics.getDeltaTime() );
		mBatch.end();
	}
	
	@Override
	public void onChanged(IProperty prop)
	{
		if( prop instanceof PositionProperty )
		{
			float x = ((PositionProperty)prop).getPosition().x;
			float y = ((PositionProperty)prop).getPosition().y;
//			mPe.getEmitters().first().setPosition( x, y );
		}
	}
	@Override
	public void dispose()
	{
//		mBatch.dispose();
		mPe.dispose();
	}
}
