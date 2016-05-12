package properties;

import com.badlogic.gdx.math.Vector2;

public class SliderInputProperty implements IProperty
{
	private float mRadius = 0;
	private Vector2 mPosition;
	
	public SliderInputProperty( float radius, Vector2 position )
	{
		mRadius = radius;
		mPosition = position;
	}
	
	public float getRadius()
	{
		return mRadius;
	}
	
	public Vector2 getPosition()
	{
		return mPosition;
	}
}
