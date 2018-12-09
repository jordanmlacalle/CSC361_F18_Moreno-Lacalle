package com.jordanml.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.jordanml.game.assets.Assets;
import com.jordanml.game.level.Level;

public class Bats extends AbstractGameObject
{
    private static final String TAG = Bats.class.getName();
    private float length;
    private Array<Bat> bats;
    private World world;
    private float playerY;
    
    /**
     * Inner class
     */
    private class Bat extends AbstractGameObject
    {
        private Animation<TextureRegion> animNormal;
        
        public Bat()
        {
           init();
        }
        
        /**
         * Initializes the current Bat. Sets the Bat's animation.
         */
        public void init()
        {
            animNormal = Assets.instance.bat.animNormal;
            setAnimation(animNormal);
        }
        
        /**
         * Initializes the physics box2d properties for the Bat and adds
         * it to the given World
         * @param world the box2d World to add the Bat to
         */
        public void initPhysics(World world)
        {
            // speed
            Vector2 speed = new Vector2();
            speed.x += 2.0f; //base speed
            // random additional speed
            speed.x += MathUtils.random(0.0f, 0.75f);
            speed.x = -speed.x;
            
            // Create new body for Bat
            BodyDef bodyDef = new BodyDef();
            // Land is static
            bodyDef.type = BodyType.KinematicBody;
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
            fixtureDef.isSensor=true;
            body.createFixture(fixtureDef);
            body.setUserData(this);
            polygonShape.dispose();
            body.setLinearVelocity(speed);
        }
        
        @Override
        public void render(SpriteBatch batch)
        {
            // TODO Auto-generated method stub
            TextureRegion reg = null;
            
            reg = animation.getKeyFrame(stateTime);
            
            batch.draw(reg.getTexture(), position.x + origin.x, position.y + origin.y, origin.x, origin.y, dimension.x,
                    dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
                    reg.getRegionHeight(), true, false);
            
        }
        
        @Override
        public void update(float deltaTime)
        {
            super.update(deltaTime);
        }
    }
    
    public Bats(float length, Level level)
    {
        this.length = length;
        //init();
    }
    
    /**
     * Initialize Bats properties and spawn first bats
     */
    public void init()
    {        
        dimension.set(0.25f, 0.25f);

        int distFac = 5;
        int numBats = (int) (length / distFac);
        
        bats = new Array<Bat>(2 * numBats);
        
        for (int i = 0; i < numBats; i++)
        {
            Bat bat = spawnBat();
            bat.position.x = i * distFac;
            bats.add(bat);
        }
    }
    
    /**
     * Creates and initializes a new Bat object
     * 
     * @return Returns a new Bat object
     */
    private Bat spawnBat()
    {
        Bat bat = new Bat();
        bat.dimension.set(dimension);
        // position
        Vector2 pos = new Vector2();
        pos.x = length + 10; // position after end of level
        pos.y += 1.75; // base position
        pos.y += MathUtils.random(playerY - 2.0f, playerY + 2.0f) + (MathUtils.randomBoolean() ? 1 : -1); // random additional position
        bat.position.set(pos);
        bat.initPhysics(world);
        Gdx.app.debug(TAG, "Spawn bat at " + pos.x + " " + pos.y);
        return bat;
    }
    
    /**
     * Initializes
     * @param world
     */
    public void initPhysics(World world)
    {
        this.world = world;
        
        /*
        for(Bat bat : bats)
        {
            if(!bat.physicsInitialized)
                bat.initPhysics(world);
        }*/
    }
    
    @Override
    public void render(SpriteBatch batch)
    {
        for(Bat bat : bats)
            bat.render(batch);
    }
    
    /**
     * Updates all bats in the level. If a bat has moved beyond the
     * end of the level (on the left) then it is destroyed. 
     * 
     * @param deltaTime
     */
    @Override
    public void update(float deltaTime)
    {   
        for(int i = bats.size - 1; i >= 0; i--)
        {
            Bat bat = bats.get(i);
            bat.update(deltaTime);
            
            if(bat.position.x < -10)
            {
                // bat moved outside of world
                // destroy and spawn new bat at end of level
                bats.removeIndex(i);
                bats.add(spawnBat());
            }
        }
    }
    
    /**
     * Get updated Y-pos for player
     * @param playerY
     */
    public void updatePlayerY(float playerY)
    {
        this.playerY = playerY;
    }

}
