package com.mygdx.game_asteroid_3d;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Disposable;

public interface IGameUserActivity extends Disposable
{
	InputProcessor getInputProcessor();
}
