package gui;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

//import main.Block;
//import main.Brick;
import main.BallBonanza;
import main.GameEngine;
import main.GridPos;

import shapes.Ball;
import usrinput.MouseControl;
import utils.Helpers;


public class BallBonanzaCanvas extends Canvas {
	
	private static final long serialVersionUID = 1L;
	
	public static final int FRAME_DELAY = 10;		// Update interval.
	
	//Colors
	public static final Color BACKGROUND_COLOR = Color.BLACK;
	private static final Color STATUS_TEXT_COLOR = Color.WHITE;
	private static final Color GAMEOVER_TEXT_COLOR = Color.RED;
	private static final Color SELECTION_COLOR = Color.YELLOW;
	
	//Textures
	private static final String BACKGROUND_IMAGE = "steel_texture.jpg";
	private static final String STATUS_FIELD_IMAGE = "steel_texture2.jpg";
	private static final String NEXT_BALL_BOX_IMAGE = "glass_texture.jpg";
	
	//Ball colors
	public static final int NUM_BALL_COLORS = 6;
	private static final int BLUE_BALL = 0;
	private static final int GREEN_BALL = 1;
	private static final int RED_BALL = 2;
	private static final int YELLOW_BALL = 3;
	private static final int MAGENTA_BALL = 4;
	private static final int TEAL_BALL = 5;
	
	/* Size of a single pane (Standard size of objects). */
	public final int PANE_SIZE = 30;
	
	private ImageCache imageCache;	// The image cache for the canvas.	
	
	private Graphics2D graphicsContext;
	
	public BufferStrategy strategy; // Double buffered strategy.
	
	/*Fonts*/
	private Font statusFieldFont; //The font of the text in the status field
	private Font gameOverFont; //The font to write the game over message
	
	private int pf_width, pf_height;
	
	public BallBonanzaCanvas()
	{
		// Create an image cache.
		imageCache = new ImageCache();
		
		//Make the font for the text in the status field
		statusFieldFont = new Font("Serif", Font.BOLD, 20);
		//Make the font for the text in the win/lose messages
		gameOverFont = new Font("SansSerif", Font.BOLD, 40);
		
		this.setBounds(0, 0, BallBonanza.CANVAS_GRID_WIDTH + GameEngine.STATUS_FIELD_WIDTH, BallBonanza.CANVAS_GRID_HEIGHT);
	}
	
	/**
	 * createStrategy
	 * Creates a double buffering strategy for this canvas.
	 */
	public void createStrategy() {
		this.setVisible(true);	// Show the canvas.

		/* Make sure that the canvas is visible. */
		if (this.isDisplayable()) {
			/* Create double buffering. */
			this.createBufferStrategy(2);
			this.strategy = this.getBufferStrategy();

			/* Set the focus. */
			this.requestFocus();
		} else {
			System.err.println("BallBonanza: Could not enable double buffering.");
			System.exit(1);	// Exit Columns.
		}
	}
	
	/**
	 * imageChache
	 * Gets the canvas-specific image cache.
	 * return image cache
	 */
	public ImageCache getImageCache() {
		return imageCache;
	}
	
	/**
	 * initGraphicsContext
	 * Initiates the graphics context
	 */
	public void initGraphicsContext() {
		graphicsContext = (Graphics2D) strategy.getDrawGraphics();
	}
	
	/**
	 * showGraphicsBuffer
	 * Makes the next available graphics buffer visible
	 */
	public void showGraphicsBuffer() {
		/*Dispose the graphics object.*/
		graphicsContext.dispose();
		/*Show buffer.*/
		strategy.show();
	}
	
	/**
	 * paintBackground
	 * Paints the background
	 */
	public void paintBackground() {
		/* Set up the background */
		graphicsContext.setBackground(BACKGROUND_COLOR);
		/*Clear the canvas*/
		paint(graphicsContext);
		/*Draw the background image*/
		graphicsContext.drawImage(imageCache.getImage(Helpers.getResourceURIString(this, "images/" + BACKGROUND_IMAGE)), 0, 0, getWidth(), getHeight(), this);
		
		//Draw the grid
		
		graphicsContext.setColor(Color.BLACK);
		graphicsContext.setStroke(new BasicStroke(1));
		
		for(int i = 0; i <= BallBonanza.CANVAS_GRID_WIDTH; i += GameEngine.CELL_WIDTH)
		{
			graphicsContext.drawLine(GameEngine.STATUS_FIELD_WIDTH + i, 0, GameEngine.STATUS_FIELD_WIDTH + i, getHeight());
		}
		
		for(int i = 0; i <= BallBonanza.CANVAS_GRID_HEIGHT; i += GameEngine.CELL_HEIGHT)
		{
			graphicsContext.drawLine(GameEngine.STATUS_FIELD_WIDTH, i, getWidth(), i);
		}
	}
	
