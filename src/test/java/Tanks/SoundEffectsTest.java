package Tanks;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;
import ddf.minim.*;

public class SoundEffectsTest {

    private Minim minim;
    private AudioPlayer explosionSound;
    private AudioPlayer popSound;
    private AudioPlayer tankMoveSound;
    private AudioPlayer turretSound;
    private AudioPlayer nextLevelSound;
    private AudioPlayer gameOverSound;

    private PApplet mockApplet = new PApplet();
    private SoundEffects soundEffects;

    @BeforeEach
    public void setUp() {
        minim = new Minim(mockApplet);
        explosionSound = minim.loadFile("src\\main\\resources\\Tanks\\vine-boom.mp3");
        popSound = minim.loadFile("src\\main\\resources\\Tanks\\pop.mp3");
        tankMoveSound = minim.loadFile("src\\main\\resources\\Tanks\\tank.mp3");
        turretSound = minim.loadFile("src\\main\\resources\\Tanks\\turret.mp3");
        nextLevelSound = minim.loadFile("src\\main\\resources\\Tanks\\whoosh.mp3");
        gameOverSound = minim.loadFile("src\\main\\resources\\Tanks\\game-over3.mp3");
        
        soundEffects = new SoundEffects(mockApplet, minim);
    }

    @Test
    public void testPlayExplosionSound() {
        soundEffects.playExplosionSound();
        assertTrue(explosionSound.isPlaying());
    }

    @Test
    public void testPlayPopSound() {
        soundEffects.playPopSound();
        assertTrue(popSound.isPlaying());
    }

    @Test
    public void testPlayTankMoveSound() {
        soundEffects.playTankMoveSound();
        assertTrue(tankMoveSound.isPlaying());
    }

    @Test
    public void testPlayTurretSound() {
        soundEffects.playTurretSound();
        assertTrue(turretSound.isPlaying());
    }

    @Test
    public void testStopTankMoveSound() {
        soundEffects.playTankMoveSound();
        soundEffects.stopTankMoveSound();
        assertFalse(tankMoveSound.isPlaying());
    }

    @Test
    public void testPlayNextLevelSound() {
        soundEffects.playNextLevelSound();
        assertTrue(nextLevelSound.isPlaying());
    }

    @Test
    public void testPlayGameOverSound() {
        soundEffects.playGameOverSound();
        assertTrue(gameOverSound.isPlaying());
    }
}
