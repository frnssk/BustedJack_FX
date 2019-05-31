package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Class used to control and communicate to the StartScreen.fxml
 * Updated UI and send button action to client 
 * @author Isak Eklund
 * @author Christoffer Palvin
 */
public class StartScreenController {
	@FXML private Button btnLogIn;
	@FXML private Button btnCreateNewUser;
	private Main mainApp;

	/**
	 * Send user to log in view
	 * @throws Exception
	 */
	@FXML
	private void handleLogIn() throws Exception {
		mainApp.showLoginView();
	}

	/**
	 * Send user to Create New User view
	 * @throws Exception
	 */
	@FXML
	private void handleCreateNewUser() throws Exception {
		mainApp.showCreateNewUser();
	}

	/**
	 * Connects main to UI
	 * @param main
	 */
	public void setMain(Main main) {
		this.mainApp = main;
	}
}
