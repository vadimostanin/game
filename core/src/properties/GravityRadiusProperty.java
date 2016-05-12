package properties;

import com.badlogic.gdx.math.Vector2;

public class GravityRadiusProperty implements IProperty
{
	private float mRadius = 0;
	
	public GravityRadiusProperty( float radius )
	{
		mRadius = radius;
	}
	
	public float getRadius()
	{
		return mRadius;
	}
	
	public void setRadius( float radius )
	{
		mRadius = radius;
	}
}
