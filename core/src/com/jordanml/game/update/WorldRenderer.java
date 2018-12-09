package com.jordanml.game.update;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;

import com.jordanml.game.assets.Assets;
import com.jordanml.game.update.WorldController;
import com.jordanml.game.util.Constants;
import com.jordanml.game.util.GamePreferences;

/**
 * WorldRenderer handles all rendering for game world objects, including GUI elements and level elements.
 *
 */
public class WorldRenderer implements Disposable
{
    private OrthographicCamera camera;
    private OrthographicCamera cameraGui;
    private OrthographicCamera cameraBg;
    private SpriteBatch batch;
    private WorldController worldController;

    private final boolean BOX2D_DEBUG = false;
    private Box2DDebugRenderer b2Debug;
    
    // TODO: Will need a camera for GUI down the line
            
    public WorldRenderer(WorldController worldController)
    {
        this.worldController = worldController;
        init();
    }

    /**
     * Initializes WorldRenderer object. Sets up the SpriteBatch used to draw objects and creates the world camera.
     */
    private void init()
    {
        batch = new SpriteBatch();
        
        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        camera.position.set(0, 0, 0);
        camera.update();
        
        cameraGui = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        cameraGui.position.set(0, 0, 0);
        cameraGui.setToOrtho(true); // flip y-axis
        cameraGui.update();
        
        cameraBg = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        cameraBg.position.set(0, 0, 0);
        //cameraBg.setToOrtho(true);
        cameraBg.update();
        
        b2Debug = new Box2DDebugRenderer();
    }

    /**
     * Renders the level 
     */
    public void render()
    {
        renderBackground(batch);
        renderWorld(batch);
        renderGui(batch);
        // TODO: remove b2Debug.render() call
        if(BOX2D_DEBUG)
            b2Debug.render(worldController.world, camera.combined);
    }

    /**
     * Renders the world using the given SpriteBatch
     * 
     * @param batch SpriteBatch used to draw world objects
     */
    private void renderWorld(SpriteBatch batch)
    {
        worldController.cameraHelper.applyTo(camera);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        worldController.level.render(batch);
        batch.end();
    }
    
    /**
     * Renders the GUI 
     * @param batch SpriteBatch used to draw world objects
     */
    private void renderGui(SpriteBatch batch)
    {
        batch.setProjectionMatrix(cameraGui.combined);
        batch.begin();
        
        renderGuiLives(batch);
        renderGuiScore(batch);
        renderGuiGameOverMessage(batch);
        
        if(GamePreferences.instance.showFpsCounter)
            renderGuiFpsCounter(batch);
        
        batch.end();
    }
    
    /**
     * Renders the current score
     * 
     * @param batch SpriteBatch used to render the score
     */
    private void renderGuiScore(SpriteBatch batch)
    {
        float x = -15;
        float y = -15;
        float offsetX = 50;
        float offsetY = 50;
        
        /*
        if(worldController.scoreVisual < worldController.score)
        {
            long shakeAlpha = System.currentTimeMillis() % 360;
            float shakeDist = 1.5f;
            offsetX += MathUtils.sinDeg(shakeAlpha * 2.2f) * shakeDist;
            offsetY += MathUtils.sinDeg(shakeAlpha * 2.9f) * shakeDist;
        }*/
        
        batch.draw(Assets.instance.candy.candycorn, x, y, 50, 50, 100, 100, 0.35f, -0.35f, 0);
        Assets.instance.fonts.defaultBig.draw(batch, "" + worldController.score, x + 75, y + 37);
    }
    
    /**
     * Renders the UI representing the lives remaining
     * 
     * @param batch SpriteBatch used to render the remaining lives
     */
    private void renderGuiLives(SpriteBatch batch)
    {
        float x = cameraGui.viewportWidth - 50 - Constants.MAX_LIVES * 50;
        float y = -15;
        
        for (int i = 0; i < worldController.lives; i++)
        {
            batch.draw(Assets.instance.gui.pumpkin, x + i * 50, y, 50, 50, 120, 100, 0.35f, -0.35f, 0);
            batch.setColor(1, 1, 1, 1);
        }
    }
    
    /**
     * Renders the "Game Over" message that is displayed when the player runs out of lives
     * 
     * @param batch SpriteBatch used to draw "Game Over" message
     */
    private void renderGuiGameOverMessage(SpriteBatch batch)
    {
        float x = cameraGui.viewportWidth / 2;
        float y = cameraGui.viewportHeight / 2;
        if (worldController.isGameOver())
        {
            BitmapFont fontGameOver = Assets.instance.fonts.defaultBig;
            fontGameOver.setColor(1, 0.75f, 0.25f, 1);
            fontGameOver.draw(batch, "GAME OVER", x, y, 0, Align.center, false);
            fontGameOver.setColor(1, 1, 1, 1);
        }
    }
    
    /**
     * Renders FPS counter in bottom right of viewport
     * 
     * @param batch SpriteBatch used to draw FPS counter
     */
    private void renderGuiFpsCounter(SpriteBatch batch)
    {
        float x = cameraGui.viewportWidth - 55;
        float y = cameraGui.viewportHeight - 15;
        int fps = Gdx.graphics.getFramesPerSecond();
        BitmapFont fpsFont = Assets.instance.fonts.defaultNormal;
        if (fps >= 45)
        {
            // 45 or more FPS show up in green
            fpsFont.setColor(0, 1, 0, 1);
        } 
        else if (fps >= 30)
        {
            // 30 or more FPS show up in yellow
            fpsFont.setColor(1, 1, 0, 1);
        } 
        else
        {
            // less than 30 FPS show up in red
            fpsFont.setColor(1, 0, 0, 1);
        }
        fpsFont.draw(batch, "FPS: " + fps, x, y);
        fpsFont.setColor(1, 1, 1, 1); // white
    }
    
    /**
     * Renders the background
     * 
     * @param batch SpriteBatch used to render the background
     */
    private void renderBackground(SpriteBatch batch)
    {
        batch.setProjectionMatrix(cameraBg.combined);
        batch.begin();
        
        batch.draw(Assets.instance.decorations.background, Constants.BG_X, Constants.BG_Y, 0, 0, Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT, 1, 1, 0.0f);
        batch.end();
    }

    /**
     * Resizes the viewport
     * 
     * @param width desired width after resizing
     * @param height desired height after resizing
     */
    public void resize(int width, int height)
    {
        camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width;
        camera.update();
    }

    /**
     * Frees memory
     */
    @Override
    public void dispose()
    {
        batch.dispose();
    }

}
