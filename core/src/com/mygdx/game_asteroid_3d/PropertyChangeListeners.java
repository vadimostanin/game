package com.mygdx.game_asteroid_3d;

import java.util.ArrayList;

import properties.IProperty;

public class PropertyChangeListeners extends ArrayList<IPropertyChangeListener> implements IPropertyChangeListener
{
	public PropertyChangeListeners()
	{
		;
	}
	@Override
	public void onChanged( IProperty prop )
	{
		for( IPropertyChangeListener listener : this )
		{
			listener.onChanged( prop );
		}
	}
}
