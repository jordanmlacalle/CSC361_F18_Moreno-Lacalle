package com.jordanml.game.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.touchable;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.jordanml.game.assets.Assets;
import com.jordanml.game.util.AudioManager;
import com.jordanml.game.util.Constants;
import com.jordanml.game.util.GamePreferences;

public class MenuScreen extends AbstractScreen
{
    public static final String TAG = MenuScreen.class.getName();

    // Debug
    private final float DEBUG_REBUILD_INTERVAL = 5.0f;
    private boolean debugEnabled = false;
    private float debugRebuildStage;

    private Stage stage;
    private Skin skinDamned;
    private Skin skinLibgdx;

    // Menu - Primary
    private Image imgLogo;
    private Image imgBackground;
    private Image imgTree;
    private Image imgSign;
    private Image imgTombstone;
    private Image imgJack;
    private Button btnPlay;
    private Button btnOptions;

    // Options
    private Window winOptions;
    private TextButton btnWinOptSave;
    private TextButton btnWinOptCancel;
    private CheckBox chkSound;
    private Slider sldSound;
    private CheckBox chkMusic;
    private Slider sldMusic;
    private Image imgCharSkin;
    private CheckBox chkShowFpsCounter;

    public MenuScreen(Game game)
    {
        super(game);
    }

    /**
     * Build the menu background layer
     * 
     * @return returns the Table representing the background layer
     */
    private Table buildBackgroundLayer()
    {
        Table layer = new Table();

        imgBackground = new Image(skinDamned, "background");
        layer.add(imgBackground);

        return layer;
    }

    /**
     * Builds the object layer. This layer contains menu decorations such as the
     * game logo.
     * 
     * @return returns the Table representing the object layer
     */
    private Table buildObjectsLayer()
    {
        Table layer = new Table();

        imgLogo = new Image(skinDamned, "logo");
        layer.addActor(imgLogo);
        imgLogo.setPosition(200, 100);

        imgTree = new Image(skinDamned, "tree");
        layer.addActor(imgTree);
        imgTree.setPosition(500, 0);

        imgTombstone = new Image(skinDamned, "tombstone");
        layer.addActor(imgTombstone);
        imgTombstone.setPosition(100, 0);

        imgJack = new Image(skinDamned, "jack");
        layer.addActor(imgJack);
        imgJack.setPosition(450, -10);

        return layer;
    }

