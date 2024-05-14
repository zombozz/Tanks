package Tanks;

import processing.core.PApplet;
import static processing.core.PApplet.cos;
import static processing.core.PApplet.sin;

import java.util.List;

import com.jogamp.opengl.util.packrect.Level;

public class Projectile {
    private PApplet parent;
    private float x;
    private float y;
    private float power;
    private int CELLSIZE;
    private float rotationAngle;
    private int ScreenY= App.HEIGHT;
    private int ScreenX = App.WIDTH;
    private List<Float> smoothedTerrainArray;
    public float initialTime;
    private float y1;
    public boolean isNull = false;
    private boolean didItExplode = false;
    public int explosionInitialTime;
    private float gravity = 0.0f;

    private float explosionX = 0;
    private float explosionY = 0;

    public Projectile(PApplet parent, float x, float y, float power, int CELLSIZE, float rotationAngle, List<Float> smoothedTerrainArray) {
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.power = power;
        this.rotationAngle = rotationAngle - 90;
        this.CELLSIZE = CELLSIZE;
        this.smoothedTerrainArray = smoothedTerrainArray;
        isNull = false;
        initialTime = parent.millis();
    }
    public float[] getExplosionCoords(){
        return new float[]{explosionX, explosionY};
    }
    public void setExplosionCoords(float x, float y){
        this.explosionX = x;
        this.explosionY = y;
    }
    public void doExplosion() {
        if(!didItExplode){
            explosionInitialTime = parent.millis();
            didItExplode = true;
        }
        if(parent.millis()-explosionInitialTime<201){
            Explosion explosion = new Explosion(parent, x, y, explosionInitialTime);
            
            try {
                explosion.Explode();
                setExplosionCoords(x, y);
            } catch (NullPointerException e)  {

            }
        }else{
            
            isNull = true;
        }
    }
    public void update() {   
        
        float angleInRadians = PApplet.radians(rotationAngle);
        float vx = power /5* cos(angleInRadians); // Calculate the x-component of velocity
        float vy = power /5* sin(angleInRadians); // Calculate the y-component of velocity
        if  (smoothedTerrainArray.size() > x) {
            try{
                y1 = smoothedTerrainArray.get(Math.round(x));
                if(((y/30)-1 > y1) || (y < 0) || (x<0) || (x>ScreenX) || (y>ScreenY)) {
                    doExplosion();
                } else {
                    gravity+=0.072;
                    //do trajectory
                    y += gravity*power/10;
                    x += vx; // Update x position
                    y += vy; // Update y position     
                } 
            } catch (IndexOutOfBoundsException e) {}
        }
    }
    public void display() {
        if(parent!= null) {
            if(!didItExplode) {
                parent.fill(0, 0, 0);
                parent.ellipse(x, y, CELLSIZE/8, CELLSIZE/8);
            }
        }
    }
}
