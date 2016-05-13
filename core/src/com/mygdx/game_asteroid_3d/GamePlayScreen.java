package com.mygdx.game_asteroid_3d;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game_asteroid_3d.TexturesCache.Texture_Types;
import com.mygdx.game_asteroid_3d.level.Barrier;
import com.mygdx.game_asteroid_3d.level.Level;
import com.mygdx.game_asteroid_3d.level.LevelGravityObject;

import properties.IProperty;
import properties.MoveProperty;
import properties.PositionProperty;
import properties.SliderInputProperty;

public class GamePlayScreen extends Actor implements InputProcessor, IPropertyChangeListener
{
	private final static int MAX_FPS = 30;
	private final static int MIN_FPS = 15;
	private final static float TIME_STEP = 1f / MAX_FPS;
	private final static float MAX_STEPS = 1f + MAX_FPS / MIN_FPS;
	private final static float MAX_TIME_PER_FRAME = TIME_STEP * MAX_STEPS;
	private final static int VELOCITY_ITERS = 6;
	private final static int POSITION_ITERS = 2;

	private float physicsTimeLeft;

	private OrthographicCamera mCamera;
	private Matrix4 mTranformation = new Matrix4();
	private SpriteBatch mBatch;
	private int mViewPortWidth = 0;
	private int mViewPortHeight = 0;
	private int mViewPortX = 0;
	private int mViewPortY = 0;

	private List<GravityBody> mGravityBodies = new ArrayList<GravityBody>();
	private List<IDraw> mDrawingBodies = new ArrayList<IDraw>();
	private List<GameActiveObject> mBallBodies = new ArrayList<GameActiveObject>();
	private ArrayList<Body> mWallCellBodies = new ArrayList<Body>();
	private List<IDisposablePropertyListener> mTouchCreatedObjects = new ArrayList<IDisposablePropertyListener>();
//	private ArrayList<Vector2[]> mGravityBodyPlaces = new ArrayList<Vector2[]>();
	private GameWorld mGameWorld;
	private CameraInputController mCameraInput;
	
	private IGameUserActivity mUserActivity;

	static ScreenProportionsConvertor mScreenProportionalConvertor;

	/** a hit body **/
	private Body mHitBody = null;

	private Box2DDebugRenderer mDebugRenderer = new Box2DDebugRenderer( true, true, true, true, true, true );
	
	private PropertyChangeListeners mListeners = new PropertyChangeListeners();
	
	private Level mLevel;
	private InputMultiplexer mInputProcessor;
	private List<Body> mDestroyableBody = new ArrayList<Body>();
	private List<Joint> mDestroyableJoint = new ArrayList<Joint>();
	private GameShipBody mShipBody;

	public GamePlayScreen( Level level, int x, int y, int width, int height, InputMultiplexer globalInputProcessor )
	{
		mLevel = level;
		
		mViewPortX = -0;
		mViewPortY = -0;
		mViewPortWidth = width;
		mViewPortHeight = height;

		mScreenProportionalConvertor = new ScreenProportionsConvertor( mViewPortWidth, mViewPortHeight );
		MathUtils.random.setSeed(Long.MIN_VALUE);

		 mCamera = new OrthographicCamera( mViewPortWidth, mViewPortHeight );
//		mCamera = new PerspectiveCamera(40f, 48, 32);
		mCamera.update();
		mCamera.position.set( 0, 0, 440);
		mCamera.lookAt( 0, 0, 0);
		mCamera.near = 1f;
		mCamera.far = 2000f;
		mCamera.setToOrtho( false );
		mCamera.update();
		
//		mTranformation.set( mCamera.combined );
		mTranformation.setToOrtho2D( -mViewPortWidth/2, -mViewPortHeight/2, mViewPortWidth, mViewPortHeight );

		mBatch = new SpriteBatch();
		mCameraInput = new CameraInputController( mCamera );
		
		mInputProcessor = globalInputProcessor;
		mInputProcessor.addProcessor( this );
//		mInputProcessor.addProcessor( mCameraInput );
		new Thread( GameShipPropertyPublisherTask.getInstance() ).start();		
		show();

	}
	
