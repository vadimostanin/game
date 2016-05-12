package com.mygdx.game_asteroid_3d;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.*;
import com.mygdx.game_asteroid_3d.level.Level;

import properties.PositionProperty;

public class GamesToolsScreen extends Actor implements InputProcessor
{
	private SpriteBatch mBatch;
	private Texture mTexture = new Texture( Gdx.files.absolute( "/home/vostanin/AndroidStudioProjects/MyGdxGame_Asteroid_3D/desktop/badlogic.jpg" ) );
	
	ArrayList<IDraw> mDrawables = new ArrayList<IDraw>();
	private OrthographicCamera mCamera;
	
	private InputMultiplexer mInputProcessor;
	private CameraInputController mCameraInput;
	
//	private VerticalGroup mGroup;
	private Table mTable;
	private Stage mStage;
	
	GravityBodyDraw mGravDraw;
	GravityBodyDraw mGravDraw2;
	
	public GamesToolsScreen( InputMultiplexer globalInputProcessor )
	{		
		mInputProcessor = globalInputProcessor;
		
		mBatch = new SpriteBatch();
//		mGroup = new VerticalGroup();
		mTable = new Table();
		
		
		mGravDraw = new GravityBodyDraw();
		
		PositionProperty positionProp = new PositionProperty();
		positionProp.setPosition( 0.0f, 0.0f );
		mGravDraw.onChanged( positionProp );
		mDrawables.add( mGravDraw );
		mGravDraw.setSize( 150, 150 );
		mGravDraw.addListener(new ClickListener()
		{
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
    			Gdx.input.vibrate(20);
    			return true;
            };
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
            	Gdx.input.vibrate(20);            	
            };
        }
		);
		
		
		
		mGravDraw2 = new GravityBodyDraw();
		positionProp = new PositionProperty();
		positionProp.setPosition( 0.0f, 50.0f );
		mGravDraw2.onChanged( positionProp );
		mDrawables.add( mGravDraw2 );
		mGravDraw2.setSize( 50, 50 );
		
		

//		mGroup.addActor( mGravDraw );
//		mGroup.addActor( mGravDraw2 );
//		mGroup.setFillParent( true );
		
		mTable.add( mGravDraw );
		mTable.add( mGravDraw2 );
		mTable.setFillParent( true );
//		mGroup.center();
		
		
	}
	
	private void initWeightHeightDepends()
	{
		int width = (int) getWidth();
		int height = (int) getHeight();
		
		mCamera = new OrthographicCamera( width, height );

		mCamera.position.set( 0, 0, 440);
		mCamera.lookAt( 0, 0, 0);
		mCamera.near = -100f;
		mCamera.far = 2000f;
		mCamera.update();
		
		mGravDraw.setCamera( mCamera );
		mGravDraw2.setCamera( mCamera );
		
		mCameraInput = new CameraInputController( mCamera );
		mInputProcessor.addProcessor( this );
		
//		mInputProcessor.addProcessor( mCameraInput );
		
		Viewport viewport = new ExtendViewport( width, height );
		viewport.apply();
		mStage = new Stage( viewport );
//		mStage.addActor( mGroup );
		mStage.addActor( mTable );
		
		mInputProcessor.addProcessor( mStage );
	}
	
	public void draw (Batch batch, float parentAlpha)
	{
		int x = (int) getX();
		int y = (int) getY();
		int w = (int) getWidth();
		int h = (int) getHeight();
		
		Gdx.gl.glViewport( x, 0, w, h );
//		Gdx.gl.glClearColor(1, 0, 1, 1);
		
		if( null == mCamera )
		{
			initWeightHeightDepends();
		}
		if( null != mCamera )
		{
//			mCamera.update();
		}
		
        mBatch.begin();
        mBatch.draw( mTexture, 0, 0, mTexture.getWidth(), mTexture.getHeight() );
        mBatch.end();
//        for( IDraw iDraw : mDrawables )
//        {
//			iDraw.draw( mBatch, Gdx.graphics.getDeltaTime() );
//		}
        
        mStage.act( Gdx.graphics.getDeltaTime() );
		mStage.draw();
	}

	@Override
	public boolean keyDown(int keycode)
	{
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean keyUp(int keycode)
	{
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean keyTyped(char character)
	{
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		
		return false;
	}
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
