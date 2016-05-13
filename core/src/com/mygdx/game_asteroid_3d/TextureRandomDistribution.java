package com.mygdx.game_asteroid_3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.mygdx.game_asteroid_3d.TexturesCache.Texture_Types;

import properties.IProperty;
import properties.MoveProperty;

public class TextureRandomDistribution implements IDraw, IPropertyChangeListener
{
	private Texture mTexture;
	
	private int mTextureWidth = 0;
	private int mTextureHeight = 0;
	
	private int mWidth = 0;
	private int mHeight = 0;
	
	private float mX = 0.0f;
	private float mY = 0.0f;
	
	private SpriteBatch mBatch;
	
	private Sprite mSprite;
	
	private FrameBuffer m_fbo = null;
	private TextureRegion m_fboRegion = null;
	
	private float m_fboScaler = 1.0f;
	
	private float mSlowMotion = 1.0f;
	
	private int mRareRatio = 1;
	private float mInitRatio = 1.0f;

	private boolean mShaking = false;
	private boolean mMoving = true;
	private boolean mRepeat = true;
	private Matrix4 mTranformation = new Matrix4();
	
	TextureRandomDistribution( Texture texture, int x, int y, int width, int height, float slowMotion, int rareRatio, float initRatio, boolean repeat )
	{
		mTexture = texture;
		mInitRatio = initRatio;
		mRepeat = repeat;
		
		mTextureWidth = (int)( mTexture.getWidth() * mInitRatio );
		mTextureHeight = (int)( mTexture.getHeight() * mInitRatio );
		
		mX = x;
		mY = y;
		
		mWidth = width;
		mHeight = height;
		
		mSlowMotion = slowMotion;
		mRareRatio = rareRatio;
		
		m_fbo = new FrameBuffer(Format.RGBA8888, (int)(Gdx.graphics.getBackBufferWidth() * m_fboScaler), (int)( Gdx.graphics.getBackBufferHeight() * m_fboScaler), false);
        m_fboRegion = new TextureRegion( m_fbo.getColorBufferTexture() );
//        m_fboRegion.flip(false, t4rue);
        m_fboRegion.getTexture().setWrap( TextureWrap.Repeat, TextureWrap.Repeat );
//        mTexture.setWrap( TextureWrap.Repeat, TextureWrap.Repeat );

		mBatch = new SpriteBatch();
		
		mTranformation.setToOrtho2D( 0, 0, width, height );

		initRotationsScaleOffsets();
	}
	
	void initRotationsScaleOffsets()
	{
		float[] rotations = new float[10000];
		float[] scales = new float[10000];
		Vector2[] offsets = new Vector2[10000];
		int copyObjectsCount = 0;

	
		float offsetX = 0.0f;
		float offsetY = 0.0f;
		copyObjectsCount = 0;
		for( int pixelWidth_i = 0 ; pixelWidth_i < ( mWidth ) ; pixelWidth_i += mTextureWidth )
		{
			for( int heightPixel_i = 0 ; heightPixel_i < ( mHeight - mTextureHeight ) ; heightPixel_i += mTextureHeight )
			{
				rotations[copyObjectsCount] = (float)Math.random() * 360;
				scales[copyObjectsCount] = (float)Math.random() * mInitRatio;
				offsetX = (float)( 1.0 - Math.random() * 2 ) * mTextureWidth * scales[copyObjectsCount];
				offsetY = (float)( 1.0 - Math.random() * 2 ) * mTextureHeight * scales[copyObjectsCount];
				offsets[copyObjectsCount] = new Vector2( offsetX, offsetY );
				copyObjectsCount++;
			}
		}
		
//		mBackground.setWrap( TextureWrap.Repeat, TextureWrap.Repeat );
		
		m_fbo.begin();
		
		Gdx.gl20.glClear( GL20.GL_DEPTH_BUFFER_BIT | GL20.GL_COLOR_BUFFER_BIT );
		Gdx.gl.glClearColor( 1, 0, 1, 0.0f );

	    Gdx.gl20.glEnable( GL20.GL_BLEND );
	    Gdx.gl20.glBlendFunc( GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA );

	    
		mBatch.begin();
        
		int copy_i = 0;
		for( float widthPixel_i = mTextureWidth ; widthPixel_i < ( mWidth - mTextureWidth ) ; widthPixel_i += mTextureWidth )
		{
			try
			{
				for( float heightPixel_i = mTextureHeight ; heightPixel_i < ( mHeight - mTextureHeight ) ; heightPixel_i += mTextureHeight )
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
		m_fbo.end();
		if( true == isRepeat() )
		{
			mSprite = new Sprite( m_fboRegion.getTexture(), 0, 0, Gdx.graphics.getBackBufferWidth() * 4, Gdx.graphics.getBackBufferHeight() * 4 );
		}
		else
		{
			mSprite = new Sprite( m_fboRegion.getTexture(), 0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight() );
		}
	}
	
	public void setShaking( boolean shaking )
	{
		mShaking = shaking;
	}
	
	@Override
	public void draw(Batch batch, float deltaTime)
	{
//		mBatch.setProjectionMatrix( batch.getProjectionMatrix() );
		mBatch.setProjectionMatrix( mTranformation );
		mBatch.begin();
//		mTexture.setWrap( TextureWrap.Repeat, TextureWrap.Repeat );
		if( true == isRepeat() )
		{
			mSprite.setU( 0 );
			mSprite.setV( 0 );
			mSprite.setU2( 4 );
			mSprite.setV2( 4 );
		}
		else
		{
			mSprite.setU( 0 );
			mSprite.setV( 0 );
			mSprite.setU2( 1 );
			mSprite.setV2( 1 );
		}
		mSprite.draw( mBatch );
//		if( true == mShaking )
//		{
//			mSprite.setPosition( ( -mX - mSprite.getWidth() ) + (float)Math.random(), ( -mY - mSprite.getHeight() / 2 ) + (float)Math.random() );
//		}
//		else
//		{
//			mSprite.setPosition( ( -mX - mSprite.getWidth() / 2 ), ( -mY - mSprite.getHeight() / 2 ) );
//		}
		
		mBatch.end();
	}

	@Override
	public void onChanged(IProperty prop)
	{
		if( prop instanceof MoveProperty && true == isMoving() )
		{
			Vector2 delta = ((MoveProperty)prop).getDelta();
//			mX += delta.x / mSlowMotion;
//			mY += delta.y / mSlowMotion;
			mTranformation.translate( ( -1 ) * delta.x / mSlowMotion, ( -1 ) * delta.y / mSlowMotion, 0 );
			
		}
	}

	public boolean isMoving()
	{
		return mMoving;
	}

	public void setMoving(boolean mMoving)
	{
		this.mMoving = mMoving;
	}

	public boolean isRepeat()
	{
		return mRepeat;
	}

	public void setRepeat(boolean mRepeat)
	{
		this.mRepeat = mRepeat;
	}
}