	private void constructWallShape()
	{	
		for( Barrier barrier : mLevel.mBariers.mBarriers )
		{
			Vector2[] cell = new Vector2[ barrier.BarrierShape.size() ];
			barrier.BarrierShape.toArray( cell );
			cell = GamePlayScreen.mScreenProportionalConvertor.convert( cell );
			{
				ChainShape chainShape = new ChainShape();
				chainShape.createLoop( cell );
				BodyDef chainBodyDef = new BodyDef();
				chainBodyDef.type = BodyDef.BodyType.StaticBody;
				Body cellBody = mGameWorld.getWorld().createBody( chainBodyDef );
				cellBody.createFixture( chainShape, 0 );
				mWallCellBodies.add( cellBody );
				mDrawingBodies.add( new WallDraw( cellBody ) );
				chainShape.dispose();
			}
		}
		
		mGameWorld.getWorld().setContactListener( new ContactListener()
		{
			@Override
			public void preSolve(Contact contact, Manifold oldManifold)
			{
				 int a = 0;
				 a++;	
			}
			
			@Override
			public void postSolve(Contact contact, ContactImpulse impulse)
			{
				int a = 0;
				 a++;
			}
			
			@Override
			public void endContact(Contact contact)
			{
				int a = 0;
				 a++;
			}
			
			@Override
			public void beginContact(Contact contact)
			{
				int a = 0;
				 a++;
				 
//				if (mMouseJoint != null && mMouseJoint.getBodyA() == contact.getFixtureA().getBody() )
//				{
//					mDestroyableJoint.add( mMouseJoint );
//					mMouseJoint = null;
//				}
//				else if (mMouseJoint != null && mMouseJoint.getBodyB() == contact.getFixtureB().getBody() )
//				{
//					mDestroyableJoint.add( mMouseJoint );
//					mMouseJoint = null;
//				}
				 
				mDestroyableBody.add( contact.getFixtureA().getBody() );
//				mDestroyableBody.add( contact.getFixtureB().getBody() );
			}
		});
	}
//
//	private void constructWallShape2()
//	{
//		{
//			int wallHeight = 20;
//			ConstrainsTableLayout cellsTable = new ConstrainsTableLayout( 
//					(int)GameScreen.mScreenProportionalConvertor.convertX( 0 ),
//					(int)GameScreen.mScreenProportionalConvertor.convertY( mViewPortHeight / 2 - wallHeight ), 
//					(int)GameScreen.mScreenProportionalConvertor.convertX( mViewPortWidth / 2 ), 
//					(int)GameScreen.mScreenProportionalConvertor.convertY( wallHeight ), 
//					3, 
//					(int)GameScreen.mScreenProportionalConvertor.convertX( 20 ) );
//			List<Vector2[]> cellLayouts = cellsTable.getHorizontalCellRects();
//
//			for( Vector2[] cell : cellLayouts )
//			{
//				ChainShape chainShape = new ChainShape();
//				chainShape.createLoop( cell );
//				BodyDef chainBodyDef = new BodyDef();
//				chainBodyDef.type = BodyDef.BodyType.StaticBody;
//				Body cellBody = mGameWorld.getWorld().createBody( chainBodyDef );
//				cellBody.createFixture( chainShape, 0 );
//				mWallCellBodies.add( cellBody );
//				mDrawingBodies.add( new WallDraw( cellBody ) );
//				chainShape.dispose();
//			}
//			cellLayouts = cellsTable.getHorizontalPaddingRects();
//			mGravityBodyPlaces.addAll( cellLayouts );
//		}
//		{
//			ConstrainsTableLayout cellsTable = new ConstrainsTableLayout((int) ( (-1) * mViewPortWidth / 2 + 150.0f ),
//					-mViewPortHeight / 2, 500, 20, 3, 20);
//			List<Vector2[]> cellLayouts = cellsTable.getHorizontalCellRects();
//
//			for (Vector2[] cell : cellLayouts) {
//				ChainShape chainShape = new ChainShape();
//				chainShape.createLoop(cell);
//				BodyDef chainBodyDef = new BodyDef();
//				chainBodyDef.type = BodyDef.BodyType.StaticBody;
//				Body cellBody = mGameWorld.getWorld().createBody(chainBodyDef);
//				cellBody.createFixture(chainShape, 0);
//				mWallCellBodies.add(cellBody);
//				mDrawingBodies.add(new WallDraw(cellBody));
//				chainShape.dispose();
//			}
//			cellLayouts = cellsTable.getHorizontalPaddingRects();
//			mGravityBodyPlaces.addAll(cellLayouts);
//		}
//	}
	
