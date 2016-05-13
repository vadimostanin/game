package com.mygdx.game_asteroid_3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game_asteroid_3d.TexturesCache.Texture_Types;

import properties.GravityRadiusProperty;
import properties.IProperty;
import properties.MoveProperty;
import properties.PositionProperty;

public class GravityBodyDraw2D extends Actor implements IDraw, IPropertyChangeListener, Disposable
{	
	private float RADIUS = GamePlayScreen.mScreenProportionalConvertor.proportionalYscale( 5.0f );
	private float DIAMETR = RADIUS * 2;
    
	private ParticleEffect mCenterPe;
	
	private SpriteBatch mSpriteBatch;
	
    private ModelBatch mModelBatch;
    private Environment mEnvironment;
	
	private float mCurrentFieldRadius = 3.0f;
	private float mLastGravFieldRadius = 3.0f;
	private long mAttributes = 0;
//	private Texture mTexture;
//	private ModelBuilder mModelBuilder;
	
	private Camera mCamera;
	
	BitmapFont mFont = new BitmapFont();
	
	private float currFrame = 19;
	private Texture allFrames[];
	private Sprite mFieldSprite;
	
	private Vector2 mPosition = new Vector2();
	private Matrix4 mTranformation = new Matrix4();
	
	public GravityBodyDraw2D( float x, float y )
	{
		String storagePaths = Gdx.files.getLocalStoragePath();
//	    ShaderProgram.pedantic = false;
	    
	    mSpriteBatch = new SpriteBatch();
	    mFieldSprite = new Sprite();
//	    mModelBatch = new ModelBatch();
//	    mModelBuilder = new ModelBuilder();
	    
//	    mEnvironment = new Environment();
//	    mEnvironment.set( new ColorAttribute( ColorAttribute.AmbientLight, 1f, 1f, 1f, 1f ) );
//		mEnvironment.add( new DirectionalLight().set( 8f, 0.8f, 0.8f, 0.0f, 0.0f, 0.2f ) );
		
//		mTexture = new Texture(Gdx.files.absolute( storagePaths + "vectorstock_5270709.png" ) );
//		mAttributes = Usage.Position | Usage.Normal | Usage.TextureCoordinates;
		
		initSphere();
		initSphereField();
		
		mFont.setColor( Color.RED );
		
		mCenterPe = new ParticleEffect();
		mCenterPe.load( Gdx.files.absolute( storagePaths + "holo_circle2/particle" ), Gdx.files.absolute( storagePaths + "holo_circle2/" ) );
		mCenterPe.start();
		mCenterPe.scaleEffect( 0.5f );
		
		setCenterPePosition( x + Gdx.graphics.getWidth() / 2, y + Gdx.graphics.getHeight() / 2 );
		mFieldSprite.setPosition( x + Gdx.graphics.getWidth() / 2 - allFrames[0].getWidth() / 2, y + Gdx.graphics.getHeight() / 2 - allFrames[0].getHeight() / 2 );
		
		mFieldSprite.setScale( mCurrentFieldRadius * 0.1f );
		
		mTranformation.setToOrtho2D( 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() );
	}
	
	private void initSphere()
	{
		
	}
	
