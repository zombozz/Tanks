package Tanks;

import static processing.core.PApplet.round;

import processing.core.PApplet;

public class Explosion {
    private PApplet parent;
    private float x;
    private float y;
    private int CELLSIZE;

    private int explosionRadius = 60;

    private int explosionInitialTime;

    public Explosion(PApplet parent, float x, float y, int explosionInitialTime) {
        this.parent=parent;
        this.x = x;
        this.y = y;
        this.explosionInitialTime = explosionInitialTime;
    }
    public void Explode(){
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
    }
}
