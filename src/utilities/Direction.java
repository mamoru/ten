package utilities;

import javax.swing.JButton;

/**
 * Common Constants for direction and directional buttons
 */
public enum Direction {
	EAST, SOUTH, WEST, NORTH;

	private JButton button;

	public JButton button() {
		return button;
	}

	public void button(JButton btn) {
		button = btn;
	}

}
