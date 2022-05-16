package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.*;

import javafx.scene.paint.Color;

public class Walnut extends Plant{
    
    public Color col;

	public Walnut(int xSpot, int ySpot) {
        super(xSpot, ySpot);
        super.cost = 8;
        super.maxHP = 50;
        super.currentHP = super.maxHP;
        super.attackSpeed = 1;
        
        col = new Color(0.529, 0.447, 0.572, 1);
    }

	@Override
	public Bullet Shoot() {
		// TODO Auto-generated method stub
		return null;
	}
    
}