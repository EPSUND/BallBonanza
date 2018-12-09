package main;

public class SearchGridPos extends GridPos implements Comparable<SearchGridPos> {
	public SearchGridPos parent;
	public int totalPathCost, estimatedDist;
	
	/***
	 * Constructor for SearchGridPos
	 */
	public SearchGridPos()
	{
		super();
		parent = null;
		totalPathCost = 0;
		estimatedDist = 0;
	}
	
	/***
	 * Constructor for SearchGridPos
	 * @param gridPos The gridpos to make into a search node
	 * @param parent The parent search node
	 * @param totalPathCost The total path cost to get to the node
	 * @param estimatedDist The estimated distance to the destination
	 */
	public SearchGridPos(GridPos gridPos, SearchGridPos parent, int totalPathCost, int estimatedDist) {
		super(gridPos);
		this.parent = parent;
		this.totalPathCost = totalPathCost;
		this.estimatedDist = estimatedDist;
	}
	
	/***
	 * Copy Constructor for SearchGridPos
	 * @param otherGridPos SearchGridPos to copy
	 */
	public SearchGridPos(SearchGridPos otherSearchGridPos)
	{
		row = otherSearchGridPos.row;
		column = otherSearchGridPos.column;
		parent = otherSearchGridPos.parent;
		totalPathCost = otherSearchGridPos.totalPathCost;
		estimatedDist = otherSearchGridPos.estimatedDist;
	}
	
	public int getPathCost() {
		return totalPathCost + estimatedDist;
	}

	@Override
	public int compareTo(SearchGridPos otherSearchGridPos) {
		return getPathCost() - otherSearchGridPos.getPathCost();
	}
}
