package Tanks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PowerupsTests {

    private Powerups powerups;
    private Tank tank;

    @BeforeEach
    public void setUp() {
        powerups = new Powerups();
        tank = new Tank(null, 'A', new int[]{255, 0, 0}, 0, 0, 32, null, null);
    }

    @Test
    public void testRepairTankEnoughScore() {
        // Tank's score is enough for repair
        tank.setScore(20);
        tank.setHealth(50);
        powerups.repairTank(tank);
        assertEquals(70, tank.getHealth());
        assertEquals(0, tank.getScore());
    }

    @Test
    public void testRepairTankNotEnoughScore() {
        // Tank's score is not enough for repair
        tank.setScore(10);
        tank.setHealth(50);
        powerups.repairTank(tank);
        assertEquals(50, tank.getHealth());
        assertEquals(10, tank.getScore());
    }

    @Test
    public void testRepairTankAlreadyFullHealth() {
        // Tank's health is already full
        tank.setScore(20);
        tank.setHealth(100);
        powerups.repairTank(tank);
        assertEquals(100, tank.getHealth());
        assertEquals(20, tank.getScore());
    }

    @Test
    public void testAddFuelEnoughScore() {
        // Tank's score is enough for fuel
        tank.setScore(10);
        tank.setFuel(100);
        powerups.addFuel(tank);
        assertEquals(300, tank.getFuel());
        assertEquals(0, tank.getScore());
    }

    @Test
    public void testAddFuelNotEnoughScore() {
        // Tank's score is not enough for fuel
        tank.setScore(5);
        tank.setFuel(100);
        powerups.addFuel(tank);
        assertEquals(100, tank.getFuel());
        assertEquals(5, tank.getScore());
    }
}
