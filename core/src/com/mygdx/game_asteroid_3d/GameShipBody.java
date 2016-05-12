package com.mygdx.game_asteroid_3d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;

import properties.IProperty;

public class GameShipBody
{
	private float mX = 0.0f;
	private float mY = 0.0f;
	private World mWorld;
	private Body mBody;
	private Body mMousePivotBody;
	private MouseJoint mMouseJoint;
	
	Vector2 mTempPoint = new Vector2();
	
	public GameShipBody( World world, float x, float y )
	{
		mWorld = world;
		mX = x;
		mY = y;
		createBody();
		createMousePivot();
	}
	
	private void createBody()
	{
		CircleShape ballShape = new CircleShape();
		ballShape.setRadius( GamePlayScreen.mScreenProportionalConvertor.proportionalYscale( 10.0f ) );

		FixtureDef def = new FixtureDef();
		def.restitution = 0.9f;
		def.friction = 0.1f;
		def.shape = ballShape;
		def.density = 1f;
		BodyDef boxBodyDef = new BodyDef();
		boxBodyDef.type = BodyDef.BodyType.DynamicBody;

		// Create the BodyDef, set a random position above the
		// ground and create a new body
		boxBodyDef.position.x = mX;
//			boxBodyDef.position.x = mLevel.mActiveObject.Position.x;
		boxBodyDef.position.y = mY;
//			boxBodyDef.position.y = mLevel.mActiveObject.Position.y;			
		boxBodyDef.fixedRotation = true;
		mBody = mWorld.createBody( boxBodyDef );
		MassData massData = mBody.getMassData();
		massData.mass = 1.0f;
		mBody.setMassData( massData );
		mBody.createFixture( def );
		mBody.setUserData( new BodyTypePlayer() );
		ballShape.dispose();
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
		
		mousePivotDef.position.x = 0;
		mousePivotDef.position.y = 0;
		
		mMousePivotBody = mWorld.createBody( mousePivotDef );
		mMousePivotBody.createFixture( mousePivotFixtureDef );
		mousePivotShape.dispose();
	}
	
	public void jointing( float x, float y )
	{
		MouseJointDef def = new MouseJointDef();
		def.bodyA = mMousePivotBody;
		def.bodyB = mBody;			
		def.collideConnected = true;
		def.target.set( x, y );
		def.maxForce = 10000.0f * mBody.getMass();

		mMouseJoint = (MouseJoint) mWorld.createJoint( def );
		mBody.setAwake( true );
	}
	
	public void moveJointed( float x, float y )
	{
		if( false == isJointing() )
		{
			return;
		}
		mTempPoint.set( x, y );
		mMouseJoint.setTarget(mTempPoint);
	}
	
	public void unjointing()
	{
		if( null == mMouseJoint )
		{
			return;
		}
		mWorld.destroyJoint( mMouseJoint );
		mMouseJoint = null;
	}
	
	public boolean isJointing()
	{
		return null != mMouseJoint;
	}
	
	public Body getBody()
	{
		return mBody;
	}
	
	public float getX()
	{
		return mX;
	}
	
	public float getY()
	{
		return mY;
	}
}
