package main;
import gui.BallBonanzaCanvas;

import java.util.ArrayList;
import java.util.Random;

import shapes.Ball;
import shapes.BallGenerator;


public class GameEngine {
	//Grid constants
	public static final int GRID_COLUMNS = 10;
	public static final int GRID_ROWS = 10;
	public static final int CELL_WIDTH = BallBonanza.CANVAS_GRID_WIDTH / GRID_COLUMNS;
	public static final int CELL_HEIGHT = BallBonanza.CANVAS_GRID_HEIGHT / GRID_ROWS;
	
	public static final int NEXT_BLOCK_X = CELL_WIDTH - BallBonanza.STATUS_FIELD_SPACING / 2;
	public static final int NEXT_BLOCK_Y = CELL_HEIGHT * 5 - 2;
	
	//Ball constants
	private static final int NUM_START_BALLS = 5;
	private static final int NUM_BALLS_TO_ADD = 3;
	private static final int BALL_MOVE_DELAY = 10;
	
	public static final int STATUS_FIELD_WIDTH = 3 * CELL_WIDTH;
	
	private BallBonanzaCanvas canvas;
	private Player player;
	private Ball[][] ballGrid;
	private ArrayList<Ball> nextBalls;
	private BallGenerator ballGenerator;
	private BallBonanzaScoreKeeper scoreKeeper;
	private Pathfinder pathfinder;
	private Ball movingBall;
	private ArrayList<GridPos> moveList;
	
	private int pf_width, pf_height;
	
	private volatile boolean restart;
	private boolean isGameOver, isPaused;
	private int moveBallCount;
	private boolean hasMovedBall;
	
	public GameEngine(BallBonanzaCanvas canvas)
	{
		this.canvas = canvas;
		restart = false;
		isGameOver = false;
		isPaused = false;
		movingBall = null;
		moveList = null;
		moveBallCount = 0;
		hasMovedBall = false;
		ballGrid = new Ball[GRID_ROWS][GRID_COLUMNS];
		nextBalls = new ArrayList<Ball>();
		
		//Set to null just in case
		for(int i = 0; i < GRID_ROWS; i++)
		{
			for(int j = 0; j < GRID_COLUMNS; j++)
			{
				ballGrid[i][j] = null;
			}
		}
		
		ballGenerator = new BallGenerator();	
		scoreKeeper = new BallBonanzaScoreKeeper();
		pathfinder = new Pathfinder(ballGrid);
	}
	
	/**
	 * initWorld
	 * Executes the initial creating of the world.
	 */
	public void initWorld()
	{
		if(!restart)
		{
			//Make the player
			player = new Player(ballGrid);
			
			pf_width = BallBonanza.CANVAS_GRID_WIDTH + STATUS_FIELD_WIDTH;
			pf_height = BallBonanza.CANVAS_GRID_HEIGHT;
			
			setCanvasSize(pf_width, pf_height);
			
			canvas.addMouseListeners(player.getMouseController());
		}
		else
		{
			//Reset the game over flag
			isGameOver = false;
			//Reset the paused flag
			isPaused = false;
			//Make the player ready for a new game
			player.restartGame();
			//Clear the brick grid
			for(int i = 0; i < GRID_ROWS; i++)
			{
				for(int j = 0; j < GRID_COLUMNS; j++)
				{
					ballGrid[i][j] = null;
				}
			}
			//Reset the score keeper
			scoreKeeper.reset();
		}
		
		//Generate the start balls
		generateRandomBalls(NUM_START_BALLS);
		
		movingBall = null;
		moveList = null;
		moveBallCount = 0;
		hasMovedBall = false;
	}
	
	/**
	 * updateWorld
	 * Updates the game world
	 */
	public void updateWorld() 
	{
		//Check if the ball grid is full
		if(isBallGridFull()) {
			isGameOver = true;
			//Run the score registration in a seperate thread to prevent the rendering from being paused
			new Thread()
			{
				public void run()
				{
					BallBonanza.highScoreSystem.registerScore(new Object[]{player.getScore(), player.getNumBalls(), player.getStartTime()});	
				}
			}.start();
		}
		
		//Check if the player wants a ball to be moved
		if(player.hasBallToMove()) {
			GridPos startPos = player.getStartPos();
			GridPos endPos = player.getEndPos();
			
			if(movingBall == null) {
				//Try to find a path between the start and end points
				moveList = pathfinder.getMoves(startPos, endPos);
				//If we have found a path
				if(moveList.size() > 0) {
					//Let the picked ball be the new moving ball
					movingBall = player.getPickedBall();
					//Remove the ball from the ball grid
					ballGrid[startPos.row][startPos.column] = null;
					//Remove the start position from the move list
					moveList.remove(moveList.size() - 1);
				}
			}
		}
		
		//Move the moving ball
		if(movingBall != null) {
			//Increment the move ball counter
			moveBallCount++;
			if(moveBallCount >= BALL_MOVE_DELAY) {
				//Reset the move ball count
				moveBallCount = 0;
				//Get the next move from the move list
				GridPos nextPos = moveList.remove(moveList.size() - 1);
				//Change the ball's position
				movingBall.setPosition(getCanvasXPos(nextPos.column), getCanvasYPos(nextPos.row));
				//If we have reached our destination
				if(moveList.size() == 0) {
					movingBall.addToBallGrid(ballGrid);
					movingBall = null;
					moveList = null;
					hasMovedBall = true;
				}
			}
		}		
		
		int score;
		
		//Clear score giving rows and add the score to the player
		score = scoreKeeper.calcScore(ballGrid);
		player.addScore(score);
		//Update the number of balls cleared
		player.setNumBalls(scoreKeeper.getNumBallsCleared());
		
		//Generate new balls if the player has moved a ball and not generated any points
		if(hasMovedBall && score == 0) {
			generateRandomBalls(NUM_BALLS_TO_ADD);
			hasMovedBall = false;
		}
		else if(hasMovedBall)
		{
			hasMovedBall = false;
		}
	}
	