	private void initSphereField()
	{
//		Vector2 position = mGravityBody.getPosition();

		float orthographicCameraGravFieldVidualRatio = 1.0f;
		float sphereFieldDiametr = DIAMETR * mLastGravFieldRadius * orthographicCameraGravFieldVidualRatio;
//		final Material materialSphereField = new Material(TextureAttribute.createDiffuse( mTexture ), ColorAttribute.createSpecular(1, 1, 1, 1),
//				FloatAttribute.createShininess( 0.8f ), new BlendingAttribute( (float)( ( Math.random() / 2 + 0.5 ) / 2.0f ) ) );
//		final Model sphereField = mModelBuilder.createSphere( DIAMETR, sphereFieldDiametr, sphereFieldDiametr, 24, 24, materialSphereField, mAttributes );
		
		allFrames = new Texture[20];
		{
			allFrames[0] = TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME1 );
			allFrames[1] = TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME2 );
			allFrames[2] = TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME3 );
			allFrames[3] = TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME4 );
			allFrames[4] = TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME5 );
			allFrames[5] = TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME6 );
			allFrames[6] = TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME7 );
			allFrames[7] = TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME8 );
			allFrames[8] = TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME9 );
			allFrames[9] = TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME10 );
			allFrames[10] = TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME11 );
			allFrames[11] = TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME12 );
			allFrames[12] = TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME13 );
			allFrames[13] = TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME14 );
			allFrames[14] = TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME15 );
			allFrames[15] = TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME16 );
			allFrames[16] = TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME17 );
			allFrames[17] = TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME18 );
			allFrames[18] = TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME19 );
			allFrames[19] = TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME20 );
		}
		
		mSpriteBatch.setColor( 0.7f, 0.7f, 0.7f, 0.5f );
		mFieldSprite.setOriginCenter();
	}
	
	@Override
	public void draw( Batch batch, float deltaTime )
	{
//		int originX = (int) getOriginX();
//		int leftX = (int) getX( Align.left );
//		int x = (int) getX();
//		int y = (int) getY();
//		int w = (int) getWidth();
//		int h = (int) getHeight();
//		Gdx.gl.glViewport( x, y, 150, 150 );
		
		if( currFrame <= 0 )
	    	currFrame = 19;
		
//		mInstanceSphereField.materials.get(0).get( TextureAttribute.class, TextureAttribute.Diffuse).set( allFrames[ (int)currFrame ] );
		currFrame -= 0.5;

		mSpriteBatch.setProjectionMatrix( mTranformation );
		
		mSpriteBatch.begin();
			mCenterPe.draw( mSpriteBatch, Gdx.graphics.getDeltaTime() );
		mSpriteBatch.end();
		
		mSpriteBatch.begin();
			mFieldSprite.setTexture( allFrames[ (int)currFrame ] );
			mFieldSprite.setRegion( 0, 0, (int)allFrames[ (int)currFrame ].getWidth(), (int)allFrames[ (int)currFrame ].getHeight() );
			mFieldSprite.setColor( 1, 1, 1, 0.5f );
			mFieldSprite.setSize( Math.abs( (int)allFrames[ (int)currFrame ].getWidth() ), Math.abs( (int)allFrames[ (int)currFrame ].getHeight() ) );
			mFieldSprite.setOriginCenter();
//			mFieldSprite.rotate( 1 );
			mFieldSprite.draw( mSpriteBatch );
//			mSpriteBatch.draw( allFrames[ (int)currFrame ], mPosition.x + Gdx.graphics.getWidth() / 2 - allFrames[ (int)currFrame ].getWidth() / 2, mPosition.y + Gdx.graphics.getHeight() / 2 - allFrames[ (int)currFrame ].getHeight() / 2 );
		mSpriteBatch.end();
		
//		if( null != mCamera )
//		{
//			mModelBatch.begin( mCamera );
//	        mModelBatch.render( mInstanceSphere, mEnvironment );
//	        mModelBatch.flush();
//	        mModelBatch.render( mInstanceSphereField, mEnvironment );
//	        mModelBatch.flush();
//	        mModelBatch.end();
//		}
        
//        mSpriteBatch.begin();
//        mSpriteBatch.draw( mTexture, x, y);
//        if( false == mGravityBody.getLastForce().equals( mEmptyVector ) )
//        {
//        	mFont.draw( mSpriteBatch, mGravityBody.getLastForce().toString(), 50, 50 );
//        }
//        mSpriteBatch.end();
//        batch.flush();
	}
	
	private void setCenterPePosition( float x, float y )
	{
		for( ParticleEmitter emitter : mCenterPe.getEmitters() )
		{
			emitter.setPosition( x, y );
		}
	}

	@Override
	public void onChanged(IProperty prop)
	{
//		if( prop instanceof PositionProperty )
//		{
//			mPosition.set( ((PositionProperty)prop).getPosition() );
//			setCenterPePosition( mPosition.x + Gdx.graphics.getWidth() / 2, mPosition.y + Gdx.graphics.getHeight() / 2 );
//			mFieldSprite.setPosition( mPosition.x + Gdx.graphics.getWidth() / 2 - allFrames[0].getWidth() / 2, mPosition.y + Gdx.graphics.getHeight() / 2 - allFrames[0].getHeight() / 2 );
//		}
		if( prop instanceof MoveProperty )
		{
			Vector2 delta = ((MoveProperty)prop).getDelta();
			mTranformation.translate( ( -1 ) * delta.x, ( -1 ) * delta.y, 0 );
		}
		else if( prop instanceof GravityRadiusProperty )
		{
			mCurrentFieldRadius = ((GravityRadiusProperty)prop).getRadius();
			
			if( mLastGravFieldRadius != mCurrentFieldRadius )
			{
				float ratio = mCurrentFieldRadius / mLastGravFieldRadius;
//				mInstanceSphereField.transform.scale( ratio, ratio, ratio );
				
//				initSphereField();
				
				final float impericalMagicNumber = 0.075f;
				
				mFieldSprite.setScale( mCurrentFieldRadius * impericalMagicNumber );
				
				mLastGravFieldRadius = mCurrentFieldRadius;
			}
		}
	}

	@Override
	public void dispose()
	{
		mCenterPe.dispose();		
	}
}
