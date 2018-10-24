package com.jordanml.game.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Disposable;

/**
 * Singleton class that manages, organizes, and provides access to game assets.
 *
 */
public class Assets implements Disposable, AssetErrorListener
{
    public static final String TAG = Assets.class.getName();
    public static final Assets instance = new Assets();
    
    private AssetManager assetManager;
    
    // Singleton
    private Assets()
    {
    }
    
    public void init(AssetManager assetManager)
    {
        this.assetManager = assetManager;
        
        
    }
    
    /**
     * Frees memory
     */
    @Override
    public void dispose()
    {
        assetManager.dispose();
    }
    
    /**
     * Outputs errors for assets that could not be loaded.
     */
    @Override
    public void error(AssetDescriptor asset, Throwable throwable)
    {
        Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'", (Exception)throwable);
    }
    
    
}
