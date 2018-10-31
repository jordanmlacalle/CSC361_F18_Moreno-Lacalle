package com.jordanml.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class AbstractGameObject
{
    /**
     * position  - vector representing an objects coordinates in game world
     * dimension - the dimensions of the object
     * origin    - positon of the objects origin
     * scale     - scale of the object
     * rotation  - angle at which the object is currently oriented
     * body      - object's Box2D body
     */
    public Vector2 position;
    public Vector2 dimension;
    public Vector2 origin;
    public Vector2 scale;
    public float rotation;
    public Body body;
    
    // objects current speed in m/s
    public Vector2 velocity;
    // objects positive and negative max speed in m/s
    public Vector2 terminalVelocity;
    // opposing force, slows object until velocity=0. If 0, objects velocity will
    // not decrease
    public Vector2 friction;

    // objects constant acceleration in m/s^2
    public Vector2 acceleration;
    // the physical body that will be used for collision detection w other objects
    public Rectangle bounds;
    
    /**
     * Constructor, initializes members
     */
    public AbstractGameObject()
    {
        position = new Vector2();
        dimension = new Vector2(1, 1);
        origin = new Vector2();
        scale = new Vector2(1, 1);
        rotation = 0;

        velocity = new Vector2();
        terminalVelocity = new Vector2(1, 1);
        friction = new Vector2();
        acceleration = new Vector2();
        bounds = new Rectangle();
    }
    
    /**
     * Updates the object, taking the time passed since the previous frame into account
     * 
     * @param deltaTime time passed since the previous frame
     */
    public void update(float deltaTime)
    {
        
    }
    
    /**
     * Abstract method for rendering the object.
     * 
     * @param deltaTime time passed since the previous frame
     */
    public abstract void render(SpriteBatch batch);
}
