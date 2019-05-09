package application;

import java.io.IOException;

import client.UserClient;
import communications.LogOutRequest;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MainMenuController {
	@FXML
	private Button btnGoToTable;
	@FXML
	private Button btnCreateTable;
	@FXML
	private Button btnProfile;
	@FXML
	private Button btnQuit;
	@FXML
	private Label lblWelcome;
	
	private Main mainApp;
	private UserClient client;

	@FXML
	private void initialize() {
	}
	
	@FXML
	private void handleJoinTabel() throws IOException {
		mainApp.showJoinTable();
	}
	
	@FXML
	private void handleCreateTable() throws IOException {
		mainApp.showCreateTabel();
	}
	
	@FXML
	private void handleProfile() throws IOException {
		mainApp.showProfile();
	}
	
	@FXML
	private void handleQuit() throws IOException {
		LogOutRequest req = new LogOutRequest(mainApp.getUsername());
		client.sendLogOutRequest(req);
		mainApp.showStartView();
	}
	
	public void setWelcome(String name) {
		lblWelcome.setText("Welcome " + name);
	}
	
	public void setMain(Main main) {
		mainApp = main;
	}
	
	public void setClient(UserClient client) {
		this.client = client;
	}

}
