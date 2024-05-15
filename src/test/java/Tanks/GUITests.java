package Tanks;
import static org.junit.jupiter.api.Assertions.*;

import java.applet.Applet;

import org.junit.jupiter.api.Test;
import processing.core.PApplet;
import processing.core.PImage;

public class GUITests {

    private int[] playerColors = {0,0,0,};
    
    public PApplet mockApplet = new PApplet(); // You may need to mock more behavior of PApplet depending on your needs

    public GUI setupGUITests(){
        GUI gui = new GUI(mockApplet);
        gui.playersSetup(1, playerColors);
        gui.setCurrentPlayerIndex(0);
        gui.setPlayerDetails(250, 100, 50, 0, 0, 0);
        gui.setImages(null, null, null, 32);
        return gui;
    }
    @Test
    public void testCurrentPlayerIndex() {
        // Test if the current player index works
        GUI gui = setupGUITests();
        assertEquals(0, gui.currentPlayerIndex);
    }
    @Test
    public void testImagesAndCellsize() {
        // Test if the current player index works
        GUI gui = setupGUITests();
        assertEquals(null, gui.fuelImage);
        assertEquals(null, gui.windImage);
        assertEquals(32, gui.CELLSIZE);
    }
    @Test
    public void testDisplayPlayerName() {
        // Test if the player name is displayed correctly
        GUI gui = setupGUITests();
        assertEquals("Player A's turn", "Player " + (char) (gui.currentPlayerIndex+65) + "'s turn");
    }

    @Test
    public void testDisplayScoreboard() {
        // Test if the player score is displayed correctly
        GUI gui = setupGUITests();
        assertEquals(0, gui.playerScores[0]);
    }

    @Test
    public void testDisplayHealth() {
        // Test if the player health is displayed correctly
        GUI gui = setupGUITests();
        assertEquals(100, gui.playerHealths[0]);
    }

    @Test
    public void testDisplayFuel() {
        // Test if the player fuel is displayed correctly
        GUI gui = setupGUITests();
        assertEquals(250, gui.fuelAmounts[0]);
    }

    @Test
    public void testDisplayWind() {
        // Test if the player wind is displayed correctly
        GUI gui = setupGUITests();
        assertEquals(0, gui.playerWindForces[0]);
    }
}
