package utils;

import main.GridPos;

public class MathToolbox {
	public static final double epsilon = 0.0001;
	
	public static boolean isZero(double val)
	{
		return Math.abs(val) <= epsilon;
	}
	
	public static int getDist(GridPos startPos, GridPos destPos) {
		double dist = Math.sqrt(Math.pow((destPos.column - startPos.column), 2) + 
					Math.pow((destPos.row - startPos.row), 2));
		return (int)dist;
	}
}
