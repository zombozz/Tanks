package Tanks;
import processing.core.PApplet;
import processing.core.PImage;

public class GUI {
    private PApplet parent;
    private int screenWidth;
    private int screenHeight;
    private int CELLSIZE;
    private PImage fuelImage; 
    private PImage windImage; 
    private int currentPlayerIndex = 0;
    private int fuelAmount;
    private int paddingTop = 30;

    private int playerHealth = 100;
    private int playerPower = 50;
    private int playerScore = 0;

    public GUI(PApplet parent) {
        this.parent=parent;
        this.screenWidth = App.BOARD_WIDTH*32;
        this.screenHeight = App.BOARD_HEIGHT*32;
    }
    public void displayGUIElements(){
        displayPlayerName();
        displayFuel();
        displayWind();
        displayBar();
        displayScoreboard();
    }

    public void displayPlayerName(){
        parent.fill(0);
        parent.textSize(15);
        parent.text("Player " + (char) (currentPlayerIndex+65) + "'s turn", 30, paddingTop);
    }
    public void displayScoreboard(){
        //todo
    }

    public void displayBar(){
        parent.fill(0);
        parent.textSize(15);

        int healthPowerX = screenWidth/2-40;
        int barX = healthPowerX+60;
        int barWidth = CELLSIZE*4;
        parent.text("Health:", healthPowerX, paddingTop);
        parent.text(playerHealth, barX + barWidth, paddingTop);
        parent.text("Power: " + playerPower, healthPowerX, paddingTop+25);

        parent.fill(255,0,0);
        parent.rect(barX, paddingTop/2, barWidth, CELLSIZE-8);
    }

    public void displayFuel(){
        fuelAmount=250;
        // this.fuelAmount = fuelAmount;
        int fuelX = 160;
        parent.image(fuelImage, fuelX, 10, CELLSIZE-8, CELLSIZE-8);
        parent.fill(0);
        parent.textSize(15);
        parent.text(Integer.toString(fuelAmount), fuelX+30, paddingTop);
    }

    public void displayWind(){
        int windPower = 200;
        int windX = screenWidth-100;
        parent.image(windImage, windX, 5, CELLSIZE+8, CELLSIZE+8);
        parent.fill(0);
        parent.textSize(15);
        parent.text(Integer.toString(windPower), windX+50, paddingTop);
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex){
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public void setImages(PImage fuelImage, PImage windImage, int CELLSIZE){
        this.fuelImage=fuelImage;
        this.windImage=windImage;
        this.CELLSIZE=CELLSIZE;
    }
}