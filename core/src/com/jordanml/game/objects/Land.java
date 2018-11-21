package com.jordanml.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

import com.jordanml.game.assets.Assets;

public class Land extends AbstractGameObject
{
    //enum representing type of Land (floating or normal)
    public enum LAND_TYPE
    {
        NORM,
        FLOAT,
        EDGE_NORM,
        EDGE_FLOAT;
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
        
        if(landType == LAND_TYPE.NORM)
        {
            regEdge = Assets.instance.land.edge_norm;
            regMiddle = Assets.instance.land.middle_norm;
            dimension.set(1.0f, 1.0f);
            setLength(1);
        }
        else if(landType == LAND_TYPE.FLOAT)
        {
            regEdge = Assets.instance.land.edge_float;
            regMiddle = Assets.instance.land.middle_float;
            dimension.set(1.0f, 0.75f);
            setLength(1);
        }     
    }
    
    /**
     * Initializes the box2d physics for this Land object
     * @param world
     */
    public void initPhysics(World world)
    {  
        float offsetHeight = 0.0f;
        float hRatio = 1.0f;
        
        // Set offsetHeight and hRatio to decrease bounding box size for floating land
        if(landType == LAND_TYPE.FLOAT)
        {
            offsetHeight = 0.25f;
            hRatio = 0.5f / 0.75f;
        }
        
        bounds.set(0, 0, dimension.x * (length + 2), dimension.y * hRatio);
        // Create new body for Land
        BodyDef bodyDef = new BodyDef();
        // Land is static
        bodyDef.type = BodyType.KinematicBody;
        bodyDef.position.set(this.position);
        // Create new body using body definition
        Body body = world.createBody(bodyDef);
        this.body = body;
        PolygonShape polygonShape = new PolygonShape();
        origin.x = this.bounds.width / 2.0f - 1.0f;
        origin.y = this.bounds.height / 2.0f + offsetHeight;
        polygonShape.setAsBox(this.bounds.width / 2.0f, this.bounds.height / 2.0f, origin, 0);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        body.createFixture(fixtureDef);
        body.setUserData(this);
        polygonShape.dispose();
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
    public void update(float deltaTime)
    {
        super.update(deltaTime);
    }
    
    /**
     * Renders the Land with edges added on the left and right ends
     */
    @Override
    public void render(SpriteBatch batch)
    {
        
        if(landType == LAND_TYPE.EDGE_NORM || landType == LAND_TYPE.EDGE_FLOAT)
            return;
        
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
