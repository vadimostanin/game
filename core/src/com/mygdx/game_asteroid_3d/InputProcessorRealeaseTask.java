package com.mygdx.game_asteroid_3d;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Timer;

public class InputProcessorRealeaseTask extends Timer.Task
{
	private InputMultiplexer mInputMultiplexer;
	private InputProcessor mInputProcessor;
	
	public InputProcessorRealeaseTask( InputMultiplexer inputMultiplexer, InputProcessor inputProcessor )
	{
		mInputMultiplexer = inputMultiplexer;
		mInputProcessor = inputProcessor;		
	}
	
	public void run()
	{
		mInputMultiplexer.removeProcessor( mInputProcessor );
	}
}
