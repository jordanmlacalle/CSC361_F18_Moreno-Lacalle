package com.jordanml.game.update;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.jordanml.game.assets.Assets;
import com.jordanml.game.level.Level;
import com.jordanml.game.objects.AbstractGameObject;
import com.jordanml.game.objects.Candycorn;
import com.jordanml.game.objects.Land;
import com.jordanml.game.objects.Orb;
import com.jordanml.game.objects.Player;
import com.jordanml.game.screens.MenuScreen;
import com.jordanml.game.util.Constants;
import com.jordanml.game.util.AudioManager;
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
    
    public int lives;
    public int score;
    
    private Game game;
    private float timeLeftGameOverDelay;
    
    public WorldController(Game game)
    {
        this.game = game;
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
        score = 0;
        lives = Constants.MAX_LIVES;
        initLevel();
    }
    
    /**
     * Initialize box2d world and object physics
     */
    private void initPhysics()
    {
        if(world != null)
            world.dispose();
        
        world = new World(new Vector2(0, -9.81f), true);

        for(Candycorn candycorn : level.candycorns)
        {
            candycorn.initPhysics(world);
        }
        
        for(Orb orb : level.orbs)
        {
            orb.initPhysics(world);
        }
        
        for(Land land : level.lands)
        {
            land.initPhysics(world);
        }
        
        level.player.initPhysics(world);
        
        world.setContactListener(new ContactListener()
                                {

                                    @Override
                                    public void beginContact(Contact contact)
                                    {
                                        Fixture fixtureA = contact.getFixtureA();
                                        Fixture fixtureB = contact.getFixtureB();
                                        Fixture object;
                                        
                                        // Check for contact between player and another object
                                        if(fixtureA.getBody().getUserData() instanceof Player || fixtureB.getBody().getUserData() instanceof Player)
                                        {   
                                            if(fixtureA.getBody().getUserData() instanceof Player)
                                                object = fixtureB;
                                            else
                                                object = fixtureA;
                                            
                                            // Check for contact between player and Candycorn
                                            if(object.getBody().getUserData() instanceof Candycorn)
                                            {
                                                Gdx.app.debug(TAG, " Player <-> Candycorn");
                                                
                                                Candycorn candy = (Candycorn) object.getBody().getUserData();
                                                
                                                if(!candy.collected)
                                                {
                                                    candy.collected = true;
                                                    //world.destroyBody(candy.body);
                                                    score += Constants.CANDYCORN_SCORE;
                                                }
                                            }
                                            // Check for contact between player and Orb
                                            else if(object.getBody().getUserData() instanceof Orb)
                                            {
                                                Gdx.app.debug(TAG, " Player <-> Orb");
                                                
                                                Orb orb = (Orb) object.getBody().getUserData();
                                                
                                                if(!orb.collected)
                                                {
                                                    orb.collected = true;
                                                    score += Constants.ORB_SCORE;
                                                    level.player.collectedOrb();
                                                    AudioManager.instance.play(Assets.instance.sound.powerup);
                                                }
                                            }
                                        }
                                        
                                        
                                    }

                                    @Override
                                    public void endContact(Contact contact)
                                    {
                                        // TODO Auto-generated method stub
                                    }

                                    @Override
                                    public void preSolve(Contact contact, Manifold oldManifold)
                                    {
                                        // TODO Auto-generated method stub
                                        
                                    }

                                    @Override
                                    public void postSolve(Contact contact, ContactImpulse impulse)
                                    {
                                        // TODO Auto-generated method stub
                                        
                                    }
                                });
    }
    
    /**
     * Initializes the level
     */
    private void initLevel()
    {
        level = new Level(Constants.LEVEL_01);
        cameraHelper.setTarget(level.player);
        initPhysics();
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
        
        if(isGameOver())
        {
            timeLeftGameOverDelay -= deltaTime;
            
            if(timeLeftGameOverDelay < 0)
            {
                backToMenu();
                return;
            }
        }
        
        // Check if player has fallen off
        else if(level.player.position.y < -5)
        {
            // Play life lost sound ?
            lives--;
            
            if(isGameOver())
            {
                timeLeftGameOverDelay = Constants.GAME_OVER_DELAY;
            }
            else
                initLevel();
        }
    }
    
    /**
     * Check if the game is over
     * @return true if the game is over
     */
    public boolean isGameOver()
    {
        if(lives < 0)
            return true;
        else
            return false;
    }

    /**
     * Return player to the menu screen
     */
    private void backToMenu()
    {
        game.setScreen(new MenuScreen(game));
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
            if(cameraHelper.hasTarget())
            {
                cameraHelper.setTarget(null);
                Gdx.app.debug(TAG, "Camera follow enabled: " + cameraHelper.hasTarget());
            }
            else
            {
                cameraHelper.setTarget(level.player);
                cameraHelper.setZoom(1.0f);
                Gdx.app.debug(TAG, "Camera follow enabled: " + cameraHelper.hasTarget());
            }
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
