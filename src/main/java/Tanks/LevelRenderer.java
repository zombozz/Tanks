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
    private static List<Float> smoothedTerrainArray = new ArrayList<>();
    private static List<Tank> tanks = new ArrayList<>();
    private static float yCoord;
    private static int xSize;
    private static int moveBy;
    private static int bruh;
    private static Set<Character> tankIds = new HashSet<>();
    
    public static void renderLevel(PApplet parent, String[] levelLines, int[] playerColors, int terrainColor, PImage treesImage, int CELLSIZE) {
        terrainSmooth = new TerrainSmooth(parent, terrainColor, CELLSIZE, heights);
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
    }
    private boolean terrainSmoothed = false;
    private int no = 0;
    public void smoothTerrain() {
        smoothedTerrainArray = terrainSmooth.smooth();
        terrainSmoothed = true;
    }



private static void drawTerrain(PApplet parent, int terrainColor, int x, int y, int CELLSIZE) {
    int x1=0;
    for (Float y1: smoothedTerrainArray) {
        parent.fill(terrainColor);
        parent.rect(x1, y1*CELLSIZE, 1, (20-y1)*CELLSIZE);
        // parent.rect(x1, y1*CELLSIZE, 1, 5);
        // parent.fill(0,0,0,20);
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
        bruh+=1;
        System.out.println(bruh);
        System.out.println(tanks);
        System.out.println(tankIds);
        Tank tank = new Tank(parent, c, playerColors, x, y, CELLSIZE, smoothedTerrainArray);
        tanks.add(tank);
        tankIds.add(c);
    } 
    for (Tank tank : tanks) {
        // System.out.println(tank.getInfo());
        tank.render(smoothedTerrainArray);
    }
    // xSize = (x*32);
    // if ((c >= 'A' && c <= 'E') && ((smoothedTerrainArray.size() > xSize))) {
    //     Float y1 = smoothedTerrainArray.get(xSize);
    //     int index = c - 'A'; 
    //     float tankX = x * CELLSIZE;
    //     float tankY = y1 * CELLSIZE;
    //     Tank tank = new Tank(parent, playerColors[index], tankX, tankY, CELLSIZE);
    //     // parent.rect(tankX, tankY, CELLSIZE, CELLSIZE);
    //     // parent.fill(0,0,0,20);
    //     tanks.add(tank);
    //     tank.render();
    //     // tank.moveTank(moveBy); // Render tanks
    // }
}
public static List<Tank> getTanks() {
    return tanks;
}
// public static void moveTanks(int tankIndex, int moveByAmount) {
//     moveBy = moveByAmount;
//     if (tankIndex >= 0 && tankIndex < tanks.size()) {
//         Tank tank = tanks.get(tankIndex);
//         tank.moveTank(moveByAmount);
//     }
// }
}