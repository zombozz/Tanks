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
import ddf.minim.*;
import ddf.minim.spi.*;



public class App extends PApplet {
    Minim minim;
    AudioPlayer player;
    public static int levelNo = 0;


    public static GUI GUI;
    LevelRenderer levelRenderer;
    public static final int CELLSIZE = 32; //8;
    public static final int CELLHEIGHT = 32;
    private Tank tank;
    public static int tanksNum;
    public static int selectedTankIndex = 0;

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

    public static PImage parachuteImage;
    public static PImage arrowImage;
    private boolean firstTankDrawn=false;

    private Powerups powerups;

    private boolean settingUp = true;
    private List<Integer> tankScores = new ArrayList<>();
	
    public static List<Tank> tanks = LevelRenderer.getTanks();
    public static float[] latestProjectile = LevelRenderer.getProjectiles();
    public static List<Integer> damageToScore;

    private Gameover gameover;
    private boolean isGameover = false;

    public App() {
        this.configPath = "config.json";
    }
	@Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    PImage backgroundImage;
    PImage treesImage;
    String[] levelLines;
    int[] playerColors = {
        color(0, 0, 255),  
        color(255, 0, 0), 
        color(0, 255, 255),  
        color(255, 255, 0), 
        color(0, 255, 0),
    };
    int terrainColor;
	@Override
    public void setup() {
        settingUp=true;
        powerups = new Powerups();
        JSONParser parser = new JSONParser();
        GUI = new GUI(this);
        timer = new Timer(this, this.millis());
        minim = new Minim(this);
        player = minim.loadFile("src\\main\\resources\\Tanks\\welcome-traveler.mp3");
        player.play();
        
        frameRate(FPS);

        PImage fuelImage = loadImage("src\\main\\resources\\Tanks\\fuel.png");
        PImage windImage = loadImage("src\\main\\resources\\Tanks\\wind.png");
        parachuteImage = loadImage("src\\main\\resources\\Tanks\\parachute.png");
        arrowImage = loadImage("src\\main\\resources\\Tanks\\arrow.png");

        GUI.setImages(fuelImage, windImage, parachuteImage, CELLSIZE);
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
                treesImage = null;
            }
            backgroundImage = loadImage("src\\main\\resources\\Tanks\\"+backgroundFilename);
            
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        levelRenderer = new LevelRenderer(this, levelLines, playerColors, terrainColor, treesImage, CELLSIZE, GUI);
        levelRenderer.reset(false);
        levelRenderer.renderLevel();
        settingUp=false;
    }

    private void loadLevel() {
        settingUp=true;
        powerups = new Powerups();
        JSONParser parser = new JSONParser();
        GUI = new GUI(this);
        timer = new Timer(this, this.millis());
        minim = new Minim(this);
        
        frameRate(FPS);

        PImage fuelImage = loadImage("src\\main\\resources\\Tanks\\fuel.png");
        PImage windImage = loadImage("src\\main\\resources\\Tanks\\wind.png");
        parachuteImage = loadImage("src\\main\\resources\\Tanks\\parachute.png");
        arrowImage = loadImage("src\\main\\resources\\Tanks\\arrow.png");

        GUI.setImages(fuelImage, windImage, parachuteImage, CELLSIZE);
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
                treesImage = null;
            }
            backgroundImage = loadImage("src\\main\\resources\\Tanks\\"+backgroundFilename);
            
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        levelRenderer = new LevelRenderer(this, levelLines, playerColors, terrainColor, treesImage, CELLSIZE, GUI);
        System.out.println(levelLines[9]);
        tankScores = levelRenderer.getTankScores();
        levelRenderer.reset(false);
        levelRenderer.renderLevel();
        levelRenderer.updateTankScores(tankScores);
        settingUp=false;
    }

    public GUI getGUI(){
        return GUI;
    }

	@Override
    
    public void keyPressed(KeyEvent event){
        if (this.keyCode == 37) {//left
            tanks.get(selectedTankIndex).moveTank(-1);
        } else if (this.keyCode == 39) {//right
            tanks.get(selectedTankIndex).moveTank(1);
        } else if (this.keyCode == 38) {//up
            tanks.get(selectedTankIndex).moveTurret(-6);
        } else if (this.keyCode == 40) {//down
            tanks.get(selectedTankIndex).moveTurret(6);
        } else if (this.keyCode == 87) {//w
            tanks.get(selectedTankIndex).changePower(1);
        } else if (this.keyCode == 83) {//s
            tanks.get(selectedTankIndex).changePower(-1);
        }else if (this.keyCode == 32) {//spacebar 
            tanks.get(selectedTankIndex).shootTurret();
            nextTank();
        } else if (this.keyCode == 82) { // R key
            powerups.repairTank(tanks.get(selectedTankIndex));
            if (isGameover) {
                levelNo = 0;
                levelRenderer.reset(true);
                setup();
                levelNo=-1;
                nextLevel();
            }
        } else if (this.keyCode == 70) { // F key
            powerups.addFuel(tanks.get(selectedTankIndex));
        }else if (this.keyCode == 81) {
            nextLevel();
        }
    }
    public void nextTank(){
        selectedTankIndex+=1;
        if(selectedTankIndex >= tanksNum){
            selectedTankIndex=0;
        }
        for (Tank tank : tanks) {
            tank.setPlayerNum(selectedTankIndex);
        }
        GUI.setCurrentPlayerIndex(selectedTankIndex);
        tanks.get(selectedTankIndex).drawTankArrow(arrowImage);
    }
    public void checkTanksRemaining(){
        int i = 0;
        for (Tank tank : tanks) {
            if(!tank.tankAlive) {
                i+=1;
            }
        }
        if(i==tanksNum-1){
            nextLevel();
        }
    }

    public void nextLevel(){
        try{
            levelNo+=1;
            for (Tank tank : tanks) {
                tank.tankAlive=true;
            }
            loadLevel();
            numSet=false;
        } catch (IndexOutOfBoundsException e) {
            settingUp = true;
            tankScores = levelRenderer.getTankScores();
            isGameover = true;
            if(isGameover) {
                gameover = new Gameover(this, tankScores);
            }
        }
    }
    
	@Override
    public void keyReleased(){
        if (this.keyCode == 37) {//left
            tanks.get(selectedTankIndex).stopTankMoveSound();
        } else if (this.keyCode == 39) {//right
            tanks.get(selectedTankIndex).stopTankMoveSound();
        }
    }
	@Override
    public void draw() {
        if(!settingUp){
            tanks = levelRenderer.getTanks();
            latestProjectile = levelRenderer.getProjectiles();
            damageToScore = levelRenderer.damageToScore();
            if(damageToScore.get(0)!=0){
                tanks.get(damageToScore.get(1)-1).addScore(damageToScore.get(0));;
            }
            noStroke();
            image(backgroundImage, 0, 0, width, height);
            levelRenderer.renderLevel();
            tanksNum = levelRenderer.tanksNum;
            
            if((tanksNum!= 0) && (numSet==false)){
                GUI.playersSetup(tanksNum, playerColors);
                numSet=true;
            }
            try{
                GUI.displayGUIElements();
            } catch (NullPointerException e){}
            
            if(timer.finishedRendering() && !firstTankDrawn){
                tanks.get(0).drawTankArrow(arrowImage);
                firstTankDrawn = true;
            }
            try{
                if(!tanks.get(selectedTankIndex).tankAlive){
                    nextTank();
                }
            } catch (IndexOutOfBoundsException e){}
            checkTanksRemaining();
        }
    }
        
    public static void main(String[] args) {
        PApplet.main("Tanks.App");
    }
}
    