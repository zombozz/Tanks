package Tanks;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;

public class Tank {
    private PApplet parent;
    public char c;
    private int[] colors;
    private int x;
    private float y;
    private int CELLSIZE;
    private List<Float> smoothedTerrainArray;

    private int moveTankBy = 0;
    private int moveTurretBy = 0;

    private Projectile projectile;
    private float rotationAngle;

    private float tankX;
    private float tankY;
    private float y1;

    private int playerHealth = 100;
    private int playerScore = 0;

    private List<Projectile> projectiles;

    public Tank(PApplet parent, char c, int[] colors, int x, float y, int size, List<Float> smoothedTerrainArray) {
        projectiles = new ArrayList<>();
        this.parent = parent;
        this.c = c;
        this.colors = colors;
        this.x = x;
        this.y = y;
        this.CELLSIZE = size;
        this.smoothedTerrainArray = smoothedTerrainArray;
    }

    public char getC(){
        return c;
    }
    public float getTankMovement(){
        return moveTankBy;
    }
    public float getY(){
        return tankY;
    }

    public void moveTank(int moveTank) {
        this.moveTankBy+=moveTank;
        render(smoothedTerrainArray);
    }

    public void moveTurret(int moveTurret) {
        this.moveTurretBy+=moveTurret;
        render(smoothedTerrainArray);
    }

    public void shootTurret() {
        float projectileSpeed = 50; // Set the speed of the projectile
        projectile = new Projectile(parent, tankX, tankY - 10, projectileSpeed, CELLSIZE, moveTurretBy, smoothedTerrainArray);
        projectiles.add(projectile);
    }
    
    public void render(List<Float> smoothedTerrainArray) {
        this.smoothedTerrainArray = smoothedTerrainArray;
        int xSize = (x*32) + moveTankBy;
        if  (smoothedTerrainArray.size() > xSize) {
            y1 = smoothedTerrainArray.get(xSize);
            // tank movement
            int index = c - 'A'; 
            tankX = x * CELLSIZE + moveTankBy;
            tankY = y1 * CELLSIZE;
            // tank shape
            parent.fill(colors[index]);
            parent.ellipse(tankX, tankY, CELLSIZE, CELLSIZE/2);
            parent.ellipse(tankX, tankY-10, CELLSIZE/2, CELLSIZE/4);
            parent.fill(0);
            // turret movement
            parent.pushMatrix();
            parent.translate(tankX, tankY-10);
            rotationAngle = PApplet.radians(moveTurretBy);
            parent.rotate(rotationAngle);
            parent.ellipse(0, -6, CELLSIZE/8, CELLSIZE/2);
            parent.popMatrix();
            try {
                if (!projectile.isNull) {
                    projectile.update(); 
                    projectile.display();
                }
            } catch (NullPointerException e)  {

            }
        }
    }


    // public String getInfo() {
    //     String bruh = Integer.toString(c) + "  "+ Integer.toString(x) +  "  "+ Float.toString(y);
    //     return bruh;
    // }
}

