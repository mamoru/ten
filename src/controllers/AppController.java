package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import models.Board;
import utilities.Direction;
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

		// KEYS
		// Press the respective direction button
		KeyAdapter keyHandler = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_LEFT)
					Direction.WEST.button().doClick();
				else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
					Direction.EAST.button().doClick();
				else if (e.getKeyCode() == KeyEvent.VK_UP)
					Direction.NORTH.button().doClick();
				else if (e.getKeyCode() == KeyEvent.VK_DOWN)
					Direction.SOUTH.button().doClick();
			}
		};

		// BUTTONS
		for (final Direction direction : Direction.values()) {
			direction.button().addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent actionEvent) {
					move(direction);
				}
			});

			// Buttons will have focus and should listen for key presses
			direction.button().addKeyListener(keyHandler);
		}

		// MENU
		view.getMenuItemLoad().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				board.set(FileHandler.boardLoadFromFile());
				resetButtons();
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
	private void move(Direction direction) {
		// Skip if already known that this direction has no more moves
		if (!direction.button().isEnabled()) {
			return;
		}

		if (board.move(direction)) {
			// Board changed, reset buttons, check if game won
			resetButtons();
			gameWon();
		} else {
			// Board has not changed, disable button, check if game over
			direction.button().setEnabled(false);
			gameOver();
		}
	}

	/**
	 * Checks if movement possible, if not invoke game over
	 */
	private void gameOver() {
		for (Direction direction : Direction.values()) {
			if (direction.button().isEnabled()) {
				return;
			}
		}
		view.displayGameOver(board.getScore());
		resetBoard();
	}

	/**
	 * Checks if score is at least 10 and ends the game, congratulating player
	 */
	private void gameWon() {
		if (board.getScore() > 99) {
			view.displayGameWon();
			resetBoard();
		}
	}

	/**
	 * Resets buttons in every direction
	 */
	private void resetButtons() {
		for (Direction direction : Direction.values()) {
			direction.button().setEnabled(true);
		}
	}

	/**
	 * Resets and starts new game
	 */
	private void resetBoard() {
		resetButtons();
		board.reset();
	}

}