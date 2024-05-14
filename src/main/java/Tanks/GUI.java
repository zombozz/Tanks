package Tanks;
import processing.core.PApplet;
import processing.core.PImage;
import java.util.Arrays;
public class GUI {
    private PApplet parent;
    private int screenWidth;
    private int screenHeight;
    private int CELLSIZE;
    private PImage fuelImage; 
    private PImage windImage; 
    private int currentPlayerIndex = 0;
    private int paddingTop = 30;
    
    private int fuelAmount;
    private int playerHealth = 100;
    private int playerPower = 50;
    private int playerScore = 0;
    private int windForce = 0;

    private int[] fuelAmounts;
    private int[] playerHealths;
    private int[] playerPowers;
    private int[] playerScores;
    private int[] playerWindForces;
    
    private int numberOfPlayers = 0;
    private int[] playerColors;

    public GUI(PApplet parent) {
        this.parent=parent;
        this.screenWidth = App.BOARD_WIDTH*32;
        this.screenHeight = App.BOARD_HEIGHT*32;
    }
    public void displayGUIElements(){
        displayPlayerName();
        displayFuel();
        displayBar();
        displayScoreboard();
        displayWind();
    }

    public void setPlayerDetails(int fuelAmount, int playerHealth, int playerPower, int playerScore, int windForce){
        // System.out.println(playerWindForces[0]);
        if(numberOfPlayers>0){
            this.fuelAmounts[currentPlayerIndex] = fuelAmount;
            this.playerHealths[currentPlayerIndex] = playerHealth;
            this.playerPowers[currentPlayerIndex] = playerPower;
            this.playerScores[currentPlayerIndex] = playerScore;
            this.playerWindForces[currentPlayerIndex] = windForce;
            try{
            } catch (NullPointerException e){}
        }
    }
    public int getCurrentPlayerIndex(){
        return currentPlayerIndex;
    }

    public void displayPlayerName(){
        parent.fill(0);
        parent.textSize(15);
        parent.text("Player " + (char) (currentPlayerIndex+65) + "'s turn", 30, paddingTop);
    }

    public void displayScoreboard(){
        parent.fill(0);
        parent.textSize(15);
        parent.text("Scores", screenWidth-120, paddingTop*2);

        for(int i = 0; i<numberOfPlayers; i++){
            parent.fill(playerColors[i]);
            parent.text("Player " + (char) (i+65), screenWidth-120, (paddingTop*2)+(16*(i+1)));
            parent.fill(0);
            parent.text(playerScores[currentPlayerIndex], screenWidth-30, (paddingTop*2)+(16*(i+1)));
        }
    }

    public void displayBar(){
        parent.fill(0);
        parent.textSize(15);

        int healthPowerX = screenWidth/2-40;
        int barX = healthPowerX+60;
        int barWidth = CELLSIZE*4;
        parent.text("Health:", healthPowerX, paddingTop);
        //remove
        for(int i = 0; i<numberOfPlayers; i++){
            parent.text(playerHealths[i], barX + barWidth, paddingTop+i*10);
        }
        //remove
        // parent.text(playerHealths[currentPlayerIndex], barX + barWidth, paddingTop);
        parent.text("Power: " + playerPowers[currentPlayerIndex], healthPowerX, paddingTop+25);

        parent.fill(255,0,0);
        parent.rect(barX, paddingTop/2, barWidth, CELLSIZE-8);
    }

    public void displayFuel(){
        // fuelAmount=250;
        // this.fuelAmount = fuelAmount;
        int fuelX = 160;
        parent.image(fuelImage, fuelX, 10, CELLSIZE-8, CELLSIZE-8);
        parent.fill(0);
        parent.textSize(15);
        parent.text(fuelAmounts[currentPlayerIndex], fuelX+30, paddingTop);

    }

    public void displayWind(){
        int windPower = 200;
        int windX = screenWidth-100;
        parent.image(windImage, windX, 5, CELLSIZE+8, CELLSIZE+8);
        parent.fill(0);
        parent.textSize(15);
        try{
            parent.text(playerWindForces[currentPlayerIndex], windX+50, paddingTop);
        } catch(NullPointerException e) {
                
        }
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex){
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public void playersSetup(int numberOfPlayers, int[] playerColors){
        this.numberOfPlayers = numberOfPlayers;
        this.playerColors = playerColors;

        this.fuelAmounts = new int[numberOfPlayers];
        this.playerHealths = new int[numberOfPlayers];
        this.playerPowers = new int[numberOfPlayers];
        this.playerScores = new int[numberOfPlayers];
        this.playerWindForces = new int[numberOfPlayers];
    }

    public void setImages(PImage fuelImage, PImage windImage, int CELLSIZE){
        this.fuelImage=fuelImage;
        this.windImage=windImage;
        this.CELLSIZE=CELLSIZE;
    }
}