package com.jordanml.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;

import com.jordanml.game.assets.Assets;
import com.jordanml.game.screens.GameScreen;
import com.jordanml.game.screens.MenuScreen;
import com.jordanml.game.util.GamePreferences;


public class CSC361_F18_Moreno_Lacalle extends Game
{	
	/**
	 * Creates the game and starts it from the GameScreen
	 */
	@Override
    public void create()
	{
      // Set Libgdx log level
      Gdx.app.setLogLevel(Application.LOG_DEBUG);
      // Load assets
      Assets.instance.init(new AssetManager());
      // Load settings
      GamePreferences.instance.load();
      // Start game at MenuScreen
      setScreen(new MenuScreen(this));
	}
}
