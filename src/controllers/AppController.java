package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;

import models.Board;
import utilities.FileHandler;
import views.View;

/**
 * Controller class: AppController handles behaviour and communication between
 * Model and View so that they don't know each other
 * 
 * @author <a href="mailto:mamoru@edwinmiltenburg.nl">Edwin Miltenburg</a>
 * 
 */
public class AppController {

	private Board board;
	
	private View view;

	private boolean[] noMoreMoves;

	// CONSTANTS: direction
	private static final int EAST = 0;
	private static final int SOUTH = 1;
	private static final int WEST = 2;
	private static final int NORTH = 3;

	/**
	 * Constructor for AppController. Creates default 4x4 board game of TEN!
	 */
	public AppController() {
		this(4);
	}

	/**
	 * Constructor for AppController. Creates custom size board game of TEN!
	 * 
	 * @param size
	 *            Length of one side of the board
	 */
	public AppController(int size) {
		noMoreMoves = new boolean[4];

		board = new Board(size);
		// Board(int) handles parameter validation, thus view gets correct size
		view = new View(board.getLength());

		// When Model is updated, update View too
		board.addObserver(view);
		board.addScoreObserver(view.getScoreView());

		// Link View Actions to Model
		handleActions();

		// Load new game
		board.reset();

		// Show GUI
		view.create();
	}

	/**
	 * Create the Action links between view and model
	 */
	private void handleActions() {
		final JButton[] buttons = view.getButtons();

		// KEYS
		// Press the respective direction button
		KeyAdapter keyHandler = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_LEFT)
					buttons[WEST].doClick();
				else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
					buttons[EAST].doClick();
				else if (e.getKeyCode() == KeyEvent.VK_UP)
					buttons[NORTH].doClick();
				else if (e.getKeyCode() == KeyEvent.VK_DOWN)
					buttons[SOUTH].doClick();
			}
		};

		// BUTTONS
		for (int b = 0; b < 4; b++) {
			final int direction = b;

			buttons[direction].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent actionEvent) {
					move(direction);
				}
			});

			// Buttons will have focus and should listen for key presses
			buttons[direction].addKeyListener(keyHandler);
		}

		// MENU
		view.getMenuItemLoad().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				board.set(FileHandler.boardLoadFromFile());
				resetNoMoreMoves();
			}
		});
		view.getMenuItemStore().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				FileHandler.boardStoreAsFile(board.get());
			}
		});

	}

	/**
	 * Move all lines on the board in the specified direction
	 * 
	 * @param direction
	 *            Which way to move the values on the TEN! board
	 */
	private void move(int direction) {
		// Skip if already known that this direction has no more moves
		if (noMoreMoves[direction]) {
			return;
		}

		if (board.move(direction)) {
			// Board changed, resetNoMoreMoves (and buttons), check if game won
			resetNoMoreMoves();
			gameWon();
		} else {
			// Board has not changed, disable button, check if game over
			noMoreMoves[direction] = true;
			view.getButtons()[direction].setEnabled(false);
			gameOver();
		}
	}

	/**
	 * Checks change after moving in all directions and if none invoke game over
	 */
	private void gameOver() {
		for (int direction = 0; direction < 4; direction++) {
			if (!noMoreMoves[direction]) {
				return;
			}
		}
		view.displayGameOver(board.getScore());
		reset();
	}

	/**
	 * Checks if score is at least 10 and ends the game, congratulating player
	 */
	private void gameWon() {
		if (board.getScore() > 99) {
			view.displayGameWon();
			reset();
		}
	}

	/**
	 * Resets noMoreMoves list and buttons
	 */
	private void resetNoMoreMoves() {
		noMoreMoves = new boolean[4];

		JButton[] buttons = view.getButtons();
		for (int direction = 0; direction < 4; direction++) {
			buttons[direction].setEnabled(true);
		}
	}

	/**
	 * Resets and starts new game
	 */
	private void reset() {
		resetNoMoreMoves();
		board.reset();
	}

}