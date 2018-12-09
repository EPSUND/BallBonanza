package main;

import java.util.ArrayList;
import java.util.Collections;

import shapes.Ball;
import utils.MathToolbox;

public class Pathfinder {
	
	private Ball[][] ballGrid;
	
	public Pathfinder(Ball[][] ballGrid) {
		this.ballGrid = ballGrid;
	}
	
	public ArrayList<GridPos> getMoves(GridPos startPos, GridPos destPos) {
		ArrayList<GridPos> moveList = new ArrayList<GridPos>();
		
		//Get a path to the destination node
		SearchGridPos destPosWithPath = calcPath(startPos, destPos);
		
		if(destPosWithPath != null) {
			SearchGridPos searchNode = destPosWithPath;
			
			//Save the moves in the path
			while(searchNode != null) {
				moveList.add(searchNode);
				searchNode = searchNode.parent;
			}
		}
		
		System.out.println("The path:");
		for(int i = moveList.size() - 1; i >= 0; i--) {
			System.out.println(moveList.get(i));
		}
		
		return moveList;
	}
	
	private SearchGridPos calcPath(GridPos startPos, GridPos destPos) {
		ArrayList<SearchGridPos> openList = new ArrayList<SearchGridPos>();
		ArrayList<SearchGridPos> closedList = new ArrayList<SearchGridPos>();
		
		//Add the start position to the open list
		openList.add(getSearchGridPos(startPos, null, destPos));
		
		SearchGridPos activeSearchGridPos = null;
		boolean pathFound = false;
		
		while(openList.size() > 0) {
			//Sort the list
			Collections.sort(openList);
			//Remove the search node with the lowest cost
			activeSearchGridPos = openList.remove(0);
			//Add it to the closed list
			closedList.add(activeSearchGridPos);
			//Check if we have found a path to the destination node
			if(destPos.equals((GridPos)activeSearchGridPos)) {
				pathFound = true;
				break;
			}
			//Try to add the neighbors of the search node to the open list(and update the open list if nescessary)
			addValidNeighbors(activeSearchGridPos, openList, closedList, destPos);
		}
		
		if(pathFound) {
			return activeSearchGridPos;
		}
		else {
			return null;
		}
	}
	
	private void addValidNeighbors(SearchGridPos activeSGPos, 
									ArrayList<SearchGridPos> openList, 
									ArrayList<SearchGridPos> closedList,
									GridPos destPos) 
	{
		//Define the neighboors of the search node
		// x
		//xyx
		// x
		GridPos leftPos = new GridPos(activeSGPos.row, activeSGPos.column - 1);
		GridPos rightPos = new GridPos(activeSGPos.row, activeSGPos.column + 1);
		GridPos downPos = new GridPos(activeSGPos.row - 1, activeSGPos.column);
		GridPos upPos = new GridPos(activeSGPos.row + 1, activeSGPos.column);
		
		//Process all neighboors of the search node
		processGridPos(leftPos, activeSGPos, openList, closedList, destPos);
		processGridPos(rightPos, activeSGPos, openList, closedList, destPos);
		processGridPos(downPos, activeSGPos, openList, closedList, destPos);
		processGridPos(upPos, activeSGPos, openList, closedList, destPos);
	}
	
	private void processGridPos(GridPos gridPos,
			SearchGridPos activeSGPos, 
			ArrayList<SearchGridPos> openList, 
			ArrayList<SearchGridPos> closedList,
			GridPos destPos) {
		//Try to add the grid position to the open list
		if(insideBallGrid(gridPos) &&//Inside ball grid 
		   ballGrid[gridPos.row][gridPos.column] == null &&//The position in the grid is empty
		   !listContainsGridPos(gridPos, closedList)//The grid position is not on the closed list
		   ) 
		{
			//If the grid position is on the open list
			if(listContainsGridPos(gridPos, openList)) {
				SearchGridPos sgPos = getGridPosFromList(gridPos, openList);
				//If it would be shorter to get to the grid position from the active search node
				if(activeSGPos.totalPathCost + 1 < sgPos.totalPathCost) {
					//Make the active search node the parent
					sgPos.parent = activeSGPos;
					//Update the total path cost
					sgPos.totalPathCost = activeSGPos.totalPathCost + 1;
					//Update the estimated distance
					sgPos.estimatedDist = MathToolbox.getDist(sgPos, destPos);
				}
			}
			else {//The grid position is not on the open list: Add it to the list
				openList.add(getSearchGridPos(gridPos, activeSGPos, destPos));
			}
		}
	}
	
	private boolean insideBallGrid(GridPos gridPos) {
		if(gridPos.row < 0) {
			return false;
		}
		
		if(gridPos.column < 0) {
			return false;
		}
		
		if(gridPos.row >= ballGrid.length) {
			return false;
		}
		
		if(gridPos.column >= ballGrid[0].length) {//It does not matter which row we check since every row has the same amount of columns
			return false;
		}
		
		return true;
	}
	
	private SearchGridPos getGridPosFromList(GridPos gridPos, ArrayList<SearchGridPos> gridPosList) {
		for(SearchGridPos sGP : gridPosList) {
			if(gridPos.row == sGP.row && gridPos.column == sGP.column) {
				return sGP;
			}
		}
		return null;		
	}
	
	private boolean listContainsGridPos(GridPos gridPos, ArrayList<SearchGridPos> gridPosList) {
		for(SearchGridPos sGP : gridPosList) {
			if(gridPos.row == sGP.row && gridPos.column == sGP.column) {
				return true;
			}
		}
		return false;
	}
	
	
	private SearchGridPos getSearchGridPos(GridPos pos, SearchGridPos prevPos, GridPos destPos) {
		int totalPathCost = prevPos != null ? prevPos.totalPathCost + 1 : 0;//The cost to get to a new node is always 1
		int estimatedDist = MathToolbox.getDist(pos, destPos);
		
		return new SearchGridPos(pos, prevPos, totalPathCost, estimatedDist);
	}
}
