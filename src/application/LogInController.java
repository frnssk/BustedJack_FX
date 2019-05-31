package application;

import java.io.IOException;

import client.UserClient;
import communications.LoginRequest;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Class used to control and communicate to the LogIn.fxml
 * Updated UI and send button action to client 
 * @author Isak Eklund
 *
 */
public class LogInController extends Dialog{
	@FXML private Label lblUserName;
	@FXML private Label lblPassword;
	@FXML private TextField tfUsername;
	@FXML private PasswordField pfPassword;
	@FXML private Button btnMenu;
	@FXML private Button btnLogIn;
	private UserClient client;
	private Main mainApp;
	
	/**
	 * Sends a logInRequest to the client
	 * @throws Exception
	 */
	@FXML
	private void handleLogIn() throws Exception {
		mainApp.setUsername(tfUsername.getText());
		createLoginRequest(tfUsername.getText(), pfPassword.getText());
	}
	
	/**
	 * Send user back to start
	 * @throws IOException
	 */
	@FXML
	private void handleBackToStart() throws IOException {
		mainApp.showStartView();
	}
	
	/**
	 * Checks response and gives a message to the user depending on response from server
	 * @param string
	 * @throws IOException
	 */
	public void checkLogIn(String string) throws IOException {
		if(string.equals("LOGIN_SUCCES")) {
			Platform.runLater(() -> {
				try {
					mainApp.showMainMenu();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		} else if(string.equals("LOGIN_FAIL")) {
			Platform.runLater(() -> {
				try {
					mainApp.showAlert("Wrong password", "You have entered an incorrect password.\nPlease try again.");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} else if(string.equals("LOGIN_NOT_EXIST")) {
			Platform.runLater(() -> {
				try {
					mainApp.showAlert("Wrong username", "The username does not exist.\nPlease try again.");
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
	 * Creates the logInRequest
	 * @param username
	 * @param passwordString
	 */
	public void createLoginRequest(String username, String passwordString) {
		char[] password = passwordString.toCharArray();
		LoginRequest request = new LoginRequest(username, password);
		client.sendLoginRequest(request);
	}
	
	/**
	 * Connects main to UI
	 * @param main
	 */
	public void setMain(Main main) {
		mainApp = main;
	}
	
	/**
	 * Connects client to UI
	 * @param client
	 */
	public void setClient(UserClient client) {
		this.client = client;
	}
}
