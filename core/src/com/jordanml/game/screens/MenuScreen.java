package com.jordanml.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.jordanml.game.assets.Assets;
import com.jordanml.game.util.AudioManager;
import com.jordanml.game.util.Constants;

public class MenuScreen extends AbstractScreen
{
    public static final String TAG = MenuScreen.class.getName();
    
    // Debug
    private final float DEBUG_REBUILD_INTERVAL = 5.0f;
    private boolean debugEnabled = false;
    private float debugRebuildStage;
    
    private Stage stage;
    private Skin skinDamned;
    private Skin skinLibgdx;
    
    // Menu - Primary
    private Image imgLogo;
    private Image imgBackground;
    private Image imgTree;
    private Image imgSign;
    private Image imgTombstone;
    private Image imgJack;
    private Button btnPlay;

    public MenuScreen(Game game)
    {
        super(game);
    }

    /**
     * Build the menu background layer
     * @return returns the Table representing the background layer
     */
    private Table buildBackgroundLayer()
    {
        Table layer = new Table();
        
        imgBackground = new Image(skinDamned, "background");
        layer.add(imgBackground);
        
        return layer;
    }
    
    /**
     * Builds the object layer. This layer contains menu decorations such as
     * the game logo.
     * 
     * @return returns the Table representing the object layer
     */
    private Table buildObjectsLayer()
    {
        Table layer = new Table();
        
        imgLogo = new Image(skinDamned, "logo");
        layer.addActor(imgLogo);
        imgLogo.setPosition(200, 100);
        
        imgTree = new Image(skinDamned, "tree");
        layer.addActor(imgTree);
        imgTree.setPosition(500, 0);
        
        imgTombstone = new Image(skinDamned, "tombstone");
        layer.addActor(imgTombstone);
        imgTombstone.setPosition(100, 0);
        
        imgJack = new Image(skinDamned, "jack");
        layer.addActor(imgJack);
        imgJack.setPosition(450, -10);
        
        
        return layer;
    }
    
    /**
     * Builds the controls layer. This layer contains any objects in the menu
     * that the player can interact with.
     * 
     * @return returns the Table representing the controls layer
     */
    private Table buildControlsLayer()
    {
        Table layer = new Table();
        
        btnPlay = new Button(skinDamned, "play");
        layer.bottom();
        layer.add(btnPlay);
        btnPlay.addListener(new ChangeListener()
                            {
                                @Override
                                   public void changed(ChangeEvent event, Actor actor)
                                   {
                                        onPlayClicked();
                                   }
                            });
                
        return layer;
    }
    
    /**
     * Defines the action to take when the "Play" button is clicked
     */
    private void onPlayClicked()
    {
        AudioManager.instance.stopMusic();
        game.setScreen(new GameScreen(game));
    }
    
    /**
     * Rebuilds the menu Stage
     */
    private void rebuildStage()
    {
        skinDamned = new Skin(Gdx.files.internal(Constants.SKIN_DAMNED_UI), new TextureAtlas(Constants.TEXTURE_ATLAS_UI));
        skinLibgdx = new Skin(Gdx.files.internal(Constants.SKIN_LIBGDX_UI), new TextureAtlas(Constants.TEXTURE_ATLAS_LIBGDX_UI));
        
        // Build all layers
        Table layerBackground = buildBackgroundLayer();
        Table layerObjects = buildObjectsLayer();
        Table layerControls = buildControlsLayer();
        
        // Assemble stage for menu screen
        stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        stack.add(layerBackground);
        stack.add(layerObjects);
        stack.add(layerControls);
    }
    
    /**
     * Renders the menu
     */
    @Override
    public void render(float deltaTime)
    {
        // OpenGL clear color -> black
        Gdx.gl.glClearColor(0.0f,  0.0f,  0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        if (debugEnabled)
        {
            debugRebuildStage -= deltaTime;
            
            if (debugRebuildStage <= 0)
            {
                debugRebuildStage = DEBUG_REBUILD_INTERVAL;
                rebuildStage();
            }
        }
        
        stage.act(deltaTime);
        stage.draw();
        stage.setDebugAll(false);
        
    }

    /**
     * Defines how to resize the menu
     * 
     * @param width the new width of the screen
     * @param height the new height of the screen
     */
    @Override
    public void resize(int width, int height)
    {
        stage.getViewport().update(width, height, true);
    }

    /**
     * Displays the menu
     */
    @Override
    public void show()
    {
        stage = new Stage(new StretchViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT));
        Gdx.input.setInputProcessor(stage);
        rebuildStage();
        AudioManager.instance.play(Assets.instance.music.menu);
    }

    /**
     * Hides the menu
     */
    @Override
    public void hide()
    {
        stage.dispose();
        skinDamned.dispose();
        skinLibgdx.dispose();
        
    }

    @Override
    public void pause()
    {
        
    }

}
