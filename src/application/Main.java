package application;

import java.io.IOException;

import client.UserClient;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class Main extends Application {
	private static UserClient client;
	private static Stage primaryStage;
	private static AnchorPane mainLayout;

	public void start(Stage primaryStage) throws Exception {
		client = new UserClient("localhost", 1200);
        this.primaryStage = primaryStage;
        showStartView();
//		Parent root = FXMLLoader.load(getClass().getResource("StartScreen.fxml"));
//		Scene scene = new Scene(root, 1000, 600);
//				
//		primaryStage.setScene(scene);
//		primaryStage.show();
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
		
	}
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}
}


