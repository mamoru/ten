package views;

import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * View class: ScoreLabel, displays latest score
 */
@SuppressWarnings("serial")
public class ScoreLabel extends JLabel implements Observer {

	private DecimalFormat scoreFormat = new DecimalFormat("#0.0");

	/**
	 * Constructor for ScoreLabel, sets alignment
	 */
	public ScoreLabel() {
		this.setHorizontalAlignment(SwingConstants.CENTER);
	}

	@Override
	public String toString() {
		return this.getText();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		double score = (double) arg1;
		this.setText(scoreFormat.format(score));
	}

}
