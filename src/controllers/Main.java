package controllers;

import java.awt.EventQueue;
import java.util.Locale;

import javax.swing.UIManager;

/**
 * @author <a href="mailto:mamoru@edwinmiltenburg.nl">Edwin Miltenburg</a>
 * @version 2014-06-22 16:34
 * 
 */
public class Main {
	public static void main(String[] args) {
		// Force locale for scoreFormat: American English
		Locale.setDefault(new Locale("en", "US"));

		// Try to adopt OS GUI theme if available
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}

		// Start the App in a seperate Thread because of Swing
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				@SuppressWarnings("unused")
				AppController app = new AppController();
			}
		});
	}
}
