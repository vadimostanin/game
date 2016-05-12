/**
 * 
 */
package com.mygdx.game_asteroid_3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.ChainShape;

/**
 * @author vadim
 *
 */
public class WallDraw implements IDraw
{
	private Body mBody;
	
	Texture mTexture;
	
	public ModelBatch modelBatch;
	public ModelInstance instanceBox;
	public Environment environment;
	private Model mWall;

	private Camera mCamera;
	
	public WallDraw( Body body )
	{
		String storagePaths = Gdx.files.getLocalStoragePath();
		mTexture = new Texture( Gdx.files.absolute( storagePaths + "wall2.png" ) );
		mBody = body;
		
		modelBatch = new ModelBatch();
	    
	    environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
		
		ModelBuilder modelBuilder = new ModelBuilder();
		
		ChainShape shape = ( ChainShape ) mBody.getFixtureList().get( 0 ).getShape();
		Vector2 vertexTopLeft = new Vector2();
		Vector2 vertexBottomRight = new Vector2();
		shape.getVertex( 0, vertexTopLeft );
		shape.getVertex( 2, vertexBottomRight );
		
		
		Texture texture = mTexture;
		float x = vertexTopLeft.x;
		float y = vertexTopLeft.y;
		float width = Math.abs( vertexBottomRight.x - vertexTopLeft.x );
		float height = Math.abs( vertexTopLeft.y - vertexBottomRight.y );
		
		mWall = modelBuilder.createBox( width, height, 50f, 
	            new Material(TextureAttribute.createDiffuse(texture), ColorAttribute.createSpecular(1, 1, 1, 0.99f), new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA) ),
	            Usage.Position | Usage.Normal | Usage.TextureCoordinates
	            );
		
		instanceBox = new ModelInstance( mWall );
		instanceBox.transform.trn( x + width / 2.0f, y - height / 2.0f, 25.0f );
	}
	
	public void setCamera( Camera camera )
	{
		mCamera = camera;
	}
	/* (non-Javadoc)
	 * @see com.mygdx.game_asteroid.IDraw#draw(com.badlogic.gdx.graphics.g2d.Batch)
	 */
	@Override
	public void draw( Batch batch, float deltaTime )
	{

//		int srcX = 0;
//		int srcY = 0;
//		int srcWidth = (int)Math.abs( vertexTopLeft.x - vertexBottomRight.x );
//		int srcHeight = (int)Math.abs( vertexTopLeft.y - vertexBottomRight.y );
//		boolean flipX = false;
//		boolean flipY = false;
//
//		batch.draw(
//				texture,
//				x, y,
//				width, height, 
//				srcX, srcY,
//				srcWidth * 26, srcHeight * 26, flipX, flipY );
		if( null != mCamera )
		{
	        modelBatch.begin( mCamera );
	        modelBatch.render( instanceBox, environment );
	        modelBatch.end();
		}		
	}

}
