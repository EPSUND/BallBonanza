package main;

import java.util.ArrayList;

import shapes.Ball;

public class BallBonanzaScoreKeeper {

	private static final int NUM_BALLS_FOR_SCORE = 5;
	public static final int SCORE_5_ROW = 1;
	public static final int SCORE_6_ROW = 3;
	public static final int SCORE_7_ROW = 10;
	public static final int SCORE_8_ROW = 15;
	public static final int SCORE_9_ROW = 25;
	public static final int SCORE_DIAG_5_ROW = 2;
	public static final int SCORE_DIAG_6_ROW = 4;
	public static final int SCORE_DIAG_7_ROW = 11;
	public static final int SCORE_DIAG_8_ROW = 16;
	public static final int SCORE_DIAG_9_ROW = 26;
	public static final int BONUS_FOR_EXTRA_ROWS = 1;
	
	private int numBallsCleared;
	
	public BallBonanzaScoreKeeper()
	{
		numBallsCleared = 0;
	}
	
	public int getNumBallsCleared()
	{
		return numBallsCleared;
	}
	
	public void reset()
	{
		numBallsCleared = 0;
	}
	
	private ArrayList<ScoreRow> getScoreRows(Ball[][] ballGrid)
	{
		int sameColorLength;
		int startRow, endRow;
		int startColumn, endColumn;
		
		ArrayList<ScoreRow> scoreRows = new ArrayList<ScoreRow>();
		
		//Search for horizontal score rows
		for(int i = 0; i < ballGrid.length; i++)
		{
			//Loop through the row
			for(int j = 0; j < ballGrid[i].length; j++)
			{
				//If we have a non-null brick
				if(ballGrid[i][j] != null)
				{
					sameColorLength = 1;
					startColumn = j;
					endColumn = j;
					//Check if there are bricks of the same color next to it
					for(int k = j + 1; k < ballGrid[i].length; k++)
					{
						//If we have a brick with the same color
						if(ballGrid[i][k] != null && ballGrid[i][j].color == ballGrid[i][k].color)
						{
							//Increment length of the score row
							sameColorLength++;
							//Update the end column
							endColumn = k;
						}
						else//We have either encountered a brick with another color or a null cell
						{
							//We have reached the end of the score row
							break;
						}
					}
					
					//If the score row is long enough
					if(sameColorLength >= NUM_BALLS_FOR_SCORE)
					{
						//Add the score row
						scoreRows.add(new ScoreRow(new GridPos(i, startColumn), new GridPos(i, endColumn)));
					}
					
					//We can continue searching for score rows beyond the score row we have found
					j = endColumn;
				}
			}
		}
		
		//Search for vertical score rows
		for(int i = 0; i < ballGrid[0].length; i++)
		{
			//Loop through the column
			for(int j = 0; j < ballGrid.length; j++)
			{
				//If we have a non-null brick
				if(ballGrid[j][i] != null)
				{
					sameColorLength = 1;
					startRow = j;
					endRow = j;
					//Check if there are bricks of the same color next to it
					for(int k = j + 1; k < ballGrid.length; k++)
					{
						//If we have a brick with the same color
						if(ballGrid[k][i] != null && ballGrid[j][i].color == ballGrid[k][i].color)
						{
							//Increment length of the score row
							sameColorLength++;
							//Update the end row
							endRow = k;
						}
						else//We have either encountered a brick with another color or a null cell
						{
							//We have reached the end of the score row
							break;
						}
					}
					
					//If the score row is long enough
					if(sameColorLength >= NUM_BALLS_FOR_SCORE)
					{
						//Add the score row
						scoreRows.add(new ScoreRow(new GridPos(startRow, i), new GridPos(endRow, i)));
					}
					
					//We can continue searching for score rows beyond the score row we have found
					j = endRow;
				}
			}
		}
		
		//Search for diagonal score rows
		scoreRows.addAll(getDiagonalScoreRows(ballGrid));
		
		return scoreRows;
	}
	
