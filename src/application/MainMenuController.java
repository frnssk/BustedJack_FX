package application;

import java.io.IOException;

import client.UserClient;
import communications.LogOutRequest;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainMenuController {
	@FXML
	private Button btnGoToTable;
	@FXML
	private Button btnCreateTable;
	@FXML
	private Button btnProfile;
	@FXML
	private Button btnQuit;
	
	private static Main mainApp;
	private UserClient client;

	
	@FXML
	private void handleGoToTable() throws IOException {
		
	}
	
	@FXML
	private void handleCreateTable() throws IOException {
		
	}
	
	@FXML
	private void handleProfile() throws IOException {
		
	}
	
	@FXML
	private void handleQuit() throws IOException {
		LogOutRequest req = new LogOutRequest(mainApp.getUsername());
		client.sendLogOutRequest(req);
		mainApp.showStartView();
	}
	
	@FXML
	private void initialize() {
	}
	
	public static void setMain(Main main) {
		mainApp = main;
	}
	
	public void setClient(UserClient client) {
		this.client = client;
	}

}
