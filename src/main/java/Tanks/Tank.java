package Tanks;

import java.util.ArrayList;
import java.util.List;
import ddf.minim.*;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * Tank objects representing each player.
 */
public class Tank {
    private PApplet parent;
    public char c;
    public int[] colors;
    private int x;
    private float y;
    private int CELLSIZE;
    private List<Float> smoothedTerrainArray;

    private int moveTankBy = 0;
    private int moveTurretBy = 0;

    private Projectile projectile;
    private float rotationAngle;

    public float tankX;
    public float tankY;
    public float previousTankX;
    public float previousTankY;
    private float y1;
    
    public int playerFuel = 250;
    public int playerHealth = 100;
    public int playerPower = 50;
    public int playerScore = 100;
    public int windForce = (int) (Math.random() *(35+35) -35);

    private int playerNum = 0;
    private int i = 0;
    private boolean initialDetailsSetup = false;
    private GUI GUI;

    private float[] explosionCoords;
    private float[] oldExplosionCoords;

    public List<Projectile> projectiles;

    private Minim minim;
    private SoundEffects soundEffects;
    private PImage parachuteImage = App.parachuteImage;

    private boolean parachuteActive = false;
    private int parachuteStartTime = 0;
    public int parachutesRemaining = 3;

    private boolean tankFalling = false;

    private boolean drawArrow = false;
    private int arrowStartTime = 0;
    private PImage arrowImage;
    
    public boolean tankAlive = true;
    public boolean tankExploded = false;

    private Explosion explosion;
    private List<Tank> tanks = App.tanks;
    public List<Integer> damageReceived = new ArrayList<>();
    private float[] latestProjectilesCoords = App.latestProjectile;
    
    private int whichTankShot;

    private float damage = 0;


    private boolean finalHealthAdded = false;
    /**
     * Constructs a Tank object.
     * @param parent The parent PApplet.
     * @param c The character representing the tank.
     * @param colors The array of colors for the tank.
     * @param x The x-coordinate of the tank.
     * @param y The y-coordinate of the tank.
     * @param size The size of the tank.
     * @param smoothedTerrainArray The list of smoothed terrain array.
     * @param GUI The GUI object.
     */
    public Tank(PApplet parent, char c, int[] colors, int x, float y, int size, List<Float> smoothedTerrainArray, GUI GUI) {
        projectiles = new ArrayList<>();
        this.parent = parent;
        this.c = c;
        this.colors = colors;
        this.x = x;
        this.y = y;
        this.CELLSIZE = size;
        this.smoothedTerrainArray = smoothedTerrainArray;
        this.GUI=GUI;

        minim = new Minim(parent);
        soundEffects = new SoundEffects(parent, minim);
    }

