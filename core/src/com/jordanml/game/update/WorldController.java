package com.jordanml.game.update;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.jordanml.game.level.Level;
import com.jordanml.game.objects.Land;
import com.jordanml.game.util.Constants;
import com.jordanml.game.util.CameraHelper;

/**
 * This class will handle most general game logic.
 *
 */
public class WorldController extends InputAdapter
{    
    public static final String TAG = WorldController.class.getName();
    public Level level;
    public CameraHelper cameraHelper;
    public World world;
    
    public WorldController()
    {
        init();
    }
    
    /**
     * Initialize WorldController
     */
    public void init()
    {
        // Set world controller as input processor
        Gdx.input.setInputProcessor(this);
        cameraHelper = new CameraHelper();
        initLevel();
        initPhysics();
    }
    
    /**
     * Initialize box2d world and object physics
     */
    private void initPhysics()
    {
        if(world != null)
            world.dispose();
        
        world = new World(new Vector2(0, -9.81f), true);

        for(Land land : level.lands)
        {
            land.initPhysics(world);
        }
        
        level.player.initPhysics(world);
    }
    
    private void initLevel()
    {
        level = new Level(Constants.LEVEL_01);
        cameraHelper.setTarget(level.player);
    }
    
    /**
     * Update world objects
     * 
     * @param deltaTime - The time passed since the last frame
     */
    public void update(float deltaTime)
    {
        
        handleDebugInput(deltaTime);
        level.update(deltaTime);
        world.step(deltaTime, 8, 3);
        
        cameraHelper.update(deltaTime);
    }

    /**
     * Handles some special inputs for resetting game world and switching camera target
     * @param keycode the keycode for the pressed key
     */
    @Override
    public boolean keyUp(int keycode)
    {
        // Reset game world
        if (keycode == Keys.R)
        {
            init();
            Gdx.app.debug(TAG, "Game world reset");
        }
        // Toggle camera follow
        else if (keycode == Keys.ENTER)
        {
            cameraHelper.setTarget(null);
            Gdx.app.debug(TAG, "Camera follow enabled: " + cameraHelper.hasTarget());
        }
        
        return false;
    }

    /**
     * Handle debug input, allows testing during development. Enables control of
     * primary (non-gui) camera.
     * 
     * @param deltaTime time passed since the previous frame
     */
    private void handleDebugInput(float deltaTime)
    {
        if (Gdx.app.getType() != ApplicationType.Desktop)
            return;

        if(!cameraHelper.hasTarget(level.player))
        {
            // Camera Controls (move)
            float camMoveSpeed = 5 * deltaTime;
            float camMoveSpeedAccelerationFactor = 5;
            if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
                camMoveSpeed *= camMoveSpeedAccelerationFactor;
            if (Gdx.input.isKeyPressed(Keys.LEFT))
                moveCamera(-camMoveSpeed, 0);
            if (Gdx.input.isKeyPressed(Keys.RIGHT))
                moveCamera(camMoveSpeed, 0);
            if (Gdx.input.isKeyPressed(Keys.UP))
                moveCamera(0, camMoveSpeed);
            if (Gdx.input.isKeyPressed(Keys.DOWN))
                moveCamera(0, -camMoveSpeed);
            if (Gdx.input.isKeyPressed(Keys.BACKSPACE))
                cameraHelper.setPosition(0, 0);

            // Camera Controls (zoom)
            float camZoomSpeed = 1 * deltaTime;
            float camZoomSpeedAccelerationFactor = 5;
            if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
                camZoomSpeed *= camZoomSpeedAccelerationFactor;
            if (Gdx.input.isKeyPressed(Keys.COMMA))
                cameraHelper.addZoom(camZoomSpeed);
            if (Gdx.input.isKeyPressed(Keys.PERIOD))
                cameraHelper.addZoom(-camZoomSpeed);
            if (Gdx.input.isKeyPressed(Keys.SLASH))
                cameraHelper.setZoom(1);
        }
        else
        {
            level.player.handleInput();
        }
        
    }
    
    /**
     * Move the camera to the coordinates specified by (x, y)
     * 
     * @param x
     * @param y
     */
    private void moveCamera(float x, float y)
    {
        x += cameraHelper.getPosition().x;
        y += cameraHelper.getPosition().y;
        cameraHelper.setPosition(x, y);
    }
}
