package com.jordanml.game.update;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
    private SpriteBatch batch;
    private WorldController worldController;

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
    }

    /**
     * Renders the level 
     */
    public void render()
    {
        renderWorld(batch);
    }

    /**
     * Renders the world using the given SpriteBatch
     * 
     * @param batch SpriteBatch used to draw world objects
     */
    private void renderWorld(SpriteBatch batch)
    {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        // Nothing to render yet
        // TODO: Will render level here down the line
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