	private ArrayList<ScoreRow> getDiagonalScoreRows(Ball[][] ballGrid)
	{
		ArrayList<ScoreRow> scoreRows = new ArrayList<ScoreRow>();
		
		int sameColorLength, curCol, curRow;
		GridPos startPos = new GridPos(), endPos = new GridPos();
		boolean onScoreRow;
		
		for(int i = ballGrid.length - 1; i >= 0; i--)
		{
			for(int j = 0; j < ballGrid[0].length; j++)
			{
				if(ballGrid[i][j] != null)
				{	
					//Try to make a score row that goes up-left
					//  0  1  2  3 
					//0[x][x][x][x]
					//1[x][x][x][x]
					//2[i][x][x][x]
					//3[x][i][x][x]
					//4[x][x][i][x]
					
					onScoreRow = true;
					
					//Save the start point for the score row
					startPos.row = i;
					startPos.column = j;
					
					curRow = i;
					curCol = j;
					
					sameColorLength = 1;
					
					do
					{
						//Go up-left
						curRow--;
						curCol--;
	
						//If we have found a brick of the same color
						if(curRow >= 0 && curCol >= 0 && 
						   ballGrid[curRow][curCol] != null &&
						   ballGrid[i][j].color == ballGrid[curRow][curCol].color)
						{	
							//Increment length of the score row
							sameColorLength++;
							//Update the end point of the score row
							endPos.row = curRow;
							endPos.column = curCol;
						}
						else//We are either outside the grid, has moved to a cell that is null or to a brick that have another color 
						{
							onScoreRow = false;
						}
					}
					while(onScoreRow);
					
					//If the score row is long enough
					if(sameColorLength >= NUM_BALLS_FOR_SCORE)
					{
						boolean overlapsOtherRows = false;
						
						ScoreRow newScoreRow = new ScoreRow(new GridPos(startPos), new GridPos(endPos));
						
						for(ScoreRow scoreRow : scoreRows)
						{
							if(newScoreRow.isPartOf(scoreRow))
							{
								overlapsOtherRows = true;
								break;
							}
						}
						
						//If the score row does not overlap any other score row, which would indicate that another longer score row containing this score row already exists 
						if(!overlapsOtherRows)
						{
							//Add the score row
							scoreRows.add(newScoreRow);
						}
					}
					
					//Try to make a score row that goes up-right
					//  0  1  2  3 
					//0[x][x][x][x]
					//1[x][x][x][x]
					//2[x][x][x][i]
					//3[x][x][i][x]
					//4[x][i][x][x]
					
					onScoreRow = true;
					
					//Save the start point for the score row
					startPos.row = i;
					startPos.column = j;
					
					curRow = i;
					curCol = j;
					
					sameColorLength = 1;
					
					do
					{
						//Go up-right
						curRow--;
						curCol++;
	
						//If we have found a brick of the same color
						if(curRow >= 0 && curCol < ballGrid[0].length && 
						   ballGrid[curRow][curCol] != null &&
						   ballGrid[i][j].color == ballGrid[curRow][curCol].color)
						{	
							//Increment length of the score row
							sameColorLength++;
							//Update the end point of the score row
							endPos.row = curRow;
							endPos.column = curCol;
						}
						else//We are either outside the grid, has moved to a cell that is null or to a brick that have another color 
						{
							onScoreRow = false;
						}
					}
					while(onScoreRow);
					
					//If the score row is long enough
					if(sameColorLength >= NUM_BALLS_FOR_SCORE)
					{
						boolean overlapsOtherRows = false;
						
						ScoreRow newScoreRow = new ScoreRow(new GridPos(startPos), new GridPos(endPos));
						
						for(ScoreRow scoreRow : scoreRows)
						{
							if(newScoreRow.isPartOf(scoreRow))
							{
								overlapsOtherRows = true;
								break;
							}
						}
						
						//If the score row does not overlap any other score row, which would indicate that another longer score row containing this score row already exists 
						if(!overlapsOtherRows)
						{
							//Add the score row
							scoreRows.add(newScoreRow);
						}
					}
				}
			}
		}
		
		return scoreRows;
	}
	
	public int calcScore(Ball[][] ballGrid) 
	{
		int score = 0;
		
		//Get all current score rows
		ArrayList<ScoreRow> scoreRows = getScoreRows(ballGrid);
		
		//Clear all bricks that are part of score rows from the brick grid
		for(ScoreRow row : scoreRows)
		{
			//Get the score of the score row
			score += row.clearScoreRow(ballGrid);
			//Add the bricks in score row to the brick count
			numBallsCleared += row.getNumBalls();
		}
		
		//Add a bonus score depending on the number of score rows we have cleared
		score += ((scoreRows.size() - 1) < 0 ? 0 : (scoreRows.size() - 1)) * BONUS_FOR_EXTRA_ROWS;
		
		//Return the accumulated score
		return score;
	}
}
