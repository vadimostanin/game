package com.mygdx.game_asteroid_3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector2;

import properties.IProperty;
import properties.MoveProperty;
import properties.PositionProperty;

public class WorldBoundsDraw2 implements IDisposablePropertyListener, IDraw
{
	private Vector2[] mVertex;
	
	private Texture mTexture;
	
	private Texture mGeneratedTextures;
	
	private int mTextureWidth = 0;
	private int mTextureHeight = 0;
	
	private SpriteBatch mBatch;
	
	private Sprite[] mSprites;
	
	private FrameBuffer[] m_fbo = null;
	private TextureRegion[] m_fboRegion = null;
	
	private Vector2 mTempVector = new Vector2();
	private float[] mRotates;
	private int mRareRatio = 1;
	private float mInitRatio = 1.0f;

	private boolean mInitialMoved = false;
	
	public WorldBoundsDraw2( Vector2[] vertex, Texture texture )
	{
		mVertex = vertex;
		mTexture = texture;
		
		mTextureWidth = (int)( mTexture.getWidth() * mInitRatio );
		mTextureHeight = (int)( mTexture.getHeight() * mInitRatio );
		
		mRotates = new float[mVertex.length];
		
		m_fbo = new FrameBuffer[mVertex.length];
        m_fboRegion = new TextureRegion[mVertex.length];
        mSprites = new Sprite[mVertex.length];
        
        Vector2 initPoint = mVertex[mVertex.length - 1];
        
        mBatch = new SpriteBatch();
        
        mVertex[0].add(   -100,   -100 );
        mVertex[1].add(   -100,  100 );
        mVertex[2].add(  100,   100 );
        mVertex[3].add(  100,  -100 );
        
        for( int bound_i = 0 ; bound_i < mVertex.length ; bound_i++ )
        {
        	Vector2 bound = mVertex[bound_i];
        	mTempVector.set( bound );
        	mTempVector.sub( initPoint );
        	float angle = Utils.angle( mTempVector );
        	mRotates[bound_i]  = angle;
        	
        	m_fbo[bound_i] = new FrameBuffer(Format.RGBA8888, (int)( 1280 ), (int)( Gdx.graphics.getBackBufferHeight() ), false);
        	float width = m_fbo[bound_i].getColorBufferTexture().getWidth();
            m_fboRegion[bound_i] = new TextureRegion( m_fbo[bound_i].getColorBufferTexture() );
        
        
            drawToFrameBuffer( m_fbo[bound_i], mTempVector.len(), 80 );
//            Texture cuttedTexture = cutTexture( m_fboRegion[bound_i].getTexture(), (int)mTempVector.len(), 50 );

            Sprite sprite = new Sprite( m_fboRegion[bound_i].getTexture() );
            
            sprite.setU( 0 );
			sprite.setV( 0 );
			sprite.setU2( 1 );
			sprite.setV2( 1 );
			
			float x = initPoint.x + Gdx.graphics.getWidth() / 2;
			float y = initPoint.y - Gdx.graphics.getHeight() / 2;
			sprite.setOrigin( 0, 520 );
			
			sprite.setPosition( x, y );
						
			sprite.rotate( angle );
						
			mSprites[bound_i] = sprite;
			
			initPoint = mVertex[bound_i];
        }
	}
	
	private void drawToFrameBuffer( FrameBuffer fbo, float width, int height )
	{
		float[] rotations = new float[1000];
		float[] scales = new float[1000];
		Vector2[] offsets = new Vector2[1000];
		int copyObjectsCount = 0;

	
		float offsetX = 0.0f;
		float offsetY = 0.0f;
		copyObjectsCount = 0;
		for( int pixelWidth_i = 0 ; pixelWidth_i < ( width - mTextureWidth ) ; pixelWidth_i += mTextureWidth )
		{
			for( int heightPixel_i = 0 ; heightPixel_i < ( height - mTextureHeight ) ; heightPixel_i += mTextureHeight )
			{
				rotations[copyObjectsCount] = (float)Math.random() * 360;
				scales[copyObjectsCount] = 1.0f;//(float)Math.random() * 1.0f;
				offsetX = (float)( 1.0 - Math.random() * 2 ) * mTextureWidth * scales[copyObjectsCount];
				offsetY = (float)( 1.0 - Math.random() * 2 ) * mTextureHeight * scales[copyObjectsCount];
				offsets[copyObjectsCount] = new Vector2( offsetX, offsetY );
				copyObjectsCount++;
			}
		}
		
//		mBackground.setWrap( TextureWrap.Repeat, TextureWrap.Repeat );
		
		fbo.begin();
		
		Gdx.gl20.glClear( GL20.GL_DEPTH_BUFFER_BIT | GL20.GL_COLOR_BUFFER_BIT );
		Gdx.gl.glClearColor( 1, 0, 1, 0.0f );

	    Gdx.gl20.glEnable( GL20.GL_BLEND );
	    Gdx.gl20.glBlendFunc( GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA );

		mBatch.begin();
        
		int copy_i = 0;
		for( float widthPixel_i = mTextureWidth ; widthPixel_i < ( width - Math.max( mTextureWidth, mTextureHeight ) ) ; widthPixel_i += mTextureWidth )
		{
			try
			{
				for( float heightPixel_i = mTextureHeight ; heightPixel_i < ( height - Math.max( mTextureWidth, mTextureHeight ) ) ; heightPixel_i += mTextureHeight )
				{
					double r = Math.random() * 1000;
					if( ( ( (int)r ) % mRareRatio ) == 0 )
					{
//						mBatch.setColor( 1.0f, 1.0f, 1.0f, mScales[copy_i] );
						mBatch.draw( mTexture, widthPixel_i + offsets[copy_i].x, heightPixel_i + offsets[copy_i].y, mTextureWidth / 2, mTextureHeight / 2, (float)mTextureWidth, (float)mTextureHeight, scales[copy_i], scales[copy_i], rotations[copy_i], 0, 0, (int)( mTextureWidth / mInitRatio ), (int)( mTextureHeight / mInitRatio ), false, false );
					}
					
					copy_i++;
					
				}
			}
			catch( Exception e )
			{
				int a = 0;
				a++;
			}
		}
		mBatch.end();
		
		Texture t = Utils.screenShoot();
		Utils.saveTexture( t, "t.png" );
		fbo.end();
	}
	
	@Override
	public void draw(Batch batch, float deltaTime)
	{
		mBatch.begin();
//		mTexture.setWrap( TextureWrap.Repeat, TextureWrap.Repeat );
		for( Sprite sprite : mSprites )
		{			
			sprite.draw( mBatch );
//			sprite.rotate( 0.3f );
		}

		mBatch.end();
	}

	@Override
	public void onChanged(IProperty prop)
	{
		if( prop instanceof MoveProperty )
		{
			if( true == mInitialMoved )
			{
				Vector2 delta = ((MoveProperty)prop).getDelta();
				move( -delta.x, -delta.y );
			}
			else
			{
				mInitialMoved = true;
			}
			
		}
	}

	private void move( float deltaX, float deltaY )
	{
		for( Sprite sprite : mSprites )
		{
			sprite.setPosition( sprite.getX() + deltaX, sprite.getY() + deltaY );
		}
	}

	@Override
	public void dispose()
	{
		;
	}

}
