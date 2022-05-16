package application;
	
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.Node;


public class Main extends Application implements EventHandler<KeyEvent> {
	private Game game = new Game(); 
	
	public GridPane GridObj;
	
	private int turn = 0;
	
	private static int GRID_LENGTH = 6;
	private static int GRID_WIDTH = 12;
	private String[][] grid;
	
	private static List<Block> blocks = new ArrayList<Block>();
	private static List<ShopBlock> shopBlocks = new ArrayList<ShopBlock>();
	
	private Block selectedBlock;
	private ShopBlock selectedShopBlock;
	private boolean shopSelected = false;
	
	public static void main(String[] args) {
		// Create block array
		for(int row = 0; row < GRID_LENGTH; row++) {
			for(int col = 0; col < GRID_WIDTH; col++) {
				// Field blocks
				if(row < GRID_LENGTH-1) {
					Block block = new Block(Block.Status.Empty, new int[] {row, col}, null);
					blocks.add(block);
				// Shop blocks
				} else {
					ShopBlock shopBlock = new ShopBlock(new int[] {row, col}, null, "Coming_Soon");
					shopBlocks.add(shopBlock); 
				}
			}
		}
		
		shopBlocks.get(0).plant = "Pea";
		shopBlocks.get(1).plant = "Walnut";
		shopBlocks.get(2).plant = "Snow_Pea";
		
		for(ShopBlock block : shopBlocks) {
			System.out.println(block.plant);
		}
		
		launch(args);
	}
	
	public void GameLoop() {
		turn = 0; 
		if(game.isAlive) {
			
			SpawnZombies();
			
			// Check all zombie and bullet positions
			for(Zombie zombie : game.zombies) {
				System.out.println("Zombie posX: " + zombie.posX + " posY: " + zombie.posY);
			}
			
		}
		else {
			System.out.println("Game Over");
		}
		
	}
	
	////////////////////////////////////////
	
	public void SpawnZombies() {
		Random rand = new Random();
		int count = rand.nextInt(2,6 );
		System.out.println("Spawn " + count);
		for(int i = 0; i < count; i++) {
			Zombie zombie = new Zombie();
			game.zombies.add(zombie);
		}
		
		for(Zombie zombie : game.zombies) {
			int blockID = (zombie.posY) * GRID_WIDTH + zombie.posX - 1;
			blocks.get(blockID).zombieOBJ = zombie;
			blocks.get(blockID).hasZombie = true;
			blocks.get(blockID).zombie = "Default Zombie";
			blocks.get(blockID).status = Block.Status.Filled;
			blocks.get(blockID).type = Block.FillType.Zombie;
		}
		
		for(Block block : blocks) {
			if(block.rect != null && block.type == Block.FillType.Zombie) {
				block.rect.setFill(Color.RED);
			}
		}
	}
	
	////////////////////////////////////////
	
