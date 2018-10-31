package com.jordanml.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.jordanml.game.assets.Assets;

public class Land extends AbstractGameObject
{
    public enum LAND_TYPE
    {
        NORM,
        FLOAT;
    }
 
    private int length;
    private LAND_TYPE landType;
    private TextureRegion regEdge;
    private TextureRegion regMiddle;
    
    public Land(LAND_TYPE landType)
    {
        init(landType);
    }
    
    /**
     * Initialize Land properties
     * 
     * @param type the LAND_TYPE for this Land
     */
    private void init(LAND_TYPE type)
    {
        landType = type;
        dimension.set(1.0f, 1.5f);
        
        if(landType == LAND_TYPE.NORM)
        {
            regEdge = Assets.instance.land.edge_norm;
            regMiddle = Assets.instance.land.middle_norm;
        }
        else if(landType == LAND_TYPE.FLOAT)
        {
            regEdge = Assets.instance.land.edge_float;
            regMiddle = Assets.instance.land.middle_float;
        }
        
        setLength(1);
    }
    
    /**
     * Sets the length of the Land
     * 
     * @param length the desired length of the Land
     */
    public void setLength(int length)
    {
        this.length = length;

        // Update bounding box for collision detection
        bounds.set(0, 0, dimension.x * length, dimension.y);
    }

    /**
     * Increases length by the given amount
     * 
     * @param amount the value to increase the Land's length by
     */
    public void increaseLength(int amount)
    {
        setLength(length + amount);
    }
    
    @Override
    public void render(SpriteBatch batch)
    {
        TextureRegion reg = null;

        float relX = 0;
        float relY = 0;

        // Draw left edge
        reg = regEdge;
        relX -= dimension.x / 4;
        batch.draw(reg.getTexture(), position.x + relX, position.y + relY, origin.x, origin.y, dimension.x / 4 + 0.1f,
                dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
                reg.getRegionHeight(), false, false);

        // Draw middle
        relX = 0;
        reg = regMiddle;
        for (int i = 0; i < length; i++)
        {
            batch.draw(reg.getTexture(), position.x + relX, position.y + relY, origin.x, origin.y, dimension.x + 0.1f,
                    dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
                    reg.getRegionHeight(), false, false);
            relX += dimension.x;
        }

        // Draw right edge
        reg = regEdge;
        batch.draw(reg.getTexture(), position.x + relX, position.y + relY, origin.x + dimension.x / 8 + 0.1f, origin.y,
                dimension.x / 4, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
                reg.getRegionWidth(), reg.getRegionHeight(), true, false);
        
    }

}
