	package application;

import java.io.IOException;

import client.UserClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Class used to control and communicate to the JoinTable.fxml
 * Updated UI and send button action to client 
 * @author Isak Eklund
 * @author Christoffer Palvin
 */
public class JoinTableController {
	@FXML private Label lblEnterRoomId;
	@FXML private TextField tfRoomId;
	@FXML private Button btnGoToTable;
	@FXML private Button btnRandomTable;
	@FXML private Button btnMenu;

	private Main mainApp;
	private UserClient client;

	/**
	 * Sends player to correct table given an entered int
	 * Shows error if no number is entered
	 * @throws IOException
	 */
	@FXML
	private void handleGoToTable() throws IOException {
		String tableId = tfRoomId.getText();
		if(tableId == null) {
			Platform.runLater(() -> {
				try {
					mainApp.showAlert("Wrong Table ID", "You did not enter a number. \nPlease enter a number and try again");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} else {
			client.checkTableId(Integer.parseInt(tableId));
		}
	}

	/**
	 * Sends a request to join a random table
	 * @throws IOException
	 */
	@FXML
	private void handleRandomTable() throws IOException {
		client.sendRandomTableRequest();
	}

	/**
	 * Send user back to main menu
	 * @throws IOException
	 */
	@FXML
	private void handleMenu() throws IOException {
		mainApp.showMainMenu();
	}

	/**
	 * Handles the response from the server. 
	 * Depending on response, an error message is showed or the user is sent to the correct table
	 * @param string
	 */
	public void checkTableId(String string) {
		if(string.equals("TABLE_FALSE")) {
			Platform.runLater(() -> {
				try {
					mainApp.showAlert("Wrong Table ID", "The table is full or the ID is incorrect.\nPlease try again");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} else if(string.equals("TABLE_TRUE")) {
			Platform.runLater(() -> {
				try {
					mainApp.showGame();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} else if(string.equals("RANDOM_TRUE")) {
			Platform.runLater(() -> {
				try {
					mainApp.showGame();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} else if(string.equals("RANDOM_FALSE")) {
			Platform.runLater(() -> {
				try {
					mainApp.showAlert("No available rooms", "No available open rooms at the moment.\nPlease try again later");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} else {
			Platform.runLater(() -> {
				try {
					mainApp.showAlert("Error", "Unknown error. Try again");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}
	}

	/**
	 * Connects main to the UI
	 * @param main
	 */
	public void setMain(Main main) {
		mainApp = main;
	}

	/**
	 * Connects client to the UI
	 * @param client
	 */
	public void setClient(UserClient client) {
		this.client = client;
	}
}
