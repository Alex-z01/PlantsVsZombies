package application;

import java.util.Random;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Zombie { 
	
	public int posX, posY;
	public int value = 10;

    public Zombie() {
    	BufferedImage img = null;
        Random rand = new Random();
        posX = rand.nextInt(11,13);
        posY = rand.nextInt(5);
    }
}