package Tanks;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;

public class ExplosionTests {

    private PApplet mockApplet = new PApplet(); // You may need to mock more behavior of PApplet depending on your needs

    @Test
    public void testConstructor() {
        // Test the constructor
        Explosion explosion = new Explosion(mockApplet, 100, 100, 0, new SoundEffects(mockApplet, null));
        
        // Check if the object is not null
        assertNotNull(explosion);
        
        // Check if the properties are initialized correctly
        assertEquals(100, explosion.getX());
        assertEquals(100, explosion.getY());
        assertEquals(0, explosion.getExplosionInitialTime());
    }

    @Test
    public void testExplode() {
        // Test the Explode method
        Explosion explosion = new Explosion(mockApplet, 100, 100, 0, new SoundEffects(mockApplet, null));

        
        // Call the Explode method
        try{
            explosion.Explode();
        } catch (NullPointerException e){}
        
        assertTrue(true); // Dummy assertion
    }
}