    /**
     * Builds the controls layer. This layer contains any objects in the menu that
     * the player can interact with.
     * 
     * @return returns the Table representing the controls layer
     */
    private Table buildControlsLayer()
    {
        Table layer = new Table();

        btnPlay = new Button(skinDamned, "play");
        btnOptions = new Button(skinDamned, "options");
        layer.bottom();
        layer.add(btnPlay);
        btnPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                onPlayClicked();
            }
        });
        layer.add(btnOptions);
        btnOptions.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                onOptionsClicked();
            }
        });

        return layer;
    }

    /**
     * Defines the action to take when the "Play" button is clicked
     */
    private void onPlayClicked()
    {
        AudioManager.instance.stopMusic();
        game.setScreen(new GameScreen(game));
    }

    /**
     * Actions to be performed when options button clicked on game menu.
     * Opens the Options window.
     */
    private void onOptionsClicked()
    {
        
        loadSettings();
        btnPlay.setVisible(false);
        btnOptions.setVisible(false);
        showMenuButtons(false); 
        showOptionsWindow(true, true);
        winOptions.setVisible(true);
    }

    private Table buildOptionsWindowLayer()
    {
        winOptions = new Window("Options", skinLibgdx);
        // Add Audio Settings: Sound/Music Checkbox and Volume Slider
        winOptions.add(buildOptWinAudioSettings()).row();
        // Add Debug: Show FPS Counter
        winOptions.add(buildOptWinDebug()).row();
        // Add separator and buttons (Save, Cancel)
        winOptions.add(buildOptWinButtons()).pad(10, 0, 10, 0);

        // Make options window slightly transparent
        winOptions.setColor(1, 1, 1, 0.8f);
        // Hide options window by default
        showOptionsWindow(false, false);

        if (debugEnabled)
        {
            winOptions.debug();
        }

        // Let TableLayout recalculate widget sizes and positions
        winOptions.pack();
        // Move options window to bottom right corner
        winOptions.setPosition(Constants.VIEWPORT_GUI_WIDTH - winOptions.getWidth() - 50, 50);
        Table layer = new Table();

        return winOptions;
    }

    /**
     * Builds audio options
     * 
     * @return Built audio options table
     */
    private Table buildOptWinAudioSettings()
    {
        Table tbl = new Table();
        // Add Title: "Audio"
        tbl.pad(10, 10, 0, 10);
        tbl.add(new Label("Audio", skinLibgdx, "default-font", Color.ORANGE)).colspan(3);
        tbl.row();
        tbl.columnDefaults(0).padRight(10);
        tbl.columnDefaults(1).padRight(10);
        // Add Checkbox, "Sound" label, sound volume slider
        chkSound = new CheckBox("", skinLibgdx);
        tbl.add(chkSound);
        tbl.add(new Label("Sound", skinLibgdx));
        sldSound = new Slider(0.0f, 1.0f, 0.1f, false, skinLibgdx);
        tbl.add(sldSound);
        tbl.row();
        // Add Checkbox, "Music" label, music volume slider
        chkMusic = new CheckBox("", skinLibgdx);
        tbl.add(chkMusic);
        tbl.add(new Label("Music", skinLibgdx));
        sldMusic = new Slider(0.0f, 1.0f, 0.1f, false, skinLibgdx);
        tbl.add(sldMusic);
        tbl.row();
        return tbl;
    }

    /**
     * Builds table containing debug settings
     * 
     * @return The built debug settings table
     */
    private Table buildOptWinDebug()
    {
        Table tbl = new Table();
        // Add title: "Debug"
        tbl.pad(10, 10, 0, 10);
        tbl.add(new Label("Debug", skinLibgdx, "default-font", Color.RED)).colspan(3);
        tbl.row();
        tbl.columnDefaults(0).padRight(10);
        tbl.columnDefaults(1).padRight(10);
        // Add checkbox, "Show FPS Counter" label
        chkShowFpsCounter = new CheckBox("", skinLibgdx);
        tbl.add(new Label("Show FPS Counter", skinLibgdx));
        tbl.add(chkShowFpsCounter);
        tbl.row();
        return tbl;
    }

    /**
     * Builds table that contains Save and Cancel buttons at the bottom of the
     * Options window.
     * 
     * @return Built options window buttons table
     */
    private Table buildOptWinButtons()
    {
        Table tbl = new Table();
        // Add separator
        Label lbl = null;
        lbl = new Label("", skinLibgdx);
        lbl.setColor(0.75f, 0.75f, 0.75f, 1);
        lbl.setStyle(new LabelStyle(lbl.getStyle()));
        lbl.getStyle().background = skinLibgdx.newDrawable("white");
        tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 1, 5, 0);
        tbl.row();
        // Add save button with event handler
        btnWinOptSave = new TextButton("Save", skinLibgdx);
        tbl.add(btnWinOptSave).padRight(30);
        btnWinOptSave.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                onSaveClicked();
            }

        });
        // Add cancel button with event handler
        btnWinOptCancel = new TextButton("Cancel", skinLibgdx);
        tbl.add(btnWinOptCancel);
        btnWinOptCancel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                onCancelClicked();
            }
        });
        return tbl;
    }

    /**
     * Loads saved settings.
     */
    private void loadSettings()
    {
        GamePreferences prefs = GamePreferences.instance;
        prefs.load();
        chkSound.setChecked(prefs.sound);
        sldSound.setValue(prefs.volSound);
        chkMusic.setChecked(prefs.music);
        sldMusic.setValue(prefs.volMusic);
        chkShowFpsCounter.setChecked(prefs.showFpsCounter);

    }

    /**
     * Saves settings.
     */
    private void saveSettings()
    {
        GamePreferences prefs = GamePreferences.instance;
        prefs.sound = chkSound.isChecked();
        prefs.volSound = sldSound.getValue();
        prefs.music = chkMusic.isChecked();
        prefs.volMusic = sldMusic.getValue();
        prefs.showFpsCounter = chkShowFpsCounter.isChecked();
        prefs.save();
    }

    /**
     * Saves the current settings of the Options window and swaps the Options window
     * for the menu controls
     */
    private void onSaveClicked()
    {
        saveSettings();
        onCancelClicked();
        AudioManager.instance.onSettingsUpdated();
    }

    /**
     * Swaps the Options and Menu widgets, discarded any changes made to settings.
     */
    private void onCancelClicked()
    {
        showMenuButtons(true);
        showOptionsWindow(false, true);
        btnPlay.setVisible(true);
        btnOptions.setVisible(true);
        winOptions.setVisible(false);
        AudioManager.instance.onSettingsUpdated();
    }

    private void showMenuButtons(boolean visible)
    {
        float moveDuration = 1.0f;
        Interpolation moveEasing = Interpolation.swing;
        float delayOptionsButton = 0.25f;
        float moveX = 300 * (visible ? -1 : 1);
        float moveY = 0 * (visible ? -1 : 1);
        final Touchable touchEnabled = visible ? Touchable.enabled : Touchable.disabled;
       // btnPlay.addAction(moveBy(moveX, moveY, moveDuration, moveEasing));
       // btnOptions.addAction(sequence(delay(delayOptionsButton), moveBy(moveX, moveY, moveDuration, moveEasing)));
        SequenceAction seq = sequence();
        if (visible)
            seq.addAction(delay(delayOptionsButton + moveDuration));
        seq.addAction(run(new Runnable() {
            public void run()
            {

                btnPlay.setTouchable(touchEnabled);
                btnOptions.setTouchable(touchEnabled);
            }
        }));
        stage.addAction(seq);
    }

    private void showOptionsWindow(boolean visible, boolean animated)
    {
        float alphaTo = visible ? 0.8f : 0.0f;
        float duration = animated ? 1.0f : 0.0f;
        Touchable touchEnabled = visible ? Touchable.enabled : Touchable.disabled;
        winOptions.addAction(sequence(touchable(touchEnabled), alpha(alphaTo, duration)));
    }

    /**
     * Rebuilds the menu Stage
     */
    private void rebuildStage()
    {
        skinDamned = new Skin(Gdx.files.internal(Constants.SKIN_DAMNED_UI),
                new TextureAtlas(Constants.TEXTURE_ATLAS_UI));
        skinLibgdx = new Skin(Gdx.files.internal(Constants.SKIN_LIBGDX_UI),
                new TextureAtlas(Constants.TEXTURE_ATLAS_LIBGDX_UI));

        // Build all layers
        Table layerBackground = buildBackgroundLayer();
        Table layerObjects = buildObjectsLayer();
        Table layerControls = buildControlsLayer();
        Table layerOptionsWindow = buildOptionsWindowLayer();
        
        // Assemble stage for menu screen
        stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        stack.add(layerBackground);
        stack.add(layerObjects);
        stack.add(layerControls);
        stack.add(layerOptionsWindow);
    }

    /**
     * Renders the menu
     */
    @Override
    public void render(float deltaTime)
    {
        // OpenGL clear color -> black
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (debugEnabled)
        {
            debugRebuildStage -= deltaTime;

            if (debugRebuildStage <= 0)
            {
                debugRebuildStage = DEBUG_REBUILD_INTERVAL;
                rebuildStage();
            }
        }

        stage.act(deltaTime);
        stage.draw();
        stage.setDebugAll(false);

    }

    /**
     * Defines how to resize the menu
     * 
     * @param width  the new width of the screen
     * @param height the new height of the screen
     */
    @Override
    public void resize(int width, int height)
    {
        stage.getViewport().update(width, height, true);
    }

    /**
     * Displays the menu
     */
    @Override
    public void show()
    {
        stage = new Stage(new StretchViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT));
        Gdx.input.setInputProcessor(stage);
        rebuildStage();
        AudioManager.instance.play(Assets.instance.music.menu);
    }

    /**
     * Hides the menu
     */
    @Override
    public void hide()
    {
        stage.dispose();
        skinDamned.dispose();
        skinLibgdx.dispose();

    }

    @Override
    public void pause()
    {

    }

}
