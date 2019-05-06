package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class StartScreenController {

	@FXML
	private Button btnLogIn;

	@FXML
	private Button btnCreateNewUser;

	private Main mainApp;

	public StartScreenController() {
	}

	@FXML
	private void initialize() {
		
	}

	@FXML
	private void handleLogIn() throws Exception {
		mainApp.showLoginView();
	}

	@FXML
	private void handleCreateNewUser() throws Exception {
		mainApp.showCreateNewUser();
	}

	public void setMain(Main main) {
		this.mainApp = main;
	}
}
