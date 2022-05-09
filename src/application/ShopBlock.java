package application;

import javafx.scene.shape.Rectangle;

public class ShopBlock {
	public int[] position;
	public Rectangle rect;
	//public Plant plant; // The corresponding plant for purchase
	
	public ShopBlock(int[] p, Rectangle r) {
		position = p;
		rect = r;
	}
}
