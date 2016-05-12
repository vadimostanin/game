package com.mygdx.game_asteroid_3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game_asteroid_3d.TexturesCache.Texture_Types;

import properties.GravityRadiusProperty;
import properties.IProperty;
import properties.PositionProperty;

public class GravityBodyDraw extends Actor implements IDraw, IPropertyChangeListener
{	
	private float RADIUS = GamePlayScreen.mScreenProportionalConvertor.proportionalYscale( 5.0f );
	private float DIAMETR = RADIUS * 2;
    
    private ModelBatch mModelBatch;
    private ModelInstance mInstanceSphere;
	private ModelInstance mInstanceSphereField;
	private Environment mEnvironment;
	
	private float mCurrentFieldRadius = 3.0f;
	private float mLastGravFieldRadius = 3.0f;
	private long mAttributes = 0;
	private Texture mTexture;
	private ModelBuilder mModelBuilder;
	
	private Camera mCamera;
	
	BitmapFont mFont = new BitmapFont();
	
	private float currFrame = 19;
	private TextureRegion allFrames[];
	
	private Vector2 mPosition = new Vector2();
	
	GravityBodyDraw()
	{
		String storagePaths = Gdx.files.getLocalStoragePath();
	    ShaderProgram.pedantic = false;
	    
	    mModelBatch = new ModelBatch();
	    mModelBuilder = new ModelBuilder();
	    
	    mEnvironment = new Environment();
	    mEnvironment.set( new ColorAttribute( ColorAttribute.AmbientLight, 1f, 1f, 1f, 1f ) );
		mEnvironment.add( new DirectionalLight().set( 8f, 0.8f, 0.8f, 0.0f, 0.0f, 0.2f ) );
		
		mTexture = new Texture(Gdx.files.absolute( storagePaths + "vectorstock_5270709.png" ) );
		mAttributes = Usage.Position | Usage.Normal | Usage.TextureCoordinates;
		
		initSphere();
		initSphereField();
		
		mFont.setColor( Color.RED );
		
		allFrames = new TextureRegion[20];
		{
			allFrames[0] = new TextureRegion( TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME1 ) );
			allFrames[1] = new TextureRegion( TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME2 ) );
			allFrames[2] = new TextureRegion( TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME3 ) );
			allFrames[3] = new TextureRegion( TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME4 ) );
			allFrames[4] = new TextureRegion( TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME5 ) );
			allFrames[5] = new TextureRegion( TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME6 ) );
			allFrames[6] = new TextureRegion( TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME7 ) );
			allFrames[7] = new TextureRegion( TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME8 ) );
			allFrames[8] = new TextureRegion( TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME9 ) );
			allFrames[9] = new TextureRegion( TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME10 ) );
			allFrames[10] = new TextureRegion( TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME11 ) );
			allFrames[11] = new TextureRegion( TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME12 ) );
			allFrames[12] = new TextureRegion( TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME13 ) );
			allFrames[13] = new TextureRegion( TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME14 ) );
			allFrames[14] = new TextureRegion( TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME15 ) );
			allFrames[15] = new TextureRegion( TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME16 ) );
			allFrames[16] = new TextureRegion( TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME17 ) );
			allFrames[17] = new TextureRegion( TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME18 ) );
			allFrames[18] = new TextureRegion( TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME19 ) );
			allFrames[19] = new TextureRegion( TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME20 ) );
		}
	}
	
	private void initSphere()
	{
		final Material materialSphere = new Material( TextureAttribute.createDiffuse( mTexture ), 
				  ColorAttribute.createSpecular(1, 1, 1, 1),
				  FloatAttribute.createShininess(8f));
		float spehereDiametr = DIAMETR;
		final Model sphere = mModelBuilder.createSphere( spehereDiametr, spehereDiametr, spehereDiametr, 24, 24, materialSphere, mAttributes );
		
		mInstanceSphere = new ModelInstance( sphere );
	}
	
	private void initSphereField()
	{
//		Vector2 position = mGravityBody.getPosition();

		float orthographicCameraGravFieldVidualRatio = 1.0f;
		float sphereFieldDiametr = DIAMETR * mLastGravFieldRadius * orthographicCameraGravFieldVidualRatio;
		final Material materialSphereField = new Material(TextureAttribute.createDiffuse( mTexture ), ColorAttribute.createSpecular(1, 1, 1, 1),
				FloatAttribute.createShininess( 0.8f ), new BlendingAttribute( (float)( ( Math.random() / 2 + 0.5 ) / 2.0f ) ) );
		final Model sphereField = mModelBuilder.createSphere( DIAMETR, sphereFieldDiametr, sphereFieldDiametr, 24, 24, materialSphereField, mAttributes );
		mInstanceSphereField = new ModelInstance( sphereField );
//		mInstanceSphereField.transform.trn( position.x, position.y, 20.0f );
		mInstanceSphereField.transform.rotate( 0, 1, 0, 90 );
	}
	
	public void setCamera( Camera camera )
	{
		mCamera = camera;
	}
	
	@Override
	public void draw( Batch batch, float deltaTime )
	{
		int originX = (int) getOriginX();
		int leftX = (int) getX( Align.left );
		int x = (int) getX();
		int y = (int) getY();
		int w = (int) getWidth();
		int h = (int) getHeight();
//		Gdx.gl.glViewport( x, y, 150, 150 );
		
		if( currFrame <= 0 )
	    	currFrame = 19;
		
		mInstanceSphereField.materials.get(0).get( TextureAttribute.class, TextureAttribute.Diffuse).set( allFrames[ (int)currFrame ] );
		currFrame -= 0.5;
		
		if( null != mCamera )
		{
			mModelBatch.begin( mCamera );
	        mModelBatch.render( mInstanceSphere, mEnvironment );
	        mModelBatch.flush();
	        mModelBatch.render( mInstanceSphereField, mEnvironment );
	        mModelBatch.flush();
	        mModelBatch.end();
		}
        
//        batch.begin();
//        batch.draw( mTexture, x, y);
//        if( false == mGravityBody.getLastForce().equals( mEmptyVector ) )
//        {
//        	mFont.draw( batch, mGravityBody.getLastForce().toString(), 50, 50 );
//        }
//        batch.end();
//        batch.flush();
	}

	@Override
	public void onChanged(IProperty prop)
	{
		if( prop instanceof PositionProperty )
		{
			mPosition.set( ((PositionProperty)prop).getPosition() );
			mInstanceSphere.transform.trn( mPosition.x, mPosition.y, 20.0f );
			mInstanceSphereField.transform.trn( mPosition.x, mPosition.y, 20.0f );
		}
		else if( prop instanceof GravityRadiusProperty )
		{
			mCurrentFieldRadius = ((GravityRadiusProperty)prop).getRadius();
			
			if( mLastGravFieldRadius != mCurrentFieldRadius )
			{
				float ratio = mCurrentFieldRadius / mLastGravFieldRadius;
				mInstanceSphereField.transform.scale( ratio, ratio, ratio );
				
//				initSphereField();
				
				mLastGravFieldRadius = mCurrentFieldRadius;
			}
		}
	}
}
