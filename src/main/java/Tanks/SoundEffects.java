package Tanks;
import ddf.minim.*;
import processing.core.PApplet;
/**
 * Allows the use of soundeffects in the game.
 *
 */
public class SoundEffects {
    private Minim minim;
    private AudioPlayer explosionSound;
    private AudioPlayer popSound;
    private AudioPlayer tankMoveSound;
    private AudioPlayer turretSound;
    private AudioPlayer nextLevelSound;
    private AudioPlayer gameOverSound;

    private PApplet parent;
    private int initialTime;

    /**
     * Constructs a SoundEffects object.
     * @param parent The parent PApplet.
     * @param minim The Minim object for audio loading.
     */
    public SoundEffects(PApplet parent, Minim minim) {
        this.parent=parent;
        this.minim = minim;
        explosionSound = minim.loadFile("src\\main\\resources\\Tanks\\vine-boom.mp3");
        popSound = minim.loadFile("src\\main\\resources\\Tanks\\pop.mp3");
        tankMoveSound = minim.loadFile("src\\main\\resources\\Tanks\\tank.mp3");
        turretSound = minim.loadFile("src\\main\\resources\\Tanks\\turret.mp3");
        nextLevelSound = minim.loadFile("src\\main\\resources\\Tanks\\whoosh.mp3");
        gameOverSound = minim.loadFile("src\\main\\resources\\Tanks\\game-over3.mp3");
        initialTime = parent.millis();
    }

    /**
     * Plays the explosion sound effect.
     */
    public void playExplosionSound() {
        explosionSound.rewind();
        explosionSound.play();
    }

    /**
     * Plays the pop sound effect.
     */
    public void playPopSound() {
        popSound.rewind();
        popSound.play();
    }

    /**
     * Plays the tank movement sound effect.
     */
    public void playTankMoveSound() {
        tankMoveSound.rewind();
        tankMoveSound.play();
    }

    /**
     * Plays the turret movement sound effect.
     */
    public void playTurretSound() {
        turretSound.rewind();
        turretSound.play();
    }

    /**
     * Stops the tank movement sound effect.
     */
    public void stopTankMoveSound() {
        tankMoveSound.pause();
        tankMoveSound.rewind();
    }

    /**
     * Plays the next level sound effect.
     */
    public void playNextLevelSound() {
        nextLevelSound.rewind();
        nextLevelSound.play();
    }

    /**
     * Plays the game over sound effect.
     */
    public void playGameOverSound() {
        gameOverSound.rewind();
        gameOverSound.play();
    }

}