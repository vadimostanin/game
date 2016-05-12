package com.mygdx.game_asteroid_3d;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController.AnimationDesc;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController.AnimationListener;
import com.badlogic.gdx.graphics.g3d.*;

public class GameBackgroundDraw3D implements IDraw
{
	private int mWidth;
	private int mHeight;
	// G3DJ Model
    public static final String SOLDIER_MODEL_PATH = "/home/vostanin/AndroidStudioProjects/MyGdxGame_Asteroid_3D/star.g3dj";
    public Environment mEnvironment;
    public ModelBatch mModelBatch;
    public Model mBackgroundModel;
    public ModelInstance mBackgroundModelInstance;
    public AssetManager mAssets;
    private AnimationController mAnimation;
    
    class StarsBoundsInfo
    {
    	StarsBoundsInfo( int x, int y )
    	{
    		mX = x;
    		mY = y;
    	}
    	private float mX;
    	private float mY;
    };
    
    final List<StarsBoundsInfo> mStarOffsets = new ArrayList<StarsBoundsInfo>();
	private Camera mCamera;
	
	public GameBackgroundDraw3D( int width, int height )
	{
		mWidth = width;
		mHeight = height;
		
		mModelBatch = new ModelBatch();

		// creates some basic lighting
		mEnvironment = new Environment();
		mEnvironment.set( new ColorAttribute( ColorAttribute.AmbientLight, 1.0f, 1.0f, 1.0f, 1.0f ) );

		// Here we load our soldierModel
		mAssets = new AssetManager();
		mAssets.load(SOLDIER_MODEL_PATH, Model.class);
		mAssets.finishLoading();

		mBackgroundModel = mAssets.get( SOLDIER_MODEL_PATH );
		mBackgroundModelInstance = new ModelInstance( mBackgroundModel );
		
//		mBackgroundModelInstance.transform.translate( 0, 0, 0);
		mBackgroundModelInstance.transform.rotate(1, 0, 0, 90);
		mBackgroundModelInstance.transform.rotate(0, 1, 0, -90);
        //move the model down a bit on the screen ( in a z-up world, down is -z ).
//        mBackgroundModelInstance.transform.translate(0, 0, -2);

		// Used to run the exported animation
		mAnimation = new AnimationController( mBackgroundModelInstance );
      
      // You use an AnimationController to um, control animations.  Each control is tied to the model instance
		mAnimation = new AnimationController( mBackgroundModelInstance );  
      // Pick the current animation by name
		mAnimation.setAnimation("Action", 1, new AnimationListener()
		{
          @Override
          public void onEnd(AnimationDesc animation)
          {
              // this will be called when the current animation is done. 
              // queue up another animation called "balloon". 
              // Passing a negative to loop count loops forever.  1f for speed is normal speed.
        	  mAnimation.queue( "Action", -1, 1.0f, null, 0.0f );
        	  animation.speed = (float)Math.random();
          }

          @Override
          public void onLoop(AnimationDesc animation)
          {
              
          }
          
      });
		
		initStarsOffsets();
	}
	
	private void initStarsOffsets()
	{
		
		
		for( int height_i = - mHeight / 2 ; height_i < mHeight / 2 ;  )
		{
			int offset_y = 30;
			
			for( int width_i = - mWidth / 2 ; width_i < mWidth / 2 ;  )
			{
				int offset_x = 30;
				mStarOffsets.add( new StarsBoundsInfo( width_i, height_i ) );
				
				width_i += offset_x;
			}
			height_i += offset_y;
		}
	}
	
	public void setCamera( Camera camera )
	{
		mCamera = camera;
	}
	
	@Override
	public void draw(Batch batch, float deltaTime)
	{
		mAnimation.update(Gdx.graphics.getDeltaTime());
		// Render the soldierModel
		
		if( null != mCamera )
		{
			mModelBatch.begin( mCamera );
			
			for( StarsBoundsInfo starOffset : mStarOffsets )
			{	
				mBackgroundModelInstance.transform.translate( starOffset.mX, 0, starOffset.mY );
				mModelBatch.render( mBackgroundModelInstance, mEnvironment );
				
				mBackgroundModelInstance.transform.translate( - starOffset.mX, 0, - starOffset.mY );
			}
	        //modelBatch.render(boxInstance, environment);
			mModelBatch.end();
		}
	}

}
