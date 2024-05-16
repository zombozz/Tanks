package Tanks;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import ddf.minim.*;

public class ProjectileTest {
    public PApplet mockApplet = new PApplet();
    private Minim minim = new Minim(mockApplet);

    @Test
    public void testProjectileCreation() {
        List<Float> terrainArray = new ArrayList<>();
        SoundEffects soundEffects = new SoundEffects(mockApplet, minim);
        float x = 100;
        float y = 200;
        float power = 50;
        int CELLSIZE = 10;
        float rotationAngle = 45;
        
        Projectile projectile = new Projectile(mockApplet, x, y, power, CELLSIZE, rotationAngle, terrainArray, soundEffects, 0);

        assertEquals(x, projectile.x);
        assertEquals(y, projectile.y);
        assertEquals(power, projectile.power);
        assertEquals(rotationAngle - 90, projectile.rotationAngle); // Ensure rotation angle adjustment
        assertEquals(CELLSIZE, projectile.CELLSIZE);
        assertEquals(0.0f, projectile.gravity);
        assertFalse(projectile.didItExplode);
        assertFalse(projectile.isNull);
    }

    @Test
    public void testUpdate() {
        List<Float> terrainArray = new ArrayList<>();
        SoundEffects soundEffects = new SoundEffects(mockApplet, minim);
        float x = 100;
        float y = 200;
        float power = 50;
        int CELLSIZE = 10;
        float rotationAngle = 45;
        
        Projectile projectile = new Projectile(mockApplet, x, y, power, CELLSIZE, rotationAngle, terrainArray, soundEffects, 0);

        float initialY = projectile.y;
        projectile.update();

        assertNotEquals(initialY, projectile.y); // Ensure y-coordinate changes after update
    }

    @Test
    public void testDisplay() {
        List<Float> terrainArray = new ArrayList<>();
        SoundEffects soundEffects = new SoundEffects(mockApplet, minim);
        float x = 100;
        float y = 200;
        float power = 50;
        int CELLSIZE = 10;
        float rotationAngle = 45;
        
        Projectile projectile = new Projectile(mockApplet, x, y, power, CELLSIZE, rotationAngle, terrainArray, soundEffects, 0);

        projectile.display(); // Just test if it runs without errors
        // You can add more assertions here to check if the display behavior is as expected
    }

    @Test
    public void testExplosion() {
        List<Float> terrainArray = new ArrayList<>();
        SoundEffects soundEffects = new SoundEffects(mockApplet, minim);
        float x = 100;
        float y = 200;
        float power = 50;
        int CELLSIZE = 10;
        float rotationAngle = 45;
        
        Projectile projectile = new Projectile(mockApplet, x, y, power, CELLSIZE, rotationAngle, terrainArray, soundEffects, 0);

        projectile.doExplosion();
    }
}
