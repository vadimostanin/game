package com.mygdx.game_asteroid_3d;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game_asteroid_3d.TexturesCache.Texture_Types;

public class MyGdxGame_Asteroid extends Game
{
	String mStorageDir;
	@Override
	public void create ()
	{
//		Gdx.graphics.setContinuousRendering(false);
//		Gdx.graphics.requestRendering();

		
		this.setScreen( new MainMenuScreen( this ) );
		mStorageDir = Gdx.files.getLocalStoragePath();
		loadTextures();
	}
	
	void loadTextures()
	{
		TexturesCache.getInstance().add( Texture_Types.TEXTURE_PLAYSCREEN_BACKGROUND, new Texture( mStorageDir + "17851506.jpg" ) );
		TexturesCache.getInstance().add( Texture_Types.TEXTURE_PLAYSCREEN_BACKGROUND_STAR1, new Texture( mStorageDir + "star1.png" ) );
		TexturesCache.getInstance().add( Texture_Types.TEXTURE_PLAYSCREEN_BACKGROUND_STAR2, new Texture( mStorageDir + "star2.png" ) );
		TexturesCache.getInstance().add( Texture_Types.TEXTURE_PLAYSCREEN_BACKGROUND_STAR3, new Texture( mStorageDir + "star3.png" ) );
		{
			TexturesCache.getInstance().add( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME1, new Texture( mStorageDir + "wave_2/1.png" ) );
			TexturesCache.getInstance().add( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME2, new Texture( mStorageDir + "wave_2/2.png" ) );
			TexturesCache.getInstance().add( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME3, new Texture( mStorageDir + "wave_2/3.png" ) );
			TexturesCache.getInstance().add( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME4, new Texture( mStorageDir + "wave_2/4.png" ) );
			TexturesCache.getInstance().add( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME5, new Texture( mStorageDir + "wave_2/5.png" ) );
			TexturesCache.getInstance().add( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME6, new Texture( mStorageDir + "wave_2/6.png" ) );
			TexturesCache.getInstance().add( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME7, new Texture( mStorageDir + "wave_2/7.png" ) );
			TexturesCache.getInstance().add( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME8, new Texture( mStorageDir + "wave_2/8.png" ) );
			TexturesCache.getInstance().add( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME9, new Texture( mStorageDir + "wave_2/9.png" ) );
			TexturesCache.getInstance().add( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME10, new Texture( mStorageDir + "wave_2/10.png" ) );
			TexturesCache.getInstance().add( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME11, new Texture( mStorageDir + "wave_2/11.png" ) );
			TexturesCache.getInstance().add( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME12, new Texture( mStorageDir + "wave_2/12.png" ) );
			TexturesCache.getInstance().add( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME13, new Texture( mStorageDir + "wave_2/13.png" ) );
			TexturesCache.getInstance().add( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME14, new Texture( mStorageDir + "wave_2/14.png" ) );
			TexturesCache.getInstance().add( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME15, new Texture( mStorageDir + "wave_2/15.png" ) );
			TexturesCache.getInstance().add( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME16, new Texture( mStorageDir + "wave_2/16.png" ) );
			TexturesCache.getInstance().add( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME17, new Texture( mStorageDir + "wave_2/17.png" ) );
			TexturesCache.getInstance().add( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME18, new Texture( mStorageDir + "wave_2/18.png" ) );
			TexturesCache.getInstance().add( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME19, new Texture( mStorageDir + "wave_2/19.png" ) );
			TexturesCache.getInstance().add( Texture_Types.TEXTURE_PLAYSCREEN_GRAVITY_FRAME20, new Texture( mStorageDir + "wave_2/20.png" ) );
			
			TexturesCache.getInstance().add( Texture_Types.TEXTURE_PLAYSCREEN_SHIP, new Texture( mStorageDir + "retrorocket-222x70.png" ) );
			TexturesCache.getInstance().add( Texture_Types.TEXTURE_PLAYSCREEN_METEORITE1, new Texture( mStorageDir + "meteorite3.png" ) );
		}
	}

	@Override
	public void render ()
	{
		super.render();
	}
}
