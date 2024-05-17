package Tanks;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;
import java.util.*;

public class GameoverTest {

    private MockPApplet mockApplet;

    @BeforeEach
    public void setup() {
        mockApplet = new MockPApplet();
    }

    @Test
    public void testWinner() {
        List<Integer> tankScores = Arrays.asList(10, 20, 30, 40, 50);
        Gameover gameover = new Gameover(mockApplet, tankScores);

        // Verify the winner is Player E (highest score)
        char expectedWinner = 'E';
        assertEquals(expectedWinner, gameover.getWinner(createTankScoresMap(tankScores)));
    }

    @Test
    public void testScoresSorting() {
        List<Integer> tankScores = Arrays.asList(10, 50, 30, 20, 40);
        Map<Character, Integer> tankScoresMap = createTankScoresMap(tankScores);
        Gameover gameover = new Gameover(mockApplet, tankScores);  // Create an instance to call the sortScoresDescending method
        List<Character> sortedTanks = gameover.sortScoresDescending(tankScoresMap);

        // Verify the scores are sorted correctly
        List<Character> expectedOrder = Arrays.asList('B', 'E', 'C', 'D', 'A');
        assertEquals(expectedOrder, sortedTanks);
    }

    @Test
    public void testRendering() {
        List<Integer> tankScores = Arrays.asList(10, 20, 30, 40, 50);
        Gameover gameover = new Gameover(mockApplet, tankScores);

        // Verify text rendering
        assertTrue(mockApplet.textCalls.contains("Player E wins!"));
        assertTrue(mockApplet.textCalls.contains("Final Scores:"));
        assertTrue(mockApplet.textCalls.contains("Player E: 50"));
        assertTrue(mockApplet.textCalls.contains("Player D: 40"));
        assertTrue(mockApplet.textCalls.contains("Player C: 30"));
        assertTrue(mockApplet.textCalls.contains("Player B: 20"));
        assertTrue(mockApplet.textCalls.contains("Player A: 10"));
        assertTrue(mockApplet.textCalls.contains("Press 'r' to restart"));
    }

    private Map<Character, Integer> createTankScoresMap(List<Integer> tankScores) {
        Map<Character, Integer> tankScoresMap = new HashMap<>();
        char c = 'A';
        for (Integer score : tankScores) {
            tankScoresMap.put(c, score);
            c++;
        }
        return tankScoresMap;
    }

    private static class MockPApplet extends PApplet {
        List<String> textCalls = new ArrayList<>();
        List<Integer> fillCalls = new ArrayList<>();
        List<float[]> rectCalls = new ArrayList<>();
        List<Float> textSizeCalls = new ArrayList<>();
        List<int[]> colorCalls = new ArrayList<>();
        Map<String, Integer> colorResults = new HashMap<>();

        @Override
        public void text(String str, float x, float y) {
            textCalls.add(str);
        }

        @Override
        public void fill(int rgb) {
            fillCalls.add(rgb);
        }

        @Override
        public void rect(float a, float b, float c, float d) {
            rectCalls.add(new float[]{a, b, c, d});
        }

        @Override
        public void textSize(float size) {
            textSizeCalls.add(size);
        }
    }
}
