package Tanks;
import ddf.minim.*;
import processing.core.PApplet;

public class SoundEffects {
    private Minim minim;
    private AudioPlayer explosionSound;
    private AudioPlayer popSound;
    private AudioPlayer tankMoveSound;
    private AudioPlayer turretSound;

    private PApplet parent;
    private int initialTime;

    public SoundEffects(PApplet parent, Minim minim) {
        this.parent=parent;
        this.minim = minim;
        explosionSound = minim.loadFile("src\\main\\resources\\Tanks\\vine-boom.mp3");
        popSound = minim.loadFile("src\\main\\resources\\Tanks\\pop.mp3");
        tankMoveSound = minim.loadFile("src\\main\\resources\\Tanks\\tank.mp3");
        turretSound = minim.loadFile("src\\main\\resources\\Tanks\\turret.mp3");
        initialTime = parent.millis();
    }
    public void playExplosionSound() {
        // explosionSound.rewind();
        // explosionSound.play();
    }
    public void playPopSound() {
        popSound.rewind();
        popSound.play();
    }
    public void playTankMoveSound() {
        // tankMoveSound.rewind();
        // tankMoveSound.play();
    }
    public void playTurretSound() {
        // turretSound.rewind();
        // turretSound.play();
    }
    public void stopTankMoveSound() {
        // tankMoveSound.pause();
        // tankMoveSound.rewind();
    }
}