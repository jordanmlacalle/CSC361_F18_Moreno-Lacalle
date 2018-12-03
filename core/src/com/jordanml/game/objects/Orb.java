package com.jordanml.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.jordanml.game.assets.Assets;

public class Orb extends AbstractGameObject
{
    public static final String TAG = Orb.class.getName();
    public boolean collected;
    
    private Animation<TextureRegion> animNormal;
    
    public Orb()
    {
        init();
    }
    
    public void init()
    {
        animNormal = Assets.instance.orb.animNormal;
        dimension.set(0.5f, 0.5f);
        bounds.set(0, 0, dimension.x, dimension.y);
        collected = false;
        setAnimation(animNormal);
    }
    
    /**
     * Initialize the physics properties for this object and add it to the world
     * @param world The box2d world in which the object will exist
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
    
    /**
     * Render the orb at its current state in animation
     */
    @Override
    public void render(SpriteBatch batch)
    {
        if(collected)
            return;
        
        TextureRegion reg = null;
        
        reg = animation.getKeyFrame(stateTime, true);
        
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
