package Tanks;

import org.checkerframework.checker.units.qual.A;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.List;
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
/**
 * The LevelRenderer class handles the rendering of the game level, including terrain, trees, and tanks.
 */
public class LevelRenderer {
    public static Map<Integer, Integer> heights = new HashMap<>();
    public static List<Float> smoothedTerrainArray = new ArrayList<>();
    public static List<Tank> tanks = new ArrayList<>();
    private static float yCoord;
    private static int xSize;
    private static int moveBy;
    public static int tanksNum;
    public static Set<Character> tankIds = new HashSet<>();

    private static int ScreenY= App.HEIGHT;
    private static int ScreenX = App.WIDTH;

    private static List<Integer> treeXArray = new ArrayList<>();
    private static List<Integer> treeYArray = new ArrayList<>();
    private static List<Integer> tankXArray = new ArrayList<>();
    private static List<Integer> tankYArray = new ArrayList<>();

    public static boolean finishedRendering = false;
    public boolean tankScoresTaken = false;

    private PApplet parent;
    public String[] levelLines;
    private int[] playerColors;
    private int terrainColor;
    private PImage treesImage;
    private int CELLSIZE;
    public GUI GUI;

    public static List<Integer> tankScores = new ArrayList<>();
    /**
     * Constructs a LevelRenderer object.
     * @param parent The parent PApplet.
     * @param levelLines An array containing level lines.
     * @param playerColors An array containing player colors.
     * @param terrainColor The terrain color.
     * @param treesImage The image representing trees.
     * @param CELLSIZE The size of a cell.
     * @param GUI The graphical user interface.
     */
    public LevelRenderer(PApplet parent, String[] levelLines, int[] playerColors, int terrainColor, PImage treesImage, int CELLSIZE, GUI GUI) {
        this.parent=parent;
        this.levelLines=levelLines;
        this.playerColors=playerColors;
        this.terrainColor=terrainColor;
        this.treesImage=treesImage;
        this.CELLSIZE=CELLSIZE;
        this.GUI=GUI;
    }

    private TerrainSmooth terrainSmooth = new TerrainSmooth(parent, terrainColor, CELLSIZE, heights);
    /**
     * Resets the level renderer.
     * @param newGame Indicates whether it's a new game.
     */
    public void reset(boolean newGame){
        finishedRendering=false;
        tanks = new ArrayList<>();
        tanks = new ArrayList<>();
        tankIds = new HashSet<>();
        tanksNum=0;
        App.selectedTankIndex=0;
        tankScoresTaken=false;
        if(newGame){
            tankScores.clear();
        }
    }

