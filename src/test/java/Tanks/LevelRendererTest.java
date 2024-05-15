package Tanks;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.*;

public class LevelRendererTest {

    private PApplet mockApplet = new PApplet();
    private GUI mockGUI = new GUI(mockApplet);

    @BeforeEach
    public void setup() {
        LevelRenderer.smoothedTerrainArray = new ArrayList<>();
        LevelRenderer.tanks = new ArrayList<>();
        LevelRenderer.heights = new HashMap<>();
        LevelRenderer.finishedRendering = false;
    }

    @Test
    public void testRenderLevel() {
        // Test rendering of level elements
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

        LevelRenderer.renderLevel(mockApplet, levelLines, playerColors, terrainColor, treesImage, CELLSIZE, mockGUI);
    }

    @Test
    public void testUpdateTerrainAfterExplosion() {
        // Test updating terrain after an explosion
        LevelRenderer.smoothedTerrainArray = new ArrayList<>(Arrays.asList(5.0f, 6.0f, 7.0f, 8.0f, 9.0f));
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
        LevelRenderer.GUI = mockGUI;

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

}
