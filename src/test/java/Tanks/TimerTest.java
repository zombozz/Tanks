package Tanks;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import processing.core.PApplet;

public class TimerTest {

    private class MockPApplet extends PApplet {
        private int mockMillis = 0;

        public void setMillis(int millis) {
            this.mockMillis = millis;
        }

        @Override
        public int millis() {
            return mockMillis;
        }
    }

    @Test
    public void testFinishedRendering_NotFinished() {
        MockPApplet mockApplet = new MockPApplet();
        mockApplet.setMillis(0);  // Simulate start time as 0

        Timer timer = new Timer(mockApplet, mockApplet.millis());

        mockApplet.setMillis(500);  // Simulate 500 milliseconds have passed

        assertFalse(timer.finishedRendering());
    }

    @Test
    public void testFinishedRendering_Finished() {
        MockPApplet mockApplet = new MockPApplet();
        mockApplet.setMillis(0);  // Simulate start time as 0

        Timer timer = new Timer(mockApplet, mockApplet.millis());

        mockApplet.setMillis(1500);  // Simulate 1500 milliseconds have passed

        assertTrue(timer.finishedRendering());
    }
}