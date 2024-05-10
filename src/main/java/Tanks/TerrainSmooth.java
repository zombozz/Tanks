package Tanks;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import processing.core.PApplet;
import processing.core.PImage;
import java.util.*;

public class TerrainSmooth {
    private Map<Integer, Integer> heights = new HashMap<>();
    private PApplet parent;
    private int terrainColor;
    private int CELLSIZE;

    public TerrainSmooth(PApplet parent, int terrainColor, int CELLSIZE, Map<Integer, Integer> heights) {
        this.heights = heights;
        this.parent = parent;
        this.terrainColor = terrainColor;
        this.CELLSIZE = CELLSIZE;
    }

    public List<Float> smooth() {
        List<Integer> sortedHeights = new ArrayList<>();
        List<Integer> multipliedHeights = new ArrayList<>();
        List<Float> smoothedHeights = new ArrayList<>();
        List<Float> smoothedHeights2 = new ArrayList<>();

        for (Map.Entry<Integer, Integer> entry : heights.entrySet()) {
            sortedHeights.add(entry.getValue());
        }
        int x = 0;
        for (Integer y: sortedHeights) {
            for (int i = 0; i < 32; i++) {
                multipliedHeights.add(y);
            }
        }
        for (int i = 0; i <= multipliedHeights.size() - 32; i++) {
            float sum = 0;
            for (int j = i; j < i + 32; j++) {
                sum += multipliedHeights.get(j);
            }
            float average = sum / 32; // Compute average
            smoothedHeights.add(average); // Add average to the smoothed heights list
        }
        for (int k = 0; k < 32; k++) {
            smoothedHeights.add(smoothedHeights.get(smoothedHeights.size() - 1));
        }
        for (int i = 0; i <= smoothedHeights.size() - 32; i++) {
            float sum = 0;
            for (int j = i; j < i + 32; j++) {
                sum += smoothedHeights.get(j);
            }
            float average = sum / 32; // Compute average
            smoothedHeights2.add(average); // Add average to the smoothed heights list
        }
        for (int k = 0; k < 32; k++) { 
            smoothedHeights2.add(smoothedHeights2.get(smoothedHeights2.size() - 1));
        }
        return smoothedHeights2;
    }
    
}
