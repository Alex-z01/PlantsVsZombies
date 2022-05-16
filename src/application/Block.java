package application;

import javafx.scene.shape.Rectangle;

public class Block {
	public enum Status { Empty, Filled };
	public enum FillType { None, Plant, Zombie };
	
	public Status status;
	public FillType type;
	public int[] position;
	public String plant, zombie;
	public Rectangle rect = new Rectangle(95,95);
	public boolean hasBullet, hasZombie;
	
	public Bullet bulletOBJ;
	public Zombie zombieOBJ;
	
	public Block() {};
	
	public Block(Status s, int[] p, Rectangle r) {
		status = s;
		position = p;
		rect = r;
	}
	
	public void OnClick(int id)
	{
		System.out.println(type + " " + zombieOBJ + " " + bulletOBJ + " block, id: " + id + " bullet: " + hasBullet + " zombie: " + hasZombie);
	}
}
