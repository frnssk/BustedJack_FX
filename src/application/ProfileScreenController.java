package application;

import client.UserClient;
import javafx.fxml.FXML;
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

	private static Main mainApp;

	private UserClient client;


	
	
	public static void setMain(Main main) {
		mainApp = main;
	}

	public void setClient(UserClient client) {
		this.client = client;
	}
}