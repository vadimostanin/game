package com.mygdx.game_asteroid_3d;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import properties.IProperty;
import properties.PositionProperty;

public class GameShipDraw implements IDraw, IPropertyChangeListener
{
	// G3DJ Model
    public static final String SOLDIER_MODEL_PATH = "/home/vostanin/AndroidStudioProjects/MyGdxGame_Asteroid_3D/ship.g3dj";

	public Environment environment;
	public PerspectiveCamera cam;
	public CameraInputController camController;
	public ModelBatch modelBatch;

    public Model mModel;

    public ModelInstance mModelInstance;

    public AssetManager assets;

	public AnimationController animationController;
	
	Vector2 mTmpVector = new Vector2( 0.0f, 0.0f );
	Vector2 mFinalPosition = new Vector2( 0.0f, 0.0f );
	Vector2 mIntermitentPosition = new Vector2( 0.0f, 0.0f );
	
	private float mLastAngle = 0.0f;

	private Camera mCamera;
	
	public GameShipDraw()
	{
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		modelBatch = new ModelBatch();

		// creates some basic lighting
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		// Here we load our soldierModel
		assets = new AssetManager();
		assets.load(SOLDIER_MODEL_PATH, Model.class);
		assets.finishLoading();

		mModel = assets.get(SOLDIER_MODEL_PATH);
		mModelInstance = new ModelInstance( mModel );
		mModelInstance.transform.rotate( 0, 0, 1, 90 );
		mModelInstance.transform.rotate( 1, 0, 0, 90 );
		mModelInstance.transform.scale( 0.5f, 0.5f, 0.5f );

		mModelInstance.transform.rotate( 0.0f, 1.0f, 0.0f, - 10 );
		mModelInstance.transform.trn( 0.0f, 0.0f, 20.0f );
	}
	
	private float angle( Vector2 vector )
	{
		int tempLinkVertical_vector_x = 0;
		int tempLinkVertical_vector_y = 10;

		int tempLinkHorizantal_vector_x = 10;
		int tempLinkHorizantal_vector_y = 0;
		
		float vectorCosDivPart = (float) Math.sqrt( vector.x * vector.x + vector.y * vector.y );
		
		if( vectorCosDivPart == 0.0f )
		{
			return 0.0f;
		}
		
		float cosRelativeVerticalVector = (float) (( vector.x * tempLinkVertical_vector_x + vector.y * tempLinkVertical_vector_y )
				/
			 ( vectorCosDivPart *
			   Math.sqrt( tempLinkVertical_vector_x * tempLinkVertical_vector_x + tempLinkVertical_vector_y * tempLinkVertical_vector_y )
			 ));
	
		float cosRelativeHorizontalVector = (float) (( vector.x * tempLinkHorizantal_vector_x + vector.y * tempLinkHorizantal_vector_y )
				/
			 ( vectorCosDivPart *
			   Math.sqrt( tempLinkHorizantal_vector_x * tempLinkHorizantal_vector_x + tempLinkHorizantal_vector_y * tempLinkHorizantal_vector_y )
			 ));
	
		float angleCosRelativeVerticalVector = (float)Math.acos( cosRelativeVerticalVector );
		float angleCosRelativeHorizontalVector = (float)Math.acos( cosRelativeHorizontalVector );
	
		float angleCosRelativeVerticalVectorInt = (float) (angleCosRelativeVerticalVector / Math.PI * 180);
		float angleCosRelativeHorizontalVectorInt = (float) (angleCosRelativeHorizontalVector / Math.PI * 180);
	
		float angleResultInt = 0;
		if( angleCosRelativeVerticalVectorInt <= 90 )//first and second quarter
		{
			angleResultInt = angleCosRelativeHorizontalVectorInt;
		}
		else//third and fourth quarter
		{
			angleResultInt = 360 - angleCosRelativeHorizontalVectorInt;
		}
	
		return angleResultInt;
	}
	
	@Override
	public void draw(Batch batch, float deltaTime)
	{
//		animationController.update(Gdx.graphics.getDeltaTime());

		if( null != mCamera )
		{
			// Render the soldierModel
			modelBatch.begin( mCamera );
			modelBatch.render( mModelInstance, environment );
	        //modelBatch.render(boxInstance, environment);
			modelBatch.end();
		}
		

		// Used to run the exported animation
//		animationController = new AnimationController(soldierInstance);
//		animationController.setAnimation("Run", -1);
	}
	
	void setCamera( Camera camera )
	{
		mCamera = camera;
	}
	
	private void deltaPartial( Vector2 fromPosition, Vector2 toPosition, Vector2 deltaPartial, float parts )
	{
		mTmpVector.set( toPosition );
		Vector2 deltaPosition = mTmpVector.sub( fromPosition );
		
		deltaPartial.set( deltaPosition.x / parts, deltaPosition.y / parts );
	}

	@Override
	public void onChanged( IProperty prop )
	{
		if( prop instanceof PositionProperty )
		{
			Vector2 position = ((PositionProperty)prop).getPosition();
			
			deltaPartial( mFinalPosition, position, mTmpVector, 1.0f );

			mModelInstance.transform.trn( mTmpVector.x, mTmpVector.y, 0.0f );
		
			mIntermitentPosition.set( mFinalPosition );
			mFinalPosition.set( position );
		}
		else if( prop instanceof LinearVelocityProperty )
		{
			Vector2 linearVelocity = ((LinearVelocityProperty)prop).getLinearVelocity();			
			float angle = angle( linearVelocity );			
			float rotateDeltaAngle = mLastAngle - angle;			
			mModelInstance.transform.rotate( 0.0f, 1.0f, 0.0f, ( -1 ) * rotateDeltaAngle );			
			mLastAngle = angle;
		}
	}
}
