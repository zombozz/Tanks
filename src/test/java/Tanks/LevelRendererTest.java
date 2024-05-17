package Tanks;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jogamp.opengl.util.packrect.Level;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.*;

public class LevelRendererTest {

    private PApplet mockApplet = new PApplet();
    private GUI mockGUI = new GUI(mockApplet);
    private LevelRenderer levelRenderer;

    @BeforeEach
    public void setup() {
        //setup all tests by initialising a new LevelRenderer object
        LevelRenderer.smoothedTerrainArray = new ArrayList<>();
        LevelRenderer.tanks = new ArrayList<>();
        LevelRenderer.heights = new HashMap<>();
        LevelRenderer.finishedRendering = false;
        String[] levelLines = {
            "XX   XXX",
            "X   T  X",
            "XTT T  X",
            "TTTTT  X",
            " XXXXXXX"
        };
        int[] playerColors = {0xFF0000, 0x00FF00, 0x0000FF};
        int terrainColor = 0xFFFFFF; 
        PImage treesImage = mockApplet.createImage(10, 10, PApplet.RGB);
        int CELLSIZE = 32; 
        levelRenderer = new LevelRenderer(mockApplet, levelLines, playerColors, terrainColor, treesImage, CELLSIZE, mockGUI);
    }

    @Test
    public void testRenderLevel() {
        // Test if renderlevel works
        levelRenderer.renderLevel();
    }

    @Test
    public void testUpdateTerrainAfterExplosion() {
        // Test updating terrain after an explosion
        levelRenderer.smoothedTerrainArray = new ArrayList<>(Arrays.asList(5.0f, 6.0f, 7.0f, 8.0f, 9.0f));
        int explosionX = 2;
        int explosionY = 0;
        LevelRenderer.updateTerrainAfterExplosion(explosionX, explosionY);
        // Check if terrain heights are updated correctly
        assertEquals(5.0f, LevelRenderer.smoothedTerrainArray.get(0));
        assertEquals(5.3f, LevelRenderer.smoothedTerrainArray.get(1));
        assertEquals(5.6f, LevelRenderer.smoothedTerrainArray.get(2));
        assertEquals(5.9f, LevelRenderer.smoothedTerrainArray.get(3));
        assertEquals(6.2f, LevelRenderer.smoothedTerrainArray.get(4));
    }
    @Test
    public void testRenderAllTanks() {
        // Test rendering all tanks
        Tank tank1 = new Tank(mockApplet, 'A', new int[]{255, 0, 0}, 0, 0, 32, LevelRenderer.smoothedTerrainArray, mockGUI);
        Tank tank2 = new Tank(mockApplet, 'B', new int[]{0, 255, 0}, 1, 0, 32, LevelRenderer.smoothedTerrainArray, mockGUI);
        LevelRenderer.tanks = new ArrayList<>(Arrays.asList(tank1, tank2));
        levelRenderer.GUI = mockGUI;

        LevelRenderer.renderAllTanks(mockGUI);
    }

    @Test
    public void testGetTanks() {
        // Test retrieving tanks list
        Tank tank1 = new Tank(mockApplet, 'A', new int[]{255, 0, 0}, 0, 0, 32, LevelRenderer.smoothedTerrainArray, mockGUI);
        Tank tank2 = new Tank(mockApplet, 'B', new int[]{0, 255, 0}, 1, 0, 32, LevelRenderer.smoothedTerrainArray, mockGUI);
        LevelRenderer.tanks = new ArrayList<>(Arrays.asList(tank1, tank2));

        List<Tank> tanks = LevelRenderer.getTanks();

        // Check if tanks list is returned correctly
        assertEquals(2, tanks.size());
        assertEquals('A', tanks.get(0).getC());
        assertEquals('B', tanks.get(1).getC());
    }

    @Test
    public void testGetTankScores() {
        // Test retrieving tank scores
        Tank tank1 = new Tank(mockApplet, 'A', new int[]{255, 0, 0}, 0, 0, 32, LevelRenderer.smoothedTerrainArray, mockGUI);
        tank1.setScore(10);
        Tank tank2 = new Tank(mockApplet, 'B', new int[]{0, 255, 0}, 1, 0, 32, LevelRenderer.smoothedTerrainArray, mockGUI);
        tank2.setScore(20);
        LevelRenderer.tanks = new ArrayList<>(Arrays.asList(tank1, tank2));

        List<Integer> scores = levelRenderer.getTankScores();

        // Check if scores are returned correctly
        assertEquals(2, scores.size());
        assertEquals(10, scores.get(0));
        assertEquals(20, scores.get(1));
    }

    @Test
    public void testUpdateTankScores() {
        // Test updating tank scores
        Tank tank1 = new Tank(mockApplet, 'A', new int[]{255, 0, 0}, 0, 0, 32, LevelRenderer.smoothedTerrainArray, mockGUI);
        Tank tank2 = new Tank(mockApplet, 'B', new int[]{0, 255, 0}, 1, 0, 32, LevelRenderer.smoothedTerrainArray, mockGUI);
        LevelRenderer.tanks = new ArrayList<>(Arrays.asList(tank1, tank2));
        
        List<Integer> newScores = Arrays.asList(15, 25);
        levelRenderer.updateTankScores(newScores);

        // Check if tank scores are updated correctly
        assertEquals(15, tank1.playerScore);
        assertEquals(25, tank2.playerScore);
    }

    @Test
    public void testGetProjectiles() {
        // Test retrieving the latest projectile
        Tank tank1 = new Tank(mockApplet, 'A', new int[]{255, 0, 0}, 0, 0, 32, LevelRenderer.smoothedTerrainArray, mockGUI);
        tank1.shootTurret();
        Tank tank2 = new Tank(mockApplet, 'B', new int[]{0, 255, 0}, 1, 0, 32, LevelRenderer.smoothedTerrainArray, mockGUI);
        tank2.shootTurret();
        LevelRenderer.tanks = new ArrayList<>(Arrays.asList(tank1, tank2));

        float[] latestProjectile = LevelRenderer.getProjectiles();

        // Check if the latest projectile is returned correctly
        assertEquals(4, latestProjectile[0]);
        assertEquals(5, latestProjectile[1]);
        assertEquals(6, latestProjectile[2]);
    }

    @Test
    public void testDamageToScore() {
        // Test converting damage to score
        Tank tank1 = new Tank(mockApplet, 'A', new int[]{255, 0, 0}, 0, 0, 32, LevelRenderer.smoothedTerrainArray, mockGUI);
        tank1.damageReceived = (Arrays.asList(10, 1));
        Tank tank2 = new Tank(mockApplet, 'B', new int[]{0, 255, 0}, 1, 0, 32, LevelRenderer.smoothedTerrainArray, mockGUI);
        tank2.damageReceived = (Arrays.asList(5, 0));
        levelRenderer.tanks = new ArrayList<>(Arrays.asList(tank1, tank2));

        List<Integer> damageToScore = LevelRenderer.damageToScore();

        // Check if damage is converted to score correctly
        assertEquals(15, (int) damageToScore.get(0));
        assertEquals(1, (int) damageToScore.get(1));
    }

    @Test
    public void testReset() {
        // Test resetting the level renderer
        levelRenderer.reset(true);

        // Check if all relevant fields are reset correctly
        assertFalse(levelRenderer.finishedRendering);
        assertTrue(levelRenderer.tanks.isEmpty());
        assertTrue(levelRenderer.tankIds.isEmpty());
        assertEquals(0, levelRenderer.tanksNum);
        assertFalse(levelRenderer.tankScoresTaken);
        assertTrue(levelRenderer.tankScores.isEmpty());
    }
}
