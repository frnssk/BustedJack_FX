package application;

import java.io.IOException;

import client.UserClient;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

/**
 * Class used to control and communicate to the ProfileScreen.fxml
 * Updated UI and send button action to client 
 * @author Isak Eklund
 * @author Christoffer Palvin
 */
public class ProfileScreenController {
	@FXML private TableView<?> TableViewAchievements;
	@FXML private TextArea TextAreaAchievements;
	@FXML private Label lblTitle;
	@FXML private Label lblRating;
	@FXML private Button btnMenu;
	private Main mainApp;

	/**
	 * Send user back to main menu
	 * @throws IOException
	 */
	@FXML
	private void handleMenu() throws IOException {
		mainApp.showMainMenu();
	}
	
	/**
	 * Connects main to UI
	 * @param main
	 */
	public void setMain(Main main) {
		mainApp = main;
	}
}