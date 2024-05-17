package Tanks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;
import processing.core.PImage;
import processing.event.KeyEvent;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

public class AppTests {

    private MockPApplet mockApplet;
    private App app;

    @BeforeEach
    public void setup() {
        mockApplet = new MockPApplet();
        app = new App();
        PApplet.runSketch(new String[]{"App"}, app);
        app.setup();
    }

    @Test
    public void testInitialSettings() {
        assertEquals(App.WIDTH, mockApplet.width);
        assertEquals(App.HEIGHT, mockApplet.height);
    }

    @Test
    public void testNextLevel() {
        int initialLevelNo = App.levelNo;
        app.nextLevel();
        assertEquals(initialLevelNo + 1, App.levelNo);
    }

    @Test
    public void testGameOver() {
        App.levelNo = 0; // Reset level number
        for (int i = 0; i < 5; i++) {
            app.nextLevel();
        }
        assertTrue(app.isGameover);
    }

    @Test
    public void testKeyPressed() {
        // Simulate pressing the right arrow key
        // Check if the selected tank moved
        Tank selectedTank = App.tanks.get(App.selectedTankIndex);
        assertTrue(selectedTank.tankX > 0);
    
        // Simulate pressing the R key (repair)
        selectedTank.setHealth(50); // Reduce health
        assertTrue(selectedTank.getHealth() > 50);
    }
    

    @Test
    public void testKeyReleased() {
        // Simulate releasing the right arrow key
        Tank selectedTank = App.tanks.get(App.selectedTankIndex);
        assertFalse(selectedTank.tankX == selectedTank.previousTankX);
    }

    private static class MockPApplet extends PApplet {
        int width;
        int height;

        @Override
        public void size(int width, int height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public PImage loadImage(String filename) {
            return new PImage();
        }
        @Test
        public void testShootTurret() {
            App app = new App();
            // Assuming that there's at least one tank in the game
            int initialProjectileCount = App.latestProjectile.length;
            // app.shootTurret();
            // Check if a projectile was added after shooting
            assertTrue(App.latestProjectile.length > initialProjectileCount);
        }
    
        @Test
        public void testRepairTank() {
            App app = new App();
            // Assuming that there's at least one tank in the game
            Tank selectedTank = App.tanks.get(App.selectedTankIndex);
            int initialHealth = selectedTank.getHealth();
            // app.repairTank();
            // Check if the tank's health increased after repair
            assertTrue(selectedTank.getHealth() > initialHealth);
        }
    
        @Test
        public void testAddFuel() {
            App app = new App();
            Tank selectedTank = App.tanks.get(App.selectedTankIndex);
            int initialFuel = selectedTank.getFuel();
            // app.addFuel();
            // Check if the tank's fuel increased after adding fuel
            assertTrue(selectedTank.getFuel() > initialFuel);
        }
    
        @Test
        public void testNextLevel() {
            App app = new App();
            int initialLevelNo = App.levelNo;
            app.nextLevel();
            // Check if the level number increased after advancing to the next level
            assertTrue(App.levelNo > initialLevelNo);
        }
        @Test
        public void testCheckTanksRemaining() {
            App app = new App();
            int initialTankCount = App.tanks.size();
            for (Tank tank : App.tanks) {
                tank.tankAlive = false; // Simulate tanks being destroyed
            }
            app.checkTanksRemaining();
            assertTrue(App.tanks.size() < initialTankCount);
        }

        @Test
        public void testNextTank() {
            App app = new App();
            int initialSelectedTankIndex = App.selectedTankIndex;
            app.nextTank();
            assertTrue(App.selectedTankIndex != initialSelectedTankIndex);
        }

        @Test
        public void testLoadLevel() {
            App app = new App();
            app.loadLevel();
            assertNotNull(App.tanks);
            assertNotNull(app.levelLines);
        }
        // @Test
        // public void testGetGUI() {    
        //     App app = new App();        
        //     assertNotNull(app.getGUI());
        // }
        @Test
        public void testSetup() {
            // Test if the setup method initializes necessary components correctly
            App app = new App();
            assertDoesNotThrow(() -> app.setup()); // Ensure the setup method runs without throwing exceptions

            // Add assertions here to verify the initialization of components
            assertNotNull(app.getGUI()); // Verify if GUI object is not null after setup
            assertNotNull(app.levelRenderer); // Verify if levelRenderer is not null after setup
            // Add more assertions as needed based on the expected behavior of the setup method
        }
        
    }
}
