package com.jordanml.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.jordanml.game.CSC361_F18_Moreno_Lacalle;

public class DesktopLauncher 
{
    private static boolean rebuildAtlas = false;
    private static boolean drawDebugOutline = false;
    
	public static void main (String[] arg) 
	{
	    if(rebuildAtlas)
	    {
	        Settings settings = new Settings();
            settings.maxWidth = 1024;
            settings.maxHeight = 1024;
            settings.debug = drawDebugOutline;
            TexturePacker.process(settings, "assets-raw/images","../core/assets/images","damned");
	    }
	    
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new CSC361_F18_Moreno_Lacalle(), config);
	}
}