    /**
     * Gets the tank scores.
     * @return The list of tank scores.
     */
    public List<Integer> getTankScores(){
        Collections.sort(tanks, Comparator.comparingInt(tank -> tank.getC() - 'A'));
        if (!tankScoresTaken) {
            for (Tank tank : tanks) {
                tankScores.add(tank.playerScore);
            }
            tankScoresTaken=true;
        }
        return tankScores;
    }
    /**
     * Updates the tank scores.
     * @param tankScores The list of tank scores to update.
     */
    public void updateTankScores(List<Integer> tankScores){
        int i=0;
        Collections.sort(tanks, Comparator.comparingInt(tank -> tank.getC() - 'A'));
        for (Tank tank : tanks) {
            tank.setScore(tankScores.get(i));
            i++;
        }
        tankScores.clear();
    }
    /**
     * Renders the game level.
     */
    public void renderLevel() {
        if(!finishedRendering){
            for (int y = 0; y < levelLines.length; y++) {
            String line = levelLines[y];
            for (int x = 0; x < line.length(); x++) {
                char c = line.charAt(x);
                switch(c) {
                    case 'X':
                        heights.put(x, y);
                        drawTerrain(parent, terrainColor, x, y, CELLSIZE);
                        break;
                    case 'T':
                        treeXArray.add(x);
                        treeYArray.add(y);
                        drawTrees(parent, x, y, CELLSIZE, treesImage);
                        break;
                    case ' ':
                        break;
                    default:
                        drawTanks(parent, c, x, y, playerColors, CELLSIZE, smoothedTerrainArray, GUI);
                        break;
                }
            }
        }
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

    /**
     * Updates the terrain after an explosion.
     * @param explosionX The x-coordinate of the explosion.
     * @param explosionY The y-coordinate of the explosion.
     */
    public static void updateTerrainAfterExplosion(int explosionX, int explosionY) {
        int xRange = 0;
        boolean middleReached = false;
        for (int x = explosionX - 30; x <= explosionX + 30; x++) {
            if(xRange==30 && !middleReached){
                middleReached = true;
            } else if (middleReached){
                xRange-=1;
            } else {
                xRange+=1;
            }
            if (x >= 0 && x < ScreenX) {
                float currentVal = smoothedTerrainArray.get(x);
                currentVal +=0.3*xRange/30;
                smoothedTerrainArray.set(x, currentVal);
            }
        }
    }

    /**
     * Draws the terrain.
     * @param parent The parent PApplet.
     * @param terrainColor The color of the terrain.
     * @param x The x-coordinate of the terrain.
     * @param y The y-coordinate of the terrain.
     * @param CELLSIZE The size of a cell.
     */
    private static void drawTerrain(PApplet parent, int terrainColor, int x, int y, int CELLSIZE) {
        int x1=0;
        for (Float y1: smoothedTerrainArray) {
            parent.fill(terrainColor);
            parent.rect(x1, y1*CELLSIZE, 1, (20-y1)*CELLSIZE);
            x1+=1;
        }
    }

    /**
     * Draws trees on the level.
     * @param parent The parent PApplet.
     * @param x The x-coordinate of the tree.
     * @param y The y-coordinate of the tree.
     * @param CELLSIZE The size of a cell.
     * @param treesImage The image representing trees.
     */
    private static void drawTrees(PApplet parent, int x, int y, int CELLSIZE, PImage treesImage) {
        xSize = (x*32)+16;
        if (smoothedTerrainArray.size() > xSize) {
            Float y1 = smoothedTerrainArray.get(xSize);
            yCoord = y1 * CELLSIZE + 2;
            if(treesImage!=null){
                parent.image(treesImage, x * CELLSIZE, yCoord-CELLSIZE, CELLSIZE, CELLSIZE);
            }
        } 
    }
   /**
     * Draws tanks on the level.
     * @param parent The parent PApplet.
     * @param c The character representing the tank.
     * @param x The x-coordinate of the tank.
     * @param y The y-coordinate of the tank.
     * @param playerColors An array containing player colors.
     * @param CELLSIZE The size of a cell.
     * @param smoothedTerrainArray An array containing smoothed terrain heights.
     * @param GUI The graphical user interface.
     */
    private static void drawTanks(PApplet parent, char c, int x, int y, int[] playerColors, int CELLSIZE, List<Float> smoothedTerrainArray, GUI GUI) {
        if (tanks.size() < 5 && !tankIds.contains(c) && c >= 'A' && c <= 'E') {
            tanksNum+=1;
            Tank tank = new Tank(parent, c, playerColors, x, y, CELLSIZE, smoothedTerrainArray, GUI);
            tanks.add(tank);
            tankIds.add(c);
        } 
    }

    /**
     * Renders all tanks.
     * @param GUI The graphical user interface.
     */
    public static void renderAllTanks(GUI GUI) {
        int i=0;
        for (Tank tank : tanks) {
            // System.out.println(tank.getInfo());
            i+=1;
            if(tank.tankAlive) {
                tank.render(smoothedTerrainArray);
            } else {
                tank.playerHealth = 0;
                tank.explodeTank();
            }
            tank.renderGUI(i);
        }
        i=0;
    }

    /**
     * Gets all tanks in the level.
     * @return The list of tanks.
     */
    public static List<Tank> getTanks() {
        Collections.sort(tanks, Comparator.comparingInt(tank -> tank.getC() - 'A'));
        return tanks;
    }

    /**
     * Gets information about projectiles.
     * @return An array containing information about projectiles.
     */
    public static float[] getProjectiles() {
        List<float[]> projectiles = new ArrayList<>();
        float[] latestProjectile ={0,0,0,0};
        float largestThirdElement = Float.NEGATIVE_INFINITY;
        for (Tank tank : tanks) {
            float[] projectile=tank.getProjectile();
            if(projectile!=null){
                projectiles.add(projectile);
            }
        }
        float i = 0;
        for (float[] projectile : projectiles) {
            if (projectile[2] > largestThirdElement) {
                largestThirdElement = projectile[2];
                latestProjectile = projectile;
                i++;
            }
        }
        float[] bruh = new float[latestProjectile.length + 1];
        for (int j = 0; j < latestProjectile.length; j++) {
            bruh[j] = latestProjectile[j];
        }
        bruh[bruh.length - 1] = i;
        return bruh;
    }

    /**
     * Calculates damage to score.
     * @return A list containing damage received and which tank was shot.
     */
    public static List<Integer> damageToScore(){
        List<Integer> damageRecieved = new ArrayList<>();
        int totalDamage = 0;
        int whichTankShot = -1;
        for (Tank tank : tanks) {
            totalDamage += tank.getDamageRecieved().get(0);
            whichTankShot = tank.getDamageRecieved().get(1);
        }
        damageRecieved.add(totalDamage);
        damageRecieved.add(whichTankShot);
        return damageRecieved;
    }
}