	/**
	 * paintStatusField
	 * Paints the status field
	 */
	public void paintStatusField(int score, int clearedBalls) 
	{
		//Draw background texture for the status field
		graphicsContext.drawImage(imageCache.getImage(Helpers.getResourceURIString(this, "images/" + STATUS_FIELD_IMAGE)), 0, 0, GameEngine.STATUS_FIELD_WIDTH, getHeight(), this);
		//Make sure the graphics context uses the status field font
		graphicsContext.setFont(statusFieldFont);
		//Draw the number of cleared balls
		drawShadowedText(GameEngine.CELL_WIDTH - BallBonanza.STATUS_FIELD_SPACING, GameEngine.CELL_HEIGHT - 2, "Balls", statusFieldFont, STATUS_TEXT_COLOR);
		drawShadowedText(GameEngine.CELL_WIDTH, GameEngine.CELL_HEIGHT + graphicsContext.getFontMetrics().getHeight() - 2, Integer.toString(clearedBalls), statusFieldFont, STATUS_TEXT_COLOR);
		//Draw the score
		drawShadowedText(GameEngine.CELL_WIDTH - BallBonanza.STATUS_FIELD_SPACING, GameEngine.CELL_HEIGHT * 3 - 2, "Score", statusFieldFont, STATUS_TEXT_COLOR);
		drawShadowedText(GameEngine.CELL_WIDTH, GameEngine.CELL_HEIGHT * 3 + graphicsContext.getFontMetrics().getHeight() - 2, Integer.toString(score), statusFieldFont, STATUS_TEXT_COLOR);
		//Draw the box containing the next ball
		graphicsContext.setStroke(new BasicStroke(4));//Make the line thicker
		graphicsContext.setColor(Color.BLACK);
		graphicsContext.drawRect(GameEngine.NEXT_BLOCK_X - BallBonanza.STATUS_FIELD_SPACING, GameEngine.NEXT_BLOCK_Y, GameEngine.CELL_WIDTH + BallBonanza.STATUS_FIELD_SPACING * 2, GameEngine.CELL_HEIGHT * 3);
		graphicsContext.drawImage(imageCache.getImage(Helpers.getResourceURIString(this, "images/" + NEXT_BALL_BOX_IMAGE)), GameEngine.NEXT_BLOCK_X - BallBonanza.STATUS_FIELD_SPACING, GameEngine.NEXT_BLOCK_Y, GameEngine.CELL_WIDTH + BallBonanza.STATUS_FIELD_SPACING * 2, GameEngine.CELL_HEIGHT * 3, this);
		drawShadowedText(GameEngine.CELL_WIDTH - BallBonanza.STATUS_FIELD_SPACING, GameEngine.NEXT_BLOCK_Y - BallBonanza.STATUS_FIELD_SPACING, "Next", statusFieldFont, STATUS_TEXT_COLOR);
		//Draw the line separating the status field from the playing field
		Stroke oldStroke = graphicsContext.getStroke();
		graphicsContext.setStroke(new BasicStroke(2));//Make the line thicker
		graphicsContext.setColor(Color.BLACK);
		graphicsContext.drawLine(GameEngine.STATUS_FIELD_WIDTH, 0, GameEngine.STATUS_FIELD_WIDTH, pf_height);
		graphicsContext.setStroke(oldStroke);
	}
	
	/**
	 * paintGameOverMessage
	 * Paints the game over message
	 */
	public void paintGameOverMessage() {
		drawShadowedText((pf_width / 2) - 80, pf_height / 2, "GAME OVER", gameOverFont, GAMEOVER_TEXT_COLOR);
	}
	
	/**
	 * @param width the width to set
	 */
	public void setPlayingFieldWidth(int width) {
		pf_width = width;
	}

	/**
	 * @return the playing field width
	 */
	public int getPlayingFieldWidth() {
		return pf_width;
	}

	/**
	 * @param height the height to set
	 */
	public void setPlayingFieldHeight(int height) {
		pf_height = height;
	}

	/**
	 * @return the playing field height
	 */
	public int getPlayingFieldHeight() {
		return pf_height;
	}
	
	private void drawShadowedText(int x, int y, String text, Font font, Color color)
	{
		//Set the font
		graphicsContext.setFont(font);
		//Draw the shadow
		graphicsContext.setColor(Color.BLACK);
		graphicsContext.drawString(text, x + 2, y + 2);
		//Draw the text
		graphicsContext.setColor(color);
		graphicsContext.drawString(text, x, y);
	}
		
	public void paint(Ball ball)
	{
		String ballTexture = "";
		
		switch(ball.getColor())
		{
		case BLUE_BALL:
			ballTexture = "Blue.bmp";
			break;
		case GREEN_BALL:
			ballTexture = "Green.bmp";
			break;
		case RED_BALL:
			ballTexture = "Red.bmp";
			break;
		case YELLOW_BALL:
			ballTexture = "Yellow.bmp";
			break;
		case MAGENTA_BALL:
			ballTexture = "Magenta.bmp";
			break;	
		case TEAL_BALL:
			ballTexture = "Teal.bmp";
			break;
		}
		
		graphicsContext.drawImage(imageCache.getTransparentImage(Helpers.getResourceURIString(this, "images/ball/" + ballTexture)), (int)ball.x, (int)ball.y, (int)ball.width, (int)ball.height, this);
	}
	
	public void paintSelection(Ball ball) {
		Stroke oldStroke = graphicsContext.getStroke();
		graphicsContext.setStroke(new BasicStroke(2));//Make the line thicker
		graphicsContext.setColor(SELECTION_COLOR);
		graphicsContext.drawRect(ball.x, ball.y, ball.width, ball.height);
		graphicsContext.setStroke(oldStroke);
	}
	
	public void paintMoveList(ArrayList<GridPos> moveList) {
		if(moveList.size() > 1) { 
			Stroke oldStroke = graphicsContext.getStroke();
			graphicsContext.setStroke(new BasicStroke(2));//Make the line thicker
			graphicsContext.setColor(SELECTION_COLOR);
			
			int[] moveArrayX = new int[moveList.size()];
			int[] moveArrayY = new int[moveList.size()];
			
			for(int i = 0; i < moveList.size(); i++) {
				moveArrayX[i] = GameEngine.getCanvasXPos(moveList.get(i).column);
				moveArrayY[i] = GameEngine.getCanvasYPos(moveList.get(i).row);
			}
			
			graphicsContext.drawPolyline(moveArrayX, moveArrayY, moveList.size());
			graphicsContext.setStroke(oldStroke);
		}
	}
	
	public void addMouseListeners(MouseControl mouseController) {
		addMouseListener(mouseController);
	}
}
