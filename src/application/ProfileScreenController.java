package application;

import java.io.IOException;

import client.UserClient;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

public class ProfileScreenController {

	@FXML
	private TableView<?> TableViewAchievements;
	@FXML
	private TextArea TextAreaAchievements;
	@FXML
	private Label lblTitle;
	@FXML
	private Label lblRating;
	@FXML
	private Button btnMenu;

	private Main mainApp;

	@FXML
	private void handleMenu() throws IOException {
		mainApp.showMainMenu();
	}
	
	public void setMain(Main main) {
		mainApp = main;
	}
}