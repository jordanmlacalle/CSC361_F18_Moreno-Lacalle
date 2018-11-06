package com.jordanml.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.jordanml.game.assets.Assets;

public class Land extends AbstractGameObject
{
    //enum representing type of Land (floating or normal)
    public enum LAND_TYPE
    {
        NORM,
        FLOAT;
    }
 
    /**
     * length    - the length of a block of land
     * landType  - the land type (floating or normal)
     * regEdge   - texture region representing the edge of a block of Land
     * regMiddle - texture region representing the middle of a block of Land 
     */
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
        dimension.set(1.0f, 1.0f);
        
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
    
    /**
     * Renders the Land with edges added on the left and right ends
     */
    @Override
    public void render(SpriteBatch batch)
    {
        TextureRegion reg = null;

        float relX = 0;
        float relY = 0;

        // Draw left edge
        reg = regEdge;
        relX -= dimension.x;
        batch.draw(reg.getTexture(), position.x + relX, position.y + relY, origin.x, origin.y, dimension.x,
                dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
                reg.getRegionHeight(), false, false);

        // Draw middle
        relX = 0;
        reg = regMiddle;
        for (int i = 0; i < length; i++)
        {
            boolean flip;
            
            if(i%2 == 0)
                flip = true;
            else
                flip = false;
            
            batch.draw(reg.getTexture(), position.x + relX, position.y + relY, origin.x, origin.y, dimension.x + 0.01f,
                    dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
                    reg.getRegionHeight(), flip, false);
            relX += dimension.x;
        }

        // Draw right edge
        reg = regEdge;
        batch.draw(reg.getTexture(), position.x + relX, position.y + relY, origin.x + dimension.x / 8, origin.y,
                dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
                reg.getRegionWidth(), reg.getRegionHeight(), true, false);
        
    }

}
