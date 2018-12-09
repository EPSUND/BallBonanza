package main;
import java.util.Date;

import shapes.Ball;
import usrinput.MouseControl;

//import usrinput.KeyControl;
import gui.BallBonanzaCanvas;


public class Player {

	private int score;
	private int numBalls;
	private MouseControl mouseController;
	private Date startTime;
	
	public Player(Ball[][] ballGrid)
	{	
		score = 0;
		numBalls = 0;
		startTime = new Date();

		mouseController = new MouseControl(ballGrid);
	}
	
	public void restartGame()
	{
		score = 0;
		numBalls = 0;
		startTime = new Date();
		mouseController.restartGame();
	}
	
	public void addScore(int newScore)
	{
		score += newScore;
	}
	
	public int getScore() 
	{
		return score;
	}
	
	public MouseControl getMouseController() {
		return mouseController;
	}
	
	public void setNumBalls(int numBalls)
	{
		this.numBalls = numBalls;
	}
	
	public int getNumBalls()
	{
		return numBalls;
	}
	
	public Date getStartTime()
	{
		return startTime;
	}
	
	public boolean hasBallToMove() {
		//Get if the player has indicated that a ball should be moved
		boolean hasBallToMove = mouseController.getBallToMove();
		//Reset the has "ball to move flag"
		mouseController.setBallToMove(false);
		
		return hasBallToMove;
	}
	
	public GridPos getStartPos() {
		return mouseController.getStartPos();
	}
	
	public GridPos getEndPos() {
		return mouseController.getEndPos();
	}
	
	public boolean hasPickedBall() {
		return mouseController.getHasPickedBall();
	}
	
	public Ball getPickedBall() {
		return mouseController.getActiveBall();
	}
}
