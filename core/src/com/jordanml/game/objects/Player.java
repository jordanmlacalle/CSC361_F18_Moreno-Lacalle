package com.jordanml.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.jordanml.game.assets.Assets;

public class Player extends AbstractGameObject
{
    public static final String TAG = Player.class.getName();
    
    
    // Player's viewing direction
    enum VIEW_DIRECTION
    {
        LEFT, RIGHT;
    }
    
    // Player's movement state
    enum MOVE_STATE
    {
        MOVE_RIGHT,
        MOVE_LEFT,
        STOPPED;
    }
    
    // Player's jumping state
    enum JUMP_STATE
    {
        JUMP_START,
        GROUNDED;
    }
    
    private VIEW_DIRECTION viewDirection;
    private MOVE_STATE moveState;
    private JUMP_STATE jumpState;
    private TextureRegion player;
    
    public Player()
    {
        init();
    }
    
    /**
     * Initializes the Player's assets and properties.
     */
    private void init()
    {
        // Until animation is added, Player sprite is a still image
        player = Assets.instance.player.player;
        
        // Set Player dimensions
        dimension.set(1, 1);        
        // Center image on game object
        origin.set(dimension.x / 2, dimension.y / 2);
        // Bounding box for collision detection
        bounds.set(0, 0, dimension.x - 0.3f, dimension.y - 0.1f);
        // Set physics values
        terminalVelocity.set(3.0f, 4.0f);
        friction.set(12.0f, 0.0f);
        acceleration.set(0.0f, -25.0f);
        // View direction
        viewDirection = VIEW_DIRECTION.RIGHT;
        moveState = MOVE_STATE.STOPPED;
        jumpState = JUMP_STATE.GROUNDED;
    }
    
    /**
     * Initializes the box2d physics for the Player and adds its body to the given world.
     * 
     * @param world
     */
    public void initPhysics(World world)
    {
        // Create new body for Player
        BodyDef bodyDef = new BodyDef();
        // Land is static
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.position.set(this.position);
        // Create new body using body definition
        Body body = world.createBody(bodyDef);
        this.body = body;
        PolygonShape polygonShape = new PolygonShape();
        origin.x = this.bounds.width / 2.0f + 0.15f;
        origin.y = this.bounds.height / 2.0f + 0.06f;
        polygonShape.setAsBox(this.bounds.width / 2.0f, this.bounds.height / 2.0f, origin, 0);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        body.createFixture(fixtureDef);
        body.setUserData(this);
        polygonShape.dispose();
    }
    
    /**
     * Updates the Player position, rotation, velocity, etc.
     * 
     * deltaTime - the time passed since the previous frame
     */
    @Override
    public void update(float deltaTime)
    {
        super.update(deltaTime);
        velocity = body.getLinearVelocity();
        
        if(velocity.x < 0)
        {
            viewDirection = VIEW_DIRECTION.LEFT;
        }
        else if(velocity.x > 0)
        {
            viewDirection = VIEW_DIRECTION.RIGHT;
        }
        
        // Update velocity
        step();
    }
    
    /**
     * Renders the Player using the given SpriteBatch
     * 
     * batch - The SpriteBatch used to render the Player
     */
    @Override
    public void render(SpriteBatch batch)
    {
        TextureRegion reg = null;
        
        reg = player;
        
        boolean flip = false;
        
        if(viewDirection == VIEW_DIRECTION.LEFT)
        {
            flip = true;
        }
        
        batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y,
                scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
                reg.getRegionWidth(), reg.getRegionHeight(), flip, false);
    }
    
    /**
     * Handles keyboard input for the Player
     */
    public void handleInput()
    {
        if(Gdx.input.isKeyPressed(Keys.RIGHT))
        {
            moveState = MOVE_STATE.MOVE_RIGHT;
        }
        else if(Gdx.input.isKeyPressed(Keys.LEFT))
        {
            moveState = MOVE_STATE.MOVE_LEFT;
        }
        else
        {
            moveState = MOVE_STATE.STOPPED;
        }
        
        if(Gdx.input.isKeyJustPressed(Keys.SPACE) && body.getLinearVelocity().y == 0)
        {
            jumpState = JUMP_STATE.JUMP_START;
        }
    }
    
    /**
     * Updates Player velocity according to movement states
     */
    public void step()
    {
        Vector2 vel = body.getLinearVelocity();
        
        switch(moveState)
        {
            case MOVE_RIGHT:
                if(body.getLinearVelocity().y == 0)
                    vel.x = 5.0f;
                break;
            case MOVE_LEFT:
                if(body.getLinearVelocity().y == 0)
                    vel.x = -5.0f;
                break;
            case STOPPED:
                vel.x = 0.0f;
                break;
        }
        
        switch(jumpState)
        {
            case JUMP_START:
                vel.y = 5.0f;
                jumpState = JUMP_STATE.GROUNDED;
                break;
            case GROUNDED:
                break;
        }
        
        body.setLinearVelocity(vel);
    }

}
