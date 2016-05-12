package properties;

public class LastGravityForceProperty implements IProperty
{
	private float mLastForce;
	
	public LastGravityForceProperty( float force )
	{
		mLastForce = force;
	}
	
	public float getForce()
	{
		return mLastForce;
	}
}
