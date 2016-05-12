package com.mygdx.game_asteroid_3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game_asteroid_3d.level.Level;
import com.mygdx.game_asteroid_3d.level.LevelGravityObject;

public class GameScreen implements Screen
{
	private Level mLevel;
	private HorizontalGroup mGroup;
	private Stage mStage;
	public InputMultiplexer mInputProcessor;
	
	public GameScreen( Level level )
	{
		mLevel = level;
	}
	
	@Override
	public void show()
	{
		mGroup = new HorizontalGroup();
		mStage = new Stage( new StretchViewport( Gdx.graphics.getWidth(), Gdx.graphics.getHeight() ) );
		mInputProcessor = new InputMultiplexer();
		
		float actor1Ratio = 1.0f;
//		float actor2Ratio = 1.0f - actor1Ratio;
		
		int playScreenWidth = (int) (actor1Ratio * mStage.getWidth());		
		int playScreenHeight = Gdx.graphics.getHeight();
		
		GamePlayScreen actor1 = new GamePlayScreen( mLevel, 0, 0, (int)playScreenWidth, (int)playScreenHeight, mInputProcessor );
		actor1.setSize( playScreenWidth, playScreenHeight );
		
//		int toolScreenWidth = (int) (actor2Ratio * mStage.getWidth());
//		int toolScreenHeight = Gdx.graphics.getHeight();
			
//		GamesToolsScreen actor2 = new GamesToolsScreen( mInputProcessor );
//		actor2.setSize( toolScreenWidth, toolScreenHeight );		
		
		mGroup.addActor( actor1 );
//		mGroup.addActor( actor2 );
//		mGroup.addActor( actor3 );
//		mGroup.center();


		
		mStage.addActor( mGroup );
		Gdx.input.setInputProcessor( mInputProcessor );
		
		for( LevelGravityObject levelGravityObject : mLevel.mGravityObjects.mGravityObjects )
		{
//			GraphicObjectsCache.getInstance().add(obj);
		}
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        
        
        mStage.act( delta );
		mStage.draw();
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
