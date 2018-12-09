package main;

/***
 * A class describing a position in a grid
 * @author Erik
 *
 */
public class GridPos {
	public int row, column;
	
	/***
	 * Constructor for GridPos
	 */
	public GridPos()
	{
		row = -1;
		column = -1;
	}
	
	/***
	 * Constructor for GridPos
	 * @param row the row
	 * @param column the column
	 */
	public GridPos(int row, int column)
	{
		this.row = row;
		this.column = column;
	}
	
	/***
	 * Copy Constructor for GridPos
	 * @param otherGridPos GridPos to copy
	 */
	public GridPos(GridPos otherGridPos)
	{
		row = otherGridPos.row;
		column = otherGridPos.column;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + column;
		result = prime * result + row;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!getClass().isAssignableFrom(obj.getClass()))
			return false;
		GridPos other = (GridPos) obj;
		if (column != other.column)
			return false;
		if (row != other.row)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "(" + row + "," + column + ")";
	}
}
