package com.jordanml.game.util;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager
{
    public static final AudioManager instance = new AudioManager();
    
    // The music that is currently playing (may be stopped or paused)
    private Music playingMusic;
    
    // Singleton: prevent instantiation from other classes
    private AudioManager() {}
    
    /**
     * Plays the given sound with default volume, pitch, and pan
     * 
     * @param sound The sound to be played
     */
    public void play(Sound sound)
    {
        play(sound, 1);
    }
    
    /**
     * Plays the given sound at the given volume. Uses the default pitch and pan.
     * 
     * @param sound The sound to be played
     * @param volume The volume of the sound
     */
    public void play(Sound sound, float volume)
    {
        play(sound, volume, 1);
    }
    
    /**
     * Plays the given sound with the given volume and pitch. Uses the default pan.
     * 
     * @param sound The sound to be played
     * @param volume The volume of the sound
     * @param pitch The pitch of the sound
     */
    public void play(Sound sound, float volume, float pitch)
    {
        play(sound, volume, pitch, 0);
    }
    
    /**
     * Plays the given sound with the given volume, pitch, and pan
     * 
     * @param sound The sound to be played
     * @param volume The volume of the sound
     * @param pitch The pitch of the sound
     * @param pan The pan of the sound
     */
    public void play(Sound sound, float volume, float pitch, float pan)
    {
        sound.play(1.0f * volume, pitch, pan);
    }
    
    /**
     * Plays the given music. If music is already playing, it is stopped before the new music plays.
     * 
     * @param music The music to be played
     */
    public void play(Music music)
    {
        stopMusic();
        playingMusic = music;

        music.setLooping(true);
        music.setVolume(1.0f);
        music.play();
    }
    
    /**
     * Stops the currently playing music
     */
    public void stopMusic()
    {
        // Check if music has been set yet
        if(playingMusic != null)
            playingMusic.stop();
    }
    
    /**
     * Update music according to settings update
     */
    public void onSettingsUpdated()
    {
        // Check if music has been set, return if not
        if(playingMusic == null)
            return;

        // If music is not playing, start playing
        if(!playingMusic.isPlaying())
            playingMusic.play();

    }

}

