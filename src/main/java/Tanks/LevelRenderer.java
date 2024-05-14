package Tanks;

import org.checkerframework.checker.units.qual.A;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.List;
// import processing.data.JSONArray;
// import processing.data.JSONObject;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

public class LevelRenderer {
    private static Map<Integer, Integer> heights = new HashMap<>();
    private static TerrainSmooth terrainSmooth;
    public static List<Float> smoothedTerrainArray = new ArrayList<>();
    private static List<Tank> tanks = new ArrayList<>();
    private static float yCoord;
    private static int xSize;
    private static int moveBy;
    public static int tanksNum;
    private static Set<Character> tankIds = new HashSet<>();

    private static int ScreenY= App.HEIGHT;
    private static int ScreenX = App.WIDTH;

    private static List<Integer> treeXArray = new ArrayList<>();
    private static List<Integer> treeYArray = new ArrayList<>();
    private static List<Integer> tankXArray = new ArrayList<>();
    private static List<Integer> tankYArray = new ArrayList<>();

    private static GUI GUI;

    private static boolean finishedRendering = false;
    
    public static void renderLevel(PApplet parent, String[] levelLines, int[] playerColors, int terrainColor, PImage treesImage, int CELLSIZE, GUI GUI) {
        terrainSmooth = new TerrainSmooth(parent, terrainColor, CELLSIZE, heights);
        if(!finishedRendering){
        for (int y = 0; y < levelLines.length; y++) {
            String line = levelLines[y];
            for (int x = 0; x < line.length(); x++) {
                char c = line.charAt(x);
                switch(c) {
                    case 'X':
                        heights.put(x, y);
                        drawTerrain(parent, terrainColor, x, y, CELLSIZE);
                        // parent.fill(terrainColor);
                        // parent.rect(x * CELLSIZE, y * CELLSIZE, CELLSIZE, (20-y)*CELLSIZE);
                        break;
                    case 'T':
                        treeXArray.add(x);
                        treeYArray.add(y);
                        drawTrees(parent, x, y, CELLSIZE, treesImage);
                        break;
                    case ' ':
                        break;
                    default:
                        drawTanks(parent, c, x, y, playerColors, CELLSIZE, smoothedTerrainArray);
                        break;
                }
            }
        }
        // smoothTerrain();
        smoothedTerrainArray = terrainSmooth.smooth();
        finishedRendering = true;
        } else {
            for (Map.Entry<Integer, Integer> entry : heights.entrySet()) {
                int x = entry.getKey();
                int y = entry.getValue();
                drawTerrain(parent, terrainColor, x, y, CELLSIZE);
            }
            for (int i = 0; i < treeXArray.size(); i++) {
                drawTrees(parent, treeXArray.get(i), treeYArray.get(i), CELLSIZE, treesImage);
            }
        }
        renderAllTanks(GUI);
    }
    // private boolean terrainSmoothed = false;
    // public static void smoothTerrain() {
    //     smoothedTerrainArray = terrainSmooth.smooth();
    //     // terrainSmoothed = true;
    // }

    public static void updateTerrainAfterExplosion(int explosionX, int explosionY) {
        // Update heights map based on the explosion coordinates
        int xRange = 0;
        boolean middleReached = false;
        for (int x = explosionX - 30; x <= explosionX + 30; x++) {
            // for (int y = explosionY - 30; y <= explosionY + 30; y++) {
                // Check if the coordinate is within the bounds of the level
                if(xRange==30 && !middleReached){
                    middleReached = true;
                } else if (middleReached){
                    xRange-=1;
                } else {
                    xRange+=1;
                }
                if (x >= 0 && x < ScreenX) {
                    // Update the heights map to reflect destruction caused by the explosion
                    float currentVal = smoothedTerrainArray.get(x);
                    currentVal +=0.3*xRange/30;
                    smoothedTerrainArray.set(x, currentVal);
                }
            // }
        }
    }

private static void drawTerrain(PApplet parent, int terrainColor, int x, int y, int CELLSIZE) {
    int x1=0;
    for (Float y1: smoothedTerrainArray) {
        parent.fill(terrainColor);
        parent.rect(x1, y1*CELLSIZE, 1, (20-y1)*CELLSIZE);
        x1+=1;
    }
}

private static void drawTrees(PApplet parent, int x, int y, int CELLSIZE, PImage treesImage) {
    xSize = (x*32)+16;
    if (smoothedTerrainArray.size() > xSize) {
        Float y1 = smoothedTerrainArray.get(xSize);
        yCoord = y1 * CELLSIZE + 2;
        parent.image(treesImage, x * CELLSIZE, yCoord-CELLSIZE, CELLSIZE, CELLSIZE);
    } 
}

private static void drawTanks(PApplet parent, char c, int x, int y, int[] playerColors, int CELLSIZE, List<Float> smoothedTerrainArray) {
    if (tanks.size() < 5 && !tankIds.contains(c) && c >= 'A' && c <= 'E') {
        tanksNum+=1;
        // System.out.println(tanks);
        // System.out.println(tankIds);
        Tank tank = new Tank(parent, c, playerColors, x, y, CELLSIZE, smoothedTerrainArray);
        tanks.add(tank);
        tankIds.add(c);
    } 
}
public static void renderAllTanks(GUI GUI) {
    int i=0;
    for (Tank tank : tanks) {
        // System.out.println(tank.getInfo());
        i+=1;
        tank.setGUI(GUI);
        tank.render(smoothedTerrainArray);
        tank.renderGUI(i);
    }
    i=0;
}
public static List<Tank> getTanks() {
    Collections.sort(tanks, Comparator.comparingInt(tank -> tank.getC() - 'A'));
    return tanks;
}
}