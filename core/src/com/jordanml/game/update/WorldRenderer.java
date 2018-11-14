package com.jordanml.game.update;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
        
        //TODO: remove b2Debug
        b2Debug = new Box2DDebugRenderer();
    }

    /**
     * Renders the level 
     */
    public void render()
    {
        renderWorld(batch);
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
