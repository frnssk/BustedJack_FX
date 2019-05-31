package application;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Class used to show when the game is over
 * @author Isak Eklund
 * @author Christoffer Palvin
 */
public class GameOverController {
	private Main mainApp;
	@FXML private Label info;
	@FXML private Button btnMainMenu;
	
	@FXML
	private void initialize() {
	}
	
	/**
	 * Send user back to main menu
	 * @throws IOException
	 */
	@FXML
	private void handleMenu() throws IOException {
		mainApp.showMainMenu();
	}
	
	/**
	 * Connects main to the UI
	 * @param main
	 */
	public void setMain(Main main) {
		mainApp = main;
	}
}
