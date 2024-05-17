package Tanks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TerrainSmoothTest {

    private TerrainSmooth terrainSmooth;
    private PApplet mockApplet;

    @BeforeEach
    public void setUp() {
        mockApplet = new PApplet();
    }

    @Test
    public void testSmooth() {
        // Create a sample map of heights
        Map<Integer, Integer> heights = new HashMap<>();
        heights.put(0, 10);
        heights.put(1, 20);
        heights.put(2, 30);
        heights.put(3, 40);

        // Initialize TerrainSmooth object
        terrainSmooth = new TerrainSmooth(mockApplet, 0xFFFFFF, 32, heights);

        // Smooth the terrain
        List<Float> smoothedHeights = terrainSmooth.smooth();

        // Expected smoothed heights
        List<Float> expectedSmoothedHeights = new ArrayList<>();
        expectedSmoothedHeights.add(20.0f);
        expectedSmoothedHeights.add(20.0f);
        expectedSmoothedHeights.add(20.0f);
        expectedSmoothedHeights.add(20.0f);

        // Check if the smoothed heights match the expected values
        assertEquals(expectedSmoothedHeights, smoothedHeights);
    }
}
