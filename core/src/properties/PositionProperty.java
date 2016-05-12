package properties;

import com.badlogic.gdx.math.Vector2;

public class PositionProperty implements IProperty
{
	Vector2 mPosition;
	
	public PositionProperty()
	{
		mPosition = new Vector2();
	}
	
	public Vector2 getPosition()
	{
		return mPosition;
	}
	
	public void setPosition( Vector2 position )
	{
		mPosition.set( position );
	}
	
	public void setPosition( float x, float y )
	{
		mPosition.set( x, y );
	}
}
