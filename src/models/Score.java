package models;

import java.util.Observable;

/**
 * Model class: Score, belongs to Board
 * 
 * @author <a href="mailto:mamoru@edwinmiltenburg.nl">Edwin Miltenburg</a>
 * 
 */
public class Score extends Observable {

	private int score;

	/**
	 * Constructor for Score. Initial value is 0
	 */
	public Score() {
		score = 0;

		handleChange();
	}

	/**
	 * @return the score
	 */
	public int get() {
		return score;
	}

	/**
	 * Update score with highest and second highest values on TEN! board to
	 * maximum of 10.0
	 */
	public void update(int[][] board) {
		int highest = 0;
		int secondHighest = 0;
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				if (highest < board[row][col]) {
					secondHighest = highest;
					highest = board[row][col];
				} else if (secondHighest < board[row][col]) {
					secondHighest = board[row][col];
				}
			}
		}

		// No need to check secondHeighest, because only 2 merges per line are
		// allowed. Maximum score is 10.0
		if (highest > 9) {
			secondHighest = 0;
		}

		score = highest * 10 + secondHighest;

		handleChange();
	}

	/**
	 * On change, update score and notify observers of new score
	 */
	private void handleChange() {
		this.setChanged();
		this.notifyObservers((double) score / 10);
	}
}
