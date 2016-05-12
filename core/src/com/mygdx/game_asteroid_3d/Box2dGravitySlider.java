package com.mygdx.game_asteroid_3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import properties.GravityRadiusProperty;
import properties.SliderInputProperty;


public class Box2dGravitySlider implements IDraw, InputProcessor, IGameUserActivity, Disposable
{	
	private Vector2 mPosition;
	
	private Stage mStage;
	private Skin mSkin;
	private Slider mSlider;
	private Label mSliderValueLabelUpper;
	private Label mSliderValueLabelBellow;
	private Table mTable;

	SliderInputProperty mProperty;
	IPropertyChangeListener mChangeListener;
	GravityRadiusProperty mGravityProperty = new GravityRadiusProperty( 0.0f );
	
	private Vector2 mLastTouchPoint = new Vector2( 0.0f, 0.0f );
	
	public Box2dGravitySlider( SliderInputProperty property, IPropertyChangeListener listener, Vector2 touchPoint )
	{
		mProperty = property;
		mChangeListener = listener;
		mLastTouchPoint.set( touchPoint );
		
		String storageDir = Gdx.files.getLocalStoragePath();
		
		mPosition = mProperty.getPosition();
		
		
		{
			mSkin = new Skin( Gdx.files.absolute( storageDir + "../core/assets/uiskin.json" ) );
			mStage = new Stage( new ScreenViewport() );
			
			mSlider = new Slider( 1.0f, 20.0f, 0.1f, false, mSkin );
			BitmapFont font = new BitmapFont();

			LabelStyle style = new LabelStyle( font, Color.WHITE );
			mSliderValueLabelUpper = new Label(
		            String.format( "%.01f", mProperty.getRadius() ), style);
			mSliderValueLabelBellow = new Label(
		            String.format( "%.01f", mProperty.getRadius() ), style);
			float sliderY = Gdx.graphics.getHeight() / 2.0f + mPosition.y;
			float sliderX = Gdx.graphics.getWidth() / 2.0f + mPosition.x + mSlider.getWidth() / 4.0f;
			
			
			mSlider.setValue( mProperty.getRadius() );
			mSlider.addListener(new ChangeListener()
						{
			
							@Override
							public void changed(ChangeEvent event, Actor actor)
							{
								mSliderValueLabelUpper.setText( String.format( "%.01f",
					        			mSlider.getValue() ) );	
								mSliderValueLabelBellow.setText( String.format( "%.01f",
					        			mSlider.getValue() ) );
							}
					    }
			);
			
			mTable = new Table();
			mTable.add( mSliderValueLabelUpper );
			mTable.row();
			mTable.add( mSlider );
			mTable.row();
			mTable.add( mSliderValueLabelBellow );
			mTable.setPosition( sliderX,  sliderY );
			
			mStage.addActor( mTable );
		}
	}

	@Override
	public void draw(Batch batch, float deltaTime)
	{
		mStage.getBatch().enableBlending();
        mStage.getBatch().setColor( 1.0f, 1.0f, 1.0f, 1.0f );
        mStage.draw();
	}

	@Override
	public void dispose()
	{
		mStage.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp( int screenX, int screenY, int pointer, int button )
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged( int screenX, int screenY, int pointer )
	{
		// TODO Auto-generated method stub
		if( screenX < mLastTouchPoint.x )
		{
			mSlider.setValue( mSlider.getValue() - 1 );
			mGravityProperty.setRadius( mSlider.getValue() );
			mChangeListener.onChanged( mGravityProperty );
		}
		else if( screenX > mLastTouchPoint.x )
		{
			mSlider.setValue( mSlider.getValue() + 1 );
			mGravityProperty.setRadius( mSlider.getValue() );
			mChangeListener.onChanged( mGravityProperty );
		}
		mLastTouchPoint.set( screenX, screenY );
		return true;
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

	@Override
	public InputProcessor getInputProcessor()
	{
		return this;
	}
}
