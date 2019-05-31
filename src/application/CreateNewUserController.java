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

/**
 * Class used to control and communicate to the CreateNewUser.fxml when creating a new user
 * Updated UI and send button action to client 
 * @author Isak Eklund
 *
 */
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
	
	/**
	 * Sends given info to client to create a new user
	 * @throws IOException
	 */
	@FXML
	private void handleCreateUser() throws IOException {
		createRegisterRequest(tfUsername.getText(), pfPassword.getText());
		mainApp.setUsername(tfUsername.getText());
	}
	
	/**
	 * updates ui depending on response from server
	 * Update UI depending on if the username and password is correct
	 * @param string
	 */
	public void checkRequest(String string) {
		if(string.equals("USERNAME_FALSE")) {
			Platform.runLater(() -> {
				try {
					mainApp.showAlert("Username taken", "The usernam is already taken.\nPlease try again with a new username");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} else if(string.equals("PASSWORD_FALSE")) {
			Platform.runLater(() -> {
				try {
					mainApp.showAlert("Wrong password", "You have entered an incorrect password.\nPassword needs to be 6-12 characters");
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
	
	/**
	 * Sends a RegisterRequest to the client
	 * @param username - The entered username
	 * @param passwordString - the entered password
	 */
	public void createRegisterRequest(String username, String passwordString) {
		char[] password = passwordString.toCharArray();
		RegisterRequest request = new RegisterRequest(username, password);
		client.sendRegisterRequest(request);
	}

	/**
	 * Connects the main class
	 * @param main
	 */
	public void setMain(Main main) {
		mainApp = main;
	}

	/**
	 * Connects the clients 
	 * @param client
	 */
	public void setClient(UserClient client) {
		this.client = client;
	}
}
