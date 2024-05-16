package Tanks;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;

public class Gameover {
    public Gameover(PApplet parent, List<Integer> tankScores) {
        int w = App.WIDTH;
        int h = App.HEIGHT;
        int pt = 10;
        parent.background(0,w, h);


        parent.textSize(30);
        parent.text("Scores:", w/2, h/2);
        parent.textSize(20);
        char c = 'A';
        for (Integer score : tankScores){
            pt+=30;
            parent.text("Player " + c + ": " + score, w/2, h/2 + pt);
            c++;
        }
        parent.text("Press 'r' to restart", w/2, h*5/6);
    }
}
