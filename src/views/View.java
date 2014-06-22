package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import utilities.Direction;

/**
 * View class: The Graphical User Interface. Doesn't know about the controller
 * or the model.
 * 
 * @author <a href="mailto:mamoru@edwinmiltenburg.nl">Edwin Miltenburg</a>
 * 
 */
public class View implements Observer {

	private JFrame frame;
	private JPanel boardView;
	private JPanel controlPanel;
	private JPanel controlLeft;
	private JPanel controlCenter;
	private JPanel controlRight;

	private JLabel[][] tiles;
	private ScoreView scoreView;

	// Input components
	private JMenuItem menuItemLoad;
	private JMenuItem menuItemStore;

	/**
	 * Constructor for View. Creates the Graphical User Interface
	 * 
	 * @param boardLength
	 *            Specifies size of board to show
	 */
	public View(int boardLength) {
		frame = new JFrame("Ten!");
		frame.getContentPane().setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);

		// Create BoardView @param int boardLength
		boardView = new JPanel(new GridLayout(boardLength, boardLength));
		boardView.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

		tiles = new JLabel[boardLength][boardLength];
		for (int row = 0; row < boardLength; row++) {
			for (int column = 0; column < boardLength; column++) {
				JLabel tile = new JLabel();

				tile.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
				tile.setPreferredSize(new Dimension(100, 100));
				tile.setFont(new Font("sans-serif", Font.BOLD, 36));
				tile.setHorizontalAlignment(SwingConstants.CENTER);
				tile.setVerticalAlignment(SwingConstants.CENTER);

				boardView.add(tile);
				tiles[row][column] = tile;
			}
		}

		// Create ControlPanel
		controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 15));
		controlPanel.setBackground(new Color(198, 252, 200));

		controlLeft = new JPanel();
		controlLeft.setLayout(new BoxLayout(controlLeft, BoxLayout.Y_AXIS));
		controlLeft.setOpaque(false);

		controlCenter = new JPanel();
		controlCenter.setLayout(new BorderLayout(10, 10));
		controlCenter.setOpaque(false);

		controlRight = new JPanel();
		controlRight.setLayout(new BoxLayout(controlRight, BoxLayout.Y_AXIS));
		controlRight.setOpaque(false);

		scoreView = new ScoreView();
		
		for (Direction direction : Direction.values()) {
			direction.button(new JButton(direction.toString()));
		}

		controlLeft.add(Direction.WEST.button());
		controlCenter.add(Direction.NORTH.button(),
				BorderLayout.NORTH);
		controlCenter.add(scoreView, BorderLayout.CENTER);
		controlCenter.add(Direction.SOUTH.button(),
				BorderLayout.SOUTH);
		controlRight.add(Direction.EAST.button());

		controlPanel.add(controlLeft);
		controlPanel.add(controlCenter);
		controlPanel.add(controlRight);

		frame.getContentPane().add(boardView, BorderLayout.CENTER);
		frame.getContentPane().add(controlPanel, BorderLayout.SOUTH);

		createMenu();
	}

	/**
	 * Creates the menubar with required menus and items
	 */
	private void createMenu() {
		JMenuBar menuBar = new JMenuBar();

		JMenu menuFile = new JMenu("File");
		menuFile.setMnemonic(KeyEvent.VK_F);

		menuItemLoad = new JMenuItem("Load...");
		menuItemLoad.setMnemonic(KeyEvent.VK_L);
		menuItemLoad.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,
				ActionEvent.CTRL_MASK));

		menuItemStore = new JMenuItem("Store...");
		menuItemStore.setMnemonic(KeyEvent.VK_S);
		menuItemStore.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				ActionEvent.CTRL_MASK));

		menuBar.add(menuFile);
		menuFile.add(menuItemLoad);
		menuFile.add(menuItemStore);

		frame.setJMenuBar(menuBar);
	}

	/**
	 * Shows the GUI once it is built
	 */
	public void create() {
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Informs the player of their score and that the game is over
	 * 
	 * @param score
	 *            highestValue * 10 + secondHighestValue with maximum of 100
	 */
	public void displayGameOver(int score) {
		JOptionPane.showMessageDialog(null,
				"No more moves. Your final score is: " + scoreView,
				"Game Over!", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Inform the player that the game is over and they won, receiving a 10
	 */
	public void displayGameWon() {
		JOptionPane.showMessageDialog(null,
				"You beat the game and receive a 10!", "Congratulations!",
				JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * @return JMenuItem Load
	 */
	public JMenuItem getMenuItemLoad() {
		return menuItemLoad;
	}

	/**
	 * @return JMenuItem Store
	 */
	public JMenuItem getMenuItemStore() {
		return menuItemStore;
	}

	public Object getScoreView() {
		return scoreView;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		int[][] board = (int[][]) arg1;

		// Update board
		for (int row = 0; row < board.length; row++) {
			for (int column = 0; column < board[row].length; column++) {
				if (board[row][column] == -1) {
					tiles[row][column].setText("");
				} else {
					tiles[row][column].setText(Integer
							.toString(board[row][column]));
				}
			}
		}
	}
}