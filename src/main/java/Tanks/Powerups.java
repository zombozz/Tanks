package Tanks;

public class Powerups {
    public void repairTank(Tank tank) {
        if (tank.playerScore>=20 && tank.playerHealth <100){
            tank.playerHealth+=20;
            tank.playerScore-=20;
        }
        if (tank.playerHealth > 100) {
            tank.playerHealth = 100; 
        }
    }
    public void addFuel(Tank tank) {
        if (tank.playerScore>=10){
            tank.playerFuel+=200;
            tank.playerScore-=10;
        }
    }
}
