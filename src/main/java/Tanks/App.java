package Tanks;

import org.checkerframework.checker.units.qual.A;
import processing.core.PApplet;
import processing.core.PImage;
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

import com.jogamp.opengl.util.packrect.Level;

import java.io.*;
import java.util.*;
import java.util.List;

public class App extends PApplet {

    public static int levelNo = 0;


    public static GUI GUI;
    LevelRenderer levelRenderer;
    public static final int CELLSIZE = 32; //8;
    public static final int CELLHEIGHT = 32;
    private Tank tank;
    private static int tanksNum;
    private static int selectedTankIndex = 0;

    public static final int CELLAVG = 32;
    public static final int TOPBAR = 0;
    public static int WIDTH = 864; //CELLSIZE*BOARD_WIDTH;
    public static int HEIGHT = 640; //BOARD_HEIGHT*CELLSIZE+TOPBAR;
    public static final int BOARD_WIDTH = WIDTH/CELLSIZE;
    public static final int BOARD_HEIGHT = 20;

    public static final int INITIAL_PARACHUTES = 1;

    public static final int FPS = 30;

    public String configPath;

    public static Random random = new Random();

    public static Timer timer;
    
    private boolean numSet = false;
	
	// Feel free to add any additional methods or attributes you want. Please put classes in different files.

    public App() {
        this.configPath = "config.json";
    }

    /**
     * Initialise the setting of the window size.
     */
	@Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player and map elements.
     */
    PImage backgroundImage;
    PImage treesImage;
    String[] levelLines;
    int[] playerColors = {
        color(0, 0, 255),   // Blue for player A
        color(255, 0, 0),   // Red for player B
        color(0, 255, 255),   // Aqua for player C
        color(255, 255, 0),   // Yellow for player D
        color(0, 255, 0),   // Green for player E
        // Add more colors for other players if needed
    };
    int terrainColor;
	@Override
    public void setup() {
        frameRate(FPS);
        levelRenderer = new LevelRenderer();
        JSONParser parser = new JSONParser();
        timer = new Timer(this, this.millis());

        GUI = new GUI(this);
        PImage fuelImage = loadImage("src\\main\\resources\\Tanks\\fuel.png");
        PImage windImage = loadImage("src\\main\\resources\\Tanks\\wind.png");
        GUI.setImages(fuelImage, windImage, CELLSIZE);
        
         
        try {
            JSONObject config = (JSONObject) parser.parse(new FileReader("config.json"));
            JSONArray levels = (JSONArray) config.get("levels");
            JSONObject level1 = (JSONObject) levels.get(levelNo);

            String layoutFilename = (String) level1.get("layout");
            levelLines = loadStrings(layoutFilename);

            String backgroundFilename = (String) level1.get("background");
            String foregroundColour = (String) level1.get("foreground-colour");
            String[] components = foregroundColour.split(",");
            int r = Integer.parseInt(components[0]);
            int g = Integer.parseInt(components[1]);
            int b = Integer.parseInt(components[2]);
            terrainColor = color(r, g, b);

            if (level1.containsKey("trees")) {
                String treesFilename = (String) level1.get("trees");
                treesImage = loadImage("src\\main\\resources\\Tanks\\"+treesFilename);
            } else {
                treesImage = loadImage("src\\main\\resources\\Tanks\\fuel.png");
            }

            backgroundImage = loadImage("src\\main\\resources\\Tanks\\"+backgroundFilename);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public GUI getGUI(){
        return GUI;
    }

	@Override
    
    public void keyPressed(KeyEvent event){
        List<Tank> tanks = LevelRenderer.getTanks();
        if (this.keyCode == 37) {//left
            tanks.get(selectedTankIndex).moveTank(-1);
        } else if (this.keyCode == 39) {//right
            tanks.get(selectedTankIndex).moveTank(1);
        } else if (this.keyCode == 38) {//up
            tanks.get(selectedTankIndex).moveTurret(-6);
            
        } else if (this.keyCode == 40) {//down
            tanks.get(selectedTankIndex).moveTurret(6);
            
        }else if (this.keyCode == 32) { 
            tanks.get(selectedTankIndex).shootTurret();
            selectedTankIndex += 1;
            if(selectedTankIndex >= tanksNum){
                selectedTankIndex=0;
            }
            for (Tank tank : tanks) {
                tank.setPlayerNum(selectedTankIndex);
            }
            GUI.setCurrentPlayerIndex(selectedTankIndex);
            System.out.println(selectedTankIndex);
        }else if (this.keyCode == 49) { 
            selectedTankIndex = 2; 
        } else if (this.keyCode == 50) {
            selectedTankIndex = 1;
        } else if (this.keyCode == 35) {
            exit();
        } 
        
    }

	@Override
    public void keyReleased(){
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //TODO - powerups, like repair and extra fuel and teleport


    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }
    
	@Override
    public void draw() {
        noStroke();
        image(backgroundImage, 0, 0, width, height);
        LevelRenderer.renderLevel(this, levelLines, playerColors, terrainColor, treesImage, CELLSIZE, GUI);
        // levelRenderer.smoothTerrain();
        tanksNum = levelRenderer.tanksNum;
        
        if((tanksNum!= 0) && (numSet==false)){
            GUI.playersSetup(tanksNum, playerColors);
            numSet=true;
        }
        GUI.displayGUIElements();

        
        //----------------------------------
        //display HUD:
        //----------------------------------
        //TODO

        //----------------------------------
        //display scoreboard:
        //----------------------------------
        //TODO
        
		//----------------------------------
        //----------------------------------

        //TODO: Check user action
    }


    public static void main(String[] args) {
        PApplet.main("Tanks.App");
    }

}
