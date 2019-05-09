	package application;

import java.io.IOException;

import client.UserClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class JoinTableController {
	@FXML
	private Label lblEnterRoomId;
	@FXML
	private TextField tfRoomId;
	@FXML
	private Button btnGoToTable;
	@FXML
	private Button btnRandomTable;
	@FXML
	private Button btnMenu;

	private Main mainApp;
	private UserClient client;

	@FXML
	private void initialize() {
	}

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

	@FXML
	private void handleRandomTable() throws IOException {
		client.sendRandomTableRequest();
	}

	@FXML
	private void handleMenu() throws IOException {
		mainApp.showMainMenu();
	}

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

	public void setMain(Main main) {
		mainApp = main;
	}

	public void setClient(UserClient client) {
		this.client = client;
	}
}
