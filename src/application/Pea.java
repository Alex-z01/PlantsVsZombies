package application;

import java.util.ArrayList;

import javax.imageio.ImageIO;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Pea extends Plant {
   
    public Color col;

	public Pea(int x, int y) {
        super(x, y);
        
        super.cost = 10;
        super.maxHP = 20;
        super.currentHP = super.maxHP;
        super.attackSpeed = 1;
        
        col = new Color(0.266, 0.772, 0.643, 1);
    }
    
    @Override
    public Bullet Shoot() {
    	Bullet bullet = new Bullet(super.posX, super.posY);
    	return bullet;
    }
}