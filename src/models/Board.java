package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import utilities.Direction;

/**
 * Model class: Board, a matrix of numbers that can be manipulated according to
 * the rules of TEN! Doesn't know about the Controller or the View
 * 
 * @author <a href="mailto:mamoru@edwinmiltenburg.nl">Edwin Miltenburg</a>
 * 
 */
public class Board extends Observable {

	private Random rand = new Random();

	private int[][] board;
	private Score score;

	/**
	 * Constructor for Board: Create board with custom dimensions
	 * 
	 * @param size
	 *            Length of one side of the board (at least 4)
	 */
	public Board(int size) {
		size = Math.max(4, size);
		board = new int[size][size];
		score = new Score();
	}

	/**
	 * Get board size without need to get entire board
	 * 
	 * @return Length of one side of the board
	 */
	public int getLength() {
		return board.length;
	}

	/**
	 * Returns current score
	 * 
	 * @return score highestValue * 10 + secondHighestValue with maximum of 100
	 */
	public int getScore() {
		return score.get();
	}

	/**
	 * Get values on the board
	 * 
	 * @return Int[][] of values on TEN! board
	 */
	public int[][] get() {
		return board;
	}

	/**
	 * Fill board with predetermined values
	 * 
	 * @param board
	 *            Int[][] for TEN! board, needs to be same size
	 */
	public void set(int[][] board) {
		if (board != null && board.length == this.board.length) {
			this.board = board;

			handleChange();
		}
	}

	/**
	 * Fills the the board with -1 and adds 2 zeroes at random location
	 */
	public void reset() {
		// Fill the board with -1
		for (int row = 0; row < board.length; row++) {
			for (int column = 0; column < board[row].length; column++) {
				board[row][column] = -1;
			}
		}

		// Generate 2 zeroes at random 'empty' locations
		int zeroes = 0;
		while (zeroes < 2) {
			int column = rand.nextInt(board.length);
			int row = rand.nextInt(board.length);
			if (board[row][column] == -1) {
				board[row][column] = 0;
				zeroes++;
			}
		}

		handleChange();
	}

	/**
	 * Move all lines on the board in the specified direction
	 * 
	 * @param direction
	 *            Which way to move the values on the TEN! board
	 * @return Whether the board has been changed
	 */
	public boolean move(Direction direction) {
		boolean hasChanged = false;

		switch (direction) {
		case EAST:
			hasChanged = move();
			break;
		case WEST:
			mirror();
			hasChanged = move();
			mirror();
			break;
		case SOUTH:
			rotateCounterClockwise();
			hasChanged = move();
			rotateClockwise();
			break;
		case NORTH:
			rotateClockwise();
			hasChanged = move();
			rotateCounterClockwise();
			break;
		default:
			break;
		}

		if (hasChanged) {
			handleChange();
		}

		return hasChanged;
	}

	/**
	 * Move all lines on the board in a single forward direction and spawn a
	 * zero at the start of a random 'changed' line
	 * 
	 * @return Whether the board has been changed
	 */
	private boolean move() {
		// Whether the board has been changed
		boolean hasChanged = false;

		// Which lines have changed
		List<Integer> changedLines = new ArrayList<Integer>();

		// For each line on the board
		for (int line = 0; line < board.length; line++) {

			// Amount of values merged in this line, max. 2
			int merges = 0;

			// Enough rounds so that first digit can reach last
			for (int r = 0; r <= board[line].length - 2; r++) {
				// For each pair on the line
				for (int p = board[line].length - 2; p >= r; p--) {

					// Logic for merging/moving a pair according to rules of TEN
					if (board[line][p] != -1) {
						if (board[line][p + 1] == -1) {
							board[line][p + 1] = board[line][p];
							board[line][p] = -1;

							// Line changed but no merged pair!
							changedLines.add(line);

						} else if (board[line][p + 1] == board[line][p]
								&& merges < 2) {
							board[line][p + 1] = board[line][p] + 1;
							board[line][p] = -1;

							// Merged pair, hence line changed
							merges++;
							changedLines.add(line);
						}
						// else: nothing changes
					}
					// else: nothing changes
				}
			}

		}

		if (!changedLines.isEmpty()) {
			hasChanged = true;

			// Generate 1 zero at start of random 'changed' line
			int line = changedLines.get(rand.nextInt(changedLines.size()));
			board[line][0] = 0;
		}

		return hasChanged;
	}

	/**
	 * Mirror the board horizontally by reversing numbers on all lines
	 */
	private void mirror() {
		for (int line = 0; line < board.length; line++) {
			for (int i = 0; i < board[line].length / 2; i++) {
				int temp = board[line][i];
				board[line][i] = board[line][board[line].length - 1 - i];
				board[line][board[line].length - 1 - i] = temp;
			}
		}
	}

	/**
	 * Rotate the board clockwise
	 */
	private void rotateClockwise() {
		int n = board.length;
		for (int row = 0; row < n / 2; row++) {
			for (int column = row; column < n - row - 1; column++) {
				int temp = board[row][column];
				board[row][column] = board[n - column - 1][row];
				board[n - column - 1][row] = board[n - row - 1][n - column - 1];
				board[n - row - 1][n - column - 1] = board[column][n - row - 1];
				board[column][n - row - 1] = temp;
			}
		}
	}

	/**
	 * Rotate the board counterclockwise
	 */
	private void rotateCounterClockwise() {
		int n = board.length;
		for (int row = 0; row < n / 2; row++) {
			for (int column = row; column < n - row - 1; column++) {
				int temp = board[row][column];
				board[row][column] = board[column][n - row - 1];
				board[column][n - row - 1] = board[n - row - 1][n - column - 1];
				board[n - row - 1][n - column - 1] = board[n - column - 1][row];
				board[n - column - 1][row] = temp;
			}
		}
	}

	/**
	 * On change, update score and notify observers of board values and score
	 */
	private void handleChange() {
		score.update(board);

		this.setChanged();
		this.notifyObservers(board);
	}

	public void addScoreObserver(Object observer) {
		score.addObserver((Observer) observer);

	}
}
