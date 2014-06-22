package utilities;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

/**
 * File handling methods for the game TEN!
 */
public abstract class FileHandler {

	// fileChooser as field to remember location
	private static JFileChooser fileChooser = new JFileChooser();
	
	/**
	 * Save TEN! board to file using JFileChooser
	 * 
	 * @param board
	 *            Int[][] of TEN! board
	 */
	public static void boardStoreAsFile(int[][] board) {
		// Generate lines to put in file
		ArrayList<String> fileLines = new ArrayList<String>();
		for (int r = 0; r < board.length; r++) {
			String fileLine = "";
			for (int c = 0; c < board[r].length; c++) {
				fileLine += Integer.toString(board[r][c]);

				if (c != board[r].length - 1) {
					// whitespace as value seperator
					fileLine += " ";
				}
			}
			fileLines.add(fileLine);
		}

		// Open Save dialog to select a target file
		int result = fileChooser.showSaveDialog(fileChooser);

		// Continue only when a file target is approved
		if (result == JFileChooser.APPROVE_OPTION) {

			// Get selected file
			File targetFile = fileChooser.getSelectedFile();
			try {
				// Make sure file is empty
				if (!targetFile.exists()) {
					targetFile.createNewFile();
				}

				// Write lines to file
				Files.write(targetFile.toPath(), fileLines,
						StandardCharsets.UTF_8);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Load TEN! board from file using JFileChooser
	 * 
	 * @return Int[][] for TEN! board
	 */
	public static int[][] boardLoadFromFile() {
		int[][] board = null;

		// Open Save dialog to select a target file
		int result = fileChooser.showOpenDialog(fileChooser);

		// Continue only when a file target is approved
		if (result == JFileChooser.APPROVE_OPTION) {
			Path path = fileChooser.getSelectedFile().toPath();

			List<String> fileLines = null;

			try {
				// Read lines from file
				fileLines = Files.readAllLines(path, StandardCharsets.UTF_8);

			} catch (IOException e) {
				e.printStackTrace();
			}

			// Make sure file has content
			if (fileLines != null) {

				// Initialise board using derived amount of rows
				board = new int[fileLines.size()][];

				// for each line in the file
				for (int row = 0; row < fileLines.size(); row++) {
					// Split line into cells
					String[] fileLine = fileLines.get(row).split(" ");

					// Initialise row using derived amount of columns
					int[] line = new int[fileLine.length];

					// for each cell in the line
					for (int cell = 0; cell < fileLine.length; cell++) {
						// Try to convert from string to integer
						try {
							line[cell] = Integer.parseInt(fileLine[cell]);
						} catch (NumberFormatException e) {
							// Debug and trace info
							line[cell] = -2;
							System.out.println("Error on row: " + (row + 1)
									+ " cell: " + (cell + 1) + " in "
									+ path.toString());
							e.printStackTrace();
						}
					}
					board[row] = line;
				}
			}
		}

		return board;
	}
}
