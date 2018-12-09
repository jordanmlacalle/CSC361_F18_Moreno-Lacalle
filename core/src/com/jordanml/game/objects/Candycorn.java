package com.jordanml.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.jordanml.game.assets.Assets;

public class Candycorn extends AbstractGameObject
{

    private TextureRegion regCorn;
    public boolean collected;
    
    public Candycorn()
    {
        init();
    }
    
    /**
     * Initialize basic properties and assets for Candycorn
     */
    private void init()
    {
        collected = false;
        dimension.set(0.5f, 0.5f);
        bounds.set(0, 0, dimension.x, dimension.y);
        regCorn = Assets.instance.candy.candycorn;
    }
    
    /**
     * Initializes the physics body for the Candycorn and adds it to the given world
     * @param world the box2d world to add the body to
     */
    public void initPhysics(World world)
    {
        // Create new body for Candycorn
        BodyDef bodyDef = new BodyDef();
        // Land is static
        bodyDef.type = BodyType.StaticBody;
        bodyDef.position.set(this.position);
        // Create new body using body definition
        Body body = world.createBody(bodyDef);
        this.body = body;
        PolygonShape polygonShape = new PolygonShape();
        origin.x = this.bounds.width / 2.0f;
        origin.y = this.bounds.height / 2.0f;
        polygonShape.setAsBox(this.bounds.width / 2.0f, this.bounds.height / 2.0f, origin, 0);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef);
        body.setUserData(this);
        polygonShape.dispose();
    }
    
    @Override
    public void render(SpriteBatch batch)
    {
        if(collected)
        {
            return;
        }
        
        TextureRegion reg = null;
        
        reg = regCorn;
        
        batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y,
                scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
                reg.getRegionWidth(), reg.getRegionHeight(), false, false);
    }

    @Override
    public void update(float deltaTime)
    {
        super.update(deltaTime);        
    }
}
