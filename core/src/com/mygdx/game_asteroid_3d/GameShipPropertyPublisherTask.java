package com.mygdx.game_asteroid_3d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Disposable;

import properties.MoveProperty;
import properties.PositionProperty;

public class GameShipPropertyPublisherTask implements Runnable, Disposable
{
	private Body mBody;
	private boolean mDisplosed = false;
	private PropertyChangeListeners mListeners = new PropertyChangeListeners();
	private PositionProperty mPositionProperty = new PositionProperty();
	private LinearVelocityProperty mLinearVelocityProperty = new LinearVelocityProperty();
	private MoveProperty mMoveProperty = new MoveProperty();

	private Vector2 mMove = new Vector2();
	private Vector2 mTempPosition = new Vector2();
	private Vector2 mLastPosition = new Vector2();

	private Vector2 mPosition = new Vector2();
	private Vector2 mLinearVelocity = new Vector2();
	
	static private GameShipPropertyPublisherTask mInstance = new GameShipPropertyPublisherTask();
	
	static public GameShipPropertyPublisherTask getInstance()
	{
		return mInstance;
	}
	
	private GameShipPropertyPublisherTask()
	{
		
	}
	
	public void setShipBody( Body body )
	{
		synchronized( mInstance )
		{
			mBody = body;
		}
	}
	
	public void addListener( IPropertyChangeListener listener )
	{
		synchronized( mInstance )
		{
			mListeners.add( listener );
		}
	}
	
	public void addListeners( PropertyChangeListeners listeners )
	{
		synchronized( mInstance )
		{
			mListeners.add( listeners );
		}
	}
	
	@Override
	public void run()
	{
		while( false == mDisplosed )
		{
			try
			{
				synchronized( mInstance )
				{	
					Thread.sleep( 20 );
					if( null == mBody )
					{
						continue;
					}
					mPosition.set( mBody.getPosition() );
					mLinearVelocity.set( mBody.getLinearVelocity() );

					if( true == positionChanged( mPosition ) )
					{
						mPositionProperty.setPosition( mPosition );
						mListeners.onChanged( mPositionProperty );
						
						mTempPosition.set( mPosition );
						mTempPosition.sub( mLastPosition );
						mMoveProperty.setDeltas( mTempPosition );
						mListeners.onChanged( mMoveProperty );
						
						mLastPosition.set( mPosition );
					}
					if( true == linearVelocityChanged( mLinearVelocity ) )
					{
						mLinearVelocityProperty.setLinearVelocity( mLinearVelocity );
						mListeners.onChanged( mLinearVelocityProperty );
					}
					
				}
			}
			catch( InterruptedException e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
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