	public void Turn() {	
		for(Block block : blocks) {
			if(block.hasBullet && block.hasZombie)
			{
				game.coins += block.zombieOBJ.value;
				game.plantBullets.remove(block.bulletOBJ);
				game.zombies.remove(block.zombieOBJ); 
			}
		}
		
		// Clear every current zombie and bullet block
		for(Block block : blocks) {	
			if(block.type != Block.FillType.Plant) {
				block.hasBullet = false;
				block.hasZombie = false;
				block.bulletOBJ = null;
				block.zombieOBJ = null;
				block.status = Block.Status.Empty;
				block.type = Block.FillType.None;
				block.rect.setFill(Color.GREEN);
			} else {
				switch(block.plant) {
				case "Pea":
					block.rect.setFill(Color.color(0.266, 0.772, 0.643, 1));
					break;
				case "Walnut":
					block.rect.setFill(Color.color(0.529, 0.447, 0.572, 1));
					break;
				}
				
			}
		}
		
		// Shoot every plant
		if(turn == 0 || turn == 2) {
			for(Plant plant : game.plants) {
				int blockID = plant.posY * GRID_WIDTH + plant.posX - 1;
				Bullet bullet = plant.Shoot();
				blocks.get(blockID).bulletOBJ = bullet;
				blocks.get(blockID).hasBullet = true;
				game.plantBullets.add(bullet);
			}
		}
		
		// Update every bullet
		for(Bullet bullet : game.plantBullets) {
			int blockID = bullet.posY * GRID_WIDTH + bullet.posX - 1;
			blocks.get(blockID).hasBullet = false;
			blocks.get(blockID).bulletOBJ = null;
			
			// Move bullet
			if(bullet.posX + 1 > GRID_WIDTH) {
				
			} else {
				bullet.posX++;
				
				// New index
				blockID = bullet.posY * GRID_WIDTH + bullet.posX - 1;
				
				// If bullet hit nothing color new bullet block
				blocks.get(blockID).hasBullet = true;
				blocks.get(blockID).bulletOBJ = bullet;
				blocks.get(blockID).rect.setFill(Color.BLACK);	
			}
		}		
	
		// Update every zombie
		for(Zombie zombie : game.zombies) {
			// Check for potential game over
			if(zombie.posX-1 == 0) {
				System.out.println("Game Over");
				AlertBox.display();
				break;
			}
			int blockID = zombie.posY * GRID_WIDTH + zombie.posX - 1;
			blocks.get(blockID).zombie = null;
			blocks.get(blockID).hasZombie = false;
			blocks.get(blockID).zombieOBJ = null;
			blocks.get(blockID).status = Block.Status.Empty;
			blocks.get(blockID).type = Block.FillType.None; 
			
			// Move zombies
			zombie.posX--;
			// New index
			blockID = zombie.posY * GRID_WIDTH + zombie.posX - 1;
			
			blocks.get(blockID).zombie = "Default Zombie";
			blocks.get(blockID).hasZombie = true;
			blocks.get(blockID).zombieOBJ = zombie;
			blocks.get(blockID).status = Block.Status.Filled;
			blocks.get(blockID).type = Block.FillType.Zombie; // Flag new block
		}
						
		// Color all new zombie blocks
		for(Block block : blocks) {
			if(block.type == Block.FillType.Zombie && !block.hasBullet) {
				block.rect.setFill(Color.RED);
				block.rect.setStrokeWidth(2);
				block.rect.setStroke(Color.BLACK);
			}
		}
		
		for(Block block : blocks) {
			if(block.hasBullet && block.hasZombie)
			{
				game.coins += block.zombieOBJ.value;
				game.plantBullets.remove(block.bulletOBJ);
				game.zombies.remove(block.zombieOBJ); 
			}
		}
		
		turn++;
		// Next wave comes every 2 turns
		if(turn == 5) {
			GameLoop();
		}
	}
	
	//////////////////////////////////////////////
	
