package com.jordanml.game.update;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Disposable;

import com.jordanml.game.assets.Assets;
import com.jordanml.game.update.WorldController;
import com.jordanml.game.util.Constants;

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

    // TODO: Remove box2DDebugDraw
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
        
        //TODO: remove b2Debug
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
     * Renders the background
     * 
     * @param batch SpriteBatch used to render the background
     */
    private void renderBackground(SpriteBatch batch)
    {
        batch.setProjectionMatrix(cameraBg.combined);
        batch.begin();
        
        batch.draw(Assets.instance.decorations.background, -cameraBg.viewportWidth / 2, -cameraBg.viewportHeight / 2, 0, 0, cameraBg.viewportWidth, cameraGui.viewportWidth, 1, 1, 0.0f);
        
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
