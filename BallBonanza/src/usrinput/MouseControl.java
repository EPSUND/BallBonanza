package usrinput;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import shapes.Ball;

import main.GameEngine;
import main.GridPos;

public class MouseControl implements MouseListener {

	private Ball[][] ballGrid;
	private Ball activeBall;
	private boolean hasPickedBall;
	private boolean ballToMove;
	private GridPos startPos;
	private GridPos endPos;
	
	public MouseControl(Ball[][] ballGrid) {
		this.ballGrid = ballGrid;
		
		activeBall = null;
		startPos = null;
		endPos = null;
		hasPickedBall = false;
		ballToMove = false;
	}
	
	public void restartGame()
	{
		activeBall = null;
		startPos = null;
		endPos = null;
		hasPickedBall = false;
		ballToMove = false;
	}
	
	public void mouseClicked(MouseEvent me) {
		//Nothing happens
	}

	public void mouseEntered(MouseEvent me) {
		//Nothing happens
	}

	public void mouseExited(MouseEvent me) {
		//Nothing happens
	}

	public void mousePressed(MouseEvent me) 
	{
		if(!hasPickedBall) {
			startPos = new GridPos(GameEngine.getGridRow(me.getY()),
								   GameEngine.getGridColumn(me.getX())); 
			activeBall = ballGrid[startPos.row][startPos.column];
			//Checked if we have picked a ball
			if(activeBall != null) {
				hasPickedBall = true;
			}
		}
		else {
			endPos = new GridPos(GameEngine.getGridRow(me.getY()),
								 GameEngine.getGridColumn(me.getX()));
			if(ballGrid[endPos.row][endPos.column] == null) {
				hasPickedBall = false;
				ballToMove = true;
			}
			else if(activeBall == ballGrid[endPos.row][endPos.column]) {//Deselect the ball if we pick it again
				hasPickedBall = false;
			}
		}
	}

	public void mouseReleased(MouseEvent me) 
	{
		//Nothing happens
	}
	
	public Ball getActiveBall() {
		return activeBall;
	}
	
	public boolean getHasPickedBall() {
		return hasPickedBall;
	}
	
	public boolean getBallToMove() {
		return ballToMove;
	}
	
	public void setBallToMove(boolean ballToMove) {
		this.ballToMove = ballToMove;
	}
	
	public GridPos getStartPos() {
		return startPos;
	}
	
	public GridPos getEndPos() {
		return endPos;
	}
}
