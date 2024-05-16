package Tanks;
import processing.core.PApplet;
import processing.core.PImage;
import java.util.Arrays;
public class GUI {
    private PApplet parent;
    private int screenWidth;
    private int screenHeight;
    public int CELLSIZE;
    public int currentPlayerIndex = 0;
    private int paddingTop = 30;

    public PImage fuelImage; 
    public PImage windImage; 
    public PImage parachuteImage; 
    
    private int fuelAmount = 0;
    private int playerHealth = 100;
    private int playerPower = 50;
    private int playerScore = 0;
    private int windForce = 0;
    private int parachutesRemaining = 3;

    public int[] fuelAmounts;
    public int[] playerHealths;
    public int[] playerPowers;
    public int[] playerScores;
    public int[] playerWindForces;
    public int[] playerParachutes;
    
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
        displayParachutesRemaining();
    }

    public void setPlayerDetails(int fuelAmount, int playerHealth, int playerPower, int playerScore, int windForce, int parachutesRemaining){
        if(numberOfPlayers>0){
            this.fuelAmounts[currentPlayerIndex] = fuelAmount;
            this.playerPowers[currentPlayerIndex] = playerPower;
            this.playerWindForces[currentPlayerIndex] = windForce;
            this.playerParachutes[currentPlayerIndex] = parachutesRemaining;
            try{
            } catch (NullPointerException e){}
        }
    }
    public void setPlayerScores(int i, int playerScore, int playerHealth){
        if(numberOfPlayers>0){
            this.playerScores[i-1] = playerScore;
            this.playerHealths[i-1] = playerHealth;
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
            parent.text(playerScores[i], screenWidth-30, (paddingTop*2)+(16*(i+1)));
        }
    }
    public void displayParachutesRemaining(){
        int parachuteX = 160;
        parent.image(parachuteImage, parachuteX, 40, CELLSIZE-8, CELLSIZE-8);
        parent.fill(0);
        parent.textSize(15);
        parent.text(playerParachutes[currentPlayerIndex], parachuteX+30, paddingTop*2);
    }

    public void displayBar(){
        parent.fill(0);
        parent.textSize(15);
        
        int healthPowerX = screenWidth/2-40;
        int barX = healthPowerX+60;
        int barY = paddingTop/2 +2;
        int barWidth = 120;
        int barHeight= 14;

        parent.text("Health:", healthPowerX, paddingTop);
        parent.text(playerHealths[currentPlayerIndex], barX + barWidth + 2, paddingTop);
        parent.text("Power: " + playerPowers[currentPlayerIndex], healthPowerX, paddingTop+25);
        
        parent.fill(0);
        parent.rect(barX, barY, barWidth, barHeight);

        parent.fill(playerColors[currentPlayerIndex]);
        parent.rect(barX, barY, Math.round(playerHealths[currentPlayerIndex]*1.2), barHeight);

        parent.fill(255);
        parent.rect(Math.round(barX+playerPowers[currentPlayerIndex]*1.15), barY, 5, barHeight);
    }

    public void displayFuel(){
        int fuelX = 160;
        parent.image(fuelImage, fuelX, 10, CELLSIZE-8, CELLSIZE-8);
        parent.fill(0);
        parent.textSize(15);
        parent.text(fuelAmounts[currentPlayerIndex], fuelX+30, paddingTop);
    }

    public void displayWind(){
        int windPower = 200;
        int windX = screenWidth-100;
        int windY = 5;

        parent.fill(0);
        parent.textSize(15);
        try{
            parent.text(playerWindForces[currentPlayerIndex], windX+50, paddingTop);
            if(playerWindForces[currentPlayerIndex] > 0){
                parent.image(windImage, windX, 5, CELLSIZE+8, CELLSIZE+8);
            } else {
                parent.pushMatrix();
                parent.translate(windX + CELLSIZE / 2, windY + CELLSIZE / 2);
                parent.rotate(PApplet.PI);
                parent.translate(-windX - CELLSIZE / 2, -windY - CELLSIZE / 2);
                parent.image(windImage, windX-8, windY-8, CELLSIZE + 8, CELLSIZE + 8);
                parent.popMatrix();
            }
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
        this.playerParachutes = new int[numberOfPlayers];
    }

    public void setImages(PImage fuelImage, PImage windImage, PImage parachuteImage, int CELLSIZE){
        this.fuelImage=fuelImage;
        this.windImage=windImage;
        this.parachuteImage=parachuteImage;
        this.CELLSIZE=CELLSIZE;
    }
}