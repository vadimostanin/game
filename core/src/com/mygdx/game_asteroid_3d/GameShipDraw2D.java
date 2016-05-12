package com.mygdx.game_asteroid_3d;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game_asteroid_3d.TexturesCache.Texture_Types;

import properties.IProperty;
import properties.MoveProperty;
import properties.PositionProperty;

public class GameShipDraw2D implements IDraw, IPropertyChangeListener
{
	Vector2 mTmpVector = new Vector2( 0.0f, 0.0f );
	Vector2 mFinalPosition = new Vector2( 0.0f, 0.0f );
//	Vector2 mIntermitentPosition = new Vector2( 0.0f, 0.0f );
	
	private float mLastAngle = 0.0f;

	private Texture mShipTexture;
	private SpriteBatch mBatch;
	
//	private int mTextureWidth = 0;
//	private int mTextureHeight = 0;
	
//	private float mRotationAngle = 0.0f;
	
	private Sprite mShipSprite;
	
	public GameShipDraw2D( float x, float y )
	{
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		mShipTexture = TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_SHIP );
		mShipSprite = new Sprite( mShipTexture );
		mBatch = new SpriteBatch();
		
		mShipSprite.setScale( 0.25f );
//		mShipeSprite.setOrigin( 0, 0 );
		mShipSprite.setOriginCenter();
		mShipSprite.setPosition( x - mShipSprite.getWidth() / 2, y - mShipSprite.getHeight() / 2 );
		
//		mTextureWidth = (int)( mShipTexture.getWidth() * 0.5f );
//		mTextureHeight = (int)( mShipTexture.getHeight() * 0.5f );
	}
	
	@Override
	public void draw(Batch batch, float deltaTime)
	{
//		animationController.update(Gdx.graphics.getDeltaTime());

		mBatch.begin();
//			mBatch.draw( mShipTexture, mFinalPosition.x, mFinalPosition.y, - mTextureWidth / 2, - mTextureHeight / 2,
//					(float)mTextureWidth, (float)mTextureHeight, 0.5f, 0.5f, mRotationAngle, 0, 0, (int)( mShipTexture.getWidth() ), (int)( mShipTexture.getHeight() ), false, false);

		mShipSprite.draw( mBatch );
		mBatch.end();
		

		// Used to run the exported animation
//		animationController = new AnimationController(soldierInstance);
//		animationController.setAnimation("Run", -1);
	}
	
	void setCamera( Camera camera )
	{
		;
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

//			mModelInstance.transform.trn( mTmpVector.x, mTmpVector.y, 0.0f );
		
//			mIntermitentPosition.set( mFinalPosition );
			mFinalPosition.set( position.x + Gdx.graphics.getWidth() / 2, position.y + Gdx.graphics.getHeight() / 2 );
			mShipSprite.setPosition( mFinalPosition.x - mShipSprite.getWidth() / 2, mFinalPosition.y - mShipSprite.getHeight() / 2 );
		}
		else if( prop instanceof LinearVelocityProperty )
		{
			Vector2 linearVelocity = ((LinearVelocityProperty)prop).getLinearVelocity();			
			float angle = Utils.angle( linearVelocity );
//			mRotationAngle = angle;
			
			float rotateDeltaAngle = angle - mLastAngle;
			mShipSprite.rotate( rotateDeltaAngle );
//			mModelInstance.transform.rotate( 0.0f, 1.0f, 0.0f, ( -1 ) * rotateDeltaAngle );			
			mLastAngle = angle;
		}
	}
}
