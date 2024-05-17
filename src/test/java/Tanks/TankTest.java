package Tanks;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import processing.core.PApplet;
import java.util.*;

public class TankTest {

    private PApplet mockApplet = new PApplet();
    private GUI mockGUI = new GUI(mockApplet);

    @Test
    public void testTankInitialization() {
        // Test tank initialization
        Tank tank = new Tank(mockApplet, 'A', new int[]{255, 0, 0}, 0, 0, 32, new ArrayList<>(), mockGUI);

        // Check if tank is initialized correctly
        assertNotNull(tank);
        assertEquals('A', tank.getC());
        assertEquals(255, tank.getColor()[0]);
        assertEquals(0, tank.getColor()[1]);
        assertEquals(0, tank.getColor()[2]);
        assertEquals(0, tank.tankX);
        assertEquals(0, tank.tankY);
        assertEquals(0, tank.getHealth());
    }


    @Test
    public void testTankShoot() {
        // Test tank shooting
        Tank tank = new Tank(mockApplet, 'A', new int[]{255, 0, 0}, 0, 0, 32, new ArrayList<>(), mockGUI);

        // Ensure no projectiles initially
        assertEquals(0, tank.projectiles.size());

        tank.shootTurret();
        // Check if a projectile is created after shooting
        assertEquals(1, tank.projectiles.size());
    }

    @Test
    public void testTankTakeDamage() {
        // Test tank taking damage
        Tank tank = new Tank(mockApplet, 'A', new int[]{255, 0, 0}, 0, 0, 32, new ArrayList<>(), mockGUI);

        int initialHealth = tank.getHealth();
        tank.takeDamage(10);

        // Check if tank health decreases after taking damage
        assertEquals(initialHealth - 10, tank.getHealth());
    }

    @Test
    public void testTankHeal() {
        // Test tank healing
        Tank tank = new Tank(mockApplet, 'A', new int[]{255, 0, 0}, 0, 0, 32, new ArrayList<>(), mockGUI);

        tank.takeDamage(10);
        int initialHealth = tank.getHealth();
        tank.heal(5);

        // Check if tank health increases after healing
        assertEquals(initialHealth + 5, tank.getHealth());
    }
}