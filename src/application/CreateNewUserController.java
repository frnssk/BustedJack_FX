package application;

import java.io.IOException;

import client.UserClient;
import communications.RegisterRequest;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class CreateNewUserController {
	@FXML
	private Label lblUsername;
	@FXML
	private TextField tfUsername;
	@FXML
	private Label lblPassword;
	@FXML
	private PasswordField pfPassword;
	@FXML
	private Button btnBack;
	@FXML
	private Button btnCreateUser;

	private Main mainApp;
	private UserClient client;

	@FXML
	private void handleBack() throws IOException {
		mainApp.showStartView();
	}
	
	@FXML
	private void handleCreateUser() throws IOException {
		createRegisterRequest(tfUsername.getText(), pfPassword.getText());
		mainApp.setUsername(tfUsername.getText());
	}
	
	public void checkRequest(String string) {
		if(string.equals("USERNAME_FALSE")) {
			Platform.runLater(() -> {
				try {
					mainApp.showAlert("Username taken", "The usernam is already taken. Please try again with a new username");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} else if(string.equals("PASSWORD_FALSE")) {
			Platform.runLater(() -> {
				try {
					mainApp.showAlert("Wrong password", "You have entered an incorrect password. \nPassword needs to be 6-12 characters");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} else if(string.equals("USER_TRUE")) {
			Platform.runLater(() -> {
				try {
					mainApp.showMainMenu();
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
	
	public void createRegisterRequest(String username, String passwordString) {
		char[] password = passwordString.toCharArray();
		RegisterRequest request = new RegisterRequest(username, password);
		client.sendRegisterRequest(request);
	}

	public void setMain(Main main) {
		mainApp = main;
	}

	public void setClient(UserClient client) {
		this.client = client;
	}
}