	private void createBackground()
	{
		GameBackgroundDraw2D background = new GameBackgroundDraw2D( mViewPortX, mViewPortY, mViewPortWidth, mViewPortHeight );
		TextureRandomDistribution backgroundRandom = new TextureRandomDistribution( TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_BACKGROUND_STAR1 ), 0, 0, mViewPortWidth, mViewPortHeight, 2.0f, 5, 1.0f, true );
		TextureRandomDistribution backgroundRandom01 = new TextureRandomDistribution( TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_BACKGROUND_STAR1 ), 0, 0, mViewPortWidth, mViewPortHeight, 2.5f, 6, 1.0f, true );
		TextureRandomDistribution backgroundRandom2 = new TextureRandomDistribution( TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_BACKGROUND_STAR2 ), 0, 0, mViewPortWidth, mViewPortHeight, 4.0f, 3, .5f, true );
		TextureRandomDistribution backgroundRandom21 = new TextureRandomDistribution( TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_BACKGROUND_STAR2 ), 0, 0, mViewPortWidth, mViewPortHeight, 5.0f, 3, .5f, true );
		TextureRandomDistribution backgroundRandom3 = new TextureRandomDistribution( TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_BACKGROUND_STAR3 ), 0, 0, mViewPortWidth, mViewPortHeight, 8.0f, 1, 1.0f, true );
