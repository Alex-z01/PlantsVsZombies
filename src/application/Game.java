package application;

import java.util.ArrayList;

public class Game {
	public ArrayList<Zombie> zombies = new ArrayList<Zombie>();
	public ArrayList<Plant> plants = new ArrayList<Plant>();
	public ArrayList<Bullet> plantBullets = new ArrayList<Bullet>(); // Every bullet fired by plants
	public boolean isAlive = true; // If game is not lost
	public boolean waveComplete = true;
	public int coins = 30; // Currency
	public int wave = 1; 
}
