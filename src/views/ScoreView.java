package views;

import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * View class: ScoreView, displays latest score
 */
@SuppressWarnings("serial")
public class ScoreView extends JLabel implements Observer {

	private DecimalFormat scoreFormat = new DecimalFormat("#0.0");
	
	/**
	 * Constructor for ScoreLabel, sets alignment
	 */
	public ScoreView() {
		this.setHorizontalAlignment(SwingConstants.CENTER);
	}

	public String toString(int score) {
		return scoreFormat.format((double) score / 10);	
	}
	
	@Override
	public String toString() {
		return this.getText();
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		int score = (int) arg1;

		this.setText(this.toString(score));
		
	}

}
