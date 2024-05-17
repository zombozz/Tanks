package Tanks;
/**
 * Contains powerups that are activated by the player if needed.
 */
public class Powerups {
    /**
     * Repairs the specified tank if the conditions are met.
     * @param tank The tank to repair.
     */
    public void repairTank(Tank tank) {
        if (tank.playerScore>=20 && tank.playerHealth <100){
            tank.playerHealth+=20;
            tank.playerScore-=20;
        }
        if (tank.playerHealth > 100) {
            tank.playerHealth = 100; 
        }
    }

    /**
     * Adds fuel to the specified tank if the conditions are met.
     * @param tank The tank to add fuel to.
     */
    public void addFuel(Tank tank) {
        if (tank.playerScore>=10){
            tank.playerFuel+=200;
            tank.playerScore-=10;
        }
    }
}
