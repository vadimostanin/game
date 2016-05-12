package com.mygdx.game_asteroid_3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class GameBallDraw implements IDraw
{
	private Body mBody;
	private Texture mTexture;
	
	private ModelBatch modelBatch;
	private ModelInstance instanceSphere;
	private ModelInstance instanceBox;
	private Environment environment;

	private float RADIUS = GamePlayScreen.mScreenProportionalConvertor.proportionalYscale( 1.0f );
	private float DIAMETR = RADIUS * 2;
	
	Vector2 mLastPosition = new Vector2( 0.0f, 0.0f );
	Vector2 mTmpVector = new Vector2( 0.0f, 0.0f );
	private Camera mCamera;

	GameBallDraw( Body body )
	{
		mBody = body;
		
		modelBatch = new ModelBatch();
	    
	    environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
		
		ModelBuilder modelBuilder = new ModelBuilder();
		String storagePaths = Gdx.files.getLocalStoragePath();
		mTexture = new Texture( Gdx.files.absolute( storagePaths + "earth.png" ) );
		
		final Material material = new Material(TextureAttribute.createDiffuse(mTexture), ColorAttribute.createSpecular(1, 1, 1, 1),
			FloatAttribute.createShininess(8f));
		final long attributes = Usage.Position | Usage.Normal | Usage.TextureCoordinates;

		final Model sphere = modelBuilder.createSphere(DIAMETR, DIAMETR, DIAMETR, 50, 50, material, attributes);
		
		instanceSphere = new ModelInstance( sphere );
		
		final Vector2 position = mBody.getPosition();
		mTmpVector.x = position.x;
		mTmpVector.y = position.y;
		
		Vector2 deltaPosition = mTmpVector.sub( mLastPosition );

		instanceSphere.transform.trn( deltaPosition.x, deltaPosition.y, 20.0f );
		
		mLastPosition.x = position.x;
		mLastPosition.y = position.y;
	}
	@Override
	public void draw( Batch batch, float deltaTime )
	{
//		Vector2 position = mBody.getPosition();
//		float angle = MathUtils.radiansToDegrees * mBody.getAngle();
//		batch.draw(
//				mTexture,
//				position.x - RADIUS, position.y - RADIUS,
//				RADIUS, RADIUS,
//				DIAMETR, DIAMETR,
//				1f, 1f,
//				angle);	
		
		final Vector2 position = mBody.getPosition();
		mTmpVector.x = position.x;
		mTmpVector.y = position.y;
		Vector2 deltaPosition = mTmpVector.sub( mLastPosition );
		instanceSphere.transform.trn( deltaPosition.x, deltaPosition.y, 0.0f );
		instanceSphere.transform.rotate( 0.0f, 1.0f, 0.0f, deltaPosition.x * 5 );
		instanceSphere.transform.rotate( 1.0f, 0.0f, 1.0f, -deltaPosition.y * 5 );
//		instanceSphere.transform.rotate( 0.0f, 1.0f, 1.0f, 10.0f );
		
		mLastPosition.x = position.x;
		mLastPosition.y = position.y;
		
		
		if( null != mCamera )
		{
			modelBatch.begin( mCamera );
	        modelBatch.render( instanceSphere, environment );
	        modelBatch.end();
		}
	}
	
	public void setCamera( Camera camera )
	{
		mCamera = camera;
	}
}
