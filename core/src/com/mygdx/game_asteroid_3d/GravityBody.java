/**
 * 
 */
package com.mygdx.game_asteroid_3d;

import java.io.FileWriter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;

import properties.GravityRadiusProperty;
import properties.IProperty;
import properties.PositionProperty;

/**
 * @author vadim
 *
 */
public class GravityBody extends GameActiveObject implements IPropertyChangeListener
{
	private float mGravityRadius = 3.0f;
	private Vector2 mEmptyVector = new Vector2( 0.0f, 0.0f );
	private Vector2 mForce = new Vector2( 0.0f, 0.0f );
	private Vector2 mLastForce;
	private Vector2 mPosition = new Vector2();
	
//	private FileWriter mCSVFileWriter;
	
	public GravityBody( Body body )
	{
		super( body );
		mLastForce = mEmptyVector;
		String sStorageDir = Gdx.files.getLocalStoragePath();
		try{
//			mCSVFileWriter = new FileWriter( sStorageDir + "distance_force.csv" );
//			mCSVFileWriter.append( "Distance,Force\n" );
		}
		catch( Exception e ){
			Gdx.app.exit();
		}
	}
	
	public float getGravityRadius()
	{
		return mGravityRadius;
	}
	
	public Vector2 getLastForce()
	{
		return mLastForce;
	}
	
	public Vector2 getPosition()
	{
		return mPosition;
	}
	
	public Vector2 getGravityForce2( Body influencedBody )
	{
		if( getBody().getFixtureList().size == 0 )
		{
			return mEmptyVector;
		}
		float freeAccel = 981f;
		float influencedBodyMass = influencedBody.getMass();
		Vector2 debrisPosition = influencedBody.getWorldCenter();
		Vector2 planetDistance = new Vector2( 0.0f, 0.0f );
		CircleShape planetShape = (CircleShape) getBody().getFixtureList().get( 0 ).getShape();
		float planetRadius = planetShape.getRadius();
		Vector2 planetPosition = getBody().getWorldCenter();
		
		planetDistance.add( debrisPosition );
		planetDistance.sub( planetPosition );
		float finalDistance = planetDistance.len();
//		Vector2 worldGravity = new Vector2( 1.0f, 1.0f );//world.getGravity();
		float distanceFreeAccel = freeAccel * ( 1 / ( 1 + finalDistance ) );
		float force = distanceFreeAccel * influencedBodyMass / 4;
//		Vector2 force = ( worldGravity.scl( worldGravity ).scl( ballMass ) );
//			Vector2 force = ( worldGravity.scl( worldGravity ).scl( ball.getMass() ) ).scl( 1.0f / (float)Math.pow( planetDistance.len(), 2 ) );
		
		float manualMagicGravRadiusRatio = 10.0f;
		
		if( finalDistance > manualMagicGravRadiusRatio * getGravityRadius() )
		{
			mLastForce = mEmptyVector;
			return mEmptyVector;
		}
		planetDistance.set( planetDistance.x < 0 ? -1.0f : 1.0f, planetDistance.y < 0 ? -1.0f : 1.0f );
		planetDistance.scl( -1 );
//		float vecSum = Math.abs( planetDistance.x ) + Math.abs( planetDistance.y );
//		planetDistance.scl( ( 1 / vecSum ) * planetRadius / finalDistance );
		planetDistance.scl( force );
		
		try{
//			mCSVFileWriter.append( (new Float( finalDistance ) ).toString() + "," + (new Float( force ) ).toString() + "\n" );
		}
		catch( Exception e ){
			Gdx.app.exit();
		}

		mLastForce = planetDistance;
		return planetDistance;
	}
	
	public Vector2 getGravityForce( Body influencedBody )
	{
		float gravMass = getBody().getMass();
		float influencedBodyMass = influencedBody.getMass();
		gravMass = Math.max( gravMass, influencedBodyMass );
		
		float freeAccel = 981f;
		
		
		Vector2 debrisPosition = influencedBody.getWorldCenter();
		Vector2 planetDistance = new Vector2( 0.0f, 0.0f );
		mForce.set( 1.0f, 1.0f );
		CircleShape planetShape = (CircleShape) getBody().getFixtureList().get( 0 ).getShape();
		float planetRadius = planetShape.getRadius();
		Vector2 planetPosition = getBody().getWorldCenter();
		
		planetDistance.add( debrisPosition );
		planetDistance.sub( planetPosition );
		float finalDistance = planetDistance.len();
		float distanceFreeAccel = freeAccel * ( 1 / ( 1 + finalDistance ) );
		float force = distanceFreeAccel * influencedBodyMass;
//			Vector2 force = ( worldGravity.scl( worldGravity ).scl( ball.getMass() ) ).scl( 1.0f / (float)Math.pow( planetDistance.len(), 2 ) );
		
		if( finalDistance > planetRadius * getGravityRadius() )
		{
			mLastForce = planetDistance;
			return mEmptyVector;
		}
		mForce.set( planetDistance );
		mForce.scl( force );
		mForce.scl( -1 );
		mForce.sub( planetDistance );

//		mForce.set( planetDistance );
		if( mForce == mEmptyVector )
		{
			int a = 0;
			a++;
		}
		mLastForce.set( mForce );
		return mForce;
	}

	@Override
	public void onChanged(IProperty prop)
	{
		if( prop instanceof GravityRadiusProperty )
		{
			mGravityRadius = ((GravityRadiusProperty)prop).getRadius();
		}
		else if( prop instanceof PositionProperty )
		{
			mPosition.set( ((PositionProperty)prop).getPosition() );
		}
	}
}