	/**
	 * paintWorld
	 * Paints all the background scenes and objects on canvas.
	 */
	public void paintWorld() 
	{
		/* Initiate the graphics context */
		canvas.initGraphicsContext();
		
		//Paint the background
		canvas.paintBackground();
		
		//Paint the status bar
		canvas.paintStatusField(player.getScore(), player.getNumBalls());
		
		//Paint the ball grid
		for(int i = 0; i < GameEngine.GRID_ROWS; i++)
		{
			for(int j = 0; j < GameEngine.GRID_COLUMNS; j++)
			{
				if(ballGrid[i][j] != null)
				{
					canvas.paint(ballGrid[i][j]);
				}
			}
		}
		
		//Paint the next balls
		for(Ball ball : nextBalls)
		{
			canvas.paint(ball);
		}

		//Paint a hightlight if the player has selected a ball
		if(player.hasPickedBall()) {
			canvas.paintSelection(player.getPickedBall());
		}
		
		//Paints any moving ball
		if(movingBall != null) {
			canvas.paint(movingBall);
		}
		
		//Paint the path of the moving ball
//		if(moveList != null) {
//			canvas.paintMoveList(moveList);
//		}
				
		//If the game is over, paint a message
		if(isGameOver) {
			canvas.paintGameOverMessage();
		}
		
		//Show the graphics buffer
		canvas.showGraphicsBuffer();
	}
	
	/**
	 * game
	 * The game main loop. Calls the updating and painting of objects
	 * every FRAME_DELAY ms.
	 */
	public void game() {
		/* Update the game as long as it is running. */
		while (canvas.isVisible() && !restart) {
			if(!isGameOver && !isPaused)
				updateWorld();	// Update the player and NPCs.
			
			paintWorld();	// Paint the objects and background scenes.
			
			try { 
				Thread.sleep(BallBonanzaCanvas.FRAME_DELAY);		// Wait a given interval.
			} catch (InterruptedException e) {
				// Ignore.
			}
		}
		
		return;
	}
	
	//Detta borde ses över
	private void setCanvasSize(int width, int height)
	{
		/*Set the canvas size*/
		canvas.setPlayingFieldWidth(width);
		canvas.setPlayingFieldHeight(height);
		canvas.setBounds(0, 0, width, height);
	}
	
	public void setRestart(boolean val) {
		restart = val;
	}
	
	public boolean getRestart() {
		return restart;
	}
	
	public void setPlayingFieldWidth(int width) {
		pf_width = width;
	}
	
	public int getPlayingFieldWidth() {
		return pf_width;
	}
	
	public void setPlayingFieldHeight(int height) {
		pf_height = height;
	}
	
	public int getPlayingFieldHeight() {
		return pf_height;
	}
	
	public void setIsPaused(boolean val)
	{
		isPaused = val;
	}
	
	public boolean getIsPaused()
	{
		return isPaused;
	}
	
	private void generateRandomBalls(int numBalls) {
		for(int i = 0; i < numBalls; i++) {
			
			Ball ball;
			
			//Get the new balls from the awaiting balls if there are any
			if(nextBalls.size() > 0)
			{
				int color = nextBalls.remove(0).getColor();
				ball = ballGenerator.generateBall(ballGrid, color);				
			}
			else
			{	
				ball = ballGenerator.generateBall(ballGrid);
			}
			
			//We can't place balls if there is no empty grid positions
			if(ball == null) {
				isGameOver = true;//The game is over if the grid is full
				//Run the score registration in a seperate thread to prevent the rendering from being paused
				new Thread()
				{
					public void run()
					{
						BallBonanza.highScoreSystem.registerScore(new Object[]{player.getScore(), player.getNumBalls(), player.getStartTime()});	
					}
				}.start();
				return;
			}
			//Add the ball to the ball grid
			ball.addToBallGrid(ballGrid);
		}
		
		//Generate the next balls
		for(int i = 0; i < NUM_BALLS_TO_ADD; i++)
		{
			nextBalls.add(ballGenerator.generateBall(i));
		}
	}
	
	private boolean isBallGridFull() {
		for(int i = 0; i < ballGrid.length; i++) {
			for(int j = 0; j < ballGrid[i].length; j++) {
				if(ballGrid[i][j] == null) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	//Static methods
	
	public static int getCanvasXPos(int column)
	{	
		return STATUS_FIELD_WIDTH + column * CELL_WIDTH;
	}
	
	public static int getGridColumn(int xPos)
	{
		if((xPos - STATUS_FIELD_WIDTH) / CELL_WIDTH < 0)
		{
			return 0;
		}
		else
		{
			return (xPos - STATUS_FIELD_WIDTH) / CELL_WIDTH;
		}
	}
	
	public static int getCanvasYPos(int row)
	{	
		return row * CELL_HEIGHT;
	}
	
	public static int getGridRow(int yPos)
	{	
		if(yPos / CELL_HEIGHT < 0)
		{
			return 0;
		}
		else
		{
			return yPos / CELL_HEIGHT;
		}
	}
}
