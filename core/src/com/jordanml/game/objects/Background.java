package com.jordanml.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.jordanml.game.assets.Assets;

public class Background extends AbstractGameObject
{
    /**
     * regBackground - texture region representing the background
     * length        - the length of the background
     * width         - the width of the background
     */
    private TextureRegion regBackground;
    private float length;
    private float width;
    
    
    public Background(float length, float width)
    {
        this.length = length;
        this.width = width;
        init();
    }
    
    /**
     * Initialize properties for the Background
     */
    private void init()
    {
        dimension.set(length, width);
        origin.x = length / 2;
        origin.y = width / 2;
        position.x = 0;
        position.y = -3;
        regBackground = Assets.instance.decorations.background;
    }
    
    /**
     * Render the Background
     * @param batch - SpriteBatch used to draw the Background
     */
    @Override
    public void render(SpriteBatch batch)
    {
        TextureRegion reg = regBackground;
        batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight(), scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
                reg.getRegionHeight(), false, false);
        //batch.draw(reg.getTexture(), 0, 0, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 1, 1, 0, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
    }
    
}
