package com.jordanml.game.util;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.jordanml.game.objects.AbstractGameObject;

public class CameraHelper
{
    private static final String TAG = CameraHelper.class.getName();
    
    /**
     * CONSTANTS
     */
    private final float FOLLOW_SPEED = 4.0f;
    private final float MAX_ZOOM_IN = 0.25f;
    private final float MAX_ZOOM_OUT = 10.0f;

    /**
     * position - camera position
     * zoom     - current zoom level
     * target   - the object that the camera is following
     */
    private Vector2 position;
    private float zoom;
    private AbstractGameObject target;

    public CameraHelper()
    {
        position = new Vector2();
        zoom = 1.0f;
    }

    /**
     * Update the camera with respect to the time passed since the previous frame
     * @param deltaTime time passed since the previous frame
     */
    public void update(float deltaTime)
    {
        if (!hasTarget())
            return;
        
        /*
         * May need to refactor rest of 'update' when fixing errors.
         */
        position.lerp(target.position, FOLLOW_SPEED * deltaTime);
        // Prevent camera from moving down too far
        position.y = Math.max(-1f, position.y);

        //position.x = target.position.x + target.origin.x;
        //position.y = target.position.y + target.origin.y;
    }

    /**
     * Sets the position of the camera
     * @param x x position
     * @param y y position
     */
    public void setPosition(float x, float y)
    {
        this.position.set(x, y);
    }

    /**
     * Returns the camera's position
     * @return
     */
    public Vector2 getPosition()
    {
        return position;
    }

    /**
     * Increases the zoom by the given amount
     * @param amount amount to increase zoom by
     */
    public void addZoom(float amount)
    {
        setZoom(zoom + amount);
    }

    /**
     * Sets the zoom to the given amount
     * @param zoom 
     */
    public void setZoom(float zoom)
    {
        this.zoom = MathUtils.clamp(zoom, MAX_ZOOM_IN, MAX_ZOOM_OUT);
    }

    /**
     * Returns the zoom 
     * @return
     */
    public float getZoom()
    {
        return zoom;
    }

    /**
     * Sets the camera's target to the given object
     * @param target
     */
    public void setTarget(AbstractGameObject target)
    {
        this.target = target;
    }

    /**
     * Returns the object that the camera is targeting
     * @return
     */
    public AbstractGameObject getTarget()
    {
        return target;
    }

    /**
     * Checks if the camera currently has a target
     * @return
     */
    public boolean hasTarget()
    {
        return target != null;
    }

    /**
     * Checks if the given object is the camera's target
     * @param target
     * @return
     */
    public boolean hasTarget(AbstractGameObject target)
    {
        return hasTarget() && this.target.equals(target);
    }

    /**
     * Apply camera settings to given OrthographicCamera
     * @param camera
     */
    public void applyTo(OrthographicCamera camera)
    {
        camera.position.x = position.x;
        camera.position.y = position.y;
        camera.zoom = zoom;
        camera.update();
    }
}
