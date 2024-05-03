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
    private List<Float> smoothedTerrainArray = new ArrayList<>();
    private int moveTankBy = 0;
    private int moveTurretBy = 0;

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
    
    public void render(List<Float> smoothedTerrainArray) {
        int xSize = (x*32) + moveTankBy;
        if  (smoothedTerrainArray.size() > xSize) {
            Float y1 = smoothedTerrainArray.get(xSize);
            int index = c - 'A'; 
            float tankX = x * CELLSIZE + moveTankBy;
            float tankY = y1 * CELLSIZE;
            parent.fill(colors[index]);
            parent.ellipse(tankX, tankY, CELLSIZE, CELLSIZE/2);
            parent.ellipse(tankX, tankY-10, CELLSIZE/2, CELLSIZE/4);
            parent.fill(0);

            parent.pushMatrix();
            float rotationAngle = PApplet.radians(moveTankBy % 60);
            parent.rotate(rotationAngle);
            parent.ellipse(tankX, tankY-16, CELLSIZE/8, CELLSIZE/2);
            parent.popMatrix();
        }
    }


    // public String getInfo() {
    //     String bruh = Integer.toString(c) + "  "+ Integer.toString(x) +  "  "+ Float.toString(y);
    //     return bruh;
    // }
}

