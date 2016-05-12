package properties;

import com.badlogic.gdx.math.Vector2;

public class MoveProperty implements IProperty
{
	private Vector2 mDelta = new Vector2();
	
	public MoveProperty()
	{
		
	}
	
	public void setDeltas( float x, float y )
	{
		mDelta.set( x, y );
	}
	
	public void setDeltas( Vector2 delta )
	{
		mDelta.set( delta );
	}
	
	public Vector2 getDelta()
	{
		return mDelta;
	}
}