    /**
     * Gets the coordinates of the projectile.
     * @return The coordinates of the projectile.
     */
    public float[] getProjectile(){
        try {
            return projectile.getExplosionCoords();
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Checks the damage taken by the tank.
     */
    public void checkDamage(){
        damage = 0;
        latestProjectilesCoords = App.latestProjectile;
        whichTankShot = Math.round(latestProjectilesCoords[3]);
        try{
            oldExplosionCoords = explosionCoords;
            explosionCoords = latestProjectilesCoords;
            if(!(oldExplosionCoords[0]==explosionCoords[0])){
                float explosionX = explosionCoords[0];
                float explosionY = explosionCoords[1];
                float distanceSquared = (tankX - explosionX) * (tankX - explosionX) + (tankY - explosionY) * (tankY - explosionY);
                float distance = parent.sqrt(distanceSquared); 
                int explosionRadius =40;
                if (distance <= explosionRadius) {
                float maxDamage = 60; 
                damage = maxDamage * (1 - distance / explosionRadius);
                playerHealth -= damage;
                System.out.println(damage);
            }
        } 
    }catch (NullPointerException  | IndexOutOfBoundsException e){}
    }

    /**
     * Gets the damage received by the tank.
     * @return A list containing the damage received and which tank was shot.
     */
    public List<Integer> getDamageRecieved() {
        // List<Integer> damageReceived = new ArrayList<>();
        damageReceived = new ArrayList<>();
        if(!finalHealthAdded){
            System.out.println(whichTankShot + " " + App.selectedTankIndex);
            if(whichTankShot != App.selectedTankIndex-1){
                damageReceived.add(Math.round(damage));
            } else {
                damageReceived.add(0);
            }
            damageReceived.add(whichTankShot);
            if(playerHealth==0){
                finalHealthAdded = true;
            }
            return damageReceived;
        } else {
            damageReceived.add(0);
            damageReceived.add(whichTankShot);
            return damageReceived;
        }
    }

    /**
     * Checks if the tank is falling.
     */
    public void checkIfTankFall() {
        int fallingStartTime;
        if (parachutesRemaining>0 && tankY > previousTankY && tankX == previousTankX) {
            parachuteActive = true;
            parachuteStartTime = parent.millis();
        } else if (parachutesRemaining==0 && tankY > previousTankY && tankX == previousTankX) {
            tankFalling = true;
        }
        if (parachuteActive) {
            tankFalling=true;
            parent.image(parachuteImage, tankX - 16, tankY - 40, 32, 32);
            if (parent.millis() - parachuteStartTime > 50) {
                parachutesRemaining-=1;
                parachuteActive = false;
            } 
        }
        previousTankX = tankX;
        previousTankY = tankY;
    }

    /**
     * Moves the tank.
     * @param moveTankBy The amount to move the tank by.
     */
    public void moveTank(int moveTankBy) {
        
        if(tankAlive && (tankX+moveTankBy > 0) && (tankX+moveTankBy < App.WIDTH)) {
            if(playerFuel>0){
                soundEffects.playTankMoveSound();
                this.moveTankBy+=moveTankBy;
                this.playerFuel-=1;
            }
        } else {
            tankAlive = false;
            int explosionInitialTime = parent.millis();
            explosion = new Explosion(parent, tankX, tankY, explosionInitialTime, soundEffects);
        }
        render(smoothedTerrainArray);
    }

    /**
     * Stops the tank move sound.
     */
    public void stopTankMoveSound(){
        soundEffects.stopTankMoveSound();
    }

    /**
     * Changes the power of the tank.
     * @param power The amount to change the power by.
     */
    public void changePower(int power){
        playerPower+=power;
        if(playerPower > playerHealth){
            playerPower=playerHealth;
        } else if(playerPower<0) {
            playerPower=0;
        }
    }

    /**
     * Sets the score of the tank.
     * @param playerScore The score of the tank.
     */
    public void setScore(int playerScore){
        this.playerScore=playerScore;
    }

    /**
     * Adds to the score of the tank.
     * @param playerScore The score to add.
     */
    public void addScore(int playerScore){
        this.playerScore+=playerScore;
    }

    /**
     * Explodes the tank.
     */
    public void explodeTank(){
        explosion.Explode();
    }

    /**
     * Resets the tank.
     * @return The score of the tank.
     */
    public int tankReset(){
        return playerScore;
    }

    /**
     * Moves the turret.
     * @param moveTurret The amount to move the turret by.
     */
    public void moveTurret(int moveTurret) {
        soundEffects.playTurretSound();
        this.moveTurretBy+=moveTurret;
        render(smoothedTerrainArray);
    }

    /**
     * Shoots the turret.
     */
    public void shootTurret() {
        float projectileSpeed = playerPower; 
        projectile = new Projectile(parent, tankX, tankY - 10, projectileSpeed, CELLSIZE, moveTurretBy, smoothedTerrainArray,soundEffects, windForce);
        projectiles.add(projectile);
        windForce+=(int) (Math.random() *(10) -5);
    }

    /**
     * Renders the GUI.
     * @param i Index of the tank.
     */
    public void renderGUI(int i){
        if(GUI.getCurrentPlayerIndex()+1 ==i){
            GUI.setPlayerDetails(playerFuel, playerHealth, playerPower, playerScore, windForce, parachutesRemaining);
        } 
        GUI.setPlayerScores(i, playerScore, playerHealth);
    }

    /**
     * Renders the tank.
     * @param smoothedTerrainArray List of smoothed terrain array.
     */  
    public void render(List<Float> smoothedTerrainArray) {
        if(tankAlive){
        checkDamage();
        this.smoothedTerrainArray = smoothedTerrainArray;
        int xSize = (x*32) + moveTankBy;
        if  (smoothedTerrainArray.size() > xSize) {
            y1 = smoothedTerrainArray.get(xSize);
            // tank movement
            int index = c - 'A'; 
            tankX = x * CELLSIZE + moveTankBy;
            if(tankFalling && tankY < (y1 * CELLSIZE) && parachutesRemaining > 0){
                tankY+=0.5;
            } else if (tankFalling && tankY < (y1 * CELLSIZE) && parachutesRemaining ==0){
                tankY+=1;
                playerHealth-=0.2;
            } else{
                tankY = y1 * CELLSIZE;
                tankFalling = false;
            }
            if (playerHealth < 0 || playerHealth==0) {
                playerHealth = 0;
                tankAlive=false;
                int explosionInitialTime = parent.millis();
                explosion = new Explosion(parent, tankX, tankY, explosionInitialTime, soundEffects);
            }
            checkIfTankFall();
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
            parent.ellipse(0, -6, CELLSIZE/8, CELLSIZE/2);
            parent.popMatrix();
            try {
                if (!projectile.isNull) {
                    projectile.update(); 
                    projectile.display();
                }
            } catch (NullPointerException e)  {

            }

            if (drawArrow) {
                if (parent.millis() - arrowStartTime < 2000) {
                    parent.image(arrowImage, tankX - 16, tankY - 50, 32, 32);
                } else {
                    drawArrow = false;
                }
            }
            if(playerPower > playerHealth){
                playerPower=playerHealth;
            }
        }
        }
    }
    /**
     * Gets the tank's character.
     * @return The tank's character.
     */
    public char getC(){
        return c;
    }
    
    /**
     * Gets the movement of the tank.
     * @return The movement of the tank.
     */
    public float getTankMovement(){
        return moveTankBy;
    }
    
    /**
     * Gets the Y coordinate of the tank.
     * @return The Y coordinate of the tank.
     */
    public float getY(){
        return tankY;
    }
    
    /**
     * Sets the player number.
     * @param playerNum The player number.
     */
    public void setPlayerNum(int playerNum){
        this.playerNum = playerNum;
    }

    /**
     * Draws the tank arrow.
     * @param arrowImage The arrow image.
     */
    public void drawTankArrow(PImage arrowImage){
        this.arrowImage = arrowImage;
        drawArrow = true;
        arrowStartTime = parent.millis();
    }
    
    /**
     * Gets information about the tank.
     * @return Information about the tank.
     */
    public String getInfo() {
        String bruh = c + "  "+ Integer.toString(x) +  "  "+ Float.toString(y);
        return bruh;
    }

    /**
     * Takes damage.
     * @param damage The amount of damage to take.
     */
    public void takeDamage(int damage) {
        playerHealth -= damage;
    }

    /**
     * Heals the tank.
     * @param health The amount of health to add.
     */
    public void heal(int health) {
        playerHealth+=health;
    }

    /**
     * Sets the health of the tank.
     * @param health The health of the tank.
     */
    public void setHealth(int health) {
        playerHealth=health;
    }

    /**
     * Sets the fuel of the tank.
     * @param fuel The fuel of the tank.
     */
    public void setFuel(int fuel) {
        playerFuel+=fuel;
    }

    /**
     * Gets the health of the tank.
     * @return The health of the tank.
     */
    public int getHealth() {
        return playerHealth;
    }

    /**
     * Gets the score of the tank.
     * @return The score of the tank.
     */
    public int getScore() {
        return playerScore;
    }

    /**
     * Gets the fuel of the tank.
     * @return The fuel of the tank.
     */
    public int getFuel() {
        return playerFuel;
    }

    /**
     * Gets the color of the tank.
     * @return The color of the tank.
     */
    public int[] getColor() {
        return colors;
    }
}

