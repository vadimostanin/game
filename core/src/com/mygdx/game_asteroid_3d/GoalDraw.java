package com.mygdx.game_asteroid_3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class GoalDraw implements IDraw
{
	TextureRegion mTexture;
	SpriteBatch mSprite = new SpriteBatch();
	
	private float RADIUS = 1.0f;
	private float DIAMETR = RADIUS * 2;

	Body mBody;
	
	public GoalDraw( Body body )
	{
		String storagePaths = Gdx.files.getLocalStoragePath();
		mTexture = new TextureRegion( new Texture( Gdx.files.absolute( storagePaths + "goal.png" ) ) );
		mBody = body;
	}
	@Override
	public void draw( Batch batch, float deltaTime )
	{
		Vector2 position = mBody.getPosition();
		float angle = MathUtils.radiansToDegrees * mBody.getAngle();
		mSprite.begin();
		mSprite.draw(
				mTexture,
				position.x - RADIUS, position.y - RADIUS,
				RADIUS, RADIUS,
				DIAMETR, DIAMETR,
				1f, 1f,
				angle);	
		mSprite.end();
	}

}
