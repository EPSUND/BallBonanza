package hscore;

/**
 * A high score system that allows users to record high score entries and view the high score list
 * @author Erik
 *
 */
public interface HighScoreSystem {
	/**
	 * Shows the high score list
	 */
	public void showHighScoreList();
	
	/**
	 * Registers a high score entry
	 * @param args The components in the high score entry. The name, score, level, etc.  
	 */
	public void registerScore(Object[] args);
}
