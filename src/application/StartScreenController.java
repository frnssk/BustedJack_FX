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

	private Main main;

	public StartScreenController() {
	}

	@FXML
	private void initialize() {
		
	}

	@FXML
	private void handleLogIn() throws Exception {
		main.showLoginView();
	}

	@FXML
	private void handleCreateNewUser() throws Exception {
		main.showCreateNewUser();
	}

	public void setMain(Main main) {
		this.main = main;
	}
}
