package com.jordanml.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;

import com.jordanml.game.assets.Assets;

public abstract class AbstractScreen implements Screen
{
    protected Game game;
    
    public AbstractScreen(Game game)
    {
        this.game = game;
    }
    
    public abstract void render (float deltaTime);
    
    public abstract void resize (int width, int height);
    
    public abstract void show ();
    
    public abstract void hide ();
    
    public abstract void pause ();
    
    /**
     * Loads assets using new AssetManager
     */
    public void resume()
    {
        Assets.instance.init(new AssetManager());
    }
    
    /**
     * Frees asset memory
     */
    public void dispose()
    {
        Assets.instance.dispose();
    } 
}
