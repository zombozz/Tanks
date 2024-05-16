package Tanks;

import static processing.core.PApplet.round;

import processing.core.PApplet;

public class Explosion {
    private PApplet parent;
    private float x;
    private float y;
    private int CELLSIZE;
    public SoundEffects soundEffects;
    private int explosionRadius = 60;

    private int explosionInitialTime;

    private boolean exploded =false;

    public Explosion(PApplet parent, float x, float y, int explosionInitialTime, SoundEffects soundEffects) {
        this.parent=parent;
        this.x = x;
        this.y = y;
        this.explosionInitialTime = explosionInitialTime;
        this.soundEffects = soundEffects;
    }
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
    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }
    public int getExplosionInitialTime(){
        return explosionInitialTime;
    }
}
