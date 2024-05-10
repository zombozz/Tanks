package Tanks;

import processing.core.PApplet;
import static processing.core.PApplet.cos;
import static processing.core.PApplet.sin;

public class Projectile {
    private PApplet parent;
    private float x;
    private float y;
    private float speed;
    private float rotationAngle;

    public Projectile(PApplet parent, float x, float y, float speed, float rotationAngle) {
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.rotationAngle = rotationAngle - 90;
    }
    public void update() {
        System.out.println(rotationAngle);
        
        float angleInRadians = PApplet.radians(rotationAngle);
        System.out.println(rotationAngle);
        float vx = speed * cos(angleInRadians); // Calculate the x-component of velocity
        float vy = speed * sin(angleInRadians); // Calculate the y-component of velocity

        x += vx; // Update x position
        y += vy; // Update y position
    }

    public void display() {
        parent.fill(255, 0, 0); // Set color to red
        parent.ellipse(x, y, 10, 10); // Draw the projectile as a circle
    }

    public boolean projectileLanded() {
        return y < 0; // Check if the projectile has gone off the screen
    }
}
