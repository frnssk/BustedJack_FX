package application;

import java.io.IOException;

import client.UserClient;
import communications.LoginRequest;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LogInController {
	@FXML
	private Label lblUserName;
	@FXML
	private Label lblPassword;
	@FXML
	private TextField tfUsername;
	@FXML
	private PasswordField pfPassword;
	@FXML
	private Button btnMenu;
	@FXML
	private Button btnLogIn;
	
	private UserClient client;
	
	private static Main mainApp;
	
	private static String available = "";
	
	@FXML
	private void initialize() {
		
	}
	
	public static void checkLogIn(String string) throws IOException {
		if(string.equals("LOGIN_SUCCES")) {
			mainApp.showMainMenu();
		} else if(string.equals("LOGIN_FAIL")) {
			Alert alert = new Alert(AlertType.WARNING);
	        alert.initOwner(mainApp.getPrimaryStage());
	        alert.setTitle("Wrong password");
	        alert.setHeaderText("Incoreect password");
	        alert.setContentText("You have entered a incorrect password. Please try again.");
	        alert.showAndWait();
		}
		
	}
	
	public void createLoginRequest(String username, String passwordString) {
		char[] password = passwordString.toCharArray();
		LoginRequest request = new LoginRequest(username, password);
		client.sendLoginRequest(request);
	}
	
	@FXML
	private void handleLogIn() throws Exception {
		createLoginRequest(tfUsername.getText(), pfPassword.getText());
	}
	
	public static void setMain(Main main) {
		mainApp = main;
	}
	
	public void setClient(UserClient client) {
		this.client = client;
	}
}