//		backgroundRandom3.setShaking( true );
		mListeners.add( background );
		mListeners.add( backgroundRandom );
		mListeners.add( backgroundRandom01 );
		mListeners.add( backgroundRandom2 );
		mListeners.add( backgroundRandom21 );
		mListeners.add( backgroundRandom3 );
		mDrawingBodies.add( background );
		mDrawingBodies.add( backgroundRandom );
		mDrawingBodies.add( backgroundRandom01 );
		mDrawingBodies.add( backgroundRandom2 );
		mDrawingBodies.add( backgroundRandom21 );
		mDrawingBodies.add( backgroundRandom3 );
				
		WorldBoundsDraw boundDraw = new WorldBoundsDraw( mGameWorld.getBounds(), TexturesCache.getInstance().get( Texture_Types.TEXTURE_PLAYSCREEN_METEORITE1 ) );
		mDrawingBodies.add( boundDraw );
		mListeners.add( boundDraw );
	}

	private void createPlayer()
	{
		mShipBody = new GameShipBody( mGameWorld.getWorld(), 
									  GamePlayScreen.mScreenProportionalConvertor.proportionalXscale( mLevel.mActiveObject.Position.x ), 
									  GamePlayScreen.mScreenProportionalConvertor.proportionalYscale( mLevel.mActiveObject.Position.y )
									);
		
		GameActiveObject playerBody = new GameActiveObject( mShipBody.getBody() );
		GameShipDraw2D playerDraw = new GameShipDraw2D( mShipBody.getX(), mShipBody.getY() );

		mBallBodies.add( playerBody );
		mDrawingBodies.add( playerDraw );
		
		PropertyChangeListeners listeners = new PropertyChangeListeners();
		listeners.add( playerBody );
		listeners.add( playerDraw );
		listeners.add( this );
		
		GameShipPropertyPublisherTask.getInstance().setShipBody( mShipBody.getBody() );
		GameShipPropertyPublisherTask.getInstance().addListeners( listeners );
	}

	private void createGravityBodies()
	{
		CircleShape ballShape = new CircleShape();
		ballShape.setRadius( GamePlayScreen.mScreenProportionalConvertor.proportionalYscale( 10.0f ) );

		FixtureDef def = new FixtureDef();
		def.restitution = 0.9f;
		def.friction = 0.1f;
		def.shape = ballShape;
		def.density = 1f;
		BodyDef boxBodyDef = new BodyDef();
		boxBodyDef.type = BodyDef.BodyType.StaticBody;

		for( LevelGravityObject levelGravityObject : mLevel.mGravityObjects.mGravityObjects )
		{
			// Create the BodyDef, set a random position above the
			// ground and create a new body
			Vector2 levelPosition = new Vector2( levelGravityObject.mPosition.x, levelGravityObject.mPosition.y );
			Vector2 propMoved = GamePlayScreen.mScreenProportionalConvertor.proportionalMove( levelPosition );
			boxBodyDef.position.set( propMoved );
			Body boxBody = mGameWorld.getWorld().createBody(boxBodyDef);
			MassData massData = boxBody.getMassData();
			massData.mass = 1.0f;
			boxBody.setMassData(massData);
			boxBody.createFixture(def);
			
			GravityBody gravBody = new GravityBody( boxBody );
			GravityBodyDraw2D gravDraw = new GravityBodyDraw2D( propMoved.x, propMoved.y );
			
			BodyGravityUserData userData = new BodyGravityUserData(gravBody, gravDraw );
			
			boxBody.setUserData( userData );
			
			mGravityBodies.add( gravBody );

			mDrawingBodies.add( gravDraw );
			
			PropertyChangeListeners listeners = new PropertyChangeListeners();
			listeners.add( gravBody );
			listeners.add( gravDraw );
			
			GameShipPropertyPublisherTask.getInstance().addListeners( listeners );
			
//			BodyWrapperTask bodyWrapperTask = new BodyWrapperTask( boxBody, listeners );
//			new Thread( bodyWrapperTask ).start();
		}
		ballShape.dispose();
	}

	private void createGoalStaticBody()
	{
		CircleShape ballShape = new CircleShape();
		ballShape.setRadius(15);

		FixtureDef def = new FixtureDef();
		def.restitution = 0.9f;
		def.friction = 0.1f;
		def.shape = ballShape;
		def.density = 10.0f;
		BodyDef boxBodyDef = new BodyDef();
		boxBodyDef.type = BodyDef.BodyType.KinematicBody;

		// Create the BodyDef, set a random position above the
		// ground and create a new body
		boxBodyDef.position.x = mLevel.mGoal.Position.x;
		boxBodyDef.position.y = mLevel.mGoal.Position.y;
		Body body = mGameWorld.getWorld().createBody(boxBodyDef);
		body.createFixture(def);

//		mGravityBodies.add( new GravityBody( body ) );
//		mDrawingBodies.add( new GoalDraw( body ) );

		ballShape.dispose();
	}

	private void applyBallForce()
	{
		for( GameActiveObject influencedBody : mBallBodies )
		{
			for( GravityBody gravBody : mGravityBodies )
			{
				Vector2 force = gravBody.getGravityForce2( influencedBody.getBody() );
				influencedBody.getBody().applyForce( force, influencedBody.getBody().getWorldCenter(), true );
			}
		}
	}

	private void renderObjects(float deltaTime)
	{
		synchronized( mDrawingBodies )
		{
			try
			{
				for( IDraw drawObj : mDrawingBodies )
				{
					drawObj.draw( mBatch, deltaTime );
				}
			}
			catch( Exception e )
			{
				int a = 0;
				a++;
			}
		}
	}

	public void show()
	{
		// Gdx.input.setInputProcessor((InputProcessor) this);
		mGameWorld = new GameWorld( mViewPortWidth, mViewPortHeight, true );
		mListeners.add( mGameWorld );
		createBackground();
		createPlayer();
		constructWallShape();
//		createGoalStaticBody();
		createGravityBodies();
	}

	public void render(float delta)
	{
		// TODO Auto-generated method stub
		
		int w = (int)getWidth();
		int h = Gdx.graphics.getHeight();
		int l = (int) getX();
		int y = (int) getY();
        
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
        Gdx.gl.glViewport( 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() );
		
		mCamera.update();
		fixedStep(Gdx.graphics.getDeltaTime());

//		mBatch.setTransformMatrix( mTranformation );
		mBatch.setProjectionMatrix( mCamera.combined );
		renderObjects(delta);
//		mTranformation.translate( -0.05f, 0.0f, 0.0f );
		mDebugRenderer.render(mGameWorld.getWorld(), mTranformation);
//		mTranformation.rotate( 1.0f, 0.0f, 0.0f, 1 );

		applyBallForce();
		
		for( Joint joint : mDestroyableJoint )
		{
			mGameWorld.getWorld().destroyJoint( joint );
		}
		mDestroyableJoint.clear();

		for( Body body : mDestroyableBody )
		{
			mGameWorld.getWorld().destroyBody( body );
		}
		mDestroyableBody.clear();
	}
	
	public void draw (Batch batch, float parentAlpha)
	{
		float delta = Gdx.graphics.getDeltaTime();
		render( delta );
	}

	private boolean fixedStep(float delta)
	{
		physicsTimeLeft += delta;
		if (physicsTimeLeft > MAX_TIME_PER_FRAME)
			physicsTimeLeft = MAX_TIME_PER_FRAME;

		boolean stepped = false;
		while (physicsTimeLeft >= TIME_STEP) {
			mGameWorld.getWorld().step(TIME_STEP, VELOCITY_ITERS, POSITION_ITERS);
			physicsTimeLeft -= TIME_STEP;
			stepped = true;
		}
		return stepped;
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

	/**
	 * we instantiate this vector and the callback here so we don't irritate the
	 * GC
	 **/
	private Vector2 testPoint = new Vector2();
	private QueryCallback callback = new QueryCallback()
	{
		@Override
		public boolean reportFixture(Fixture fixture)
		{
//			if ( fixture.getBody().getUserData() != null && ( fixture.getBody().getUserData() instanceof String ) &&  ((String)fixture.getBody().getUserData()).equalsIgnoreCase( "Player" ) )
			if( fixture.testPoint( testPoint.x, testPoint.y ) )
			{
				// && 
				mHitBody = fixture.getBody();
				return false;
			}
			else
				return true;
		}
	};

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		// TODO Auto-generated method stub
		// mBallX = screenX - mViewPortWidth / 2;
		// mBallY = screenY;

		int normScreenX = ( screenX - mViewPortWidth / 2 );
		int normScreenY = ( mViewPortHeight / 2 - screenY );
		
		// translate the mouse coordinates to world coordinates
		testPoint.set( normScreenX, normScreenY );
//		Vector3 unProjected = mCamera.unproject(testPoint);

		// ask the world which bodies are within the given
		// bounding box around the mouse pointer
		mHitBody = null;
		
		mGameWorld.getWorld().QueryAABB( callback, normScreenX - 200, normScreenY - 200, normScreenX + 200, normScreenY + 200 );

		// if we hit something we create a new mouse joint
		// and attach it to the hit body.
		if (mHitBody == null)
		{
			return false;
		}
		
		if( mHitBody.getUserData() instanceof BodyGravityUserData )
		{
			BodyGravityUserData userData = ( BodyGravityUserData ) mHitBody.getUserData();
			PropertyChangeListeners listeners = new PropertyChangeListeners();
			listeners.add( userData.getGravBody() );
			listeners.add( userData.getGravDraw() );
			Box2dGravitySlider gravSlider = new Box2dGravitySlider( new SliderInputProperty( userData.getGravBody().getGravityRadius(), userData.getGravBody().getBody().getPosition() ), listeners, testPoint );
			mInputProcessor.addProcessor( gravSlider.getInputProcessor() );
			mUserActivity = gravSlider;
			mDrawingBodies.add( gravSlider );
			
			PartialCicleDraw obj = new PartialCicleDraw( (int)userData.getGravBody().getBody().getPosition().x + (int)getWidth() / 2, (int)userData.getGravBody().getBody().getPosition().y + (int)getHeight() / 2 );
			mTouchCreatedObjects.add( obj );
			mDrawingBodies.add( obj );
		}
		else if( mHitBody.getUserData() instanceof BodyTypePlayer )
		{
			mShipBody.jointing( testPoint.x, testPoint.y );
		}

		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		if( null != mUserActivity )
		{
			//due to we are in InputMultiplexer.touchUp function, I cannot remove InputProcessor here
			new Timer().scheduleTask( new InputProcessorRealeaseTask( mInputProcessor, mUserActivity.getInputProcessor() ), 0.5f );
			mUserActivity.dispose();
			mUserActivity = null;
		}
		mShipBody.unjointing();
		List<Object> removedObjects = new ArrayList<Object>();
		for( Disposable disposable : mTouchCreatedObjects )
		{
			disposable.dispose();
			for( IDraw draw : mDrawingBodies )
			{
				if( draw == disposable )
				{
					removedObjects.add( draw );
				}
			}
		}
		
		for( Object obj : removedObjects )
		{
			mDrawingBodies.remove( obj );
		}
		
		return false;
	}

	/** another temporary vector **/
	private Vector2 target = new Vector2();
	
	private PositionProperty mPositionProperty = new PositionProperty();

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer)
	{
		if( false == mShipBody.isJointing() )
		{
			processNotBodiesDragging( screenX, screenY );
			
			return false;
		}
		int normScreenX = ( screenX - mViewPortWidth / 2 );
		int normScreenY = ( mViewPortHeight / 2 - screenY );

		mShipBody.moveJointed( normScreenX, normScreenY );
		
		return false;
	}
	
	private void processNotBodiesDragging( int screenX, int screenY )
	{
		mPositionProperty.setPosition( screenX, mViewPortHeight - screenY );
		for( IDisposablePropertyListener listener : mTouchCreatedObjects )
		{
			listener.onChanged( mPositionProperty );
		}
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		// mBallX = screenX - mViewPortWidth / 2;
		// mBallY = screenY;
		return false;
	}

	@Override
	public boolean scrolled(int amount)
	{
//		mCamera.rotate((float) amount * 3f, 0, 0, 1);
		return false;
	}

	private boolean mInitialMove = false;
	@Override
	public void onChanged(IProperty prop)
	{
		if( prop instanceof MoveProperty )
		{
			if( true == mInitialMove )
			{
				Vector2 delta = ((MoveProperty)prop).getDelta();
				mTranformation.translate( -delta.x, -delta.y, 0 );
			}
			else
			{
				mInitialMove = true;
			}
		}
		mListeners.onChanged( prop );
	}
}
