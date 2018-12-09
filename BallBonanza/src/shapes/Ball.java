package shapes;

import gui.BallBonanzaCanvas;

import java.util.Random;

import main.GameEngine;

public class Ball {
public int x, y, width, height, left, right, top, bottom, color;
	
	public Ball(int x, int y) 
	{
		this.x = x;
		this.y = y;
		width = GameEngine.CELL_WIDTH;
		height = GameEngine.CELL_HEIGHT;
		this.left = x;
		this.right = x + width;
		this.bottom = y;
		this.top = y + height;
		
		//Pick a random color
		Random r = new Random();
		this.color = r.nextInt(BallBonanzaCanvas.NUM_BALL_COLORS);
	}
	
	public Ball(int x, int y, int color) 
	{
		this.x = x;
		this.y = y;
		width = GameEngine.CELL_WIDTH;
		height = GameEngine.CELL_HEIGHT;
		this.left = x;
		this.right = x + width;
		this.bottom = y;
		this.top = y + height;
		
		//Pick a random color
		Random r = new Random();
		this.color = color;
	}
	
	public boolean intersectsBounderies(int pf_width, int pf_height) {
		boolean intersectBoundery = false;
		
		/*Check if the ball is outside the canvas bounderies and move it inside if that is the case*/
		if(x < GameEngine.STATUS_FIELD_WIDTH) {
			intersectBoundery = true;
		}
		else if((x + width) > pf_width) {
			intersectBoundery = true;
		}
		if(y < 0) {
			intersectBoundery = true;
		}
		else if((y + height) > pf_height) {
			intersectBoundery = true;
		}
		
		return intersectBoundery;
	}
	
	public boolean checkForIntersection(Ball[][] ballGrid)
	{
		boolean intersectsBall = false;
		
		for(int i = 0; i < ballGrid.length; i++)
		{
			for(int j = 0; j < ballGrid[i].length; j++)
			{
				if(ballGrid[i][j] != null && checkForIntersection(ballGrid[i][j]))
				{
					intersectsBall = true;
					break;
				}
			}
		}
		
		return intersectsBall;
	}
	
	public boolean checkForIntersection(Ball ball)
	{	
		return left < ball.right && right > ball.left && 
			   bottom < ball.top && top > ball.bottom;
	}
	
	/**
	 * move
	 * Moves the ball
	 * @param xMove The amount to move the ball along x
	 * @param yMove The amount to move the ball along y
	 */
	public void move(int xMove, int yMove) {
		x += xMove;
		left += xMove;
		right += xMove;
		y += yMove;
		bottom += yMove;
		top += yMove;
	}
	
	public void xMove(int xMove)
	{
		x += xMove;
		left += xMove;
		right += xMove;
	}
	
	public void yMove(int yMove)
	{
		y += yMove;
		bottom += yMove;
		top += yMove;
	}
	
	public void setPosition(int x, int y)
	{
		this.x = x;
		this.left = x;
		this.right = x + width;
		this.y = y;
		this.bottom = y;
		this.top = y + height;
	}
	
	public void setXPos(int x)
	{
		this.x = x;
		this.left = x;
		this.right = x + width;
	}
	
	public void setYPos(int y)
	{
		this.y = y;
		this.bottom = y;
		this.top = y + height;
	}
	
	public int getColor()
	{
		return color;
	}
	
	public void addToBallGrid(Ball[][] ballGrid)
	{	
		int row = GameEngine.getGridRow(y);
		int column = GameEngine.getGridColumn(x);
		
		ballGrid[row][column] = this;
	}
}
