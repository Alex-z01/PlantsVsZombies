package application;

import javafx.scene.shape.Rectangle;

public class ShopBlock {
	public int[] position;
	public Rectangle rect;
	public String plant;
	//public Plant plant; // The corresponding plant for purchase
	
	public ShopBlock(int[] pos, Rectangle r, String p) {
		position = pos;
		rect = r;
		plant = p;
	}
	
	public void OnClick(int id)
	{
		System.out.println("Shop block: " + plant);
	}
}
