package application;
	
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.Node;


public class Main extends Application {
	private final int GRID_LENGTH = 6;
	private final int GRID_WIDTH = 9;
	private String[][] grid;
	
	private List<Block> blocks = new ArrayList<Block>();
	private List<ShopBlock> shopBlocks = new ArrayList<ShopBlock>();
	
	private Block selectedBlock;
	private ShopBlock selectedShopBlock;
	private boolean shopSelected = false;
	
	@Override	
	public void start(Stage primaryStage) {
		//Create the view
		GridPane grid = new GridPane();
		//Go through the model, using its values to initialize the view
		for(int row = 0; row < GRID_LENGTH; row++) {
			for(int col = 0; col < GRID_WIDTH; col++) {
				//My individual Nodes are going to be Rectangles
				Rectangle rect = new Rectangle(95,95);

				if(row < GRID_LENGTH-1) {
					Block block = new Block(Block.Status.Empty, new int[] {row, col}, rect);
					blocks.add(block);
				} else {
					ShopBlock shopBlock = new ShopBlock(new int[] {row, col}, rect);
					shopBlocks.add(shopBlock);
				}

				//set the background color - based on the model's values
				if (row < GRID_LENGTH-1) {
					rect.setFill(Color.GREEN);
				} else {
					rect.setFill(Color.BROWN);
				}
				//Now the Rectangle has a internal color. Need to give it a border:
				rect.setStrokeWidth(2);
				rect.setStroke(Color.BLACK);
				//Add my bit of code that is going to act as the controller.
				rect.setOnMouseClicked(e -> {
					Node n = (Node)e.getSource();     	  //get the Node living in the gridpane
					Integer r1 = grid.getRowIndex(n);     //get the node's row
					Integer c1 = grid.getColumnIndex(n);  //get the node's column
					
					int blockID = (r1 * GRID_WIDTH) + c1;
					
					if(blockID >= (GRID_LENGTH-1) * GRID_WIDTH) {
						blockID -= (GRID_LENGTH-1) * GRID_WIDTH;
						//System.out.println("Shop Block: " + blockID);
						
						if(!shopSelected) {
							selectedShopBlock = shopBlocks.get(blockID); // Set the block
							
							System.out.println("Selected shop item " + blockID);
							shopSelected = true; // Selected
							
							// Update the block values
							selectedShopBlock.position = new int[] {r1, c1};
							selectedShopBlock.rect = (Rectangle)n;
							selectedShopBlock.rect.setFill(Color.GRAY);
						} else {
							selectedShopBlock.rect.setFill(Color.BROWN); // Clean previous block
							selectedShopBlock = shopBlocks.get(blockID); // Set the block
							
							System.out.println("Selected shop item " + blockID);
							// Update the block values
							selectedShopBlock.position = new int[] {r1, c1};
							selectedShopBlock.rect = (Rectangle)n;
							selectedShopBlock.rect.setFill(Color.GRAY);
						}
					} 
					else {
						selectedBlock = blocks.get(blockID);
						
						if(shopSelected)
						{
							// If player has enough money updated selected block values
							selectedBlock.status = Block.Status.Filled;
							selectedBlock.rect.setFill(Color.BLUE);
							
							// Reset shop selection
							shopSelected = false;
							// Clean shop block
							selectedShopBlock.rect.setFill(Color.BROWN);
							selectedShopBlock = null;
						} else {
							// Regular click on ground should do nothing
						}
						
						selectedBlock.OnClick(blockID);
						
						
					}
				
				});
				grid.add(rect, col, row);
			}
		}
		
		
		for(int row = 0; row < GRID_LENGTH; row++) {
			for(int col = 0; col < GRID_WIDTH; col++) {
				//My individual Nodes are going to be Rectangles
				Rectangle rect = new Rectangle(40,40);

				//set the background color - based on the model's values
				if (row < GRID_LENGTH-1) {
					rect.setFill(Color.PURPLE);
				} else {
					rect.setFill(Color.BROWN);
				}
				//Now the Rectangle has a internal color. Need to give it a border:
				rect.setStrokeWidth(2);
				rect.setStroke(Color.BLACK);
			}
		}
		
		Scene scene = new Scene(grid);  //scene will have dimensions equal to the grid
		primaryStage.setScene(scene);
        primaryStage.show();
		
        
        
	}
	
	public static void main(String[] args) {
		launch(args);
		System.out.print("repeat");
	}
	
}
