package application;

import java.util.ArrayList;

import javafx.scene.shape.Rectangle;

public abstract class Plant {
    
	public Block block; // The block the plant resides on
    public int cost, maxHP, currentHP, attackSpeed;
    public int posX, posY;
    
    public Plant(int x, int y) {
    	posX = x;
    	posY = y;
    }
    
    public abstract Bullet Shoot();
    
}