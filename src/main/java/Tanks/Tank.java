package Tanks;

import java.util.ArrayList;
import java.util.List;
import ddf.minim.*;
import processing.core.PApplet;
import processing.core.PImage;

public class Tank {
    private PApplet parent;
    public char c;
    private int[] colors;
    private int x;
    private float y;
    private int CELLSIZE;
    private List<Float> smoothedTerrainArray;

    private int moveTankBy = 0;
    private int moveTurretBy = 0;

    private Projectile projectile;
    private float rotationAngle;

    private float tankX;
    private float tankY;
    private float previousTankX;
    private float previousTankY;
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

    private List<Projectile> projectiles;

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
    private float[] latestProjectilesCoords = App.latestProjectile;
    
    private int whichTankShot;

    private float damage = 0;

    private boolean finalHealthAdded = false;
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

    public float[] getProjectile(){
        try {
            return projectile.getExplosionCoords();
        } catch (NullPointerException e) {
            return null;
        }
    }
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

    public List<Integer> getDamageRecieved() {
        List<Integer> damageRecieved = new ArrayList<>();
        if(!finalHealthAdded){
            System.out.println(whichTankShot + " " + App.selectedTankIndex);
            if(whichTankShot != App.selectedTankIndex-1){
                damageRecieved.add(Math.round(damage));
            } else {
                damageRecieved.add(0);
            }
            damageRecieved.add(whichTankShot);
            if(playerHealth==0){
                finalHealthAdded = true;
            }
            return damageRecieved;
        } else {
            damageRecieved.add(0);
            damageRecieved.add(whichTankShot);
            return damageRecieved;
        }
    }

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

    public void stopTankMoveSound(){
        soundEffects.stopTankMoveSound();
    }

    public void changePower(int power){
        playerPower+=power;
        if(playerPower > playerHealth){
            playerPower=playerHealth;
        } else if(playerPower<0) {
            playerPower=0;
        }
    }

    public void setScore(int playerScore){
        this.playerScore=playerScore;
    }
    public void addScore(int playerScore){
        this.playerScore+=playerScore;
    }
    
    public void explodeTank(){
        explosion.Explode();
    }

    public int tankReset(){
        return playerScore;
    }

    public void moveTurret(int moveTurret) {
        soundEffects.playTurretSound();
        this.moveTurretBy+=moveTurret;
        render(smoothedTerrainArray);
    }

    public void shootTurret() {
        float projectileSpeed = playerPower; // Set the speed of the projectile
        projectile = new Projectile(parent, tankX, tankY - 10, projectileSpeed, CELLSIZE, moveTurretBy, smoothedTerrainArray,soundEffects, windForce);
        projectiles.add(projectile);
        windForce+=(int) (Math.random() *(10) -5);
    }

    public void renderGUI(int i){
        if(GUI.getCurrentPlayerIndex()+1 ==i){
            GUI.setPlayerDetails(playerFuel, playerHealth, playerPower, playerScore, windForce, parachutesRemaining);
        } 
        GUI.setPlayerScores(i, playerScore, playerHealth);
    }
    
    public void render(List<Float> smoothedTerrainArray) {
        // System.out.println(playerScore);
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
    public char getC(){
        return c;
    }
    public float getTankMovement(){
        return moveTankBy;
    }
    public float getY(){
        return tankY;
    }
    public void setPlayerNum(int playerNum){
        this.playerNum = playerNum;
    }

    public void drawTankArrow(PImage arrowImage){
        this.arrowImage = arrowImage;
        drawArrow = true;
        arrowStartTime = parent.millis();
    }
    public String getInfo() {
        String bruh = c + "  "+ Integer.toString(x) +  "  "+ Float.toString(y);
        return bruh;
    }
}

