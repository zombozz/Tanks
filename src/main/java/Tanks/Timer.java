package Tanks;

import processing.core.PApplet;

public class Timer {
    private int startTime;
    private PApplet parent;
    public Timer(PApplet parent, int startTime){
        this.parent=parent;
        this.startTime=startTime;
    }
    public boolean finishedRendering(){
        if(parent.millis()-startTime > 1000){
            return true;
        } else {
            return false;
        }
    }
}
