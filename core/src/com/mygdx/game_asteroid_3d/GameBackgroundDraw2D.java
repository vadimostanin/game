package com.mygdx.game_asteroid_3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.mygdx.game_asteroid_3d.TexturesCache.Texture_Types;

import properties.IProperty;
import properties.MoveProperty;

public class GameBackgroundDraw2D implements IDraw, IPropertyChangeListener
{
	private int mWidth;
	private int mHeight;
	private Texture mTexture;
	
	SpriteBatch mBatch = new SpriteBatch();
	
	int offset = 1;
	
	private float mX = 0.0f;
	private float mY = 0.0f;

	public GameBackgroundDraw2D( int x, int y, int width, int height )
	{
		mX = x;
		mY = y;
		mWidth = width;
		mHeight = height;
	    
		String storagePaths = Gdx.files.getLocalStoragePath();
//		mTexture = new Texture(Gdx.files.absolute( storagePaths + "alnilam-Stars-Pattern-2400px.png" ) );
		mTexture = TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_BACKGROUND );
		
//		mTexture.setWrap( TextureWrap.Repeat, TextureWrap.Repeat );
	}
	
	public Texture getTexture()
	{
		return mTexture;
	}
	@Override
	public void draw(Batch batch, float deltaTime)
	{
//		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
//	    Gdx.gl20.glEnable(GL20.GL_BLEND);
//	    Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
//	    Gdx.gl20.glEnable(GL20.GL_TEXTURE_2D);
//	    Gdx.gl20.glBlendEquation(GL20.GL_BLEND);
	    
		batch.begin();
        batch.draw( mTexture, mX, mY, mWidth, mHeight );
        batch.end();
	}

	@Override
	public void onChanged(IProperty prop)
	{
		if( prop instanceof MoveProperty )
		{
//			mX += ((MoveProperty)prop).getDelta().x;
//			mY += ((MoveProperty)prop).getDelta().y;
		}
	}
}
