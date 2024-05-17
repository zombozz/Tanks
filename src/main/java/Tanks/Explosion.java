package Tanks;

import static processing.core.PApplet.round;

import processing.core.PApplet;

/**
 * Represents an explosion in the game.
 */
public class Explosion {
    private PApplet parent;
    private float x;
    private float y;
    private int CELLSIZE;
    public SoundEffects soundEffects;
    private int explosionRadius = 60;

    private int explosionInitialTime;

    private boolean exploded =false;
    /**
     * Constructs an explosion.
     * 
     * @param parent The PApplet parent.
     * @param x The x-coordinate of the explosion.
     * @param y The y-coordinate of the explosion.
     * @param explosionInitialTime The initial time of the explosion.
     * @param soundEffects The sound effects associated with the explosion.
     */
    public Explosion(PApplet parent, float x, float y, int explosionInitialTime, SoundEffects soundEffects) {
        this.parent=parent;
        this.x = x;
        this.y = y;
        this.explosionInitialTime = explosionInitialTime;
        this.soundEffects = soundEffects;
    }

    /**
     * Triggers the explosion animation and sound.
     */
    public void Explode(){
        if(!exploded){
        float explosionDuration = 200.0f; // Duration of the explosion animation in milliseconds
        float currentTime = (float)(parent.millis() - explosionInitialTime);
        
        // Calculate scaling factors for each circle based on elapsed time
        float redScale = PApplet.map(currentTime, 0.0f, explosionDuration, 0.0f, 1.0f);
        float orangeScale = PApplet.map(currentTime, 0.0f, explosionDuration, 0.0f, 1.0f);
        float yellowScale = PApplet.map(currentTime, 0.0f, explosionDuration, 0.0f, 1.0f);
        
        float redExplosionRadius = explosionRadius * redScale;
        float orangelosionRadius = explosionRadius * 0.5f * yellowScale;
        float yellowExplosionRadius = explosionRadius * 0.2f * orangeScale;
        // Draw circles with scaled radii
        if (redExplosionRadius<explosionRadius) {
            parent.fill(255, 0, 0); // Red circle
            parent.ellipse(x, y, redExplosionRadius, redExplosionRadius);
            parent.fill(255, 165, 0); // Orange circle
            parent.ellipse(x, y, orangelosionRadius, orangelosionRadius);
            parent.fill(255, 255, 0); // Yellow circle
            parent.ellipse(x, y, yellowExplosionRadius, yellowExplosionRadius);
        }
        LevelRenderer.updateTerrainAfterExplosion(Math.round(x),Math.round(y));
        soundEffects.playExplosionSound();
        }
        exploded = true;
    }

    /**
     * Gets the x-coordinate of the explosion.
     * 
     * @return The x-coordinate of the explosion.
     */
    public float getX(){
        return x;
    }

    /**
     * Gets the y-coordinate of the explosion.
     * 
     * @return The y-coordinate of the explosion.
     */
    public float getY() {
        return y;
    }

    /**
     * Gets the initial time of the explosion.
     * 
     * @return The initial time of the explosion.
     */
    public int getExplosionInitialTime() {
        return explosionInitialTime;
    }
}
