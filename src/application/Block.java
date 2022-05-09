package application;

import javafx.scene.shape.Rectangle;

public class Block {
	public enum Status { Empty, Filled };
	
	public Status status;
	public int[] position;
	public Rectangle rect = new Rectangle(95,95);
	
	public Block() {};
	
	public Block(Status s, int[] p, Rectangle r) {
		status = s;
		position = p;
		rect = r;
	}
	
	public void OnClick(int id)
	{
		System.out.println(status.toString() + " regular block " + id);
	}
}
