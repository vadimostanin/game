package com.mygdx.game_asteroid_3d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.utils.Disposable;

import properties.IProperty;
import properties.MoveProperty;
import properties.PositionProperty;

public class GameWorldBounds implements IPropertyChangeListener, Disposable
{
	private Vector2[] mBounds;
	private Vector2 mBottomLeft;
	private Vector2 mBottomRight;
	private Vector2 mTopRight;
	private Vector2 mTopLeft;
	private float mWidth;
	private float mHeight;
	
	private World mWorld;
	private Body mGroundBody;
	private Body mMousePivotBody;
	private MouseJoint mMouseJoint;
	private Vector2 mTempPoint = new Vector2();
	private Vector2 mPosition = new Vector2();
	
	private boolean mInitialMoved = false;
	
	public GameWorldBounds( World world, float width, float height )
	{
		mWorld = world;
		mWidth = width;
		mHeight = height;
		
		float halfWidth = mWidth / 2f;
		float halfHeight = mHeight / 2f;
		
		mBottomLeft = new Vector2( -halfWidth + 50, -halfHeight + 50 );
		mTopLeft = new Vector2( -halfWidth + 50,  halfHeight - 50 );
		mTopRight = new Vector2( halfWidth + 150, halfHeight - 50 );
		mBottomRight = new Vector2( halfWidth + 150,  -halfHeight + 50 );
		
		mBounds = new Vector2[] {
				mBottomLeft,
				mTopLeft,
				mTopRight,
				mBottomRight,					
				 };

		createBody();
		
//		createMousePivot();
		
//		jointing( 100, 100 );
	}
	
	private void createBody()
	{
		ChainShape chainShape = new ChainShape();
		chainShape.createLoop( mBounds );
		
		BodyDef chainBodyDef = new BodyDef();
		chainBodyDef.type = BodyDef.BodyType.DynamicBody;
		
		mGroundBody = mWorld.createBody(chainBodyDef);
		mGroundBody.createFixture(chainShape, 0);
		
		chainShape.dispose();
	}
	
	private void createMousePivot()
	{
		CircleShape mousePivotShape = new CircleShape();
		mousePivotShape.setRadius( 10.0f );
		
		FixtureDef mousePivotFixtureDef = new FixtureDef();
		mousePivotFixtureDef.restitution = 0.9f;
		mousePivotFixtureDef.friction = 0.1f;
		mousePivotFixtureDef.shape = mousePivotShape;
		mousePivotFixtureDef.density = 1f;
		BodyDef mousePivotDef = new BodyDef();
		mousePivotDef.type = BodyDef.BodyType.StaticBody;
		
		mousePivotDef.position.x = mBottomLeft.x - 3000;
		mousePivotDef.position.y = mBottomLeft.y - 3000;
		
		mMousePivotBody = mWorld.createBody( mousePivotDef );
		mMousePivotBody.createFixture( mousePivotFixtureDef );
		mousePivotShape.dispose();
	}
	
	public void jointing( float x, float y )
	{
		MouseJointDef def = new MouseJointDef();
		def.bodyA = mMousePivotBody;
		def.bodyB = mGroundBody;			
		def.collideConnected = true;
		def.target.set( x, y );
		def.maxForce = 10000.0f * mGroundBody.getMass();

		mMouseJoint = (MouseJoint) mWorld.createJoint( def );
		mGroundBody.setAwake( true );
	}
	
	private void move( float x, float y )
	{
		mTempPoint.set( x, y );
		mMouseJoint.setTarget( mTempPoint );
	}
	
	public Body getGroundBody()
	{
		return mGroundBody;
	}
	
	public Vector2[] getBounds()
	{
		return mBounds;
	}
	
	public Vector2 getTopLeft()
	{
		return mTopLeft;
	}
	
	public Vector2 getTopRight()
	{
		return mTopRight;
	}
	
	public Vector2 getBottomLeft()
	{
		return mBottomLeft;
	}
	
	public Vector2 getBottomRight()
	{
		return mBottomRight;
	}
	
	public float getWidth()
	{
		return Math.abs( ( ( mBottomRight.x - mBottomLeft.x ) + ( mTopRight.x - mTopLeft.x ) ) / 2.0f );
	}
	
	public float getHeight()
	{
		return Math.abs( ( ( mTopRight.y - mBottomRight.y ) + ( mTopLeft.y - mBottomLeft.y ) ) / 2.0f );
	}
	
	@Override
	public void onChanged(IProperty prop)
	{
//		if( prop instanceof PositionProperty )
//		{
//			if( true == mInitialMoved )
//			{
//				Vector2 position = ((PositionProperty)prop).getPosition();
//				move( position.x, -position.y );
//			}
//			else
//			{
//				mInitialMoved = true;
//			}
//		}
		if( prop instanceof MoveProperty )
		{
			if( true == mInitialMoved )
			{
				Vector2 delta = ((MoveProperty)prop).getDelta();
				mPosition.add( -delta.x / 2, delta.y );
//				move( mPosition.x, -mPosition.y );
			}
			else
			{
				mInitialMoved = true;
			}
		}
	}

	@Override
	public void dispose()
	{
		mWorld.destroyJoint( mMouseJoint );
		mWorld.destroyBody( mGroundBody );
		mWorld.destroyBody( mMousePivotBody );
	}
}
