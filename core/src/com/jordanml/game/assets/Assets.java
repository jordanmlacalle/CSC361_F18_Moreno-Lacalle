package com.jordanml.game.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Disposable;

import com.jordanml.game.util.Constants;

/**
 * Singleton class that manages, organizes, and provides access to game assets.
 *
 */
public class Assets implements Disposable, AssetErrorListener
{
    public static final String TAG = Assets.class.getName();
    public static final Assets instance = new Assets();
    
    private AssetManager assetManager;
    
    public AssetLand land;
    public AssetDecorations decorations;
    public AssetPlayer player;
    
    // Singleton
    private Assets()
    {
    }
    
    public void init(AssetManager assetManager)
    {
        this.assetManager = assetManager;
     // set asset manager error handler
        assetManager.setErrorListener(this);
        // load texture atlas
        assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
        assetManager.finishLoading();

        Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
        
        for (String a : assetManager.getAssetNames())
        {
            Gdx.app.debug(TAG, "asset: " + a);
        }

        TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);

        // enable texture filtering for pixel smoothing
        for (Texture t : atlas.getTextures())
        {
            t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        }

        // create game resource objects
        land = new AssetLand(atlas);
        decorations = new AssetDecorations(atlas);
        player = new AssetPlayer(atlas);
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
    
    /**
     * Class that acts as a container for Player assets
     */
    public class AssetPlayer
    {
        public final AtlasRegion player;
        
        public AssetPlayer(TextureAtlas atlas)
        {
            player = atlas.findRegion("jack_idle", 1);
        }
    }
    
    /**
     * Class that acts as a container for Land assets
     */
    public class AssetLand
    {
        public final AtlasRegion edge_norm;
        public final AtlasRegion middle_norm;
        public final AtlasRegion edge_float;
        public final AtlasRegion middle_float;
        
        public AssetLand(TextureAtlas atlas)
        {
            edge_norm = atlas.findRegion("land_edge_norm");
            middle_norm = atlas.findRegion("land_middle_norm");
            edge_float = atlas.findRegion("land_edge_float");
            middle_float = atlas.findRegion("land_middle_float");
        }
    }
    
    /**
     * Class that acts as a container for decoration assets
     */
    public class AssetDecorations
    {
        public final AtlasRegion background;
        
        public AssetDecorations(TextureAtlas atlas)
        {
            background = atlas.findRegion("background");
        }
    }
    
}
