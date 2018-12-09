package shapes;

import java.util.ArrayList;
import java.util.Random;

import main.GameEngine;
import main.GridPos;

public class BallGenerator {
	
	public BallGenerator() {
		
	}
	
	public Ball generateBall(int yPos)
	{
		return new Ball(GameEngine.NEXT_BLOCK_X, GameEngine.NEXT_BLOCK_Y + yPos * GameEngine.CELL_HEIGHT);
	}
	
	public Ball generateBall(Ball[][] ballGrid) {
		return generateBall(ballGrid, -1);
	}
	
	public Ball generateBall(Ball[][] ballGrid, int color) {
		ArrayList<GridPos> emptyGridPositions = new ArrayList<GridPos>();
		
		//Look for empty grid positions
		for(int i = 0; i < ballGrid.length; i++) {
			for(int j = 0; j < ballGrid[i].length; j++) {
				if(ballGrid[i][j] == null) {
					emptyGridPositions.add(new GridPos(i, j));
				}
			}
		}
		
		//Can't create a ball if there are no free grid positions left
		if(emptyGridPositions.size() == 0) {
			return null;
		}
		
		Random r = new Random();
		
		//Pick an empty position at random
		GridPos pos = emptyGridPositions.get(
				r.nextInt(emptyGridPositions.size())
				);
	
		Ball ball;
		//Make a new ball at the empty position
		if(color >= 0)
		{
			ball = new Ball(GameEngine.getCanvasXPos(pos.column),
								 GameEngine.getCanvasYPos(pos.row),
								 color);
		}
		else
		{
			ball = new Ball(GameEngine.getCanvasXPos(pos.column),
					 GameEngine.getCanvasYPos(pos.row));
		}
		
		//Return the ball
		return ball;
	}
}
