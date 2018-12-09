package com.jordanml.game.util;

/**
 * Keeps track of constants that may be needed across multiple classes.
 *
 */
public class Constants
{
    // Visible game world dimensions (in meters)
    public static final float VIEWPORT_WIDTH = 5.0f;
    public static final float VIEWPORT_HEIGHT = 5.0f;
    
    // GUI Width
    public static final float VIEWPORT_GUI_WIDTH = 800.0f;
    // GUI Height
    public static final float VIEWPORT_GUI_HEIGHT = 480.0f;
    
    // Location of description file for texture atlas
    public static final String TEXTURE_ATLAS_OBJECTS = "images/damned.atlas";
    
    // Location of level-01 image
    public static final String LEVEL_01 = "levels/level-01.png";
    
    // Skins
    public static final String TEXTURE_ATLAS_UI = "images/damned-ui.atlas";
    public static final String TEXTURE_ATLAS_LIBGDX_UI = "images/uiskin.atlas";
    
    public static final String SKIN_DAMNED_UI = "images/damned-ui.json";
    public static final String SKIN_LIBGDX_UI = "images/uiskin.json";
    
    public static final String PREFERENCES = "/CSC361_F18_Moreno-Lacalle-core/src/com/jordanml/game/util/GamePreferences.java";

    
    // Background
    public static final float BG_X = -400.0f;
    public static final float BG_Y = -240.0f;
    
    // Level constants
    public static final int MAX_LIVES = 3;
    public static final int CANDYCORN_SCORE = 50;
    public static final int ORB_SCORE = 100;
    public static final int ORB_TIMEOUT = 10;
    public static final int GAME_OVER_DELAY = 5;
    public static final int GOAL_REACHED = 400;
}
