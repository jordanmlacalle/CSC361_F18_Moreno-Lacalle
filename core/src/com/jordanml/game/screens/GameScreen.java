package com.jordanml.game.screens;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.jordanml.game.screens.AbstractScreen;
import com.jordanml.game.update.WorldController;
import com.jordanml.game.update.WorldRenderer;

public class GameScreen extends AbstractScreen
{
    private WorldController worldController;
    private WorldRenderer worldRenderer;
    
    public GameScreen(Game game)
    {
        super(game);
    }
    
    /**
     * Update the world and render it to the screen
     */
    @Override
    public void render(float deltaTime)
    {
        // TODO: will need to be able to pause game down the line
        // Update world objects
        worldController.update(deltaTime);
        // Set clear screen color to orange
        Gdx.gl.glClearColor(214.0f / 255.0f, 122.0f / 255.0f, 10.0f / 255.0f, 1.0f);
        // Clear screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Render world
        worldRenderer.render();
    }
    
    /**
     * Resizes the viewport to the desired width and height
     */
    @Override
    public void resize(int width, int height)
    {
        worldRenderer.resize(width, height);
    }
    
    /**
     * Initializes the GameScreen
     */
    @Override
    public void show()
    {
        worldController = new WorldController();
        worldRenderer = new WorldRenderer(worldController);
    }
    
    /**
     * Hides the GameScreen, disposing of all assets
     */
    @Override
    public void hide()
    {
        worldRenderer.dispose();
    }
    
    /**
     * Method to be used when game paused
     */
    @Override
    public void pause() {}
    
    /**
     * Method to be used when game resumed
     */
    @Override
    public void resume()
    {
        super.resume();
    }
}
