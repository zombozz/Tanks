package Tanks;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;

public class Tank {
    private PApplet parent;
    private char c;
    private int[] colors;
    private int x;
    private float y;
    private int CELLSIZE;
    private List<Float> smoothedTerrainArray;

    private int moveTankBy = 0;
    private int moveTurretBy = 0;
    private boolean turretShot = false;

    private Projectile projectile;
    private float rotationAngle;

    private float tankX;
    private float tankY;
    private float y1;

    public Tank(PApplet parent, char c, int[] colors, int x, float y, int size, List<Float> smoothedTerrainArray) {
        this.parent = parent;
        this.c = c;
        this.colors = colors;
        this.x = x;
        this.y = y;
        this.CELLSIZE = size;
        this.smoothedTerrainArray = smoothedTerrainArray;
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
        float projectileSpeed = 5; // Set the speed of the projectile
        projectile = new Projectile(parent, tankX, tankY - 10, projectileSpeed, moveTurretBy);
        System.out.println("'BOOM!'");
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
            parent.ellipse(0, -6, CELLSIZE/2, CELLSIZE*2);
            parent.popMatrix();
            if (projectile != null) {
                projectile.update(); // Update the projectile's position
                projectile.display(); // Display the projectile
    
                // Check if the projectile has gone off screen
                if (projectile.projectileLanded()) {
                    projectile = null; // Delete the projectile
                }
            }
        }
    }


    // public String getInfo() {
    //     String bruh = Integer.toString(c) + "  "+ Integer.toString(x) +  "  "+ Float.toString(y);
    //     return bruh;
    // }
}

