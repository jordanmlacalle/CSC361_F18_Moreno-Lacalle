package com.jordanml.game.objects;

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

public class Goal extends AbstractGameObject
{

    private Animation<TextureRegion> animNormal;
    private Animation<TextureRegion> animExplode;
    
    public boolean reached = false;
    
    public Goal()
    {
        init();
    }
    
    /**
     * Initialize animations, dimenisons, and bounds for the Goal
     */
    private void init()
    {
        animNormal = Assets.instance.goal.animNormal;
        animExplode = Assets.instance.goal.animExplode;
        dimension.set(1.0f, 1.0f);
        bounds.set(0, 0, dimension.x, dimension.y);
        setAnimation(animNormal);
    }
    
    /**
     * Initializes the box2d physics body for the Goal and adds it to the given world
     * @param world The box2d world to add the body to
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
        TextureRegion reg = null;
                
        reg = animation.getKeyFrame(stateTime);
        
        batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y,
                scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
                reg.getRegionWidth(), reg.getRegionHeight(), false, false);
    }

    @Override
    public void update(float deltaTime)
    {
        super.update(deltaTime); 
    }
    
    /**
     * To be called when the player reaches the goal. Triggers the explosion animation.
     */
    public void onPlayerReached()
    {
        reached = true;
        setAnimation(animExplode);
    }
}
