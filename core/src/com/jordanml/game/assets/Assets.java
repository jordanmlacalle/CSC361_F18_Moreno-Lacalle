package com.jordanml.game.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
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
    public AssetGui gui;
    public AssetMusic music;
    public AssetSound sound;
    public AssetFonts fonts;
    public AssetCandy candy;
    public AssetOrb orb;
    
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
        assetManager.load("music/menu_song.mp3", Music.class);
        assetManager.load("sounds/jump.ogg", Sound.class);
        assetManager.load("sounds/powerup.wav", Sound.class);
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
        gui = new AssetGui(atlas);
        music = new AssetMusic(assetManager);
        sound = new AssetSound(assetManager);
        candy = new AssetCandy(atlas);
        orb = new AssetOrb(atlas);
        fonts = new AssetFonts();
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
        // Player animations
        public final Animation<TextureRegion> animIdle;
        public final Animation<TextureRegion> animRun;
        public final Animation<TextureRegion> animJump;
        
        public AssetPlayer(TextureAtlas atlas)
        {
            Array<AtlasRegion> regions = null;
            
            // Idle animation
            regions = atlas.findRegions("jack_idle");
            animIdle = new Animation<TextureRegion>(1.0f / 10.0f, regions, Animation.PlayMode.LOOP);
            
            // Run animation
            regions = atlas.findRegions("jack_run");
            animRun = new Animation<TextureRegion>(1.0f / 10.0f, regions, Animation.PlayMode.LOOP);

            // Jump animation
            regions = atlas.findRegions("jack_jump");
            animJump = new Animation<TextureRegion>(1.0f / 10.0f, regions, Animation.PlayMode.NORMAL);
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
    
    /**
     * Class that acts as a container for GUI assets
     */
    public class AssetGui
    {
        public final AtlasRegion pumpkin;
        
        public AssetGui(TextureAtlas atlas)
        {
            pumpkin = atlas.findRegion("pumpkin");
        }
    }
    
    /**
     * Class that acts as a container for Music assets
     */
    public class AssetMusic
    {
        public final Music menu;
        
        public AssetMusic(AssetManager am)
        {
            menu = am.get("music/menu_song.mp3", Music.class);
        }
    }
    
    /**
     * Class that acts as a container for Sound assets
     */
    public class AssetSound
    {
        public final Sound jump;
        public final Sound powerup;
        
        public AssetSound(AssetManager am)
        {
            jump = am.get("sounds/jump.ogg", Sound.class);
            powerup = am.get("sounds/powerup.wav", Sound.class);
        }
    }
    
    /**
     * Class that acts as a container for Candy collectible assets
     */
    public class AssetCandy
    {
        public final AtlasRegion candycorn;
        
        public AssetCandy(TextureAtlas atlas)
        {
            candycorn = atlas.findRegion("candycorn");
        }
    }
    
    /**
     * Class that acts as a container or Orb assets
     */
    public class AssetOrb
    {
        public final Animation<TextureRegion> animNormal;
        
        public AssetOrb(TextureAtlas atlas)
        {
            Array<AtlasRegion> regions = null;
            
            regions = atlas.findRegions("powerup");
            animNormal = new Animation<TextureRegion>(1.0f / 10.0f, regions, Animation.PlayMode.LOOP);
        }
    }
    /**
     * Gathers fonts to be used for text necessary to provide the user information while playing
     * the game.
     */
    public class AssetFonts
    {
        public final BitmapFont defaultSmall;
        public final BitmapFont defaultNormal;
        public final BitmapFont defaultBig;

        public AssetFonts()
        {
            // create three fonts using Libgdx's 15px bitmap font
            defaultSmall = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);
            defaultNormal = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);
            defaultBig = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);

            // set font sizes
            defaultSmall.getData().setScale(0.75f);
            defaultNormal.getData().setScale(1.0f);
            defaultBig.getData().setScale(2.0f);

            // enable linear texture filtering for smooth fonts
            defaultSmall.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
            defaultNormal.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
            defaultBig.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        }
    }
}
