package com.mygdx.game_asteroid_3d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Disposable;

import properties.MoveProperty;
import properties.PositionProperty;

public class BodyWrapperTask implements Runnable, Disposable
{
	private Body mBody;
	private boolean mDisplosed = false;
	IPropertyChangeListener mListener;
	PositionProperty mPositionProperty = new PositionProperty();
	LinearVelocityProperty mLinearVelocityProperty = new LinearVelocityProperty();
	MoveProperty mMoveProperty = new MoveProperty();

	Vector2 mMove = new Vector2();
	Vector2 mTempPosition = new Vector2();
	Vector2 mLastPosition = new Vector2();

	Vector2 mPosition = new Vector2();
	Vector2 mLinearVelocity = new Vector2();
	
	BodyWrapperTask( Body body, IPropertyChangeListener listener )
	{
		mBody = body;
		mListener = listener;
	}
	
	@Override
	public void run()
	{
		synchronized( mBody )
		{
			while( false == mDisplosed )
			{
				try
				{
					Thread.sleep( 20 );
					mPosition.set( mBody.getPosition() );
					mLinearVelocity.set( mBody.getLinearVelocity() );

					if( true == positionChanged( mPosition ) )
					{
						mPositionProperty.setPosition( mPosition );
						mListener.onChanged( mPositionProperty );
						
						mTempPosition.set( mPosition );
						mTempPosition.sub( mLastPosition );
						mMoveProperty.setDeltas( mTempPosition );
						mListener.onChanged( mMoveProperty );
						
						mLastPosition.set( mPosition );
					}
					if( true == linearVelocityChanged( mLinearVelocity ) )
					{
						mLinearVelocityProperty.setLinearVelocity( mLinearVelocity );
						mListener.onChanged( mLinearVelocityProperty );
					}
					
				}
				catch( InterruptedException e )
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private boolean positionChanged( Vector2 position )
	{
		if( false == mPositionProperty.getPosition().epsilonEquals( position, 0.01f ) )
		{
			return true;
		}
		return false;
	}
	
	private boolean linearVelocityChanged( Vector2 velocity )
	{
		if( false == mLinearVelocityProperty.getLinearVelocity().epsilonEquals( velocity, 0.0f ) )
		{
			return true;
		}
		return false;
	}

	@Override
	public void dispose()
	{
		mDisplosed = true;
	}
}
