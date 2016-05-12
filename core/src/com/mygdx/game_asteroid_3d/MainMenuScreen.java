package com.mygdx.game_asteroid_3d;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeBitmapFontData;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game_asteroid_3d.level.Level;

import sun.font.TrueTypeFont;

public class MainMenuScreen implements Screen, InputProcessor
{
	private Game mGame;
	private SpriteBatch mBatch;
	private BitmapFont mFont;
	private Texture mBackgroundTexture;
	private static final String FONT_CHARACTERS = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>";
	private Table mTable;
	private Stage mStage;
	
	public MainMenuScreen( Game game )
	{
		mGame = game;
		mBatch = new SpriteBatch();
		String storageDir = Gdx.files.getLocalStoragePath();
		mBackgroundTexture = new Texture( Gdx.files.absolute( storageDir + "vectorstock_5270709.png" ) );
		
		mFont = new BitmapFont();
//		new TrueTypeFont(Gdx.files.internal("font.ttf"), "qazwsxedcrfvtgbyhnujmikolp", 12.5f, 7.5f, 1.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator( Gdx.files.absolute( storageDir + "9767.ttf" ) );
		FreeTypeBitmapFontData font15 = generator.generateData(15);
		FreeTypeBitmapFontData font22 = generator.generateData(22);
		FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.size = Gdx.graphics.getHeight() / 18; // Размер шрифта. Я сделал его исходя из размеров экрана. Правда коряво, но вы сами можете поиграться, как вам угодно.
        param.characters = FONT_CHARACTERS; // Наши символы
        mFont = generator.generateFont( param, font15 ); // Генерируем шрифт
        generator.dispose();
        createPlayButton();
		
//        Gdx.input.setInputProcessor( this );
	}
	
	private void createPlayButton()
	{
		Skin skin = new Skin();
		TextureAtlas buttonAtlas = new TextureAtlas(Gdx.files.local("images.pack"));
        skin.addRegions(buttonAtlas);
        
		TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = mFont;
        textButtonStyle.up = skin.getDrawable("button-up");
        textButtonStyle.down = skin.getDrawable("button-down");
        textButtonStyle.checked = skin.getDrawable("button-up");
        TextButton play = new TextButton( "Play", textButtonStyle );
        play.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
    			Gdx.input.vibrate(20);
    			return true;
            };
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            	
            	XMLparse xmlParse = new XMLparse();
        		String localDir = Gdx.files.getLocalStoragePath();
        		Array<String> levelsList = xmlParse.XMLparseLevels( localDir );
        		
            	for( String levelFile : levelsList )
        		{		
        			Level level = xmlParse.loadLevel( localDir + levelFile );
        			mGame.setScreen( new GameScreen( level ) );
        		}
            	
    			dispose();
            };
        });
        
        mStage = new Stage(new ScreenViewport());
		        
        mTable = new Table();
        mTable.setFillParent( true );
        
        mTable.add( play );
        mStage.addActor( mTable );
 
        Gdx.input.setInputProcessor( mStage );
        Gdx.input.setCatchBackKey( true );
	}
	
	@Override
	public boolean keyDown(int keycode)
	{
		
		
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

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta)
	{
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        
        mBatch.begin();
        mBatch.draw( mBackgroundTexture, 0, 0 );
        mBatch.end();
        
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
	public void hide()
	{
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
