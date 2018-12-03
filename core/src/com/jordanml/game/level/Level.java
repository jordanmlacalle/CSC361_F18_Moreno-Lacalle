package com.jordanml.game.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import com.jordanml.game.objects.Land;
import com.jordanml.game.objects.Orb;
import com.jordanml.game.objects.Player;
import com.jordanml.game.objects.AbstractGameObject;
import com.jordanml.game.objects.Background;
import com.jordanml.game.objects.Candycorn;

public class Level
{
    public static final String TAG = Level.class.getName();
    
    // Objects
    public Array<Candycorn> candycorns;
    public Array<Land> lands;
    public Array<Orb> orbs;
    public Background background;
    public Player player;
    
    public enum BLOCK_TYPE
    {
        EMPTY        (  0,   0,   0), // Black
        PLAYER_SPAWN (255, 255, 255), // White
        LAND_NORM    (  0, 255,   0), // Green
        LAND_FLOAT   (  0, 255, 255), // Green + Blue
        CANDY_CORN   (255, 255,   0), // Yellow
        ORB          (  0,   0, 255), // Blue
        GOAL         (255,   0,   0); // Red
        
        // The color of the block
        private int color;
        
        /**
         * Sets color for the block
         * @param r amount of red
         * @param g amount of green
         * @param b amount of blue
         */
        private BLOCK_TYPE(int r, int g, int b)
        {
            color = r << 24 | g << 16 | b << 8 | 0xff;
        }
        
        /**
         * Checks if the given color is the same color
         * 
         * @param color the color to be checked
         * @return boolean - true if the color is the same
         */
        public boolean sameColor(int color)
        {
            return this.color == color;
        }
        
        /**
         * Gets the color
         * @return integer representing the color of the block
         */
        public int getColor()
        {
            return color;
        }
    }
    
    public Level(String filename)
    {
        init(filename);
    }
    
    /**
     * Initialize the level, loading game objects from the given file
     * 
     * @param filename
     */
    private void init(String filename)
    {
        candycorns = new Array<Candycorn>();
        orbs = new Array<Orb>();
        lands = new Array<Land>();
        background = new Background(20,15);
        
        // Load image file that represents the level data
        Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));
        // Scan pixels from top-left to bottom-right
        int lastPixel = -1;
        for (int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++)
        {
            for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++)
            {
                AbstractGameObject obj = null;
                float offsetHeight = 0;
                // height grows from bottom to top
                float baseHeight = pixmap.getHeight() - pixelY;
                // Get color of current pixel as 32-bit RGBA value
                int currentPixel = pixmap.getPixel(pixelX, pixelY);
                /*
                 * Find matching color value to identify block type at (x, y) point and create
                 * the corresponding game object if there is a match
                 */
                // Empty space
                if (BLOCK_TYPE.EMPTY.sameColor(currentPixel))
                {
                    // do nothing
                } 
                else if(isLand(currentPixel))
                {                    
                    if(lastPixel != currentPixel || !isLand(pixmap.getPixel(pixelX + 1, pixelY)))
                    {
                        /** DO NOTHING*/
                    }
                    else
                    {
                        // CHECK FOR MIDDLE
                        // Check if current pixel is middle (extend)
                        if(isLand(pixmap.getPixel(pixelX + 1, pixelY)))
                        {
                            if(!isLand(pixmap.getPixel(pixelX - 2, pixelY)))
                            {
                                // New land
                                if(BLOCK_TYPE.LAND_NORM.sameColor(currentPixel))
                                    obj = new Land(Land.LAND_TYPE.NORM);
                                else
                                    obj = new Land(Land.LAND_TYPE.FLOAT);

                                obj.position.set(pixelX , baseHeight);

                                lands.add((Land) obj);
                            }
                            else
                            {
                                // Extend Land
                                lands.get(lands.size - 1).increaseLength(1);
                            }
                        }
                            
                    }
                } 
                else if(BLOCK_TYPE.CANDY_CORN.sameColor(currentPixel))
                {
                    obj = new Candycorn();
                    offsetHeight = 2.5f;
                    obj.position.set(pixelX, baseHeight *  obj.dimension.y + offsetHeight);
                    candycorns.add((Candycorn) obj);
                }
                else if(BLOCK_TYPE.PLAYER_SPAWN.sameColor(currentPixel))
                {
                    obj = new Player();
                    obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
                    player = (Player) obj;
                }
                else if(BLOCK_TYPE.ORB.sameColor(currentPixel))
                {
                    obj = new Orb();
                    obj.position.set(pixelX, baseHeight + offsetHeight);
                    orbs.add((Orb) obj);
                }
                else
                {
                    // decode currentPixel color
                    int r = 0xff & (currentPixel >>> 24); // red color channel
                    int g = 0xff & (currentPixel >>> 16); // green color channel
                    int b = 0xff & (currentPixel >>> 8); // blue color channel
                    int a = 0xff & currentPixel; // alpha channel
                    Gdx.app.error(TAG, "Unknown object at x<" + pixelX + "> y<" + pixelY + ">: r<" + r + "> g<" + g
                            + "> b<" + b + "> a<" + a + ">");
                }
                lastPixel = currentPixel;
            }
        }
    }
    
    /**
     * Checks if the given pixel is a Land block
     * @param pixel the pixel to be checked
     * @return true if the pixel is a Land block
     */
    public boolean isLand(int pixel)
    {
        if(BLOCK_TYPE.LAND_NORM.sameColor(pixel) || BLOCK_TYPE.LAND_FLOAT.sameColor(pixel))
        {
            return true;
        }
        
        return false;
    }
    
    /**
     * Update level objects with respect to time passed since the previous frame
     * 
     * @param deltaTime time passed since the previous frame
     */
    public void update(float deltaTime)
    {        
        for(Land land : lands)
        {
            land.update(deltaTime);
        }
        
        for(Orb orb : orbs)
        {
            orb.update(deltaTime);
        }
        player.update(deltaTime);
    }
    
    /**
     * Render level objects
     * 
     * @param batch
     */
    public void render(SpriteBatch batch)
    {
        //background.render(batch);
        
        for(Candycorn candycorn : candycorns)
        {
            candycorn.render(batch);
        }
        
        for(Land land : lands)
        {
            land.render(batch);
        }
        
        for(Orb orb : orbs)
        {
            orb.render(batch);
        }
        
        player.render(batch);
    }
}

