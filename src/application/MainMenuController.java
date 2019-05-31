package application;

import java.io.IOException;

import client.UserClient;
import communications.LogOutRequest;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Controlling communication to MainMenu.fxml
 * Used to update UI and send actions to client
 * @author Isak Eklund
 * @author Christoffer Palvin
 */
public class MainMenuController {
	@FXML private Button btnGoToTable;
	@FXML private Button btnCreateTable;
	@FXML private Button btnProfile;
	@FXML private Button btnQuit;
	@FXML private Label lblWelcome;
	
	private Main mainApp;
	private UserClient client;

	/**
	 * Used to switch scene to the Join Table view
	 * @throws IOException
	 */
	@FXML
	private void handleJoinTabel() throws IOException {
		mainApp.showJoinTable();
	}
	
	/**
	 * Used to switch scene to the Create Table view
	 * @throws IOException
	 */
	@FXML
	private void handleCreateTable() throws IOException {
		mainApp.showCreateTabel();
	}
	
	/**
	 * Used to switch scene to the Profile view
	 * @throws IOException
	 */
	@FXML
	private void handleProfile() throws IOException {
		mainApp.showProfile();
	}
	
	/**
	 * Used to switch scene to the Start view
	 * @throws IOException
	 */
	@FXML
	private void handleQuit() throws IOException {
		LogOutRequest req = new LogOutRequest(mainApp.getUsername());
		client.sendLogOutRequest(req);
		mainApp.showStartView();
	}
	
	/**
	 * Shows username when logged in
	 * @param name
	 */
	public void setWelcome(String name) {
		lblWelcome.setText("Welcome " + name);
	}
	
	/**
	 * Connects main to UI
	 * @param main
	 */
	public void setMain(Main main) {
		mainApp = main;
	}
	
	/**
	 * Connects Client to UI
	 * @param client
	 */
	public void setClient(UserClient client) {
		this.client = client;
	}

}
