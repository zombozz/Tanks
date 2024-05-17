package Tanks;

import processing.core.PApplet;
import static processing.core.PApplet.cos;
import static processing.core.PApplet.sin;

import java.util.List;
import ddf.minim.*;
import com.jogamp.opengl.util.packrect.Level;
/**
 * The class for each projectile object shot by a tank.
 */
public class Projectile {
    public PApplet parent;
    public float x;
    public float y;
    public float power;
    public int CELLSIZE;
    public float rotationAngle;
    public int ScreenY= App.HEIGHT;
    public int ScreenX = App.WIDTH;
    public List<Float> smoothedTerrainArray;
    public float initialTime;
    public float y1;
    public boolean isNull = false;
    public boolean didItExplode = false;
    public int explosionInitialTime;
    public float gravity = 0.0f;
    public int windForce;

    public SoundEffects soundEffects;

    public float explosionX = 0;
    public float explosionY = 0;

    /**
     * Constructs a Projectile object.
     * @param parent The parent PApplet.
     * @param x The initial x-coordinate of the projectile.
     * @param y The initial y-coordinate of the projectile.
     * @param power The initial power of the projectile.
     * @param CELLSIZE The size of each cell in the terrain.
     * @param rotationAngle The initial rotation angle of the projectile.
     * @param smoothedTerrainArray The list containing smoothed terrain heights.
     * @param soundEffects The SoundEffects object for playing sounds.
     * @param windForce The initial wind force affecting the projectile.
     */
    public Projectile(PApplet parent, float x, float y, float power, int CELLSIZE, float rotationAngle, List<Float> smoothedTerrainArray, SoundEffects soundEffects, int windForce) {
        this.soundEffects = soundEffects;
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.power = power;
        this.rotationAngle = rotationAngle - 90;
        this.CELLSIZE = CELLSIZE;
        this.smoothedTerrainArray = smoothedTerrainArray;
        this.windForce = windForce; 

        isNull = false;
        initialTime = parent.millis();
        soundEffects.playPopSound();
    }

    /**
     * Retrieves the coordinates of the explosion.
     * @return An array containing the x-coordinate, y-coordinate, and initial time of the explosion.
     */
    public float[] getExplosionCoords(){
        return new float[]{explosionX, explosionY, explosionInitialTime};
    }

    /**
     * Sets the coordinates of the explosion.
     * @param x The x-coordinate of the explosion.
     * @param y The y-coordinate of the explosion.
     */
    public void setExplosionCoords(float x, float y){
        this.explosionX = x;
        this.explosionY = y;
    }

    /**
     * Initiates the explosion effect.
     */
    public void doExplosion() {
        if(!didItExplode){
            explosionInitialTime = parent.millis();
            didItExplode = true;
            setExplosionCoords(x, y);
        }
        Explosion explosion = new Explosion(parent, x, y, explosionInitialTime, soundEffects);
        if(parent.millis()-explosionInitialTime<201){
            try {
                explosion.Explode();
            } catch (NullPointerException e)  {
            }
        }else{
            isNull = true;
        }
    }

    /**
     * Updates the projectile's position and velocity.
     */
    public void update() {   
        float angleInRadians = PApplet.radians(rotationAngle);
        float vx = power / 5 * cos(angleInRadians); // Calculate the x-component of velocity
        float vy = power / 5 * sin(angleInRadians); // Calculate the y-component of velocity
        if  (smoothedTerrainArray.size() > x) {
            try{
                y1 = smoothedTerrainArray.get(Math.round(x));
                if((((y/30)-1 > y1) || (y < 0) || (x<0) || (x>ScreenX) || (y>ScreenY))) {
                    doExplosion();
                } else {
                    gravity+=3.6;
                    x+=windForce*0.03;
                    y += gravity/5;
                    x += vx; // Update x position
                    y += vy; // Update y position     
                } 
            } catch (IndexOutOfBoundsException e) {}
        }
    }

    /**
     * Displays the projectile.
     */
    public void display() {
        if(parent!= null) {
            if(!didItExplode) {
                parent.fill(0, 0, 0);
                parent.ellipse(x, y, CELLSIZE/8, CELLSIZE/8);
            }
        }
    }
}
