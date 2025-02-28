package Tanks;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import processing.core.PApplet;

/**
 * Represents the game over screen.
 */
public class Gameover {
    /**
     * Constructs the game over screen.
     * 
     * @param parent The PApplet parent.
     * @param tankScores The scores of the tanks.
     */
    public Gameover(PApplet parent, List<Integer> tankScores) {
        int w = App.WIDTH;
        int h = App.HEIGHT;
        int pt = 10;

        int[] playerColors = {
            parent.color(0, 0, 255),  
            parent.color(255, 0, 0), 
            parent.color(0, 255, 255),  
            parent.color(255, 255, 0), 
            parent.color(0, 255, 0),
        };

        Map<Character, Integer> tankScoresMap = new HashMap<>();
        char c = 'A';
        for (Integer score : tankScores) {
            tankScoresMap.put(c, score);
            c++;
        }

        char winner = getWinner(tankScoresMap);
        int centerWidth = w/2-40;
        parent.fill(255);
        parent.textSize(30);
        parent.text("Player " + winner + " wins!", centerWidth-40, h / 3-10);
        parent.fill(playerColors[winner-65], 60);
        parent.rect(centerWidth-30, h / 3, 180, 170);
        parent.textSize(20);
        pt += 50;
        parent.fill(255);
        parent.text("Final Scores:", centerWidth-5, h / 3+30);
        parent.textSize(16);
        List<Character> tankKeys = sortScoresDescending(tankScoresMap);
        parent.fill(255);
        for (int i = 0; i < tankKeys.size(); i++) {
            int startTime = parent.millis();
            char tank = tankKeys.get(i);
            int score = tankScoresMap.get(tank);
            String playerName = "Player " + tank;
            String scoreText = playerName + ": " + score;
            int delay = 7000; 
            displayScoreWithDelay(parent, scoreText, centerWidth, h / 2 - 100 + pt, delay, i, startTime);
            pt += 30;
        }

        parent.text("Press 'r' to restart", centerWidth-5, h * 5 / 6);
    }

    /**
     * Gets the winner based on the tank scores.
     * 
     * @param tankScoresMap The map containing tank scores.
     * @return The character representing the winner.
     */
    public char getWinner(Map<Character, Integer> tankScoresMap) {
        char winner = 'A';
        int maxScore = Integer.MIN_VALUE;
        for (Map.Entry<Character, Integer> entry : tankScoresMap.entrySet()) {
            if (entry.getValue() > maxScore) {
                maxScore = entry.getValue();
                winner = entry.getKey();
            }
        }
        return winner;
    }

    /**
     * Sorts tank scores in descending order.
     * 
     * @param tankScoresMap The map containing tank scores.
     * @return The list of tank keys sorted in descending order of scores.
     */
    public List<Character> sortScoresDescending(Map<Character, Integer> tankScoresMap) {
        List<Character> tankKeys = new ArrayList<>(tankScoresMap.keySet());
        tankKeys.sort((t1, t2) -> tankScoresMap.get(t2) - tankScoresMap.get(t1));
        return tankKeys;
    }

    /**
     * Displays score with a delay.
     * 
     * @param parent The PApplet parent.
     * @param scoreText The text to display.
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param delay The delay in milliseconds.
     * @param index The index of the score.
     * @param startTime The start time of the game over screen.
     */
    public void displayScoreWithDelay(PApplet parent, String scoreText, int x, int y, int delay, int index, int startTime) {
        parent.fill(255); // Set text color to white
        // if (startTime > delay * (index + 1)) {
            parent.text(scoreText, x, y);
        // }
    }
}
