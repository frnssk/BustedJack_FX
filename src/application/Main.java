package application;

import java.io.IOException;

import client.UserClient;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class Main extends Application {
	private static UserClient client;
	private static Stage primaryStage;
	private static AnchorPane mainLayout;
	private String username;

	public void start(Stage primaryStage) throws Exception {
		client = new UserClient("localhost", 1200);
        this.primaryStage = primaryStage;
        showStartView();
	}
	
	public void showStartView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("StartScreen.fxml"));
        mainLayout = loader.load();
        
        StartScreenController controller = loader.getController();
        controller.setMain(this);
        
        Scene scene = new Scene(mainLayout, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
	}
	
	public void showLoginView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("LogIn.fxml"));
        mainLayout = loader.load();
        
        LogInController controller = loader.getController();
        controller.setClient(client);
        controller.setMain(this);
        
        Scene scene = new Scene(mainLayout, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
	}
	
	public void showCreateNewUser() throws IOException {
		
	}
	
	public void showMainMenu() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("MainMenu.fxml"));
        mainLayout = loader.load();
        
        MainMenuController controller = loader.getController();
        controller.setClient(client);
        controller.setMain(this);
        
        Scene scene = new Scene(mainLayout, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
	}
	
	public void showAlert(String title, String message) throws IOException {
		Alert alert = new Alert(AlertType.INFORMATION);
        alert.initOwner(getPrimaryStage());
        alert.setTitle(title);
        alert.setHeaderText("");
        alert.setContentText(message);
        alert.showAndWait();
	}
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}

	public static void main(String[] args) {
		launch(args);
	}
}


