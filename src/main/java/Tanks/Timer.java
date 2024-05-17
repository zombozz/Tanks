package Tanks;

import processing.core.PApplet;
/**
 * Allows for us to set a timer to avoid rendering issues by delaying by a set amount of time.
 *
 */
public class Timer {
    private int startTime;
    private PApplet parent;

    /**
     * Constructs a Timer object.
     * @param parent The parent PApplet.
     * @param startTime The starting time of the timer.
     */
    public Timer(PApplet parent, int startTime){
        this.parent=parent;
        this.startTime=startTime;
    }

    /**
     * Checks if the timer has finished rendering.
     * @return true if the timer has finished rendering, otherwise false.
     */
    public boolean finishedRendering(){
        if(parent.millis()-startTime > 1000){
            return true;
        } else {
            return false;
        }
    }
}