	@Override	
	public void start(Stage primaryStage) {
		//Create the view
		GridPane grid = new GridPane();
		StackPane stack = new StackPane();
		
		GameLoop();
		
		int counter = 0;
		int shopCounter = 0;
		//Go through the model, using its values to initialize the view
		for(int row = 0; row < GRID_LENGTH; row++) {
			for(int col = 0; col < GRID_WIDTH; col++) {
				//My individual Nodes are going to be Rectangles
				Rectangle rect = new Rectangle(95,95);
				
				// Color field blocks
				if(counter < (GRID_LENGTH-1) * GRID_WIDTH) {
					blocks.get(counter).rect = rect;
					
					if(blocks.get(counter).type == Block.FillType.Zombie) {
						blocks.get(counter).rect.setFill(Color.RED);
					} else {
						blocks.get(counter).rect.setFill(Color.GREEN);
					}
					blocks.get(counter).rect.setStrokeWidth(2);
					blocks.get(counter).rect.setStroke(Color.BLACK);
					counter++;
				}
				// Color shop blocks
				else {
					shopBlocks.get(shopCounter).rect = rect;
					shopBlocks.get(shopCounter).rect.setFill(Color.color(0.392, 0.274, 0.141));
					shopBlocks.get(shopCounter).rect.setStrokeWidth(2);
					shopBlocks.get(shopCounter).rect.setStroke(Color.BLACK);
					shopCounter++;
				}			
				
				rect.setOnMouseClicked(e -> {
					Node n = (Node)e.getSource();     	  //get the Node living in the gridpane
					Integer r1 = grid.getRowIndex(n);     //get the node's row
					Integer c1 = grid.getColumnIndex(n);  //get the node's column
					
						
					int blockID = (r1 * GRID_WIDTH) + c1; // Index of the clicked block/rect
					
					// If row clicked is any field block
					if(r1 < GRID_LENGTH-1) {
						selectedBlock = blocks.get(blockID);
						selectedBlock.OnClick(blockID);
						if(shopSelected) {
							// Act accordingly to which plant was selected
							switch(selectedShopBlock.plant) {
								case "Pea":
									Pea pea = new Pea(selectedBlock.position[1]+1, selectedBlock.position[0]);
									if(game.coins >= pea.cost && selectedBlock.status == Block.Status.Empty) {
										// Game vars
										game.plants.add(pea);
										game.coins -= pea.cost;
										
										// Update the selected block
										selectedBlock.plant = selectedShopBlock.plant;
										selectedBlock.status = Block.Status.Filled;
										selectedBlock.type = Block.FillType.Plant;
										selectedBlock.rect.setFill(pea.col);
										
										// Reset shop info 
										selectedShopBlock.rect.setFill(Color.color(0.392, 0.274, 0.141));
										selectedShopBlock = null;
										shopSelected = false;

										System.out.println(game.coins);
									} else {
										// Reset shop info 
										selectedShopBlock.rect.setFill(Color.color(0.392, 0.274, 0.141));
										selectedShopBlock = null;
										shopSelected = false;
										
										System.out.println("Not enough coins or block is occupied");
									}
									break;
									
								case "Walnut":
									Walnut walnut = new Walnut(selectedBlock.position[1], selectedBlock.position[0]);
									if(game.coins >= walnut.cost) {
										// Update the selected block
										selectedBlock.plant = selectedShopBlock.plant;
										selectedBlock.status = Block.Status.Filled;
										selectedBlock.type = Block.FillType.Plant;
										selectedBlock.rect.setFill(walnut.col);
										
										// Reset shop info 
										selectedShopBlock.rect.setFill(Color.color(0.392, 0.274, 0.141));
										selectedShopBlock = null;
										shopSelected = false;
										
										// Game vars
										game.coins -= walnut.cost;
										
										System.out.println(game.coins);
									} else {
										// Reset shop info 
										selectedShopBlock.rect.setFill(Color.color(0.392, 0.274, 0.141));
										selectedShopBlock = null;
										shopSelected = false;
										
										System.out.println("Not enough coins or block is occupied");
									}
									break;
							}
							
						} else {
							// Do nothing
							
						}
					// If shop row clicked
					} else {
						blockID -= (GRID_LENGTH-1) * GRID_WIDTH; // Account for reset indexing
						
						if(shopSelected) {
							// Reset previous shop block
							selectedShopBlock.rect.setFill(Color.color(0.392, 0.274, 0.141));
							
							// Get newly clicked shop block
							selectedShopBlock = shopBlocks.get(blockID); 
							selectedShopBlock.rect.setFill(Color.color(0.392, 0.274, 0.141, 0.6));
						} else {
							selectedShopBlock = shopBlocks.get(blockID); 
							selectedShopBlock.rect.setFill(Color.color(0.392, 0.274, 0.141, 0.6));
							shopSelected = true;
						}
						selectedShopBlock.OnClick(blockID);
					}
					
				});
				grid.add(rect, col, row);
			}
		}
		
		Scene scene = new Scene(grid);  //scene will have dimensions equal to the grid		
		scene.setOnKeyPressed(this);
		
		primaryStage.setScene(scene);
        primaryStage.show();
        
	}
	
	@Override
	public void handle(KeyEvent event) {
		String code = event.getCode().toString();
		switch(code) {
			case "S":
				try {
			        FileOutputStream fileOut =
			        new FileOutputStream("/tmp/savedata.ser");
			        ObjectOutputStream out = new ObjectOutputStream(fileOut);
			        out.writeObject(blocks);
			        out.close();
			        fileOut.close();
			        System.out.printf("Serialized data is saved in /savedata.ser");
			     } catch (IOException i) {
			        i.printStackTrace();
			     }
				break;
			case "R":
				System.out.println("Restart");
				break;
			case "SPACE":
				System.out.println("Move");
				Turn();
				break;
			case "ESCAPE":
				System.out.println("Deselected all blocks");
				if(selectedShopBlock != null)
					selectedShopBlock.rect.setFill(Color.color(0.392, 0.274, 0.141));
				selectedBlock = null;
				selectedShopBlock = null;
				shopSelected = false;
		}
	}
}
