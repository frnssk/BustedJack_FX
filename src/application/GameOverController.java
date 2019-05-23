package application;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class GameOverController {
	private Main mainApp;
	@FXML private Label info;
	@FXML private Button btnMainMenu;
	
	@FXML
	private void initialize() {
	}
	
	@FXML
	private void handleMenu() throws IOException {
		mainApp.showMainMenu();
	}
	

	public void setMain(Main main) {
		mainApp = main;
	}
}
